import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  server: {
    proxy: {
      // 课程服务代理（原有配置）
      '/course': {
        target: 'http://localhost:8082',
        changeOrigin: true,
        secure: false,
      },
      // 新增：AI功能接口代理（8083端口）
      '/api/ai': {
        target: 'http://localhost:8083',
        changeOrigin: true,
        secure: false,
        // 可选：若后端接口路径无需重写，可省略rewrite；若有路径前缀差异可在这里调整
        // rewrite: (path) => path.replace(/^\/api\/ai/, '/api/ai')
      }
    },
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
})
