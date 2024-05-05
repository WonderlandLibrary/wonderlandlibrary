/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.AveReborn.Value;
import me.AveReborn.events.EventMove;
import me.AveReborn.events.EventPreMotion;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class Speed
extends Mod {
    private int tick;
    public Value<String> mode = new Value("Speed", "Mode", 0);
    private int stage;
    private double moveSpeed;
    private double lastDist;

    public Speed() {
        super("Speed", Category.MOVEMENT);
        this.mode.mode.add("AAC3314");
        this.mode.mode.add("Hypixel");
        this.mode.mode.add("AAC339");
        this.mode.mode.add("NCP");
    }

    @Override
    public void onDisable() {
        Minecraft.thePlayer.motionX *= 1.0;
        Minecraft.thePlayer.motionY *= 1.0;
        Minecraft.thePlayer.motionZ *= 1.0;
        this.mc.timer.timerSpeed = 1.0f;
        Minecraft.thePlayer.speedInAir = 0.02f;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        Minecraft.thePlayer.motionX *= 0.0;
        Minecraft.thePlayer.motionZ *= 0.0;
        super.onEnable();
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2583000063896179;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.1 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public int getRandom(int cap) {
        Random rng = new Random();
        return rng.nextInt(cap);
    }

    public double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd2 = new BigDecimal(value);
        bd2 = bd2.setScale(places, RoundingMode.HALF_UP);
        return bd2.doubleValue();
    }

    public boolean isBlockUnder(Material blockMaterial) {
        if (this.mc.theWorld.getBlockState(this.underPlayer()).getBlock().getMaterial() == blockMaterial) {
            return true;
        }
        return false;
    }

    public BlockPos underPlayer() {
        return new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.getEntityBoundingBox().minY - 1.0, Minecraft.thePlayer.posZ);
    }

    @EventTarget
    public void onMove(EventMove event) {
        if (this.mode.isCurrentMode("Hypixel")) {
            if (this.round(Minecraft.thePlayer.posY - (double)((int)Minecraft.thePlayer.posY), 3) == this.round(0.138, 3)) {
                Minecraft.thePlayer.motionY -= 1.0;
                event.y -= 0.0931;
                Minecraft.thePlayer.posY -= 0.0931;
                this.moveSpeed *= 1.0;
            }
            if (this.stage != 2 || Minecraft.thePlayer.moveForward == 0.0f && Minecraft.thePlayer.moveStrafing == 0.0f) {
                if (this.stage == 3) {
                    this.moveSpeed = this.getBaseMoveSpeed() * 1.509999475479126;
                } else {
                    if (this.mc.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.boundingBox.offset(0.0, Minecraft.thePlayer.motionY, 0.0)).size() > 0 || Minecraft.thePlayer.isCollidedVertically) {
                        this.stage = 1;
                    }
                    this.moveSpeed = this.getBaseMoveSpeed() * 1.00000011920929;
                    this.moveSpeed = this.lastDist - this.lastDist / 160.0;
                }
            }
            this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
            float forward = Minecraft.thePlayer.movementInput.moveForward;
            float strafe = Minecraft.thePlayer.movementInput.moveStrafe;
            float yaw = Minecraft.thePlayer.rotationYaw;
            if (forward == 0.0f && strafe == 0.0f) {
                Minecraft.thePlayer.motionX *= (Minecraft.thePlayer.motionZ *= 0.0);
                event.x = 0.0;
                event.z = 0.0;
            } else if (forward != 0.0f) {
                if (strafe >= 1.0f) {
                    yaw += forward > 0.0f ? -45.0f : 45.0f;
                    strafe = 0.0f;
                } else if (strafe <= -1.0f) {
                    yaw += forward > 0.0f ? 45.0f : -45.0f;
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
            event.x = ((double)forward * this.moveSpeed * mx2 + (double)strafe * this.moveSpeed * mz2) * 0.99;
            event.z = ((double)forward * this.moveSpeed * mz2 - (double)strafe * this.moveSpeed * mx2) * 0.99;
            if (forward == 0.0f && strafe == 0.0f) {
                event.x = 0.0;
                event.z = 0.0;
                Minecraft.thePlayer.motionX *= (Minecraft.thePlayer.motionZ *= 0.0);
            } else if (forward != 0.0f) {
                if (strafe >= 1.0f) {
                    yaw += forward > 0.0f ? -45.0f : 45.0f;
                    strafe = 0.0f;
                } else if (strafe <= -1.0f) {
                    yaw += forward > 0.0f ? 45.0f : -45.0f;
                    strafe = 0.0f;
                }
                if (forward > 0.0f) {
                    forward = 1.0f;
                } else if (forward < 0.0f) {
                    forward = -1.0f;
                }
            }
            ++this.stage;
        }
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (this.mode.isCurrentMode("Hypixel")) {
            this.setDisplayName("Hypixel");
            if (PlayerUtil.MovementInput()) {
                double[] xd2 = new double[]{0.368, 0.55, 0.249, 0.171, 0.427};
                if (Minecraft.thePlayer.onGround) {
                    Minecraft.thePlayer.jump();
                    this.moveSpeed *= 1.0;
                    this.mc.timer.timerSpeed = 1.0f + (float)xd2[this.getRandom(xd2.length - 1)];
                } else {
                    this.mc.timer.timerSpeed = Minecraft.thePlayer.motionY <= -0.009999999776482582 && !this.isBlockUnder(Material.air) ? 1.0f : 1.0f;
                }
            }
            double xDist = Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX;
            double zDist = Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
        if (this.mode.isCurrentMode("NCP")) {
            this.setDisplayName("NCP");
            if (Minecraft.thePlayer.isInWater()) {
                return;
            }
            if (Minecraft.thePlayer.onGround && PlayerUtil.MovementInput()) {
                Minecraft.thePlayer.jump();
                Minecraft.thePlayer.motionX *= (double)(1.09f + (new Random().nextBoolean() ? 8.0E-4f : 5.0E-4f));
                Minecraft.thePlayer.motionZ *= (double)(1.09f + (new Random().nextBoolean() ? 8.0E-4f : 5.0E-4f));
            } else if (!Minecraft.thePlayer.onGround && PlayerUtil.MovementInput()) {
                PlayerUtil.setSpeed(PlayerUtil.getBaseMoveSpeed() - 0.05);
                Minecraft.thePlayer.jumpMovementFactor = 0.0265f;
            }
        }
        if (this.mode.isCurrentMode("AAC3314")) {
            this.setDisplayName("AAC3.3.14");
            if (PlayerUtil.MovementInput()) {
                if (Minecraft.thePlayer.onGround) {
                    Minecraft.thePlayer.jump();
                } else {
                    Minecraft.thePlayer.motionY -= 0.0139;
                    Minecraft.thePlayer.motionX *= 1.01;
                    Minecraft.thePlayer.motionZ *= 1.01;
                    Minecraft.thePlayer.jumpMovementFactor = 0.025f;
                    Minecraft.thePlayer.speedInAir = 0.222f;
                }
            }
        }
        if (this.mode.isCurrentMode("AAC339")) {
            this.setDisplayName("AAC3.3.9");
            if (PlayerUtil.MovementInput()) {
                if (Minecraft.thePlayer.hurtTime == 0) {
                    if (Minecraft.thePlayer.onGround) {
                        Minecraft.thePlayer.jump();
                        Minecraft.thePlayer.motionY = 0.3875;
                    } else {
                        Minecraft.thePlayer.motionY -= 0.0145;
                    }
                    PlayerUtil.toFwd(0.00149);
                }
            } else {
                boolean tick = false;
                this.tick = 0;
                double n4 = 0.0;
                Minecraft.thePlayer.motionZ = 0.0;
                Minecraft.thePlayer.motionX = 0.0;
            }
        }
    }
}

