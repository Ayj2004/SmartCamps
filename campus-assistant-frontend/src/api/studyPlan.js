// 引入项目已封装的axios实例（需确保utils/request.js存在基础配置）
import request from '@/utils/request'

/**
 * 获取学习计划列表
 * @param {Object} params 查询参数（如userId，需与后端接口入参一致）
 * @returns {Promise<Result>} 后端Result格式响应
 */
export const getStudyPlanList = (params) => {
  return request({
    url: '/study-plan/list', // 需与后端学习计划列表接口路径完全一致
    method: 'get',
    params
  })
}

/**
 * 新增学习计划
 * @param {Object} data 计划表单数据（planName/planContent/planTime等，与后端入参一致）
 * @returns {Promise<Result>} 后端Result格式响应
 */
export const addStudyPlan = (data) => {
  return request({
    url: '/study-plan/add', // 需与后端新增接口路径一致
    method: 'post',
    data
  })
}

/**
 * 修改学习计划
 * @param {Object} data 含planId的修改数据
 * @returns {Promise<Result>} 后端Result格式响应
 */
export const updateStudyPlan = (data) => {
  return request({
    url: '/study-plan/update', // 需与后端修改接口路径一致
    method: 'put',
    data
  })
}

/**
 * 删除学习计划
 * @param {String/Number} planId 计划ID
 * @returns {Promise<Result>} 后端Result格式响应
 */
export const deleteStudyPlan = (planId) => {
  return request({
    url: `/study-plan/delete/${planId}`, // 需与后端删除接口路径一致
    method: 'delete'
  })
}
