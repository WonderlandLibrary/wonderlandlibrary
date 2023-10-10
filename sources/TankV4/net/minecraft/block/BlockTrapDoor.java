package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrapDoor extends Block {
   public static final PropertyEnum HALF;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
   public static final PropertyDirection FACING;
   public static final PropertyBool OPEN;

   protected BlockTrapDoor(Material var1) {
      super(var1);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, false).withProperty(HALF, BlockTrapDoor.DoorHalf.BOTTOM));
      float var2 = 0.5F;
      float var3 = 1.0F;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      this.setCreativeTab(CreativeTabs.tabRedstone);
   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      this.setBlockBoundsBasedOnState(var1, var2);
      return super.getCollisionBoundingBox(var1, var2, var3);
   }

   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      if (this.blockMaterial == Material.iron) {
         return true;
      } else {
         var3 = var3.cycleProperty(OPEN);
         var1.setBlockState(var2, var3, 2);
         var1.playAuxSFXAtEntity(var4, (Boolean)var3.getValue(OPEN) ? 1003 : 1006, var2, 0);
         return true;
      }
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

   public boolean isOpaqueCube() {
      return false;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING, OPEN, HALF});
   }

   public void setBounds(IBlockState var1) {
      if (var1.getBlock() == this) {
         boolean var2 = var1.getValue(HALF) == BlockTrapDoor.DoorHalf.TOP;
         Boolean var3 = (Boolean)var1.getValue(OPEN);
         EnumFacing var4 = (EnumFacing)var1.getValue(FACING);
         float var5 = 0.1875F;
         if (var2) {
            this.setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);
         } else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
         }

         if (var3) {
            if (var4 == EnumFacing.NORTH) {
               this.setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, 1.0F, 1.0F);
            }

            if (var4 == EnumFacing.SOUTH) {
               this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1875F);
            }

            if (var4 == EnumFacing.WEST) {
               this.setBlockBounds(0.8125F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }

            if (var4 == EnumFacing.EAST) {
               this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.1875F, 1.0F, 1.0F);
            }
         }
      }

   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, getFacing(var1)).withProperty(OPEN, (var1 & 4) != 0).withProperty(HALF, (var1 & 8) == 0 ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
   }

   public int getMetaFromState(IBlockState var1) {
      byte var2 = 0;
      int var3 = var2 | getMetaForFacing((EnumFacing)var1.getValue(FACING));
      if ((Boolean)var1.getValue(OPEN)) {
         var3 |= 4;
      }

      if (var1.getValue(HALF) == BlockTrapDoor.DoorHalf.TOP) {
         var3 |= 8;
      }

      return var3;
   }

   protected static EnumFacing getFacing(int var0) {
      switch(var0 & 3) {
      case 0:
         return EnumFacing.NORTH;
      case 1:
         return EnumFacing.SOUTH;
      case 2:
         return EnumFacing.WEST;
      case 3:
      default:
         return EnumFacing.EAST;
      }
   }

   public void setBlockBoundsForItemRender() {
      float var1 = 0.1875F;
      this.setBlockBounds(0.0F, 0.40625F, 0.0F, 1.0F, 0.59375F, 1.0F);
   }

   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3) {
      return !var3.getAxis().isVertical() && var1.getBlockState(var2.offset(var3.getOpposite())).getBlock() != false;
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (!var1.isRemote) {
         BlockPos var5 = var2.offset(((EnumFacing)var3.getValue(FACING)).getOpposite());
         if (var1.getBlockState(var5).getBlock() != false) {
            var1.setBlockToAir(var2);
            this.dropBlockAsItem(var1, var2, var3, 0);
         } else {
            boolean var6 = var1.isBlockPowered(var2);
            if (var6 || var4.canProvidePower()) {
               boolean var7 = (Boolean)var3.getValue(OPEN);
               if (var7 != var6) {
                  var1.setBlockState(var2, var3.withProperty(OPEN, var6), 2);
                  var1.playAuxSFXAtEntity((EntityPlayer)null, var6 ? 1003 : 1006, var2, 0);
               }
            }
         }
      }

   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      IBlockState var9 = this.getDefaultState();
      if (var3.getAxis().isHorizontal()) {
         var9 = var9.withProperty(FACING, var3).withProperty(OPEN, false);
         var9 = var9.withProperty(HALF, var5 > 0.5F ? BlockTrapDoor.DoorHalf.TOP : BlockTrapDoor.DoorHalf.BOTTOM);
      }

      return var9;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      this.setBounds(var1.getBlockState(var2));
   }

   public AxisAlignedBB getSelectedBoundingBox(World var1, BlockPos var2) {
      this.setBlockBoundsBasedOnState(var1, var2);
      return super.getSelectedBoundingBox(var1, var2);
   }

   public MovingObjectPosition collisionRayTrace(World var1, BlockPos var2, Vec3 var3, Vec3 var4) {
      this.setBlockBoundsBasedOnState(var1, var2);
      return super.collisionRayTrace(var1, var2, var3, var4);
   }

   protected static int getMetaForFacing(EnumFacing var0) {
      switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[var0.ordinal()]) {
      case 3:
         return 0;
      case 4:
         return 1;
      case 5:
         return 2;
      case 6:
      default:
         return 3;
      }
   }

   public boolean isPassable(IBlockAccess var1, BlockPos var2) {
      return !(Boolean)var1.getBlockState(var2).getValue(OPEN);
   }

   static {
      FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
      OPEN = PropertyBool.create("open");
      HALF = PropertyEnum.create("half", BlockTrapDoor.DoorHalf.class);
   }

   public boolean isFullCube() {
      return false;
   }

   public static enum DoorHalf implements IStringSerializable {
      BOTTOM("bottom"),
      TOP("top");

      private final String name;
      private static final BlockTrapDoor.DoorHalf[] ENUM$VALUES = new BlockTrapDoor.DoorHalf[]{TOP, BOTTOM};

      public String getName() {
         return this.name;
      }

      public String toString() {
         return this.name;
      }

      private DoorHalf(String var3) {
         this.name = var3;
      }
   }
}
