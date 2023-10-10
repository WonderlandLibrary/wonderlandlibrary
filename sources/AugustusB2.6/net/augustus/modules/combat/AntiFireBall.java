// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.combat;

import net.minecraft.entity.projectile.EntityFireball;
import net.augustus.utils.BlockUtil;
import net.augustus.events.EventSilentMove;
import net.augustus.events.EventJump;
import net.augustus.events.EventMove;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.lenni0451.eventapi.events.IEvent;
import net.augustus.utils.EventHandler;
import net.augustus.events.EventClickKillAura;
import net.minecraft.util.MovingObjectPosition;
import net.augustus.events.EventClick;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.augustus.utils.RandomUtil;
import java.util.Comparator;
import java.util.function.Predicate;
import net.augustus.events.EventEarlyTick;
import net.augustus.Augustus;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.entity.Entity;
import net.augustus.utils.TimeHelper;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.utils.RotationUtil;
import net.augustus.modules.Module;

public class AntiFireBall extends Module
{
    public final RotationUtil rotationUtil;
    public final BooleanValue rotate;
    public final DoubleValue yawSpeed;
    public final DoubleValue pitchSpeed;
    public final DoubleValue preRange;
    public final DoubleValue delay;
    public final DoubleValue range;
    public final BooleanValue moveFix;
    public final BooleanValue slowDown;
    public final DoubleValue ticksExisted;
    private final TimeHelper delayTimeHelper;
    public float[] rots;
    public float[] lastRots;
    private Object[] listOfTargets;
    private Entity fireball;
    
    public AntiFireBall() {
        super("AntiFireBall", new Color(232, 147, 0), Categorys.COMBAT);
        this.rotationUtil = new RotationUtil();
        this.rotate = new BooleanValue(8, "Rotate", this, true);
        this.yawSpeed = new DoubleValue(1, "YawSpeed", this, 60.0, 0.0, 180.0, 0);
        this.pitchSpeed = new DoubleValue(2, "PitchSpeed", this, 60.0, 0.0, 180.0, 0);
        this.preRange = new DoubleValue(3, "PreAimRange", this, 4.0, 0.0, 15.0, 1);
        this.delay = new DoubleValue(9, "Delay", this, 60.0, 0.0, 400.0, 0);
        this.range = new DoubleValue(7, "Range", this, 4.0, 0.0, 6.0, 2);
        this.moveFix = new BooleanValue(5, "MoveFix", this, true);
        this.slowDown = new BooleanValue(6, "SlowDown", this, true);
        this.ticksExisted = new DoubleValue(9, "TicksExisted", this, 10.0, 0.0, 20.0, 0);
        this.delayTimeHelper = new TimeHelper();
        this.rots = new float[2];
        this.lastRots = new float[2];
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.fireball = null;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.rots = new float[] { Augustus.getInstance().getYawPitchHelper().realYaw, Augustus.getInstance().getYawPitchHelper().realPitch };
        this.lastRots = new float[] { Augustus.getInstance().getYawPitchHelper().realLastYaw, Augustus.getInstance().getYawPitchHelper().realLastPitch };
    }
    
    @EventTarget
    public void onEventEarlyTick(final EventEarlyTick eventEarlyTick) {
        this.listOfTargets = null;
        this.fireball = null;
        this.listOfTargets = AntiFireBall.mc.theWorld.loadedEntityList.stream().filter((Predicate<? super Object>)this::canAttacked).sorted(Comparator.comparingDouble(entity -> AntiFireBall.mc.thePlayer.getDistanceToEntity(entity))).toArray();
        if (this.listOfTargets.length > 0) {
            this.fireball = (Entity)this.listOfTargets[0];
        }
        if (this.rotate.getBoolean()) {
            if (this.shouldFireBall()) {
                final float yawSpeed = (float)(this.yawSpeed.getValue() + RandomUtil.nextFloat(0.0f, 15.0f));
                final float pitchSpeed = (float)(this.yawSpeed.getValue() + RandomUtil.nextFloat(0.0f, 15.0f));
                final float[] floats = this.rotationUtil.faceEntityCustom(this.fireball, yawSpeed, pitchSpeed, this.rots[0], this.rots[1], "Basic", false, false, false, 0.95f, null, false, false, false, true, true);
                this.lastRots = this.rots;
                this.rots = floats;
                this.setRotation();
            }
            else {
                this.restRotation();
            }
        }
        else if (this.fireball != null) {
            final double range = AntiFireBall.mc.thePlayer.canEntityBeSeen(this.fireball) ? this.range.getValue() : Math.min(3.0, this.range.getValue());
            if (AntiFireBall.mc.thePlayer.getDistanceToEntity(this.fireball) < range && this.delayTimeHelper.reached((this.delay.getValue() != 0.0) ? ((long)(this.delay.getValue() + RandomUtil.nextSecureInt(0, 60))) : 0L)) {
                AntiFireBall.mc.thePlayer.swingItem();
                AntiFireBall.mc.playerController.attackEntity(AntiFireBall.mc.thePlayer, this.fireball);
                this.delayTimeHelper.reset();
            }
        }
    }
    
