package net.minecraft.block;

import com.google.common.cache.LoadingCache;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortal extends BlockBreakable {
   public static final PropertyEnum AXIS;

   public boolean func_176548_d(World var1, BlockPos var2) {
      BlockPortal.Size var3 = new BlockPortal.Size(var1, var2, EnumFacing.Axis.X);
      if (var3.func_150860_b() && BlockPortal.Size.access$0(var3) == 0) {
         var3.func_150859_c();
         return true;
      } else {
         BlockPortal.Size var4 = new BlockPortal.Size(var1, var2, EnumFacing.Axis.Z);
         if (var4.func_150860_b() && BlockPortal.Size.access$0(var4) == 0) {
            var4.func_150859_c();
            return true;
         } else {
            return false;
         }
      }
   }

   static {
      AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class, (Enum[])(EnumFacing.Axis.X, EnumFacing.Axis.Z));
   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      return null;
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(AXIS, (var1 & 3) == 2 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
   }

   public static int getMetaForAxis(EnumFacing.Axis var0) {
      return var0 == EnumFacing.Axis.X ? 1 : (var0 == EnumFacing.Axis.Z ? 2 : 0);
   }

   public int getMetaFromState(IBlockState var1) {
      return getMetaForAxis((EnumFacing.Axis)var1.getValue(AXIS));
   }

   public boolean shouldSideBeRendered(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      EnumFacing.Axis var4 = null;
      IBlockState var5 = var1.getBlockState(var2);
      if (var1.getBlockState(var2).getBlock() == this) {
         var4 = (EnumFacing.Axis)var5.getValue(AXIS);
         if (var4 == null) {
            return false;
         }

         if (var4 == EnumFacing.Axis.Z && var3 != EnumFacing.EAST && var3 != EnumFacing.WEST) {
            return false;
         }

         if (var4 == EnumFacing.Axis.X && var3 != EnumFacing.SOUTH && var3 != EnumFacing.NORTH) {
            return false;
         }
      }

      boolean var6 = var1.getBlockState(var2.west()).getBlock() == this && var1.getBlockState(var2.west(2)).getBlock() != this;
      boolean var7 = var1.getBlockState(var2.east()).getBlock() == this && var1.getBlockState(var2.east(2)).getBlock() != this;
      boolean var8 = var1.getBlockState(var2.north()).getBlock() == this && var1.getBlockState(var2.north(2)).getBlock() != this;
      boolean var9 = var1.getBlockState(var2.south()).getBlock() == this && var1.getBlockState(var2.south(2)).getBlock() != this;
      boolean var10 = var6 || var7 || var4 == EnumFacing.Axis.X;
      boolean var11 = var8 || var9 || var4 == EnumFacing.Axis.Z;
      return var10 && var3 == EnumFacing.WEST ? true : (var10 && var3 == EnumFacing.EAST ? true : (var11 && var3 == EnumFacing.NORTH ? true : var11 && var3 == EnumFacing.SOUTH));
   }

   public int quantityDropped(Random var1) {
      return 0;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{AXIS});
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      EnumFacing.Axis var5 = (EnumFacing.Axis)var3.getValue(AXIS);
      BlockPortal.Size var6;
      if (var5 == EnumFacing.Axis.X) {
         var6 = new BlockPortal.Size(var1, var2, EnumFacing.Axis.X);
         if (!var6.func_150860_b() || BlockPortal.Size.access$0(var6) < BlockPortal.Size.access$1(var6) * BlockPortal.Size.access$2(var6)) {
            var1.setBlockState(var2, Blocks.air.getDefaultState());
         }
      } else if (var5 == EnumFacing.Axis.Z) {
         var6 = new BlockPortal.Size(var1, var2, EnumFacing.Axis.Z);
         if (!var6.func_150860_b() || BlockPortal.Size.access$0(var6) < BlockPortal.Size.access$1(var6) * BlockPortal.Size.access$2(var6)) {
            var1.setBlockState(var2, Blocks.air.getDefaultState());
         }
      }

   }

   public BlockPattern.PatternHelper func_181089_f(World var1, BlockPos var2) {
      EnumFacing.Axis var3 = EnumFacing.Axis.Z;
      BlockPortal.Size var4 = new BlockPortal.Size(var1, var2, EnumFacing.Axis.X);
      LoadingCache var5 = BlockPattern.func_181627_a(var1, true);
      if (!var4.func_150860_b()) {
         var3 = EnumFacing.Axis.X;
         var4 = new BlockPortal.Size(var1, var2, EnumFacing.Axis.Z);
      }

      if (!var4.func_150860_b()) {
         return new BlockPattern.PatternHelper(var2, EnumFacing.NORTH, EnumFacing.UP, var5, 1, 1, 1);
      } else {
         int[] var6 = new int[EnumFacing.AxisDirection.values().length];
         EnumFacing var7 = BlockPortal.Size.access$3(var4).rotateYCCW();
         BlockPos var8 = BlockPortal.Size.access$4(var4).up(var4.func_181100_a() - 1);
         EnumFacing.AxisDirection[] var12;
         int var11 = (var12 = EnumFacing.AxisDirection.values()).length;

         EnumFacing.AxisDirection var9;
         for(int var10 = 0; var10 < var11; ++var10) {
            var9 = var12[var10];
            BlockPattern.PatternHelper var13 = new BlockPattern.PatternHelper(var7.getAxisDirection() == var9 ? var8 : var8.offset(BlockPortal.Size.access$3(var4), var4.func_181101_b() - 1), EnumFacing.func_181076_a(var9, var3), EnumFacing.UP, var5, var4.func_181101_b(), var4.func_181100_a(), 1);

            for(int var14 = 0; var14 < var4.func_181101_b(); ++var14) {
               for(int var15 = 0; var15 < var4.func_181100_a(); ++var15) {
                  BlockWorldState var16 = var13.translateOffset(var14, var15, 1);
                  if (var16.getBlockState() != null && var16.getBlockState().getBlock().getMaterial() != Material.air) {
                     ++var6[var9.ordinal()];
                  }
               }
            }
         }

         var9 = EnumFacing.AxisDirection.POSITIVE;
         EnumFacing.AxisDirection[] var19;
         int var18 = (var19 = EnumFacing.AxisDirection.values()).length;

         for(var11 = 0; var11 < var18; ++var11) {
            EnumFacing.AxisDirection var17 = var19[var11];
            if (var6[var17.ordinal()] < var6[var9.ordinal()]) {
               var9 = var17;
            }
         }

         return new BlockPattern.PatternHelper(var7.getAxisDirection() == var9 ? var8 : var8.offset(BlockPortal.Size.access$3(var4), var4.func_181101_b() - 1), EnumFacing.func_181076_a(var9, var3), EnumFacing.UP, var5, var4.func_181101_b(), var4.func_181100_a(), 1);
      }
   }

   public Item getItem(World var1, BlockPos var2) {
      return null;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      EnumFacing.Axis var3 = (EnumFacing.Axis)var1.getBlockState(var2).getValue(AXIS);
      float var4 = 0.125F;
      float var5 = 0.125F;
      if (var3 == EnumFacing.Axis.X) {
         var4 = 0.5F;
      }

      if (var3 == EnumFacing.Axis.Z) {
         var5 = 0.5F;
      }

      this.setBlockBounds(0.5F - var4, 0.0F, 0.5F - var5, 0.5F + var4, 1.0F, 0.5F + var5);
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.TRANSLUCENT;
   }

   public boolean isFullCube() {
      return false;
   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      super.updateTick(var1, var2, var3, var4);
      if (var1.provider.isSurfaceWorld() && var1.getGameRules().getBoolean("doMobSpawning") && var4.nextInt(2000) < var1.getDifficulty().getDifficultyId()) {
         int var5 = var2.getY();

         BlockPos var6;
         for(var6 = var2; !World.doesBlockHaveSolidTopSurface(var1, var6) && var6.getY() > 0; var6 = var6.down()) {
         }

         if (var5 > 0 && !var1.getBlockState(var6.up()).getBlock().isNormalCube()) {
            Entity var7 = ItemMonsterPlacer.spawnCreature(var1, 57, (double)var6.getX() + 0.5D, (double)var6.getY() + 1.1D, (double)var6.getZ() + 0.5D);
            if (var7 != null) {
               var7.timeUntilPortal = var7.getPortalCooldown();
            }
         }
      }

   }

   public void randomDisplayTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (var4.nextInt(100) == 0) {
         var1.playSound((double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D, "portal.portal", 0.5F, var4.nextFloat() * 0.4F + 0.8F, false);
      }

      for(int var5 = 0; var5 < 4; ++var5) {
         double var6 = (double)((float)var2.getX() + var4.nextFloat());
         double var8 = (double)((float)var2.getY() + var4.nextFloat());
         double var10 = (double)((float)var2.getZ() + var4.nextFloat());
         double var12 = ((double)var4.nextFloat() - 0.5D) * 0.5D;
         double var14 = ((double)var4.nextFloat() - 0.5D) * 0.5D;
         double var16 = ((double)var4.nextFloat() - 0.5D) * 0.5D;
         int var18 = var4.nextInt(2) * 2 - 1;
         if (var1.getBlockState(var2.west()).getBlock() != this && var1.getBlockState(var2.east()).getBlock() != this) {
            var6 = (double)var2.getX() + 0.5D + 0.25D * (double)var18;
            var12 = (double)(var4.nextFloat() * 2.0F * (float)var18);
         } else {
            var10 = (double)var2.getZ() + 0.5D + 0.25D * (double)var18;
            var16 = (double)(var4.nextFloat() * 2.0F * (float)var18);
         }

         var1.spawnParticle(EnumParticleTypes.PORTAL, var6, var8, var10, var12, var14, var16);
      }

   }

   public BlockPortal() {
      super(Material.portal, false);
      this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.X));
      this.setTickRandomly(true);
   }

   public void onEntityCollidedWithBlock(World var1, BlockPos var2, IBlockState var3, Entity var4) {
      if (var4.ridingEntity == null && var4.riddenByEntity == null) {
         var4.func_181015_d(var2);
      }

   }

   public static class Size {
      private int field_150864_e = 0;
      private final EnumFacing.Axis axis;
      private int field_150862_g;
      private final EnumFacing field_150863_d;
      private int field_150868_h;
      private final EnumFacing field_150866_c;
      private BlockPos field_150861_f;
      private final World world;

      public boolean func_150860_b() {
         return this.field_150861_f != null && this.field_150868_h >= 2 && this.field_150868_h <= 21 && this.field_150862_g >= 3 && this.field_150862_g <= 21;
      }

      static int access$2(BlockPortal.Size var0) {
         return var0.field_150862_g;
      }

      static BlockPos access$4(BlockPortal.Size var0) {
         return var0.field_150861_f;
      }

      protected int func_150858_a() {
         int var1;
         label56:
         for(this.field_150862_g = 0; this.field_150862_g < 21; ++this.field_150862_g) {
            for(var1 = 0; var1 < this.field_150868_h; ++var1) {
               BlockPos var2 = this.field_150861_f.offset(this.field_150866_c, var1).up(this.field_150862_g);
               Block var3 = this.world.getBlockState(var2).getBlock();
               if (var3 != false) {
                  break label56;
               }

               if (var3 == Blocks.portal) {
                  ++this.field_150864_e;
               }

               if (var1 == 0) {
                  var3 = this.world.getBlockState(var2.offset(this.field_150863_d)).getBlock();
                  if (var3 != Blocks.obsidian) {
                     break label56;
                  }
               } else if (var1 == this.field_150868_h - 1) {
                  var3 = this.world.getBlockState(var2.offset(this.field_150866_c)).getBlock();
                  if (var3 != Blocks.obsidian) {
                     break label56;
                  }
               }
            }
         }

         for(var1 = 0; var1 < this.field_150868_h; ++var1) {
            if (this.world.getBlockState(this.field_150861_f.offset(this.field_150866_c, var1).up(this.field_150862_g)).getBlock() != Blocks.obsidian) {
               this.field_150862_g = 0;
               break;
            }
         }

         if (this.field_150862_g <= 21 && this.field_150862_g >= 3) {
            return this.field_150862_g;
         } else {
            this.field_150861_f = null;
            this.field_150868_h = 0;
            this.field_150862_g = 0;
            return 0;
         }
      }

      protected int func_180120_a(BlockPos var1, EnumFacing var2) {
         int var3;
         for(var3 = 0; var3 < 22; ++var3) {
            BlockPos var4 = var1.offset(var2, var3);
            if (this.world.getBlockState(var4).getBlock() == false || this.world.getBlockState(var4.down()).getBlock() != Blocks.obsidian) {
               break;
            }
         }

         Block var5 = this.world.getBlockState(var1.offset(var2, var3)).getBlock();
         return var5 == Blocks.obsidian ? var3 : 0;
      }

      static int access$0(BlockPortal.Size var0) {
         return var0.field_150864_e;
      }

      static EnumFacing access$3(BlockPortal.Size var0) {
         return var0.field_150866_c;
      }

      static int access$1(BlockPortal.Size var0) {
         return var0.field_150868_h;
      }

      public Size(World var1, BlockPos var2, EnumFacing.Axis var3) {
         this.world = var1;
         this.axis = var3;
         if (var3 == EnumFacing.Axis.X) {
            this.field_150863_d = EnumFacing.EAST;
            this.field_150866_c = EnumFacing.WEST;
         } else {
            this.field_150863_d = EnumFacing.NORTH;
            this.field_150866_c = EnumFacing.SOUTH;
         }

         for(BlockPos var4 = var2; var2.getY() > var4.getY() - 21 && var2.getY() > 0 && var1.getBlockState(var2.down()).getBlock() == false; var2 = var2.down()) {
         }

         int var5 = this.func_180120_a(var2, this.field_150863_d) - 1;
         if (var5 >= 0) {
            this.field_150861_f = var2.offset(this.field_150863_d, var5);
            this.field_150868_h = this.func_180120_a(this.field_150861_f, this.field_150866_c);
            if (this.field_150868_h < 2 || this.field_150868_h > 21) {
               this.field_150861_f = null;
               this.field_150868_h = 0;
            }
         }

         if (this.field_150861_f != null) {
            this.field_150862_g = this.func_150858_a();
         }

      }

      public int func_181100_a() {
         return this.field_150862_g;
      }

      public void func_150859_c() {
         for(int var1 = 0; var1 < this.field_150868_h; ++var1) {
            BlockPos var2 = this.field_150861_f.offset(this.field_150866_c, var1);

            for(int var3 = 0; var3 < this.field_150862_g; ++var3) {
               this.world.setBlockState(var2.up(var3), Blocks.portal.getDefaultState().withProperty(BlockPortal.AXIS, this.axis), 2);
            }
         }

      }

      public int func_181101_b() {
         return this.field_150868_h;
      }
   }
}
