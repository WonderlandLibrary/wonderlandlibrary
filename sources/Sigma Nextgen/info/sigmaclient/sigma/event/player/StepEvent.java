package info.sigmaclient.sigma.event.player;

import info.sigmaclient.sigma.event.Event;
import lombok.Getter;

public class StepEvent extends Event {
    @Getter
    private boolean pre;
    public boolean isPost(){
        return !pre;
    }
    public StepEvent(boolean key){
        this.eventID = 9;
        this.pre = key;
    }
}
