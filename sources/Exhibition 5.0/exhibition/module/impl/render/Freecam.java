// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.render;

import java.util.HashMap;
import exhibition.event.RegisterEvent;
import exhibition.event.impl.EventPushBlock;
import exhibition.event.impl.EventMove;
import exhibition.event.impl.EventBlockBounds;
import net.minecraft.network.play.client.C03PacketPlayer;
import exhibition.event.impl.EventPacket;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import net.minecraft.potion.Potion;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import exhibition.module.Module;

public class Freecam extends Module
{
    public static final String SPEED = "SPEED";
    private EntityOtherPlayerMP freecamEntity;
    
    public Freecam(final ModuleData data) {
        super(data);
        ((HashMap<String, Setting<Double>>)this.settings).put("SPEED", new Setting<Double>("SPEED", 1.0, "Movement speed."));
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
        this.freecamEntity = new EntityOtherPlayerMP(Freecam.mc.theWorld, new GameProfile(new UUID(69L, 96L), "XDDD"));
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
            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    @RegisterEvent(events = { EventMotion.class, EventPacket.class, EventBlockBounds.class, EventMove.class, EventPushBlock.class })
    @Override
    public void onEvent(final Event event) {
        float speed = ((HashMap<K, Setting<Number>>)this.settings).get("SPEED").getValue().floatValue();
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            Freecam.mc.thePlayer.noClip = true;
        }
        if (event instanceof EventPacket) {
            final EventPacket ep = (EventPacket)event;
            if (ep.isOutgoing()) {
                if (ep.getPacket() instanceof C03PacketPlayer) {
                    final C03PacketPlayer packet = (C03PacketPlayer)ep.getPacket();
                    packet.yaw = this.freecamEntity.rotationYaw;
                    packet.pitch = this.freecamEntity.rotationPitch;
                    packet.x = this.freecamEntity.posX;
                    packet.y = this.freecamEntity.posY;
                    packet.z = this.freecamEntity.posZ;
                    packet.onGround = this.freecamEntity.onGround;
                }
                else if (ep.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                    final C03PacketPlayer.C04PacketPlayerPosition packet2 = (C03PacketPlayer.C04PacketPlayerPosition)ep.getPacket();
                    packet2.yaw = this.freecamEntity.rotationYaw;
                    packet2.pitch = this.freecamEntity.rotationPitch;
                    packet2.x = this.freecamEntity.posX;
                    packet2.y = this.freecamEntity.posY;
                    packet2.z = this.freecamEntity.posZ;
                    packet2.onGround = this.freecamEntity.onGround;
                }
            }
        }
        if (event instanceof EventBlockBounds) {
            final EventBlockBounds ebb = (EventBlockBounds)event;
            ebb.setCancelled(true);
        }
        if (event instanceof EventMove) {
            final EventMove em2 = (EventMove)event;
            if (Freecam.mc.thePlayer.movementInput.jump) {
                em2.setY(Freecam.mc.thePlayer.motionY = speed);
            }
            else if (Freecam.mc.thePlayer.movementInput.sneak) {
                em2.setY(Freecam.mc.thePlayer.motionY = -speed);
            }
            else {
                em2.setY(Freecam.mc.thePlayer.motionY = 0.0);
            }
            speed = (float)Math.max(speed, getBaseMoveSpeed());
            double forward = Freecam.mc.thePlayer.movementInput.moveForward;
            double strafe = Freecam.mc.thePlayer.movementInput.moveStrafe;
            float yaw = Freecam.mc.thePlayer.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                em2.setX(0.0);
                em2.setZ(0.0);
            }
            else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        strafe = 1.0;
                        yaw += ((forward > 0.0) ? -45 : 45);
                    }
                    else if (strafe < 0.0) {
                        yaw += ((forward > 0.0) ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    }
                    else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                em2.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
                em2.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
            }
        }
        if (event instanceof EventPushBlock) {
            final EventPushBlock ebp = (EventPushBlock)event;
            ebp.setCancelled(true);
        }
    }
}
