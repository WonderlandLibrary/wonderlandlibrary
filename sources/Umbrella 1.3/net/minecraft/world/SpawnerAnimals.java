/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 */
package net.minecraft.world;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public final class SpawnerAnimals {
    private static final int field_180268_a = (int)Math.pow(17.0, 2.0);
    private final Set eligibleChunksForSpawning = Sets.newHashSet();
    private static final String __OBFID = "CL_00000152";

    public int findChunksForSpawning(WorldServer p_77192_1_, boolean p_77192_2_, boolean p_77192_3_, boolean p_77192_4_) {
        int var12;
        if (!p_77192_2_ && !p_77192_3_) {
            return 0;
        }
        this.eligibleChunksForSpawning.clear();
        int var5 = 0;
        for (EntityPlayer var7 : p_77192_1_.playerEntities) {
            if (var7.func_175149_v()) continue;
            int var8 = MathHelper.floor_double(var7.posX / 16.0);
            int var9 = MathHelper.floor_double(var7.posZ / 16.0);
            int var10 = 8;
            for (int var11 = -var10; var11 <= var10; ++var11) {
                for (var12 = -var10; var12 <= var10; ++var12) {
                    boolean var13 = var11 == -var10 || var11 == var10 || var12 == -var10 || var12 == var10;
                    ChunkCoordIntPair var14 = new ChunkCoordIntPair(var11 + var8, var12 + var9);
                    if (this.eligibleChunksForSpawning.contains(var14)) continue;
                    ++var5;
                    if (var13 || !p_77192_1_.getWorldBorder().contains(var14)) continue;
                    this.eligibleChunksForSpawning.add(var14);
                }
            }
        }
        int var36 = 0;
        BlockPos var37 = p_77192_1_.getSpawnPoint();
        for (EnumCreatureType var40 : EnumCreatureType.values()) {
            int var41;
            if (var40.getPeacefulCreature() && !p_77192_3_ || !var40.getPeacefulCreature() && !p_77192_2_ || var40.getAnimal() && !p_77192_4_ || (var12 = p_77192_1_.countEntities(var40.getCreatureClass())) > (var41 = var40.getMaxNumberOfCreature() * var5 / field_180268_a)) continue;
            block6: for (ChunkCoordIntPair var15 : this.eligibleChunksForSpawning) {
                BlockPos var16 = SpawnerAnimals.func_180621_a(p_77192_1_, var15.chunkXPos, var15.chunkZPos);
                int var17 = var16.getX();
                int var18 = var16.getY();
                int var19 = var16.getZ();
                Block var20 = p_77192_1_.getBlockState(var16).getBlock();
                if (var20.isNormalCube()) continue;
                int var21 = 0;
                block7: for (int var22 = 0; var22 < 3; ++var22) {
                    int var23 = var17;
                    int var24 = var18;
                    int var25 = var19;
                    int var26 = 6;
                    BiomeGenBase.SpawnListEntry var27 = null;
                    IEntityLivingData var28 = null;
                    for (int var29 = 0; var29 < 4; ++var29) {
                        EntityLiving var33;
                        BlockPos var30 = new BlockPos(var23 += p_77192_1_.rand.nextInt(var26) - p_77192_1_.rand.nextInt(var26), var24 += p_77192_1_.rand.nextInt(1) - p_77192_1_.rand.nextInt(1), var25 += p_77192_1_.rand.nextInt(var26) - p_77192_1_.rand.nextInt(var26));
                        float var31 = (float)var23 + 0.5f;
                        float var32 = (float)var25 + 0.5f;
                        if (p_77192_1_.func_175636_b(var31, var24, var32, 24.0) || !(var37.distanceSq(var31, var24, var32) >= 576.0)) continue;
                        if (var27 == null && (var27 = p_77192_1_.func_175734_a(var40, var30)) == null) continue block7;
                        if (!p_77192_1_.func_175732_a(var40, var27, var30) || !SpawnerAnimals.func_180267_a(EntitySpawnPlacementRegistry.func_180109_a(var27.entityClass), p_77192_1_, var30)) continue;
                        try {
                            var33 = (EntityLiving)var27.entityClass.getConstructor(World.class).newInstance(p_77192_1_);
                        }
                        catch (Exception var35) {
                            var35.printStackTrace();
                            return var36;
                        }
                        var33.setLocationAndAngles(var31, var24, var32, p_77192_1_.rand.nextFloat() * 360.0f, 0.0f);
                        if (var33.getCanSpawnHere() && var33.func_180799_ab()) {
                            var28 = var33.func_180482_a(p_77192_1_.getDifficultyForLocation(new BlockPos(var33)), var28);
                            if (var33.func_180799_ab()) {
                                ++var21;
                                p_77192_1_.spawnEntityInWorld(var33);
                            }
                            if (var21 >= var33.getMaxSpawnedInChunk()) continue block6;
                        }
                        var36 += var21;
                    }
                }
            }
        }
        return var36;
    }

    protected static BlockPos func_180621_a(World worldIn, int p_180621_1_, int p_180621_2_) {
        int var5;
        int var4;
        Chunk var3 = worldIn.getChunkFromChunkCoords(p_180621_1_, p_180621_2_);
        int var6 = MathHelper.func_154354_b(var3.getHeight(new BlockPos(var4 = p_180621_1_ * 16 + worldIn.rand.nextInt(16), 0, var5 = p_180621_2_ * 16 + worldIn.rand.nextInt(16))) + 1, 16);
        int var7 = worldIn.rand.nextInt(var6 > 0 ? var6 : var3.getTopFilledSegment() + 16 - 1);
        return new BlockPos(var4, var7, var5);
    }

    public static boolean func_180267_a(EntityLiving.SpawnPlacementType p_180267_0_, World worldIn, BlockPos p_180267_2_) {
        boolean var6;
        if (!worldIn.getWorldBorder().contains(p_180267_2_)) {
            return false;
        }
        Block var3 = worldIn.getBlockState(p_180267_2_).getBlock();
        if (p_180267_0_ == EntityLiving.SpawnPlacementType.IN_WATER) {
            return var3.getMaterial().isLiquid() && worldIn.getBlockState(p_180267_2_.offsetDown()).getBlock().getMaterial().isLiquid() && !worldIn.getBlockState(p_180267_2_.offsetUp()).getBlock().isNormalCube();
        }
        BlockPos var4 = p_180267_2_.offsetDown();
        if (!World.doesBlockHaveSolidTopSurface(worldIn, var4)) {
            return false;
        }
        Block var5 = worldIn.getBlockState(var4).getBlock();
        boolean bl = var6 = var5 != Blocks.bedrock && var5 != Blocks.barrier;
        return var6 && !var3.isNormalCube() && !var3.getMaterial().isLiquid() && !worldIn.getBlockState(p_180267_2_.offsetUp()).getBlock().isNormalCube();
    }

    public static void performWorldGenSpawning(World worldIn, BiomeGenBase p_77191_1_, int p_77191_2_, int p_77191_3_, int p_77191_4_, int p_77191_5_, Random p_77191_6_) {
        List var7 = p_77191_1_.getSpawnableList(EnumCreatureType.CREATURE);
        if (!var7.isEmpty()) {
            while (p_77191_6_.nextFloat() < p_77191_1_.getSpawningChance()) {
                BiomeGenBase.SpawnListEntry var8 = (BiomeGenBase.SpawnListEntry)WeightedRandom.getRandomItem(worldIn.rand, var7);
                int var9 = var8.minGroupCount + p_77191_6_.nextInt(1 + var8.maxGroupCount - var8.minGroupCount);
                IEntityLivingData var10 = null;
                int var11 = p_77191_2_ + p_77191_6_.nextInt(p_77191_4_);
                int var12 = p_77191_3_ + p_77191_6_.nextInt(p_77191_5_);
                int var13 = var11;
                int var14 = var12;
                for (int var15 = 0; var15 < var9; ++var15) {
                    boolean var16 = false;
                    for (int var17 = 0; !var16 && var17 < 4; ++var17) {
                        BlockPos var18 = worldIn.func_175672_r(new BlockPos(var11, 0, var12));
                        if (SpawnerAnimals.func_180267_a(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, var18)) {
                            EntityLiving var19;
                            try {
                                var19 = (EntityLiving)var8.entityClass.getConstructor(World.class).newInstance(worldIn);
                            }
                            catch (Exception var21) {
                                var21.printStackTrace();
                                continue;
                            }
                            var19.setLocationAndAngles((float)var11 + 0.5f, var18.getY(), (float)var12 + 0.5f, p_77191_6_.nextFloat() * 360.0f, 0.0f);
                            worldIn.spawnEntityInWorld(var19);
                            var10 = var19.func_180482_a(worldIn.getDifficultyForLocation(new BlockPos(var19)), var10);
                            var16 = true;
                        }
                        while ((var11 += p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5)) < p_77191_2_ || var11 >= p_77191_2_ + p_77191_4_ || (var12 += p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5)) < p_77191_3_ || var12 >= p_77191_3_ + p_77191_4_) {
                            var11 = var13 + p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5);
                            var12 = var14 + p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5);
                        }
                    }
                }
            }
        }
    }
}

