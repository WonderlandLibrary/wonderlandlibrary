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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.src.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.optifine.ChunkPosComparator;

public class PlayerChunkMap
{
    private static final Predicate<EntityPlayerMP> NOT_SPECTATOR = new Predicate<EntityPlayerMP>()
    {
        public boolean apply(@Nullable EntityPlayerMP p_apply_1_)
        {
            return p_apply_1_ != null && !p_apply_1_.isSpectator();
        }
    };
    private static final Predicate<EntityPlayerMP> CAN_GENERATE_CHUNKS = new Predicate<EntityPlayerMP>()
    {
        public boolean apply(@Nullable EntityPlayerMP p_apply_1_)
        {
            return p_apply_1_ != null && (!p_apply_1_.isSpectator() || p_apply_1_.getServerWorld().getGameRules().getBoolean("spectatorsGenerateChunks"));
        }
    };
    private final WorldServer world;
    private final List<EntityPlayerMP> players = Lists.<EntityPlayerMP>newArrayList();
    private final Long2ObjectMap<PlayerChunkMapEntry> entryMap = new Long2ObjectOpenHashMap<PlayerChunkMapEntry>(4096);
    private final Set<PlayerChunkMapEntry> dirtyEntries = Sets.<PlayerChunkMapEntry>newHashSet();
    private final List<PlayerChunkMapEntry> pendingSendToPlayers = Lists.<PlayerChunkMapEntry>newLinkedList();
    private final List<PlayerChunkMapEntry> entriesWithoutChunks = Lists.<PlayerChunkMapEntry>newLinkedList();
    private final List<PlayerChunkMapEntry> entries = Lists.<PlayerChunkMapEntry>newArrayList();

    /** Player view distance, in chunks. */
    private int playerViewRadius;

    /** time what is using to check if InhabitedTime should be calculated */
    private long previousTotalWorldTime;
    private boolean sortMissingChunks = true;
    private boolean sortSendToPlayers = true;
    private final Map<EntityPlayerMP, Set<ChunkPos>> mapPlayerPendingEntries = new HashMap<EntityPlayerMP, Set<ChunkPos>>();

    public PlayerChunkMap(WorldServer serverWorld)
    {
        this.world = serverWorld;
        this.setPlayerViewRadius(serverWorld.getMinecraftServer().getPlayerList().getViewDistance());
    }

    /**
     * Returns the WorldServer associated with this PlayerManager
     */
    public WorldServer getWorldServer()
    {
        return this.world;
    }

    public Iterator<Chunk> getChunkIterator()
    {
        final Iterator<PlayerChunkMapEntry> iterator = this.entries.iterator();
        return new AbstractIterator<Chunk>()
        {
            protected Chunk computeNext()
            {
                while (true)
                {
                    if (iterator.hasNext())
                    {
                        PlayerChunkMapEntry playerchunkmapentry = iterator.next();
                        Chunk chunk = playerchunkmapentry.getChunk();

                        if (chunk == null)
                        {
                            continue;
                        }

                        if (!chunk.isLightPopulated() && chunk.isTerrainPopulated())
                        {
                            return chunk;
                        }

                        if (!chunk.wasTicked())
                        {
                            return chunk;
                        }

                        if (!playerchunkmapentry.hasPlayerMatchingInRange(128.0D, PlayerChunkMap.NOT_SPECTATOR))
                        {
                            continue;
                        }

                        return chunk;
                    }

                    return (Chunk)this.endOfData();
                }
            }
        };
    }

