<template>
  <div class="study-plan-view">
    <!-- 顶部导航 -->
    <div class="page-header">
      <el-page-header @back="router.back()">
        <template #title>
          <span>返回首页</span>
        </template>
        <template #content>
          <div class="header-content">
            <el-icon size="24" color="#409EFF">
              <Calendar />
            </el-icon>
            <span class="header-title">个性化学习计划</span>
          </div>
        </template>
      </el-page-header>
    </div>

    <div class="plan-container">
      <!-- 侧边栏 - 计划生成器 -->
      <div class="sidebar">
        <el-card class="generator-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon>
                <MagicStick />
              </el-icon>
              <span>学习计划生成器</span>
            </div>
          </template>

          <el-form :model="form" label-width="80px">
            <el-form-item label="学号">
              <el-input v-model="form.studentId" placeholder="请输入学号" />
            </el-form-item>

            <el-form-item label="姓名">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>

            <el-form-item label="专业">
              <el-select v-model="form.major" placeholder="请选择专业">
                <el-option label="计算机科学" value="计算机科学" />
                <el-option label="软件工程" value="软件工程" />
                <el-option label="人工智能" value="人工智能" />
                <el-option label="数据科学" value="数据科学" />
                <el-option label="网络安全" value="网络安全" />
              </el-select>
            </el-form-item>

            <el-form-item label="年级">
              <el-select v-model="form.grade" placeholder="请选择年级">
                <el-option label="大一" value="1" />
                <el-option label="大二" value="2" />
                <el-option label="大三" value="3" />
                <el-option label="大四" value="4" />
              </el-select>
            </el-form-item>

            <el-form-item label="学习目标">
              <el-input v-model="form.goal" type="textarea" :rows="3" placeholder="请输入学习目标，例如：掌握Java编程、通过英语四级等" />
            </el-form-item>

            <el-form-item label="可用时间">
              <el-slider v-model="form.weeklyHours" :min="10" :max="40" :step="5" show-stops show-input>
                <template #prefix>每周</template>
                <template #suffix>小时</template>
              </el-slider>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" :loading="loading" @click="generatePlan" style="width: 100%">
                <el-icon>
                  <MagicStick />
                </el-icon>
                生成学习计划
              </el-button>
            </el-form-item>
          </el-form>

          <div class="quick-templates">
            <h4>🚀 快速模板</h4>
            <div class="template-buttons">
              <el-button v-for="template in templates" :key="template.name" size="small"
                         @click="applyTemplate(template)">
                {{ template.name }}
              </el-button>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 主内容区 - 计划展示 -->
      <div class="main-content">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-state">
          <el-result icon="info" title="正在生成学习计划">
            <template #sub-title>
              <div class="loading-animation">
                <div class="loading-text">AI正在为您定制学习方案...</div>
                <div class="loading-dots">
                  <span class="dot"></span>
                  <span class="dot"></span>
                  <span class="dot"></span>
                </div>
              </div>
            </template>
          </el-result>
        </div>

        <!-- 空状态 -->
        <div v-else-if="!studyPlan" class="empty-state">
          <el-result icon="info" title="未生成学习计划">
            <template #sub-title>
              请填写左侧信息，生成您的个性化学习计划
            </template>
            <template #extra>
              <div class="empty-tips">
                <el-alert title="提示" type="info" :closable="false" show-icon>
                  <p>我们的AI助手会根据您的专业、目标和可用时间，制定科学的学习计划</p>
                  <p>包括：学习目标、周计划、学习资源、评估方法等</p>
                </el-alert>

                <div class="example-plans">
                  <h3>📋 计划示例</h3>
                  <el-collapse>
                    <el-collapse-item title="计算机科学专业 - 学期计划">
                      <div class="plan-example">
                        <h4>学习目标：</h4>
                        <ul>
                          <li>掌握Java编程基础</li>
                          <li>理解数据结构与算法</li>
                          <li>完成一个实战项目</li>
                        </ul>

                        <h4>时间安排：</h4>
                        <ul>
                          <li>每周20小时学习时间</li>
                          <li>每日3-4小时专注学习</li>
                          <li>周末复习和实践</li>
                        </ul>
                      </div>
                    </el-collapse-item>

                    <el-collapse-item title="考研复习 - 全年计划">
                      <div class="plan-example">
                        <h4>阶段划分：</h4>
                        <ul>
                          <li>基础阶段（3-6月）：梳理知识点</li>
                          <li>强化阶段（7-9月）：专项突破</li>
                          <li>冲刺阶段（10-12月）：模拟实战</li>
                        </ul>
                      </div>
                    </el-collapse-item>
                  </el-collapse>
                </div>
              </div>
            </template>
          </el-result>
        </div>

        <!-- 计划展示 -->
        <div v-else class="plan-display">
          <el-card class="plan-card" shadow="never">
            <!-- 计划标题 -->
            <div class="plan-header">
              <h1 class="plan-title">
                <el-icon>
                  <Calendar />
                </el-icon>
                {{ studyPlan.major }}专业学习计划
              </h1>
              <div class="plan-meta">
                <el-tag type="primary">{{ form.grade }}年级</el-tag>
                <el-tag type="success">每周{{ form.weeklyHours }}小时</el-tag>
                <span class="plan-time">生成时间: {{ formatTime(studyPlan.createTime) }}</span>
              </div>
            </div>

            <!-- 计划内容 -->
            <div class="plan-content">
              <!-- 基本信息 -->
              <div class="section basic-info">
                <h2 class="section-title">
                  <el-icon>
                    <User />
                  </el-icon>
                  基本信息
                </h2>
                <el-descriptions :column="2" border>
                  <el-descriptions-item label="学号">{{ form.studentId }}</el-descriptions-item>
                  <el-descriptions-item label="姓名">{{ form.name }}</el-descriptions-item>
                  <el-descriptions-item label="专业">{{ studyPlan.major }}</el-descriptions-item>
                  <el-descriptions-item label="年级">{{ form.grade }}年级</el-descriptions-item>
                </el-descriptions>
              </div>

              <!-- 计划详情 -->
              <div class="section plan-detail" v-if="studyPlan.planContent">
                <h2 class="section-title">
                  <el-icon>
                    <Document />
                  </el-icon>
                  学习计划详情
                </h2>
                <div class="detail-content" v-html="formatPlanContent(studyPlan.planContent)"></div>
              </div>

              <!-- AI建议 -->
              <div class="section ai-suggestion" v-if="studyPlan.suggestion">
                <h2 class="section-title">
                  <el-icon>
                    <ChatDotRound />
                  </el-icon>
                  AI建议
                </h2>
                <el-alert :title="studyPlan.suggestion" type="info" :closable="false" show-icon />
              </div>

              <!-- 行动建议 -->
              <div class="section action-suggestions">
                <h2 class="section-title">
                  <el-icon>
                    <Guide />
                  </el-icon>
                  行动建议
                </h2>
                <div class="action-grid">
                  <div class="action-card" v-for="action in actionList" :key="action.title">
                    <div class="action-icon" :style="{ backgroundColor: action.color }">
                      <component :is="action.icon" size="24" />
                    </div>
                    <h3>{{ action.title }}</h3>
                    <p>{{ action.description }}</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 操作按钮 -->
            <div class="plan-actions">
              <el-button-group>
                <el-button type="primary" @click="downloadPlan">
                  <el-icon>
                    <Download />
                  </el-icon>
                  下载计划
                </el-button>
                <el-button type="success" @click="sharePlan">
                  <el-icon>
                    <Share />
                  </el-icon>
                  分享计划
                </el-button>
                <el-button @click="printPlan">
                  <el-icon>
                    <Printer />
                  </el-icon>
                  打印计划
                </el-button>
                <el-button type="warning" @click="regeneratePlan">
                  <el-icon>
                    <Refresh />
                  </el-icon>
                  重新生成
                </el-button>
              </el-button-group>
            </div>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAiStore } from '@/stores/ai'
