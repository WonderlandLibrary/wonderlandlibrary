package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFarmland extends Block {
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
   public static final PropertyInteger MOISTURE = PropertyInteger.create("moisture", 0, 7);

   protected BlockFarmland() {
      super(Material.ground);
      this.setDefaultState(this.blockState.getBaseState().withProperty(MOISTURE, 0));
      this.setTickRandomly(true);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
      this.setLightOpacity(255);
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

   public void onFallenUpon(World var1, BlockPos var2, Entity var3, float var4) {
      if (var3 instanceof EntityLivingBase) {
         if (!var1.isRemote && var1.rand.nextFloat() < var4 - 0.5F) {
            if (!(var3 instanceof EntityPlayer) && !var1.getGameRules().getBoolean("mobGriefing")) {
               return;
            }

            var1.setBlockState(var2, Blocks.dirt.getDefaultState());
         }

         super.onFallenUpon(var1, var2, var3, var4);
      }

   }

   public int getMetaFromState(IBlockState var1) {
      return (Integer)var1.getValue(MOISTURE);
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), var2, var3);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public Item getItem(World var1, BlockPos var2) {
      return Item.getItemFromBlock(Blocks.dirt);
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      super.onNeighborBlockChange(var1, var2, var3, var4);
      if (var1.getBlockState(var2.up()).getBlock().getMaterial().isSolid()) {
         var1.setBlockState(var2, Blocks.dirt.getDefaultState());
      }

   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{MOISTURE});
   }

   public boolean shouldSideBeRendered(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[var3.ordinal()]) {
      case 2:
         return true;
      case 3:
      case 4:
      case 5:
      case 6:
         Block var4 = var1.getBlockState(var2).getBlock();
         if (!var4.isOpaqueCube() && var4 != Blocks.farmland) {
            return true;
         }

         return false;
      default:
         return super.shouldSideBeRendered(var1, var2, var3);
      }
   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      return new AxisAlignedBB((double)var2.getX(), (double)var2.getY(), (double)var2.getZ(), (double)(var2.getX() + 1), (double)(var2.getY() + 1), (double)(var2.getZ() + 1));
   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      int var5 = (Integer)var3.getValue(MOISTURE);
      if (var1 == var2 && !var1.canLightningStrike(var2.up())) {
         if (var5 > 0) {
            var1.setBlockState(var2, var3.withProperty(MOISTURE, var5 - 1), 2);
         } else if (var2 == false) {
            var1.setBlockState(var2, Blocks.dirt.getDefaultState());
         }
      } else if (var5 < 7) {
         var1.setBlockState(var2, var3.withProperty(MOISTURE, 7), 2);
      }

   }

   public boolean isFullCube() {
      return false;
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(MOISTURE, var1 & 7);
   }
}
