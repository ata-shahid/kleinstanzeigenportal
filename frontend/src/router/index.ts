import { createRouter, createWebHistory } from 'vue-router'
import AnzeigeListeView from '@/views/AnzeigeListeView.vue'
import LoginView from '@/views/LoginView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/anzeige',
    },
    {
      path: '/anzeige',
      component: AnzeigeListeView,
    },
    {
      path: '/login',
      component: LoginView,
    },
  ],
})

export default router
