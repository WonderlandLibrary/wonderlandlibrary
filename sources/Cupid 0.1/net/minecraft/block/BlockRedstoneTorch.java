package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneTorch extends BlockTorch {
  private static Map<World, List<Toggle>> toggles = Maps.newHashMap();
  
  private final boolean isOn;
  
  private boolean isBurnedOut(World worldIn, BlockPos pos, boolean turnOff) {
    if (!toggles.containsKey(worldIn))
      toggles.put(worldIn, Lists.newArrayList()); 
    List<Toggle> list = toggles.get(worldIn);
    if (turnOff)
      list.add(new Toggle(pos, worldIn.getTotalWorldTime())); 
    int i = 0;
    for (int j = 0; j < list.size(); j++) {
      Toggle blockredstonetorch$toggle = list.get(j);
      if (blockredstonetorch$toggle.pos.equals(pos)) {
        i++;
        if (i >= 8)
          return true; 
      } 
    } 
    return false;
  }
  
  protected BlockRedstoneTorch(boolean isOn) {
    this.isOn = isOn;
    setTickRandomly(true);
    setCreativeTab((CreativeTabs)null);
  }
  
  public int tickRate(World worldIn) {
    return 2;
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    if (this.isOn)
      for (EnumFacing enumfacing : EnumFacing.values())
        worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);  
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (this.isOn)
      for (EnumFacing enumfacing : EnumFacing.values())
        worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);  
  }
  
  public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
    return (this.isOn && state.getValue((IProperty)FACING) != side) ? 15 : 0;
  }
  
  private boolean shouldBeOff(World worldIn, BlockPos pos, IBlockState state) {
    EnumFacing enumfacing = ((EnumFacing)state.getValue((IProperty)FACING)).getOpposite();
    return worldIn.isSidePowered(pos.offset(enumfacing), enumfacing);
  }
  
  public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    boolean flag = shouldBeOff(worldIn, pos, state);
    List<Toggle> list = toggles.get(worldIn);
    while (list != null && !list.isEmpty() && worldIn.getTotalWorldTime() - ((Toggle)list.get(0)).time > 60L)
      list.remove(0); 
    if (this.isOn) {
      if (flag) {
        worldIn.setBlockState(pos, Blocks.unlit_redstone_torch.getDefaultState().withProperty((IProperty)FACING, state.getValue((IProperty)FACING)), 3);
        if (isBurnedOut(worldIn, pos, true)) {
          worldIn.playSoundEffect((pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F), "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
          for (int i = 0; i < 5; i++) {
            double d0 = pos.getX() + rand.nextDouble() * 0.6D + 0.2D;
            double d1 = pos.getY() + rand.nextDouble() * 0.6D + 0.2D;
            double d2 = pos.getZ() + rand.nextDouble() * 0.6D + 0.2D;
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
          } 
          worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), 160);
        } 
      } 
    } else if (!flag && !isBurnedOut(worldIn, pos, false)) {
      worldIn.setBlockState(pos, Blocks.redstone_torch.getDefaultState().withProperty((IProperty)FACING, state.getValue((IProperty)FACING)), 3);
    } 
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    if (!onNeighborChangeInternal(worldIn, pos, state))
      if (this.isOn == shouldBeOff(worldIn, pos, state))
        worldIn.scheduleUpdate(pos, this, tickRate(worldIn));  
  }
  
  public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
    return (side == EnumFacing.DOWN) ? getWeakPower(worldIn, pos, state, side) : 0;
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Item.getItemFromBlock(Blocks.redstone_torch);
  }
  
  public boolean canProvidePower() {
    return true;
  }
  
  public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (this.isOn) {
      double d0 = pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
      double d1 = pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
      double d2 = pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
      EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
      if (enumfacing.getAxis().isHorizontal()) {
        EnumFacing enumfacing1 = enumfacing.getOpposite();
        double d3 = 0.27D;
        d0 += 0.27D * enumfacing1.getFrontOffsetX();
        d1 += 0.22D;
        d2 += 0.27D * enumfacing1.getFrontOffsetZ();
      } 
      worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
    } 
  }
  
  public Item getItem(World worldIn, BlockPos pos) {
    return Item.getItemFromBlock(Blocks.redstone_torch);
  }
  
  public boolean isAssociatedBlock(Block other) {
    return (other == Blocks.unlit_redstone_torch || other == Blocks.redstone_torch);
  }
  
  static class Toggle {
    BlockPos pos;
    
    long time;
    
    public Toggle(BlockPos pos, long time) {
      this.pos = pos;
      this.time = time;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockRedstoneTorch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */