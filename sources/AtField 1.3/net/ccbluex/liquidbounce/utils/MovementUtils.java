/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmName
 *  kotlin.jvm.JvmOverloads
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.entity.EntityLivingBase
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils;

import java.math.BigDecimal;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.entity.EntityLivingBase;
import org.jetbrains.annotations.Nullable;

public final class MovementUtils
extends MinecraftInstance {
    private static double bps;
    public static final MovementUtils INSTANCE;

    public final void setMotion(double d) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double d2 = iEntityPlayerSP.getMovementInput().getMoveForward();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        double d3 = iEntityPlayerSP2.getMovementInput().getMoveStrafe();
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        float f = iEntityPlayerSP3.getRotationYaw();
        if (d2 == 0.0 && d3 == 0.0) {
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP4.setMotionX(0.0);
            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP5.setMotionZ(0.0);
        } else {
            if (d2 != 0.0) {
                if (d3 > 0.0) {
                    f += (float)(d2 > 0.0 ? -45 : 45);
                } else if (d3 < 0.0) {
                    f += (float)(d2 > 0.0 ? 45 : -45);
                }
                d3 = 0.0;
                if (d2 > 0.0) {
                    d2 = 1.0;
                } else if (d2 < 0.0) {
                    d2 = -1.0;
                }
            }
            double d4 = Math.cos(Math.toRadians((double)f + (double)90.0f));
            double d5 = Math.sin(Math.toRadians((double)f + (double)90.0f));
            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP6.setMotionX(d2 * d * d4 + d3 * d * d5);
            IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP7 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP7.setMotionZ(d2 * d * d5 - d3 * d * d4);
        }
    }

    public static void strafe$default(float f, int n, Object object) {
        if ((n & 1) != 0) {
            f = INSTANCE.getSpeed();
        }
        MovementUtils.strafe(f);
    }

    public static final double getDirection() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        float f = iEntityPlayerSP2.getRotationYaw();
        if (iEntityPlayerSP2.getMoveForward() < 0.0f) {
            f += 180.0f;
        }
        float f2 = 1.0f;
        if (iEntityPlayerSP2.getMoveForward() < 0.0f) {
            f2 = -0.5f;
        } else if (iEntityPlayerSP2.getMoveForward() > 0.0f) {
            f2 = 0.5f;
        }
        if (iEntityPlayerSP2.getMoveStrafing() > 0.0f) {
            f -= 90.0f * f2;
        }
        if (iEntityPlayerSP2.getMoveStrafing() < 0.0f) {
            f += 90.0f * f2;
        }
        return Math.toRadians(f);
    }

    static {
        MovementUtils movementUtils;
        INSTANCE = movementUtils = new MovementUtils();
    }

    /*
     * Exception decompiling
     */
    public final void doTargetStrafe(EntityLivingBase var1, float var2, float var3, MoveEvent var4, int var5) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl1 : IFNULL - null : Stack underflow
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
    @JvmStatic
    @JvmOverloads
    public static final void strafe(float var0) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl1 : IFNULL - null : Stack underflow
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

    public static void doTargetStrafe$default(MovementUtils movementUtils, EntityLivingBase entityLivingBase, float f, float f2, MoveEvent moveEvent, int n, int n2, Object object) {
        if ((n2 & 0x10) != 0) {
            n = 0;
        }
        movementUtils.doTargetStrafe(entityLivingBase, f, f2, moveEvent, n);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean hasMotion() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getMotionX() == 0.0) return false;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP2.getMotionZ() == 0.0) return false;
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP3.getMotionY() == 0.0) return false;
        return true;
    }

    public final double getBps() {
        return bps;
    }

    @JvmName(name="getSpeed1")
    public final float getSpeed1() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double d = iEntityPlayerSP.getMotionX();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        double d2 = d * iEntityPlayerSP2.getMotionX();
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        double d3 = iEntityPlayerSP3.getMotionZ();
        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        double d4 = d2 + d3 * iEntityPlayerSP4.getMotionZ();
        boolean bl = false;
        return (float)Math.sqrt(d4);
    }

    public final float getSpeed() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double d = iEntityPlayerSP.getMotionX();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        double d2 = d * iEntityPlayerSP2.getMotionX();
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        double d3 = iEntityPlayerSP3.getMotionZ();
        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        double d4 = d2 + d3 * iEntityPlayerSP4.getMotionZ();
        boolean bl = false;
        return (float)Math.sqrt(d4);
    }

    public final double getBlockSpeed(EntityLivingBase entityLivingBase) {
        return BigDecimal.valueOf(Math.sqrt(Math.pow(entityLivingBase.field_70165_t - entityLivingBase.field_70169_q, 2.0) + Math.pow(entityLivingBase.field_70161_v - entityLivingBase.field_70166_s, 2.0)) * (double)20).setScale(1, 4).doubleValue();
    }

    @JvmStatic
    public static final float getScaffoldRotation(float f, float f2) {
        float f3 = f;
        f3 += 180.0f;
        float f4 = -0.5f;
        if (f2 < 0.0f) {
            f3 -= 90.0f * f4;
        }
        if (f2 > 0.0f) {
            f3 += 90.0f * f4;
        }
        return f3;
    }

    public final void setBps(double d) {
        bps = d;
    }

    @JvmStatic
    @JvmOverloads
    public static final void strafe() {
        MovementUtils.strafe$default(0.0f, 1, null);
    }

    @JvmStatic
    public static void isMoving$annotations() {
    }

    private MovementUtils() {
    }

    public final boolean isOnGround(double d) {
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntity iEntity = iEntityPlayerSP;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        return !iWorldClient.getCollidingBoundingBoxes(iEntity, iEntityPlayerSP2.getEntityBoundingBox().offset(0.0, -d, 0.0)).isEmpty();
    }

    @JvmStatic
    public static void direction$annotations() {
    }

    @JvmName(name="strafe")
    public final void strafe(float f, int n, @Nullable Object object) {
        float f2 = f;
        if ((n & 1) != 0) {
            f2 = INSTANCE.getSpeed1();
        }
        MovementUtils.strafe(f2);
    }

    @JvmStatic
    public static final void forward(double d) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        double d2 = Math.toRadians(iEntityPlayerSP2.getRotationYaw());
        double d3 = iEntityPlayerSP2.getPosX();
        IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
        boolean bl = false;
        double d4 = Math.sin(d2);
        double d5 = d3 + -d4 * d;
        double d6 = iEntityPlayerSP2.getPosZ();
        d4 = iEntityPlayerSP2.getPosY();
        d3 = d5;
        bl = false;
        double d7 = Math.cos(d2);
        iEntityPlayerSP3.setPosition(d3, d4, d6 + d7 * d);
    }
}

