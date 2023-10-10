package vestige.event.impl;


import lombok.AllArgsConstructor;
import lombok.Getter;
import vestige.event.Event;

@Getter
@AllArgsConstructor
public class RenderEvent extends Event {

    private float partialTicks;

}
