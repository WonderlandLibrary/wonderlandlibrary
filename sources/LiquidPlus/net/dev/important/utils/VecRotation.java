/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.utils.Rotation;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2={"Lnet/dev/important/utils/VecRotation;", "", "vec", "Lnet/minecraft/util/Vec3;", "rotation", "Lnet/dev/important/utils/Rotation;", "(Lnet/minecraft/util/Vec3;Lnet/dev/important/utils/Rotation;)V", "getRotation", "()Lnet/dev/important/utils/Rotation;", "getVec", "()Lnet/minecraft/util/Vec3;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "LiquidBounce"})
public final class VecRotation {
    @NotNull
    private final Vec3 vec;
    @NotNull
    private final Rotation rotation;

    public VecRotation(@NotNull Vec3 vec, @NotNull Rotation rotation) {
        Intrinsics.checkNotNullParameter(vec, "vec");
        Intrinsics.checkNotNullParameter(rotation, "rotation");
        this.vec = vec;
        this.rotation = rotation;
    }

    @NotNull
    public final Vec3 getVec() {
        return this.vec;
    }

    @NotNull
    public final Rotation getRotation() {
        return this.rotation;
    }

    @NotNull
    public final Vec3 component1() {
        return this.vec;
    }

    @NotNull
    public final Rotation component2() {
        return this.rotation;
    }

    @NotNull
    public final VecRotation copy(@NotNull Vec3 vec, @NotNull Rotation rotation) {
        Intrinsics.checkNotNullParameter(vec, "vec");
        Intrinsics.checkNotNullParameter(rotation, "rotation");
        return new VecRotation(vec, rotation);
    }

    public static /* synthetic */ VecRotation copy$default(VecRotation vecRotation, Vec3 vec3, Rotation rotation, int n, Object object) {
        if ((n & 1) != 0) {
            vec3 = vecRotation.vec;
        }
        if ((n & 2) != 0) {
            rotation = vecRotation.rotation;
        }
        return vecRotation.copy(vec3, rotation);
    }

    @NotNull
    public String toString() {
        return "VecRotation(vec=" + this.vec + ", rotation=" + this.rotation + ')';
    }

    public int hashCode() {
        int result = this.vec.hashCode();
        result = result * 31 + this.rotation.hashCode();
        return result;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof VecRotation)) {
            return false;
        }
        VecRotation vecRotation = (VecRotation)other;
        if (!Intrinsics.areEqual(this.vec, vecRotation.vec)) {
            return false;
        }
        return Intrinsics.areEqual(this.rotation, vecRotation.rotation);
    }
}

