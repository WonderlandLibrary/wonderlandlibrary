/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.utils.extensions;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0016\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u0012\u0010\u0005\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007\u00a8\u0006\t"}, d2={"getNearestPointBB", "Lnet/minecraft/util/Vec3;", "eye", "box", "Lnet/minecraft/util/AxisAlignedBB;", "getDistanceToEntityBox", "", "Lnet/minecraft/entity/Entity;", "entity", "LiquidBounce"})
public final class PlayerExtensionKt {
    public static final double getDistanceToEntityBox(@NotNull Entity $this$getDistanceToEntityBox, @NotNull Entity entity) {
        Intrinsics.checkNotNullParameter($this$getDistanceToEntityBox, "<this>");
        Intrinsics.checkNotNullParameter(entity, "entity");
        Vec3 eyes = $this$getDistanceToEntityBox.func_174824_e(1.0f);
        Intrinsics.checkNotNullExpressionValue(eyes, "eyes");
        AxisAlignedBB axisAlignedBB = entity.func_174813_aQ();
        Intrinsics.checkNotNullExpressionValue(axisAlignedBB, "entity.entityBoundingBox");
        Vec3 pos = PlayerExtensionKt.getNearestPointBB(eyes, axisAlignedBB);
        double xDist = Math.abs(pos.field_72450_a - eyes.field_72450_a);
        double yDist = Math.abs(pos.field_72448_b - eyes.field_72448_b);
        double zDist = Math.abs(pos.field_72449_c - eyes.field_72449_c);
        return Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2) + Math.pow(zDist, 2));
    }

    @NotNull
    public static final Vec3 getNearestPointBB(@NotNull Vec3 eye, @NotNull AxisAlignedBB box) {
        Intrinsics.checkNotNullParameter(eye, "eye");
        Intrinsics.checkNotNullParameter(box, "box");
        double[] dArray = new double[]{eye.field_72450_a, eye.field_72448_b, eye.field_72449_c};
        double[] origin = dArray;
        double[] dArray2 = new double[]{box.field_72340_a, box.field_72338_b, box.field_72339_c};
        double[] destMins = dArray2;
        double[] dArray3 = new double[]{box.field_72336_d, box.field_72337_e, box.field_72334_f};
        double[] destMaxs = dArray3;
        int n = 0;
        while (n < 3) {
            int i;
            if (origin[i = n++] > destMaxs[i]) {
                origin[i] = destMaxs[i];
                continue;
            }
            if (!(origin[i] < destMins[i])) continue;
            origin[i] = destMins[i];
        }
        return new Vec3(origin[0], origin[1], origin[2]);
    }
}

