// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import java.util.HashSet;
import net.optifine.ChunkPosComparator;
import java.util.PriorityQueue;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.world.WorldProvider;
import java.util.Collections;
import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import net.minecraft.src.Config;
import com.google.common.collect.AbstractIterator;
import net.minecraft.world.chunk.Chunk;
import java.util.Iterator;
import java.util.HashMap;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import com.google.common.collect.Lists;
import net.minecraft.util.math.ChunkPos;
import java.util.Map;
import java.util.Set;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.util.List;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.base.Predicate;

public class PlayerChunkMap
{
    private static final Predicate<EntityPlayerMP> NOT_SPECTATOR;
    private static final Predicate<EntityPlayerMP> CAN_GENERATE_CHUNKS;
    private final WorldServer world;
    private final List<EntityPlayerMP> players;
    private final Long2ObjectMap<PlayerChunkMapEntry> entryMap;
    private final Set<PlayerChunkMapEntry> dirtyEntries;
    private final List<PlayerChunkMapEntry> pendingSendToPlayers;
    private final List<PlayerChunkMapEntry> entriesWithoutChunks;
    private final List<PlayerChunkMapEntry> entries;
    private int playerViewRadius;
    private long previousTotalWorldTime;
    private boolean sortMissingChunks;
    private boolean sortSendToPlayers;
    private final Map<EntityPlayerMP, Set<ChunkPos>> mapPlayerPendingEntries;
    
    public PlayerChunkMap(final WorldServer serverWorld) {
        this.players = (List<EntityPlayerMP>)Lists.newArrayList();
        this.entryMap = (Long2ObjectMap<PlayerChunkMapEntry>)new Long2ObjectOpenHashMap(4096);
        this.dirtyEntries = (Set<PlayerChunkMapEntry>)Sets.newHashSet();
        this.pendingSendToPlayers = (List<PlayerChunkMapEntry>)Lists.newLinkedList();
        this.entriesWithoutChunks = (List<PlayerChunkMapEntry>)Lists.newLinkedList();
        this.entries = (List<PlayerChunkMapEntry>)Lists.newArrayList();
        this.sortMissingChunks = true;
        this.sortSendToPlayers = true;
        this.mapPlayerPendingEntries = new HashMap<EntityPlayerMP, Set<ChunkPos>>();
        this.world = serverWorld;
        this.setPlayerViewRadius(serverWorld.getMinecraftServer().getPlayerList().getViewDistance());
    }
    
    public WorldServer getWorldServer() {
        return this.world;
    }
    
    public Iterator<Chunk> getChunkIterator() {
        final Iterator<PlayerChunkMapEntry> iterator = this.entries.iterator();
        return (Iterator<Chunk>)new AbstractIterator<Chunk>() {
            protected Chunk computeNext() {
                while (iterator.hasNext()) {
                    final PlayerChunkMapEntry playerchunkmapentry = iterator.next();
                    final Chunk chunk = playerchunkmapentry.getChunk();
                    if (chunk == null) {
                        continue;
                    }
                    if (!chunk.isLightPopulated() && chunk.isTerrainPopulated()) {
                        return chunk;
                    }
                    if (!chunk.wasTicked()) {
                        return chunk;
                    }
                    if (!playerchunkmapentry.hasPlayerMatchingInRange(128.0, PlayerChunkMap.NOT_SPECTATOR)) {
                        continue;
                    }
                    return chunk;
                }
                return (Chunk)this.endOfData();
            }
        };
    }
    
