package dev.rise.event.impl.render;

import dev.rise.event.api.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class Render3DEvent extends Event {
    private final float partialTicks;
}
