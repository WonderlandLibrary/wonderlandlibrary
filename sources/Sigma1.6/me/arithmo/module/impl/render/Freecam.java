/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package me.arithmo.module.impl.render;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventBlockBounds;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.event.impl.EventMove;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.event.impl.EventPushBlock;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;

public class Freecam
extends Module {
    public static final String SPEED = "SPEED";
    private EntityOtherPlayerMP freecamEntity;

    public Freecam(ModuleData data) {
        super(data);
        this.settings.put("SPEED", new Setting<Double>("SPEED", 1.0, "Movement speed.", 1.0, 1.0, 10.0));
    }

    @Override
    public void onDisable() {
        Freecam.mc.thePlayer.setPositionAndRotation(this.freecamEntity.posX, this.freecamEntity.posY, this.freecamEntity.posZ, this.freecamEntity.rotationYaw, this.freecamEntity.rotationPitch);
        Freecam.mc.theWorld.removeEntityFromWorld(this.freecamEntity.getEntityId());
        Freecam.mc.renderGlobal.loadRenderers();
        Freecam.mc.thePlayer.noClip = false;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        if (Freecam.mc.thePlayer == null) {
            return;
        }
        this.freecamEntity = new EntityOtherPlayerMP(Freecam.mc.theWorld, new GameProfile(new UUID(69, 96), "XDDD"));
        this.freecamEntity.inventory = Freecam.mc.thePlayer.inventory;
        this.freecamEntity.inventoryContainer = Freecam.mc.thePlayer.inventoryContainer;
        this.freecamEntity.setPositionAndRotation(Freecam.mc.thePlayer.posX, Freecam.mc.thePlayer.posY, Freecam.mc.thePlayer.posZ, Freecam.mc.thePlayer.rotationYaw, Freecam.mc.thePlayer.rotationPitch);
        this.freecamEntity.rotationYawHead = Freecam.mc.thePlayer.rotationYawHead;
        Freecam.mc.theWorld.addEntityToWorld(this.freecamEntity.getEntityId(), this.freecamEntity);
        Freecam.mc.renderGlobal.loadRenderers();
        super.onEnable();
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    @RegisterEvent(events={EventMotion.class, EventPacket.class, EventBlockBounds.class, EventMove.class, EventPushBlock.class})
    public void onEvent(Event event) {
        EventPacket ep;
        float speed = ((Number)((Setting)this.settings.get("SPEED")).getValue()).floatValue();
        if (event instanceof EventMotion) {
            EventMotion em = (EventMotion)event;
            Freecam.mc.thePlayer.noClip = true;
        }
        if (event instanceof EventPacket && (ep = (EventPacket)event).isOutgoing()) {
            C03PacketPlayer packet;
            if (ep.getPacket() instanceof C03PacketPlayer) {
                packet = (C03PacketPlayer)ep.getPacket();
                packet.yaw = this.freecamEntity.rotationYaw;
                packet.pitch = this.freecamEntity.rotationPitch;
                packet.x = this.freecamEntity.posX;
                packet.y = this.freecamEntity.posY;
                packet.z = this.freecamEntity.posZ;
                packet.onGround = this.freecamEntity.onGround;
            } else if (ep.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                packet = (C03PacketPlayer.C04PacketPlayerPosition)ep.getPacket();
                packet.yaw = this.freecamEntity.rotationYaw;
                packet.pitch = this.freecamEntity.rotationPitch;
                packet.x = this.freecamEntity.posX;
                packet.y = this.freecamEntity.posY;
                packet.z = this.freecamEntity.posZ;
                packet.onGround = this.freecamEntity.onGround;
            }
        }
        if (event instanceof EventBlockBounds) {
            EventBlockBounds ebb = (EventBlockBounds)event;
            ebb.setCancelled(true);
        }
        if (event instanceof EventMove) {
            EventMove em = (EventMove)event;
            if (Freecam.mc.thePlayer.movementInput.jump) {
                Freecam.mc.thePlayer.motionY = speed;
                em.setY(Freecam.mc.thePlayer.motionY);
            } else if (Freecam.mc.thePlayer.movementInput.sneak) {
                Freecam.mc.thePlayer.motionY = - speed;
                em.setY(Freecam.mc.thePlayer.motionY);
            } else {
                Freecam.mc.thePlayer.motionY = 0.0;
                em.setY(0.0);
            }
            speed = (float)Math.max((double)speed, Freecam.getBaseMoveSpeed());
            double forward = Freecam.mc.thePlayer.movementInput.moveForward;
            double strafe = Freecam.mc.thePlayer.movementInput.moveStrafe;
            float yaw = Freecam.mc.thePlayer.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                em.setX(0.0);
                em.setZ(0.0);
            } else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        strafe = 1.0;
                        yaw += (float)(forward > 0.0 ? -45 : 45);
                    } else if (strafe < 0.0) {
                        yaw += (float)(forward > 0.0 ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    } else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                em.setX(forward * (double)speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * (double)speed * Math.sin(Math.toRadians(yaw + 90.0f)));
                em.setZ(forward * (double)speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * (double)speed * Math.cos(Math.toRadians(yaw + 90.0f)));
            }
        }
        if (event instanceof EventPushBlock) {
            EventPushBlock ebp = (EventPushBlock)event;
            ebp.setCancelled(true);
        }
    }
}

