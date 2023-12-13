package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDaylightDetector extends BlockContainer {
   private final boolean inverted;
   public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);

   public void updatePower(World var1, BlockPos var2) {
      if (!var1.provider.getHasNoSky()) {
         IBlockState var3 = var1.getBlockState(var2);
         int var4 = var1.getLightFor(EnumSkyBlock.SKY, var2) - var1.getSkylightSubtracted();
         float var5 = var1.getCelestialAngleRadians(1.0F);
         float var6 = var5 < 3.1415927F ? 0.0F : 6.2831855F;
         var5 += (var6 - var5) * 0.2F;
         var4 = Math.round((float)var4 * MathHelper.cos(var5));
         var4 = MathHelper.clamp_int(var4, 0, 15);
         if (this.inverted) {
            var4 = 15 - var4;
         }

         if ((Integer)var3.getValue(POWER) != var4) {
            var1.setBlockState(var2, var3.withProperty(POWER, var4), 3);
         }
      }

   }

   public Item getItem(World var1, BlockPos var2) {
      return Item.getItemFromBlock(Blocks.daylight_detector);
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(POWER, var1);
   }

   public boolean canProvidePower() {
      return true;
   }

   public int getMetaFromState(IBlockState var1) {
      return (Integer)var1.getValue(POWER);
   }

   public int getWeakPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      return (Integer)var3.getValue(POWER);
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityDaylightDetector();
   }

   public BlockDaylightDetector(boolean var1) {
      super(Material.wood);
      this.inverted = var1;
      this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, 0));
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
      this.setCreativeTab(CreativeTabs.tabRedstone);
      this.setHardness(0.2F);
      this.setStepSound(soundTypeWood);
      this.setUnlocalizedName("daylightDetector");
   }

   public int getRenderType() {
      return 3;
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(Blocks.daylight_detector);
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{POWER});
   }

   public void getSubBlocks(Item var1, CreativeTabs var2, List var3) {
      if (!this.inverted) {
         super.getSubBlocks(var1, var2, var3);
      }

   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var4.isAllowEdit()) {
         if (var1.isRemote) {
            return true;
         } else {
            if (this.inverted) {
               var1.setBlockState(var2, Blocks.daylight_detector.getDefaultState().withProperty(POWER, (Integer)var3.getValue(POWER)), 4);
               Blocks.daylight_detector.updatePower(var1, var2);
            } else {
               var1.setBlockState(var2, Blocks.daylight_detector_inverted.getDefaultState().withProperty(POWER, (Integer)var3.getValue(POWER)), 4);
               Blocks.daylight_detector_inverted.updatePower(var1, var2);
            }

            return true;
         }
      } else {
         return super.onBlockActivated(var1, var2, var3, var4, var5, var6, var7, var8);
      }
   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
   }

   public boolean isFullCube() {
      return false;
   }
}
