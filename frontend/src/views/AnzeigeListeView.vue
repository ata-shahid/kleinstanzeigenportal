<template>
  <div>
    <div>
      <h2 class="title">Unsere aktuellen Anzeigen</h2>
    </div>
    <div>
      <AnzeigeListe :anzeigen="liste"></AnzeigeListe>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import AnzeigeListe from '@/components/anzeige/AnzeigeListe.vue'

export interface IAnzeigeDTD {
  id: number
  titel: string
  beschreibung: string
  preis: number
  anzahl: number
  verfuegbar: number
  ablaufdatum: string
  anbieterName: string
  anbieterAdresse: string
}

const liste = ref<IAnzeigeDTD[]>(
  JSON.parse(`
[
  {
    "id": 1,
    "titel": "Orangenhammer",
    "beschreibung": "Ein Hammer, der Orangen zerschmettert, und das gründlich.",
    "preis": 10,
    "anzahl": 20,
    "verfuegbar": 17,
    "ablaufdatum": "2026-08-17",
    "anbieterName": "Joghurta Biffel",
    "anbieterAdresse": "An der Ecke, Vollradisroda"
  },
  {
    "id": 2,
    "titel": "Bananenbrecher",
    "beschreibung": "Ein Gerät, das Bananen in Scheiben schneidet, ideal für Obstsalate.",
    "preis": 15,
    "anzahl": 30,
    "verfuegbar": 25,
    "ablaufdatum": "2026-09-01",
    "anbieterName": "Trubert vom Senkel",
    "anbieterAdresse": "Obstweg 5, Fruchtstadt"
  },
  {
    "id": 3,
    "titel": "Apfelzertrümmerer",
    "beschreibung": "Ein robustes Werkzeug, das Äpfel in Stücke zerschmettert, perfekt für Apfelmus.",
    "preis": 20,
    "anzahl": 10,
    "verfuegbar": 8,
    "ablaufdatum": "2026-10-15",
    "anbieterName": "Jöndhard Biffel",
    "anbieterAdresse": "Kernstraße 12, Apfelhausen"
  },
  {
    "id": 4,
    "titel": "Traubenzerquetscher",
    "beschreibung": "Ein praktisches Gerät, das Trauben zerquetscht, ideal für die Weinherstellung.",
    "preis": 25,
    "anzahl": 15,
    "verfuegbar": 12,
    "ablaufdatum": "2026-11-30",
    "anbieterName": "Kees van Bommelding",
    "anbieterAdresse": "Weinstraße 8, Traubendorf"
  }
]
`),
)
</script>

<style scoped>
.title {
  font-size: xx-large;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 0.5rem;
}
</style>