    public void tick() {
        final Set<Map.Entry<EntityPlayerMP, Set<ChunkPos>>> set = this.mapPlayerPendingEntries.entrySet();
        final Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            final Map.Entry<EntityPlayerMP, Set<ChunkPos>> entry = iterator.next();
            final Set<ChunkPos> set2 = entry.getValue();
            if (!set2.isEmpty()) {
                final EntityPlayerMP entityplayermp = entry.getKey();
                if (entityplayermp.getServerWorld() != this.world) {
                    iterator.remove();
                }
                else {
                    int i = this.playerViewRadius / 3 + 1;
                    if (!Config.isLazyChunkLoading()) {
                        i = this.playerViewRadius * 2 + 1;
                    }
                    for (final ChunkPos chunkpos : this.getNearest(set2, entityplayermp, i)) {
                        final PlayerChunkMapEntry playerchunkmapentry = this.getOrCreateEntry(chunkpos.x, chunkpos.z);
                        if (!playerchunkmapentry.containsPlayer(entityplayermp)) {
                            playerchunkmapentry.addPlayer(entityplayermp);
                        }
                        set2.remove(chunkpos);
                    }
                }
            }
        }
        final long j = this.world.getTotalWorldTime();
        if (j - this.previousTotalWorldTime > 8000L) {
            this.previousTotalWorldTime = j;
            for (int k = 0; k < this.entries.size(); ++k) {
                final PlayerChunkMapEntry playerchunkmapentry2 = this.entries.get(k);
                playerchunkmapentry2.update();
                playerchunkmapentry2.updateChunkInhabitedTime();
            }
        }
        if (!this.dirtyEntries.isEmpty()) {
            for (final PlayerChunkMapEntry playerchunkmapentry3 : this.dirtyEntries) {
                playerchunkmapentry3.update();
            }
            this.dirtyEntries.clear();
        }
        if (this.sortMissingChunks && j % 4L == 0L) {
            this.sortMissingChunks = false;
            Collections.sort(this.entriesWithoutChunks, new Comparator<PlayerChunkMapEntry>() {
                @Override
                public int compare(final PlayerChunkMapEntry p_compare_1_, final PlayerChunkMapEntry p_compare_2_) {
                    return ComparisonChain.start().compare(p_compare_1_.getClosestPlayerDistance(), p_compare_2_.getClosestPlayerDistance()).result();
                }
            });
        }
        if (this.sortSendToPlayers && j % 4L == 2L) {
            this.sortSendToPlayers = false;
            Collections.sort(this.pendingSendToPlayers, new Comparator<PlayerChunkMapEntry>() {
                @Override
                public int compare(final PlayerChunkMapEntry p_compare_1_, final PlayerChunkMapEntry p_compare_2_) {
                    return ComparisonChain.start().compare(p_compare_1_.getClosestPlayerDistance(), p_compare_2_.getClosestPlayerDistance()).result();
                }
            });
        }
        if (!this.entriesWithoutChunks.isEmpty()) {
            final long l = System.nanoTime() + 50000000L;
            int j2 = 49;
            final Iterator<PlayerChunkMapEntry> iterator2 = this.entriesWithoutChunks.iterator();
            while (iterator2.hasNext()) {
                final PlayerChunkMapEntry playerchunkmapentry4 = iterator2.next();
                if (playerchunkmapentry4.getChunk() == null) {
                    final boolean flag = playerchunkmapentry4.hasPlayerMatching(PlayerChunkMap.CAN_GENERATE_CHUNKS);
                    if (!playerchunkmapentry4.providePlayerChunk(flag)) {
                        continue;
                    }
                    iterator2.remove();
                    if (playerchunkmapentry4.sendToPlayers()) {
                        this.pendingSendToPlayers.remove(playerchunkmapentry4);
                    }
                    if (--j2 < 0) {
                        break;
                    }
                    if (System.nanoTime() > l) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (!this.pendingSendToPlayers.isEmpty()) {
            int i2 = 81;
            final Iterator<PlayerChunkMapEntry> iterator3 = this.pendingSendToPlayers.iterator();
            while (iterator3.hasNext()) {
                final PlayerChunkMapEntry playerchunkmapentry5 = iterator3.next();
                if (playerchunkmapentry5.sendToPlayers()) {
                    iterator3.remove();
                    if (--i2 < 0) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (this.players.isEmpty()) {
            final WorldProvider worldprovider = this.world.provider;
            if (!worldprovider.canRespawnHere()) {
                this.world.getChunkProvider().queueUnloadAll();
            }
        }
    }
    
    public boolean contains(final int chunkX, final int chunkZ) {
        final long i = getIndex(chunkX, chunkZ);
        return this.entryMap.get(i) != null;
    }
    
    @Nullable
    public PlayerChunkMapEntry getEntry(final int x, final int z) {
        return (PlayerChunkMapEntry)this.entryMap.get(getIndex(x, z));
    }
    
    private PlayerChunkMapEntry getOrCreateEntry(final int chunkX, final int chunkZ) {
        final long i = getIndex(chunkX, chunkZ);
        PlayerChunkMapEntry playerchunkmapentry = (PlayerChunkMapEntry)this.entryMap.get(i);
        if (playerchunkmapentry == null) {
            playerchunkmapentry = new PlayerChunkMapEntry(this, chunkX, chunkZ);
            this.entryMap.put(i, (Object)playerchunkmapentry);
            this.entries.add(playerchunkmapentry);
            if (playerchunkmapentry.getChunk() == null) {
                this.entriesWithoutChunks.add(playerchunkmapentry);
            }
            if (!playerchunkmapentry.sendToPlayers()) {
                this.pendingSendToPlayers.add(playerchunkmapentry);
            }
        }
        return playerchunkmapentry;
    }
    
    public void markBlockForUpdate(final BlockPos pos) {
        final int i = pos.getX() >> 4;
        final int j = pos.getZ() >> 4;
        final PlayerChunkMapEntry playerchunkmapentry = this.getEntry(i, j);
        if (playerchunkmapentry != null) {
            playerchunkmapentry.blockChanged(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
        }
    }
    
    public void addPlayer(final EntityPlayerMP player) {
        final int i = (int)player.posX >> 4;
        final int j = (int)player.posZ >> 4;
        player.managedPosX = player.posX;
        player.managedPosZ = player.posZ;
        final int k = Math.min(this.playerViewRadius, 8);
        final int l = i - k;
        final int i2 = i + k;
        final int j2 = j - k;
        final int k2 = j + k;
        final Set<ChunkPos> set = this.getPendingEntriesSafe(player);
        for (int l2 = i - this.playerViewRadius; l2 <= i + this.playerViewRadius; ++l2) {
            for (int i3 = j - this.playerViewRadius; i3 <= j + this.playerViewRadius; ++i3) {
                if (l2 >= l && l2 <= i2 && i3 >= j2 && i3 <= k2) {
                    this.getOrCreateEntry(l2, i3).addPlayer(player);
                }
                else {
                    set.add(new ChunkPos(l2, i3));
                }
            }
        }
        this.players.add(player);
        this.markSortPending();
    }
    
    public void removePlayer(final EntityPlayerMP player) {
        this.mapPlayerPendingEntries.remove(player);
        final int i = (int)player.managedPosX >> 4;
        final int j = (int)player.managedPosZ >> 4;
        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k) {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                final PlayerChunkMapEntry playerchunkmapentry = this.getEntry(k, l);
                if (playerchunkmapentry != null) {
                    playerchunkmapentry.removePlayer(player);
                }
            }
        }
        this.players.remove(player);
        this.markSortPending();
    }
    
    private boolean overlaps(final int x1, final int z1, final int x2, final int z2, final int radius) {
        final int i = x1 - x2;
        final int j = z1 - z2;
        return i >= -radius && i <= radius && j >= -radius && j <= radius;
    }
    
    public void updateMovingPlayer(final EntityPlayerMP player) {
        final int i = (int)player.posX >> 4;
        final int j = (int)player.posZ >> 4;
        final double d0 = player.managedPosX - player.posX;
        final double d2 = player.managedPosZ - player.posZ;
        final double d3 = d0 * d0 + d2 * d2;
        if (d3 >= 64.0) {
            final int k = (int)player.managedPosX >> 4;
            final int l = (int)player.managedPosZ >> 4;
            final int i2 = this.playerViewRadius;
            final int j2 = i - k;
            final int k2 = j - l;
            if (j2 != 0 || k2 != 0) {
                final Set<ChunkPos> set = this.getPendingEntriesSafe(player);
                for (int l2 = i - i2; l2 <= i + i2; ++l2) {
                    for (int i3 = j - i2; i3 <= j + i2; ++i3) {
                        if (!this.overlaps(l2, i3, k, l, i2)) {
                            if (Config.isLazyChunkLoading()) {
                                set.add(new ChunkPos(l2, i3));
                            }
                            else {
                                this.getOrCreateEntry(l2, i3).addPlayer(player);
                            }
                        }
                        if (!this.overlaps(l2 - j2, i3 - k2, i, j, i2)) {
                            set.remove(new ChunkPos(l2 - j2, i3 - k2));
                            final PlayerChunkMapEntry playerchunkmapentry = this.getEntry(l2 - j2, i3 - k2);
                            if (playerchunkmapentry != null) {
                                playerchunkmapentry.removePlayer(player);
                            }
                        }
                    }
                }
                player.managedPosX = player.posX;
                player.managedPosZ = player.posZ;
                this.markSortPending();
            }
        }
    }
    
    public boolean isPlayerWatchingChunk(final EntityPlayerMP player, final int chunkX, final int chunkZ) {
        final PlayerChunkMapEntry playerchunkmapentry = this.getEntry(chunkX, chunkZ);
        return playerchunkmapentry != null && playerchunkmapentry.containsPlayer(player) && playerchunkmapentry.isSentToPlayers();
    }
    
    public void setPlayerViewRadius(int radius) {
        radius = MathHelper.clamp(radius, 3, 64);
        if (radius != this.playerViewRadius) {
            final int i = radius - this.playerViewRadius;
            for (final EntityPlayerMP entityplayermp : Lists.newArrayList((Iterable)this.players)) {
                final int j = (int)entityplayermp.posX >> 4;
                final int k = (int)entityplayermp.posZ >> 4;
                final Set<ChunkPos> set = this.getPendingEntriesSafe(entityplayermp);
                if (i > 0) {
                    for (int j2 = j - radius; j2 <= j + radius; ++j2) {
                        for (int k2 = k - radius; k2 <= k + radius; ++k2) {
                            if (Config.isLazyChunkLoading()) {
                                set.add(new ChunkPos(j2, k2));
                            }
                            else {
                                final PlayerChunkMapEntry playerchunkmapentry1 = this.getOrCreateEntry(j2, k2);
                                if (!playerchunkmapentry1.containsPlayer(entityplayermp)) {
                                    playerchunkmapentry1.addPlayer(entityplayermp);
                                }
                            }
                        }
                    }
                }
                else {
                    for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                        for (int i2 = k - this.playerViewRadius; i2 <= k + this.playerViewRadius; ++i2) {
                            if (!this.overlaps(l, i2, j, k, radius)) {
                                set.remove(new ChunkPos(l, i2));
                                final PlayerChunkMapEntry playerchunkmapentry2 = this.getEntry(l, i2);
                                if (playerchunkmapentry2 != null) {
                                    playerchunkmapentry2.removePlayer(entityplayermp);
                                }
                            }
                        }
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
    
    public static int getFurthestViewableBlock(final int distance) {
        return distance * 16 - 16;
    }
    
    private static long getIndex(final int chunkX, final int chunkZ) {
        return chunkX + 2147483647L | chunkZ + 2147483647L << 32;
    }
    
    public void entryChanged(final PlayerChunkMapEntry entry) {
        this.dirtyEntries.add(entry);
    }
    
    public void removeEntry(final PlayerChunkMapEntry entry) {
        final ChunkPos chunkpos = entry.getPos();
        final long i = getIndex(chunkpos.x, chunkpos.z);
        entry.updateChunkInhabitedTime();
        this.entryMap.remove(i);
        this.entries.remove(entry);
        this.dirtyEntries.remove(entry);
        this.pendingSendToPlayers.remove(entry);
        this.entriesWithoutChunks.remove(entry);
        final Chunk chunk = entry.getChunk();
        if (chunk != null) {
            this.getWorldServer().getChunkProvider().queueUnload(chunk);
        }
    }
    
    private PriorityQueue<ChunkPos> getNearest(final Set<ChunkPos> p_getNearest_1_, final EntityPlayerMP p_getNearest_2_, final int p_getNearest_3_) {
        float f;
        for (f = p_getNearest_2_.rotationYaw + 90.0f; f <= -180.0f; f += 360.0f) {}
        while (f > 180.0f) {
            f -= 360.0f;
        }
        final double d0 = f * 0.017453292519943295;
        final double d2 = p_getNearest_2_.rotationPitch;
        final double d3 = d2 * 0.017453292519943295;
        final ChunkPosComparator chunkposcomparator = new ChunkPosComparator(p_getNearest_2_.chunkCoordX, p_getNearest_2_.chunkCoordZ, d0, d3);
        final Comparator<ChunkPos> comparator = Collections.reverseOrder((Comparator<ChunkPos>)chunkposcomparator);
        final PriorityQueue<ChunkPos> priorityqueue = new PriorityQueue<ChunkPos>(comparator);
        for (final ChunkPos chunkpos : p_getNearest_1_) {
            if (priorityqueue.size() < p_getNearest_3_) {
                priorityqueue.add(chunkpos);
            }
            else {
                final ChunkPos chunkpos2 = priorityqueue.peek();
                if (chunkposcomparator.compare(chunkpos, chunkpos2) >= 0) {
                    continue;
                }
                priorityqueue.remove();
                priorityqueue.add(chunkpos);
            }
        }
        return priorityqueue;
    }
    
    private Set<ChunkPos> getPendingEntriesSafe(final EntityPlayerMP p_getPendingEntriesSafe_1_) {
        final Set<ChunkPos> set = this.mapPlayerPendingEntries.get(p_getPendingEntriesSafe_1_);
        if (set != null) {
            return set;
        }
        final int i = Math.min(this.playerViewRadius, 8);
        final int j = this.playerViewRadius * 2 + 1;
        final int k = i * 2 + 1;
        int l = j * j - k * k;
        l = Math.max(l, 16);
        final Set<ChunkPos> hashset = new HashSet<ChunkPos>(l);
        this.mapPlayerPendingEntries.put(p_getPendingEntriesSafe_1_, hashset);
        return hashset;
    }
    
    static {
        NOT_SPECTATOR = (Predicate)new Predicate<EntityPlayerMP>() {
            public boolean apply(@Nullable final EntityPlayerMP p_apply_1_) {
                return p_apply_1_ != null && !p_apply_1_.isSpectator();
            }
        };
        CAN_GENERATE_CHUNKS = (Predicate)new Predicate<EntityPlayerMP>() {
            public boolean apply(@Nullable final EntityPlayerMP p_apply_1_) {
                return p_apply_1_ != null && (!p_apply_1_.isSpectator() || p_apply_1_.getServerWorld().getGameRules().getBoolean("spectatorsGenerateChunks"));
            }
        };
    }
}
