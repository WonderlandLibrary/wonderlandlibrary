/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 */
package net.minecraft.client.multiplayer;

import com.google.common.collect.Sets;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EntityFirework;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;

public class WorldClient
extends World {
    private final Minecraft mc;
    private NetHandlerPlayClient sendQueue;
    private ChunkProviderClient clientChunkProvider;
    private final Set<Entity> entityList = Sets.newHashSet();
    private final Set<ChunkCoordIntPair> previousActiveChunkSet;
    private final Set<Entity> entitySpawnQueue = Sets.newHashSet();

    public boolean invalidateRegionAndSetBlock(BlockPos blockPos, IBlockState iBlockState) {
        int n = blockPos.getX();
        int n2 = blockPos.getY();
        int n3 = blockPos.getZ();
        this.invalidateBlockReceiveRegion(n, n2, n3, n, n2, n3);
        return super.setBlockState(blockPos, iBlockState, 3);
    }

    @Override
    public boolean spawnEntityInWorld(Entity entity) {
        boolean bl = super.spawnEntityInWorld(entity);
        this.entityList.add(entity);
        if (!bl) {
            this.entitySpawnQueue.add(entity);
        } else if (entity instanceof EntityMinecart) {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecart((EntityMinecart)entity));
        }
        return bl;
    }

    public void playSoundAtPos(BlockPos blockPos, String string, float f, float f2, boolean bl) {
        this.playSound((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, string, f, f2, bl);
    }

    public void removeAllEntities() {
        int n;
        int n2;
        Entity entity;
        this.loadedEntityList.removeAll(this.unloadedEntityList);
        int n3 = 0;
        while (n3 < this.unloadedEntityList.size()) {
            entity = (Entity)this.unloadedEntityList.get(n3);
            n2 = entity.chunkCoordX;
            n = entity.chunkCoordZ;
            if (entity.addedToChunk && this.isChunkLoaded(n2, n, true)) {
                this.getChunkFromChunkCoords(n2, n).removeEntity(entity);
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < this.unloadedEntityList.size()) {
            this.onEntityRemoved((Entity)this.unloadedEntityList.get(n3));
            ++n3;
        }
        this.unloadedEntityList.clear();
        n3 = 0;
        while (n3 < this.loadedEntityList.size()) {
            block10: {
                block9: {
                    entity = (Entity)this.loadedEntityList.get(n3);
                    if (entity.ridingEntity == null) break block9;
                    if (!entity.ridingEntity.isDead && entity.ridingEntity.riddenByEntity == entity) break block10;
                    entity.ridingEntity.riddenByEntity = null;
                    entity.ridingEntity = null;
                }
                if (entity.isDead) {
                    n2 = entity.chunkCoordX;
                    n = entity.chunkCoordZ;
                    if (entity.addedToChunk && this.isChunkLoaded(n2, n, true)) {
                        this.getChunkFromChunkCoords(n2, n).removeEntity(entity);
                    }
                    this.loadedEntityList.remove(n3--);
                    this.onEntityRemoved(entity);
                }
            }
            ++n3;
        }
    }

    @Override
    public void playSound(double d, double d2, double d3, String string, float f, float f2, boolean bl) {
        double d4 = this.mc.getRenderViewEntity().getDistanceSq(d, d2, d3);
        PositionedSoundRecord positionedSoundRecord = new PositionedSoundRecord(new ResourceLocation(string), f, f2, (float)d, (float)d2, (float)d3);
        if (bl && d4 > 100.0) {
            double d5 = Math.sqrt(d4) / 40.0;
            this.mc.getSoundHandler().playDelayedSound(positionedSoundRecord, (int)(d5 * 20.0));
        } else {
            this.mc.getSoundHandler().playSound(positionedSoundRecord);
        }
    }

    @Override
    protected void updateBlocks() {
        super.updateBlocks();
        this.previousActiveChunkSet.retainAll(this.activeChunkSet);
        if (this.previousActiveChunkSet.size() == this.activeChunkSet.size()) {
            this.previousActiveChunkSet.clear();
        }
        int n = 0;
        for (ChunkCoordIntPair chunkCoordIntPair : this.activeChunkSet) {
            if (this.previousActiveChunkSet.contains(chunkCoordIntPair)) continue;
            int n2 = chunkCoordIntPair.chunkXPos * 16;
            int n3 = chunkCoordIntPair.chunkZPos * 16;
            this.theProfiler.startSection("getChunk");
            Chunk chunk = this.getChunkFromChunkCoords(chunkCoordIntPair.chunkXPos, chunkCoordIntPair.chunkZPos);
            this.playMoodSoundAndCheckLight(n2, n3, chunk);
            this.theProfiler.endSection();
            this.previousActiveChunkSet.add(chunkCoordIntPair);
            if (++n < 10) continue;
            return;
        }
    }

    public void doVoidFogParticles(int n, int n2, int n3) {
        int n4 = 16;
        Random random = new Random();
        ItemStack itemStack = Minecraft.thePlayer.getHeldItem();
        boolean bl = Minecraft.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE && itemStack != null && Block.getBlockFromItem(itemStack.getItem()) == Blocks.barrier;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int n5 = 0;
        while (n5 < 1000) {
            int n6 = n + this.rand.nextInt(n4) - this.rand.nextInt(n4);
            int n7 = n2 + this.rand.nextInt(n4) - this.rand.nextInt(n4);
            int n8 = n3 + this.rand.nextInt(n4) - this.rand.nextInt(n4);
            mutableBlockPos.func_181079_c(n6, n7, n8);
            IBlockState iBlockState = this.getBlockState(mutableBlockPos);
            iBlockState.getBlock().randomDisplayTick(this, mutableBlockPos, iBlockState, random);
            if (bl && iBlockState.getBlock() == Blocks.barrier) {
                this.spawnParticle(EnumParticleTypes.BARRIER, (float)n6 + 0.5f, (double)((float)n7 + 0.5f), (double)((float)n8 + 0.5f), 0.0, 0.0, 0.0, new int[0]);
            }
            ++n5;
        }
    }

    @Override
    protected void onEntityAdded(Entity entity) {
        super.onEntityAdded(entity);
        if (this.entitySpawnQueue.contains(entity)) {
            this.entitySpawnQueue.remove(entity);
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
        int n = 0;
        while (n < 10 && !this.entitySpawnQueue.isEmpty()) {
            Entity entity = this.entitySpawnQueue.iterator().next();
            this.entitySpawnQueue.remove(entity);
            if (!this.loadedEntityList.contains(entity)) {
                this.spawnEntityInWorld(entity);
            }
            ++n;
        }
        this.theProfiler.endStartSection("chunkCache");
        this.clientChunkProvider.unloadQueuedChunks();
        this.theProfiler.endStartSection("blocks");
        this.updateBlocks();
        this.theProfiler.endSection();
    }

    @Override
    public void removeEntity(Entity entity) {
        super.removeEntity(entity);
        this.entityList.remove(entity);
    }

    public WorldClient(NetHandlerPlayClient netHandlerPlayClient, WorldSettings worldSettings, int n, EnumDifficulty enumDifficulty, Profiler profiler) {
        super(new SaveHandlerMP(), new WorldInfo(worldSettings, "MpServer"), WorldProvider.getProviderForDimension(n), profiler, true);
        this.mc = Minecraft.getMinecraft();
        this.previousActiveChunkSet = Sets.newHashSet();
        this.sendQueue = netHandlerPlayClient;
        this.getWorldInfo().setDifficulty(enumDifficulty);
        this.setSpawnPoint(new BlockPos(8, 64, 8));
        this.provider.registerWorld(this);
        this.chunkProvider = this.createChunkProvider();
        this.mapStorage = new SaveDataMemoryStorage();
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
    }

    @Override
    public void setWorldTime(long l) {
        if (l < 0L) {
            l = -l;
            this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
        } else {
            this.getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
        }
        super.setWorldTime(l);
    }

    @Override
    protected void updateWeather() {
    }

    @Override
    protected int getRenderDistanceChunks() {
        return Minecraft.gameSettings.renderDistanceChunks;
    }

    public void doPreChunk(int n, int n2, boolean bl) {
        if (bl) {
            this.clientChunkProvider.loadChunk(n, n2);
        } else {
            this.clientChunkProvider.unloadChunk(n, n2);
        }
        if (!bl) {
            this.markBlockRangeForRenderUpdate(n * 16, 0, n2 * 16, n * 16 + 15, 256, n2 * 16 + 15);
        }
    }

    @Override
    public void sendQuittingDisconnectingPacket() {
        this.sendQueue.getNetworkManager().closeChannel(new ChatComponentText("Quitting"));
    }

    public void invalidateBlockReceiveRegion(int n, int n2, int n3, int n4, int n5, int n6) {
    }

    @Override
    public void makeFireworks(double d, double d2, double d3, double d4, double d5, double d6, NBTTagCompound nBTTagCompound) {
        this.mc.effectRenderer.addEffect(new EntityFirework.StarterFX(this, d, d2, d3, d4, d5, d6, this.mc.effectRenderer, nBTTagCompound));
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        this.clientChunkProvider = new ChunkProviderClient(this);
        return this.clientChunkProvider;
    }

    @Override
    public CrashReportCategory addWorldInfoToCrashReport(CrashReport crashReport) {
        CrashReportCategory crashReportCategory = super.addWorldInfoToCrashReport(crashReport);
        crashReportCategory.addCrashSectionCallable("Forced entities", new Callable<String>(){

            @Override
            public String call() {
                return String.valueOf(WorldClient.this.entityList.size()) + " total; " + WorldClient.this.entityList.toString();
            }
        });
        crashReportCategory.addCrashSectionCallable("Retry entities", new Callable<String>(){

            @Override
            public String call() {
                return String.valueOf(WorldClient.this.entitySpawnQueue.size()) + " total; " + WorldClient.this.entitySpawnQueue.toString();
            }
        });
        crashReportCategory.addCrashSectionCallable("Server brand", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return Minecraft.thePlayer.getClientBrand();
            }
        });
        crashReportCategory.addCrashSectionCallable("Server type", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return WorldClient.this.mc.getIntegratedServer() == null ? "Non-integrated multiplayer server" : "Integrated singleplayer server";
            }
        });
        return crashReportCategory;
    }

    @Override
    public Entity getEntityByID(int n) {
        return n == Minecraft.thePlayer.getEntityId() ? Minecraft.thePlayer : super.getEntityByID(n);
    }

    public void setWorldScoreboard(Scoreboard scoreboard) {
        this.worldScoreboard = scoreboard;
    }

    public void addEntityToWorld(int n, Entity entity) {
        Entity entity2 = this.getEntityByID(n);
        if (entity2 != null) {
            this.removeEntity(entity2);
        }
        this.entityList.add(entity);
        entity.setEntityId(n);
        if (!this.spawnEntityInWorld(entity)) {
            this.entitySpawnQueue.add(entity);
        }
        this.entitiesById.addKey(n, entity);
    }

    @Override
    protected void onEntityRemoved(Entity entity) {
        super.onEntityRemoved(entity);
        boolean bl = false;
        if (this.entityList.contains(entity)) {
            if (entity.isEntityAlive()) {
                this.entitySpawnQueue.add(entity);
                bl = true;
            } else {
                this.entityList.remove(entity);
            }
        }
    }

    public Entity removeEntityFromWorld(int n) {
        Entity entity = (Entity)this.entitiesById.removeObject(n);
        if (entity != null) {
            this.entityList.remove(entity);
            this.removeEntity(entity);
        }
        return entity;
    }
}

