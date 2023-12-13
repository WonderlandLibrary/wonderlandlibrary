// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.movement;

import net.minecraft.entity.player.EntityPlayer;
import com.elementars.eclient.util.MathUtil;
import com.elementars.eclient.event.events.MotionEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import com.elementars.eclient.event.events.LocalPlayerUpdateEvent;
import net.minecraft.item.ItemStack;
import com.elementars.eclient.event.Event;
import net.minecraft.item.ItemElytra;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import com.elementars.eclient.event.events.PlayerMoveEvent;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.network.play.client.CPacketPlayer;
import com.elementars.eclient.event.events.EventSendPacket;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.util.Timer;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class ElytraFly extends Module
{
    public Value<Mode> mode;
    public Value<Integer> devMode;
    public Value<Float> speed;
    public Value<Float> vSpeed;
    public Value<Float> hSpeed;
    public Value<Float> glide;
    public Value<Float> tooBeeSpeed;
    public Value<Boolean> autoStart;
    public Value<Boolean> disableInLiquid;
    public Value<Boolean> infiniteDura;
    public Value<Boolean> noKick;
    public Value<Boolean> keepMotion;
    public Value<Boolean> instaFly;
    public Value<String> instaMode;
    public Value<Boolean> allowUp;
    private static ElytraFly INSTANCE;
    private final Timer timer;
    private final Timer instaTimer;
    private Double posX;
    private Double flyHeight;
    private Double posZ;
    
    public ElytraFly() {
        super("ElytraFly", "Makes Elytra Flight better.", 0, Category.MOVEMENT, true);
        this.mode = this.register(new Value<Mode>("Mode", this, Mode.FLY, Mode.values()));
        this.devMode = this.register(new Value<Integer>("Type", this, 2, 1, 3)).visibleWhen(t -> this.mode.getValue() == Mode.BYPASS || this.mode.getValue() == Mode.BETTER);
        this.speed = this.register(new Value<Float>("Speed", this, 1.0f, 0.0f, 10.0f)).visibleWhen(t -> this.mode.getValue() != Mode.FLY && this.mode.getValue() != Mode.BOOST && this.mode.getValue() != Mode.BETTER && this.mode.getValue() != Mode.OHARE);
        this.vSpeed = this.register(new Value<Float>("VSpeed", this, 0.3f, 0.0f, 10.0f)).visibleWhen(t -> this.mode.getValue() == Mode.BETTER || this.mode.getValue() == Mode.OHARE);
        this.hSpeed = this.register(new Value<Float>("HSpeed", this, 1.0f, 0.0f, 10.0f)).visibleWhen(t -> this.mode.getValue() == Mode.BETTER || this.mode.getValue() == Mode.OHARE);
        this.glide = this.register(new Value<Float>("Glide", this, 1.0E-4f, 0.0f, 0.2f)).visibleWhen(t -> this.mode.getValue() == Mode.BETTER);
        this.tooBeeSpeed = this.register(new Value<Float>("TooBeeSpeed", this, 1.8000001f, 1.0f, 2.0f)).visibleWhen(t -> this.mode.getValue() == Mode.TOOBEE);
        this.autoStart = this.register(new Value<Boolean>("AutoStart", this, true));
        this.disableInLiquid = this.register(new Value<Boolean>("NoLiquid", this, true));
        this.infiniteDura = this.register(new Value<Boolean>("InfiniteDura", this, false));
        this.noKick = this.register(new Value<Boolean>("NoKick", this, false)).visibleWhen(t -> this.mode.getValue() == Mode.PACKET || this.mode.getValue() == Mode.BETTER);
        this.keepMotion = this.register(new Value<Boolean>("KeepMotion", this, true)).visibleWhen(aBoolean -> this.mode.getValue() == Mode.BETTER);
        this.instaFly = this.register(new Value<Boolean>("InstaFly", this, true)).visibleWhen(aBoolean -> this.mode.getValue() == Mode.BETTER);
        this.instaMode = this.register(new Value<String>("Takeoff Mode", this, "Static", new String[] { "None", "Static" })).visibleWhen(aBoolean -> this.mode.getValue() == Mode.BETTER);
        this.allowUp = this.register(new Value<Boolean>("AllowUp", this, true)).visibleWhen(t -> this.mode.getValue() == Mode.BETTER);
        this.timer = new Timer();
        this.instaTimer = new Timer();
        this.setInstance();
    }
    
    private void setInstance() {
        ElytraFly.INSTANCE = this;
    }
    
    public static ElytraFly getInstance() {
        if (ElytraFly.INSTANCE == null) {
            ElytraFly.INSTANCE = new ElytraFly();
        }
        return ElytraFly.INSTANCE;
    }
    
    @Override
    public void onEnable() {
        if (this.mode.getValue() == Mode.BETTER && !this.autoStart.getValue() && this.devMode.getValue() == 1) {
            ElytraFly.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }
        this.flyHeight = null;
        this.posX = null;
        this.posZ = null;
    }
    
    @Override
    public String getHudInfo() {
        return this.mode.getValue().name();
    }
    
    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Mode.BYPASS && this.devMode.getValue() == 1 && ElytraFly.mc.player.isElytraFlying()) {
            ElytraFly.mc.player.motionX = 0.0;
            ElytraFly.mc.player.motionY = -1.0E-4;
            ElytraFly.mc.player.motionZ = 0.0;
            final double forwardInput = ElytraFly.mc.player.movementInput.moveForward;
            final double strafeInput = ElytraFly.mc.player.movementInput.moveStrafe;
            final double[] result = this.forwardStrafeYaw(forwardInput, strafeInput, ElytraFly.mc.player.rotationYaw);
            final double forward = result[0];
            final double strafe = result[1];
            final double yaw = result[2];
            if (forwardInput != 0.0 || strafeInput != 0.0) {
                ElytraFly.mc.player.motionX = forward * this.speed.getValue() * Math.cos(Math.toRadians(yaw + 90.0)) + strafe * this.speed.getValue() * Math.sin(Math.toRadians(yaw + 90.0));
                ElytraFly.mc.player.motionZ = forward * this.speed.getValue() * Math.sin(Math.toRadians(yaw + 90.0)) - strafe * this.speed.getValue() * Math.cos(Math.toRadians(yaw + 90.0));
            }
            if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                ElytraFly.mc.player.motionY = -1.0;
            }
        }
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket event) {
        if (event.getPacket() instanceof CPacketPlayer && this.mode.getValue() == Mode.TOOBEE) {
            final CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            if (ElytraFly.mc.player.isElytraFlying() && !ElytraFly.mc.player.movementInput.jump && packet.pitch < 1.0f) {
                packet.pitch = 1.0f;
            }
        }
        if (event.getPacket() instanceof CPacketPlayer && this.mode.getValue() == Mode.BETTER) {
            final CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            if (ElytraFly.mc.player.isElytraFlying() && !ElytraFly.mc.player.movementInput.jump && packet.pitch < 1.0f) {
                packet.pitch = 1.0f;
            }
        }
    }
    
    @EventTarget
    public void onMove(final PlayerMoveEvent event) {
        if (this.mode.getValue() == Mode.OHARE) {
            final ItemStack itemstack = ElytraFly.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isUsable(itemstack) && ElytraFly.mc.player.isElytraFlying()) {
                event.setY(ElytraFly.mc.gameSettings.keyBindJump.isKeyDown() ? ((double)this.vSpeed.getValue()) : (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown() ? (-this.vSpeed.getValue()) : 0.0));
                ElytraFly.mc.player.addVelocity(0.0, ElytraFly.mc.gameSettings.keyBindJump.isKeyDown() ? ((double)this.vSpeed.getValue()) : (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown() ? (-this.vSpeed.getValue()) : 0.0), 0.0);
                ElytraFly.mc.player.rotateElytraX = 0.0f;
                ElytraFly.mc.player.rotateElytraY = 0.0f;
                ElytraFly.mc.player.rotateElytraZ = 0.0f;
                ElytraFly.mc.player.moveVertical = (ElytraFly.mc.gameSettings.keyBindJump.isKeyDown() ? this.vSpeed.getValue() : (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown() ? (-this.vSpeed.getValue()) : 0.0f));
                double forward = ElytraFly.mc.player.movementInput.moveForward;
                double strafe = ElytraFly.mc.player.movementInput.moveStrafe;
                float yaw = ElytraFly.mc.player.rotationYaw;
                if (forward == 0.0 && strafe == 0.0) {
                    event.setX(0.0);
                    event.setZ(0.0);
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
                    final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
                    final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
                    event.setX(forward * this.hSpeed.getValue() * cos + strafe * this.hSpeed.getValue() * sin);
                    event.setZ(forward * this.hSpeed.getValue() * sin - strafe * this.hSpeed.getValue() * cos);
                }
            }
        }
        else if (event.getEventState() == Event.State.PRE && this.mode.getValue() == Mode.BYPASS && this.devMode.getValue() == 3) {
            if (ElytraFly.mc.player.isElytraFlying()) {
                event.setX(0.0);
                event.setY(-1.0E-4);
                event.setZ(0.0);
                final double forwardInput = ElytraFly.mc.player.movementInput.moveForward;
                final double strafeInput = ElytraFly.mc.player.movementInput.moveStrafe;
                final double[] result = this.forwardStrafeYaw(forwardInput, strafeInput, ElytraFly.mc.player.rotationYaw);
                final double forward2 = result[0];
                final double strafe2 = result[1];
                final double yaw2 = result[2];
                if (forwardInput != 0.0 || strafeInput != 0.0) {
                    event.setX(forward2 * this.speed.getValue() * Math.cos(Math.toRadians(yaw2 + 90.0)) + strafe2 * this.speed.getValue() * Math.sin(Math.toRadians(yaw2 + 90.0)));
                    event.setY(forward2 * this.speed.getValue() * Math.sin(Math.toRadians(yaw2 + 90.0)) - strafe2 * this.speed.getValue() * Math.cos(Math.toRadians(yaw2 + 90.0)));
                }
                if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    event.setY(-1.0);
                }
            }
        }
        else if (this.mode.getValue() == Mode.TOOBEE) {
            if (!ElytraFly.mc.player.isElytraFlying()) {
                return;
            }
            if (ElytraFly.mc.player.movementInput.jump) {
                return;
            }
            if (ElytraFly.mc.player.movementInput.sneak) {
                ElytraFly.mc.player.motionY = -(this.tooBeeSpeed.getValue() / 2.0f);
                event.setY(-(this.speed.getValue() / 2.0f));
            }
            else if (event.getY() != -1.01E-4) {
                event.setY(-1.01E-4);
                ElytraFly.mc.player.motionY = -1.01E-4;
            }
        }
        this.setMoveSpeed(event, this.tooBeeSpeed.getValue());
    }
    
    private void setMoveSpeed(final PlayerMoveEvent event, final double speed) {
        double forward = ElytraFly.mc.player.movementInput.moveForward;
        double strafe = ElytraFly.mc.player.movementInput.moveStrafe;
        float yaw = ElytraFly.mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
            ElytraFly.mc.player.motionX = 0.0;
            ElytraFly.mc.player.motionZ = 0.0;
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
            final double x = forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw));
            final double z = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw));
            event.setX(x);
            event.setZ(z);
            ElytraFly.mc.player.motionX = x;
            ElytraFly.mc.player.motionZ = z;
        }
    }
    
    @EventTarget
    public void onLivingUpdate(final LocalPlayerUpdateEvent event) {
        if (!ElytraFly.mc.player.isElytraFlying()) {
            return;
        }
        switch (this.mode.getValue()) {
            case BOOST: {
                if (ElytraFly.mc.player.isInWater()) {
                    ElytraFly.mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    return;
                }
                if (ElytraFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                    final EntityPlayerSP player;
                    final EntityPlayerSP player = player = ElytraFly.mc.player;
                    player.motionY += 0.08;
                }
                else if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    final EntityPlayerSP player2;
                    final EntityPlayerSP player2 = player2 = ElytraFly.mc.player;
                    player2.motionY -= 0.04;
                }
                if (ElytraFly.mc.gameSettings.keyBindForward.isKeyDown()) {
                    final float yaw = (float)Math.toRadians(ElytraFly.mc.player.rotationYaw);
                    final EntityPlayerSP player3;
                    final EntityPlayerSP player3 = player3 = ElytraFly.mc.player;
                    player3.motionX -= MathHelper.sin(yaw) * 0.05f;
                    final EntityPlayerSP player4;
                    final EntityPlayerSP player4 = player4 = ElytraFly.mc.player;
                    player4.motionZ += MathHelper.cos(yaw) * 0.05f;
                    break;
                }
                if (ElytraFly.mc.gameSettings.keyBindBack.isKeyDown()) {
                    final float yaw = (float)Math.toRadians(ElytraFly.mc.player.rotationYaw);
                    final EntityPlayerSP player5;
                    final EntityPlayerSP player5 = player5 = ElytraFly.mc.player;
                    player5.motionX += MathHelper.sin(yaw) * 0.05f;
                    final EntityPlayerSP player6;
                    final EntityPlayerSP player6 = player6 = ElytraFly.mc.player;
                    player6.motionZ -= MathHelper.cos(yaw) * 0.05f;
                    break;
                }
                break;
            }
            case FLY: {
                ElytraFly.mc.player.capabilities.isFlying = true;
                break;
            }
        }
    }
    
    @EventTarget
    public void onUpdateWalkingPlayer(final MotionEvent event) {
        if (ElytraFly.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA) {
            return;
        }
        if (!ElytraFly.mc.player.isElytraFlying()) {
            if (!ElytraFly.mc.player.onGround && this.instaFly.getValue()) {
                double height = ElytraFly.mc.player.posY;
                if (this.noKick.getValue()) {
                    height -= this.glide.getValue();
                }
                if ("Static".equals(this.instaMode.getValue())) {
                    ElytraFly.mc.player.setPosition(ElytraFly.mc.player.posX, height, ElytraFly.mc.player.posZ);
                    ElytraFly.mc.player.setVelocity(0.0, 0.0, 0.0);
                }
                if (!this.instaTimer.hasReached(1000L)) {
                    return;
                }
                this.instaTimer.reset();
                ElytraFly.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
            return;
        }
        switch (event.getEventState()) {
            case PRE: {
                if (this.disableInLiquid.getValue() && (ElytraFly.mc.player.isInWater() || ElytraFly.mc.player.isInLava())) {
                    if (ElytraFly.mc.player.isElytraFlying()) {
                        ElytraFly.mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    }
                    return;
                }
                if (this.autoStart.getValue() && ElytraFly.mc.gameSettings.keyBindJump.isKeyDown() && !ElytraFly.mc.player.isElytraFlying() && ElytraFly.mc.player.motionY < 0.0 && this.timer.hasReached(250L)) {
                    ElytraFly.mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    this.timer.reset();
                }
                if (this.mode.getValue() == Mode.BETTER) {
                    final double[] dir = MathUtil.directionSpeed((this.devMode.getValue() == 1) ? ((double)this.speed.getValue()) : ((double)this.hSpeed.getValue()));
                    switch (this.devMode.getValue()) {
                        case 1: {
                            ElytraFly.mc.player.setVelocity(0.0, 0.0, 0.0);
                            ElytraFly.mc.player.jumpMovementFactor = this.speed.getValue();
                            if (ElytraFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                                final EntityPlayerSP player;
                                final EntityPlayerSP player = player = ElytraFly.mc.player;
                                player.motionY += this.speed.getValue();
                            }
                            if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                final EntityPlayerSP player2;
                                final EntityPlayerSP player2 = player2 = ElytraFly.mc.player;
                                player2.motionY -= this.speed.getValue();
                            }
                            if (ElytraFly.mc.player.movementInput.moveStrafe != 0.0f || ElytraFly.mc.player.movementInput.moveForward != 0.0f) {
                                ElytraFly.mc.player.motionX = dir[0];
                                ElytraFly.mc.player.motionZ = dir[1];
                                break;
                            }
                            ElytraFly.mc.player.motionX = 0.0;
                            ElytraFly.mc.player.motionZ = 0.0;
                            break;
                        }
                        case 2: {
                            if (ElytraFly.mc.player.isElytraFlying()) {
                                if (this.flyHeight == null) {
                                    this.flyHeight = ElytraFly.mc.player.posY;
                                }
                                if (this.noKick.getValue()) {
                                    this.flyHeight -= (Double)this.glide.getValue();
                                }
                                this.posX = 0.0;
                                this.posZ = 0.0;
                                if (ElytraFly.mc.player.movementInput.moveStrafe != 0.0f || ElytraFly.mc.player.movementInput.moveForward != 0.0f) {
                                    this.posX = dir[0];
                                    this.posZ = dir[1];
                                }
                                if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                    this.flyHeight = ElytraFly.mc.player.posY - this.vSpeed.getValue();
                                }
                                ElytraFly.mc.player.setPosition(ElytraFly.mc.player.posX + this.posX, (double)this.flyHeight, ElytraFly.mc.player.posZ + this.posZ);
                                ElytraFly.mc.player.setVelocity(0.0, 0.0, 0.0);
                                break;
                            }
                            this.flyHeight = null;
                            return;
                        }
                        case 3: {
                            if (ElytraFly.mc.player.isElytraFlying()) {
                                if (this.flyHeight == null || this.posX == null || this.posX == 0.0 || this.posZ == null || this.posZ == 0.0) {
                                    this.flyHeight = ElytraFly.mc.player.posY;
                                    this.posX = ElytraFly.mc.player.posX;
                                    this.posZ = ElytraFly.mc.player.posZ;
                                }
                                if (this.noKick.getValue()) {
                                    this.flyHeight -= (Double)this.glide.getValue();
                                }
                                if (ElytraFly.mc.player.movementInput.moveStrafe != 0.0f || ElytraFly.mc.player.movementInput.moveForward != 0.0f) {
                                    this.posX += dir[0];
                                    this.posZ += dir[1];
                                }
                                if (this.allowUp.getValue() && ElytraFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                                    this.flyHeight = ElytraFly.mc.player.posY + this.vSpeed.getValue() / 10.0f;
                                }
                                if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                    this.flyHeight = ElytraFly.mc.player.posY - this.vSpeed.getValue() / 10.0f;
                                }
                                ElytraFly.mc.player.setPosition((double)this.posX, (double)this.flyHeight, (double)this.posZ);
                                ElytraFly.mc.player.setVelocity(0.0, 0.0, 0.0);
                                break;
                            }
                            this.flyHeight = null;
                            this.posX = null;
                            this.posZ = null;
                            return;
                        }
                    }
                }
                final double rotationYaw = Math.toRadians(ElytraFly.mc.player.rotationYaw);
                if (ElytraFly.mc.player.isElytraFlying()) {
                    switch (this.mode.getValue()) {
                        case VANILLA: {
                            final float speedScaled = this.speed.getValue() * 0.05f;
                            if (ElytraFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                                final EntityPlayerSP player3;
                                final EntityPlayerSP player3 = player3 = ElytraFly.mc.player;
                                player3.motionY += speedScaled;
                            }
                            if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                final EntityPlayerSP player4;
                                final EntityPlayerSP player4 = player4 = ElytraFly.mc.player;
                                player4.motionY -= speedScaled;
                            }
                            if (ElytraFly.mc.gameSettings.keyBindForward.isKeyDown()) {
                                final EntityPlayerSP player5;
                                final EntityPlayerSP player5 = player5 = ElytraFly.mc.player;
                                player5.motionX -= Math.sin(rotationYaw) * speedScaled;
                                final EntityPlayerSP player6;
                                final EntityPlayerSP player6 = player6 = ElytraFly.mc.player;
                                player6.motionZ += Math.cos(rotationYaw) * speedScaled;
                            }
                            if (ElytraFly.mc.gameSettings.keyBindBack.isKeyDown()) {
                                final EntityPlayerSP player7;
                                final EntityPlayerSP player7 = player7 = ElytraFly.mc.player;
                                player7.motionX += Math.sin(rotationYaw) * speedScaled;
                                final EntityPlayerSP player8;
                                final EntityPlayerSP player8 = player8 = ElytraFly.mc.player;
                                player8.motionZ -= Math.cos(rotationYaw) * speedScaled;
                                break;
                            }
                            break;
                        }
                        case PACKET: {
                            this.freezePlayer((EntityPlayer)ElytraFly.mc.player);
                            this.runNoKick((EntityPlayer)ElytraFly.mc.player);
                            final double[] directionSpeedPacket = MathUtil.directionSpeed(this.speed.getValue());
                            if (ElytraFly.mc.player.movementInput.jump) {
                                ElytraFly.mc.player.motionY = this.speed.getValue();
                            }
                            if (ElytraFly.mc.player.movementInput.sneak) {
                                ElytraFly.mc.player.motionY = -this.speed.getValue();
                            }
                            if (ElytraFly.mc.player.movementInput.moveStrafe != 0.0f || ElytraFly.mc.player.movementInput.moveForward != 0.0f) {
                                ElytraFly.mc.player.motionX = directionSpeedPacket[0];
                                ElytraFly.mc.player.motionZ = directionSpeedPacket[1];
                            }
                            ElytraFly.mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                            ElytraFly.mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                            break;
                        }
                        case BYPASS: {
                            if (this.devMode.getValue() != 3) {
                                break;
                            }
                            if (ElytraFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                                ElytraFly.mc.player.motionY = 0.019999999552965164;
                            }
                            if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                ElytraFly.mc.player.motionY = -0.20000000298023224;
                            }
                            if (ElytraFly.mc.player.ticksExisted % 8 == 0 && ElytraFly.mc.player.posY <= 240.0) {
                                ElytraFly.mc.player.motionY = 0.019999999552965164;
                            }
                            ElytraFly.mc.player.capabilities.isFlying = true;
                            ElytraFly.mc.player.capabilities.setFlySpeed(0.025f);
                            final double[] directionSpeedBypass = MathUtil.directionSpeed(0.5199999809265137);
                            if (ElytraFly.mc.player.movementInput.moveStrafe != 0.0f || ElytraFly.mc.player.movementInput.moveForward != 0.0f) {
                                ElytraFly.mc.player.motionX = directionSpeedBypass[0];
                                ElytraFly.mc.player.motionZ = directionSpeedBypass[1];
                                break;
                            }
                            ElytraFly.mc.player.motionX = 0.0;
                            ElytraFly.mc.player.motionZ = 0.0;
                            break;
                        }
                    }
                }
                if (this.infiniteDura.getValue()) {
                    ElytraFly.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    break;
                }
                break;
            }
            case POST: {
                if (this.infiniteDura.getValue()) {
                    ElytraFly.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    break;
                }
                break;
            }
        }
    }
    
    private double[] forwardStrafeYaw(final double forward, final double strafe, final double yaw) {
        final double[] result = { forward, strafe, yaw };
        if ((forward != 0.0 || strafe != 0.0) && forward != 0.0) {
            if (strafe > 0.0) {
                final double[] array = result;
                final int n = 2;
                final double[] array3 = array;
                final int n3 = 2;
                array3[n3] += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                final double[] array2 = result;
                final int n2 = 2;
                final double[] array4 = array2;
                final int n4 = 2;
                array4[n4] += ((forward > 0.0) ? 45 : -45);
            }
            result[1] = 0.0;
            if (forward > 0.0) {
                result[0] = 1.0;
            }
            else if (forward < 0.0) {
                result[0] = -1.0;
            }
        }
        return result;
    }
    
    private void freezePlayer(final EntityPlayer player) {
        player.motionX = 0.0;
        player.motionY = 0.0;
        player.motionZ = 0.0;
    }
    
    private void runNoKick(final EntityPlayer player) {
        if (this.noKick.getValue() && !player.isElytraFlying() && player.ticksExisted % 4 == 0) {
            player.motionY = -0.03999999910593033;
        }
    }
    
    @Override
    public void onDisable() {
        if (ElytraFly.mc.player == null || ElytraFly.mc.world == null) {
            return;
        }
        if (this.mode.getValue() == Mode.BETTER && this.devMode.getValue() == 2 && this.keepMotion.getValue()) {
            ElytraFly.mc.player.motionX = this.posX;
            ElytraFly.mc.player.motionZ = this.posZ;
        }
        ElytraFly.mc.timer.tickLength = 50.0f;
        if (ElytraFly.mc.player.capabilities.isCreativeMode) {
            return;
        }
        ElytraFly.mc.player.capabilities.isFlying = false;
    }
    
    static {
        ElytraFly.INSTANCE = new ElytraFly();
    }
    
    public enum Mode
    {
        VANILLA, 
        PACKET, 
        BOOST, 
        FLY, 
        BYPASS, 
        BETTER, 
        OHARE, 
        TOOBEE;
    }
}
