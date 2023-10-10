package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFlowerPot extends BlockContainer {
   public static final PropertyInteger LEGACY_DATA = PropertyInteger.create("legacy_data", 0, 15);
   private static volatile int[] $SWITCH_TABLE$net$minecraft$block$BlockPlanks$EnumType;
   public static final PropertyEnum CONTENTS = PropertyEnum.create("contents", BlockFlowerPot.EnumFlowerType.class);
   private static volatile int[] $SWITCH_TABLE$net$minecraft$block$BlockFlower$EnumFlowerType;

   static int[] $SWITCH_TABLE$net$minecraft$block$BlockFlower$EnumFlowerType() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$block$BlockFlower$EnumFlowerType;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[BlockFlower.EnumFlowerType.values().length];

         try {
            var0[BlockFlower.EnumFlowerType.ALLIUM.ordinal()] = 4;
         } catch (NoSuchFieldError var10) {
         }

         try {
            var0[BlockFlower.EnumFlowerType.BLUE_ORCHID.ordinal()] = 3;
         } catch (NoSuchFieldError var9) {
         }

         try {
            var0[BlockFlower.EnumFlowerType.DANDELION.ordinal()] = 1;
         } catch (NoSuchFieldError var8) {
         }

         try {
            var0[BlockFlower.EnumFlowerType.HOUSTONIA.ordinal()] = 5;
         } catch (NoSuchFieldError var7) {
         }

         try {
            var0[BlockFlower.EnumFlowerType.ORANGE_TULIP.ordinal()] = 7;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[BlockFlower.EnumFlowerType.OXEYE_DAISY.ordinal()] = 10;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[BlockFlower.EnumFlowerType.PINK_TULIP.ordinal()] = 9;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[BlockFlower.EnumFlowerType.POPPY.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[BlockFlower.EnumFlowerType.RED_TULIP.ordinal()] = 6;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[BlockFlower.EnumFlowerType.WHITE_TULIP.ordinal()] = 8;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$block$BlockFlower$EnumFlowerType = var0;
         return var0;
      }
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.flower_pot;
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      Object var3 = null;
      int var4 = 0;
      switch(var2) {
      case 1:
         var3 = Blocks.red_flower;
         var4 = BlockFlower.EnumFlowerType.POPPY.getMeta();
         break;
      case 2:
         var3 = Blocks.yellow_flower;
         break;
      case 3:
         var3 = Blocks.sapling;
         var4 = BlockPlanks.EnumType.OAK.getMetadata();
         break;
      case 4:
         var3 = Blocks.sapling;
         var4 = BlockPlanks.EnumType.SPRUCE.getMetadata();
         break;
      case 5:
         var3 = Blocks.sapling;
         var4 = BlockPlanks.EnumType.BIRCH.getMetadata();
         break;
      case 6:
         var3 = Blocks.sapling;
         var4 = BlockPlanks.EnumType.JUNGLE.getMetadata();
         break;
      case 7:
         var3 = Blocks.red_mushroom;
         break;
      case 8:
         var3 = Blocks.brown_mushroom;
         break;
      case 9:
         var3 = Blocks.cactus;
         break;
      case 10:
         var3 = Blocks.deadbush;
         break;
      case 11:
         var3 = Blocks.tallgrass;
         var4 = BlockTallGrass.EnumType.FERN.getMeta();
         break;
      case 12:
         var3 = Blocks.sapling;
         var4 = BlockPlanks.EnumType.ACACIA.getMetadata();
         break;
      case 13:
         var3 = Blocks.sapling;
         var4 = BlockPlanks.EnumType.DARK_OAK.getMetadata();
      }

      return new TileEntityFlowerPot(Item.getItemFromBlock((Block)var3), var4);
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{CONTENTS, LEGACY_DATA});
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   public BlockFlowerPot() {
      super(Material.circuits);
      this.setDefaultState(this.blockState.getBaseState().withProperty(CONTENTS, BlockFlowerPot.EnumFlowerType.EMPTY).withProperty(LEGACY_DATA, 0));
      this.setBlockBoundsForItemRender();
   }

   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      BlockFlowerPot.EnumFlowerType var4 = BlockFlowerPot.EnumFlowerType.EMPTY;
      TileEntity var5 = var2.getTileEntity(var3);
      if (var5 instanceof TileEntityFlowerPot) {
         TileEntityFlowerPot var6 = (TileEntityFlowerPot)var5;
         Item var7 = var6.getFlowerPotItem();
         if (var7 instanceof ItemBlock) {
            int var8 = var6.getFlowerPotData();
            Block var9 = Block.getBlockFromItem(var7);
            if (var9 == Blocks.sapling) {
               switch($SWITCH_TABLE$net$minecraft$block$BlockPlanks$EnumType()[BlockPlanks.EnumType.byMetadata(var8).ordinal()]) {
               case 1:
                  var4 = BlockFlowerPot.EnumFlowerType.OAK_SAPLING;
                  break;
               case 2:
                  var4 = BlockFlowerPot.EnumFlowerType.SPRUCE_SAPLING;
                  break;
               case 3:
                  var4 = BlockFlowerPot.EnumFlowerType.BIRCH_SAPLING;
                  break;
               case 4:
                  var4 = BlockFlowerPot.EnumFlowerType.JUNGLE_SAPLING;
                  break;
               case 5:
                  var4 = BlockFlowerPot.EnumFlowerType.ACACIA_SAPLING;
                  break;
               case 6:
                  var4 = BlockFlowerPot.EnumFlowerType.DARK_OAK_SAPLING;
                  break;
               default:
                  var4 = BlockFlowerPot.EnumFlowerType.EMPTY;
               }
            } else if (var9 == Blocks.tallgrass) {
               switch(var8) {
               case 0:
                  var4 = BlockFlowerPot.EnumFlowerType.DEAD_BUSH;
                  break;
               case 1:
               default:
                  var4 = BlockFlowerPot.EnumFlowerType.EMPTY;
                  break;
               case 2:
                  var4 = BlockFlowerPot.EnumFlowerType.FERN;
               }
            } else if (var9 == Blocks.yellow_flower) {
               var4 = BlockFlowerPot.EnumFlowerType.DANDELION;
            } else if (var9 == Blocks.red_flower) {
               switch($SWITCH_TABLE$net$minecraft$block$BlockFlower$EnumFlowerType()[BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, var8).ordinal()]) {
               case 2:
                  var4 = BlockFlowerPot.EnumFlowerType.POPPY;
                  break;
               case 3:
                  var4 = BlockFlowerPot.EnumFlowerType.BLUE_ORCHID;
                  break;
               case 4:
                  var4 = BlockFlowerPot.EnumFlowerType.ALLIUM;
                  break;
               case 5:
                  var4 = BlockFlowerPot.EnumFlowerType.HOUSTONIA;
                  break;
               case 6:
                  var4 = BlockFlowerPot.EnumFlowerType.RED_TULIP;
                  break;
               case 7:
                  var4 = BlockFlowerPot.EnumFlowerType.ORANGE_TULIP;
                  break;
               case 8:
                  var4 = BlockFlowerPot.EnumFlowerType.WHITE_TULIP;
                  break;
               case 9:
                  var4 = BlockFlowerPot.EnumFlowerType.PINK_TULIP;
                  break;
               case 10:
                  var4 = BlockFlowerPot.EnumFlowerType.OXEYE_DAISY;
                  break;
               default:
                  var4 = BlockFlowerPot.EnumFlowerType.EMPTY;
               }
            } else if (var9 == Blocks.red_mushroom) {
               var4 = BlockFlowerPot.EnumFlowerType.MUSHROOM_RED;
            } else if (var9 == Blocks.brown_mushroom) {
               var4 = BlockFlowerPot.EnumFlowerType.MUSHROOM_BROWN;
            } else if (var9 == Blocks.deadbush) {
               var4 = BlockFlowerPot.EnumFlowerType.DEAD_BUSH;
            } else if (var9 == Blocks.cactus) {
               var4 = BlockFlowerPot.EnumFlowerType.CACTUS;
            }
         }
      }

      return var1.withProperty(CONTENTS, var4);
   }

   public int getRenderType() {
      return 3;
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (!World.doesBlockHaveSolidTopSurface(var1, var2.down())) {
         this.dropBlockAsItem(var1, var2, var3, 0);
         var1.setBlockToAir(var2);
      }

   }

   public boolean isFlowerPot() {
      return true;
   }

   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return super.canPlaceBlockAt(var1, var2) && World.doesBlockHaveSolidTopSurface(var1, var2.down());
   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      TileEntityFlowerPot var4 = this.getTileEntity(var1, var2);
      if (var4 != null && var4.getFlowerPotItem() != null) {
         spawnAsEntity(var1, var2, new ItemStack(var4.getFlowerPotItem(), 1, var4.getFlowerPotData()));
      }

      super.breakBlock(var1, var2, var3);
   }

   private TileEntityFlowerPot getTileEntity(World var1, BlockPos var2) {
      TileEntity var3 = var1.getTileEntity(var2);
      return var3 instanceof TileEntityFlowerPot ? (TileEntityFlowerPot)var3 : null;
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      ItemStack var9 = var4.inventory.getCurrentItem();
      if (var9 != null && var9.getItem() instanceof ItemBlock) {
         TileEntityFlowerPot var10 = this.getTileEntity(var1, var2);
         if (var10 == null) {
            return false;
         } else if (var10.getFlowerPotItem() != null) {
            return false;
         } else {
            Block var11 = Block.getBlockFromItem(var9.getItem());
            if (var9.getMetadata() != 0) {
               return false;
            } else {
               var10.setFlowerPotData(var9.getItem(), var9.getMetadata());
               var10.markDirty();
               var1.markBlockForUpdate(var2);
               var4.triggerAchievement(StatList.field_181736_T);
               if (!var4.capabilities.isCreativeMode && --var9.stackSize <= 0) {
                  var4.inventory.setInventorySlotContents(var4.inventory.currentItem, (ItemStack)null);
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   static int[] $SWITCH_TABLE$net$minecraft$block$BlockPlanks$EnumType() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$block$BlockPlanks$EnumType;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[BlockPlanks.EnumType.values().length];

         try {
            var0[BlockPlanks.EnumType.ACACIA.ordinal()] = 5;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[BlockPlanks.EnumType.BIRCH.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[BlockPlanks.EnumType.DARK_OAK.ordinal()] = 6;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[BlockPlanks.EnumType.JUNGLE.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[BlockPlanks.EnumType.OAK.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[BlockPlanks.EnumType.SPRUCE.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$block$BlockPlanks$EnumType = var0;
         return var0;
      }
   }

   public String getLocalizedName() {
      return StatCollector.translateToLocal("item.flowerPot.name");
   }

   public void setBlockBoundsForItemRender() {
      float var1 = 0.375F;
      float var2 = var1 / 2.0F;
      this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, var1, 0.5F + var2);
   }

   public void onBlockHarvested(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4) {
      super.onBlockHarvested(var1, var2, var3, var4);
      if (var4.capabilities.isCreativeMode) {
         TileEntityFlowerPot var5 = this.getTileEntity(var1, var2);
         if (var5 != null) {
            var5.setFlowerPotData((Item)null, 0);
         }
      }

   }

   public Item getItem(World var1, BlockPos var2) {
      TileEntityFlowerPot var3 = this.getTileEntity(var1, var2);
      return var3 != null && var3.getFlowerPotItem() != null ? var3.getFlowerPotItem() : Items.flower_pot;
   }

   public int colorMultiplier(IBlockAccess var1, BlockPos var2, int var3) {
      TileEntity var4 = var1.getTileEntity(var2);
      if (var4 instanceof TileEntityFlowerPot) {
         Item var5 = ((TileEntityFlowerPot)var4).getFlowerPotItem();
         if (var5 instanceof ItemBlock) {
            return Block.getBlockFromItem(var5).colorMultiplier(var1, var2, var3);
         }
      }

      return 16777215;
   }

   public int getMetaFromState(IBlockState var1) {
      return (Integer)var1.getValue(LEGACY_DATA);
   }

   public int getDamageValue(World var1, BlockPos var2) {
      TileEntityFlowerPot var3 = this.getTileEntity(var1, var2);
      return var3 != null && var3.getFlowerPotItem() != null ? var3.getFlowerPotData() : 0;
   }

   public static enum EnumFlowerType implements IStringSerializable {
      OAK_SAPLING("oak_sapling");

      private static final BlockFlowerPot.EnumFlowerType[] ENUM$VALUES = new BlockFlowerPot.EnumFlowerType[]{EMPTY, POPPY, BLUE_ORCHID, ALLIUM, HOUSTONIA, RED_TULIP, ORANGE_TULIP, WHITE_TULIP, PINK_TULIP, OXEYE_DAISY, DANDELION, OAK_SAPLING, SPRUCE_SAPLING, BIRCH_SAPLING, JUNGLE_SAPLING, ACACIA_SAPLING, DARK_OAK_SAPLING, MUSHROOM_RED, MUSHROOM_BROWN, DEAD_BUSH, FERN, CACTUS};
      BLUE_ORCHID("blue_orchid"),
      CACTUS("cactus"),
      HOUSTONIA("houstonia"),
      MUSHROOM_BROWN("mushroom_brown"),
      ALLIUM("allium"),
      POPPY("rose"),
      EMPTY("empty"),
      RED_TULIP("red_tulip"),
      JUNGLE_SAPLING("jungle_sapling"),
      DEAD_BUSH("dead_bush"),
      ACACIA_SAPLING("acacia_sapling"),
      DARK_OAK_SAPLING("dark_oak_sapling"),
      ORANGE_TULIP("orange_tulip"),
      FERN("fern"),
      OXEYE_DAISY("oxeye_daisy"),
      PINK_TULIP("pink_tulip"),
      DANDELION("dandelion"),
      MUSHROOM_RED("mushroom_red"),
      WHITE_TULIP("white_tulip"),
      SPRUCE_SAPLING("spruce_sapling"),
      BIRCH_SAPLING("birch_sapling");

      private final String name;

      public String toString() {
         return this.name;
      }

      private EnumFlowerType(String var3) {
         this.name = var3;
      }

      public String getName() {
         return this.name;
      }
   }
}
