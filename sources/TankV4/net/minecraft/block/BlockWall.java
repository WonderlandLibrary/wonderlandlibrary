package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWall extends Block {
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool WEST = PropertyBool.create("west");
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockWall.EnumType.class);
   public static final PropertyBool UP = PropertyBool.create("up");
   public static final PropertyBool NORTH = PropertyBool.create("north");

   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return var1.withProperty(UP, !var2.isAirBlock(var3.up())).withProperty(NORTH, this.canConnectTo(var2, var3.north())).withProperty(EAST, this.canConnectTo(var2, var3.east())).withProperty(SOUTH, this.canConnectTo(var2, var3.south())).withProperty(WEST, this.canConnectTo(var2, var3.west()));
   }

   public boolean canConnectTo(IBlockAccess var1, BlockPos var2) {
      Block var3 = var1.getBlockState(var2).getBlock();
      return var3 == Blocks.barrier ? false : (var3 != this && !(var3 instanceof BlockFenceGate) ? (var3.blockMaterial.isOpaque() && var3.isFullCube() ? var3.blockMaterial != Material.gourd : false) : true);
   }

   public int damageDropped(IBlockState var1) {
      return ((BlockWall.EnumType)var1.getValue(VARIANT)).getMetadata();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{UP, NORTH, EAST, WEST, SOUTH, VARIANT});
   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      boolean var3 = this.canConnectTo(var1, var2.north());
      boolean var4 = this.canConnectTo(var1, var2.south());
      boolean var5 = this.canConnectTo(var1, var2.west());
      boolean var6 = this.canConnectTo(var1, var2.east());
      float var7 = 0.25F;
      float var8 = 0.75F;
      float var9 = 0.25F;
      float var10 = 0.75F;
      float var11 = 1.0F;
      if (var3) {
         var9 = 0.0F;
      }

      if (var4) {
         var10 = 1.0F;
      }

      if (var5) {
         var7 = 0.0F;
      }

      if (var6) {
         var8 = 1.0F;
      }

      if (var3 && var4 && !var5 && !var6) {
         var11 = 0.8125F;
         var7 = 0.3125F;
         var8 = 0.6875F;
      } else if (!var3 && !var4 && var5 && var6) {
         var11 = 0.8125F;
         var9 = 0.3125F;
         var10 = 0.6875F;
      }

      this.setBlockBounds(var7, 0.0F, var9, var8, var11, var10);
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, BlockWall.EnumType.byMetadata(var1));
   }

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      BlockWall.EnumType[] var7;
      int var6 = (var7 = BlockWall.EnumType.values()).length;

      for(int var5 = 0; var5 < var6; ++var5) {
         BlockWall.EnumType var4 = var7[var5];
         var3.add(new ItemStack(var1, 1, var4.getMetadata()));
      }

   }

   public BlockWall(Block var1) {
      super(var1.blockMaterial);
      this.setDefaultState(this.blockState.getBaseState().withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(VARIANT, BlockWall.EnumType.NORMAL));
      this.setHardness(var1.blockHardness);
      this.setResistance(var1.blockResistance / 3.0F);
      this.setStepSound(var1.stepSound);
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      this.setBlockBoundsBasedOnState(var1, var2);
      this.maxY = 1.5D;
      return super.getCollisionBoundingBox(var1, var2, var3);
   }

   public boolean isPassable(IBlockAccess var1, BlockPos var2) {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public int getMetaFromState(IBlockState var1) {
      return ((BlockWall.EnumType)var1.getValue(VARIANT)).getMetadata();
   }

   public boolean shouldSideBeRendered(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      return var3 == EnumFacing.DOWN ? super.shouldSideBeRendered(var1, var2, var3) : true;
   }

   public String getLocalizedName() {
      return StatCollector.translateToLocal(this.getUnlocalizedName() + "." + BlockWall.EnumType.NORMAL.getUnlocalizedName() + ".name");
   }

   public static enum EnumType implements IStringSerializable {
      private final int meta;
      MOSSY(1, "mossy_cobblestone", "mossy"),
      NORMAL(0, "cobblestone", "normal");

      private final String name;
      private static final BlockWall.EnumType[] META_LOOKUP = new BlockWall.EnumType[values().length];
      private String unlocalizedName;
      private static final BlockWall.EnumType[] ENUM$VALUES = new BlockWall.EnumType[]{NORMAL, MOSSY};

      public String getName() {
         return this.name;
      }

      public static BlockWall.EnumType byMetadata(int var0) {
         if (var0 < 0 || var0 >= META_LOOKUP.length) {
            var0 = 0;
         }

         return META_LOOKUP[var0];
      }

      public int getMetadata() {
         return this.meta;
      }

      static {
         BlockWall.EnumType[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            BlockWall.EnumType var0 = var3[var1];
            META_LOOKUP[var0.getMetadata()] = var0;
         }

      }

      private EnumType(int var3, String var4, String var5) {
         this.meta = var3;
         this.name = var4;
         this.unlocalizedName = var5;
      }

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }

      public String toString() {
         return this.name;
      }
   }
}
