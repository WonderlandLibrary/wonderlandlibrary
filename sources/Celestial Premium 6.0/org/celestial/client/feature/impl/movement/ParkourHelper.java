/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;

public class ParkourHelper
extends Feature {
    public ParkourHelper() {
        super("ParkourHelper", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043f\u0440\u044b\u0433\u0430\u0435\u0442 \u043d\u0430 \u043a\u043e\u043d\u0446\u0435 \u0431\u043b\u043e\u043a\u0430", Type.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (ParkourHelper.mc.world.getCollisionBoxes(ParkourHelper.mc.player, ParkourHelper.mc.player.getEntityBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001)).isEmpty() && ParkourHelper.mc.player.onGround && !ParkourHelper.mc.gameSettings.keyBindJump.isKeyDown()) {
            ParkourHelper.mc.player.jump();
        }
    }
}

