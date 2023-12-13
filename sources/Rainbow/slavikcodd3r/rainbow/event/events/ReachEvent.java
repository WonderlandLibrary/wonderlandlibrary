package slavikcodd3r.rainbow.event.events;

import slavikcodd3r.rainbow.event.Event;

public class ReachEvent extends Event
{
    private float reach;
    
    public ReachEvent(final float reach) {
        this.reach = reach;
    }
    
    public float getReach() {
        return this.reach;
    }
    
    public void setReach(final float reach) {
        this.reach = reach;
    }
}
