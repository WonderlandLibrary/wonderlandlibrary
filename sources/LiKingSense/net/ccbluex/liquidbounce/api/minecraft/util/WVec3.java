/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u001d\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\tJ\u0011\u0010\u000e\u001a\u00020\u00002\u0006\u0010\u000f\u001a\u00020\u0000H\u0086\bJ!\u0010\u0010\u001a\u00020\u00002\u0006\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0006H\u0086\bJ\u000e\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0000J\u0013\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\u000e\u0010\u001a\u001a\u00020\u00002\u0006\u0010\u001b\u001a\u00020\u001cJ\u000e\u0010\u001d\u001a\u00020\u00002\u0006\u0010\u001e\u001a\u00020\u001cJ\u0011\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0000H\u0086\bR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0011\u0010\b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000b\u00a8\u0006 "}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3i;", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3i;)V", "xCoord", "", "yCoord", "zCoord", "(DDD)V", "getXCoord", "()D", "getYCoord", "getZCoord", "add", "vec", "addVector", "x", "y", "z", "distanceTo", "equals", "", "other", "hashCode", "", "rotatePitch", "pitch", "", "rotateYaw", "yaw", "squareDistanceTo", "LiKingSense"})
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
        Intrinsics.checkParameterIsNotNull((Object)vec, (String)"vec");
        double d0 = vec.xCoord - this.xCoord;
        double d1 = vec.yCoord - this.yCoord;
        double d2 = vec.zCoord - this.zCoord;
        double d = d0 * d0 + d1 * d1 + d2 * d2;
        boolean bl = false;
        return Math.sqrt(d);
    }

    public final double squareDistanceTo(@NotNull WVec3 vec) {
        int $i$f$squareDistanceTo = 0;
        Intrinsics.checkParameterIsNotNull((Object)vec, (String)"vec");
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
        Intrinsics.checkParameterIsNotNull((Object)vec, (String)"vec");
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
        Intrinsics.checkParameterIsNotNull((Object)blockPos, (String)"blockPos");
        this(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
}

