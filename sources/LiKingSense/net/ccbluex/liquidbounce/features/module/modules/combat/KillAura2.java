/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.comparisons.ComparisonsKt
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
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
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.event.EntityMovementEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.NoFriends;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RaycastUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="KillAura2", description="Automatically attacks targets around you.", category=ModuleCategory.COMBAT, keyBind=19)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0094\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0010\u0007\n\u0002\b\u000f\n\u0002\u0010!\n\u0002\b\u0012\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0007\u0018\u0000 \u0087\u00012\u00020\u0001:\u0002\u0087\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010j\u001a\u00020k2\u0006\u0010l\u001a\u00020\"H\u0002J\u0010\u0010m\u001a\u00020;2\u0006\u0010l\u001a\u00020nH\u0002J\u0010\u0010o\u001a\u00020\u00102\u0006\u0010l\u001a\u00020\"H\u0002J\u0012\u0010p\u001a\u00020\u00102\b\u0010l\u001a\u0004\u0018\u00010nH\u0002J\b\u0010q\u001a\u00020kH\u0016J\b\u0010r\u001a\u00020kH\u0016J\u0010\u0010s\u001a\u00020k2\u0006\u0010t\u001a\u00020uH\u0007J\u0010\u0010v\u001a\u00020k2\u0006\u0010t\u001a\u00020wH\u0007J\u0010\u0010x\u001a\u00020k2\u0006\u0010t\u001a\u00020yH\u0007J\u0010\u0010z\u001a\u00020k2\u0006\u0010t\u001a\u00020{H\u0007J\u0010\u0010|\u001a\u00020k2\u0006\u0010t\u001a\u00020}H\u0007J\b\u0010~\u001a\u00020kH\u0002J\u001a\u0010\u007f\u001a\u00020k2\u0007\u0010\u0080\u0001\u001a\u00020n2\u0007\u0010\u0081\u0001\u001a\u00020\u0010H\u0002J\t\u0010\u0082\u0001\u001a\u00020kH\u0002J\u0007\u0010\u0083\u0001\u001a\u00020kJ\t\u0010\u0084\u0001\u001a\u00020kH\u0002J\u0011\u0010\u0085\u0001\u001a\u00020\u00102\u0006\u0010l\u001a\u00020nH\u0002J\t\u0010\u0086\u0001\u001a\u00020kH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0015\u0010\u0015\u001a\u00020\u00108\u00c2\u0002X\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0012R\u0015\u0010\u0017\u001a\u00020\u00108\u00c2\u0002X\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u0012R\u000e\u0010\u0019\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010&\u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b'\u0010(R\u000e\u0010)\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010+\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010-R\u000e\u0010.\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010/\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u00101R\u0010\u00102\u001a\u0004\u0018\u00010\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u00107\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010-R\u000e\u00109\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010:\u001a\u00020;8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b<\u0010=R\u0011\u0010>\u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010(R\u0011\u0010@\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u0010-R\u000e\u0010B\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010C\u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010(R\u000e\u0010E\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010F\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010G\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010J\u001a\b\u0012\u0004\u0012\u00020\u001e0KX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010L\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010M\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010N\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010O\u001a\u00020\u001bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bP\u0010(\"\u0004\bQ\u0010RR\u000e\u0010S\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010T\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010U\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010V\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010W\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010X\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bY\u00101R\u000e\u0010Z\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010[\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\\\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010]\u001a\u0004\u0018\u00010^8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b_\u0010`R\u001e\u0010a\u001a\u0004\u0018\u00010\"X\u0086\u000e\u00a2\u0006\u0010\n\u0002\bf\u001a\u0004\bb\u0010c\"\u0004\bd\u0010eR\u0011\u0010g\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\bh\u0010\fR\u000e\u0010i\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0088\u0001"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/KillAura2;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aacValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "attackDelay", "", "attackTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "autoBlockValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getAutoBlockValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "blockRate", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "blockingStatus", "", "getBlockingStatus", "()Z", "setBlockingStatus", "(Z)V", "canBlock", "getCanBlock", "cancelRun", "getCancelRun", "circleAccuracy", "circleThicknessValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "circleValue", "clicks", "", "containerOpen", "cooldownValue", "currentTarget", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "failRateValue", "fakeSharpValue", "fakeSwingValue", "fovValue", "getFovValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "hitable", "hiteffect", "hurtTimeValue", "getHurtTimeValue", "()Lnet/ccbluex/liquidbounce/value/IntegerValue;", "interactAutoBlockValue", "keepSprintValue", "getKeepSprintValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "lastTarget", "lightingSoundValue", "limitedMultiTargetsValue", "livingRaycastValue", "markValue", "maxCPS", "getMaxCPS", "maxPredictSize", "maxRange", "", "getMaxRange", "()F", "maxTurnSpeed", "getMaxTurnSpeed", "minCPS", "getMinCPS", "minPredictSize", "minTurnSpeed", "getMinTurnSpeed", "noHitableCheckValue", "noInventoryAttackValue", "noInventoryDelayValue", "outborderValue", "predictValue", "prevTargetEntities", "", "priorityValue", "randomCenterValue", "rangeSprintReducementValue", "rangeValue", "getRangeValue", "setRangeValue", "(Lnet/ccbluex/liquidbounce/value/FloatValue;)V", "raycastIgnoredValue", "raycastValue", "rotationStrafeValue", "rotations", "silentRotationValue", "stopSprintValue", "getStopSprintValue", "swingValue", "switchDelayValue", "switchTimer", "tag", "", "getTag", "()Ljava/lang/String;", "target", "getTarget", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "setTarget", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;)V", "target$1", "targetModeValue", "getTargetModeValue", "throughWallsRangeValue", "attackEntity", "", "entity", "getRange", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "isAlive", "isEnemy", "onDisable", "onEnable", "onEntityMove", "event", "Lnet/ccbluex/liquidbounce/event/EntityMovementEvent;", "onMotion", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onStrafe", "Lnet/ccbluex/liquidbounce/event/StrafeEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "runAttack", "startBlocking", "interactEntity", "interact", "stopBlocking", "update", "updateHitable", "updateRotations", "updateTarget", "Companion", "LiKingSense"})
public final class KillAura2
extends Module {
    @NotNull
    public final IntegerValue maxCPS;
    @NotNull
    public final IntegerValue minCPS;
    @NotNull
    public final IntegerValue hurtTimeValue;
    public final FloatValue cooldownValue;
    public final IntegerValue switchDelayValue;
    @NotNull
    public FloatValue rangeValue;
    public final FloatValue throughWallsRangeValue;
    public final FloatValue rangeSprintReducementValue;
    public final ListValue priorityValue;
    @NotNull
    public final ListValue targetModeValue;
    public final BoolValue swingValue;
    @NotNull
    public final BoolValue keepSprintValue;
    @NotNull
    public final BoolValue stopSprintValue;
    public final BoolValue noHitableCheckValue;
    @NotNull
    public final ListValue autoBlockValue;
    public final BoolValue interactAutoBlockValue;
    public final IntegerValue blockRate;
    public final BoolValue raycastValue;
    public final BoolValue raycastIgnoredValue;
    public final BoolValue livingRaycastValue;
    public final BoolValue aacValue;
    @NotNull
    public final FloatValue maxTurnSpeed;
    @NotNull
    public final FloatValue minTurnSpeed;
    public final ListValue rotations;
    public final BoolValue silentRotationValue;
    public final ListValue rotationStrafeValue;
    public final BoolValue randomCenterValue;
    public final BoolValue outborderValue;
    @NotNull
    public final FloatValue fovValue;
    public final BoolValue predictValue;
    public final FloatValue maxPredictSize;
    public final FloatValue minPredictSize;
    public final FloatValue failRateValue;
    public final BoolValue fakeSwingValue;
    public final BoolValue noInventoryAttackValue;
    public final IntegerValue noInventoryDelayValue;
    public final IntegerValue limitedMultiTargetsValue;
    public final ListValue markValue;
    public final ListValue hiteffect;
    public final BoolValue lightingSoundValue;
    public final BoolValue fakeSharpValue;
    public final BoolValue circleValue;
    public final FloatValue circleThicknessValue;
    public final IntegerValue circleAccuracy;
    @Nullable
    public IEntityLivingBase target$1;
    public IEntityLivingBase currentTarget;
    public boolean hitable;
    public final List<Integer> prevTargetEntities;
    public IEntityLivingBase lastTarget;
    public final MSTimer attackTimer;
    public final MSTimer switchTimer;
    public long attackDelay;
    public int clicks;
    public long containerOpen;
    public boolean blockingStatus;
    @NotNull
    public static Object target;
    public static final Companion Companion;

    @NotNull
    public final IntegerValue getMaxCPS() {
        return this.maxCPS;
    }

    @NotNull
    public final IntegerValue getMinCPS() {
        return this.minCPS;
    }

    @NotNull
    public final IntegerValue getHurtTimeValue() {
        return this.hurtTimeValue;
    }

    @NotNull
    public final FloatValue getRangeValue() {
        return this.rangeValue;
    }

    public final void setRangeValue(@NotNull FloatValue floatValue) {
        Intrinsics.checkParameterIsNotNull((Object)floatValue, (String)"<set-?>");
        this.rangeValue = floatValue;
    }

    @NotNull
    public final ListValue getTargetModeValue() {
        return this.targetModeValue;
    }

    @NotNull
    public final BoolValue getKeepSprintValue() {
        return this.keepSprintValue;
    }

    @NotNull
    public final BoolValue getStopSprintValue() {
        return this.stopSprintValue;
    }

    @NotNull
    public final ListValue getAutoBlockValue() {
        return this.autoBlockValue;
    }

    @NotNull
    public final FloatValue getMaxTurnSpeed() {
        return this.maxTurnSpeed;
    }

    @NotNull
    public final FloatValue getMinTurnSpeed() {
        return this.minTurnSpeed;
    }

    @NotNull
    public final FloatValue getFovValue() {
        return this.fovValue;
    }

    @Nullable
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
        this.hitable = 0;
        this.prevTargetEntities.clear();
        this.attackTimer.reset();
        this.clicks = 0;
        this.lastTarget = null;
        this.stopBlocking();
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl80 : INVOKEINTERFACE - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onStrafe(@NotNull StrafeEvent event) {
        String string;
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (StringsKt.equals((String)((String)this.rotationStrafeValue.get()), (String)"Off", (boolean)true)) {
            return;
        }
        this.update();
        if (this.currentTarget == null || RotationUtils.targetRotation == null) return;
        String string2 = string = (String)this.rotationStrafeValue.get();
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
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
                    if ((f = (float)Math.sqrt(f)) < 1.0f) {
                        f = 1.0f;
                    }
                    f = friction / f;
                    strafe *= f;
                    forward *= f;
                    float f2 = (float)((double)yaw * Math.PI / (double)180.0f);
                    float yawSin = (float)Math.sin(f2);
                    float f3 = (float)((double)yaw * Math.PI / (double)180.0f);
                    float yawCos = (float)Math.cos(f3);
                    IEntityPlayerSP iEntityPlayerSP = player = MinecraftInstance.mc.getThePlayer();
                    iEntityPlayerSP.setMotionX(iEntityPlayerSP.getMotionX() + (double)(strafe * yawCos - forward * yawSin));
                    IEntityPlayerSP iEntityPlayerSP2 = player;
                    iEntityPlayerSP2.setMotionZ(iEntityPlayerSP2.getMotionZ() + (double)(forward * yawCos + strafe * yawSin));
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

    public final void update() {
        KillAura2 this_$iv = this;
        if ((MinecraftInstance.mc.getThePlayer().isSpectator() || !this_$iv.isAlive(MinecraftInstance.mc.getThePlayer()) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState() ? 1 : 0) != 0 || ((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()) || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            return;
        }
        this.updateTarget();
        if (this.target$1 == null) {
            this.stopBlocking();
            return;
        }
        this.currentTarget = this.target$1;
        if (!StringsKt.equals((String)((String)this.targetModeValue.get()), (String)"Switch", (boolean)true) && this.isEnemy(this.currentTarget)) {
            this.target$1 = this.currentTarget;
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (((Boolean)this.noHitableCheckValue.get()).booleanValue()) {
            this.hitable = 1;
        }
        KillAura2 this_$iv = this;
        if ((MinecraftInstance.mc.getThePlayer().isSpectator() || !this_$iv.isAlive(MinecraftInstance.mc.getThePlayer()) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState() ? 1 : 0) != 0) {
            this.target$1 = null;
            this.currentTarget = null;
            this.hitable = 0;
            this.stopBlocking();
            return;
        }
        if (((Boolean)this.noInventoryAttackValue.get()).booleanValue() && (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()) || System.currentTimeMillis() - this.containerOpen < ((Number)this.noInventoryDelayValue.get()).longValue())) {
            this.target$1 = null;
            this.currentTarget = null;
            this.hitable = 0;
            if (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen())) {
                this.containerOpen = System.currentTimeMillis();
            }
            return;
        }
        if (this.target$1 != null && this.currentTarget != null && MinecraftInstance.mc.getThePlayer().getCooledAttackStrength(0.0f) >= ((Number)this.cooldownValue.get()).floatValue()) {
            while (this.clicks > 0) {
                this.runAttack();
                int n = this.clicks;
                this.clicks = n + -1;
            }
        }
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl91 : INVOKESTATIC - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @EventTarget
    public final void onEntityMove(@NotNull EntityMovementEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IEntity movedEntity = event.getMovedEntity();
        if (this.target$1 == null || (Intrinsics.areEqual((Object)movedEntity, (Object)this.currentTarget) ^ 1) != 0) {
            return;
        }
        this.updateHitable();
    }

    /*
     * Unable to fully structure code
     */
    public final void runAttack() {
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
        v2 = openInventory = (Boolean)this.aacValue.get() != false && MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen()) != false ? 1 : 0;
        if (!(failRate > (float)0)) ** GOTO lbl-1000
        v3 = new Random();
        if ((float)v3.nextInt(100) <= failRate) {
            v4 = 1;
        } else lbl-1000:
        // 2 sources

        {
            v4 = failHit = 0;
        }
        if (openInventory != 0) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketCloseWindow());
        }
        if (!this.hitable || failHit != 0) {
            if (swing && (((Boolean)this.fakeSwingValue.get()).booleanValue() || failHit != 0)) {
                thePlayer.swingItem();
            }
        } else {
            if (!multi) {
                this.attackEntity(this.currentTarget);
            } else {
                for (IEntity entity : theWorld.getLoadedEntityList()) {
                    distance = PlayerExtensionKt.getDistanceToEntityBox(thePlayer, entity);
                    if (!MinecraftInstance.classProvider.isEntityLivingBase(entity) || !this.isEnemy(entity) || !(distance <= (double)this.getRange(entity))) continue;
                    this.attackEntity(entity.asEntityLivingBase());
                    if (((Number)this.limitedMultiTargetsValue.get()).intValue() == 0 || ((Number)this.limitedMultiTargetsValue.get()).intValue() > ++targets) continue;
                    break;
                }
            }
            if (this.switchTimer.hasTimePassed(((Number)this.switchDelayValue.get()).intValue()) || (Intrinsics.areEqual((Object)((String)this.targetModeValue.get()), (Object)"Switch") ^ 1) != 0) {
                this.prevTargetEntities.add((Boolean)this.aacValue.get() != false ? this.target$1.getEntityId() : this.currentTarget.getEntityId());
                this.switchTimer.reset();
            }
            this.prevTargetEntities.add((Boolean)this.aacValue.get() != false ? this.target$1.getEntityId() : this.currentTarget.getEntityId());
            if (Intrinsics.areEqual((Object)this.target$1, (Object)this.currentTarget)) {
                this.target$1 = null;
            }
        }
        if (openInventory != 0) {
            var13_12 = MinecraftInstance.mc.getNetHandler();
            var14_13 = WrapperImpl.INSTANCE.getClassProvider().createCPacketEntityAction(LiquidBounce.INSTANCE.getWrapper().getMinecraft().getThePlayer(), ICPacketEntityAction.WAction.OPEN_INVENTORY);
            var13_12.addToSendQueue(var14_13);
        }
    }

    public final void updateTarget() {
        String string;
        this.target$1 = null;
        int hurtTime = ((Number)this.hurtTimeValue.get()).intValue();
        float fov = ((Number)this.fovValue.get()).floatValue();
        boolean switchMode = StringsKt.equals((String)((String)this.targetModeValue.get()), (String)"Switch", (boolean)true);
        List targets = new ArrayList();
        IWorldClient theWorld = MinecraftInstance.mc.getTheWorld();
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        for (IEntity collection : theWorld.getLoadedEntityList()) {
            if (!MinecraftInstance.classProvider.isEntityLivingBase(collection) || !this.isEnemy(collection) || switchMode && this.prevTargetEntities.contains(collection.getEntityId())) continue;
            double distance = PlayerExtensionKt.getDistanceToEntityBox(thePlayer, collection);
            double entityFov = RotationUtils.getRotationDifference(collection);
            if (!(distance <= (double)this.getMaxRange()) || fov != 180.0f && !(entityFov <= (double)fov) || collection.asEntityLivingBase().getHurtTime() > hurtTime) continue;
            targets.add(collection.asEntityLivingBase());
        }
        String string2 = string = (String)this.priorityValue.get();
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "distance": {
                List $this$sortBy$iv = targets;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                Comparator comparator = new Comparator<T>(thePlayer){
                    public final /* synthetic */ IEntityPlayerSP $thePlayer$inlined;
                    {
                        this.$thePlayer$inlined = iEntityPlayerSP;
                    }

                    public final int compare(T a, T b) {
                        IEntityLivingBase it = (IEntityLivingBase)a;
                        Comparable comparable = Double.valueOf(PlayerExtensionKt.getDistanceToEntityBox(this.$thePlayer$inlined, it));
                        it = (IEntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        Double d = PlayerExtensionKt.getDistanceToEntityBox(this.$thePlayer$inlined, it);
                        return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)d);
                    }
                };
                CollectionsKt.sortWith((List)list, (Comparator)comparator);
                break;
            }
            case "health": {
                List $this$sortBy$iv = targets;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        IEntityLivingBase it = (IEntityLivingBase)a;
                        Comparable comparable = Float.valueOf(it.getHealth());
                        it = (IEntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        Float f = Float.valueOf(it.getHealth());
                        return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)f);
                    }
                };
                CollectionsKt.sortWith((List)list, (Comparator)comparator);
                break;
            }
            case "direction": {
                List $this$sortBy$iv = targets;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        IEntityLivingBase it = (IEntityLivingBase)a;
                        Comparable comparable = Double.valueOf(RotationUtils.getRotationDifference(it));
                        it = (IEntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        Double d = RotationUtils.getRotationDifference(it);
                        return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)d);
                    }
                };
                CollectionsKt.sortWith((List)list, (Comparator)comparator);
                break;
            }
            case "livingtime": {
                List $this$sortBy$iv = targets;
                if ($this$sortBy$iv.size() <= 1) break;
                List list = $this$sortBy$iv;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        IEntityLivingBase it = (IEntityLivingBase)a;
                        Comparable comparable = Integer.valueOf(-it.getTicksExisted());
                        it = (IEntityLivingBase)b;
                        Comparable comparable2 = comparable;
                        Integer n = -it.getTicksExisted();
                        return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)n);
                    }
                };
                CollectionsKt.sortWith((List)list, (Comparator)comparator);
                break;
            }
        }
        for (IEntityLivingBase iEntityLivingBase : targets) {
            if (!this.updateRotations(iEntityLivingBase)) continue;
            this.target$1 = iEntityLivingBase;
            return;
        }
        Collection collection = this.prevTargetEntities;
        if ((!collection.isEmpty() ? 1 : 0) != 0) {
            this.prevTargetEntities.clear();
            this.updateTarget();
        }
    }

    public final boolean isEnemy(IEntity entity) {
        if (MinecraftInstance.classProvider.isEntityLivingBase(entity) && entity != null && (EntityUtils.targetDead || this.isAlive(entity.asEntityLivingBase())) && (Intrinsics.areEqual((Object)entity, (Object)MinecraftInstance.mc.getThePlayer()) ^ 1) != 0) {
            if (!EntityUtils.targetInvisible && entity.isInvisible()) {
                return false;
            }
            if (EntityUtils.targetPlayer && MinecraftInstance.classProvider.isEntityPlayer(entity)) {
                IEntityPlayer player = entity.asEntityPlayer();
                if (player.isSpectator()) {
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
                return (!teams.getState() || !teams.isInYourTeam(entity.asEntityLivingBase()) ? 1 : 0) != 0;
            }
            return EntityUtils.targetMobs && PlayerExtensionKt.isMob(entity) || EntityUtils.targetAnimals && PlayerExtensionKt.isAnimal(entity);
        }
        return false;
    }

    /*
     * Exception decompiling
     */
    public final void attackEntity(IEntityLivingBase entity) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * java.lang.IllegalStateException: Invisible function parameters on a non-constructor (or reads of uninitialised local variables).
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.assignSSAIdentifiers(Op02WithProcessedDataAndRefs.java:1631)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.discoverStorageLiveness(Op02WithProcessedDataAndRefs.java:1871)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:461)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public final boolean updateRotations(IEntity entity) {
        Object rotation;
        Object limitedRotation3;
        IAxisAlignedBB boundingBox = entity.getEntityBoundingBox();
        if (StringsKt.equals((String)((String)this.rotations.get()), (String)"Vanilla", (boolean)true)) {
            if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
                return true;
            }
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.offset((entity.getPosX() - entity.getPrevPosX()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosY() - entity.getPrevPosY()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosZ() - entity.getPrevPosZ()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            VecRotation vecRotation = RotationUtils.searchCenter(boundingBox, (Boolean)this.outborderValue.get() != false && !this.attackTimer.hasTimePassed(this.attackDelay / (long)2), (Boolean)this.randomCenterValue.get(), (Boolean)this.predictValue.get(), PlayerExtensionKt.getDistanceToEntityBox(MinecraftInstance.mc.getThePlayer(), entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            VecRotation vecRotation2 = vecRotation;
            WVec3 wVec3 = vecRotation2.component1();
            Rotation rotation2 = vecRotation2.component2();
            Rotation rotation3 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation2, (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            Intrinsics.checkExpressionValueIsNotNull((Object)rotation3, (String)"RotationUtils.limitAngle\u2026).toFloat()\n            )");
            Rotation limitedRotation2 = rotation3;
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation(limitedRotation2, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                limitedRotation2.toPlayer(MinecraftInstance.mc.getThePlayer());
            }
            return true;
        }
        if (StringsKt.equals((String)((String)this.rotations.get()), (String)"Other", (boolean)true)) {
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.offset((entity.getPosX() - entity.getPrevPosX()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosY() - entity.getPrevPosY()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosZ() - entity.getPrevPosZ()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            VecRotation vecRotation = RotationUtils.lockView(boundingBox, (Boolean)this.outborderValue.get() != false && !this.attackTimer.hasTimePassed(this.attackDelay / (long)2), (Boolean)this.randomCenterValue.get(), (Boolean)this.predictValue.get(), PlayerExtensionKt.getDistanceToEntityBox(MinecraftInstance.mc.getThePlayer(), entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            VecRotation rotation2 = vecRotation;
            Rotation rotation4 = rotation2.component2();
            Rotation rotation5 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation4, (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            Intrinsics.checkExpressionValueIsNotNull((Object)rotation5, (String)"RotationUtils.limitAngle\u2026).toFloat()\n            )");
            Rotation limitedRotation3 = rotation5;
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation(limitedRotation3, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                limitedRotation3.toPlayer(MinecraftInstance.mc.getThePlayer());
            }
            return true;
        }
        if (StringsKt.equals((String)((String)this.rotations.get()), (String)"MoreTest", (boolean)true)) {
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.offset((entity.getPosX() - entity.getPrevPosX()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosY() - entity.getPrevPosY()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosZ() - entity.getPrevPosZ()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            VecRotation vecRotation = RotationUtils.lockView(boundingBox, (Boolean)this.outborderValue.get() != false && !this.attackTimer.hasTimePassed(this.attackDelay / (long)2), (Boolean)this.randomCenterValue.get(), (Boolean)this.predictValue.get(), PlayerExtensionKt.getDistanceToEntityBox(MinecraftInstance.mc.getThePlayer(), entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            limitedRotation3 = vecRotation;
            rotation = ((VecRotation)limitedRotation3).component2();
            Rotation rotation6 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, (Rotation)rotation, (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            Intrinsics.checkExpressionValueIsNotNull((Object)rotation6, (String)"RotationUtils.limitAngle\u2026rnSpeed.get()).toFloat())");
            limitedRotation3 = rotation6;
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation((Rotation)limitedRotation3, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                ((Rotation)limitedRotation3).toPlayer(MinecraftInstance.mc.getThePlayer());
                return true;
            }
        }
        if (StringsKt.equals((String)((String)this.rotations.get()), (String)"More", (boolean)true)) {
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.offset((entity.getPosX() - entity.getPrevPosX()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosY() - entity.getPrevPosY()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosZ() - entity.getPrevPosZ()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            VecRotation vecRotation = RotationUtils.lockView(boundingBox, (Boolean)this.outborderValue.get() != false && !this.attackTimer.hasTimePassed(this.attackDelay / (long)2), (Boolean)this.randomCenterValue.get(), (Boolean)this.predictValue.get(), PlayerExtensionKt.getDistanceToEntityBox(MinecraftInstance.mc.getThePlayer(), entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            limitedRotation3 = vecRotation;
            rotation = ((VecRotation)limitedRotation3).component2();
            Rotation rotation7 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, (Rotation)rotation, (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            Intrinsics.checkExpressionValueIsNotNull((Object)rotation7, (String)"RotationUtils.limitAngle\u2026).toFloat()\n            )");
            limitedRotation3 = rotation7;
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation((Rotation)limitedRotation3, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                ((Rotation)limitedRotation3).toPlayer(MinecraftInstance.mc.getThePlayer());
                return true;
            }
        }
        if (StringsKt.equals((String)((String)this.rotations.get()), (String)"Center", (boolean)true)) {
            if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
                return true;
            }
            if (((Boolean)this.predictValue.get()).booleanValue()) {
                boundingBox = boundingBox.offset((entity.getPosX() - entity.getPrevPosX()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosY() - entity.getPrevPosY()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()), (entity.getPosZ() - entity.getPrevPosZ()) * (double)RandomUtils.INSTANCE.nextFloat(((Number)this.minPredictSize.get()).floatValue(), ((Number)this.maxPredictSize.get()).floatValue()));
            }
            VecRotation vecRotation = RotationUtils.searchCenter(boundingBox, (Boolean)this.outborderValue.get() != false && !this.attackTimer.hasTimePassed(this.attackDelay / (long)2), (Boolean)this.randomCenterValue.get(), (Boolean)this.predictValue.get(), PlayerExtensionKt.getDistanceToEntityBox(MinecraftInstance.mc.getThePlayer(), entity) < ((Number)this.throughWallsRangeValue.get()).doubleValue(), this.getMaxRange());
            if (vecRotation == null) {
                return false;
            }
            Object limitedRotation2 = vecRotation;
            rotation = ((VecRotation)limitedRotation2).component1();
            Rotation rotation8 = ((VecRotation)limitedRotation2).component2();
            Rotation rotation9 = RotationUtils.limitAngleChange(RotationUtils.serverRotation, RotationUtils.toRotation(RotationUtils.getCenter(entity.getEntityBoundingBox()), false), (float)(Math.random() * (double)(((Number)this.maxTurnSpeed.get()).floatValue() - ((Number)this.minTurnSpeed.get()).floatValue()) + ((Number)this.minTurnSpeed.get()).doubleValue()));
            Intrinsics.checkExpressionValueIsNotNull((Object)rotation9, (String)"RotationUtils.limitAngle\u2026).toFloat()\n            )");
            limitedRotation2 = rotation9;
            if (((Boolean)this.silentRotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation((Rotation)limitedRotation2, (Boolean)this.aacValue.get() != false ? 15 : 0);
            } else {
                ((Rotation)limitedRotation2).toPlayer(MinecraftInstance.mc.getThePlayer());
            }
            return true;
        }
        return true;
    }

    public final void updateHitable() {
        if (((Number)this.maxTurnSpeed.get()).floatValue() <= 0.0f) {
            this.hitable = 1;
            return;
        }
        double d = this.getMaxRange();
        double d2 = PlayerExtensionKt.getDistanceToEntityBox(MinecraftInstance.mc.getThePlayer(), this.target$1);
        double reach = Math.min(d, d2) + (double)1;
        if (((Boolean)this.raycastValue.get()).booleanValue()) {
            IEntity raycastedEntity2 = RaycastUtils.raycastEntity(reach, new RaycastUtils.EntityFilter(this){
                public final /* synthetic */ KillAura2 this$0;

                public boolean canRaycast(@Nullable IEntity entity) {
                    Collection<IEntity> collection;
                    return (((Boolean)KillAura2.access$getLivingRaycastValue$p(this.this$0).get() == false || MinecraftInstance.classProvider.isEntityLivingBase(entity) && !MinecraftInstance.classProvider.isEntityArmorStand(entity)) && (KillAura2.access$isEnemy(this.this$0, entity) || (Boolean)KillAura2.access$getRaycastIgnoredValue$p(this.this$0).get() != false || (Boolean)KillAura2.access$getAacValue$p(this.this$0).get() != false && (!(collection = MinecraftInstance.mc.getTheWorld().getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox())).isEmpty() ? 1 : 0) != 0) ? 1 : 0) != 0;
                }
                {
                    this.this$0 = $outer;
                }
            });
            if (((Boolean)this.raycastValue.get()).booleanValue() && raycastedEntity2 != null && MinecraftInstance.classProvider.isEntityLivingBase(raycastedEntity2) && (LiquidBounce.INSTANCE.getModuleManager().get(NoFriends.class).getState() || !MinecraftInstance.classProvider.isEntityPlayer(raycastedEntity2) || !PlayerExtensionKt.isClientFriend(raycastedEntity2.asEntityPlayer()))) {
                this.currentTarget = raycastedEntity2.asEntityLivingBase();
            }
            this.hitable = ((Number)this.maxTurnSpeed.get()).floatValue() > 0.0f ? (int)(Intrinsics.areEqual((Object)this.currentTarget, (Object)raycastedEntity2) ? 1 : 0) : 1;
        } else {
            this.hitable = RotationUtils.isFaced(this.currentTarget, reach);
        }
    }

    /*
     * WARNING - void declaration
     */
    public final void startBlocking(IEntity interactEntity, boolean interact) {
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
                rotation = new Rotation(MinecraftInstance.mc.getThePlayer().getRotationYaw(), MinecraftInstance.mc.getThePlayer().getRotationPitch());
            }
            Rotation rotation2 = rotation;
            float f = rotation2.component1();
            float pitch = rotation2.component2();
            void var10_12 = -yaw * ((float)Math.PI / 180) - (float)Math.PI;
            float yawCos = (float)Math.cos((double)var10_12);
            void var11_13 = -yaw * ((float)Math.PI / 180) - (float)Math.PI;
            float yawSin = (float)Math.sin((double)var11_13);
            float f2 = -pitch * ((float)Math.PI / 180);
            float pitchCos = -((float)Math.cos(f2));
            float f3 = -pitch * ((float)Math.PI / 180);
            float pitchSin = (float)Math.sin(f3);
            double d = this.getMaxRange();
            double d2 = PlayerExtensionKt.getDistanceToEntityBox(MinecraftInstance.mc.getThePlayer(), interactEntity);
            double range = Math.min(d, d2) + (double)1;
            Object object = positionEye;
            d2 = (double)(yawSin * pitchCos) * range;
            double d3 = (double)pitchSin * range;
            double z$iv = (double)(yawCos * pitchCos) * range;
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
            positionEye = MinecraftInstance.mc.getThePlayer().getInventory().getCurrentItemInHand();
            WEnumHand expandSize = WEnumHand.OFF_HAND;
            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            IPacket iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem((WEnumHand)hand$iv);
            iINetHandlerPlayClient.addToSendQueue(iPacket);
        } else {
            IItemStack itemStack$iv = MinecraftInstance.mc.getThePlayer().getInventory().getCurrentItemInHand();
            WEnumHand hand$iv = WEnumHand.MAIN_HAND;
            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            IPacket iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(hand$iv);
            iINetHandlerPlayClient.addToSendQueue(iPacket);
            itemStack$iv = MinecraftInstance.mc.getThePlayer().getInventory().getCurrentItemInHand();
            hand$iv = WEnumHand.OFF_HAND;
            iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            iPacket = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(hand$iv);
            iINetHandlerPlayClient.addToSendQueue(iPacket);
        }
        this.blockingStatus = 1;
    }

    public final void stopBlocking() {
        if (this.blockingStatus) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
            this.blockingStatus = 0;
        }
    }

    public final boolean getCancelRun() {
        return (MinecraftInstance.mc.getThePlayer().isSpectator() || !this.isAlive(MinecraftInstance.mc.getThePlayer()) || LiquidBounce.INSTANCE.getModuleManager().get(Blink.class).getState() ? 1 : 0) != 0;
    }

    public final boolean isAlive(IEntityLivingBase entity) {
        return entity.isEntityAlive() && entity.getHealth() > (float)0 || (Boolean)this.aacValue.get() != false && entity.getHurtTime() > 5;
    }

    public final boolean getCanBlock() {
        return (MinecraftInstance.mc.getThePlayer().getHeldItem() != null && MinecraftInstance.classProvider.isItemSword(MinecraftInstance.mc.getThePlayer().getHeldItem().getItem()) ? 1 : 0) != 0;
    }

    public final float getMaxRange() {
        float f = ((Number)this.rangeValue.get()).floatValue();
        float f2 = ((Number)this.throughWallsRangeValue.get()).floatValue();
        return Math.max(f, f2);
    }

    public final float getRange(IEntity entity) {
        return (PlayerExtensionKt.getDistanceToEntityBox(MinecraftInstance.mc.getThePlayer(), entity) >= ((Number)this.throughWallsRangeValue.get()).doubleValue() ? ((Number)this.rangeValue.get()).floatValue() : ((Number)this.throughWallsRangeValue.get()).floatValue()) - (MinecraftInstance.mc.getThePlayer().getSprinting() ? ((Number)this.rangeSprintReducementValue.get()).floatValue() : 0.0f);
    }

    @Override
    @Nullable
    public String getTag() {
        return "Switch";
    }

    /*
     * Exception decompiling
     */
    public KillAura2() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl10 : CHECKCAST - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    static {
        Companion = new Companion(null);
    }

    public static final /* synthetic */ IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
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

    public static final /* synthetic */ long access$getAttackDelay$p(KillAura2 $this) {
        return $this.attackDelay;
    }

    public static final /* synthetic */ void access$setAttackDelay$p(KillAura2 $this, long l) {
        $this.attackDelay = l;
    }

    public static final /* synthetic */ FloatValue access$getMinPredictSize$p(KillAura2 $this) {
        return $this.minPredictSize;
    }

    public static final /* synthetic */ FloatValue access$getMaxPredictSize$p(KillAura2 $this) {
        return $this.maxPredictSize;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0001X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/KillAura2$Companion;", "", "()V", "target", "getTarget", "()Ljava/lang/Object;", "setTarget", "(Ljava/lang/Object;)V", "LiKingSense"})
    public static final class Companion {
        @NotNull
        public final Object getTarget() {
            Object object = target;
            if (object == null) {
                Intrinsics.throwUninitializedPropertyAccessException((String)"target");
            }
            return object;
        }

        public final void setTarget(@NotNull Object object) {
            Intrinsics.checkParameterIsNotNull((Object)object, (String)"<set-?>");
            target = object;
        }

        public Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

