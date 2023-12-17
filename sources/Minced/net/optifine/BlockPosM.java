// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;

public class BlockPosM extends BlockPos
{
    private int mx;
    private int my;
    private int mz;
    private int level;
    private BlockPosM[] facings;
    private boolean needsUpdate;
    
    public BlockPosM(final int x, final int y, final int z) {
        this(x, y, z, 0);
    }
    
    public BlockPosM(final double xIn, final double yIn, final double zIn) {
        this(MathHelper.floor(xIn), MathHelper.floor(yIn), MathHelper.floor(zIn));
    }
    
    public BlockPosM(final int x, final int y, final int z, final int level) {
        super(0, 0, 0);
        this.mx = x;
        this.my = y;
        this.mz = z;
        this.level = level;
    }
    
    @Override
    public int getX() {
        return this.mx;
    }
    
    @Override
    public int getY() {
        return this.my;
    }
    
    @Override
    public int getZ() {
        return this.mz;
    }
    
    public void setXyz(final int x, final int y, final int z) {
        this.mx = x;
        this.my = y;
        this.mz = z;
        this.needsUpdate = true;
    }
    
    public void setXyz(final double xIn, final double yIn, final double zIn) {
        this.setXyz(MathHelper.floor(xIn), MathHelper.floor(yIn), MathHelper.floor(zIn));
    }
    
    @Override
    public BlockPos offset(final EnumFacing facing) {
        if (this.level <= 0) {
            return super.offset(facing, 1).toImmutable();
        }
        if (this.facings == null) {
            this.facings = new BlockPosM[EnumFacing.VALUES.length];
        }
        if (this.needsUpdate) {
            this.update();
        }
        final int i = facing.getIndex();
        BlockPosM blockposm = this.facings[i];
        if (blockposm == null) {
            final int j = this.mx + facing.getXOffset();
            final int k = this.my + facing.getYOffset();
            final int l = this.mz + facing.getZOffset();
            blockposm = new BlockPosM(j, k, l, this.level - 1);
            this.facings[i] = blockposm;
        }
        return blockposm;
    }
    
    @Override
    public BlockPos offset(final EnumFacing facing, final int n) {
        return (n == 1) ? this.offset(facing) : super.offset(facing, n).toImmutable();
    }
    
    private void update() {
        for (int i = 0; i < 6; ++i) {
            final BlockPosM blockposm = this.facings[i];
            if (blockposm != null) {
                final EnumFacing enumfacing = EnumFacing.VALUES[i];
                final int j = this.mx + enumfacing.getXOffset();
                final int k = this.my + enumfacing.getYOffset();
                final int l = this.mz + enumfacing.getZOffset();
                blockposm.setXyz(j, k, l);
            }
        }
        this.needsUpdate = false;
    }
    
    @Override
    public BlockPos toImmutable() {
        return new BlockPos(this.mx, this.my, this.mz);
    }
    
    public static Iterable getAllInBoxMutable(final BlockPos from, final BlockPos to) {
        final BlockPos blockpos = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final BlockPos blockpos2 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        return new Iterable() {
            @Override
            public Iterator iterator() {
                return (Iterator)new AbstractIterator() {
                    private BlockPosM theBlockPosM = null;
                    
                    protected BlockPosM computeNext0() {
                        if (this.theBlockPosM == null) {
                            return this.theBlockPosM = new BlockPosM(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 3);
                        }
                        if (this.theBlockPosM.equals(blockpos2)) {
                            return (BlockPosM)this.endOfData();
                        }
                        int i = this.theBlockPosM.getX();
                        int j = this.theBlockPosM.getY();
                        int k = this.theBlockPosM.getZ();
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
                        this.theBlockPosM.setXyz(i, j, k);
                        return this.theBlockPosM;
                    }
                    
                    protected Object computeNext() {
                        return this.computeNext0();
                    }
                };
            }
        };
    }
}
