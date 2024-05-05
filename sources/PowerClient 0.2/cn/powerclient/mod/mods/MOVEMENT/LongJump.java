/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import me.AveReborn.Value;
import me.AveReborn.events.EventMove;
import me.AveReborn.events.EventUpdate;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;

public class LongJump
extends Mod {
    int jumps;
    private double speed;
    private int stage;
    private double moveSpeed;
    private double lastDist;
    private int airTicks;
    private int groundTicks;
    public Value<String> mode = new Value("LongJump", "Mode", 0);

    public LongJump() {
        super("LongJump", Category.MOVEMENT);
        this.mode.mode.add("Hypixel");
    }

    public void damagePlayer() {
        int index = 0;
        while (index < 60) {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.06, Minecraft.thePlayer.posZ, false));
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, false));
            ++index;
        }
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.1, Minecraft.thePlayer.posZ, false));
    }

    public double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd2 = new BigDecimal(value);
        bd2 = bd2.setScale(places, RoundingMode.HALF_UP);
        return bd2.doubleValue();
    }

    @Override
    public void onEnable() {
        if (this.mode.isCurrentMode("Hypixel")) {
            this.moveSpeed = PlayerUtil.getBaseMoveSpeed();
            this.speed = 1.0;
            this.groundTicks = 8;
            Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 0.001, Minecraft.thePlayer.posZ);
            Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.001, Minecraft.thePlayer.posZ);
            this.stage = 0;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        double xDist = Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX;
        double zDist = Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    @EventTarget
    public void onMove(EventMove event) {
        if (this.mode.isCurrentMode("Hypixel")) {
            this.setDisplayName("Hypixel");
            if (!Minecraft.thePlayer.onGround) {
                this.moveSpeed = PlayerUtil.getBaseMoveSpeed() / 2.0;
            }
            MovementInput movementInput = Minecraft.thePlayer.movementInput;
            float forward = Minecraft.thePlayer.movementInput.moveForward;
            float strafe = Minecraft.thePlayer.movementInput.moveStrafe;
            float yaw = Minecraft.thePlayer.rotationYaw;
            if (this.stage == 1 && (Minecraft.thePlayer.moveForward != 0.0f || Minecraft.thePlayer.moveStrafing != 0.0f)) {
                this.stage = 2;
                this.moveSpeed = (1.38 * PlayerUtil.getBaseMoveSpeed() - 0.01) / 1.6;
            } else if (this.stage == 2) {
                this.stage = 3;
                event.x = 0.0;
                event.z = 0.0;
                Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 0.001, Minecraft.thePlayer.posZ);
                Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.001, Minecraft.thePlayer.posZ);
                Minecraft.thePlayer.motionY = 0.423;
                event.y = 0.423;
                this.moveSpeed *= 2.149;
            } else if (this.stage == 3) {
                this.stage = 4;
                double difference = 0.66 * (this.lastDist - PlayerUtil.getBaseMoveSpeed());
                this.moveSpeed = (this.lastDist - difference) * 1.95;
            } else {
                if (this.mc.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.boundingBox.offset(0.0, Minecraft.thePlayer.motionY, 0.0)).size() > 0 || Minecraft.thePlayer.isCollidedVertically) {
                    this.stage = 1;
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                if (Minecraft.thePlayer.motionY < 0.1) {
                    Minecraft.thePlayer.motionY -= 0.005;
                }
            }
            this.moveSpeed = Math.max(this.moveSpeed, PlayerUtil.getBaseMoveSpeed());
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
            event.x = (double)forward * this.moveSpeed * mx2 + (double)strafe * this.moveSpeed * mz2;
            event.z = (double)forward * this.moveSpeed * mz2 - (double)strafe * this.moveSpeed * mx2;
            if ((double)Minecraft.thePlayer.fallDistance > 1.0) {
                event.x = 0.0;
                event.z = 0.0;
            }
        }
    }
}

