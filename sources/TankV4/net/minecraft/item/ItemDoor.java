package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemDoor extends Item {
   private Block block;

   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var5 != EnumFacing.UP) {
         return false;
      } else {
         IBlockState var9 = var3.getBlockState(var4);
         Block var10 = var9.getBlock();
         if (!var10.isReplaceable(var3, var4)) {
            var4 = var4.offset(var5);
         }

         if (!var2.canPlayerEdit(var4, var5, var1)) {
            return false;
         } else if (!this.block.canPlaceBlockAt(var3, var4)) {
            return false;
         } else {
            placeDoor(var3, var4, EnumFacing.fromAngle((double)var2.rotationYaw), this.block);
            --var1.stackSize;
            return true;
         }
      }
   }

   public ItemDoor(Block var1) {
      this.block = var1;
      this.setCreativeTab(CreativeTabs.tabRedstone);
   }

   public static void placeDoor(World var0, BlockPos var1, EnumFacing var2, Block var3) {
      BlockPos var4 = var1.offset(var2.rotateY());
      BlockPos var5 = var1.offset(var2.rotateYCCW());
      int var6 = (var0.getBlockState(var5).getBlock().isNormalCube() ? 1 : 0) + (var0.getBlockState(var5.up()).getBlock().isNormalCube() ? 1 : 0);
      int var7 = (var0.getBlockState(var4).getBlock().isNormalCube() ? 1 : 0) + (var0.getBlockState(var4.up()).getBlock().isNormalCube() ? 1 : 0);
      boolean var8 = var0.getBlockState(var5).getBlock() == var3 || var0.getBlockState(var5.up()).getBlock() == var3;
      boolean var9 = var0.getBlockState(var4).getBlock() == var3 || var0.getBlockState(var4.up()).getBlock() == var3;
      boolean var10 = false;
      if (var8 && !var9 || var7 > var6) {
         var10 = true;
      }

      BlockPos var11 = var1.up();
      IBlockState var12 = var3.getDefaultState().withProperty(BlockDoor.FACING, var2).withProperty(BlockDoor.HINGE, var10 ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT);
      var0.setBlockState(var1, var12.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 2);
      var0.setBlockState(var11, var12.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);
      var0.notifyNeighborsOfStateChange(var1, var3);
      var0.notifyNeighborsOfStateChange(var11, var3);
   }
}
