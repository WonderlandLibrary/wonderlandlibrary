package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldGenDungeons extends WorldGenerator {
   private static final String[] SPAWNERTYPES = new String[]{"Skeleton", "Zombie", "Zombie", "Spider"};
   private static final Logger field_175918_a = LogManager.getLogger();
   private static final List CHESTCONTENT;

   static {
      CHESTCONTENT = Lists.newArrayList((Object[])(new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 10), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 4, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 1, 10), new WeightedRandomChestContent(Items.wheat, 0, 1, 4, 10), new WeightedRandomChestContent(Items.gunpowder, 0, 1, 4, 10), new WeightedRandomChestContent(Items.string, 0, 1, 4, 10), new WeightedRandomChestContent(Items.bucket, 0, 1, 1, 10), new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1), new WeightedRandomChestContent(Items.redstone, 0, 1, 4, 10), new WeightedRandomChestContent(Items.record_13, 0, 1, 1, 4), new WeightedRandomChestContent(Items.record_cat, 0, 1, 1, 4), new WeightedRandomChestContent(Items.name_tag, 0, 1, 1, 10), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 2), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 5), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)));
   }

   public boolean generate(World var1, Random var2, BlockPos var3) {
      boolean var4 = true;
      int var5 = var2.nextInt(2) + 2;
      int var6 = -var5 - 1;
      int var7 = var5 + 1;
      boolean var8 = true;
      boolean var9 = true;
      int var10 = var2.nextInt(2) + 2;
      int var11 = -var10 - 1;
      int var12 = var10 + 1;
      int var13 = 0;

      int var14;
      int var15;
      int var16;
      BlockPos var17;
      for(var14 = var6; var14 <= var7; ++var14) {
         for(var15 = -1; var15 <= 4; ++var15) {
            for(var16 = var11; var16 <= var12; ++var16) {
               var17 = var3.add(var14, var15, var16);
               Material var18 = var1.getBlockState(var17).getBlock().getMaterial();
               boolean var19 = var18.isSolid();
               if (var15 == -1 && !var19) {
                  return false;
               }

               if (var15 == 4 && !var19) {
                  return false;
               }

               if ((var14 == var6 || var14 == var7 || var16 == var11 || var16 == var12) && var15 == 0 && var1.isAirBlock(var17) && var1.isAirBlock(var17.up())) {
                  ++var13;
               }
            }
         }
      }

      if (var13 >= 1 && var13 <= 5) {
         for(var14 = var6; var14 <= var7; ++var14) {
            for(var15 = 3; var15 >= -1; --var15) {
               for(var16 = var11; var16 <= var12; ++var16) {
                  var17 = var3.add(var14, var15, var16);
                  if (var14 != var6 && var15 != -1 && var16 != var11 && var14 != var7 && var15 != 4 && var16 != var12) {
                     if (var1.getBlockState(var17).getBlock() != Blocks.chest) {
                        var1.setBlockToAir(var17);
                     }
                  } else if (var17.getY() >= 0 && !var1.getBlockState(var17.down()).getBlock().getMaterial().isSolid()) {
                     var1.setBlockToAir(var17);
                  } else if (var1.getBlockState(var17).getBlock().getMaterial().isSolid() && var1.getBlockState(var17).getBlock() != Blocks.chest) {
                     if (var15 == -1 && var2.nextInt(4) != 0) {
                        var1.setBlockState(var17, Blocks.mossy_cobblestone.getDefaultState(), 2);
                     } else {
                        var1.setBlockState(var17, Blocks.cobblestone.getDefaultState(), 2);
                     }
                  }
               }
            }
         }

         for(var14 = 0; var14 < 2; ++var14) {
            for(var15 = 0; var15 < 3; ++var15) {
               var16 = var3.getX() + var2.nextInt(var5 * 2 + 1) - var5;
               int var24 = var3.getY();
               int var25 = var3.getZ() + var2.nextInt(var10 * 2 + 1) - var10;
               BlockPos var26 = new BlockPos(var16, var24, var25);
               if (var1.isAirBlock(var26)) {
                  int var20 = 0;
                  Iterator var22 = EnumFacing.Plane.HORIZONTAL.iterator();

                  while(var22.hasNext()) {
                     Object var21 = var22.next();
                     if (var1.getBlockState(var26.offset((EnumFacing)var21)).getBlock().getMaterial().isSolid()) {
                        ++var20;
                     }
                  }

                  if (var20 == 1) {
                     var1.setBlockState(var26, Blocks.chest.correctFacing(var1, var26, Blocks.chest.getDefaultState()), 2);
                     List var27 = WeightedRandomChestContent.func_177629_a(CHESTCONTENT, Items.enchanted_book.getRandom(var2));
                     TileEntity var28 = var1.getTileEntity(var26);
                     if (var28 instanceof TileEntityChest) {
                        WeightedRandomChestContent.generateChestContents(var2, var27, (TileEntityChest)var28, 8);
                     }
                     break;
                  }
               }
            }
         }

         var1.setBlockState(var3, Blocks.mob_spawner.getDefaultState(), 2);
         TileEntity var23 = var1.getTileEntity(var3);
         if (var23 instanceof TileEntityMobSpawner) {
            ((TileEntityMobSpawner)var23).getSpawnerBaseLogic().setEntityName(this.pickMobSpawner(var2));
         } else {
            field_175918_a.error("Failed to fetch mob spawner entity at (" + var3.getX() + ", " + var3.getY() + ", " + var3.getZ() + ")");
         }

         return true;
      } else {
         return false;
      }
   }

   private String pickMobSpawner(Random var1) {
      return SPAWNERTYPES[var1.nextInt(SPAWNERTYPES.length)];
   }
}
