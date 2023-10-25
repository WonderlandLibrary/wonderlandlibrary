/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.math;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Immutable
public class BlockPos
extends Vec3i {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);
    private static final int NUM_X_BITS;
    private static final int NUM_Z_BITS;
    private static final int NUM_Y_BITS;
    private static final int Y_SHIFT;
    private static final int X_SHIFT;
    private static final long X_MASK;
    private static final long Y_MASK;
    private static final long Z_MASK;

    public BlockPos(int x, int y, int z) {
        super(x, y, z);
    }

    public BlockPos(double x, double y, double z) {
        super(x, y, z);
    }

    public BlockPos(Entity source) {
        this(source.posX, source.posY, source.posZ);
    }

    public BlockPos(Vec3d vec) {
        this(vec.x, vec.y, vec.z);
    }

    public BlockPos(Vec3i source) {
        this(source.getX(), source.getY(), source.getZ());
    }

    public BlockPos add(double x, double y, double z) {
        return x == 0.0 && y == 0.0 && z == 0.0 ? this : new BlockPos((double)this.getX() + x, (double)this.getY() + y, (double)this.getZ() + z);
    }

    public BlockPos add(int x, int y, int z) {
        return x == 0 && y == 0 && z == 0 ? this : new BlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    public BlockPos add(Vec3i vec) {
        return this.add(vec.getX(), vec.getY(), vec.getZ());
    }

    public BlockPos subtract(Vec3i vec) {
        return this.add(-vec.getX(), -vec.getY(), -vec.getZ());
    }

    public BlockPos up() {
        return this.up(1);
    }

    public BlockPos up(int n) {
        return this.offset(EnumFacing.UP, n);
    }

    public BlockPos down() {
        return this.down(1);
    }

    public BlockPos down(int n) {
        return this.offset(EnumFacing.DOWN, n);
    }

    public BlockPos north() {
        return this.north(1);
    }

    public BlockPos north(int n) {
        return this.offset(EnumFacing.NORTH, n);
    }

    public BlockPos south() {
        return this.south(1);
    }

    public BlockPos south(int n) {
        return this.offset(EnumFacing.SOUTH, n);
    }

    public BlockPos west() {
        return this.west(1);
    }

    public BlockPos west(int n) {
        return this.offset(EnumFacing.WEST, n);
    }

    public BlockPos east() {
        return this.east(1);
    }

    public BlockPos east(int n) {
        return this.offset(EnumFacing.EAST, n);
    }

    public BlockPos offset(EnumFacing facing) {
        return this.offset(facing, 1);
    }

    public BlockPos offset(EnumFacing facing, int n) {
        return n == 0 ? this : new BlockPos(this.getX() + facing.getXOffset() * n, this.getY() + facing.getYOffset() * n, this.getZ() + facing.getZOffset() * n);
    }

    public BlockPos func_190942_a(Rotation p_190942_1_) {
        switch (p_190942_1_) {
            default: {
                return this;
            }
            case CLOCKWISE_90: {
                return new BlockPos(-this.getZ(), this.getY(), this.getX());
            }
            case CLOCKWISE_180: {
                return new BlockPos(-this.getX(), this.getY(), -this.getZ());
            }
            case COUNTERCLOCKWISE_90: 
        }
        return new BlockPos(this.getZ(), this.getY(), -this.getX());
    }

    @Override
    public BlockPos crossProduct(Vec3i vec) {
        return new BlockPos(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }

    public long toLong() {
        return ((long)this.getX() & X_MASK) << X_SHIFT | ((long)this.getY() & Y_MASK) << Y_SHIFT | ((long)this.getZ() & Z_MASK) << 0;
    }

    public static BlockPos fromLong(long serialized) {
        int i = (int)(serialized << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
        int j = (int)(serialized << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
        int k = (int)(serialized << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
        return new BlockPos(i, j, k);
    }

    public static Iterable<BlockPos> getAllInBox(BlockPos from, BlockPos to) {
        return BlockPos.func_191532_a(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()), Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
    }

    public static Iterable<BlockPos> func_191532_a(final int p_191532_0_, final int p_191532_1_, final int p_191532_2_, final int p_191532_3_, final int p_191532_4_, final int p_191532_5_) {
        return new Iterable<BlockPos>(){

            @Override
            public Iterator<BlockPos> iterator() {
                return new AbstractIterator<BlockPos>(){
                    private boolean field_191534_b = true;
                    private int field_191535_c;
                    private int field_191536_d;
                    private int field_191537_e;

                    @Override
                    protected BlockPos computeNext() {
                        if (this.field_191534_b) {
                            this.field_191534_b = false;
                            this.field_191535_c = p_191532_0_;
                            this.field_191536_d = p_191532_1_;
                            this.field_191537_e = p_191532_2_;
                            return new BlockPos(p_191532_0_, p_191532_1_, p_191532_2_);
                        }
                        if (this.field_191535_c == p_191532_3_ && this.field_191536_d == p_191532_4_ && this.field_191537_e == p_191532_5_) {
                            return (BlockPos)this.endOfData();
                        }
                        if (this.field_191535_c < p_191532_3_) {
                            ++this.field_191535_c;
                        } else if (this.field_191536_d < p_191532_4_) {
                            this.field_191535_c = p_191532_0_;
                            ++this.field_191536_d;
                        } else if (this.field_191537_e < p_191532_5_) {
                            this.field_191535_c = p_191532_0_;
                            this.field_191536_d = p_191532_1_;
                            ++this.field_191537_e;
                        }
                        return new BlockPos(this.field_191535_c, this.field_191536_d, this.field_191537_e);
                    }
                };
            }
        };
    }

    public BlockPos toImmutable() {
        return this;
    }

    public static Iterable<MutableBlockPos> getAllInBoxMutable(BlockPos from, BlockPos to) {
        return BlockPos.func_191531_b(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()), Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
    }

    public static Iterable<MutableBlockPos> func_191531_b(final int p_191531_0_, final int p_191531_1_, final int p_191531_2_, final int p_191531_3_, final int p_191531_4_, final int p_191531_5_) {
        return new Iterable<MutableBlockPos>(){

            @Override
            public Iterator<MutableBlockPos> iterator() {
                return new AbstractIterator<MutableBlockPos>(){
                    private MutableBlockPos theBlockPos;

                    @Override
                    protected MutableBlockPos computeNext() {
                        if (this.theBlockPos == null) {
                            this.theBlockPos = new MutableBlockPos(p_191531_0_, p_191531_1_, p_191531_2_);
                            return this.theBlockPos;
                        }
                        if (this.theBlockPos.x == p_191531_3_ && this.theBlockPos.y == p_191531_4_ && this.theBlockPos.z == p_191531_5_) {
                            return (MutableBlockPos)this.endOfData();
                        }
                        if (this.theBlockPos.x < p_191531_3_) {
                            ++this.theBlockPos.x;
                        } else if (this.theBlockPos.y < p_191531_4_) {
                            this.theBlockPos.x = p_191531_0_;
                            ++this.theBlockPos.y;
                        } else if (this.theBlockPos.z < p_191531_5_) {
                            this.theBlockPos.x = p_191531_0_;
                            this.theBlockPos.y = p_191531_1_;
                            ++this.theBlockPos.z;
                        }
                        return this.theBlockPos;
                    }
                };
            }
        };
    }

    static {
        NUM_Z_BITS = NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
        NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
        Y_SHIFT = 0 + NUM_Z_BITS;
        X_SHIFT = Y_SHIFT + NUM_Y_BITS;
        X_MASK = (1L << NUM_X_BITS) - 1L;
        Y_MASK = (1L << NUM_Y_BITS) - 1L;
        Z_MASK = (1L << NUM_Z_BITS) - 1L;
    }

    public static final class PooledMutableBlockPos
    extends MutableBlockPos {
        private boolean released;
        private static final List<PooledMutableBlockPos> POOL = Lists.newArrayList();

        private PooledMutableBlockPos(int xIn, int yIn, int zIn) {
            super(xIn, yIn, zIn);
        }

        public static PooledMutableBlockPos retain() {
            return PooledMutableBlockPos.retain(0, 0, 0);
        }

        public static PooledMutableBlockPos retain(double xIn, double yIn, double zIn) {
            return PooledMutableBlockPos.retain(MathHelper.floor(xIn), MathHelper.floor(yIn), MathHelper.floor(zIn));
        }

        public static PooledMutableBlockPos retain(Vec3i vec) {
            return PooledMutableBlockPos.retain(vec.getX(), vec.getY(), vec.getZ());
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public static PooledMutableBlockPos retain(int xIn, int yIn, int zIn) {
            List<PooledMutableBlockPos> list = POOL;
            synchronized (list) {
                PooledMutableBlockPos blockpos$pooledmutableblockpos;
                if (!POOL.isEmpty() && (blockpos$pooledmutableblockpos = POOL.remove(POOL.size() - 1)) != null && blockpos$pooledmutableblockpos.released) {
                    blockpos$pooledmutableblockpos.released = false;
                    blockpos$pooledmutableblockpos.setPos(xIn, yIn, zIn);
                    return blockpos$pooledmutableblockpos;
                }
            }
            return new PooledMutableBlockPos(xIn, yIn, zIn);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void release() {
            List<PooledMutableBlockPos> list = POOL;
            synchronized (list) {
                if (POOL.size() < 100) {
                    POOL.add(this);
                }
                this.released = true;
            }
        }

        @Override
        public PooledMutableBlockPos setPos(int xIn, int yIn, int zIn) {
            if (this.released) {
                LOGGER.error("PooledMutableBlockPosition modified after it was released.", new Throwable());
                this.released = false;
            }
            return (PooledMutableBlockPos)super.setPos(xIn, yIn, zIn);
        }

        @Override
        public PooledMutableBlockPos setPos(Entity entityIn) {
            return (PooledMutableBlockPos)super.setPos(entityIn);
        }

        @Override
        public PooledMutableBlockPos setPos(double xIn, double yIn, double zIn) {
            return (PooledMutableBlockPos)super.setPos(xIn, yIn, zIn);
        }

        @Override
        public PooledMutableBlockPos setPos(Vec3i vec) {
            return (PooledMutableBlockPos)super.setPos(vec);
        }

        @Override
        public PooledMutableBlockPos move(EnumFacing facing) {
            return (PooledMutableBlockPos)super.move(facing);
        }

        @Override
        public PooledMutableBlockPos move(EnumFacing facing, int p_189534_2_) {
            return (PooledMutableBlockPos)super.move(facing, p_189534_2_);
        }
    }

    public static class MutableBlockPos
    extends BlockPos {
        protected int x;
        protected int y;
        protected int z;

        public MutableBlockPos() {
            this(0, 0, 0);
        }

        public MutableBlockPos(BlockPos pos) {
            this(pos.getX(), pos.getY(), pos.getZ());
        }

        public MutableBlockPos(int x_, int y_, int z_) {
            super(0, 0, 0);
            this.x = x_;
            this.y = y_;
            this.z = z_;
        }

        @Override
        public BlockPos add(double x, double y, double z) {
            return super.add(x, y, z).toImmutable();
        }

        @Override
        public BlockPos add(int x, int y, int z) {
            return super.add(x, y, z).toImmutable();
        }

        @Override
        public BlockPos offset(EnumFacing facing, int n) {
            return super.offset(facing, n).toImmutable();
        }

        @Override
        public BlockPos func_190942_a(Rotation p_190942_1_) {
            return super.func_190942_a(p_190942_1_).toImmutable();
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

        public MutableBlockPos setPos(int xIn, int yIn, int zIn) {
            this.x = xIn;
            this.y = yIn;
            this.z = zIn;
            return this;
        }

        public MutableBlockPos setPos(Entity entityIn) {
            return this.setPos(entityIn.posX, entityIn.posY, entityIn.posZ);
        }

        public MutableBlockPos setPos(double xIn, double yIn, double zIn) {
            return this.setPos(MathHelper.floor(xIn), MathHelper.floor(yIn), MathHelper.floor(zIn));
        }

        public MutableBlockPos setPos(Vec3i vec) {
            return this.setPos(vec.getX(), vec.getY(), vec.getZ());
        }

        public MutableBlockPos move(EnumFacing facing) {
            return this.move(facing, 1);
        }

        public MutableBlockPos move(EnumFacing facing, int p_189534_2_) {
            return this.setPos(this.x + facing.getXOffset() * p_189534_2_, this.y + facing.getYOffset() * p_189534_2_, this.z + facing.getZOffset() * p_189534_2_);
        }

        public void setY(int yIn) {
            this.y = yIn;
        }

        @Override
        public BlockPos toImmutable() {
            return new BlockPos(this);
        }
    }
}

