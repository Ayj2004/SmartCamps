<template>
  <div class="courses-view">
    <!-- 顶部导航（参考学习计划页的page-header） -->
    <div class="page-header">
      <el-page-header @back="goToHome">
        <template #title>
          <span>返回首页</span>
        </template>
        <template #content>
          <div class="header-content">
            <el-icon size="24" color="#409EFF">
              <House />
            </el-icon>
            <span class="header-title">课程查询与推荐</span>
          </div>
        </template>
      </el-page-header>
    </div>

    <!-- 主容器（参考学习计划页的plan-container布局） -->
    <div class="plan-container">
      <!-- 查询/推荐区域（标签页） -->
      <div class="main-content">
        <el-card class="query-card" shadow="hover">
          <el-tabs v-model="activeTab" type="border-card">
            <!-- 按专业查询 -->
            <el-tab-pane label="按专业查询" name="byMajor">
              <el-form :model="queryForm" inline @submit.prevent="queryCourses" class="query-form">
                <el-form-item label="选择专业：">
                  <el-select
                    v-model="queryForm.majorName"
                    placeholder="请选择专业"
                    style="width: 220px"
                  >
                    <el-option label="计算机科学" value="计算机科学" />
                    <el-option label="软件工程" value="软件工程" />
                    <el-option label="人工智能" value="人工智能" />
                    <el-option label="数据科学" value="数据科学" />
                    <el-option label="网络安全" value="网络安全" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="queryCourses" :loading="loading">
                    <el-icon><Search /></el-icon>
                    查询课程
                  </el-button>
                </el-form-item>
              </el-form>

              <div class="course-result">
                <!-- 加载状态 -->
                <div v-if="loading && activeTab === 'byMajor'" class="loading">
                  <el-skeleton :rows="5" animated />
                </div>

                <!-- 无数据状态 -->
                <div v-else-if="courses.length === 0 && hasQuery" class="empty">
                  <el-empty description="暂无该专业的课程信息" />
                </div>

                <!-- 课程列表展示 -->
                <div v-else-if="courses.length > 0" class="course-list">
                  <h2>✅ {{ queryForm.majorName }} 专业课程列表</h2>
                  <div class="course-cards">
                    <el-card
                      v-for="(course, index) in courses"
                      :key="index"
                      class="course-card"
                      shadow="hover"
                    >
                      <!-- 兼容后端返回的CourseDto对象和字符串 -->
                      <div class="course-name">{{ course.courseName || course }}</div>
                      <el-divider direction="horizontal" size="small" />
                      <div class="course-tips">
                        <el-tag size="small" type="info">专业核心课程</el-tag>
                      </div>
                    </el-card>
                  </div>
                </div>

                <!-- 初始提示 -->
                <div v-else class="init-tip">
                  <el-alert
                    title="提示"
                    type="info"
                    :closable="false"
                    show-icon
                  >
                    请选择专业并点击查询，获取该专业的课程列表
                  </el-alert>
                </div>
              </div>
            </el-tab-pane>

            <!-- AI 智能推荐 -->
            <el-tab-pane label="AI 智能推荐" name="ai">
              <el-form :model="aiForm" label-width="90px" @submit.prevent="handleAiRecommend">
                <el-form-item label="学习方向">
                  <el-input
                    v-model="aiForm.interests"
                    type="textarea"
                    :rows="3"
                    placeholder="例如：对 Java 后端开发、微服务架构感兴趣；想做 Web 全栈开发；想了解人工智能与大数据分析等"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" :loading="aiLoading" @click="handleAiRecommend">
                    <el-icon><Search /></el-icon>
                    获取AI推荐课程
                  </el-button>
                </el-form-item>
              </el-form>

              <div class="course-result">
                <div v-if="aiLoading" class="loading">
                  <el-skeleton :rows="3" animated />
                </div>
                <div v-else-if="aiCourses.length === 0 && aiQueried" class="empty">
                  <el-empty description="暂时没有合适的推荐课程，请尝试换一种描述方式" />
                </div>
                <div v-else-if="aiCourses.length > 0" class="course-list">
                  <h2>🤖 AI 为你推荐的课程</h2>
                  <p class="ai-tip">根据你的兴趣描述，系统为你筛选出以下更匹配的课程：</p>
                  <div class="course-cards">
                    <el-card
                      v-for="(course, index) in aiCourses"
                      :key="index"
                      class="course-card"
                      shadow="hover"
                    >
                      <div class="course-name">{{ course }}</div>
                      <el-divider />
                      <div class="course-tips">
                        <el-tag size="small" type="success">AI 推荐</el-tag>
                      </div>
                    </el-card>
                  </div>
                </div>
                <div v-else class="init-tip">
                  <el-alert
                    title="在这里用自然语言描述你的兴趣，AI 会结合课程库为你推荐合适的课程"
                    type="success"
                    :closable="false"
                    show-icon
                  />
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, House } from '@element-plus/icons-vue'
import { recommendCourses } from '@/api/ai'

