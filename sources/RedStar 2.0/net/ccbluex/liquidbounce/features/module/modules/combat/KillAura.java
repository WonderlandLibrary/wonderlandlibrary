package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
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
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.modules.hyt.GidentColor;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

@ModuleInfo(name="KillAura", category=ModuleCategory.COMBAT, keyBind=19, description="深海大牛杀戮")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\b\n\t\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\b\n\b\n\n\b\n\n\b\t\n\n\b\n!\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\b\u0000 20:B¢Jp0q2r0&HJs0H2r0tHJu02r0&HJv02\br0tHJ\bw0qHJx0q2y0zHJ{0q2y0|HJ}0q2y0~HJ0q2y0HJ0q2y0HJ0q2y0HJ\t0qHJ0q20t20HJ\t0qHJ0qJ\t0qHJ02r0tHJ\t0qHR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0\tX¢\n\u0000R\n0X¢\n\u0000R\f0X¢\n\u0000R\r0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000\b\"\bR08ÂX¢\bR08ÂX¢\bR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R 0X¢\n\u0000R!0\"X¢\n\u0000R#0\tX¢\n\u0000R$0X¢\n\u0000R%0&X¢\n\u0000R'0X¢\n\u0000R(0X¢\n\u0000R)0X¢\n\u0000R*0X¢\n\u0000R+0X¢\n\u0000R,0X¢\n\u0000R-0X¢\n\u0000R.0¢\b\n\u0000\b/0R10X¢\n\u0000R20X¢\n\u0000R30X¢\n\u0000R40X¢\n\u0000R50¢\b\n\u0000\b67R80&X¢\n\u0000R90X¢\n\u0000R:0X¢\n\u0000R;0X¢\n\u0000R<0X¢\n\u0000R=0>X¢\n\u0000\b?@\"\bABRC0X¢\n\u0000RD0X¢\n\u0000RE0X¢\n\u0000RF0X¢\n\u0000RG0H8BX¢\bIJRK0X¢\n\u0000RL0X¢\n\u0000RM0X¢\n\u0000RN0X¢\n\u0000RO0X¢\n\u0000RP0X¢\n\u0000RQ0X¢\n\u0000RR0X¢\n\u0000RS\b0\"0TX¢\n\u0000RU0X¢\n\u0000RV0X¢\n\u0000RW0X¢\n\u0000RX0¢\b\n\u0000\bYZR[0X¢\n\u0000R\\\u001a0X¢\n\u0000R]0X¢\n\u0000R^0X¢\n\u0000R_0X¢\n\u0000R`0X¢\n\u0000Ra0X¢\n\u0000Rb0X¢\n\u0000Rc0X¢\n\u0000Rd0&X¢\n\u0000\bef\"\bghRi0>8VX¢\bj@Rk0&X¢\n\u0000\blf\"\bmhRn0X¢\n\u0000Ro0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/KillAura;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "BlockRangeValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "aacValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "afterAttackValue", "attackDelay", "", "attackTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "autodisable", "blockModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "blockRate", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "blockingStatus", "", "getBlockingStatus", "()Z", "setBlockingStatus", "(Z)V", "canBlock", "getCanBlock", "cancelRun", "getCancelRun", "circleAccuracy", "circleAlpha", "circleBlue", "circleGreen", "circleRed", "circleValue", "clicks", "", "containerOpen", "cooldownValue", "currentTarget", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "delayedBlockValue", "failRateValue", "fakeSharpValue", "fakeSwingValue", "fovValue", "hitable", "hitableValue", "hurtTimeValue", "getHurtTimeValue", "()Lnet/ccbluex/liquidbounce/value/IntegerValue;", "interactAutoBlockValue", "jelloBlue", "jelloGreen", "jelloRed", "keepSprintValue", "getKeepSprintValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "lastTarget", "lightingModeValue", "lightingSoundValue", "lightingValue", "limitedMultiTargetsValue", "list", "", "getList", "()Ljava/lang/String;", "setList", "(Ljava/lang/String;)V", "livingRaycastValue", "markValue", "maxCPS", "maxPredictSize", "maxRange", "", "getMaxRange", "()F", "maxTurnSpeed", "minCPS", "minPredictSize", "minTurnSpeed", "noInventoryAttackValue", "noInventoryDelayValue", "outborderValue", "predictValue", "prevTargetEntities", "", "priorityValue", "randomCenterValue", "rangeSprintReducementValue", "rangeValue", "getRangeValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "raycastIgnoredValue", "raycastValue", "rotationStrafeValue", "rotations", "silentRotationValue", "stopSprintAir", "swingValue", "switchDelayValue", "switchTimer", "syncEntity", "getSyncEntity", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "setSyncEntity", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;)V", "tag", "getTag", "target", "getTarget", "setTarget", "targetModeValue", "throughWallsRangeValue", "attackEntity", "", "entity", "getRange", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "isAlive", "isEnemy", "onDisable", "onEntityMove", "event", "Lnet/ccbluex/liquidbounce/event/EntityMovementEvent;", "onMotion", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onStrafe", "Lnet/ccbluex/liquidbounce/event/StrafeEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "runAttack", "startBlocking", "interactEntity", "interact", "stopBlocking", "update", "updateHitable", "updateRotations", "updateTarget", "Companion", "Pride"})
public final class KillAura
extends Module {
    private final IntegerValue maxCPS = new IntegerValue(this, "MaxCPS", 8, 1, 20){
        final KillAura this$0;

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
        final KillAura this$0;

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
    @NotNull
    private final IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
    private final FloatValue cooldownValue = new FloatValue("Cooldown", 1.0f, 0.0f, 1.0f);
    @NotNull
    private final FloatValue rangeValue = new FloatValue("Range", 3.7f, 1.0f, 8.0f);
    private final FloatValue BlockRangeValue = new FloatValue("BlockRange", 3.0f, 0.0f, 8.0f);
    private final FloatValue throughWallsRangeValue = new FloatValue("ThroughWallsRange", 3.0f, 0.0f, 8.0f);
    private final FloatValue rangeSprintReducementValue = new FloatValue("RangeSprintReducement", 0.0f, 0.0f, 0.4f);
    private final ListValue priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "Direction", "LivingTime", "HYT"}, "Distance");
    private final ListValue targetModeValue = new ListValue("TargetMode", new String[]{"Single", "Switch", "Multi"}, "Switch");
    private final IntegerValue switchDelayValue = new IntegerValue("SwitchDelay", 700, 0, 2000);
    private final BoolValue swingValue = new BoolValue("Swing", true);
    @NotNull
    private final BoolValue keepSprintValue = new BoolValue("KeepSprint", true);
    private final BoolValue stopSprintAir = new BoolValue("StopSprintOnAir", true);
    private final BoolValue afterAttackValue = new BoolValue("AutoBlock-AfterAttack", false);
    private final BoolValue delayedBlockValue = new BoolValue("AutoBlock-AfterTck", false);
    private final ListValue blockModeValue = new ListValue("AutoBlock", new String[]{"Off", "Packet", "AfterTick", "Nore"}, "Packet");
    private final BoolValue interactAutoBlockValue = new BoolValue("InteractAutoBlock", true);
    private final IntegerValue blockRate = new IntegerValue("BlockRate", 100, 1, 100);
    private final BoolValue autodisable = new BoolValue("AutoDisable", false);
    private final BoolValue raycastValue = new BoolValue("RayCast", true);
    private final BoolValue raycastIgnoredValue = new BoolValue("RayCastIgnored", false);
    private final BoolValue livingRaycastValue = new BoolValue("LivingRayCast", true);
    private final BoolValue aacValue = new BoolValue("AAC", false);
    private final ListValue rotations = new ListValue("RotationMode", new String[]{"Vanilla", "BackTrack", "Test", "HytRotation", "Test2"}, "Test");
    private final BoolValue silentRotationValue = new BoolValue("SilentRotation", true);
    private final ListValue rotationStrafeValue = new ListValue("Strafe", new String[]{"Off", "Strict", "Silent"}, "Off");
    private final BoolValue randomCenterValue = new BoolValue("RandomCenter", true);
    private final BoolValue outborderValue = new BoolValue("Outborder", false);
    private final FloatValue maxTurnSpeed = new FloatValue(this, "MaxTurnSpeed", 180.0f, 0.0f, 180.0f){
        final KillAura this$0;

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
        final KillAura this$0;

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
        final KillAura this$0;

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
        final KillAura this$0;

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
    private final BoolValue fakeSwingValue = new BoolValue("FakeSwing", true);
    private final BoolValue noInventoryAttackValue = new BoolValue("NoInvAttack", false);
    private final IntegerValue noInventoryDelayValue = new IntegerValue("NoInvDelay", 200, 0, 500);
    private final IntegerValue limitedMultiTargetsValue = new IntegerValue("LimitedMultiTargets", 0, 0, 50);
    private final ListValue markValue = new ListValue("Mark", new String[]{"Liquid", "FDP", "Block", "Jello", "Plat", "Red", "Sims", "None", "RedStar", "DeepSea"}, "FDP");
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
    @Nullable
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
    @Nullable
    private IEntityLivingBase syncEntity;
    @NotNull
    private String list;
    private static int killCounts;
    public static final Companion Companion;

    @NotNull
    public final IntegerValue getHurtTimeValue() {
        return this.hurtTimeValue;
    }

    @NotNull
    public final FloatValue getRangeValue() {
        return this.rangeValue;
    }

    @NotNull
    public final BoolValue getKeepSprintValue() {
        return this.keepSprintValue;
    }

    @Nullable
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

    @Nullable
    public final IEntityLivingBase getSyncEntity() {
        return this.syncEntity;
    }

    public final void setSyncEntity(@Nullable IEntityLivingBase iEntityLivingBase) {
        this.syncEntity = iEntityLivingBase;
    }

    @NotNull
    public final String getList() {
        return this.list;
    }

    public final void setList(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull(string, "<set-?>");
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
    public final void onMotion(@NotNull MotionEvent event) {
        block19: {
            block20: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                if (((Boolean)this.stopSprintAir.get()).booleanValue()) {
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
                if (!StringsKt.equals((String)this.blockModeValue.get(), "AfterTick", true)) break block20;
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
            if (this.switchTimer.hasTimePassed(((Number)this.switchDelayValue.get()).intValue()) || Intrinsics.areEqual((String)this.targetModeValue.get(), "Switch") ^ true) {
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
        if (StringsKt.equals((String)this.rotationStrafeValue.get(), "Off", true)) {
            this.update();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onStrafe(@NotNull StrafeEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (StringsKt.equals((String)this.rotationStrafeValue.get(), "Off", true)) {
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
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        string = string3;
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
        return;
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
        if (StringsKt.equals((String)this.targetModeValue.get(), "Switch", true)) return;
        if (!this.isEnemy(this.currentTarget)) return;
        this.target = this.currentTarget;
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.autodisable.get()).booleanValue()) {
            LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class).setState(false);
            LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Disabler", "KillAura was disabled,because you are dead", NotifyType.WARNING, 6000, 0, 16, null));
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.lightingValue.get()).booleanValue()) {
            var2_2 = (String)this.lightingModeValue.get();
            var3_4 = false;
            v0 = var2_2;
            if (v0 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            v1 = v0.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(v1, "(this as java.lang.String).toLowerCase()");
            var2_2 = v1;
            switch (var2_2.hashCode()) {
                case -1407259064: {
                    if (!var2_2.equals("attack")) ** break;
                    break;
                }
                case 3079268: {
                    if (!var2_2.equals("dead")) ** break;
                    if (this.target != null) {
                        if (this.lastTarget == null) {
                            v2 = this.target;
                        } else {
                            v3 = this.lastTarget;
                            if (v3 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (v3.getHealth() <= (float)false) {
                                v4 = MinecraftInstance.mc.getNetHandler2();
                                v5 = (World)MinecraftInstance.mc2.world;
                                v6 = this.lastTarget;
                                if (v6 == null) {
                                    Intrinsics.throwNpe();
                                }
                                v7 = v6.getPosX();
                                v8 = this.lastTarget;
                                if (v8 == null) {
                                    Intrinsics.throwNpe();
                                }
                                v9 = v8.getPosY();
                                v10 = this.lastTarget;
                                if (v10 == null) {
                                    Intrinsics.throwNpe();
                                }
                                v4.handleSpawnGlobalEntity(new SPacketSpawnGlobalEntity((Entity)new EntityLightningBolt(v5, v7, v9, v10.getPosZ(), true)));
                                if (((Boolean)this.lightingSoundValue.get()).booleanValue()) {
                                    MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 0.5f);
                                }
                            }
                            v2 = this.target;
                        }
                        this.lastTarget = v2;
                        ** break;
                    }
                    if (this.lastTarget == null) ** break;
                    v11 = this.lastTarget;
                    if (v11 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!(v11.getHealth() <= (float)false)) ** break;
                    v12 = MinecraftInstance.mc.getNetHandler2();
                    v13 = (World)MinecraftInstance.mc2.world;
                    v14 = this.lastTarget;
                    if (v14 == null) {
                        Intrinsics.throwNpe();
                    }
                    v15 = v14.getPosX();
                    v16 = this.lastTarget;
                    if (v16 == null) {
                        Intrinsics.throwNpe();
                    }
                    v17 = v16.getPosY();
                    v18 = this.lastTarget;
                    if (v18 == null) {
                        Intrinsics.throwNpe();
                    }
                    v12.handleSpawnGlobalEntity(new SPacketSpawnGlobalEntity((Entity)new EntityLightningBolt(v13, v15, v17, v18.getPosZ(), true)));
                    if (((Boolean)this.lightingSoundValue.get()).booleanValue()) {
                        MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 0.5f);
                    }
                    this.lastTarget = this.target;
                    ** break;
                }
            }
            v19 = MinecraftInstance.mc.getNetHandler2();
            v20 = (World)MinecraftInstance.mc2.world;
            v21 = this.target;
            if (v21 == null) {
                Intrinsics.throwNpe();
            }
            v22 = v21.getPosX();
            v23 = this.target;
            if (v23 == null) {
                Intrinsics.throwNpe();
            }
            v24 = v23.getPosY();
            v25 = this.target;
            if (v25 == null) {
                Intrinsics.throwNpe();
            }
            v19.handleSpawnGlobalEntity(new SPacketSpawnGlobalEntity((Entity)new EntityLightningBolt(v20, v22, v24, v25.getPosZ(), true)));
            if (!((Boolean)this.lightingSoundValue.get()).booleanValue()) ** break;
            MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 0.5f);
            ** break;
        }
lbl84:
        // 11 sources

        if (this.syncEntity != null) {
            v26 = this.syncEntity;
            if (v26 == null) {
                Intrinsics.throwNpe();
            }
            if (v26.isDead()) {
                ++KillAura.killCounts;
                this.syncEntity = null;
            }
        }
        this_$iv = this;
        $i$f$getCancelRun = false;
        v27 = MinecraftInstance.mc.getThePlayer();
        if (v27 == null) {
            Intrinsics.throwNpe();
        }
        if (v27.isSpectator()) ** GOTO lbl-1000
        v28 = MinecraftInstance.mc.getThePlayer();
        if (v28 == null) {
            Intrinsics.throwNpe();
        }
        if (!KillAura.access$isAlive(this_$iv, v28) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState() || LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class).getState()) lbl-1000:
        // 2 sources

        {
            v29 = true;
        } else {
            v29 = false;
        }
        if (v29) {
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
            v30 = MinecraftInstance.mc.getThePlayer();
            if (v30 == null) {
                Intrinsics.throwNpe();
            }
            if (v30.getCooledAttackStrength(0.0f) >= ((Number)this.cooldownValue.get()).floatValue()) {
                while (this.clicks > 0) {
                    this.runAttack();
                    this_$iv = this.clicks;
                    this.clicks = this_$iv + -1;
                }
            }
        }
        v31 = MinecraftInstance.mc.getThePlayer();
        if (v31 == null) {
            return;
        }
        thePlayer = v31;
        if (((Boolean)this.autodisable.get()).booleanValue()) {
            if (thePlayer.getHealth() == 0.0f || thePlayer.isDead()) {
                LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class).setState(false);
                LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Win", "KillAura was disabled,because the game is over", NotifyType.INFO, 6000, 0, 16, null));
            }
            if (thePlayer.isSpectator()) {
                LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class).setState(false);
                LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Win", "KillAura was disabled,because the game is over", NotifyType.INFO, 6000, 0, 16, null));
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
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
            v17 = RangesKt.step(new IntRange(var5_2, 360), 61 - ((Number)this.circleAccuracy.get()).intValue());
            var2_8 = v17.getFirst();
            var3_13 = v17.getLast();
            var4_17 = v17.getStep();
            v18 = var2_8;
            v19 = var3_13;
            if (var4_17 >= 0 ? v18 <= v19 : v18 >= v19) {
                while (true) {
                    var5_3 = (double)i * 3.141592653589793 / 180.0;
                    var7_18 = false;
                    v20 = (float)Math.cos(var5_3) * ((Number)this.rangeValue.get()).floatValue();
                    var5_3 = (double)i * 3.141592653589793 / 180.0;
                    var30_22 = v20;
                    var7_18 = false;
                    var31_26 = Math.sin(var5_3);
                    GL11.glVertex2f((float)var30_22, (float)((float)var31_26 * ((Number)this.rangeValue.get()).floatValue()));
                    if (i == var3_13) break;
                    i += var4_17;
                }
            }
            i = 6.283185307179586;
            var4_17 = 0;
            v21 = (float)Math.cos(i) * ((Number)this.rangeValue.get()).floatValue();
            i = 6.283185307179586;
            var30_22 = v21;
            var4_17 = 0;
            var31_26 = Math.sin(i);
            GL11.glVertex2f((float)var30_22, (float)((float)var31_26 * ((Number)this.rangeValue.get()).floatValue()));
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
        var2_10 = (String)this.markValue.get();
        $i$f$getCancelRun = false;
        v25 = var2_10;
        if (v25 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        v26 = v25.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(v26, "(this as java.lang.String).toLowerCase()");
        var2_10 = v26;
        tmp = -1;
        switch (var2_10.hashCode()) {
            case 112785: {
                if (!var2_10.equals("red")) break;
                tmp = 1;
                break;
            }
            case 101009364: {
                if (!var2_10.equals("jello")) break;
                tmp = 2;
                break;
            }
            case 101234: {
                if (!var2_10.equals("fdp")) break;
                tmp = 3;
                break;
            }
            case -1102567108: {
                if (!var2_10.equals("liquid")) break;
                tmp = 4;
                break;
            }
            case 3530364: {
                if (!var2_10.equals("sims")) break;
                tmp = 5;
                break;
            }
            case 93832333: {
                if (!var2_10.equals("block")) break;
                tmp = 6;
                break;
            }
            case 3443503: {
                if (!var2_10.equals("plat")) break;
                tmp = 7;
                break;
            }
            case 1082688131: {
                if (!var2_10.equals("redStar")) break;
                tmp = 8;
                break;
            }
        }
        switch (tmp) {
            case 4: {
                v27 = this.target;
                if (v27 == null) {
                    Intrinsics.throwNpe();
                }
                v28 = v27;
                v29 = this.target;
                if (v29 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v28, v29.getHurtTime() <= 0 ? new Color(37, 126, 255, 170) : new Color(255, 0, 0, 170));
                break;
            }
            case 7: {
                v30 = this.target;
                if (v30 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v30, this.hitable != false ? new Color(37, 126, 255, 70) : new Color(255, 0, 0, 70));
                break;
            }
            case 6: {
                v31 = this.target;
                if (v31 == null) {
                    Intrinsics.throwNpe();
                }
                bb = v31.getEntityBoundingBox();
                v32 = this.target;
                if (v32 == null) {
                    Intrinsics.throwNpe();
                }
                v32.setEntityBoundingBox(bb.expand(0.2, 0.2, 0.2));
                v33 = this.target;
                if (v33 == null) {
                    Intrinsics.throwNpe();
                }
                v34 = v33;
                v35 = this.target;
                if (v35 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawEntityBox(v34, v35.getHurtTime() <= 0 ? Color.GREEN : Color.RED, true);
                v36 = this.target;
                if (v36 == null) {
                    Intrinsics.throwNpe();
                }
                v36.setEntityBoundingBox(bb);
                break;
            }
            case 1: {
                v37 = this.target;
                if (v37 == null) {
                    Intrinsics.throwNpe();
                }
                v38 = v37;
                v39 = this.target;
                if (v39 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.drawPlatform(v38, v39.getHurtTime() <= 0 ? new Color(255, 255, 255, 255) : new Color(124, 215, 255, 255));
                break;
            }
            case 5: {
                radius = 0.15f;
                side = 4;
                GL11.glPushMatrix();
                v40 = this.target;
                if (v40 == null) {
                    Intrinsics.throwNpe();
                }
                v41 = v40.getLastTickPosX();
                v42 = this.target;
                if (v42 == null) {
                    Intrinsics.throwNpe();
                }
                v43 = v42.getPosX();
                v44 = this.target;
                if (v44 == null) {
                    Intrinsics.throwNpe();
                }
                v45 = v41 + (v43 - v44.getLastTickPosX()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosX();
                v46 = this.target;
                if (v46 == null) {
                    Intrinsics.throwNpe();
                }
                v47 = v46.getLastTickPosY();
                v48 = this.target;
                if (v48 == null) {
                    Intrinsics.throwNpe();
                }
                v49 = v48.getPosY();
                v50 = this.target;
                if (v50 == null) {
                    Intrinsics.throwNpe();
                }
                v51 = v47 + (v49 - v50.getLastTickPosY()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosY();
                v52 = this.target;
                if (v52 == null) {
                    Intrinsics.throwNpe();
                }
                v53 = v51 + (double)v52.getHeight() * 1.1;
                v54 = this.target;
                if (v54 == null) {
                    Intrinsics.throwNpe();
                }
                v55 = v54.getLastTickPosZ();
                v56 = this.target;
                if (v56 == null) {
                    Intrinsics.throwNpe();
                }
                v57 = v56.getPosZ();
                v58 = this.target;
                if (v58 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glTranslated((double)v45, (double)v53, (double)(v55 + (v57 - v58.getLastTickPosZ()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                v59 = this.target;
                if (v59 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glRotatef((float)(-v59.getWidth()), (float)0.0f, (float)1.0f, (float)0.0f);
                v60 = MinecraftInstance.mc.getThePlayer();
                if (v60 == null) {
                    Intrinsics.throwNpe();
                }
                GL11.glRotatef((float)(((float)v60.getTicksExisted() + MinecraftInstance.mc.getTimer().getRenderPartialTicks()) * (float)5), (float)0.0f, (float)1.0f, (float)0.0f);
                v61 = this.target;
                if (v61 == null) {
                    Intrinsics.throwNpe();
                }
                RenderUtils.glColor(v61.getHurtTime() <= 0 ? new Color(80, 255, 80) : new Color(255, 0, 0));
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
                v62 = this.target;
                if (v62 == null) {
                    Intrinsics.throwNpe();
                }
                bb = v62.getEntityBoundingBox();
                radius = bb.getMaxX() - bb.getMinX() + 0.3;
                height = bb.getMaxY() - bb.getMinY();
                v63 = this.target;
                if (v63 == null) {
                    Intrinsics.throwNpe();
                }
                v64 = v63.getLastTickPosX();
                v65 = this.target;
                if (v65 == null) {
                    Intrinsics.throwNpe();
                }
                v66 = v65.getPosX();
                v67 = this.target;
                if (v67 == null) {
                    Intrinsics.throwNpe();
                }
                x = v64 + (v66 - v67.getLastTickPosX()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosX();
                v68 = this.target;
                if (v68 == null) {
                    Intrinsics.throwNpe();
                }
                v69 = v68.getLastTickPosY();
                v70 = this.target;
                if (v70 == null) {
                    Intrinsics.throwNpe();
                }
                v71 = v70.getPosY();
                v72 = this.target;
                if (v72 == null) {
                    Intrinsics.throwNpe();
                }
                y = v69 + (v71 - v72.getLastTickPosY()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosY() + height * drawPercent;
                v73 = this.target;
                if (v73 == null) {
                    Intrinsics.throwNpe();
                }
                v74 = v73.getLastTickPosZ();
                v75 = this.target;
                if (v75 == null) {
                    Intrinsics.throwNpe();
                }
                v76 = v75.getPosZ();
                v77 = this.target;
                if (v77 == null) {
                    Intrinsics.throwNpe();
                }
                z = v74 + (v76 - v77.getLastTickPosZ()) * (double)event.getPartialTicks() - MinecraftInstance.mc.getRenderManager().getViewerPosZ();
                GL11.glLineWidth((float)((float)(radius * (double)5.0f)));
                GL11.glBegin((int)3);
                var18_36 = 0;
                var19_37 = 360;
                while (var18_36 <= var19_37) {
                    v78 = MinecraftInstance.mc.getThePlayer();
                    if (v78 == null) {
                        Intrinsics.throwNpe();
                    }
                    var21_50 = (double)i / 50.0 * 1.75;
                    var32_42 = (double)v78.getTicksExisted() / 70.0;
                    var23_55 = false;
                    var34_43 = Math.sin(var21_50);
                    var41_46 = Color.HSBtoRGB((float)(var32_42 + var34_43) % 1.0f, 0.7f, 1.0f);
                    rainbow = new Color(var41_46);
                    GL11.glColor3f((float)((float)rainbow.getRed() / 255.0f), (float)((float)rainbow.getGreen() / 255.0f), (float)((float)rainbow.getBlue() / 255.0f));
                    var21_50 = (double)i * 6.283185307179586 / 45.0;
                    var32_42 = radius;
                    var30_23 = x;
                    var23_55 = false;
                    var34_43 = Math.cos(var21_50);
                    v79 = var30_23 + var32_42 * var34_43;
                    var21_50 = (double)i * 6.283185307179586 / 45.0;
                    var36_44 = radius;
                    var34_43 = z;
                    var32_42 = y;
                    var30_23 = v79;
                    var23_55 = false;
                    var38_45 = Math.sin(var21_50);
                    GL11.glVertex3d((double)var30_23, (double)var32_42, (double)(var34_43 + var36_44 * var38_45));
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
                v80 = this.target;
                if (v80 == null) {
                    Intrinsics.throwNpe();
                }
                bb = v80.getEntityBoundingBox();
                radius = bb.getMaxX() - bb.getMinX();
                height = bb.getMaxY() - bb.getMinY();
                v81 = this.target;
                if (v81 == null) {
                    Intrinsics.throwNpe();
                }
                v82 = v81.getLastTickPosX();
                v83 = this.target;
                if (v83 == null) {
                    Intrinsics.throwNpe();
                }
                v84 = v83.getPosX();
                v85 = this.target;
                if (v85 == null) {
                    Intrinsics.throwNpe();
                }
                posX = v82 + (v84 - v85.getLastTickPosX()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                v86 = this.target;
                if (v86 == null) {
                    Intrinsics.throwNpe();
                }
                v87 = v86.getLastTickPosY();
                v88 = this.target;
                if (v88 == null) {
                    Intrinsics.throwNpe();
                }
                v89 = v88.getPosY();
                v90 = this.target;
                if (v90 == null) {
                    Intrinsics.throwNpe();
                }
                posY = v87 + (v89 - v90.getLastTickPosY()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                posY = drawMode ? (posY -= 0.5) : (posY += 0.5);
                v91 = this.target;
                if (v91 == null) {
                    Intrinsics.throwNpe();
                }
                v92 = v91.getLastTickPosZ();
                v93 = this.target;
                if (v93 == null) {
                    Intrinsics.throwNpe();
                }
                v94 = v93.getPosZ();
                v95 = this.target;
                if (v95 == null) {
                    Intrinsics.throwNpe();
                }
                posZ = v92 + (v94 - v95.getLastTickPosZ()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                var22_68 = 0;
                v96 = RangesKt.step(new IntRange(var22_68, 360), 7);
                var19_38 = v96.getFirst();
                var20_48 = v96.getLast();
                var21_51 = v96.getStep();
                v97 = var19_38;
                v98 = var20_48;
                if (var21_51 >= 0 ? v97 <= v98 : v97 >= v98) {
                    while (true) {
                        var22_69 = (double)i * 3.141592653589793 / (double)180.0f;
                        var33_74 = posX;
                        var30_24 = points;
                        var24_72 = 0;
                        var35_76 = Math.sin(var22_69);
                        v99 = var33_74 - var35_76 * radius;
                        var22_69 = (double)i * 3.141592653589793 / (double)180.0f;
                        var37_78 = posZ;
                        var35_76 = posY + height * drawPercent;
                        var33_74 = v99;
                        var24_72 = 0;
                        var39_80 = Math.cos(var22_69);
                        var42_82 = var37_78 + var39_80 * radius;
                        var44_83 = var35_76;
                        var46_84 = var33_74;
                        var30_24.add(new WVec3(var46_84, var44_83, var42_82));
                        if (i == var20_48) break;
                        i += var21_51;
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
                var23_56 = 0;
                var24_72 = 20;
                while (var23_56 <= var24_72) {
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
                break;
            }
            case 8: {
                drawTime = (int)(System.currentTimeMillis() % (long)2000);
                drawMode = drawTime > 1000;
                drawPercent = (double)drawTime / 1000.0;
                drawPercent = !drawMode ? (double)true - drawPercent : (drawPercent -= (double)true);
                drawPercent = EaseUtils.easeInOutQuad(drawPercent);
                bb = false;
                points = new ArrayList<E>();
                v100 = this.target;
                if (v100 == null) {
                    Intrinsics.throwNpe();
                }
                bb = v100.getEntityBoundingBox();
                radius = bb.getMaxX() - bb.getMinX();
                height = bb.getMaxY() - bb.getMinY();
                v101 = this.target;
                if (v101 == null) {
                    Intrinsics.throwNpe();
                }
                v102 = v101.getLastTickPosX();
                v103 = this.target;
                if (v103 == null) {
                    Intrinsics.throwNpe();
                }
                v104 = v103.getPosX();
                v105 = this.target;
                if (v105 == null) {
                    Intrinsics.throwNpe();
                }
                posX = v102 + (v104 - v105.getLastTickPosX()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                v106 = this.target;
                if (v106 == null) {
                    Intrinsics.throwNpe();
                }
                v107 = v106.getLastTickPosY();
                v108 = this.target;
                if (v108 == null) {
                    Intrinsics.throwNpe();
                }
                v109 = v108.getPosY();
                v110 = this.target;
                if (v110 == null) {
                    Intrinsics.throwNpe();
                }
                posY = v107 + (v109 - v110.getLastTickPosY()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                posY = drawMode ? (posY -= 0.5) : (posY += 0.5);
                v111 = this.target;
                if (v111 == null) {
                    Intrinsics.throwNpe();
                }
                v112 = v111.getLastTickPosZ();
                v113 = this.target;
                if (v113 == null) {
                    Intrinsics.throwNpe();
                }
                v114 = v113.getPosZ();
                v115 = this.target;
                if (v115 == null) {
                    Intrinsics.throwNpe();
                }
                posZ = v112 + (v114 - v115.getLastTickPosZ()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks();
                var22_70 = 0;
                v116 = RangesKt.step(new IntRange(var22_70, 360), 7);
                baseMove = v116.getFirst();
                var20_49 = v116.getLast();
                min = v116.getStep();
                v117 = baseMove;
                v118 = var20_49;
                if (min >= 0 ? v117 <= v118 : v117 >= v118) {
                    while (true) {
                        var22_71 = (double)i * 3.141592653589793 / (double)180.0f;
                        var33_75 = posX;
                        var30_25 = points;
                        var24_73 = 0;
                        var35_77 = Math.sin(var22_71);
                        v119 = var33_75 - var35_77 * radius;
                        var22_71 = (double)i * 3.141592653589793 / (double)180.0f;
                        var37_79 = posZ;
                        var35_77 = posY + height * drawPercent;
                        var33_75 = v119;
                        var24_73 = 0;
                        var39_81 = Math.cos(var22_71);
                        var48_93 = var37_79 + var39_81 * radius;
                        var50_94 = var35_77;
                        var52_95 = var33_75;
                        var30_25.add(new WVec3(var52_95, var50_94, var48_93));
                        if (i == var20_49) break;
                        i += min;
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
                var24_73 = 20;
                for (i = 0; i <= var24_73; ++i) {
                    moveFace = height / (double)60.0f * (double)i * baseMove;
                    if (drawMode) {
                        moveFace = -moveFace;
                    }
                    firstPoint = (WVec3)points.get(0);
                    GL11.glVertex3d((double)(firstPoint.getXCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosX()), (double)(firstPoint.getYCoord() - moveFace - min - MinecraftInstance.mc.getRenderManager().getViewerPosY()), (double)(firstPoint.getZCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                    GL11.glColor4f((float)((float)((Number)GidentColor.Companion.getR().get()).intValue() / 255.0f), (float)((float)((Number)GidentColor.Companion.getG().get()).intValue() / 255.0f), (float)((float)((Number)GidentColor.Companion.getB().get()).intValue() / 255.0f), (float)(0.7f * ((float)i / 20.0f)));
                    for (WVec3 vec3 : points) {
                        GL11.glVertex3d((double)(vec3.getXCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosX()), (double)(vec3.getYCoord() - moveFace - min - MinecraftInstance.mc.getRenderManager().getViewerPosY()), (double)(vec3.getZCoord() - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
                    }
                    GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
                }
                GL11.glEnd();
                GL11.glEnable((int)2929);
                GL11.glDisable((int)2848);
                GL11.glDisable((int)3042);
                GL11.glEnable((int)3553);
                GL11.glPopMatrix();
                break;
            }
        }
        if (this.currentTarget != null && this.attackTimer.hasTimePassed(this.attackDelay)) {
            v120 = this.currentTarget;
            if (v120 == null) {
                Intrinsics.throwNpe();
            }
            if (v120.getHurtTime() <= ((Number)this.hurtTimeValue.get()).intValue()) {
                var2_11 = this.clicks;
                this.clicks = var2_11 + 1;
                this.attackTimer.reset();
                this.attackDelay = TimeUtils.randomClickDelay(((Number)this.minCPS.get()).intValue(), ((Number)this.maxCPS.get()).intValue());
            }
        }
        var2_12 = (String)this.markValue.get();
        var3_16 = false;
        v121 = var2_12;
        if (v121 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        v122 = v121.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(v122, "(this as java.lang.String).toLowerCase()");
        if (v122.equals("DeepSea") && !StringsKt.equals((String)this.targetModeValue.get(), "Multi", true)) {
            v123 = this.target;
            v124 = Color.RED;
            Intrinsics.checkExpressionValueIsNotNull(v124, "Color.RED");
            RenderUtils.drawCircleESP(v123, 0.67, v124.getRGB(), true);
        }
    }

    @EventTarget
    public final void onEntityMove(@NotNull EntityMovementEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntity movedEntity = event.getMovedEntity();
        if (this.target == null || Intrinsics.areEqual(movedEntity, this.currentTarget) ^ true) {
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
        multi = StringsKt.equals((String)this.targetModeValue.get(), "Multi", true);
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
            if (Intrinsics.areEqual(this.target, this.currentTarget)) {
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
        boolean switchMode = StringsKt.equals((String)this.targetModeValue.get(), "Switch", true);
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
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "distance": {
                List $this$sortBy$iv = targets;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                boolean bl3 = false;
                Comparator comparator = new Comparator<T>(thePlayer){
                    final IEntityPlayerSP $thePlayer$inlined;
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
                        return ComparisonsKt.compareValues(comparable2, (Comparable)d);
                    }
                };
                CollectionsKt.sortWith(list, comparator);
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
                        return ComparisonsKt.compareValues(comparable2, (Comparable)f);
                    }
                };
                CollectionsKt.sortWith(list, comparator);
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
                        return ComparisonsKt.compareValues(comparable2, (Comparable)d);
                    }
                };
                CollectionsKt.sortWith(list, comparator);
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
                        return ComparisonsKt.compareValues(comparable2, (Comparable)n);
                    }
                };
                CollectionsKt.sortWith(list, comparator);
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
                        return ComparisonsKt.compareValues(comparable2, (Comparable)n);
                    }
                };
                CollectionsKt.sortWith(list, comparator);
                break;
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
        if (MinecraftInstance.classProvider.isEntityLivingBase(entity) && entity != null && (EntityUtils.targetDead || this.isAlive(entity.asEntityLivingBase())) && Intrinsics.areEqual(entity, MinecraftInstance.mc.getThePlayer()) ^ true) {
            if (!EntityUtils.targetInvisible && entity.isInvisible()) {
                return false;
            }
            if (EntityUtils.targetPlayer && MinecraftInstance.classProvider.isEntityPlayer(entity)) {
                IEntityPlayer player = entity.asEntityPlayer();
                if (player.isSpectator() || AntiBot.isBot(player)) {
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
        block27: {
            block28: {
                v0 = MinecraftInstance.mc.getThePlayer();
                if (v0 == null) {
                    Intrinsics.throwNpe();
                }
                if ((thePlayer = v0).isBlocking() || this.blockingStatus) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                    if (((Number)this.blockRate.get()).intValue() <= 0 || new Random().nextInt(100) > ((Number)this.blockRate.get()).intValue()) {
                        return;
                    }
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
                if (!StringsKt.equals((String)this.blockModeValue.get(), "Packet", true)) break block27;
                if (thePlayer.isBlocking()) break block28;
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
                if (!v9) break block27;
            }
            this.startBlocking(entity, (Boolean)this.interactAutoBlockValue.get());
        }
        thePlayer.resetCooldown();
        thePlayer.resetCooldown();
    }

    private final boolean updateRotations(IEntity entity) {
        Object limitedRotation;
        Rotation rotation;
        if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
            return true;
        }
        IAxisAlignedBB boundingBox = entity.getEntityBoundingBox();
        if (StringsKt.equals((String)this.rotations.get(), "Vanilla", true)) {
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
            Rotation rotation2 = vecRotation2.component2();
            Rotation rotation3 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation2, (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            Intrinsics.checkExpressionValueIsNotNull(rotation3, "RotationUtils.limitAngle…rnSpeed.get()).toFloat())");
            Rotation limitedRotation2 = rotation3;
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
        if (StringsKt.equals((String)this.rotations.get(), "Test2", true)) {
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
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                double d3 = (d - (d2 - iEntityPlayerSP3.getPrevPosX())) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue());
                double d4 = entity.getPosY() - entity.getPrevPosY();
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                double d5 = iEntityPlayerSP4.getPosY();
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                double d6 = (d4 - (d5 - iEntityPlayerSP5.getPrevPosY())) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue());
                double d7 = entity.getPosZ() - entity.getPrevPosZ();
                IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP6 == null) {
                    Intrinsics.throwNpe();
                }
                double d8 = iEntityPlayerSP6.getPosZ();
                IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP7 == null) {
                    Intrinsics.throwNpe();
                }
                boundingBox2 = boundingBox2.offset(d3, d6, (d7 - (d8 - iEntityPlayerSP7.getPrevPosZ())) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            boolean bl = (Boolean)this.outborderValue.get() != false && !this.attackTimer.hasTimePassed(this.attackDelay / (long)2);
            boolean bl4 = (Boolean)this.randomCenterValue.get();
            boolean bl5 = (Boolean)this.predictValue.get();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            VecRotation vecRotation = RotationUtils.searchCenter(boundingBox2, bl, bl4, bl5, PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            Object limitedRotation2 = vecRotation;
            Rotation rotation4 = ((VecRotation)limitedRotation2).component2();
            Rotation rotation5 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation4, (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            Intrinsics.checkExpressionValueIsNotNull(rotation5, "RotationUtils.limitAngle…rnSpeed.get()).toFloat())");
            limitedRotation2 = rotation5;
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation((Rotation)limitedRotation2, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP8 == null) {
                    Intrinsics.throwNpe();
                }
                ((Rotation)limitedRotation2).toPlayer(iEntityPlayerSP8);
            }
            return true;
        }
        if (StringsKt.equals((String)this.rotations.get(), "BackTrack", true)) {
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.offset((entity.getPosX() - entity.getPrevPosX()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosY() - entity.getPrevPosY()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosZ() - entity.getPrevPosZ()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            WVec3 wVec3 = RotationUtils.getCenter(entity.getEntityBoundingBox());
            boolean bl = (Boolean)this.predictValue.get();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            Rotation rotation6 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, RotationUtils.OtherRotation(boundingBox, wVec3, bl, PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange()), (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            Intrinsics.checkExpressionValueIsNotNull(rotation6, "RotationUtils.limitAngle…rnSpeed.get()).toFloat())");
            Rotation limitedRotation3 = rotation6;
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation(limitedRotation3, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP9 == null) {
                    Intrinsics.throwNpe();
                }
                limitedRotation3.toPlayer(iEntityPlayerSP9);
                return true;
            }
        }
        if (StringsKt.equals((String)this.rotations.get(), "Test", true)) {
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
            VecRotation vecRotation = RotationUtils.lockView(boundingBox, bl, bl6, bl7, PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            VecRotation rotation4 = vecRotation;
            rotation = rotation4.component2();
            Rotation rotation7 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation, (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            Intrinsics.checkExpressionValueIsNotNull(rotation7, "RotationUtils.limitAngle…rnSpeed.get()).toFloat())");
            limitedRotation = rotation7;
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation((Rotation)limitedRotation, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP10 == null) {
                    Intrinsics.throwNpe();
                }
                ((Rotation)limitedRotation).toPlayer(iEntityPlayerSP10);
                return true;
            }
        }
        if (StringsKt.equals((String)this.rotations.get(), "HytRotation", true)) {
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
            VecRotation vecRotation = RotationUtils.lockView(boundingBox, bl, bl8, bl9, PlayerExtensionKt.getDistanceToEntityBox(iEntityPlayerSP, entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            limitedRotation = vecRotation;
            rotation = ((VecRotation)limitedRotation).component2();
            Rotation rotation8 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation, (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            Intrinsics.checkExpressionValueIsNotNull(rotation8, "RotationUtils.limitAngle…rnSpeed.get()).toFloat())");
            limitedRotation = rotation8;
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation((Rotation)limitedRotation, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP11 == null) {
                    Intrinsics.throwNpe();
                }
                ((Rotation)limitedRotation).toPlayer(iEntityPlayerSP11);
            }
            return true;
        }
        return true;
    }

    private final void updateHitable() {
        int hurtTime = ((Number)this.hurtTimeValue.get()).intValue();
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
                final KillAura this$0;

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
            this.hitable = ((Number)this.maxTurnSpeed.get()).floatValue() > 0.0f ? Intrinsics.areEqual(this.currentTarget, raycastedEntity2) : true;
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
            this.blockingStatus = false;
        }
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
    @Nullable
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

    public static final boolean access$isEnemy(KillAura $this, IEntity entity) {
        return $this.isEnemy(entity);
    }

    public static final BoolValue access$getLivingRaycastValue$p(KillAura $this) {
        return $this.livingRaycastValue;
    }

    public static final BoolValue access$getRaycastIgnoredValue$p(KillAura $this) {
        return $this.raycastIgnoredValue;
    }

    public static final BoolValue access$getAacValue$p(KillAura $this) {
        return $this.aacValue;
    }

    public static final IntegerValue access$getMinCPS$p(KillAura $this) {
        return $this.minCPS;
    }

    public static final long access$getAttackDelay$p(KillAura $this) {
        return $this.attackDelay;
    }

    public static final void access$setAttackDelay$p(KillAura $this, long l) {
        $this.attackDelay = l;
    }

    public static final IntegerValue access$getMaxCPS$p(KillAura $this) {
        return $this.maxCPS;
    }

    public static final FloatValue access$getMinTurnSpeed$p(KillAura $this) {
        return $this.minTurnSpeed;
    }

    public static final FloatValue access$getMaxTurnSpeed$p(KillAura $this) {
        return $this.maxTurnSpeed;
    }

    public static final FloatValue access$getMinPredictSize$p(KillAura $this) {
        return $this.minPredictSize;
    }

    public static final FloatValue access$getMaxPredictSize$p(KillAura $this) {
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

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\b\n\b\n\b\b\u000020B\b¢R$08@X¢\n\u0000\b\b\"\b\b\t¨\n"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/KillAura$Companion;", "", "()V", "killCounts", "", "killCounts$annotations", "getKillCounts", "()I", "setKillCounts", "(I)V", "Pride"})
    public static final class Companion {
        @JvmStatic
        public static void killCounts$annotations() {
        }

        public final int getKillCounts() {
            return killCounts;
        }

        public final void setKillCounts(int n) {
            killCounts = n;
        }

        private Companion() {
        }

        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