    @EventTarget
    public void onEventClick(final EventClick eventClick) {
        if (this.shouldFireBall() && this.rotate.getBoolean() && AntiFireBall.mc.objectMouseOver != null && AntiFireBall.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && this.delayTimeHelper.reached((this.delay.getValue() != 0.0) ? ((long)(this.delay.getValue() + RandomUtil.nextSecureInt(0, 60))) : 0L)) {
            eventClick.setCanceled(true);
            if (this.slowDown.getBoolean()) {
                AntiFireBall.mc.clickMouse();
            }
            else {
                AntiFireBall.mc.thePlayer.swingItem();
                if (AntiFireBall.mc.objectMouseOver.entityHit != null) {
                    final EventClickKillAura eventClickKillAura = new EventClickKillAura();
                    EventHandler.call(eventClickKillAura);
                    AntiFireBall.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(AntiFireBall.mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.ATTACK));
                }
            }
            this.delayTimeHelper.reset();
            AntiFireBall.mc.sendClickBlockToController(AntiFireBall.mc.currentScreen == null && AntiFireBall.mc.gameSettings.keyBindAttack.isKeyDown() && AntiFireBall.mc.inGameHasFocus);
        }
    }
    
    @EventTarget
    public void onEventMove(final EventMove eventMove) {
        if (this.shouldFireBall() && !this.moveFix.getBoolean() && this.rotate.getBoolean()) {
            eventMove.setYaw(Augustus.getInstance().getYawPitchHelper().realYaw);
        }
    }
    
    @EventTarget
    public void onEventJump(final EventJump eventJump) {
        if (this.shouldFireBall() && !this.moveFix.getBoolean() && this.rotate.getBoolean()) {
            eventJump.setYaw(Augustus.getInstance().getYawPitchHelper().realYaw);
        }
    }
    
    @EventTarget
    public void onEventSilentMove(final EventSilentMove eventSilentMove) {
        if (this.shouldFireBall() && this.moveFix.getBoolean() && this.rotate.getBoolean()) {
            eventSilentMove.setSilent(true);
        }
    }
    
    private boolean shouldFireBall() {
        return this.fireball != null && AntiFireBall.mm.killAura.target == null && !BlockUtil.isScaffoldToggled() && AntiFireBall.mc.currentScreen == null;
    }
    
    private boolean canAttacked(final Entity entity) {
        return entity instanceof EntityFireball && entity.ticksExisted > this.ticksExisted.getValue() && AntiFireBall.mc.thePlayer.getDistanceToEntity(entity) < this.preRange.getValue() && AntiFireBall.mc.thePlayer.getDistanceToEntity(entity) <= AntiFireBall.mc.thePlayer.getDistance(entity.prevPosX, entity.prevPosY, entity.prevPosZ);
    }
    
    private void setRotation() {
        if (!BlockUtil.isScaffoldToggled() && AntiFireBall.mm.killAura.target == null) {
            AntiFireBall.mc.thePlayer.rotationYaw = this.rots[0];
            AntiFireBall.mc.thePlayer.rotationPitch = this.rots[1];
            AntiFireBall.mc.thePlayer.prevRotationYaw = this.lastRots[0];
            AntiFireBall.mc.thePlayer.prevRotationPitch = this.lastRots[1];
        }
        else {
            this.restRotation();
        }
    }
    
    private void restRotation() {
        (this.rots = this.lastRots)[0] = Augustus.getInstance().getYawPitchHelper().realYaw;
        this.rots[1] = Augustus.getInstance().getYawPitchHelper().realPitch;
        this.lastRots[0] = Augustus.getInstance().getYawPitchHelper().realLastYaw;
        this.lastRots[1] = Augustus.getInstance().getYawPitchHelper().realLastPitch;
    }
}
