/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.specials.speed;

import net.minecraft.client.Minecraft;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.MotionUpdateEvent;
import tk.rektsky.event.impl.UpdateSprintingEvent;
import tk.rektsky.module.impl.movement.Speed;
import tk.rektsky.utils.MovementUtil;

public class VanillaBhop {
    public static int tick = 0;
    public static Minecraft mc = Minecraft.getMinecraft();
    private static double jumpY = 0.0;

    public static void onEvent(Speed module, Event event) {
        if (event instanceof UpdateSprintingEvent) {
            ((UpdateSprintingEvent)event).setSprinting(false);
        }
        if (event instanceof MotionUpdateEvent) {
            ++tick;
            if (VanillaBhop.mc.thePlayer.onGround && MovementUtil.isMoving()) {
                MovementUtil.strafe(module.vanillaSpeed.getValue());
                VanillaBhop.mc.thePlayer.jump();
                jumpY = VanillaBhop.mc.thePlayer.posY;
            }
            if (MovementUtil.isMoving()) {
                MovementUtil.strafe(module.vanillaSpeed.getValue());
            } else {
                VanillaBhop.mc.thePlayer.motionX = 0.0;
                VanillaBhop.mc.thePlayer.motionZ = 0.0;
            }
        }
    }

    public static void onEnable(Speed module) {
        VanillaBhop.mc.timer.timerSpeed = 1.0;
        tick = 0;
    }

    public static void onDisable(Speed module) {
        VanillaBhop.mc.timer.timerSpeed = 1.0;
        tick = 0;
    }
}

