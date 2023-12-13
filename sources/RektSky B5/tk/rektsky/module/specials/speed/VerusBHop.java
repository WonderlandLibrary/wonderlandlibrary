/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.specials.speed;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.MotionUpdateEvent;
import tk.rektsky.event.impl.PacketReceiveEvent;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.exploits.PingSpoof;
import tk.rektsky.module.impl.movement.Speed;
import tk.rektsky.utils.MovementUtil;

public class VerusBHop {
    public static int tick = 0;
    public static Minecraft mc = Minecraft.getMinecraft();
    private static double jumpY = 0.0;
    private static int bypassTicks = 0;

    public static void onEvent(Speed module, Event event) {
        if (event instanceof PacketReceiveEvent && ((PacketReceiveEvent)event).getPacket() instanceof S12PacketEntityVelocity && ModulesManager.getModuleByClass(Speed.class).boost.getValue().booleanValue() && ((S12PacketEntityVelocity)((PacketReceiveEvent)event).getPacket()).getEntityID() == VerusBHop.mc.thePlayer.getEntityId()) {
            PingSpoof moduleByClass = ModulesManager.getModuleByClass(PingSpoof.class);
            bypassTicks = 20 + (moduleByClass.isToggled() ? Math.min(40, moduleByClass.delay.getValue()) : 0);
        }
        if (event instanceof MotionUpdateEvent) {
            double speedBoost = 0.0;
            --bypassTicks;
            ++tick;
            if (VerusBHop.mc.thePlayer.onGround && MovementUtil.isMoving()) {
                MovementUtil.strafe(0.35);
                VerusBHop.mc.thePlayer.jump();
                jumpY = VerusBHop.mc.thePlayer.posY;
            }
            if (MovementUtil.isMoving()) {
                if ((double)VerusBHop.mc.thePlayer.fallDistance >= 1.5) {
                    if (bypassTicks > 0) {
                        MovementUtil.strafe(1.0);
                    } else {
                        MovementUtil.strafe(0.26);
                    }
                } else if (bypassTicks > 0) {
                    MovementUtil.strafe(1.0 + speedBoost);
                } else {
                    MovementUtil.strafe(0.33 + speedBoost);
                    if (VerusBHop.mc.thePlayer.posY - jumpY < 0.35) {
                        MovementUtil.strafe(0.5 + speedBoost);
                    }
                }
            } else {
                VerusBHop.mc.thePlayer.motionX = 0.0;
                VerusBHop.mc.thePlayer.motionZ = 0.0;
            }
        }
    }

    public static void onEnable(Speed module) {
        VerusBHop.mc.timer.timerSpeed = 1.0;
        bypassTicks = 0;
        tick = 0;
    }

    public static void onDisable(Speed module) {
        VerusBHop.mc.timer.timerSpeed = 1.0;
        tick = 0;
    }
}

