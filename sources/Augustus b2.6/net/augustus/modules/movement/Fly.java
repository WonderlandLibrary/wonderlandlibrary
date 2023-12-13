// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.movement;

import net.augustus.events.EventSendPacket;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.augustus.events.EventReadPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.events.EventUpdate;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.util.BlockPos;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockAir;
import net.augustus.events.EventBlockBoundingBox;
import net.augustus.utils.PlayerUtil;
import net.augustus.utils.MoveUtil;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class Fly extends Module
{
    private final TimeHelper timeHelper;
    public StringValue mode;
    public BooleanValue autoJump;
    public BooleanValue sendOnGroundPacket;
    public DoubleValue speed;
    public DoubleValue verusspeed;
    public int count;
    boolean verusdmg;
    private double startY;
    private double jumpGround;
    
    public Fly() {
        super("Fly", new Color(123, 240, 156), Categorys.MOVEMENT);
        this.timeHelper = new TimeHelper();
        this.mode = new StringValue(1, "Mode", this, "Vanilla", new String[] { "Vanilla", "GroundColl", "AirJump", "Verus", "Verus2" });
        this.autoJump = new BooleanValue(2, "AutoJump", this, true);
        this.sendOnGroundPacket = new BooleanValue(5, "OnGroundPacket", this, true);
        this.speed = new DoubleValue(3, "Speed", this, 1.0, 0.1, 9.0, 1);
        this.verusspeed = new DoubleValue(4, "Speed", this, 1.0, 0.1, 9.0, 1);
        this.verusdmg = false;
        this.startY = 0.0;
    }
    
    @Override
    public void onDisable() {
        this.verusdmg = false;
        Fly.mc.thePlayer.capabilities.isFlying = false;
        Fly.mc.getTimer().timerSpeed = 1.0f;
        Fly.mc.thePlayer.setSpeedInAir(0.02f);
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Vanilla": {
                Fly.mc.thePlayer.motionX = 0.0;
                Fly.mc.thePlayer.motionZ = 0.0;
                break;
            }
            case "Verus": {
                MoveUtil.setSpeed(0.0f);
                break;
            }
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.verusdmg = false;
        if (Fly.mc.thePlayer != null) {
            final String selected = this.mode.getSelected();
            switch (selected) {
                case "Verus": {
                    PlayerUtil.verusdmg();
                    break;
                }
                case "GroundColl": {
                    this.startY = Fly.mc.thePlayer.posY;
                    break;
                }
            }
        }
        this.jumpGround = 0.0;
    }
    
    @EventTarget
    public void onEventBlockBoundingBox(final EventBlockBoundingBox eventBlockBoundingBox) {
        if (this.mode.getSelected().equals("GroundColl") && eventBlockBoundingBox.getBlock() instanceof BlockAir && eventBlockBoundingBox.getBlockPos().getY() < this.startY) {
            final BlockPos blockPos = eventBlockBoundingBox.getBlockPos();
            eventBlockBoundingBox.setAxisAlignedBB(new AxisAlignedBB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1));
        }
    }
    
    @EventTarget
    public void onEventUpdate(final EventUpdate eventUpdate) {
        this.setDisplayName(super.getName() + " §8" + this.mode.getSelected());
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Vanilla": {
                Fly.mc.thePlayer.capabilities.isFlying = true;
                Fly.mc.thePlayer.capabilities.setFlySpeed((float)this.speed.getValue());
                break;
            }
            case "Verus": {
                if (Fly.mc.thePlayer.hurtTime != 0) {
                    this.verusdmg = true;
                }
                if (!this.verusdmg) {
                    Fly.mc.thePlayer.motionZ = 0.0;
                    Fly.mc.thePlayer.motionX = 0.0;
                    Fly.mc.gameSettings.keyBindJump.pressed = false;
                }
                if (this.verusdmg) {
                    Fly.mc.getTimer().timerSpeed = 0.3f;
                    if (Fly.mc.gameSettings.keyBindJump.pressed) {
                        Fly.mc.thePlayer.motionY = 1.5;
                    }
                    else if (Fly.mc.gameSettings.keyBindSneak.pressed) {
                        Fly.mc.thePlayer.motionY = -1.5;
                    }
                    else {
                        Fly.mc.thePlayer.motionY = 0.0;
                    }
                    Fly.mc.thePlayer.onGround = true;
                    MoveUtil.setSpeed((float)this.verusspeed.getValue());
                    break;
                }
                break;
            }
            case "Verus2": {
                final double constantMotionValue = 0.41999998688697815;
                final float constantMotionJumpGroundValue = 0.76f;
                if (Fly.mc.thePlayer.onGround) {
                    this.jumpGround = Fly.mc.thePlayer.posY;
                    Fly.mc.thePlayer.jump();
                }
                if (Fly.mc.thePlayer.posY > this.jumpGround + constantMotionJumpGroundValue) {
                    MoveUtil.setMotion(0.35, 45.0, Fly.mc.thePlayer.rotationYaw, true);
                    Fly.mc.thePlayer.motionY = constantMotionValue;
                    this.jumpGround = Fly.mc.thePlayer.posY;
                    break;
                }
                break;
            }
            case "AirJump": {
                if (Fly.mc.gameSettings.keyBindJump.isPressed()) {
                    if (Fly.mc.thePlayer.onGround) {
                        break;
                    }
                    Fly.mc.thePlayer.onGround = true;
                    if (this.sendOnGroundPacket.getBoolean()) {
                        Fly.mc.thePlayer.sendQueue.addToSendQueueDirect(new C03PacketPlayer(true));
                        break;
                    }
                    break;
                }
                else {
                    if (!this.autoJump.getBoolean() || Fly.mc.thePlayer.motionY >= -0.44 || Fly.mc.thePlayer.onGround) {
                        break;
                    }
                    Fly.mc.thePlayer.jump();
                    if (this.sendOnGroundPacket.getBoolean()) {
                        Fly.mc.thePlayer.sendQueue.addToSendQueueDirect(new C03PacketPlayer(true));
                        break;
                    }
                    break;
                }
                break;
            }
        }
        if (Fly.mc.theWorld == null) {
            this.toggle();
        }
    }
    
    @EventTarget
    public void onEventReadPacket(final EventReadPacket eventReadPacket) {
        final Packet packet = eventReadPacket.getPacket();
        if (packet instanceof S08PacketPlayerPosLook && Fly.mc.thePlayer != null && Fly.mc.theWorld != null) {
            final S08PacketPlayerPosLook s08PacketPlayerPosLook = (S08PacketPlayerPosLook)packet;
        }
        if (packet instanceof S06PacketUpdateHealth) {}
    }
    
    @EventTarget
    public void onEventSendPacket(final EventSendPacket eventSendPacket) {
    }
}
