// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

public class BlockPos extends Vec3i
{
    public static final BlockPos ORIGIN;
    public static final BlockPos NEGATIVE;
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
    
    public BlockPos(final Vec3 source) {
        this(source.xCoord, source.yCoord, source.zCoord);
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
        return (vec.getX() == 0 && vec.getY() == 0 && vec.getZ() == 0) ? this : new BlockPos(this.getX() + vec.getX(), this.getY() + vec.getY(), this.getZ() + vec.getZ());
    }
    
    public BlockPos subtract(final Vec3i vec) {
        return (vec.getX() == 0 && vec.getY() == 0 && vec.getZ() == 0) ? this : new BlockPos(this.getX() - vec.getX(), this.getY() - vec.getY(), this.getZ() - vec.getZ());
    }
    
    public BlockPos up() {
        return new BlockPos(this.getX(), this.getY() + 1, this.getZ());
    }
    
    public BlockPos up(final int n) {
        return (n == 0) ? this : new BlockPos(this.getX(), this.getY() + n, this.getZ());
    }
    
    public BlockPos down() {
        return new BlockPos(this.getX(), this.getY() - 1, this.getZ());
    }
    
    public BlockPos down(final int n) {
        return (n == 0) ? this : new BlockPos(this.getX(), this.getY() - n, this.getZ());
    }
    
    public BlockPos north() {
        return new BlockPos(this.getX(), this.getY(), this.getZ() - 1);
    }
    
    public BlockPos north(final int n) {
        return (n == 0) ? this : new BlockPos(this.getX(), this.getY(), this.getZ() - n);
    }
    
    public BlockPos south() {
        return new BlockPos(this.getX(), this.getY(), this.getZ() + 1);
    }
    
    public BlockPos south(final int n) {
        return (n == 0) ? this : new BlockPos(this.getX(), this.getY(), this.getZ() + n);
    }
    
    public BlockPos west() {
        return new BlockPos(this.getX() - 1, this.getY(), this.getZ());
    }
    
    public BlockPos west(final int n) {
        return (n == 0) ? this : new BlockPos(this.getX() - n, this.getY(), this.getZ());
    }
    
    public BlockPos east() {
        return new BlockPos(this.getX() + 1, this.getY(), this.getZ());
    }
    
    public BlockPos east(final int n) {
        return (n == 0) ? this : new BlockPos(this.getX() + n, this.getY(), this.getZ());
    }
    
    public BlockPos offset(final EnumFacing facing) {
        return new BlockPos(this.getX() + facing.getFrontOffsetX(), this.getY() + facing.getFrontOffsetY(), this.getZ() + facing.getFrontOffsetZ());
    }
    
    public BlockPos offset(final EnumFacing facing, final int n) {
        return (n == 0) ? this : new BlockPos(this.getX() + facing.getFrontOffsetX() * n, this.getY() + facing.getFrontOffsetY() * n, this.getZ() + facing.getFrontOffsetZ() * n);
    }
    
