/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.AbstractIterator
 */
package net.minecraft.util;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class BlockPos
extends Vec3i {
    public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);
    private static final int NUM_Y_BITS;
    private static final int NUM_X_BITS;
    private static final int NUM_Z_BITS;
    private static final int X_SHIFT;
    private static final long Y_MASK;
    private static final long X_MASK;
    private static final long Z_MASK;
    private static final int Y_SHIFT;

    public static Iterable<BlockPos> getAllInBox(BlockPos blockPos, BlockPos blockPos2) {
        final BlockPos blockPos3 = new BlockPos(Math.min(blockPos.getX(), blockPos2.getX()), Math.min(blockPos.getY(), blockPos2.getY()), Math.min(blockPos.getZ(), blockPos2.getZ()));
        final BlockPos blockPos4 = new BlockPos(Math.max(blockPos.getX(), blockPos2.getX()), Math.max(blockPos.getY(), blockPos2.getY()), Math.max(blockPos.getZ(), blockPos2.getZ()));
        return new Iterable<BlockPos>(){

            @Override
            public Iterator<BlockPos> iterator() {
                return new AbstractIterator<BlockPos>(){
                    private BlockPos lastReturned = null;

                    protected BlockPos computeNext() {
                        if (this.lastReturned == null) {
                            this.lastReturned = blockPos3;
                            return this.lastReturned;
                        }
                        if (this.lastReturned.equals(blockPos4)) {
                            return (BlockPos)this.endOfData();
                        }
                        int n = this.lastReturned.getX();
                        int n2 = this.lastReturned.getY();
                        int n3 = this.lastReturned.getZ();
                        if (n < blockPos4.getX()) {
                            ++n;
                        } else if (n2 < blockPos4.getY()) {
                            n = blockPos3.getX();
                            ++n2;
                        } else if (n3 < blockPos4.getZ()) {
                            n = blockPos3.getX();
                            n2 = blockPos3.getY();
                            ++n3;
                        }
                        this.lastReturned = new BlockPos(n, n2, n3);
                        return this.lastReturned;
                    }
                };
            }
        };
    }

    public BlockPos offset(EnumFacing enumFacing, int n) {
        return n == 0 ? this : new BlockPos(this.getX() + enumFacing.getFrontOffsetX() * n, this.getY() + enumFacing.getFrontOffsetY() * n, this.getZ() + enumFacing.getFrontOffsetZ() * n);
    }

    public Block getBlock() {
        Minecraft.getMinecraft();
        return Minecraft.theWorld.getBlockState(this).getBlock();
    }

    public BlockPos down(int n) {
        return this.offset(EnumFacing.DOWN, n);
    }

    public BlockPos down() {
        return this.down(1);
    }

    public static Iterable<MutableBlockPos> getAllInBoxMutable(BlockPos blockPos, BlockPos blockPos2) {
        final BlockPos blockPos3 = new BlockPos(Math.min(blockPos.getX(), blockPos2.getX()), Math.min(blockPos.getY(), blockPos2.getY()), Math.min(blockPos.getZ(), blockPos2.getZ()));
        final BlockPos blockPos4 = new BlockPos(Math.max(blockPos.getX(), blockPos2.getX()), Math.max(blockPos.getY(), blockPos2.getY()), Math.max(blockPos.getZ(), blockPos2.getZ()));
        return new Iterable<MutableBlockPos>(){

            @Override
            public Iterator<MutableBlockPos> iterator() {
                return new AbstractIterator<MutableBlockPos>(){
                    private MutableBlockPos theBlockPos = null;

                    protected MutableBlockPos computeNext() {
                        if (this.theBlockPos == null) {
                            this.theBlockPos = new MutableBlockPos(blockPos3.getX(), blockPos3.getY(), blockPos3.getZ());
                            return this.theBlockPos;
                        }
                        if (this.theBlockPos.equals(blockPos4)) {
                            return (MutableBlockPos)this.endOfData();
                        }
                        int n = this.theBlockPos.getX();
                        int n2 = this.theBlockPos.getY();
                        int n3 = this.theBlockPos.getZ();
                        if (n < blockPos4.getX()) {
                            ++n;
                        } else if (n2 < blockPos4.getY()) {
                            n = blockPos3.getX();
                            ++n2;
                        } else if (n3 < blockPos4.getZ()) {
                            n = blockPos3.getX();
                            n2 = blockPos3.getY();
                            ++n3;
                        }
                        this.theBlockPos.x = n;
                        this.theBlockPos.y = n2;
                        this.theBlockPos.z = n3;
                        return this.theBlockPos;
                    }
                };
            }
        };
    }

    public BlockPos add(int n, int n2, int n3) {
        return n == 0 && n2 == 0 && n3 == 0 ? this : new BlockPos(this.getX() + n, this.getY() + n2, this.getZ() + n3);
    }

    public BlockPos(Entity entity) {
        this(entity.posX, entity.posY, entity.posZ);
    }

    public BlockPos west(int n) {
        return this.offset(EnumFacing.WEST, n);
    }

    public BlockPos north() {
        return this.north(1);
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

    public BlockPos add(double d, double d2, double d3) {
        return d == 0.0 && d2 == 0.0 && d3 == 0.0 ? this : new BlockPos((double)this.getX() + d, (double)this.getY() + d2, (double)this.getZ() + d3);
    }

    public BlockPos(Vec3 vec3) {
        this(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }

    public BlockPos(double d, double d2, double d3) {
        super(d, d2, d3);
    }

    public BlockPos east() {
        return this.east(1);
    }

    public BlockPos offset(EnumFacing enumFacing) {
        return this.offset(enumFacing, 1);
    }

    public BlockPos up() {
        return this.up(1);
    }

    public BlockPos west() {
        return this.west(1);
    }

    public BlockPos north(int n) {
        return this.offset(EnumFacing.NORTH, n);
    }

    public BlockPos subtract(Vec3i vec3i) {
        return vec3i.getX() == 0 && vec3i.getY() == 0 && vec3i.getZ() == 0 ? this : new BlockPos(this.getX() - vec3i.getX(), this.getY() - vec3i.getY(), this.getZ() - vec3i.getZ());
    }

    public static BlockPos fromLong(long l) {
        int n = (int)(l << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
        int n2 = (int)(l << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
        int n3 = (int)(l << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
        return new BlockPos(n, n2, n3);
    }

    @Override
    public BlockPos crossProduct(Vec3i vec3i) {
        return new BlockPos(this.getY() * vec3i.getZ() - this.getZ() * vec3i.getY(), this.getZ() * vec3i.getX() - this.getX() * vec3i.getZ(), this.getX() * vec3i.getY() - this.getY() * vec3i.getX());
    }

    public BlockPos south() {
        return this.south(1);
    }

    public BlockPos up(int n) {
        return this.offset(EnumFacing.UP, n);
    }

    public BlockPos south(int n) {
        return this.offset(EnumFacing.SOUTH, n);
    }

    public long toLong() {
        return ((long)this.getX() & X_MASK) << X_SHIFT | ((long)this.getY() & Y_MASK) << Y_SHIFT | ((long)this.getZ() & Z_MASK) << 0;
    }

    public BlockPos east(int n) {
        return this.offset(EnumFacing.EAST, n);
    }

    public BlockPos add(Vec3i vec3i) {
        return vec3i.getX() == 0 && vec3i.getY() == 0 && vec3i.getZ() == 0 ? this : new BlockPos(this.getX() + vec3i.getX(), this.getY() + vec3i.getY(), this.getZ() + vec3i.getZ());
    }

    public BlockPos(int n, int n2, int n3) {
        super(n, n2, n3);
    }

    public BlockPos(Vec3i vec3i) {
        this(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public static final class MutableBlockPos
    extends BlockPos {
        private int x;
        private int y;
        private int z;

        @Override
        public int getY() {
            return this.y;
        }

        @Override
        public int getX() {
            return this.x;
        }

        public MutableBlockPos(int n, int n2, int n3) {
            super(0, 0, 0);
            this.x = n;
            this.y = n2;
            this.z = n3;
        }

        @Override
        public int getZ() {
            return this.z;
        }

        public MutableBlockPos func_181079_c(int n, int n2, int n3) {
            this.x = n;
            this.y = n2;
            this.z = n3;
            return this;
        }

        public MutableBlockPos() {
            this(0, 0, 0);
        }
    }
}

