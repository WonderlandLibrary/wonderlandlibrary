package events.listeners;

import events.Event;

public class EventGlowESP
extends Event {
    private final Runnable runnable;
    public boolean cancelled;

    public Runnable getRunnable() {
        return this.runnable;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EventGlowESP)) {
            return false;
        }
        EventGlowESP other = (EventGlowESP)((Object)o);
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.isCancelled() != other.isCancelled()) {
            return false;
        }
        Runnable this$runnable = this.getRunnable();
        Runnable other$runnable = other.getRunnable();
        return !(this$runnable == null ? other$runnable != null : !this$runnable.equals((Object)other$runnable));
    }

    protected boolean canEqual(Object other) {
        return other instanceof EventGlowESP;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isCancelled() ? 79 : 97);
        Runnable $runnable = this.getRunnable();
        result = result * 59 + ($runnable == null ? 43 : $runnable.hashCode());
        return result;
    }

    public String toString() {
        return "EventGlowESP(runnable=" + this.getRunnable() + ", cancelled=" + this.isCancelled() + ")";
    }

    public EventGlowESP(Runnable runnable) {
        this.runnable = runnable;
    }
}
