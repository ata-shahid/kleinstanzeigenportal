<script setup lang="ts">
import { ref } from 'vue'

const info = ref('Beachten Sie unseren aktuellen Anzeigen mit Sonderangeboten zur Ball-WM')
</script>

<template>
  <header class="kopf">
    <div class="kopf-container">
      <div class="brandname-container">
        <h1 class="company-name">Kleinstanzeigen</h1>
      </div>
      <div class="navbar">
        <div class="nav-links">
          <a class="benutzer-link" href="/admin/benutzer">Benutzer</a>
          <a class="benutzer-link" href="/admin/anzeige">Anzeige</a>
        </div>
      </div>
    </div>
  </header>

  <div v-if="info" class="infobox">
    <div>
      <strong>Info</strong>
      <p>{{ info }}</p>
    </div>
    <button v-on:click="info = ''" aria-label="Meldung schließen">✕</button>
  </div>

  <main class="main-container">
    <div></div>
  </main>

  <footer class="fuss">
    <div class="fuss-container">
      <p class="fuss-text">&copy; 2026 Webbasierte Anwendungen. All rights reserved.</p>
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
</style>
