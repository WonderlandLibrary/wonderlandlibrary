/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.NumberSetting;

public class ElytraFlight
extends Feature {
    public static NumberSetting motion;

    public ElytraFlight() {
        super("ElytraFlight", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043b\u0435\u0442\u0430\u0442\u044c \u043d\u0430 \u044d\u043b\u0438\u0442\u0440\u0430\u0445 \u0431\u0435\u0437 \u0444\u0435\u0439\u0435\u0440\u0432\u0435\u0440\u043a\u043e\u0432", Type.Movement);
        motion = new NumberSetting("Elytra Speed", 1.5f, 0.5f, 5.0f, 0.5f, () -> true);
        this.addSettings(motion);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (ElytraFlight.mc.player.isElytraFlying()) {
            ElytraFlight.mc.player.setVelocity(0.0, 0.0, 0.0);
            if (ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                ElytraFlight.mc.player.motionY = -motion.getCurrentValue();
            }
            if (ElytraFlight.mc.gameSettings.keyBindJump.isKeyDown()) {
                ElytraFlight.mc.player.motionY = motion.getCurrentValue();
            }
            if (MovementHelper.isMoving()) {
                MovementHelper.setSpeed(motion.getCurrentValue());
            }
        }
    }

    @Override
    public void onDisable() {
        ElytraFlight.mc.player.capabilities.isFlying = false;
        ElytraFlight.mc.player.capabilities.setFlySpeed(0.05f);
        if (!ElytraFlight.mc.player.capabilities.isCreativeMode) {
            ElytraFlight.mc.player.capabilities.allowFlying = false;
        }
        super.onDisable();
    }
}

