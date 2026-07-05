import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(), vueDevTools()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    proxy: {
      // Diese URI-Pfade werden vom Frontend-Dev-Server an das Spring-Backend durchgeleitet
      '/api': 'http://localhost:8080',
      '/admin': 'http://localhost:8080',
      '/stompbroker': {
        target: 'http://localhost:8080/',
        ws: true,
      },
    },
  },
})
