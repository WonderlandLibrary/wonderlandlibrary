// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockFlower;

public class WorldGenFlowers extends WorldGenerator
{
    private BlockFlower flower;
    private IBlockState state;
    
    public WorldGenFlowers(final BlockFlower flowerIn, final BlockFlower.EnumFlowerType type) {
        this.setGeneratedBlock(flowerIn, type);
    }
    
    public void setGeneratedBlock(final BlockFlower flowerIn, final BlockFlower.EnumFlowerType typeIn) {
        this.flower = flowerIn;
        this.state = flowerIn.getDefaultState().withProperty(flowerIn.getTypeProperty(), typeIn);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        for (int i = 0; i < 64; ++i) {
            final BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.isNether() || blockpos.getY() < 255) && this.flower.canBlockStay(worldIn, blockpos, this.state)) {
                worldIn.setBlockState(blockpos, this.state, 2);
            }
        }
        return true;
    }
}
