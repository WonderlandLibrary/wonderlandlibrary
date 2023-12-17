// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenEndIsland extends WorldGenerator
{
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        float f = (float)(rand.nextInt(3) + 4);
        for (int i = 0; f > 0.5f; f -= (float)(rand.nextInt(2) + 0.5), --i) {
            for (int j = MathHelper.floor(-f); j <= MathHelper.ceil(f); ++j) {
                for (int k = MathHelper.floor(-f); k <= MathHelper.ceil(f); ++k) {
                    if (j * j + k * k <= (f + 1.0f) * (f + 1.0f)) {
                        this.setBlockAndNotifyAdequately(worldIn, position.add(j, i, k), Blocks.END_STONE.getDefaultState());
                    }
                }
            }
        }
        return true;
    }
}
