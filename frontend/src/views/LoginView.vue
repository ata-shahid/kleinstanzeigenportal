<template>
  <div class="login-wrapper">
    <div class="login-card">
      <h2 class="login-title">Anmelden</h2>

      <form class="login-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username-input">Benutzername</label>
          <input
            id="username-input"
            v-model="username"
            type="text"
            placeholder="Benutzername eingeben"
            autocomplete="username"
          />
        </div>

        <div class="form-group">
          <label for="passwort-input">Passwort</label>
          <input
            id="passwort-input"
            v-model="passwort"
            type="password"
            placeholder="Passwort eingeben"
            autocomplete="current-password"
          />
        </div>

        <button id="login-btn" type="submit" class="login-btn">Einloggen</button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useLoginStore } from '@/stores/loginstore'
import { useInfo } from '@/composables/useInfo'

const router = useRouter()
const loginStore = useLoginStore()
const { loginState, login, logout } = loginStore
const { setzeInfo, loescheInfo } = useInfo()

const username = ref('')
const passwort = ref('')

onMounted(() => {
  logout()
})

function handleLogin() {
  login(username.value, passwort.value)

  if (!loginState.loggedIn) {
    passwort.value = ''
    setzeInfo('Jammer - der Login-Versuch war zwar nicht erfolgreich, aber erfolglos')
  } else {
    loescheInfo()
    router.push('/anzeige')
  }
}
</script>

<style scoped>
.login-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
  padding: 2rem;
}

.login-card {
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 2.5rem 2rem;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.login-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 1.5rem;
  text-align: center;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
}

.form-group label {
  font-size: 0.9rem;
  font-weight: 600;
  color: #374151;
}

.form-group input {
  padding: 0.5rem 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 4px;
  font-size: 0.95rem;
  outline: none;
  transition: border-color 0.2s;
}

.form-group input:focus {
  border-color: #3b82f6;
}

.login-btn {
  margin-top: 0.5rem;
  padding: 0.6rem;
  background-color: #2563eb;
  color: #ffffff;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s;
}

.login-btn:hover {
  background-color: #1d4ed8;
}
</style>
