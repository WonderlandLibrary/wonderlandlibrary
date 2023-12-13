package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSnow extends ItemBlock {
   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var1.stackSize == 0) {
         return false;
      } else if (!var2.canPlayerEdit(var4, var5, var1)) {
         return false;
      } else {
         IBlockState var9 = var3.getBlockState(var4);
         Block var10 = var9.getBlock();
         BlockPos var11 = var4;
         if ((var5 != EnumFacing.UP || var10 != this.block) && !var10.isReplaceable(var3, var4)) {
            var11 = var4.offset(var5);
            var9 = var3.getBlockState(var11);
            var10 = var9.getBlock();
         }

         if (var10 == this.block) {
            int var12 = (Integer)var9.getValue(BlockSnow.LAYERS);
            if (var12 <= 7) {
               IBlockState var13 = var9.withProperty(BlockSnow.LAYERS, var12 + 1);
               AxisAlignedBB var14 = this.block.getCollisionBoundingBox(var3, var11, var13);
               if (var14 != null && var3.checkNoEntityCollision(var14) && var3.setBlockState(var11, var13, 2)) {
                  var3.playSoundEffect((double)((float)var11.getX() + 0.5F), (double)((float)var11.getY() + 0.5F), (double)((float)var11.getZ() + 0.5F), this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
                  --var1.stackSize;
                  return true;
               }
            }
         }

         return super.onItemUse(var1, var2, var3, var11, var5, var6, var7, var8);
      }
   }

   public ItemSnow(Block var1) {
      super(var1);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int var1) {
      return var1;
   }
}
