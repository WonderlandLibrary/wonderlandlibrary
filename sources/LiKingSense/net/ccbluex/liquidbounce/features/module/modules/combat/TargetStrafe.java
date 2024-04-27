/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.entity.EntityLivingBase
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.entity.EntityLivingBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="TargetStrafe", category=ModuleCategory.MOVEMENT, description="null")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010#\u001a\u00020\u000b2\b\u0010$\u001a\u0004\u0018\u00010\u001dH\u0002J\b\u0010%\u001a\u00020\u000bH\u0002J\u000e\u0010&\u001a\u00020\u000b2\u0006\u0010'\u001a\u00020(J\u0018\u0010)\u001a\u00020\u000b2\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020+H\u0002J\u000e\u0010-\u001a\u00020\u000b2\u0006\u0010'\u001a\u00020.J\u0010\u0010/\u001a\u0002002\u0006\u0010'\u001a\u000201H\u0007J\u0010\u00102\u001a\u0002002\u0006\u0010'\u001a\u000203H\u0007J\u0006\u00104\u001a\u00020\u000bR\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\r\"\u0004\b\u0013\u0010\u000fR\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u000e\u0010\"\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00065"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/TargetStrafe;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "callBackYaw", "", "getCallBackYaw", "()D", "setCallBackYaw", "(D)V", "direction", "doStrafe", "", "getDoStrafe", "()Z", "setDoStrafe", "(Z)V", "holdSpaceValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "isEnabled", "setEnabled", "lineWidthValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "ongroundValue", "onlySpeedValue", "radiusModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "radiusValue", "renderModeValue", "targetEntity", "Lnet/minecraft/entity/EntityLivingBase;", "getTargetEntity", "()Lnet/minecraft/entity/EntityLivingBase;", "setTargetEntity", "(Lnet/minecraft/entity/EntityLivingBase;)V", "thirdPersonViewValue", "canStrafe", "target", "checkVoid", "doMove", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "isVoid", "xPos", "", "zPos", "modifyStrafe", "Lnet/ccbluex/liquidbounce/event/StrafeEvent;", "onRender3D", "", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "toggleStrafe", "LiKingSense"})
public final class TargetStrafe
extends Module {
    public final BoolValue thirdPersonViewValue = new BoolValue("ThirdPersonView", false);
    public final ListValue renderModeValue = new ListValue("RenderMode", new String[]{"Circle", "Polygon", "None"}, "Polygon");
    public final FloatValue lineWidthValue = new FloatValue("LineWidth", 1.0f, 1.0f, 10.0f);
    public final ListValue radiusModeValue = new ListValue("RadiusMode", new String[]{"Normal", "Strict"}, "Normal");
    public final FloatValue radiusValue = new FloatValue("Radius", 0.5f, 0.1f, 5.0f);
    public final BoolValue ongroundValue = new BoolValue("OnlyOnGround", false);
    public final BoolValue holdSpaceValue = new BoolValue("HoldSpace", false);
    public final BoolValue onlySpeedValue = new BoolValue("OnlySpeed", true);
    public double direction = -1.0;
    @Nullable
    public EntityLivingBase targetEntity;
    public boolean isEnabled;
    public boolean doStrafe;
    public double callBackYaw;

    @Nullable
    public final EntityLivingBase getTargetEntity() {
        return this.targetEntity;
    }

    public final void setTargetEntity(@Nullable EntityLivingBase entityLivingBase) {
        this.targetEntity = entityLivingBase;
    }

    public final boolean isEnabled() {
        return this.isEnabled;
    }

    public final void setEnabled(boolean bl) {
        this.isEnabled = bl;
    }

    public final boolean getDoStrafe() {
        return this.doStrafe;
    }

    public final void setDoStrafe(boolean bl) {
        this.doStrafe = bl;
    }

    public final double getCallBackYaw() {
        return this.callBackYaw;
    }

    public final void setCallBackYaw(double d) {
        this.callBackYaw = d;
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl317 : ILOAD - null : trying to set 4 previously set to 2
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
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

    public final boolean canStrafe(EntityLivingBase target) {
        return (!(target == null || (Boolean)this.holdSpaceValue.get() != false && !MinecraftInstance.mc.getThePlayer().getMovementInput().getJump() || (Boolean)this.onlySpeedValue.get() != false && !LiquidBounce.INSTANCE.getModuleManager().get(Speed.class).getState()) ? 1 : 0) != 0;
    }

    public final boolean modifyStrafe(@NotNull StrafeEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (!this.isEnabled || event.isCancelled()) {
            return false;
        }
        MovementUtils.strafe$default(0.0f, 1, null);
        return true;
    }

    public final boolean toggleStrafe() {
        return (!(this.targetEntity == null || (Boolean)this.holdSpaceValue.get() != false && !MinecraftInstance.mc.getThePlayer().getMovementInput().getJump() || (Boolean)this.onlySpeedValue.get() != false && !LiquidBounce.INSTANCE.getModuleManager().get(Speed.class).getState()) ? 1 : 0) != 0;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        if (MinecraftInstance.mc.getThePlayer().isCollidedHorizontally()) {
            this.direction = -this.direction;
            this.direction = this.direction >= (double)0 ? 1.0 : -1.0;
        }
    }

    /*
     * Exception decompiling
     */
    public final boolean doMove(@NotNull MoveEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl45 : INVOKESTATIC - null : Stack underflow
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
     * Exception decompiling
     */
    public final boolean checkVoid() {
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

    /*
     * WARNING - void declaration
     */
    public final boolean isVoid(int xPos, int zPos) {
        void off;
        if (MinecraftInstance.mc.getThePlayer().getPosY() < 0.0) {
            return true;
        }
        while (off < (int)MinecraftInstance.mc.getThePlayer().getPosY() + 2) {
            IAxisAlignedBB bb = MinecraftInstance.mc.getThePlayer().getEntityBoundingBox().offset(xPos, -((double)off), zPos);
            if (MinecraftInstance.mc.getTheWorld().getCollidingBoundingBoxes(MinecraftInstance.mc.getThePlayer(), bb).isEmpty()) {
                off += 2;
                continue;
            }
            return false;
        }
        return true;
    }

    public static final /* synthetic */ BoolValue access$getOngroundValue$p(TargetStrafe $this) {
        return $this.ongroundValue;
    }

    public static final /* synthetic */ boolean access$canStrafe(TargetStrafe $this, EntityLivingBase target) {
        return $this.canStrafe(target);
    }

    public static final /* synthetic */ boolean access$isVoid(TargetStrafe $this, int xPos, int zPos) {
        return $this.isVoid(xPos, zPos);
    }

    public static final /* synthetic */ double access$getDirection$p(TargetStrafe $this) {
        return $this.direction;
    }

    public static final /* synthetic */ void access$setDirection$p(TargetStrafe $this, double d) {
        $this.direction = d;
    }

    public static final /* synthetic */ ListValue access$getRadiusModeValue$p(TargetStrafe $this) {
        return $this.radiusModeValue;
    }

    public static final /* synthetic */ FloatValue access$getRadiusValue$p(TargetStrafe $this) {
        return $this.radiusValue;
    }

    public static final /* synthetic */ BoolValue access$getThirdPersonViewValue$p(TargetStrafe $this) {
        return $this.thirdPersonViewValue;
    }
}

