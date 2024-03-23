package xyz.northclient.features.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.northclient.features.Event;

@AllArgsConstructor
@Getter
public class Render3DEvent extends Event {
    private float ticks;
}
