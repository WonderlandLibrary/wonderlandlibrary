package net.minecraft.server.management;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.optifine.ChunkPosComparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Exception performing whole class analysis ignored.
 */
public class PlayerManager {
    private static final Logger pmLogger = LogManager.getLogger();
    private final WorldServer theWorldServer;
    private final List<EntityPlayerMP> players = Lists.newArrayList();
    private final LongHashMap<PlayerInstance> playerInstances = new LongHashMap();
    private final List<PlayerInstance> playerInstancesToUpdate = Lists.newArrayList();
    private final List<PlayerInstance> playerInstanceList = Lists.newArrayList();
    private int playerViewRadius;
    private long previousTotalWorldTime;
    private final int[][] xzDirectionsConst = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    private final Map<EntityPlayerMP, Set<ChunkCoordIntPair>> mapPlayerPendingEntries = new HashMap();

    public PlayerManager(WorldServer serverWorld) {
        this.theWorldServer = serverWorld;
        this.setPlayerViewRadius(serverWorld.getMinecraftServer().getConfigurationManager().getViewDistance());
    }

    public WorldServer getWorldServer() {
        return this.theWorldServer;
    }

    public void updatePlayerInstances() {
        WorldProvider worldprovider;
        Set set = this.mapPlayerPendingEntries.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            Set set1 = (Set)entry.getValue();
            if (set1.isEmpty()) continue;
            EntityPlayerMP entityplayermp = (EntityPlayerMP)entry.getKey();
            if (entityplayermp.worldObj != this.theWorldServer) {
                iterator.remove();
                continue;
            }
            int i = this.playerViewRadius / 3 + 1;
            if (!Config.isLazyChunkLoading()) {
                i = this.playerViewRadius * 2 + 1;
            }
            for (ChunkCoordIntPair chunkcoordintpair : this.getNearest((Set<ChunkCoordIntPair>)set1, entityplayermp, i)) {
                PlayerInstance playermanager$playerinstance = this.getPlayerInstance(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos, true);
                playermanager$playerinstance.addPlayer(entityplayermp);
                set1.remove((Object)chunkcoordintpair);
            }
        }
        long j = this.theWorldServer.getTotalWorldTime();
        if (j - this.previousTotalWorldTime > 8000L) {
            this.previousTotalWorldTime = j;
            for (int k = 0; k < this.playerInstanceList.size(); ++k) {
                PlayerInstance playermanager$playerinstance1 = (PlayerInstance)this.playerInstanceList.get(k);
                playermanager$playerinstance1.onUpdate();
                playermanager$playerinstance1.processChunk();
            }
        } else {
            for (int l = 0; l < this.playerInstancesToUpdate.size(); ++l) {
                PlayerInstance playermanager$playerinstance2 = (PlayerInstance)this.playerInstancesToUpdate.get(l);
                playermanager$playerinstance2.onUpdate();
            }
        }
        this.playerInstancesToUpdate.clear();
        if (this.players.isEmpty() && !(worldprovider = this.theWorldServer.provider).canRespawnHere()) {
            this.theWorldServer.theChunkProviderServer.unloadAllChunks();
        }
    }

    public boolean hasPlayerInstance(int chunkX, int chunkZ) {
        long i = (long)chunkX + Integer.MAX_VALUE | (long)chunkZ + Integer.MAX_VALUE << 32;
        return this.playerInstances.getValueByKey(i) != null;
    }

    private PlayerInstance getPlayerInstance(int chunkX, int chunkZ, boolean createIfAbsent) {
        long i = (long)chunkX + Integer.MAX_VALUE | (long)chunkZ + Integer.MAX_VALUE << 32;
        PlayerInstance playermanager$playerinstance = (PlayerInstance)this.playerInstances.getValueByKey(i);
        if (playermanager$playerinstance == null && createIfAbsent) {
            playermanager$playerinstance = new PlayerInstance(this, chunkX, chunkZ);
            this.playerInstances.add(i, (Object)playermanager$playerinstance);
            this.playerInstanceList.add((Object)playermanager$playerinstance);
        }
        return playermanager$playerinstance;
    }

    public void markBlockForUpdate(BlockPos pos) {
        int j;
        int i = pos.getX() >> 4;
        PlayerInstance playermanager$playerinstance = this.getPlayerInstance(i, j = pos.getZ() >> 4, false);
        if (playermanager$playerinstance != null) {
            playermanager$playerinstance.flagChunkForUpdate(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
        }
    }

    public void addPlayer(EntityPlayerMP player) {
        int i = (int)player.posX >> 4;
        int j = (int)player.posZ >> 4;
        player.managedPosX = player.posX;
        player.managedPosZ = player.posZ;
        int k = Math.min((int)this.playerViewRadius, (int)8);
        int l = i - k;
        int i1 = i + k;
        int j1 = j - k;
        int k1 = j + k;
        Set<ChunkCoordIntPair> set = this.getPendingEntriesSafe(player);
        for (int l1 = i - this.playerViewRadius; l1 <= i + this.playerViewRadius; ++l1) {
            for (int i2 = j - this.playerViewRadius; i2 <= j + this.playerViewRadius; ++i2) {
                if (l1 >= l && l1 <= i1 && i2 >= j1 && i2 <= k1) {
                    this.getPlayerInstance(l1, i2, true).addPlayer(player);
                    continue;
                }
                set.add((Object)new ChunkCoordIntPair(l1, i2));
            }
        }
        this.players.add((Object)player);
        this.filterChunkLoadQueue(player);
    }

    public void filterChunkLoadQueue(EntityPlayerMP player) {
        ArrayList list = Lists.newArrayList((Iterable)player.loadedChunks);
        int i = 0;
        int j = this.playerViewRadius;
        int k = (int)player.posX >> 4;
        int l = (int)player.posZ >> 4;
        int i1 = 0;
        int j1 = 0;
        ChunkCoordIntPair chunkcoordintpair = PlayerInstance.access$000((PlayerInstance)this.getPlayerInstance(k, l, true));
        player.loadedChunks.clear();
        if (list.contains((Object)chunkcoordintpair)) {
            player.loadedChunks.add((Object)chunkcoordintpair);
        }
        for (int k1 = 1; k1 <= j * 2; ++k1) {
            for (int l1 = 0; l1 < 2; ++l1) {
                int[] aint = this.xzDirectionsConst[i++ % 4];
                for (int i2 = 0; i2 < k1; ++i2) {
                    chunkcoordintpair = PlayerInstance.access$000((PlayerInstance)this.getPlayerInstance(k + (i1 += aint[0]), l + (j1 += aint[1]), true));
                    if (!list.contains((Object)chunkcoordintpair)) continue;
                    player.loadedChunks.add((Object)chunkcoordintpair);
                }
            }
        }
        i %= 4;
        for (int j2 = 0; j2 < j * 2; ++j2) {
            chunkcoordintpair = PlayerInstance.access$000((PlayerInstance)this.getPlayerInstance(k + (i1 += this.xzDirectionsConst[i][0]), l + (j1 += this.xzDirectionsConst[i][1]), true));
            if (!list.contains((Object)chunkcoordintpair)) continue;
            player.loadedChunks.add((Object)chunkcoordintpair);
        }
    }

    public void removePlayer(EntityPlayerMP player) {
        this.mapPlayerPendingEntries.remove((Object)player);
        int i = (int)player.managedPosX >> 4;
        int j = (int)player.managedPosZ >> 4;
        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k) {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                PlayerInstance playermanager$playerinstance = this.getPlayerInstance(k, l, false);
                if (playermanager$playerinstance == null) continue;
                playermanager$playerinstance.removePlayer(player);
            }
        }
        this.players.remove((Object)player);
    }

    private boolean overlaps(int x1, int z1, int x2, int z2, int radius) {
        int i = x1 - x2;
        int j = z1 - z2;
        return i >= -radius && i <= radius ? j >= -radius && j <= radius : false;
    }

    public void updateMountedMovingPlayer(EntityPlayerMP player) {
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
                Set<ChunkCoordIntPair> set = this.getPendingEntriesSafe(player);
                for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                    for (int i2 = j - i1; i2 <= j + i1; ++i2) {
                        if (!this.overlaps(l1, i2, k, l, i1)) {
                            if (Config.isLazyChunkLoading()) {
                                set.add((Object)new ChunkCoordIntPair(l1, i2));
                            } else {
                                this.getPlayerInstance(l1, i2, true).addPlayer(player);
                            }
                        }
                        if (this.overlaps(l1 - j1, i2 - k1, i, j, i1)) continue;
                        set.remove((Object)new ChunkCoordIntPair(l1 - j1, i2 - k1));
                        PlayerInstance playermanager$playerinstance = this.getPlayerInstance(l1 - j1, i2 - k1, false);
                        if (playermanager$playerinstance == null) continue;
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
        return playermanager$playerinstance != null && PlayerInstance.access$100((PlayerInstance)playermanager$playerinstance).contains((Object)player) && !player.loadedChunks.contains((Object)PlayerInstance.access$000((PlayerInstance)playermanager$playerinstance));
    }

    public void setPlayerViewRadius(int radius) {
        if ((radius = MathHelper.clamp_int((int)radius, (int)3, (int)64)) != this.playerViewRadius) {
            int i = radius - this.playerViewRadius;
            for (EntityPlayerMP entityplayermp : Lists.newArrayList(this.players)) {
                int j = (int)entityplayermp.posX >> 4;
                int k = (int)entityplayermp.posZ >> 4;
                Set<ChunkCoordIntPair> set = this.getPendingEntriesSafe(entityplayermp);
                if (i > 0) {
                    for (int j1 = j - radius; j1 <= j + radius; ++j1) {
                        for (int k1 = k - radius; k1 <= k + radius; ++k1) {
                            if (Config.isLazyChunkLoading()) {
                                set.add((Object)new ChunkCoordIntPair(j1, k1));
                                continue;
                            }
                            PlayerInstance playermanager$playerinstance1 = this.getPlayerInstance(j1, k1, true);
                            if (PlayerInstance.access$100((PlayerInstance)playermanager$playerinstance1).contains((Object)entityplayermp)) continue;
                            playermanager$playerinstance1.addPlayer(entityplayermp);
                        }
                    }
                    continue;
                }
                for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                    for (int i1 = k - this.playerViewRadius; i1 <= k + this.playerViewRadius; ++i1) {
                        if (this.overlaps(l, i1, j, k, radius)) continue;
                        set.remove((Object)new ChunkCoordIntPair(l, i1));
                        PlayerInstance playermanager$playerinstance = this.getPlayerInstance(l, i1, true);
                        if (playermanager$playerinstance == null) continue;
                        playermanager$playerinstance.removePlayer(entityplayermp);
                    }
                }
            }
            this.playerViewRadius = radius;
        }
    }

    public static int getFurthestViewableBlock(int distance) {
        return distance * 16 - 16;
    }

    private PriorityQueue<ChunkCoordIntPair> getNearest(Set<ChunkCoordIntPair> p_getNearest_1_, EntityPlayerMP p_getNearest_2_, int p_getNearest_3_) {
        float f;
        for (f = p_getNearest_2_.rotationYaw + 90.0f; f <= -180.0f; f += 360.0f) {
        }
        while (f > 180.0f) {
            f -= 360.0f;
        }
        double d0 = (double)f * (Math.PI / 180);
        double d1 = p_getNearest_2_.rotationPitch;
        double d2 = d1 * (Math.PI / 180);
        ChunkPosComparator chunkposcomparator = new ChunkPosComparator(p_getNearest_2_.chunkCoordX, p_getNearest_2_.chunkCoordZ, d0, d2);
        Comparator comparator = Collections.reverseOrder((Comparator)chunkposcomparator);
        PriorityQueue priorityqueue = new PriorityQueue(p_getNearest_3_, comparator);
        for (ChunkCoordIntPair chunkcoordintpair : p_getNearest_1_) {
            if (priorityqueue.size() < p_getNearest_3_) {
                priorityqueue.add((Object)chunkcoordintpair);
                continue;
            }
            ChunkCoordIntPair chunkcoordintpair1 = (ChunkCoordIntPair)priorityqueue.peek();
            if (chunkposcomparator.compare(chunkcoordintpair, chunkcoordintpair1) >= 0) continue;
            priorityqueue.remove();
            priorityqueue.add((Object)chunkcoordintpair);
        }
        return priorityqueue;
    }

    private Set<ChunkCoordIntPair> getPendingEntriesSafe(EntityPlayerMP p_getPendingEntriesSafe_1_) {
        Set set = (Set)this.mapPlayerPendingEntries.get((Object)p_getPendingEntriesSafe_1_);
        if (set != null) {
            return set;
        }
        int i = Math.min((int)this.playerViewRadius, (int)8);
        int j = this.playerViewRadius * 2 + 1;
        int k = i * 2 + 1;
        int l = j * j - k * k;
        l = Math.max((int)l, (int)16);
        HashSet hashset = new HashSet(l);
        this.mapPlayerPendingEntries.put((Object)p_getPendingEntriesSafe_1_, (Object)hashset);
        return hashset;
    }

    static /* synthetic */ Logger access$200() {
        return pmLogger;
    }

    static /* synthetic */ WorldServer access$300(PlayerManager x0) {
        return x0.theWorldServer;
    }

    static /* synthetic */ LongHashMap access$400(PlayerManager x0) {
        return x0.playerInstances;
    }

    static /* synthetic */ List access$500(PlayerManager x0) {
        return x0.playerInstanceList;
    }

    static /* synthetic */ List access$600(PlayerManager x0) {
        return x0.playerInstancesToUpdate;
    }
}
