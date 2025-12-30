<template>
  <div class="chat-view">
    <!-- 导航栏 -->
    <nav class="navbar">
      <div class="container">
        <router-link to="/" class="logo">
          <i class="fas fa-arrow-left"></i>
          <span>返回首页</span>
        </router-link>
        <div class="chat-header">
          <h1><i class="fas fa-robot"></i> 智能问答助手</h1>
          <p class="subtitle">24小时在线，解答你的学习疑问</p>
        </div>
        <div class="header-actions">
          <button class="action-btn" @click="clearChat" title="清空对话">
            <i class="fas fa-trash-alt"></i>
          </button>
          <button class="action-btn" @click="toggleDarkMode" title="切换主题">
            <i class="fas fa-moon"></i>
          </button>
          <button class="action-btn" @click="testConnection" title="测试连接">
            <i class="fas fa-wifi"></i>
          </button>
        </div>
      </div>
    </nav>

    <!-- 聊天主体 -->
    <div class="chat-container">
      <!-- 左侧边栏 -->
      <div class="sidebar">
        <!-- 用户信息 -->
        <div class="user-card">
          <div class="user-avatar">
            <i class="fas fa-user-graduate"></i>
          </div>
          <div class="user-info">
            <h3>张三</h3>
            <p>计算机科学 · 大三</p>
            <p class="student-id">学号: {{ studentId }}</p>
          </div>
        </div>

        <!-- 快速提问 -->
        <div class="quick-questions">
          <h3><i class="fas fa-bolt"></i> 快速提问</h3>
          <div class="question-buttons">
            <button v-for="(question, index) in quickQuestions" :key="index" class="question-btn"
                    @click="askQuickQuestion(question)" :disabled="isLoading">
              {{ question }}
            </button>
          </div>
        </div>

        <!-- 历史记录 -->
        <div class="history-section">
          <h3><i class="fas fa-history"></i> 最近对话</h3>
          <div class="history-list">
            <div v-for="(item, index) in history" :key="index" class="history-item" @click="loadHistory(item)">
              <div class="history-question">{{ truncateText(item.question, 25) }}</div>
              <div class="history-time">{{ item.time }}</div>
            </div>
          </div>
        </div>

        <!-- 连接状态 -->
        <div class="connection-status" :class="{ 'connected': isConnected }">
          <i class="fas fa-circle"></i>
          <span>{{ isConnected ? '已连接到AI服务' : 'AI服务未连接' }}</span>
        </div>
      </div>

      <!-- 聊天区域 -->
      <div class="chat-area">
        <!-- 消息列表 -->
        <div class="messages-container" ref="messagesContainer">
          <!-- 欢迎消息 -->
          <div v-if="messages.length === 0" class="welcome-message">
            <div class="welcome-icon">
              <i class="fas fa-robot"></i>
            </div>
            <h2>欢迎使用智能问答</h2>
            <p>我是您的校园学习助手，可以解答各种学习问题</p>
            <p>例如：编程学习、课程选择、考研规划、时间管理等</p>
            <div v-if="!isConnected" class="connection-alert">
              <i class="fas fa-exclamation-triangle"></i>
              <span>AI服务连接异常，当前使用模拟数据</span>
            </div>
          </div>

          <!-- 消息列表 -->
          <div v-for="(message, index) in messages" :key="index" class="message" :class="message.type">
            <div class="message-avatar">
              <i :class="message.type === 'user' ? 'fas fa-user' : 'fas fa-robot'"></i>
            </div>
            <div class="message-content">
              <div class="message-bubble">{{ message.content }}</div>
              <div class="message-time">{{ message.time }}</div>
              <div v-if="message.isMock" class="mock-badge">
                <i class="fas fa-flask"></i> 模拟数据
              </div>
            </div>
          </div>

          <!-- 加载动画 -->
          <div v-if="isLoading" class="loading-indicator">
            <div class="loading-avatar">
              <i class="fas fa-robot"></i>
            </div>
            <div class="loading-text">
              <div>正在思考...</div>
              <div class="loading-dots">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
          </div>
        </div>

        <!-- 输入区域 -->
        <div class="input-area">
          <div class="input-container">
            <textarea v-model="userInput" class="message-input" placeholder="请输入您的问题..." rows="3"
                      @keydown.enter.exact.prevent="sendMessage" :disabled="isLoading"></textarea>
            <button class="send-btn" @click="sendMessage" :disabled="!userInput.trim() || isLoading">
              <i class="fas fa-paper-plane"></i>
            </button>
          </div>
          <div class="input-tips">
            <i class="fas fa-info-circle"></i> Enter 发送，Shift + Enter 换行
            <span v-if="!isConnected" class="connection-warning">
              <i class="fas fa-exclamation-triangle"></i> 使用模拟数据
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, computed } from 'vue'
import { useRouter } from 'vue-router'
import { askQuestion, checkHealth } from '@/api/ai' // 导入 API

