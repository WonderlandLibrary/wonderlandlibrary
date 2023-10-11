package com.initial.modules.impl.movement;

import com.initial.utils.player.*;
import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import java.util.concurrent.*;
import com.initial.utils.network.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import com.initial.events.impl.*;
import com.initial.utils.movement.*;
import net.minecraft.entity.*;
import net.minecraft.client.*;
import net.minecraft.client.settings.*;

public class Flight extends Module
{
    Timer timer;
    public boolean doneBow;
    private double launchY;
    int ticks;
    int damagedTicks;
    boolean damaged;
    public ModeSet mode;
    public ModeSet acms;
    public DoubleSet flightSpeed;
    public BooleanSet viewBobbing;
    public BooleanSet antiKick;
    
    public Flight() {
        super("Flight", 33, Category.MOVEMENT);
        this.timer = new Timer();
        this.doneBow = false;
        this.ticks = 0;
        this.damagedTicks = 0;
        this.damaged = false;
        this.mode = new ModeSet("Mode", "Vanilla", new String[] { "Vanilla", "Creative", "Air Collide", "Verus Infinite", "Collide", "Latest Verus" });
        this.acms = new ModeSet("AirCollide Mode", "Slow", new String[] { "Fast", "Slow" });
        this.flightSpeed = new DoubleSet("Flight Speed", 1.0, 0.1, 20.0, 0.01);
        this.viewBobbing = new BooleanSet("View Bobbing", false);
        this.antiKick = new BooleanSet("Anti Kick", false);
        this.addSettings(this.mode, this.acms, this.flightSpeed, this.viewBobbing, this.antiKick);
    }
    
