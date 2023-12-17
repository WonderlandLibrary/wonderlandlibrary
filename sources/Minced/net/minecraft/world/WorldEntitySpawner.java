// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.util.WeightedRandom;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockRailBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.biome.Biome;
import net.minecraft.block.state.IBlockState;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.server.management.PlayerChunkMapEntry;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.optifine.reflect.ReflectorForge;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import java.util.List;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.optifine.reflect.Reflector;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.optifine.BlockPosM;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import java.util.HashMap;
import com.google.common.collect.Sets;
import net.minecraft.entity.EntityLiving;
import java.util.Map;
import net.minecraft.util.math.ChunkPos;
import java.util.Set;

public final class WorldEntitySpawner
{
    private static final int MOB_COUNT_DIV;
    private final Set<ChunkPos> eligibleChunksForSpawning;
    private Map<Class, EntityLiving> mapSampleEntitiesByClass;
    private int lastPlayerChunkX;
    private int lastPlayerChunkZ;
    private int countChunkPos;
    
    public WorldEntitySpawner() {
        this.eligibleChunksForSpawning = (Set<ChunkPos>)Sets.newHashSet();
        this.mapSampleEntitiesByClass = new HashMap<Class, EntityLiving>();
        this.lastPlayerChunkX = Integer.MAX_VALUE;
        this.lastPlayerChunkZ = Integer.MAX_VALUE;
    }
    
