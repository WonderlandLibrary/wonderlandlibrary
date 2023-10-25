/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 */
package net.minecraft.world;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;

public abstract class World
implements IBlockAccess {
    public final List<Entity> weatherEffects;
    protected final List<Entity> unloadedEntityList;
    private final List<TileEntity> addedTileEntityList;
    private boolean processingLoadedTiles;
    protected boolean findingSpawnPoint;
    public final WorldProvider provider;
    protected float prevThunderingStrength;
    protected List<IWorldAccess> worldAccesses;
    private int field_181546_a = 63;
    public final boolean isRemote;
    protected VillageCollection villageCollectionObj;
    protected int updateLCG;
    private final WorldBorder worldBorder;
    private int lastLightningBolt;
    protected Scoreboard worldScoreboard;
    protected WorldInfo worldInfo;
    public final List<TileEntity> loadedTileEntityList;
    protected final IntHashMap<Entity> entitiesById;
    private int skylightSubtracted;
    protected final int DIST_HASH_MAGIC = 1013904223;
    protected IChunkProvider chunkProvider;
    protected MapStorage mapStorage;
    protected boolean spawnHostileMobs = true;
    protected boolean spawnPeacefulMobs = true;
    private final Calendar theCalendar;
    protected float prevRainingStrength;
    private final List<TileEntity> tileEntitiesToBeRemoved;
    protected final ISaveHandler saveHandler;
    public final List<TileEntity> tickableTileEntities;
    protected float rainingStrength;
    protected float thunderingStrength;
    public final Profiler theProfiler;
    public final List<Entity> loadedEntityList = Lists.newArrayList();
    public final List<EntityPlayer> playerEntities;
    private long cloudColour = 0xFFFFFFL;
    protected Set<ChunkCoordIntPair> activeChunkSet;
    protected boolean scheduledUpdatesAreImmediate;
    public final Random rand;
    int[] lightUpdateBlockList;
    private int ambientTickCountdown;

    public List<Entity> getEntitiesWithinAABBExcludingEntity(Entity entity, AxisAlignedBB axisAlignedBB) {
        return this.getEntitiesInAABBexcluding(entity, axisAlignedBB, EntitySelectors.NOT_SPECTATING);
    }

    @Override
    public int getStrongPower(BlockPos blockPos, EnumFacing enumFacing) {
        IBlockState iBlockState = this.getBlockState(blockPos);
        return iBlockState.getBlock().getStrongPower(this, blockPos, iBlockState, enumFacing);
    }

    public EntityPlayer getClosestPlayer(double d, double d2, double d3, double d4) {
        double d5 = -1.0;
        EntityPlayer entityPlayer = null;
        int n = 0;
        while (n < this.playerEntities.size()) {
            EntityPlayer entityPlayer2 = this.playerEntities.get(n);
            if (EntitySelectors.NOT_SPECTATING.apply((Object)entityPlayer2)) {
                double d6 = entityPlayer2.getDistanceSq(d, d2, d3);
                if ((d4 < 0.0 || d6 < d4 * d4) && (d5 == -1.0 || d6 < d5)) {
                    d5 = d6;
                    entityPlayer = entityPlayer2;
                }
            }
            ++n;
        }
        return entityPlayer;
    }

    public float getRainStrength(float f) {
        return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * f;
    }

    public double getHorizon() {
        return this.worldInfo.getTerrainType() == WorldType.FLAT ? 0.0 : 63.0;
    }

    public void forceBlockUpdateTick(Block block, BlockPos blockPos, Random random) {
        this.scheduledUpdatesAreImmediate = true;
        block.updateTick(this, blockPos, this.getBlockState(blockPos), random);
        this.scheduledUpdatesAreImmediate = false;
    }

    public boolean isAreaLoaded(BlockPos blockPos, BlockPos blockPos2, boolean bl) {
        return this.isAreaLoaded(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos2.getX(), blockPos2.getY(), blockPos2.getZ(), bl);
    }

    public boolean addWeatherEffect(Entity entity) {
        this.weatherEffects.add(entity);
        return true;
    }

    private boolean isWater(BlockPos blockPos) {
        return this.getBlockState(blockPos).getBlock().getMaterial() == Material.water;
    }

    private boolean isAreaLoaded(int n, int n2, int n3, int n4, int n5, int n6, boolean bl) {
        if (n5 >= 0 && n2 < 256) {
            n3 >>= 4;
            n4 >>= 4;
            n6 >>= 4;
            int n7 = n >>= 4;
            while (n7 <= n4) {
                int n8 = n3;
                while (n8 <= n6) {
                    if (!this.isChunkLoaded(n7, n8, bl)) {
                        return false;
                    }
                    ++n8;
                }
                ++n7;
            }
            return true;
        }
        return false;
    }

    public void updateAllPlayersSleepingFlag() {
    }

    public void updateEntityWithOptionalForce(Entity entity, boolean bl) {
        int n = MathHelper.floor_double(entity.posX);
        int n2 = MathHelper.floor_double(entity.posZ);
        int n3 = 32;
        if (!bl || this.isAreaLoaded(n - n3, 0, n2 - n3, n + n3, 0, n2 + n3, true)) {
            entity.lastTickPosX = entity.posX;
            entity.lastTickPosY = entity.posY;
            entity.lastTickPosZ = entity.posZ;
            entity.prevRotationYaw = entity.rotationYaw;
            entity.prevRotationPitch = entity.rotationPitch;
            if (bl && entity.addedToChunk) {
                ++entity.ticksExisted;
                if (entity.ridingEntity != null) {
                    entity.updateRidden();
                } else {
                    entity.onUpdate();
                }
            }
            this.theProfiler.startSection("chunkCheck");
            if (Double.isNaN(entity.posX) || Double.isInfinite(entity.posX)) {
                entity.posX = entity.lastTickPosX;
            }
            if (Double.isNaN(entity.posY) || Double.isInfinite(entity.posY)) {
                entity.posY = entity.lastTickPosY;
            }
            if (Double.isNaN(entity.posZ) || Double.isInfinite(entity.posZ)) {
                entity.posZ = entity.lastTickPosZ;
            }
            if (Double.isNaN(entity.rotationPitch) || Double.isInfinite(entity.rotationPitch)) {
                entity.rotationPitch = entity.prevRotationPitch;
            }
            if (Double.isNaN(entity.rotationYaw) || Double.isInfinite(entity.rotationYaw)) {
                entity.rotationYaw = entity.prevRotationYaw;
            }
            int n4 = MathHelper.floor_double(entity.posX / 16.0);
            int n5 = MathHelper.floor_double(entity.posY / 16.0);
            int n6 = MathHelper.floor_double(entity.posZ / 16.0);
            if (!entity.addedToChunk || entity.chunkCoordX != n4 || entity.chunkCoordY != n5 || entity.chunkCoordZ != n6) {
                if (entity.addedToChunk && this.isChunkLoaded(entity.chunkCoordX, entity.chunkCoordZ, true)) {
                    this.getChunkFromChunkCoords(entity.chunkCoordX, entity.chunkCoordZ).removeEntityAtIndex(entity, entity.chunkCoordY);
                }
                if (this.isChunkLoaded(n4, n6, true)) {
                    entity.addedToChunk = true;
                    this.getChunkFromChunkCoords(n4, n6).addEntity(entity);
                } else {
                    entity.addedToChunk = false;
                }
            }
            this.theProfiler.endSection();
            if (bl && entity.addedToChunk && entity.riddenByEntity != null) {
                if (!entity.riddenByEntity.isDead && entity.riddenByEntity.ridingEntity == entity) {
                    this.updateEntity(entity.riddenByEntity);
                } else {
                    entity.riddenByEntity.ridingEntity = null;
                    entity.riddenByEntity = null;
                }
            }
        }
    }

    public Block getGroundAboveSeaLevel(BlockPos blockPos) {
        BlockPos blockPos2 = new BlockPos(blockPos.getX(), this.func_181545_F(), blockPos.getZ());
        while (!this.isAirBlock(blockPos2.up())) {
            blockPos2 = blockPos2.up();
        }
        return this.getBlockState(blockPos2).getBlock();
    }

    public <T extends Entity> List<T> getPlayers(Class<? extends T> clazz, Predicate<? super T> predicate) {
        ArrayList arrayList = Lists.newArrayList();
        for (Entity entity : this.playerEntities) {
            if (!clazz.isAssignableFrom(entity.getClass()) || !predicate.apply((Object)entity)) continue;
            arrayList.add(entity);
        }
        return arrayList;
    }

    public int countEntities(Class<?> clazz) {
        int n = 0;
        for (Entity entity : this.loadedEntityList) {
            if (entity instanceof EntityLiving && ((EntityLiving)entity).isNoDespawnRequired() || !clazz.isAssignableFrom(entity.getClass())) continue;
            ++n;
        }
        return n;
    }

    public boolean checkNoEntityCollision(AxisAlignedBB axisAlignedBB, Entity entity) {
        List<Entity> list = this.getEntitiesWithinAABBExcludingEntity(null, axisAlignedBB);
        int n = 0;
        while (n < list.size()) {
            Entity entity2 = list.get(n);
            if (!entity2.isDead && entity2.preventEntitySpawning && entity2 != entity && (entity == null || entity.ridingEntity != entity2 && entity.riddenByEntity != entity2)) {
                return false;
            }
            ++n;
        }
        return true;
    }

    protected World(ISaveHandler iSaveHandler, WorldInfo worldInfo, WorldProvider worldProvider, Profiler profiler, boolean bl) {
        this.unloadedEntityList = Lists.newArrayList();
        this.loadedTileEntityList = Lists.newArrayList();
        this.tickableTileEntities = Lists.newArrayList();
        this.addedTileEntityList = Lists.newArrayList();
        this.tileEntitiesToBeRemoved = Lists.newArrayList();
        this.playerEntities = Lists.newArrayList();
        this.weatherEffects = Lists.newArrayList();
        this.entitiesById = new IntHashMap();
        this.updateLCG = new Random().nextInt();
        this.rand = new Random();
        this.worldAccesses = Lists.newArrayList();
        this.theCalendar = Calendar.getInstance();
        this.worldScoreboard = new Scoreboard();
        this.activeChunkSet = Sets.newHashSet();
        this.ambientTickCountdown = this.rand.nextInt(12000);
        this.lightUpdateBlockList = new int[32768];
        this.saveHandler = iSaveHandler;
        this.theProfiler = profiler;
        this.worldInfo = worldInfo;
        this.provider = worldProvider;
        this.isRemote = bl;
        this.worldBorder = worldProvider.getWorldBorder();
    }

    public Vec3 getCloudColour(float f) {
        float f2;
        float f3;
        float f4 = this.getCelestialAngle(f);
        float f5 = MathHelper.cos(f4 * (float)Math.PI * 2.0f) * 2.0f + 0.5f;
        f5 = MathHelper.clamp_float(f5, 0.0f, 1.0f);
        float f6 = (float)(this.cloudColour >> 16 & 0xFFL) / 255.0f;
        float f7 = (float)(this.cloudColour >> 8 & 0xFFL) / 255.0f;
        float f8 = (float)(this.cloudColour & 0xFFL) / 255.0f;
        float f9 = this.getRainStrength(f);
        if (f9 > 0.0f) {
            f3 = (f6 * 0.3f + f7 * 0.59f + f8 * 0.11f) * 0.6f;
            f2 = 1.0f - f9 * 0.95f;
            f6 = f6 * f2 + f3 * (1.0f - f2);
            f7 = f7 * f2 + f3 * (1.0f - f2);
            f8 = f8 * f2 + f3 * (1.0f - f2);
        }
        f6 *= f5 * 0.9f + 0.1f;
        f7 *= f5 * 0.9f + 0.1f;
        f8 *= f5 * 0.85f + 0.15f;
        f3 = this.getThunderStrength(f);
        if (f3 > 0.0f) {
            f2 = (f6 * 0.3f + f7 * 0.59f + f8 * 0.11f) * 0.2f;
            float f10 = 1.0f - f3 * 0.95f;
            f6 = f6 * f10 + f2 * (1.0f - f10);
            f7 = f7 * f10 + f2 * (1.0f - f10);
            f8 = f8 * f10 + f2 * (1.0f - f10);
        }
        return new Vec3(f6, f7, f8);
    }

    public boolean isThundering() {
        return (double)this.getThunderStrength(1.0f) > 0.9;
    }

    public void updateComparatorOutputLevel(BlockPos blockPos, Block block) {
        for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            if (!this.isBlockLoaded(blockPos2)) continue;
            IBlockState iBlockState = this.getBlockState(blockPos2);
            if (Blocks.unpowered_comparator.isAssociated(iBlockState.getBlock())) {
                iBlockState.getBlock().onNeighborBlockChange(this, blockPos2, iBlockState, block);
                continue;
            }
            if (!iBlockState.getBlock().isNormalCube() || !Blocks.unpowered_comparator.isAssociated((iBlockState = this.getBlockState(blockPos2 = blockPos2.offset(enumFacing))).getBlock())) continue;
            iBlockState.getBlock().onNeighborBlockChange(this, blockPos2, iBlockState, block);
        }
    }

    public boolean isAnyLiquid(AxisAlignedBB axisAlignedBB) {
        int n = MathHelper.floor_double(axisAlignedBB.minX);
        int n2 = MathHelper.floor_double(axisAlignedBB.maxX);
        int n3 = MathHelper.floor_double(axisAlignedBB.minY);
        int n4 = MathHelper.floor_double(axisAlignedBB.maxY);
        int n5 = MathHelper.floor_double(axisAlignedBB.minZ);
        int n6 = MathHelper.floor_double(axisAlignedBB.maxZ);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int n7 = n;
        while (n7 <= n2) {
            int n8 = n3;
            while (n8 <= n4) {
                int n9 = n5;
                while (n9 <= n6) {
                    Block block = this.getBlockState(mutableBlockPos.func_181079_c(n7, n8, n9)).getBlock();
                    if (block.getMaterial().isLiquid()) {
                        return true;
                    }
                    ++n9;
                }
                ++n8;
            }
            ++n7;
        }
        return false;
    }

    public List<NextTickListEntry> getPendingBlockUpdates(Chunk chunk, boolean bl) {
        return null;
    }

    public boolean isDaytime() {
        return this.skylightSubtracted < 4;
    }

    protected abstract int getRenderDistanceChunks();

    public void spawnParticle(EnumParticleTypes enumParticleTypes, boolean bl, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
        this.spawnParticle(enumParticleTypes.getParticleID(), enumParticleTypes.getShouldIgnoreRange() | bl, d, d2, d3, d4, d5, d6, nArray);
    }

    public int getRedstonePower(BlockPos blockPos, EnumFacing enumFacing) {
        IBlockState iBlockState = this.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        return block.isNormalCube() ? this.getStrongPower(blockPos) : block.getWeakPower(this, blockPos, iBlockState, enumFacing);
    }

    public boolean setBlockState(BlockPos blockPos, IBlockState iBlockState) {
        return this.setBlockState(blockPos, iBlockState, 3);
    }

    public boolean isBlockLoaded(BlockPos blockPos, boolean bl) {
        return !this.isValid(blockPos) ? false : this.isChunkLoaded(blockPos.getX() >> 4, blockPos.getZ() >> 4, bl);
    }

    public void setAllowedSpawnTypes(boolean bl, boolean bl2) {
        this.spawnHostileMobs = bl;
        this.spawnPeacefulMobs = bl2;
    }

    public int getChunksLowestHorizon(int n, int n2) {
        if (n >= -30000000 && n2 >= -30000000 && n < 30000000 && n2 < 30000000) {
            if (!this.isChunkLoaded(n >> 4, n2 >> 4, true)) {
                return 0;
            }
            Chunk chunk = this.getChunkFromChunkCoords(n >> 4, n2 >> 4);
            return chunk.getLowestHeight();
        }
        return this.func_181545_F() + 1;
    }

    public Explosion createExplosion(Entity entity, double d, double d2, double d3, float f, boolean bl) {
        return this.newExplosion(entity, d, d2, d3, f, false, bl);
    }

    public Vec3 getSkyColor(Entity entity, float f) {
        float f2;
        float f3;
        float f4 = this.getCelestialAngle(f);
        float f5 = MathHelper.cos(f4 * (float)Math.PI * 2.0f) * 2.0f + 0.5f;
        f5 = MathHelper.clamp_float(f5, 0.0f, 1.0f);
        int n = MathHelper.floor_double(entity.posX);
        int n2 = MathHelper.floor_double(entity.posY);
        int n3 = MathHelper.floor_double(entity.posZ);
        BlockPos blockPos = new BlockPos(n, n2, n3);
        BiomeGenBase biomeGenBase = this.getBiomeGenForCoords(blockPos);
        float f6 = biomeGenBase.getFloatTemperature(blockPos);
        int n4 = biomeGenBase.getSkyColorByTemp(f6);
        float f7 = (float)(n4 >> 16 & 0xFF) / 255.0f;
        float f8 = (float)(n4 >> 8 & 0xFF) / 255.0f;
        float f9 = (float)(n4 & 0xFF) / 255.0f;
        f7 *= f5;
        f8 *= f5;
        f9 *= f5;
        float f10 = this.getRainStrength(f);
        if (f10 > 0.0f) {
            f3 = (f7 * 0.3f + f8 * 0.59f + f9 * 0.11f) * 0.6f;
            f2 = 1.0f - f10 * 0.75f;
            f7 = f7 * f2 + f3 * (1.0f - f2);
            f8 = f8 * f2 + f3 * (1.0f - f2);
            f9 = f9 * f2 + f3 * (1.0f - f2);
        }
        if ((f3 = this.getThunderStrength(f)) > 0.0f) {
            f2 = (f7 * 0.3f + f8 * 0.59f + f9 * 0.11f) * 0.2f;
            float f11 = 1.0f - f3 * 0.75f;
            f7 = f7 * f11 + f2 * (1.0f - f11);
            f8 = f8 * f11 + f2 * (1.0f - f11);
            f9 = f9 * f11 + f2 * (1.0f - f11);
        }
        if (this.lastLightningBolt > 0) {
            f2 = (float)this.lastLightningBolt - f;
            if (f2 > 1.0f) {
                f2 = 1.0f;
            }
            f7 = f7 * (1.0f - (f2 *= 0.45f)) + 0.8f * f2;
            f8 = f8 * (1.0f - f2) + 0.8f * f2;
            f9 = f9 * (1.0f - f2) + 1.0f * f2;
        }
        return new Vec3(f7, f8, f9);
    }

    public boolean addTileEntity(TileEntity tileEntity) {
        boolean bl = this.loadedTileEntityList.add(tileEntity);
        if (bl && tileEntity instanceof ITickable) {
            this.tickableTileEntities.add(tileEntity);
        }
        return bl;
    }

    protected void updateBlocks() {
        this.setActivePlayerChunksAndCheckLight();
    }

    public void setSkylightSubtracted(int n) {
        this.skylightSubtracted = n;
    }

    public boolean isFindingSpawnPoint() {
        return this.findingSpawnPoint;
    }

    public boolean canBlockFreezeNoWater(BlockPos blockPos) {
        return this.canBlockFreeze(blockPos, true);
    }

    public void playAuxSFX(int n, BlockPos blockPos, int n2) {
        this.playAuxSFXAtEntity(null, n, blockPos, n2);
    }

    public void markTileEntityForRemoval(TileEntity tileEntity) {
        this.tileEntitiesToBeRemoved.add(tileEntity);
    }

    @Override
    public boolean extendedLevelsInChunkCache() {
        return false;
    }

    public boolean isBlockinHighHumidity(BlockPos blockPos) {
        BiomeGenBase biomeGenBase = this.getBiomeGenForCoords(blockPos);
        return biomeGenBase.isHighHumidity();
    }

    public boolean tickUpdates(boolean bl) {
        return false;
    }

    public Calendar getCurrentDate() {
        if (this.getTotalWorldTime() % 600L == 0L) {
            this.theCalendar.setTimeInMillis(MinecraftServer.getCurrentTimeMillis());
        }
        return this.theCalendar;
    }

    public void notifyNeighborsOfStateExcept(BlockPos blockPos, Block block, EnumFacing enumFacing) {
        if (enumFacing != EnumFacing.WEST) {
            this.notifyBlockOfStateChange(blockPos.west(), block);
        }
        if (enumFacing != EnumFacing.EAST) {
            this.notifyBlockOfStateChange(blockPos.east(), block);
        }
        if (enumFacing != EnumFacing.DOWN) {
            this.notifyBlockOfStateChange(blockPos.down(), block);
        }
        if (enumFacing != EnumFacing.UP) {
            this.notifyBlockOfStateChange(blockPos.up(), block);
        }
        if (enumFacing != EnumFacing.NORTH) {
            this.notifyBlockOfStateChange(blockPos.north(), block);
        }
        if (enumFacing != EnumFacing.SOUTH) {
            this.notifyBlockOfStateChange(blockPos.south(), block);
        }
    }

    public List<AxisAlignedBB> getCollidingBoundingBoxes(Entity entity, AxisAlignedBB axisAlignedBB) {
        ArrayList arrayList = Lists.newArrayList();
        int n = MathHelper.floor_double(axisAlignedBB.minX);
        int n2 = MathHelper.floor_double(axisAlignedBB.maxX + 1.0);
        int n3 = MathHelper.floor_double(axisAlignedBB.minY);
        int n4 = MathHelper.floor_double(axisAlignedBB.maxY + 1.0);
        int n5 = MathHelper.floor_double(axisAlignedBB.minZ);
        int n6 = MathHelper.floor_double(axisAlignedBB.maxZ + 1.0);
        WorldBorder worldBorder = this.getWorldBorder();
        boolean bl = entity.isOutsideBorder();
        boolean bl2 = this.isInsideBorder(worldBorder, entity);
        IBlockState iBlockState = Blocks.stone.getDefaultState();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int n7 = n;
        while (n7 < n2) {
            int n8 = n5;
            while (n8 < n6) {
                if (this.isBlockLoaded(mutableBlockPos.func_181079_c(n7, 64, n8))) {
                    int n9 = n3 - 1;
                    while (n9 < n4) {
                        mutableBlockPos.func_181079_c(n7, n9, n8);
                        if (bl && bl2) {
                            entity.setOutsideBorder(false);
                        } else if (!bl && !bl2) {
                            entity.setOutsideBorder(true);
                        }
                        IBlockState iBlockState2 = iBlockState;
                        if (worldBorder.contains(mutableBlockPos) || !bl2) {
                            iBlockState2 = this.getBlockState(mutableBlockPos);
                        }
                        iBlockState2.getBlock().addCollisionBoxesToList(this, mutableBlockPos, iBlockState2, axisAlignedBB, arrayList, entity);
                        ++n9;
                    }
                }
                ++n8;
            }
            ++n7;
        }
        double d = 0.25;
        List<Entity> list = this.getEntitiesWithinAABBExcludingEntity(entity, axisAlignedBB.expand(d, d, d));
        int n10 = 0;
        while (n10 < list.size()) {
            if (entity.riddenByEntity != list && entity.ridingEntity != list) {
                AxisAlignedBB axisAlignedBB2 = list.get(n10).getCollisionBoundingBox();
                if (axisAlignedBB2 != null && axisAlignedBB2.intersectsWith(axisAlignedBB)) {
                    arrayList.add(axisAlignedBB2);
                }
                if ((axisAlignedBB2 = entity.getCollisionBox(list.get(n10))) != null && axisAlignedBB2.intersectsWith(axisAlignedBB)) {
                    arrayList.add(axisAlignedBB2);
                }
            }
            ++n10;
        }
        return arrayList;
    }

    @Override
    public WorldType getWorldType() {
        return this.worldInfo.getTerrainType();
    }

    public boolean isInsideBorder(WorldBorder worldBorder, Entity entity) {
        double d = worldBorder.minX();
        double d2 = worldBorder.minZ();
        double d3 = worldBorder.maxX();
        double d4 = worldBorder.maxZ();
        if (entity.isOutsideBorder()) {
            d += 1.0;
            d2 += 1.0;
            d3 -= 1.0;
            d4 -= 1.0;
        } else {
            d -= 1.0;
            d2 -= 1.0;
            d3 += 1.0;
            d4 += 1.0;
        }
        return entity.posX > d && entity.posX < d3 && entity.posZ > d2 && entity.posZ < d4;
    }

    public World init() {
        return this;
    }

    public boolean isAreaLoaded(StructureBoundingBox structureBoundingBox, boolean bl) {
        return this.isAreaLoaded(structureBoundingBox.minX, structureBoundingBox.minY, structureBoundingBox.minZ, structureBoundingBox.maxX, structureBoundingBox.maxY, structureBoundingBox.maxZ, bl);
    }

    public MovingObjectPosition rayTraceBlocks(Vec3 vec3, Vec3 vec32, boolean bl) {
        return this.rayTraceBlocks(vec3, vec32, bl, false, false);
    }

    public int isBlockIndirectlyGettingPowered(BlockPos blockPos) {
        int n = 0;
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n2 = enumFacingArray.length;
        int n3 = 0;
        while (n3 < n2) {
            EnumFacing enumFacing = enumFacingArray[n3];
            int n4 = this.getRedstonePower(blockPos.offset(enumFacing), enumFacing);
            if (n4 >= 15) {
                return 15;
            }
            if (n4 > n) {
                n = n4;
            }
            ++n3;
        }
        return n;
    }

    public MovingObjectPosition rayTraceBlocks(Vec3 vec3, Vec3 vec32) {
        return this.rayTraceBlocks(vec3, vec32, false, false, false);
    }

    public boolean isAreaLoaded(StructureBoundingBox structureBoundingBox) {
        return this.isAreaLoaded(structureBoundingBox, true);
    }

    public void playBroadcastSound(int n, BlockPos blockPos, int n2) {
        int n3 = 0;
        while (n3 < this.worldAccesses.size()) {
            this.worldAccesses.get(n3).broadcastSound(n, blockPos, n2);
            ++n3;
        }
    }

    public WorldSavedData loadItemData(Class<? extends WorldSavedData> clazz, String string) {
        return this.mapStorage.loadData(clazz, string);
    }

    public boolean isBlockFullCube(BlockPos blockPos) {
        IBlockState iBlockState = this.getBlockState(blockPos);
        AxisAlignedBB axisAlignedBB = iBlockState.getBlock().getCollisionBoundingBox(this, blockPos, iBlockState);
        return axisAlignedBB != null && axisAlignedBB.getAverageEdgeLength() >= 1.0;
    }

    public Explosion newExplosion(Entity entity, double d, double d2, double d3, float f, boolean bl, boolean bl2) {
        Explosion explosion = new Explosion(this, entity, d, d2, d3, f, bl, bl2);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }

    public float getThunderStrength(float f) {
        return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * f) * this.getRainStrength(f);
    }

    public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> clazz, AxisAlignedBB axisAlignedBB) {
        return this.getEntitiesWithinAABB(clazz, axisAlignedBB, EntitySelectors.NOT_SPECTATING);
    }

    public Random setRandomSeed(int n, int n2, int n3) {
        long l = (long)n * 341873128712L + (long)n2 * 132897987541L + this.getWorldInfo().getSeed() + (long)n3;
        this.rand.setSeed(l);
        return this.rand;
    }

    public void updateEntities() {
        int n;
        int n2;
        Object object;
        this.theProfiler.startSection("entities");
        this.theProfiler.startSection("global");
        int n3 = 0;
        while (n3 < this.weatherEffects.size()) {
            object = this.weatherEffects.get(n3);
            try {
                ++((Entity)object).ticksExisted;
                ((Entity)object).onUpdate();
            }
            catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Ticking entity");
                CrashReportCategory crashReportCategory = crashReport.makeCategory("Entity being ticked");
                if (object == null) {
                    crashReportCategory.addCrashSection("Entity", "~~NULL~~");
                } else {
                    ((Entity)object).addEntityCrashInfo(crashReportCategory);
                }
                throw new ReportedException(crashReport);
            }
            if (((Entity)object).isDead) {
                this.weatherEffects.remove(n3--);
            }
            ++n3;
        }
        this.theProfiler.endStartSection("remove");
        this.loadedEntityList.removeAll(this.unloadedEntityList);
        n3 = 0;
        while (n3 < this.unloadedEntityList.size()) {
            object = this.unloadedEntityList.get(n3);
            n2 = ((Entity)object).chunkCoordX;
            n = ((Entity)object).chunkCoordZ;
            if (((Entity)object).addedToChunk && this.isChunkLoaded(n2, n, true)) {
                this.getChunkFromChunkCoords(n2, n).removeEntity((Entity)object);
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < this.unloadedEntityList.size()) {
            this.onEntityRemoved(this.unloadedEntityList.get(n3));
            ++n3;
        }
        this.unloadedEntityList.clear();
        this.theProfiler.endStartSection("regular");
        n3 = 0;
        while (n3 < this.loadedEntityList.size()) {
            block27: {
                block26: {
                    object = this.loadedEntityList.get(n3);
                    if (((Entity)object).ridingEntity == null) break block26;
                    if (!((Entity)object).ridingEntity.isDead && ((Entity)object).ridingEntity.riddenByEntity == object) break block27;
                    ((Entity)object).ridingEntity.riddenByEntity = null;
                    ((Entity)object).ridingEntity = null;
                }
                this.theProfiler.startSection("tick");
                if (!((Entity)object).isDead) {
                    try {
                        this.updateEntity((Entity)object);
                    }
                    catch (Throwable throwable) {
                        CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Ticking entity");
                        CrashReportCategory crashReportCategory = crashReport.makeCategory("Entity being ticked");
                        ((Entity)object).addEntityCrashInfo(crashReportCategory);
                        throw new ReportedException(crashReport);
                    }
                }
                this.theProfiler.endSection();
                this.theProfiler.startSection("remove");
                if (((Entity)object).isDead) {
                    n2 = ((Entity)object).chunkCoordX;
                    n = ((Entity)object).chunkCoordZ;
                    if (((Entity)object).addedToChunk && this.isChunkLoaded(n2, n, true)) {
                        this.getChunkFromChunkCoords(n2, n).removeEntity((Entity)object);
                    }
                    this.loadedEntityList.remove(n3--);
                    this.onEntityRemoved((Entity)object);
                }
                this.theProfiler.endSection();
            }
            ++n3;
        }
        this.theProfiler.endStartSection("blockEntities");
        this.processingLoadedTiles = true;
        Iterator<TileEntity> iterator = this.tickableTileEntities.iterator();
        while (iterator.hasNext()) {
            BlockPos blockPos;
            object = iterator.next();
            if (!((TileEntity)object).isInvalid() && ((TileEntity)object).hasWorldObj() && this.isBlockLoaded(blockPos = ((TileEntity)object).getPos()) && this.worldBorder.contains(blockPos)) {
                try {
                    ((ITickable)object).update();
                }
                catch (Throwable throwable) {
                    CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Ticking block entity");
                    CrashReportCategory crashReportCategory = crashReport.makeCategory("Block entity being ticked");
                    ((TileEntity)object).addInfoToCrashReport(crashReportCategory);
                    throw new ReportedException(crashReport);
                }
            }
            if (!((TileEntity)object).isInvalid()) continue;
            iterator.remove();
            this.loadedTileEntityList.remove(object);
            if (!this.isBlockLoaded(((TileEntity)object).getPos())) continue;
            this.getChunkFromBlockCoords(((TileEntity)object).getPos()).removeTileEntity(((TileEntity)object).getPos());
        }
        this.processingLoadedTiles = false;
        if (!this.tileEntitiesToBeRemoved.isEmpty()) {
            this.tickableTileEntities.removeAll(this.tileEntitiesToBeRemoved);
            this.loadedTileEntityList.removeAll(this.tileEntitiesToBeRemoved);
            this.tileEntitiesToBeRemoved.clear();
        }
        this.theProfiler.endStartSection("pendingBlockEntities");
        if (!this.addedTileEntityList.isEmpty()) {
            int n4 = 0;
            while (n4 < this.addedTileEntityList.size()) {
                TileEntity tileEntity = this.addedTileEntityList.get(n4);
                if (!tileEntity.isInvalid()) {
                    if (!this.loadedTileEntityList.contains(tileEntity)) {
                        this.addTileEntity(tileEntity);
                    }
                    if (this.isBlockLoaded(tileEntity.getPos())) {
                        this.getChunkFromBlockCoords(tileEntity.getPos()).addTileEntity(tileEntity.getPos(), tileEntity);
                    }
                    this.markBlockForUpdate(tileEntity.getPos());
                }
                ++n4;
            }
            this.addedTileEntityList.clear();
        }
        this.theProfiler.endSection();
        this.theProfiler.endSection();
    }

    public boolean isAreaLoaded(BlockPos blockPos, int n) {
        return this.isAreaLoaded(blockPos, n, true);
    }

    public float getLightBrightness(BlockPos blockPos) {
        return this.provider.getLightBrightnessTable()[this.getLightFromNeighbors(blockPos)];
    }

    public int getLightFromNeighborsFor(EnumSkyBlock enumSkyBlock, BlockPos blockPos) {
        if (this.provider.getHasNoSky() && enumSkyBlock == EnumSkyBlock.SKY) {
            return 0;
        }
        if (blockPos.getY() < 0) {
            blockPos = new BlockPos(blockPos.getX(), 0, blockPos.getZ());
        }
        if (!this.isValid(blockPos)) {
            return enumSkyBlock.defaultLightValue;
        }
        if (!this.isBlockLoaded(blockPos)) {
            return enumSkyBlock.defaultLightValue;
        }
        if (this.getBlockState(blockPos).getBlock().getUseNeighborBrightness()) {
            int n = this.getLightFor(enumSkyBlock, blockPos.up());
            int n2 = this.getLightFor(enumSkyBlock, blockPos.east());
            int n3 = this.getLightFor(enumSkyBlock, blockPos.west());
            int n4 = this.getLightFor(enumSkyBlock, blockPos.south());
            int n5 = this.getLightFor(enumSkyBlock, blockPos.north());
            if (n2 > n) {
                n = n2;
            }
            if (n3 > n) {
                n = n3;
            }
            if (n4 > n) {
                n = n4;
            }
            if (n5 > n) {
                n = n5;
            }
            return n;
        }
        Chunk chunk = this.getChunkFromBlockCoords(blockPos);
        return chunk.getLightFor(enumSkyBlock, blockPos);
    }

    public MovingObjectPosition rayTraceBlocks(Vec3 vec3, Vec3 vec32, boolean bl, boolean bl2, boolean bl3) {
        if (!(Double.isNaN(vec3.xCoord) || Double.isNaN(vec3.yCoord) || Double.isNaN(vec3.zCoord))) {
            if (!(Double.isNaN(vec32.xCoord) || Double.isNaN(vec32.yCoord) || Double.isNaN(vec32.zCoord))) {
                MovingObjectPosition movingObjectPosition;
                int n = MathHelper.floor_double(vec32.xCoord);
                int n2 = MathHelper.floor_double(vec32.yCoord);
                int n3 = MathHelper.floor_double(vec32.zCoord);
                int n4 = MathHelper.floor_double(vec3.xCoord);
                int n5 = MathHelper.floor_double(vec3.yCoord);
                int n6 = MathHelper.floor_double(vec3.zCoord);
                BlockPos blockPos = new BlockPos(n4, n5, n6);
                IBlockState iBlockState = this.getBlockState(blockPos);
                Block block = iBlockState.getBlock();
                if ((!bl2 || block.getCollisionBoundingBox(this, blockPos, iBlockState) != null) && block.canCollideCheck(iBlockState, bl) && (movingObjectPosition = block.collisionRayTrace(this, blockPos, vec3, vec32)) != null) {
                    return movingObjectPosition;
                }
                movingObjectPosition = null;
                int n7 = 200;
                while (n7-- >= 0) {
                    EnumFacing enumFacing;
                    if (Double.isNaN(vec3.xCoord) || Double.isNaN(vec3.yCoord) || Double.isNaN(vec3.zCoord)) {
                        return null;
                    }
                    if (n4 == n && n5 == n2 && n6 == n3) {
                        return bl3 ? movingObjectPosition : null;
                    }
                    boolean bl4 = true;
                    boolean bl5 = true;
                    boolean bl6 = true;
                    double d = 999.0;
                    double d2 = 999.0;
                    double d3 = 999.0;
                    if (n > n4) {
                        d = (double)n4 + 1.0;
                    } else if (n < n4) {
                        d = (double)n4 + 0.0;
                    } else {
                        bl4 = false;
                    }
                    if (n2 > n5) {
                        d2 = (double)n5 + 1.0;
                    } else if (n2 < n5) {
                        d2 = (double)n5 + 0.0;
                    } else {
                        bl5 = false;
                    }
                    if (n3 > n6) {
                        d3 = (double)n6 + 1.0;
                    } else if (n3 < n6) {
                        d3 = (double)n6 + 0.0;
                    } else {
                        bl6 = false;
                    }
                    double d4 = 999.0;
                    double d5 = 999.0;
                    double d6 = 999.0;
                    double d7 = vec32.xCoord - vec3.xCoord;
                    double d8 = vec32.yCoord - vec3.yCoord;
                    double d9 = vec32.zCoord - vec3.zCoord;
                    if (bl4) {
                        d4 = (d - vec3.xCoord) / d7;
                    }
                    if (bl5) {
                        d5 = (d2 - vec3.yCoord) / d8;
                    }
                    if (bl6) {
                        d6 = (d3 - vec3.zCoord) / d9;
                    }
                    if (d4 == -0.0) {
                        d4 = -1.0E-4;
                    }
                    if (d5 == -0.0) {
                        d5 = -1.0E-4;
                    }
                    if (d6 == -0.0) {
                        d6 = -1.0E-4;
                    }
                    if (d4 < d5 && d4 < d6) {
                        enumFacing = n > n4 ? EnumFacing.WEST : EnumFacing.EAST;
                        vec3 = new Vec3(d, vec3.yCoord + d8 * d4, vec3.zCoord + d9 * d4);
                    } else if (d5 < d6) {
                        enumFacing = n2 > n5 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec3 = new Vec3(vec3.xCoord + d7 * d5, d2, vec3.zCoord + d9 * d5);
                    } else {
                        enumFacing = n3 > n6 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec3 = new Vec3(vec3.xCoord + d7 * d6, vec3.yCoord + d8 * d6, d3);
                    }
                    n4 = MathHelper.floor_double(vec3.xCoord) - (enumFacing == EnumFacing.EAST ? 1 : 0);
                    n5 = MathHelper.floor_double(vec3.yCoord) - (enumFacing == EnumFacing.UP ? 1 : 0);
                    n6 = MathHelper.floor_double(vec3.zCoord) - (enumFacing == EnumFacing.SOUTH ? 1 : 0);
                    blockPos = new BlockPos(n4, n5, n6);
                    IBlockState iBlockState2 = this.getBlockState(blockPos);
                    Block block2 = iBlockState2.getBlock();
                    if (bl2 && block2.getCollisionBoundingBox(this, blockPos, iBlockState2) == null) continue;
                    if (block2.canCollideCheck(iBlockState2, bl)) {
                        MovingObjectPosition movingObjectPosition2 = block2.collisionRayTrace(this, blockPos, vec3, vec32);
                        if (movingObjectPosition2 == null) continue;
                        return movingObjectPosition2;
                    }
                    movingObjectPosition = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec3, enumFacing, blockPos);
                }
                return bl3 ? movingObjectPosition : null;
            }
            return null;
        }
        return null;
    }

    public void markChunkDirty(BlockPos blockPos, TileEntity tileEntity) {
        if (this.isBlockLoaded(blockPos)) {
            this.getChunkFromBlockCoords(blockPos).setChunkModified();
        }
    }

    public boolean canBlockBePlaced(Block block, BlockPos blockPos, boolean bl, EnumFacing enumFacing, Entity entity, ItemStack itemStack) {
        AxisAlignedBB axisAlignedBB;
        Block block2 = this.getBlockState(blockPos).getBlock();
        AxisAlignedBB axisAlignedBB2 = axisAlignedBB = bl ? null : block.getCollisionBoundingBox(this, blockPos, block.getDefaultState());
        return axisAlignedBB != null && !this.checkNoEntityCollision(axisAlignedBB, entity) ? false : (block2.getMaterial() == Material.circuits && block == Blocks.anvil ? true : block2.getMaterial().isReplaceable() && block.canReplace(this, blockPos, enumFacing, itemStack));
    }

    public void addTileEntities(Collection<TileEntity> collection) {
        if (this.processingLoadedTiles) {
            this.addedTileEntityList.addAll(collection);
        } else {
            for (TileEntity tileEntity : collection) {
                this.loadedTileEntityList.add(tileEntity);
                if (!(tileEntity instanceof ITickable)) continue;
                this.tickableTileEntities.add(tileEntity);
            }
        }
    }

    @Override
    public IBlockState getBlockState(BlockPos blockPos) {
        if (!this.isValid(blockPos)) {
            return Blocks.air.getDefaultState();
        }
        Chunk chunk = this.getChunkFromBlockCoords(blockPos);
        return chunk.getBlockState(blockPos);
    }

    public void setTileEntity(BlockPos blockPos, TileEntity tileEntity) {
        if (tileEntity != null && !tileEntity.isInvalid()) {
            if (this.processingLoadedTiles) {
                tileEntity.setPos(blockPos);
                Iterator<TileEntity> iterator = this.addedTileEntityList.iterator();
                while (iterator.hasNext()) {
                    TileEntity tileEntity2 = iterator.next();
                    if (!tileEntity2.getPos().equals(blockPos)) continue;
                    tileEntity2.invalidate();
                    iterator.remove();
                }
                this.addedTileEntityList.add(tileEntity);
            } else {
                this.addTileEntity(tileEntity);
                this.getChunkFromBlockCoords(blockPos).addTileEntity(blockPos, tileEntity);
            }
        }
    }

    public void setTotalWorldTime(long l) {
        this.worldInfo.setWorldTotalTime(l);
    }

    public void markBlockRangeForRenderUpdate(int n, int n2, int n3, int n4, int n5, int n6) {
        int n7 = 0;
        while (n7 < this.worldAccesses.size()) {
            this.worldAccesses.get(n7).markBlockRangeForRenderUpdate(n, n2, n3, n4, n5, n6);
            ++n7;
        }
    }

    public int getLightFromNeighbors(BlockPos blockPos) {
        return this.getLight(blockPos, true);
    }

    public BlockPos getStrongholdPos(String string, BlockPos blockPos) {
        return this.getChunkProvider().getStrongholdGen(this, string, blockPos);
    }

    public void markBlockForUpdate(BlockPos blockPos) {
        int n = 0;
        while (n < this.worldAccesses.size()) {
            this.worldAccesses.get(n).markBlockForUpdate(blockPos);
            ++n;
        }
    }

    public boolean isFlammableWithin(AxisAlignedBB axisAlignedBB) {
        int n;
        int n2 = MathHelper.floor_double(axisAlignedBB.minX);
        int n3 = MathHelper.floor_double(axisAlignedBB.maxX + 1.0);
        int n4 = MathHelper.floor_double(axisAlignedBB.minY);
        int n5 = MathHelper.floor_double(axisAlignedBB.maxY + 1.0);
        int n6 = MathHelper.floor_double(axisAlignedBB.minZ);
        if (this.isAreaLoaded(n2, n4, n6, n3, n5, n = MathHelper.floor_double(axisAlignedBB.maxZ + 1.0), true)) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            int n7 = n2;
            while (n7 < n3) {
                int n8 = n4;
                while (n8 < n5) {
                    int n9 = n6;
                    while (n9 < n) {
                        Block block = this.getBlockState(mutableBlockPos.func_181079_c(n7, n8, n9)).getBlock();
                        if (block == Blocks.fire || block == Blocks.flowing_lava || block == Blocks.lava) {
                            return true;
                        }
                        ++n9;
                    }
                    ++n8;
                }
                ++n7;
            }
        }
        return false;
    }

    private void spawnParticle(int n, boolean bl, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
        int n2 = 0;
        while (n2 < this.worldAccesses.size()) {
            this.worldAccesses.get(n2).spawnParticle(n, bl, d, d2, d3, d4, d5, d6, nArray);
            ++n2;
        }
    }

    public int getActualHeight() {
        return this.provider.getHasNoSky() ? 128 : 256;
    }

    public String getDebugLoadedEntities() {
        return "All: " + this.loadedEntityList.size();
    }

    public GameRules getGameRules() {
        return this.worldInfo.getGameRulesInstance();
    }

    public long getTotalWorldTime() {
        return this.worldInfo.getWorldTotalTime();
    }

    public long getWorldTime() {
        return this.worldInfo.getWorldTime();
    }

    public boolean isRaining() {
        return (double)this.getRainStrength(1.0f) > 0.2;
    }

    public void scheduleUpdate(BlockPos blockPos, Block block, int n) {
    }

    public Entity getEntityByID(int n) {
        return this.entitiesById.lookup(n);
    }

    public EnumDifficulty getDifficulty() {
        return this.getWorldInfo().getDifficulty();
    }

    public void calculateInitialSkylight() {
        int n = this.calculateSkylightSubtracted(1.0f);
        if (n != this.skylightSubtracted) {
            this.skylightSubtracted = n;
        }
    }

    public Chunk getChunkFromChunkCoords(int n, int n2) {
        return this.chunkProvider.provideChunk(n, n2);
    }

    protected void onEntityAdded(Entity entity) {
        int n = 0;
        while (n < this.worldAccesses.size()) {
            this.worldAccesses.get(n).onEntityAdded(entity);
            ++n;
        }
    }

    public void updateEntity(Entity entity) {
        this.updateEntityWithOptionalForce(entity, true);
    }

    public void setRainStrength(float f) {
        this.prevRainingStrength = f;
        this.rainingStrength = f;
    }

    public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> clazz, AxisAlignedBB axisAlignedBB, Predicate<? super T> predicate) {
        int n = MathHelper.floor_double((axisAlignedBB.minX - 2.0) / 16.0);
        int n2 = MathHelper.floor_double((axisAlignedBB.maxX + 2.0) / 16.0);
        int n3 = MathHelper.floor_double((axisAlignedBB.minZ - 2.0) / 16.0);
        int n4 = MathHelper.floor_double((axisAlignedBB.maxZ + 2.0) / 16.0);
        ArrayList arrayList = Lists.newArrayList();
        int n5 = n;
        while (n5 <= n2) {
            int n6 = n3;
            while (n6 <= n4) {
                if (this.isChunkLoaded(n5, n6, true)) {
                    this.getChunkFromChunkCoords(n5, n6).getEntitiesOfTypeWithinAAAB(clazz, axisAlignedBB, arrayList, predicate);
                }
                ++n6;
            }
            ++n5;
        }
        return arrayList;
    }

    public void spawnParticle(EnumParticleTypes enumParticleTypes, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
        this.spawnParticle(enumParticleTypes.getParticleID(), enumParticleTypes.getShouldIgnoreRange(), d, d2, d3, d4, d5, d6, nArray);
    }

    public float getStarBrightness(float f) {
        float f2 = this.getCelestialAngle(f);
        float f3 = 1.0f - (MathHelper.cos(f2 * (float)Math.PI * 2.0f) * 2.0f + 0.25f);
        f3 = MathHelper.clamp_float(f3, 0.0f, 1.0f);
        return f3 * f3 * 0.5f;
    }

    public IChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }

    public WorldInfo getWorldInfo() {
        return this.worldInfo;
    }

    public List<AxisAlignedBB> func_147461_a(AxisAlignedBB axisAlignedBB) {
        ArrayList arrayList = Lists.newArrayList();
        int n = MathHelper.floor_double(axisAlignedBB.minX);
        int n2 = MathHelper.floor_double(axisAlignedBB.maxX + 1.0);
        int n3 = MathHelper.floor_double(axisAlignedBB.minY);
        int n4 = MathHelper.floor_double(axisAlignedBB.maxY + 1.0);
        int n5 = MathHelper.floor_double(axisAlignedBB.minZ);
        int n6 = MathHelper.floor_double(axisAlignedBB.maxZ + 1.0);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int n7 = n;
        while (n7 < n2) {
            int n8 = n5;
            while (n8 < n6) {
                if (this.isBlockLoaded(mutableBlockPos.func_181079_c(n7, 64, n8))) {
                    int n9 = n3 - 1;
                    while (n9 < n4) {
                        mutableBlockPos.func_181079_c(n7, n9, n8);
                        IBlockState iBlockState = n7 >= -30000000 && n7 < 30000000 && n8 >= -30000000 && n8 < 30000000 ? this.getBlockState(mutableBlockPos) : Blocks.bedrock.getDefaultState();
                        iBlockState.getBlock().addCollisionBoxesToList(this, mutableBlockPos, iBlockState, axisAlignedBB, arrayList, null);
                        ++n9;
                    }
                }
                ++n8;
            }
            ++n7;
        }
        return arrayList;
    }

    public void setLastLightningBolt(int n) {
        this.lastLightningBolt = n;
    }

    public boolean checkBlockCollision(AxisAlignedBB axisAlignedBB) {
        int n = MathHelper.floor_double(axisAlignedBB.minX);
        int n2 = MathHelper.floor_double(axisAlignedBB.maxX);
        int n3 = MathHelper.floor_double(axisAlignedBB.minY);
        int n4 = MathHelper.floor_double(axisAlignedBB.maxY);
        int n5 = MathHelper.floor_double(axisAlignedBB.minZ);
        int n6 = MathHelper.floor_double(axisAlignedBB.maxZ);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int n7 = n;
        while (n7 <= n2) {
            int n8 = n3;
            while (n8 <= n4) {
                int n9 = n5;
                while (n9 <= n6) {
                    Block block = this.getBlockState(mutableBlockPos.func_181079_c(n7, n8, n9)).getBlock();
                    if (block.getMaterial() != Material.air) {
                        return true;
                    }
                    ++n9;
                }
                ++n8;
            }
            ++n7;
        }
        return false;
    }

    public BlockPos getSpawnPoint() {
        BlockPos blockPos = new BlockPos(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
        if (!this.getWorldBorder().contains(blockPos)) {
            blockPos = this.getHeight(new BlockPos(this.getWorldBorder().getCenterX(), 0.0, this.getWorldBorder().getCenterZ()));
        }
        return blockPos;
    }

    public boolean isMaterialInBB(AxisAlignedBB axisAlignedBB, Material material) {
        int n = MathHelper.floor_double(axisAlignedBB.minX);
        int n2 = MathHelper.floor_double(axisAlignedBB.maxX + 1.0);
        int n3 = MathHelper.floor_double(axisAlignedBB.minY);
        int n4 = MathHelper.floor_double(axisAlignedBB.maxY + 1.0);
        int n5 = MathHelper.floor_double(axisAlignedBB.minZ);
        int n6 = MathHelper.floor_double(axisAlignedBB.maxZ + 1.0);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int n7 = n;
        while (n7 < n2) {
            int n8 = n3;
            while (n8 < n4) {
                int n9 = n5;
                while (n9 < n6) {
                    if (this.getBlockState(mutableBlockPos.func_181079_c(n7, n8, n9)).getBlock().getMaterial() == material) {
                        return true;
                    }
                    ++n9;
                }
                ++n8;
            }
            ++n7;
        }
        return false;
    }

    protected void playMoodSoundAndCheckLight(int n, int n2, Chunk chunk) {
        this.theProfiler.endStartSection("moodSound");
        if (this.ambientTickCountdown == 0 && !this.isRemote) {
            EntityPlayer entityPlayer;
            this.updateLCG = this.updateLCG * 3 + 1013904223;
            int n3 = this.updateLCG >> 2;
            int n4 = n3 & 0xF;
            int n5 = n3 >> 8 & 0xF;
            int n6 = n3 >> 16 & 0xFF;
            BlockPos blockPos = new BlockPos(n4, n6, n5);
            Block block = chunk.getBlock(blockPos);
            if (block.getMaterial() == Material.air && this.getLight(blockPos) <= this.rand.nextInt(8) && this.getLightFor(EnumSkyBlock.SKY, blockPos) <= 0 && (entityPlayer = this.getClosestPlayer((double)(n4 += n) + 0.5, (double)n6 + 0.5, (double)(n5 += n2) + 0.5, 8.0)) != null && entityPlayer.getDistanceSq((double)n4 + 0.5, (double)n6 + 0.5, (double)n5 + 0.5) > 4.0) {
                this.playSoundEffect((double)n4 + 0.5, (double)n6 + 0.5, (double)n5 + 0.5, "ambient.cave.cave", 0.7f, 0.8f + this.rand.nextFloat() * 0.2f);
                this.ambientTickCountdown = this.rand.nextInt(12000) + 6000;
            }
        }
        this.theProfiler.endStartSection("checkLight");
        chunk.enqueueRelightChecks();
    }

    public void func_181544_b(int n) {
        this.field_181546_a = n;
    }

    public void tick() {
        this.updateWeather();
    }

    public void notifyNeighborsRespectDebug(BlockPos blockPos, Block block) {
        if (this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD) {
            this.notifyNeighborsOfStateChange(blockPos, block);
        }
    }

    public CrashReportCategory addWorldInfoToCrashReport(CrashReport crashReport) {
        CrashReportCategory crashReportCategory = crashReport.makeCategoryDepth("Affected level", 1);
        crashReportCategory.addCrashSection("Level name", this.worldInfo == null ? "????" : this.worldInfo.getWorldName());
        crashReportCategory.addCrashSectionCallable("All players", new Callable<String>(){

            @Override
            public String call() {
                return String.valueOf(World.this.playerEntities.size()) + " total; " + World.this.playerEntities.toString();
            }
        });
        crashReportCategory.addCrashSectionCallable("Chunk stats", new Callable<String>(){

            @Override
            public String call() {
                return World.this.chunkProvider.makeString();
            }
        });
        try {
            this.worldInfo.addToCrashReport(crashReportCategory);
        }
        catch (Throwable throwable) {
            crashReportCategory.addCrashSectionThrowable("Level Data Unobtainable", throwable);
        }
        return crashReportCategory;
    }

    public int getMoonPhase() {
        return this.provider.getMoonPhase(this.worldInfo.getWorldTime());
    }

    public int getLight(BlockPos blockPos, boolean bl) {
        if (blockPos.getX() >= -30000000 && blockPos.getZ() >= -30000000 && blockPos.getX() < 30000000 && blockPos.getZ() < 30000000) {
            if (bl && this.getBlockState(blockPos).getBlock().getUseNeighborBrightness()) {
                int n = this.getLight(blockPos.up(), false);
                int n2 = this.getLight(blockPos.east(), false);
                int n3 = this.getLight(blockPos.west(), false);
                int n4 = this.getLight(blockPos.south(), false);
                int n5 = this.getLight(blockPos.north(), false);
                if (n2 > n) {
                    n = n2;
                }
                if (n3 > n) {
                    n = n3;
                }
                if (n4 > n) {
                    n = n4;
                }
                if (n5 > n) {
                    n = n5;
                }
                return n;
            }
            if (blockPos.getY() < 0) {
                return 0;
            }
            if (blockPos.getY() >= 256) {
                blockPos = new BlockPos(blockPos.getX(), 255, blockPos.getZ());
            }
            Chunk chunk = this.getChunkFromBlockCoords(blockPos);
            return chunk.getLightSubtracted(blockPos, this.skylightSubtracted);
        }
        return 15;
    }

    public void sendQuittingDisconnectingPacket() {
    }

    public boolean checkNoEntityCollision(AxisAlignedBB axisAlignedBB) {
        return this.checkNoEntityCollision(axisAlignedBB, null);
    }

    public boolean isBlockNormalCube(BlockPos blockPos, boolean bl) {
        if (!this.isValid(blockPos)) {
            return bl;
        }
        Chunk chunk = this.chunkProvider.provideChunk(blockPos);
        if (chunk.isEmpty()) {
            return bl;
        }
        Block block = this.getBlockState(blockPos).getBlock();
        return block.getMaterial().isOpaque() && block.isFullCube();
    }

    private int getRawLight(BlockPos blockPos, EnumSkyBlock enumSkyBlock) {
        if (enumSkyBlock == EnumSkyBlock.SKY && this.canSeeSky(blockPos)) {
            return 15;
        }
        Block block = this.getBlockState(blockPos).getBlock();
        int n = enumSkyBlock == EnumSkyBlock.SKY ? 0 : block.getLightValue();
        int n2 = block.getLightOpacity();
        if (n2 >= 15 && block.getLightValue() > 0) {
            n2 = 1;
        }
        if (n2 < 1) {
            n2 = 1;
        }
        if (n2 >= 15) {
            return 0;
        }
        if (n >= 14) {
            return n;
        }
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n3 = enumFacingArray.length;
        int n4 = 0;
        while (n4 < n3) {
            EnumFacing enumFacing = enumFacingArray[n4];
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            int n5 = this.getLightFor(enumSkyBlock, blockPos2) - n2;
            if (n5 > n) {
                n = n5;
            }
            if (n >= 14) {
                return n;
            }
            ++n4;
        }
        return n;
    }

    public void removeTileEntity(BlockPos blockPos) {
        TileEntity tileEntity = this.getTileEntity(blockPos);
        if (tileEntity != null && this.processingLoadedTiles) {
            tileEntity.invalidate();
            this.addedTileEntityList.remove(tileEntity);
        } else {
            if (tileEntity != null) {
                this.addedTileEntityList.remove(tileEntity);
                this.loadedTileEntityList.remove(tileEntity);
                this.tickableTileEntities.remove(tileEntity);
            }
            this.getChunkFromBlockCoords(blockPos).removeTileEntity(blockPos);
        }
    }

    public void checkSessionLock() throws MinecraftException {
        this.saveHandler.checkSessionLock();
    }

    protected void onEntityRemoved(Entity entity) {
        int n = 0;
        while (n < this.worldAccesses.size()) {
            this.worldAccesses.get(n).onEntityRemoved(entity);
            ++n;
        }
    }

    public void removePlayerEntityDangerously(Entity entity) {
        entity.setDead();
        if (entity instanceof EntityPlayer) {
            this.playerEntities.remove(entity);
            this.updateAllPlayersSleepingFlag();
        }
        int n = entity.chunkCoordX;
        int n2 = entity.chunkCoordZ;
        if (entity.addedToChunk && this.isChunkLoaded(n, n2, true)) {
            this.getChunkFromChunkCoords(n, n2).removeEntity(entity);
        }
        this.loadedEntityList.remove(entity);
        this.onEntityRemoved(entity);
    }

    public float getSunBrightness(float f) {
        float f2 = this.getCelestialAngle(f);
        float f3 = 1.0f - (MathHelper.cos(f2 * (float)Math.PI * 2.0f) * 2.0f + 0.2f);
        f3 = MathHelper.clamp_float(f3, 0.0f, 1.0f);
        f3 = 1.0f - f3;
        f3 = (float)((double)f3 * (1.0 - (double)(this.getRainStrength(f) * 5.0f) / 16.0));
        f3 = (float)((double)f3 * (1.0 - (double)(this.getThunderStrength(f) * 5.0f) / 16.0));
        return f3 * 0.8f + 0.2f;
    }

    protected boolean isChunkLoaded(int n, int n2, boolean bl) {
        return this.chunkProvider.chunkExists(n, n2) && (bl || !this.chunkProvider.provideChunk(n, n2).isEmpty());
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(final BlockPos blockPos) {
        if (this.isBlockLoaded(blockPos)) {
            Chunk chunk = this.getChunkFromBlockCoords(blockPos);
            try {
                return chunk.getBiome(blockPos, this.provider.getWorldChunkManager());
            }
            catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Getting biome");
                CrashReportCategory crashReportCategory = crashReport.makeCategory("Coordinates of biome request");
                crashReportCategory.addCrashSectionCallable("Location", new Callable<String>(){

                    @Override
                    public String call() throws Exception {
                        return CrashReportCategory.getCoordinateInfo(blockPos);
                    }
                });
                throw new ReportedException(crashReport);
            }
        }
        return this.provider.getWorldChunkManager().getBiomeGenerator(blockPos, BiomeGenBase.plains);
    }

    public <T extends Entity> List<T> getEntities(Class<? extends T> clazz, Predicate<? super T> predicate) {
        ArrayList arrayList = Lists.newArrayList();
        for (Entity entity : this.loadedEntityList) {
            if (!clazz.isAssignableFrom(entity.getClass()) || !predicate.apply((Object)entity)) continue;
            arrayList.add(entity);
        }
        return arrayList;
    }

    protected void calculateInitialWeather() {
        if (this.worldInfo.isRaining()) {
            this.rainingStrength = 1.0f;
            if (this.worldInfo.isThundering()) {
                this.thunderingStrength = 1.0f;
            }
        }
    }

    public void playRecord(BlockPos blockPos, String string) {
        int n = 0;
        while (n < this.worldAccesses.size()) {
            this.worldAccesses.get(n).playRecord(string, blockPos);
            ++n;
        }
    }

    public long getSeed() {
        return this.worldInfo.getSeed();
    }

    public EntityPlayer getPlayerEntityByName(String string) {
        int n = 0;
        while (n < this.playerEntities.size()) {
            EntityPlayer entityPlayer = this.playerEntities.get(n);
            if (string.equals(entityPlayer.getName())) {
                return entityPlayer;
            }
            ++n;
        }
        return null;
    }

    public void loadEntities(Collection<Entity> collection) {
        this.loadedEntityList.addAll(collection);
        for (Entity entity : collection) {
            this.onEntityAdded(entity);
        }
    }

    @Override
    public TileEntity getTileEntity(BlockPos blockPos) {
        TileEntity tileEntity;
        int n;
        if (!this.isValid(blockPos)) {
            return null;
        }
        TileEntity tileEntity2 = null;
        if (this.processingLoadedTiles) {
            n = 0;
            while (n < this.addedTileEntityList.size()) {
                tileEntity = this.addedTileEntityList.get(n);
                if (!tileEntity.isInvalid() && tileEntity.getPos().equals(blockPos)) {
                    tileEntity2 = tileEntity;
                    break;
                }
                ++n;
            }
        }
        if (tileEntity2 == null) {
            tileEntity2 = this.getChunkFromBlockCoords(blockPos).getTileEntity(blockPos, Chunk.EnumCreateEntityType.IMMEDIATE);
        }
        if (tileEntity2 == null) {
            n = 0;
            while (n < this.addedTileEntityList.size()) {
                tileEntity = this.addedTileEntityList.get(n);
                if (!tileEntity.isInvalid() && tileEntity.getPos().equals(blockPos)) {
                    tileEntity2 = tileEntity;
                    break;
                }
                ++n;
            }
        }
        return tileEntity2;
    }

    public <T extends Entity> T findNearestEntityWithinAABB(Class<? extends T> clazz, AxisAlignedBB axisAlignedBB, T t) {
        List<T> list = this.getEntitiesWithinAABB(clazz, axisAlignedBB);
        Entity entity = null;
        double d = Double.MAX_VALUE;
        int n = 0;
        while (n < list.size()) {
            double d2;
            Entity entity2 = (Entity)list.get(n);
            if (entity2 != t && EntitySelectors.NOT_SPECTATING.apply((Object)entity2) && (d2 = t.getDistanceSqToEntity(entity2)) <= d) {
                entity = entity2;
                d = d2;
            }
            ++n;
        }
        return (T)entity;
    }

    public void joinEntityInSurroundings(Entity entity) {
        int n = MathHelper.floor_double(entity.posX / 16.0);
        int n2 = MathHelper.floor_double(entity.posZ / 16.0);
        int n3 = 2;
        int n4 = n - n3;
        while (n4 <= n + n3) {
            int n5 = n2 - n3;
            while (n5 <= n2 + n3) {
                this.getChunkFromChunkCoords(n4, n5);
                ++n5;
            }
            ++n4;
        }
        if (!this.loadedEntityList.contains(entity)) {
            this.loadedEntityList.add(entity);
        }
    }

    public boolean isAnyPlayerWithinRangeAt(double d, double d2, double d3, double d4) {
        int n = 0;
        while (n < this.playerEntities.size()) {
            EntityPlayer entityPlayer = this.playerEntities.get(n);
            if (EntitySelectors.NOT_SPECTATING.apply((Object)entityPlayer)) {
                double d5 = entityPlayer.getDistanceSq(d, d2, d3);
                if (d4 < 0.0 || d5 < d4 * d4) {
                    return true;
                }
            }
            ++n;
        }
        return false;
    }

    public int func_181545_F() {
        return this.field_181546_a;
    }

    public boolean checkLight(BlockPos blockPos) {
        boolean bl = false;
        if (!this.provider.getHasNoSky()) {
            bl |= this.checkLightFor(EnumSkyBlock.SKY, blockPos);
        }
        return bl |= this.checkLightFor(EnumSkyBlock.BLOCK, blockPos);
    }

    public void removeEntity(Entity entity) {
        if (entity.riddenByEntity != null) {
            entity.riddenByEntity.mountEntity(null);
        }
        if (entity.ridingEntity != null) {
            entity.mountEntity(null);
        }
        entity.setDead();
        if (entity instanceof EntityPlayer) {
            this.playerEntities.remove(entity);
            this.updateAllPlayersSleepingFlag();
            this.onEntityRemoved(entity);
        }
    }

    public EntityPlayer getClosestPlayerToEntity(Entity entity, double d) {
        return this.getClosestPlayer(entity.posX, entity.posY, entity.posZ, d);
    }

    public void notifyNeighborsOfStateChange(BlockPos blockPos, Block block) {
        this.notifyBlockOfStateChange(blockPos.west(), block);
        this.notifyBlockOfStateChange(blockPos.east(), block);
        this.notifyBlockOfStateChange(blockPos.down(), block);
        this.notifyBlockOfStateChange(blockPos.up(), block);
        this.notifyBlockOfStateChange(blockPos.north(), block);
        this.notifyBlockOfStateChange(blockPos.south(), block);
    }

    public boolean canBlockFreezeWater(BlockPos blockPos) {
        return this.canBlockFreeze(blockPos, false);
    }

    public int getSkylightSubtracted() {
        return this.skylightSubtracted;
    }

    public boolean canSnowAt(BlockPos blockPos, boolean bl) {
        Block block;
        BiomeGenBase biomeGenBase = this.getBiomeGenForCoords(blockPos);
        float f = biomeGenBase.getFloatTemperature(blockPos);
        if (f > 0.15f) {
            return false;
        }
        if (!bl) {
            return true;
        }
        return blockPos.getY() >= 0 && blockPos.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, blockPos) < 10 && (block = this.getBlockState(blockPos).getBlock()).getMaterial() == Material.air && Blocks.snow_layer.canPlaceBlockAt(this, blockPos);
    }

    public DifficultyInstance getDifficultyForLocation(BlockPos blockPos) {
        long l = 0L;
        float f = 0.0f;
        if (this.isBlockLoaded(blockPos)) {
            f = this.getCurrentMoonPhaseFactor();
            l = this.getChunkFromBlockCoords(blockPos).getInhabitedTime();
        }
        return new DifficultyInstance(this.getDifficulty(), this.getWorldTime(), l, f);
    }

    public float getCelestialAngle(float f) {
        return this.provider.calculateCelestialAngle(this.worldInfo.getWorldTime(), f);
    }

    public boolean isBlockModifiable(EntityPlayer entityPlayer, BlockPos blockPos) {
        return true;
    }

    public List<NextTickListEntry> func_175712_a(StructureBoundingBox structureBoundingBox, boolean bl) {
        return null;
    }

    public void setWorldTime(long l) {
        this.worldInfo.setWorldTime(l);
    }

    public void sendBlockBreakProgress(int n, BlockPos blockPos, int n2) {
        int n3 = 0;
        while (n3 < this.worldAccesses.size()) {
            IWorldAccess iWorldAccess = this.worldAccesses.get(n3);
            iWorldAccess.sendBlockBreakProgress(n, blockPos, n2);
            ++n3;
        }
    }

    public boolean isSidePowered(BlockPos blockPos, EnumFacing enumFacing) {
        return this.getRedstonePower(blockPos, enumFacing) > 0;
    }

    public BlockPos getPrecipitationHeight(BlockPos blockPos) {
        return this.getChunkFromBlockCoords(blockPos).getPrecipitationHeight(blockPos);
    }

    public void playSoundAtEntity(Entity entity, String string, float f, float f2) {
        int n = 0;
        while (n < this.worldAccesses.size()) {
            this.worldAccesses.get(n).playSound(string, entity.posX, entity.posY, entity.posZ, f, f2);
            ++n;
        }
    }

    public boolean canSeeSky(BlockPos blockPos) {
        return this.getChunkFromBlockCoords(blockPos).canSeeSky(blockPos);
    }

    public void updateBlockTick(BlockPos blockPos, Block block, int n, int n2) {
    }

    public float getCurrentMoonPhaseFactor() {
        return WorldProvider.moonPhaseFactors[this.provider.getMoonPhase(this.worldInfo.getWorldTime())];
    }

    public int getHeight() {
        return 256;
    }

    public boolean isAreaLoaded(BlockPos blockPos, int n, boolean bl) {
        return this.isAreaLoaded(blockPos.getX() - n, blockPos.getY() - n, blockPos.getZ() - n, blockPos.getX() + n, blockPos.getY() + n, blockPos.getZ() + n, bl);
    }

    public void scheduleBlockUpdate(BlockPos blockPos, Block block, int n, int n2) {
    }

    public void notifyBlockOfStateChange(BlockPos blockPos, final Block block) {
        if (!this.isRemote) {
            IBlockState iBlockState = this.getBlockState(blockPos);
            try {
                iBlockState.getBlock().onNeighborBlockChange(this, blockPos, iBlockState, block);
            }
            catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
                CrashReportCategory crashReportCategory = crashReport.makeCategory("Block being updated");
                crashReportCategory.addCrashSectionCallable("Source block type", new Callable<String>(){

                    @Override
                    public String call() throws Exception {
                        try {
                            return String.format("ID #%d (%s // %s)", Block.getIdFromBlock(block), block.getUnlocalizedName(), block.getClass().getCanonicalName());
                        }
                        catch (Throwable throwable) {
                            return "ID #" + Block.getIdFromBlock(block);
                        }
                    }
                });
                CrashReportCategory.addBlockInfo(crashReportCategory, blockPos, iBlockState);
                throw new ReportedException(crashReport);
            }
        }
    }

    public Chunk getChunkFromBlockCoords(BlockPos blockPos) {
        return this.getChunkFromChunkCoords(blockPos.getX() >> 4, blockPos.getZ() >> 4);
    }

    public int getLight(BlockPos blockPos) {
        if (blockPos.getY() < 0) {
            return 0;
        }
        if (blockPos.getY() >= 256) {
            blockPos = new BlockPos(blockPos.getX(), 255, blockPos.getZ());
        }
        return this.getChunkFromBlockCoords(blockPos).getLightSubtracted(blockPos, 0);
    }

    public boolean isBlockPowered(BlockPos blockPos) {
        return this.getRedstonePower(blockPos.down(), EnumFacing.DOWN) > 0 ? true : (this.getRedstonePower(blockPos.up(), EnumFacing.UP) > 0 ? true : (this.getRedstonePower(blockPos.north(), EnumFacing.NORTH) > 0 ? true : (this.getRedstonePower(blockPos.south(), EnumFacing.SOUTH) > 0 ? true : (this.getRedstonePower(blockPos.west(), EnumFacing.WEST) > 0 ? true : this.getRedstonePower(blockPos.east(), EnumFacing.EAST) > 0))));
    }

    public float getCelestialAngleRadians(float f) {
        float f2 = this.getCelestialAngle(f);
        return f2 * (float)Math.PI * 2.0f;
    }

    public int calculateSkylightSubtracted(float f) {
        float f2 = this.getCelestialAngle(f);
        float f3 = 1.0f - (MathHelper.cos(f2 * (float)Math.PI * 2.0f) * 2.0f + 0.5f);
        f3 = MathHelper.clamp_float(f3, 0.0f, 1.0f);
        f3 = 1.0f - f3;
        f3 = (float)((double)f3 * (1.0 - (double)(this.getRainStrength(f) * 5.0f) / 16.0));
        f3 = (float)((double)f3 * (1.0 - (double)(this.getThunderStrength(f) * 5.0f) / 16.0));
        f3 = 1.0f - f3;
        return (int)(f3 * 11.0f);
    }

    @Override
    public int getCombinedLight(BlockPos blockPos, int n) {
        int n2 = this.getLightFromNeighborsFor(EnumSkyBlock.SKY, blockPos);
        int n3 = this.getLightFromNeighborsFor(EnumSkyBlock.BLOCK, blockPos);
        if (n3 < n) {
            n3 = n;
        }
        return n2 << 20 | n3 << 4;
    }

    public boolean canLightningStrike(BlockPos blockPos) {
        if (!this.isRaining()) {
            return false;
        }
        if (!this.canSeeSky(blockPos)) {
            return false;
        }
        if (this.getPrecipitationHeight(blockPos).getY() > blockPos.getY()) {
            return false;
        }
        BiomeGenBase biomeGenBase = this.getBiomeGenForCoords(blockPos);
        return biomeGenBase.getEnableSnow() ? false : (this.canSnowAt(blockPos, false) ? false : biomeGenBase.canSpawnLightningBolt());
    }

    public String getProviderName() {
        return this.chunkProvider.makeString();
    }

    protected void setActivePlayerChunksAndCheckLight() {
        int n;
        int n2;
        int n3;
        EntityPlayer entityPlayer;
        this.activeChunkSet.clear();
        this.theProfiler.startSection("buildList");
        int n4 = 0;
        while (n4 < this.playerEntities.size()) {
            entityPlayer = this.playerEntities.get(n4);
            n3 = MathHelper.floor_double(entityPlayer.posX / 16.0);
            n2 = MathHelper.floor_double(entityPlayer.posZ / 16.0);
            n = this.getRenderDistanceChunks();
            int n5 = -n;
            while (n5 <= n) {
                int n6 = -n;
                while (n6 <= n) {
                    this.activeChunkSet.add(new ChunkCoordIntPair(n5 + n3, n6 + n2));
                    ++n6;
                }
                ++n5;
            }
            ++n4;
        }
        this.theProfiler.endSection();
        if (this.ambientTickCountdown > 0) {
            --this.ambientTickCountdown;
        }
        this.theProfiler.startSection("playerCheckLight");
        if (!this.playerEntities.isEmpty()) {
            n4 = this.rand.nextInt(this.playerEntities.size());
            entityPlayer = this.playerEntities.get(n4);
            n3 = MathHelper.floor_double(entityPlayer.posX) + this.rand.nextInt(11) - 5;
            n2 = MathHelper.floor_double(entityPlayer.posY) + this.rand.nextInt(11) - 5;
            n = MathHelper.floor_double(entityPlayer.posZ) + this.rand.nextInt(11) - 5;
            this.checkLight(new BlockPos(n3, n2, n));
        }
        this.theProfiler.endSection();
    }

    public void setInitialSpawnLocation() {
        this.setSpawnPoint(new BlockPos(8, 64, 8));
    }

    public boolean isBlockTickPending(BlockPos blockPos, Block block) {
        return false;
    }

    public List<Entity> getEntitiesInAABBexcluding(Entity entity, AxisAlignedBB axisAlignedBB, Predicate<? super Entity> predicate) {
        ArrayList arrayList = Lists.newArrayList();
        int n = MathHelper.floor_double((axisAlignedBB.minX - 2.0) / 16.0);
        int n2 = MathHelper.floor_double((axisAlignedBB.maxX + 2.0) / 16.0);
        int n3 = MathHelper.floor_double((axisAlignedBB.minZ - 2.0) / 16.0);
        int n4 = MathHelper.floor_double((axisAlignedBB.maxZ + 2.0) / 16.0);
        int n5 = n;
        while (n5 <= n2) {
            int n6 = n3;
            while (n6 <= n4) {
                if (this.isChunkLoaded(n5, n6, true)) {
                    this.getChunkFromChunkCoords(n5, n6).getEntitiesWithinAABBForEntity(entity, axisAlignedBB, arrayList, predicate);
                }
                ++n6;
            }
            ++n5;
        }
        return arrayList;
    }

    public void setLightFor(EnumSkyBlock enumSkyBlock, BlockPos blockPos, int n) {
        if (this.isValid(blockPos) && this.isBlockLoaded(blockPos)) {
            Chunk chunk = this.getChunkFromBlockCoords(blockPos);
            chunk.setLightFor(enumSkyBlock, blockPos, n);
            this.notifyLightSet(blockPos);
        }
    }

    public void addBlockEvent(BlockPos blockPos, Block block, int n, int n2) {
        block.onBlockEventReceived(this, blockPos, this.getBlockState(blockPos), n, n2);
    }

    public void playSoundEffect(double d, double d2, double d3, String string, float f, float f2) {
        int n = 0;
        while (n < this.worldAccesses.size()) {
            this.worldAccesses.get(n).playSound(string, d, d2, d3, f, f2);
            ++n;
        }
    }

    public boolean destroyBlock(BlockPos blockPos, boolean bl) {
        IBlockState iBlockState = this.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        if (block.getMaterial() == Material.air) {
            return false;
        }
        this.playAuxSFX(2001, blockPos, Block.getStateId(iBlockState));
        if (bl) {
            block.dropBlockAsItem(this, blockPos, iBlockState, 0);
        }
        return this.setBlockState(blockPos, Blocks.air.getDefaultState(), 3);
    }

    public boolean isAreaLoaded(BlockPos blockPos, BlockPos blockPos2) {
        return this.isAreaLoaded(blockPos, blockPos2, true);
    }

    public void markBlockRangeForRenderUpdate(BlockPos blockPos, BlockPos blockPos2) {
        this.markBlockRangeForRenderUpdate(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
    }

    public int getUniqueDataId(String string) {
        return this.mapStorage.getUniqueDataId(string);
    }

    public boolean handleMaterialAcceleration(AxisAlignedBB axisAlignedBB, Material material, Entity entity) {
        int n;
        int n2 = MathHelper.floor_double(axisAlignedBB.minX);
        int n3 = MathHelper.floor_double(axisAlignedBB.maxX + 1.0);
        int n4 = MathHelper.floor_double(axisAlignedBB.minY);
        int n5 = MathHelper.floor_double(axisAlignedBB.maxY + 1.0);
        int n6 = MathHelper.floor_double(axisAlignedBB.minZ);
        if (!this.isAreaLoaded(n2, n4, n6, n3, n5, n = MathHelper.floor_double(axisAlignedBB.maxZ + 1.0), true)) {
            return false;
        }
        boolean bl = false;
        Vec3 vec3 = new Vec3(0.0, 0.0, 0.0);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int n7 = n2;
        while (n7 < n3) {
            int n8 = n4;
            while (n8 < n5) {
                int n9 = n6;
                while (n9 < n) {
                    double d;
                    mutableBlockPos.func_181079_c(n7, n8, n9);
                    IBlockState iBlockState = this.getBlockState(mutableBlockPos);
                    Block block = iBlockState.getBlock();
                    if (block.getMaterial() == material && (double)n5 >= (d = (double)((float)(n8 + 1) - BlockLiquid.getLiquidHeightPercent(iBlockState.getValue(BlockLiquid.LEVEL))))) {
                        bl = true;
                        vec3 = block.modifyAcceleration(this, mutableBlockPos, entity, vec3);
                    }
                    ++n9;
                }
                ++n8;
            }
            ++n7;
        }
        if (vec3.lengthVector() > 0.0 && entity.isPushedByWater()) {
            vec3 = vec3.normalize();
            double d = 0.014;
            entity.motionX += vec3.xCoord * d;
            entity.motionY += vec3.yCoord * d;
            entity.motionZ += vec3.zCoord * d;
        }
        return bl;
    }

    public Vec3 getFogColor(float f) {
        float f2 = this.getCelestialAngle(f);
        return this.provider.getFogColor(f2, f);
    }

    public boolean checkLightFor(EnumSkyBlock enumSkyBlock, BlockPos blockPos) {
        int n;
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        if (!this.isAreaLoaded(blockPos, 17, false)) {
            return false;
        }
        int n9 = 0;
        int n10 = 0;
        this.theProfiler.startSection("getBrightness");
        int n11 = this.getLightFor(enumSkyBlock, blockPos);
        int n12 = this.getRawLight(blockPos, enumSkyBlock);
        int n13 = blockPos.getX();
        int n14 = blockPos.getY();
        int n15 = blockPos.getZ();
        if (n12 > n11) {
            this.lightUpdateBlockList[n10++] = 133152;
        } else if (n12 < n11) {
            this.lightUpdateBlockList[n10++] = 0x20820 | n11 << 18;
            while (n9 < n10) {
                n8 = this.lightUpdateBlockList[n9++];
                n7 = (n8 & 0x3F) - 32 + n13;
                n6 = (n8 >> 6 & 0x3F) - 32 + n14;
                n5 = (n8 >> 12 & 0x3F) - 32 + n15;
                int n16 = n8 >> 18 & 0xF;
                BlockPos blockPos2 = new BlockPos(n7, n6, n5);
                n4 = this.getLightFor(enumSkyBlock, blockPos2);
                if (n4 != n16) continue;
                this.setLightFor(enumSkyBlock, blockPos2, 0);
                if (n16 <= 0 || (n3 = MathHelper.abs_int(n7 - n13)) + (n2 = MathHelper.abs_int(n6 - n14)) + (n = MathHelper.abs_int(n5 - n15)) >= 17) continue;
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                EnumFacing[] enumFacingArray = EnumFacing.values();
                int n17 = enumFacingArray.length;
                int n18 = 0;
                while (n18 < n17) {
                    EnumFacing enumFacing = enumFacingArray[n18];
                    int n19 = n7 + enumFacing.getFrontOffsetX();
                    int n20 = n6 + enumFacing.getFrontOffsetY();
                    int n21 = n5 + enumFacing.getFrontOffsetZ();
                    mutableBlockPos.func_181079_c(n19, n20, n21);
                    int n22 = Math.max(1, this.getBlockState(mutableBlockPos).getBlock().getLightOpacity());
                    n4 = this.getLightFor(enumSkyBlock, mutableBlockPos);
                    if (n4 == n16 - n22 && n10 < this.lightUpdateBlockList.length) {
                        this.lightUpdateBlockList[n10++] = n19 - n13 + 32 | n20 - n14 + 32 << 6 | n21 - n15 + 32 << 12 | n16 - n22 << 18;
                    }
                    ++n18;
                }
            }
            n9 = 0;
        }
        this.theProfiler.endSection();
        this.theProfiler.startSection("checkedPosition < toCheckCount");
        while (n9 < n10) {
            boolean bl;
            n8 = this.lightUpdateBlockList[n9++];
            n7 = (n8 & 0x3F) - 32 + n13;
            n6 = (n8 >> 6 & 0x3F) - 32 + n14;
            n5 = (n8 >> 12 & 0x3F) - 32 + n15;
            BlockPos blockPos3 = new BlockPos(n7, n6, n5);
            int n23 = this.getLightFor(enumSkyBlock, blockPos3);
            n4 = this.getRawLight(blockPos3, enumSkyBlock);
            if (n4 == n23) continue;
            this.setLightFor(enumSkyBlock, blockPos3, n4);
            if (n4 <= n23) continue;
            n3 = Math.abs(n7 - n13);
            n2 = Math.abs(n6 - n14);
            n = Math.abs(n5 - n15);
            boolean bl2 = bl = n10 < this.lightUpdateBlockList.length - 6;
            if (n3 + n2 + n >= 17 || !bl) continue;
            if (this.getLightFor(enumSkyBlock, blockPos3.west()) < n4) {
                this.lightUpdateBlockList[n10++] = n7 - 1 - n13 + 32 + (n6 - n14 + 32 << 6) + (n5 - n15 + 32 << 12);
            }
            if (this.getLightFor(enumSkyBlock, blockPos3.east()) < n4) {
                this.lightUpdateBlockList[n10++] = n7 + 1 - n13 + 32 + (n6 - n14 + 32 << 6) + (n5 - n15 + 32 << 12);
            }
            if (this.getLightFor(enumSkyBlock, blockPos3.down()) < n4) {
                this.lightUpdateBlockList[n10++] = n7 - n13 + 32 + (n6 - 1 - n14 + 32 << 6) + (n5 - n15 + 32 << 12);
            }
            if (this.getLightFor(enumSkyBlock, blockPos3.up()) < n4) {
                this.lightUpdateBlockList[n10++] = n7 - n13 + 32 + (n6 + 1 - n14 + 32 << 6) + (n5 - n15 + 32 << 12);
            }
            if (this.getLightFor(enumSkyBlock, blockPos3.north()) < n4) {
                this.lightUpdateBlockList[n10++] = n7 - n13 + 32 + (n6 - n14 + 32 << 6) + (n5 - 1 - n15 + 32 << 12);
            }
            if (this.getLightFor(enumSkyBlock, blockPos3.south()) >= n4) continue;
            this.lightUpdateBlockList[n10++] = n7 - n13 + 32 + (n6 - n14 + 32 << 6) + (n5 + 1 - n15 + 32 << 12);
        }
        this.theProfiler.endSection();
        return true;
    }

    public WorldBorder getWorldBorder() {
        return this.worldBorder;
    }

    public List<Entity> getLoadedEntityList() {
        return this.loadedEntityList;
    }

    public void notifyLightSet(BlockPos blockPos) {
        int n = 0;
        while (n < this.worldAccesses.size()) {
            this.worldAccesses.get(n).notifyLightSet(blockPos);
            ++n;
        }
    }

    public ISaveHandler getSaveHandler() {
        return this.saveHandler;
    }

    public float getBlockDensity(Vec3 vec3, AxisAlignedBB axisAlignedBB) {
        double d = 1.0 / ((axisAlignedBB.maxX - axisAlignedBB.minX) * 2.0 + 1.0);
        double d2 = 1.0 / ((axisAlignedBB.maxY - axisAlignedBB.minY) * 2.0 + 1.0);
        double d3 = 1.0 / ((axisAlignedBB.maxZ - axisAlignedBB.minZ) * 2.0 + 1.0);
        double d4 = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
        double d5 = (1.0 - Math.floor(1.0 / d3) * d3) / 2.0;
        if (d >= 0.0 && d2 >= 0.0 && d3 >= 0.0) {
            int n = 0;
            int n2 = 0;
            float f = 0.0f;
            while (f <= 1.0f) {
                float f2 = 0.0f;
                while (f2 <= 1.0f) {
                    float f3 = 0.0f;
                    while (f3 <= 1.0f) {
                        double d6 = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) * (double)f;
                        double d7 = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) * (double)f2;
                        double d8 = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) * (double)f3;
                        if (this.rayTraceBlocks(new Vec3(d6 + d4, d7, d8 + d5), vec3) == null) {
                            ++n;
                        }
                        ++n2;
                        f3 = (float)((double)f3 + d3);
                    }
                    f2 = (float)((double)f2 + d2);
                }
                f = (float)((double)f + d);
            }
            return (float)n / (float)n2;
        }
        return 0.0f;
    }

    public void playSoundToNearExcept(EntityPlayer entityPlayer, String string, float f, float f2) {
        int n = 0;
        while (n < this.worldAccesses.size()) {
            this.worldAccesses.get(n).playSoundToNearExcept(entityPlayer, string, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, f, f2);
            ++n;
        }
    }

    public void unloadEntities(Collection<Entity> collection) {
        this.unloadedEntityList.addAll(collection);
    }

    public WorldChunkManager getWorldChunkManager() {
        return this.provider.getWorldChunkManager();
    }

    public void playSound(double d, double d2, double d3, String string, float f, float f2, boolean bl) {
    }

    public void markBlocksDirtyVertical(int n, int n2, int n3, int n4) {
        int n5;
        if (n3 > n4) {
            n5 = n4;
            n4 = n3;
            n3 = n5;
        }
        if (!this.provider.getHasNoSky()) {
            n5 = n3;
            while (n5 <= n4) {
                this.checkLightFor(EnumSkyBlock.SKY, new BlockPos(n, n5, n2));
                ++n5;
            }
        }
        this.markBlockRangeForRenderUpdate(n, n3, n2, n, n4, n2);
    }

    public void setItemData(String string, WorldSavedData worldSavedData) {
        this.mapStorage.setData(string, worldSavedData);
    }

    @Override
    public boolean isAirBlock(BlockPos blockPos) {
        return this.getBlockState(blockPos).getBlock().getMaterial() == Material.air;
    }

    public int getStrongPower(BlockPos blockPos) {
        int n = 0;
        if ((n = Math.max(n, this.getStrongPower(blockPos.down(), EnumFacing.DOWN))) >= 15) {
            return n;
        }
        if ((n = Math.max(n, this.getStrongPower(blockPos.up(), EnumFacing.UP))) >= 15) {
            return n;
        }
        if ((n = Math.max(n, this.getStrongPower(blockPos.north(), EnumFacing.NORTH))) >= 15) {
            return n;
        }
        if ((n = Math.max(n, this.getStrongPower(blockPos.south(), EnumFacing.SOUTH))) >= 15) {
            return n;
        }
        if ((n = Math.max(n, this.getStrongPower(blockPos.west(), EnumFacing.WEST))) >= 15) {
            return n;
        }
        return (n = Math.max(n, this.getStrongPower(blockPos.east(), EnumFacing.EAST))) >= 15 ? n : n;
    }

    public boolean setBlockState(BlockPos blockPos, IBlockState iBlockState, int n) {
        if (!this.isValid(blockPos)) {
            return false;
        }
        if (!this.isRemote && this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
            return false;
        }
        Chunk chunk = this.getChunkFromBlockCoords(blockPos);
        Block block = iBlockState.getBlock();
        IBlockState iBlockState2 = chunk.setBlockState(blockPos, iBlockState);
        if (iBlockState2 == null) {
            return false;
        }
        Block block2 = iBlockState2.getBlock();
        if (block.getLightOpacity() != block2.getLightOpacity() || block.getLightValue() != block2.getLightValue()) {
            this.theProfiler.startSection("checkLight");
            this.checkLight(blockPos);
            this.theProfiler.endSection();
        }
        if ((n & 2) != 0 && (!this.isRemote || (n & 4) == 0) && chunk.isPopulated()) {
            this.markBlockForUpdate(blockPos);
        }
        if (!this.isRemote && (n & 1) != 0) {
            this.notifyNeighborsRespectDebug(blockPos, iBlockState2.getBlock());
            if (block.hasComparatorInputOverride()) {
                this.updateComparatorOutputLevel(blockPos, block);
            }
        }
        return true;
    }

    protected void updateWeather() {
        if (!this.provider.getHasNoSky() && !this.isRemote) {
            int n;
            int n2 = this.worldInfo.getCleanWeatherTime();
            if (n2 > 0) {
                this.worldInfo.setCleanWeatherTime(--n2);
                this.worldInfo.setThunderTime(this.worldInfo.isThundering() ? 1 : 2);
                this.worldInfo.setRainTime(this.worldInfo.isRaining() ? 1 : 2);
            }
            if ((n = this.worldInfo.getThunderTime()) <= 0) {
                if (this.worldInfo.isThundering()) {
                    this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
                } else {
                    this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
                }
            } else {
                this.worldInfo.setThunderTime(--n);
                if (n <= 0) {
                    this.worldInfo.setThundering(!this.worldInfo.isThundering());
                }
            }
            this.prevThunderingStrength = this.thunderingStrength;
            this.thunderingStrength = this.worldInfo.isThundering() ? (float)((double)this.thunderingStrength + 0.01) : (float)((double)this.thunderingStrength - 0.01);
            this.thunderingStrength = MathHelper.clamp_float(this.thunderingStrength, 0.0f, 1.0f);
            int n3 = this.worldInfo.getRainTime();
            if (n3 <= 0) {
                if (this.worldInfo.isRaining()) {
                    this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
                } else {
                    this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
                }
            } else {
                this.worldInfo.setRainTime(--n3);
                if (n3 <= 0) {
                    this.worldInfo.setRaining(!this.worldInfo.isRaining());
                }
            }
            this.prevRainingStrength = this.rainingStrength;
            this.rainingStrength = this.worldInfo.isRaining() ? (float)((double)this.rainingStrength + 0.01) : (float)((double)this.rainingStrength - 0.01);
            this.rainingStrength = MathHelper.clamp_float(this.rainingStrength, 0.0f, 1.0f);
        }
    }

    public boolean setBlockToAir(BlockPos blockPos) {
        return this.setBlockState(blockPos, Blocks.air.getDefaultState(), 3);
    }

    public void makeFireworks(double d, double d2, double d3, double d4, double d5, double d6, NBTTagCompound nBTTagCompound) {
    }

    public BlockPos getTopSolidOrLiquidBlock(BlockPos blockPos) {
        Chunk chunk = this.getChunkFromBlockCoords(blockPos);
        BlockPos blockPos2 = new BlockPos(blockPos.getX(), chunk.getTopFilledSegment() + 16, blockPos.getZ());
        while (blockPos2.getY() >= 0) {
            BlockPos blockPos3 = blockPos2.down();
            Material material = chunk.getBlock(blockPos3).getMaterial();
            if (material.blocksMovement() && material != Material.leaves) break;
            blockPos2 = blockPos3;
        }
        return blockPos2;
    }

    public void initialize(WorldSettings worldSettings) {
        this.worldInfo.setServerInitialized(true);
    }

    protected abstract IChunkProvider createChunkProvider();

    public void setEntityState(Entity entity, byte by) {
    }

    public MapStorage getMapStorage() {
        return this.mapStorage;
    }

    public boolean extinguishFire(EntityPlayer entityPlayer, BlockPos blockPos, EnumFacing enumFacing) {
        if (this.getBlockState(blockPos = blockPos.offset(enumFacing)).getBlock() == Blocks.fire) {
            this.playAuxSFXAtEntity(entityPlayer, 1004, blockPos, 0);
            this.setBlockToAir(blockPos);
            return true;
        }
        return false;
    }

    public void setThunderStrength(float f) {
        this.prevThunderingStrength = f;
        this.thunderingStrength = f;
    }

    public VillageCollection getVillageCollection() {
        return this.villageCollectionObj;
    }

    public Scoreboard getScoreboard() {
        return this.worldScoreboard;
    }

    public boolean canBlockFreeze(BlockPos blockPos, boolean bl) {
        IBlockState iBlockState;
        Block block;
        BiomeGenBase biomeGenBase = this.getBiomeGenForCoords(blockPos);
        float f = biomeGenBase.getFloatTemperature(blockPos);
        if (f > 0.15f) {
            return false;
        }
        if (blockPos.getY() >= 0 && blockPos.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, blockPos) < 10 && ((block = (iBlockState = this.getBlockState(blockPos)).getBlock()) == Blocks.water || block == Blocks.flowing_water) && iBlockState.getValue(BlockLiquid.LEVEL) == 0) {
            boolean bl2;
            if (!bl) {
                return true;
            }
            boolean bl3 = bl2 = this.isWater(blockPos.west()) && this.isWater(blockPos.east()) && this.isWater(blockPos.north()) && this.isWater(blockPos.south());
            if (!bl2) {
                return true;
            }
        }
        return false;
    }

    public boolean canBlockSeeSky(BlockPos blockPos) {
        if (blockPos.getY() >= this.func_181545_F()) {
            return this.canSeeSky(blockPos);
        }
        BlockPos blockPos2 = new BlockPos(blockPos.getX(), this.func_181545_F(), blockPos.getZ());
        if (!this.canSeeSky(blockPos2)) {
            return false;
        }
        blockPos2 = blockPos2.down();
        while (blockPos2.getY() > blockPos.getY()) {
            Block block = this.getBlockState(blockPos2).getBlock();
            if (block.getLightOpacity() > 0 && !block.getMaterial().isLiquid()) {
                return false;
            }
            blockPos2 = blockPos2.down();
        }
        return true;
    }

    public int getLastLightningBolt() {
        return this.lastLightningBolt;
    }

    public static boolean doesBlockHaveSolidTopSurface(IBlockAccess iBlockAccess, BlockPos blockPos) {
        IBlockState iBlockState = iBlockAccess.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        return block.getMaterial().isOpaque() && block.isFullCube() ? true : (block instanceof BlockStairs ? iBlockState.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP : (block instanceof BlockSlab ? iBlockState.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP : (block instanceof BlockHopper ? true : (block instanceof BlockSnow ? iBlockState.getValue(BlockSnow.LAYERS) == 7 : false))));
    }

    public boolean isBlockLoaded(BlockPos blockPos) {
        return this.isBlockLoaded(blockPos, true);
    }

    private boolean isValid(BlockPos blockPos) {
        return blockPos.getX() >= -30000000 && blockPos.getZ() >= -30000000 && blockPos.getX() < 30000000 && blockPos.getZ() < 30000000 && blockPos.getY() >= 0 && blockPos.getY() < 256;
    }

    public void removeWorldAccess(IWorldAccess iWorldAccess) {
        this.worldAccesses.remove(iWorldAccess);
    }

    public boolean spawnEntityInWorld(Entity entity) {
        int n = MathHelper.floor_double(entity.posX / 16.0);
        int n2 = MathHelper.floor_double(entity.posZ / 16.0);
        boolean bl = entity.forceSpawn;
        if (entity instanceof EntityPlayer) {
            bl = true;
        }
        if (!bl && !this.isChunkLoaded(n, n2, true)) {
            return false;
        }
        if (entity instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer)entity;
            this.playerEntities.add(entityPlayer);
            this.updateAllPlayersSleepingFlag();
        }
        this.getChunkFromChunkCoords(n, n2).addEntity(entity);
        this.loadedEntityList.add(entity);
        this.onEntityAdded(entity);
        return true;
    }

    public EntityPlayer getPlayerEntityByUUID(UUID uUID) {
        int n = 0;
        while (n < this.playerEntities.size()) {
            EntityPlayer entityPlayer = this.playerEntities.get(n);
            if (uUID.equals(entityPlayer.getUniqueID())) {
                return entityPlayer;
            }
            ++n;
        }
        return null;
    }

    public void setSpawnPoint(BlockPos blockPos) {
        this.worldInfo.setSpawn(blockPos);
    }

    public BlockPos getHeight(BlockPos blockPos) {
        int n = blockPos.getX() >= -30000000 && blockPos.getZ() >= -30000000 && blockPos.getX() < 30000000 && blockPos.getZ() < 30000000 ? (this.isChunkLoaded(blockPos.getX() >> 4, blockPos.getZ() >> 4, true) ? this.getChunkFromChunkCoords(blockPos.getX() >> 4, blockPos.getZ() >> 4).getHeightValue(blockPos.getX() & 0xF, blockPos.getZ() & 0xF) : 0) : this.func_181545_F() + 1;
        return new BlockPos(blockPos.getX(), n, blockPos.getZ());
    }

    public int getLightFor(EnumSkyBlock enumSkyBlock, BlockPos blockPos) {
        if (blockPos.getY() < 0) {
            blockPos = new BlockPos(blockPos.getX(), 0, blockPos.getZ());
        }
        if (!this.isValid(blockPos)) {
            return enumSkyBlock.defaultLightValue;
        }
        if (!this.isBlockLoaded(blockPos)) {
            return enumSkyBlock.defaultLightValue;
        }
        Chunk chunk = this.getChunkFromBlockCoords(blockPos);
        return chunk.getLightFor(enumSkyBlock, blockPos);
    }

    public boolean isAABBInMaterial(AxisAlignedBB axisAlignedBB, Material material) {
        int n = MathHelper.floor_double(axisAlignedBB.minX);
        int n2 = MathHelper.floor_double(axisAlignedBB.maxX + 1.0);
        int n3 = MathHelper.floor_double(axisAlignedBB.minY);
        int n4 = MathHelper.floor_double(axisAlignedBB.maxY + 1.0);
        int n5 = MathHelper.floor_double(axisAlignedBB.minZ);
        int n6 = MathHelper.floor_double(axisAlignedBB.maxZ + 1.0);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int n7 = n;
        while (n7 < n2) {
            int n8 = n3;
            while (n8 < n4) {
                int n9 = n5;
                while (n9 < n6) {
                    IBlockState iBlockState = this.getBlockState(mutableBlockPos.func_181079_c(n7, n8, n9));
                    Block block = iBlockState.getBlock();
                    if (block.getMaterial() == material) {
                        int n10 = iBlockState.getValue(BlockLiquid.LEVEL);
                        double d = n8 + 1;
                        if (n10 < 8) {
                            d = (double)(n8 + 1) - (double)n10 / 8.0;
                        }
                        if (d >= axisAlignedBB.minY) {
                            return true;
                        }
                    }
                    ++n9;
                }
                ++n8;
            }
            ++n7;
        }
        return false;
    }

    public boolean isSpawnChunk(int n, int n2) {
        BlockPos blockPos = this.getSpawnPoint();
        int n3 = n * 16 + 8 - blockPos.getX();
        int n4 = n2 * 16 + 8 - blockPos.getZ();
        int n5 = 128;
        return n3 >= -n5 && n3 <= n5 && n4 >= -n5 && n4 <= n5;
    }

    public void playAuxSFXAtEntity(EntityPlayer entityPlayer, int n, BlockPos blockPos, int n2) {
        try {
            int n3 = 0;
            while (n3 < this.worldAccesses.size()) {
                this.worldAccesses.get(n3).playAuxSFX(entityPlayer, n, blockPos, n2);
                ++n3;
            }
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Playing level event");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("Level event being played");
            crashReportCategory.addCrashSection("Block coordinates", CrashReportCategory.getCoordinateInfo(blockPos));
            crashReportCategory.addCrashSection("Event source", entityPlayer);
            crashReportCategory.addCrashSection("Event type", n);
            crashReportCategory.addCrashSection("Event data", n2);
            throw new ReportedException(crashReport);
        }
    }

    public void addWorldAccess(IWorldAccess iWorldAccess) {
        this.worldAccesses.add(iWorldAccess);
    }
}

