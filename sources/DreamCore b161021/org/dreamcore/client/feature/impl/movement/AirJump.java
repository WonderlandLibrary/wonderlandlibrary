package org.dreamcore.client.feature.impl.movement;

import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;

public class AirJump extends Feature {

    public AirJump() {
        super("AirJump", "Позволяет прыгать по воздуху", Type.Movement);
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion event) {
        if (mc.gameSettings.keyBindJump.pressed) {
            mc.player.onGround = true;
        }
    }
}
