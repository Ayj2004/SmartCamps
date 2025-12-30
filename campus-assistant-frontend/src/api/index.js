import axios from 'axios'

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:8083', // AI服务地址
  timeout: 30000, // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 可以在这里添加token等
    return config
  },
  (error) => {
    console.log('请求错误:', error)
    return Promise.reject(error)
  },
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    console.log('响应错误:', error)
    // 补充错误信息提示
    let errorMsg = '服务连接失败，请检查后端服务是否启动！'
    if (error.message.includes('timeout')) {
      errorMsg = '请求超时，请稍后重试！'
    } else if (error.message.includes('Network Error')) {
      errorMsg = '网络异常，请检查网络连接！'
    } else if (error.response) {
      errorMsg = `服务错误：${error.response.status} - ${error.response.statusText}`
    }
    // 抛出友好的错误信息
    return Promise.reject(new Error(errorMsg))
  },
)

export default service
