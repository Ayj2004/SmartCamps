<template>
  <div class="tools-view">
    <!-- 新增：返回主页按钮 + 原页面头部（适配学习计划页样式） -->
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

    <!-- 主容器（参考学习计划页的plan-container布局） -->
    <div class="plan-container">
      <main class="tools-grid">
        <!-- 番茄钟 -->
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

        <!-- 学习任务清单 -->
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

        <!-- 简易学习时长记录 -->
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
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { Tools } from '@element-plus/icons-vue'

// 初始化路由（适配学习计划页的跳转逻辑）
const router = useRouter()

// 新增：返回主页方法（统一学习计划页的跳转方式）
const goToHome = () => {
  router.back() // 和学习计划页保持一致的返回逻辑
}

// 番茄钟：25 分钟专注 + 5 分钟休息（业务逻辑完全不变）
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

// 学习任务清单（业务逻辑完全不变）
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

// 学习时长记录（业务逻辑完全不变）
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
</script>

<style scoped lang="scss">
// 统一学习计划页的全局样式
.tools-view {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); // 统一渐变背景
  padding: 20px;
}

// 顶部导航（完全复用学习计划页样式）
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

// 主容器（参考学习计划页的plan-container布局）
.plan-container {
  min-height: calc(100vh - 120px);

  .tools-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 20px;
  }
}

// 工具卡片（适配学习计划页的卡片样式）
.tool-card {
  background: rgba(255, 255, 255, 0.95); // 毛玻璃背景
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px); // 兼容webkit
  border-radius: 10px;
  padding: 18px 20px 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s;

  &:hover {
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
    transform: translateY(-5px); // 统一hover上浮效果
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
}

// 番茄钟（样式适配，逻辑不变）
.pomodoro-timer {
  text-align: center;

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

// 任务清单（样式适配，逻辑不变）
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

// 学习时长记录（样式适配，逻辑不变）
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

// 响应式适配（和学习计划页保持一致）
@media (max-width: 992px) {
  .plan-container .tools-grid {
    grid-template-columns: 1fr;
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
}
</style>
