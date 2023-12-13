/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.world.gen.feature;

import com.google.common.base.Predicate;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenMinable
extends WorldGenerator {
    private final IBlockState oreBlock;
    private final int numberOfBlocks;
    private final Predicate field_175919_c;
    private static final String __OBFID = "CL_00000426";

    public WorldGenMinable(IBlockState p_i45630_1_, int p_i45630_2_) {
        this(p_i45630_1_, p_i45630_2_, BlockHelper.forBlock(Blocks.stone));
    }

    public WorldGenMinable(IBlockState p_i45631_1_, int p_i45631_2_, Predicate p_i45631_3_) {
        this.oreBlock = p_i45631_1_;
        this.numberOfBlocks = p_i45631_2_;
        this.field_175919_c = p_i45631_3_;
    }

    @Override
    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_) {
        float var4 = p_180709_2_.nextFloat() * (float)Math.PI;
        double var5 = (float)(p_180709_3_.getX() + 8) + MathHelper.sin(var4) * (float)this.numberOfBlocks / 8.0f;
        double var7 = (float)(p_180709_3_.getX() + 8) - MathHelper.sin(var4) * (float)this.numberOfBlocks / 8.0f;
        double var9 = (float)(p_180709_3_.getZ() + 8) + MathHelper.cos(var4) * (float)this.numberOfBlocks / 8.0f;
        double var11 = (float)(p_180709_3_.getZ() + 8) - MathHelper.cos(var4) * (float)this.numberOfBlocks / 8.0f;
        double var13 = p_180709_3_.getY() + p_180709_2_.nextInt(3) - 2;
        double var15 = p_180709_3_.getY() + p_180709_2_.nextInt(3) - 2;
        for (int var17 = 0; var17 < this.numberOfBlocks; ++var17) {
            float var18 = (float)var17 / (float)this.numberOfBlocks;
            double var19 = var5 + (var7 - var5) * (double)var18;
            double var21 = var13 + (var15 - var13) * (double)var18;
            double var23 = var9 + (var11 - var9) * (double)var18;
            double var25 = p_180709_2_.nextDouble() * (double)this.numberOfBlocks / 16.0;
            double var27 = (double)(MathHelper.sin((float)Math.PI * var18) + 1.0f) * var25 + 1.0;
            double var29 = (double)(MathHelper.sin((float)Math.PI * var18) + 1.0f) * var25 + 1.0;
            int var31 = MathHelper.floor_double(var19 - var27 / 2.0);
            int var32 = MathHelper.floor_double(var21 - var29 / 2.0);
            int var33 = MathHelper.floor_double(var23 - var27 / 2.0);
            int var34 = MathHelper.floor_double(var19 + var27 / 2.0);
            int var35 = MathHelper.floor_double(var21 + var29 / 2.0);
            int var36 = MathHelper.floor_double(var23 + var27 / 2.0);
            for (int var37 = var31; var37 <= var34; ++var37) {
                double var38 = ((double)var37 + 0.5 - var19) / (var27 / 2.0);
                if (!(var38 * var38 < 1.0)) continue;
                for (int var40 = var32; var40 <= var35; ++var40) {
                    double var41 = ((double)var40 + 0.5 - var21) / (var29 / 2.0);
                    if (!(var38 * var38 + var41 * var41 < 1.0)) continue;
                    for (int var43 = var33; var43 <= var36; ++var43) {
                        BlockPos var46;
                        double var44 = ((double)var43 + 0.5 - var23) / (var27 / 2.0);
                        if (!(var38 * var38 + var41 * var41 + var44 * var44 < 1.0) || !this.field_175919_c.apply((Object)worldIn.getBlockState(var46 = new BlockPos(var37, var40, var43)))) continue;
                        worldIn.setBlockState(var46, this.oreBlock, 2);
                    }
                }
            }
        }
        return true;
    }
}

