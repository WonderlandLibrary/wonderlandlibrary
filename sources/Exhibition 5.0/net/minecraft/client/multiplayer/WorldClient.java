// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.multiplayer;

import net.minecraft.util.ReportedException;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFireworkStarterFX;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.optifine.BlockPosM;
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
import net.minecraft.world.ChunkCoordIntPair;
import java.util.Collection;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.entity.Entity;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.util.BlockPos;
import com.google.common.collect.Sets;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.Minecraft;
import java.util.Set;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.world.World;

public class WorldClient extends World
{
    private NetHandlerPlayClient sendQueue;
    private ChunkProviderClient clientChunkProvider;
    private final Set entityList;
    private final Set entitySpawnQueue;
    private final Minecraft mc;
    private final Set previousActiveChunkSet;
    private static final String __OBFID = "CL_00000882";
    
    public WorldClient(final NetHandlerPlayClient p_i45063_1_, final WorldSettings p_i45063_2_, final int p_i45063_3_, final EnumDifficulty p_i45063_4_, final Profiler p_i45063_5_) {
        super(new SaveHandlerMP(), new WorldInfo(p_i45063_2_, "MpServer"), WorldProvider.getProviderForDimension(p_i45063_3_), p_i45063_5_, true);
        this.entityList = Sets.newHashSet();
        this.entitySpawnQueue = Sets.newHashSet();
        this.mc = Minecraft.getMinecraft();
        this.previousActiveChunkSet = Sets.newHashSet();
        this.sendQueue = p_i45063_1_;
        this.getWorldInfo().setDifficulty(p_i45063_4_);
        this.setSpawnLocation(new BlockPos(8, 64, 8));
        this.provider.registerWorld(this);
        this.chunkProvider = this.createChunkProvider();
        this.mapStorage = new SaveDataMemoryStorage();
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
    }
    
    @Override
    public void tick() {
        super.tick();
        this.func_82738_a(this.getTotalWorldTime() + 1L);
        if (this.getGameRules().getGameRuleBooleanValue("doDaylightCycle")) {
            this.setWorldTime(this.getWorldTime() + 1L);
        }
        this.theProfiler.startSection("reEntryProcessing");
        for (int var1 = 0; var1 < 10 && !this.entitySpawnQueue.isEmpty(); ++var1) {
            final Entity var2 = this.entitySpawnQueue.iterator().next();
            this.entitySpawnQueue.remove(var2);
            if (!this.loadedEntityList.contains(var2)) {
                this.spawnEntityInWorld(var2);
            }
        }
        this.theProfiler.endStartSection("chunkCache");
        this.clientChunkProvider.unloadQueuedChunks();
        this.theProfiler.endStartSection("blocks");
        this.func_147456_g();
        this.theProfiler.endSection();
    }
    
    public void invalidateBlockReceiveRegion(final int p_73031_1_, final int p_73031_2_, final int p_73031_3_, final int p_73031_4_, final int p_73031_5_, final int p_73031_6_) {
    }
    
    @Override
    protected IChunkProvider createChunkProvider() {
        return this.clientChunkProvider = new ChunkProviderClient(this);
    }
    
    @Override
    protected void func_147456_g() {
        super.func_147456_g();
        this.previousActiveChunkSet.retainAll(this.activeChunkSet);
        if (this.previousActiveChunkSet.size() == this.activeChunkSet.size()) {
            this.previousActiveChunkSet.clear();
        }
        int var1 = 0;
        for (final ChunkCoordIntPair var3 : this.activeChunkSet) {
            if (!this.previousActiveChunkSet.contains(var3)) {
                final int var4 = var3.chunkXPos * 16;
                final int var5 = var3.chunkZPos * 16;
                this.theProfiler.startSection("getChunk");
                final Chunk var6 = this.getChunkFromChunkCoords(var3.chunkXPos, var3.chunkZPos);
                this.func_147467_a(var4, var5, var6);
                this.theProfiler.endSection();
                this.previousActiveChunkSet.add(var3);
                if (++var1 >= 10) {
                    return;
                }
                continue;
            }
        }
    }
    
    public void doPreChunk(final int p_73025_1_, final int p_73025_2_, final boolean p_73025_3_) {
        if (p_73025_3_) {
            this.clientChunkProvider.loadChunk(p_73025_1_, p_73025_2_);
        }
        else {
            this.clientChunkProvider.unloadChunk(p_73025_1_, p_73025_2_);
        }
        if (!p_73025_3_) {
            this.markBlockRangeForRenderUpdate(p_73025_1_ * 16, 0, p_73025_2_ * 16, p_73025_1_ * 16 + 15, 256, p_73025_2_ * 16 + 15);
        }
    }
    
