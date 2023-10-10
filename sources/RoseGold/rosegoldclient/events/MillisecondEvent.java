package rosegoldclient.events;

import net.minecraftforge.fml.common.eventhandler.Event;

import java.time.LocalDateTime;

public class MillisecondEvent extends Event {
    public LocalDateTime dateTime;

    public MillisecondEvent() {
        dateTime = LocalDateTime.now();
    }
}