// 初始化路由
const router = useRouter()

// 状态管理
const activeTab = ref('byMajor')
const loading = ref(false) // 按专业查询加载状态
const aiLoading = ref(false) // AI 推荐加载状态
const courses = ref([])    // 课程列表
const hasQuery = ref(false) // 是否执行过按专业查询
const aiCourses = ref([]) // AI 推荐课程列表
const aiQueried = ref(false) // 是否执行过 AI 推荐

// 查询表单（仅包含后端接口需要的majorName字段）
const queryForm = reactive({
  majorName: ''
})

// AI 推荐表单
const aiForm = reactive({
  interests: ''
})

// 跳转首页
const goToHome = () => {
  router.push('/')
}

// 查询课程（核心逻辑完全未动）
const queryCourses = async () => {
  // 表单校验
  if (!queryForm.majorName) {
    ElMessage.warning('请先选择专业！')
    return
  }

  loading.value = true
  hasQuery.value = true
  try {
    // 1. 构造查询参数（匹配后端?major=xxx格式）
    const params = new URLSearchParams()
    params.append('major', queryForm.majorName)
    // 2. 请求代理后的接口（/api转发到8082端口）
    const response = await fetch(`/course/major?${params.toString()}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    })

    if (response.ok) {
      const result = await response.json()
      // 修复：匹配字符串 "0"（后端返回的code是字符串类型）
      if (result.code === "0" && result.data) {
        courses.value = result.data // data是CourseDto列表
        ElMessage.success(`成功查询到 ${result.data.length} 门课程`)
      } else {
        courses.value = []
        ElMessage.info(result.msg || '暂无课程数据')
      }
    } else {
      throw new Error(`请求失败：${response.status} ${response.statusText}`)
    }
  } catch (error) {
    ElMessage.error('课程查询失败，请稍后重试')
    console.error('查询课程异常：', error)
    courses.value = []
  } finally {
    loading.value = false
  }
}

// AI 智能推荐（核心逻辑完全未动）
const handleAiRecommend = async () => {
  if (!aiForm.interests.trim()) {
    ElMessage.warning('请先描述一下你的学习兴趣或方向！')
    return
  }
  aiLoading.value = true
  aiQueried.value = true
  try {
    const data = await recommendCourses(aiForm.interests.trim())
    // 后端直接返回 List<String>
    aiCourses.value = Array.isArray(data) ? data : []
    if (aiCourses.value.length > 0) {
      ElMessage.success(`为你推荐了 ${aiCourses.value.length} 门课程`)
    } else {
      ElMessage.info('暂时没有匹配到合适的课程，可以尝试换一种表述方式哦')
    }
  } catch (error) {
    console.error('AI 推荐课程异常：', error)
    ElMessage.error(error.message || 'AI 推荐失败，请稍后重试')
    aiCourses.value = []
  } finally {
    aiLoading.value = false
  }
}
</script>

<style scoped lang="scss">
// 统一学习计划页的布局样式，业务逻辑相关样式完全保留
.courses-view {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); // 统一背景
  padding: 20px;
  position: relative;
}

// 顶部导航（复用学习计划页样式）
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

// 主容器（参考学习计划页布局）
.plan-container {
  min-height: calc(100vh - 120px);

  .main-content {
    .query-card {
      background: rgba(255, 255, 255, 0.95);
      backdrop-filter: blur(10px);
      -webkit-backdrop-filter: blur(10px); // 兼容
      border: none;
      border-radius: 10px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);

      .el-tabs {
        --el-tabs-header-text-color: #303133;
        --el-tabs-active-text-color: #409EFF;
        --el-tabs-border-color: #e4e7ed;
      }
    }
  }
}

// 原有业务样式完全保留
.query-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.course-result {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
}

.loading {
  padding: 10px;
}

.empty {
  padding: 40px 0;
  text-align: center;
}

.init-tip {
  padding: 20px;
}

.course-list h2 {
  color: #303133;
  margin-bottom: 15px;
  font-size: 18px;
}

.ai-tip {
  margin-bottom: 10px;
  color: #606266;
  font-size: 13px;
}

.course-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 15px;
}

.course-card {
  text-align: center;
  padding: 15px 0;
  transition: transform 0.3s; // 增加hover动画（仅样式）

  &:hover {
    transform: translateY(-5px);
  }
}

.course-name {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 10px;
}

// 响应式适配（参考学习计划页）
@media (max-width: 992px) {
  .course-cards {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)) !important;
  }
}
</style>
