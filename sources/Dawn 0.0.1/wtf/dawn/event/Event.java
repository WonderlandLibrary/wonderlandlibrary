package wtf.dawn.event;

import wtf.dawn.Dawn;
import wtf.dawn.module.Module;

public class Event<E extends Event> {
    private EventType type;
    private EventDirection direction;
    private boolean cancelled;
    public static void dispatch(Event<Event> e) {
        Dawn.getInstance().getModuleManager().getModules().stream().filter(Module::isEnabled).forEach(m -> m.onEvent(e));

    }

    public EventType getType() {
        return type;
    }
    public EventDirection getDirection() {
        return direction;
    }

    public void setType(EventType type) {
        this.type = type;
    }
    public void setDirection(EventDirection direction) {
        this.direction = direction;
    }
    public boolean isPre() {
        return type != null && type == EventType.PRE;
    }
    public boolean isPost() {
        return type != null && type == EventType.POST;
    }
    public boolean isIncoming() {
        return direction != null && direction == EventDirection.INCOMING;
    }
    public boolean isOutgoing() {
        return direction != null && direction == EventDirection.OUTGOING;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    public void cancel() {
        setCancelled(true);
    }
}
