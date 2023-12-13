// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.player;

import com.elementars.eclient.event.Event;
import com.elementars.eclient.event.events.PlayerMoveEvent;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import com.elementars.eclient.event.events.EventReceivePacket;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import com.elementars.eclient.event.events.EventSendPacket;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.client.entity.EntityPlayerSP;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import com.elementars.eclient.module.Module;

public class Freecam extends Module
{
    private double posX;
    private double posY;
    private double posZ;
    private float pitch;
    private float yaw;
    private double startPosX;
    private double startPosY;
    private double startPosZ;
    private float startPitch;
    private float startYaw;
    private EntityOtherPlayerMP clonedPlayer;
    private boolean isRidingEntity;
    private Entity ridingEntity;
    private final Value<Boolean> cancelPackets;
    private final Value<Integer> speed;
    private final Value<Integer> vspeed;
    
    public Freecam() {
        super("Freecam", "Allows you to fly out of your body", 0, Category.PLAYER, true);
        this.cancelPackets = this.register(new Value<Boolean>("Cancel Packets", this, true));
        this.speed = this.register(new Value<Integer>("Speed", this, 11, 1, 20));
        this.vspeed = this.register(new Value<Integer>("V Speed", this, 7, 1, 20));
    }
    
    @Override
    public void onUpdate() {
        if (!Wrapper.getMinecraft().player.onGround) {
            Wrapper.getMinecraft().player.motionY = -0.2;
        }
        Wrapper.getMinecraft().player.onGround = true;
        Wrapper.getMinecraft().player.motionY = 0.0;
        Wrapper.getMinecraft().player.noClip = true;
        Wrapper.getMinecraft().player.capabilities.isFlying = true;
        Wrapper.getMinecraft().player.capabilities.setFlySpeed(this.speed.getValue() / 100.0f);
        Wrapper.getMinecraft().player.onGround = false;
        Wrapper.getMinecraft().player.fallDistance = 0.0f;
        if (Freecam.mc.gameSettings.keyBindJump.isKeyDown()) {
            final EntityPlayerSP player = Freecam.mc.player;
            player.motionY += this.vspeed.getValue() / 10.0f;
        }
        if (Freecam.mc.gameSettings.keyBindSneak.isKeyDown()) {
            final EntityPlayerSP player2 = Freecam.mc.player;
            player2.motionY += -this.vspeed.getValue() / 10.0f;
        }
    }
    
    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        if (Wrapper.getMinecraft().player != null) {
            this.isRidingEntity = (Wrapper.getMinecraft().player.getRidingEntity() != null);
            if (Wrapper.getMinecraft().player.getRidingEntity() == null) {
                this.posX = Wrapper.getMinecraft().player.posX;
                this.posY = Wrapper.getMinecraft().player.posY;
                this.posZ = Wrapper.getMinecraft().player.posZ;
            }
            else {
                this.ridingEntity = Wrapper.getMinecraft().player.getRidingEntity();
                Wrapper.getMinecraft().player.dismountRidingEntity();
            }
            this.pitch = Wrapper.getMinecraft().player.rotationPitch;
            this.yaw = Wrapper.getMinecraft().player.rotationYaw;
            (this.clonedPlayer = new EntityOtherPlayerMP((World)Wrapper.getMinecraft().world, Wrapper.getMinecraft().getSession().getProfile())).copyLocationAndAnglesFrom((Entity)Wrapper.getMinecraft().player);
            this.clonedPlayer.rotationYawHead = Wrapper.getMinecraft().player.rotationYawHead;
            Wrapper.getMinecraft().world.addEntityToWorld(-100, (Entity)this.clonedPlayer);
            Wrapper.getMinecraft().player.noClip = true;
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        if (Wrapper.getMinecraft().player != null) {
            Wrapper.getMinecraft().player.setPositionAndRotation(this.posX, this.posY, this.posZ, this.yaw, this.pitch);
            Wrapper.getMinecraft().world.removeEntityFromWorld(-100);
            this.clonedPlayer = null;
            final double posX = 0.0;
            this.posZ = posX;
            this.posY = posX;
            this.posX = posX;
            final float n = 0.0f;
            this.yaw = n;
            this.pitch = n;
            Freecam.mc.player.capabilities.isFlying = false;
            Wrapper.getMinecraft().player.capabilities.setFlySpeed(0.05f);
            Wrapper.getMinecraft().player.noClip = false;
            final EntityPlayerSP player = Wrapper.getMinecraft().player;
            final EntityPlayerSP player2 = Wrapper.getMinecraft().player;
            final EntityPlayerSP player3 = Wrapper.getMinecraft().player;
            final double motionX = 0.0;
            player3.motionZ = motionX;
            player2.motionY = motionX;
            player.motionX = motionX;
            if (this.isRidingEntity) {
                Wrapper.getMinecraft().player.startRiding(this.ridingEntity, true);
            }
        }
        Wrapper.getMinecraft().renderGlobal.loadRenderers();
        super.onDisable();
    }
    
    @EventTarget
    public void onPacketSent(final EventSendPacket event) {
        if (this.cancelPackets.getValue() && (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketInput)) {
            event.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onPacketRecived(final EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            this.startPosX = packet.getX();
            this.startPosY = packet.getY();
            this.startPosZ = packet.getZ();
            this.startPitch = packet.getPitch();
            this.startYaw = packet.getYaw();
        }
    }
    
    @SubscribeEvent
    public void onPush(final PlayerSPPushOutOfBlocksEvent event) {
        event.setCanceled(true);
    }
    
    private void playersSpeed(final double speed) {
        if (Wrapper.getMinecraft().player != null) {
            final MovementInput movementInput = Wrapper.getMinecraft().player.movementInput;
            double forward = movementInput.moveForward;
            double strafe = movementInput.moveStrafe;
            float yaw = Wrapper.getMinecraft().player.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                Wrapper.getMinecraft().player.motionX = 0.0;
                Wrapper.getMinecraft().player.motionZ = 0.0;
            }
            else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
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
                Wrapper.getMinecraft().player.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
                Wrapper.getMinecraft().player.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
            }
        }
    }
    
    @EventTarget
    public void onMove(final PlayerMoveEvent event) {
        if (event.getEventState() != Event.State.PRE) {
            return;
        }
        Freecam.mc.player.noClip = true;
    }
}
