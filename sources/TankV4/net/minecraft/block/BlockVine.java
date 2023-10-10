package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVine extends Block {
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool NORTH = PropertyBool.create("north");
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
   public static final PropertyBool[] ALL_FACES;
   public static final PropertyBool WEST = PropertyBool.create("west");
   public static final PropertyBool UP = PropertyBool.create("up");
   public static final PropertyBool SOUTH = PropertyBool.create("south");

   public boolean isFullCube() {
      return false;
   }

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      IBlockState var9 = this.getDefaultState().withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false);
      return var3.getAxis().isHorizontal() ? var9.withProperty(getPropertyFor(var3.getOpposite()), true) : var9;
   }

   public int colorMultiplier(IBlockAccess var1, BlockPos var2, int var3) {
      return var1.getBiomeGenForCoords(var2).getFoliageColorAtPos(var2);
   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!var1.isRemote && var1.rand.nextInt(4) == 0) {
         byte var5 = 4;
         int var6 = 5;
         boolean var7 = false;

         label177:
         for(int var8 = -var5; var8 <= var5; ++var8) {
            for(int var9 = -var5; var9 <= var5; ++var9) {
               for(int var10 = -1; var10 <= 1; ++var10) {
                  if (var1.getBlockState(var2.add(var8, var10, var9)).getBlock() == this) {
                     --var6;
                     if (var6 <= 0) {
                        var7 = true;
                        break label177;
                     }
                  }
               }
            }
         }

         EnumFacing var18 = EnumFacing.random(var4);
         BlockPos var19 = var2.up();
         if (var18 == EnumFacing.UP && var2.getY() < 255 && var1.isAirBlock(var19)) {
            if (!var7) {
               IBlockState var21 = var3;
               Iterator var25 = EnumFacing.Plane.HORIZONTAL.iterator();

               while(true) {
                  Object var23;
                  do {
                     if (!var25.hasNext()) {
                        if ((Boolean)var21.getValue(NORTH) || (Boolean)var21.getValue(EAST) || (Boolean)var21.getValue(SOUTH) || (Boolean)var21.getValue(WEST)) {
                           var1.setBlockState(var19, var21, 2);
                        }

                        return;
                     }

                     var23 = var25.next();
                  } while(!var4.nextBoolean() && var1.getBlockState(var19.offset((EnumFacing)var23)).getBlock() == false);

                  var21 = var21.withProperty(getPropertyFor((EnumFacing)var23), false);
               }
            }
         } else {
            BlockPos var20;
            if (var18.getAxis().isHorizontal() && !(Boolean)var3.getValue(getPropertyFor(var18))) {
               if (!var7) {
                  var20 = var2.offset(var18);
                  Block var22 = var1.getBlockState(var20).getBlock();
                  if (var22.blockMaterial == Material.air) {
                     EnumFacing var24 = var18.rotateY();
                     EnumFacing var26 = var18.rotateYCCW();
                     boolean var27 = (Boolean)var3.getValue(getPropertyFor(var24));
                     boolean var28 = (Boolean)var3.getValue(getPropertyFor(var26));
                     BlockPos var29 = var20.offset(var24);
                     BlockPos var17 = var20.offset(var26);
                     if (var27 && var1.getBlockState(var29).getBlock() != false) {
                        var1.setBlockState(var20, this.getDefaultState().withProperty(getPropertyFor(var24), true), 2);
                     } else if (var28 && var1.getBlockState(var17).getBlock() != false) {
                        var1.setBlockState(var20, this.getDefaultState().withProperty(getPropertyFor(var26), true), 2);
                     } else if (var27 && var1.isAirBlock(var29) && var1.getBlockState(var2.offset(var24)).getBlock() != false) {
                        var1.setBlockState(var29, this.getDefaultState().withProperty(getPropertyFor(var18.getOpposite()), true), 2);
                     } else if (var28 && var1.isAirBlock(var17) && var1.getBlockState(var2.offset(var26)).getBlock() != false) {
                        var1.setBlockState(var17, this.getDefaultState().withProperty(getPropertyFor(var18.getOpposite()), true), 2);
                     } else if (var1.getBlockState(var20.up()).getBlock() != false) {
                        var1.setBlockState(var20, this.getDefaultState(), 2);
                     }
                  } else if (var22.blockMaterial.isOpaque() && var22.isFullCube()) {
                     var1.setBlockState(var2, var3.withProperty(getPropertyFor(var18), true), 2);
                  }
               }
            } else if (var2.getY() > 1) {
               var20 = var2.down();
               IBlockState var11 = var1.getBlockState(var20);
               Block var12 = var11.getBlock();
               IBlockState var13;
               Object var14;
               Iterator var15;
               if (var12.blockMaterial == Material.air) {
                  var13 = var3;
                  var15 = EnumFacing.Plane.HORIZONTAL.iterator();

                  while(var15.hasNext()) {
                     var14 = var15.next();
                     if (var4.nextBoolean()) {
                        var13 = var13.withProperty(getPropertyFor((EnumFacing)var14), false);
                     }
                  }

                  if ((Boolean)var13.getValue(NORTH) || (Boolean)var13.getValue(EAST) || (Boolean)var13.getValue(SOUTH) || (Boolean)var13.getValue(WEST)) {
                     var1.setBlockState(var20, var13, 2);
                  }
               } else if (var12 == this) {
                  var13 = var11;
                  var15 = EnumFacing.Plane.HORIZONTAL.iterator();

                  while(var15.hasNext()) {
                     var14 = var15.next();
                     PropertyBool var16 = getPropertyFor((EnumFacing)var14);
                     if (var4.nextBoolean() && (Boolean)var3.getValue(var16)) {
                        var13 = var13.withProperty(var16, true);
                     }
                  }

                  if ((Boolean)var13.getValue(NORTH) || (Boolean)var13.getValue(EAST) || (Boolean)var13.getValue(SOUTH) || (Boolean)var13.getValue(WEST)) {
                     var1.setBlockState(var20, var13, 2);
                  }
               }
            }
         }
      }

   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      float var3 = 0.0625F;
      float var4 = 1.0F;
      float var5 = 1.0F;
      float var6 = 1.0F;
      float var7 = 0.0F;
      float var8 = 0.0F;
      float var9 = 0.0F;
      boolean var10 = false;
      if ((Boolean)var1.getBlockState(var2).getValue(WEST)) {
         var7 = Math.max(var7, 0.0625F);
         var4 = 0.0F;
         var5 = 0.0F;
         var8 = 1.0F;
         var6 = 0.0F;
         var9 = 1.0F;
         var10 = true;
      }

      if ((Boolean)var1.getBlockState(var2).getValue(EAST)) {
         var4 = Math.min(var4, 0.9375F);
         var7 = 1.0F;
         var5 = 0.0F;
         var8 = 1.0F;
         var6 = 0.0F;
         var9 = 1.0F;
         var10 = true;
      }

      if ((Boolean)var1.getBlockState(var2).getValue(NORTH)) {
         var9 = Math.max(var9, 0.0625F);
         var6 = 0.0F;
         var4 = 0.0F;
         var7 = 1.0F;
         var5 = 0.0F;
         var8 = 1.0F;
         var10 = true;
      }

      if ((Boolean)var1.getBlockState(var2).getValue(SOUTH)) {
         var6 = Math.min(var6, 0.9375F);
         var9 = 1.0F;
         var4 = 0.0F;
         var7 = 1.0F;
         var5 = 0.0F;
         var8 = 1.0F;
         var10 = true;
      }

      if (!var10 && var1.getBlockState(var2.up()).getBlock() != false) {
         var5 = Math.min(var5, 0.9375F);
         var8 = 1.0F;
         var4 = 0.0F;
         var7 = 1.0F;
         var6 = 0.0F;
         var9 = 1.0F;
      }

      this.setBlockBounds(var4, var5, var6, var7, var8, var9);
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (!var1.isRemote && var3 != false) {
         this.dropBlockAsItem(var1, var2, var3, 0);
         var1.setBlockToAir(var2);
      }

   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   public int getBlockColor() {
      return ColorizerFoliage.getFoliageColorBasic();
   }

   public int quantityDropped(Random var1) {
      return 0;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public static PropertyBool getPropertyFor(EnumFacing var0) {
      switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[var0.ordinal()]) {
      case 2:
         return UP;
      case 3:
         return NORTH;
      case 4:
         return SOUTH;
      case 5:
         return WEST;
      case 6:
         return EAST;
      default:
         throw new IllegalArgumentException(var0 + " is an invalid choice");
      }
   }

   public void setBlockBoundsForItemRender() {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public BlockVine() {
      super(Material.vine);
      this.setDefaultState(this.blockState.getBaseState().withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   static {
      ALL_FACES = new PropertyBool[]{UP, NORTH, SOUTH, WEST, EAST};
   }

   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, TileEntity var5) {
      if (!var1.isRemote && var2.getCurrentEquippedItem() != null && var2.getCurrentEquippedItem().getItem() == Items.shears) {
         var2.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
         spawnAsEntity(var1, var3, new ItemStack(Blocks.vine, 1, 0));
      } else {
         super.harvestBlock(var1, var2, var3, var4, var5);
      }

   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{UP, NORTH, EAST, SOUTH, WEST});
   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      return null;
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return null;
   }

   public boolean isReplaceable(World var1, BlockPos var2) {
      return true;
   }

   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return var1.withProperty(UP, var2.getBlockState(var3.up()).getBlock().isBlockNormalCube());
   }

   public int getMetaFromState(IBlockState var1) {
      int var2 = 0;
      if ((Boolean)var1.getValue(SOUTH)) {
         var2 |= 1;
      }

      if ((Boolean)var1.getValue(WEST)) {
         var2 |= 2;
      }

      if ((Boolean)var1.getValue(NORTH)) {
         var2 |= 4;
      }

      if ((Boolean)var1.getValue(EAST)) {
         var2 |= 8;
      }

      return var2;
   }

   public int getRenderColor(IBlockState var1) {
      return ColorizerFoliage.getFoliageColorBasic();
   }

   static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[EnumFacing.values().length];

         try {
            var0[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$util$EnumFacing = var0;
         return var0;
      }
   }

   public static int getNumGrownFaces(IBlockState var0) {
      int var1 = 0;
      PropertyBool[] var5;
      int var4 = (var5 = ALL_FACES).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         PropertyBool var2 = var5[var3];
         if ((Boolean)var0.getValue(var2)) {
            ++var1;
         }
      }

      return var1;
   }

   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3) {
      switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[var3.ordinal()]) {
      case 2:
         return this.canPlaceOn(var1.getBlockState(var2.up()).getBlock());
      case 3:
      case 4:
      case 5:
      case 6:
         return this.canPlaceOn(var1.getBlockState(var2.offset(var3.getOpposite())).getBlock());
      default:
         return false;
      }
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(SOUTH, (var1 & 1) > 0).withProperty(WEST, (var1 & 2) > 0).withProperty(NORTH, (var1 & 4) > 0).withProperty(EAST, (var1 & 8) > 0);
   }
}
