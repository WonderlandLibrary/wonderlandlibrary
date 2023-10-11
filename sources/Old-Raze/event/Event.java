package markgg.event;


public class Event {
    private boolean isCancelled;

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
    
    public void cancel() {
        isCancelled = true;
    }
}
