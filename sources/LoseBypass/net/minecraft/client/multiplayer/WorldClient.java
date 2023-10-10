/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.multiplayer;

import com.google.common.collect.Sets;
import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.event.events.TickEvent;
import java.util.Iterator;
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
    private NetHandlerPlayClient sendQueue;
    private ChunkProviderClient clientChunkProvider;
    private final Set<Entity> entityList = Sets.newHashSet();
    private final Set<Entity> entitySpawnQueue = Sets.newHashSet();
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Set<ChunkCoordIntPair> previousActiveChunkSet = Sets.newHashSet();

    public WorldClient(NetHandlerPlayClient p_i45063_1_, WorldSettings p_i45063_2_, int p_i45063_3_, EnumDifficulty p_i45063_4_, Profiler p_i45063_5_) {
        super(new SaveHandlerMP(), new WorldInfo(p_i45063_2_, "MpServer"), WorldProvider.getProviderForDimension(p_i45063_3_), p_i45063_5_, true);
        this.sendQueue = p_i45063_1_;
        this.getWorldInfo().setDifficulty(p_i45063_4_);
        this.setSpawnPoint(new BlockPos(8, 64, 8));
        this.provider.registerWorld(this);
        this.chunkProvider = this.createChunkProvider();
        this.mapStorage = new SaveDataMemoryStorage();
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
    }

    @Override
    public void tick() {
        super.tick();
        this.setTotalWorldTime(this.getTotalWorldTime() + 1L);
        if (this.getGameRules().getBoolean("doDaylightCycle")) {
            this.setWorldTime(this.getWorldTime() + 1L);
        }
        this.theProfiler.startSection("reEntryProcessing");
        LoseBypass.eventBus.post(new TickEvent());
        for (int i = 0; i < 10 && !this.entitySpawnQueue.isEmpty(); ++i) {
            Entity entity = this.entitySpawnQueue.iterator().next();
            this.entitySpawnQueue.remove(entity);
            if (this.loadedEntityList.contains(entity)) continue;
            this.spawnEntityInWorld(entity);
        }
        this.theProfiler.endStartSection("chunkCache");
        this.clientChunkProvider.unloadQueuedChunks();
        this.theProfiler.endStartSection("blocks");
        this.updateBlocks();
        this.theProfiler.endSection();
    }

    public void invalidateBlockReceiveRegion(int p_73031_1_, int p_73031_2_, int p_73031_3_, int p_73031_4_, int p_73031_5_, int p_73031_6_) {
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        this.clientChunkProvider = new ChunkProviderClient(this);
        return this.clientChunkProvider;
    }

    @Override
    protected void updateBlocks() {
        super.updateBlocks();
        this.previousActiveChunkSet.retainAll(this.activeChunkSet);
        if (this.previousActiveChunkSet.size() == this.activeChunkSet.size()) {
            this.previousActiveChunkSet.clear();
        }
        int i = 0;
        Iterator iterator = this.activeChunkSet.iterator();
        while (iterator.hasNext()) {
            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)iterator.next();
            if (this.previousActiveChunkSet.contains(chunkcoordintpair)) continue;
            int j = chunkcoordintpair.chunkXPos * 16;
            int k = chunkcoordintpair.chunkZPos * 16;
            this.theProfiler.startSection("getChunk");
            Chunk chunk = this.getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
            this.playMoodSoundAndCheckLight(j, k, chunk);
            this.theProfiler.endSection();
            this.previousActiveChunkSet.add(chunkcoordintpair);
            if (++i >= 10) return;
        }
    }

    public void doPreChunk(int p_73025_1_, int p_73025_2_, boolean p_73025_3_) {
        if (p_73025_3_) {
            this.clientChunkProvider.loadChunk(p_73025_1_, p_73025_2_);
        } else {
            this.clientChunkProvider.unloadChunk(p_73025_1_, p_73025_2_);
        }
        if (p_73025_3_) return;
        this.markBlockRangeForRenderUpdate(p_73025_1_ * 16, 0, p_73025_2_ * 16, p_73025_1_ * 16 + 15, 256, p_73025_2_ * 16 + 15);
    }

    @Override
    public boolean spawnEntityInWorld(Entity entityIn) {
        boolean flag = super.spawnEntityInWorld(entityIn);
        this.entityList.add(entityIn);
        if (!flag) {
            this.entitySpawnQueue.add(entityIn);
            return flag;
        }
        if (!(entityIn instanceof EntityMinecart)) return flag;
        this.mc.getSoundHandler().playSound(new MovingSoundMinecart((EntityMinecart)entityIn));
        return flag;
    }

    @Override
    public void removeEntity(Entity entityIn) {
        super.removeEntity(entityIn);
        this.entityList.remove(entityIn);
    }

    @Override
    protected void onEntityAdded(Entity entityIn) {
        super.onEntityAdded(entityIn);
        if (!this.entitySpawnQueue.contains(entityIn)) return;
        this.entitySpawnQueue.remove(entityIn);
    }

    @Override
    protected void onEntityRemoved(Entity entityIn) {
        super.onEntityRemoved(entityIn);
        boolean flag = false;
        if (!this.entityList.contains(entityIn)) return;
        if (entityIn.isEntityAlive()) {
            this.entitySpawnQueue.add(entityIn);
            return;
        }
        this.entityList.remove(entityIn);
    }

    public void addEntityToWorld(int p_73027_1_, Entity p_73027_2_) {
        Entity entity = this.getEntityByID(p_73027_1_);
        if (entity != null) {
            this.removeEntity(entity);
        }
        this.entityList.add(p_73027_2_);
        p_73027_2_.setEntityId(p_73027_1_);
        if (!this.spawnEntityInWorld(p_73027_2_)) {
            this.entitySpawnQueue.add(p_73027_2_);
        }
        this.entitiesById.addKey(p_73027_1_, p_73027_2_);
    }

    @Override
    public Entity getEntityByID(int id) {
        Entity entity;
        if (id == this.mc.thePlayer.getEntityId()) {
            entity = this.mc.thePlayer;
            return entity;
        }
        entity = super.getEntityByID(id);
        return entity;
    }

    public Entity removeEntityFromWorld(int p_73028_1_) {
        Entity entity = (Entity)this.entitiesById.removeObject(p_73028_1_);
        if (entity == null) return entity;
        this.entityList.remove(entity);
        this.removeEntity(entity);
        return entity;
    }

    public boolean invalidateRegionAndSetBlock(BlockPos p_180503_1_, IBlockState p_180503_2_) {
        int i = p_180503_1_.getX();
        int j = p_180503_1_.getY();
        int k = p_180503_1_.getZ();
        this.invalidateBlockReceiveRegion(i, j, k, i, j, k);
        return super.setBlockState(p_180503_1_, p_180503_2_, 3);
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

    public void doVoidFogParticles(int p_73029_1_, int p_73029_2_, int p_73029_3_) {
        int i = 16;
        Random random = new Random();
        ItemStack itemstack = this.mc.thePlayer.getHeldItem();
        boolean flag = this.mc.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE && itemstack != null && Block.getBlockFromItem(itemstack.getItem()) == Blocks.barrier;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int j = 0;
        while (j < 1000) {
            int k = p_73029_1_ + this.rand.nextInt(i) - this.rand.nextInt(i);
            int l = p_73029_2_ + this.rand.nextInt(i) - this.rand.nextInt(i);
            int i1 = p_73029_3_ + this.rand.nextInt(i) - this.rand.nextInt(i);
            blockpos$mutableblockpos.func_181079_c(k, l, i1);
            IBlockState iblockstate = this.getBlockState(blockpos$mutableblockpos);
            iblockstate.getBlock().randomDisplayTick(this, blockpos$mutableblockpos, iblockstate, random);
            if (flag && iblockstate.getBlock() == Blocks.barrier) {
                this.spawnParticle(EnumParticleTypes.BARRIER, (float)k + 0.5f, (double)((float)l + 0.5f), (double)((float)i1 + 0.5f), 0.0, 0.0, 0.0, new int[0]);
            }
            ++j;
        }
    }

    public void removeAllEntities() {
        this.loadedEntityList.removeAll(this.unloadedEntityList);
        for (int i = 0; i < this.unloadedEntityList.size(); ++i) {
            Entity entity = (Entity)this.unloadedEntityList.get(i);
            int j = entity.chunkCoordX;
            int k = entity.chunkCoordZ;
            if (!entity.addedToChunk || !this.isChunkLoaded(j, k, true)) continue;
            this.getChunkFromChunkCoords(j, k).removeEntity(entity);
        }
        for (int l = 0; l < this.unloadedEntityList.size(); ++l) {
            this.onEntityRemoved((Entity)this.unloadedEntityList.get(l));
        }
        this.unloadedEntityList.clear();
        int i1 = 0;
        while (i1 < this.loadedEntityList.size()) {
            block9: {
                Entity entity1;
                block8: {
                    entity1 = (Entity)this.loadedEntityList.get(i1);
                    if (entity1.ridingEntity == null) break block8;
                    if (!entity1.ridingEntity.isDead && entity1.ridingEntity.riddenByEntity == entity1) break block9;
                    entity1.ridingEntity.riddenByEntity = null;
                    entity1.ridingEntity = null;
                }
                if (entity1.isDead) {
                    int j1 = entity1.chunkCoordX;
                    int k1 = entity1.chunkCoordZ;
                    if (entity1.addedToChunk && this.isChunkLoaded(j1, k1, true)) {
                        this.getChunkFromChunkCoords(j1, k1).removeEntity(entity1);
                    }
                    this.loadedEntityList.remove(i1--);
                    this.onEntityRemoved(entity1);
                }
            }
            ++i1;
        }
    }

    @Override
    public CrashReportCategory addWorldInfoToCrashReport(CrashReport report) {
        CrashReportCategory crashreportcategory = super.addWorldInfoToCrashReport(report);
        crashreportcategory.addCrashSectionCallable("Forced entities", new Callable<String>(){

            @Override
            public String call() {
                return WorldClient.this.entityList.size() + " total; " + WorldClient.this.entityList.toString();
            }
        });
        crashreportcategory.addCrashSectionCallable("Retry entities", new Callable<String>(){

            @Override
            public String call() {
                return WorldClient.this.entitySpawnQueue.size() + " total; " + WorldClient.this.entitySpawnQueue.toString();
            }
        });
        crashreportcategory.addCrashSectionCallable("Server brand", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return ((WorldClient)WorldClient.this).mc.thePlayer.getClientBrand();
            }
        });
        crashreportcategory.addCrashSectionCallable("Server type", new Callable<String>(){

            @Override
            public String call() throws Exception {
                if (WorldClient.this.mc.getIntegratedServer() != null) return "Integrated singleplayer server";
                return "Non-integrated multiplayer server";
            }
        });
        return crashreportcategory;
    }

    public void playSoundAtPos(BlockPos p_175731_1_, String p_175731_2_, float p_175731_3_, float p_175731_4_, boolean p_175731_5_) {
        this.playSound((double)p_175731_1_.getX() + 0.5, (double)p_175731_1_.getY() + 0.5, (double)p_175731_1_.getZ() + 0.5, p_175731_2_, p_175731_3_, p_175731_4_, p_175731_5_);
    }

    @Override
    public void playSound(double x, double y, double z, String soundName, float volume, float pitch, boolean distanceDelay) {
        double d0 = this.mc.getRenderViewEntity().getDistanceSq(x, y, z);
        PositionedSoundRecord positionedsoundrecord = new PositionedSoundRecord(new ResourceLocation(soundName), volume, pitch, (float)x, (float)y, (float)z);
        if (distanceDelay && d0 > 100.0) {
            double d1 = Math.sqrt(d0) / 40.0;
            this.mc.getSoundHandler().playDelayedSound(positionedsoundrecord, (int)(d1 * 20.0));
            return;
        }
        this.mc.getSoundHandler().playSound(positionedsoundrecord);
    }

    @Override
    public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, NBTTagCompound compund) {
        this.mc.effectRenderer.addEffect(new EntityFirework.StarterFX(this, x, y, z, motionX, motionY, motionZ, this.mc.effectRenderer, compund));
    }

    public void setWorldScoreboard(Scoreboard p_96443_1_) {
        this.worldScoreboard = p_96443_1_;
    }

    @Override
    public void setWorldTime(long time) {
        if (time < 0L) {
            time = -time;
            this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
        } else {
            this.getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
        }
        super.setWorldTime(time);
    }
}

