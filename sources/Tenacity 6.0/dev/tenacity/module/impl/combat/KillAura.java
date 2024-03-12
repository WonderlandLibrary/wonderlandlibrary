// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.combat;

import java.util.ArrayList;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.event.impl.render.Render3DEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import dev.tenacity.event.impl.player.StrafeEvent;
import dev.tenacity.event.impl.player.KeepSprintEvent;
import dev.tenacity.event.impl.player.JumpFixEvent;
import dev.tenacity.event.impl.player.PlayerMoveUpdateEvent;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import dev.tenacity.commands.impl.FriendCommand;
import java.util.Iterator;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.entity.player.EntityPlayer;
import dev.tenacity.viamcp.utils.AttackOrder;
import dev.tenacity.event.Event;
import dev.tenacity.event.impl.player.AttackEvent;
import dev.tenacity.utils.misc.MathUtils;
import net.minecraft.entity.Entity;
import dev.tenacity.utils.player.RotationUtils;
import dev.tenacity.utils.player.InventoryUtils;
import dev.tenacity.module.impl.player.Scaffold;
import dev.tenacity.Tenacity;
import dev.tenacity.event.impl.player.MotionEvent;
import net.minecraft.network.Packet;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import dev.tenacity.module.api.TargetManager;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import java.awt.Color;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.Category;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.utils.time.TimerUtil;
import net.minecraft.entity.EntityLivingBase;
import java.util.List;
import dev.tenacity.module.Module;

public final class KillAura extends Module
{
    public static boolean attacking;
    public static boolean blocking;
    public static boolean wasBlocking;
    private float yaw;
    private int cps;
    public static final List<EntityLivingBase> targets;
    private final TimerUtil attackTimer;
    private final TimerUtil switchTimer;
    private final ModeSetting mode;
    private final NumberSetting switchDelay;
    private final NumberSetting maxTargetAmount;
    private final NumberSetting minCPS;
    private final NumberSetting maxCPS;
    private final NumberSetting reach;
    private final ModeSetting autoBlockMode;
    private final ModeSetting rotationMode;
    private final ModeSetting sortMode;
    private final MultipleBoolSetting addons;
    private final MultipleBoolSetting auraESP;
    private final ColorSetting customColor;
    private EntityLivingBase auraESPTarget;
    private final Animation auraESPAnim;
    
    public KillAura() {
        super("KillAura", "Kill Aura", Category.COMBAT, "Automatically attacks players");
        this.yaw = 0.0f;
        this.attackTimer = new TimerUtil();
        this.switchTimer = new TimerUtil();
        this.mode = new ModeSetting("Mode", "Single", new String[] { "Single", "Switch", "Multi" });
        this.switchDelay = new NumberSetting("Switch Delay", 50.0, 500.0, 1.0, 1.0);
        this.maxTargetAmount = new NumberSetting("Max Target Amount", 3.0, 50.0, 2.0, 1.0);
        this.minCPS = new NumberSetting("Min CPS", 10.0, 20.0, 1.0, 1.0);
        this.maxCPS = new NumberSetting("Max CPS", 10.0, 20.0, 1.0, 1.0);
        this.reach = new NumberSetting("Reach", 4.0, 6.0, 3.0, 0.1);
        this.autoBlockMode = new ModeSetting("AutoBlock Mode", "Off", new String[] { "Fake", "Verus", "Watchdog", "Mospixel", "Off" });
        this.rotationMode = new ModeSetting("Rotation Mode", "Vanilla", new String[] { "Vanilla", "Smooth", "Off" });
        this.sortMode = new ModeSetting("Sort Mode", "Range", new String[] { "Range", "Hurt Time", "Health", "Armor" });
        this.addons = new MultipleBoolSetting("Addons", new BooleanSetting[] { new BooleanSetting("Keep Sprint", true), new BooleanSetting("Through Walls", true), new BooleanSetting("Allow Scaffold", false), new BooleanSetting("Movement Fix", false), new BooleanSetting("Ray Cast", false) });
        this.auraESP = new MultipleBoolSetting("Target ESP", new BooleanSetting[] { new BooleanSetting("Circle", true), new BooleanSetting("Tracer", false), new BooleanSetting("Box", false), new BooleanSetting("Custom Color", false) });
        this.customColor = new ColorSetting("Custom Color", Color.WHITE);
        this.auraESPAnim = new DecelerateAnimation(300, 1.0);
        this.switchDelay.addParent(this.mode, m -> this.mode.is("Switch"));
        this.maxTargetAmount.addParent(this.mode, m -> this.mode.is("Multi"));
        this.customColor.addParent(this.auraESP, r -> r.isEnabled("Custom Color"));
        this.addSettings(this.mode, this.maxTargetAmount, this.switchDelay, this.minCPS, this.maxCPS, this.reach, this.autoBlockMode, this.rotationMode, this.sortMode, this.addons, this.auraESP, this.customColor);
    }
    
