// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.BlockBush;

public class WorldGenBush extends WorldGenerator
{
    private final BlockBush block;
    
    public WorldGenBush(final BlockBush blockIn) {
        this.block = blockIn;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        for (int i = 0; i < 64; ++i) {
            final BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.isNether() || blockpos.getY() < 255) && this.block.canBlockStay(worldIn, blockpos, this.block.getDefaultState())) {
                worldIn.setBlockState(blockpos, this.block.getDefaultState(), 2);
            }
        }
        return true;
    }
}
