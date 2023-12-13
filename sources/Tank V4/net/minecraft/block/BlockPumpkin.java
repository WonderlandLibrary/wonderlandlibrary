package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockPumpkin extends BlockDirectional {
   private BlockPattern snowmanBasePattern;
   private static final Predicate field_181085_Q = new Predicate() {
      public boolean apply(Object var1) {
         return this.apply((IBlockState)var1);
      }

      public boolean apply(IBlockState var1) {
         return var1 != null && (var1.getBlock() == Blocks.pumpkin || var1.getBlock() == Blocks.lit_pumpkin);
      }
   };
   private BlockPattern golemBasePattern;
   private BlockPattern golemPattern;
   private BlockPattern snowmanPattern;

   public boolean canDispenserPlace(World var1, BlockPos var2) {
      return this.getSnowmanBasePattern().match(var1, var2) != null || this.getGolemBasePattern().match(var1, var2) != null;
   }

   private void trySpawnGolem(World var1, BlockPos var2) {
      BlockPattern.PatternHelper var3;
      int var4;
      int var6;
      if ((var3 = this.getSnowmanPattern().match(var1, var2)) != null) {
         for(var4 = 0; var4 < this.getSnowmanPattern().getThumbLength(); ++var4) {
            BlockWorldState var5 = var3.translateOffset(0, var4, 0);
            var1.setBlockState(var5.getPos(), Blocks.air.getDefaultState(), 2);
         }

         EntitySnowman var9 = new EntitySnowman(var1);
         BlockPos var10 = var3.translateOffset(0, 2, 0).getPos();
         var9.setLocationAndAngles((double)var10.getX() + 0.5D, (double)var10.getY() + 0.05D, (double)var10.getZ() + 0.5D, 0.0F, 0.0F);
         var1.spawnEntityInWorld(var9);

         for(var6 = 0; var6 < 120; ++var6) {
            var1.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double)var10.getX() + var1.rand.nextDouble(), (double)var10.getY() + var1.rand.nextDouble() * 2.5D, (double)var10.getZ() + var1.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
         }

         for(var6 = 0; var6 < this.getSnowmanPattern().getThumbLength(); ++var6) {
            BlockWorldState var7 = var3.translateOffset(0, var6, 0);
            var1.notifyNeighborsRespectDebug(var7.getPos(), Blocks.air);
         }
      } else if ((var3 = this.getGolemPattern().match(var1, var2)) != null) {
         for(var4 = 0; var4 < this.getGolemPattern().getPalmLength(); ++var4) {
            for(int var12 = 0; var12 < this.getGolemPattern().getThumbLength(); ++var12) {
               var1.setBlockState(var3.translateOffset(var4, var12, 0).getPos(), Blocks.air.getDefaultState(), 2);
            }
         }

         BlockPos var11 = var3.translateOffset(1, 2, 0).getPos();
         EntityIronGolem var13 = new EntityIronGolem(var1);
         var13.setPlayerCreated(true);
         var13.setLocationAndAngles((double)var11.getX() + 0.5D, (double)var11.getY() + 0.05D, (double)var11.getZ() + 0.5D, 0.0F, 0.0F);
         var1.spawnEntityInWorld(var13);

         for(var6 = 0; var6 < 120; ++var6) {
            var1.spawnParticle(EnumParticleTypes.SNOWBALL, (double)var11.getX() + var1.rand.nextDouble(), (double)var11.getY() + var1.rand.nextDouble() * 3.9D, (double)var11.getZ() + var1.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
         }

         for(var6 = 0; var6 < this.getGolemPattern().getPalmLength(); ++var6) {
            for(int var14 = 0; var14 < this.getGolemPattern().getThumbLength(); ++var14) {
               BlockWorldState var8 = var3.translateOffset(var6, var14, 0);
               var1.notifyNeighborsRespectDebug(var8.getPos(), Blocks.air);
            }
         }
      }

   }

   protected BlockPattern getSnowmanPattern() {
      if (this.snowmanPattern == null) {
         this.snowmanPattern = FactoryBlockPattern.start().aisle("^", "#", "#").where('^', BlockWorldState.hasState(field_181085_Q)).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
      }

      return this.snowmanPattern;
   }

   protected BlockPattern getSnowmanBasePattern() {
      if (this.snowmanBasePattern == null) {
         this.snowmanBasePattern = FactoryBlockPattern.start().aisle(" ", "#", "#").where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
      }

      return this.snowmanBasePattern;
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(var1));
   }

   protected BlockPattern getGolemPattern() {
      if (this.golemPattern == null) {
         this.golemPattern = FactoryBlockPattern.start().aisle("~^~", "###", "~#~").where('^', BlockWorldState.hasState(field_181085_Q)).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
      }

      return this.golemPattern;
   }

   public int getMetaFromState(IBlockState var1) {
      return ((EnumFacing)var1.getValue(FACING)).getHorizontalIndex();
   }

   protected BlockPattern getGolemBasePattern() {
      if (this.golemBasePattern == null) {
         this.golemBasePattern = FactoryBlockPattern.start().aisle("~ ~", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
      }

      return this.golemBasePattern;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING});
   }

   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return var1.getBlockState(var2).getBlock().blockMaterial.isReplaceable() && World.doesBlockHaveSolidTopSurface(var1, var2.down());
   }

   protected BlockPumpkin() {
      super(Material.gourd, MapColor.adobeColor);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      super.onBlockAdded(var1, var2, var3);
      this.trySpawnGolem(var1, var2);
   }

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, var8.getHorizontalFacing().getOpposite());
   }
}