import { ElMessage } from 'element-plus'
import {
  Calendar,
  MagicStick,
  User,
  Document,
  ChatDotRound,
  Guide,
  Download,
  Share,
  Printer,
  Refresh,
  Clock,
  Reading,
  Collection,
  Star,
  Trophy
} from '@element-plus/icons-vue'

const router = useRouter()
const aiStore = useAiStore()

const loading = ref(false)
const studyPlan = ref(null)

// 表单数据
const form = reactive({
  studentId: '2023001',
  name: '张三',
  major: '计算机科学',
  grade: '3',
  goal: '掌握Java编程，学习Spring Boot框架，完成一个实战项目',
  weeklyHours: 25
})

// 快速模板
const templates = [
  { name: '计算机基础', major: '计算机科学', goal: '掌握编程基础和算法' },
  { name: '考研复习', major: '计算机科学', goal: '考研专业课和公共课复习' },
  { name: '项目实践', major: '软件工程', goal: '完成一个完整的软件项目' },
  { name: 'AI学习', major: '人工智能', goal: '学习机器学习和深度学习' }
]

// 行动建议列表
const actionList = [
  {
    icon: Clock,
    title: '时间管理',
    description: '每日固定学习时间，避免拖延',
    color: '#409EFF'
  },
  {
    icon: Reading,
    title: '资源利用',
    description: '充分利用图书馆和在线资源',
    color: '#67C23A'
  },
  {
    icon: Collection,
    title: '定期复习',
    description: '每周回顾学习内容',
    color: '#F56C6C'
  },
  {
    icon: Star,
    title: '质量优先',
    description: '注重学习质量而非时长',
    color: '#909399'
  },
  {
    icon: Trophy,
    title: '奖励机制',
    description: '完成目标后适当奖励自己',
    color: '#F56C6C'
  }
]

