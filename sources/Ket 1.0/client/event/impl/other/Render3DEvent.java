package client.event.impl.other;

import client.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class Render3DEvent implements Event {
    private final float partialTicks;
}
