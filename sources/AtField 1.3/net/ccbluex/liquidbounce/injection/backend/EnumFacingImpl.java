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
    public boolean isSouth() {
        return this.wrapped == EnumFacing.SOUTH;
    }

    @Override
    public WVec3i getDirectionVec() {
        Vec3i vec3i = this.wrapped.func_176730_m();
        boolean bl = false;
        return new WVec3i(vec3i.func_177958_n(), vec3i.func_177956_o(), vec3i.func_177952_p());
    }

    @Override
    public boolean isWest() {
        return this.wrapped == EnumFacing.WEST;
    }

    public EnumFacingImpl(EnumFacing enumFacing) {
        this.wrapped = enumFacing;
    }

    public final EnumFacing getWrapped() {
        return this.wrapped;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof EnumFacingImpl && ((EnumFacingImpl)object).wrapped == this.wrapped;
    }

    @Override
    public IEnumFacing getOpposite() {
        EnumFacing enumFacing = this.wrapped.func_176734_d();
        boolean bl = false;
        return new EnumFacingImpl(enumFacing);
    }

    @Override
    public boolean isNorth() {
        return this.wrapped == EnumFacing.NORTH;
    }

    @Override
    public boolean isDown() {
        return this.wrapped == EnumFacing.DOWN;
    }

    @Override
    public int getAxisOrdinal() {
        return this.wrapped.func_176740_k().ordinal();
    }

    @Override
    public boolean isUp() {
        return this.wrapped == EnumFacing.UP;
    }

    @Override
    public boolean isEast() {
        return this.wrapped == EnumFacing.EAST;
    }
}

