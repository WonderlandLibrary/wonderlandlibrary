package cow.milkgod.cheese.events;

import com.darkmagician6.eventapi.events.*;

public class EventRenderChat implements Event
{
    private String text;
    
    public EventRenderChat(final String text) {
        this.text = text;
    }
    
    public String getString() {
        return this.text;
    }
}
