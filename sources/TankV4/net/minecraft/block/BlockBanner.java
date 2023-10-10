package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBanner extends BlockContainer {
   public static final PropertyInteger ROTATION;
   public static final PropertyDirection FACING;

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      return null;
   }

   static {
      FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
      ROTATION = PropertyInteger.create("rotation", 0, 15);
   }

   public boolean isPassable(IBlockAccess var1, BlockPos var2) {
      return true;
   }

   public String getLocalizedName() {
      return StatCollector.translateToLocal("item.banner.white.name");
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityBanner();
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.banner;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, TileEntity var5) {
      if (var5 instanceof TileEntityBanner) {
         TileEntityBanner var6 = (TileEntityBanner)var5;
         ItemStack var7 = new ItemStack(Items.banner, 1, ((TileEntityBanner)var5).getBaseColor());
         NBTTagCompound var8 = new NBTTagCompound();
         TileEntityBanner.func_181020_a(var8, var6.getBaseColor(), var6.func_181021_d());
         var7.setTagInfo("BlockEntityTag", var8);
         spawnAsEntity(var1, var3, var7);
      } else {
         super.harvestBlock(var1, var2, var3, var4, (TileEntity)null);
      }

   }

   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      TileEntity var6 = var1.getTileEntity(var2);
      if (var6 instanceof TileEntityBanner) {
         ItemStack var7 = new ItemStack(Items.banner, 1, ((TileEntityBanner)var6).getBaseColor());
         NBTTagCompound var8 = new NBTTagCompound();
         var6.writeToNBT(var8);
         var8.removeTag("x");
         var8.removeTag("y");
         var8.removeTag("z");
         var8.removeTag("id");
         var7.setTagInfo("BlockEntityTag", var8);
         spawnAsEntity(var1, var2, var7);
      } else {
         super.dropBlockAsItemWithChance(var1, var2, var3, var4, var5);
      }

   }

   public boolean isFullCube() {
      return false;
   }

   protected BlockBanner() {
      super(Material.wood);
      float var1 = 0.25F;
      float var2 = 1.0F;
      this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var2, 0.5F + var1);
   }

   public Item getItem(World var1, BlockPos var2) {
      return Items.banner;
   }

   public boolean func_181623_g() {
      return true;
   }

   public AxisAlignedBB getSelectedBoundingBox(World var1, BlockPos var2) {
      this.setBlockBoundsBasedOnState(var1, var2);
      return super.getSelectedBoundingBox(var1, var2);
   }

   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return !this.func_181087_e(var1, var2) && super.canPlaceBlockAt(var1, var2);
   }

   public static class BlockBannerStanding extends BlockBanner {
      public IBlockState getStateFromMeta(int var1) {
         return this.getDefaultState().withProperty(ROTATION, var1);
      }

      public BlockBannerStanding() {
         this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATION, 0));
      }

      public int getMetaFromState(IBlockState var1) {
         return (Integer)var1.getValue(ROTATION);
      }

      protected BlockState createBlockState() {
         return new BlockState(this, new IProperty[]{ROTATION});
      }

      public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
         if (!var1.getBlockState(var2.down()).getBlock().getMaterial().isSolid()) {
            this.dropBlockAsItem(var1, var2, var3, 0);
            var1.setBlockToAir(var2);
         }

         super.onNeighborBlockChange(var1, var2, var3, var4);
      }
   }

   public static class BlockBannerHanging extends BlockBanner {
      private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;

      public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
         EnumFacing var3 = (EnumFacing)var1.getBlockState(var2).getValue(FACING);
         float var4 = 0.0F;
         float var5 = 0.78125F;
         float var6 = 0.0F;
         float var7 = 1.0F;
         float var8 = 0.125F;
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[var3.ordinal()]) {
         case 3:
         default:
            this.setBlockBounds(var6, var4, 1.0F - var8, var7, var5, 1.0F);
            break;
         case 4:
            this.setBlockBounds(var6, var4, 0.0F, var7, var5, var8);
            break;
         case 5:
            this.setBlockBounds(1.0F - var8, var4, var6, 1.0F, var5, var7);
            break;
         case 6:
            this.setBlockBounds(0.0F, var4, var6, var8, var5, var7);
         }

      }

      public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
         EnumFacing var5 = (EnumFacing)var3.getValue(FACING);
         if (!var1.getBlockState(var2.offset(var5.getOpposite())).getBlock().getMaterial().isSolid()) {
            this.dropBlockAsItem(var1, var2, var3, 0);
            var1.setBlockToAir(var2);
         }

         super.onNeighborBlockChange(var1, var2, var3, var4);
      }

      public BlockBannerHanging() {
         this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
      }

      protected BlockState createBlockState() {
         return new BlockState(this, new IProperty[]{FACING});
      }

      public int getMetaFromState(IBlockState var1) {
         return ((EnumFacing)var1.getValue(FACING)).getIndex();
      }

      public IBlockState getStateFromMeta(int var1) {
         EnumFacing var2 = EnumFacing.getFront(var1);
         if (var2.getAxis() == EnumFacing.Axis.Y) {
            var2 = EnumFacing.NORTH;
         }

         return this.getDefaultState().withProperty(FACING, var2);
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
   }
}
