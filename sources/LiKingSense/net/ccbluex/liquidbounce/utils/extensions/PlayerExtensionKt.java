/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils.extensions;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=2, d1={"\u0000F\n\u0000\n\u0002\u0010\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000b\u001a\u0012\u0010\f\u001a\u00020\r*\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e\u001a\n\u0010\u0010\u001a\u00020\u0011*\u00020\u000e\u001a\n\u0010\u0012\u001a\u00020\u0011*\u00020\u0013\u001a\n\u0010\u0014\u001a\u00020\u0011*\u00020\u000e\u001a$\u0010\u0015\u001a\u0004\u0018\u00010\u0016*\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u0001\u001a\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0016*\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\u001b\u001a\u0014\u0010\u001c\u001a\u0004\u0018\u00010\u0016*\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\r\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0004\u00a8\u0006\u001d"}, d2={"hurtPercent", "", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "getHurtPercent", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;)F", "renderHurtTime", "getRenderHurtTime", "getNearestPointBB", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "eye", "box", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "getDistanceToEntityBox", "", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "entity", "isAnimal", "", "isClientFriend", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityPlayer;", "isMob", "rayTraceWithCustomRotation", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition;", "blockReachDistance", "yaw", "pitch", "rotation", "Lnet/ccbluex/liquidbounce/utils/Rotation;", "rayTraceWithServerSideRotation", "LiKingSense"})
public final class PlayerExtensionKt {
    public static final boolean isAnimal(@NotNull IEntity $this$isAnimal) {
        Intrinsics.checkParameterIsNotNull((Object)$this$isAnimal, (String)"$this$isAnimal");
        return MinecraftInstance.classProvider.isEntityAnimal($this$isAnimal) || MinecraftInstance.classProvider.isEntitySquid($this$isAnimal) || MinecraftInstance.classProvider.isEntityGolem($this$isAnimal) || MinecraftInstance.classProvider.isEntityBat($this$isAnimal);
    }

    public static final boolean isMob(@NotNull IEntity $this$isMob) {
        Intrinsics.checkParameterIsNotNull((Object)$this$isMob, (String)"$this$isMob");
        return MinecraftInstance.classProvider.isEntityMob($this$isMob) || MinecraftInstance.classProvider.isEntityVillager($this$isMob) || MinecraftInstance.classProvider.isEntitySlime($this$isMob) || MinecraftInstance.classProvider.isEntityGhast($this$isMob) || MinecraftInstance.classProvider.isEntityDragon($this$isMob) || MinecraftInstance.classProvider.isEntityShulker($this$isMob);
    }

    public static final boolean isClientFriend(@NotNull IEntityPlayer $this$isClientFriend) {
        Intrinsics.checkParameterIsNotNull((Object)$this$isClientFriend, (String)"$this$isClientFriend");
        String string = $this$isClientFriend.getName();
        if (string == null) {
            return false;
        }
        String entityName = string;
        return LiquidBounce.INSTANCE.getFileManager().friendsConfig.isFriend(ColorUtils.stripColor(entityName));
    }

    @Nullable
    public static final IMovingObjectPosition rayTraceWithCustomRotation(@NotNull IEntity $this$rayTraceWithCustomRotation, double blockReachDistance, @NotNull Rotation rotation) {
        Intrinsics.checkParameterIsNotNull((Object)$this$rayTraceWithCustomRotation, (String)"$this$rayTraceWithCustomRotation");
        Intrinsics.checkParameterIsNotNull((Object)rotation, (String)"rotation");
        return PlayerExtensionKt.rayTraceWithCustomRotation($this$rayTraceWithCustomRotation, blockReachDistance, rotation.getYaw(), rotation.getPitch());
    }

    /*
     * Exception decompiling
     */
    @Nullable
    public static final IMovingObjectPosition rayTraceWithCustomRotation(@NotNull IEntity $this$rayTraceWithCustomRotation, double blockReachDistance, float yaw, float pitch) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl52 : INVOKEINTERFACE - null : Stack underflow
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

