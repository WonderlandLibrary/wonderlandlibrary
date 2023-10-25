package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Tuple;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSponge extends Block {
  public static final PropertyBool WET = PropertyBool.create("wet");
  
  protected BlockSponge() {
    super(Material.sponge);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)WET, Boolean.valueOf(false)));
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public String getLocalizedName() {
    return StatCollector.translateToLocal(getUnlocalizedName() + ".dry.name");
  }
  
  public int damageDropped(IBlockState state) {
    return ((Boolean)state.getValue((IProperty)WET)).booleanValue() ? 1 : 0;
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    tryAbsorb(worldIn, pos, state);
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    tryAbsorb(worldIn, pos, state);
    super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
  }
  
  protected void tryAbsorb(World worldIn, BlockPos pos, IBlockState state) {
    if (!((Boolean)state.getValue((IProperty)WET)).booleanValue() && absorb(worldIn, pos)) {
      worldIn.setBlockState(pos, state.withProperty((IProperty)WET, Boolean.valueOf(true)), 2);
      worldIn.playAuxSFX(2001, pos, Block.getIdFromBlock(Blocks.water));
    } 
  }
  
  private boolean absorb(World worldIn, BlockPos pos) {
    Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
    ArrayList<BlockPos> arraylist = Lists.newArrayList();
    queue.add(new Tuple(pos, Integer.valueOf(0)));
    int i = 0;
    while (!queue.isEmpty()) {
      Tuple<BlockPos, Integer> tuple = queue.poll();
      BlockPos blockpos = (BlockPos)tuple.getFirst();
      int j = ((Integer)tuple.getSecond()).intValue();
      for (EnumFacing enumfacing : EnumFacing.values()) {
        BlockPos blockpos1 = blockpos.offset(enumfacing);
        if (worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.water) {
          worldIn.setBlockState(blockpos1, Blocks.air.getDefaultState(), 2);
          arraylist.add(blockpos1);
          i++;
          if (j < 6)
            queue.add(new Tuple(blockpos1, Integer.valueOf(j + 1))); 
        } 
      } 
      if (i > 64)
        break; 
    } 
    for (BlockPos blockpos2 : arraylist)
      worldIn.notifyNeighborsOfStateChange(blockpos2, Blocks.air); 
    return (i > 0);
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
    list.add(new ItemStack(itemIn, 1, 0));
    list.add(new ItemStack(itemIn, 1, 1));
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)WET, Boolean.valueOf(((meta & 0x1) == 1)));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Boolean)state.getValue((IProperty)WET)).booleanValue() ? 1 : 0;
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)WET });
  }
  
  public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (((Boolean)state.getValue((IProperty)WET)).booleanValue()) {
      EnumFacing enumfacing = EnumFacing.random(rand);
      if (enumfacing != EnumFacing.UP && !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.offset(enumfacing))) {
        double d0 = pos.getX();
        double d1 = pos.getY();
        double d2 = pos.getZ();
        if (enumfacing == EnumFacing.DOWN) {
          d1 -= 0.05D;
          d0 += rand.nextDouble();
          d2 += rand.nextDouble();
        } else {
          d1 += rand.nextDouble() * 0.8D;
          if (enumfacing.getAxis() == EnumFacing.Axis.X) {
            d2 += rand.nextDouble();
            if (enumfacing == EnumFacing.EAST) {
              d0++;
            } else {
              d0 += 0.05D;
            } 
          } else {
            d0 += rand.nextDouble();
            if (enumfacing == EnumFacing.SOUTH) {
              d2++;
            } else {
              d2 += 0.05D;
            } 
          } 
        } 
        worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockSponge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */