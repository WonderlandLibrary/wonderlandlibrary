package net.minecraft.command;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

static class CommandSpreadPlayers.Position {
    double field_111101_a;
    double field_111100_b;

    CommandSpreadPlayers.Position() {
    }

    CommandSpreadPlayers.Position(double p_i1358_1_, double p_i1358_3_) {
        this.field_111101_a = p_i1358_1_;
        this.field_111100_b = p_i1358_3_;
    }

    double func_111099_a(CommandSpreadPlayers.Position p_111099_1_) {
        double d0 = this.field_111101_a - p_111099_1_.field_111101_a;
        double d1 = this.field_111100_b - p_111099_1_.field_111100_b;
        return Math.sqrt((double)(d0 * d0 + d1 * d1));
    }

    void func_111095_a() {
        double d0 = this.func_111096_b();
        this.field_111101_a /= d0;
        this.field_111100_b /= d0;
    }

    float func_111096_b() {
        return MathHelper.sqrt_double((double)(this.field_111101_a * this.field_111101_a + this.field_111100_b * this.field_111100_b));
    }

    public void func_111094_b(CommandSpreadPlayers.Position p_111094_1_) {
        this.field_111101_a -= p_111094_1_.field_111101_a;
        this.field_111100_b -= p_111094_1_.field_111100_b;
    }

    public boolean func_111093_a(double p_111093_1_, double p_111093_3_, double p_111093_5_, double p_111093_7_) {
        boolean flag = false;
        if (this.field_111101_a < p_111093_1_) {
            this.field_111101_a = p_111093_1_;
            flag = true;
        } else if (this.field_111101_a > p_111093_5_) {
            this.field_111101_a = p_111093_5_;
            flag = true;
        }
        if (this.field_111100_b < p_111093_3_) {
            this.field_111100_b = p_111093_3_;
            flag = true;
        } else if (this.field_111100_b > p_111093_7_) {
            this.field_111100_b = p_111093_7_;
            flag = true;
        }
        return flag;
    }

    public int func_111092_a(World worldIn) {
        BlockPos blockpos = new BlockPos(this.field_111101_a, 256.0, this.field_111100_b);
        while (blockpos.getY() > 0) {
            if (worldIn.getBlockState(blockpos = blockpos.down()).getBlock().getMaterial() == Material.air) continue;
            return blockpos.getY() + 1;
        }
        return 257;
    }

    public boolean func_111098_b(World worldIn) {
        BlockPos blockpos = new BlockPos(this.field_111101_a, 256.0, this.field_111100_b);
        while (blockpos.getY() > 0) {
            Material material = worldIn.getBlockState(blockpos = blockpos.down()).getBlock().getMaterial();
            if (material == Material.air) continue;
            return !material.isLiquid() && material != Material.fire;
        }
        return false;
    }

    public void func_111097_a(Random p_111097_1_, double p_111097_2_, double p_111097_4_, double p_111097_6_, double p_111097_8_) {
        this.field_111101_a = MathHelper.getRandomDoubleInRange((Random)p_111097_1_, (double)p_111097_2_, (double)p_111097_6_);
        this.field_111100_b = MathHelper.getRandomDoubleInRange((Random)p_111097_1_, (double)p_111097_4_, (double)p_111097_8_);
    }
}
