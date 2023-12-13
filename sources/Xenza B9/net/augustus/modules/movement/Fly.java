// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.movement;

import net.augustus.events.EventSendPacket;
import net.minecraft.network.play.server.S02PacketChat;
import net.augustus.events.EventReadPacket;
import net.minecraft.util.MathHelper;
import net.augustus.events.EventUpdate;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.util.BlockPos;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockAir;
import net.augustus.events.EventBlockBoundingBox;
import net.augustus.utils.PlayerUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
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
    private double oldY;
    private double x;
    private double z;
    
    public Fly() {
        super("Fly", new Color(123, 240, 156), Categorys.MOVEMENT);
        this.timeHelper = new TimeHelper();
        this.mode = new StringValue(1, "Mode", this, "Vanilla", new String[] { "Vanilla", "Motion", "Collide", "AirJump", "Verus", "Verus2", "SkycaveBad", "Teleport", "FurtherTeleport", "Test", "SilentACAbuse", "Karhu", "FoxAC", "VulcanGlide", "Grim" });
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
        final String selected;
        final String var1 = selected = this.mode.getSelected();
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
        try {
            this.oldY = Fly.mc.thePlayer.posY;
            this.verusdmg = false;
            if (Fly.mc.thePlayer != null) {
                final String selected;
                final String var1 = selected = this.mode.getSelected();
                switch (selected) {
                    case "FoxAC": {
                        Fly.mc.thePlayer.sendQueue.addToSendQueueDirect(new C03PacketPlayer.C06PacketPlayerPosLook(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY - 2.0, Fly.mc.thePlayer.posZ, Fly.mc.thePlayer.rotationYaw, Fly.mc.thePlayer.rotationPitch, false));
                        break;
                    }
                    case "Verus": {
                        PlayerUtil.verusdmg();
                        break;
                    }
                    case "Collide": {
                        this.startY = Fly.mc.thePlayer.posY;
                        break;
                    }
                }
            }
            this.jumpGround = 0.0;
        }
        catch (final NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    @EventTarget
    public void onEventBlockBoundingBox(final EventBlockBoundingBox eventBlockBoundingBox) {
        if (this.mode.getSelected().equals("Collide") && eventBlockBoundingBox.getBlock() instanceof BlockAir && eventBlockBoundingBox.getBlockPos().getY() < this.startY) {
            final BlockPos blockPos = eventBlockBoundingBox.getBlockPos();
            eventBlockBoundingBox.setAxisAlignedBB(new AxisAlignedBB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1));
        }
    }
    
    @EventTarget
    public void onEventUpdate(final EventUpdate eventUpdate) {
        this.setDisplayName(super.getName() + " §8" + this.mode.getSelected());
        final String selected;
        final String var2 = selected = this.mode.getSelected();
        switch (selected) {
            case "Karhu": {
                if (Fly.mc.thePlayer.fallDistance >= 0.05) {
                    Fly.mc.thePlayer.motionY = 0.05;
                    Fly.mc.thePlayer.fallDistance = 0.0f;
                    break;
                }
                break;
            }
            case "VulcanGlide": {
                Fly.mc.thePlayer.motionY = ((Fly.mc.thePlayer.ticksExisted % 2 == 0) ? -0.17 : -0.1);
                break;
            }
            case "Motion":
            case "FoxAC":
            case "SilentACAbuse": {
                Fly.mc.thePlayer.motionY = 0.0;
                if (Fly.mc.gameSettings.keyBindJump.pressed) {
                    Fly.mc.thePlayer.motionY = this.speed.getValue();
                }
                if (Fly.mc.gameSettings.keyBindSneak.pressed) {
                    Fly.mc.thePlayer.motionY = -this.speed.getValue();
                }
                if (MoveUtil.isMoving()) {
                    MoveUtil.setSpeed((float)this.speed.getValue());
                    break;
                }
                MoveUtil.setSpeed(0.0f);
                break;
            }
            case "Test": {
                MoveUtil.multiplyXZ(0.0);
                Fly.mc.thePlayer.motionY = 0.0;
                final double multiplier = 0.01;
                if (Fly.mc.thePlayer.ticksExisted % 3 == 0) {
                    this.z = Fly.mc.thePlayer.posZ + MathHelper.cos((float)MoveUtil.direction()) * multiplier * this.speed.getValue();
                    this.x = Fly.mc.thePlayer.posX + -MathHelper.sin((float)MoveUtil.direction()) * multiplier * this.speed.getValue();
                }
                Fly.mc.thePlayer.setPosition(this.x, this.oldY, this.z);
                if (Fly.mc.gameSettings.keyBindJump.pressed) {
                    this.oldY += multiplier * this.speed.getValue();
                    break;
                }
                if (Fly.mc.gameSettings.keyBindSneak.pressed) {
                    this.oldY -= multiplier * this.speed.getValue();
                    break;
                }
                break;
            }
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
            case "Teleport": {
                MoveUtil.multiplyXZ(0.0);
                Fly.mc.thePlayer.motionY = 0.0;
                final double multiplier = 0.01;
                this.z = Fly.mc.thePlayer.posZ + MathHelper.cos((float)MoveUtil.direction()) * multiplier * this.speed.getValue();
                this.x = Fly.mc.thePlayer.posX + -MathHelper.sin((float)MoveUtil.direction()) * multiplier * this.speed.getValue();
                Fly.mc.thePlayer.setPositionAndUpdate(this.x, this.oldY, this.z);
                if (Fly.mc.gameSettings.keyBindJump.pressed) {
                    this.oldY += multiplier * this.speed.getValue();
                    break;
                }
                if (Fly.mc.gameSettings.keyBindSneak.pressed) {
                    this.oldY -= multiplier * this.speed.getValue();
                    break;
                }
                break;
            }
            case "FurtherTeleport": {
                MoveUtil.multiplyXZ(0.0);
                Fly.mc.thePlayer.motionY = 0.0;
                final double multiplier = 1.0;
                this.z = Fly.mc.thePlayer.posZ + MathHelper.cos((float)MoveUtil.direction()) * multiplier * this.speed.getValue();
                this.x = Fly.mc.thePlayer.posX + -MathHelper.sin((float)MoveUtil.direction()) * multiplier * this.speed.getValue();
                Fly.mc.thePlayer.setPositionAndUpdate(this.x, this.oldY, this.z);
                if (Fly.mc.gameSettings.keyBindJump.pressed) {
                    this.oldY += multiplier * this.speed.getValue();
                    break;
                }
                if (Fly.mc.gameSettings.keyBindSneak.pressed) {
                    this.oldY -= multiplier * this.speed.getValue();
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
            case "SkycaveBad": {
                if (Fly.mc.thePlayer.fallDistance >= 3.8) {
                    Fly.mc.thePlayer.sendQueue.addToSendQueueDirect(new C03PacketPlayer(true));
                    Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX, Fly.mc.thePlayer.posY - Fly.mc.thePlayer.motionY, Fly.mc.thePlayer.posZ);
                    Fly.mc.thePlayer.motionY = 0.8;
                    MoveUtil.setSpeed((float)this.speed.getValue());
                    Fly.mc.thePlayer.fallDistance = 0.0f;
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
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "VulcanSex": {
                if (packet instanceof S02PacketChat && ((S02PacketChat)packet).getChatComponent().getFormattedText().contains("Vulcan")) {
                    eventReadPacket.setCanceled(true);
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventSendPacket(final EventSendPacket eventSendPacket) {
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Grim": {
                if (eventSendPacket.getPacket() instanceof C03PacketPlayer) {
                    ((C03PacketPlayer)eventSendPacket.getPacket()).setX(Fly.mc.thePlayer.posX + 1000.0);
                    ((C03PacketPlayer)eventSendPacket.getPacket()).setZ(Fly.mc.thePlayer.posZ + 1000.0);
                    break;
                }
                break;
            }
            case "Test": {
                if (eventSendPacket.getPacket() instanceof C03PacketPlayer) {
                    ((C03PacketPlayer)eventSendPacket.getPacket()).setOnGround(false);
                    break;
                }
                break;
            }
            case "SilentACAbuse": {
                if (!(eventSendPacket.getPacket() instanceof C03PacketPlayer)) {
                    break;
                }
                final double newY = this.oldY - Fly.mc.thePlayer.posY;
                final double newX = this.x - Fly.mc.thePlayer.posX;
                final double newZ = this.z - Fly.mc.thePlayer.posZ;
                final double diff = Math.sqrt(newX * newX + newY * newY + newZ * newZ);
                boolean should = false;
                if (diff >= 8.0) {
                    should = true;
                }
                if (!should) {
                    eventSendPacket.setCanceled(true);
                }
                if (should) {
                    this.x = Fly.mc.thePlayer.posX;
                    this.oldY = Fly.mc.thePlayer.posY;
                    this.z = Fly.mc.thePlayer.posZ;
                    break;
                }
                break;
            }
        }
    }
}
