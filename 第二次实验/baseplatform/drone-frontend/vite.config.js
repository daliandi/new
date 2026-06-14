// 导入 Vite 配置函数
import { defineConfig } from 'vite'
// 导入 Vue 插件，用于支持 .vue 文件
import vue from '@vitejs/plugin-vue'
// 导入 Node.js 路径处理模块
import { resolve } from 'path'

// 导出 Vite 配置对象
export default defineConfig({
  // 配置使用的插件
  plugins: [vue()],

  // 路径解析配置
  resolve: {
    // 配置路径别名，@ 指向项目根目录下的 src 文件夹
    alias: {
      '@': resolve(__dirname, 'src')  // 例如 @/api 等价于 ./src/api
    }
  },

  // 构建配置
  build: {
    outDir: 'dist',                    // 构建输出目录
    emptyOutDir: true,                 // 构建前清空输出目录
    rollupOptions: {                   // Rollup 打包配置
      input: {
        main: resolve(__dirname, 'index.html')  // 入口 HTML 文件
      }
    }
  },

  // 开发服务器配置
  server: {
    port: 5173,                        // 开发服务器端口
    host: '0.0.0.0',                   // 允许外部访问（局域网/外网）
    proxy: {                           // 代理配置，用于解决跨域问题
      // 将 /api 开头的请求代理到后端服务器
      '/api': {
        target: 'http://localhost:8082',  // 后端服务地址
        changeOrigin: true                 // 改变请求头中的 Origin 字段
      },
      // 将 /drones 开头的请求代理到后端服务器
      '/drones': {
        target: 'http://localhost:8082',  // 后端服务地址
        changeOrigin: true                 // 改变请求头中的 Origin 字段
      }
    }
  }
})