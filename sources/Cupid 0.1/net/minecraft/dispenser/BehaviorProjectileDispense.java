package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class BehaviorProjectileDispense extends BehaviorDefaultDispenseItem {
  public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
    World world = source.getWorld();
    IPosition iposition = BlockDispenser.getDispensePosition(source);
    EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
    IProjectile iprojectile = getProjectileEntity(world, iposition);
    iprojectile.setThrowableHeading(enumfacing.getFrontOffsetX(), (enumfacing.getFrontOffsetY() + 0.1F), enumfacing.getFrontOffsetZ(), func_82500_b(), func_82498_a());
    world.spawnEntityInWorld((Entity)iprojectile);
    stack.splitStack(1);
    return stack;
  }
  
  protected void playDispenseSound(IBlockSource source) {
    source.getWorld().playAuxSFX(1002, source.getBlockPos(), 0);
  }
  
  protected abstract IProjectile getProjectileEntity(World paramWorld, IPosition paramIPosition);
  
  protected float func_82498_a() {
    return 6.0F;
  }
  
  protected float func_82500_b() {
    return 1.1F;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\dispenser\BehaviorProjectileDispense.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */