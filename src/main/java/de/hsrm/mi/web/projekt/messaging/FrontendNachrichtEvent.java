package de.hsrm.mi.web.projekt.messaging;

public class FrontendNachrichtEvent {

    public enum EventTyp {
        ANZEIGE
    }

    public enum EventOperation {
        CREATE, UPDATE, DELETE
    }

    private final EventTyp typ;
    private final long id;
    private final EventOperation op;

    public FrontendNachrichtEvent(EventTyp typ, long id, EventOperation op) {
        this.typ = typ;
        this.id = id;
        this.op = op;
    }

    public EventTyp getTyp() {
        return typ;
    }

    public long getId() {
        return id;
    }

    public EventOperation getOp() {
        return op;
    }

    @Override
    public String toString() {
        return "FrontendNachrichtEvent{typ=" + typ + ", id=" + id + ", op=" + op + "}";
    }
}
