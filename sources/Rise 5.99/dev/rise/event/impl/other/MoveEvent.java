package dev.rise.event.impl.other;

import dev.rise.event.api.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class MoveEvent extends Event {
    private double x, y, z;
}
