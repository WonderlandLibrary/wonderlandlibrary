// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.combat;

import net.augustus.events.EventUpdate;
import net.augustus.events.EventPostMotion;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import net.augustus.utils.PlayerUtil;
import java.util.HashMap;
import net.augustus.utils.MoveUtil;
import net.augustus.events.EventSilentMove;
import net.augustus.events.EventSendPacket;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.augustus.events.EventReadPacket;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.util.Vec3;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.network.Packet;
import java.util.ArrayList;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class Velocity extends Module
{
    private final TimeHelper timeHelper;
    private final TimeHelper timeDelay;
    private final ArrayList<Packet> packets;
    public StringValue mode;
    public DoubleValue XZValue;
    public DoubleValue YValue;
    public DoubleValue XZValueIntave;
    public BooleanValue jumpIntave;
    public BooleanValue ignoreExplosion;
    public DoubleValue pushXZ;
    public DoubleValue pushStart;
    public DoubleValue pushEnd;
    public DoubleValue reverseStart;
    public DoubleValue karhuStart;
    public BooleanValue reverseStrafe;
    public BooleanValue pushOnGround;
    public BooleanValue hitBug;
    public Vec3 position;
    private int counter;
    private int grimTCancel;
    private boolean grimFlag;
    
    public Velocity() {
        super("Velocity", new Color(83, 102, 109, 255), Categorys.COMBAT);
        this.timeHelper = new TimeHelper();
        this.timeDelay = new TimeHelper();
        this.packets = new ArrayList<Packet>();
        this.mode = new StringValue(1, "Mode", this, "Basic", new String[] { "Basic", "Legit", "PushGround", "Push", "Intave", "Reverse", "Spoof", "Test", "Grim", "Grim2", "BuzzReverse", "TickZero" });
        this.XZValue = new DoubleValue(2, "XZVelocity", this, 20.0, 0.0, 100.0, 0);
        this.YValue = new DoubleValue(3, "YVelocity", this, 20.0, 0.0, 100.0, 0);
        this.XZValueIntave = new DoubleValue(5, "XZVelocity", this, 0.6, -1.0, 1.0, 2);
        this.jumpIntave = new BooleanValue(7, "Jump", this, false);
        this.ignoreExplosion = new BooleanValue(4, "Explosion", this, true);
        this.pushXZ = new DoubleValue(7, "Push", this, 1.1, 0.01, 20.0, 2);
        this.pushStart = new DoubleValue(9, "PushStart", this, 9.0, 1.0, 10.0, 0);
        this.pushEnd = new DoubleValue(10, "PushEnd", this, 2.0, 1.0, 10.0, 0);
        this.reverseStart = new DoubleValue(12, "ReverseStart", this, 9.0, 1.0, 10.0, 0);
        this.karhuStart = new DoubleValue(69, "KarhuStart", this, 4.0, 1.0, 10.0, 0);
        this.reverseStrafe = new BooleanValue(13, "ReverseStrafe", this, false);
        this.pushOnGround = new BooleanValue(10, "OnGround", this, false);
        this.hitBug = new BooleanValue(11, "HitBug", this, false);
        this.position = new Vec3(0.0, 0.0, 0.0);
        this.counter = 0;
        this.grimTCancel = 0;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @EventTarget
    public void onEventReadPacket(final EventReadPacket eventReadPacket) {
        final Packet packet = eventReadPacket.getPacket();
        if (this.mode.getSelected().equalsIgnoreCase("Grim") && !Velocity.mm.blockFly.isToggled()) {
            final Packet p = eventReadPacket.getPacket();
            if (p instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)p).getEntityID() == Velocity.mc.thePlayer.getEntityId()) {
                eventReadPacket.setCanceled(true);
                this.grimTCancel = 6;
            }
            if (p instanceof S32PacketConfirmTransaction) {
                eventReadPacket.setCanceled(true);
                --this.grimTCancel;
            }
        }
        if (this.mode.getSelected().equalsIgnoreCase("Grim2")) {
            final Packet p = eventReadPacket.getPacket();
            if (p instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)p).getEntityID() == Velocity.mc.thePlayer.getEntityId()) {
                eventReadPacket.setCanceled(true);
                final EntityPlayerSP thePlayer = Velocity.mc.thePlayer;
                thePlayer.motionX += 0.1;
                final EntityPlayerSP thePlayer2 = Velocity.mc.thePlayer;
                thePlayer2.motionY += 0.1;
                final EntityPlayerSP thePlayer3 = Velocity.mc.thePlayer;
                thePlayer3.motionZ += 0.1;
            }
        }
        if (this.mode.getSelected().equalsIgnoreCase("Basic")) {
            final Packet p = eventReadPacket.getPacket();
            if (p instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)p).getEntityID() == Velocity.mc.thePlayer.getEntityId()) {
                if (this.XZValue.getValue() <= 0.0 && this.YValue.getValue() <= 0.0) {
                    eventReadPacket.setCanceled(true);
                }
                else {
                    ((S12PacketEntityVelocity)p).setMotionX((int)(((S12PacketEntityVelocity)p).getMotionX() * this.XZValue.getValue() / 100.0));
                    ((S12PacketEntityVelocity)p).setMotionY((int)(((S12PacketEntityVelocity)p).getMotionY() * this.YValue.getValue() / 100.0));
                    ((S12PacketEntityVelocity)p).setMotionZ((int)(((S12PacketEntityVelocity)p).getMotionZ() * this.XZValue.getValue() / 100.0));
                }
            }
            if (p instanceof S27PacketExplosion && this.ignoreExplosion.getBoolean()) {
                if (this.XZValue.getValue() <= 0.0 && this.YValue.getValue() <= 0.0) {
                    eventReadPacket.setCanceled(true);
                }
                else {
                    ((S27PacketExplosion)p).setField_149152_f((float)(int)(((S27PacketExplosion)p).getField_149152_f() * this.XZValue.getValue() / 100.0));
                    ((S27PacketExplosion)p).setField_149153_g((float)(int)(((S27PacketExplosion)p).getField_149152_f() * this.YValue.getValue() / 100.0));
                    ((S27PacketExplosion)p).setField_149159_h((float)(int)(((S27PacketExplosion)p).getField_149152_f() * this.XZValue.getValue() / 100.0));
                }
            }
        }
        if (packet instanceof S29PacketSoundEffect && this.hitBug.getBoolean()) {
            final S29PacketSoundEffect soundEffect = (S29PacketSoundEffect)packet;
            if (soundEffect.getSoundName().equalsIgnoreCase("game.player.hurt") || soundEffect.getSoundName().equalsIgnoreCase("game.player.die")) {
                eventReadPacket.setCanceled(true);
            }
        }
        if (packet instanceof S12PacketEntityVelocity && this.mode.getSelected().equals("Spoof")) {
            final S12PacketEntityVelocity s12PacketEntityVelocity = (S12PacketEntityVelocity)packet;
            if (s12PacketEntityVelocity.getEntityID() == Velocity.mc.thePlayer.getEntityId()) {
                eventReadPacket.setCanceled(true);
                Velocity.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Velocity.mc.thePlayer.posX + s12PacketEntityVelocity.getMotionX() / 8000.0, Velocity.mc.thePlayer.posY + s12PacketEntityVelocity.getMotionY() / 8000.0, Velocity.mc.thePlayer.posZ + s12PacketEntityVelocity.getMotionZ() / 8000.0, false));
            }
        }
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket eventSendPacket) {
        final Packet packet = eventSendPacket.getPacket();
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Grim2": {
                if (Velocity.mc.thePlayer.hurtTime != 0) {
                    this.grimFlag = true;
                }
                if (Velocity.mc.thePlayer.onGround) {
                    this.grimFlag = false;
                }
                if (this.grimFlag && packet instanceof C03PacketPlayer) {
                    ((C03PacketPlayer)packet).setX(Velocity.mc.thePlayer.posX + 210.0);
                    ((C03PacketPlayer)packet).setZ(Velocity.mc.thePlayer.posZ + 210.0);
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventSilentMove(final EventSilentMove eventSilentMove) {
        final String selected;
        final String var2 = selected = this.mode.getSelected();
        switch (selected) {
            case "BuzzReverse": {
                try {
                    if (Velocity.mc.thePlayer.hurtTime == 7) {
                        MoveUtil.multiplyXZ(-1.0);
                    }
                }
                catch (final Exception ex) {}
                break;
            }
            case "Legit": {
                if (Velocity.mc.thePlayer.hurtTime <= 0 || !Velocity.mm.killAura.isToggled()) {
                    break;
                }
                final KillAura killAura = Velocity.mm.killAura;
                if (KillAura.target != null) {
                    final ArrayList<Vec3> vec3s = new ArrayList<Vec3>();
                    final HashMap<Vec3, Integer> map = new HashMap<Vec3, Integer>();
                    final Vec3 playerPos = new Vec3(Velocity.mc.thePlayer.posX, Velocity.mc.thePlayer.posY, Velocity.mc.thePlayer.posZ);
                    final boolean isHitting = false;
                    final KillAura killAura2 = Velocity.mm.killAura;
                    final Vec3 onlyForward = PlayerUtil.getPredictedPos(isHitting, KillAura.target, 1.0f, 0.0f).add(playerPos);
                    final boolean isHitting2 = false;
                    final KillAura killAura3 = Velocity.mm.killAura;
                    final Vec3 strafeLeft = PlayerUtil.getPredictedPos(isHitting2, KillAura.target, 1.0f, 1.0f).add(playerPos);
                    final boolean isHitting3 = false;
                    final KillAura killAura4 = Velocity.mm.killAura;
                    final Vec3 strafeRight = PlayerUtil.getPredictedPos(isHitting3, KillAura.target, 1.0f, -1.0f).add(playerPos);
                    map.put(onlyForward, 0);
                    map.put(strafeLeft, 1);
                    map.put(strafeRight, -1);
                    vec3s.add(onlyForward);
                    vec3s.add(strafeLeft);
                    vec3s.add(strafeRight);
                    final KillAura killAura5 = Velocity.mm.killAura;
                    final double posX = KillAura.target.posX;
                    final KillAura killAura6 = Velocity.mm.killAura;
                    final double posY = KillAura.target.posY;
                    final KillAura killAura7 = Velocity.mm.killAura;
                    final Vec3 targetVec = new Vec3(posX, posY, KillAura.target.posZ);
                    vec3s.sort(Comparator.comparingDouble((ToDoubleFunction<? super Vec3>)targetVec::distanceXZTo));
                    if (!Velocity.mc.thePlayer.movementInput.sneak) {
                        System.out.println(map.get(vec3s.get(0)));
                        Velocity.mc.thePlayer.movementInput.moveStrafe = map.get(vec3s.get(0));
                    }
                    break;
                }
                break;
            }
            case "Intave": {
                if (this.jumpIntave.getBoolean() && Velocity.mc.thePlayer.hurtTime == 9 && Velocity.mc.thePlayer.onGround && this.counter++ % 2 == 0) {
                    Velocity.mc.thePlayer.movementInput.jump = true;
                    break;
                }
                break;
            }
            case "Test": {
                if (Velocity.mc.thePlayer.hurtTime <= 2) {
                    break;
                }
                MoveUtil.setSpeed(0.01f);
                if (Velocity.mc.thePlayer.hurtTime == 9 && Velocity.mc.thePlayer.onGround) {
                    Velocity.mc.thePlayer.movementInput.jump = true;
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventPostMotion(final EventPostMotion eventPostMotion) {
        if (this.mode.getSelected().equals("Reverse") && this.reverseStrafe.getBoolean() && Velocity.mc.thePlayer.hurtTime <= this.reverseStart.getValue() && Velocity.mc.thePlayer.hurtTime > 0) {
            MoveUtil.strafe();
        }
    }
    
    @EventTarget
    public void onEventUpdate(final EventUpdate eventUpdate) {
        this.setDisplayName(this.getName() + " §8" + this.mode.getSelected());
        final String selected;
        final String var2 = selected = this.mode.getSelected();
        switch (selected) {
            case "Grim2": {
                if (Velocity.mc.thePlayer.hurtTime != 0) {
                    Velocity.mc.thePlayer.setPosition(Velocity.mc.thePlayer.lastTickPosX, Velocity.mc.thePlayer.lastTickPosY, Velocity.mc.thePlayer.lastTickPosZ);
                    break;
                }
                break;
            }
            case "TickZero": {
                if (Velocity.mc.thePlayer.hurtTime == this.karhuStart.getValue()) {
                    MoveUtil.multiplyXZ(0.0);
                    break;
                }
                break;
            }
            case "PushGround": {
                this.pushGround();
                break;
            }
            case "Push": {
                this.push();
                break;
            }
            case "Reverse": {
                this.reverse();
                break;
            }
        }
    }
    
    private void reverse() {
        if (Velocity.mc.thePlayer.hurtTime == this.reverseStart.getValue()) {
            final EntityPlayerSP thePlayer = Velocity.mc.thePlayer;
            thePlayer.motionX *= -1.0;
            final EntityPlayerSP thePlayer2 = Velocity.mc.thePlayer;
            thePlayer2.motionZ *= -1.0;
            if (this.reverseStrafe.getBoolean() && Velocity.mc.thePlayer.hurtTime <= this.reverseStart.getValue() && Velocity.mc.thePlayer.hurtTime > 0) {
                MoveUtil.strafe();
            }
        }
    }
    
    private void push() {
        if (Velocity.mc.thePlayer.hurtTime <= Math.max(this.pushStart.getValue(), this.pushEnd.getValue()) && Velocity.mc.thePlayer.hurtTime >= Math.min(this.pushStart.getValue(), this.pushEnd.getValue())) {
            Velocity.mc.thePlayer.moveFlying(0.0f, 0.98f, (float)(this.pushXZ.getValue() / 100.0));
            if (this.pushOnGround.getBoolean()) {
                Velocity.mc.thePlayer.onGround = true;
            }
        }
    }
    
    private void pushGround() {
        if (Velocity.mc.thePlayer.hurtTime > 0) {
            Velocity.mc.thePlayer.onGround = true;
        }
    }
}
