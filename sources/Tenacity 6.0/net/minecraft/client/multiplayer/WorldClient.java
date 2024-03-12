// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.multiplayer;

import net.optifine.DynamicLights;
import net.minecraft.src.Config;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFirework;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import java.util.Random;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.chunk.Chunk;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.world.chunk.IChunkProvider;
import net.optifine.CustomGuis;
import net.optifine.override.PlayerControllerOF;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.util.BlockPos;
import com.google.common.collect.Sets;
import net.minecraft.world.storage.ISaveHandler;
import java.util.Objects;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import java.util.Set;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.world.World;

public class WorldClient extends World
{
    private final NetHandlerPlayClient sendQueue;
    private ChunkProviderClient clientChunkProvider;
    private final Set<Entity> entityList;
    private final Set<Entity> entitySpawnQueue;
    private final Minecraft mc;
    private final Set<ChunkCoordIntPair> previousActiveChunkSet;
    private boolean playerUpdate;
    
    public WorldClient(final NetHandlerPlayClient netHandler, final WorldSettings settings, final int dimension, final EnumDifficulty difficulty, final Profiler profilerIn) {
        super(new SaveHandlerMP(), new WorldInfo(settings, "MpServer"), Objects.requireNonNull(WorldProvider.getProviderForDimension(dimension)), profilerIn, true);
        this.entityList = (Set<Entity>)Sets.newHashSet();
        this.entitySpawnQueue = (Set<Entity>)Sets.newHashSet();
        this.mc = Minecraft.getMinecraft();
        this.previousActiveChunkSet = (Set<ChunkCoordIntPair>)Sets.newHashSet();
        this.playerUpdate = false;
        this.sendQueue = netHandler;
        this.getWorldInfo().setDifficulty(difficulty);
        this.provider.registerWorld(this);
        this.setSpawnPoint(new BlockPos(8, 64, 8));
        this.chunkProvider = this.createChunkProvider();
        this.mapStorage = new SaveDataMemoryStorage();
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
        if (this.mc.playerController != null && this.mc.playerController.getClass() == PlayerControllerMP.class) {
            this.mc.playerController = new PlayerControllerOF(this.mc, netHandler);
            CustomGuis.setPlayerControllerOF((PlayerControllerOF)this.mc.playerController);
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        this.setTotalWorldTime(this.getTotalWorldTime() + 1L);
        if (this.getGameRules().getBoolean("doDaylightCycle")) {
            this.setWorldTime(this.getWorldTime() + 1L);
        }
        this.theProfiler.startSection("reEntryProcessing");
        for (int i = 0; i < 10 && !this.entitySpawnQueue.isEmpty(); ++i) {
            final Entity entity = this.entitySpawnQueue.iterator().next();
            this.entitySpawnQueue.remove(entity);
            if (!this.loadedEntityList.contains(entity)) {
                this.spawnEntityInWorld(entity);
            }
        }
        this.theProfiler.endStartSection("chunkCache");
        this.clientChunkProvider.unloadQueuedChunks();
        this.theProfiler.endStartSection("blocks");
        this.updateBlocks();
        this.theProfiler.endSection();
    }
    
    public void invalidateBlockReceiveRegion(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
    }
    
    @Override
    protected IChunkProvider createChunkProvider() {
        return this.clientChunkProvider = new ChunkProviderClient(this);
    }
    
    @Override
    protected void updateBlocks() {
        super.updateBlocks();
        this.previousActiveChunkSet.retainAll(this.activeChunkSet);
        if (this.previousActiveChunkSet.size() == this.activeChunkSet.size()) {
            this.previousActiveChunkSet.clear();
        }
        int i = 0;
        for (final ChunkCoordIntPair chunkcoordintpair : this.activeChunkSet) {
            if (!this.previousActiveChunkSet.contains(chunkcoordintpair)) {
                final int j = chunkcoordintpair.chunkXPos * 16;
                final int k = chunkcoordintpair.chunkZPos * 16;
                this.theProfiler.startSection("getChunk");
                final Chunk chunk = this.getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
                this.playMoodSoundAndCheckLight(j, k, chunk);
                this.theProfiler.endSection();
                this.previousActiveChunkSet.add(chunkcoordintpair);
                if (++i >= 10) {
                    return;
                }
                continue;
            }
        }
    }
    
    public void doPreChunk(final int chuncX, final int chuncZ, final boolean loadChunk) {
        if (loadChunk) {
            this.clientChunkProvider.loadChunk(chuncX, chuncZ);
        }
        else {
            this.clientChunkProvider.unloadChunk(chuncX, chuncZ);
        }
        if (!loadChunk) {
            this.markBlockRangeForRenderUpdate(chuncX * 16, 0, chuncZ * 16, chuncX * 16 + 15, 256, chuncZ * 16 + 15);
        }
    }
    
    @Override
    public boolean spawnEntityInWorld(final Entity entityIn) {
        final boolean flag = super.spawnEntityInWorld(entityIn);
        this.entityList.add(entityIn);
        if (!flag) {
            this.entitySpawnQueue.add(entityIn);
        }
        else if (entityIn instanceof EntityMinecart) {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecart((EntityMinecart)entityIn));
        }
        return flag;
    }
    
