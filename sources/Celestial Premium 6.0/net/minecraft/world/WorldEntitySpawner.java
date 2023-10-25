/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public final class WorldEntitySpawner {
    private static final int MOB_COUNT_DIV = (int)Math.pow(17.0, 2.0);
    private final Set<ChunkPos> eligibleChunksForSpawning = Sets.newHashSet();

    public int findChunksForSpawning(WorldServer worldServerIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnOnSetTickRate) {
        if (!spawnHostileMobs && !spawnPeacefulMobs) {
            return 0;
        }
        this.eligibleChunksForSpawning.clear();
        int i = 0;
        for (EntityPlayer entityplayer : worldServerIn.playerEntities) {
            if (entityplayer.isSpectator()) continue;
            int j = MathHelper.floor(entityplayer.posX / 16.0);
            int k = MathHelper.floor(entityplayer.posZ / 16.0);
            int l = 8;
            for (int i1 = -8; i1 <= 8; ++i1) {
                for (int j1 = -8; j1 <= 8; ++j1) {
                    PlayerChunkMapEntry playerchunkmapentry;
                    boolean flag = i1 == -8 || i1 == 8 || j1 == -8 || j1 == 8;
                    ChunkPos chunkpos = new ChunkPos(i1 + j, j1 + k);
                    if (this.eligibleChunksForSpawning.contains(chunkpos)) continue;
                    ++i;
                    if (flag || !worldServerIn.getWorldBorder().contains(chunkpos) || (playerchunkmapentry = worldServerIn.getPlayerChunkMap().getEntry(chunkpos.x, chunkpos.z)) == null || !playerchunkmapentry.isSentToPlayers()) continue;
                    this.eligibleChunksForSpawning.add(chunkpos);
                }
            }
        }
        int j4 = 0;
        BlockPos blockpos1 = worldServerIn.getSpawnPoint();
        for (EnumCreatureType enumcreaturetype : EnumCreatureType.values()) {
            int l4;
            int k4;
            if (enumcreaturetype.getPeacefulCreature() && !spawnPeacefulMobs || !enumcreaturetype.getPeacefulCreature() && !spawnHostileMobs || enumcreaturetype.getAnimal() && !spawnOnSetTickRate || (k4 = worldServerIn.countEntities(enumcreaturetype.getCreatureClass())) > (l4 = enumcreaturetype.getMaxNumberOfCreature() * i / MOB_COUNT_DIV)) continue;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            block6: for (ChunkPos chunkpos1 : this.eligibleChunksForSpawning) {
                BlockPos blockpos = WorldEntitySpawner.getRandomChunkPosition(worldServerIn, chunkpos1.x, chunkpos1.z);
                int k1 = blockpos.getX();
                int l1 = blockpos.getY();
                int i2 = blockpos.getZ();
                IBlockState iblockstate = worldServerIn.getBlockState(blockpos);
                if (iblockstate.isNormalCube()) continue;
                int j2 = 0;
                block7: for (int k2 = 0; k2 < 3; ++k2) {
                    int l2 = k1;
                    int i3 = l1;
                    int j3 = i2;
                    int k3 = 6;
                    Biome.SpawnListEntry biome$spawnlistentry = null;
                    IEntityLivingData ientitylivingdata = null;
                    int l3 = MathHelper.ceil(Math.random() * 4.0);
                    for (int i4 = 0; i4 < l3; ++i4) {
                        EntityLiving entityliving;
                        blockpos$mutableblockpos.setPos(l2 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6), i3 += worldServerIn.rand.nextInt(1) - worldServerIn.rand.nextInt(1), j3 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6));
                        float f = (float)l2 + 0.5f;
                        float f1 = (float)j3 + 0.5f;
                        if (worldServerIn.isAnyPlayerWithinRangeAt(f, i3, f1, 24.0) || !(blockpos1.distanceSq(f, i3, f1) >= 576.0)) continue;
                        if (biome$spawnlistentry == null && (biome$spawnlistentry = worldServerIn.getSpawnListEntryForTypeAt(enumcreaturetype, blockpos$mutableblockpos)) == null) continue block7;
                        if (!worldServerIn.canCreatureTypeSpawnHere(enumcreaturetype, biome$spawnlistentry, blockpos$mutableblockpos) || !WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(biome$spawnlistentry.entityClass), worldServerIn, blockpos$mutableblockpos)) continue;
                        try {
                            entityliving = biome$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldServerIn);
                        }
                        catch (Exception exception) {
                            exception.printStackTrace();
                            return j4;
                        }
                        entityliving.setLocationAndAngles(f, i3, f1, worldServerIn.rand.nextFloat() * 360.0f, 0.0f);
                        if (entityliving.getCanSpawnHere() && entityliving.isNotColliding()) {
                            ientitylivingdata = entityliving.onInitialSpawn(worldServerIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                            if (entityliving.isNotColliding()) {
                                ++j2;
                                worldServerIn.spawnEntityInWorld(entityliving);
                            } else {
                                entityliving.setDead();
                            }
                            if (j2 >= entityliving.getMaxSpawnedInChunk()) continue block6;
                        }
                        j4 += j2;
                    }
                }
            }
        }
        return j4;
    }

    private static BlockPos getRandomChunkPosition(World worldIn, int x, int z) {
        int j;
        int i;
        Chunk chunk = worldIn.getChunk(x, z);
        int k = MathHelper.roundUp(chunk.getHeight(new BlockPos(i = x * 16 + worldIn.rand.nextInt(16), 0, j = z * 16 + worldIn.rand.nextInt(16))) + 1, 16);
        int l = worldIn.rand.nextInt(k > 0 ? k : chunk.getTopFilledSegment() + 16 - 1);
        return new BlockPos(i, l, j);
    }

    public static boolean isValidEmptySpawnBlock(IBlockState state) {
        if (state.isBlockNormalCube()) {
            return false;
        }
        if (state.canProvidePower()) {
            return false;
        }
        if (state.getMaterial().isLiquid()) {
            return false;
        }
        return !BlockRailBase.isRailBlock(state);
    }

    public static boolean canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType spawnPlacementTypeIn, World worldIn, BlockPos pos) {
        if (!worldIn.getWorldBorder().contains(pos)) {
            return false;
        }
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (spawnPlacementTypeIn == EntityLiving.SpawnPlacementType.IN_WATER) {
            return iblockstate.getMaterial() == Material.WATER && worldIn.getBlockState(pos.down()).getMaterial() == Material.WATER && !worldIn.getBlockState(pos.up()).isNormalCube();
        }
        BlockPos blockpos = pos.down();
        if (!worldIn.getBlockState(blockpos).isFullyOpaque()) {
            return false;
        }
        Block block = worldIn.getBlockState(blockpos).getBlock();
        boolean flag = block != Blocks.BEDROCK && block != Blocks.BARRIER;
        return flag && WorldEntitySpawner.isValidEmptySpawnBlock(iblockstate) && WorldEntitySpawner.isValidEmptySpawnBlock(worldIn.getBlockState(pos.up()));
    }

    public static void performWorldGenSpawning(World worldIn, Biome biomeIn, int p_77191_2_, int p_77191_3_, int p_77191_4_, int p_77191_5_, Random randomIn) {
        List<Biome.SpawnListEntry> list = biomeIn.getSpawnableList(EnumCreatureType.CREATURE);
        if (!list.isEmpty()) {
            while (randomIn.nextFloat() < biomeIn.getSpawningChance()) {
                Biome.SpawnListEntry biome$spawnlistentry = WeightedRandom.getRandomItem(worldIn.rand, list);
                int i = biome$spawnlistentry.minGroupCount + randomIn.nextInt(1 + biome$spawnlistentry.maxGroupCount - biome$spawnlistentry.minGroupCount);
                IEntityLivingData ientitylivingdata = null;
                int j = p_77191_2_ + randomIn.nextInt(p_77191_4_);
                int k = p_77191_3_ + randomIn.nextInt(p_77191_5_);
                int l = j;
                int i1 = k;
                for (int j1 = 0; j1 < i; ++j1) {
                    boolean flag = false;
                    for (int k1 = 0; !flag && k1 < 4; ++k1) {
                        BlockPos blockpos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k));
                        if (WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos)) {
                            EntityLiving entityliving;
                            try {
                                entityliving = biome$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldIn);
                            }
                            catch (Exception exception) {
                                exception.printStackTrace();
                                continue;
                            }
                            entityliving.setLocationAndAngles((float)j + 0.5f, blockpos.getY(), (float)k + 0.5f, randomIn.nextFloat() * 360.0f, 0.0f);
                            worldIn.spawnEntityInWorld(entityliving);
                            ientitylivingdata = entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                            flag = true;
                        }
                        j += randomIn.nextInt(5) - randomIn.nextInt(5);
                        k += randomIn.nextInt(5) - randomIn.nextInt(5);
                        while (j < p_77191_2_ || j >= p_77191_2_ + p_77191_4_ || k < p_77191_3_ || k >= p_77191_3_ + p_77191_4_) {
                            j = l + randomIn.nextInt(5) - randomIn.nextInt(5);
                            k = i1 + randomIn.nextInt(5) - randomIn.nextInt(5);
                        }
                    }
                }
            }
        }
    }
}

