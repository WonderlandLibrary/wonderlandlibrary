package rosegoldclient.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class KeybindEvent extends Event {
    public int keyCode;

    public KeybindEvent(int keyCode) {
        this.keyCode = keyCode;
    }
}
