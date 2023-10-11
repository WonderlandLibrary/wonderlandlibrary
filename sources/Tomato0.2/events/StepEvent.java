package cow.milkgod.cheese.events;

import com.darkmagician6.eventapi.events.*;

public class StepEvent implements Event
{
    public double stepHeight;
    public boolean bypass;
    
    public StepEvent(final double stepHeight) {
        this.stepHeight = stepHeight;
    }
}
