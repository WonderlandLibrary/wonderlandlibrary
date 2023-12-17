// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.math;

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.util.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.Entity;
import org.apache.logging.log4j.Logger;
import javax.annotation.concurrent.Immutable;

@Immutable
public class BlockPos extends Vec3i
{
    private static final Logger LOGGER;
    public static final BlockPos ORIGIN;
    private static final int NUM_X_BITS;
    private static final int NUM_Z_BITS;
    private static final int NUM_Y_BITS;
    private static final int Y_SHIFT;
    private static final int X_SHIFT;
    private static final long X_MASK;
    private static final long Y_MASK;
    private static final long Z_MASK;
    
    public BlockPos(final int x, final int y, final int z) {
        super(x, y, z);
    }
    
    public BlockPos(final double x, final double y, final double z) {
        super(x, y, z);
    }
    
    public BlockPos(final Entity source) {
        this(source.posX, source.posY, source.posZ);
    }
    
    public BlockPos(final Vec3d vec) {
        this(vec.x, vec.y, vec.z);
    }
    
    public BlockPos(final Vec3i source) {
        this(source.getX(), source.getY(), source.getZ());
    }
    
    public BlockPos add(final double x, final double y, final double z) {
        return (x == 0.0 && y == 0.0 && z == 0.0) ? this : new BlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }
    
    public BlockPos add(final int x, final int y, final int z) {
        return (x == 0 && y == 0 && z == 0) ? this : new BlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }
    
    public BlockPos add(final Vec3i vec) {
        return this.add(vec.getX(), vec.getY(), vec.getZ());
    }
    
    public BlockPos subtract(final Vec3i vec) {
        return this.add(-vec.getX(), -vec.getY(), -vec.getZ());
    }
    
    public BlockPos up() {
        return this.up(1);
    }
    
    public BlockPos up(final int n) {
        return this.offset(EnumFacing.UP, n);
    }
    
    public BlockPos down() {
        return this.down(1);
    }
    
    public BlockPos down(final int n) {
        return this.offset(EnumFacing.DOWN, n);
    }
    
    public BlockPos north() {
        return this.north(1);
    }
    
    public BlockPos north(final int n) {
        return this.offset(EnumFacing.NORTH, n);
    }
    
    public BlockPos south() {
        return this.south(1);
    }
    
    public BlockPos south(final int n) {
        return this.offset(EnumFacing.SOUTH, n);
    }
    
    public BlockPos west() {
        return this.west(1);
    }
    
    public BlockPos west(final int n) {
        return this.offset(EnumFacing.WEST, n);
    }
    
    public BlockPos east() {
        return this.east(1);
    }
    
    public BlockPos east(final int n) {
        return this.offset(EnumFacing.EAST, n);
    }
    
    public BlockPos offset(final EnumFacing facing) {
        return this.offset(facing, 1);
    }
    
    public BlockPos offset(final EnumFacing facing, final int n) {
        return (n == 0) ? this : new BlockPos(this.getX() + facing.getXOffset() * n, this.getY() + facing.getYOffset() * n, this.getZ() + facing.getZOffset() * n);
    }
    
    public BlockPos rotate(final Rotation rotationIn) {
        switch (rotationIn) {
            default: {
                return this;
            }
            case CLOCKWISE_90: {
                return new BlockPos(-this.getZ(), this.getY(), this.getX());
            }
            case CLOCKWISE_180: {
                return new BlockPos(-this.getX(), this.getY(), -this.getZ());
            }
            case COUNTERCLOCKWISE_90: {
                return new BlockPos(this.getZ(), this.getY(), -this.getX());
            }
        }
    }
    