    @Override
    public void removeEntity(final Entity entityIn) {
        super.removeEntity(entityIn);
        this.entityList.remove(entityIn);
    }
    
    @Override
    protected void onEntityAdded(final Entity entityIn) {
        super.onEntityAdded(entityIn);
        this.entitySpawnQueue.remove(entityIn);
    }
    
    @Override
    protected void onEntityRemoved(final Entity entityIn) {
        super.onEntityRemoved(entityIn);
        if (this.entityList.contains(entityIn)) {
            if (entityIn.isEntityAlive()) {
                this.entitySpawnQueue.add(entityIn);
            }
            else {
                this.entityList.remove(entityIn);
            }
        }
    }
    
    public void addEntityToWorld(final int entityID, final Entity entityToSpawn) {
        final Entity entity = this.getEntityByID(entityID);
        if (entity != null) {
            this.removeEntity(entity);
        }
        this.entityList.add(entityToSpawn);
        entityToSpawn.setEntityId(entityID);
        if (!this.spawnEntityInWorld(entityToSpawn)) {
            this.entitySpawnQueue.add(entityToSpawn);
        }
        this.entitiesById.addKey(entityID, entityToSpawn);
    }
    
    @Override
    public Entity getEntityByID(final int id) {
        return (id == this.mc.thePlayer.getEntityId()) ? this.mc.thePlayer : super.getEntityByID(id);
    }
    
    public void removeEntityFromWorld(final int entityID) {
        final Entity entity = this.entitiesById.removeObject(entityID);
        if (entity != null) {
            this.entityList.remove(entity);
            this.removeEntity(entity);
        }
    }
    
    public void invalidateRegionAndSetBlock(final BlockPos pos, final IBlockState state) {
        final int i = pos.getX();
        final int j = pos.getY();
        final int k = pos.getZ();
        this.invalidateBlockReceiveRegion(i, j, k, i, j, k);
        super.setBlockState(pos, state, 3);
    }
    
    @Override
    public void sendQuittingDisconnectingPacket() {
        this.sendQueue.getNetworkManager().closeChannel(new ChatComponentText("Quitting"));
    }
    
    @Override
    protected void updateWeather() {
    }
    
    @Override
    protected int getRenderDistanceChunks() {
        return this.mc.gameSettings.renderDistanceChunks;
    }
    
