/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.Vec3i
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0096\u0002J\b\u0010\u0016\u001a\u00020\u0013H\u0016J\b\u0010\u0017\u001a\u00020\u0013H\u0016J\b\u0010\u0018\u001a\u00020\u0013H\u0016J\b\u0010\u0019\u001a\u00020\u0013H\u0016J\b\u0010\u001a\u001a\u00020\u0013H\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u00018VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u001b"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/EnumFacingImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "wrapped", "Lnet/minecraft/util/EnumFacing;", "(Lnet/minecraft/util/EnumFacing;)V", "axisOrdinal", "", "getAxisOrdinal", "()I", "directionVec", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3i;", "getDirectionVec", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3i;", "opposite", "getOpposite", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "getWrapped", "()Lnet/minecraft/util/EnumFacing;", "equals", "", "other", "", "isEast", "isNorth", "isSouth", "isUp", "isWest", "LiKingSense"})
public final class EnumFacingImpl
implements IEnumFacing {
    @NotNull
    private final EnumFacing wrapped;

    @Override
    public boolean isNorth() {
        return this.wrapped == EnumFacing.NORTH;
    }

    @Override
    public boolean isSouth() {
        return this.wrapped == EnumFacing.SOUTH;
    }

    @Override
    public boolean isEast() {
        return this.wrapped == EnumFacing.EAST;
    }

    @Override
    public boolean isWest() {
        return this.wrapped == EnumFacing.WEST;
    }

    @Override
    public boolean isUp() {
        return this.wrapped == EnumFacing.UP;
    }

    @Override
    @NotNull
    public IEnumFacing getOpposite() {
        EnumFacing enumFacing = this.wrapped.func_176734_d();
        Intrinsics.checkExpressionValueIsNotNull((Object)enumFacing, (String)"wrapped.opposite");
        EnumFacing $this$wrap$iv = enumFacing;
        boolean $i$f$wrap = false;
        return new EnumFacingImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public WVec3i getDirectionVec() {
        Vec3i vec3i = this.wrapped.func_176730_m();
        Intrinsics.checkExpressionValueIsNotNull((Object)vec3i, (String)"wrapped.directionVec");
        Vec3i $this$wrap$iv = vec3i;
        boolean $i$f$wrap = false;
        return new WVec3i($this$wrap$iv.func_177958_n(), $this$wrap$iv.func_177956_o(), $this$wrap$iv.func_177952_p());
    }

    @Override
    public int getAxisOrdinal() {
        return this.wrapped.func_176740_k().ordinal();
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof EnumFacingImpl && ((EnumFacingImpl)other).wrapped == this.wrapped;
    }

    @NotNull
    public final EnumFacing getWrapped() {
        return this.wrapped;
    }

    public EnumFacingImpl(@NotNull EnumFacing wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

