// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world;

import net.minecraft.util.WeightedRandom;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.block.Block;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.optifine.reflect.ReflectorForge;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import java.util.List;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.optifine.reflect.Reflector;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.optifine.BlockPosM;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import java.util.HashMap;
import com.google.common.collect.Sets;
import net.minecraft.entity.EntityLiving;
import java.util.Map;
import java.util.Set;

public final class SpawnerAnimals
{
    private static final int MOB_COUNT_DIV;
    private final Set<ChunkCoordIntPair> eligibleChunksForSpawning;
    private Map<Class, EntityLiving> mapSampleEntitiesByClass;
    private int lastPlayerChunkX;
    private int lastPlayerChunkZ;
    private int countChunkPos;
    
    static {
        MOB_COUNT_DIV = (int)Math.pow(17.0, 2.0);
    }
    
    public SpawnerAnimals() {
        this.eligibleChunksForSpawning = Sets.newHashSet();
        this.mapSampleEntitiesByClass = new HashMap<Class, EntityLiving>();
        this.lastPlayerChunkX = Integer.MAX_VALUE;
        this.lastPlayerChunkZ = Integer.MAX_VALUE;
    }
    
    public int findChunksForSpawning(final WorldServer worldServerIn, final boolean spawnHostileMobs, final boolean spawnPeacefulMobs, final boolean p_77192_4_) {
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
                    final int j = MathHelper.floor_double(entityplayer2.posX / 16.0);
                    final int k = MathHelper.floor_double(entityplayer2.posZ / 16.0);
                    for (int l = 8, i2 = -l; i2 <= l; ++i2) {
                        for (int j2 = -l; j2 <= l; ++j2) {
                            final boolean flag2 = i2 == -l || i2 == l || j2 == -l || j2 == l;
                            final ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i2 + j, j2 + k);
                            if (!this.eligibleChunksForSpawning.contains(chunkcoordintpair)) {
                                ++i;
                                if (!flag2 && worldServerIn.getWorldBorder().contains(chunkcoordintpair)) {
                                    this.eligibleChunksForSpawning.add(chunkcoordintpair);
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
        int j3 = 0;
        final BlockPos blockpos2 = worldServerIn.getSpawnPoint();
        final BlockPosM blockposm = new BlockPosM(0, 0, 0);
        new BlockPos.MutableBlockPos();
        EnumCreatureType[] values;
        for (int length = (values = EnumCreatureType.values()).length, n = 0; n < length; ++n) {
            final EnumCreatureType enumcreaturetype = values[n];
            if ((!enumcreaturetype.getPeacefulCreature() || spawnPeacefulMobs) && (enumcreaturetype.getPeacefulCreature() || spawnHostileMobs) && (!enumcreaturetype.getAnimal() || p_77192_4_)) {
                final int k2 = Reflector.ForgeWorld_countEntities.exists() ? Reflector.callInt(worldServerIn, Reflector.ForgeWorld_countEntities, enumcreaturetype, true) : worldServerIn.countEntities(enumcreaturetype.getCreatureClass());
                final int l2 = enumcreaturetype.getMaxNumberOfCreature() * this.countChunkPos / SpawnerAnimals.MOB_COUNT_DIV;
                if (k2 <= l2) {
                    Collection<ChunkCoordIntPair> collection = this.eligibleChunksForSpawning;
                    if (Reflector.ForgeHooksClient.exists()) {
                        final ArrayList<ChunkCoordIntPair> arraylist = Lists.newArrayList((Iterable)collection);
                        Collections.shuffle(arraylist);
                        collection = arraylist;
                    }
                Label_1157:
                    while (true) {
                        for (final ChunkCoordIntPair chunkcoordintpair2 : collection) {
                            final BlockPos blockpos3 = getRandomChunkPosition(worldServerIn, chunkcoordintpair2.chunkXPos, chunkcoordintpair2.chunkZPos, blockposm);
                            final int k3 = blockpos3.getX();
                            final int l3 = blockpos3.getY();
                            final int i3 = blockpos3.getZ();
                            final Block block = worldServerIn.getBlockState(blockpos3).getBlock();
                            if (!block.isNormalCube()) {
                                int j4 = 0;
                                for (int k4 = 0; k4 < 3; ++k4) {
                                    int l4 = k3;
                                    int i4 = l3;
                                    int j5 = i3;
                                    final int k5 = 6;
                                    BiomeGenBase.SpawnListEntry biomegenbase$spawnlistentry = null;
                                    IEntityLivingData ientitylivingdata = null;
                                    for (int l5 = 0; l5 < 4; ++l5) {
                                        l4 += worldServerIn.rand.nextInt(k5) - worldServerIn.rand.nextInt(k5);
                                        i4 += worldServerIn.rand.nextInt(1) - worldServerIn.rand.nextInt(1);
                                        j5 += worldServerIn.rand.nextInt(k5) - worldServerIn.rand.nextInt(k5);
                                        final BlockPos blockpos4 = new BlockPos(l4, i4, j5);
                                        final float f = l4 + 0.5f;
                                        final float f2 = j5 + 0.5f;
                                        if (!worldServerIn.isAnyPlayerWithinRangeAt(f, i4, f2, 24.0) && blockpos2.distanceSq(f, i4, f2) >= 576.0) {
                                            if (biomegenbase$spawnlistentry == null) {
                                                biomegenbase$spawnlistentry = worldServerIn.getSpawnListEntryForTypeAt(enumcreaturetype, blockpos4);
                                                if (biomegenbase$spawnlistentry == null) {
                                                    break;
                                                }
                                            }
                                            if (worldServerIn.canCreatureTypeSpawnHere(enumcreaturetype, biomegenbase$spawnlistentry, blockpos4) && canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(biomegenbase$spawnlistentry.entityClass), worldServerIn, blockpos4)) {
                                                EntityLiving entityliving;
                                                try {
                                                    entityliving = this.mapSampleEntitiesByClass.get(biomegenbase$spawnlistentry.entityClass);
                                                    if (entityliving == null) {
                                                        entityliving = (EntityLiving)biomegenbase$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldServerIn);
                                                        this.mapSampleEntitiesByClass.put(biomegenbase$spawnlistentry.entityClass, entityliving);
                                                    }
                                                }
                                                catch (final Exception exception1) {
                                                    exception1.printStackTrace();
                                                    return j3;
                                                }
                                                entityliving.setLocationAndAngles(f, i4, f2, worldServerIn.rand.nextFloat() * 360.0f, 0.0f);
                                                final boolean flag3 = Reflector.ForgeEventFactory_canEntitySpawn.exists() ? ReflectorForge.canEntitySpawn(entityliving, worldServerIn, f, (float)i4, f2) : (entityliving.getCanSpawnHere() && entityliving.isNotColliding());
                                                if (flag3) {
                                                    this.mapSampleEntitiesByClass.remove(biomegenbase$spawnlistentry.entityClass);
                                                    if (!ReflectorForge.doSpecialSpawn(entityliving, worldServerIn, f, i4, f2)) {
                                                        ientitylivingdata = entityliving.onInitialSpawn(worldServerIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                                                    }
                                                    if (entityliving.isNotColliding()) {
                                                        ++j4;
                                                        worldServerIn.spawnEntityInWorld(entityliving);
                                                    }
                                                    final int i5 = Reflector.ForgeEventFactory_getMaxSpawnPackSize.exists() ? Reflector.callInt(Reflector.ForgeEventFactory_getMaxSpawnPackSize, entityliving) : entityliving.getMaxSpawnedInChunk();
                                                    if (j4 >= i5) {
                                                        continue Label_1157;
                                                    }
                                                }
                                                j3 += j4;
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
        return j3;
    }
    
    protected static BlockPos getRandomChunkPosition(final World worldIn, final int x, final int z) {
        final Chunk chunk = worldIn.getChunkFromChunkCoords(x, z);
        final int i = x * 16 + worldIn.rand.nextInt(16);
        final int j = z * 16 + worldIn.rand.nextInt(16);
        final int k = MathHelper.roundUp(chunk.getHeight(new BlockPos(i, 0, j)) + 1, 16);
        final int l = worldIn.rand.nextInt((k > 0) ? k : (chunk.getTopFilledSegment() + 16 - 1));
        return new BlockPos(i, l, j);
    }
    
    private static BlockPosM getRandomChunkPosition(final World p_getRandomChunkPosition_0_, final int p_getRandomChunkPosition_1_, final int p_getRandomChunkPosition_2_, final BlockPosM p_getRandomChunkPosition_3_) {
        final Chunk chunk = p_getRandomChunkPosition_0_.getChunkFromChunkCoords(p_getRandomChunkPosition_1_, p_getRandomChunkPosition_2_);
        final int i = p_getRandomChunkPosition_1_ * 16 + p_getRandomChunkPosition_0_.rand.nextInt(16);
        final int j = p_getRandomChunkPosition_2_ * 16 + p_getRandomChunkPosition_0_.rand.nextInt(16);
        final int k = MathHelper.roundUp(chunk.getHeightValue(i & 0xF, j & 0xF) + 1, 16);
        final int l = p_getRandomChunkPosition_0_.rand.nextInt((k > 0) ? k : (chunk.getTopFilledSegment() + 16 - 1));
        p_getRandomChunkPosition_3_.setXyz(i, l, j);
        return p_getRandomChunkPosition_3_;
    }
    
    public static boolean canCreatureTypeSpawnAtLocation(final EntityLiving.SpawnPlacementType spawnPlacementTypeIn, final World worldIn, final BlockPos pos) {
        if (!worldIn.getWorldBorder().contains(pos)) {
            return false;
        }
        if (spawnPlacementTypeIn == null) {
            return false;
        }
        final Block block = worldIn.getBlockState(pos).getBlock();
        if (spawnPlacementTypeIn == EntityLiving.SpawnPlacementType.IN_WATER) {
            return block.getMaterial().isLiquid() && worldIn.getBlockState(pos.down()).getBlock().getMaterial().isLiquid() && !worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
        }
        final BlockPos blockpos = pos.down();
        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
        final boolean flag = Reflector.ForgeBlock_canCreatureSpawn.exists() ? Reflector.callBoolean(iblockstate.getBlock(), Reflector.ForgeBlock_canCreatureSpawn, worldIn, blockpos, spawnPlacementTypeIn) : World.doesBlockHaveSolidTopSurface(worldIn, blockpos);
        if (!flag) {
            return false;
        }
        final Block block2 = worldIn.getBlockState(blockpos).getBlock();
        final boolean flag2 = block2 != Blocks.bedrock && block2 != Blocks.barrier;
        return flag2 && !block.isNormalCube() && !block.getMaterial().isLiquid() && !worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
    }
    
    public static void performWorldGenSpawning(final World worldIn, final BiomeGenBase biomeIn, final int p_77191_2_, final int p_77191_3_, final int p_77191_4_, final int p_77191_5_, final Random randomIn) {
        final List<BiomeGenBase.SpawnListEntry> list = biomeIn.getSpawnableList(EnumCreatureType.CREATURE);
        if (!list.isEmpty()) {
            while (randomIn.nextFloat() < biomeIn.getSpawningChance()) {
                final BiomeGenBase.SpawnListEntry biomegenbase$spawnlistentry = WeightedRandom.getRandomItem(worldIn.rand, list);
                final int i = biomegenbase$spawnlistentry.minGroupCount + randomIn.nextInt(1 + biomegenbase$spawnlistentry.maxGroupCount - biomegenbase$spawnlistentry.minGroupCount);
                IEntityLivingData ientitylivingdata = null;
                int j = p_77191_2_ + randomIn.nextInt(p_77191_4_);
                int k = p_77191_3_ + randomIn.nextInt(p_77191_5_);
                final int l = j;
                final int i2 = k;
                for (int j2 = 0; j2 < i; ++j2) {
                    boolean flag = false;
                    for (int k2 = 0; !flag && k2 < 4; ++k2) {
                        final BlockPos blockpos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k));
                        if (canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos)) {
                            EntityLiving entityliving;
                            try {
                                entityliving = (EntityLiving)biomegenbase$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldIn);
                            }
                            catch (final Exception exception1) {
                                exception1.printStackTrace();
                                continue;
                            }
                            if (Reflector.ForgeEventFactory_canEntitySpawn.exists()) {
                                final Object object = Reflector.call(Reflector.ForgeEventFactory_canEntitySpawn, entityliving, worldIn, j + 0.5f, blockpos.getY(), k + 0.5f);
                                if (object == ReflectorForge.EVENT_RESULT_DENY) {
                                    continue;
                                }
                            }
                            entityliving.setLocationAndAngles(j + 0.5f, blockpos.getY(), k + 0.5f, randomIn.nextFloat() * 360.0f, 0.0f);
                            worldIn.spawnEntityInWorld(entityliving);
                            ientitylivingdata = entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                            flag = true;
                        }
                        for (j += randomIn.nextInt(5) - randomIn.nextInt(5), k += randomIn.nextInt(5) - randomIn.nextInt(5); j < p_77191_2_ || j >= p_77191_2_ + p_77191_4_ || k < p_77191_3_ || k >= p_77191_3_ + p_77191_4_; j = l + randomIn.nextInt(5) - randomIn.nextInt(5), k = i2 + randomIn.nextInt(5) - randomIn.nextInt(5)) {}
                    }
                }
            }
        }
    }
}
