package today.flux.addon.api.event.events;

import lombok.Getter;
import lombok.Setter;
import today.flux.addon.api.event.AddonEvent;

public class Event2DRender extends AddonEvent {
    @Getter @Setter
    private float particalTicks;
}
