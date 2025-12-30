import request from './index'

/**
 * AI问答
 * @param {Object} data - 请求参数
 * @param {string} data.studentId - 学号
 * @param {string} data.question - 问题内容
 * @returns {Promise<string>} 问答结果
 */
export function askQuestion(data) {
  return request.post('/api/ai/ask', data)
}

/**
 * 生成学习计划
 * @param {Object} params - 请求参数
 * @param {string} params.major - 专业名称
 * @param {string} params.studentId - 学号
 * @returns {Promise<StudyPlan>} 学习计划
 */
export function generateStudyPlan(params) {
  return request.get('/api/ai/study-plan', { params })
}

/**
 * 生成学习图像
 * 优化：改用表单参数传递（更符合POST语义），也可保留URL参数（根据后端实际支持）
 * @param {string} theme - 图像主题
 * @returns {Promise<string>} 生成结果
 */
export function generateLearningImage(theme) {
  // 方案1：表单参数（推荐，适配POST传参）
  const formData = new FormData();
  formData.append('theme', theme);
  return request.post('/api/ai/generate-image', formData);

  // 方案2：保留URL参数（兼容现有后端）
  // return request.post('/api/ai/generate-image', null, { params: { theme } });
}

/**
 * 推荐课程
 * @param {string} interests - 兴趣关键词
 * @returns {Promise<Array<string>>} 课程列表
 */
export function recommendCourses(interests) {
  return request.get('/api/ai/recommend-courses', {
    params: { interests }
  })
}

/**
 * 获取配置信息
 * @returns {Promise<Object>} 配置数据
 */
export function getConfig() {
  return request.get('/api/ai/config')
}

/**
 * 健康检查
 * @returns {Promise<Object>} 健康状态
 */
export function checkHealth() {
  return request.get('/api/ai/health')
}
