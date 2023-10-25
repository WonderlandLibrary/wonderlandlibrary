package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMycelium extends Block {
  public static final PropertyBool SNOWY = PropertyBool.create("snowy");
  
  protected BlockMycelium() {
    super(Material.grass, MapColor.purpleColor);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)SNOWY, Boolean.valueOf(false)));
    setTickRandomly(true);
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    Block block = worldIn.getBlockState(pos.up()).getBlock();
    return state.withProperty((IProperty)SNOWY, Boolean.valueOf((block == Blocks.snow || block == Blocks.snow_layer)));
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!worldIn.isRemote)
      if (worldIn.getLightFromNeighbors(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getBlock().getLightOpacity() > 2) {
        worldIn.setBlockState(pos, Blocks.dirt.getDefaultState().withProperty((IProperty)BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
      } else if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
        for (int i = 0; i < 4; i++) {
          BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
          IBlockState iblockstate = worldIn.getBlockState(blockpos);
          Block block = worldIn.getBlockState(blockpos.up()).getBlock();
          if (iblockstate.getBlock() == Blocks.dirt && iblockstate.getValue((IProperty)BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && worldIn.getLightFromNeighbors(blockpos.up()) >= 4 && block.getLightOpacity() <= 2)
            worldIn.setBlockState(blockpos, getDefaultState()); 
        } 
      }  
  }
  
  public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    super.randomDisplayTick(worldIn, pos, state, rand);
    if (rand.nextInt(10) == 0)
      worldIn.spawnParticle(EnumParticleTypes.TOWN_AURA, (pos.getX() + rand.nextFloat()), (pos.getY() + 1.1F), (pos.getZ() + rand.nextFloat()), 0.0D, 0.0D, 0.0D, new int[0]); 
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty((IProperty)BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
  }
  
  public int getMetaFromState(IBlockState state) {
    return 0;
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)SNOWY });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockMycelium.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */