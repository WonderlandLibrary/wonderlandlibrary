package koks.event;

import koks.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kroko
 * @created on 10.12.2020 : 15:03
 */

@AllArgsConstructor @Getter
public class Render3DEvent extends Event {
    float renderPartialTicks;
}
