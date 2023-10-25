package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemLead extends Item {
  public ItemLead() {
    setCreativeTab(CreativeTabs.tabTools);
  }
  
  public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    Block block = worldIn.getBlockState(pos).getBlock();
    if (block instanceof net.minecraft.block.BlockFence) {
      if (worldIn.isRemote)
        return true; 
      attachToFence(playerIn, worldIn, pos);
      return true;
    } 
    return false;
  }
  
  public static boolean attachToFence(EntityPlayer player, World worldIn, BlockPos fence) {
    EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForPosition(worldIn, fence);
    boolean flag = false;
    double d0 = 7.0D;
    int i = fence.getX();
    int j = fence.getY();
    int k = fence.getZ();
    for (EntityLiving entityliving : worldIn.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(i - d0, j - d0, k - d0, i + d0, j + d0, k + d0))) {
      if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == player) {
        if (entityleashknot == null)
          entityleashknot = EntityLeashKnot.createKnot(worldIn, fence); 
        entityliving.setLeashedToEntity((Entity)entityleashknot, true);
        flag = true;
      } 
    } 
    return flag;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemLead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */