/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.motion.EventSafeWalk;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;

public class SafeWalk
extends Feature {
    public SafeWalk() {
        super("SafeWalk", "\u041d\u0435 \u0434\u0430\u0435\u0442 \u0443\u043f\u0430\u0441\u0442\u044c \u0432\u0430\u043c \u0441 \u0431\u043b\u043e\u043a\u0430", Type.Player);
    }

    @EventTarget
    public void onSafeWalk(EventSafeWalk event) {
        if (SafeWalk.mc.player == null || SafeWalk.mc.world == null) {
            return;
        }
        event.setCancelled(SafeWalk.mc.player.onGround);
    }
}

