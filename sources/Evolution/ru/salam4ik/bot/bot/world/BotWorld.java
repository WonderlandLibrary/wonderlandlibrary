package ru.salam4ik.bot.bot.world;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;
import ru.salam4ik.bot.bot.entity.BotPlayer;
import ru.salam4ik.bot.bot.network.BotPlayClient;

public class BotWorld
        extends World {
    private ChunkProviderClient clientChunkProvider;
    private final BotPlayClient connection;
    private int playerChunkX;
    private BotPlayer bot;
    private final Set<Entity> entitySpawnQueue;
    private final Set<Entity> entityList;
    private final Set<ChunkPos> viewableChunks;
    private final Set<ChunkPos> previousActiveChunkSet;
    private int playerChunkY;

    public void removeAllEntities() {
        int n;
        Entity entity;
        int n2;
        this.loadedEntityList.removeAll(this.unloadedEntityList);
        for (n2 = 0; n2 < this.unloadedEntityList.size(); ++n2) {
            entity = (Entity)this.unloadedEntityList.get(n2);
            int n3 = entity.chunkCoordX;
            n = entity.chunkCoordZ;
            if (!entity.addedToChunk || !this.isChunkLoaded(n3, n, true)) continue;
            this.getChunk(n3, n).removeEntity(entity);
        }
        for (n2 = 0; n2 < this.unloadedEntityList.size(); ++n2) {
            this.onEntityRemoved((Entity)this.unloadedEntityList.get(n2));
        }
        this.unloadedEntityList.clear();
        for (n2 = 0; n2 < this.loadedEntityList.size(); ++n2) {
            entity = (Entity)this.loadedEntityList.get(n2);
            Entity entity2 = entity.getRidingEntity();
            if (entity2 != null) {
                if (!entity2.isDead && entity2.isPassenger(entity)) continue;
                entity.dismountRidingEntity();
            }
            if (!entity.isDead) continue;
            n = entity.chunkCoordX;
            int n4 = entity.chunkCoordZ;
            if (entity.addedToChunk && this.isChunkLoaded(n, n4, true)) {
                this.getChunk(n, n4).removeEntity(entity);
            }
            this.loadedEntityList.remove(n2--);
            this.onEntityRemoved(entity);
        }
    }

    public void addEntityToWorld(int entityID, Entity entityToSpawn)
    {
        Entity entity = this.getEntityByID(entityID);

        if (entity != null)
        {
            this.removeEntity(entity);
        }

        this.entityList.add(entityToSpawn);
        entityToSpawn.setEntityId(entityID);

        if (!this.spawnEntity(entityToSpawn))
        {
            this.entitySpawnQueue.add(entityToSpawn);
        }

        this.entitiesById.addKey(entityID, entityToSpawn);
    }

    @Override
    public ChunkProviderClient getChunkProvider() {
        return (ChunkProviderClient)super.getChunkProvider();
    }

    public void setWorldScoreboard(Scoreboard scoreboard) {
        this.worldScoreboard = scoreboard;
    }

    @Override
    public void sendPacketToServer(Packet<?> packet) {
        this.connection.sendPacket(packet);
    }

    @Override
    public void tick() {
        super.tick();
        this.setTotalWorldTime(this.getTotalWorldTime() + 1L);
        if (this.getGameRules().getBoolean("doDaylightCycle")) {
            this.setWorldTime(this.getWorldTime() + 1L);
        }
        for (int i = 0; i < 10 && !this.entitySpawnQueue.isEmpty(); ++i) {
            Entity entity = this.entitySpawnQueue.iterator().next();
            this.entitySpawnQueue.remove(entity);
            if (this.loadedEntityList.contains(entity)) continue;

        }
        try {

        }
        catch (Exception exception) {
            // empty catch block
        }
        this.updateBlocks();
    }



    @Deprecated
    public boolean invalidateRegionAndSetBlock(BlockPos blockPos, IBlockState iBlockState) {
        int n = blockPos.getX();
        int n2 = blockPos.getY();
        int n3 = blockPos.getZ();
        this.invalidateBlockReceiveRegion(n, n2, n3, n, n2, n3);
        return super.setBlockState(blockPos, iBlockState, 3);
    }

    public void removeEntityFromWorld(int n) {
        Entity entity = this.entitiesById.removeObject(n);
        if (entity != null) {
            this.entityList.remove(entity);
            this.removeEntity(entity);
        }
    }

    @Override
    protected void onEntityRemoved(Entity entity) {
        super.onEntityRemoved(entity);
        if (this.entityList.contains(entity)) {
            if (entity.isEntityAlive()) {
                this.entitySpawnQueue.add(entity);
            } else {
                this.entityList.remove(entity);
            }
        }
    }

    public void invalidateBlockReceiveRegion(int n, int n2, int n3, int n4, int n5, int n6) {
    }

    public BotWorld(BotPlayClient botPlayClient, WorldSettings worldSettings, int n, EnumDifficulty enumDifficulty) {
        super(new SaveHandlerMP(), new WorldInfo(worldSettings, "MpServer"), DimensionType.getById(n).createDimension(), Minecraft.getMinecraft().profiler, true);
        this.entityList = Sets.newHashSet();
        this.entitySpawnQueue = Sets.newHashSet();
        this.previousActiveChunkSet = Sets.newHashSet();
        this.playerChunkX = Integer.MIN_VALUE;
        this.playerChunkY = Integer.MIN_VALUE;
        this.viewableChunks = Sets.newHashSet();
        this.connection = botPlayClient;
        this.getWorldInfo().setDifficulty(enumDifficulty);
        this.provider.setWorld(this);
        this.setSpawnPoint(new BlockPos(8, 64, 8));
        this.chunkProvider = this.createChunkProvider();
        this.mapStorage = new SaveDataMemoryStorage();
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
    }

    public BotPlayer getBot() {
        return this.bot;
    }

    @Override
    public void sendQuittingDisconnectingPacket() {
        this.connection.getNetworkManager().closeChannel();
    }



    public void setBot(BotPlayer botPlayer) {
        this.bot = botPlayer;
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        this.clientChunkProvider = new ChunkProviderClient(this);
        return this.clientChunkProvider;
    }

    @Override
    public void playSound(double d, double d2, double d3, SoundEvent soundEvent, SoundCategory soundCategory, float f, float f2, boolean bl) {
    }

    @Override
    protected boolean isChunkLoaded(int n, int n2, boolean bl) {
        return bl || !this.getChunkProvider().provideChunk(n, n2).isEmpty();
    }

    @Override
    protected void onEntityAdded(Entity entity) {
        super.onEntityAdded(entity);
        this.entitySpawnQueue.remove(entity);
    }

    @Override
    protected void updateWeather() {
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

    public void playSound(BlockPos blockPos, SoundEvent soundEvent, SoundCategory soundCategory, float f, float f2, boolean bl) {
    }

    protected void buildChunkCoordList() {
        int n = MathHelper.floor(this.bot.posX / 16.0);
        int n2 = MathHelper.floor(this.bot.posZ / 16.0);
        if (n != this.playerChunkX || n2 != this.playerChunkY) {
            this.playerChunkX = n;
            this.playerChunkY = n2;
            this.viewableChunks.clear();
            int n3 = 2;
            int n4 = MathHelper.floor(this.bot.posX / 16.0);
            int n5 = MathHelper.floor(this.bot.posZ / 16.0);
            for (int i = -n3; i <= n3; ++i) {
                for (int j = -n3; j <= n3; ++j) {
                    this.viewableChunks.add(new ChunkPos(i + n4, j + n5));
                }
            }
        }
    }

    @Override
    public void removeEntity(Entity entity) {
        super.removeEntity(entity);
        this.entityList.remove(entity);
    }

    @Override
    protected void playMoodSoundAndCheckLight(int n, int n2, Chunk chunk) {
    }

    @Override
    protected void updateBlocks() {
        this.buildChunkCoordList();
        this.previousActiveChunkSet.retainAll(this.viewableChunks);
        if (this.previousActiveChunkSet.size() == this.viewableChunks.size()) {
            this.previousActiveChunkSet.clear();
        }
        int n = 0;
        for (ChunkPos chunkPos : this.viewableChunks) {
            if (this.previousActiveChunkSet.contains(chunkPos)) continue;
            int n2 = chunkPos.x * 16;
            int n3 = chunkPos.z * 16;
            Chunk chunk = this.getChunk(chunkPos.x, chunkPos.z);
            this.playMoodSoundAndCheckLight(n2, n3, chunk);
            this.previousActiveChunkSet.add(chunkPos);
            if (++n < 10) continue;
            return;
        }
    }

    @Override
    public void makeFireworks(double d, double d2, double d3, double d4, double d5, double d6, @Nullable NBTTagCompound nBTTagCompound) {
    }

    public void doPreChunk(int n, int n2, boolean bl) {
        if (bl) {
            this.clientChunkProvider.loadChunk(n, n2);
        } else {
            this.clientChunkProvider.unloadChunk(n, n2);
            this.markBlockRangeForRenderUpdate(n * 16, 0, n2 * 16, n * 16 + 15, 256, n2 * 16 + 15);
        }
    }

    @Override
    public void playSound(@Nullable EntityPlayer entityPlayer, double d, double d2, double d3, SoundEvent soundEvent, SoundCategory soundCategory, float f, float f2) {
    }

    @Override
    @Nullable
    public Entity getEntityByID(int n) {
        return n == this.bot.getEntityId() ? this.bot : super.getEntityByID(n);
    }
}
 