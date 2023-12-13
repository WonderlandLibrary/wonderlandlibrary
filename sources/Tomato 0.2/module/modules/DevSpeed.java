/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.events.MoveEvent;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.ModuleManager;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class DevSpeed
extends Module {
    private static float speed;
    private double speed1;
    private int stage;
    private boolean disabling;
    private boolean stopMotionUntilNext;
    private double moveSpeed;
    private boolean spedUp;
    public static boolean canStep;
    private double lastDist;
    public static double yOffset;
    private boolean cancel;

    public DevSpeed() {
        super("DevSpeed", 0, Category.MOVEMENT, 3342540, true, "AirMove", new String[]{"dspeed"});
        speed = 0.08f;
    }

    @Override
    public void onEnable() {
        Timer.timerSpeed = 1.0f;
        this.cancel = false;
        this.stage = 1;
        double d2 = this.moveSpeed = Wrapper.mc.thePlayer == null ? 0.2873 : this.getBaseMoveSpeed();
        if (!this.disabling) {
            super.onEnable();
        }
    }

    @EventTarget
    public void onPreUpdate(MoveEvent event) {
        if (!this.getState()) {
            return;
        }
        if (Wrapper.mc.thePlayer.isAirBorne) {
            Cheese.getInstance();
            if (!Cheese.moduleManager.getModState("TickHop")) {
                Timer.timerSpeed = 1.3f;
                this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
                MovementInput movementInput = Wrapper.mc.thePlayer.movementInput;
                float forward = movementInput.moveForward;
                float strafe = movementInput.moveStrafe;
                float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
                if (forward == 0.0f && strafe == 0.0f) {
                    event.x = 0.0;
                    event.z = 0.0;
                } else if (forward != 0.0f) {
                    if (strafe >= 1.0f) {
                        yaw += (float)(forward > 0.0f ? -45 : 45);
                        strafe = 0.0f;
                    } else if (strafe <= -1.0f) {
                        yaw += (float)(forward > 0.0f ? 45 : -45);
                        strafe = 0.0f;
                    }
                    if (forward > 0.0f) {
                        forward = 1.0f;
                    } else if (forward < 0.0f) {
                        forward = -1.0f;
                    }
                }
                double mx2 = Math.cos(Math.toRadians(yaw + 90.0f));
                double mz2 = Math.sin(Math.toRadians(yaw + 90.0f));
                double motionX = (double)forward * this.moveSpeed * mx2 + (double)strafe * this.moveSpeed * mz2;
                double motionZ = (double)forward * this.moveSpeed * mz2 - (double)strafe * this.moveSpeed * mx2;
                event.x = (double)forward * this.moveSpeed * mx2 + (double)strafe * this.moveSpeed * mz2;
                event.z = (double)forward * this.moveSpeed * mz2 - (double)strafe * this.moveSpeed * mx2;
                Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            }
        } else {
            Wrapper.mc.thePlayer.jumpMovementFactor = 0.02f;
            Timer.timerSpeed = 1.0f;
            Wrapper.mc.thePlayer.speedInAir = 0.02f;
        }
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdates pre) {
        String currentMode = "";
        double xDist = Wrapper.mc.thePlayer.posX - Wrapper.mc.thePlayer.prevPosX;
        double zDist = Wrapper.mc.thePlayer.posZ - Wrapper.mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    @Override
    public void onDisable() {
        Timer.timerSpeed = 1.0f;
        this.moveSpeed = this.getBaseMoveSpeed();
        yOffset = 0.0;
        this.stage = 0;
        this.disabling = false;
        super.onDisable();
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Wrapper.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Wrapper.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }
}

