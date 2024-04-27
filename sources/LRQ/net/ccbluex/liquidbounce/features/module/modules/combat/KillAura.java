/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.comparisons.ComparisonsKt
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.IntProgression
 *  kotlin.ranges.IntRange
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.effect.EntityLightningBolt
 *  net.minecraft.network.play.server.SPacketSpawnGlobalEntity
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.JvmStatic;
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
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

@ModuleInfo(name="KillAura", category=ModuleCategory.COMBAT, keyBind=19, description="\u4fee\u590d\u7248")
public final class KillAura
extends Module {
    private final IntegerValue maxCPS = new IntegerValue(this, "MaxCPS", 8, 1, 20){
        final /* synthetic */ KillAura this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)KillAura.access$getMinCPS$p(this.this$0).get()).intValue();
            if (i > newValue) {
                this.set(i);
            }
            KillAura.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)KillAura.access$getMinCPS$p(this.this$0).get()).intValue(), ((Number)this.get()).intValue()));
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final IntegerValue minCPS = new IntegerValue(this, "MinCPS", 5, 1, 20){
        final /* synthetic */ KillAura this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)KillAura.access$getMaxCPS$p(this.this$0).get()).intValue();
            if (i < newValue) {
                this.set(i);
            }
            KillAura.access$setAttackDelay$p(this.this$0, TimeUtils.randomClickDelay(((Number)this.get()).intValue(), ((Number)KillAura.access$getMaxCPS$p(this.this$0).get()).intValue()));
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
    private final FloatValue cooldownValue = new FloatValue("Cooldown", 1.0f, 0.0f, 1.0f);
    private final FloatValue rangeValue = new FloatValue("Range", 3.7f, 1.0f, 8.0f);
    private final FloatValue BlockRangeValue = new FloatValue("BlockRange", 3.0f, 0.0f, 8.0f);
    private final FloatValue throughWallsRangeValue = new FloatValue("ThroughWallsRange", 3.0f, 0.0f, 8.0f);
    private final FloatValue rangeSprintReducementValue = new FloatValue("RangeSprintReducement", 0.0f, 0.0f, 0.4f);
    private final ListValue priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "Direction", "LivingTime", "HYT"}, "Distance");
    private final ListValue targetModeValue = new ListValue("TargetMode", new String[]{"Single", "Switch", "Multi"}, "Switch");
    private final IntegerValue switchDelayValue = new IntegerValue("SwitchDelay", 700, 0, 2000);
    private final BoolValue swingValue = new BoolValue("Swing", true);
    private final BoolValue keepSprintValue = new BoolValue("KeepSprint", true);
    private final BoolValue afterAttackValue = new BoolValue("AutoBlock-AfterAttack", false);
    private final ListValue blockModeValue = new ListValue("AutoBlock", new String[]{"Off", "Packet", "AfterTick", "Nore"}, "Packet");
    private final BoolValue interactAutoBlockValue = new BoolValue("InteractAutoBlock", true);
    private final BoolValue raycastValue = new BoolValue("RayCast", true);
    private final BoolValue raycastIgnoredValue = new BoolValue("RayCastIgnored", false);
    private final BoolValue livingRaycastValue = new BoolValue("LivingRayCast", true);
    private final BoolValue aacValue = new BoolValue("AAC", false);
    private final ListValue rotations = new ListValue("RotationMode", new String[]{"Vanilla", "BackTrack", "Test", "Test1", "Test2", "Test3", "HytRotation"}, "Test");
    private final BoolValue silentRotationValue = new BoolValue("SilentRotation", true);
    private final ListValue rotationStrafeValue = new ListValue("Strafe", new String[]{"Off", "Strict", "Silent"}, "Off");
    private final BoolValue randomCenterValue = new BoolValue("RandomCenter", true);
    private final BoolValue outborderValue = new BoolValue("Outborder", false);
    private final FloatValue maxTurnSpeed = new FloatValue(this, "MaxTurnSpeed", 180.0f, 0.0f, 180.0f){
        final /* synthetic */ KillAura this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura.access$getMinTurnSpeed$p(this.this$0).get()).floatValue();
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
        final /* synthetic */ KillAura this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura.access$getMaxTurnSpeed$p(this.this$0).get()).floatValue();
            if (v < newValue) {
                this.set(Float.valueOf(v));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final BoolValue predictValue = new BoolValue("Predict", true);
    private final FloatValue maxPredictSize = new FloatValue(this, "MaxPredictSize", 1.0f, 0.1f, 5.0f){
        final /* synthetic */ KillAura this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura.access$getMinPredictSize$p(this.this$0).get()).floatValue();
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
        final /* synthetic */ KillAura this$0;

        protected void onChanged(float oldValue, float newValue) {
            float v = ((Number)KillAura.access$getMaxPredictSize$p(this.this$0).get()).floatValue();
            if (v < newValue) {
                this.set(Float.valueOf(v));
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final BoolValue lightingValue = new BoolValue("Lighting", false);
    private final ListValue lightingModeValue = new ListValue("Lighting-Mode", new String[]{"Dead", "Attack"}, "Dead");
    private final BoolValue lightingSoundValue = new BoolValue("Lighting-Sound", true);
    private final FloatValue fovValue = new FloatValue("FOV", 180.0f, 0.0f, 180.0f);
    private final BoolValue hitableValue = new BoolValue("AlwaysHitable", true);
    private final FloatValue failRateValue = new FloatValue("FailRate", 0.0f, 0.0f, 100.0f);
    private final BoolValue stopSprintValue = new BoolValue("StopSprintOnAir", true);
    private final BoolValue fakeSwingValue = new BoolValue("FakeSwing", true);
    private final BoolValue noInventoryAttackValue = new BoolValue("NoInvAttack", false);
    private final IntegerValue noInventoryDelayValue = new IntegerValue("NoInvDelay", 200, 0, 500);
    private final IntegerValue limitedMultiTargetsValue = new IntegerValue("LimitedMultiTargets", 0, 0, 50);
    private final ListValue markValue = new ListValue("Mark", new String[]{"Liquid", "FDP", "Block", "Jello", "Plat", "Red", "Sims", "None", "LiYing"}, "FDP");
    private final IntegerValue jelloRed = new IntegerValue("jelloRed", 255, 0, 255);
    private final IntegerValue jelloGreen = new IntegerValue("jelloGreen", 255, 0, 255);
    private final IntegerValue jelloBlue = new IntegerValue("jelloBlue", 255, 0, 255);
    private final BoolValue fakeSharpValue = new BoolValue("FakeSharp", true);
    private final BoolValue circleValue = new BoolValue("Circle", true);
    private final IntegerValue circleRed = new IntegerValue("CircleRed", 255, 0, 255);
    private final IntegerValue circleGreen = new IntegerValue("CircleGreen", 255, 0, 255);
    private final IntegerValue circleBlue = new IntegerValue("CircleBlue", 255, 0, 255);
    private final IntegerValue circleAlpha = new IntegerValue("CircleAlpha", 255, 0, 255);
    private final IntegerValue circleAccuracy = new IntegerValue("CircleAccuracy", 15, 0, 60);
    private IEntityLivingBase target;
    private IEntityLivingBase currentTarget;
    private boolean hitable;
    private final List<Integer> prevTargetEntities;
    private IEntityLivingBase lastTarget;
    private final MSTimer attackTimer;
    private long attackDelay;
    private int clicks;
    private final MSTimer switchTimer;
    private long containerOpen;
    private boolean blockingStatus;
    private IEntityLivingBase syncEntity;
    private String list;
    private static int killCounts;
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

    public final IEntityLivingBase getTarget() {
        return this.target;
    }

    public final void setTarget(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.target = iEntityLivingBase;
    }

    public final boolean getBlockingStatus() {
        return this.blockingStatus;
    }

    public final void setBlockingStatus(boolean bl) {
        this.blockingStatus = bl;
    }

    public final IEntityLivingBase getSyncEntity() {
        return this.syncEntity;
    }

    public final void setSyncEntity(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.syncEntity = iEntityLivingBase;
    }

    public final String getList() {
        return this.list;
    }

    public final void setList(String string) {
        this.list = string;
    }

    @Override
    public void onDisable() {
        this.target = null;
        this.currentTarget = null;
        this.lastTarget = null;
        this.hitable = false;
        this.prevTargetEntities.clear();
        this.attackTimer.reset();
        this.clicks = 0;
        this.stopBlocking();
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onMotion(MotionEvent event) {
        block19: {
            block20: {
                if (((Boolean)this.stopSprintValue.get()).booleanValue()) {
                    v0 = MinecraftInstance.mc.getThePlayer();
                    if (v0 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v0.getOnGround()) {
                        this.keepSprintValue.set(true);
                    } else {
                        this.keepSprintValue.set(false);
                    }
                }
                if (event.getEventState() != EventState.POST) break block19;
                if (this.target == null) {
                    return;
                }
                if (this.currentTarget == null) {
                    return;
                }
                this.updateHitable();
                if (!StringsKt.equals((String)((String)this.blockModeValue.get()), (String)"AfterTick", (boolean)true)) break block20;
                this_$iv = this;
                $i$f$getCanBlock = false;
                v1 = MinecraftInstance.mc.getThePlayer();
                if (v1 == null) {
                    Intrinsics.throwNpe();
                }
                if (v1.getHeldItem() == null) ** GOTO lbl-1000
                v2 = MinecraftInstance.mc.getThePlayer();
                if (v2 == null) {
                    Intrinsics.throwNpe();
                }
                v3 = v2.getHeldItem();
                if (v3 == null) {
                    Intrinsics.throwNpe();
                }
                if (MinecraftInstance.classProvider.isItemSword(v3.getItem())) {
                    v4 = true;
                } else lbl-1000:
                // 2 sources

                {
                    v4 = false;
                }
                if (v4) {
                    v5 = this.currentTarget;
                    if (v5 == null) {
                        Intrinsics.throwNpe();
                    }
                    this.startBlocking(v5, this.hitable);
                }
            }
            if (this.switchTimer.hasTimePassed(((Number)this.switchDelayValue.get()).intValue()) || ((String)this.targetModeValue.get()).equals("Switch") ^ true) {
                if (((Boolean)this.aacValue.get()).booleanValue()) {
                    v6 = this.target;
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
            return;
        }
        if (StringsKt.equals((String)((String)this.rotationStrafeValue.get()), (String)"Off", (boolean)true)) {
            this.update();
        }
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
        KillAura this_$iv = this;
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
        if (this.target == null) {
            this.stopBlocking();
            return;
        }
        this.currentTarget = this.target;
        if (StringsKt.equals((String)((String)this.targetModeValue.get()), (String)"Switch", (boolean)true)) return;
        if (!this.isEnemy(this.currentTarget)) return;
        this.target = this.currentTarget;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        if (((Boolean)this.lightingValue.get()).booleanValue()) {
            var2_2 = (String)this.lightingModeValue.get();
            var3_4 = false;
            v0 = var2_2;
            if (v0 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            var2_2 = v0.toLowerCase();
            switch (var2_2.hashCode()) {
                case -1407259064: {
                    if (!var2_2.equals("attack")) ** break;
                    break;
                }
                case 3079268: {
                    if (!var2_2.equals("dead")) ** break;
                    if (this.target != null) {
                        if (this.lastTarget == null) {
                            v1 = this.target;
                        } else {
                            v2 = this.lastTarget;
                            if (v2 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (v2.getHealth() <= (float)false) {
                                v3 = MinecraftInstance.mc.getNetHandler2();
                                v4 = (World)MinecraftInstance.mc2.field_71441_e;
                                v5 = this.lastTarget;
                                if (v5 == null) {
                                    Intrinsics.throwNpe();
                                }
                                v6 = v5.getPosX();
                                v7 = this.lastTarget;
                                if (v7 == null) {
                                    Intrinsics.throwNpe();
                                }
                                v8 = v7.getPosY();
                                v9 = this.lastTarget;
                                if (v9 == null) {
                                    Intrinsics.throwNpe();
                                }
                                v3.func_147292_a(new SPacketSpawnGlobalEntity((Entity)new EntityLightningBolt(v4, v6, v8, v9.getPosZ(), true)));
                                if (((Boolean)this.lightingSoundValue.get()).booleanValue()) {
                                    MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 0.5f);
                                }
                            }
                            v1 = this.target;
                        }
                        this.lastTarget = v1;
                        ** break;
                    }
                    if (this.lastTarget == null) ** break;
                    v10 = this.lastTarget;
                    if (v10 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!(v10.getHealth() <= (float)false)) ** break;
                    v11 = MinecraftInstance.mc.getNetHandler2();
                    v12 = (World)MinecraftInstance.mc2.field_71441_e;
                    v13 = this.lastTarget;
                    if (v13 == null) {
                        Intrinsics.throwNpe();
                    }
                    v14 = v13.getPosX();
                    v15 = this.lastTarget;
                    if (v15 == null) {
                        Intrinsics.throwNpe();
                    }
                    v16 = v15.getPosY();
                    v17 = this.lastTarget;
                    if (v17 == null) {
                        Intrinsics.throwNpe();
                    }
                    v11.func_147292_a(new SPacketSpawnGlobalEntity((Entity)new EntityLightningBolt(v12, v14, v16, v17.getPosZ(), true)));
                    if (((Boolean)this.lightingSoundValue.get()).booleanValue()) {
                        MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 0.5f);
                    }
                    this.lastTarget = this.target;
                    ** break;
                }
            }
            v18 = MinecraftInstance.mc.getNetHandler2();
            v19 = (World)MinecraftInstance.mc2.field_71441_e;
            v20 = this.target;
            if (v20 == null) {
                Intrinsics.throwNpe();
            }
            v21 = v20.getPosX();
            v22 = this.target;
            if (v22 == null) {
                Intrinsics.throwNpe();
            }
            v23 = v22.getPosY();
            v24 = this.target;
            if (v24 == null) {
                Intrinsics.throwNpe();
            }
            v18.func_147292_a(new SPacketSpawnGlobalEntity((Entity)new EntityLightningBolt(v19, v21, v23, v24.getPosZ(), true)));
            if (!((Boolean)this.lightingSoundValue.get()).booleanValue()) ** break;
            MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 0.5f);
        }
lbl80:
        // 11 sources

        if (this.syncEntity != null) {
            v25 = this.syncEntity;
            if (v25 == null) {
                Intrinsics.throwNpe();
            }
            if (v25.isDead()) {
                ++KillAura.killCounts;
                this.syncEntity = null;
            }
        }
        this_$iv = this;
        $i$f$getCancelRun = false;
        v26 = MinecraftInstance.mc.getThePlayer();
        if (v26 == null) {
            Intrinsics.throwNpe();
        }
        if (v26.isSpectator()) ** GOTO lbl-1000
        v27 = MinecraftInstance.mc.getThePlayer();
        if (v27 == null) {
            Intrinsics.throwNpe();
        }
        if (!KillAura.access$isAlive(this_$iv, v27) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState() || LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) lbl-1000:
        // 2 sources

        {
            v28 = true;
        } else {
            v28 = false;
        }
        if (v28) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            this.stopBlocking();
            return;
        }
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()) || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            if (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen())) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        if (this.target != null && this.currentTarget != null) {
            v29 = MinecraftInstance.mc.getThePlayer();
            if (v29 == null) {
                Intrinsics.throwNpe();
            }
            if (v29.getCooledAttackStrength(0.0f) >= ((Number)this.cooldownValue.get()).floatValue()) {
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
            GL11.glLineWidth((float)1.0f);
            GL11.glColor4f((float)((float)((Number)this.circleRed.get()).intValue() / 255.0f), (float)((float)((Number)this.circleGreen.get()).intValue() / 255.0f), (float)((float)((Number)this.circleBlue.get()).intValue() / 255.0f), (float)((float)((Number)this.circleAlpha.get()).intValue() / 255.0f));
            GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glBegin((int)3);
            var5_2 = 0;
            v17 = RangesKt.step((IntProgression)((IntProgression)new IntRange(var5_2, 360)), (int)(61 - ((Number)this.circleAccuracy.get()).intValue()));
            var2_7 = v17.getFirst();
            var3_12 = v17.getLast();
            var4_16 = v17.getStep();
            v18 = var2_7;
            v19 = var3_12;
            if (var4_16 >= 0 ? v18 <= v19 : v18 >= v19) {
                while (true) {
                    var5_3 = (double)i * 3.141592653589793 / 180.0;
                    var7_17 = false;
                    v20 = (float)Math.cos(var5_3) * ((Number)this.rangeValue.get()).floatValue();
                    var5_3 = (double)i * 3.141592653589793 / 180.0;
                    var30_20 = v20;
                    var7_17 = false;
                    var31_23 = Math.sin(var5_3);
                    GL11.glVertex2f((float)var30_20, (float)((float)var31_23 * ((Number)this.rangeValue.get()).floatValue()));
                    if (i == var3_12) break;
                    i += var4_16;
                }
            }
            i = 6.283185307179586;
            var4_16 = 0;
            v21 = (float)Math.cos(i) * ((Number)this.rangeValue.get()).floatValue();
            i = 6.283185307179586;
            var30_20 = v21;
            var4_16 = 0;
            var31_23 = Math.sin(i);
            GL11.glVertex2f((float)var30_20, (float)((float)var31_23 * ((Number)this.rangeValue.get()).floatValue()));
            GL11.glEnd();
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glPopMatrix();
        }
        this_$iv = this;
        $i$f$getCancelRun = false;
        v22 = MinecraftInstance.mc.getThePlayer();
        if (v22 == null) {
            Intrinsics.throwNpe();
        }
        if (v22.isSpectator()) ** GOTO lbl-1000
        v23 = MinecraftInstance.mc.getThePlayer();
        if (v23 == null) {
            Intrinsics.throwNpe();
        }
        if (!KillAura.access$isAlive(this_$iv, v23) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState() || LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) lbl-1000:
        // 2 sources

        {
            v24 = true;
        } else {
            v24 = false;
        }
        if (v24) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            this.stopBlocking();
            return;
        }
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()) || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            this.target = null;
            this.currentTarget = null;
            this.hitable = false;
            if (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen())) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        if (this.target == null) {
            return;
        }
        var2_9 = (String)this.markValue.get();
        $i$f$getCancelRun = false;
        v25 = var2_9;
        if (v25 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        var2_9 = v25.toLowerCase();
        tmp = -1;
        switch (var2_9.hashCode()) {
            case 112785: {
                if (!var2_9.equals("red")) break;
                tmp = 1;
                break;
            }
            case 101009364: {
                if (!var2_9.equals("jello")) break;
                tmp = 2;
                break;
            }
            case 101234: {
                if (!var2_9.equals("fdp")) break;
                tmp = 3;
                break;
            }
            case -1102567108: {
                if (!var2_9.equals("liquid")) break;
                tmp = 4;
                break;
            }
            case 3530364: {
                if (!var2_9.equals("sims")) break;
                tmp = 5;
                break;
            }
            case 93832333: {
                if (!var2_9.equals("block")) break;
                tmp = 6;
                break;
            }
            case 3443503: {
                if (!var2_9.equals("plat")) break;
                tmp = 7;
                break;
            }
        }
        switch (tmp) {
            case 4: {
                v26 = this.target;
                if (v26 == null) {
                    Intrinsics.throwNpe();
                }
                v27 = v26;
                v28 = this.target;
                if (v28 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v27, v28.getHurtTime() <= 0 ? new Color(37, 126, 255, 170) : new Color(255, 0, 0, 170));
                break;
            }
            case 7: {
                v29 = this.target;
                if (v29 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v29, this.hitable != false ? new Color(37, 126, 255, 70) : new Color(255, 0, 0, 70));
                break;
            }
            case 6: {
                v30 = this.target;
                if (v30 == null) {
                    Intrinsics.throwNpe();
                }
                bb = v30.getEntityBoundingBox();
                v31 = this.target;
                if (v31 == null) {
                    Intrinsics.throwNpe();
                }
                v31.setEntityBoundingBox(bb.expand(0.2, 0.2, 0.2));
                v32 = this.target;
                if (v32 == null) {
                    Intrinsics.throwNpe();
                }
                v33 = v32;
                v34 = this.target;
                if (v34 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawEntityBox(v33, v34.getHurtTime() <= 0 ? Color.GREEN : Color.RED, true);
                v35 = this.target;
                if (v35 == null) {
                    Intrinsics.throwNpe();
                }
                v35.setEntityBoundingBox(bb);
                break;
            }
            case 1: {
                v36 = this.target;
                if (v36 == null) {
                    Intrinsics.throwNpe();
                }
                v37 = v36;
                v38 = this.target;
                if (v38 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v37, v38.getHurtTime() <= 0 ? new Color(255, 255, 255, 255) : new Color(124, 215, 255, 255));
                break;
            }
            case 5: {
                radius = 0.15f;
                side = 4;
                GL11.glPushMatrix();
                v39 = this.target;
                if (v39 == null) {
                    Intrinsics.throwNpe();
                }
                v40 = v39.getLastTickPosX();
                v41 = this.target;
                if (v41 == null) {
                    Intrinsics.throwNpe();
                }
                v42 = v41.getPosX();
                v43 = this.target;
                if (v43 == null) {
                    Intrinsics.throwNpe();
                }
                v44 = v40 + (v42 - v43.getLastTickPosX()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosX();
                v45 = this.target;
                if (v45 == null) {
                    Intrinsics.throwNpe();
                }
                v46 = v45.getLastTickPosY();
                v47 = this.target;
                if (v47 == null) {
                    Intrinsics.throwNpe();
                }
                v48 = v47.getPosY();
                v49 = this.target;
                if (v49 == null) {
                    Intrinsics.throwNpe();
                }
                v50 = v46 + (v48 - v49.getLastTickPosY()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosY();
                v51 = this.target;
                if (v51 == null) {
                    Intrinsics.throwNpe();
                }
                v52 = v50 + (double)v51.getHeight() * 1.1;
                v53 = this.target;
                if (v53 == null) {
                    Intrinsics.throwNpe();
                }
                v54 = v53.getLastTickPosZ();
                v55 = this.target;
                if (v55 == null) {
                    Intrinsics.throwNpe();
                }
                v56 = v55.getPosZ();
                v57 = this.target;
                if (v57 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glTranslated((double)v44, (double)v52, (double)(v54 + (v56 - v57.getLastTickPosZ()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                v58 = this.target;
                if (v58 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glRotatef((float)(-v58.getWidth()), (float)0.0f, (float)1.0f, (float)0.0f);
                v59 = MinecraftInstance.mc.getThePlayer();
                if (v59 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glRotatef((float)(((float)v59.getTicksExisted() + MinecraftInstance.mc.getTimer().getRenderPartialTicks()) * (float)5), (float)0.0f, (float)1.0f, (float)0.0f);
                v60 = this.target;
                if (v60 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.glColor(v60.getHurtTime() <= 0 ? new Color(80, 255, 80) : new Color(255, 0, 0));
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
                drawPercent = EaseUtils.INSTANCE.easeInOutQuad(drawPercent);
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
                v61 = this.target;
                if (v61 == null) {
                    Intrinsics.throwNpe();
                }
                bb = v61.getEntityBoundingBox();
                radius = bb.getMaxX() - bb.getMinX() + 0.3;
                height = bb.getMaxY() - bb.getMinY();
                v62 = this.target;
                if (v62 == null) {
                    Intrinsics.throwNpe();
                }
                v63 = v62.getLastTickPosX();
                v64 = this.target;
                if (v64 == null) {
                    Intrinsics.throwNpe();
                }
                v65 = v64.getPosX();
                v66 = this.target;
                if (v66 == null) {
                    Intrinsics.throwNpe();
                }
                x = v63 + (v65 - v66.getLastTickPosX()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosX();
                v67 = this.target;
                if (v67 == null) {
                    Intrinsics.throwNpe();
                }
                v68 = v67.getLastTickPosY();
                v69 = this.target;
                if (v69 == null) {
                    Intrinsics.throwNpe();
                }
                v70 = v69.getPosY();
                v71 = this.target;
                if (v71 == null) {
                    Intrinsics.throwNpe();
                }
                y = v68 + (v70 - v71.getLastTickPosY()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosY() + height * drawPercent;
                v72 = this.target;
                if (v72 == null) {
                    Intrinsics.throwNpe();
                }
                v73 = v72.getLastTickPosZ();
                v74 = this.target;
                if (v74 == null) {
                    Intrinsics.throwNpe();
                }
                v75 = v74.getPosZ();
                v76 = this.target;
                if (v76 == null) {
                    Intrinsics.throwNpe();
                }
                z = v73 + (v75 - v76.getLastTickPosZ()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosZ();
                GL11.glLineWidth((float)((float)(radius * (double)5.0f)));
                GL11.glBegin((int)3);
                var18_31 = 0;
                var19_32 = 360;
                while (var18_31 <= var19_32) {
                    v77 = MinecraftInstance.mc.getThePlayer();
                    if (v77 == null) {
                        Intrinsics.throwNpe();
                    }
                    var21_42 = (double)i / 50.0 * 1.75;
                    var32_35 = (double)v77.getTicksExisted() / 70.0;
                    var23_45 = false;
                    var34_36 = Math.sin(var21_42);
                    var41_39 = Color.HSBtoRGB((float)(var32_35 + var34_36) % 1.0f, 0.7f, 1.0f);
                    rainbow = new Color(var41_39);
                    GL11.glColor3f((float)((float)rainbow.getRed() / 255.0f), (float)((float)rainbow.getGreen() / 255.0f), (float)((float)rainbow.getBlue() / 255.0f));
                    var21_42 = (double)i * 6.283185307179586 / 45.0;
                    var32_35 = radius;
                    var30_21 = x;
                    var23_45 = false;
                    var34_36 = Math.cos(var21_42);
                    v78 = var30_21 + var32_35 * var34_36;
                    var21_42 = (double)i * 6.283185307179586 / 45.0;
                    var36_37 = radius;
                    var34_36 = z;
                    var32_35 = y;
                    var30_21 = v78;
                    var23_45 = false;
                    var38_38 = Math.sin(var21_42);
                    GL11.glVertex3d((double)var30_21, (double)var32_35, (double)(var34_36 + var36_37 * var38_38));
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
                drawPercent = EaseUtils.INSTANCE.easeInOutQuad(drawPercent);
                radius = false;
                points = new ArrayList<E>();
                v79 = this.target;
                if (v79 == null) {
                    Intrinsics.throwNpe();
                }
                bb = v79.getEntityBoundingBox();
                radius = bb.getMaxX() - bb.getMinX();
                height = bb.getMaxY() - bb.getMinY();
                v80 = this.target;
                if (v80 == null) {
                    Intrinsics.throwNpe();
                }
                v81 = v80.getLastTickPosX();
                v82 = this.target;
                if (v82 == null) {
                    Intrinsics.throwNpe();
                }
                v83 = v82.getPosX();
                v84 = this.target;
                if (v84 == null) {
                    Intrinsics.throwNpe();
                }
                posX = v81 + (v83 - v84.getLastTickPosX()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                v85 = this.target;
                if (v85 == null) {
                    Intrinsics.throwNpe();
                }
                v86 = v85.getLastTickPosY();
                v87 = this.target;
                if (v87 == null) {
                    Intrinsics.throwNpe();
                }
                v88 = v87.getPosY();
                v89 = this.target;
                if (v89 == null) {
                    Intrinsics.throwNpe();
                }
                posY = v86 + (v88 - v89.getLastTickPosY()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                posY = drawMode ? (posY -= 0.5) : (posY += 0.5);
                v90 = this.target;
                if (v90 == null) {
                    Intrinsics.throwNpe();
                }
                v91 = v90.getLastTickPosZ();
                v92 = this.target;
                if (v92 == null) {
                    Intrinsics.throwNpe();
                }
                v93 = v92.getPosZ();
                v94 = this.target;
                if (v94 == null) {
                    Intrinsics.throwNpe();
                }
                posZ = v91 + (v93 - v94.getLastTickPosZ()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                var22_52 = 0;
                v95 = RangesKt.step((IntProgression)((IntProgression)new IntRange(var22_52, 360)), (int)7);
                var19_33 = v95.getFirst();
                var20_41 = v95.getLast();
                var21_43 = v95.getStep();
                v96 = var19_33;
                v97 = var20_41;
                if (var21_43 >= 0 ? v96 <= v97 : v96 >= v97) {
                    while (true) {
                        var22_53 = (double)i * 3.141592653589793 / (double)180.0f;
                        var33_55 = posX;
                        var30_22 = points;
                        var24_54 = 0;
                        var35_56 = Math.sin(var22_53);
                        v98 = var33_55 - var35_56 * radius;
                        var22_53 = (double)i * 3.141592653589793 / (double)180.0f;
                        var37_57 = posZ;
                        var35_56 = posY + height * drawPercent;
                        var33_55 = v98;
                        var24_54 = 0;
                        var39_58 = Math.cos(var22_53);
                        var42_59 = var37_57 + var39_58 * radius;
                        var44_60 = var35_56;
                        var46_61 = var33_55;
                        var30_22.add(new WVec3(var46_61, var44_60, var42_59));
                        if (i == var20_41) break;
                        i += var21_43;
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
                var23_46 = 0;
                var24_54 = 20;
                while (var23_46 <= var24_54) {
                    moveFace = height / (double)60.0f * (double)i * baseMove;
                    if (drawMode) {
                        moveFace = -moveFace;
                    }
                    firstPoint = (WVec3)points.get(0);
                    GL11.glVertex3d((double)(firstPoint.getXCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosX()), (double)(firstPoint.getYCoord() - moveFace - min - MinecraftInstance.mc.getRenderManager().getViewerPosY()), (double)(firstPoint.getZCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                    GL11.glColor4f((float)((float)((Number)this.jelloRed.get()).intValue() / 255.0f), (float)((float)((Number)this.jelloGreen.get()).intValue() / 255.0f), (float)((float)((Number)this.jelloBlue.get()).intValue() / 255.0f), (float)(0.7f * ((float)i / 20.0f)));
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
            v99 = this.currentTarget;
            if (v99 == null) {
                Intrinsics.throwNpe();
            }
            if (v99.getHurtTime() <= ((Number)this.hurtTimeValue.get()).intValue()) {
                var2_10 = this.clicks;
                this.clicks = var2_10 + 1;
                this.attackTimer.reset();
                this.attackDelay = TimeUtils.randomClickDelay(((Number)this.minCPS.get()).intValue(), ((Number)this.maxCPS.get()).intValue());
            }
        }
        var2_11 = (String)this.markValue.get();
        var3_15 = false;
        v100 = var2_11;
        if (v100 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        if (v100.toLowerCase().equals("Killer") && !StringsKt.equals((String)((String)this.targetModeValue.get()), (String)"Multi", (boolean)true)) {
            RenderUtils.drawCircleESP(this.target, 0.67, Color.RED.getRGB(), true);
        }
    }

    @EventTarget
    public final void onEntityMove(EntityMovementEvent event) {
        IEntity movedEntity = event.getMovedEntity();
        if (this.target == null || movedEntity.equals(this.currentTarget) ^ true) {
            return;
        }
        this.updateHitable();
    }

    /*
     * Unable to fully structure code
     */
    private final void runAttack() {
        if (this.target == null) {
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
            if (((Boolean)this.aacValue.get()).booleanValue()) {
                v6 = this.target;
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
            if (this.target.equals(this.currentTarget)) {
                this.target = null;
            }
        }
        if (openInventory) {
            var13_12 = MinecraftInstance.mc.getNetHandler();
            $i$f$createOpenInventoryPacket = false;
            v9 = WrapperImpl.INSTANCE.getClassProvider();
            v10 = LiquidBounce.INSTANCE.getWrapper().getMinecraft().getThePlayer();
            if (v10 == null) {
                Intrinsics.throwNpe();
            }
            var14_13 = v9.createCPacketEntityAction(v10, ICPacketEntityAction.WAction.OPEN_INVENTORY);
            var13_12.addToSendQueue(var14_13);
        }
    }

    private final void updateTarget() {
        this.target = null;
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
                break;
            }
            case "hyt": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                boolean bl7 = false;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        boolean bl = false;
                        IEntityLivingBase it = (IEntityLivingBase)a;
                        boolean bl2 = false;
                        Comparable comparable = Integer.valueOf(it.getHurtResistantTime());
                        it = (IEntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        bl2 = false;
                        Integer n = it.getHurtResistantTime();
                        return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)n);
                    }
                };
                CollectionsKt.sortWith((List)list, (Comparator)comparator);
            }
        }
        for (IEntityLivingBase iEntityLivingBase : targets) {
            if (!this.updateRotations(iEntityLivingBase)) continue;
            this.target = iEntityLivingBase;
            return;
        }
        Collection collection = this.prevTargetEntities;
        boolean bl8 = false;
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
                if (PlayerExtensionKt.isClientFriend(player)) {
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
        block26: {
            block27: {
                v0 = MinecraftInstance.mc.getThePlayer();
                if (v0 == null) {
                    Intrinsics.throwNpe();
                }
                if ((thePlayer = v0).isBlocking() || this.blockingStatus) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                    if (((Boolean)this.afterAttackValue.get()).booleanValue()) {
                        this.blockingStatus = false;
                    }
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
                        v2 = this.target;
                        if (v2 == null) {
                            Intrinsics.throwNpe();
                        }
                        thePlayer.onCriticalHit(v2);
                    }
                    v3 = thePlayer.getHeldItem();
                    v4 = this.target;
                    if (v4 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (MinecraftInstance.functions.getModifierForCreature(v3, v4.getCreatureAttribute()) > 0.0f || ((Boolean)this.fakeSharpValue.get()).booleanValue()) {
                        v5 = this.target;
                        if (v5 == null) {
                            Intrinsics.throwNpe();
                        }
                        thePlayer.onEnchantmentCritical(v5);
                    }
                    ++i;
                }
                if (!StringsKt.equals((String)((String)this.blockModeValue.get()), (String)"Packet", (boolean)true)) break block26;
                if (thePlayer.isBlocking()) break block27;
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
                if (!v9) break block26;
            }
            this.startBlocking(entity, (Boolean)this.interactAutoBlockValue.get());
        }
        thePlayer.resetCooldown();
        thePlayer.resetCooldown();
    }

    private final boolean updateRotations(IEntity entity) {
        Rotation limitedRotation;
        Object rotation;
        Object rotation22;
        if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
            return true;
        }
        IAxisAlignedBB boundingBox = entity.getEntityBoundingBox();
        if (StringsKt.equals((String)((String)this.rotations.get()), (String)"Vanilla", (boolean)true)) {
            if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
                return true;
            }
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
            VecRotation vecRotation = RotationUtils.searchCenter(boundingBox, bl, bl2, bl3, PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            VecRotation vecRotation2 = vecRotation;
            WVec3 wVec3 = vecRotation2.component1();
            Rotation rotation22 = vecRotation2.component2();
            Rotation limitedRotation2 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation22, (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation(limitedRotation2, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                limitedRotation2.toPlayer(iEntityPlayerSP2);
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
            Rotation limitedRotation3 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, RotationUtils.OtherRotation(boundingBox, wVec3, bl, PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange()), (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation(limitedRotation3, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                limitedRotation3.toPlayer(iEntityPlayerSP3);
                return true;
            }
        }
        if (StringsKt.equals((String)((String)this.rotations.get()), (String)"Test", (boolean)true)) {
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
            VecRotation vecRotation = RotationUtils.lockView(boundingBox, bl, bl4, bl5, PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            rotation22 = vecRotation;
            rotation = ((VecRotation)rotation22).component2();
            limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, (Rotation)rotation, (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
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
        if (StringsKt.equals((String)((String)this.rotations.get()), (String)"test1", (boolean)true)) {
            if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
                return true;
            }
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.offset((entity.getPosX() - entity.getPrevPosX()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosY() - entity.getPrevPosY()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosZ() - entity.getPrevPosZ()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            boolean bl = (Boolean)this.outborderValue.get() != false && !this.attackTimer.hasTimePassed(this.attackDelay / (long)2);
            boolean bl6 = (Boolean)this.randomCenterValue.get();
            boolean bl7 = (Boolean)this.predictValue.get();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            VecRotation vecRotation = RotationUtils.searchCenter(boundingBox, bl, bl6, bl7, PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            Object limitedRotation2 = vecRotation;
            rotation = ((VecRotation)limitedRotation2).component1();
            rotation22 = ((VecRotation)limitedRotation2).component2();
            limitedRotation2 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, RotationUtils.getNCPRotations(RotationUtils.getCenter(entity.getEntityBoundingBox()), false), (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation((Rotation)limitedRotation2, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                ((Rotation)limitedRotation2).toPlayer(iEntityPlayerSP5);
            }
            return true;
        }
        if (StringsKt.equals((String)((String)this.rotations.get()), (String)"test2", (boolean)true)) {
            if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
                return true;
            }
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.offset((entity.getPosX() - entity.getPrevPosX()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosY() - entity.getPrevPosY()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosZ() - entity.getPrevPosZ()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            boolean bl = (Boolean)this.outborderValue.get() != false && !this.attackTimer.hasTimePassed(this.attackDelay / (long)2);
            boolean bl8 = (Boolean)this.randomCenterValue.get();
            boolean bl9 = (Boolean)this.predictValue.get();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            VecRotation vecRotation = RotationUtils.searchCenter(boundingBox, bl, bl8, bl9, PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            Object limitedRotation4 = vecRotation;
            WVec3 vec = ((VecRotation)limitedRotation4).component1();
            rotation22 = ((VecRotation)limitedRotation4).component2();
            limitedRotation4 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, RotationUtils.toRotation(RotationUtils.getCenter(entity.getEntityBoundingBox()), false), (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation((Rotation)limitedRotation4, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP6 == null) {
                    Intrinsics.throwNpe();
                }
                ((Rotation)limitedRotation4).toPlayer(iEntityPlayerSP6);
            }
            return true;
        }
        if (StringsKt.equals((String)((String)this.rotations.get()), (String)"Test3", (boolean)true)) {
            if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
                return true;
            }
            IAxisAlignedBB boundingBox2 = entity.getEntityBoundingBox();
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                double d = entity.getPosX() - entity.getPrevPosX();
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                double d2 = iEntityPlayerSP.getPosX();
                IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP7 == null) {
                    Intrinsics.throwNpe();
                }
                double d3 = (d - (d2 - iEntityPlayerSP7.getPrevPosX())) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue());
                double d4 = entity.getPosY() - entity.getPrevPosY();
                IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP8 == null) {
                    Intrinsics.throwNpe();
                }
                double d5 = iEntityPlayerSP8.getPosY();
                IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP9 == null) {
                    Intrinsics.throwNpe();
                }
                double d6 = (d4 - (d5 - iEntityPlayerSP9.getPrevPosY())) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue());
                double d7 = entity.getPosZ() - entity.getPrevPosZ();
                IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP10 == null) {
                    Intrinsics.throwNpe();
                }
                double d8 = iEntityPlayerSP10.getPosZ();
                IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP11 == null) {
                    Intrinsics.throwNpe();
                }
                boundingBox2 = boundingBox2.offset(d3, d6, (d7 - (d8 - iEntityPlayerSP11.getPrevPosZ())) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            boolean bl = (Boolean)this.outborderValue.get() != false && !this.attackTimer.hasTimePassed(this.attackDelay / (long)2);
            boolean bl10 = (Boolean)this.randomCenterValue.get();
            boolean bl11 = (Boolean)this.predictValue.get();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            VecRotation vecRotation = RotationUtils.searchCenter(boundingBox2, bl, bl10, bl11, PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            Object limitedRotation5 = vecRotation;
            rotation22 = ((VecRotation)limitedRotation5).component2();
            limitedRotation5 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, (Rotation)rotation22, (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation((Rotation)limitedRotation5, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP12 == null) {
                    Intrinsics.throwNpe();
                }
                ((Rotation)limitedRotation5).toPlayer(iEntityPlayerSP12);
            }
            return true;
        }
        if (StringsKt.equals((String)((String)this.rotations.get()), (String)"HytRotation", (boolean)true)) {
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.offset((entity.getPosX() - entity.getPrevPosX()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosY() - entity.getPrevPosY()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosZ() - entity.getPrevPosZ()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            boolean bl = (Boolean)this.outborderValue.get() != false && !this.attackTimer.hasTimePassed(this.attackDelay / (long)2);
            boolean bl12 = (Boolean)this.randomCenterValue.get();
            boolean bl13 = (Boolean)this.predictValue.get();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            VecRotation vecRotation = RotationUtils.lockView(boundingBox, bl, bl12, bl13, PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            rotation22 = vecRotation;
            rotation = ((VecRotation)rotation22).component2();
            limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, (Rotation)rotation, (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation(limitedRotation, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP13 == null) {
                    Intrinsics.throwNpe();
                }
                limitedRotation.toPlayer(iEntityPlayerSP13);
            }
            return true;
        }
        return true;
    }

    private final void updateHitable() {
        if (((Boolean)this.hitableValue.get()).booleanValue()) {
            this.hitable = true;
            return;
        }
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
        IEntityLivingBase iEntityLivingBase = this.target;
        if (iEntityLivingBase == null) {
            Intrinsics.throwNpe();
        }
        double d2 = PlayerExtensionKt.getDistanceToEntityBox(iEntity, iEntityLivingBase);
        boolean bl = false;
        double reach = Math.min(d, d2) + 1.0;
        if (((Boolean)this.raycastValue.get()).booleanValue()) {
            IEntity raycastedEntity2 = RaycastUtils.raycastEntity(reach, new RaycastUtils.EntityFilter(this){
                final /* synthetic */ KillAura this$0;

                /*
                 * Enabled force condition propagation
                 * Lifted jumps to return sites
                 */
                public boolean canRaycast(@Nullable IEntity entity) {
                    if (((Boolean)KillAura.access$getLivingRaycastValue$p(this.this$0).get()).booleanValue()) {
                        if (!MinecraftInstance.classProvider.isEntityLivingBase(entity)) return false;
                        if (MinecraftInstance.classProvider.isEntityArmorStand(entity)) return false;
                    }
                    if (KillAura.access$isEnemy(this.this$0, entity)) return true;
                    if ((Boolean)KillAura.access$getRaycastIgnoredValue$p(this.this$0).get() != false) return true;
                    if ((Boolean)KillAura.access$getAacValue$p(this.this$0).get() == false) return false;
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
        void hand$iv;
        Object positionEye;
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
            void var10_11 = -yaw * ((float)Math.PI / 180) - (float)Math.PI;
            boolean bl = false;
            float yawCos = (float)Math.cos((double)var10_11);
            void var11_13 = -yaw * ((float)Math.PI / 180) - (float)Math.PI;
            boolean bl2 = false;
            float yawSin = (float)Math.sin((double)var11_13);
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
        IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        positionEye = iEntityPlayerSP.getInventory().getCurrentItemInHand();
        WEnumHand expandSize = WEnumHand.MAIN_HAND;
        IINetHandlerPlayClient iINetHandlerPlayClient2 = iINetHandlerPlayClient;
        boolean $i$f$createUseItemPacket = false;
        IPacket iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem((WEnumHand)hand$iv);
        iINetHandlerPlayClient2.addToSendQueue(iPacket);
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
        float f2 = ((Number)this.BlockRangeValue.get()).floatValue();
        boolean bl = false;
        return Math.max(f, f2);
    }

    private final float getRange(IEntity entity) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        float f = PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) >= ((Number)this.BlockRangeValue.get()).doubleValue() ? ((Number)this.rangeValue.get()).floatValue() : ((Number)this.rangeValue.get()).floatValue();
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

    public KillAura() {
        List list;
        KillAura killAura = this;
        boolean bl = false;
        killAura.prevTargetEntities = list = (List)new ArrayList();
        this.attackTimer = new MSTimer();
        this.switchTimer = new MSTimer();
        this.containerOpen = -1L;
        this.list = "";
    }

    static {
        Companion = new Companion(null);
    }

    public static final /* synthetic */ boolean access$isEnemy(KillAura $this, IEntity entity) {
        return $this.isEnemy(entity);
    }

    public static final /* synthetic */ BoolValue access$getLivingRaycastValue$p(KillAura $this) {
        return $this.livingRaycastValue;
    }

    public static final /* synthetic */ BoolValue access$getRaycastIgnoredValue$p(KillAura $this) {
        return $this.raycastIgnoredValue;
    }

    public static final /* synthetic */ BoolValue access$getAacValue$p(KillAura $this) {
        return $this.aacValue;
    }

    public static final /* synthetic */ IntegerValue access$getMinCPS$p(KillAura $this) {
        return $this.minCPS;
    }

    public static final /* synthetic */ long access$getAttackDelay$p(KillAura $this) {
        return $this.attackDelay;
    }

    public static final /* synthetic */ void access$setAttackDelay$p(KillAura $this, long l) {
        $this.attackDelay = l;
    }

    public static final /* synthetic */ IntegerValue access$getMaxCPS$p(KillAura $this) {
        return $this.maxCPS;
    }

    public static final /* synthetic */ FloatValue access$getMinTurnSpeed$p(KillAura $this) {
        return $this.minTurnSpeed;
    }

    public static final /* synthetic */ FloatValue access$getMaxTurnSpeed$p(KillAura $this) {
        return $this.maxTurnSpeed;
    }

    public static final /* synthetic */ FloatValue access$getMinPredictSize$p(KillAura $this) {
        return $this.minPredictSize;
    }

    public static final /* synthetic */ FloatValue access$getMaxPredictSize$p(KillAura $this) {
        return $this.maxPredictSize;
    }

    public static final int getKillCounts() {
        Companion companion = Companion;
        return killCounts;
    }

    public static final void setKillCounts(int n) {
        Companion companion = Companion;
        killCounts = n;
    }

    public static final class Companion {
        @JvmStatic
        public static /* synthetic */ void killCounts$annotations() {
        }

        public final int getKillCounts() {
            return killCounts;
        }

        public final void setKillCounts(int n) {
            killCounts = n;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