    @Override
    public BlockPos crossProduct(final Vec3i vec) {
        return new BlockPos(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }
    
    public long toLong() {
        return ((long)this.getX() & BlockPos.X_MASK) << BlockPos.X_SHIFT | ((long)this.getY() & BlockPos.Y_MASK) << BlockPos.Y_SHIFT | ((long)this.getZ() & BlockPos.Z_MASK) << 0;
    }
    
    public static BlockPos fromLong(final long serialized) {
        final int i = (int)(serialized << 64 - BlockPos.X_SHIFT - BlockPos.NUM_X_BITS >> 64 - BlockPos.NUM_X_BITS);
        final int j = (int)(serialized << 64 - BlockPos.Y_SHIFT - BlockPos.NUM_Y_BITS >> 64 - BlockPos.NUM_Y_BITS);
        final int k = (int)(serialized << 64 - BlockPos.NUM_Z_BITS >> 64 - BlockPos.NUM_Z_BITS);
        return new BlockPos(i, j, k);
    }
    
    public static Iterable<BlockPos> getAllInBox(final BlockPos from, final BlockPos to) {
        return getAllInBox(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()), Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
    }
    
    public static Iterable<BlockPos> getAllInBox(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        return new Iterable<BlockPos>() {
            @Override
            public Iterator<BlockPos> iterator() {
                return (Iterator<BlockPos>)new AbstractIterator<BlockPos>() {
                    private boolean first = true;
                    private int lastPosX;
                    private int lastPosY;
                    private int lastPosZ;
                    
                    protected BlockPos computeNext() {
                        if (this.first) {
                            this.first = false;
                            this.lastPosX = x1;
                            this.lastPosY = y1;
                            this.lastPosZ = z1;
                            return new BlockPos(x1, y1, z1);
                        }
                        if (this.lastPosX == x2 && this.lastPosY == y2 && this.lastPosZ == z2) {
                            return (BlockPos)this.endOfData();
                        }
                        if (this.lastPosX < x2) {
                            ++this.lastPosX;
                        }
                        else if (this.lastPosY < y2) {
                            this.lastPosX = x1;
                            ++this.lastPosY;
                        }
                        else if (this.lastPosZ < z2) {
                            this.lastPosX = x1;
                            this.lastPosY = y1;
                            ++this.lastPosZ;
                        }
                        return new BlockPos(this.lastPosX, this.lastPosY, this.lastPosZ);
                    }
                };
            }
        };
    }
    
    public BlockPos toImmutable() {
        return this;
    }
    
    public static Iterable<MutableBlockPos> getAllInBoxMutable(final BlockPos from, final BlockPos to) {
        return getAllInBoxMutable(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()), Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
    }
    
    public static Iterable<MutableBlockPos> getAllInBoxMutable(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        return new Iterable<MutableBlockPos>() {
            @Override
            public Iterator<MutableBlockPos> iterator() {
                return (Iterator<MutableBlockPos>)new AbstractIterator<MutableBlockPos>() {
                    private MutableBlockPos pos;
                    
                    protected MutableBlockPos computeNext() {
                        if (this.pos == null) {
                            return this.pos = new MutableBlockPos(x1, y1, z1);
                        }
                        if (this.pos.x == x2 && this.pos.y == y2 && this.pos.z == z2) {
                            return (MutableBlockPos)this.endOfData();
                        }
                        if (this.pos.x < x2) {
                            final MutableBlockPos pos = this.pos;
                            ++pos.x;
                        }
                        else if (this.pos.y < y2) {
                            this.pos.x = x1;
                            final MutableBlockPos pos2 = this.pos;
                            ++pos2.y;
                        }
                        else if (this.pos.z < z2) {
                            this.pos.x = x1;
                            this.pos.y = y1;
                            final MutableBlockPos pos3 = this.pos;
                            ++pos3.z;
                        }
                        return this.pos;
                    }
                };
            }
        };
    }
    
    static {
        LOGGER = LogManager.getLogger();
        ORIGIN = new BlockPos(0, 0, 0);
        NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
        NUM_Z_BITS = BlockPos.NUM_X_BITS;
        NUM_Y_BITS = 64 - BlockPos.NUM_X_BITS - BlockPos.NUM_Z_BITS;
        Y_SHIFT = 0 + BlockPos.NUM_Z_BITS;
        X_SHIFT = BlockPos.Y_SHIFT + BlockPos.NUM_Y_BITS;
        X_MASK = (1L << BlockPos.NUM_X_BITS) - 1L;
        Y_MASK = (1L << BlockPos.NUM_Y_BITS) - 1L;
        Z_MASK = (1L << BlockPos.NUM_Z_BITS) - 1L;
    }
    
    public static class MutableBlockPos extends BlockPos
    {
        protected int x;
        protected int y;
        protected int z;
        
        public MutableBlockPos() {
            this(0, 0, 0);
        }
        
        public MutableBlockPos(final BlockPos pos) {
            this(pos.getX(), pos.getY(), pos.getZ());
        }
        
        public MutableBlockPos(final int x_, final int y_, final int z_) {
            super(0, 0, 0);
            this.x = x_;
            this.y = y_;
            this.z = z_;
        }
        
        @Override
        public BlockPos add(final double x, final double y, final double z) {
            return super.add(x, y, z).toImmutable();
        }
        
        @Override
        public BlockPos add(final int x, final int y, final int z) {
            return super.add(x, y, z).toImmutable();
        }
        
        @Override
        public BlockPos offset(final EnumFacing facing, final int n) {
            return super.offset(facing, n).toImmutable();
        }
        
        @Override
        public BlockPos rotate(final Rotation rotationIn) {
            return super.rotate(rotationIn).toImmutable();
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
        
        public MutableBlockPos setPos(final int xIn, final int yIn, final int zIn) {
            this.x = xIn;
            this.y = yIn;
            this.z = zIn;
            return this;
        }
        
        public MutableBlockPos setPos(final Entity entityIn) {
            return this.setPos(entityIn.posX, entityIn.posY, entityIn.posZ);
        }
        
        public MutableBlockPos setPos(final double xIn, final double yIn, final double zIn) {
            return this.setPos(MathHelper.floor(xIn), MathHelper.floor(yIn), MathHelper.floor(zIn));
        }
        
        public MutableBlockPos setPos(final Vec3i vec) {
            return this.setPos(vec.getX(), vec.getY(), vec.getZ());
        }
        
        public MutableBlockPos move(final EnumFacing facing) {
            return this.move(facing, 1);
        }
        
        public MutableBlockPos move(final EnumFacing facing, final int n) {
            return this.setPos(this.x + facing.getXOffset() * n, this.y + facing.getYOffset() * n, this.z + facing.getZOffset() * n);
        }
        
        public void setY(final int yIn) {
            this.y = yIn;
        }
        
        @Override
        public BlockPos toImmutable() {
            return new BlockPos(this);
        }
    }
    
    public static final class PooledMutableBlockPos extends MutableBlockPos
    {
        private boolean released;
        private static final List<PooledMutableBlockPos> POOL;
        
        private PooledMutableBlockPos(final int xIn, final int yIn, final int zIn) {
            super(xIn, yIn, zIn);
        }
        
        public static PooledMutableBlockPos retain() {
            return retain(0, 0, 0);
        }
        
        public static PooledMutableBlockPos retain(final double xIn, final double yIn, final double zIn) {
            return retain(MathHelper.floor(xIn), MathHelper.floor(yIn), MathHelper.floor(zIn));
        }
        
        public static PooledMutableBlockPos retain(final Vec3i vec) {
            return retain(vec.getX(), vec.getY(), vec.getZ());
        }
        
        public static PooledMutableBlockPos retain(final int xIn, final int yIn, final int zIn) {
            synchronized (PooledMutableBlockPos.POOL) {
                if (!PooledMutableBlockPos.POOL.isEmpty()) {
                    final PooledMutableBlockPos blockpos$pooledmutableblockpos = PooledMutableBlockPos.POOL.remove(PooledMutableBlockPos.POOL.size() - 1);
                    if (blockpos$pooledmutableblockpos != null && blockpos$pooledmutableblockpos.released) {
                        blockpos$pooledmutableblockpos.released = false;
                        blockpos$pooledmutableblockpos.setPos(xIn, yIn, zIn);
                        return blockpos$pooledmutableblockpos;
                    }
                }
            }
            return new PooledMutableBlockPos(xIn, yIn, zIn);
        }
        
        public void release() {
            synchronized (PooledMutableBlockPos.POOL) {
                if (PooledMutableBlockPos.POOL.size() < 100) {
                    PooledMutableBlockPos.POOL.add(this);
                }
                this.released = true;
            }
        }
        
        @Override
        public PooledMutableBlockPos setPos(final int xIn, final int yIn, final int zIn) {
            if (this.released) {
                BlockPos.LOGGER.error("PooledMutableBlockPosition modified after it was released.", new Throwable());
                this.released = false;
            }
            return (PooledMutableBlockPos)super.setPos(xIn, yIn, zIn);
        }
        
        @Override
        public PooledMutableBlockPos setPos(final Entity entityIn) {
            return (PooledMutableBlockPos)super.setPos(entityIn);
        }
        
        @Override
        public PooledMutableBlockPos setPos(final double xIn, final double yIn, final double zIn) {
            return (PooledMutableBlockPos)super.setPos(xIn, yIn, zIn);
        }
        
        @Override
        public PooledMutableBlockPos setPos(final Vec3i vec) {
            return (PooledMutableBlockPos)super.setPos(vec);
        }
        
        @Override
        public PooledMutableBlockPos move(final EnumFacing facing) {
            return (PooledMutableBlockPos)super.move(facing);
        }
        
        @Override
        public PooledMutableBlockPos move(final EnumFacing facing, final int n) {
            return (PooledMutableBlockPos)super.move(facing, n);
        }
        
        static {
            POOL = Lists.newArrayList();
        }
    }
}
