package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\b\n\b\n\b\n\n\b\u000020B\b0¢B00\b0¢\tJ0\u000020\u0000H\bJ!0\u0000202020H\bJ020\u0000J02\b0HJ\b0HJ0\u000020J0\u000020J020\u0000H\bR0¢\b\n\u0000\b\nR0¢\b\n\u0000\b\fR\b0¢\b\n\u0000\b\r¨ "}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3i;", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3i;)V", "xCoord", "", "yCoord", "zCoord", "(DDD)V", "getXCoord", "()D", "getYCoord", "getZCoord", "add", "vec", "addVector", "x", "y", "z", "distanceTo", "equals", "", "other", "hashCode", "", "rotatePitch", "pitch", "", "rotateYaw", "yaw", "squareDistanceTo", "Pride"})
public final class WVec3 {
    private final double xCoord;
    private final double yCoord;
    private final double zCoord;

    @NotNull
    public final WVec3 addVector(double x, double y, double z) {
        int $i$f$addVector = 0;
        return new WVec3(this.getXCoord() + x, this.getYCoord() + y, this.getZCoord() + z);
    }

    public final double distanceTo(@NotNull WVec3 vec) {
        Intrinsics.checkParameterIsNotNull(vec, "vec");
        double d0 = vec.xCoord - this.xCoord;
        double d1 = vec.yCoord - this.yCoord;
        double d2 = vec.zCoord - this.zCoord;
        double d = d0 * d0 + d1 * d1 + d2 * d2;
        boolean bl = false;
        return Math.sqrt(d);
    }

    public final double squareDistanceTo(@NotNull WVec3 vec) {
        int $i$f$squareDistanceTo = 0;
        Intrinsics.checkParameterIsNotNull(vec, "vec");
        double d0 = vec.getXCoord() - this.getXCoord();
        double d1 = vec.getYCoord() - this.getYCoord();
        double d2 = vec.getZCoord() - this.getZCoord();
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final WVec3 add(@NotNull WVec3 vec) {
        void y$iv;
        void x$iv;
        void this_$iv;
        int $i$f$add = 0;
        Intrinsics.checkParameterIsNotNull(vec, "vec");
        WVec3 wVec3 = this;
        double d = vec.getXCoord();
        double d2 = vec.getYCoord();
        double z$iv = vec.getZCoord();
        boolean $i$f$addVector = false;
        return new WVec3(this_$iv.getXCoord() + x$iv, this_$iv.getYCoord() + y$iv, this_$iv.getZCoord() + z$iv);
    }

    @NotNull
    public final WVec3 rotatePitch(float pitch) {
        boolean bl = false;
        float f = (float)Math.cos(pitch);
        boolean bl2 = false;
        float f1 = (float)Math.sin(pitch);
        double d0 = this.xCoord;
        double d1 = this.yCoord * (double)f + this.zCoord * (double)f1;
        double d2 = this.zCoord * (double)f - this.yCoord * (double)f1;
        return new WVec3(d0, d1, d2);
    }

    @NotNull
    public final WVec3 rotateYaw(float yaw) {
        boolean bl = false;
        float f = (float)Math.cos(yaw);
        boolean bl2 = false;
        float f1 = (float)Math.sin(yaw);
        double d0 = this.xCoord * (double)f + this.zCoord * (double)f1;
        double d1 = this.yCoord;
        double d2 = this.zCoord * (double)f - this.xCoord * (double)f1;
        return new WVec3(d0, d1, d2);
    }

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
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.util.WVec3");
        }
        WVec3 cfr_ignored_0 = (WVec3)object2;
        if (this.xCoord != ((WVec3)other).xCoord) {
            return false;
        }
        if (this.yCoord != ((WVec3)other).yCoord) {
            return false;
        }
        return this.zCoord == ((WVec3)other).zCoord;
    }

    public int hashCode() {
        int result = Double.hashCode(this.xCoord);
        result = 31 * result + Double.hashCode(this.yCoord);
        result = 31 * result + Double.hashCode(this.zCoord);
        return result;
    }

    public final double getXCoord() {
        return this.xCoord;
    }

    public final double getYCoord() {
        return this.yCoord;
    }

    public final double getZCoord() {
        return this.zCoord;
    }

    public WVec3(double xCoord, double yCoord, double zCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
    }

    public WVec3(@NotNull WVec3i blockPos) {
        Intrinsics.checkParameterIsNotNull(blockPos, "blockPos");
        this(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
}
