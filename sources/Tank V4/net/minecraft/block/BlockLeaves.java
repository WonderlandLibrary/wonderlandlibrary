package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public abstract class BlockLeaves extends BlockLeavesBase {
   protected int iconIndex;
   int[] surroundings;
   protected boolean isTransparent;
   public static final PropertyBool DECAYABLE = PropertyBool.create("decayable");
   public static final PropertyBool CHECK_DECAY = PropertyBool.create("check_decay");

   public int quantityDropped(Random var1) {
      return var1.nextInt(20) == 0 ? 1 : 0;
   }

   public BlockLeaves() {
      super(Material.leaves, false);
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.tabDecorations);
      this.setHardness(0.2F);
      this.setLightOpacity(1);
      this.setStepSound(soundTypeGrass);
   }

   public int getRenderColor(IBlockState var1) {
      return ColorizerFoliage.getFoliageColorBasic();
   }

   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      if (!var1.isRemote) {
         int var6 = this.getSaplingDropChance(var3);
         if (var5 > 0) {
            var6 -= 2 << var5;
            if (var6 < 10) {
               var6 = 10;
            }
         }

         if (var1.rand.nextInt(var6) == 0) {
            Item var7 = this.getItemDropped(var3, var1.rand, var5);
            spawnAsEntity(var1, var2, new ItemStack(var7, 1, this.damageDropped(var3)));
         }

         var6 = 200;
         if (var5 > 0) {
            var6 -= 10 << var5;
            if (var6 < 40) {
               var6 = 40;
            }
         }

         this.dropApple(var1, var2, var3, var6);
      }

   }

   public void randomDisplayTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (var1.canLightningStrike(var2.up()) && !World.doesBlockHaveSolidTopSurface(var1, var2.down()) && var4.nextInt(15) == 1) {
         double var5 = (double)((float)var2.getX() + var4.nextFloat());
         double var7 = (double)var2.getY() - 0.05D;
         double var9 = (double)((float)var2.getZ() + var4.nextFloat());
         var1.spawnParticle(EnumParticleTypes.DRIP_WATER, var5, var7, var9, 0.0D, 0.0D, 0.0D);
      }

   }

   public EnumWorldBlockLayer getBlockLayer() {
      return this.isTransparent ? EnumWorldBlockLayer.CUTOUT_MIPPED : EnumWorldBlockLayer.SOLID;
   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!var1.isRemote && (Boolean)var3.getValue(CHECK_DECAY) && (Boolean)var3.getValue(DECAYABLE)) {
         byte var5 = 4;
         int var6 = var5 + 1;
         int var7 = var2.getX();
         int var8 = var2.getY();
         int var9 = var2.getZ();
         byte var10 = 32;
         int var11 = var10 * var10;
         int var12 = var10 / 2;
         if (this.surroundings == null) {
            this.surroundings = new int[var10 * var10 * var10];
         }

         if (var1.isAreaLoaded(new BlockPos(var7 - var6, var8 - var6, var9 - var6), new BlockPos(var7 + var6, var8 + var6, var9 + var6))) {
            BlockPos.MutableBlockPos var13 = new BlockPos.MutableBlockPos();
            int var14 = -var5;

            label114:
            while(true) {
               int var15;
               int var16;
               if (var14 > var5) {
                  var14 = 1;

                  while(true) {
                     if (var14 > 4) {
                        break label114;
                     }

                     for(var15 = -var5; var15 <= var5; ++var15) {
                        for(var16 = -var5; var16 <= var5; ++var16) {
                           for(int var20 = -var5; var20 <= var5; ++var20) {
                              if (this.surroundings[(var15 + var12) * var11 + (var16 + var12) * var10 + var20 + var12] == var14 - 1) {
                                 if (this.surroundings[(var15 + var12 - 1) * var11 + (var16 + var12) * var10 + var20 + var12] == -2) {
                                    this.surroundings[(var15 + var12 - 1) * var11 + (var16 + var12) * var10 + var20 + var12] = var14;
                                 }

                                 if (this.surroundings[(var15 + var12 + 1) * var11 + (var16 + var12) * var10 + var20 + var12] == -2) {
                                    this.surroundings[(var15 + var12 + 1) * var11 + (var16 + var12) * var10 + var20 + var12] = var14;
                                 }

                                 if (this.surroundings[(var15 + var12) * var11 + (var16 + var12 - 1) * var10 + var20 + var12] == -2) {
                                    this.surroundings[(var15 + var12) * var11 + (var16 + var12 - 1) * var10 + var20 + var12] = var14;
                                 }

                                 if (this.surroundings[(var15 + var12) * var11 + (var16 + var12 + 1) * var10 + var20 + var12] == -2) {
                                    this.surroundings[(var15 + var12) * var11 + (var16 + var12 + 1) * var10 + var20 + var12] = var14;
                                 }

                                 if (this.surroundings[(var15 + var12) * var11 + (var16 + var12) * var10 + (var20 + var12 - 1)] == -2) {
                                    this.surroundings[(var15 + var12) * var11 + (var16 + var12) * var10 + (var20 + var12 - 1)] = var14;
                                 }

                                 if (this.surroundings[(var15 + var12) * var11 + (var16 + var12) * var10 + var20 + var12 + 1] == -2) {
                                    this.surroundings[(var15 + var12) * var11 + (var16 + var12) * var10 + var20 + var12 + 1] = var14;
                                 }
                              }
                           }
                        }
                     }

                     ++var14;
                  }
               }

               for(var15 = -var5; var15 <= var5; ++var15) {
                  for(var16 = -var5; var16 <= var5; ++var16) {
                     Block var17 = var1.getBlockState(var13.func_181079_c(var7 + var14, var8 + var15, var9 + var16)).getBlock();
                     if (var17 != Blocks.log && var17 != Blocks.log2) {
                        if (var17.getMaterial() == Material.leaves) {
                           this.surroundings[(var14 + var12) * var11 + (var15 + var12) * var10 + var16 + var12] = -2;
                        } else {
                           this.surroundings[(var14 + var12) * var11 + (var15 + var12) * var10 + var16 + var12] = -1;
                        }
                     } else {
                        this.surroundings[(var14 + var12) * var11 + (var15 + var12) * var10 + var16 + var12] = 0;
                     }
                  }
               }

               ++var14;
            }
         }

         int var19 = this.surroundings[var12 * var11 + var12 * var10 + var12];
         if (var19 >= 0) {
            var1.setBlockState(var2, var3.withProperty(CHECK_DECAY, false), 4);
         } else {
            this.destroy(var1, var2);
         }
      }

   }

   public void setGraphicsLevel(boolean var1) {
      this.isTransparent = var1;
      this.fancyGraphics = var1;
      this.iconIndex = var1 ? 0 : 1;
   }

   public int colorMultiplier(IBlockAccess var1, BlockPos var2, int var3) {
      return BiomeColorHelper.getFoliageColorAtPos(var1, var2);
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(Blocks.sapling);
   }

   private void destroy(World var1, BlockPos var2) {
      this.dropBlockAsItem(var1, var2, var1.getBlockState(var2), 0);
      var1.setBlockToAir(var2);
   }

   public boolean isOpaqueCube() {
      return !this.fancyGraphics;
   }

   public abstract BlockPlanks.EnumType getWoodType(int var1);

   public boolean isVisuallyOpaque() {
      return false;
   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      byte var4 = 1;
      int var5 = var4 + 1;
      int var6 = var2.getX();
      int var7 = var2.getY();
      int var8 = var2.getZ();
      if (var1.isAreaLoaded(new BlockPos(var6 - var5, var7 - var5, var8 - var5), new BlockPos(var6 + var5, var7 + var5, var8 + var5))) {
         for(int var9 = -var4; var9 <= var4; ++var9) {
            for(int var10 = -var4; var10 <= var4; ++var10) {
               for(int var11 = -var4; var11 <= var4; ++var11) {
                  BlockPos var12 = var2.add(var9, var10, var11);
                  IBlockState var13 = var1.getBlockState(var12);
                  if (var13.getBlock().getMaterial() == Material.leaves && !(Boolean)var13.getValue(CHECK_DECAY)) {
                     var1.setBlockState(var12, var13.withProperty(CHECK_DECAY, true), 4);
                  }
               }
            }
         }
      }

   }

   public int getBlockColor() {
      return ColorizerFoliage.getFoliageColor(0.5D, 1.0D);
   }

   protected int getSaplingDropChance(IBlockState var1) {
      return 20;
   }

   protected void dropApple(World var1, BlockPos var2, IBlockState var3, int var4) {
   }
}
