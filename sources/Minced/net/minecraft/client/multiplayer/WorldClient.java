// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.multiplayer;

//import com.sun.istack.internal.Nullable;
import net.optifine.DynamicLights;
import net.minecraft.src.Config;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.network.Packet;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.world.GameType;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import java.util.Random;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.block.state.IBlockState;
import javax.annotation.Nullable;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.chunk.Chunk;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.optifine.CustomGuis;
import net.optifine.override.PlayerControllerOF;
import net.optifine.reflect.Reflector;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.util.math.BlockPos;
import com.google.common.collect.Sets;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import java.util.Set;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.world.World;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets;

public class WorldClient extends World
{
    private final NetHandlerPlayClient connection;
    private ChunkProviderClient clientChunkProvider;
    private final Set<Entity> entityList;
    private final Set<Entity> entitySpawnQueue;
    private final Minecraft mc;
    private final Set<ChunkPos> previousActiveChunkSet;
    private int ambienceTicks;
    protected Set<ChunkPos> visibleChunks;
    private int playerChunkX;
    private int playerChunkY;
    private boolean playerUpdate;
    
    public WorldClient(final NetHandlerPlayClient netHandler, final WorldSettings settings, final int dimension, final EnumDifficulty difficulty, final Profiler profilerIn) {
        super(new SaveHandlerMP(), new WorldInfo(settings, "MpServer"), makeWorldProvider(dimension), profilerIn, true);

        this.entityList = (Set<Entity>)Sets.newHashSet();
        this.entitySpawnQueue = (Set<Entity>)Sets.newHashSet();
        this.mc = Minecraft.getMinecraft();
        this.previousActiveChunkSet = (Set<ChunkPos>)Sets.newHashSet();
        this.playerChunkX = Integer.MIN_VALUE;
        this.playerChunkY = Integer.MIN_VALUE;
        this.playerUpdate = false;
        this.ambienceTicks = this.rand.nextInt(12000);
        this.visibleChunks = (Set<ChunkPos>)Sets.newHashSet();
        this.connection = netHandler;
        this.getWorldInfo().setDifficulty(difficulty);
        this.provider.setWorld(this);
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
    
    private static WorldProvider makeWorldProvider(final int p_makeWorldProvider_0_) {
        return (WorldProvider)(Reflector.DimensionManager_createProviderFor.exists() ? Reflector.call(Reflector.DimensionManager_createProviderFor, p_makeWorldProvider_0_) : DimensionType.getById(p_makeWorldProvider_0_).createDimension());
    }
    
    @Override
    public void tick() {
        super.tick();
        this.setTotalWorldTime(this.getTotalWorldTime() + 1L);
        if (this.getGameRules().getBoolean("doDaylightCycle")) {
            this.setWorldTime(this.getWorldTime() + 1L);
        }
        this.profiler.startSection("reEntryProcessing");
        for (int i = 0; i < 10 && !this.entitySpawnQueue.isEmpty(); ++i) {
            final Entity entity = this.entitySpawnQueue.iterator().next();
            this.entitySpawnQueue.remove(entity);
            if (!this.loadedEntityList.contains(entity)) {
                this.spawnEntity(entity);
            }
        }
        this.profiler.endStartSection("chunkCache");
        this.clientChunkProvider.tick();
        this.profiler.endStartSection("blocks");
        this.updateBlocks();
        this.profiler.endSection();
    }
    
    public void invalidateBlockReceiveRegion(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
    }
    
    @Override
    protected IChunkProvider createChunkProvider() {
        return this.clientChunkProvider = new ChunkProviderClient(this);
    }
    
    @Override
    protected boolean isChunkLoaded(final int x, final int z, final boolean allowEmpty) {
        return allowEmpty || !this.getChunkProvider().provideChunk(x, z).isEmpty();
    }
    
    protected void refreshVisibleChunks() {
        final Minecraft mc = this.mc;
        final int i = MathHelper.floor(Minecraft.player.posX / 16.0);
        final Minecraft mc2 = this.mc;
        final int j = MathHelper.floor(Minecraft.player.posZ / 16.0);
        if (i != this.playerChunkX || j != this.playerChunkY) {
            this.playerChunkX = i;
            this.playerChunkY = j;
            this.visibleChunks.clear();
            final int k = this.mc.gameSettings.renderDistanceChunks;
            this.profiler.startSection("buildList");
            final Minecraft mc3 = this.mc;
            final int l = MathHelper.floor(Minecraft.player.posX / 16.0);
            final Minecraft mc4 = this.mc;
            final int i2 = MathHelper.floor(Minecraft.player.posZ / 16.0);
            for (int j2 = -k; j2 <= k; ++j2) {
                for (int k2 = -k; k2 <= k; ++k2) {
                    this.visibleChunks.add(new ChunkPos(j2 + l, k2 + i2));
                }
            }
            this.profiler.endSection();
        }
    }
    
    @Override
    protected void updateBlocks() {
        this.refreshVisibleChunks();
        if (this.ambienceTicks > 0) {
            --this.ambienceTicks;
        }
        this.previousActiveChunkSet.retainAll(this.visibleChunks);
        if (this.previousActiveChunkSet.size() == this.visibleChunks.size()) {
            this.previousActiveChunkSet.clear();
        }
        int i = 0;
        for (final ChunkPos chunkpos : this.visibleChunks) {
            if (!this.previousActiveChunkSet.contains(chunkpos)) {
                final int j = chunkpos.x * 16;
                final int k = chunkpos.z * 16;
                this.profiler.startSection("getChunk");
                final Chunk chunk = this.getChunk(chunkpos.x, chunkpos.z);
                this.playMoodSoundAndCheckLight(j, k, chunk);
                this.profiler.endSection();
                this.previousActiveChunkSet.add(chunkpos);
                if (++i >= 10) {
                    return;
                }
                continue;
            }
        }
    }
    
    public void doPreChunk(final int chunkX, final int chunkZ, final boolean loadChunk) {
        if (loadChunk) {
            this.clientChunkProvider.loadChunk(chunkX, chunkZ);
        }
        else {
            this.clientChunkProvider.unloadChunk(chunkX, chunkZ);
            this.markBlockRangeForRenderUpdate(chunkX * 16, 0, chunkZ * 16, chunkX * 16 + 15, 256, chunkZ * 16 + 15);
        }
    }
    
    @Override
    public boolean spawnEntity(final Entity entityIn) {
        final boolean flag = super.spawnEntity(entityIn);
        this.entityList.add(entityIn);
        if (flag) {
            if (entityIn instanceof EntityMinecart) {
                this.mc.getSoundHandler().playSound(new MovingSoundMinecart((EntityMinecart)entityIn));
            }
        }
        else {
            this.entitySpawnQueue.add(entityIn);
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
        if (this.entitySpawnQueue.contains(entityIn)) {
            this.entitySpawnQueue.remove(entityIn);
        }
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
        if (!this.spawnEntity(entityToSpawn)) {
            this.entitySpawnQueue.add(entityToSpawn);
        }
        this.entitiesById.addKey(entityID, entityToSpawn);
    }
    
    @Nullable
    @Override
    public Entity getEntityByID(final int id) {
        final Minecraft mc = this.mc;
        Entity entity;
        if (id == Minecraft.player.getEntityId()) {
            final Minecraft mc2 = this.mc;
            entity = Minecraft.player;
        }
        else {
            entity = super.getEntityByID(id);
        }
        return entity;
    }
    
    public Entity removeEntityFromWorld(final int entityID) {
        final Entity entity = this.entitiesById.removeObject(entityID);
        if (entity != null) {
            this.entityList.remove(entity);
            this.removeEntity(entity);
        }
        return entity;
    }
    
    @Deprecated
    public boolean invalidateRegionAndSetBlock(final BlockPos pos, final IBlockState state) {
        final int i = pos.getX();
        final int j = pos.getY();
        final int k = pos.getZ();
        this.invalidateBlockReceiveRegion(i, j, k, i, j, k);
        return super.setBlockState(pos, state, 3);
    }
    
    @Override
    public void sendQuittingDisconnectingPacket() {
        this.connection.getNetworkManager().closeChannel(new TextComponentString("Quitting"));
    }
    
    @Override
    protected void updateWeather() {
    }
    
    @Override
    protected void playMoodSoundAndCheckLight(final int x, final int z, final Chunk chunkIn) {
        super.playMoodSoundAndCheckLight(x, z, chunkIn);
        if (this.ambienceTicks == 0) {
            final Minecraft mc = this.mc;
            final EntityPlayerSP entityplayersp = Minecraft.player;
            if (entityplayersp == null) {
                return;
            }
            if (Math.abs(entityplayersp.chunkCoordX - chunkIn.x) > 1 || Math.abs(entityplayersp.chunkCoordZ - chunkIn.z) > 1) {
                return;
            }
            this.updateLCG = this.updateLCG * 3 + 1013904223;
            final int i = this.updateLCG >> 2;
            int j = i & 0xF;
            int k = i >> 8 & 0xF;
            int l = i >> 16 & 0xFF;
            l /= 2;
            if (entityplayersp.posY > 160.0) {
                l += 128;
            }
            else if (entityplayersp.posY > 96.0) {
                l += 64;
            }
            final BlockPos blockpos = new BlockPos(j + x, l, k + z);
            final IBlockState iblockstate = chunkIn.getBlockState(blockpos);
            j += x;
            k += z;
            final Minecraft mc2 = this.mc;
            final double d0 = Minecraft.player.getDistanceSq(j + 0.5, l + 0.5, k + 0.5);
            if (d0 < 4.0) {
                return;
            }
            if (d0 > 255.0) {
                return;
            }
            if (iblockstate.getMaterial() == Material.AIR && this.getLight(blockpos) <= this.rand.nextInt(8) && this.getLightFor(EnumSkyBlock.SKY, blockpos) <= 0) {
                this.playSound(j + 0.5, l + 0.5, k + 0.5, SoundEvents.AMBIENT_CAVE, SoundCategory.AMBIENT, 0.7f, 0.8f + this.rand.nextFloat() * 0.2f, false);
                this.ambienceTicks = this.rand.nextInt(12000) + 6000;
            }
        }
    }
    
    public void doVoidFogParticles(final int posX, final int posY, final int posZ) {
        final int i = 32;
        final Random random = new Random();
        final Minecraft mc = this.mc;
        ItemStack itemstack = Minecraft.player.getHeldItemMainhand();
        if (itemstack == null || Block.getBlockFromItem(itemstack.getItem()) != Blocks.BARRIER) {
            final Minecraft mc2 = this.mc;
            itemstack = Minecraft.player.getHeldItemOffhand();
        }
        final boolean flag = this.mc.playerController.getCurrentGameType() == GameType.CREATIVE && !itemstack.isEmpty() && itemstack.getItem() == Item.getItemFromBlock(Blocks.BARRIER);
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int j = 0; j < 667; ++j) {
            this.showBarrierParticles(posX, posY, posZ, 16, random, flag, blockpos$mutableblockpos);
            this.showBarrierParticles(posX, posY, posZ, 32, random, flag, blockpos$mutableblockpos);
        }
    }
    
    public void showBarrierParticles(final int x, final int y, final int z, final int offset, final Random random, final boolean holdingBarrier, final BlockPos.MutableBlockPos pos) {
        final int i = x + this.rand.nextInt(offset) - this.rand.nextInt(offset);
        final int j = y + this.rand.nextInt(offset) - this.rand.nextInt(offset);
        final int k = z + this.rand.nextInt(offset) - this.rand.nextInt(offset);
        pos.setPos(i, j, k);
        final IBlockState iblockstate = this.getBlockState(pos);
        iblockstate.getBlock().randomDisplayTick(iblockstate, this, pos, random);
        if (holdingBarrier && iblockstate.getBlock() == Blocks.BARRIER) {
            this.spawnParticle(EnumParticleTypes.BARRIER, i + 0.5f, j + 0.5f, k + 0.5f, 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    public void removeAllEntities() {
        this.loadedEntityList.removeAll(this.unloadedEntityList);
        for (int i = 0; i < this.unloadedEntityList.size(); ++i) {
            final Entity entity = this.unloadedEntityList.get(i);
            final int j = entity.chunkCoordX;
            final int k = entity.chunkCoordZ;
            if (entity.addedToChunk && this.isChunkLoaded(j, k, true)) {
                this.getChunk(j, k).removeEntity(entity);
            }
        }
        for (int i2 = 0; i2 < this.unloadedEntityList.size(); ++i2) {
            this.onEntityRemoved(this.unloadedEntityList.get(i2));
        }
        this.unloadedEntityList.clear();
        for (int j2 = 0; j2 < this.loadedEntityList.size(); ++j2) {
            final Entity entity2 = this.loadedEntityList.get(j2);
            final Entity entity3 = entity2.getRidingEntity();
            if (entity3 != null) {
                if (!entity3.isDead && entity3.isPassenger(entity2)) {
                    continue;
                }
                entity2.dismountRidingEntity();
            }
            if (entity2.isDead) {
                final int k2 = entity2.chunkCoordX;
                final int l = entity2.chunkCoordZ;
                if (entity2.addedToChunk && this.isChunkLoaded(k2, l, true)) {
                    this.getChunk(k2, l).removeEntity(entity2);
                }
                this.loadedEntityList.remove(j2--);
                this.onEntityRemoved(entity2);
            }
        }
    }
    
    @Override
    public CrashReportCategory addWorldInfoToCrashReport(final CrashReport report) {
        final CrashReportCategory crashreportcategory = super.addWorldInfoToCrashReport(report);
        crashreportcategory.addDetail("Forced entities", new ICrashReportDetail<String>() {
            @Override
            public String call() {
                return WorldClient.this.entityList.size() + " total; " + WorldClient.this.entityList;
            }
        });
        crashreportcategory.addDetail("Retry entities", new ICrashReportDetail<String>() {
            @Override
            public String call() {
                return WorldClient.this.entitySpawnQueue.size() + " total; " + WorldClient.this.entitySpawnQueue;
            }
        });
        crashreportcategory.addDetail("Server brand", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                Minecraft mc1 = WorldClient.this.mc;
                return Minecraft.player.getServerBrand();
            }
        });
        crashreportcategory.addDetail("Server type", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return (WorldClient.this.mc.getIntegratedServer() == null) ? "Non-integrated multiplayer server" : "Integrated singleplayer server";
            }
        });
        return crashreportcategory;
    }
    
