/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap
 *  it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
 */
package net.minecraft.server.management;

import com.google.common.base.Predicate;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class PlayerChunkMap {
    private static final Predicate<EntityPlayerMP> NOT_SPECTATOR = new Predicate<EntityPlayerMP>(){

        @Override
        public boolean apply(@Nullable EntityPlayerMP p_apply_1_) {
            return p_apply_1_ != null && !p_apply_1_.isSpectator();
        }
    };
    private static final Predicate<EntityPlayerMP> CAN_GENERATE_CHUNKS = new Predicate<EntityPlayerMP>(){

        @Override
        public boolean apply(@Nullable EntityPlayerMP p_apply_1_) {
            return p_apply_1_ != null && (!p_apply_1_.isSpectator() || p_apply_1_.getServerWorld().getGameRules().getBoolean("spectatorsGenerateChunks"));
        }
    };
    private final WorldServer theWorldServer;
    private final List<EntityPlayerMP> players = Lists.newArrayList();
    private final Long2ObjectMap<PlayerChunkMapEntry> playerInstances = new Long2ObjectOpenHashMap(4096);
    private final Set<PlayerChunkMapEntry> playerInstancesToUpdate = Sets.newHashSet();
    private final List<PlayerChunkMapEntry> pendingSendToPlayers = Lists.newLinkedList();
    private final List<PlayerChunkMapEntry> playersNeedingChunks = Lists.newLinkedList();
    private final List<PlayerChunkMapEntry> playerInstanceList = Lists.newArrayList();
    private int playerViewRadius;
    private long previousTotalWorldTime;
    private boolean sortMissingChunks = true;
    private boolean sortSendToPlayers = true;

    public PlayerChunkMap(WorldServer serverWorld) {
        this.theWorldServer = serverWorld;
        this.setPlayerViewRadius(serverWorld.getInstanceServer().getPlayerList().getViewDistance());
    }

    public WorldServer getWorldServer() {
        return this.theWorldServer;
    }

    public Iterator<Chunk> getChunkIterator() {
        final Iterator<PlayerChunkMapEntry> iterator = this.playerInstanceList.iterator();
        return new AbstractIterator<Chunk>(){

            @Override
            protected Chunk computeNext() {
                while (iterator.hasNext()) {
                    PlayerChunkMapEntry playerchunkmapentry = (PlayerChunkMapEntry)iterator.next();
                    Chunk chunk = playerchunkmapentry.getChunk();
                    if (chunk == null) continue;
                    if (!chunk.isLightPopulated() && chunk.isTerrainPopulated()) {
                        return chunk;
                    }
                    if (!chunk.isChunkTicked()) {
                        return chunk;
                    }
                    if (!playerchunkmapentry.hasPlayerMatchingInRange(128.0, NOT_SPECTATOR)) continue;
                    return chunk;
                }
                return (Chunk)this.endOfData();
            }
        };
    }

    public void tick() {
        WorldProvider worldprovider;
        long i = this.theWorldServer.getTotalWorldTime();
        if (i - this.previousTotalWorldTime > 8000L) {
            this.previousTotalWorldTime = i;
            for (int j = 0; j < this.playerInstanceList.size(); ++j) {
                PlayerChunkMapEntry playerchunkmapentry = this.playerInstanceList.get(j);
                playerchunkmapentry.update();
                playerchunkmapentry.updateChunkInhabitedTime();
            }
        }
        if (!this.playerInstancesToUpdate.isEmpty()) {
            for (PlayerChunkMapEntry playerchunkmapentry2 : this.playerInstancesToUpdate) {
                playerchunkmapentry2.update();
            }
            this.playerInstancesToUpdate.clear();
        }
        if (this.sortMissingChunks && i % 4L == 0L) {
            this.sortMissingChunks = false;
            Collections.sort(this.playersNeedingChunks, new Comparator<PlayerChunkMapEntry>(){

                @Override
                public int compare(PlayerChunkMapEntry p_compare_1_, PlayerChunkMapEntry p_compare_2_) {
                    return ComparisonChain.start().compare(p_compare_1_.getClosestPlayerDistance(), p_compare_2_.getClosestPlayerDistance()).result();
                }
            });
        }
        if (this.sortSendToPlayers && i % 4L == 2L) {
            this.sortSendToPlayers = false;
            Collections.sort(this.pendingSendToPlayers, new Comparator<PlayerChunkMapEntry>(){

                @Override
                public int compare(PlayerChunkMapEntry p_compare_1_, PlayerChunkMapEntry p_compare_2_) {
                    return ComparisonChain.start().compare(p_compare_1_.getClosestPlayerDistance(), p_compare_2_.getClosestPlayerDistance()).result();
                }
            });
        }
        if (!this.playersNeedingChunks.isEmpty()) {
            long l = System.nanoTime() + 50000000L;
            int k = 49;
            Iterator<PlayerChunkMapEntry> iterator = this.playersNeedingChunks.iterator();
            while (iterator.hasNext()) {
                boolean flag;
                PlayerChunkMapEntry playerchunkmapentry1 = iterator.next();
                if (playerchunkmapentry1.getChunk() != null || !playerchunkmapentry1.providePlayerChunk(flag = playerchunkmapentry1.hasPlayerMatching(CAN_GENERATE_CHUNKS))) continue;
                iterator.remove();
                if (playerchunkmapentry1.sendToPlayers()) {
                    this.pendingSendToPlayers.remove(playerchunkmapentry1);
                }
                if (--k >= 0 && System.nanoTime() <= l) continue;
                break;
            }
        }
        if (!this.pendingSendToPlayers.isEmpty()) {
            int i1 = 81;
            Iterator<PlayerChunkMapEntry> iterator1 = this.pendingSendToPlayers.iterator();
            while (iterator1.hasNext()) {
                PlayerChunkMapEntry playerchunkmapentry3 = iterator1.next();
                if (!playerchunkmapentry3.sendToPlayers()) continue;
                iterator1.remove();
                if (--i1 >= 0) continue;
                break;
            }
        }
        if (this.players.isEmpty() && !(worldprovider = this.theWorldServer.provider).canRespawnHere()) {
            this.theWorldServer.getChunkProvider().unloadAllChunks();
        }
    }

    public boolean contains(int chunkX, int chunkZ) {
        long i = PlayerChunkMap.getIndex(chunkX, chunkZ);
        return this.playerInstances.get(i) != null;
    }

    @Nullable
    public PlayerChunkMapEntry getEntry(int x, int z) {
        return (PlayerChunkMapEntry)this.playerInstances.get(PlayerChunkMap.getIndex(x, z));
    }

    private PlayerChunkMapEntry getOrCreateEntry(int chunkX, int chunkZ) {
        long i = PlayerChunkMap.getIndex(chunkX, chunkZ);
        PlayerChunkMapEntry playerchunkmapentry = (PlayerChunkMapEntry)this.playerInstances.get(i);
        if (playerchunkmapentry == null) {
            playerchunkmapentry = new PlayerChunkMapEntry(this, chunkX, chunkZ);
            this.playerInstances.put(i, (Object)playerchunkmapentry);
            this.playerInstanceList.add(playerchunkmapentry);
            if (playerchunkmapentry.getChunk() == null) {
                this.playersNeedingChunks.add(playerchunkmapentry);
            }
            if (!playerchunkmapentry.sendToPlayers()) {
                this.pendingSendToPlayers.add(playerchunkmapentry);
            }
        }
        return playerchunkmapentry;
    }

    public void markBlockForUpdate(BlockPos pos) {
        int j;
        int i = pos.getX() >> 4;
        PlayerChunkMapEntry playerchunkmapentry = this.getEntry(i, j = pos.getZ() >> 4);
        if (playerchunkmapentry != null) {
            playerchunkmapentry.blockChanged(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
        }
    }

    public void addPlayer(EntityPlayerMP player) {
        int i = (int)player.posX >> 4;
        int j = (int)player.posZ >> 4;
        player.managedPosX = player.posX;
        player.managedPosZ = player.posZ;
        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k) {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                this.getOrCreateEntry(k, l).addPlayer(player);
            }
        }
        this.players.add(player);
        this.markSortPending();
    }

    public void removePlayer(EntityPlayerMP player) {
        int i = (int)player.managedPosX >> 4;
        int j = (int)player.managedPosZ >> 4;
        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k) {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                PlayerChunkMapEntry playerchunkmapentry = this.getEntry(k, l);
                if (playerchunkmapentry == null) continue;
                playerchunkmapentry.removePlayer(player);
            }
        }
        this.players.remove(player);
        this.markSortPending();
    }

    private boolean overlaps(int x1, int z1, int x2, int z2, int radius) {
        int i = x1 - x2;
        int j = z1 - z2;
        if (i >= -radius && i <= radius) {
            return j >= -radius && j <= radius;
        }
        return false;
    }

    public void updateMovingPlayer(EntityPlayerMP player) {
        int i = (int)player.posX >> 4;
        int j = (int)player.posZ >> 4;
        double d0 = player.managedPosX - player.posX;
        double d1 = player.managedPosZ - player.posZ;
        double d2 = d0 * d0 + d1 * d1;
        if (d2 >= 64.0) {
            int k = (int)player.managedPosX >> 4;
            int l = (int)player.managedPosZ >> 4;
            int i1 = this.playerViewRadius;
            int j1 = i - k;
            int k1 = j - l;
            if (j1 != 0 || k1 != 0) {
                for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                    for (int i2 = j - i1; i2 <= j + i1; ++i2) {
                        PlayerChunkMapEntry playerchunkmapentry;
                        if (!this.overlaps(l1, i2, k, l, i1)) {
                            this.getOrCreateEntry(l1, i2).addPlayer(player);
                        }
                        if (this.overlaps(l1 - j1, i2 - k1, i, j, i1) || (playerchunkmapentry = this.getEntry(l1 - j1, i2 - k1)) == null) continue;
                        playerchunkmapentry.removePlayer(player);
                    }
                }
                player.managedPosX = player.posX;
                player.managedPosZ = player.posZ;
                this.markSortPending();
            }
        }
    }

    public boolean isPlayerWatchingChunk(EntityPlayerMP player, int chunkX, int chunkZ) {
        PlayerChunkMapEntry playerchunkmapentry = this.getEntry(chunkX, chunkZ);
        return playerchunkmapentry != null && playerchunkmapentry.containsPlayer(player) && playerchunkmapentry.isSentToPlayers();
    }

    public void setPlayerViewRadius(int radius) {
        if ((radius = MathHelper.clamp(radius, 3, 32)) != this.playerViewRadius) {
            int i = radius - this.playerViewRadius;
            for (EntityPlayerMP entityplayermp : Lists.newArrayList(this.players)) {
                int j = (int)entityplayermp.posX >> 4;
                int k = (int)entityplayermp.posZ >> 4;
                if (i > 0) {
                    for (int j1 = j - radius; j1 <= j + radius; ++j1) {
                        for (int k1 = k - radius; k1 <= k + radius; ++k1) {
                            PlayerChunkMapEntry playerchunkmapentry = this.getOrCreateEntry(j1, k1);
                            if (playerchunkmapentry.containsPlayer(entityplayermp)) continue;
                            playerchunkmapentry.addPlayer(entityplayermp);
                        }
                    }
                    continue;
                }
                for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                    for (int i1 = k - this.playerViewRadius; i1 <= k + this.playerViewRadius; ++i1) {
                        if (this.overlaps(l, i1, j, k, radius)) continue;
                        this.getOrCreateEntry(l, i1).removePlayer(entityplayermp);
                    }
                }
            }
            this.playerViewRadius = radius;
            this.markSortPending();
        }
    }

    private void markSortPending() {
        this.sortMissingChunks = true;
        this.sortSendToPlayers = true;
    }

    public static int getFurthestViewableBlock(int distance) {
        return distance * 16 - 16;
    }

    private static long getIndex(int p_187307_0_, int p_187307_1_) {
        return (long)p_187307_0_ + Integer.MAX_VALUE | (long)p_187307_1_ + Integer.MAX_VALUE << 32;
    }

    public void addEntry(PlayerChunkMapEntry entry) {
        this.playerInstancesToUpdate.add(entry);
    }

    public void removeEntry(PlayerChunkMapEntry entry) {
        ChunkPos chunkpos = entry.getPos();
        long i = PlayerChunkMap.getIndex(chunkpos.x, chunkpos.z);
        entry.updateChunkInhabitedTime();
        this.playerInstances.remove(i);
        this.playerInstanceList.remove(entry);
        this.playerInstancesToUpdate.remove(entry);
        this.pendingSendToPlayers.remove(entry);
        this.playersNeedingChunks.remove(entry);
        Chunk chunk = entry.getChunk();
        if (chunk != null) {
            this.getWorldServer().getChunkProvider().unload(chunk);
        }
    }
}

