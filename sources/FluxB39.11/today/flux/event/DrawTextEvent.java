package today.flux.event;

import com.darkmagician6.eventapi.events.Event;

/**
 * Created by John on 2016/12/16.
 */
public class DrawTextEvent implements Event {

    public String text;

    public DrawTextEvent(String text) {
        this.text = text;
    }
}