    @Nullable
    public static final IMovingObjectPosition rayTraceWithServerSideRotation(@NotNull IEntity $this$rayTraceWithServerSideRotation, double blockReachDistance) {
        Intrinsics.checkParameterIsNotNull((Object)$this$rayTraceWithServerSideRotation, (String)"$this$rayTraceWithServerSideRotation");
        Rotation rotation = RotationUtils.serverRotation;
        Intrinsics.checkExpressionValueIsNotNull((Object)rotation, (String)"RotationUtils.serverRotation");
        return PlayerExtensionKt.rayTraceWithCustomRotation($this$rayTraceWithServerSideRotation, blockReachDistance, rotation);
    }

    public static final double getDistanceToEntityBox(@NotNull IEntity $this$getDistanceToEntityBox, @NotNull IEntity entity) {
        Intrinsics.checkParameterIsNotNull((Object)$this$getDistanceToEntityBox, (String)"$this$getDistanceToEntityBox");
        Intrinsics.checkParameterIsNotNull((Object)entity, (String)"entity");
        WVec3 eyes = $this$getDistanceToEntityBox.getPositionEyes(1.0f);
        WVec3 pos = PlayerExtensionKt.getNearestPointBB(eyes, entity.getEntityBoundingBox());
        double d = pos.getXCoord() - eyes.getXCoord();
        boolean bl = false;
        double xDist = Math.abs(d);
        double d2 = pos.getYCoord() - eyes.getYCoord();
        boolean bl2 = false;
        double yDist = Math.abs(d2);
        double d3 = pos.getZCoord() - eyes.getZCoord();
        int n = 0;
        double zDist = Math.abs(d3);
        d3 = xDist;
        n = 2;
        boolean bl3 = false;
        double d4 = Math.pow(d3, n);
        d3 = yDist;
        n = 2;
        double d5 = d4;
        bl3 = false;
        double d6 = Math.pow(d3, n);
        d3 = zDist;
        n = 2;
        d5 += d6;
        bl3 = false;
        d6 = Math.pow(d3, n);
        d3 = d5 + d6;
        n = 0;
        return Math.sqrt(d3);
    }

    public static final float getRenderHurtTime(@NotNull IEntityLivingBase $this$renderHurtTime) {
        Intrinsics.checkParameterIsNotNull((Object)$this$renderHurtTime, (String)"$this$renderHurtTime");
        return (float)$this$renderHurtTime.getHurtTime() - ($this$renderHurtTime.getHurtTime() != 0 ? Minecraft.func_71410_x().field_71428_T.field_194147_b : 0.0f);
    }

    public static final float getHurtPercent(@NotNull IEntityLivingBase $this$hurtPercent) {
        Intrinsics.checkParameterIsNotNull((Object)$this$hurtPercent, (String)"$this$hurtPercent");
        return PlayerExtensionKt.getRenderHurtTime($this$hurtPercent) / (float)10;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final WVec3 getNearestPointBB(@NotNull WVec3 eye, @NotNull IAxisAlignedBB box) {
        Intrinsics.checkParameterIsNotNull((Object)eye, (String)"eye");
        Intrinsics.checkParameterIsNotNull((Object)box, (String)"box");
        double[] origin = new double[]{eye.getXCoord(), eye.getYCoord(), eye.getZCoord()};
        double[] destMins = new double[]{box.getMinX(), box.getMinY(), box.getMinZ()};
        double[] destMaxs = new double[]{box.getMaxX(), box.getMaxY(), box.getMaxZ()};
        int n = 0;
        int n2 = 2;
        while (n <= n2) {
            void i;
            if (origin[i] > destMaxs[i]) {
                origin[i] = destMaxs[i];
            } else if (origin[i] < destMins[i]) {
                origin[i] = destMins[i];
            }
            ++i;
        }
        return new WVec3(origin[0], origin[1], origin[2]);
    }
}

