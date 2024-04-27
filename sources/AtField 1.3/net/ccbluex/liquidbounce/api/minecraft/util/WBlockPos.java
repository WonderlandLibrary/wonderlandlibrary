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

    public final WBlockPos west() {
        return this.west(1);
    }

    public WBlockPos(double d, double d2, double d3) {
        WBlockPos wBlockPos = this;
        boolean bl = false;
        double d4 = Math.floor(d);
        int n = (int)d4;
        bl = false;
        double d5 = Math.floor(d2);
        int n2 = (int)d5;
        bl = false;
        double d6 = Math.floor(d3);
        wBlockPos(n, n2, (int)d6);
    }

    public final WBlockPos north() {
        return this.north(1);
    }

    public final WBlockPos east(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.EAST), n);
    }

    @JvmOverloads
    public final WBlockPos offset(IEnumFacing iEnumFacing) {
        return WBlockPos.offset$default(this, iEnumFacing, 0, 2, null);
    }

    static {
        Companion = new Companion(null);
        ORIGIN = new WBlockPos(0, 0, 0);
    }

    public final WBlockPos down() {
        return this.down(1);
    }

    public static final WBlockPos access$getORIGIN$cp() {
        return ORIGIN;
    }

    public final WBlockPos east() {
        return this.east(1);
    }

    public final WBlockPos up() {
        return this.up(1);
    }

    public static WBlockPos offset$default(WBlockPos wBlockPos, IEnumFacing iEnumFacing, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 1;
        }
        return wBlockPos.offset(iEnumFacing, n);
    }

    public final WBlockPos up(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.UP), n);
    }

    public final WBlockPos north(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.NORTH), n);
    }

    public final IBlock getBlock() {
        return BlockUtils.getBlock(this);
    }

    public final WBlockPos add(int n, int n2, int n3) {
        return n == 0 && n2 == 0 && n3 == 0 ? this : new WBlockPos(this.getX() + n, this.getY() + n2, this.getZ() + n3);
    }

    public final WBlockPos south(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.SOUTH), n);
    }

    public final WBlockPos south() {
        return this.south(1);
    }

    public final WBlockPos west(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.WEST), n);
    }

    public WBlockPos(int n, int n2, int n3) {
        super(n, n2, n3);
    }

    @JvmOverloads
    public final WBlockPos offset(IEnumFacing iEnumFacing, int n) {
        return n == 0 ? this : new WBlockPos(this.getX() + iEnumFacing.getDirectionVec().getX() * n, this.getY() + iEnumFacing.getDirectionVec().getY() * n, this.getZ() + iEnumFacing.getDirectionVec().getZ() * n);
    }

    public WBlockPos(IEntity iEntity) {
        this(iEntity.getPosX(), iEntity.getPosY(), iEntity.getPosZ());
    }

    public final WBlockPos down(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.DOWN), n);
    }

    public static final class Companion {
        public final WBlockPos getORIGIN() {
            return WBlockPos.access$getORIGIN$cp();
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}

