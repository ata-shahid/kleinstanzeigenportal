<template>
  <table>
    <thead>
      <tr>
        <th>ID</th>
        <th>Titel</th>
        <th>Preis</th>
        <th>Verfügbarkeit</th>
        <th>Ablaufdatum</th>
        <th></th>
      </tr>
    </thead>
    <tbody>
      <AnzeigeListeZeile v-for="anzeige in anzeigen" :key="anzeige.id" :anzeige="anzeige" />
    </tbody>
  </table>
</template>

<script setup lang="ts">
import type { IAnzeigeDTD } from '@/views/AnzeigeListeView.vue'
import AnzeigeListeZeile from './AnzeigeListeZeile.vue'

defineProps<{ anzeigen: IAnzeigeDTD[] }>()
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
  background-color: #ffffff;
}

th,
td {
  border: 1px solid #ccc;
  padding: 0.5rem 1rem;
  text-align: center;
  color: #2c3e50;
  background-color: #ffffff;
}

th {
  background-color: #f2f2f2;
  font-weight: 600;
}
</style>
