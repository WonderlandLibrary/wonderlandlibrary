package today.flux.addon.api.event.events;

import lombok.Getter;
import lombok.Setter;
import today.flux.addon.api.event.AddonEvent;

public class Event3DRender extends AddonEvent {
    @Getter @Setter
    private float partialTicks;
}
