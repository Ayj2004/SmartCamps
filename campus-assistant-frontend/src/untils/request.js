import axios from 'axios'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API || '/api', // 后端接口前缀（需在.env文件配置）
  timeout: 5000 // 请求超时时间
})

// 请求拦截器（可选，如添加token）
service.interceptors.request.use(
  config => {
    // 可添加请求头，如token
    // config.headers['Authorization'] = 'Bearer ' + localStorage.getItem('token')
    return config
  },
  error => {
    console.error('请求错误：', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 统一处理响应数据
    return response.data
  },
  error => {
    console.error('响应错误：', error)
    // 统一错误提示
    ElMessage.error(error.message || '请求失败')
    return Promise.reject(error)
  }
)

export default service
