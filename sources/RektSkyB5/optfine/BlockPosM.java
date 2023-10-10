/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class BlockPosM
extends BlockPos {
    private int mx;
    private int my;
    private int mz;
    private int level;
    private BlockPosM[] facings;
    private boolean needsUpdate;

    public BlockPosM(int p_i20_1_, int p_i20_2_, int p_i20_3_) {
        this(p_i20_1_, p_i20_2_, p_i20_3_, 0);
    }

    public BlockPosM(double p_i21_1_, double p_i21_3_, double p_i21_5_) {
        this(MathHelper.floor_double(p_i21_1_), MathHelper.floor_double(p_i21_3_), MathHelper.floor_double(p_i21_5_));
    }

    public BlockPosM(int p_i22_1_, int p_i22_2_, int p_i22_3_, int p_i22_4_) {
        super(0, 0, 0);
        this.mx = p_i22_1_;
        this.my = p_i22_2_;
        this.mz = p_i22_3_;
        this.level = p_i22_4_;
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

    public void setXyz(int p_setXyz_1_, int p_setXyz_2_, int p_setXyz_3_) {
        this.mx = p_setXyz_1_;
        this.my = p_setXyz_2_;
        this.mz = p_setXyz_3_;
        this.needsUpdate = true;
    }

    public void setXyz(double p_setXyz_1_, double p_setXyz_3_, double p_setXyz_5_) {
        this.setXyz(MathHelper.floor_double(p_setXyz_1_), MathHelper.floor_double(p_setXyz_3_), MathHelper.floor_double(p_setXyz_5_));
    }

    @Override
    public BlockPos offset(EnumFacing facing) {
        int i2;
        BlockPosM blockposm;
        if (this.level <= 0) {
            return super.offset(facing, 1);
        }
        if (this.facings == null) {
            this.facings = new BlockPosM[EnumFacing.VALUES.length];
        }
        if (this.needsUpdate) {
            this.update();
        }
        if ((blockposm = this.facings[i2 = facing.getIndex()]) == null) {
            int j2 = this.mx + facing.getFrontOffsetX();
            int k2 = this.my + facing.getFrontOffsetY();
            int l2 = this.mz + facing.getFrontOffsetZ();
            this.facings[i2] = blockposm = new BlockPosM(j2, k2, l2, this.level - 1);
        }
        return blockposm;
    }

    @Override
    public BlockPos offset(EnumFacing facing, int n2) {
        return n2 == 1 ? this.offset(facing) : super.offset(facing, n2);
    }

    private void update() {
        for (int i2 = 0; i2 < 6; ++i2) {
            BlockPosM blockposm = this.facings[i2];
            if (blockposm == null) continue;
            EnumFacing enumfacing = EnumFacing.VALUES[i2];
            int j2 = this.mx + enumfacing.getFrontOffsetX();
            int k2 = this.my + enumfacing.getFrontOffsetY();
            int l2 = this.mz + enumfacing.getFrontOffsetZ();
            blockposm.setXyz(j2, k2, l2);
        }
        this.needsUpdate = false;
    }

    public static Iterable getAllInBoxMutable(BlockPos p_getAllInBoxMutable_0_, BlockPos p_getAllInBoxMutable_1_) {
        final BlockPos blockpos = new BlockPos(Math.min(p_getAllInBoxMutable_0_.getX(), p_getAllInBoxMutable_1_.getX()), Math.min(p_getAllInBoxMutable_0_.getY(), p_getAllInBoxMutable_1_.getY()), Math.min(p_getAllInBoxMutable_0_.getZ(), p_getAllInBoxMutable_1_.getZ()));
        final BlockPos blockpos1 = new BlockPos(Math.max(p_getAllInBoxMutable_0_.getX(), p_getAllInBoxMutable_1_.getX()), Math.max(p_getAllInBoxMutable_0_.getY(), p_getAllInBoxMutable_1_.getY()), Math.max(p_getAllInBoxMutable_0_.getZ(), p_getAllInBoxMutable_1_.getZ()));
        return new Iterable(){

            public Iterator iterator() {
                return new AbstractIterator(){
                    private BlockPosM theBlockPosM = null;

                    protected BlockPosM computeNext0() {
                        if (this.theBlockPosM == null) {
                            this.theBlockPosM = new BlockPosM(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 3);
                            return this.theBlockPosM;
                        }
                        if (this.theBlockPosM.equals(blockpos1)) {
                            return (BlockPosM)this.endOfData();
                        }
                        int i2 = this.theBlockPosM.getX();
                        int j2 = this.theBlockPosM.getY();
                        int k2 = this.theBlockPosM.getZ();
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
                        this.theBlockPosM.setXyz(i2, j2, k2);
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

