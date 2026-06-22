<template>
  <tr>
    <td>{{ anzeige.id }}</td>
    <td>{{ anzeige.titel }}</td>
    <td>{{ anzeige.preis }}</td>
    <td>{{ anzeige.verfuegbar }} von {{ anzeige.anzahl }}</td>
    <td>{{ anzeige.ablaufdatum }}</td>
  </tr>
</template>

<script setup lang="ts">
import type { IAnzeigeDTD } from '@/views/AnzeigeListeView.vue'

defineProps<{ anzeige: IAnzeigeDTD }>()
</script>

<style scoped>
td {
  text-align: center;
}
</style>
