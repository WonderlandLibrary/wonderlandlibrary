package me.dev.legacy.event.events;

public class EventCancellable extends EventStageable {

    private boolean canceled;

    public EventCancellable() {
    }

    public EventCancellable(EventStage stage) {
        super(stage);
    }

    public EventCancellable(EventStage stage, boolean canceled) {
        super(stage);
        this.canceled = canceled;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