    /**
     * updates all the player instances that need to be updated
     */
    public void tick()
    {
        Set<Entry<EntityPlayerMP, Set<ChunkPos>>> set = this.mapPlayerPendingEntries.entrySet();
        Iterator iterator = set.iterator();

        while (iterator.hasNext())
        {
            Entry<EntityPlayerMP, Set<ChunkPos>> entry = (Entry)iterator.next();
            Set<ChunkPos> set1 = (Set)entry.getValue();

            if (!set1.isEmpty())
            {
                EntityPlayerMP entityplayermp = entry.getKey();

                if (entityplayermp.getServerWorld() != this.world)
                {
                    iterator.remove();
                }
                else
                {
                    int i = this.playerViewRadius / 3 + 1;

                    if (!Config.isLazyChunkLoading())
                    {
                        i = this.playerViewRadius * 2 + 1;
                    }

                    for (ChunkPos chunkpos : this.getNearest(set1, entityplayermp, i))
                    {
                        PlayerChunkMapEntry playerchunkmapentry = this.getOrCreateEntry(chunkpos.x, chunkpos.z);

                        if (!playerchunkmapentry.containsPlayer(entityplayermp))
                        {
                            playerchunkmapentry.addPlayer(entityplayermp);
                        }

                        set1.remove(chunkpos);
                    }
                }
            }
        }

        long j = this.world.getTotalWorldTime();

        if (j - this.previousTotalWorldTime > 8000L)
        {
            this.previousTotalWorldTime = j;

            for (int k = 0; k < this.entries.size(); ++k)
            {
                PlayerChunkMapEntry playerchunkmapentry1 = this.entries.get(k);
                playerchunkmapentry1.update();
                playerchunkmapentry1.updateChunkInhabitedTime();
            }
        }

        if (!this.dirtyEntries.isEmpty())
        {
            for (PlayerChunkMapEntry playerchunkmapentry2 : this.dirtyEntries)
            {
                playerchunkmapentry2.update();
            }

            this.dirtyEntries.clear();
        }

        if (this.sortMissingChunks && j % 4L == 0L)
        {
            this.sortMissingChunks = false;
            Collections.sort(this.entriesWithoutChunks, new Comparator<PlayerChunkMapEntry>()
            {
                public int compare(PlayerChunkMapEntry p_compare_1_, PlayerChunkMapEntry p_compare_2_)
                {
                    return ComparisonChain.start().compare(p_compare_1_.getClosestPlayerDistance(), p_compare_2_.getClosestPlayerDistance()).result();
                }
            });
        }

        if (this.sortSendToPlayers && j % 4L == 2L)
        {
            this.sortSendToPlayers = false;
            Collections.sort(this.pendingSendToPlayers, new Comparator<PlayerChunkMapEntry>()
            {
                public int compare(PlayerChunkMapEntry p_compare_1_, PlayerChunkMapEntry p_compare_2_)
                {
                    return ComparisonChain.start().compare(p_compare_1_.getClosestPlayerDistance(), p_compare_2_.getClosestPlayerDistance()).result();
                }
            });
        }

        if (!this.entriesWithoutChunks.isEmpty())
        {
            long l = System.nanoTime() + 50000000L;
            int j1 = 49;
            Iterator<PlayerChunkMapEntry> iterator2 = this.entriesWithoutChunks.iterator();

            while (iterator2.hasNext())
            {
                PlayerChunkMapEntry playerchunkmapentry4 = iterator2.next();

                if (playerchunkmapentry4.getChunk() == null)
                {
                    boolean flag = playerchunkmapentry4.hasPlayerMatching(CAN_GENERATE_CHUNKS);

                    if (playerchunkmapentry4.providePlayerChunk(flag))
                    {
                        iterator2.remove();

                        if (playerchunkmapentry4.sendToPlayers())
                        {
                            this.pendingSendToPlayers.remove(playerchunkmapentry4);
                        }

                        --j1;

                        if (j1 < 0 || System.nanoTime() > l)
                        {
                            break;
                        }
                    }
                }
            }
        }

        if (!this.pendingSendToPlayers.isEmpty())
        {
            int i1 = 81;
            Iterator<PlayerChunkMapEntry> iterator1 = this.pendingSendToPlayers.iterator();

            while (iterator1.hasNext())
            {
                PlayerChunkMapEntry playerchunkmapentry3 = iterator1.next();

                if (playerchunkmapentry3.sendToPlayers())
                {
                    iterator1.remove();
                    --i1;

                    if (i1 < 0)
                    {
                        break;
                    }
                }
            }
        }

        if (this.players.isEmpty())
        {
            WorldProvider worldprovider = this.world.provider;

            if (!worldprovider.canRespawnHere())
            {
                this.world.getChunkProvider().queueUnloadAll();
            }
        }
    }

    public boolean contains(int chunkX, int chunkZ)
    {
        long i = getIndex(chunkX, chunkZ);
        return this.entryMap.get(i) != null;
    }

    @Nullable
    public PlayerChunkMapEntry getEntry(int x, int z)
    {
        return (PlayerChunkMapEntry)this.entryMap.get(getIndex(x, z));
    }

    private PlayerChunkMapEntry getOrCreateEntry(int chunkX, int chunkZ)
    {
        long i = getIndex(chunkX, chunkZ);
        PlayerChunkMapEntry playerchunkmapentry = (PlayerChunkMapEntry)this.entryMap.get(i);

        if (playerchunkmapentry == null)
        {
            playerchunkmapentry = new PlayerChunkMapEntry(this, chunkX, chunkZ);
            this.entryMap.put(i, playerchunkmapentry);
            this.entries.add(playerchunkmapentry);

            if (playerchunkmapentry.getChunk() == null)
            {
                this.entriesWithoutChunks.add(playerchunkmapentry);
            }

            if (!playerchunkmapentry.sendToPlayers())
            {
                this.pendingSendToPlayers.add(playerchunkmapentry);
            }
        }

        return playerchunkmapentry;
    }

