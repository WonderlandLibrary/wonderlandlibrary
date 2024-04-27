/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  qiriyou.verV3Z.Loader
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.event.EntityMovementEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import qiriyou.verV3Z.Loader;

@ModuleInfo(name="KillAura", description="Automatically attacks targets around you.", category=ModuleCategory.COMBAT, keyBind=19)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0094\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0010\u0007\n\u0002\b\u000f\n\u0002\u0010!\n\u0002\b\u0012\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0007\u0018\u0000 \u0087\u00012\u00020\u0001:\u0002\u0087\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010j\u001a\u00020k2\u0006\u0010l\u001a\u00020\"H\u0002J\u0010\u0010m\u001a\u00020;2\u0006\u0010l\u001a\u00020nH\u0002J\u0010\u0010o\u001a\u00020\u00102\u0006\u0010l\u001a\u00020\"H\u0002J\u0012\u0010p\u001a\u00020\u00102\b\u0010l\u001a\u0004\u0018\u00010nH\u0002J\b\u0010q\u001a\u00020kH\u0016J\b\u0010r\u001a\u00020kH\u0016J\u0010\u0010s\u001a\u00020k2\u0006\u0010t\u001a\u00020uH\u0007J\u0010\u0010v\u001a\u00020k2\u0006\u0010t\u001a\u00020wH\u0007J\u0010\u0010x\u001a\u00020k2\u0006\u0010t\u001a\u00020yH\u0007J\u0010\u0010z\u001a\u00020k2\u0006\u0010t\u001a\u00020{H\u0007J\u0010\u0010|\u001a\u00020k2\u0006\u0010t\u001a\u00020}H\u0007J\b\u0010~\u001a\u00020kH\u0002J\u001a\u0010\u007f\u001a\u00020k2\u0007\u0010\u0080\u0001\u001a\u00020n2\u0007\u0010\u0081\u0001\u001a\u00020\u0010H\u0002J\t\u0010\u0082\u0001\u001a\u00020kH\u0002J\u0007\u0010\u0083\u0001\u001a\u00020kJ\t\u0010\u0084\u0001\u001a\u00020kH\u0002J\u0011\u0010\u0085\u0001\u001a\u00020\u00102\u0006\u0010l\u001a\u00020nH\u0002J\t\u0010\u0086\u0001\u001a\u00020kH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0015\u0010\u0015\u001a\u00020\u00108\u00c2\u0002X\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0012R\u0015\u0010\u0017\u001a\u00020\u00108\u00c2\u0002X\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u0012R\u000e\u0010\u0019\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010&\u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b'\u0010(R\u000e\u0010)\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010+\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010-R\u000e\u0010.\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010/\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u00101R\u0010\u00102\u001a\u0004\u0018\u00010\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u00107\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010-R\u000e\u00109\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010:\u001a\u00020;8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b<\u0010=R\u0011\u0010>\u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010(R\u0011\u0010@\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u0010-R\u000e\u0010B\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010C\u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010(R\u000e\u0010E\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010F\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010G\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010J\u001a\b\u0012\u0004\u0012\u00020\u001e0KX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010L\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010M\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010N\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010O\u001a\u00020\u001bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bP\u0010(\"\u0004\bQ\u0010RR\u000e\u0010S\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010T\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010U\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010V\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010W\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010X\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bY\u00101R\u000e\u0010Z\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010[\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\\\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010]\u001a\u0004\u0018\u00010^8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b_\u0010`R\u001e\u0010a\u001a\u0004\u0018\u00010\"X\u0086\u000e\u00a2\u0006\u0010\n\u0002\bf\u001a\u0004\bb\u0010c\"\u0004\bd\u0010eR\u0011\u0010g\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\bh\u0010\fR\u000e\u0010i\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0088\u0001"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/KillAura;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aacValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "attackDelay", "", "attackTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "autoBlockValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getAutoBlockValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "blockRate", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "blockingStatus", "", "getBlockingStatus", "()Z", "setBlockingStatus", "(Z)V", "canBlock", "getCanBlock", "cancelRun", "getCancelRun", "circleAccuracy", "circleThicknessValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "circleValue", "clicks", "", "containerOpen", "cooldownValue", "currentTarget", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "failRateValue", "fakeSharpValue", "fakeSwingValue", "fovValue", "getFovValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "hitable", "hiteffect", "hurtTimeValue", "getHurtTimeValue", "()Lnet/ccbluex/liquidbounce/value/IntegerValue;", "interactAutoBlockValue", "keepSprintValue", "getKeepSprintValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "lastTarget", "lightingSoundValue", "limitedMultiTargetsValue", "livingRaycastValue", "markValue", "maxCPS", "getMaxCPS", "maxPredictSize", "maxRange", "", "getMaxRange", "()F", "maxTurnSpeed", "getMaxTurnSpeed", "minCPS", "getMinCPS", "minPredictSize", "minTurnSpeed", "getMinTurnSpeed", "noHitableCheckValue", "noInventoryAttackValue", "noInventoryDelayValue", "outborderValue", "predictValue", "prevTargetEntities", "", "priorityValue", "randomCenterValue", "rangeSprintReducementValue", "rangeValue", "getRangeValue", "setRangeValue", "(Lnet/ccbluex/liquidbounce/value/FloatValue;)V", "raycastIgnoredValue", "raycastValue", "rotationStrafeValue", "rotations", "silentRotationValue", "stopSprintValue", "getStopSprintValue", "swingValue", "switchDelayValue", "switchTimer", "tag", "", "getTag", "()Ljava/lang/String;", "target", "getTarget", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "setTarget", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;)V", "target$1", "targetModeValue", "getTargetModeValue", "throughWallsRangeValue", "attackEntity", "", "entity", "getRange", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "isAlive", "isEnemy", "onDisable", "onEnable", "onEntityMove", "event", "Lnet/ccbluex/liquidbounce/event/EntityMovementEvent;", "onMotion", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onStrafe", "Lnet/ccbluex/liquidbounce/event/StrafeEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "runAttack", "startBlocking", "interactEntity", "interact", "stopBlocking", "update", "updateHitable", "updateRotations", "updateTarget", "Companion", "LiKingSense"})
public final class KillAura
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
    public final native IntegerValue getMaxCPS();

    @NotNull
    public final native IntegerValue getMinCPS();

    @NotNull
    public final native IntegerValue getHurtTimeValue();

    @NotNull
    public final native FloatValue getRangeValue();

    public final native void setRangeValue(@NotNull FloatValue var1);

    @NotNull
    public final native ListValue getTargetModeValue();

    @NotNull
    public final native BoolValue getKeepSprintValue();

    @NotNull
    public final native BoolValue getStopSprintValue();

    @NotNull
    public final native ListValue getAutoBlockValue();

    @NotNull
    public final native FloatValue getMaxTurnSpeed();

    @NotNull
    public final native FloatValue getMinTurnSpeed();

    @NotNull
    public final native FloatValue getFovValue();

    @Nullable
    public final native IEntityLivingBase getTarget();

    public final native void setTarget(@Nullable IEntityLivingBase var1);

    public final native boolean getBlockingStatus();

    public final native void setBlockingStatus(boolean var1);

    @Override
    public native void onEnable();

    @Override
    public native void onDisable();

    @EventTarget
    public final native void onMotion(@NotNull MotionEvent var1);

    @EventTarget
    public final native void onStrafe(@NotNull StrafeEvent var1);

    public final native void update();

    @EventTarget
    public final native void onUpdate(@NotNull UpdateEvent var1);

    @EventTarget
    public final native void onRender3D(@NotNull Render3DEvent var1);

    @EventTarget
    public final native void onEntityMove(@NotNull EntityMovementEvent var1);

    public final native void runAttack();

    public final native void updateTarget();

    public final native boolean isEnemy(IEntity var1);

    public final native void attackEntity(IEntityLivingBase var1);

    public final native boolean updateRotations(IEntity var1);

    public final native void updateHitable();

    public final native void startBlocking(IEntity var1, boolean var2);

    public final native void stopBlocking();

    public final native boolean getCancelRun();

    public final native boolean isAlive(IEntityLivingBase var1);

    public final native boolean getCanBlock();

    public final native float getMaxRange();

    public final native float getRange(IEntity var1);

    @Override
    @Nullable
    public native String getTag();

    /*
     * Exception decompiling
     */
    public KillAura() {
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
        Loader.registerNativesForClass((int)6, KillAura.class);
        KillAura.$qiriyouLoader();
        KillAura.$qiriyouClinit();
    }

    public static final native /* synthetic */ IExtractedFunctions access$getFunctions$p$s1046033730();

    public static final native /* synthetic */ boolean access$isEnemy(KillAura var0, IEntity var1);

    public static final native /* synthetic */ BoolValue access$getLivingRaycastValue$p(KillAura var0);

    public static final native /* synthetic */ BoolValue access$getRaycastIgnoredValue$p(KillAura var0);

    public static final native /* synthetic */ BoolValue access$getAacValue$p(KillAura var0);

    public static final native /* synthetic */ boolean access$isAlive(KillAura var0, IEntityLivingBase var1);

    public static final native /* synthetic */ long access$getAttackDelay$p(KillAura var0);

    public static final native /* synthetic */ void access$setAttackDelay$p(KillAura var0, long var1);

    public static final native /* synthetic */ FloatValue access$getMinPredictSize$p(KillAura var0);

    public static final native /* synthetic */ FloatValue access$getMaxPredictSize$p(KillAura var0);

    public static final native /* synthetic */ Object access$getTarget$cp();

    public static final native /* synthetic */ void access$setTarget$cp(Object var0);

    public static native /* synthetic */ void $qiriyouLoader();

    public static native /* synthetic */ void $qiriyouClinit();

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0001X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/KillAura$Companion;", "", "()V", "target", "getTarget", "()Ljava/lang/Object;", "setTarget", "(Ljava/lang/Object;)V", "LiKingSense"})
    public static final class Companion {
        @NotNull
        public final Object getTarget() {
            Object object = KillAura.access$getTarget$cp();
            if (object == null) {
                Intrinsics.throwUninitializedPropertyAccessException((String)"target");
            }
            return object;
        }

        public final void setTarget(@NotNull Object object) {
            Intrinsics.checkParameterIsNotNull((Object)object, (String)"<set-?>");
            KillAura.access$setTarget$cp(object);
        }

        public Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

