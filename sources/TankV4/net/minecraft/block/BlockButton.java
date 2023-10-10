package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockButton extends Block {
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
   public static final PropertyDirection FACING = PropertyDirection.create("facing");
   private final boolean wooden;
   public static final PropertyBool POWERED = PropertyBool.create("powered");

   public int tickRate(World var1) {
      return this.wooden ? 30 : 20;
   }

   public int getStrongPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      return !(Boolean)var3.getValue(POWERED) ? 0 : (var3.getValue(FACING) == var4 ? 15 : 0);
   }

   protected BlockButton(boolean var1) {
      super(Material.circuits);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.tabRedstone);
      this.wooden = var1;
   }

   public int getMetaFromState(IBlockState var1) {
      int var2;
      switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[((EnumFacing)var1.getValue(FACING)).ordinal()]) {
      case 1:
         var2 = 0;
         break;
      case 2:
      default:
         var2 = 5;
         break;
      case 3:
         var2 = 4;
         break;
      case 4:
         var2 = 3;
         break;
      case 5:
         var2 = 2;
         break;
      case 6:
         var2 = 1;
      }

      if ((Boolean)var1.getValue(POWERED)) {
         var2 |= 8;
      }

      return var2;
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

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING, POWERED});
   }

   public boolean canProvidePower() {
      return true;
   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      return null;
   }

   private void notifyNeighbors(World var1, BlockPos var2, EnumFacing var3) {
      var1.notifyNeighborsOfStateChange(var2, this);
      var1.notifyNeighborsOfStateChange(var2.offset(var3.getOpposite()), this);
   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      if ((Boolean)var3.getValue(POWERED)) {
         this.notifyNeighbors(var1, var2, (EnumFacing)var3.getValue(FACING));
      }

      super.breakBlock(var1, var2, var3);
   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!var1.isRemote && (Boolean)var3.getValue(POWERED)) {
         if (this.wooden) {
            this.checkForArrows(var1, var2, var3);
         } else {
            var1.setBlockState(var2, var3.withProperty(POWERED, false));
            this.notifyNeighbors(var1, var2, (EnumFacing)var3.getValue(FACING));
            var1.playSoundEffect((double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
            var1.markBlockRangeForRenderUpdate(var2, var2);
         }
      }

   }

   public boolean isOpaqueCube() {
      return false;
   }

   public void setBlockBoundsForItemRender() {
      float var1 = 0.1875F;
      float var2 = 0.125F;
      float var3 = 0.125F;
      this.setBlockBounds(0.5F - var1, 0.5F - var2, 0.5F - var3, 0.5F + var1, 0.5F + var2, 0.5F + var3);
   }

   public IBlockState getStateFromMeta(int var1) {
      EnumFacing var2;
      switch(var1 & 7) {
      case 0:
         var2 = EnumFacing.DOWN;
         break;
      case 1:
         var2 = EnumFacing.EAST;
         break;
      case 2:
         var2 = EnumFacing.WEST;
         break;
      case 3:
         var2 = EnumFacing.SOUTH;
         break;
      case 4:
         var2 = EnumFacing.NORTH;
         break;
      case 5:
      default:
         var2 = EnumFacing.UP;
      }

      return this.getDefaultState().withProperty(FACING, var2).withProperty(POWERED, (var1 & 8) > 0);
   }

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return var3.getOpposite() != false ? this.getDefaultState().withProperty(FACING, var3).withProperty(POWERED, false) : this.getDefaultState().withProperty(FACING, EnumFacing.DOWN).withProperty(POWERED, false);
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (var2 < var3 && ((EnumFacing)var3.getValue(FACING)).getOpposite() != false) {
         this.dropBlockAsItem(var1, var2, var3, 0);
         var1.setBlockToAir(var2);
      }

   }

   public boolean isFullCube() {
      return false;
   }

   public void randomTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      this.updateBlockBounds(var1.getBlockState(var2));
   }

   public void onEntityCollidedWithBlock(World var1, BlockPos var2, IBlockState var3, Entity var4) {
      if (!var1.isRemote && this.wooden && !(Boolean)var3.getValue(POWERED)) {
         this.checkForArrows(var1, var2, var3);
      }

   }

   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      if ((Boolean)var3.getValue(POWERED)) {
         return true;
      } else {
         var1.setBlockState(var2, var3.withProperty(POWERED, true), 3);
         var1.markBlockRangeForRenderUpdate(var2, var2);
         var1.playSoundEffect((double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
         this.notifyNeighbors(var1, var2, (EnumFacing)var3.getValue(FACING));
         var1.scheduleUpdate(var2, this, this.tickRate(var1));
         return true;
      }
   }

   public int getWeakPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      return (Boolean)var3.getValue(POWERED) ? 15 : 0;
   }

   private void checkForArrows(World var1, BlockPos var2, IBlockState var3) {
      this.updateBlockBounds(var3);
      List var4 = var1.getEntitiesWithinAABB(EntityArrow.class, new AxisAlignedBB((double)var2.getX() + this.minX, (double)var2.getY() + this.minY, (double)var2.getZ() + this.minZ, (double)var2.getX() + this.maxX, (double)var2.getY() + this.maxY, (double)var2.getZ() + this.maxZ));
      boolean var5 = !var4.isEmpty();
      boolean var6 = (Boolean)var3.getValue(POWERED);
      if (var5 && !var6) {
         var1.setBlockState(var2, var3.withProperty(POWERED, true));
         this.notifyNeighbors(var1, var2, (EnumFacing)var3.getValue(FACING));
         var1.markBlockRangeForRenderUpdate(var2, var2);
         var1.playSoundEffect((double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
      }

      if (!var5 && var6) {
         var1.setBlockState(var2, var3.withProperty(POWERED, false));
         this.notifyNeighbors(var1, var2, (EnumFacing)var3.getValue(FACING));
         var1.markBlockRangeForRenderUpdate(var2, var2);
         var1.playSoundEffect((double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
      }

      if (var5) {
         var1.scheduleUpdate(var2, this, this.tickRate(var1));
      }

   }

   private void updateBlockBounds(IBlockState var1) {
      EnumFacing var2 = (EnumFacing)var1.getValue(FACING);
      boolean var3 = (Boolean)var1.getValue(POWERED);
      float var4 = 0.25F;
      float var5 = 0.375F;
      float var6 = (float)(var3 ? 1 : 2) / 16.0F;
      float var7 = 0.125F;
      float var8 = 0.1875F;
      switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[var2.ordinal()]) {
      case 1:
         this.setBlockBounds(0.3125F, 1.0F - var6, 0.375F, 0.6875F, 1.0F, 0.625F);
         break;
      case 2:
         this.setBlockBounds(0.3125F, 0.0F, 0.375F, 0.6875F, 0.0F + var6, 0.625F);
         break;
      case 3:
         this.setBlockBounds(0.3125F, 0.375F, 1.0F - var6, 0.6875F, 0.625F, 1.0F);
         break;
      case 4:
         this.setBlockBounds(0.3125F, 0.375F, 0.0F, 0.6875F, 0.625F, var6);
         break;
      case 5:
         this.setBlockBounds(1.0F - var6, 0.375F, 0.3125F, 1.0F, 0.625F, 0.6875F);
         break;
      case 6:
         this.setBlockBounds(0.0F, 0.375F, 0.3125F, var6, 0.625F, 0.6875F);
      }

   }

   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3) {
      return func_181088_a(var1, var2, var3.getOpposite());
   }
}
