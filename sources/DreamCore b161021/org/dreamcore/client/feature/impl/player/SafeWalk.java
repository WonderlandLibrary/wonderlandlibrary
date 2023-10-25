package org.dreamcore.client.feature.impl.player;

import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.motion.EventSafeWalk;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;

public class SafeWalk extends Feature {

    public SafeWalk() {
        super("SafeWalk", "Не дает упасть вам с блока", Type.Player);
    }

    @EventTarget
    public void onSafeWalk(EventSafeWalk event) {
        if (mc.player == null || mc.world == null)
            return;
        event.setCancelled(mc.player.onGround);
    }
}
