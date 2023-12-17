// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import org.apache.logging.log4j.LogManager;
import net.minecraft.entity.Entity;
import com.google.common.collect.Iterables;
import com.google.common.base.Predicate;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class PlayerChunkMapEntry
{
    private static final Logger LOGGER;
    private final PlayerChunkMap playerChunkMap;
    private final List<EntityPlayerMP> players;
    private final ChunkPos pos;
    private final short[] changedBlocks;
    @Nullable
    private Chunk chunk;
    private int changes;
    private int changedSectionFilter;
    private long lastUpdateInhabitedTime;
    private boolean sentToPlayers;
    
    public PlayerChunkMapEntry(final PlayerChunkMap mapIn, final int chunkX, final int chunkZ) {
        this.players = (List<EntityPlayerMP>)Lists.newArrayList();
        this.changedBlocks = new short[64];
        this.playerChunkMap = mapIn;
        this.pos = new ChunkPos(chunkX, chunkZ);
        this.chunk = mapIn.getWorldServer().getChunkProvider().loadChunk(chunkX, chunkZ);
    }
    
    public ChunkPos getPos() {
        return this.pos;
    }
    
    public void addPlayer(final EntityPlayerMP player) {
        if (this.players.contains(player)) {
            PlayerChunkMapEntry.LOGGER.debug("Failed to add player. {} already is in chunk {}, {}", (Object)player, (Object)this.pos.x, (Object)this.pos.z);
        }
        else {
            if (this.players.isEmpty()) {
                this.lastUpdateInhabitedTime = this.playerChunkMap.getWorldServer().getTotalWorldTime();
            }
            this.players.add(player);
            if (this.sentToPlayers) {
                this.sendToPlayer(player);
            }
        }
    }
    
    public void removePlayer(final EntityPlayerMP player) {
        if (this.players.contains(player)) {
            if (this.sentToPlayers) {
                player.connection.sendPacket(new SPacketUnloadChunk(this.pos.x, this.pos.z));
            }
            this.players.remove(player);
            if (this.players.isEmpty()) {
                this.playerChunkMap.removeEntry(this);
            }
        }
    }
    
    public boolean providePlayerChunk(final boolean canGenerate) {
        if (this.chunk != null) {
            return true;
        }
        if (canGenerate) {
            this.chunk = this.playerChunkMap.getWorldServer().getChunkProvider().provideChunk(this.pos.x, this.pos.z);
        }
        else {
            this.chunk = this.playerChunkMap.getWorldServer().getChunkProvider().loadChunk(this.pos.x, this.pos.z);
        }
        return this.chunk != null;
    }
    
    public boolean sendToPlayers() {
        if (this.sentToPlayers) {
            return true;
        }
        if (this.chunk == null) {
            return false;
        }
        if (!this.chunk.isPopulated()) {
            return false;
        }
        this.changes = 0;
        this.changedSectionFilter = 0;
        this.sentToPlayers = true;
        final Packet<?> packet = new SPacketChunkData(this.chunk, 65535);
        for (final EntityPlayerMP entityplayermp : this.players) {
            entityplayermp.connection.sendPacket(packet);
            this.playerChunkMap.getWorldServer().getEntityTracker().sendLeashedEntitiesInChunk(entityplayermp, this.chunk);
        }
        return true;
    }
    
    public void sendToPlayer(final EntityPlayerMP player) {
        if (this.sentToPlayers) {
            player.connection.sendPacket(new SPacketChunkData(this.chunk, 65535));
            this.playerChunkMap.getWorldServer().getEntityTracker().sendLeashedEntitiesInChunk(player, this.chunk);
        }
    }
    
    public void updateChunkInhabitedTime() {
        final long i = this.playerChunkMap.getWorldServer().getTotalWorldTime();
        if (this.chunk != null) {
            this.chunk.setInhabitedTime(this.chunk.getInhabitedTime() + i - this.lastUpdateInhabitedTime);
        }
        this.lastUpdateInhabitedTime = i;
    }
    
    public void blockChanged(final int x, final int y, final int z) {
        if (this.sentToPlayers) {
            if (this.changes == 0) {
                this.playerChunkMap.entryChanged(this);
            }
            this.changedSectionFilter |= 1 << (y >> 4);
            if (this.changes < 64) {
                final short short1 = (short)(x << 12 | z << 8 | y);
                for (int i = 0; i < this.changes; ++i) {
                    if (this.changedBlocks[i] == short1) {
                        return;
                    }
                }
                this.changedBlocks[this.changes++] = short1;
            }
        }
    }
    
    public void sendPacket(final Packet<?> packetIn) {
        if (this.sentToPlayers) {
            for (int i = 0; i < this.players.size(); ++i) {
                this.players.get(i).connection.sendPacket(packetIn);
            }
        }
    }
    
    public void update() {
        if (this.sentToPlayers && this.chunk != null && this.changes != 0) {
            if (this.changes == 1) {
                final int i = (this.changedBlocks[0] >> 12 & 0xF) + this.pos.x * 16;
                final int j = this.changedBlocks[0] & 0xFF;
                final int k = (this.changedBlocks[0] >> 8 & 0xF) + this.pos.z * 16;
                final BlockPos blockpos = new BlockPos(i, j, k);
                this.sendPacket(new SPacketBlockChange(this.playerChunkMap.getWorldServer(), blockpos));
                if (this.playerChunkMap.getWorldServer().getBlockState(blockpos).getBlock().hasTileEntity()) {
                    this.sendBlockEntity(this.playerChunkMap.getWorldServer().getTileEntity(blockpos));
                }
            }
            else if (this.changes == 64) {
                this.sendPacket(new SPacketChunkData(this.chunk, this.changedSectionFilter));
            }
            else {
                this.sendPacket(new SPacketMultiBlockChange(this.changes, this.changedBlocks, this.chunk));
                for (int l = 0; l < this.changes; ++l) {
                    final int i2 = (this.changedBlocks[l] >> 12 & 0xF) + this.pos.x * 16;
                    final int j2 = this.changedBlocks[l] & 0xFF;
                    final int k2 = (this.changedBlocks[l] >> 8 & 0xF) + this.pos.z * 16;
                    final BlockPos blockpos2 = new BlockPos(i2, j2, k2);
                    if (this.playerChunkMap.getWorldServer().getBlockState(blockpos2).getBlock().hasTileEntity()) {
                        this.sendBlockEntity(this.playerChunkMap.getWorldServer().getTileEntity(blockpos2));
                    }
                }
            }
            this.changes = 0;
            this.changedSectionFilter = 0;
        }
    }
    
    private void sendBlockEntity(@Nullable final TileEntity be) {
        if (be != null) {
            final SPacketUpdateTileEntity spacketupdatetileentity = be.getUpdatePacket();
            if (spacketupdatetileentity != null) {
                this.sendPacket(spacketupdatetileentity);
            }
        }
    }
    
    public boolean containsPlayer(final EntityPlayerMP player) {
        return this.players.contains(player);
    }
    
    public boolean hasPlayerMatching(final Predicate<EntityPlayerMP> predicate) {
        return Iterables.tryFind((Iterable)this.players, (Predicate)predicate).isPresent();
    }
    
    public boolean hasPlayerMatchingInRange(final double range, final Predicate<EntityPlayerMP> predicate) {
        for (int i = 0, j = this.players.size(); i < j; ++i) {
            final EntityPlayerMP entityplayermp = this.players.get(i);
            if (predicate.apply((Object)entityplayermp) && this.pos.getDistanceSq(entityplayermp) < range * range) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isSentToPlayers() {
        return this.sentToPlayers;
    }
    
    @Nullable
    public Chunk getChunk() {
        return this.chunk;
    }
    
    public double getClosestPlayerDistance() {
        double d0 = Double.MAX_VALUE;
        for (final EntityPlayerMP entityplayermp : this.players) {
            final double d2 = this.pos.getDistanceSq(entityplayermp);
            if (d2 < d0) {
                d0 = d2;
            }
        }
        return d0;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