    @Override
    public void playSound(@Nullable final EntityPlayer player, final double x, final double y, final double z, final SoundEvent soundIn, final SoundCategory category, final float volume, final float pitch) {
        final Minecraft mc = this.mc;
        if (player == Minecraft.player) {
            this.playSound(x, y, z, soundIn, category, volume, pitch, false);
        }
    }
    
    public void playSound(final BlockPos pos, final SoundEvent soundIn, final SoundCategory category, final float volume, final float pitch, final boolean distanceDelay) {
        this.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, soundIn, category, volume, pitch, distanceDelay);
    }
    
    @Override
    public void playSound(final double x, final double y, final double z, final SoundEvent soundIn, final SoundCategory category, final float volume, final float pitch, final boolean distanceDelay) {
        final double d0 = this.mc.getRenderViewEntity().getDistanceSq(x, y, z);
        final PositionedSoundRecord positionedsoundrecord = new PositionedSoundRecord(soundIn, category, volume, pitch, (float)x, (float)y, (float)z);
        if (distanceDelay && d0 > 100.0) {
            final double d2 = Math.sqrt(d0) / 40.0;
            this.mc.getSoundHandler().playDelayedSound(positionedsoundrecord, (int)(d2 * 20.0));
        }
        else {
            this.mc.getSoundHandler().playSound(positionedsoundrecord);
        }
    }
    
    @Override
    public void makeFireworks(final double x, final double y, final double z, final double motionX, final double motionY, final double motionZ, @Nullable final NBTTagCompound compound) {
        this.mc.effectRenderer.addEffect(new ParticleFirework.Starter(this, x, y, z, motionX, motionY, motionZ, this.mc.effectRenderer, compound));
    }
    
    @Override
    public void sendPacketToServer(final Packet<?> packetIn) {
        this.connection.sendPacket(packetIn);
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
    public ChunkProviderClient getChunkProvider() {
        return (ChunkProviderClient)super.getChunkProvider();
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
