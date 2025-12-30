import { defineStore } from 'pinia'
import { ref } from 'vue'
import * as aiApi from '@/api/ai'

export const useAiStore = defineStore('ai', () => {
  // 状态
  const conversation = ref([]) // 对话历史
  const studyPlan = ref(null) // 学习计划
  const loading = ref(false) // 加载状态
  const config = ref(null) // 配置信息

  // 示例问题
  const exampleQuestions = ref([
    '如何学习Java编程？',
    'MySQL索引优化有哪些技巧？',
    '计算机专业考研应该如何准备？',
    '如何平衡学习和社团活动？',
    '大二应该选哪些专业选修课？',
  ])

  // 获取配置信息
  const fetchConfig = async () => {
    try {
      const data = await aiApi.getConfig()
      config.value = data
    } catch (error) {
      console.error('获取配置失败:', error)
    }
  }

  // 智能问答
  const askQuestion = async (question, studentId = '2023001') => {
    loading.value = true
    try {
      const response = await aiApi.askQuestion({
        question,
        studentId,
      })

      // 添加到对话历史
      conversation.value.push({
        type: 'user',
        content: question,
        time: new Date().toLocaleTimeString(),
      })

      conversation.value.push({
        type: 'assistant',
        content: response,
        time: new Date().toLocaleTimeString(),
      })

      return response
    } catch (error) {
      console.error('问答失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  // 生成学习计划
  const fetchStudyPlan = async (major = '计算机科学', studentId = '2023001') => {
    loading.value = true
    try {
      const data = await aiApi.generateStudyPlan({ major, studentId })
      studyPlan.value = data
      return data
    } catch (error) {
      console.error('获取学习计划失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  // 推荐课程
  const fetchRecommendedCourses = async (interests) => {
    loading.value = true
    try {
      const data = await aiApi.recommendCourses(interests)
      return data
    } catch (error) {
      console.error('推荐课程失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  // 生成学习图像
  const generateImage = async (theme) => {
    loading.value = true
    try {
      const data = await aiApi.generateLearningImage(theme)
      return data
    } catch (error) {
      console.error('生成图像失败:', error)
      throw error
    } finally {
      loading.value = false
    }
  }

  // 清空对话
  const clearConversation = () => {
    conversation.value = []
  }

  return {
    conversation,
    studyPlan,
    loading,
    config,
    exampleQuestions,
    fetchConfig,
    askQuestion,
    fetchStudyPlan,
    fetchRecommendedCourses,
    generateImage,
    clearConversation,
  }
})
