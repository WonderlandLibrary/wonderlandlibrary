package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemHangingEntity extends Item {
  private final Class<? extends EntityHanging> hangingEntityClass;
  
  public ItemHangingEntity(Class<? extends EntityHanging> entityClass) {
    this.hangingEntityClass = entityClass;
    setCreativeTab(CreativeTabs.tabDecorations);
  }
  
  public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (side == EnumFacing.DOWN)
      return false; 
    if (side == EnumFacing.UP)
      return false; 
    BlockPos blockpos = pos.offset(side);
    if (!playerIn.canPlayerEdit(blockpos, side, stack))
      return false; 
    EntityHanging entityhanging = createEntity(worldIn, blockpos, side);
    if (entityhanging != null && entityhanging.onValidSurface()) {
      if (!worldIn.isRemote)
        worldIn.spawnEntityInWorld((Entity)entityhanging); 
      stack.stackSize--;
    } 
    return true;
  }
  
  private EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing clickedSide) {
    return (this.hangingEntityClass == EntityPainting.class) ? (EntityHanging)new EntityPainting(worldIn, pos, clickedSide) : ((this.hangingEntityClass == EntityItemFrame.class) ? (EntityHanging)new EntityItemFrame(worldIn, pos, clickedSide) : null);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemHangingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */