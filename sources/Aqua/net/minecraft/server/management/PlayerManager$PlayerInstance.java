package net.minecraft.server.management;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/*
 * Exception performing whole class analysis ignored.
 */
class PlayerManager.PlayerInstance {
    private final List<EntityPlayerMP> playersWatchingChunk = Lists.newArrayList();
    private final ChunkCoordIntPair chunkCoords;
    private short[] locationOfBlockChange = new short[64];
    private int numBlocksToUpdate;
    private int flagsYAreasToUpdate;
    private long previousWorldTime;

    public PlayerManager.PlayerInstance(int chunkX, int chunkZ) {
        this.chunkCoords = new ChunkCoordIntPair(chunkX, chunkZ);
        PlayerManager.this.getWorldServer().theChunkProviderServer.loadChunk(chunkX, chunkZ);
    }

    public void addPlayer(EntityPlayerMP player) {
        if (this.playersWatchingChunk.contains((Object)player)) {
            PlayerManager.access$200().debug("Failed to add player. {} already is in chunk {}, {}", new Object[]{player, this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos});
        } else {
            if (this.playersWatchingChunk.isEmpty()) {
                this.previousWorldTime = PlayerManager.access$300((PlayerManager)PlayerManager.this).getTotalWorldTime();
            }
            this.playersWatchingChunk.add((Object)player);
            player.loadedChunks.add((Object)this.chunkCoords);
        }
    }

    public void removePlayer(EntityPlayerMP player) {
        if (this.playersWatchingChunk.contains((Object)player)) {
            Chunk chunk = PlayerManager.access$300((PlayerManager)PlayerManager.this).getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
            if (chunk.isPopulated()) {
                player.playerNetServerHandler.sendPacket((Packet)new S21PacketChunkData(chunk, true, 0));
            }
            this.playersWatchingChunk.remove((Object)player);
            player.loadedChunks.remove((Object)this.chunkCoords);
            if (this.playersWatchingChunk.isEmpty()) {
                long i = (long)this.chunkCoords.chunkXPos + Integer.MAX_VALUE | (long)this.chunkCoords.chunkZPos + Integer.MAX_VALUE << 32;
                this.increaseInhabitedTime(chunk);
                PlayerManager.access$400((PlayerManager)PlayerManager.this).remove(i);
                PlayerManager.access$500((PlayerManager)PlayerManager.this).remove((Object)this);
                if (this.numBlocksToUpdate > 0) {
                    PlayerManager.access$600((PlayerManager)PlayerManager.this).remove((Object)this);
                }
                PlayerManager.this.getWorldServer().theChunkProviderServer.dropChunk(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
            }
        }
    }

    public void processChunk() {
        this.increaseInhabitedTime(PlayerManager.access$300((PlayerManager)PlayerManager.this).getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos));
    }

    private void increaseInhabitedTime(Chunk theChunk) {
        theChunk.setInhabitedTime(theChunk.getInhabitedTime() + PlayerManager.access$300((PlayerManager)PlayerManager.this).getTotalWorldTime() - this.previousWorldTime);
        this.previousWorldTime = PlayerManager.access$300((PlayerManager)PlayerManager.this).getTotalWorldTime();
    }

    public void flagChunkForUpdate(int x, int y, int z) {
        if (this.numBlocksToUpdate == 0) {
            PlayerManager.access$600((PlayerManager)PlayerManager.this).add((Object)this);
        }
        this.flagsYAreasToUpdate |= 1 << (y >> 4);
        if (this.numBlocksToUpdate < 64) {
            short short1 = (short)(x << 12 | z << 8 | y);
            for (int i = 0; i < this.numBlocksToUpdate; ++i) {
                if (this.locationOfBlockChange[i] != short1) continue;
                return;
            }
            this.locationOfBlockChange[this.numBlocksToUpdate++] = short1;
        }
    }

    public void sendToAllPlayersWatchingChunk(Packet thePacket) {
        for (int i = 0; i < this.playersWatchingChunk.size(); ++i) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)this.playersWatchingChunk.get(i);
            if (entityplayermp.loadedChunks.contains((Object)this.chunkCoords)) continue;
            entityplayermp.playerNetServerHandler.sendPacket(thePacket);
        }
    }

    public void onUpdate() {
        if (this.numBlocksToUpdate != 0) {
            if (this.numBlocksToUpdate == 1) {
                int k1 = (this.locationOfBlockChange[0] >> 12 & 0xF) + this.chunkCoords.chunkXPos * 16;
                int i2 = this.locationOfBlockChange[0] & 0xFF;
                int k2 = (this.locationOfBlockChange[0] >> 8 & 0xF) + this.chunkCoords.chunkZPos * 16;
                BlockPos blockpos = new BlockPos(k1, i2, k2);
                this.sendToAllPlayersWatchingChunk((Packet)new S23PacketBlockChange((World)PlayerManager.access$300((PlayerManager)PlayerManager.this), blockpos));
                if (PlayerManager.access$300((PlayerManager)PlayerManager.this).getBlockState(blockpos).getBlock().hasTileEntity()) {
                    this.sendTileToAllPlayersWatchingChunk(PlayerManager.access$300((PlayerManager)PlayerManager.this).getTileEntity(blockpos));
                }
            } else if (this.numBlocksToUpdate != 64) {
                this.sendToAllPlayersWatchingChunk((Packet)new S22PacketMultiBlockChange(this.numBlocksToUpdate, this.locationOfBlockChange, PlayerManager.access$300((PlayerManager)PlayerManager.this).getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos)));
                for (int j1 = 0; j1 < this.numBlocksToUpdate; ++j1) {
                    int l1 = (this.locationOfBlockChange[j1] >> 12 & 0xF) + this.chunkCoords.chunkXPos * 16;
                    int j2 = this.locationOfBlockChange[j1] & 0xFF;
                    int l2 = (this.locationOfBlockChange[j1] >> 8 & 0xF) + this.chunkCoords.chunkZPos * 16;
                    BlockPos blockpos1 = new BlockPos(l1, j2, l2);
                    if (!PlayerManager.access$300((PlayerManager)PlayerManager.this).getBlockState(blockpos1).getBlock().hasTileEntity()) continue;
                    this.sendTileToAllPlayersWatchingChunk(PlayerManager.access$300((PlayerManager)PlayerManager.this).getTileEntity(blockpos1));
                }
            } else {
                int i = this.chunkCoords.chunkXPos * 16;
                int j = this.chunkCoords.chunkZPos * 16;
                this.sendToAllPlayersWatchingChunk((Packet)new S21PacketChunkData(PlayerManager.access$300((PlayerManager)PlayerManager.this).getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos), false, this.flagsYAreasToUpdate));
                for (int k = 0; k < 16; ++k) {
                    if ((this.flagsYAreasToUpdate & 1 << k) == 0) continue;
                    int l = k << 4;
                    List list = PlayerManager.access$300((PlayerManager)PlayerManager.this).getTileEntitiesIn(i, l, j, i + 16, l + 16, j + 16);
                    for (int i1 = 0; i1 < list.size(); ++i1) {
                        this.sendTileToAllPlayersWatchingChunk((TileEntity)list.get(i1));
                    }
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

    static /* synthetic */ ChunkCoordIntPair access$000(PlayerManager.PlayerInstance x0) {
        return x0.chunkCoords;
    }

    static /* synthetic */ List access$100(PlayerManager.PlayerInstance x0) {
        return x0.playersWatchingChunk;
    }
}
