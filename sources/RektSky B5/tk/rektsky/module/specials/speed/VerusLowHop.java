/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.specials.speed;

import net.minecraft.client.Minecraft;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.MotionUpdateEvent;
import tk.rektsky.utils.MovementUtil;

public class VerusLowHop {
    public static void onDisable() {
        Minecraft.getMinecraft().thePlayer.stepHeight = 0.5f;
    }

    public static void onEnable() {
    }

    public static void onEvent(Event event) {
        if (event instanceof MotionUpdateEvent) {
            Minecraft.getMinecraft().thePlayer.stepHeight = 0.0f;
            if (!((MotionUpdateEvent)event).isPre()) {
                return;
            }
            Minecraft mc = Minecraft.getMinecraft();
            if (!(mc.thePlayer.isInWeb || mc.thePlayer.isInLava() || mc.thePlayer.isInWater() || mc.thePlayer.isOnLadder() || mc.thePlayer.ridingEntity != null || !MovementUtil.isMoving())) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    if (!mc.thePlayer.isCollidedHorizontally && !mc.gameSettings.keyBindJump.pressed) {
                        mc.thePlayer.motionY = 0.0;
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.41999998688698, mc.thePlayer.posZ);
                        mc.thePlayer.onGround = false;
                    }
                }
                MovementUtil.strafe(0.33);
            }
        }
    }
}

