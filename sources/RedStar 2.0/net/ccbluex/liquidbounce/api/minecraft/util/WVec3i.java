package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\n\u0000\n\u0000\n\n\b\n\b\n\b\n\n\n\u0000\n\n\b\b\u000020B\b000¢B000¢\bJ\r0202020J\r020J02\b0HJ\b0HR0¢\b\n\u0000\b\t\nR0¢\b\n\u0000\b\nR0¢\b\n\u0000\b\f\n¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3i;", "", "x", "", "y", "z", "(DDD)V", "", "(III)V", "getX", "()I", "getY", "getZ", "distanceSq", "p_distanceSq_1_", "p_distanceSq_3_", "p_distanceSq_5_", "zeroedCenteredChunkPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "equals", "", "other", "hashCode", "Pride"})
public class WVec3i {
    private final int x;
    private final int y;
    private final int z;

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        Object object = other;
        if (Intrinsics.areEqual(this.getClass(), object != null ? object.getClass() : null) ^ true) {
            return false;
        }
        Object object2 = other;
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.util.WVec3i");
        }
        WVec3i cfr_ignored_0 = (WVec3i)object2;
        if (this.x != ((WVec3i)other).x) {
            return false;
        }
        if (this.y != ((WVec3i)other).y) {
            return false;
        }
        return this.z == ((WVec3i)other).z;
    }

    public int hashCode() {
        int result = this.x;
        result = 31 * result + this.y;
        result = 31 * result + this.z;
        return result;
    }

    public final double distanceSq(@NotNull WBlockPos zeroedCenteredChunkPos) {
        Intrinsics.checkParameterIsNotNull(zeroedCenteredChunkPos, "zeroedCenteredChunkPos");
        return this.distanceSq(zeroedCenteredChunkPos.getX(), zeroedCenteredChunkPos.getY(), zeroedCenteredChunkPos.getZ());
    }

    public final double distanceSq(double p_distanceSq_1_, double p_distanceSq_3_, double p_distanceSq_5_) {
        double x = (double)this.x - p_distanceSq_1_;
        double y = (double)this.y - p_distanceSq_3_;
        double z = (double)this.z - p_distanceSq_5_;
        return x * x + y * y + z * z;
    }

    public final int getX() {
        return this.x;
    }

    public final int getY() {
        return this.y;
    }

    public final int getZ() {
        return this.z;
    }

    public WVec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public WVec3i(double x, double y, double z) {
        WVec3i wVec3i = this;
        boolean bl = false;
        double d = Math.floor(x);
        int n = (int)d;
        bl = false;
        double d2 = Math.floor(y);
        int n2 = (int)d2;
        bl = false;
        double d3 = Math.floor(z);
        wVec3i(n, n2, (int)d3);
    }
}
