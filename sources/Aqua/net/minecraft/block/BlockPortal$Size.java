package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public static class BlockPortal.Size {
    private final World world;
    private final EnumFacing.Axis axis;
    private final EnumFacing field_150866_c;
    private final EnumFacing field_150863_d;
    private int field_150864_e = 0;
    private BlockPos field_150861_f;
    private int field_150862_g;
    private int field_150868_h;

    public BlockPortal.Size(World worldIn, BlockPos p_i45694_2_, EnumFacing.Axis p_i45694_3_) {
        this.world = worldIn;
        this.axis = p_i45694_3_;
        if (p_i45694_3_ == EnumFacing.Axis.X) {
            this.field_150863_d = EnumFacing.EAST;
            this.field_150866_c = EnumFacing.WEST;
        } else {
            this.field_150863_d = EnumFacing.NORTH;
            this.field_150866_c = EnumFacing.SOUTH;
        }
        BlockPos blockpos = p_i45694_2_;
        while (p_i45694_2_.getY() > blockpos.getY() - 21 && p_i45694_2_.getY() > 0 && this.func_150857_a(worldIn.getBlockState(p_i45694_2_.down()).getBlock())) {
            p_i45694_2_ = p_i45694_2_.down();
        }
        int i = this.func_180120_a(p_i45694_2_, this.field_150863_d) - 1;
        if (i >= 0) {
            this.field_150861_f = p_i45694_2_.offset(this.field_150863_d, i);
            this.field_150868_h = this.func_180120_a(this.field_150861_f, this.field_150866_c);
            if (this.field_150868_h < 2 || this.field_150868_h > 21) {
                this.field_150861_f = null;
                this.field_150868_h = 0;
            }
        }
        if (this.field_150861_f != null) {
            this.field_150862_g = this.func_150858_a();
        }
    }

    protected int func_180120_a(BlockPos p_180120_1_, EnumFacing p_180120_2_) {
        BlockPos blockpos;
        int i;
        for (i = 0; i < 22 && this.func_150857_a(this.world.getBlockState(blockpos = p_180120_1_.offset(p_180120_2_, i)).getBlock()) && this.world.getBlockState(blockpos.down()).getBlock() == Blocks.obsidian; ++i) {
        }
        Block block = this.world.getBlockState(p_180120_1_.offset(p_180120_2_, i)).getBlock();
        return block == Blocks.obsidian ? i : 0;
    }

    public int func_181100_a() {
        return this.field_150862_g;
    }

    public int func_181101_b() {
        return this.field_150868_h;
    }

    protected int func_150858_a() {
        this.field_150862_g = 0;
        block0: while (this.field_150862_g < 21) {
            for (int i = 0; i < this.field_150868_h; ++i) {
                BlockPos blockpos = this.field_150861_f.offset(this.field_150866_c, i).up(this.field_150862_g);
                Block block = this.world.getBlockState(blockpos).getBlock();
                if (!this.func_150857_a(block)) break block0;
                if (block == Blocks.portal) {
                    ++this.field_150864_e;
                }
                if (i == 0 ? (block = this.world.getBlockState(blockpos.offset(this.field_150863_d)).getBlock()) != Blocks.obsidian : i == this.field_150868_h - 1 && (block = this.world.getBlockState(blockpos.offset(this.field_150866_c)).getBlock()) != Blocks.obsidian) break block0;
            }
            ++this.field_150862_g;
        }
        for (int j = 0; j < this.field_150868_h; ++j) {
            if (this.world.getBlockState(this.field_150861_f.offset(this.field_150866_c, j).up(this.field_150862_g)).getBlock() == Blocks.obsidian) continue;
            this.field_150862_g = 0;
            break;
        }
        if (this.field_150862_g <= 21 && this.field_150862_g >= 3) {
            return this.field_150862_g;
        }
        this.field_150861_f = null;
        this.field_150868_h = 0;
        this.field_150862_g = 0;
        return 0;
    }

    protected boolean func_150857_a(Block p_150857_1_) {
        return p_150857_1_.blockMaterial == Material.air || p_150857_1_ == Blocks.fire || p_150857_1_ == Blocks.portal;
    }

    public boolean func_150860_b() {
        return this.field_150861_f != null && this.field_150868_h >= 2 && this.field_150868_h <= 21 && this.field_150862_g >= 3 && this.field_150862_g <= 21;
    }

    public void func_150859_c() {
        for (int i = 0; i < this.field_150868_h; ++i) {
            BlockPos blockpos = this.field_150861_f.offset(this.field_150866_c, i);
            for (int j = 0; j < this.field_150862_g; ++j) {
                this.world.setBlockState(blockpos.up(j), Blocks.portal.getDefaultState().withProperty((IProperty)BlockPortal.AXIS, (Comparable)this.axis), 2);
            }
        }
    }

    static /* synthetic */ int access$000(BlockPortal.Size x0) {
        return x0.field_150864_e;
    }

    static /* synthetic */ int access$100(BlockPortal.Size x0) {
        return x0.field_150868_h;
    }

    static /* synthetic */ int access$200(BlockPortal.Size x0) {
        return x0.field_150862_g;
    }

    static /* synthetic */ EnumFacing access$300(BlockPortal.Size x0) {
        return x0.field_150866_c;
    }

    static /* synthetic */ BlockPos access$400(BlockPortal.Size x0) {
        return x0.field_150861_f;
    }
}
