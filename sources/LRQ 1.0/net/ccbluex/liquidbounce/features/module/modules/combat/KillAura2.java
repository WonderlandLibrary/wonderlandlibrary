/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.comparisons.ComparisonsKt
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.IntProgression
 *  kotlin.ranges.IntRange
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.EnumParticleTypes
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.awt.Color;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import jx.utils.EaseUtils;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorldSettings;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EntityMovementEvent;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.modules.hyt.Animations;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RaycastUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

@ModuleInfo(name="KillAura2", description="Automatically attacks targets around you.", category=ModuleCategory.COMBAT, keyBind=19)
public final class KillAura2
extends Module {
    private final IntegerValue maxCPS = new IntegerValue(this, "MaxCPS", 8, 1, 20){
        final /* synthetic */ KillAura2 this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)KillAura2.access$getMinCPS$p(this.this$0).get()).intValue();
            if (i > newValue) {
                this.set(i);
            }
            KillAura2.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)KillAura2.access$getMinCPS$p(this.this$0).get()).intValue(), ((Number)this.get()).intValue()));
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final IntegerValue minCPS = new IntegerValue(this, "MinCPS", 5, 1, 20){
        final /* synthetic */ KillAura2 this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)KillAura2.access$getMaxCPS$p(this.this$0).get()).intValue();
            if (i < newValue) {
                this.set(i);
            }
            KillAura2.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)this.get()).intValue(), ((Number)KillAura2.access$getMaxCPS$p(this.this$0).get()).intValue()));
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
    private final FloatValue cooldownValue = new FloatValue("Cooldown", 1.0f, 0.0f, 1.0f);
    private final IntegerValue switchDelayValue = new IntegerValue("SwitchDelay", 700, 0, 2000);
    private final FloatValue rangeValue = new FloatValue("Range", 3.7f, 1.0f, 8.0f);
    private final FloatValue throughWallsRangeValue = new FloatValue("ThroughWallsRange", 3.0f, 0.0f, 8.0f);
    private final FloatValue rangeSprintReducementValue = new FloatValue("RangeSprintReducement", 0.0f, 0.0f, 0.4f);
    private final ListValue priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "Direction", "LivingTime"}, "Distance");
    private final ListValue targetModeValue = new ListValue("TargetMode", new String[]{"Single", "Switch", "Multi"}, "Switch");
    private final BoolValue swingValue = new BoolValue("Swing", true);
    private final BoolValue keepSprintValue = new BoolValue("KeepSprint", true);
    private final ListValue autoBlockValue = new ListValue("AutoBlock", new String[]{"Off", "Packet", "AfterTick", "Right", "AACBy1.12.2", "Old1.8.9", "Hyt1.12.2"}, "Packet");
    private final BoolValue interactAutoBlockValue = new BoolValue("InteractAutoBlock", true);
    private final IntegerValue blockRate = new IntegerValue("BlockRate", 100, 1, 100);
    private final BoolValue raycastValue = new BoolValue("RayCast", true);
    private final BoolValue raycastIgnoredValue = new BoolValue("RayCastIgnored", false);
    private final BoolValue livingRaycastValue = new BoolValue("LivingRayCast", true);
    private final BoolValue aacValue = new BoolValue("AAC", false);
    private final FloatValue maxTurnSpeed = new FloatValue(this, "MaxTurnSpeed", 180.0f, 0.0f, 180.0f){
        final /* synthetic */ KillAura2 this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura2.access$getMinTurnSpeed$p(this.this$0).get()).floatValue();
            if (v > newValue) {
                this.set(Float.valueOf(v));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final FloatValue minTurnSpeed = new FloatValue(this, "MinTurnSpeed", 180.0f, 0.0f, 180.0f){
        final /* synthetic */ KillAura2 this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura2.access$getMaxTurnSpeed$p(this.this$0).get()).floatValue();
            if (v < newValue) {
                this.set(Float.valueOf(v));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final ListValue rotations = new ListValue("RotationMode", new String[]{"None", "New", "BackTrack", "HytRotation"}, "HytRotation");
    private final BoolValue silentRotationValue = new BoolValue("SilentRotation", true);
    private final ListValue rotationStrafeValue = new ListValue("Strafe", new String[]{"Off", "Strict", "Silent"}, "Off");
    private final BoolValue randomCenterValue = new BoolValue("RandomCenter", true);
    private final BoolValue outborderValue = new BoolValue("Outborder", false);
    private final FloatValue fovValue = new FloatValue("FOV", 180.0f, 0.0f, 180.0f);
    private final BoolValue predictValue = new BoolValue("Predict", true);
    private final FloatValue maxPredictSize = new FloatValue(this, "MaxPredictSize", 1.0f, 0.1f, 5.0f){
        final /* synthetic */ KillAura2 this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura2.access$getMinPredictSize$p(this.this$0).get()).floatValue();
            if (v > newValue) {
                this.set(Float.valueOf(v));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final FloatValue minPredictSize = new FloatValue(this, "MinPredictSize", 1.0f, 0.1f, 5.0f){
        final /* synthetic */ KillAura2 this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura2.access$getMaxPredictSize$p(this.this$0).get()).floatValue();
            if (v < newValue) {
                this.set(Float.valueOf(v));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final FloatValue failRateValue = new FloatValue("FailRate", 0.0f, 0.0f, 100.0f);
    private final BoolValue fakeSwingValue = new BoolValue("FakeSwing", true);
    private final BoolValue noInventoryAttackValue = new BoolValue("NoInvAttack", false);
    private final IntegerValue noInventoryDelayValue = new IntegerValue("NoInvDelay", 200, 0, 500);
    private final IntegerValue limitedMultiTargetsValue = new IntegerValue("LimitedMultiTargets", 0, 0, 50);
    private final ListValue markValue = new ListValue("Mark", new String[]{"Liquid", "FDP", "Block", "Jello", "Plat", "Red", "Sims", "None"}, "FDP");
    private final ListValue hiteffect = new ListValue("HitEffect", new String[]{"Lightningbolt", "Criticals", "Blood", "Fire", "Water", "Smoke", "Flame", "Heart", "None"}, "Always");
    private final BoolValue lightingSoundValue = new BoolValue("LightingSound", true);
    private final BoolValue fakeSharpValue = new BoolValue("FakeSharp", true);
    private final BoolValue circleValue = new BoolValue("Circle", true);
    private final IntegerValue circleRedValue = new IntegerValue("CircleRed", 255, 0, 255);
    private final IntegerValue circleGreenValue = new IntegerValue("CircleGreen", 255, 0, 255);
    private final IntegerValue circleBlueValue = new IntegerValue("CircleBlue", 255, 0, 255);
    private final IntegerValue circleAlphaValue = new IntegerValue("CircleAlpha", 255, 0, 255);
    private final FloatValue circleThicknessValue = new FloatValue("CircleThickness", 2.0f, 1.0f, 5.0f);
    private IEntityLivingBase target$1;
    private IEntityLivingBase currentTarget;
    private boolean hitable;
    private final List<Integer> prevTargetEntities;
    private final MSTimer attackTimer;
    private final MSTimer switchTimer;
    private long attackDelay;
    private int clicks;
    private long containerOpen;
    private boolean blockingStatus;
    public static Object target;
    public static final Companion Companion;

    public final IntegerValue getHurtTimeValue() {
        return this.hurtTimeValue;
    }

    public final FloatValue getRangeValue() {
        return this.rangeValue;
    }

    public final BoolValue getKeepSprintValue() {
        return this.keepSprintValue;
    }

    public final ListValue getAutoBlockValue() {
        return this.autoBlockValue;
    }

    public final IEntityLivingBase getTarget() {
        return this.target$1;
    }

    public final void setTarget(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.target$1 = iEntityLivingBase;
    }

    public final boolean getBlockingStatus() {
        return this.blockingStatus;
    }

    public final void setBlockingStatus(boolean bl) {
        this.blockingStatus = bl;
    }

    @Override
    public void onEnable() {
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        if (MinecraftInstance.mc.getTheWorld() == null) {
            return;
        }
        this.updateTarget();
    }

    @Override
    public void onDisable() {
        this.target$1 = null;
        this.currentTarget = null;
        this.hitable = false;
        this.prevTargetEntities.clear();
        this.attackTimer.reset();
        this.clicks = 0;
        this.stopBlocking();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onMotion(MotionEvent event) {
        if (event.getEventState() == EventState.POST) {
            if (this.target$1 == null) return;
            if (this.currentTarget == null) return;
            this.updateHitable();
            if (StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"AACBy1.12.2", (boolean)true)) {
                MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().setPressed(true);
            }
            if (StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"Old1.8.9", (boolean)true)) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(new WBlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f));
            }
            if (StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"Hyt1.12.2", (boolean)true) && StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"Right", (boolean)true)) {
                new Robot().mousePress(4096);
            }
            if (!StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"AfterTick", (boolean)true)) return;
            KillAura2 this_$iv = this;
            boolean $i$f$getCanBlock = false;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getHeldItem() == null) return;
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            IItemStack iItemStack = iEntityPlayerSP2.getHeldItem();
            if (iItemStack == null) {
                Intrinsics.throwNpe();
            }
            if (!MinecraftInstance.classProvider.isItemSword(iItemStack.getItem())) return;
            boolean bl = true;
            if (!bl) return;
            IEntityLivingBase iEntityLivingBase = this.currentTarget;
            if (iEntityLivingBase == null) {
                Intrinsics.throwNpe();
            }
            this.startBlocking(iEntityLivingBase, this.hitable);
            return;
        }
        if (!StringsKt.equals((String)((String)this.rotationStrafeValue.get()), (String)"Off", (boolean)true)) return;
        this.update();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onStrafe(StrafeEvent event) {
        if (StringsKt.equals((String)((String)this.rotationStrafeValue.get()), (String)"Off", (boolean)true)) {
            return;
        }
        this.update();
        if (this.currentTarget == null || RotationUtils.targetRotation == null) return;
        String string = (String)this.rotationStrafeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        string = string2.toLowerCase();
        switch (string.hashCode()) {
            case -902327211: {
                if (!string.equals("silent")) return;
                break;
            }
            case -891986231: {
                if (!string.equals("strict")) return;
                Rotation rotation = RotationUtils.targetRotation;
                if (rotation == null) {
                    return;
                }
                Rotation rotation2 = rotation;
                float yaw = rotation2.component1();
                float strafe = event.getStrafe();
                float forward = event.getForward();
                float friction = event.getFriction();
                float f = strafe * strafe + forward * forward;
                if (f >= 1.0E-4f) {
                    IEntityPlayerSP player;
                    boolean bl2 = false;
                    if ((f = (float)Math.sqrt(f)) < 1.0f) {
                        f = 1.0f;
                    }
                    f = friction / f;
                    strafe *= f;
                    forward *= f;
                    float f2 = (float)((double)yaw * Math.PI / (double)180.0f);
                    boolean bl3 = false;
                    float yawSin = (float)Math.sin(f2);
                    float f3 = (float)((double)yaw * Math.PI / (double)180.0f);
                    boolean bl4 = false;
                    float yawCos = (float)Math.cos(f3);
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntityPlayerSP iEntityPlayerSP2 = player = iEntityPlayerSP;
                    iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() + (double)(strafe * yawCos - forward * yawSin));
                    IEntityPlayerSP iEntityPlayerSP3 = player;
                    iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() + (double)(forward * yawCos + strafe * yawSin));
                }
                event.cancelEvent();
                return;
            }
        }
        this.update();
        RotationUtils.targetRotation.applyStrafeToPlayer(event);
        event.cancelEvent();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final void update() {
        KillAura2 this_$iv = this;
        boolean $i$f$getCancelRun = false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isSpectator()) return;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        if (!this_$iv.isAlive(iEntityPlayerSP2)) return;
        if (LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState()) return;
        if (LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) {
            return;
        }
        boolean bl = false;
        if (bl) return;
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue()) {
            if (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen())) return;
            if (System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue()) {
                return;
            }
        }
        this.updateTarget();
        if (this.target$1 == null) {
            this.stopBlocking();
            return;
        }
        this.currentTarget = this.target$1;
        if (StringsKt.equals((String)((String)this.targetModeValue.get()), (String)"Switch", (boolean)true)) return;
        if (!this.isEnemy(this.currentTarget)) return;
        this.target$1 = this.currentTarget;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        this_$iv = this;
        $i$f$getCancelRun = false;
        v0 = MinecraftInstance.mc.getThePlayer();
        if (v0 == null) {
            Intrinsics.throwNpe();
        }
        if (v0.isSpectator()) ** GOTO lbl-1000
        v1 = MinecraftInstance.mc.getThePlayer();
        if (v1 == null) {
            Intrinsics.throwNpe();
        }
        if (!KillAura2.access$isAlive(this_$iv, v1) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState() || LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) lbl-1000:
        // 2 sources

        {
            v2 = true;
        } else {
            v2 = false;
        }
        if (v2) {
            this.target$1 = null;
            this.currentTarget = null;
            this.hitable = false;
            this.stopBlocking();
            return;
        }
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()) || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            this.target$1 = null;
            this.currentTarget = null;
            this.hitable = false;
            if (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen())) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        if (this.target$1 != null && this.currentTarget != null) {
            v3 = MinecraftInstance.mc.getThePlayer();
            if (v3 == null) {
                Intrinsics.throwNpe();
            }
            if (v3.getCooledAttackStrength(0.0f) >= ((Number)this.cooldownValue.get()).floatValue()) {
                while (this.clicks > 0) {
                    this.runAttack();
                    var2_3 = this.clicks;
                    this.clicks = var2_3 + -1;
                }
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender3D(Render3DEvent event) {
        if (((Boolean)this.circleValue.get()).booleanValue()) {
            GL11.glPushMatrix();
            v0 = MinecraftInstance.mc.getThePlayer();
            if (v0 == null) {
                Intrinsics.throwNpe();
            }
            v1 = v0.getLastTickPosX();
            v2 = MinecraftInstance.mc.getThePlayer();
            if (v2 == null) {
                Intrinsics.throwNpe();
            }
            v3 = v2.getPosX();
            v4 = MinecraftInstance.mc.getThePlayer();
            if (v4 == null) {
                Intrinsics.throwNpe();
            }
            v5 = v1 + (v3 - v4.getLastTickPosX()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosX();
            v6 = MinecraftInstance.mc.getThePlayer();
            if (v6 == null) {
                Intrinsics.throwNpe();
            }
            v7 = v6.getLastTickPosY();
            v8 = MinecraftInstance.mc.getThePlayer();
            if (v8 == null) {
                Intrinsics.throwNpe();
            }
            v9 = v8.getPosY();
            v10 = MinecraftInstance.mc.getThePlayer();
            if (v10 == null) {
                Intrinsics.throwNpe();
            }
            v11 = v7 + (v9 - v10.getLastTickPosY()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosY();
            v12 = MinecraftInstance.mc.getThePlayer();
            if (v12 == null) {
                Intrinsics.throwNpe();
            }
            v13 = v12.getLastTickPosZ();
            v14 = MinecraftInstance.mc.getThePlayer();
            if (v14 == null) {
                Intrinsics.throwNpe();
            }
            v15 = v14.getPosZ();
            v16 = MinecraftInstance.mc.getThePlayer();
            if (v16 == null) {
                Intrinsics.throwNpe();
            }
            GL11.glTranslated((double)v5, (double)v11, (double)(v13 + (v15 - v16.getLastTickPosZ()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosZ()));
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)((Number)this.circleThicknessValue.get()).floatValue());
            GL11.glColor4f((float)((float)((Number)this.circleRedValue.get()).intValue() / 255.0f), (float)((float)((Number)this.circleGreenValue.get()).intValue() / 255.0f), (float)((float)((Number)this.circleBlueValue.get()).intValue() / 255.0f), (float)((float)((Number)this.circleAlphaValue.get()).intValue() / 255.0f));
            GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glBegin((int)3);
            var5_2 = 0;
            v17 = RangesKt.step((IntProgression)((IntProgression)new IntRange(var5_2, 360)), (int)5);
            var2_4 = v17.getFirst();
            var3_6 = v17.getLast();
            var4_9 = v17.getStep();
            v18 = var2_4;
            v19 = var3_6;
            if (var4_9 >= 0 ? v18 <= v19 : v18 >= v19) {
                while (true) {
                    var5_3 = (double)i * 3.141592653589793 / 180.0;
                    var7_13 = 0;
                    v20 = (float)Math.cos(var5_3) * ((Number)this.rangeValue.get()).floatValue();
                    var5_3 = (double)i * 3.141592653589793 / 180.0;
                    var31_14 = v20;
                    var7_13 = 0;
                    var32_12 = Math.sin(var5_3);
                    GL11.glVertex2f((float)var31_14, (float)((float)var32_12 * ((Number)this.rangeValue.get()).floatValue()));
                    if (i == var3_6) break;
                    i += var4_9;
                }
            }
            GL11.glEnd();
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glPopMatrix();
        }
        this_$iv = this;
        $i$f$getCancelRun = false;
        v21 = MinecraftInstance.mc.getThePlayer();
        if (v21 == null) {
            Intrinsics.throwNpe();
        }
        if (v21.isSpectator()) ** GOTO lbl-1000
        v22 = MinecraftInstance.mc.getThePlayer();
        if (v22 == null) {
            Intrinsics.throwNpe();
        }
        if (!KillAura2.access$isAlive(this_$iv, v22) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState() || LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) lbl-1000:
        // 2 sources

        {
            v23 = true;
        } else {
            v23 = false;
        }
        if (v23) {
            this.target$1 = null;
            this.currentTarget = null;
            this.hitable = false;
            this.stopBlocking();
            return;
        }
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()) || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            this.target$1 = null;
            this.currentTarget = null;
            this.hitable = false;
            if (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen())) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        if (this.target$1 == null) {
            return;
        }
        v24 = MinecraftInstance.mc.getTheWorld();
        if (v24 == null) {
            Intrinsics.throwNpe();
        }
        v25 = v24;
        v26 = this.target$1;
        if (v26 == null) {
            Intrinsics.throwNpe();
        }
        v27 = v26.getPosX();
        v28 = this.target$1;
        if (v28 == null) {
            Intrinsics.throwNpe();
        }
        v29 = v28.getPosY();
        v30 = this.target$1;
        if (v30 == null) {
            Intrinsics.throwNpe();
        }
        ent = MinecraftInstance.classProvider.createEntityLightningBolt(v25, v27, v29, v30.getPosZ(), false);
        var3_7 = (String)this.hiteffect.get();
        var4_9 = 0;
        v31 = var3_7;
        if (v31 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        var3_7 = v31.toLowerCase();
        tmp = -1;
        switch (var3_7.hashCode()) {
            case 851318879: {
                if (!var3_7.equals("lightningbolt")) break;
                tmp = 1;
                break;
            }
            case 387153076: {
                if (!var3_7.equals("criticals")) break;
                tmp = 2;
                break;
            }
            case 109562223: {
                if (!var3_7.equals("smoke")) break;
                tmp = 3;
                break;
            }
            case 3143222: {
                if (!var3_7.equals("fire")) break;
                tmp = 4;
                break;
            }
            case 97513267: {
                if (!var3_7.equals("flame")) break;
                tmp = 5;
                break;
            }
            case 112903447: {
                if (!var3_7.equals("water")) break;
                tmp = 6;
                break;
            }
            case 93832698: {
                if (!var3_7.equals("blood")) break;
                tmp = 7;
                break;
            }
            case 99151942: {
                if (!var3_7.equals("heart")) break;
                tmp = 8;
                break;
            }
        }
        switch (tmp) {
            case 1: {
                v32 = MinecraftInstance.mc.getTheWorld();
                if (v32 == null) {
                    Intrinsics.throwNpe();
                }
                v32.addEntityToWorld(-1, ent);
                if (!((Boolean)this.lightingSoundValue.get()).booleanValue()) break;
                MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 1.0f);
                MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.thunder", 1.0f);
                break;
            }
            case 5: {
                v33 = MinecraftInstance.mc.getEffectRenderer();
                v34 = this.target$1;
                if (v34 == null) {
                    Intrinsics.throwNpe();
                }
                v33.emitParticleAtEntity(v34, EnumParticleTypes.FLAME);
                break;
            }
            case 3: {
                v35 = MinecraftInstance.mc.getEffectRenderer();
                v36 = this.target$1;
                if (v36 == null) {
                    Intrinsics.throwNpe();
                }
                v35.emitParticleAtEntity(v36, EnumParticleTypes.SMOKE_LARGE);
                break;
            }
            case 8: {
                v37 = MinecraftInstance.mc.getEffectRenderer();
                v38 = this.target$1;
                if (v38 == null) {
                    Intrinsics.throwNpe();
                }
                v37.emitParticleAtEntity(v38, EnumParticleTypes.HEART);
                break;
            }
            case 4: {
                v39 = MinecraftInstance.mc.getEffectRenderer();
                v40 = this.target$1;
                if (v40 == null) {
                    Intrinsics.throwNpe();
                }
                v39.emitParticleAtEntity(v40, EnumParticleTypes.LAVA);
                break;
            }
            case 6: {
                v41 = MinecraftInstance.mc.getEffectRenderer();
                v42 = this.target$1;
                if (v42 == null) {
                    Intrinsics.throwNpe();
                }
                v41.emitParticleAtEntity(v42, EnumParticleTypes.WATER_DROP);
                break;
            }
            case 2: {
                v43 = MinecraftInstance.mc.getEffectRenderer();
                v44 = this.target$1;
                if (v44 == null) {
                    Intrinsics.throwNpe();
                }
                v43.emitParticleAtEntity(v44, EnumParticleTypes.CRIT);
                break;
            }
            case 7: {
                var4_9 = 10;
                var5_2 = 0;
                var6_17 = 0;
                var7_13 = var4_9;
                for (var6_17 = 0; var6_17 < var7_13; ++var6_17) {
                    it = var6_17;
                    $i$a$-repeat-KillAura2$onRender3D$1 = false;
                    v45 = MinecraftInstance.mc.getEffectRenderer();
                    v46 = EnumParticleTypes.BLOCK_CRACK.func_179348_c();
                    v47 = this.target$1;
                    if (v47 == null) {
                        Intrinsics.throwNpe();
                    }
                    v48 = v47.getPosX();
                    v49 = this.target$1;
                    if (v49 == null) {
                        Intrinsics.throwNpe();
                    }
                    v50 = v49.getPosY();
                    v51 = this.target$1;
                    if (v51 == null) {
                        Intrinsics.throwNpe();
                    }
                    v52 = v50 + (double)(v51.getHeight() / (float)2);
                    v53 = this.target$1;
                    if (v53 == null) {
                        Intrinsics.throwNpe();
                    }
                    v54 = v53.getPosZ();
                    v55 = this.target$1;
                    if (v55 == null) {
                        Intrinsics.throwNpe();
                    }
                    v56 = v55.getMotionX() + (double)RandomUtils.INSTANCE.nextFloat(-0.5f, 0.5f);
                    v57 = this.target$1;
                    if (v57 == null) {
                        Intrinsics.throwNpe();
                    }
                    v58 = v57.getMotionY() + (double)RandomUtils.INSTANCE.nextFloat(-0.5f, 0.5f);
                    v59 = this.target$1;
                    if (v59 == null) {
                        Intrinsics.throwNpe();
                    }
                    v45.spawnEffectParticle(v46, v48, v52, v54, v56, v58, v59.getMotionZ() + (double)RandomUtils.INSTANCE.nextFloat(-0.5f, 0.5f), Block.func_176210_f((IBlockState)Blocks.field_150451_bX.func_176223_P()));
                }
                break;
            }
        }
        var3_7 = (String)this.markValue.get();
        var4_9 = 0;
        v60 = var3_7;
        if (v60 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        var3_7 = v60.toLowerCase();
        tmp = -1;
        switch (var3_7.hashCode()) {
            case 112785: {
                if (!var3_7.equals("red")) break;
                tmp = 1;
                break;
            }
            case 101009364: {
                if (!var3_7.equals("jello")) break;
                tmp = 2;
                break;
            }
            case 101234: {
                if (!var3_7.equals("fdp")) break;
                tmp = 3;
                break;
            }
            case -1102567108: {
                if (!var3_7.equals("liquid")) break;
                tmp = 4;
                break;
            }
            case 3530364: {
                if (!var3_7.equals("sims")) break;
                tmp = 5;
                break;
            }
            case 93832333: {
                if (!var3_7.equals("block")) break;
                tmp = 6;
                break;
            }
            case 3443503: {
                if (!var3_7.equals("plat")) break;
                tmp = 7;
                break;
            }
        }
        switch (tmp) {
            case 4: {
                v61 = this.target$1;
                if (v61 == null) {
                    Intrinsics.throwNpe();
                }
                v62 = v61;
                v63 = this.target$1;
                if (v63 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v62, v63.getHurtTime() <= 0 ? new Color(37, 126, 255, 170) : new Color(255, 0, 0, 170));
                break;
            }
            case 7: {
                v64 = this.target$1;
                if (v64 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v64, this.hitable != false ? new Color(37, 126, 255, 70) : new Color(255, 0, 0, 70));
                break;
            }
            case 6: {
                v65 = this.target$1;
                if (v65 == null) {
                    Intrinsics.throwNpe();
                }
                bb = v65.getEntityBoundingBox();
                v66 = this.target$1;
                if (v66 == null) {
                    Intrinsics.throwNpe();
                }
                v66.setEntityBoundingBox(bb.expand(0.2, 0.2, 0.2));
                v67 = this.target$1;
                if (v67 == null) {
                    Intrinsics.throwNpe();
                }
                v68 = v67;
                v69 = this.target$1;
                if (v69 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawEntityBox(v68, v69.getHurtTime() <= 0 ? Color.GREEN : Color.RED, true);
                v70 = this.target$1;
                if (v70 == null) {
                    Intrinsics.throwNpe();
                }
                v70.setEntityBoundingBox(bb);
                break;
            }
            case 1: {
                v71 = this.target$1;
                if (v71 == null) {
                    Intrinsics.throwNpe();
                }
                v72 = v71;
                v73 = this.target$1;
                if (v73 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v72, v73.getHurtTime() <= 0 ? new Color(255, 255, 255, 255) : new Color(124, 215, 255, 255));
                break;
            }
            case 5: {
                radius = 0.15f;
                side = 4;
                GL11.glPushMatrix();
                v74 = this.target$1;
                if (v74 == null) {
                    Intrinsics.throwNpe();
                }
                v75 = v74.getLastTickPosX();
                v76 = this.target$1;
                if (v76 == null) {
                    Intrinsics.throwNpe();
                }
                v77 = v76.getPosX();
                v78 = this.target$1;
                if (v78 == null) {
                    Intrinsics.throwNpe();
                }
                v79 = v75 + (v77 - v78.getLastTickPosX()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosX();
                v80 = this.target$1;
                if (v80 == null) {
                    Intrinsics.throwNpe();
                }
                v81 = v80.getLastTickPosY();
                v82 = this.target$1;
                if (v82 == null) {
                    Intrinsics.throwNpe();
                }
                v83 = v82.getPosY();
                v84 = this.target$1;
                if (v84 == null) {
                    Intrinsics.throwNpe();
                }
                v85 = v81 + (v83 - v84.getLastTickPosY()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosY();
                v86 = this.target$1;
                if (v86 == null) {
                    Intrinsics.throwNpe();
                }
                v87 = v85 + (double)v86.getHeight() * 1.1;
                v88 = this.target$1;
                if (v88 == null) {
                    Intrinsics.throwNpe();
                }
                v89 = v88.getLastTickPosZ();
                v90 = this.target$1;
                if (v90 == null) {
                    Intrinsics.throwNpe();
                }
                v91 = v90.getPosZ();
                v92 = this.target$1;
                if (v92 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glTranslated((double)v79, (double)v87, (double)(v89 + (v91 - v92.getLastTickPosZ()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                v93 = this.target$1;
                if (v93 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glRotatef((float)(-v93.getWidth()), (float)0.0f, (float)1.0f, (float)0.0f);
                v94 = MinecraftInstance.mc.getThePlayer();
                if (v94 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glRotatef((float)(((float)v94.getTicksExisted() + MinecraftInstance.mc.getTimer().getRenderPartialTicks()) * (float)5), (float)0.0f, (float)1.0f, (float)0.0f);
                v95 = this.target$1;
                if (v95 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.glColor(v95.getHurtTime() <= 0 ? new Color(80, 255, 80) : new Color(255, 0, 0));
                RenderUtils.enableSmoothLine(1.5f);
                c = new Cylinder();
                GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                c.draw(0.0f, radius, 0.3f, side, 1);
                c.setDrawStyle(100012);
                GL11.glTranslated((double)0.0, (double)0.0, (double)0.3);
                c.draw(radius, 0.0f, 0.3f, side, 1);
                GL11.glRotatef((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glTranslated((double)0.0, (double)0.0, (double)-0.3);
                c.draw(0.0f, radius, 0.3f, side, 1);
                GL11.glTranslated((double)0.0, (double)0.0, (double)0.3);
                c.draw(radius, 0.0f, 0.3f, side, 1);
                RenderUtils.disableSmoothLine();
                GL11.glPopMatrix();
                break;
            }
            case 3: {
                drawTime = (int)(System.currentTimeMillis() % (long)1500);
                drawMode = drawTime > 750;
                drawPercent = (double)drawTime / 750.0;
                drawPercent = !drawMode ? (double)true - drawPercent : (drawPercent -= (double)true);
                drawPercent = EaseUtils.easeInOutQuad(drawPercent);
                GL11.glPushMatrix();
                GL11.glDisable((int)3553);
                GL11.glEnable((int)2848);
                GL11.glEnable((int)2881);
                GL11.glEnable((int)2832);
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glHint((int)3154, (int)4354);
                GL11.glHint((int)3155, (int)4354);
                GL11.glHint((int)3153, (int)4354);
                GL11.glDisable((int)2929);
                GL11.glDepthMask((boolean)false);
                v96 = this.target$1;
                if (v96 == null) {
                    Intrinsics.throwNpe();
                }
                bb = v96.getEntityBoundingBox();
                radius = bb.getMaxX() - bb.getMinX() + 0.3;
                height = bb.getMaxY() - bb.getMinY();
                v97 = this.target$1;
                if (v97 == null) {
                    Intrinsics.throwNpe();
                }
                v98 = v97.getLastTickPosX();
                v99 = this.target$1;
                if (v99 == null) {
                    Intrinsics.throwNpe();
                }
                v100 = v99.getPosX();
                v101 = this.target$1;
                if (v101 == null) {
                    Intrinsics.throwNpe();
                }
                x = v98 + (v100 - v101.getLastTickPosX()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosX();
                v102 = this.target$1;
                if (v102 == null) {
                    Intrinsics.throwNpe();
                }
                v103 = v102.getLastTickPosY();
                v104 = this.target$1;
                if (v104 == null) {
                    Intrinsics.throwNpe();
                }
                v105 = v104.getPosY();
                v106 = this.target$1;
                if (v106 == null) {
                    Intrinsics.throwNpe();
                }
                y = v103 + (v105 - v106.getLastTickPosY()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosY() + height * drawPercent;
                v107 = this.target$1;
                if (v107 == null) {
                    Intrinsics.throwNpe();
                }
                v108 = v107.getLastTickPosZ();
                v109 = this.target$1;
                if (v109 == null) {
                    Intrinsics.throwNpe();
                }
                v110 = v109.getPosZ();
                v111 = this.target$1;
                if (v111 == null) {
                    Intrinsics.throwNpe();
                }
                z = v108 + (v110 - v111.getLastTickPosZ()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosZ();
                GL11.glLineWidth((float)((float)(radius * (double)5.0f)));
                GL11.glBegin((int)3);
                var19_31 = 0;
                var20_32 = 360;
                while (var19_31 <= var20_32) {
                    v112 = MinecraftInstance.mc.getThePlayer();
                    if (v112 == null) {
                        Intrinsics.throwNpe();
                    }
                    var22_37 = (double)i / 50.0 * 1.75;
                    var33_42 = (double)v112.getTicksExisted() / 70.0;
                    var24_40 = false;
                    var35_43 = Math.sin(var22_37);
                    var42_46 = Color.HSBtoRGB((float)(var33_42 + var35_43) % 1.0f, 0.7f, 1.0f);
                    rainbow = new Color(var42_46);
                    GL11.glColor3f((float)((float)rainbow.getRed() / 255.0f), (float)((float)rainbow.getGreen() / 255.0f), (float)((float)rainbow.getBlue() / 255.0f));
                    var22_37 = (double)i * 6.283185307179586 / 45.0;
                    var33_42 = radius;
                    var31_15 = x;
                    var24_40 = false;
                    var35_43 = Math.cos(var22_37);
                    v113 = var31_15 + var33_42 * var35_43;
                    var22_37 = (double)i * 6.283185307179586 / 45.0;
                    var37_44 = radius;
                    var35_43 = z;
                    var33_42 = y;
                    var31_15 = v113;
                    var24_40 = false;
                    var39_45 = Math.sin(var22_37);
                    GL11.glVertex3d((double)var31_15, (double)var33_42, (double)(var35_43 + var37_44 * var39_45));
                    ++i;
                }
                GL11.glEnd();
                GL11.glDepthMask((boolean)true);
                GL11.glEnable((int)2929);
                GL11.glDisable((int)2848);
                GL11.glDisable((int)2881);
                GL11.glEnable((int)2832);
                GL11.glEnable((int)3553);
                GL11.glPopMatrix();
                break;
            }
            case 2: {
                drawTime = (int)(System.currentTimeMillis() % (long)2000);
                drawMode = drawTime > 1000;
                drawPercent = (double)drawTime / 1000.0;
                drawPercent = !drawMode ? (double)true - drawPercent : (drawPercent -= (double)true);
                drawPercent = EaseUtils.easeInOutQuad(drawPercent);
                radius = false;
                points = new ArrayList<E>();
                v114 = this.target$1;
                if (v114 == null) {
                    Intrinsics.throwNpe();
                }
                bb = v114.getEntityBoundingBox();
                radius = bb.getMaxX() - bb.getMinX();
                height = bb.getMaxY() - bb.getMinY();
                v115 = this.target$1;
                if (v115 == null) {
                    Intrinsics.throwNpe();
                }
                v116 = v115.getLastTickPosX();
                v117 = this.target$1;
                if (v117 == null) {
                    Intrinsics.throwNpe();
                }
                v118 = v117.getPosX();
                v119 = this.target$1;
                if (v119 == null) {
                    Intrinsics.throwNpe();
                }
                posX = v116 + (v118 - v119.getLastTickPosX()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                v120 = this.target$1;
                if (v120 == null) {
                    Intrinsics.throwNpe();
                }
                v121 = v120.getLastTickPosY();
                v122 = this.target$1;
                if (v122 == null) {
                    Intrinsics.throwNpe();
                }
                v123 = v122.getPosY();
                v124 = this.target$1;
                if (v124 == null) {
                    Intrinsics.throwNpe();
                }
                posY = v121 + (v123 - v124.getLastTickPosY()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                posY = drawMode ? (posY -= 0.5) : (posY += 0.5);
                v125 = this.target$1;
                if (v125 == null) {
                    Intrinsics.throwNpe();
                }
                v126 = v125.getLastTickPosZ();
                v127 = this.target$1;
                if (v127 == null) {
                    Intrinsics.throwNpe();
                }
                v128 = v127.getPosZ();
                v129 = this.target$1;
                if (v129 == null) {
                    Intrinsics.throwNpe();
                }
                posZ = v126 + (v128 - v129.getLastTickPosZ()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                var23_52 = 0;
                v130 = RangesKt.step((IntProgression)((IntProgression)new IntRange(var23_52, 360)), (int)7);
                var20_33 = v130.getFirst();
                var21_36 = v130.getLast();
                var22_38 = v130.getStep();
                v131 = var20_33;
                v132 = var21_36;
                if (var22_38 >= 0 ? v131 <= v132 : v131 >= v132) {
                    while (true) {
                        var23_53 = (double)i * 3.141592653589793 / (double)180.0f;
                        var34_55 = posX;
                        var31_16 = points;
                        var25_54 = 0;
                        var36_56 = Math.sin(var23_53);
                        v133 = var34_55 - var36_56 * radius;
                        var23_53 = (double)i * 3.141592653589793 / (double)180.0f;
                        var38_57 = posZ;
                        var36_56 = posY + height * drawPercent;
                        var34_55 = v133;
                        var25_54 = 0;
                        var40_58 = Math.cos(var23_53);
                        var43_59 = var38_57 + var40_58 * radius;
                        var45_60 = var36_56;
                        var47_61 = var34_55;
                        var31_16.add(new WVec3(var47_61, var45_60, var43_59));
                        if (i == var21_36) break;
                        i += var22_38;
                    }
                }
                points.add(points.get(0));
                MinecraftInstance.mc.getEntityRenderer().disableLightmap();
                GL11.glPushMatrix();
                GL11.glDisable((int)3553);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glEnable((int)2848);
                GL11.glEnable((int)3042);
                GL11.glDisable((int)2929);
                GL11.glBegin((int)3);
                baseMove = (drawPercent > 0.5 ? (double)true - drawPercent : drawPercent) * (double)2;
                min = height / (double)60 * (double)20 * ((double)true - baseMove) * (double)(drawMode != false ? -1 : 1);
                var24_41 = 0;
                var25_54 = 20;
                while (var24_41 <= var25_54) {
                    moveFace = height / (double)60.0f * (double)i * baseMove;
                    if (drawMode) {
                        moveFace = -moveFace;
                    }
                    firstPoint = (WVec3)points.get(0);
                    GL11.glVertex3d((double)(firstPoint.getXCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosX()), (double)(firstPoint.getYCoord() - moveFace - min - MinecraftInstance.mc.getRenderManager().getViewerPosY()), (double)(firstPoint.getZCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                    GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)(0.7f * ((float)i / 20.0f)));
                    for (WVec3 vec3 : points) {
                        GL11.glVertex3d((double)(vec3.getXCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosX()), (double)(vec3.getYCoord() - moveFace - min - MinecraftInstance.mc.getRenderManager().getViewerPosY()), (double)(vec3.getZCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                    }
                    GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
                    ++i;
                }
                GL11.glEnd();
                GL11.glEnable((int)2929);
                GL11.glDisable((int)2848);
                GL11.glDisable((int)3042);
                GL11.glEnable((int)3553);
                GL11.glPopMatrix();
            }
        }
        if (this.currentTarget != null && this.attackTimer.hasTimePassed(this.attackDelay)) {
            v134 = this.currentTarget;
            if (v134 == null) {
                Intrinsics.throwNpe();
            }
            if (v134.getHurtTime() <= ((Number)this.hurtTimeValue.get()).intValue()) {
                var3_8 = this.clicks;
                this.clicks = var3_8 + 1;
                this.attackTimer.reset();
                this.attackDelay = TimeUtils.randomClickDelay(((Number)this.minCPS.get()).intValue(), ((Number)this.maxCPS.get()).intValue());
            }
        }
    }

    private final boolean updateRotations(IEntity entity) {
        IAxisAlignedBB boundingBox = entity.getEntityBoundingBox();
        if (StringsKt.equals((String)((String)this.rotations.get()), (String)"HytRotation", (boolean)true)) {
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.offset((entity.getPosX() - entity.getPrevPosX()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosY() - entity.getPrevPosY()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosZ() - entity.getPrevPosZ()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            boolean bl = (Boolean)this.outborderValue.get() != false && !this.attackTimer.hasTimePassed(this.attackDelay / (long)2);
            boolean bl2 = (Boolean)this.randomCenterValue.get();
            boolean bl3 = (Boolean)this.predictValue.get();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            VecRotation vecRotation = RotationUtils.lockView(boundingBox, bl, bl2, bl3, PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            VecRotation vecRotation2 = vecRotation;
            Rotation rotation = vecRotation2.component2();
            Rotation limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation, (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation(limitedRotation, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                limitedRotation.toPlayer(iEntityPlayerSP2);
            }
            return true;
        }
        if (StringsKt.equals((String)((String)this.rotations.get()), (String)"New", (boolean)true)) {
            if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
                return true;
            }
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.offset((entity.getPosX() - entity.getPrevPosX()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosY() - entity.getPrevPosY()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosZ() - entity.getPrevPosZ()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            boolean bl = (Boolean)this.outborderValue.get() != false && !this.attackTimer.hasTimePassed(this.attackDelay / (long)2);
            boolean bl4 = (Boolean)this.randomCenterValue.get();
            boolean bl5 = (Boolean)this.predictValue.get();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            VecRotation vecRotation = RotationUtils.searchCenter(boundingBox, bl, bl4, bl5, PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            VecRotation vecRotation3 = vecRotation;
            WVec3 rotation = vecRotation3.component1();
            Rotation rotation2 = vecRotation3.component2();
            Rotation limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, RotationUtils.getNewRotations(RotationUtils.getCenter(entity.getEntityBoundingBox()), false), (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation(limitedRotation, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                limitedRotation.toPlayer(iEntityPlayerSP3);
            }
            return true;
        }
        if (StringsKt.equals((String)((String)this.rotations.get()), (String)"BackTrack", (boolean)true)) {
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.offset((entity.getPosX() - entity.getPrevPosX()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosY() - entity.getPrevPosY()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosZ() - entity.getPrevPosZ()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            WVec3 wVec3 = RotationUtils.getCenter(entity.getEntityBoundingBox());
            boolean bl = (Boolean)this.predictValue.get();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            Rotation limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, RotationUtils.OtherRotation(boundingBox, wVec3, bl, PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange()), (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation(limitedRotation, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                limitedRotation.toPlayer(iEntityPlayerSP4);
                return true;
            }
        }
        return true;
    }

    @EventTarget
    public final void onEntityMove(EntityMovementEvent event) {
        IEntity movedEntity = event.getMovedEntity();
        if (this.target$1 == null || movedEntity.equals(this.currentTarget) ^ true) {
            return;
        }
        this.updateHitable();
    }

    /*
     * Unable to fully structure code
     */
    private final void runAttack() {
        if (this.target$1 == null) {
            return;
        }
        if (this.currentTarget == null) {
            return;
        }
        v0 = MinecraftInstance.mc.getThePlayer();
        if (v0 == null) {
            return;
        }
        thePlayer = v0;
        v1 = MinecraftInstance.mc.getTheWorld();
        if (v1 == null) {
            return;
        }
        theWorld = v1;
        failRate = ((Number)this.failRateValue.get()).floatValue();
        swing = (Boolean)this.swingValue.get();
        multi = StringsKt.equals((String)((String)this.targetModeValue.get()), (String)"Multi", (boolean)true);
        v2 = openInventory = (Boolean)this.aacValue.get() != false && MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()) != false;
        if (!(failRate > (float)false)) ** GOTO lbl-1000
        v3 = new Random();
        if ((float)v3.nextInt(100) <= failRate) {
            v4 = true;
        } else lbl-1000:
        // 2 sources

        {
            v4 = failHit = false;
        }
        if (openInventory) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketCloseWindow());
        }
        if (!this.hitable || failHit) {
            if (swing && (((Boolean)this.fakeSwingValue.get()).booleanValue() || failHit)) {
                thePlayer.swingItem();
            }
        } else {
            if (!multi) {
                v5 = this.currentTarget;
                if (v5 == null) {
                    Intrinsics.throwNpe();
                }
                this.attackEntity(v5);
            } else {
                targets = 0;
                for (IEntity entity : theWorld.getLoadedEntityList()) {
                    distance = PlayerExtensionKt.getDistanceToEntityBox(thePlayer, entity);
                    if (!MinecraftInstance.classProvider.isEntityLivingBase(entity) || !this.isEnemy(entity) || !(distance <= (double)this.getRange(entity))) continue;
                    this.attackEntity(entity.asEntityLivingBase());
                    if (((Number)this.limitedMultiTargetsValue.get()).intValue() == 0 || ((Number)this.limitedMultiTargetsValue.get()).intValue() > ++targets) continue;
                    break;
                }
            }
            if (this.switchTimer.hasTimePassed(((Number)this.switchDelayValue.get()).intValue()) || ((String)this.targetModeValue.get()).equals("Switch") ^ true) {
                if (((Boolean)this.aacValue.get()).booleanValue()) {
                    v6 = this.target$1;
                    if (v6 == null) {
                        Intrinsics.throwNpe();
                    }
                    v7 = v6.getEntityId();
                } else {
                    v8 = this.currentTarget;
                    if (v8 == null) {
                        Intrinsics.throwNpe();
                    }
                    v7 = v8.getEntityId();
                }
                this.prevTargetEntities.add(v7);
                this.switchTimer.reset();
            }
            if (((Boolean)this.aacValue.get()).booleanValue()) {
                v9 = this.target$1;
                if (v9 == null) {
                    Intrinsics.throwNpe();
                }
                v10 = v9.getEntityId();
            } else {
                v11 = this.currentTarget;
                if (v11 == null) {
                    Intrinsics.throwNpe();
                }
                v10 = v11.getEntityId();
            }
            this.prevTargetEntities.add(v10);
            if (this.target$1.equals(this.currentTarget)) {
                this.target$1 = null;
            }
        }
        if (openInventory) {
            var13_12 = MinecraftInstance.mc.getNetHandler();
            $i$f$createOpenInventoryPacket = false;
            v12 = WrapperImpl.INSTANCE.getClassProvider();
            v13 = LiquidBounce.INSTANCE.getWrapper().getMinecraft().getThePlayer();
            if (v13 == null) {
                Intrinsics.throwNpe();
            }
            var14_13 = v12.createCPacketEntityAction(v13, ICPacketEntityAction.WAction.OPEN_INVENTORY);
            var13_12.addToSendQueue(var14_13);
        }
    }

    private final void updateTarget() {
        this.target$1 = null;
        int hurtTime = ((Number)this.hurtTimeValue.get()).intValue();
        float fov = ((Number)this.fovValue.get()).floatValue();
        boolean switchMode = StringsKt.equals((String)((String)this.targetModeValue.get()), (String)"Switch", (boolean)true);
        boolean bl = false;
        List targets = new ArrayList();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IWorldClient theWorld = iWorldClient;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        for (IEntity collection : theWorld.getLoadedEntityList()) {
            if (!MinecraftInstance.classProvider.isEntityLivingBase(collection) || !this.isEnemy(collection) || switchMode && this.prevTargetEntities.contains(collection.getEntityId())) continue;
            double distance = PlayerExtensionKt.getDistanceToEntityBox(thePlayer, collection);
            double entityFov = RotationUtils.getRotationDifference(collection);
            if (!(distance <= (double)this.getMaxRange()) || fov != 180.0f && !(entityFov <= (double)fov) || collection.asEntityLivingBase().getHurtTime() > hurtTime) continue;
            targets.add(collection.asEntityLivingBase());
        }
        String string = (String)this.priorityValue.get();
        boolean bl2 = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "distance": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                boolean bl3 = false;
                Comparator comparator = new Comparator<T>(thePlayer){
                    final /* synthetic */ IEntityPlayerSP $thePlayer$inlined;
                    {
                        this.$thePlayer$inlined = iEntityPlayerSP;
                    }

                    public final int compare(T a, T b) {
                        boolean bl = false;
                        IEntityLivingBase it = (IEntityLivingBase)a;
                        boolean bl2 = false;
                        Comparable comparable = Double.valueOf(PlayerExtensionKt.getDistanceToEntityBox(this.$thePlayer$inlined, it));
                        it = (IEntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        bl2 = false;
                        Double d = PlayerExtensionKt.getDistanceToEntityBox(this.$thePlayer$inlined, it);
                        return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)d);
                    }
                };
                CollectionsKt.sortWith((List)list, (Comparator)comparator);
                break;
            }
            case "health": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                boolean bl4 = false;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        boolean bl = false;
                        IEntityLivingBase it = (IEntityLivingBase)a;
                        boolean bl2 = false;
                        Comparable comparable = Float.valueOf(it.getHealth());
                        it = (IEntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        bl2 = false;
                        Float f = Float.valueOf(it.getHealth());
                        return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)f);
                    }
                };
                CollectionsKt.sortWith((List)list, (Comparator)comparator);
                break;
            }
            case "direction": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                boolean bl5 = false;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        boolean bl = false;
                        IEntityLivingBase it = (IEntityLivingBase)a;
                        boolean bl2 = false;
                        Comparable comparable = Double.valueOf(RotationUtils.getRotationDifference(it));
                        it = (IEntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        bl2 = false;
                        Double d = RotationUtils.getRotationDifference(it);
                        return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)d);
                    }
                };
                CollectionsKt.sortWith((List)list, (Comparator)comparator);
                break;
            }
            case "livingtime": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                boolean bl6 = false;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        boolean bl = false;
                        IEntityLivingBase it = (IEntityLivingBase)a;
                        boolean bl2 = false;
                        Comparable comparable = Integer.valueOf(-it.getTicksExisted());
                        it = (IEntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        bl2 = false;
                        Integer n = -it.getTicksExisted();
                        return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)n);
                    }
                };
                CollectionsKt.sortWith((List)list, (Comparator)comparator);
            }
        }
        for (IEntityLivingBase iEntityLivingBase : targets) {
            if (!this.updateRotations(iEntityLivingBase)) continue;
            this.target$1 = iEntityLivingBase;
            return;
        }
        Collection collection = this.prevTargetEntities;
        boolean bl7 = false;
        if (!collection.isEmpty()) {
            this.prevTargetEntities.clear();
            this.updateTarget();
        }
    }

    private final boolean isEnemy(IEntity entity) {
        if (MinecraftInstance.classProvider.isEntityLivingBase(entity) && entity != null && (EntityUtils.targetDead || this.isAlive(entity.asEntityLivingBase())) && entity.equals(MinecraftInstance.mc.getThePlayer()) ^ true) {
            if (!EntityUtils.targetInvisible && entity.isInvisible()) {
                return false;
            }
            if (EntityUtils.targetPlayer && MinecraftInstance.classProvider.isEntityPlayer(entity)) {
                IEntityPlayer player = entity.asEntityPlayer();
                if (player.isSpectator() || AntiBot.Companion.isBot(player)) {
                    return false;
                }
                if (PlayerExtensionKt.isClientFriend(player) && !LiquidBounce.INSTANCE.getModuleManager().get(NoFriends.class).getState()) {
                    return false;
                }
                Module module = LiquidBounce.INSTANCE.getModuleManager().get(Teams.class);
                if (module == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.misc.Teams");
                }
                Teams teams = (Teams)module;
                return !teams.getState() || !teams.isInYourTeam(entity.asEntityLivingBase());
            }
            return EntityUtils.targetMobs && PlayerExtensionKt.isMob(entity) || EntityUtils.targetAnimals && PlayerExtensionKt.isAnimal(entity);
        }
        return false;
    }

    /*
     * Unable to fully structure code
     */
    private final void attackEntity(IEntityLivingBase entity) {
        block25: {
            block26: {
                v0 = MinecraftInstance.mc.getThePlayer();
                if (v0 == null) {
                    Intrinsics.throwNpe();
                }
                if ((thePlayer = v0).isBlocking() || this.blockingStatus) {
                    this.stopBlocking();
                }
                LiquidBounce.INSTANCE.getEventManager().callEvent(new AttackEvent(entity));
                if (((Boolean)this.swingValue.get()).booleanValue()) {
                    // empty if block
                }
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity((IEntity)entity, ICPacketUseEntity.WAction.ATTACK));
                if (((Boolean)this.swingValue.get()).booleanValue()) {
                    thePlayer.swingItem();
                }
                if (((Boolean)this.keepSprintValue.get()).booleanValue()) {
                    if (!(!(thePlayer.getFallDistance() > 0.0f) || thePlayer.getOnGround() || thePlayer.isOnLadder() || thePlayer.isInWater() || thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.BLINDNESS)) || thePlayer.isRiding())) {
                        thePlayer.onCriticalHit(entity);
                    }
                    if (MinecraftInstance.functions.getModifierForCreature(thePlayer.getHeldItem(), entity.getCreatureAttribute()) > 0.0f) {
                        thePlayer.onEnchantmentCritical(entity);
                    }
                } else if (MinecraftInstance.mc.getPlayerController().getCurrentGameType() != IWorldSettings.WGameType.SPECTATOR) {
                    thePlayer.attackTargetEntityWithCurrentItem(entity);
                }
                v1 = LiquidBounce.INSTANCE.getModuleManager().get(Criticals.class);
                if (v1 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.Criticals");
                }
                criticals = (Criticals)v1;
                var4_4 = 0;
                var5_6 = 2;
                while (var4_4 <= var5_6) {
                    if (thePlayer.getFallDistance() > 0.0f && thePlayer.getOnGround() == false && thePlayer.isOnLadder() == false && thePlayer.isInWater() == false && thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.BLINDNESS)) == false && thePlayer.getRidingEntity() == null || criticals.getState() && criticals.getMsTimer().hasTimePassed(((Number)criticals.getDelayValue().get()).intValue()) && !thePlayer.isInWater() && !thePlayer.isInLava() && !thePlayer.isInWeb()) {
                        v2 = this.target$1;
                        if (v2 == null) {
                            Intrinsics.throwNpe();
                        }
                        thePlayer.onCriticalHit(v2);
                    }
                    v3 = thePlayer.getHeldItem();
                    v4 = this.target$1;
                    if (v4 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (MinecraftInstance.functions.getModifierForCreature(v3, v4.getCreatureAttribute()) > 0.0f || ((Boolean)this.fakeSharpValue.get()).booleanValue()) {
                        v5 = this.target$1;
                        if (v5 == null) {
                            Intrinsics.throwNpe();
                        }
                        thePlayer.onEnchantmentCritical(v5);
                    }
                    ++i;
                }
                if (!StringsKt.equals((String)((String)this.autoBlockValue.get()), (String)"Packet", (boolean)true)) break block25;
                if (thePlayer.isBlocking()) break block26;
                this_$iv = this;
                $i$f$getCanBlock = false;
                v6 = MinecraftInstance.mc.getThePlayer();
                if (v6 == null) {
                    Intrinsics.throwNpe();
                }
                if (v6.getHeldItem() == null) ** GOTO lbl-1000
                v7 = MinecraftInstance.mc.getThePlayer();
                if (v7 == null) {
                    Intrinsics.throwNpe();
                }
                v8 = v7.getHeldItem();
                if (v8 == null) {
                    Intrinsics.throwNpe();
                }
                if (MinecraftInstance.classProvider.isItemSword(v8.getItem())) {
                    v9 = true;
                } else lbl-1000:
                // 2 sources

                {
                    v9 = false;
                }
                if (!v9) break block25;
            }
            this.startBlocking(entity, (Boolean)this.interactAutoBlockValue.get());
        }
        thePlayer.resetCooldown();
    }

    private final void updateHitable() {
        if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
            this.hitable = true;
            return;
        }
        double d = this.getMaxRange();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntity iEntity = iEntityPlayerSP;
        IEntityLivingBase iEntityLivingBase = this.target$1;
        if (iEntityLivingBase == null) {
            Intrinsics.throwNpe();
        }
        double d2 = PlayerExtensionKt.getDistanceToEntityBox(iEntity, iEntityLivingBase);
        boolean bl = false;
        double reach = Math.min(d, d2) + 1.0;
        if (((Boolean)this.raycastValue.get()).booleanValue()) {
            IEntity raycastedEntity2 = RaycastUtils.raycastEntity(reach, new RaycastUtils.EntityFilter(this){
                final /* synthetic */ KillAura2 this$0;

                /*
                 * Enabled force condition propagation
                 * Lifted jumps to return sites
                 */
                public boolean canRaycast(@Nullable IEntity entity) {
                    if (((Boolean)KillAura2.access$getLivingRaycastValue$p(this.this$0).get()).booleanValue()) {
                        if (!MinecraftInstance.classProvider.isEntityLivingBase(entity)) return false;
                        if (MinecraftInstance.classProvider.isEntityArmorStand(entity)) return false;
                    }
                    if (KillAura2.access$isEnemy(this.this$0, entity)) return true;
                    if ((Boolean)KillAura2.access$getRaycastIgnoredValue$p(this.this$0).get() != false) return true;
                    if ((Boolean)KillAura2.access$getAacValue$p(this.this$0).get() == false) return false;
                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient == null) {
                        Intrinsics.throwNpe();
                    }
                    IEntity iEntity = entity;
                    if (iEntity == null) {
                        Intrinsics.throwNpe();
                    }
                    Collection<IEntity> collection = iWorldClient.getEntitiesWithinAABBExcludingEntity(entity, iEntity.getEntityBoundingBox());
                    boolean bl = false;
                    if (collection.isEmpty()) return false;
                    return true;
                }
                {
                    this.this$0 = $outer;
                }
            });
            if (((Boolean)this.raycastValue.get()).booleanValue() && raycastedEntity2 != null && MinecraftInstance.classProvider.isEntityLivingBase(raycastedEntity2) && (LiquidBounce.INSTANCE.getModuleManager().get(NoFriends.class).getState() || !MinecraftInstance.classProvider.isEntityPlayer(raycastedEntity2) || !PlayerExtensionKt.isClientFriend(raycastedEntity2.asEntityPlayer()))) {
                this.currentTarget = raycastedEntity2.asEntityLivingBase();
            }
            this.hitable = ((Number)this.maxTurnSpeed.get()).floatValue() > 0.0f ? this.currentTarget.equals(raycastedEntity2) : true;
        } else {
            this.hitable = RotationUtils.isFaced(this.currentTarget, reach);
        }
    }

    /*
     * WARNING - void declaration
     */
    private final void startBlocking(IEntity interactEntity, boolean interact) {
        Object positionEye;
        if (((Number)this.blockRate.get()).intValue() <= 0 || new Random().nextInt(100) > ((Number)this.blockRate.get()).intValue()) {
            return;
        }
        if (interact) {
            void y$iv;
            void x$iv;
            void this_$iv;
            void yaw;
            IEntity iEntity = MinecraftInstance.mc.getRenderViewEntity();
            positionEye = iEntity != null ? iEntity.getPositionEyes(1.0f) : null;
            double expandSize = interactEntity.getCollisionBorderSize();
            IAxisAlignedBB boundingBox = interactEntity.getEntityBoundingBox().expand(expandSize, expandSize, expandSize);
            Rotation rotation = RotationUtils.targetRotation;
            if (rotation == null) {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                float f = iEntityPlayerSP.getRotationYaw();
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                rotation = new Rotation(f, iEntityPlayerSP2.getRotationPitch());
            }
            Rotation rotation2 = rotation;
            float f = rotation2.component1();
            float pitch = rotation2.component2();
            void var10_12 = -yaw * ((float)Math.PI / 180) - (float)Math.PI;
            boolean bl = false;
            float yawCos = (float)Math.cos((double)var10_12);
            void var11_14 = -yaw * ((float)Math.PI / 180) - (float)Math.PI;
            boolean bl2 = false;
            float yawSin = (float)Math.sin((double)var11_14);
            float f2 = -pitch * ((float)Math.PI / 180);
            boolean bl3 = false;
            float pitchCos = -((float)Math.cos(f2));
            float f3 = -pitch * ((float)Math.PI / 180);
            boolean bl4 = false;
            float pitchSin = (float)Math.sin(f3);
            double d = this.getMaxRange();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            double d2 = PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, interactEntity);
            boolean bl5 = false;
            double range = Math.min(d, d2) + 1.0;
            Object object = positionEye;
            if (object == null) {
                Intrinsics.throwNpe();
            }
            Object object2 = object;
            d2 = (double)(yawSin * pitchCos) * range;
            double d3 = (double)pitchSin * range;
            double z$iv = (double)(yawCos * pitchCos) * range;
            boolean $i$f$addVector = false;
            WVec3 lookAt = new WVec3(this_$iv.getXCoord() + x$iv, this_$iv.getYCoord() + y$iv, this_$iv.getZCoord() + z$iv);
            IMovingObjectPosition iMovingObjectPosition = boundingBox.calculateIntercept((WVec3)positionEye, lookAt);
            if (iMovingObjectPosition == null) {
                return;
            }
            IMovingObjectPosition movingObject = iMovingObjectPosition;
            WVec3 hitVec = movingObject.getHitVec();
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(interactEntity, new WVec3(hitVec.getXCoord() - interactEntity.getPosX(), hitVec.getYCoord() - interactEntity.getPosY(), hitVec.getZCoord() - interactEntity.getPosZ())));
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(interactEntity, ICPacketUseEntity.WAction.INTERACT));
        }
        if (LiquidBounce.INSTANCE.getModuleManager().get(Animations.class).getState()) {
            void hand$iv;
            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            positionEye = iEntityPlayerSP.getInventory().getCurrentItemInHand();
            WEnumHand expandSize = WEnumHand.OFF_HAND;
            IINetHandlerPlayClient iINetHandlerPlayClient2 = iINetHandlerPlayClient;
            boolean $i$f$createUseItemPacket = false;
            IPacket iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem((WEnumHand)hand$iv);
            iINetHandlerPlayClient2.addToSendQueue(iPacket);
        } else {
            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            IItemStack itemStack$iv = iEntityPlayerSP.getInventory().getCurrentItemInHand();
            WEnumHand hand$iv = WEnumHand.MAIN_HAND;
            IINetHandlerPlayClient iINetHandlerPlayClient3 = iINetHandlerPlayClient;
            boolean $i$f$createUseItemPacket = false;
            IPacket iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(hand$iv);
            iINetHandlerPlayClient3.addToSendQueue(iPacket);
            IINetHandlerPlayClient iINetHandlerPlayClient4 = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            itemStack$iv = iEntityPlayerSP3.getInventory().getCurrentItemInHand();
            hand$iv = WEnumHand.OFF_HAND;
            iINetHandlerPlayClient3 = iINetHandlerPlayClient4;
            $i$f$createUseItemPacket = false;
            iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(hand$iv);
            iINetHandlerPlayClient3.addToSendQueue(iPacket);
        }
        this.blockingStatus = true;
    }

    private final void stopBlocking() {
        if (this.blockingStatus) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
            this.blockingStatus = false;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final boolean getCancelRun() {
        int $i$f$getCancelRun = 0;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isSpectator()) return true;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        if (!this.isAlive(iEntityPlayerSP2)) return true;
        if (LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState()) return true;
        if (!LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) return false;
        return true;
    }

    private final boolean isAlive(IEntityLivingBase entity) {
        return entity.isEntityAlive() && entity.getHealth() > 0.0f || (Boolean)this.aacValue.get() != false && entity.getHurtTime() > 5;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final boolean getCanBlock() {
        int $i$f$getCanBlock = 0;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getHeldItem() == null) return false;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        IItemStack iItemStack = iEntityPlayerSP2.getHeldItem();
        if (iItemStack == null) {
            Intrinsics.throwNpe();
        }
        if (!MinecraftInstance.classProvider.isItemSword(iItemStack.getItem())) return false;
        return true;
    }

    private final float getMaxRange() {
        float f = ((Number)this.rangeValue.get()).floatValue();
        float f2 = ((Number)this.throughWallsRangeValue.get()).floatValue();
        boolean bl = false;
        return Math.max(f, f2);
    }

    private final float getRange(IEntity entity) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        float f = PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) >= ((Number)this.throughWallsRangeValue.get()).doubleValue() ? ((Number)this.rangeValue.get()).floatValue() : ((Number)this.throughWallsRangeValue.get()).floatValue();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        return f - (iEntityPlayerSP2.getSprinting() ? ((Number)this.rangeSprintReducementValue.get()).floatValue() : 0.0f);
    }

    @Override
    public String getTag() {
        return (String)this.targetModeValue.get();
    }

    public final boolean isBlockingChestAura() {
        return this.getState() && this.target$1 != null;
    }

    public KillAura2() {
        List list;
        KillAura2 killAura2 = this;
        boolean bl = false;
        killAura2.prevTargetEntities = list = (List)new ArrayList();
        this.attackTimer = new MSTimer();
        this.switchTimer = new MSTimer();
        this.containerOpen = -1L;
    }

    static {
        Companion = new Companion(null);
    }

    public static final /* synthetic */ boolean access$isEnemy(KillAura2 $this, IEntity entity) {
        return $this.isEnemy(entity);
    }

    public static final /* synthetic */ BoolValue access$getLivingRaycastValue$p(KillAura2 $this) {
        return $this.livingRaycastValue;
    }

    public static final /* synthetic */ BoolValue access$getRaycastIgnoredValue$p(KillAura2 $this) {
        return $this.raycastIgnoredValue;
    }

    public static final /* synthetic */ BoolValue access$getAacValue$p(KillAura2 $this) {
        return $this.aacValue;
    }

    public static final /* synthetic */ IntegerValue access$getMinCPS$p(KillAura2 $this) {
        return $this.minCPS;
    }

    public static final /* synthetic */ long access$getAttackDelay$p(KillAura2 $this) {
        return $this.attackDelay;
    }

    public static final /* synthetic */ void access$setAttackDelay$p(KillAura2 $this, long l) {
        $this.attackDelay = l;
    }

    public static final /* synthetic */ IntegerValue access$getMaxCPS$p(KillAura2 $this) {
        return $this.maxCPS;
    }

    public static final /* synthetic */ FloatValue access$getMinTurnSpeed$p(KillAura2 $this) {
        return $this.minTurnSpeed;
    }

    public static final /* synthetic */ FloatValue access$getMaxTurnSpeed$p(KillAura2 $this) {
        return $this.maxTurnSpeed;
    }

    public static final /* synthetic */ FloatValue access$getMinPredictSize$p(KillAura2 $this) {
        return $this.minPredictSize;
    }

    public static final /* synthetic */ FloatValue access$getMaxPredictSize$p(KillAura2 $this) {
        return $this.maxPredictSize;
    }

    public static final class Companion {
        public final Object getTarget() {
            return target;
        }

        public final void setTarget(Object object) {
            target = object;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