const router = useRouter()

// 状态管理
const userInput = ref('')
const messages = ref([])
const isLoading = ref(false)
const messagesContainer = ref(null)
const isDarkMode = ref(false)
const studentId = ref('2023001')
const isConnected = ref(true) // 连接状态

// 快速问题列表
const quickQuestions = ref([
  '如何学习Java编程？',
  'MySQL索引优化有哪些技巧？',
  '计算机专业考研应该如何准备？',
  '如何平衡学习和社团活动？',
  '大二应该选哪些专业选修课？',
  '如何提高编程能力？'
])

// 历史记录
const history = ref([
  { question: 'Java学习路线', time: '昨天 15:30' },
  { question: '数据库设计原则', time: '昨天 10:20' },
  { question: '考研时间规划', time: '前天 14:45' }
])

// Mock响应数据（作为后备）
const mockResponses = {
  '如何学习Java编程？': `【Java编程学习建议】\n\n1️⃣ 基础阶段（1-2个月）\n📚 学习内容：\n- Java基础语法\n- 面向对象编程\n- 集合框架\n- 异常处理\n- IO流操作\n\n2️⃣ 进阶阶段（2-3个月）\n🔧 技术栈：\n- Spring Boot框架\n- MyBatis/MyBatis Plus\n- Spring MVC\n- 数据库操作（MySQL）\n\n3️⃣ 项目实践\n💻 推荐项目：\n- 学生管理系统\n- 博客系统\n- 电商平台（简化版）\n\n坚持每天编码，从"Hello World"到项目实战！`,

  'MySQL索引优化有哪些技巧？': `【MySQL索引优化技巧】\n\n📊 基础概念\n- 关系型数据库原理\n- SQL语法（增删改查）\n- 数据库设计三范式\n\n🔍 MySQL优化重点\n1. 索引优化\n   - B+树索引原理\n   - 索引使用规则\n   - 查询性能优化\n\n2. 事务管理\n   - ACID特性\n   - 事务隔离级别\n   - 锁机制\n\n3. 存储引擎\n   - InnoDB vs MyISAM\n   - 存储结构\n   - 性能对比\n\n🛠️ 实践建议\n- 设计一个小型数据库\n- 学习Explain分析查询\n- 了解分库分表\n- 学习Redis缓存\n\n推荐书籍：《高性能MySQL》`
}

// 计算属性：页面标题
const pageTitle = computed(() => {
  return `智能问答 (${messages.value.length}条对话)`
})

// 测试连接
const testConnection = async () => {
  try {
    console.log('正在测试后端连接...')
    const response = await checkHealth()
    console.log('后端健康检查响应:', response)
    isConnected.value = true
    alert('连接成功！AI服务正常工作')
  } catch (error) {
    console.error('连接测试失败:', error)
    isConnected.value = false
    alert(`连接失败: ${error.message || '网络异常'}`)
  }
}

// 发送消息
const sendMessage = async () => {
  const question = userInput.value.trim()
  if (!question || isLoading.value) return

  // 添加用户消息
  addMessage(question, 'user')
  userInput.value = ''

  // 添加到历史记录
  addToHistory(question)

  // 调用AI响应
  await getAIResponse(question)
}

