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
    </td>
  </tr>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import type { IAnzeigeDTD } from '@/views/AnzeigeListeView.vue'

const props = defineProps<{ anzeige: IAnzeigeDTD }>()

const offen = ref(false)

const karteUrl = computed(
  () =>
    'https://nominatim.openstreetmap.org/ui/search.html?q=' +
    encodeURI(props.anzeige.anbieterAdresse),
)
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
</style>