// 生成学习计划
const generatePlan = async () => {
  if (!form.major || !form.studentId) {
    ElMessage.warning('请填写专业和学号')
    return
  }

  loading.value = true
  try {
    const plan = await aiStore.fetchStudyPlan(form.major, form.studentId)
    studyPlan.value = {
      ...plan,
      createTime: new Date(),
      major: form.major
    }
    ElMessage.success('学习计划生成成功！')
  } catch (error) {
    ElMessage.error('生成学习计划失败')
  } finally {
    loading.value = false
  }
}

// 应用模板
const applyTemplate = (template) => {
  form.major = template.major
  form.goal = template.goal
  ElMessage.info(`已应用"${template.name}"模板`)
}

// 格式化计划内容
const formatPlanContent = (content) => {
  return content
    .replace(/\n/g, '<br>')
    .replace(/^#\s+(.*)$/gm, '<h2>$1</h2>')
    .replace(/^##\s+(.*)$/gm, '<h3>$1</h3>')
    .replace(/^-\s+(.*)$/gm, '<li>$1</li>')
    .replace(/^(\d+)\.\s+(.*)$/gm, '<li>$2</li>')
    .replace(/(\*\*.*?\*\*)/g, '<strong>$1</strong>')
    .replace(/\*\*/g, '')
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleString('zh-CN')
}

// 下载计划
const downloadPlan = () => {
  if (!studyPlan.value) return

  const content = `
学习计划报告
=============

基本信息
--------
学号：${form.studentId}
姓名：${form.name}
专业：${studyPlan.value.major}
年级：${form.grade}年级
生成时间：${formatTime(studyPlan.value.createTime)}

学习计划详情
------------
${studyPlan.value.planContent}

AI建议
------
${studyPlan.value.suggestion}
`

  const blob = new Blob([content], { type: 'text/plain' })
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = `学习计划-${form.name}-${form.major}.txt`
  link.click()
  URL.revokeObjectURL(url)

  ElMessage.success('计划已下载')
}

// 分享计划
const sharePlan = () => {
  ElMessage.info('分享功能开发中...')
}

// 打印计划
const printPlan = () => {
  window.print()
}

// 重新生成计划
const regeneratePlan = () => {
  studyPlan.value = null
  ElMessage.info('请重新填写信息生成计划')
}
</script>

<style scoped lang="scss">
.study-plan-view {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

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

.plan-container {
  display: grid;
  grid-template-columns: 350px 1fr;
  gap: 20px;
  min-height: calc(100vh - 120px);

  @media (max-width: 992px) {
    grid-template-columns: 1fr;
  }
}

.sidebar {
  .generator-card {
    height: 100%;
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);

    .card-header {
      display: flex;
      align-items: center;
      gap: 10px;
      font-size: 18px;
      font-weight: bold;
      color: #303133;

      .el-icon {
        color: #409EFF;
      }
    }

    .el-form {
      .el-form-item {
        margin-bottom: 20px;

        &:last-child {
          margin-bottom: 0;
        }
      }
    }

    .quick-templates {
      margin-top: 20px;
      padding-top: 20px;
      border-top: 1px solid #e4e7ed;

      h4 {
        margin-bottom: 15px;
        color: #303133;
      }

      .template-buttons {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 10px;

        .el-button {
          width: 100%;
        }
      }
    }
  }
}

.main-content {

  .loading-state,
  .empty-state {
    background: white;
    border-radius: 10px;
    padding: 40px;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;

    .loading-animation {
      text-align: center;

      .loading-text {
        margin-bottom: 20px;
        color: #606266;
      }

      .loading-dots {
        display: flex;
        justify-content: center;
        gap: 5px;

        .dot {
          width: 8px;
          height: 8px;
          background: #409EFF;
          border-radius: 50%;
          animation: pulse 1.4s infinite ease-in-out;

          &:nth-child(1) {
            animation-delay: -0.32s;
          }

          &:nth-child(2) {
            animation-delay: -0.16s;
          }
        }
      }
    }

    .empty-tips {
      margin-top: 20px;
      max-width: 600px;

      .example-plans {
        margin-top: 30px;

        h3 {
          margin-bottom: 15px;
          color: #303133;
        }

        .plan-example {
          padding: 10px;

          h4 {
            margin: 15px 0 10px;
            color: #409EFF;
          }

          ul {
            margin-left: 20px;
            color: #606266;

            li {
              margin-bottom: 5px;
            }
          }
        }
      }
    }
  }
}

.plan-display {
  .plan-card {
    border: none;
    background: rgba(255, 255, 255, 0.95);

    .plan-header {
      text-align: center;
      margin-bottom: 30px;
      padding-bottom: 20px;
      border-bottom: 2px solid #409EFF;

      .plan-title {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
        margin-bottom: 15px;
        color: #303133;
        font-size: 28px;

        .el-icon {
          color: #409EFF;
        }
      }

      .plan-meta {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 15px;
        flex-wrap: wrap;

        .plan-time {
          color: #909399;
          font-size: 14px;
        }
      }
    }

    .plan-content {
      .section {
        margin-bottom: 30px;

        .section-title {
          display: flex;
          align-items: center;
          gap: 10px;
          margin-bottom: 15px;
          padding-bottom: 10px;
          border-bottom: 1px solid #e4e7ed;
          color: #303133;

          .el-icon {
            color: #409EFF;
          }
        }
      }

      .detail-content {
        padding: 20px;
        background: #f8f9fa;
        border-radius: 8px;
        line-height: 1.8;

        :deep(h2) {
          color: #409EFF;
          margin: 20px 0 15px;
          font-size: 20px;
        }

        :deep(h3) {
          color: #67C23A;
          margin: 15px 0 10px;
          font-size: 16px;
        }

        :deep(li) {
          margin-bottom: 5px;
          position: relative;
          padding-left: 20px;

          &:before {
            content: '•';
            position: absolute;
            left: 0;
            color: #409EFF;
          }
        }
      }

      .action-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 20px;

        .action-card {
          background: white;
          padding: 20px;
          border-radius: 8px;
          box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
          text-align: center;
          transition: transform 0.3s;

          &:hover {
            transform: translateY(-5px);
          }

          .action-icon {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 15px;
            color: white;
          }

          h3 {
            margin: 10px 0;
            color: #303133;
            font-size: 16px;
          }

          p {
            color: #606266;
            font-size: 14px;
            line-height: 1.5;
          }
        }
      }
    }

    .plan-actions {
      display: flex;
      justify-content: center;
      margin-top: 30px;
      padding-top: 20px;
      border-top: 1px solid #e4e7ed;
    }
  }
}

@keyframes pulse {

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

// 打印样式
@media print {
  .study-plan-view {
    background: white !important;
    padding: 0 !important;
  }

  .page-header,
  .sidebar,
  .plan-actions {
    display: none !important;
  }

  .plan-container {
    grid-template-columns: 1fr !important;
    min-height: auto !important;
  }

  .plan-card {
    box-shadow: none !important;
    border: 1px solid #ddd !important;
  }
}
</style>