    @EventTarget
    public void onMove(final EventMove e) {
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Verus Infinite": {
                if (!this.damaged) {
                    e.setX(0.0);
                    e.setZ(0.0);
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onBB(final EventCollide event) {
        if (this.mc.thePlayer == null) {
            return;
        }
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Collide":
            case "Air Collide": {
                if (this.mc.thePlayer.isSneaking()) {
                    return;
                }
                if (event.getBlock() instanceof BlockAir && event.getPosY() < this.mc.thePlayer.posY) {
                    event.setBoundingBox(AxisAlignedBB.fromBounds(event.getPosX(), event.getPosY(), event.getPosZ(), event.getPosX() + 1.0, this.mc.thePlayer.posY, event.getPosZ() + 1.0));
                    break;
                }
                break;
            }
            case "Latest Verus": {
                if (event.getBlock() instanceof BlockAir && event.getPosY() <= this.launchY) {
                    event.setBoundingBox(AxisAlignedBB.fromBounds(event.getPosX(), event.getPosY(), event.getPosZ(), event.getPosX() + 1.0, this.launchY, event.getPosZ() + 1.0));
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onSend(final EventSendPacket event) {
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Verus Infinite": {
                if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
                    final C0FPacketConfirmTransaction c0f = (C0FPacketConfirmTransaction)event.getPacket();
                    if (c0f.windowId < 5.0) {
                        event.setCancelled(true);
                        PacketUtil.sendPacketSilent(new C0FPacketConfirmTransaction(ThreadLocalRandom.current().nextInt(100, 500), (short)500, true));
                    }
                    break;
                }
                if (event.getPacket() instanceof C03PacketPlayer) {
                    final C03PacketPlayer packetPlayer = (C03PacketPlayer)event.getPacket();
                    packetPlayer.onGround = true;
                    if (this.mc.thePlayer.ticksExisted > 15) {
                        event.setCancelled(this.mc.thePlayer.ticksExisted % 3 == 0);
                    }
                    break;
                }
                break;
            }
            case "Collide": {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    final C03PacketPlayer c03 = (C03PacketPlayer)event.getPacket();
                    c03.onGround = true;
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onMotion(final EventMotionUpdate e) {
        if (this.viewBobbing.isEnabled()) {
            this.mc.thePlayer.cameraYaw = 0.105f;
        }
        this.setDisplayName("Flight §7" + this.mode.getMode());
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Verus Infinite": {
                final double x = this.mc.thePlayer.posX;
                final double y = this.mc.thePlayer.posY;
                final double z = this.mc.thePlayer.posZ;
                if (this.ticks == 0) {
                    PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 3.001, z, false));
                    PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
                }
                if (this.mc.thePlayer.hurtTime > 0 && !this.damaged) {
                    this.damagedTicks = 0;
                    this.damaged = true;
                }
                if (!MovementUtils.isMoving()) {
                    MovementUtils.setMotion(0.0);
                }
                if (!this.damaged) {
                    this.mc.thePlayer.motionX = 0.0;
                    this.mc.thePlayer.motionZ = 0.0;
                }
                else {
                    if (this.damagedTicks == 0) {
                        this.mc.thePlayer.jump();
                    }
                    else {
                        this.mc.thePlayer.motionY = 0.0;
                        if (Flight.localPlayer.hurtTime > 0) {
                            MovementUtils.setSpeed1(1.8);
                        }
                        else {
                            MovementUtils.setSpeed1(MovementUtils.getSpeed());
                        }
                    }
                    e.setOnGround(true);
                    ++this.damagedTicks;
                }
                ++this.ticks;
                break;
            }
            case "Air Collide": {
                final String mode2 = this.acms.getMode();
                switch (mode2) {
                    case "Slow": {
                        if (!this.mc.thePlayer.isMoving() || !this.mc.thePlayer.onGround) {
                            break;
                        }
                        if (this.mc.thePlayer.ticksExisted % 2 == 0) {
                            this.mc.thePlayer.setSpeed(0.32f);
                            break;
                        }
                        this.mc.thePlayer.setSpeed(0.52f);
                        break;
                    }
                    case "Fast": {
                        if (!this.mc.thePlayer.isMoving() || !this.mc.thePlayer.onGround) {
                            break;
                        }
                        if (this.mc.thePlayer.ticksExisted % 2 == 0) {
                            this.mc.thePlayer.setSpeed(1.8f);
                            break;
                        }
                        this.mc.thePlayer.setSpeed(0.35f);
                        break;
                    }
                }
            }
            case "Latest Verus": {
                if (this.mc.thePlayer.onGround && MovementUtils.isMoving()) {
                    this.mc.thePlayer.jump();
                    MovementUtils.setSpeed1(0.47999998927116394);
                    break;
                }
                MovementUtils.setSpeed1(MovementUtils.getSpeed());
                break;
            }
            case "Vanilla": {
                this.mc.thePlayer.motionY = 0.0;
                if (!MovementUtils.isMoving()) {
                    MovementUtils.setMotion(0.0);
                }
                else {
                    MovementUtils.setSpeed1(this.flightSpeed.getValue());
                }
                if (this.mc.gameSettings.keyBindJump.pressed) {
                    this.mc.thePlayer.motionY = this.flightSpeed.getValue() * 0.5;
                }
                if (this.mc.gameSettings.keyBindSneak.pressed) {
                    this.mc.thePlayer.motionY = -this.flightSpeed.getValue() * 0.5;
                }
                e.setOnGround(true);
                break;
            }
            case "Creative": {
                this.mc.thePlayer.capabilities.isFlying = true;
                break;
            }
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (this.mc.thePlayer == null) {
            return;
        }
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Collide": {
                this.mc.thePlayer.motionY = 0.0;
                break;
            }
        }
        this.damaged = false;
        this.ticks = 0;
        this.damagedTicks = 0;
        this.launchY = this.mc.thePlayer.posY;
        this.timer.reset();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.thePlayer.motionX = 0.0;
        this.mc.thePlayer.motionZ = 0.0;
        resetCapabilities();
        this.doneBow = false;
    }
    
    public boolean isGround() {
        return this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically && MovementUtils.getOnRealGround(this.mc.thePlayer, 1.0E-4);
    }
    
    public static void resetCapabilities() {
        Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump);
        Minecraft.getMinecraft().thePlayer.stepHeight = 0.625f;
        Minecraft.getMinecraft().timer.timerSpeed = 1.0f;
        Minecraft.getMinecraft().thePlayer.isCollided = false;
        if (Minecraft.getMinecraft().thePlayer.isSpectator()) {
            return;
        }
        Minecraft.getMinecraft().thePlayer.capabilities.isFlying = false;
        Minecraft.getMinecraft().thePlayer.capabilities.allowFlying = Minecraft.getMinecraft().playerController.isInCreativeMode();
        Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode = Minecraft.getMinecraft().playerController.isInCreativeMode();
    }
}
