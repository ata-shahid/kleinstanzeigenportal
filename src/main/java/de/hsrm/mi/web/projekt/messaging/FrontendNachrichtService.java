package de.hsrm.mi.web.projekt.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Service
public class FrontendNachrichtService {

    private static final Logger logger = LoggerFactory.getLogger(FrontendNachrichtService.class);

    private final SimpMessagingTemplate messagingTemplate;

    public FrontendNachrichtService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendEvent(FrontendNachrichtEvent ev) {
        logger.info("FrontendNachrichtService: sende STOMP-Event -> {}", ev);
        messagingTemplate.convertAndSend("/topic/anzeige", ev);
        logger.info("FrontendNachrichtService: STOMP-Event gesendet an /topic/anzeige: {}", ev);
    }
}
