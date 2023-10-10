package net.minecraft.world;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public final class SpawnerAnimals {
   private static final int MOB_COUNT_DIV = (int)Math.pow(17.0D, 2.0D);
   private final Set eligibleChunksForSpawning = Sets.newHashSet();

   public int findChunksForSpawning(WorldServer var1, boolean var2, boolean var3, boolean var4) {
      if (!var2 && !var3) {
         return 0;
      } else {
         this.eligibleChunksForSpawning.clear();
         int var5 = 0;
         Iterator var7 = var1.playerEntities.iterator();

         while(true) {
            EntityPlayer var6;
            int var9;
            int var12;
            ChunkCoordIntPair var14;
            do {
               if (!var7.hasNext()) {
                  int var36 = 0;
                  BlockPos var37 = var1.getSpawnPoint();
                  EnumCreatureType[] var40;
                  int var39 = (var40 = EnumCreatureType.values()).length;

                  label122:
                  for(var9 = 0; var9 < var39; ++var9) {
                     EnumCreatureType var38 = var40[var9];
                     if ((!var38.getPeacefulCreature() || var3) && (var38.getPeacefulCreature() || var2) && (!var38.getAnimal() || var4)) {
                        var12 = var1.countEntities(var38.getCreatureClass());
                        int var41 = var38.getMaxNumberOfCreature() * var5 / MOB_COUNT_DIV;
                        if (var12 <= var41) {
                           Iterator var15 = this.eligibleChunksForSpawning.iterator();

                           label119:
                           while(true) {
                              int var17;
                              int var18;
                              int var19;
                              Block var20;
                              do {
                                 if (!var15.hasNext()) {
                                    continue label122;
                                 }

                                 var14 = (ChunkCoordIntPair)var15.next();
                                 BlockPos var16 = getRandomChunkPosition(var1, var14.chunkXPos, var14.chunkZPos);
                                 var17 = var16.getX();
                                 var18 = var16.getY();
                                 var19 = var16.getZ();
                                 var20 = var1.getBlockState(var16).getBlock();
                              } while(var20.isNormalCube());

                              int var21 = 0;

                              for(int var22 = 0; var22 < 3; ++var22) {
                                 int var23 = var17;
                                 int var24 = var18;
                                 int var25 = var19;
                                 byte var26 = 6;
                                 BiomeGenBase.SpawnListEntry var27 = null;
                                 IEntityLivingData var28 = null;

                                 for(int var29 = 0; var29 < 4; ++var29) {
                                    var23 += var1.rand.nextInt(var26) - var1.rand.nextInt(var26);
                                    var24 += var1.rand.nextInt(1) - var1.rand.nextInt(1);
                                    var25 += var1.rand.nextInt(var26) - var1.rand.nextInt(var26);
                                    BlockPos var30 = new BlockPos(var23, var24, var25);
                                    float var31 = (float)var23 + 0.5F;
                                    float var32 = (float)var25 + 0.5F;
                                    if (!var1.isAnyPlayerWithinRangeAt((double)var31, (double)var24, (double)var32, 24.0D) && var37.distanceSq((double)var31, (double)var24, (double)var32) >= 576.0D) {
                                       if (var27 == null) {
                                          var27 = var1.getSpawnListEntryForTypeAt(var38, var30);
                                          if (var27 == null) {
                                             break;
                                          }
                                       }

                                       if (var1.canCreatureTypeSpawnHere(var38, var27, var30)) {
                                          EntitySpawnPlacementRegistry.getPlacementForEntity(var27.entityClass);
                                          if (var30 != false) {
                                             EntityLiving var33;
                                             try {
                                                var33 = (EntityLiving)var27.entityClass.getConstructor(World.class).newInstance(var1);
                                             } catch (Exception var35) {
                                                var35.printStackTrace();
                                                return var36;
                                             }

                                             var33.setLocationAndAngles((double)var31, (double)var24, (double)var32, var1.rand.nextFloat() * 360.0F, 0.0F);
                                             if (var33.getCanSpawnHere() && var33.isNotColliding()) {
                                                var28 = var33.onInitialSpawn(var1.getDifficultyForLocation(new BlockPos(var33)), var28);
                                                if (var33.isNotColliding()) {
                                                   ++var21;
                                                   var1.spawnEntityInWorld(var33);
                                                }

                                                if (var21 >= var33.getMaxSpawnedInChunk()) {
                                                   continue label119;
                                                }
                                             }

                                             var36 += var21;
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }

                  return var36;
               }

               var6 = (EntityPlayer)var7.next();
            } while(var6.isSpectator());

            int var8 = MathHelper.floor_double(var6.posX / 16.0D);
            var9 = MathHelper.floor_double(var6.posZ / 16.0D);
            byte var10 = 8;

            for(int var11 = -var10; var11 <= var10; ++var11) {
               for(var12 = -var10; var12 <= var10; ++var12) {
                  boolean var13 = var11 == -var10 || var11 == var10 || var12 == -var10 || var12 == var10;
                  var14 = new ChunkCoordIntPair(var11 + var8, var12 + var9);
                  if (!this.eligibleChunksForSpawning.contains(var14)) {
                     ++var5;
                     if (!var13 && var1.getWorldBorder().contains(var14)) {
                        this.eligibleChunksForSpawning.add(var14);
                     }
                  }
               }
            }
         }
      }
   }

   protected static BlockPos getRandomChunkPosition(World var0, int var1, int var2) {
      Chunk var3 = var0.getChunkFromChunkCoords(var1, var2);
      int var4 = var1 * 16 + var0.rand.nextInt(16);
      int var5 = var2 * 16 + var0.rand.nextInt(16);
      int var6 = MathHelper.func_154354_b(var3.getHeight(new BlockPos(var4, 0, var5)) + 1, 16);
      int var7 = var0.rand.nextInt(var6 > 0 ? var6 : var3.getTopFilledSegment() + 16 - 1);
      return new BlockPos(var4, var7, var5);
   }

   public static void performWorldGenSpawning(World var0, BiomeGenBase var1, int var2, int var3, int var4, int var5, Random var6) {
      List var7 = var1.getSpawnableList(EnumCreatureType.CREATURE);
      if (!var7.isEmpty()) {
         while(var6.nextFloat() < var1.getSpawningChance()) {
            BiomeGenBase.SpawnListEntry var8 = (BiomeGenBase.SpawnListEntry)WeightedRandom.getRandomItem(var0.rand, var7);
            int var9 = var8.minGroupCount + var6.nextInt(1 + var8.maxGroupCount - var8.minGroupCount);
            IEntityLivingData var10 = null;
            int var11 = var2 + var6.nextInt(var4);
            int var12 = var3 + var6.nextInt(var5);
            int var13 = var11;
            int var14 = var12;

            for(int var15 = 0; var15 < var9; ++var15) {
               boolean var16 = false;

               for(int var17 = 0; !var16 && var17 < 4; ++var17) {
                  BlockPos var18 = var0.getTopSolidOrLiquidBlock(new BlockPos(var11, 0, var12));
                  EntityLiving.SpawnPlacementType var10000 = EntityLiving.SpawnPlacementType.ON_GROUND;
                  if (var18 == false) {
                     EntityLiving var19;
                     try {
                        var19 = (EntityLiving)var8.entityClass.getConstructor(World.class).newInstance(var0);
                     } catch (Exception var22) {
                        var22.printStackTrace();
                        continue;
                     }

                     var19.setLocationAndAngles((double)((float)var11 + 0.5F), (double)var18.getY(), (double)((float)var12 + 0.5F), var6.nextFloat() * 360.0F, 0.0F);
                     var0.spawnEntityInWorld(var19);
                     var10 = var19.onInitialSpawn(var0.getDifficultyForLocation(new BlockPos(var19)), var10);
                     var16 = true;
                  }

                  var11 += var6.nextInt(5) - var6.nextInt(5);

                  for(var12 += var6.nextInt(5) - var6.nextInt(5); var11 < var2 || var11 >= var2 + var4 || var12 < var3 || var12 >= var3 + var4; var12 = var14 + var6.nextInt(5) - var6.nextInt(5)) {
                     var11 = var13 + var6.nextInt(5) - var6.nextInt(5);
                  }
               }
            }
         }
      }

   }
}