    @Override
    public void onDisable() {
        TargetManager.target = null;
        KillAura.targets.clear();
        KillAura.blocking = false;
        KillAura.attacking = false;
        if (KillAura.wasBlocking) {
            PacketUtils.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        KillAura.wasBlocking = false;
        super.onDisable();
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        this.setSuffix(this.mode.getMode());
        if (this.minCPS.getValue() > this.maxCPS.getValue()) {
            this.minCPS.setValue(this.minCPS.getValue() - 1.0);
        }
        this.sortTargets();
        if (event.isPre()) {
            KillAura.attacking = (!KillAura.targets.isEmpty() && (this.addons.getSetting("Allow Scaffold").isEnabled() || !Tenacity.INSTANCE.isEnabled(Scaffold.class)));
            KillAura.blocking = (!this.autoBlockMode.is("Off") && KillAura.attacking && InventoryUtils.isHoldingSword());
            if (KillAura.attacking) {
                TargetManager.target = KillAura.targets.get(0);
                if (!this.rotationMode.is("Off")) {
                    float[] rotations = { 0.0f, 0.0f };
                    final String mode = this.rotationMode.getMode();
                    switch (mode) {
                        case "Vanilla": {
                            rotations = RotationUtils.getRotationsNeeded(TargetManager.target);
                            break;
                        }
                        case "Smooth": {
                            rotations = RotationUtils.getSmoothRotations(TargetManager.target);
                            break;
                        }
                    }
                    this.yaw = event.getYaw();
                    event.setRotations(rotations[0], rotations[1]);
                    RotationUtils.setVisualRotations(event.getYaw(), event.getPitch());
                }
                if (this.addons.getSetting("Ray Cast").isEnabled() && !RotationUtils.isMouseOver(event.getYaw(), event.getPitch(), TargetManager.target, this.reach.getValue().floatValue())) {
                    return;
                }
                if (this.attackTimer.hasTimeElapsed(this.cps, true)) {
                    final int maxValue = (int)((this.minCPS.getMaxValue() - this.maxCPS.getValue()) * 20.0);
                    final int minValue = (int)((this.minCPS.getMaxValue() - this.minCPS.getValue()) * 20.0);
                    this.cps = MathUtils.getRandomInRange(minValue, maxValue);
                    if (this.mode.is("Multi")) {
                        for (final EntityLivingBase entityLivingBase : KillAura.targets) {
                            final AttackEvent attackEvent = new AttackEvent(entityLivingBase);
                            Tenacity.INSTANCE.getEventProtocol().handleEvent(attackEvent);
                            if (!attackEvent.isCancelled()) {
                                AttackOrder.sendFixedAttack(KillAura.mc.thePlayer, entityLivingBase);
                            }
                        }
                    }
                    else {
                        final AttackEvent attackEvent2 = new AttackEvent(TargetManager.target);
                        Tenacity.INSTANCE.getEventProtocol().handleEvent(attackEvent2);
                        if (!attackEvent2.isCancelled()) {
                            AttackOrder.sendFixedAttack(KillAura.mc.thePlayer, TargetManager.target);
                        }
                    }
                }
            }
            else {
                TargetManager.target = null;
                this.switchTimer.reset();
            }
        }
        if (KillAura.blocking) {
            final String mode2 = this.autoBlockMode.getMode();
            switch (mode2) {
                case "Mospixel": {
                    if (event.isPre() && KillAura.wasBlocking) {
                        KillAura.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-4, -7, -10), EnumFacing.SOUTH));
                    }
                }
                case "Watchdog": {
                    if (event.isPre() && KillAura.wasBlocking && KillAura.mc.thePlayer.ticksExisted % 4 == 0) {
                        PacketUtils.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        KillAura.wasBlocking = false;
                    }
                    if (event.isPost() && !KillAura.wasBlocking) {
                        PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, KillAura.mc.thePlayer.getHeldItem(), 255.0f, 255.0f, 255.0f));
                        KillAura.wasBlocking = true;
                        break;
                    }
                    break;
                }
                case "Verus": {
                    if (event.isPre()) {
                        if (KillAura.wasBlocking) {
                            PacketUtils.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        }
                        PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(KillAura.mc.thePlayer.getHeldItem()));
                        KillAura.wasBlocking = true;
                        break;
                    }
                    break;
                }
            }
        }
        else if (KillAura.wasBlocking && this.autoBlockMode.is("Watchdog") && event.isPre()) {
            PacketUtils.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            KillAura.wasBlocking = false;
        }
    }
    
    private void sortTargets() {
        KillAura.targets.clear();
        for (final Entity entity : KillAura.mc.theWorld.getLoadedEntityList()) {
            if (entity instanceof EntityLivingBase) {
                final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
                if (KillAura.mc.thePlayer.getDistanceToEntity(entity) > this.reach.getValue() || !this.isValid(entity) || KillAura.mc.thePlayer == entityLivingBase || FriendCommand.isFriend(entityLivingBase.getName())) {
                    continue;
                }
                KillAura.targets.add(entityLivingBase);
            }
        }
        final String mode = this.sortMode.getMode();
        switch (mode) {
            case "Range": {
                KillAura.targets.sort(Comparator.comparingDouble((ToDoubleFunction<? super EntityLivingBase>)KillAura.mc.thePlayer::getDistanceToEntity));
                break;
            }
            case "Hurt Time": {
                KillAura.targets.sort(Comparator.comparingInt(EntityLivingBase::getHurtTime));
                break;
            }
            case "Health": {
                KillAura.targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;
            }
            case "Armor": {
                KillAura.targets.sort(Comparator.comparingInt(EntityLivingBase::getTotalArmorValue));
                break;
            }
        }
    }
    
    public boolean isValid(final Entity entity) {
        return (!this.addons.isEnabled("Through Walls") || KillAura.mc.thePlayer.canEntityBeSeen(entity)) && TargetManager.checkEntity(entity);
    }
    
    @Override
    public void onPlayerMoveUpdateEvent(final PlayerMoveUpdateEvent event) {
        if (this.addons.getSetting("Movement Fix").isEnabled() && TargetManager.target != null) {
            event.setYaw(this.yaw);
        }
    }
    
    @Override
    public void onJumpFixEvent(final JumpFixEvent event) {
        if (this.addons.getSetting("Movement Fix").isEnabled() && TargetManager.target != null) {
            event.setYaw(this.yaw);
        }
    }
    
    @Override
    public void onKeepSprintEvent(final KeepSprintEvent event) {
        if (this.addons.getSetting("Keep Sprint").isEnabled()) {
            event.cancel();
        }
    }
    
    @Override
    public void onStrafeEvent(final StrafeEvent event) {
        if (this.addons.getSetting("Movement Fix").isEnabled()) {
            event.cancel();
            silentRotationStrafe(event, this.yaw);
        }
    }
    
    public static void silentRotationStrafe(final StrafeEvent event, final float yaw) {
        final int dif = (int)((MathHelper.wrapAngleTo180_float(KillAura.mc.thePlayer.rotationYaw - yaw - 23.5f - 135.0f) + 180.0f) / 45.0f);
        final float strafe = event.getStrafe();
        final float forward = event.getForward();
        final float friction = event.getFriction();
        float calcForward = 0.0f;
        float calcStrafe = 0.0f;
        switch (dif) {
            case 0: {
                calcForward = forward;
                calcStrafe = strafe;
                break;
            }
            case 1: {
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            }
            case 2: {
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            }
            case 3: {
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            }
            case 4: {
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            }
            case 5: {
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            }
            case 6: {
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            }
            case 7: {
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
                break;
            }
        }
        if (calcForward > 1.0f || (calcForward < 0.9f && calcForward > 0.3f) || calcForward < -1.0f || (calcForward > -0.9f && calcForward < -0.3f)) {
            calcForward *= 0.5f;
        }
        if (calcStrafe > 1.0f || (calcStrafe < 0.9f && calcStrafe > 0.3f) || calcStrafe < -1.0f || (calcStrafe > -0.9f && calcStrafe < -0.3f)) {
            calcStrafe *= 0.5f;
        }
        float d;
        if ((d = calcStrafe * calcStrafe + calcForward * calcForward) >= 1.0E-4f) {
            if ((d = MathHelper.sqrt_float(d)) < 1.0f) {
                d = 1.0f;
            }
            d = friction / d;
            final float yawSin = MathHelper.sin((float)(yaw * 3.141592653589793 / 180.0));
            final float yawCos = MathHelper.cos((float)(yaw * 3.141592653589793 / 180.0));
            final EntityPlayerSP thePlayer = KillAura.mc.thePlayer;
            thePlayer.motionX += (calcStrafe *= d) * yawCos - (calcForward *= d) * yawSin;
            final EntityPlayerSP thePlayer2 = KillAura.mc.thePlayer;
            thePlayer2.motionZ += calcForward * yawCos + calcStrafe * yawSin;
        }
    }
    
    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        this.auraESPAnim.setDirection((TargetManager.target != null) ? Direction.FORWARDS : Direction.BACKWARDS);
        if (TargetManager.target != null) {
            this.auraESPTarget = TargetManager.target;
        }
        if (this.auraESPAnim.finished(Direction.BACKWARDS)) {
            this.auraESPTarget = null;
        }
        Color color = HUDMod.getClientColors().getFirst();
        if (this.auraESP.isEnabled("Custom Color")) {
            color = this.customColor.getColor();
        }
        if (this.auraESPTarget != null) {
            if (this.auraESP.getSetting("Box").isEnabled()) {
                RenderUtil.renderBoundingBox(this.auraESPTarget, color, this.auraESPAnim.getOutput().floatValue());
            }
            if (this.auraESP.getSetting("Circle").isEnabled()) {
                RenderUtil.drawCircle(this.auraESPTarget, event.getTicks(), 0.75, color.getRGB(), this.auraESPAnim.getOutput().floatValue());
            }
            if (this.auraESP.getSetting("Tracer").isEnabled()) {
                RenderUtil.drawTracerLine(this.auraESPTarget, 4.0f, Color.BLACK, this.auraESPAnim.getOutput().floatValue());
                RenderUtil.drawTracerLine(this.auraESPTarget, 2.5f, color, this.auraESPAnim.getOutput().floatValue());
            }
        }
    }
    
    static {
        targets = new ArrayList<EntityLivingBase>();
    }
}
