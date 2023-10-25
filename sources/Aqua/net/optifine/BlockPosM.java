package net.optifine;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;

public class BlockPosM
extends BlockPos {
    private int mx;
    private int my;
    private int mz;
    private int level;
    private BlockPosM[] facings;
    private boolean needsUpdate;

    public BlockPosM(int x, int y, int z) {
        this(x, y, z, 0);
    }

    public BlockPosM(double xIn, double yIn, double zIn) {
        this(MathHelper.floor_double((double)xIn), MathHelper.floor_double((double)yIn), MathHelper.floor_double((double)zIn));
    }

    public BlockPosM(int x, int y, int z, int level) {
        super(0, 0, 0);
        this.mx = x;
        this.my = y;
        this.mz = z;
        this.level = level;
    }

    public int getX() {
        return this.mx;
    }

    public int getY() {
        return this.my;
    }

    public int getZ() {
        return this.mz;
    }

    public void setXyz(int x, int y, int z) {
        this.mx = x;
        this.my = y;
        this.mz = z;
        this.needsUpdate = true;
    }

    public void setXyz(double xIn, double yIn, double zIn) {
        this.setXyz(MathHelper.floor_double((double)xIn), MathHelper.floor_double((double)yIn), MathHelper.floor_double((double)zIn));
    }

    public BlockPosM set(Vec3i vec) {
        this.setXyz(vec.getX(), vec.getY(), vec.getZ());
        return this;
    }

    public BlockPosM set(int xIn, int yIn, int zIn) {
        this.setXyz(xIn, yIn, zIn);
        return this;
    }

    public BlockPos offsetMutable(EnumFacing facing) {
        return this.offset(facing);
    }

    public BlockPos offset(EnumFacing facing) {
        int i;
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
        if ((blockposm = this.facings[i = facing.getIndex()]) == null) {
            int j = this.mx + facing.getFrontOffsetX();
            int k = this.my + facing.getFrontOffsetY();
            int l = this.mz + facing.getFrontOffsetZ();
            this.facings[i] = blockposm = new BlockPosM(j, k, l, this.level - 1);
        }
        return blockposm;
    }

    public BlockPos offset(EnumFacing facing, int n) {
        return n == 1 ? this.offset(facing) : super.offset(facing, n);
    }

    private void update() {
        for (int i = 0; i < 6; ++i) {
            BlockPosM blockposm = this.facings[i];
            if (blockposm == null) continue;
            EnumFacing enumfacing = EnumFacing.VALUES[i];
            int j = this.mx + enumfacing.getFrontOffsetX();
            int k = this.my + enumfacing.getFrontOffsetY();
            int l = this.mz + enumfacing.getFrontOffsetZ();
            blockposm.setXyz(j, k, l);
        }
        this.needsUpdate = false;
    }

    public BlockPos toImmutable() {
        return new BlockPos(this.mx, this.my, this.mz);
    }

    public static Iterable getAllInBoxMutable(BlockPos from, BlockPos to) {
        BlockPos blockpos = new BlockPos(Math.min((int)from.getX(), (int)to.getX()), Math.min((int)from.getY(), (int)to.getY()), Math.min((int)from.getZ(), (int)to.getZ()));
        BlockPos blockpos1 = new BlockPos(Math.max((int)from.getX(), (int)to.getX()), Math.max((int)from.getY(), (int)to.getY()), Math.max((int)from.getZ(), (int)to.getZ()));
        return new /* Unavailable Anonymous Inner Class!! */;
    }
}