// 快速提问
const askQuickQuestion = async (question) => {
  userInput.value = question
  await sendMessage()
}

// 添加消息
const addMessage = (content, type, isMock = false) => {
  const time = new Date().toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  })

  messages.value.push({
    content,
    type,
    time,
    isMock
  })

  // 滚动到底部
  scrollToBottom()
}

// 获取AI响应
const getAIResponse = async (question) => {
  isLoading.value = true

  try {
    // 尝试调用真实API
    console.log('正在调用AI接口...', { question, studentId: studentId.value })

    const response = await askQuestion({
      question: question,
      studentId: studentId.value
    })

    console.log('AI响应:', response)

    // 添加AI消息
    addMessage(response, 'assistant', false)
    isConnected.value = true

  } catch (error) {
    console.error('AI请求失败:', error)
    isConnected.value = false

    // 使用Mock数据作为后备
    const mockResponse = mockResponses[question] ||
      `【AI助手】关于"${question}"的建议：\n\n1. 明确学习目标，分解为具体任务\n2. 寻找优质学习资源\n3. 坚持每日学习，定期复习\n4. 理论结合实践，多做项目\n\n（当前使用模拟数据，真实AI服务连接失败）`

    addMessage(mockResponse, 'assistant', true)
  } finally {
    isLoading.value = false
  }
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 添加到历史记录
const addToHistory = (question) => {
  const time = new Date().toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit',
    month: '2-digit',
    day: '2-digit'
  })

  history.value.unshift({
    question,
    time
  })

  // 只保留最近的10条记录
  if (history.value.length > 10) {
    history.value = history.value.slice(0, 10)
  }
}

// 加载历史记录
const loadHistory = async (item) => {
  addMessage(item.question, 'user')
  await getAIResponse(item.question)
}

// 清空聊天
const clearChat = () => {
  if (confirm('确定要清空当前对话吗？')) {
    messages.value = []
  }
}

// 切换暗黑模式
const toggleDarkMode = () => {
  isDarkMode.value = !isDarkMode.value
  document.body.classList.toggle('dark-mode', isDarkMode.value)
}

// 截断文本
const truncateText = (text, maxLength) => {
  if (!text) return ''
  if (text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}

// 初始化检查连接
const initConnectionCheck = async () => {
  try {
    const response = await checkHealth()
    console.log('初始连接检查:', response)
    isConnected.value = true
  } catch (error) {
    console.warn('初始连接检查失败，使用模拟模式:', error)
    isConnected.value = false
  }
}

// 生命周期
onMounted(() => {
  // 确保滚动到底部
  scrollToBottom()

  // 初始化连接检查
  initConnectionCheck()
})
</script>

<style scoped>
.chat-view {
  min-height: 100vh;
  background: #f5f7fa;
}

/* 导航栏 */
.navbar {
  background: white;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 1rem 0;
}

.navbar .container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #409EFF;
  text-decoration: none;
  font-weight: 500;
}

.chat-header {
  text-align: center;
}

.chat-header h1 {
  font-size: 1.5rem;
  color: #303133;
  margin-bottom: 0.25rem;
}

.subtitle {
  color: #606266;
  font-size: 0.9rem;
}

.header-actions {
  display: flex;
  gap: 0.5rem;
}

.action-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #f0f7ff;
  border: none;
  color: #409EFF;
  cursor: pointer;
  transition: all 0.3s;
}

.action-btn:hover {
  background: #409EFF;
  color: white;
}

/* 聊天容器 */
.chat-container {
  display: flex;
  max-width: 1400px;
  margin: 2rem auto;
  gap: 2rem;
  padding: 0 1rem;
  height: calc(100vh - 80px);
}

/* 侧边栏 */
.sidebar {
  width: 300px;
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
}

.user-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding-bottom: 1.5rem;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 1.5rem;
}

