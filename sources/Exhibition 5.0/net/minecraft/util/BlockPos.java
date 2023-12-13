// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.entity.Entity;

public class BlockPos extends Vec3i
{
    public static final BlockPos ORIGIN;
    private static final int field_177990_b;
    private static final int field_177991_c;
    private static final int field_177989_d;
    private static final int field_177987_f;
    private static final int field_177988_g;
    private static final long field_177994_h;
    private static final long field_177995_i;
    private static final long field_177993_j;
    private static final String __OBFID = "CL_00002334";
    
    public BlockPos(final int x, final int y, final int z) {
        super(x, y, z);
    }
    
    public BlockPos(final double x, final double y, final double z) {
        super(x, y, z);
    }
    
    public BlockPos(final Entity p_i46032_1_) {
        this(p_i46032_1_.posX, p_i46032_1_.posY, p_i46032_1_.posZ);
    }
    
    public BlockPos(final Vec3 p_i46033_1_) {
        this(p_i46033_1_.xCoord, p_i46033_1_.yCoord, p_i46033_1_.zCoord);
    }
    
    public BlockPos(final Vec3i p_i46034_1_) {
        this(p_i46034_1_.getX(), p_i46034_1_.getY(), p_i46034_1_.getZ());
    }
    
    public BlockPos add(final double x, final double y, final double z) {
        return new BlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }
    
    public BlockPos add(final int x, final int y, final int z) {
        return new BlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }
    
    public BlockPos add(final Vec3i vec) {
        return new BlockPos(this.getX() + vec.getX(), this.getY() + vec.getY(), this.getZ() + vec.getZ());
    }
    
    public BlockPos subtract(final Vec3i vec) {
        return new BlockPos(this.getX() - vec.getX(), this.getY() - vec.getY(), this.getZ() - vec.getZ());
    }
    
    public BlockPos multiply(final int factor) {
        return new BlockPos(this.getX() * factor, this.getY() * factor, this.getZ() * factor);
    }
    
    public BlockPos offsetUp() {
        return this.offsetUp(1);
    }
    
    public BlockPos offsetUp(final int n) {
        return this.offset(EnumFacing.UP, n);
    }
    
    public BlockPos offsetDown() {
        return this.offsetDown(1);
    }
    
    public BlockPos offsetDown(final int n) {
        return this.offset(EnumFacing.DOWN, n);
    }
    
    public BlockPos offsetNorth() {
        return this.offsetNorth(1);
    }
    
    public BlockPos offsetNorth(final int n) {
        return this.offset(EnumFacing.NORTH, n);
    }
    
    public BlockPos offsetSouth() {
        return this.offsetSouth(1);
    }
    
    public BlockPos offsetSouth(final int n) {
        return this.offset(EnumFacing.SOUTH, n);
    }
    
    public BlockPos offsetWest() {
        return this.offsetWest(1);
    }
    
    public BlockPos offsetWest(final int n) {
        return this.offset(EnumFacing.WEST, n);
    }
    
    public BlockPos offsetEast() {
        return this.offsetEast(1);
    }
    
    public BlockPos offsetEast(final int n) {
        return this.offset(EnumFacing.EAST, n);
    }
    
    public BlockPos offset(final EnumFacing facing) {
        return this.offset(facing, 1);
    }
    
    public BlockPos offset(final EnumFacing facing, final int n) {
        return new BlockPos(this.getX() + facing.getFrontOffsetX() * n, this.getY() + facing.getFrontOffsetY() * n, this.getZ() + facing.getFrontOffsetZ() * n);
    }
    
