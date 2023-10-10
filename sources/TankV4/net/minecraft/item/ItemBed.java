package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemBed extends Item {
   public ItemBed() {
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var3.isRemote) {
         return true;
      } else if (var5 != EnumFacing.UP) {
         return false;
      } else {
         IBlockState var9 = var3.getBlockState(var4);
         Block var10 = var9.getBlock();
         boolean var11 = var10.isReplaceable(var3, var4);
         if (!var11) {
            var4 = var4.up();
         }

         int var12 = MathHelper.floor_double((double)(var2.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
         EnumFacing var13 = EnumFacing.getHorizontal(var12);
         BlockPos var14 = var4.offset(var13);
         if (var2.canPlayerEdit(var4, var5, var1) && var2.canPlayerEdit(var14, var5, var1)) {
            boolean var15 = var3.getBlockState(var14).getBlock().isReplaceable(var3, var14);
            boolean var16 = var11 || var3.isAirBlock(var4);
            boolean var17 = var15 || var3.isAirBlock(var14);
            if (var16 && var17 && World.doesBlockHaveSolidTopSurface(var3, var4.down()) && World.doesBlockHaveSolidTopSurface(var3, var14.down())) {
               IBlockState var18 = Blocks.bed.getDefaultState().withProperty(BlockBed.OCCUPIED, false).withProperty(BlockBed.FACING, var13).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
               if (var3.setBlockState(var4, var18, 3)) {
                  IBlockState var19 = var18.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
                  var3.setBlockState(var14, var19, 3);
               }

               --var1.stackSize;
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }
}
