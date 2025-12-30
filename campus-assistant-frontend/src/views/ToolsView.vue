<template>
  <div class="tools-view">
    <!-- 头部返回与标题区域 -->
    <div class="page-header">
      <el-page-header @back="goToHome">
        <template #title>
          <span>返回首页</span>
        </template>
        <template #content>
          <div class="header-content">
            <el-icon size="24" color="#409EFF">
              <Tools />
            </el-icon>
            <span class="header-title">学习工具中心</span>
          </div>
        </template>
      </el-page-header>
    </div>

    <!-- 主容器 -->
    <div class="plan-container">
      <!-- 外层容器：强制分为两行，每行3列 -->
      <main class="tools-grid">
        <!-- 第一行：番茄钟、任务清单、学习时长 -->
        <section class="tool-card" shadow="hover">
          <h2>⏰ 番茄钟</h2>
          <p class="desc">专注学习 25 分钟，休息 5 分钟，提升效率</p>
          <div class="pomodoro-timer">
            <div class="time-display">{{ formattedTime }}</div>
            <div class="status-text">
              当前状态：{{ isBreak ? '休息中' : '专注中' }}
            </div>
            <div class="controls">
              <el-button @click="startTimer" :disabled="running" type="primary" size="small">开始</el-button>
              <el-button @click="pauseTimer" :disabled="!running" type="warning" size="small">暂停</el-button>
              <el-button @click="resetTimer" type="info" size="small">重置</el-button>
            </div>
            <div class="sessions">
              今日完成番茄数：<strong>{{ completedSessions }}</strong>
            </div>
          </div>
        </section>

        <section class="tool-card" shadow="hover">
          <h2>📋 学习任务清单</h2>
          <p class="desc">记录今天要完成的学习任务，完成后打勾</p>
          <div class="task-input">
            <el-input
              v-model="newTask"
              placeholder="例如：完成数据结构作业 / 预习操作系统第3章"
              @keyup.enter="addTask"
              size="small"
            />
            <el-button @click="addTask" type="success" size="small">添加</el-button>
          </div>
          <ul class="task-list">
            <li
              v-for="task in tasks"
              :key="task.id"
              :class="{ done: task.done }"
            >
              <label>
                <input type="checkbox" v-model="task.done" />
                <span>{{ task.title }}</span>
              </label>
              <el-button class="delete-btn" @click="removeTask(task.id)" type="danger" text size="small">删除</el-button>
            </li>
            <li v-if="tasks.length === 0" class="empty-tip">
              还没有任务，先给自己定一个小目标吧～
            </li>
          </ul>
        </section>

        <section class="tool-card" shadow="hover">
          <h2>📈 学习时长记录</h2>
          <p class="desc">简单记录今天各科的学习时间，了解自己的时间分配</p>
          <div class="study-log-form">
            <el-input
              v-model="logSubject"
              placeholder="科目，例如：算法 / 英语 / 操作系统"
              size="small"
            />
            <el-input
              v-model.number="logMinutes"
              type="number"
              min="0"
              placeholder="分钟数，例如：60"
              size="small"
              style="width: 90px"
            />
            <el-button @click="addStudyLog" type="primary" size="small">记录</el-button>
          </div>
          <ul class="log-list">
            <li v-for="(item, index) in studyLogs" :key="index">
              <span class="subject">{{ item.subject }}</span>
              <span class="time">{{ item.minutes }} 分钟</span>
            </li>
            <li v-if="studyLogs.length === 0" class="empty-tip">
              记录一下今天第一段学习时间吧～
            </li>
          </ul>
          <div class="total-time">
            今日累计学习时长：<strong>{{ totalMinutes }}</strong> 分钟
          </div>
        </section>

        <!-- 第二行：AI翻译、AI计算、AI诗词 -->
        <section class="tool-card" shadow="hover">
          <h2>🌐 AI智能翻译</h2>
          <p class="desc">多语言精准翻译，支持文本/专业术语翻译</p>
          <div class="ai-translate">
            <el-input
              v-model="translateContent"
              type="textarea"
              placeholder="请输入需要翻译的内容（例如：专业术语/英文文献）"
              :rows="3"
              size="small"
            />
            <div class="translate-select" style="margin: 8px 0;">
              <el-select
                v-model="targetLang"
                placeholder="选择目标语言"
                size="small"
                style="width: 100%;"
              >
                <el-option label="英语" value="英语" />
                <el-option label="日语" value="日语" />
                <el-option label="韩语" value="韩语" />
                <el-option label="法语" value="法语" />
                <el-option label="德语" value="德语" />
              </el-select>
            </div>
            <el-button
              @click="handleTranslate"
              type="primary"
              size="small"
              :loading="translateLoading"
            >
              开始翻译
            </el-button>
            <div v-if="translateResult" class="translate-result">
              {{ translateResult }}
            </div>
            <div v-if="translateError" class="translate-error">
              {{ translateError }}
            </div>
          </div>
        </section>

        <section class="tool-card" shadow="hover">
          <h2>🧮 AI智能计算</h2>
          <p class="desc">解决数学/编程问题，提供详细解题步骤</p>
          <div class="ai-calculator">
            <el-input
              v-model="calcProblem"
              type="textarea"
              placeholder="请输入数学/编程问题（例如：求1+2+...+100的和 / 冒泡排序代码实现）"
              :rows="3"
              size="small"
              @keyup.enter="handleCalculate"
            />
            <el-button
              @click="handleCalculate"
              type="primary"
              size="small"
              :loading="calcLoading"
              style="margin-top: 8px;"
            >
              解题计算
            </el-button>
            <div v-if="calcResult" class="calc-result">
              {{ calcResult }}
            </div>
            <div v-if="calcError" class="calc-error">
              {{ calcError }}
            </div>
          </div>
        </section>

        <section class="tool-card" shadow="hover">
          <h2>📜 AI诗词创作</h2>
          <p class="desc">根据主题创作不同类型的诗词作品</p>
          <div class="ai-poetry">
            <el-input
              v-model="poetryTheme"
              placeholder="请输入创作主题（例如：校园、青春、梦想）"
              size="small"
              style="margin-bottom: 8px;"
            />
            <el-select
              v-model="poetryType"
              placeholder="选择诗词类型"
              size="small"
              style="margin-bottom: 8px; width: 100%;"
            >
              <el-option label="五言绝句" value="五言绝句" />
              <el-option label="七言律诗" value="七言律诗" />
              <el-option label="现代诗" value="现代诗" />
              <el-option label="宋词（蝶恋花）" value="宋词（蝶恋花）" />
            </el-select>
            <el-button
              @click="handleCreatePoetry"
              type="primary"
              size="small"
              :loading="poetryLoading"
            >
              创作诗词
            </el-button>
            <div v-if="poetryResult" class="poetry-result">
              {{ poetryResult }}
            </div>
            <div v-if="poetryError" class="poetry-error">
              {{ poetryError }}
            </div>
          </div>
        </section>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { Tools } from '@element-plus/icons-vue'
