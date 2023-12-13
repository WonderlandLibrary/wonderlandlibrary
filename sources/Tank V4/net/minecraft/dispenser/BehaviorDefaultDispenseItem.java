package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BehaviorDefaultDispenseItem implements IBehaviorDispenseItem {
   protected void spawnDispenseParticles(IBlockSource var1, EnumFacing var2) {
      var1.getWorld().playAuxSFX(2000, var1.getBlockPos(), this.func_82488_a(var2));
   }

   public static void doDispense(World var0, ItemStack var1, int var2, EnumFacing var3, IPosition var4) {
      double var5 = var4.getX();
      double var7 = var4.getY();
      double var9 = var4.getZ();
      if (var3.getAxis() == EnumFacing.Axis.Y) {
         var7 -= 0.125D;
      } else {
         var7 -= 0.15625D;
      }

      EntityItem var11 = new EntityItem(var0, var5, var7, var9, var1);
      double var12 = var0.rand.nextDouble() * 0.1D + 0.2D;
      var11.motionX = (double)var3.getFrontOffsetX() * var12;
      var11.motionY = 0.20000000298023224D;
      var11.motionZ = (double)var3.getFrontOffsetZ() * var12;
      var11.motionX += var0.rand.nextGaussian() * 0.007499999832361937D * (double)var2;
      var11.motionY += var0.rand.nextGaussian() * 0.007499999832361937D * (double)var2;
      var11.motionZ += var0.rand.nextGaussian() * 0.007499999832361937D * (double)var2;
      var0.spawnEntityInWorld(var11);
   }

   protected void playDispenseSound(IBlockSource var1) {
      var1.getWorld().playAuxSFX(1000, var1.getBlockPos(), 0);
   }

   private int func_82488_a(EnumFacing var1) {
      return var1.getFrontOffsetX() + 1 + (var1.getFrontOffsetZ() + 1) * 3;
   }

   protected ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
      EnumFacing var3 = BlockDispenser.getFacing(var1.getBlockMetadata());
      IPosition var4 = BlockDispenser.getDispensePosition(var1);
      ItemStack var5 = var2.splitStack(1);
      doDispense(var1.getWorld(), var5, 6, var3, var4);
      return var2;
   }

   public final ItemStack dispense(IBlockSource var1, ItemStack var2) {
      ItemStack var3 = this.dispenseStack(var1, var2);
      this.playDispenseSound(var1);
      this.spawnDispenseParticles(var1, BlockDispenser.getFacing(var1.getBlockMetadata()));
      return var3;
   }
}
