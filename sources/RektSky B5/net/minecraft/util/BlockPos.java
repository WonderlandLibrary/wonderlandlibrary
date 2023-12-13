/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class BlockPos
extends Vec3i {
    public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);
    private static final int NUM_X_BITS;
    private static final int NUM_Z_BITS;
    private static final int NUM_Y_BITS;
    private static final int Y_SHIFT;
    private static final int X_SHIFT;
    private static final long X_MASK;
    private static final long Y_MASK;
    private static final long Z_MASK;

    public BlockPos(int x2, int y2, int z2) {
        super(x2, y2, z2);
    }

    public BlockPos(double x2, double y2, double z2) {
        super(x2, y2, z2);
    }

    public BlockPos(Entity source) {
        this(source.posX, source.posY, source.posZ);
    }

    public BlockPos(Vec3 source) {
        this(source.xCoord, source.yCoord, source.zCoord);
    }

    public BlockPos(Vec3i source) {
        this(source.getX(), source.getY(), source.getZ());
    }

    public BlockPos add(double x2, double y2, double z2) {
        return x2 == 0.0 && y2 == 0.0 && z2 == 0.0 ? this : new BlockPos((double)this.getX() + x2, (double)this.getY() + y2, (double)this.getZ() + z2);
    }

    public BlockPos add(int x2, int y2, int z2) {
        return x2 == 0 && y2 == 0 && z2 == 0 ? this : new BlockPos(this.getX() + x2, this.getY() + y2, this.getZ() + z2);
    }

    public BlockPos add(Vec3i vec) {
        return vec.getX() == 0 && vec.getY() == 0 && vec.getZ() == 0 ? this : new BlockPos(this.getX() + vec.getX(), this.getY() + vec.getY(), this.getZ() + vec.getZ());
    }

    public BlockPos subtract(Vec3i vec) {
        return vec.getX() == 0 && vec.getY() == 0 && vec.getZ() == 0 ? this : new BlockPos(this.getX() - vec.getX(), this.getY() - vec.getY(), this.getZ() - vec.getZ());
    }

    public BlockPos up() {
        return this.up(1);
    }

    public BlockPos up(int n2) {
        return this.offset(EnumFacing.UP, n2);
    }

    public BlockPos down() {
        return this.down(1);
    }

    public BlockPos down(int n2) {
        return this.offset(EnumFacing.DOWN, n2);
    }

    public BlockPos north() {
        return this.north(1);
    }

    public BlockPos north(int n2) {
        return this.offset(EnumFacing.NORTH, n2);
    }

    public BlockPos south() {
        return this.south(1);
    }

    public BlockPos south(int n2) {
        return this.offset(EnumFacing.SOUTH, n2);
    }

    public BlockPos west() {
        return this.west(1);
    }

    public BlockPos west(int n2) {
        return this.offset(EnumFacing.WEST, n2);
    }

    public BlockPos east() {
        return this.east(1);
    }

    public BlockPos east(int n2) {
        return this.offset(EnumFacing.EAST, n2);
    }

    public BlockPos offset(EnumFacing facing) {
        return this.offset(facing, 1);
    }

    public BlockPos offset(EnumFacing facing, int n2) {
        return n2 == 0 ? this : new BlockPos(this.getX() + facing.getFrontOffsetX() * n2, this.getY() + facing.getFrontOffsetY() * n2, this.getZ() + facing.getFrontOffsetZ() * n2);
    }

    @Override
    public BlockPos crossProduct(Vec3i vec) {
        return new BlockPos(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }

    public long toLong() {
        return ((long)this.getX() & X_MASK) << X_SHIFT | ((long)this.getY() & Y_MASK) << Y_SHIFT | ((long)this.getZ() & Z_MASK) << 0;
    }

    public static BlockPos fromLong(long serialized) {
        int i2 = (int)(serialized << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
        int j2 = (int)(serialized << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
        int k2 = (int)(serialized << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
        return new BlockPos(i2, j2, k2);
    }

    public static Iterable<BlockPos> getAllInBox(BlockPos from, BlockPos to) {
        final BlockPos blockpos = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final BlockPos blockpos1 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable<BlockPos>(){

            @Override
            public Iterator<BlockPos> iterator() {
                return new AbstractIterator<BlockPos>(){
                    private BlockPos lastReturned = null;

                    @Override
                    protected BlockPos computeNext() {
                        if (this.lastReturned == null) {
                            this.lastReturned = blockpos;
                            return this.lastReturned;
                        }
                        if (this.lastReturned.equals(blockpos1)) {
                            return (BlockPos)this.endOfData();
                        }
                        int i2 = this.lastReturned.getX();
                        int j2 = this.lastReturned.getY();
                        int k2 = this.lastReturned.getZ();
                        if (i2 < blockpos1.getX()) {
                            ++i2;
                        } else if (j2 < blockpos1.getY()) {
                            i2 = blockpos.getX();
                            ++j2;
                        } else if (k2 < blockpos1.getZ()) {
                            i2 = blockpos.getX();
                            j2 = blockpos.getY();
                            ++k2;
                        }
                        this.lastReturned = new BlockPos(i2, j2, k2);
                        return this.lastReturned;
                    }
                };
            }
        };
    }

    public static Iterable<MutableBlockPos> getAllInBoxMutable(BlockPos from, BlockPos to) {
        final BlockPos blockpos = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final BlockPos blockpos1 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable<MutableBlockPos>(){

            @Override
            public Iterator<MutableBlockPos> iterator() {
                return new AbstractIterator<MutableBlockPos>(){
                    private MutableBlockPos theBlockPos = null;

                    @Override
                    protected MutableBlockPos computeNext() {
                        if (this.theBlockPos == null) {
                            this.theBlockPos = new MutableBlockPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                            return this.theBlockPos;
                        }
                        if (this.theBlockPos.equals(blockpos1)) {
                            return (MutableBlockPos)this.endOfData();
                        }
                        int i2 = this.theBlockPos.getX();
                        int j2 = this.theBlockPos.getY();
                        int k2 = this.theBlockPos.getZ();
                        if (i2 < blockpos1.getX()) {
                            ++i2;
                        } else if (j2 < blockpos1.getY()) {
                            i2 = blockpos.getX();
                            ++j2;
                        } else if (k2 < blockpos1.getZ()) {
                            i2 = blockpos.getX();
                            j2 = blockpos.getY();
                            ++k2;
                        }
                        this.theBlockPos.x = i2;
                        this.theBlockPos.y = j2;
                        this.theBlockPos.z = k2;
                        return this.theBlockPos;
                    }
                };
            }
        };
    }

    static {
        NUM_Z_BITS = NUM_X_BITS = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
        NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
        Y_SHIFT = 0 + NUM_Z_BITS;
        X_SHIFT = Y_SHIFT + NUM_Y_BITS;
        X_MASK = (1L << NUM_X_BITS) - 1L;
        Y_MASK = (1L << NUM_Y_BITS) - 1L;
        Z_MASK = (1L << NUM_Z_BITS) - 1L;
    }

    public static final class MutableBlockPos
    extends BlockPos {
        private int x;
        private int y;
        private int z;

        public MutableBlockPos() {
            this(0, 0, 0);
        }

        public MutableBlockPos(int x_, int y_, int z_) {
            super(0, 0, 0);
            this.x = x_;
            this.y = y_;
            this.z = z_;
        }

        @Override
        public int getX() {
            return this.x;
        }

        @Override
        public int getY() {
            return this.y;
        }

        @Override
        public int getZ() {
            return this.z;
        }

        public MutableBlockPos func_181079_c(int p_181079_1_, int p_181079_2_, int p_181079_3_) {
            this.x = p_181079_1_;
            this.y = p_181079_2_;
            this.z = p_181079_3_;
            return this;
        }
    }
}

