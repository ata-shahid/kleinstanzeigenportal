<template>
  <tr>
    <td>{{ anzeige.id }}</td>
    <td>{{ anzeige.titel }}</td>
    <td>{{ anzeige.preis }}</td>
    <td>{{ anzeige.verfuegbar }} von {{ anzeige.anzahl }}</td>
    <td>{{ anzeige.ablaufdatum }}</td>
    <td><button @click="offen = !offen">+</button></td>
  </tr>
  <tr v-if="offen">
    <td colspan="6" class="details">
      <strong>{{ anzeige.anbieterName }}</strong><br />
      {{ anzeige.anbieterAdresse }}
      <a :href="karteUrl" target="_blank">[Karte]</a><br />
      {{ anzeige.beschreibung }}
      <div class="bestellungs-buttons">
        <!-- "bestellen" only shown when at least one item is available -->
        <button
          v-if="anzeige.verfuegbar > 0"
          class="bestellen-btn"
          @click="bestellen"
        >
          bestellen
        </button>
        <!-- "stornieren" only shown when at least one item has been ordered (verfuegbar < anzahl) -->
        <button
          v-if="anzeige.verfuegbar < anzeige.anzahl"
          class="stornieren-btn"
          @click="stornieren"
        >
          stornieren
        </button>
      </div>
    </td>
  </tr>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import type { IAnzeigeDTD } from '@/stores/IAnzeige'
import { useLoginStore } from '@/stores/loginstore'
import { useInfo } from '@/composables/useInfo'

const props = defineProps<{ anzeige: IAnzeigeDTD }>()

const { loginState } = useLoginStore()
const { setzeInfo } = useInfo()

const offen = ref(false)

const karteUrl = computed(
  () =>
    'https://nominatim.openstreetmap.org/ui/search.html?q=' +
    encodeURI(props.anzeige.anbieterAdresse),
)

async function bestellen() {
  try {
    const response = await fetch(`/api/anzeige/${props.anzeige.id}/bestellen`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ loginName: loginState.username }),
    })
    if (!response.ok) {
      const msg = await response.text()
      setzeInfo(`Bestellung fehlgeschlagen: ${msg}`)
    }
    // Do NOT manually update the store here.
    // The STOMP event will trigger updateAnzeigeListe() for all frontends.
  } catch {
    setzeInfo('Fehler beim Bestellen: Backend nicht erreichbar.')
  }
}

async function stornieren() {
  try {
    const response = await fetch(
      `/api/anzeige/${props.anzeige.id}/bestellen/${loginState.username}`,
      { method: 'DELETE' },
    )
    if (!response.ok) {
      const msg = await response.text()
      setzeInfo(`Stornierung fehlgeschlagen: ${msg}`)
    }
    // Do NOT manually update the store here.
    // The STOMP event will trigger updateAnzeigeListe() for all frontends.
  } catch {
    setzeInfo('Fehler beim Stornieren: Backend nicht erreichbar.')
  }
}
</script>

<style scoped>
td {
  text-align: center;
}

.details {
  text-align: left;
  background-color: #f0f4ff;
  padding: 0.6rem 1rem;
}

button {
  cursor: pointer;
  font-weight: bold;
}

.bestellungs-buttons {
  margin-top: 0.6rem;
  display: flex;
  gap: 0.5rem;
}

.bestellen-btn {
  background-color: #22c55e;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 0.3rem 0.8rem;
  cursor: pointer;
  font-size: 0.9rem;
}

.bestellen-btn:hover {
  background-color: #16a34a;
}

.stornieren-btn {
  background-color: #ef4444;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 0.3rem 0.8rem;
  cursor: pointer;
  font-size: 0.9rem;
}

.stornieren-btn:hover {
  background-color: #dc2626;
}
</style>
