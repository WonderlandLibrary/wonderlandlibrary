package wtf.diablo.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Event {
    private boolean canceled;
    private EventType type;

    public void setCanceled(){
        this.canceled = true;
    }

    public void setCanceled(boolean value){
        this.canceled = value;
    }

    public void setType(EventType type){
        this.type = type;
    }

    public boolean isCanceled(){
        return canceled;
    }

    public EventType getType(){
        return type;
    }
}