    public void markBlockForUpdate(BlockPos pos)
    {
        int i = pos.getX() >> 4;
        int j = pos.getZ() >> 4;
        PlayerChunkMapEntry playerchunkmapentry = this.getEntry(i, j);

        if (playerchunkmapentry != null)
        {
            playerchunkmapentry.blockChanged(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
        }
    }

    /**
     * Adds an EntityPlayerMP to the PlayerManager and to all player instances within player visibility
     */
    public void addPlayer(EntityPlayerMP player)
    {
        int i = (int)player.posX >> 4;
        int j = (int)player.posZ >> 4;
        player.managedPosX = player.posX;
        player.managedPosZ = player.posZ;
        int k = Math.min(this.playerViewRadius, 8);
        int l = i - k;
        int i1 = i + k;
        int j1 = j - k;
        int k1 = j + k;
        Set<ChunkPos> set = this.getPendingEntriesSafe(player);

        for (int l1 = i - this.playerViewRadius; l1 <= i + this.playerViewRadius; ++l1)
        {
            for (int i2 = j - this.playerViewRadius; i2 <= j + this.playerViewRadius; ++i2)
            {
                if (l1 >= l && l1 <= i1 && i2 >= j1 && i2 <= k1)
                {
                    this.getOrCreateEntry(l1, i2).addPlayer(player);
                }
                else
                {
                    set.add(new ChunkPos(l1, i2));
                }
            }
        }

        this.players.add(player);
        this.markSortPending();
    }

    /**
     * Removes an EntityPlayerMP from the PlayerManager.
     */
    public void removePlayer(EntityPlayerMP player)
    {
        this.mapPlayerPendingEntries.remove(player);
        int i = (int)player.managedPosX >> 4;
        int j = (int)player.managedPosZ >> 4;

        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k)
        {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l)
            {
                PlayerChunkMapEntry playerchunkmapentry = this.getEntry(k, l);

                if (playerchunkmapentry != null)
                {
                    playerchunkmapentry.removePlayer(player);
                }
            }
        }

