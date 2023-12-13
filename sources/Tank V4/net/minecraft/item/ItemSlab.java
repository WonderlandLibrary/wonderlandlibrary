package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSlab extends ItemBlock {
   private final BlockSlab singleSlab;
   private final BlockSlab doubleSlab;

   public ItemSlab(Block var1, BlockSlab var2, BlockSlab var3) {
      super(var1);
      this.singleSlab = var2;
      this.doubleSlab = var3;
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public String getUnlocalizedName(ItemStack var1) {
      return this.singleSlab.getUnlocalizedName(var1.getMetadata());
   }

   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var1.stackSize == 0) {
         return false;
      } else if (!var2.canPlayerEdit(var4.offset(var5), var5, var1)) {
         return false;
      } else {
         Object var9 = this.singleSlab.getVariant(var1);
         IBlockState var10 = var3.getBlockState(var4);
         if (var10.getBlock() == this.singleSlab) {
            IProperty var11 = this.singleSlab.getVariantProperty();
            Comparable var12 = var10.getValue(var11);
            BlockSlab.EnumBlockHalf var13 = (BlockSlab.EnumBlockHalf)var10.getValue(BlockSlab.HALF);
            if ((var5 == EnumFacing.UP && var13 == BlockSlab.EnumBlockHalf.BOTTOM || var5 == EnumFacing.DOWN && var13 == BlockSlab.EnumBlockHalf.TOP) && var12 == var9) {
               IBlockState var14 = this.doubleSlab.getDefaultState().withProperty(var11, var12);
               if (var3.checkNoEntityCollision(this.doubleSlab.getCollisionBoundingBox(var3, var4, var14)) && var3.setBlockState(var4, var14, 3)) {
                  var3.playSoundEffect((double)((float)var4.getX() + 0.5F), (double)((float)var4.getY() + 0.5F), (double)((float)var4.getZ() + 0.5F), this.doubleSlab.stepSound.getPlaceSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getFrequency() * 0.8F);
                  --var1.stackSize;
               }

               return true;
            }
         }

         var4.offset(var5);
         return var9 != false ? true : super.onItemUse(var1, var2, var3, var4, var5, var6, var7, var8);
      }
   }

   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3, EntityPlayer var4, ItemStack var5) {
      BlockPos var6 = var2;
      IProperty var7 = this.singleSlab.getVariantProperty();
      Object var8 = this.singleSlab.getVariant(var5);
      IBlockState var9 = var1.getBlockState(var2);
      if (var9.getBlock() == this.singleSlab) {
         boolean var10 = var9.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;
         if ((var3 == EnumFacing.UP && !var10 || var3 == EnumFacing.DOWN && var10) && var8 == var9.getValue(var7)) {
            return true;
         }
      }

      var2 = var2.offset(var3);
      IBlockState var11 = var1.getBlockState(var2);
      return var11.getBlock() == this.singleSlab && var8 == var11.getValue(var7) ? true : super.canPlaceBlockOnSide(var1, var6, var3, var4, var5);
   }

   public int getMetadata(int var1) {
      return var1;
   }
}