    public void doVoidFogParticles(final int posX, final int posY, final int posZ) {
        final int i = 16;
        final Random random = new Random();
        final ItemStack itemstack = this.mc.thePlayer.getHeldItem();
        final boolean flag = this.mc.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE && itemstack != null && Block.getBlockFromItem(itemstack.getItem()) == Blocks.barrier;
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int j = 0; j < 1000; ++j) {
            final int k = posX + this.rand.nextInt(i) - this.rand.nextInt(i);
            final int l = posY + this.rand.nextInt(i) - this.rand.nextInt(i);
            final int i2 = posZ + this.rand.nextInt(i) - this.rand.nextInt(i);
            blockpos$mutableblockpos.set(k, l, i2);
            final IBlockState iblockstate = this.getBlockState(blockpos$mutableblockpos);
            iblockstate.getBlock().randomDisplayTick(this, blockpos$mutableblockpos, iblockstate, random);
            if (flag && iblockstate.getBlock() == Blocks.barrier) {
                this.spawnParticle(EnumParticleTypes.BARRIER, k + 0.5f, l + 0.5f, i2 + 0.5f, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }
    
    public void removeAllEntities() {
        this.loadedEntityList.removeAll(this.unloadedEntityList);
        for (final Entity value : this.unloadedEntityList) {
            final int j = value.chunkCoordX;
            final int k = value.chunkCoordZ;
            if (value.addedToChunk && this.isChunkLoaded(j, k, true)) {
                this.getChunkFromChunkCoords(j, k).removeEntity(value);
            }
        }
        for (final Entity entity : this.unloadedEntityList) {
            this.onEntityRemoved(entity);
        }
        this.unloadedEntityList.clear();
        for (int i1 = 0; i1 < this.loadedEntityList.size(); ++i1) {
            final Entity entity2 = this.loadedEntityList.get(i1);
            if (entity2.ridingEntity != null) {
                if (!entity2.ridingEntity.isDead && entity2.ridingEntity.riddenByEntity == entity2) {
                    continue;
                }
                entity2.ridingEntity.riddenByEntity = null;
                entity2.ridingEntity = null;
            }
            if (entity2.isDead) {
                final int j2 = entity2.chunkCoordX;
                final int k2 = entity2.chunkCoordZ;
                if (entity2.addedToChunk && this.isChunkLoaded(j2, k2, true)) {
                    this.getChunkFromChunkCoords(j2, k2).removeEntity(entity2);
                }
                this.loadedEntityList.remove(i1--);
                this.onEntityRemoved(entity2);
            }
        }
    }
    
    @Override
    public CrashReportCategory addWorldInfoToCrashReport(final CrashReport report) {
        final CrashReportCategory crashreportcategory = super.addWorldInfoToCrashReport(report);
        crashreportcategory.addCrashSectionCallable("Forced entities", () -> this.entityList.size() + " total; " + this.entityList);
        crashreportcategory.addCrashSectionCallable("Retry entities", () -> this.entitySpawnQueue.size() + " total; " + this.entitySpawnQueue);
        crashreportcategory.addCrashSectionCallable("Server brand", () -> this.mc.thePlayer.getClientBrand());
        crashreportcategory.addCrashSectionCallable("Server type", () -> (this.mc.getIntegratedServer() == null) ? "Non-integrated multiplayer server" : "Integrated singleplayer server");
        return crashreportcategory;
    }
    
    public void playSoundAtPos(final BlockPos pos, final String soundName, final float volume, final float pitch, final boolean distanceDelay) {
        this.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, soundName, volume, pitch, distanceDelay);
    }
    
    @Override
    public void playSound(final double x, final double y, final double z, final String soundName, final float volume, final float pitch, final boolean distanceDelay) {
        final double d0 = this.mc.getRenderViewEntity().getDistanceSq(x, y, z);
        final PositionedSoundRecord positionedsoundrecord = new PositionedSoundRecord(new ResourceLocation(soundName), volume, pitch, (float)x, (float)y, (float)z);
        if (distanceDelay && d0 > 100.0) {
            final double d2 = Math.sqrt(d0) / 40.0;
            this.mc.getSoundHandler().playDelayedSound(positionedsoundrecord, (int)(d2 * 20.0));
        }
        else {
            this.mc.getSoundHandler().playSound(positionedsoundrecord);
        }
    }
    
    @Override
    public void makeFireworks(final double x, final double y, final double z, final double motionX, final double motionY, final double motionZ, final NBTTagCompound compund) {
        this.mc.effectRenderer.addEffect(new EntityFirework.StarterFX(this, x, y, z, motionX, motionY, motionZ, this.mc.effectRenderer, compund));
    }
    
    public void setWorldScoreboard(final Scoreboard scoreboardIn) {
        this.worldScoreboard = scoreboardIn;
    }
    
    @Override
    public void setWorldTime(long time) {
        if (time < 0L) {
            time = -time;
            this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
        }
        else {
            this.getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
        }
        super.setWorldTime(time);
    }
    
    @Override
    public int getCombinedLight(final BlockPos pos, final int lightValue) {
        int i = super.getCombinedLight(pos, lightValue);
        if (Config.isDynamicLights()) {
            i = DynamicLights.getCombinedLight(pos, i);
        }
        return i;
    }
    
    @Override
    public boolean setBlockState(final BlockPos pos, final IBlockState newState, final int flags) {
        this.playerUpdate = this.isPlayerActing();
        final boolean flag = super.setBlockState(pos, newState, flags);
        this.playerUpdate = false;
        return flag;
    }
    
    private boolean isPlayerActing() {
        if (this.mc.playerController instanceof PlayerControllerOF) {
            final PlayerControllerOF playercontrollerof = (PlayerControllerOF)this.mc.playerController;
            return playercontrollerof.isActing();
        }
        return false;
    }
    
    public boolean isPlayerUpdate() {
        return this.playerUpdate;
    }
}