        this.players.remove(player);
        this.markSortPending();
    }

    /**
     * Determine if two rectangles centered at the given points overlap for the provided radius. Arguments: x1, z1, x2,
     * z2, radius.
     */
    private boolean overlaps(int x1, int z1, int x2, int z2, int radius)
    {
        int i = x1 - x2;
        int j = z1 - z2;

        if (i >= -radius && i <= radius)
        {
            return j >= -radius && j <= radius;
        }
        else
        {
            return false;
        }
    }

    /**
     * Update chunks around a player that moved
     */
    public void updateMovingPlayer(EntityPlayerMP player)
    {
        int i = (int)player.posX >> 4;
        int j = (int)player.posZ >> 4;
        double d0 = player.managedPosX - player.posX;
        double d1 = player.managedPosZ - player.posZ;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 >= 64.0D)
        {
            int k = (int)player.managedPosX >> 4;
            int l = (int)player.managedPosZ >> 4;
            int i1 = this.playerViewRadius;
            int j1 = i - k;
            int k1 = j - l;

            if (j1 != 0 || k1 != 0)
            {
                Set<ChunkPos> set = this.getPendingEntriesSafe(player);

                for (int l1 = i - i1; l1 <= i + i1; ++l1)
                {
                    for (int i2 = j - i1; i2 <= j + i1; ++i2)
                    {
                        if (!this.overlaps(l1, i2, k, l, i1))
                        {
                            if (Config.isLazyChunkLoading())
                            {
                                set.add(new ChunkPos(l1, i2));
                            }
                            else
                            {
                                this.getOrCreateEntry(l1, i2).addPlayer(player);
                            }
                        }

                        if (!this.overlaps(l1 - j1, i2 - k1, i, j, i1))
                        {
                            set.remove(new ChunkPos(l1 - j1, i2 - k1));
                            PlayerChunkMapEntry playerchunkmapentry = this.getEntry(l1 - j1, i2 - k1);

                            if (playerchunkmapentry != null)
                            {
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

    public boolean isPlayerWatchingChunk(EntityPlayerMP player, int chunkX, int chunkZ)
    {
        PlayerChunkMapEntry playerchunkmapentry = this.getEntry(chunkX, chunkZ);
        return playerchunkmapentry != null && playerchunkmapentry.containsPlayer(player) && playerchunkmapentry.isSentToPlayers();
    }

    /**
     * Called when the server's view distance changes, sending or rescinding chunks as needed.
     *  
     * @param radius Radius in chunks
     */
    public void setPlayerViewRadius(int radius)
    {
        radius = MathHelper.clamp(radius, 3, 64);

        if (radius != this.playerViewRadius)
        {
            int i = radius - this.playerViewRadius;

            for (EntityPlayerMP entityplayermp : Lists.newArrayList(this.players))
            {
                int j = (int)entityplayermp.posX >> 4;
                int k = (int)entityplayermp.posZ >> 4;
                Set<ChunkPos> set = this.getPendingEntriesSafe(entityplayermp);

                if (i > 0)
                {
                    for (int j1 = j - radius; j1 <= j + radius; ++j1)
                    {
                        for (int k1 = k - radius; k1 <= k + radius; ++k1)
                        {
                            if (Config.isLazyChunkLoading())
                            {
                                set.add(new ChunkPos(j1, k1));
                            }
                            else
                            {
                                PlayerChunkMapEntry playerchunkmapentry1 = this.getOrCreateEntry(j1, k1);

                                if (!playerchunkmapentry1.containsPlayer(entityplayermp))
                                {
                                    playerchunkmapentry1.addPlayer(entityplayermp);
                                }
                            }
                        }
                    }
                }
                else
                {
                    for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l)
                    {
                        for (int i1 = k - this.playerViewRadius; i1 <= k + this.playerViewRadius; ++i1)
                        {
                            if (!this.overlaps(l, i1, j, k, radius))
                            {
                                set.remove(new ChunkPos(l, i1));
                                PlayerChunkMapEntry playerchunkmapentry = this.getEntry(l, i1);

                                if (playerchunkmapentry != null)
                                {
                                    playerchunkmapentry.removePlayer(entityplayermp);
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

    private void markSortPending()
    {
        this.sortMissingChunks = true;
        this.sortSendToPlayers = true;
    }

    /**
     * Gets the max entity track distance (in blocks) for the given view distance.
     *  
     * @param distance The view distance in chunks
     */
    public static int getFurthestViewableBlock(int distance)
    {
        return distance * 16 - 16;
    }

    private static long getIndex(int chunkX, int chunkZ)
    {
        return (long)chunkX + 2147483647L | (long)chunkZ + 2147483647L << 32;
    }

    /**
     * Marks an entry as dirty
     */
    public void entryChanged(PlayerChunkMapEntry entry)
    {
        this.dirtyEntries.add(entry);
    }

    public void removeEntry(PlayerChunkMapEntry entry)
    {
        ChunkPos chunkpos = entry.getPos();
        long i = getIndex(chunkpos.x, chunkpos.z);
        entry.updateChunkInhabitedTime();
        this.entryMap.remove(i);
        this.entries.remove(entry);
        this.dirtyEntries.remove(entry);
        this.pendingSendToPlayers.remove(entry);
        this.entriesWithoutChunks.remove(entry);
        Chunk chunk = entry.getChunk();

        if (chunk != null)
        {
            this.getWorldServer().getChunkProvider().queueUnload(chunk);
        }
    }

    private PriorityQueue<ChunkPos> getNearest(Set<ChunkPos> p_getNearest_1_, EntityPlayerMP p_getNearest_2_, int p_getNearest_3_)
    {
        float f;

        for (f = p_getNearest_2_.rotationYaw + 90.0F; f <= -180.0F; f += 360.0F)
        {
            ;
        }

        while (f > 180.0F)
        {
            f -= 360.0F;
        }

        double d0 = (double)f * 0.017453292519943295D;
        double d1 = (double)p_getNearest_2_.rotationPitch;
        double d2 = d1 * 0.017453292519943295D;
        ChunkPosComparator chunkposcomparator = new ChunkPosComparator(p_getNearest_2_.chunkCoordX, p_getNearest_2_.chunkCoordZ, d0, d2);
        Comparator<ChunkPos> comparator = Collections.<ChunkPos>reverseOrder(chunkposcomparator);
        PriorityQueue<ChunkPos> priorityqueue = new PriorityQueue<ChunkPos>(comparator);

        for (ChunkPos chunkpos : p_getNearest_1_)
        {
            if (priorityqueue.size() < p_getNearest_3_)
            {
                priorityqueue.add(chunkpos);
            }
            else
            {
                ChunkPos chunkpos1 = priorityqueue.peek();

                if (chunkposcomparator.compare(chunkpos, chunkpos1) < 0)
                {
                    priorityqueue.remove();
                    priorityqueue.add(chunkpos);
                }
            }
        }

        return priorityqueue;
    }

    private Set<ChunkPos> getPendingEntriesSafe(EntityPlayerMP p_getPendingEntriesSafe_1_)
    {
        Set<ChunkPos> set = (Set)this.mapPlayerPendingEntries.get(p_getPendingEntriesSafe_1_);

        if (set != null)
        {
            return set;
        }
        else
        {
            int i = Math.min(this.playerViewRadius, 8);
            int j = this.playerViewRadius * 2 + 1;
            int k = i * 2 + 1;
            int l = j * j - k * k;
            l = Math.max(l, 16);
            Set<ChunkPos> hashset = new HashSet(l);
            this.mapPlayerPendingEntries.put(p_getPendingEntriesSafe_1_, hashset);
            return hashset;
        }
    }
}
