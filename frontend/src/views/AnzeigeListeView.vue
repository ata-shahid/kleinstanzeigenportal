<template>
  <div>
    <div>
      <h2 class="title">Unsere aktuellen Anzeigen</h2>
    </div>
    <div class="suchleiste">
      <input
        id="suchfeld"
        v-model="suchstring"
        type="text"
        placeholder="Suche nach Titel oder Beschreibung …"
        class="suchfeld"
      />
      <button class="reset-btn" @click="suchstring = ''">Reset</button>
    </div>
    <div>
      <AnzeigeListe :anzeigen="gefilterteListe" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import AnzeigeListe from '@/components/anzeige/AnzeigeListe.vue'
import { useAnzeigeStore } from '@/stores/anzeigestore'

const anzeigeStore = useAnzeigeStore()

onMounted(() => {
  anzeigeStore.updateAnzeigeListe()
})

const suchstring = ref('')

const gefilterteListe = computed(() => {
  const term = suchstring.value.toLowerCase()
  if (term === '') {
    return anzeigeStore.anzeigedata.anzeigeliste
  }
  return anzeigeStore.anzeigedata.anzeigeliste.filter(
    (a) =>
      a.titel.toLowerCase().includes(term) || a.beschreibung.toLowerCase().includes(term),
  )
})
</script>

<style scoped>
.title {
  font-size: xx-large;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.suchleiste {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1rem;
  align-items: center;
}

.suchfeld {
  flex: 1;
  padding: 0.45rem 0.75rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 0.95rem;
  outline: none;
  transition: border-color 0.2s;
}

.suchfeld:focus {
  border-color: #3b82f6;
}

.reset-btn {
  padding: 0.45rem 1rem;
  background-color: #e5e7eb;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.2s;
}

.reset-btn:hover {
  background-color: #d1d5db;
}
</style>
