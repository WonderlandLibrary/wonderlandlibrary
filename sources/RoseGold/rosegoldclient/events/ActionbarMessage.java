package rosegoldclient.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ActionbarMessage extends Event {

    public String message;

    public ActionbarMessage(String message) {
        this.message = message;
    }
}
