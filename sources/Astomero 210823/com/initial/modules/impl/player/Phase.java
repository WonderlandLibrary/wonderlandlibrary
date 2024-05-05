package com.initial.modules.impl.player;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import net.minecraft.util.*;
import com.initial.events.*;
import com.initial.events.impl.*;
import com.initial.utils.movement.*;
import net.minecraft.network.play.client.*;
import com.initial.utils.network.*;
import net.minecraft.network.*;
import java.util.concurrent.*;

public class Phase extends Module
{
    public ModeSet mode;
    public DoubleSet watchdogPackets;
    public ModuleCategory subCategory;
    public DoubleSet offset;
    public BooleanSet motionY;
    public BooleanSet packet;
    public BooleanSet packetGround;
    public DoubleSet packetOffset;
    public DoubleSet packetYOffset;
    
    public Phase() {
        super("Phase", 0, Category.PLAYER);
        this.mode = new ModeSet("Mode", "Watchdog", new String[] { "Instant", "Watchdog", "Custom", "Redesky", "Clip" });
        this.watchdogPackets = new DoubleSet("Watchdog Packets", 10.0, 1.0, 20.0, 1.0);
        this.subCategory = new ModuleCategory("Custom...");
        this.offset = new DoubleSet("Offset", 0.204, 0.1, 0.5, 0.005);
        this.motionY = new BooleanSet("MotionY", false);
        this.packet = new BooleanSet("Packet", true);
        this.packetGround = new BooleanSet("Packet Ground", false);
        this.packetOffset = new DoubleSet("Packet Offset", 4.0, 0.0, 7.0, 0.1);
        this.packetYOffset = new DoubleSet("Packet Y Offset", 4.0E-4, 0.0, 0.1, 1.0E-4);
        this.addSettings(this.mode, this.watchdogPackets, this.subCategory);
        this.subCategory.addCatSettings(this.offset, this.motionY, this.packetOffset, this.packetGround, this.packetOffset, this.packetYOffset);
    }
    
    @EventTarget
    public void onCollide(final EventCollide e) {
        if (this.mode.getMode().equalsIgnoreCase("Clip")) {
            e.setBoundingBox(null);
        }
    }
    
    @EventTarget
    public void onUpdate(final EventMotion e) {
        final double x = this.mc.thePlayer.posX;
        final double y = this.mc.thePlayer.posY;
        final double z = this.mc.thePlayer.posZ;
        final double direction = MovementUtils.getDirection();
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Custom": {
                final double posX = -Math.sin(direction) * this.offset.getValue();
                final double posZ = Math.cos(direction) * this.offset.getValue();
                this.mc.thePlayer.onGround = true;
                this.mc.thePlayer.isCollidedVertically = true;
                this.mc.thePlayer.isCollided = true;
                if (this.motionY.enabled) {
                    this.mc.thePlayer.motionY = 0.0;
                }
                if (MovementUtils.isMoving()) {
                    this.mc.thePlayer.setEntityBoundingBox(this.mc.thePlayer.getEntityBoundingBox().offset(posX, 0.0, posZ));
                    this.mc.thePlayer.resetPositionToBB();
                }
                if (this.packet.enabled) {
                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x + posX * this.packetOffset.getValue(), y + this.packetYOffset.getValue(), z + posZ * this.packetOffset.getValue(), this.packetGround.enabled));
                    break;
                }
                break;
            }
            case "Watchdog": {
                final double posXdoggo = -Math.sin(direction) * 0.2;
                final double posZdoggo = Math.cos(direction) * 0.2;
                if (this.mc.thePlayer.isCollidedHorizontally && !this.mc.thePlayer.isOnLadder() && MovementUtils.isMoving()) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + posXdoggo, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + posZdoggo, true));
                    for (int i = 1; i < this.watchdogPackets.getValue(); ++i) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, ThreadLocalRandom.current().nextDouble(161.0, 200.0), this.mc.thePlayer.posZ, true));
                    }
                    this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + posXdoggo, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + posZdoggo);
                    break;
                }
                break;
            }
            case "Instant": {
                this.mc.thePlayer.onGround = true;
                final double editX = -Math.sin(direction) * 0.6973181;
                final double editZ = Math.cos(direction) * 0.6973181;
                if (this.mc.thePlayer.isCollidedHorizontally && !this.mc.thePlayer.isOnLadder() && MovementUtils.isMoving()) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + editX * 2.0, this.mc.thePlayer.posY + 4.0E-4, this.mc.thePlayer.posZ + editZ * 2.0, false));
                    this.mc.thePlayer.setEntityBoundingBox(this.mc.thePlayer.getEntityBoundingBox().offset(editX, 0.0, editZ));
                    this.mc.thePlayer.resetPositionToBB();
                    break;
                }
                break;
            }
            case "Redesky": {
                PacketUtil.sendPacketSilent(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0E-7, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, false));
                PacketUtil.sendPacketSilent(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, false));
                this.setToggled(false);
                break;
            }
        }
    }
}