    @Override
    public BlockPos crossProduct(final Vec3i vec) {
        return new BlockPos(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }
    
    public long toLong() {
        return ((long)this.getX() & BlockPos.X_MASK) << BlockPos.X_SHIFT | ((long)this.getY() & BlockPos.Y_MASK) << BlockPos.Y_SHIFT | ((long)this.getZ() & BlockPos.Z_MASK);
    }
    
    public Block getBlock() {
        return Minecraft.getMinecraft().theWorld.getBlockState(this).getBlock();
    }
    
    public static BlockPos fromLong(final long serialized) {
        final int i = (int)(serialized << 64 - BlockPos.X_SHIFT - BlockPos.NUM_X_BITS >> 64 - BlockPos.NUM_X_BITS);
        final int j = (int)(serialized << 64 - BlockPos.Y_SHIFT - BlockPos.NUM_Y_BITS >> 64 - BlockPos.NUM_Y_BITS);
        final int k = (int)(serialized << 64 - BlockPos.NUM_Z_BITS >> 64 - BlockPos.NUM_Z_BITS);
        return new BlockPos(i, j, k);
    }
    
    public static Iterable<BlockPos> getAllInBox(final BlockPos from, final BlockPos to) {
        final BlockPos blockpos = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final BlockPos blockpos2 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable<BlockPos>() {
            @Override
            public Iterator<BlockPos> iterator() {
                return (Iterator<BlockPos>)new AbstractIterator<BlockPos>() {
                    private BlockPos lastReturned = null;
                    
                    protected BlockPos computeNext() {
                        if (this.lastReturned == null) {
                            return this.lastReturned = blockpos;
                        }
                        if (this.lastReturned.equals(blockpos2)) {
                            return (BlockPos)this.endOfData();
                        }
                        int i = this.lastReturned.getX();
                        int j = this.lastReturned.getY();
                        int k = this.lastReturned.getZ();
                        if (i < blockpos2.getX()) {
                            ++i;
                        }
                        else if (j < blockpos2.getY()) {
                            i = blockpos.getX();
                            ++j;
                        }
                        else if (k < blockpos2.getZ()) {
                            i = blockpos.getX();
                            j = blockpos.getY();
                            ++k;
                        }
                        return this.lastReturned = new BlockPos(i, j, k);
                    }
                };
            }
        };
    }
    
    public static Iterable<MutableBlockPos> getAllInBoxMutable(final BlockPos from, final BlockPos to) {
        final BlockPos blockpos = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final BlockPos blockpos2 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable<MutableBlockPos>() {
            @Override
            public Iterator<MutableBlockPos> iterator() {
                return (Iterator<MutableBlockPos>)new AbstractIterator<MutableBlockPos>() {
                    private MutableBlockPos theBlockPos = null;
                    
                    protected MutableBlockPos computeNext() {
                        if (this.theBlockPos == null) {
                            return this.theBlockPos = new MutableBlockPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                        }
                        if (this.theBlockPos.equals(blockpos2)) {
                            return (MutableBlockPos)this.endOfData();
                        }
                        int i = this.theBlockPos.getX();
                        int j = this.theBlockPos.getY();
                        int k = this.theBlockPos.getZ();
                        if (i < blockpos2.getX()) {
                            ++i;
                        }
                        else if (j < blockpos2.getY()) {
                            i = blockpos.getX();
                            ++j;
                        }
                        else if (k < blockpos2.getZ()) {
                            i = blockpos.getX();
                            j = blockpos.getY();
                            ++k;
                        }
                        this.theBlockPos.x = i;
                        this.theBlockPos.y = j;
                        this.theBlockPos.z = k;
                        return this.theBlockPos;
                    }
                };
            }
        };
    }
    
    static {
        ORIGIN = new BlockPos(0, 0, 0);
        NEGATIVE = new BlockPos(-1, -1, -1);
        NUM_X_BITS = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
        NUM_Z_BITS = BlockPos.NUM_X_BITS;
        NUM_Y_BITS = 64 - BlockPos.NUM_X_BITS - BlockPos.NUM_Z_BITS;
        Y_SHIFT = BlockPos.NUM_Z_BITS;
        X_SHIFT = BlockPos.Y_SHIFT + BlockPos.NUM_Y_BITS;
        X_MASK = (1L << BlockPos.NUM_X_BITS) - 1L;
        Y_MASK = (1L << BlockPos.NUM_Y_BITS) - 1L;
        Z_MASK = (1L << BlockPos.NUM_Z_BITS) - 1L;
    }
    
    public static final class MutableBlockPos extends BlockPos
    {
        private int x;
        private int y;
        private int z;
        
        public MutableBlockPos() {
            this(0, 0, 0);
        }
        
        public MutableBlockPos(final int x_, final int y_, final int z_) {
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
        
        public MutableBlockPos set(final int xIn, final int yIn, final int zIn) {
            this.x = xIn;
            this.y = yIn;
            this.z = zIn;
            return this;
        }
    }
}
