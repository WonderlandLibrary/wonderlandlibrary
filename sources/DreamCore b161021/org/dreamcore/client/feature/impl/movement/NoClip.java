package org.dreamcore.client.feature.impl.movement;

import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventFullCube;
import org.dreamcore.client.event.events.impl.player.EventPush;
import org.dreamcore.client.event.events.impl.player.EventUpdateLiving;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.player.MovementHelper;
import org.dreamcore.client.settings.impl.NumberSetting;

public class NoClip extends Feature {

    public static NumberSetting speed;

    public NoClip() {
        super("No Clip", "Позволяет ходить сквозь стены", Type.Movement);
        speed = new NumberSetting("Speed", 0.02F, 0F, 2, 0.01F, () -> true);
        addSettings(speed);
    }

    @EventTarget
    public void onFullCube(EventFullCube event) {
        if (mc.world != null) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onPush(EventPush event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onLivingUpdate(EventUpdateLiving event) {
        if (mc.player != null) {
            mc.player.noClip = true;
            mc.player.motionY = 0;
            mc.player.onGround = false;
            mc.player.capabilities.isFlying = false;
            MovementHelper.setSpeed(speed.getNumberValue() == 0 ? MovementHelper.getBaseMoveSpeed() : speed.getNumberValue());
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY += 0.5;
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY -= 0.5;
            }
            event.setCancelled(true);
        }
    }
}
