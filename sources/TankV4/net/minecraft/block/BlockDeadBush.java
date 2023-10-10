package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockDeadBush extends BlockBush {
   protected BlockDeadBush() {
      super(Material.vine);
      float var1 = 0.4F;
      this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.8F, 0.5F + var1);
   }

   public MapColor getMapColor(IBlockState var1) {
      return MapColor.woodColor;
   }

   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, TileEntity var5) {
      if (!var1.isRemote && var2.getCurrentEquippedItem() != null && var2.getCurrentEquippedItem().getItem() == Items.shears) {
         var2.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
         spawnAsEntity(var1, var3, new ItemStack(Blocks.deadbush, 1, 0));
      } else {
         super.harvestBlock(var1, var2, var3, var4, var5);
      }

   }

   public boolean isReplaceable(World var1, BlockPos var2) {
      return true;
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return null;
   }

   protected boolean canPlaceBlockOn(Block var1) {
      return var1 == Blocks.sand || var1 == Blocks.hardened_clay || var1 == Blocks.stained_hardened_clay || var1 == Blocks.dirt;
   }
}
