package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPane extends Block {
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool WEST = PropertyBool.create("west");
   private final boolean canDrop;
   public static final PropertyBool NORTH = PropertyBool.create("north");

   protected boolean canSilkHarvest() {
      return true;
   }

   public boolean shouldSideBeRendered(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      return var1.getBlockState(var2).getBlock() == this ? false : super.shouldSideBeRendered(var1, var2, var3);
   }

   protected BlockPane(Material var1, boolean var2) {
      super(var1);
      this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false));
      this.canDrop = var2;
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{NORTH, EAST, WEST, SOUTH});
   }

   public void setBlockBoundsForItemRender() {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return var1.withProperty(NORTH, this.canPaneConnectToBlock(var2.getBlockState(var3.north()).getBlock())).withProperty(SOUTH, this.canPaneConnectToBlock(var2.getBlockState(var3.south()).getBlock())).withProperty(WEST, this.canPaneConnectToBlock(var2.getBlockState(var3.west()).getBlock())).withProperty(EAST, this.canPaneConnectToBlock(var2.getBlockState(var3.east()).getBlock()));
   }

   public final boolean canPaneConnectToBlock(Block var1) {
      return var1.isFullBlock() || var1 == this || var1 == Blocks.glass || var1 == Blocks.stained_glass || var1 == Blocks.stained_glass_pane || var1 instanceof BlockPane;
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT_MIPPED;
   }

   public int getMetaFromState(IBlockState var1) {
      return 0;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      float var3 = 0.4375F;
      float var4 = 0.5625F;
      float var5 = 0.4375F;
      float var6 = 0.5625F;
      boolean var7 = this.canPaneConnectToBlock(var1.getBlockState(var2.north()).getBlock());
      boolean var8 = this.canPaneConnectToBlock(var1.getBlockState(var2.south()).getBlock());
      boolean var9 = this.canPaneConnectToBlock(var1.getBlockState(var2.west()).getBlock());
      boolean var10 = this.canPaneConnectToBlock(var1.getBlockState(var2.east()).getBlock());
      if ((!var9 || !var10) && (var9 || var10 || var7 || var8)) {
         if (var9) {
            var3 = 0.0F;
         } else if (var10) {
            var4 = 1.0F;
         }
      } else {
         var3 = 0.0F;
         var4 = 1.0F;
      }

      if ((!var7 || !var8) && (var9 || var10 || var7 || var8)) {
         if (var7) {
            var5 = 0.0F;
         } else if (var8) {
            var6 = 1.0F;
         }
      } else {
         var5 = 0.0F;
         var6 = 1.0F;
      }

      this.setBlockBounds(var3, 0.0F, var5, var4, 1.0F, var6);
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return !this.canDrop ? null : super.getItemDropped(var1, var2, var3);
   }

   public void addCollisionBoxesToList(World var1, BlockPos var2, IBlockState var3, AxisAlignedBB var4, List var5, Entity var6) {
      boolean var7 = this.canPaneConnectToBlock(var1.getBlockState(var2.north()).getBlock());
      boolean var8 = this.canPaneConnectToBlock(var1.getBlockState(var2.south()).getBlock());
      boolean var9 = this.canPaneConnectToBlock(var1.getBlockState(var2.west()).getBlock());
      boolean var10 = this.canPaneConnectToBlock(var1.getBlockState(var2.east()).getBlock());
      if ((!var9 || !var10) && (var9 || var10 || var7 || var8)) {
         if (var9) {
            this.setBlockBounds(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
            super.addCollisionBoxesToList(var1, var2, var3, var4, var5, var6);
         } else if (var10) {
            this.setBlockBounds(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
            super.addCollisionBoxesToList(var1, var2, var3, var4, var5, var6);
         }
      } else {
         this.setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
         super.addCollisionBoxesToList(var1, var2, var3, var4, var5, var6);
      }

      if ((!var7 || !var8) && (var9 || var10 || var7 || var8)) {
         if (var7) {
            this.setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
            super.addCollisionBoxesToList(var1, var2, var3, var4, var5, var6);
         } else if (var8) {
            this.setBlockBounds(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
            super.addCollisionBoxesToList(var1, var2, var3, var4, var5, var6);
         }
      } else {
         this.setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
         super.addCollisionBoxesToList(var1, var2, var3, var4, var5, var6);
      }

   }

   public boolean isFullCube() {
      return false;
   }
}
