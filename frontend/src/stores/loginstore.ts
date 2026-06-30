import { reactive } from 'vue'
import { defineStore } from 'pinia'

export const useLoginStore = defineStore('loginstore', () => {
  const loginState = reactive<{ username: string; loggedIn: boolean }>({
    username: '',
    loggedIn: false,
  })

  function logout() {
    loginState.username = ''
    loginState.loggedIn = false
  }

  function login(username: string, passwort: string) {
    if (username !== '' && passwort !== '') {
      loginState.loggedIn = true
      loginState.username = username
    } else {
      logout()
    }
  }

  return { loginState, login, logout }
})
