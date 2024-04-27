/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.entity.EntityLivingBase
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
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
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.entity.EntityLivingBase;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="TargetStrafe", category=ModuleCategory.MOVEMENT, description="null")
public final class TargetStrafe
extends Module {
    private final ListValue renderModeValue;
    private double direction = -1.0;
    private final BoolValue thirdPersonViewValue = new BoolValue("ThirdPersonView", false);
    private final ListValue radiusModeValue;
    private final FloatValue lineWidthValue;
    private EntityLivingBase targetEntity;
    private boolean isEnabled;
    private boolean doStrafe;
    private final FloatValue radiusValue;
    private final BoolValue onlySpeedValue;
    private final BoolValue holdSpaceValue;
    private final BoolValue ongroundValue;
    private double callBackYaw;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean toggleStrafe() {
        if (this.targetEntity == null) return false;
        if (((Boolean)this.holdSpaceValue.get()).booleanValue()) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (!iEntityPlayerSP.getMovementInput().getJump()) return false;
        }
        if ((Boolean)this.onlySpeedValue.get() == false) return true;
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Speed.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        if (!module.getState()) return false;
        return true;
    }

    public final void setCallBackYaw(double d) {
        this.callBackYaw = d;
    }

    public final void setDoStrafe(boolean bl) {
        this.doStrafe = bl;
    }

    public final void setTargetEntity(@Nullable EntityLivingBase entityLivingBase) {
        this.targetEntity = entityLivingBase;
    }

    public static final double access$getDirection$p(TargetStrafe targetStrafe) {
        return targetStrafe.direction;
    }

    public static final boolean access$isVoid(TargetStrafe targetStrafe, int n, int n2) {
        return targetStrafe.isVoid(n, n2);
    }

    /*
     * Exception decompiling
     */
    private final boolean checkVoid() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl12 : ILOAD_3 - null : trying to set 2 previously set to 0
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

    public final void setEnabled(boolean bl) {
        this.isEnabled = bl;
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isCollidedHorizontally()) {
            this.direction = -this.direction;
            this.direction = this.direction >= 0.0 ? 1.0 : -1.0;
        }
    }

    public final EntityLivingBase getTargetEntity() {
        return this.targetEntity;
    }

    public TargetStrafe() {
        this.renderModeValue = new ListValue("RenderMode", new String[]{"Circle", "Polygon", "None"}, "Polygon");
        this.lineWidthValue = new FloatValue("LineWidth", 1.0f, 1.0f, 10.0f);
        this.radiusModeValue = new ListValue("RadiusMode", new String[]{"Normal", "Strict"}, "Normal");
        this.radiusValue = new FloatValue("Radius", 0.5f, 0.1f, 5.0f);
        this.ongroundValue = new BoolValue("OnlyOnGround", false);
        this.holdSpaceValue = new BoolValue("HoldSpace", false);
        this.onlySpeedValue = new BoolValue("OnlySpeed", true);
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onRender3D(Render3DEvent var1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl983 : NEW - null : trying to set 0 previously set to 1
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

    public static final BoolValue access$getOngroundValue$p(TargetStrafe targetStrafe) {
        return targetStrafe.ongroundValue;
    }

    public static final BoolValue access$getThirdPersonViewValue$p(TargetStrafe targetStrafe) {
        return targetStrafe.thirdPersonViewValue;
    }

    public static final boolean access$canStrafe(TargetStrafe targetStrafe, EntityLivingBase entityLivingBase) {
        return targetStrafe.canStrafe(entityLivingBase);
    }

    public final boolean modifyStrafe(StrafeEvent strafeEvent) {
        if (!this.isEnabled || strafeEvent.isCancelled()) {
            return false;
        }
        MovementUtils.strafe$default(0.0f, 1, null);
        return true;
    }

    public final boolean getDoStrafe() {
        return this.doStrafe;
    }

    public static final void access$setDirection$p(TargetStrafe targetStrafe, double d) {
        targetStrafe.direction = d;
    }

    public final boolean isEnabled() {
        return this.isEnabled;
    }

    public static final ListValue access$getRadiusModeValue$p(TargetStrafe targetStrafe) {
        return targetStrafe.radiusModeValue;
    }

    public final double getCallBackYaw() {
        return this.callBackYaw;
    }

    /*
     * Enabled aggressive block sorting
     */
    public final boolean doMove(MoveEvent moveEvent) {
        block5: {
            block6: {
                if (!this.getState()) {
                    return false;
                }
                if (!this.doStrafe) break block5;
                if (!((Boolean)this.ongroundValue.get()).booleanValue()) break block6;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!iEntityPlayerSP.getOnGround()) break block5;
            }
            EntityLivingBase entityLivingBase = this.targetEntity;
            if (entityLivingBase == null) return false;
            EntityLivingBase entityLivingBase2 = entityLivingBase;
            MovementUtils.doTargetStrafe$default(MovementUtils.INSTANCE, entityLivingBase2, (float)this.direction, ((Number)this.radiusValue.get()).floatValue(), moveEvent, 0, 16, null);
            this.callBackYaw = RotationUtils.getRotationsEntity(entityLivingBase2).getYaw();
            this.isEnabled = true;
            return true;
        }
        this.isEnabled = false;
        return true;
    }

    public static final FloatValue access$getRadiusValue$p(TargetStrafe targetStrafe) {
        return targetStrafe.radiusValue;
    }
}

