package events.listeners;

import events.Event;

public class EventPostRender3D
extends Event {
    private float partialTicks;

    public EventPostRender3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EventPostRender3D)) {
            return false;
        }
        EventPostRender3D other = (EventPostRender3D)((Object)o);
        if (!other.canEqual((Object)this)) {
            return false;
        }
        return Float.compare((float)this.getPartialTicks(), (float)other.getPartialTicks()) == 0;
    }

    protected boolean canEqual(Object other) {
        return other instanceof EventPostRender3D;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + Float.floatToIntBits((float)this.getPartialTicks());
        return result;
    }

    public String toString() {
        return "EventPostRender3D(partialTicks=" + this.getPartialTicks() + ")";
    }
}
