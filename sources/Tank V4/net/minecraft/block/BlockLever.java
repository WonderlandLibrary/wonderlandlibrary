package net.minecraft.block;

import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLever extends Block {
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
   public static final PropertyEnum FACING = PropertyEnum.create("facing", BlockLever.EnumOrientation.class);
   private static volatile int[] $SWITCH_TABLE$net$minecraft$block$BlockLever$EnumOrientation;
   public static final PropertyBool POWERED = PropertyBool.create("powered");

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING, POWERED});
   }

   public int getMetaFromState(IBlockState var1) {
      byte var2 = 0;
      int var3 = var2 | ((BlockLever.EnumOrientation)var1.getValue(FACING)).getMetadata();
      if ((Boolean)var1.getValue(POWERED)) {
         var3 |= 8;
      }

      return var3;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      float var3 = 0.1875F;
      switch($SWITCH_TABLE$net$minecraft$block$BlockLever$EnumOrientation()[((BlockLever.EnumOrientation)var1.getBlockState(var2).getValue(FACING)).ordinal()]) {
      case 1:
      case 8:
         var3 = 0.25F;
         this.setBlockBounds(0.5F - var3, 0.4F, 0.5F - var3, 0.5F + var3, 1.0F, 0.5F + var3);
         break;
      case 2:
         this.setBlockBounds(0.0F, 0.2F, 0.5F - var3, var3 * 2.0F, 0.8F, 0.5F + var3);
         break;
      case 3:
         this.setBlockBounds(1.0F - var3 * 2.0F, 0.2F, 0.5F - var3, 1.0F, 0.8F, 0.5F + var3);
         break;
      case 4:
         this.setBlockBounds(0.5F - var3, 0.2F, 0.0F, 0.5F + var3, 0.8F, var3 * 2.0F);
         break;
      case 5:
         this.setBlockBounds(0.5F - var3, 0.2F, 1.0F - var3 * 2.0F, 0.5F + var3, 0.8F, 1.0F);
         break;
      case 6:
      case 7:
         var3 = 0.25F;
         this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.6F, 0.5F + var3);
      }

   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (var2 != var3 && !func_181090_a(var1, var2, ((BlockLever.EnumOrientation)var3.getValue(FACING)).getFacing().getOpposite())) {
         this.dropBlockAsItem(var1, var2, var3, 0);
         var1.setBlockToAir(var2);
      }

   }

   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var1.isRemote) {
         return true;
      } else {
         var3 = var3.cycleProperty(POWERED);
         var1.setBlockState(var2, var3, 3);
         var1.playSoundEffect((double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D, "random.click", 0.3F, (Boolean)var3.getValue(POWERED) ? 0.6F : 0.5F);
         var1.notifyNeighborsOfStateChange(var2, this);
         EnumFacing var9 = ((BlockLever.EnumOrientation)var3.getValue(FACING)).getFacing();
         var1.notifyNeighborsOfStateChange(var2.offset(var9.getOpposite()), this);
         return true;
      }
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, BlockLever.EnumOrientation.byMetadata(var1 & 7)).withProperty(POWERED, (var1 & 8) > 0);
   }

   public boolean canProvidePower() {
      return true;
   }

   public int getWeakPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      return (Boolean)var3.getValue(POWERED) ? 15 : 0;
   }

   protected static boolean func_181090_a(World var0, BlockPos var1, EnumFacing var2) {
      return BlockButton.func_181088_a(var0, var1, var2);
   }

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      IBlockState var9 = this.getDefaultState().withProperty(POWERED, false);
      if (func_181090_a(var1, var2, var3.getOpposite())) {
         return var9.withProperty(FACING, BlockLever.EnumOrientation.forFacings(var3, var8.getHorizontalFacing()));
      } else {
         Iterator var11 = EnumFacing.Plane.HORIZONTAL.iterator();

         Object var10;
         do {
            if (!var11.hasNext()) {
               if (World.doesBlockHaveSolidTopSurface(var1, var2.down())) {
                  return var9.withProperty(FACING, BlockLever.EnumOrientation.forFacings(EnumFacing.UP, var8.getHorizontalFacing()));
               }

               return var9;
            }

            var10 = var11.next();
         } while(var10 == var3 || !func_181090_a(var1, var2, ((EnumFacing)var10).getOpposite()));

         return var9.withProperty(FACING, BlockLever.EnumOrientation.forFacings((EnumFacing)var10, var8.getHorizontalFacing()));
      }
   }

   public boolean isFullCube() {
      return false;
   }

   public static int getMetadataForFacing(EnumFacing var0) {
      switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[var0.ordinal()]) {
      case 1:
         return 0;
      case 2:
         return 5;
      case 3:
         return 4;
      case 4:
         return 3;
      case 5:
         return 2;
      case 6:
         return 1;
      default:
         return -1;
      }
   }

   public int getStrongPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      return !(Boolean)var3.getValue(POWERED) ? 0 : (((BlockLever.EnumOrientation)var3.getValue(FACING)).getFacing() == var4 ? 15 : 0);
   }

   protected BlockLever() {
      super(Material.circuits);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, BlockLever.EnumOrientation.NORTH).withProperty(POWERED, false));
      this.setCreativeTab(CreativeTabs.tabRedstone);
   }

   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3) {
      return func_181090_a(var1, var2, var3.getOpposite());
   }

   static int[] $SWITCH_TABLE$net$minecraft$block$BlockLever$EnumOrientation() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$block$BlockLever$EnumOrientation;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[BlockLever.EnumOrientation.values().length];

         try {
            var0[BlockLever.EnumOrientation.DOWN_X.ordinal()] = 1;
         } catch (NoSuchFieldError var8) {
         }

         try {
            var0[BlockLever.EnumOrientation.DOWN_Z.ordinal()] = 8;
         } catch (NoSuchFieldError var7) {
         }

         try {
            var0[BlockLever.EnumOrientation.EAST.ordinal()] = 2;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[BlockLever.EnumOrientation.NORTH.ordinal()] = 5;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[BlockLever.EnumOrientation.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[BlockLever.EnumOrientation.UP_X.ordinal()] = 7;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[BlockLever.EnumOrientation.UP_Z.ordinal()] = 6;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[BlockLever.EnumOrientation.WEST.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$block$BlockLever$EnumOrientation = var0;
         return var0;
      }
   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      if ((Boolean)var3.getValue(POWERED)) {
         var1.notifyNeighborsOfStateChange(var2, this);
         EnumFacing var4 = ((BlockLever.EnumOrientation)var3.getValue(FACING)).getFacing();
         var1.notifyNeighborsOfStateChange(var2.offset(var4.getOpposite()), this);
      }

      super.breakBlock(var1, var2, var3);
   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      return null;
   }

   static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[EnumFacing.values().length];

         try {
            var0[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var7) {
         }

         try {
            var0[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
         }

         $SWITCH_TABLE$net$minecraft$util$EnumFacing = var0;
         return var0;
      }
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public static enum EnumOrientation implements IStringSerializable {
      private static final BlockLever.EnumOrientation[] META_LOOKUP = new BlockLever.EnumOrientation[values().length];
      private final int meta;
      private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis;
      private final String name;
      DOWN_X(0, "down_x", EnumFacing.DOWN),
      SOUTH(3, "south", EnumFacing.SOUTH),
      DOWN_Z(7, "down_z", EnumFacing.DOWN),
      UP_Z(5, "up_z", EnumFacing.UP);

      private static final BlockLever.EnumOrientation[] ENUM$VALUES = new BlockLever.EnumOrientation[]{DOWN_X, EAST, WEST, SOUTH, NORTH, UP_Z, UP_X, DOWN_Z};
      private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
      EAST(1, "east", EnumFacing.EAST),
      NORTH(4, "north", EnumFacing.NORTH);

      private final EnumFacing facing;
      UP_X(6, "up_x", EnumFacing.UP),
      WEST(2, "west", EnumFacing.WEST);

      static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis() {
         int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis;
         if (var10000 != null) {
            return var10000;
         } else {
            int[] var0 = new int[EnumFacing.Axis.values().length];

            try {
               var0[EnumFacing.Axis.X.ordinal()] = 1;
            } catch (NoSuchFieldError var3) {
            }

            try {
               var0[EnumFacing.Axis.Y.ordinal()] = 2;
            } catch (NoSuchFieldError var2) {
            }

            try {
               var0[EnumFacing.Axis.Z.ordinal()] = 3;
            } catch (NoSuchFieldError var1) {
            }

            $SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis = var0;
            return var0;
         }
      }

      public EnumFacing getFacing() {
         return this.facing;
      }

      public static BlockLever.EnumOrientation forFacings(EnumFacing var0, EnumFacing var1) {
         switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[var0.ordinal()]) {
         case 1:
            switch($SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis()[var1.getAxis().ordinal()]) {
            case 1:
               return DOWN_X;
            case 2:
            default:
               throw new IllegalArgumentException("Invalid entityFacing " + var1 + " for facing " + var0);
            case 3:
               return DOWN_Z;
            }
         case 2:
            switch($SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis()[var1.getAxis().ordinal()]) {
            case 1:
               return UP_X;
            case 2:
            default:
               throw new IllegalArgumentException("Invalid entityFacing " + var1 + " for facing " + var0);
            case 3:
               return UP_Z;
            }
         case 3:
            return NORTH;
         case 4:
            return SOUTH;
         case 5:
            return WEST;
         case 6:
            return EAST;
         default:
            throw new IllegalArgumentException("Invalid facing: " + var0);
         }
      }

      private EnumOrientation(int var3, String var4, EnumFacing var5) {
         this.meta = var3;
         this.name = var4;
         this.facing = var5;
      }

      public String getName() {
         return this.name;
      }

      public int getMetadata() {
         return this.meta;
      }

      public String toString() {
         return this.name;
      }

      public static BlockLever.EnumOrientation byMetadata(int var0) {
         if (var0 < 0 || var0 >= META_LOOKUP.length) {
            var0 = 0;
         }

         return META_LOOKUP[var0];
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

      static {
         BlockLever.EnumOrientation[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            BlockLever.EnumOrientation var0 = var3[var1];
            META_LOOKUP[var0.getMetadata()] = var0;
         }

      }
   }
}
