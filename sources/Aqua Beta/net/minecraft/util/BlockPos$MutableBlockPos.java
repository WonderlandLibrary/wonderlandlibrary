package net.minecraft.util;

import net.minecraft.util.BlockPos;

public static final class BlockPos.MutableBlockPos
extends BlockPos {
    private int x;
    private int y;
    private int z;

    public BlockPos.MutableBlockPos() {
        this(0, 0, 0);
    }

    public BlockPos.MutableBlockPos(int x_, int y_, int z_) {
        super(0, 0, 0);
        this.x = x_;
        this.y = y_;
        this.z = z_;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public BlockPos.MutableBlockPos set(int p_181079_1_, int p_181079_2_, int p_181079_3_) {
        this.x = p_181079_1_;
        this.y = p_181079_2_;
        this.z = p_181079_3_;
        return this;
    }

    static /* synthetic */ int access$002(BlockPos.MutableBlockPos x0, int x1) {
        x0.x = x1;
        return x0.x;
    }

    static /* synthetic */ int access$102(BlockPos.MutableBlockPos x0, int x1) {
        x0.y = x1;
        return x0.y;
    }

    static /* synthetic */ int access$202(BlockPos.MutableBlockPos x0, int x1) {
        x0.z = x1;
        return x0.z;
    }
}