    @Override
    public boolean spawnEntityInWorld(final Entity p_72838_1_) {
        final boolean var2 = super.spawnEntityInWorld(p_72838_1_);
        this.entityList.add(p_72838_1_);
        if (!var2) {
            this.entitySpawnQueue.add(p_72838_1_);
        }
        else if (p_72838_1_ instanceof EntityMinecart) {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecart((EntityMinecart)p_72838_1_));
        }
        return var2;
    }
    
    @Override
    public void removeEntity(final Entity p_72900_1_) {
        super.removeEntity(p_72900_1_);
        this.entityList.remove(p_72900_1_);
    }
    
    @Override
    protected void onEntityAdded(final Entity p_72923_1_) {
        super.onEntityAdded(p_72923_1_);
        if (this.entitySpawnQueue.contains(p_72923_1_)) {
            this.entitySpawnQueue.remove(p_72923_1_);
        }
    }
    
    @Override
    protected void onEntityRemoved(final Entity p_72847_1_) {
        super.onEntityRemoved(p_72847_1_);
        boolean var2 = false;
        if (this.entityList.contains(p_72847_1_)) {
            if (p_72847_1_.isEntityAlive()) {
                this.entitySpawnQueue.add(p_72847_1_);
                var2 = true;
            }
            else {
                this.entityList.remove(p_72847_1_);
            }
        }
    }
    
    public void addEntityToWorld(final int p_73027_1_, final Entity p_73027_2_) {
        final Entity var3 = this.getEntityByID(p_73027_1_);
        if (var3 != null) {
            this.removeEntity(var3);
        }
        this.entityList.add(p_73027_2_);
        p_73027_2_.setEntityId(p_73027_1_);
        if (!this.spawnEntityInWorld(p_73027_2_)) {
            this.entitySpawnQueue.add(p_73027_2_);
        }
        this.entitiesById.addKey(p_73027_1_, p_73027_2_);
    }
    
    @Override
    public Entity getEntityByID(final int p_73045_1_) {
        return (p_73045_1_ == this.mc.thePlayer.getEntityId()) ? this.mc.thePlayer : super.getEntityByID(p_73045_1_);
    }
    
    public Entity removeEntityFromWorld(final int p_73028_1_) {
        final Entity var2 = (Entity)this.entitiesById.removeObject(p_73028_1_);
        if (var2 != null) {
            this.entityList.remove(var2);
            this.removeEntity(var2);
        }
        return var2;
    }
    
    public boolean func_180503_b(final BlockPos p_180503_1_, final IBlockState p_180503_2_) {
        final int var3 = p_180503_1_.getX();
        final int var4 = p_180503_1_.getY();
        final int var5 = p_180503_1_.getZ();
        this.invalidateBlockReceiveRegion(var3, var4, var5, var3, var4, var5);
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
    
    public void doVoidFogParticles(final int p_73029_1_, final int p_73029_2_, final int p_73029_3_) {
        final byte var4 = 16;
        final Random var5 = new Random();
        final ItemStack var6 = this.mc.thePlayer.getHeldItem();
        final boolean var7 = this.mc.playerController.func_178889_l() == WorldSettings.GameType.CREATIVE && var6 != null && Block.getBlockFromItem(var6.getItem()) == Blocks.barrier;
        final BlockPosM blockPosM = new BlockPosM(0, 0, 0, 3);
        for (int var8 = 0; var8 < 1000; ++var8) {
            final int var9 = p_73029_1_ + this.rand.nextInt(var4) - this.rand.nextInt(var4);
            final int var10 = p_73029_2_ + this.rand.nextInt(var4) - this.rand.nextInt(var4);
            final int var11 = p_73029_3_ + this.rand.nextInt(var4) - this.rand.nextInt(var4);
            blockPosM.setXyz(var9, var10, var11);
            final IBlockState var12 = this.getBlockState(blockPosM);
            var12.getBlock().randomDisplayTick(this, blockPosM, var12, var5);
            if (var7 && var12.getBlock() == Blocks.barrier) {
                this.spawnParticle(EnumParticleTypes.BARRIER, var9 + 0.5f, var10 + 0.5f, var11 + 0.5f, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }
    
    public void removeAllEntities() {
        this.loadedEntityList.removeAll(this.unloadedEntityList);
        for (int var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
            final Entity var2 = this.unloadedEntityList.get(var1);
            final int var3 = var2.chunkCoordX;
            final int var4 = var2.chunkCoordZ;
            if (var2.addedToChunk && this.isChunkLoaded(var3, var4, true)) {
                this.getChunkFromChunkCoords(var3, var4).removeEntity(var2);
            }
        }
        for (int var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
            this.onEntityRemoved(this.unloadedEntityList.get(var1));
        }
        this.unloadedEntityList.clear();
        for (int var1 = 0; var1 < this.loadedEntityList.size(); ++var1) {
            final Entity var2 = this.loadedEntityList.get(var1);
            if (var2.ridingEntity != null) {
                if (!var2.ridingEntity.isDead && var2.ridingEntity.riddenByEntity == var2) {
                    continue;
                }
                var2.ridingEntity.riddenByEntity = null;
                var2.ridingEntity = null;
            }
            if (var2.isDead) {
                final int var3 = var2.chunkCoordX;
                final int var4 = var2.chunkCoordZ;
                if (var2.addedToChunk && this.isChunkLoaded(var3, var4, true)) {
                    this.getChunkFromChunkCoords(var3, var4).removeEntity(var2);
                }
                this.loadedEntityList.remove(var1--);
                this.onEntityRemoved(var2);
            }
        }
    }
    
    @Override
    public CrashReportCategory addWorldInfoToCrashReport(final CrashReport report) {
        final CrashReportCategory var2 = super.addWorldInfoToCrashReport(report);
        var2.addCrashSectionCallable("Forced entities", new Callable() {
            private static final String __OBFID = "CL_00000883";
            
            @Override
            public String call() {
                return WorldClient.this.entityList.size() + " total; " + WorldClient.this.entityList.toString();
            }
        });
        var2.addCrashSectionCallable("Retry entities", new Callable() {
            private static final String __OBFID = "CL_00000884";
            
            @Override
            public String call() {
                return WorldClient.this.entitySpawnQueue.size() + " total; " + WorldClient.this.entitySpawnQueue.toString();
            }
        });
        var2.addCrashSectionCallable("Server brand", new Callable() {
            private static final String __OBFID = "CL_00000885";
            
            @Override
            public String call() {
                return WorldClient.this.mc.thePlayer.getClientBrand();
            }
        });
        var2.addCrashSectionCallable("Server type", new Callable() {
            private static final String __OBFID = "CL_00000886";
            
            @Override
            public String call() {
                return (WorldClient.this.mc.getIntegratedServer() == null) ? "Non-integrated multiplayer server" : "Integrated singleplayer server";
            }
        });
        return var2;
    }
    
    public void func_175731_a(final BlockPos p_175731_1_, final String p_175731_2_, final float p_175731_3_, final float p_175731_4_, final boolean p_175731_5_) {
        this.playSound(p_175731_1_.getX() + 0.5, p_175731_1_.getY() + 0.5, p_175731_1_.getZ() + 0.5, p_175731_2_, p_175731_3_, p_175731_4_, p_175731_5_);
    }
    
    @Override
    public void playSound(final double x, final double y, final double z, final String soundName, final float volume, final float pitch, final boolean distanceDelay) {
        final double var11 = this.mc.func_175606_aa().getDistanceSq(x, y, z);
        final PositionedSoundRecord var12 = new PositionedSoundRecord(new ResourceLocation(soundName), volume, pitch, (float)x, (float)y, (float)z);
        if (distanceDelay && var11 > 100.0) {
            final double var13 = Math.sqrt(var11) / 40.0;
            this.mc.getSoundHandler().playDelayedSound(var12, (int)(var13 * 20.0));
        }
        else {
            this.mc.getSoundHandler().playSound(var12);
        }
    }
    
    @Override
    public void makeFireworks(final double x, final double y, final double z, final double motionX, final double motionY, final double motionZ, final NBTTagCompound compund) {
        this.mc.effectRenderer.addEffect(new EntityFireworkStarterFX(this, x, y, z, motionX, motionY, motionZ, this.mc.effectRenderer, compund));
    }
    
    public void setWorldScoreboard(final Scoreboard p_96443_1_) {
        this.worldScoreboard = p_96443_1_;
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
    
    public Block getBlock(final int p_147439_1_, final int p_147439_2_, final int p_147439_3_) {
        if (p_147439_1_ >= -30000000 && p_147439_3_ >= -30000000 && p_147439_1_ < 30000000 && p_147439_3_ < 30000000 && p_147439_2_ >= 0 && p_147439_2_ < 256) {
            Chunk var4 = null;
            try {
                var4 = this.getChunkFromChunkCoords(p_147439_1_ >> 4, p_147439_3_ >> 4);
                return var4.getBlock0(p_147439_1_ & 0xF, p_147439_2_, p_147439_3_ & 0xF);
            }
            catch (Throwable var6) {
                final CrashReport var5 = CrashReport.makeCrashReport(var6, "Exception getting block type in world");
                final CrashReportCategory var7 = var5.makeCategory("Requested block coordinates");
                var7.addCrashSection("Found chunk", var4 == null);
                throw new ReportedException(var5);
            }
        }
        return Blocks.air;
    }
}