    public BlockPos crossProductBP(final Vec3i vec) {
        return new BlockPos(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }
    
    public long toLong() {
        return (this.getX() & BlockPos.field_177994_h) << BlockPos.field_177988_g | (this.getY() & BlockPos.field_177995_i) << BlockPos.field_177987_f | (this.getZ() & BlockPos.field_177993_j) << 0;
    }
    
    public static BlockPos fromLong(final long serialized) {
        final int var2 = (int)(serialized << 64 - BlockPos.field_177988_g - BlockPos.field_177990_b >> 64 - BlockPos.field_177990_b);
        final int var3 = (int)(serialized << 64 - BlockPos.field_177987_f - BlockPos.field_177989_d >> 64 - BlockPos.field_177989_d);
        final int var4 = (int)(serialized << 64 - BlockPos.field_177991_c >> 64 - BlockPos.field_177991_c);
        return new BlockPos(var2, var3, var4);
    }
    
    public static Iterable getAllInBox(final BlockPos from, final BlockPos to) {
        final BlockPos var2 = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final BlockPos var3 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable() {
            private static final String __OBFID = "CL_00002333";
            
            @Override
            public Iterator iterator() {
                return (Iterator)new AbstractIterator() {
                    private BlockPos lastReturned = null;
                    private static final String __OBFID = "CL_00002332";
                    
                    protected BlockPos computeNext0() {
                        if (this.lastReturned == null) {
                            return this.lastReturned = var2;
                        }
                        if (this.lastReturned.equals(var3)) {
                            return (BlockPos)this.endOfData();
                        }
                        int var1 = this.lastReturned.getX();
                        int var2x = this.lastReturned.getY();
                        int var3x = this.lastReturned.getZ();
                        if (var1 < var3.getX()) {
                            ++var1;
                        }
                        else if (var2x < var3.getY()) {
                            var1 = var2.getX();
                            ++var2x;
                        }
                        else if (var3x < var3.getZ()) {
                            var1 = var2.getX();
                            var2x = var2.getY();
                            ++var3x;
                        }
                        return this.lastReturned = new BlockPos(var1, var2x, var3x);
                    }
                    
                    protected Object computeNext() {
                        return this.computeNext0();
                    }
                };
            }
        };
    }
    
    public static Iterable getAllInBoxMutable(final BlockPos from, final BlockPos to) {
        final BlockPos var2 = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final BlockPos var3 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable() {
            private static final String __OBFID = "CL_00002331";
            
            @Override
            public Iterator iterator() {
                return (Iterator)new AbstractIterator() {
                    private MutableBlockPos theBlockPos = null;
                    private static final String __OBFID = "CL_00002330";
                    
                    protected MutableBlockPos computeNext0() {
                        if (this.theBlockPos == null) {
                            return this.theBlockPos = new MutableBlockPos(var2.getX(), var2.getY(), var2.getZ(), null);
                        }
                        if (this.theBlockPos.equals(var3)) {
                            return (MutableBlockPos)this.endOfData();
                        }
                        int var1 = this.theBlockPos.getX();
                        int var2xx = this.theBlockPos.getY();
                        int var3x = this.theBlockPos.getZ();
                        if (var1 < var3.getX()) {
                            ++var1;
                        }
                        else if (var2xx < var3.getY()) {
                            var1 = var2.getX();
                            ++var2xx;
                        }
                        else if (var3x < var3.getZ()) {
                            var1 = var2.getX();
                            var2xx = var2.getY();
                            ++var3x;
                        }
                        this.theBlockPos.x = var1;
                        this.theBlockPos.y = var2xx;
                        this.theBlockPos.z = var3x;
                        return this.theBlockPos;
                    }
                    
                    protected Object computeNext() {
                        return this.computeNext0();
                    }
                };
            }
        };
    }
    
    @Override
    public Vec3i crossProduct(final Vec3i vec) {
        return this.crossProductBP(vec);
    }
    
    static {
        ORIGIN = new BlockPos(0, 0, 0);
        field_177990_b = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
        field_177991_c = BlockPos.field_177990_b;
        field_177989_d = 64 - BlockPos.field_177990_b - BlockPos.field_177991_c;
        field_177987_f = 0 + BlockPos.field_177991_c;
        field_177988_g = BlockPos.field_177987_f + BlockPos.field_177989_d;
        field_177994_h = (1L << BlockPos.field_177990_b) - 1L;
        field_177995_i = (1L << BlockPos.field_177989_d) - 1L;
        field_177993_j = (1L << BlockPos.field_177991_c) - 1L;
    }
    
    public static final class MutableBlockPos extends BlockPos
    {
        public int x;
        public int y;
        public int z;
        private static final String __OBFID = "CL_00002329";
        
        private MutableBlockPos(final int x_, final int y_, final int z_) {
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
        
        @Override
        public Vec3i crossProduct(final Vec3i vec) {
            return super.crossProductBP(vec);
        }
        
        MutableBlockPos(final int p_i46025_1_, final int p_i46025_2_, final int p_i46025_3_, final Object p_i46025_4_) {
            this(p_i46025_1_, p_i46025_2_, p_i46025_3_);
        }
    }
}