import axios from 'axios'

// 初始化路由
const router = useRouter()

// 返回首页方法
const goToHome = () => {
  router.back()
}

// 番茄钟逻辑
const FOCUS_DURATION = 25 * 60
const BREAK_DURATION = 5 * 60

const remaining = ref(FOCUS_DURATION)
const running = ref(false)
const isBreak = ref(false)
const completedSessions = ref(0)
let timerId = null

const formattedTime = computed(() => {
  const m = String(Math.floor(remaining.value / 60)).padStart(2, '0')
  const s = String(remaining.value % 60).padStart(2, '0')
  return `${m}:${s}`
})

const startTimer = () => {
  if (running.value) return
  running.value = true
  if (!timerId) {
    timerId = window.setInterval(() => {
      if (remaining.value > 0) {
        remaining.value--
      } else {
        // 当前阶段结束
        if (!isBreak.value) {
          completedSessions.value++
        }
        isBreak.value = !isBreak.value
        remaining.value = isBreak.value ? BREAK_DURATION : FOCUS_DURATION
      }
    }, 1000)
  }
}

const pauseTimer = () => {
  running.value = false
  if (timerId) {
    window.clearInterval(timerId)
    timerId = null
  }
}

const resetTimer = () => {
  pauseTimer()
  isBreak.value = false
  remaining.value = FOCUS_DURATION
}

onBeforeUnmount(() => {
  if (timerId) {
    window.clearInterval(timerId)
  }
})

// 学习任务清单逻辑
const tasks = ref([
  { id: 1, title: '预习明天的专业课', done: false },
  { id: 2, title: '整理今天的课堂笔记', done: false }
])
const newTask = ref('')

