package client.module.impl.movement.noslow;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.SlowDownEvent;
import client.module.impl.movement.NoSlow;
import client.value.Mode;

public class VanillaNoSlow extends Mode<NoSlow> {

    public VanillaNoSlow(final String name, final NoSlow parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = event -> event.setCancelled(true);
}