.user-avatar {
  width: 60px;
  height: 60px;
  background: linear-gradient(45deg, #409EFF, #67C23A);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.5rem;
}

.user-info h3 {
  font-size: 1.1rem;
  color: #303133;
  margin-bottom: 0.25rem;
}

.user-info p {
  color: #606266;
  font-size: 0.9rem;
  margin-bottom: 0.25rem;
}

.student-id {
  font-size: 0.8rem;
  color: #909399;
}

.quick-questions,
.history-section {
  margin-bottom: 2rem;
}

.quick-questions h3,
.history-section h3 {
  font-size: 1rem;
  color: #303133;
  margin-bottom: 1rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.question-buttons {
  display: grid;
  grid-template-columns: 1fr;
  gap: 0.5rem;
}

.question-btn {
  padding: 0.75rem 1rem;
  background: #f8f9fa;
  border: none;
  border-radius: 8px;
  text-align: left;
  color: #606266;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 0.9rem;
}

.question-btn:hover {
  background: #409EFF;
  color: white;
  transform: translateX(5px);
}

.history-list {
  max-height: 200px;
  overflow-y: auto;
}

.history-item {
  padding: 0.75rem;
  background: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 0.5rem;
  cursor: pointer;
  transition: all 0.3s;
}

.history-item:hover {
  background: #e4f0ff;
}

.history-question {
  font-size: 0.9rem;
  color: #606266;
  margin-bottom: 0.25rem;
}

.history-time {
  font-size: 0.8rem;
  color: #909399;
}

/* 聊天区域 */
.chat-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.messages-container {
  flex: 1;
  padding: 2rem;
  overflow-y: auto;
}

.welcome-message {
  text-align: center;
  padding: 3rem 1rem;
}

.welcome-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(45deg, #409EFF, #67C23A);
  border-radius: 50%;
  margin: 0 auto 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 2rem;
}

.welcome-message h2 {
  font-size: 1.8rem;
  color: #303133;
  margin-bottom: 1rem;
}

.welcome-message p {
  color: #606266;
  margin-bottom: 0.5rem;
}

/* 消息样式 */
.message {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
  animation: fadeIn 0.3s ease-out;
}

.message.user {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  font-size: 1.2rem;
}

.message.user .message-avatar {
  background: linear-gradient(45deg, #409EFF, #67C23A);
  color: white;
}

.message.assistant .message-avatar {
  background: linear-gradient(45deg, #764ba2, #667eea);
  color: white;
}

.message-content {
  max-width: 70%;
}

.message.user .message-content {
  margin-right: 1rem;
}

.message.assistant .message-content {
  margin-left: 1rem;
}

.message-bubble {
  padding: 1rem 1.25rem;
  border-radius: 18px;
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.5;
}

.message.user .message-bubble {
  background: linear-gradient(45deg, #409EFF, #67C23A);
  color: white;
  border-radius: 18px 18px 4px 18px;
}

.message.assistant .message-bubble {
  background: #f0f7ff;
  color: #303133;
  border-radius: 18px 18px 18px 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.message-time {
  font-size: 0.8rem;
  color: #909399;
  margin-top: 0.5rem;
  text-align: right;
}

/* 加载动画 */
.loading-indicator {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: #f0f7ff;
  border-radius: 12px;
  margin-top: 1rem;
}

.loading-avatar {
  width: 40px;
  height: 40px;
  background: linear-gradient(45deg, #764ba2, #667eea);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.2rem;
}

.loading-text {
  font-size: 0.9rem;
  color: #606266;
}

.loading-dots {
  display: flex;
  gap: 4px;
  margin-top: 4px;
}

.loading-dots span {
  width: 6px;
  height: 6px;
  background: #409EFF;
  border-radius: 50%;
  animation: dotPulse 1.4s infinite ease-in-out;
}

.loading-dots span:nth-child(1) {
  animation-delay: -0.32s;
}

.loading-dots span:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes dotPulse {

  0%,
  80%,
  100% {
    transform: scale(0.8);
    opacity: 0.5;
  }

  40% {
    transform: scale(1);
    opacity: 1;
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 输入区域 */
.input-area {
  padding: 1.5rem;
  border-top: 1px solid #e4e7ed;
  background: white;
}

.input-container {
  position: relative;
}

.message-input {
  width: 100%;
  min-height: 80px;
  padding: 1rem 4rem 1rem 1rem;
  border: 2px solid #e4e7ed;
  border-radius: 12px;
  font-size: 1rem;
  resize: none;
  outline: none;
  transition: all 0.3s;
  font-family: inherit;
}

.message-input:focus {
  border-color: #409EFF;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.1);
}

.message-input:disabled {
  background: #f8f9fa;
  cursor: not-allowed;
}

.send-btn {
  position: absolute;
  right: 1rem;
  bottom: 1rem;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: linear-gradient(45deg, #409EFF, #67C23A);
  border: none;
  color: white;
  font-size: 1.2rem;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.send-btn:hover:not(:disabled) {
  transform: scale(1.1);
  box-shadow: 0 5px 15px rgba(64, 158, 255, 0.3);
}

.send-btn:disabled {
  background: #c0c4cc;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.input-tips {
  margin-top: 0.75rem;
  font-size: 0.8rem;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .chat-container {
    flex-direction: column;
    height: auto;
  }

  .sidebar {
    width: 100%;
    margin-bottom: 1rem;
  }

  .question-buttons {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .navbar .container {
    flex-direction: column;
    gap: 1rem;
  }

  .chat-container {
    padding: 0 0.5rem;
  }

  .messages-container {
    padding: 1rem;
  }

  .message-content {
    max-width: 85%;
  }

  .question-buttons {
    grid-template-columns: 1fr;
  }
}

/* 暗色模式 */
.dark-mode .chat-view {
  background: #1a1a1a;
}

.dark-mode .navbar,
.dark-mode .sidebar,
.dark-mode .chat-area {
  background: #2d2d2d;
}

.dark-mode .user-card,
.dark-mode .history-item {
  border-color: #404040;
}

.dark-mode .question-btn,
.dark-mode .history-item {
  background: #3d3d3d;
}

.dark-mode .message-bubble {
  background: #3d3d3d;
  color: #e0e0e0;
}

.dark-mode .message-input {
  background: #3d3d3d;
  border-color: #404040;
  color: #e0e0e0;
}

.dark-mode .message-input:focus {
  border-color: #409EFF;
}
/* 连接状态指示器 */
.connection-status {
  margin-top: 1rem;
  padding: 0.5rem;
  border-radius: 8px;
  background: #fff3cd;
  border: 1px solid #ffeaa7;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.85rem;
  color: #856404;
}

.connection-status.connected {
  background: #d1ecf1;
  border-color: #bee5eb;
  color: #0c5460;
}

.connection-status i {
  font-size: 0.6rem;
}

.connection-status.connected i {
  color: #28a745;
}

.connection-status:not(.connected) i {
  color: #dc3545;
}

/* 连接警告 */
.connection-alert {
  margin-top: 1rem;
  padding: 0.5rem;
  background: #fff3cd;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  color: #856404;
  font-size: 0.9rem;
}

.connection-warning {
  margin-left: 1rem;
  color: #dc3545;
  font-size: 0.8rem;
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

/* Mock数据徽章 */
.mock-badge {
  margin-top: 0.25rem;
  font-size: 0.7rem;
  color: #6c757d;
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

/* 禁用状态 */
.question-btn:disabled,
.action-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.question-btn:disabled:hover {
  background: #f8f9fa;
  color: #606266;
  transform: none;
}

/* 测试连接按钮样式 */
.action-btn:nth-child(3) {
  background: #f0f7ff;
  color: #409EFF;
}

.action-btn:nth-child(3):hover {
  background: #409EFF;
  color: white;
}

/* 暗黑模式适配 */
.dark-mode .connection-status {
  background: #3d3d3d;
  border-color: #404040;
  color: #e0e0e0;
}

.dark-mode .connection-status.connected {
  background: #2d2d2d;
  border-color: #0c5460;
}

.dark-mode .connection-alert {
  background: #3d3d3d;
  color: #ffc107;
}

.dark-mode .connection-warning {
  color: #ff6b6b;
}

.dark-mode .mock-badge {
  color: #adb5bd;
}
</style>
