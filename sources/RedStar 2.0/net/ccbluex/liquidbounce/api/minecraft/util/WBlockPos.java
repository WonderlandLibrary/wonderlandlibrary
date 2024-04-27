package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00008\n\n\n\u0000\n\n\b\n\n\u0000\n\b\n\b\n\t\n\b\n\n\b\n\n\b\b\u0000 20:B\b000¢B\b0\b¢\tB0\n0\n0\n¢J0\u000020\n20\n20\nJ0\u0000J0\u000020\nJ0\u0000J0\u000020\nJ\b0J0\u0000J0\u000020\nJ0\u0000202\b\b0\nHJ0\u0000J0\u000020\nJ0\u000020\u0000J0\u0000J0\u000020\nJ0\u0000J0\u000020\nR\f0\rXD¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3i;", "x", "", "y", "z", "(DDD)V", "source", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;)V", "", "(III)V", "Z_MASK", "", "add", "down", "n", "east", "getBlock", "Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "north", "offset", "side", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "south", "subtract", "p_subtract_1_", "up", "west", "Companion", "Pride"})
public class WBlockPos
extends WVec3i {
    private final long Z_MASK = 0L;
    @NotNull
    private static final WBlockPos ORIGIN;
    private static final int NUM_X_BITS;
    private static final int NUM_Z_BITS = 0;
    private static final int NUM_Y_BITS = 0;
    private static final int Y_SHIFT = 0;
    private static final int X_SHIFT = 0;
    public static final Companion Companion;

    @NotNull
    public final WBlockPos add(int x, int y, int z) {
        return x == 0 && y == 0 && z == 0 ? this : new WBlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    @Nullable
    public final WBlockPos subtract(@NotNull WBlockPos p_subtract_1_) {
        Intrinsics.checkParameterIsNotNull(p_subtract_1_, "p_subtract_1_");
        return this.add(-p_subtract_1_.getX(), -p_subtract_1_.getY(), -p_subtract_1_.getZ());
    }

    @JvmOverloads
    @NotNull
    public final WBlockPos offset(@NotNull IEnumFacing side, int n) {
        Intrinsics.checkParameterIsNotNull(side, "side");
        return n == 0 ? this : new WBlockPos(this.getX() + side.getDirectionVec().getX() * n, this.getY() + side.getDirectionVec().getY() * n, this.getZ() + side.getDirectionVec().getZ() * n);
    }

    public static WBlockPos offset$default(WBlockPos wBlockPos, IEnumFacing iEnumFacing, int n, int n2, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: offset");
        }
        if ((n2 & 2) != 0) {
            n = 1;
        }
        return wBlockPos.offset(iEnumFacing, n);
    }

    @JvmOverloads
    @NotNull
    public final WBlockPos offset(@NotNull IEnumFacing side) {
        return WBlockPos.offset$default(this, side, 0, 2, null);
    }

    @NotNull
    public final WBlockPos up() {
        return this.up(1);
    }

    @NotNull
    public final WBlockPos up(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.UP), n);
    }

    @NotNull
    public final WBlockPos down() {
        return this.down(1);
    }

    @NotNull
    public final WBlockPos down(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.DOWN), n);
    }

    @NotNull
    public final WBlockPos west() {
        return this.west(1);
    }

    @NotNull
    public final WBlockPos west(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.WEST), n);
    }

    @NotNull
    public final WBlockPos east() {
        return this.east(1);
    }

    @NotNull
    public final WBlockPos east(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.EAST), n);
    }

    @NotNull
    public final WBlockPos north() {
        return this.north(1);
    }

    @NotNull
    public final WBlockPos north(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.NORTH), n);
    }

    @NotNull
    public final WBlockPos south() {
        return this.south(1);
    }

    @NotNull
    public final WBlockPos south(int n) {
        return this.offset(WrapperImpl.INSTANCE.getClassProvider().getEnumFacing(EnumFacingType.SOUTH), n);
    }

    @Nullable
    public final IBlock getBlock() {
        WBlockPos blockPos$iv = this;
        boolean $i$f$getBlock = false;
        Object object = MinecraftInstance.mc.getTheWorld();
        return object != null && (object = object.getBlockState(blockPos$iv)) != null ? object.getBlock() : null;
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

    public WBlockPos(@NotNull IEntity source) {
        Intrinsics.checkParameterIsNotNull(source, "source");
        this(source.getPosX(), source.getPosY(), source.getPosZ());
    }

    static {
        Companion = new Companion(null);
        ORIGIN = new WBlockPos(0, 0, 0);
        NUM_X_BITS = 1 + MathHelper.log2((int)MathHelper.smallestEncompassingPowerOfTwo((int)30000000));
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\b\n\b\n\b\n\n\b\b\u000020B\b¢R0¢\b\n\u0000\bR0XD¢\b\n\u0000\b\bR\t0XD¢\b\n\u0000\b\nR0\f¢\b\n\u0000\b\rR0XD¢\b\n\u0000\bR0XD¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos$Companion;", "", "()V", "NUM_X_BITS", "", "getNUM_X_BITS", "()I", "NUM_Y_BITS", "getNUM_Y_BITS", "NUM_Z_BITS", "getNUM_Z_BITS", "ORIGIN", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getORIGIN", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "X_SHIFT", "getX_SHIFT", "Y_SHIFT", "getY_SHIFT", "Pride"})
    public static final class Companion {
        @NotNull
        public final WBlockPos getORIGIN() {
            return ORIGIN;
        }

        public final int getNUM_X_BITS() {
            return NUM_X_BITS;
        }

        public final int getNUM_Z_BITS() {
            return NUM_Z_BITS;
        }

        public final int getNUM_Y_BITS() {
            return NUM_Y_BITS;
        }

        public final int getY_SHIFT() {
            return Y_SHIFT;
        }

        public final int getX_SHIFT() {
            return X_SHIFT;
        }

        private Companion() {
        }

        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