const addTask = () => {
  const title = newTask.value.trim()
  if (!title) return
  tasks.value.push({
    id: Date.now(),
    title,
    done: false
  })
  newTask.value = ''
}

const removeTask = (id) => {
  tasks.value = tasks.value.filter((t) => t.id !== id)
}

// 学习时长记录逻辑
const studyLogs = ref([])
const logSubject = ref('')
const logMinutes = ref(null)

const addStudyLog = () => {
  const subject = logSubject.value.trim()
  const minutes = Number(logMinutes.value)
  if (!subject || !minutes || minutes <= 0) return
  studyLogs.value.push({ subject, minutes })
  logSubject.value = ''
  logMinutes.value = null
}

const totalMinutes = computed(() =>
  studyLogs.value.reduce((sum, item) => sum + (item.minutes || 0), 0)
)

// ---------------- 新增：AI翻译相关 ----------------
const translateContent = ref('')
const targetLang = ref('')
const translateResult = ref('')
const translateError = ref('')
const translateLoading = ref(false)

const handleTranslate = async () => {
  if (!translateContent.value.trim() || !targetLang.value) {
    translateError.value = '请输入翻译内容并选择目标语言！'
    return
  }
  translateLoading.value = true
  translateError.value = ''
  translateResult.value = ''
  try {
    const res = await axios.get('/api/ai/translate', {
      params: {
        content: translateContent.value.trim(),
        targetLang: targetLang.value
      }
    })
    translateResult.value = res.data
  } catch (err) {
    translateError.value = '翻译失败：' + (err.message || '服务器异常')
  } finally {
    translateLoading.value = false
  }
}

// ---------------- 新增：AI计算机相关 ----------------
const calcProblem = ref('')
const calcResult = ref('')
const calcError = ref('')
const calcLoading = ref(false)

const handleCalculate = async () => {
  if (!calcProblem.value.trim()) {
    calcError.value = '请输入需要解决的问题！'
    return
  }
  calcLoading.value = true
  calcError.value = ''
  calcResult.value = ''
  try {
    const res = await axios.post('/api/ai/calculator', {
      problem: calcProblem.value.trim()
    })
    calcResult.value = res.data
  } catch (err) {
    calcError.value = '解题失败：' + (err.message || '服务器异常')
  } finally {
    calcLoading.value = false
  }
}

// ---------------- 新增：AI诗词创作相关 ----------------
const poetryTheme = ref('')
const poetryType = ref('')
const poetryResult = ref('')
const poetryError = ref('')
const poetryLoading = ref(false)

const handleCreatePoetry = async () => {
  if (!poetryTheme.value.trim() || !poetryType.value) {
    poetryError.value = '请输入创作主题并选择诗词类型！'
    return
  }
  poetryLoading.value = true
  poetryError.value = ''
  poetryResult.value = ''
  try {
    const res = await axios.get('/api/ai/poetry', {
      params: {
        theme: poetryTheme.value.trim(),
        type: poetryType.value
      }
    })
    poetryResult.value = res.data
  } catch (err) {
    poetryError.value = '创作失败：' + (err.message || '服务器异常')
  } finally {
    poetryLoading.value = false
  }
}
</script>

<style scoped lang="scss">
// 全局样式
.tools-view {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

// 顶部导航
.page-header {
  background: white;
  border-radius: 10px;
  padding: 16px 20px;
  margin-bottom: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

  .header-content {
    display: flex;
    align-items: center;
    gap: 10px;

    .header-title {
      font-size: 20px;
      font-weight: bold;
      color: #303133;
    }
  }
}

// 主容器
.plan-container {
  min-height: calc(100vh - 120px);

  // 核心修改：强制网格为 2行3列 布局
  .tools-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr); // 固定3列
    grid-template-rows: repeat(2, 1fr);    // 固定2行
    gap: 20px;
    // 限制最大宽度，保证3列布局不拉伸
    max-width: 1200px;
    margin: 0 auto;
  }
}

// 工具卡片通用样式
.tool-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-radius: 10px;
  padding: 18px 20px 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s;
  // 让卡片高度一致（可选，优化视觉）
  display: flex;
  flex-direction: column;
  height: 100%;

  &:hover {
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
    transform: translateY(-5px);
  }

  h2 {
    margin: 0 0 6px;
    font-size: 18px;
    color: #303133;
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .desc {
    margin: 0 0 14px;
    font-size: 13px;
    color: #909399;
  }

  // 内容区域自动填充，按钮/结果区域靠下
  > div {
    flex: 1;
    display: flex;
    flex-direction: column;
  }
}

