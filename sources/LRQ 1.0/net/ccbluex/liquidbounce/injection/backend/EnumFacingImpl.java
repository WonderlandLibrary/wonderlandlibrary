/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.Vec3i
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;

public final class EnumFacingImpl
implements IEnumFacing {
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
    public IEnumFacing getOpposite() {
        EnumFacing $this$wrap$iv = this.wrapped.func_176734_d();
        boolean $i$f$wrap = false;
        return new EnumFacingImpl($this$wrap$iv);
    }

    @Override
    public WVec3i getDirectionVec() {
        Vec3i $this$wrap$iv = this.wrapped.func_176730_m();
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

    public final EnumFacing getWrapped() {
        return this.wrapped;
    }

    public EnumFacingImpl(EnumFacing wrapped) {
        this.wrapped = wrapped;
    }
}

