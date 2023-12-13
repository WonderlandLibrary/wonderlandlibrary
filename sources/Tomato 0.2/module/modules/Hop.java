/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.events.MoveEvent;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.MathUtils;
import cow.milkgod.cheese.utils.Wrapper;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class Hop
extends Module {
    private double speed = 0.07999999821186066;
    private int stage;
    private boolean disabling;
    private boolean stopMotionUntilNext;
    private double moveSpeed;
    private boolean spedUp;
    public static boolean canStep;
    private double lastDist;
    public static double yOffset;
    private boolean cancel;

    public Hop() {
        super("Hop", 0, Category.MOVEMENT, 3342540, true, "Better TTF Chat", new String[]{"bounce"});
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
    public void onPreMotion(MoveEvent event) {
        Timer.timerSpeed = 1.0f;
        if (MathUtils.round(Wrapper.mc.thePlayer.posY - (double)((int)Wrapper.mc.thePlayer.posY), 3) == MathUtils.round(0.138, 3)) {
            EntityPlayerSP thePlayer = Wrapper.mc.thePlayer;
            thePlayer.motionY -= 0.08;
            event.y -= 0.09316090325960147;
            EntityPlayerSP thePlayer2 = Wrapper.mc.thePlayer;
            thePlayer2.posY -= 0.09316090325960147;
        }
        if (this.stage == 1 && (Wrapper.mc.thePlayer.moveForward != 0.0f || Wrapper.mc.thePlayer.moveStrafing != 0.0f)) {
            this.stage = 2;
            this.moveSpeed = 1.38 * this.getBaseMoveSpeed() - 0.01;
        } else if (this.stage == 2) {
            this.stage = 3;
            Wrapper.mc.thePlayer.motionY = 0.399399995803833;
            event.y = 0.399399995803833;
            this.moveSpeed *= 2.149;
        } else if (this.stage == 3) {
            this.stage = 4;
            double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
            this.moveSpeed = this.lastDist - difference;
        } else {
            if (Wrapper.mc.theWorld.getCollidingBoundingBoxes(Wrapper.mc.thePlayer, Wrapper.mc.thePlayer.boundingBox.offset(0.0, Wrapper.mc.thePlayer.motionY, 0.0)).size() > 0 || Wrapper.mc.thePlayer.isCollidedVertically) {
                this.stage = 1;
            }
            this.moveSpeed = this.lastDist - this.lastDist / 159.0;
        }
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
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdates pre) {
        String currentMode = "";
        double xDist = Wrapper.mc.thePlayer.posX - Wrapper.mc.thePlayer.prevPosX;
        double zDist = Wrapper.mc.thePlayer.posZ - Wrapper.mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Wrapper.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Wrapper.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
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
}

