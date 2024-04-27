/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmOverloads
 *  kotlin.jvm.internal.DefaultConstructorMarker
 */
package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;

public final class WBlockPos
extends WVec3i {
    private static final WBlockPos ORIGIN;
    public static final Companion Companion;

    public final WBlockPos add(int x, int y, int z) {
        return x == 0 && y == 0 && z == 0 ? this : new WBlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    @JvmOverloads
    public final WBlockPos offset(IEnumFacing side, int n) {
        return n == 0 ? this : new WBlockPos(this.getX() + side.getDirectionVec().getX() * n, this.getY() + side.getDirectionVec().getY() * n, this.getZ() + side.getDirectionVec().getZ() * n);
    }

    public static /* synthetic */ WBlockPos offset$default(WBlockPos wBlockPos, IEnumFacing iEnumFacing, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 1;
        }
        return wBlockPos.offset(iEnumFacing, n);
    }

    @JvmOverloads
    public final WBlockPos offset(IEnumFacing side) {
        return WBlockPos.offset$default(this, side, 0, 2, null);
    }

    public final WBlockPos up() {
        return this.up(1);
    }

    public final WBlockPos up(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.UP), n);
    }

    public final WBlockPos down() {
        return this.down(1);
    }

    public final WBlockPos down(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.DOWN), n);
    }

    public final WBlockPos west() {
        return this.west(1);
    }

    public final WBlockPos west(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.WEST), n);
    }

    public final WBlockPos east() {
        return this.east(1);
    }

    public final WBlockPos east(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.EAST), n);
    }

    public final WBlockPos north() {
        return this.north(1);
    }

    public final WBlockPos north(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.NORTH), n);
    }

    public final WBlockPos south() {
        return this.south(1);
    }

    public final WBlockPos south(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.SOUTH), n);
    }

    public final IBlock getBlock() {
        return BlockUtils.getBlock(this);
    }

    public WBlockPos(int x, int y, int z) {
        super(x, y, z);
    }

    public WBlockPos(double x, double y, double z) {
        WBlockPos wBlockPos = this;
        boolean bl = false;
        double d = Math.floor(x);
        int n = (int)d;
        bl = false;
        double d2 = Math.floor(y);
        int n2 = (int)d2;
        bl = false;
        double d3 = Math.floor(z);
        wBlockPos(n, n2, (int)d3);
    }

    public WBlockPos(IEntity source) {
        this(source.getPosX(), source.getPosY(), source.getPosZ());
    }

    static {
        Companion = new Companion(null);
        ORIGIN = new WBlockPos(0, 0, 0);
    }

    public static final class Companion {
        public final WBlockPos getORIGIN() {
            return ORIGIN;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

