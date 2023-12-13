/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.management;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerManager {
    private static final Logger pmLogger = LogManager.getLogger();
    private final WorldServer theWorldServer;
    private final List<EntityPlayerMP> players = Lists.newArrayList();
    private final LongHashMap playerInstances = new LongHashMap();
    private final List<PlayerInstance> playerInstancesToUpdate = Lists.newArrayList();
    private final List<PlayerInstance> playerInstanceList = Lists.newArrayList();
    private int playerViewRadius;
    private long previousTotalWorldTime;
    private final int[][] xzDirectionsConst = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    public PlayerManager(WorldServer serverWorld) {
        this.theWorldServer = serverWorld;
        this.setPlayerViewRadius(serverWorld.getMinecraftServer().getConfigurationManager().getViewDistance());
    }

    public WorldServer getWorldServer() {
        return this.theWorldServer;
    }

    public void updatePlayerInstances() {
        WorldProvider worldprovider;
        long i2 = this.theWorldServer.getTotalWorldTime();
        if (i2 - this.previousTotalWorldTime > 8000L) {
            this.previousTotalWorldTime = i2;
            for (int j2 = 0; j2 < this.playerInstanceList.size(); ++j2) {
                PlayerInstance playermanager$playerinstance = this.playerInstanceList.get(j2);
                playermanager$playerinstance.onUpdate();
                playermanager$playerinstance.processChunk();
            }
        } else {
            for (int k2 = 0; k2 < this.playerInstancesToUpdate.size(); ++k2) {
                PlayerInstance playermanager$playerinstance1 = this.playerInstancesToUpdate.get(k2);
                playermanager$playerinstance1.onUpdate();
            }
        }
        this.playerInstancesToUpdate.clear();
        if (this.players.isEmpty() && !(worldprovider = this.theWorldServer.provider).canRespawnHere()) {
            this.theWorldServer.theChunkProviderServer.unloadAllChunks();
        }
    }

    public boolean hasPlayerInstance(int chunkX, int chunkZ) {
        long i2 = (long)chunkX + Integer.MAX_VALUE | (long)chunkZ + Integer.MAX_VALUE << 32;
        return this.playerInstances.getValueByKey(i2) != null;
    }

    private PlayerInstance getPlayerInstance(int chunkX, int chunkZ, boolean createIfAbsent) {
        long i2 = (long)chunkX + Integer.MAX_VALUE | (long)chunkZ + Integer.MAX_VALUE << 32;
        PlayerInstance playermanager$playerinstance = (PlayerInstance)this.playerInstances.getValueByKey(i2);
        if (playermanager$playerinstance == null && createIfAbsent) {
            playermanager$playerinstance = new PlayerInstance(chunkX, chunkZ);
            this.playerInstances.add(i2, playermanager$playerinstance);
            this.playerInstanceList.add(playermanager$playerinstance);
        }
        return playermanager$playerinstance;
    }

    public void markBlockForUpdate(BlockPos pos) {
        int j2;
        int i2 = pos.getX() >> 4;
        PlayerInstance playermanager$playerinstance = this.getPlayerInstance(i2, j2 = pos.getZ() >> 4, false);
        if (playermanager$playerinstance != null) {
            playermanager$playerinstance.flagChunkForUpdate(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
        }
    }

    public void addPlayer(EntityPlayerMP player) {
        int i2 = (int)player.posX >> 4;
        int j2 = (int)player.posZ >> 4;
        player.managedPosX = player.posX;
        player.managedPosZ = player.posZ;
        for (int k2 = i2 - this.playerViewRadius; k2 <= i2 + this.playerViewRadius; ++k2) {
            for (int l2 = j2 - this.playerViewRadius; l2 <= j2 + this.playerViewRadius; ++l2) {
                this.getPlayerInstance(k2, l2, true).addPlayer(player);
            }
        }
        this.players.add(player);
        this.filterChunkLoadQueue(player);
    }

    public void filterChunkLoadQueue(EntityPlayerMP player) {
        ArrayList<ChunkCoordIntPair> list = Lists.newArrayList(player.loadedChunks);
        int i2 = 0;
        int j2 = this.playerViewRadius;
        int k2 = (int)player.posX >> 4;
        int l2 = (int)player.posZ >> 4;
        int i1 = 0;
        int j1 = 0;
        ChunkCoordIntPair chunkcoordintpair = this.getPlayerInstance(k2, l2, true).chunkCoords;
        player.loadedChunks.clear();
        if (list.contains(chunkcoordintpair)) {
            player.loadedChunks.add(chunkcoordintpair);
        }
        for (int k1 = 1; k1 <= j2 * 2; ++k1) {
            for (int l1 = 0; l1 < 2; ++l1) {
                int[] aint = this.xzDirectionsConst[i2++ % 4];
                for (int i22 = 0; i22 < k1; ++i22) {
                    chunkcoordintpair = this.getPlayerInstance(k2 + (i1 += aint[0]), l2 + (j1 += aint[1]), true).chunkCoords;
                    if (!list.contains(chunkcoordintpair)) continue;
                    player.loadedChunks.add(chunkcoordintpair);
                }
            }
        }
        i2 %= 4;
        for (int j22 = 0; j22 < j2 * 2; ++j22) {
            chunkcoordintpair = this.getPlayerInstance(k2 + (i1 += this.xzDirectionsConst[i2][0]), l2 + (j1 += this.xzDirectionsConst[i2][1]), true).chunkCoords;
            if (!list.contains(chunkcoordintpair)) continue;
            player.loadedChunks.add(chunkcoordintpair);
        }
    }

    public void removePlayer(EntityPlayerMP player) {
        int i2 = (int)player.managedPosX >> 4;
        int j2 = (int)player.managedPosZ >> 4;
        for (int k2 = i2 - this.playerViewRadius; k2 <= i2 + this.playerViewRadius; ++k2) {
            for (int l2 = j2 - this.playerViewRadius; l2 <= j2 + this.playerViewRadius; ++l2) {
                PlayerInstance playermanager$playerinstance = this.getPlayerInstance(k2, l2, false);
                if (playermanager$playerinstance == null) continue;
                playermanager$playerinstance.removePlayer(player);
            }
        }
        this.players.remove(player);
    }

    private boolean overlaps(int x1, int z1, int x2, int z2, int radius) {
        int i2 = x1 - x2;
        int j2 = z1 - z2;
        return i2 >= -radius && i2 <= radius ? j2 >= -radius && j2 <= radius : false;
    }

    public void updateMountedMovingPlayer(EntityPlayerMP player) {
        int i2 = (int)player.posX >> 4;
        int j2 = (int)player.posZ >> 4;
        double d0 = player.managedPosX - player.posX;
        double d1 = player.managedPosZ - player.posZ;
        double d2 = d0 * d0 + d1 * d1;
        if (d2 >= 64.0) {
            int k2 = (int)player.managedPosX >> 4;
            int l2 = (int)player.managedPosZ >> 4;
            int i1 = this.playerViewRadius;
            int j1 = i2 - k2;
            int k1 = j2 - l2;
            if (j1 != 0 || k1 != 0) {
                for (int l1 = i2 - i1; l1 <= i2 + i1; ++l1) {
                    for (int i22 = j2 - i1; i22 <= j2 + i1; ++i22) {
                        PlayerInstance playermanager$playerinstance;
                        if (!this.overlaps(l1, i22, k2, l2, i1)) {
                            this.getPlayerInstance(l1, i22, true).addPlayer(player);
                        }
                        if (this.overlaps(l1 - j1, i22 - k1, i2, j2, i1) || (playermanager$playerinstance = this.getPlayerInstance(l1 - j1, i22 - k1, false)) == null) continue;
                        playermanager$playerinstance.removePlayer(player);
                    }
                }
                this.filterChunkLoadQueue(player);
                player.managedPosX = player.posX;
                player.managedPosZ = player.posZ;
            }
        }
    }

    public boolean isPlayerWatchingChunk(EntityPlayerMP player, int chunkX, int chunkZ) {
        PlayerInstance playermanager$playerinstance = this.getPlayerInstance(chunkX, chunkZ, false);
        return playermanager$playerinstance != null && playermanager$playerinstance.playersWatchingChunk.contains(player) && !player.loadedChunks.contains(playermanager$playerinstance.chunkCoords);
    }

    public void setPlayerViewRadius(int radius) {
        if ((radius = MathHelper.clamp_int(radius, 3, 32)) != this.playerViewRadius) {
            int i2 = radius - this.playerViewRadius;
            for (EntityPlayerMP entityplayermp : Lists.newArrayList(this.players)) {
                int j2 = (int)entityplayermp.posX >> 4;
                int k2 = (int)entityplayermp.posZ >> 4;
                if (i2 > 0) {
                    for (int j1 = j2 - radius; j1 <= j2 + radius; ++j1) {
                        for (int k1 = k2 - radius; k1 <= k2 + radius; ++k1) {
                            PlayerInstance playermanager$playerinstance = this.getPlayerInstance(j1, k1, true);
                            if (playermanager$playerinstance.playersWatchingChunk.contains(entityplayermp)) continue;
                            playermanager$playerinstance.addPlayer(entityplayermp);
                        }
                    }
                    continue;
                }
                for (int l2 = j2 - this.playerViewRadius; l2 <= j2 + this.playerViewRadius; ++l2) {
                    for (int i1 = k2 - this.playerViewRadius; i1 <= k2 + this.playerViewRadius; ++i1) {
                        if (this.overlaps(l2, i1, j2, k2, radius)) continue;
                        this.getPlayerInstance(l2, i1, true).removePlayer(entityplayermp);
                    }
                }
            }
            this.playerViewRadius = radius;
        }
    }

    public static int getFurthestViewableBlock(int distance) {
        return distance * 16 - 16;
    }

    class PlayerInstance {
        private final List<EntityPlayerMP> playersWatchingChunk = Lists.newArrayList();
        private final ChunkCoordIntPair chunkCoords;
        private short[] locationOfBlockChange = new short[64];
        private int numBlocksToUpdate;
        private int flagsYAreasToUpdate;
        private long previousWorldTime;

        public PlayerInstance(int chunkX, int chunkZ) {
            this.chunkCoords = new ChunkCoordIntPair(chunkX, chunkZ);
            PlayerManager.this.getWorldServer().theChunkProviderServer.loadChunk(chunkX, chunkZ);
        }

        public void addPlayer(EntityPlayerMP player) {
            if (this.playersWatchingChunk.contains(player)) {
                pmLogger.debug("Failed to add player. {} already is in chunk {}, {}", player, this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
            } else {
                if (this.playersWatchingChunk.isEmpty()) {
                    this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
                }
                this.playersWatchingChunk.add(player);
                player.loadedChunks.add(this.chunkCoords);
            }
        }

        public void removePlayer(EntityPlayerMP player) {
            if (this.playersWatchingChunk.contains(player)) {
                Chunk chunk = PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
                if (chunk.isPopulated()) {
                    player.playerNetServerHandler.sendPacket(new S21PacketChunkData(chunk, true, 0));
                }
                this.playersWatchingChunk.remove(player);
                player.loadedChunks.remove(this.chunkCoords);
                if (this.playersWatchingChunk.isEmpty()) {
                    long i2 = (long)this.chunkCoords.chunkXPos + Integer.MAX_VALUE | (long)this.chunkCoords.chunkZPos + Integer.MAX_VALUE << 32;
                    this.increaseInhabitedTime(chunk);
                    PlayerManager.this.playerInstances.remove(i2);
                    PlayerManager.this.playerInstanceList.remove(this);
                    if (this.numBlocksToUpdate > 0) {
                        PlayerManager.this.playerInstancesToUpdate.remove(this);
                    }
                    PlayerManager.this.getWorldServer().theChunkProviderServer.dropChunk(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
                }
            }
        }

        public void processChunk() {
            this.increaseInhabitedTime(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos));
        }

        private void increaseInhabitedTime(Chunk theChunk) {
            theChunk.setInhabitedTime(theChunk.getInhabitedTime() + PlayerManager.this.theWorldServer.getTotalWorldTime() - this.previousWorldTime);
            this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
        }

        public void flagChunkForUpdate(int x2, int y2, int z2) {
            if (this.numBlocksToUpdate == 0) {
                PlayerManager.this.playerInstancesToUpdate.add(this);
            }
            this.flagsYAreasToUpdate |= 1 << (y2 >> 4);
            if (this.numBlocksToUpdate < 64) {
                short short1 = (short)(x2 << 12 | z2 << 8 | y2);
                for (int i2 = 0; i2 < this.numBlocksToUpdate; ++i2) {
                    if (this.locationOfBlockChange[i2] != short1) continue;
                    return;
                }
                this.locationOfBlockChange[this.numBlocksToUpdate++] = short1;
            }
        }

        public void sendToAllPlayersWatchingChunk(Packet thePacket) {
            for (int i2 = 0; i2 < this.playersWatchingChunk.size(); ++i2) {
                EntityPlayerMP entityplayermp = this.playersWatchingChunk.get(i2);
                if (entityplayermp.loadedChunks.contains(this.chunkCoords)) continue;
                entityplayermp.playerNetServerHandler.sendPacket(thePacket);
            }
        }

        public void onUpdate() {
            if (this.numBlocksToUpdate != 0) {
                if (this.numBlocksToUpdate == 1) {
                    int i2 = (this.locationOfBlockChange[0] >> 12 & 0xF) + this.chunkCoords.chunkXPos * 16;
                    int j2 = this.locationOfBlockChange[0] & 0xFF;
                    int k2 = (this.locationOfBlockChange[0] >> 8 & 0xF) + this.chunkCoords.chunkZPos * 16;
                    BlockPos blockpos = new BlockPos(i2, j2, k2);
                    this.sendToAllPlayersWatchingChunk(new S23PacketBlockChange(PlayerManager.this.theWorldServer, blockpos));
                    if (PlayerManager.this.theWorldServer.getBlockState(blockpos).getBlock().hasTileEntity()) {
                        this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos));
                    }
                } else if (this.numBlocksToUpdate == 64) {
                    int i1 = this.chunkCoords.chunkXPos * 16;
                    int k1 = this.chunkCoords.chunkZPos * 16;
                    this.sendToAllPlayersWatchingChunk(new S21PacketChunkData(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos), false, this.flagsYAreasToUpdate));
                    for (int i2 = 0; i2 < 16; ++i2) {
                        if ((this.flagsYAreasToUpdate & 1 << i2) == 0) continue;
                        int k2 = i2 << 4;
                        List<TileEntity> list = PlayerManager.this.theWorldServer.getTileEntitiesIn(i1, k2, k1, i1 + 16, k2 + 16, k1 + 16);
                        for (int l2 = 0; l2 < list.size(); ++l2) {
                            this.sendTileToAllPlayersWatchingChunk(list.get(l2));
                        }
                    }
                } else {
                    this.sendToAllPlayersWatchingChunk(new S22PacketMultiBlockChange(this.numBlocksToUpdate, this.locationOfBlockChange, PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos)));
                    for (int j1 = 0; j1 < this.numBlocksToUpdate; ++j1) {
                        int l1 = (this.locationOfBlockChange[j1] >> 12 & 0xF) + this.chunkCoords.chunkXPos * 16;
                        int j2 = this.locationOfBlockChange[j1] & 0xFF;
                        int l2 = (this.locationOfBlockChange[j1] >> 8 & 0xF) + this.chunkCoords.chunkZPos * 16;
                        BlockPos blockpos1 = new BlockPos(l1, j2, l2);
                        if (!PlayerManager.this.theWorldServer.getBlockState(blockpos1).getBlock().hasTileEntity()) continue;
                        this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos1));
                    }
                }
                this.numBlocksToUpdate = 0;
                this.flagsYAreasToUpdate = 0;
            }
        }

        private void sendTileToAllPlayersWatchingChunk(TileEntity theTileEntity) {
            Packet packet;
            if (theTileEntity != null && (packet = theTileEntity.getDescriptionPacket()) != null) {
                this.sendToAllPlayersWatchingChunk(packet);
            }
        }
    }
}

