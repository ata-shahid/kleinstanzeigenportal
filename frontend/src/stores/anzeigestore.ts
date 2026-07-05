import { reactive } from 'vue'
import { defineStore } from 'pinia'
import { Client } from '@stomp/stompjs'
import type { IAnzeigeDTD } from './IAnzeige'
import type { IFrontendNachrichtEvent } from '@/services/IFrontendNachrichtEvent'
import { useInfo } from '@/composables/useInfo'

export const useAnzeigeStore = defineStore('anzeigestore', () => {
  const { setzeInfo } = useInfo()

  const anzeigedata = reactive<{ ok: boolean; anzeigeliste: IAnzeigeDTD[] }>({
    ok: false,
    anzeigeliste: [],
  })

  // Only one STOMP client instance is ever created
  let stompClient: Client | null = null

  async function updateAnzeigeListe() {
    console.log('anzeigestore: updateAnzeigeListe() – lade Daten vom Backend...')
    try {
      const response = await fetch('/api/anzeige')
      if (!response.ok) {
        console.error('anzeigestore: Fehler beim Laden der Anzeigeliste:', response.statusText)
        anzeigedata.anzeigeliste = []
        anzeigedata.ok = false
        setzeInfo(`Fehler beim Laden der Anzeigen: ${response.statusText}`)
        return
      }
      const data: IAnzeigeDTD[] = await response.json()
      anzeigedata.anzeigeliste = data
      anzeigedata.ok = true
      console.log(`anzeigestore: ${data.length} Anzeigen geladen.`)

      // Only start live updates after a successful fetch
      startAnzeigeLiveUpdate()
    } catch (err) {
      console.error('anzeigestore: Netzwerkfehler beim Laden der Anzeigeliste:', err)
      anzeigedata.anzeigeliste = []
      anzeigedata.ok = false
      setzeInfo('Fehler beim Laden der Anzeigen: Backend nicht erreichbar.')
    }
  }

  function startAnzeigeLiveUpdate() {
    // Guard: do not create multiple STOMP connections
    if (stompClient !== null) {
      console.log('anzeigestore: STOMP-Client bereits aktiv, kein neuer wird erstellt.')
      return
    }

    console.log('anzeigestore: Starte STOMP-Live-Update-Verbindung...')
    stompClient = new Client({
      brokerURL: `ws://${window.location.host}/stompbroker`,
      onConnect: () => {
        console.log('anzeigestore: STOMP verbunden.')
        stompClient!.subscribe('/topic/anzeige', (message) => {
          const event: IFrontendNachrichtEvent = JSON.parse(message.body)
          console.log('anzeigestore: STOMP-Nachricht empfangen:', JSON.stringify(event))
          if (event.typ === 'ANZEIGE') {
            console.log(`anzeigestore: Anzeige-Event (${event.op}) für ID ${event.id} – lade Liste neu.`)
            updateAnzeigeListe()
          }
        })
      },
      onStompError: (frame) => {
        console.error('anzeigestore: STOMP-Fehler:', frame.headers['message'])
        setzeInfo(`STOMP-Fehler: ${frame.headers['message']}`)
      },
      onWebSocketError: (event) => {
        console.error('anzeigestore: WebSocket-Fehler:', event)
        setzeInfo('WebSocket-Verbindung zum Server fehlgeschlagen.')
      },
    })

    stompClient.activate()
  }

  return { anzeigedata, updateAnzeigeListe, startAnzeigeLiveUpdate }
})
