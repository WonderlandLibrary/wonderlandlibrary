package rip.helium.event.minecraft;

import net.minecraft.entity.Entity;
import rip.helium.event.CancellableEvent;

public class RenderPlayerNameEvent extends CancellableEvent {
    public Entity p;

    public RenderPlayerNameEvent(final Entity p2) {
        this.p = p2;
    }

}

