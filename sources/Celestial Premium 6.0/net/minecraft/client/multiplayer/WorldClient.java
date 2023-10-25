/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.multiplayer;

import baritone.events.events.baritoneOnly.EventBarAChunkPost;
import baritone.events.events.baritoneOnly.EventBarAChunkPre;
import com.google.common.collect.Sets;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;
import optifine.Config;
import optifine.CustomGuis;
import optifine.DynamicLights;
import optifine.PlayerControllerOF;
import optifine.Reflector;
import org.celestial.client.event.EventManager;

public class WorldClient
extends World {
    private final NetHandlerPlayClient connection;
    private ChunkProviderClient clientChunkProvider;
    private final Set<Entity> entityList = Sets.newHashSet();
    private final Set<Entity> entitySpawnQueue = Sets.newHashSet();
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Set<ChunkPos> previousActiveChunkSet = Sets.newHashSet();
    private int ambienceTicks;
    protected Set<ChunkPos> viewableChunks;
    private int playerChunkX = Integer.MIN_VALUE;
    private int playerChunkY = Integer.MIN_VALUE;
    private boolean playerUpdate = false;

    public WorldClient(NetHandlerPlayClient netHandler, WorldSettings settings, int dimension, EnumDifficulty difficulty, Profiler profilerIn) {
        super(new SaveHandlerMP(), new WorldInfo(settings, "MpServer"), WorldClient.makeWorldProvider(dimension), profilerIn, true);
        this.ambienceTicks = this.rand.nextInt(12000);
        this.viewableChunks = Sets.newHashSet();
        this.connection = netHandler;
        this.getWorldInfo().setDifficulty(difficulty);
        this.provider.registerWorld(this);
        this.setSpawnPoint(new BlockPos(8, 64, 8));
        this.chunkProvider = this.createChunkProvider();
        this.mapStorage = new SaveDataMemoryStorage();
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
        Reflector.call(this, Reflector.ForgeWorld_initCapabilities, new Object[0]);
        Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, this);
        if (this.mc.playerController != null && this.mc.playerController.getClass() == PlayerControllerMP.class) {
            this.mc.playerController = new PlayerControllerOF(this.mc, netHandler);
            CustomGuis.setPlayerControllerOF((PlayerControllerOF)this.mc.playerController);
        }
    }

    private static WorldProvider makeWorldProvider(int p_makeWorldProvider_0_) {
        return Reflector.DimensionManager_createProviderFor.exists() ? (WorldProvider)Reflector.call(Reflector.DimensionManager_createProviderFor, p_makeWorldProvider_0_) : DimensionType.getById(p_makeWorldProvider_0_).createDimension();
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

    public void invalidateBlockReceiveRegion(int x1, int y1, int z1, int x2, int y2, int z2) {
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        this.clientChunkProvider = new ChunkProviderClient(this);
        return this.clientChunkProvider;
    }

    @Override
    protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
        return allowEmpty || !this.getChunkProvider().provideChunk(x, z).isEmpty();
    }

    protected void buildChunkCoordList() {
        int i = MathHelper.floor(this.mc.player.posX / 16.0);
        int j = MathHelper.floor(this.mc.player.posZ / 16.0);
        if (i != this.playerChunkX || j != this.playerChunkY) {
            this.playerChunkX = i;
            this.playerChunkY = j;
            this.viewableChunks.clear();
            int k = this.mc.gameSettings.renderDistanceChunks;
            this.theProfiler.startSection("buildList");
            int l = MathHelper.floor(this.mc.player.posX / 16.0);
            int i1 = MathHelper.floor(this.mc.player.posZ / 16.0);
            for (int j1 = -k; j1 <= k; ++j1) {
                for (int k1 = -k; k1 <= k; ++k1) {
                    this.viewableChunks.add(new ChunkPos(j1 + l, k1 + i1));
                }
            }
            this.theProfiler.endSection();
        }
    }

    @Override
    protected void updateBlocks() {
        this.buildChunkCoordList();
        if (this.ambienceTicks > 0) {
            --this.ambienceTicks;
        }
        this.previousActiveChunkSet.retainAll(this.viewableChunks);
        if (this.previousActiveChunkSet.size() == this.viewableChunks.size()) {
            this.previousActiveChunkSet.clear();
        }
        int i = 0;
        for (ChunkPos chunkpos : this.viewableChunks) {
            if (this.previousActiveChunkSet.contains(chunkpos)) continue;
            int j = chunkpos.x * 16;
            int k = chunkpos.z * 16;
            this.theProfiler.startSection("getChunk");
            Chunk chunk = this.getChunk(chunkpos.x, chunkpos.z);
            this.playMoodSoundAndCheckLight(j, k, chunk);
            this.theProfiler.endSection();
            this.previousActiveChunkSet.add(chunkpos);
            if (++i < 10) continue;
            return;
        }
    }

    public void doPreChunk(int chunkX, int chunkZ, boolean loadChunk) {
        EventManager.call(new EventBarAChunkPre(chunkX, chunkZ, loadChunk));
        if (loadChunk) {
            this.clientChunkProvider.loadChunk(chunkX, chunkZ);
        } else {
            this.clientChunkProvider.unloadChunk(chunkX, chunkZ);
            this.markBlockRangeForRenderUpdate(chunkX * 16, 0, chunkZ * 16, chunkX * 16 + 15, 256, chunkZ * 16 + 15);
        }
        EventManager.call(new EventBarAChunkPost(chunkX, chunkZ, loadChunk));
    }

    @Override
    public boolean spawnEntityInWorld(Entity entityIn) {
        boolean flag = super.spawnEntityInWorld(entityIn);
        this.entityList.add(entityIn);
        if (flag) {
            if (entityIn instanceof EntityMinecart) {
                this.mc.getSoundHandler().playSound(new MovingSoundMinecart((EntityMinecart)entityIn));
            }
        } else {
            this.entitySpawnQueue.add(entityIn);
        }
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
        if (this.entitySpawnQueue.contains(entityIn)) {
            this.entitySpawnQueue.remove(entityIn);
        }
    }

    @Override
    protected void onEntityRemoved(Entity entityIn) {
        super.onEntityRemoved(entityIn);
        if (this.entityList.contains(entityIn)) {
            if (entityIn.isEntityAlive()) {
                this.entitySpawnQueue.add(entityIn);
            } else {
                this.entityList.remove(entityIn);
            }
        }
    }

    public void addEntityToWorld(int entityID, Entity entityToSpawn) {
        Entity entity = this.getEntityByID(entityID);
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
    @Nullable
    public Entity getEntityByID(int id) {
        return id == this.mc.player.getEntityId() ? this.mc.player : super.getEntityByID(id);
    }

    public Entity removeEntityFromWorld(int entityID) {
        Entity entity = (Entity)this.entitiesById.removeObject(entityID);
        if (entity != null) {
            this.entityList.remove(entity);
            this.removeEntity(entity);
        }
        return entity;
    }

    @Deprecated
    public boolean invalidateRegionAndSetBlock(BlockPos pos, IBlockState state) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        this.invalidateBlockReceiveRegion(i, j, k, i, j, k);
        return super.setBlockState(pos, state, 3);
    }

    @Override
    public void sendQuittingDisconnectingPacket() {
        this.connection.getNetworkManager().closeChannel(new TextComponentString("Quitting"));
    }

    public void sendAutoLeavePacket(Entity entity) {
        String dist = (int)this.mc.player.getDistanceToEntity(entity) + "m";
        this.connection.getNetworkManager().closeChannel(new TextComponentString("Auto Leave " + (Object)((Object)ChatFormatting.RED) + entity.getName() + " " + (Object)((Object)ChatFormatting.GRAY) + "(" + dist + ")"));
    }

    public void sendFullInventoryLeavePacket() {
        this.connection.getNetworkManager().closeChannel(new TextComponentString((Object)((Object)ChatFormatting.GREEN) + "Baritone " + (Object)((Object)ChatFormatting.WHITE) + "disconnected you from server, because of your inventory is full!"));
    }

    @Override
    protected void updateWeather() {
    }

    @Override
    protected void playMoodSoundAndCheckLight(int p_147467_1_, int p_147467_2_, Chunk chunkIn) {
        super.playMoodSoundAndCheckLight(p_147467_1_, p_147467_2_, chunkIn);
        if (this.ambienceTicks == 0) {
            EntityPlayerSP entityplayersp = this.mc.player;
            if (entityplayersp == null) {
                return;
            }
            if (Math.abs(entityplayersp.chunkCoordX - chunkIn.x) > 1 || Math.abs(entityplayersp.chunkCoordZ - chunkIn.z) > 1) {
                return;
            }
            this.updateLCG = this.updateLCG * 3 + 1013904223;
            int i = this.updateLCG >> 2;
            int j = i & 0xF;
            int k = i >> 8 & 0xF;
            int l = i >> 16 & 0xFF;
            l /= 2;
            if (entityplayersp.posY > 160.0) {
                l += 128;
            } else if (entityplayersp.posY > 96.0) {
                l += 64;
            }
            BlockPos blockpos = new BlockPos(j + p_147467_1_, l, k + p_147467_2_);
            IBlockState iblockstate = chunkIn.getBlockState(blockpos);
            double d0 = this.mc.player.getDistanceSq((double)(j += p_147467_1_) + 0.5, (double)l + 0.5, (double)(k += p_147467_2_) + 0.5);
            if (d0 < 4.0) {
                return;
            }
            if (d0 > 255.0) {
                return;
            }
            if (iblockstate.getMaterial() == Material.AIR && this.getLight(blockpos) <= this.rand.nextInt(8) && this.getLightFor(EnumSkyBlock.SKY, blockpos) <= 0) {
                this.playSound((double)j + 0.5, (double)l + 0.5, (double)k + 0.5, SoundEvents.AMBIENT_CAVE, SoundCategory.AMBIENT, 0.7f, 0.8f + this.rand.nextFloat() * 0.2f, false);
                this.ambienceTicks = this.rand.nextInt(12000) + 6000;
            }
        }
    }

    public void doVoidFogParticles(int posX, int posY, int posZ) {
        int i = 32;
        Random random = new Random();
        ItemStack itemstack = this.mc.player.getHeldItemMainhand();
        if (itemstack == null || Block.getBlockFromItem(itemstack.getItem()) != Blocks.BARRIER) {
            itemstack = this.mc.player.getHeldItemOffhand();
        }
        boolean flag = this.mc.playerController.getCurrentGameType() == GameType.CREATIVE && !itemstack.isEmpty() && itemstack.getItem() == Item.getItemFromBlock(Blocks.BARRIER);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int j = 0; j < 667; ++j) {
            this.showBarrierParticles(posX, posY, posZ, 16, random, flag, blockpos$mutableblockpos);
            this.showBarrierParticles(posX, posY, posZ, 32, random, flag, blockpos$mutableblockpos);
        }
    }

    public void showBarrierParticles(int p_184153_1_, int p_184153_2_, int p_184153_3_, int p_184153_4_, Random random, boolean p_184153_6_, BlockPos.MutableBlockPos pos) {
        int i = p_184153_1_ + this.rand.nextInt(p_184153_4_) - this.rand.nextInt(p_184153_4_);
        int j = p_184153_2_ + this.rand.nextInt(p_184153_4_) - this.rand.nextInt(p_184153_4_);
        int k = p_184153_3_ + this.rand.nextInt(p_184153_4_) - this.rand.nextInt(p_184153_4_);
        pos.setPos(i, j, k);
        IBlockState iblockstate = this.getBlockState(pos);
        iblockstate.getBlock().randomDisplayTick(iblockstate, this, pos, random);
        if (p_184153_6_ && iblockstate.getBlock() == Blocks.BARRIER) {
            this.spawnParticle(EnumParticleTypes.BARRIER, (float)i + 0.5f, (double)((float)j + 0.5f), (double)((float)k + 0.5f), 0.0, 0.0, 0.0, new int[0]);
        }
    }

    public void removeAllEntities() {
        this.loadedEntityList.removeAll(this.unloadedEntityList);
        for (int i = 0; i < this.unloadedEntityList.size(); ++i) {
            Entity entity = (Entity)this.unloadedEntityList.get(i);
            int j = entity.chunkCoordX;
            int k = entity.chunkCoordZ;
            if (!entity.addedToChunk || !this.isChunkLoaded(j, k, true)) continue;
            this.getChunk(j, k).removeEntity(entity);
        }
        for (int i1 = 0; i1 < this.unloadedEntityList.size(); ++i1) {
            this.onEntityRemoved((Entity)this.unloadedEntityList.get(i1));
        }
        this.unloadedEntityList.clear();
        for (int j1 = 0; j1 < this.loadedEntityList.size(); ++j1) {
            Entity entity1 = (Entity)this.loadedEntityList.get(j1);
            Entity entity2 = entity1.getRidingEntity();
            if (entity2 != null) {
                if (!entity2.isDead && entity2.isPassenger(entity1)) continue;
                entity1.dismountRidingEntity();
            }
            if (!entity1.isDead) continue;
            int k1 = entity1.chunkCoordX;
            int l = entity1.chunkCoordZ;
            if (entity1.addedToChunk && this.isChunkLoaded(k1, l, true)) {
                this.getChunk(k1, l).removeEntity(entity1);
            }
            this.loadedEntityList.remove(j1--);
            this.onEntityRemoved(entity1);
        }
    }

    @Override
    public CrashReportCategory addWorldInfoToCrashReport(CrashReport report) {
        CrashReportCategory crashreportcategory = super.addWorldInfoToCrashReport(report);
        crashreportcategory.setDetail("Forced entities", new ICrashReportDetail<String>(){

            @Override
            public String call() {
                return WorldClient.this.entityList.size() + " total; " + WorldClient.this.entityList;
            }
        });
        crashreportcategory.setDetail("Retry entities", new ICrashReportDetail<String>(){

            @Override
            public String call() {
                return WorldClient.this.entitySpawnQueue.size() + " total; " + WorldClient.this.entitySpawnQueue;
            }
        });
        crashreportcategory.setDetail("Server brand", new ICrashReportDetail<String>(){

            @Override
            public String call() throws Exception {
                return ((WorldClient)WorldClient.this).mc.player.getServerBrand();
            }
        });
        crashreportcategory.setDetail("Server type", new ICrashReportDetail<String>(){

            @Override
            public String call() throws Exception {
                return WorldClient.this.mc.getIntegratedServer() == null ? "Non-integrated multiplayer server" : "Integrated singleplayer server";
            }
        });
        return crashreportcategory;
    }

    @Override
    public void playSound(@Nullable EntityPlayer player, double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {
        if (player == this.mc.player) {
            this.playSound(x, y, z, soundIn, category, volume, pitch, false);
        }
    }

    public void playSound(BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay) {
        this.playSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, soundIn, category, volume, pitch, distanceDelay);
    }

    @Override
    public void playSound(double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay) {
        double d0 = this.mc.getRenderViewEntity().getDistanceSq(x, y, z);
        PositionedSoundRecord positionedsoundrecord = new PositionedSoundRecord(soundIn, category, volume, pitch, (float)x, (float)y, (float)z);
        if (distanceDelay && d0 > 100.0) {
            double d1 = Math.sqrt(d0) / 40.0;
            this.mc.getSoundHandler().playDelayedSound(positionedsoundrecord, (int)(d1 * 20.0));
        } else {
            this.mc.getSoundHandler().playSound(positionedsoundrecord);
        }
    }

    @Override
    public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, @Nullable NBTTagCompound compund) {
        this.mc.effectRenderer.addEffect(new ParticleFirework.Starter(this, x, y, z, motionX, motionY, motionZ, this.mc.effectRenderer, compund));
    }

    @Override
    public void sendPacketToServer(Packet<?> packetIn) {
        this.connection.sendPacket(packetIn);
    }

    public void setWorldScoreboard(Scoreboard scoreboardIn) {
        this.worldScoreboard = scoreboardIn;
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

    @Override
    public ChunkProviderClient getChunkProvider() {
        return (ChunkProviderClient)super.getChunkProvider();
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {
        int i = super.getCombinedLight(pos, lightValue);
        if (Config.isDynamicLights()) {
            i = DynamicLights.getCombinedLight(pos, i);
        }
        return i;
    }

    @Override
    public boolean setBlockState(BlockPos pos, IBlockState newState, int flags) {
        this.playerUpdate = this.isPlayerActing();
        boolean flag = super.setBlockState(pos, newState, flags);
        this.playerUpdate = false;
        return flag;
    }

    private boolean isPlayerActing() {
        if (this.mc.playerController instanceof PlayerControllerOF) {
            PlayerControllerOF playercontrollerof = (PlayerControllerOF)this.mc.playerController;
            return playercontrollerof.isActing();
        }
        return false;
    }

    public boolean isPlayerUpdate() {
        return this.playerUpdate;
    }
}

