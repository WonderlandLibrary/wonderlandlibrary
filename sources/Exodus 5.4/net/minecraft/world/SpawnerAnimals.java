/*
 * Decompiled with CFR 0.152.
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
    private final Set<ChunkCoordIntPair> eligibleChunksForSpawning = Sets.newHashSet();
    private static final int MOB_COUNT_DIV = (int)Math.pow(17.0, 2.0);

    protected static BlockPos getRandomChunkPosition(World world, int n, int n2) {
        Chunk chunk = world.getChunkFromChunkCoords(n, n2);
        int n3 = n * 16 + world.rand.nextInt(16);
        int n4 = n2 * 16 + world.rand.nextInt(16);
        int n5 = MathHelper.func_154354_b(chunk.getHeight(new BlockPos(n3, 0, n4)) + 1, 16);
        int n6 = world.rand.nextInt(n5 > 0 ? n5 : chunk.getTopFilledSegment() + 16 - 1);
        return new BlockPos(n3, n6, n4);
    }

    public static void performWorldGenSpawning(World world, BiomeGenBase biomeGenBase, int n, int n2, int n3, int n4, Random random) {
        List<BiomeGenBase.SpawnListEntry> list = biomeGenBase.getSpawnableList(EnumCreatureType.CREATURE);
        if (!list.isEmpty()) {
            while (random.nextFloat() < biomeGenBase.getSpawningChance()) {
                BiomeGenBase.SpawnListEntry spawnListEntry = WeightedRandom.getRandomItem(world.rand, list);
                int n5 = spawnListEntry.minGroupCount + random.nextInt(1 + spawnListEntry.maxGroupCount - spawnListEntry.minGroupCount);
                IEntityLivingData iEntityLivingData = null;
                int n6 = n + random.nextInt(n3);
                int n7 = n2 + random.nextInt(n4);
                int n8 = n6;
                int n9 = n7;
                int n10 = 0;
                while (n10 < n5) {
                    boolean bl = false;
                    int n11 = 0;
                    while (!bl && n11 < 4) {
                        block8: {
                            BlockPos blockPos = world.getTopSolidOrLiquidBlock(new BlockPos(n6, 0, n7));
                            if (SpawnerAnimals.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, world, blockPos)) {
                                EntityLiving entityLiving;
                                try {
                                    entityLiving = spawnListEntry.entityClass.getConstructor(World.class).newInstance(world);
                                }
                                catch (Exception exception) {
                                    exception.printStackTrace();
                                    break block8;
                                }
                                entityLiving.setLocationAndAngles((float)n6 + 0.5f, blockPos.getY(), (float)n7 + 0.5f, random.nextFloat() * 360.0f, 0.0f);
                                world.spawnEntityInWorld(entityLiving);
                                iEntityLivingData = entityLiving.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityLiving)), iEntityLivingData);
                                bl = true;
                            }
                            n6 += random.nextInt(5) - random.nextInt(5);
                            n7 += random.nextInt(5) - random.nextInt(5);
                            while (n6 < n || n6 >= n + n3 || n7 < n2 || n7 >= n2 + n3) {
                                n6 = n8 + random.nextInt(5) - random.nextInt(5);
                                n7 = n9 + random.nextInt(5) - random.nextInt(5);
                            }
                        }
                        ++n11;
                    }
                    ++n10;
                }
            }
        }
    }

    public int findChunksForSpawning(WorldServer worldServer, boolean bl, boolean bl2, boolean bl3) {
        int n;
        int n2;
        int n3;
        int n4;
        if (!bl && !bl2) {
            return 0;
        }
        this.eligibleChunksForSpawning.clear();
        int n5 = 0;
        for (EntityPlayer entityPlayer : worldServer.playerEntities) {
            if (entityPlayer.isSpectator()) continue;
            int n6 = MathHelper.floor_double(entityPlayer.posX / 16.0);
            n4 = MathHelper.floor_double(entityPlayer.posZ / 16.0);
            n3 = 8;
            int n7 = -n3;
            while (n7 <= n3) {
                n2 = -n3;
                while (n2 <= n3) {
                    n = n7 != -n3 && n7 != n3 && n2 != -n3 && n2 != n3 ? 0 : 1;
                    ChunkCoordIntPair chunkCoordIntPair = new ChunkCoordIntPair(n7 + n6, n2 + n4);
                    if (!this.eligibleChunksForSpawning.contains(chunkCoordIntPair)) {
                        ++n5;
                        if (n == 0 && worldServer.getWorldBorder().contains(chunkCoordIntPair)) {
                            this.eligibleChunksForSpawning.add(chunkCoordIntPair);
                        }
                    }
                    ++n2;
                }
                ++n7;
            }
        }
        int n8 = 0;
        BlockPos blockPos = worldServer.getSpawnPoint();
        EnumCreatureType[] enumCreatureTypeArray = EnumCreatureType.values();
        n3 = enumCreatureTypeArray.length;
        n4 = 0;
        while (n4 < n3) {
            EnumCreatureType enumCreatureType = enumCreatureTypeArray[n4];
            if (!(enumCreatureType.getPeacefulCreature() && !bl2 || !enumCreatureType.getPeacefulCreature() && !bl || enumCreatureType.getAnimal() && !bl3 || (n2 = worldServer.countEntities(enumCreatureType.getCreatureClass())) > (n = enumCreatureType.getMaxNumberOfCreature() * n5 / MOB_COUNT_DIV))) {
                block6: for (ChunkCoordIntPair chunkCoordIntPair : this.eligibleChunksForSpawning) {
                    BlockPos blockPos2 = SpawnerAnimals.getRandomChunkPosition(worldServer, chunkCoordIntPair.chunkXPos, chunkCoordIntPair.chunkZPos);
                    int n9 = blockPos2.getX();
                    int n10 = blockPos2.getY();
                    int n11 = blockPos2.getZ();
                    Block block = worldServer.getBlockState(blockPos2).getBlock();
                    if (block.isNormalCube()) continue;
                    int n12 = 0;
                    int n13 = 0;
                    while (n13 < 3) {
                        int n14 = n9;
                        int n15 = n10;
                        int n16 = n11;
                        int n17 = 6;
                        BiomeGenBase.SpawnListEntry spawnListEntry = null;
                        IEntityLivingData iEntityLivingData = null;
                        int n18 = 0;
                        while (n18 < 4) {
                            BlockPos blockPos3 = new BlockPos(n14 += worldServer.rand.nextInt(n17) - worldServer.rand.nextInt(n17), n15 += worldServer.rand.nextInt(1) - worldServer.rand.nextInt(1), n16 += worldServer.rand.nextInt(n17) - worldServer.rand.nextInt(n17));
                            float f = (float)n14 + 0.5f;
                            float f2 = (float)n16 + 0.5f;
                            if (!worldServer.isAnyPlayerWithinRangeAt(f, n15, f2, 24.0) && blockPos.distanceSq(f, n15, f2) >= 576.0) {
                                if (spawnListEntry == null && (spawnListEntry = worldServer.getSpawnListEntryForTypeAt(enumCreatureType, blockPos3)) == null) break;
                                if (worldServer.canCreatureTypeSpawnHere(enumCreatureType, spawnListEntry, blockPos3) && SpawnerAnimals.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(spawnListEntry.entityClass), worldServer, blockPos3)) {
                                    EntityLiving entityLiving;
                                    try {
                                        entityLiving = spawnListEntry.entityClass.getConstructor(World.class).newInstance(worldServer);
                                    }
                                    catch (Exception exception) {
                                        exception.printStackTrace();
                                        return n8;
                                    }
                                    entityLiving.setLocationAndAngles(f, n15, f2, worldServer.rand.nextFloat() * 360.0f, 0.0f);
                                    if (entityLiving.getCanSpawnHere() && entityLiving.isNotColliding()) {
                                        iEntityLivingData = entityLiving.onInitialSpawn(worldServer.getDifficultyForLocation(new BlockPos(entityLiving)), iEntityLivingData);
                                        if (entityLiving.isNotColliding()) {
                                            ++n12;
                                            worldServer.spawnEntityInWorld(entityLiving);
                                        }
                                        if (n12 >= entityLiving.getMaxSpawnedInChunk()) continue block6;
                                    }
                                    n8 += n12;
                                }
                            }
                            ++n18;
                        }
                        ++n13;
                    }
                }
            }
            ++n4;
        }
        return n8;
    }

    public static boolean canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType spawnPlacementType, World world, BlockPos blockPos) {
        boolean bl;
        if (!world.getWorldBorder().contains(blockPos)) {
            return false;
        }
        Block block = world.getBlockState(blockPos).getBlock();
        if (spawnPlacementType == EntityLiving.SpawnPlacementType.IN_WATER) {
            return block.getMaterial().isLiquid() && world.getBlockState(blockPos.down()).getBlock().getMaterial().isLiquid() && !world.getBlockState(blockPos.up()).getBlock().isNormalCube();
        }
        BlockPos blockPos2 = blockPos.down();
        if (!World.doesBlockHaveSolidTopSurface(world, blockPos2)) {
            return false;
        }
        Block block2 = world.getBlockState(blockPos2).getBlock();
        boolean bl2 = bl = block2 != Blocks.bedrock && block2 != Blocks.barrier;
        return bl && !block.isNormalCube() && !block.getMaterial().isLiquid() && !world.getBlockState(blockPos.up()).getBlock().isNormalCube();
    }
}

