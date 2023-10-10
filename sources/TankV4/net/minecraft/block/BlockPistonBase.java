package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonBase extends Block {
   public static final PropertyBool EXTENDED = PropertyBool.create("extended");
   private final boolean isSticky;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
   public static final PropertyDirection FACING = PropertyDirection.create("facing");

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      this.setBlockBoundsBasedOnState(var1, var2);
      return super.getCollisionBoundingBox(var1, var2, var3);
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, getFacing(var1)).withProperty(EXTENDED, (var1 & 8) > 0);
   }

   public int getMetaFromState(IBlockState var1) {
      byte var2 = 0;
      int var3 = var2 | ((EnumFacing)var1.getValue(FACING)).getIndex();
      if ((Boolean)var1.getValue(EXTENDED)) {
         var3 |= 8;
      }

      return var3;
   }

   public static EnumFacing getFacing(int var0) {
      int var1 = var0 & 7;
      return var1 > 5 ? null : EnumFacing.getFront(var1);
   }

   public void addCollisionBoxesToList(World var1, BlockPos var2, IBlockState var3, AxisAlignedBB var4, List var5, Entity var6) {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      super.addCollisionBoxesToList(var1, var2, var3, var4, var5, var6);
   }

   private boolean shouldBeExtended(World var1, BlockPos var2, EnumFacing var3) {
      EnumFacing[] var7;
      int var6 = (var7 = EnumFacing.values()).length;

      for(int var5 = 0; var5 < var6; ++var5) {
         EnumFacing var4 = var7[var5];
         if (var4 != var3 && var1.isSidePowered(var2.offset(var4), var4)) {
            return true;
         }
      }

      if (var1.isSidePowered(var2, EnumFacing.DOWN)) {
         return true;
      } else {
         BlockPos var9 = var2.up();
         EnumFacing[] var8;
         int var11 = (var8 = EnumFacing.values()).length;

         for(var6 = 0; var6 < var11; ++var6) {
            EnumFacing var10 = var8[var6];
            if (var10 != EnumFacing.DOWN && var1.isSidePowered(var9.offset(var10), var10)) {
               return true;
            }
         }

         return false;
      }
   }

   private void checkForMove(World var1, BlockPos var2, IBlockState var3) {
      EnumFacing var4 = (EnumFacing)var3.getValue(FACING);
      boolean var5 = this.shouldBeExtended(var1, var2, var4);
      if (var5 && !(Boolean)var3.getValue(EXTENDED)) {
         if ((new BlockPistonStructureHelper(var1, var2, var4, true)).canMove()) {
            var1.addBlockEvent(var2, this, 0, var4.getIndex());
         }
      } else if (!var5 && (Boolean)var3.getValue(EXTENDED)) {
         var1.setBlockState(var2, var3.withProperty(EXTENDED, false), 2);
         var1.addBlockEvent(var2, this, 1, var4.getIndex());
      }

   }

   public BlockPistonBase(boolean var1) {
      super(Material.piston);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(EXTENDED, false));
      this.isSticky = var1;
      this.setStepSound(soundTypePiston);
      this.setHardness(0.5F);
      this.setCreativeTab(CreativeTabs.tabRedstone);
   }

   public void setBlockBoundsForItemRender() {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      IBlockState var3 = var1.getBlockState(var2);
      if (var3.getBlock() == this && (Boolean)var3.getValue(EXTENDED)) {
         float var4 = 0.25F;
         EnumFacing var5 = (EnumFacing)var3.getValue(FACING);
         if (var5 != null) {
            switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[var5.ordinal()]) {
            case 1:
               this.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
               break;
            case 2:
               this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
               break;
            case 3:
               this.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
               break;
            case 4:
               this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
               break;
            case 5:
               this.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
               break;
            case 6:
               this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
            }
         }
      } else {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (!var1.isRemote && var1.getTileEntity(var2) == null) {
         this.checkForMove(var1, var2, var3);
      }

   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING, EXTENDED});
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      var1.setBlockState(var2, var3.withProperty(FACING, getFacingFromEntity(var1, var2, var4)), 2);
      if (!var1.isRemote) {
         this.checkForMove(var1, var2, var3);
      }

   }

   public boolean isFullCube() {
      return false;
   }

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, getFacingFromEntity(var1, var2, var8)).withProperty(EXTENDED, false);
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

   public IBlockState getStateForEntityRender(IBlockState var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.UP);
   }

   public boolean onBlockEventReceived(World var1, BlockPos var2, IBlockState var3, int var4, int var5) {
      EnumFacing var6 = (EnumFacing)var3.getValue(FACING);
      if (!var1.isRemote) {
         boolean var7 = this.shouldBeExtended(var1, var2, var6);
         if (var7 && var4 == 1) {
            var1.setBlockState(var2, var3.withProperty(EXTENDED, true), 2);
            return false;
         }

         if (!var7 && var4 == 0) {
            return false;
         }
      }

      if (var4 == 0) {
         var1.setBlockState(var2, var3.withProperty(EXTENDED, true), 2);
         var1.playSoundEffect((double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D, "tile.piston.out", 0.5F, var1.rand.nextFloat() * 0.25F + 0.6F);
      } else if (var4 == 1) {
         TileEntity var13 = var1.getTileEntity(var2.offset(var6));
         if (var13 instanceof TileEntityPiston) {
            ((TileEntityPiston)var13).clearPistonTileEntity();
         }

         var1.setBlockState(var2, Blocks.piston_extension.getDefaultState().withProperty(BlockPistonMoving.FACING, var6).withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT), 3);
         var1.setTileEntity(var2, BlockPistonMoving.newTileEntity(this.getStateFromMeta(var5), var6, false, true));
         if (this.isSticky) {
            BlockPos var8 = var2.add(var6.getFrontOffsetX() * 2, var6.getFrontOffsetY() * 2, var6.getFrontOffsetZ() * 2);
            Block var9 = var1.getBlockState(var8).getBlock();
            boolean var10 = false;
            if (var9 == Blocks.piston_extension) {
               TileEntity var11 = var1.getTileEntity(var8);
               if (var11 instanceof TileEntityPiston) {
                  TileEntityPiston var12 = (TileEntityPiston)var11;
                  if (var12.getFacing() == var6 && var12.isExtending()) {
                     var12.clearPistonTileEntity();
                     var10 = true;
                  }
               }
            }

            if (!var10 && var9.getMaterial() != Material.air && var6.getOpposite() == false && (var9.getMobilityFlag() == 0 || var9 == Blocks.piston || var9 == Blocks.sticky_piston)) {
               this.doMove(var1, var2, var6, false);
            }
         } else {
            var1.setBlockToAir(var2.offset(var6));
         }

         var1.playSoundEffect((double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D, "tile.piston.in", 0.5F, var1.rand.nextFloat() * 0.15F + 0.6F);
      }

      return true;
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (!var1.isRemote) {
         this.checkForMove(var1, var2, var3);
      }

   }

   public static EnumFacing getFacingFromEntity(World var0, BlockPos var1, EntityLivingBase var2) {
      if (MathHelper.abs((float)var2.posX - (float)var1.getX()) < 2.0F && MathHelper.abs((float)var2.posZ - (float)var1.getZ()) < 2.0F) {
         double var3 = var2.posY + (double)var2.getEyeHeight();
         if (var3 - (double)var1.getY() > 2.0D) {
            return EnumFacing.UP;
         }

         if ((double)var1.getY() - var3 > 0.0D) {
            return EnumFacing.DOWN;
         }
      }

      return var2.getHorizontalFacing().getOpposite();
   }
}
