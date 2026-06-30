<script setup lang="ts">
import { RouterView, RouterLink } from 'vue-router'
import { useInfo } from '@/composables/useInfo'
import { useLoginStore } from '@/stores/loginstore'

const { info, loescheInfo } = useInfo()
const { loginState } = useLoginStore()
</script>

<template>
  <header class="kopf">
    <div class="kopf-container">
      <div class="brandname-container">
        <h1 class="company-name">Kleinstanzeigen</h1>
      </div>
      <div class="navbar">
        <div class="nav-links">
          <RouterLink v-if="loginState.loggedIn" class="benutzer-link" to="/anzeige">
            Anzeigen
          </RouterLink>
          <RouterLink class="benutzer-link benutzer-link--login" to="/login">Login</RouterLink>
        </div>
      </div>
    </div>
  </header>

  <div v-if="info" class="infobox">
    <div>
      <strong>Info</strong>
      <p>{{ info }}</p>
    </div>
    <button aria-label="Meldung schließen" @click="loescheInfo()">✕</button>
  </div>

  <main class="main-container">
    <RouterView />
  </main>

  <footer class="fuss">
    <div class="fuss-container">
      <p class="fuss-text">
        © 2026 Webbasierte Anwendungen. All rights reserved.
        <span v-if="loginState.username" class="fuss-user">
          Eingeloggt als: <strong>{{ loginState.username }}</strong>
        </span>
      </p>
    </div>
  </footer>
</template>

<style scoped>
.infobox {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-left: 25%;
  margin-right: 25%;
  padding: 0.6rem 0.6rem 0.6rem 20rem;

  background: #dbeafe;
  border: 1px solid #93c5fd;
  border-radius: 4px;
  color: #1e40af;
}

.infobox p {
  margin: 0;
  font-size: 0.9rem;
}

.infobox button {
  background: none;
  border: none;
  cursor: pointer;
  color: black;
  font-size: 1rem;
  font-weight: bold;
}

.main-container {
  background-color: transparent;
}

.nav-links {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.benutzer-link--login {
  margin-left: auto;
}

.fuss-user {
  margin-left: 1rem;
  font-size: 0.9rem;
}
</style>
