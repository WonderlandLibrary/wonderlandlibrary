/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
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
    private final List<EntityPlayerMP> players = Lists.newArrayList();
    private int playerViewRadius;
    private final WorldServer theWorldServer;
    private static final Logger pmLogger = LogManager.getLogger();
    private final List<PlayerInstance> playerInstanceList;
    private final List<PlayerInstance> playerInstancesToUpdate;
    private final LongHashMap<PlayerInstance> playerInstances = new LongHashMap();
    private final int[][] xzDirectionsConst;
    private long previousTotalWorldTime;

    public void addPlayer(EntityPlayerMP entityPlayerMP) {
        int n = (int)entityPlayerMP.posX >> 4;
        int n2 = (int)entityPlayerMP.posZ >> 4;
        entityPlayerMP.managedPosX = entityPlayerMP.posX;
        entityPlayerMP.managedPosZ = entityPlayerMP.posZ;
        int n3 = n - this.playerViewRadius;
        while (n3 <= n + this.playerViewRadius) {
            int n4 = n2 - this.playerViewRadius;
            while (n4 <= n2 + this.playerViewRadius) {
                this.getPlayerInstance(n3, n4, true).addPlayer(entityPlayerMP);
                ++n4;
            }
            ++n3;
        }
        this.players.add(entityPlayerMP);
        this.filterChunkLoadQueue(entityPlayerMP);
    }

    public boolean hasPlayerInstance(int n, int n2) {
        long l = (long)n + Integer.MAX_VALUE | (long)n2 + Integer.MAX_VALUE << 32;
        return this.playerInstances.getValueByKey(l) != null;
    }

    public void updateMountedMovingPlayer(EntityPlayerMP entityPlayerMP) {
        int n = (int)entityPlayerMP.posX >> 4;
        int n2 = (int)entityPlayerMP.posZ >> 4;
        double d = entityPlayerMP.managedPosX - entityPlayerMP.posX;
        double d2 = entityPlayerMP.managedPosZ - entityPlayerMP.posZ;
        double d3 = d * d + d2 * d2;
        if (d3 >= 64.0) {
            int n3 = (int)entityPlayerMP.managedPosX >> 4;
            int n4 = (int)entityPlayerMP.managedPosZ >> 4;
            int n5 = this.playerViewRadius;
            int n6 = n - n3;
            int n7 = n2 - n4;
            if (n6 != 0 || n7 != 0) {
                int n8 = n - n5;
                while (n8 <= n + n5) {
                    int n9 = n2 - n5;
                    while (n9 <= n2 + n5) {
                        PlayerInstance playerInstance;
                        if (!this.overlaps(n8, n9, n3, n4, n5)) {
                            this.getPlayerInstance(n8, n9, true).addPlayer(entityPlayerMP);
                        }
                        if (!this.overlaps(n8 - n6, n9 - n7, n, n2, n5) && (playerInstance = this.getPlayerInstance(n8 - n6, n9 - n7, false)) != null) {
                            playerInstance.removePlayer(entityPlayerMP);
                        }
                        ++n9;
                    }
                    ++n8;
                }
                this.filterChunkLoadQueue(entityPlayerMP);
                entityPlayerMP.managedPosX = entityPlayerMP.posX;
                entityPlayerMP.managedPosZ = entityPlayerMP.posZ;
            }
        }
    }

    public void setPlayerViewRadius(int n) {
        if ((n = MathHelper.clamp_int(n, 3, 32)) != this.playerViewRadius) {
            int n2 = n - this.playerViewRadius;
            for (EntityPlayerMP entityPlayerMP : Lists.newArrayList(this.players)) {
                int n3;
                int n4;
                int n5 = (int)entityPlayerMP.posX >> 4;
                int n6 = (int)entityPlayerMP.posZ >> 4;
                if (n2 > 0) {
                    n4 = n5 - n;
                    while (n4 <= n5 + n) {
                        n3 = n6 - n;
                        while (n3 <= n6 + n) {
                            PlayerInstance playerInstance = this.getPlayerInstance(n4, n3, true);
                            if (!playerInstance.playersWatchingChunk.contains(entityPlayerMP)) {
                                playerInstance.addPlayer(entityPlayerMP);
                            }
                            ++n3;
                        }
                        ++n4;
                    }
                    continue;
                }
                n4 = n5 - this.playerViewRadius;
                while (n4 <= n5 + this.playerViewRadius) {
                    n3 = n6 - this.playerViewRadius;
                    while (n3 <= n6 + this.playerViewRadius) {
                        if (!this.overlaps(n4, n3, n5, n6, n)) {
                            this.getPlayerInstance(n4, n3, true).removePlayer(entityPlayerMP);
                        }
                        ++n3;
                    }
                    ++n4;
                }
            }
            this.playerViewRadius = n;
        }
    }

    public void updatePlayerInstances() {
        WorldProvider worldProvider;
        int n;
        long l = this.theWorldServer.getTotalWorldTime();
        if (l - this.previousTotalWorldTime > 8000L) {
            this.previousTotalWorldTime = l;
            n = 0;
            while (n < this.playerInstanceList.size()) {
                PlayerInstance playerInstance = this.playerInstanceList.get(n);
                playerInstance.onUpdate();
                playerInstance.processChunk();
                ++n;
            }
        } else {
            n = 0;
            while (n < this.playerInstancesToUpdate.size()) {
                PlayerInstance playerInstance = this.playerInstancesToUpdate.get(n);
                playerInstance.onUpdate();
                ++n;
            }
        }
        this.playerInstancesToUpdate.clear();
        if (this.players.isEmpty() && !(worldProvider = this.theWorldServer.provider).canRespawnHere()) {
            this.theWorldServer.theChunkProviderServer.unloadAllChunks();
        }
    }

    private PlayerInstance getPlayerInstance(int n, int n2, boolean bl) {
        long l = (long)n + Integer.MAX_VALUE | (long)n2 + Integer.MAX_VALUE << 32;
        PlayerInstance playerInstance = this.playerInstances.getValueByKey(l);
        if (playerInstance == null && bl) {
            playerInstance = new PlayerInstance(n, n2);
            this.playerInstances.add(l, playerInstance);
            this.playerInstanceList.add(playerInstance);
        }
        return playerInstance;
    }

    public boolean isPlayerWatchingChunk(EntityPlayerMP entityPlayerMP, int n, int n2) {
        PlayerInstance playerInstance = this.getPlayerInstance(n, n2, false);
        return playerInstance != null && playerInstance.playersWatchingChunk.contains(entityPlayerMP) && !entityPlayerMP.loadedChunks.contains(playerInstance.chunkCoords);
    }

    private boolean overlaps(int n, int n2, int n3, int n4, int n5) {
        int n6 = n - n3;
        int n7 = n2 - n4;
        return n6 >= -n5 && n6 <= n5 ? n7 >= -n5 && n7 <= n5 : false;
    }

    public void removePlayer(EntityPlayerMP entityPlayerMP) {
        int n = (int)entityPlayerMP.managedPosX >> 4;
        int n2 = (int)entityPlayerMP.managedPosZ >> 4;
        int n3 = n - this.playerViewRadius;
        while (n3 <= n + this.playerViewRadius) {
            int n4 = n2 - this.playerViewRadius;
            while (n4 <= n2 + this.playerViewRadius) {
                PlayerInstance playerInstance = this.getPlayerInstance(n3, n4, false);
                if (playerInstance != null) {
                    playerInstance.removePlayer(entityPlayerMP);
                }
                ++n4;
            }
            ++n3;
        }
        this.players.remove(entityPlayerMP);
    }

    public void markBlockForUpdate(BlockPos blockPos) {
        int n;
        int n2 = blockPos.getX() >> 4;
        PlayerInstance playerInstance = this.getPlayerInstance(n2, n = blockPos.getZ() >> 4, false);
        if (playerInstance != null) {
            playerInstance.flagChunkForUpdate(blockPos.getX() & 0xF, blockPos.getY(), blockPos.getZ() & 0xF);
        }
    }

    public void filterChunkLoadQueue(EntityPlayerMP entityPlayerMP) {
        ArrayList arrayList = Lists.newArrayList(entityPlayerMP.loadedChunks);
        int n = 0;
        int n2 = this.playerViewRadius;
        int n3 = (int)entityPlayerMP.posX >> 4;
        int n4 = (int)entityPlayerMP.posZ >> 4;
        int n5 = 0;
        int n6 = 0;
        ChunkCoordIntPair chunkCoordIntPair = this.getPlayerInstance(n3, n4, true).chunkCoords;
        entityPlayerMP.loadedChunks.clear();
        if (arrayList.contains(chunkCoordIntPair)) {
            entityPlayerMP.loadedChunks.add(chunkCoordIntPair);
        }
        int n7 = 1;
        while (n7 <= n2 * 2) {
            int n8 = 0;
            while (n8 < 2) {
                int[] nArray = this.xzDirectionsConst[n++ % 4];
                int n9 = 0;
                while (n9 < n7) {
                    chunkCoordIntPair = this.getPlayerInstance(n3 + (n5 += nArray[0]), n4 + (n6 += nArray[1]), true).chunkCoords;
                    if (arrayList.contains(chunkCoordIntPair)) {
                        entityPlayerMP.loadedChunks.add(chunkCoordIntPair);
                    }
                    ++n9;
                }
                ++n8;
            }
            ++n7;
        }
        n %= 4;
        n7 = 0;
        while (n7 < n2 * 2) {
            chunkCoordIntPair = this.getPlayerInstance(n3 + (n5 += this.xzDirectionsConst[n][0]), n4 + (n6 += this.xzDirectionsConst[n][1]), true).chunkCoords;
            if (arrayList.contains(chunkCoordIntPair)) {
                entityPlayerMP.loadedChunks.add(chunkCoordIntPair);
            }
            ++n7;
        }
    }

    public WorldServer getWorldServer() {
        return this.theWorldServer;
    }

    public PlayerManager(WorldServer worldServer) {
        this.playerInstancesToUpdate = Lists.newArrayList();
        this.playerInstanceList = Lists.newArrayList();
        int[][] nArrayArray = new int[4][];
        int[] nArray = new int[2];
        nArray[0] = 1;
        nArrayArray[0] = nArray;
        int[] nArray2 = new int[2];
        nArray2[1] = 1;
        nArrayArray[1] = nArray2;
        int[] nArray3 = new int[2];
        nArray3[0] = -1;
        nArrayArray[2] = nArray3;
        int[] nArray4 = new int[2];
        nArray4[1] = -1;
        nArrayArray[3] = nArray4;
        this.xzDirectionsConst = nArrayArray;
        this.theWorldServer = worldServer;
        this.setPlayerViewRadius(worldServer.getMinecraftServer().getConfigurationManager().getViewDistance());
    }

    public static int getFurthestViewableBlock(int n) {
        return n * 16 - 16;
    }

    class PlayerInstance {
        private short[] locationOfBlockChange;
        private int numBlocksToUpdate;
        private final List<EntityPlayerMP> playersWatchingChunk = Lists.newArrayList();
        private int flagsYAreasToUpdate;
        private final ChunkCoordIntPair chunkCoords;
        private long previousWorldTime;

        public void removePlayer(EntityPlayerMP entityPlayerMP) {
            if (this.playersWatchingChunk.contains(entityPlayerMP)) {
                Chunk chunk = PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
                if (chunk.isPopulated()) {
                    entityPlayerMP.playerNetServerHandler.sendPacket(new S21PacketChunkData(chunk, true, 0));
                }
                this.playersWatchingChunk.remove(entityPlayerMP);
                entityPlayerMP.loadedChunks.remove(this.chunkCoords);
                if (this.playersWatchingChunk.isEmpty()) {
                    long l = (long)this.chunkCoords.chunkXPos + Integer.MAX_VALUE | (long)this.chunkCoords.chunkZPos + Integer.MAX_VALUE << 32;
                    this.increaseInhabitedTime(chunk);
                    PlayerManager.this.playerInstances.remove(l);
                    PlayerManager.this.playerInstanceList.remove(this);
                    if (this.numBlocksToUpdate > 0) {
                        PlayerManager.this.playerInstancesToUpdate.remove(this);
                    }
                    PlayerManager.this.getWorldServer().theChunkProviderServer.dropChunk(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
                }
            }
        }

        public void addPlayer(EntityPlayerMP entityPlayerMP) {
            if (this.playersWatchingChunk.contains(entityPlayerMP)) {
                pmLogger.debug("Failed to add player. {} already is in chunk {}, {}", new Object[]{entityPlayerMP, this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos});
            } else {
                if (this.playersWatchingChunk.isEmpty()) {
                    this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
                }
                this.playersWatchingChunk.add(entityPlayerMP);
                entityPlayerMP.loadedChunks.add(this.chunkCoords);
            }
        }

        public void sendToAllPlayersWatchingChunk(Packet packet) {
            int n = 0;
            while (n < this.playersWatchingChunk.size()) {
                EntityPlayerMP entityPlayerMP = this.playersWatchingChunk.get(n);
                if (!entityPlayerMP.loadedChunks.contains(this.chunkCoords)) {
                    entityPlayerMP.playerNetServerHandler.sendPacket(packet);
                }
                ++n;
            }
        }

        private void increaseInhabitedTime(Chunk chunk) {
            chunk.setInhabitedTime(chunk.getInhabitedTime() + PlayerManager.this.theWorldServer.getTotalWorldTime() - this.previousWorldTime);
            this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
        }

        public PlayerInstance(int n, int n2) {
            this.locationOfBlockChange = new short[64];
            this.chunkCoords = new ChunkCoordIntPair(n, n2);
            PlayerManager.this.getWorldServer().theChunkProviderServer.loadChunk(n, n2);
        }

        public void processChunk() {
            this.increaseInhabitedTime(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos));
        }

        private void sendTileToAllPlayersWatchingChunk(TileEntity tileEntity) {
            Packet packet;
            if (tileEntity != null && (packet = tileEntity.getDescriptionPacket()) != null) {
                this.sendToAllPlayersWatchingChunk(packet);
            }
        }

        public void onUpdate() {
            if (this.numBlocksToUpdate != 0) {
                if (this.numBlocksToUpdate == 1) {
                    int n = (this.locationOfBlockChange[0] >> 12 & 0xF) + this.chunkCoords.chunkXPos * 16;
                    int n2 = this.locationOfBlockChange[0] & 0xFF;
                    int n3 = (this.locationOfBlockChange[0] >> 8 & 0xF) + this.chunkCoords.chunkZPos * 16;
                    BlockPos blockPos = new BlockPos(n, n2, n3);
                    this.sendToAllPlayersWatchingChunk(new S23PacketBlockChange(PlayerManager.this.theWorldServer, blockPos));
                    if (PlayerManager.this.theWorldServer.getBlockState(blockPos).getBlock().hasTileEntity()) {
                        this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockPos));
                    }
                } else if (this.numBlocksToUpdate == 64) {
                    int n = this.chunkCoords.chunkXPos * 16;
                    int n4 = this.chunkCoords.chunkZPos * 16;
                    this.sendToAllPlayersWatchingChunk(new S21PacketChunkData(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos), false, this.flagsYAreasToUpdate));
                    int n5 = 0;
                    while (n5 < 16) {
                        if ((this.flagsYAreasToUpdate & 1 << n5) != 0) {
                            int n6 = n5 << 4;
                            List<TileEntity> list = PlayerManager.this.theWorldServer.getTileEntitiesIn(n, n6, n4, n + 16, n6 + 16, n4 + 16);
                            int n7 = 0;
                            while (n7 < list.size()) {
                                this.sendTileToAllPlayersWatchingChunk(list.get(n7));
                                ++n7;
                            }
                        }
                        ++n5;
                    }
                } else {
                    this.sendToAllPlayersWatchingChunk(new S22PacketMultiBlockChange(this.numBlocksToUpdate, this.locationOfBlockChange, PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos)));
                    int n = 0;
                    while (n < this.numBlocksToUpdate) {
                        int n8 = (this.locationOfBlockChange[n] >> 12 & 0xF) + this.chunkCoords.chunkXPos * 16;
                        int n9 = this.locationOfBlockChange[n] & 0xFF;
                        int n10 = (this.locationOfBlockChange[n] >> 8 & 0xF) + this.chunkCoords.chunkZPos * 16;
                        BlockPos blockPos = new BlockPos(n8, n9, n10);
                        if (PlayerManager.this.theWorldServer.getBlockState(blockPos).getBlock().hasTileEntity()) {
                            this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockPos));
                        }
                        ++n;
                    }
                }
                this.numBlocksToUpdate = 0;
                this.flagsYAreasToUpdate = 0;
            }
        }

        public void flagChunkForUpdate(int n, int n2, int n3) {
            if (this.numBlocksToUpdate == 0) {
                PlayerManager.this.playerInstancesToUpdate.add(this);
            }
            this.flagsYAreasToUpdate |= 1 << (n2 >> 4);
            if (this.numBlocksToUpdate < 64) {
                short s = (short)(n << 12 | n3 << 8 | n2);
                int n4 = 0;
                while (n4 < this.numBlocksToUpdate) {
                    if (this.locationOfBlockChange[n4] == s) {
                        return;
                    }
                    ++n4;
                }
                this.locationOfBlockChange[this.numBlocksToUpdate++] = s;
            }
        }
    }
}