    public int findChunksForSpawning(final WorldServer worldServerIn, final boolean spawnHostileMobs, final boolean spawnPeacefulMobs, final boolean spawnOnSetTickRate) {
        if (!spawnHostileMobs && !spawnPeacefulMobs) {
            return 0;
        }
        boolean flag = true;
        EntityPlayer entityplayer = null;
        if (worldServerIn.playerEntities.size() == 1) {
            entityplayer = worldServerIn.playerEntities.get(0);
            if (this.eligibleChunksForSpawning.size() > 0 && entityplayer != null && entityplayer.chunkCoordX == this.lastPlayerChunkX && entityplayer.chunkCoordZ == this.lastPlayerChunkZ) {
                flag = false;
            }
        }
        if (flag) {
            this.eligibleChunksForSpawning.clear();
            int i = 0;
            for (final EntityPlayer entityplayer2 : worldServerIn.playerEntities) {
                if (!entityplayer2.isSpectator()) {
                    final int j = MathHelper.floor(entityplayer2.posX / 16.0);
                    final int k = MathHelper.floor(entityplayer2.posZ / 16.0);
                    final int l = 8;
                    for (int i2 = -8; i2 <= 8; ++i2) {
                        for (int j2 = -8; j2 <= 8; ++j2) {
                            final boolean flag2 = i2 == -8 || i2 == 8 || j2 == -8 || j2 == 8;
                            final ChunkPos chunkpos = new ChunkPos(i2 + j, j2 + k);
                            if (!this.eligibleChunksForSpawning.contains(chunkpos)) {
                                ++i;
                                if (!flag2 && worldServerIn.getWorldBorder().contains(chunkpos)) {
                                    final PlayerChunkMapEntry playerchunkmapentry = worldServerIn.getPlayerChunkMap().getEntry(chunkpos.x, chunkpos.z);
                                    if (playerchunkmapentry != null && playerchunkmapentry.isSentToPlayers()) {
                                        this.eligibleChunksForSpawning.add(chunkpos);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.countChunkPos = i;
            if (entityplayer != null) {
                this.lastPlayerChunkX = entityplayer.chunkCoordX;
                this.lastPlayerChunkZ = entityplayer.chunkCoordZ;
            }
        }
        int k2 = 0;
        final BlockPos blockpos1 = worldServerIn.getSpawnPoint();
        final BlockPosM blockposm = new BlockPosM(0, 0, 0);
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (final EnumCreatureType enumcreaturetype : EnumCreatureType.values()) {
            if ((!enumcreaturetype.getPeacefulCreature() || spawnPeacefulMobs) && (enumcreaturetype.getPeacefulCreature() || spawnHostileMobs) && (!enumcreaturetype.getAnimal() || spawnOnSetTickRate)) {
                final int l2 = Reflector.ForgeWorld_countEntities.exists() ? Reflector.callInt(worldServerIn, Reflector.ForgeWorld_countEntities, enumcreaturetype, true) : worldServerIn.countEntities(enumcreaturetype.getCreatureClass());
                final int i3 = enumcreaturetype.getMaxNumberOfCreature() * this.countChunkPos / WorldEntitySpawner.MOB_COUNT_DIV;
                if (l2 <= i3) {
                    Collection<ChunkPos> collection = this.eligibleChunksForSpawning;
                    if (Reflector.ForgeHooksClient.exists()) {
                        final ArrayList<ChunkPos> arraylist = (ArrayList<ChunkPos>)Lists.newArrayList((Iterable)collection);
                        Collections.shuffle(arraylist);
                        collection = arraylist;
                    }
                Label_0579:
                    while (true) {
                        for (final ChunkPos chunkpos2 : collection) {
                            final BlockPos blockpos2 = getRandomChunkPosition(worldServerIn, chunkpos2.x, chunkpos2.z, blockposm);
                            final int k3 = blockpos2.getX();
                            final int l3 = blockpos2.getY();
                            final int i4 = blockpos2.getZ();
                            final IBlockState iblockstate = worldServerIn.getBlockState(blockpos2);
                            if (!iblockstate.isNormalCube()) {
                                int j3 = 0;
                                for (int k4 = 0; k4 < 3; ++k4) {
                                    int l4 = k3;
                                    int i5 = l3;
                                    int j4 = i4;
                                    final int k5 = 6;
                                    Biome.SpawnListEntry biome$spawnlistentry = null;
                                    IEntityLivingData ientitylivingdata = null;
                                    for (int l5 = MathHelper.ceil(Math.random() * 4.0), i6 = 0; i6 < l5; ++i6) {
                                        l4 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6);
                                        i5 += worldServerIn.rand.nextInt(1) - worldServerIn.rand.nextInt(1);
                                        j4 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6);
                                        blockpos$mutableblockpos.setPos(l4, i5, j4);
                                        final float f = l4 + 0.5f;
                                        final float f2 = j4 + 0.5f;
                                        if (!worldServerIn.isAnyPlayerWithinRangeAt(f, i5, f2, 24.0) && blockpos1.distanceSq(f, i5, f2) >= 576.0) {
                                            if (biome$spawnlistentry == null) {
                                                biome$spawnlistentry = worldServerIn.getSpawnListEntryForTypeAt(enumcreaturetype, blockpos$mutableblockpos);
                                                if (biome$spawnlistentry == null) {
                                                    break;
                                                }
                                            }
                                            if (worldServerIn.canCreatureTypeSpawnHere(enumcreaturetype, biome$spawnlistentry, blockpos$mutableblockpos) && canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(biome$spawnlistentry.entityClass), worldServerIn, blockpos$mutableblockpos)) {
                                                EntityLiving entityliving;
                                                try {
                                                    entityliving = this.mapSampleEntitiesByClass.get(biome$spawnlistentry.entityClass);
                                                    if (entityliving == null) {
                                                        if (Reflector.ForgeBiomeSpawnListEntry_newInstance.exists()) {
                                                            entityliving = (EntityLiving)Reflector.call(biome$spawnlistentry, Reflector.ForgeBiomeSpawnListEntry_newInstance, worldServerIn);
                                                        }
                                                        else {
                                                            entityliving = (EntityLiving)biome$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldServerIn);
                                                        }
                                                        this.mapSampleEntitiesByClass.put(biome$spawnlistentry.entityClass, entityliving);
                                                    }
                                                }
                                                catch (Exception exception) {
                                                    exception.printStackTrace();
                                                    return k2;
                                                }
                                                entityliving.setLocationAndAngles(f, i5, f2, worldServerIn.rand.nextFloat() * 360.0f, 0.0f);
                                                final boolean flag3 = Reflector.ForgeEventFactory_canEntitySpawn.exists() ? ReflectorForge.canEntitySpawn(entityliving, worldServerIn, f, (float)i5, f2) : (entityliving.getCanSpawnHere() && entityliving.isNotColliding());
                                                if (flag3) {
                                                    this.mapSampleEntitiesByClass.remove(biome$spawnlistentry.entityClass);
                                                    if (!ReflectorForge.doSpecialSpawn(entityliving, worldServerIn, f, i5, f2)) {
                                                        ientitylivingdata = entityliving.onInitialSpawn(worldServerIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                                                    }
                                                    if (entityliving.isNotColliding()) {
                                                        ++j3;
                                                        worldServerIn.spawnEntity(entityliving);
                                                    }
                                                    else {
                                                        entityliving.setDead();
                                                    }
                                                    final int j5 = Reflector.ForgeEventFactory_getMaxSpawnPackSize.exists() ? Reflector.callInt(Reflector.ForgeEventFactory_getMaxSpawnPackSize, entityliving) : entityliving.getMaxSpawnedInChunk();
                                                    if (j3 >= j5) {
                                                        continue Label_0579;
                                                    }
                                                }
                                                k2 += j3;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        return k2;
    }
    
    private static BlockPos getRandomChunkPosition(final World worldIn, final int x, final int z) {
        final Chunk chunk = worldIn.getChunk(x, z);
        final int i = x * 16 + worldIn.rand.nextInt(16);
        final int j = z * 16 + worldIn.rand.nextInt(16);
        final int k = MathHelper.roundUp(chunk.getHeight(new BlockPos(i, 0, j)) + 1, 16);
        final int l = worldIn.rand.nextInt((k > 0) ? k : (chunk.getTopFilledSegment() + 16 - 1));
        return new BlockPos(i, l, j);
    }
    
    private static BlockPosM getRandomChunkPosition(final World p_getRandomChunkPosition_0_, final int p_getRandomChunkPosition_1_, final int p_getRandomChunkPosition_2_, final BlockPosM p_getRandomChunkPosition_3_) {
        final Chunk chunk = p_getRandomChunkPosition_0_.getChunk(p_getRandomChunkPosition_1_, p_getRandomChunkPosition_2_);
        final int i = p_getRandomChunkPosition_1_ * 16 + p_getRandomChunkPosition_0_.rand.nextInt(16);
        final int j = p_getRandomChunkPosition_2_ * 16 + p_getRandomChunkPosition_0_.rand.nextInt(16);
        final int k = MathHelper.roundUp(chunk.getHeightValue(i & 0xF, j & 0xF) + 1, 16);
        final int l = p_getRandomChunkPosition_0_.rand.nextInt((k > 0) ? k : (chunk.getTopFilledSegment() + 16 - 1));
        p_getRandomChunkPosition_3_.setXyz(i, l, j);
        return p_getRandomChunkPosition_3_;
    }
    
    public static boolean isValidEmptySpawnBlock(final IBlockState state) {
        return !state.isBlockNormalCube() && !state.canProvidePower() && !state.getMaterial().isLiquid() && !BlockRailBase.isRailBlock(state);
    }
    
    public static boolean canCreatureTypeSpawnAtLocation(final EntityLiving.SpawnPlacementType spawnPlacementTypeIn, final World worldIn, final BlockPos pos) {
        return worldIn.getWorldBorder().contains(pos) && spawnPlacementTypeIn != null && spawnPlacementTypeIn.canSpawnAt(worldIn, pos);
    }
    
    public static boolean canCreatureTypeSpawnBody(final EntityLiving.SpawnPlacementType p_canCreatureTypeSpawnBody_0_, final World p_canCreatureTypeSpawnBody_1_, final BlockPos p_canCreatureTypeSpawnBody_2_) {
        final IBlockState iblockstate = p_canCreatureTypeSpawnBody_1_.getBlockState(p_canCreatureTypeSpawnBody_2_);
        if (p_canCreatureTypeSpawnBody_0_ == EntityLiving.SpawnPlacementType.IN_WATER) {
            return iblockstate.getMaterial() == Material.WATER && p_canCreatureTypeSpawnBody_1_.getBlockState(p_canCreatureTypeSpawnBody_2_.down()).getMaterial() == Material.WATER && !p_canCreatureTypeSpawnBody_1_.getBlockState(p_canCreatureTypeSpawnBody_2_.up()).isNormalCube();
        }
        final BlockPos blockpos = p_canCreatureTypeSpawnBody_2_.down();
        final IBlockState iblockstate2 = p_canCreatureTypeSpawnBody_1_.getBlockState(blockpos);
        final boolean flag = Reflector.ForgeBlock_canCreatureSpawn.exists() ? Reflector.callBoolean(iblockstate2.getBlock(), Reflector.ForgeBlock_canCreatureSpawn, iblockstate2, p_canCreatureTypeSpawnBody_1_, blockpos, p_canCreatureTypeSpawnBody_0_) : iblockstate2.isTopSolid();
        if (!flag) {
            return false;
        }
        final Block block = p_canCreatureTypeSpawnBody_1_.getBlockState(blockpos).getBlock();
        final boolean flag2 = block != Blocks.BEDROCK && block != Blocks.BARRIER;
        return flag2 && isValidEmptySpawnBlock(iblockstate) && isValidEmptySpawnBlock(p_canCreatureTypeSpawnBody_1_.getBlockState(p_canCreatureTypeSpawnBody_2_.up()));
    }
    
    public static void performWorldGenSpawning(final World worldIn, final Biome biomeIn, final int centerX, final int centerZ, final int diameterX, final int diameterZ, final Random randomIn) {
        final List<Biome.SpawnListEntry> list = biomeIn.getSpawnableList(EnumCreatureType.CREATURE);
        if (!list.isEmpty()) {
            while (randomIn.nextFloat() < biomeIn.getSpawningChance()) {
                final Biome.SpawnListEntry biome$spawnlistentry = WeightedRandom.getRandomItem(worldIn.rand, list);
                final int i = biome$spawnlistentry.minGroupCount + randomIn.nextInt(1 + biome$spawnlistentry.maxGroupCount - biome$spawnlistentry.minGroupCount);
                IEntityLivingData ientitylivingdata = null;
                int j = centerX + randomIn.nextInt(diameterX);
                int k = centerZ + randomIn.nextInt(diameterZ);
                final int l = j;
                final int i2 = k;
                for (int j2 = 0; j2 < i; ++j2) {
                    boolean flag = false;
                    for (int k2 = 0; !flag && k2 < 4; ++k2) {
                        final BlockPos blockpos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k));
                        if (canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos)) {
                            EntityLiving entityliving;
                            try {
                                if (Reflector.ForgeBiomeSpawnListEntry_newInstance.exists()) {
                                    entityliving = (EntityLiving)Reflector.call(biome$spawnlistentry, Reflector.ForgeBiomeSpawnListEntry_newInstance, worldIn);
                                }
                                else {
                                    entityliving = (EntityLiving)biome$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldIn);
                                }
                            }
                            catch (Exception exception) {
                                exception.printStackTrace();
                                continue;
                            }
                            if (Reflector.ForgeEventFactory_canEntitySpawn.exists()) {
                                final Object object = Reflector.call(Reflector.ForgeEventFactory_canEntitySpawn, entityliving, worldIn, j + 0.5f, blockpos.getY(), k + 0.5f, false);
                                if (object == ReflectorForge.EVENT_RESULT_DENY) {
                                    continue;
                                }
                            }
                            entityliving.setLocationAndAngles(j + 0.5f, blockpos.getY(), k + 0.5f, randomIn.nextFloat() * 360.0f, 0.0f);
                            worldIn.spawnEntity(entityliving);
                            ientitylivingdata = entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                            flag = true;
                        }
                        for (j += randomIn.nextInt(5) - randomIn.nextInt(5), k += randomIn.nextInt(5) - randomIn.nextInt(5); j < centerX || j >= centerX + diameterX || k < centerZ || k >= centerZ + diameterX; j = l + randomIn.nextInt(5) - randomIn.nextInt(5), k = i2 + randomIn.nextInt(5) - randomIn.nextInt(5)) {}
                    }
                }
            }
        }
    }
    
    static {
        MOB_COUNT_DIV = (int)Math.pow(17.0, 2.0);
    }
}