// 番茄钟样式
.pomodoro-timer {
  text-align: center;
  flex: 1;

  .time-display {
    font-size: 36px;
    font-weight: bold;
    margin-bottom: 8px;
    color: #409EFF;
  }

  .status-text {
    margin-bottom: 12px;
    font-size: 14px;
    color: #606266;
  }

  .controls {
    display: flex;
    justify-content: center;
    gap: 10px;
    margin-bottom: 10px;
  }

  .sessions {
    font-size: 13px;
    color: #606266;
  }
}

// 任务清单样式
.task-input {
  display: flex;
  gap: 8px;
  margin-bottom: 10px;

  .el-input {
    flex: 1;
  }
}

.task-list {
  list-style: none;
  padding: 0;
  margin: 0;
  max-height: 200px;
  overflow-y: auto;
  flex: 1;

  li {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 6px 0;
    border-bottom: 1px dashed #ebeef5;
    font-size: 13px;

    &:last-child {
      border-bottom: none;
    }

    &.done span {
      text-decoration: line-through;
      color: #c0c4cc;
    }

    label {
      display: flex;
      align-items: center;
      gap: 6px;
      flex: 1;
      cursor: pointer;
    }
  }
}

// 学习时长记录样式
.study-log-form {
  display: flex;
  gap: 8px;
  margin-bottom: 10px;

  .el-input:first-child {
    flex: 1.2;
  }
}

.log-list {
  list-style: none;
  padding: 0;
  margin: 0 0 8px;
  max-height: 160px;
  overflow-y: auto;
  font-size: 13px;
  flex: 1;

  li {
    display: flex;
    justify-content: space-between;
    padding: 4px 0;

    .subject {
      color: #606266;
    }

    .time {
      color: #409EFF;
    }
  }
}

.total-time {
  font-size: 13px;
  color: #606266;
  border-top: 1px dashed #ebeef5;
  padding-top: 6px;
}

.empty-tip {
  text-align: center;
  color: #c0c4cc;
  font-size: 13px;
  padding: 8px 0;
}

// 新增AI翻译样式
.ai-translate {
  flex: 1;
  display: flex;
  flex-direction: column;

  .el-textarea {
    flex: 1;
  }

  .translate-result {
    margin-top: 12px;
    max-height: 150px;
    overflow-y: auto;
    font-size: 13px;
    color: #606266;
    padding: 8px;
    background: #f5f7fa;
    border-radius: 4px;
    line-height: 1.6;
    flex: 1;
  }

  .translate-error {
    margin-top: 12px;
    font-size: 13px;
    color: #f56c6c;
  }
}

// 新增AI计算机样式
.ai-calculator {
  flex: 1;
  display: flex;
  flex-direction: column;

  .el-textarea {
    flex: 1;
  }

  .calc-result {
    margin-top: 12px;
    max-height: 200px;
    overflow-y: auto;
    font-size: 13px;
    color: #606266;
    padding: 8px;
    background: #f5f7fa;
    border-radius: 4px;
    white-space: pre-line;
    line-height: 1.6;
    flex: 1;
  }

  .calc-error {
    margin-top: 12px;
    font-size: 13px;
    color: #f56c6c;
  }
}

// 新增AI诗词创作样式
.ai-poetry {
  flex: 1;
  display: flex;
  flex-direction: column;

  .poetry-result {
    margin-top: 12px;
    max-height: 180px;
    overflow-y: auto;
    font-size: 14px;
    color: #606266;
    padding: 12px;
    background: #f5f7fa;
    border-radius: 4px;
    line-height: 1.8;
    font-family: "SimSun", "宋体", serif;
    text-align: center;
    flex: 1;
  }

  .poetry-error {
    margin-top: 12px;
    font-size: 13px;
    color: #f56c6c;
  }
}

// 响应式适配（小屏幕自动改为单列）
@media (max-width: 992px) {
  .plan-container .tools-grid {
    grid-template-columns: 1fr;
    grid-template-rows: repeat(6, auto);
  }
}

@media (max-width: 768px) {
  .tools-view {
    padding: 16px;
  }

  .page-header {
    padding: 12px 16px;
  }

  .tool-card {
    padding: 16px;
  }

  .ai-translate, .ai-calculator, .ai-poetry {
    .el-input, .el-select {
      width: 100% !important;
    }
  }
}
</style>
