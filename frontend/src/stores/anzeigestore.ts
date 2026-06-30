import { reactive } from 'vue'
import { defineStore } from 'pinia'
import type { IAnzeigeDTD } from './IAnzeige'

export const useAnzeigeStore = defineStore('anzeigestore', () => {
  const anzeigedata = reactive<{ ok: boolean; anzeigeliste: IAnzeigeDTD[] }>({
    ok: false,
    anzeigeliste: [],
  })

  function updateAnzeigeListe() {
    anzeigedata.anzeigeliste = [
      {
        id: 1,
        titel: 'Orangenhammer',
        beschreibung: 'Ein Hammer, der Orangen zerschmettert, und das gründlich.',
        preis: 10,
        anzahl: 20,
        verfuegbar: 17,
        ablaufdatum: '2026-08-17',
        anbieterName: 'Joghurta Biffel',
        anbieterAdresse: 'An der Ecke, Vollradisroda',
      },
      {
        id: 2,
        titel: 'Bananenbrecher',
        beschreibung: 'Ein Gerät, das Bananen in Scheiben schneidet, ideal für Obstsalate.',
        preis: 15,
        anzahl: 30,
        verfuegbar: 25,
        ablaufdatum: '2026-09-01',
        anbieterName: 'Trubert vom Senkel',
        anbieterAdresse: 'Obstweg 5, Fruchtstadt',
      },
      {
        id: 3,
        titel: 'Apfelzertrümmerer',
        beschreibung:
          'Ein robustes Werkzeug, das Äpfel in Stücke zerschmettert, perfekt für Apfelmus.',
        preis: 20,
        anzahl: 10,
        verfuegbar: 8,
        ablaufdatum: '2026-10-15',
        anbieterName: 'Jöndhard Biffel',
        anbieterAdresse: 'Kernstraße 12, Apfelhausen',
      },
      {
        id: 4,
        titel: 'Traubenzerquetscher',
        beschreibung:
          'Ein praktisches Gerät, das Trauben zerquetscht, ideal für die Weinherstellung.',
        preis: 25,
        anzahl: 15,
        verfuegbar: 12,
        ablaufdatum: '2026-11-30',
        anbieterName: 'Kees van Bommelding',
        anbieterAdresse: 'Weinstraße 8, Traubendorf',
      },
    ]
    anzeigedata.ok = true
  }

  return { anzeigedata, updateAnzeigeListe }
})
