package net.minecraft.dispenser;

import java.util.Random;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.world.World;

public class BehaviorDefaultDispenseItem
  implements IBehaviorDispenseItem
{
  private static final String __OBFID = "CL_00001195";
  
  public BehaviorDefaultDispenseItem() {}
  
  public final ItemStack dispense(IBlockSource source, ItemStack stack)
  {
    ItemStack var3 = dispenseStack(source, stack);
    playDispenseSound(source);
    spawnDispenseParticles(source, BlockDispenser.getFacing(source.getBlockMetadata()));
    return var3;
  }
  
  protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
  {
    EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
    IPosition var4 = BlockDispenser.getDispensePosition(source);
    ItemStack var5 = stack.splitStack(1);
    doDispense(source.getWorld(), var5, 6, var3, var4);
    return stack;
  }
  
  public static void doDispense(World worldIn, ItemStack stack, int speed, EnumFacing p_82486_3_, IPosition position)
  {
    double var5 = position.getX();
    double var7 = position.getY();
    double var9 = position.getZ();
    if (p_82486_3_.getAxis() == EnumFacing.Axis.Y) {
      var7 -= 0.125D;
    } else {
      var7 -= 0.15625D;
    }
    EntityItem var11 = new EntityItem(worldIn, var5, var7, var9, stack);
    double var12 = worldIn.rand.nextDouble() * 0.1D + 0.2D;
    var11.motionX = (p_82486_3_.getFrontOffsetX() * var12);
    var11.motionY = 0.20000000298023224D;
    var11.motionZ = (p_82486_3_.getFrontOffsetZ() * var12);
    var11.motionX += worldIn.rand.nextGaussian() * 0.007499999832361937D * speed;
    var11.motionY += worldIn.rand.nextGaussian() * 0.007499999832361937D * speed;
    var11.motionZ += worldIn.rand.nextGaussian() * 0.007499999832361937D * speed;
    worldIn.spawnEntityInWorld(var11);
  }
  
  protected void playDispenseSound(IBlockSource source)
  {
    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
  }
  
  protected void spawnDispenseParticles(IBlockSource source, EnumFacing facingIn)
  {
    source.getWorld().playAuxSFX(2000, source.getBlockPos(), func_82488_a(facingIn));
  }
  
  private int func_82488_a(EnumFacing facingIn)
  {
    return facingIn.getFrontOffsetX() + 1 + (facingIn.getFrontOffsetZ() + 1) * 3;
  }
}
