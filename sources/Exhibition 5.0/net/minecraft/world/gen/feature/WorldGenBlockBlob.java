// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import java.util.Iterator;
import net.minecraft.util.Vec3i;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.Block;

public class WorldGenBlockBlob extends WorldGenerator
{
    private final Block field_150545_a;
    private final int field_150544_b;
    private static final String __OBFID = "CL_00000402";
    
    public WorldGenBlockBlob(final Block p_i45450_1_, final int p_i45450_2_) {
        super(false);
        this.field_150545_a = p_i45450_1_;
        this.field_150544_b = p_i45450_2_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random p_180709_2_, BlockPos p_180709_3_) {
        while (p_180709_3_.getY() > 3) {
            if (!worldIn.isAirBlock(p_180709_3_.offsetDown())) {
                final Block var4 = worldIn.getBlockState(p_180709_3_.offsetDown()).getBlock();
                if (var4 == Blocks.grass || var4 == Blocks.dirt) {
                    break;
                }
                if (var4 == Blocks.stone) {
                    break;
                }
            }
            p_180709_3_ = p_180709_3_.offsetDown();
        }
        if (p_180709_3_.getY() <= 3) {
            return false;
        }
        for (int var5 = this.field_150544_b, var6 = 0; var5 >= 0 && var6 < 3; ++var6) {
            final int var7 = var5 + p_180709_2_.nextInt(2);
            final int var8 = var5 + p_180709_2_.nextInt(2);
            final int var9 = var5 + p_180709_2_.nextInt(2);
            final float var10 = (var7 + var8 + var9) * 0.333f + 0.5f;
            for (final BlockPos var12 : BlockPos.getAllInBox(p_180709_3_.add(-var7, -var8, -var9), p_180709_3_.add(var7, var8, var9))) {
                if (var12.distanceSq(p_180709_3_) <= var10 * var10) {
                    worldIn.setBlockState(var12, this.field_150545_a.getDefaultState(), 4);
                }
            }
            p_180709_3_ = p_180709_3_.add(-(var5 + 1) + p_180709_2_.nextInt(2 + var5 * 2), 0 - p_180709_2_.nextInt(2), -(var5 + 1) + p_180709_2_.nextInt(2 + var5 * 2));
        }
        return true;
    }
}
