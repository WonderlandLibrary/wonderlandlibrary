package org.dreamcore.client.feature.impl.movement;

import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.player.MovementHelper;
import org.dreamcore.client.settings.impl.NumberSetting;

public class ElytraFlight extends Feature {

    public static NumberSetting motion;

    public ElytraFlight() {
        super("ElytraFlight", "Позволяет летать на элитрах без фейерверков", Type.Movement);
        motion = new NumberSetting("Elytra Speed", 1.5F, 0.5F, 5F, 0.5F, () -> true);
        addSettings(motion);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.isElytraFlying()) {
            mc.player.onGround = false;
            mc.player.setVelocity(0.0, 0.0, 0.0);
            if (mc.gameSettings.keyBindSneak.isKeyDown())
                mc.player.motionY = -motion.getNumberValue();
            if (mc.gameSettings.keyBindJump.isKeyDown())
                mc.player.motionY = motion.getNumberValue();
            if (MovementHelper.isMoving()) {
                MovementHelper.setSpeed(motion.getNumberValue());
            }
        }
    }

    @Override
    public void onDisable() {
        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.setFlySpeed(0.05f);
        if (!mc.player.capabilities.isCreativeMode) {
            mc.player.capabilities.allowFlying = false;
        }
        super.onDisable();
    }
}