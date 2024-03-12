// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.world.World;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S21PacketChunkData;
import org.apache.logging.log4j.LogManager;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Collections;
import net.optifine.ChunkPosComparator;
import java.util.PriorityQueue;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldProvider;
import java.util.Iterator;
import net.minecraft.src.Config;
import java.util.HashMap;
import com.google.common.collect.Lists;
import net.minecraft.world.ChunkCoordIntPair;
import java.util.Set;
import java.util.Map;
import net.minecraft.util.LongHashMap;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.List;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.Logger;

public class PlayerManager
{
    private static final Logger pmLogger;
    private final WorldServer theWorldServer;
    private final List<EntityPlayerMP> players;
    private final LongHashMap<PlayerInstance> playerInstances;
    private final List<PlayerInstance> playerInstancesToUpdate;
    private final List<PlayerInstance> playerInstanceList;
    private int playerViewRadius;
    private long previousTotalWorldTime;
    private final int[][] xzDirectionsConst;
    private final Map<EntityPlayerMP, Set<ChunkCoordIntPair>> mapPlayerPendingEntries;
    
    public PlayerManager(final WorldServer serverWorld) {
        this.players = (List<EntityPlayerMP>)Lists.newArrayList();
        this.playerInstances = new LongHashMap<PlayerInstance>();
        this.playerInstancesToUpdate = (List<PlayerInstance>)Lists.newArrayList();
        this.playerInstanceList = (List<PlayerInstance>)Lists.newArrayList();
        this.xzDirectionsConst = new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
        this.mapPlayerPendingEntries = new HashMap<EntityPlayerMP, Set<ChunkCoordIntPair>>();
        this.theWorldServer = serverWorld;
        this.setPlayerViewRadius(serverWorld.getMinecraftServer().getConfigurationManager().getViewDistance());
    }
    
    public WorldServer getWorldServer() {
        return this.theWorldServer;
    }
    
    public void updatePlayerInstances() {
        final Set<Map.Entry<EntityPlayerMP, Set<ChunkCoordIntPair>>> set = this.mapPlayerPendingEntries.entrySet();
        final Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            final Map.Entry<EntityPlayerMP, Set<ChunkCoordIntPair>> entry = iterator.next();
            final Set<ChunkCoordIntPair> set2 = entry.getValue();
            if (!set2.isEmpty()) {
                final EntityPlayerMP entityplayermp = entry.getKey();
                if (entityplayermp.worldObj != this.theWorldServer) {
                    iterator.remove();
                }
                else {
                    int i = this.playerViewRadius / 3 + 1;
                    if (!Config.isLazyChunkLoading()) {
                        i = this.playerViewRadius * 2 + 1;
                    }
                    for (final ChunkCoordIntPair chunkcoordintpair : this.getNearest(set2, entityplayermp, i)) {
                        final PlayerInstance playermanager$playerinstance = this.getPlayerInstance(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos, true);
                        playermanager$playerinstance.addPlayer(entityplayermp);
                        set2.remove(chunkcoordintpair);
                    }
                }
            }
        }
        final long j = this.theWorldServer.getTotalWorldTime();
        if (j - this.previousTotalWorldTime > 8000L) {
            this.previousTotalWorldTime = j;
            for (int k = 0; k < this.playerInstanceList.size(); ++k) {
                final PlayerInstance playermanager$playerinstance2 = this.playerInstanceList.get(k);
                playermanager$playerinstance2.onUpdate();
                playermanager$playerinstance2.processChunk();
            }
        }
        else {
            for (int l = 0; l < this.playerInstancesToUpdate.size(); ++l) {
                final PlayerInstance playermanager$playerinstance3 = this.playerInstancesToUpdate.get(l);
                playermanager$playerinstance3.onUpdate();
            }
        }
        this.playerInstancesToUpdate.clear();
        if (this.players.isEmpty()) {
            final WorldProvider worldprovider = this.theWorldServer.provider;
            if (!worldprovider.canRespawnHere()) {
                this.theWorldServer.theChunkProviderServer.unloadAllChunks();
            }
        }
    }
    
    public boolean hasPlayerInstance(final int chunkX, final int chunkZ) {
        final long i = chunkX + 2147483647L | chunkZ + 2147483647L << 32;
        return this.playerInstances.getValueByKey(i) != null;
    }
    
    private PlayerInstance getPlayerInstance(final int chunkX, final int chunkZ, final boolean createIfAbsent) {
        final long i = chunkX + 2147483647L | chunkZ + 2147483647L << 32;
        PlayerInstance playermanager$playerinstance = this.playerInstances.getValueByKey(i);
        if (playermanager$playerinstance == null && createIfAbsent) {
            playermanager$playerinstance = new PlayerInstance(chunkX, chunkZ);
            this.playerInstances.add(i, playermanager$playerinstance);
            this.playerInstanceList.add(playermanager$playerinstance);
        }
        return playermanager$playerinstance;
    }
    
    public void markBlockForUpdate(final BlockPos pos) {
        final int i = pos.getX() >> 4;
        final int j = pos.getZ() >> 4;
        final PlayerInstance playermanager$playerinstance = this.getPlayerInstance(i, j, false);
        if (playermanager$playerinstance != null) {
            playermanager$playerinstance.flagChunkForUpdate(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
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
        final Set<ChunkCoordIntPair> set = this.getPendingEntriesSafe(player);
        for (int l2 = i - this.playerViewRadius; l2 <= i + this.playerViewRadius; ++l2) {
            for (int i3 = j - this.playerViewRadius; i3 <= j + this.playerViewRadius; ++i3) {
                if (l2 >= l && l2 <= i2 && i3 >= j2 && i3 <= k2) {
                    this.getPlayerInstance(l2, i3, true).addPlayer(player);
                }
                else {
                    set.add(new ChunkCoordIntPair(l2, i3));
                }
            }
        }
        this.players.add(player);
        this.filterChunkLoadQueue(player);
    }
    
    public void filterChunkLoadQueue(final EntityPlayerMP player) {
        final List<ChunkCoordIntPair> list = (List<ChunkCoordIntPair>)Lists.newArrayList((Iterable)player.loadedChunks);
        int i = 0;
        final int j = this.playerViewRadius;
        final int k = (int)player.posX >> 4;
        final int l = (int)player.posZ >> 4;
        int i2 = 0;
        int j2 = 0;
        ChunkCoordIntPair chunkcoordintpair = this.getPlayerInstance(k, l, true).chunkCoords;
        player.loadedChunks.clear();
        if (list.contains(chunkcoordintpair)) {
            player.loadedChunks.add(chunkcoordintpair);
        }
        for (int k2 = 1; k2 <= j * 2; ++k2) {
            for (int l2 = 0; l2 < 2; ++l2) {
                final int[] aint = this.xzDirectionsConst[i++ % 4];
                for (int i3 = 0; i3 < k2; ++i3) {
                    i2 += aint[0];
                    j2 += aint[1];
                    chunkcoordintpair = this.getPlayerInstance(k + i2, l + j2, true).chunkCoords;
                    if (list.contains(chunkcoordintpair)) {
                        player.loadedChunks.add(chunkcoordintpair);
                    }
                }
            }
        }
        i %= 4;
        for (int j3 = 0; j3 < j * 2; ++j3) {
            i2 += this.xzDirectionsConst[i][0];
            j2 += this.xzDirectionsConst[i][1];
            chunkcoordintpair = this.getPlayerInstance(k + i2, l + j2, true).chunkCoords;
            if (list.contains(chunkcoordintpair)) {
                player.loadedChunks.add(chunkcoordintpair);
            }
        }
    }
    
    public void removePlayer(final EntityPlayerMP player) {
        this.mapPlayerPendingEntries.remove(player);
        final int i = (int)player.managedPosX >> 4;
        final int j = (int)player.managedPosZ >> 4;
        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k) {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                final PlayerInstance playermanager$playerinstance = this.getPlayerInstance(k, l, false);
                if (playermanager$playerinstance != null) {
                    playermanager$playerinstance.removePlayer(player);
                }
            }
        }
        this.players.remove(player);
    }
    
    private boolean overlaps(final int x1, final int z1, final int x2, final int z2, final int radius) {
        final int i = x1 - x2;
        final int j = z1 - z2;
        return i >= -radius && i <= radius && (j >= -radius && j <= radius);
    }
    
    public void updateMountedMovingPlayer(final EntityPlayerMP player) {
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
                final Set<ChunkCoordIntPair> set = this.getPendingEntriesSafe(player);
                for (int l2 = i - i2; l2 <= i + i2; ++l2) {
                    for (int i3 = j - i2; i3 <= j + i2; ++i3) {
                        if (!this.overlaps(l2, i3, k, l, i2)) {
                            if (Config.isLazyChunkLoading()) {
                                set.add(new ChunkCoordIntPair(l2, i3));
                            }
                            else {
                                this.getPlayerInstance(l2, i3, true).addPlayer(player);
                            }
                        }
                        if (!this.overlaps(l2 - j2, i3 - k2, i, j, i2)) {
                            set.remove(new ChunkCoordIntPair(l2 - j2, i3 - k2));
                            final PlayerInstance playermanager$playerinstance = this.getPlayerInstance(l2 - j2, i3 - k2, false);
                            if (playermanager$playerinstance != null) {
                                playermanager$playerinstance.removePlayer(player);
                            }
                        }
                    }
                }
                this.filterChunkLoadQueue(player);
                player.managedPosX = player.posX;
                player.managedPosZ = player.posZ;
            }
        }
    }
    
    public boolean isPlayerWatchingChunk(final EntityPlayerMP player, final int chunkX, final int chunkZ) {
        final PlayerInstance playermanager$playerinstance = this.getPlayerInstance(chunkX, chunkZ, false);
        return playermanager$playerinstance != null && playermanager$playerinstance.playersWatchingChunk.contains(player) && !player.loadedChunks.contains(playermanager$playerinstance.chunkCoords);
    }
    
    public void setPlayerViewRadius(int radius) {
        radius = MathHelper.clamp_int(radius, 3, 64);
        if (radius != this.playerViewRadius) {
            final int i = radius - this.playerViewRadius;
            for (final EntityPlayerMP entityplayermp : Lists.newArrayList((Iterable)this.players)) {
                final int j = (int)entityplayermp.posX >> 4;
                final int k = (int)entityplayermp.posZ >> 4;
                final Set<ChunkCoordIntPair> set = this.getPendingEntriesSafe(entityplayermp);
                if (i > 0) {
                    for (int j2 = j - radius; j2 <= j + radius; ++j2) {
                        for (int k2 = k - radius; k2 <= k + radius; ++k2) {
                            if (Config.isLazyChunkLoading()) {
                                set.add(new ChunkCoordIntPair(j2, k2));
                            }
                            else {
                                final PlayerInstance playermanager$playerinstance1 = this.getPlayerInstance(j2, k2, true);
                                if (!playermanager$playerinstance1.playersWatchingChunk.contains(entityplayermp)) {
                                    playermanager$playerinstance1.addPlayer(entityplayermp);
                                }
                            }
                        }
                    }
                }
                else {
                    for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                        for (int i2 = k - this.playerViewRadius; i2 <= k + this.playerViewRadius; ++i2) {
                            if (!this.overlaps(l, i2, j, k, radius)) {
                                set.remove(new ChunkCoordIntPair(l, i2));
                                final PlayerInstance playermanager$playerinstance2 = this.getPlayerInstance(l, i2, true);
                                if (playermanager$playerinstance2 != null) {
                                    playermanager$playerinstance2.removePlayer(entityplayermp);
                                }
                            }
                        }
                    }
                }
            }
            this.playerViewRadius = radius;
        }
    }
    
    public static int getFurthestViewableBlock(final int distance) {
        return distance * 16 - 16;
    }
    
    private PriorityQueue<ChunkCoordIntPair> getNearest(final Set<ChunkCoordIntPair> p_getNearest_1_, final EntityPlayerMP p_getNearest_2_, final int p_getNearest_3_) {
        float f;
        for (f = p_getNearest_2_.rotationYaw + 90.0f; f <= -180.0f; f += 360.0f) {}
        while (f > 180.0f) {
            f -= 360.0f;
        }
        final double d0 = f * 0.017453292519943295;
        final double d2 = p_getNearest_2_.rotationPitch;
        final double d3 = d2 * 0.017453292519943295;
        final ChunkPosComparator chunkposcomparator = new ChunkPosComparator(p_getNearest_2_.chunkCoordX, p_getNearest_2_.chunkCoordZ, d0, d3);
        final Comparator<ChunkCoordIntPair> comparator = Collections.reverseOrder((Comparator<ChunkCoordIntPair>)chunkposcomparator);
        final PriorityQueue<ChunkCoordIntPair> priorityqueue = new PriorityQueue<ChunkCoordIntPair>(p_getNearest_3_, comparator);
        for (final ChunkCoordIntPair chunkcoordintpair : p_getNearest_1_) {
            if (priorityqueue.size() < p_getNearest_3_) {
                priorityqueue.add(chunkcoordintpair);
            }
            else {
                final ChunkCoordIntPair chunkcoordintpair2 = priorityqueue.peek();
                if (chunkposcomparator.compare(chunkcoordintpair, chunkcoordintpair2) >= 0) {
                    continue;
                }
                priorityqueue.remove();
                priorityqueue.add(chunkcoordintpair);
            }
        }
        return priorityqueue;
    }
    
    private Set<ChunkCoordIntPair> getPendingEntriesSafe(final EntityPlayerMP p_getPendingEntriesSafe_1_) {
        final Set<ChunkCoordIntPair> set = this.mapPlayerPendingEntries.get(p_getPendingEntriesSafe_1_);
        if (set != null) {
            return set;
        }
        final int i = Math.min(this.playerViewRadius, 8);
        final int j = this.playerViewRadius * 2 + 1;
        final int k = i * 2 + 1;
        int l = j * j - k * k;
        l = Math.max(l, 16);
        final HashSet hashset = new HashSet(l);
        this.mapPlayerPendingEntries.put(p_getPendingEntriesSafe_1_, hashset);
        return (Set<ChunkCoordIntPair>)hashset;
    }
    
    static {
        pmLogger = LogManager.getLogger();
    }
    
    class PlayerInstance
    {
        private final List<EntityPlayerMP> playersWatchingChunk;
        private final ChunkCoordIntPair chunkCoords;
        private short[] locationOfBlockChange;
        private int numBlocksToUpdate;
        private int flagsYAreasToUpdate;
        private long previousWorldTime;
        
        public PlayerInstance(final int chunkX, final int chunkZ) {
            this.playersWatchingChunk = (List<EntityPlayerMP>)Lists.newArrayList();
            this.locationOfBlockChange = new short[64];
            this.chunkCoords = new ChunkCoordIntPair(chunkX, chunkZ);
            PlayerManager.this.getWorldServer().theChunkProviderServer.loadChunk(chunkX, chunkZ);
        }
        
        public void addPlayer(final EntityPlayerMP player) {
            if (this.playersWatchingChunk.contains(player)) {
                PlayerManager.pmLogger.debug("Failed to add player. {} already is in chunk {}, {}", new Object[] { player, this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos });
            }
            else {
                if (this.playersWatchingChunk.isEmpty()) {
                    this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
                }
                this.playersWatchingChunk.add(player);
                player.loadedChunks.add(this.chunkCoords);
            }
        }
        
        public void removePlayer(final EntityPlayerMP player) {
            if (this.playersWatchingChunk.contains(player)) {
                final Chunk chunk = PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
                if (chunk.isPopulated()) {
                    player.playerNetServerHandler.sendPacket(new S21PacketChunkData(chunk, true, 0));
                }
                this.playersWatchingChunk.remove(player);
                player.loadedChunks.remove(this.chunkCoords);
                if (this.playersWatchingChunk.isEmpty()) {
                    final long i = this.chunkCoords.chunkXPos + 2147483647L | this.chunkCoords.chunkZPos + 2147483647L << 32;
                    this.increaseInhabitedTime(chunk);
                    PlayerManager.this.playerInstances.remove(i);
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
        
        private void increaseInhabitedTime(final Chunk theChunk) {
            theChunk.setInhabitedTime(theChunk.getInhabitedTime() + PlayerManager.this.theWorldServer.getTotalWorldTime() - this.previousWorldTime);
            this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
        }
        
        public void flagChunkForUpdate(final int x, final int y, final int z) {
            if (this.numBlocksToUpdate == 0) {
                PlayerManager.this.playerInstancesToUpdate.add(this);
            }
            this.flagsYAreasToUpdate |= 1 << (y >> 4);
            if (this.numBlocksToUpdate < 64) {
                final short short1 = (short)(x << 12 | z << 8 | y);
                for (int i = 0; i < this.numBlocksToUpdate; ++i) {
                    if (this.locationOfBlockChange[i] == short1) {
                        return;
                    }
                }
                this.locationOfBlockChange[this.numBlocksToUpdate++] = short1;
            }
        }
        
        public void sendToAllPlayersWatchingChunk(final Packet thePacket) {
            for (int i = 0; i < this.playersWatchingChunk.size(); ++i) {
                final EntityPlayerMP entityplayermp = this.playersWatchingChunk.get(i);
                if (!entityplayermp.loadedChunks.contains(this.chunkCoords)) {
                    entityplayermp.playerNetServerHandler.sendPacket(thePacket);
                }
            }
        }
        
        public void onUpdate() {
            if (this.numBlocksToUpdate != 0) {
                if (this.numBlocksToUpdate == 1) {
                    final int k1 = (this.locationOfBlockChange[0] >> 12 & 0xF) + this.chunkCoords.chunkXPos * 16;
                    final int i2 = this.locationOfBlockChange[0] & 0xFF;
                    final int k2 = (this.locationOfBlockChange[0] >> 8 & 0xF) + this.chunkCoords.chunkZPos * 16;
                    final BlockPos blockpos = new BlockPos(k1, i2, k2);
                    this.sendToAllPlayersWatchingChunk(new S23PacketBlockChange(PlayerManager.this.theWorldServer, blockpos));
                    if (PlayerManager.this.theWorldServer.getBlockState(blockpos).getBlock().hasTileEntity()) {
                        this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos));
                    }
                }
                else if (this.numBlocksToUpdate != 64) {
                    this.sendToAllPlayersWatchingChunk(new S22PacketMultiBlockChange(this.numBlocksToUpdate, this.locationOfBlockChange, PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos)));
                    for (int j1 = 0; j1 < this.numBlocksToUpdate; ++j1) {
                        final int l1 = (this.locationOfBlockChange[j1] >> 12 & 0xF) + this.chunkCoords.chunkXPos * 16;
                        final int j2 = this.locationOfBlockChange[j1] & 0xFF;
                        final int l2 = (this.locationOfBlockChange[j1] >> 8 & 0xF) + this.chunkCoords.chunkZPos * 16;
                        final BlockPos blockpos2 = new BlockPos(l1, j2, l2);
                        if (PlayerManager.this.theWorldServer.getBlockState(blockpos2).getBlock().hasTileEntity()) {
                            this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos2));
                        }
                    }
                }
                else {
                    final int m = this.chunkCoords.chunkXPos * 16;
                    final int j3 = this.chunkCoords.chunkZPos * 16;
                    this.sendToAllPlayersWatchingChunk(new S21PacketChunkData(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos), false, this.flagsYAreasToUpdate));
                    for (int k3 = 0; k3 < 16; ++k3) {
                        if ((this.flagsYAreasToUpdate & 1 << k3) != 0x0) {
                            final int l3 = k3 << 4;
                            final List<TileEntity> list = PlayerManager.this.theWorldServer.getTileEntitiesIn(m, l3, j3, m + 16, l3 + 16, j3 + 16);
                            for (int i3 = 0; i3 < list.size(); ++i3) {
                                this.sendTileToAllPlayersWatchingChunk(list.get(i3));
                            }
                        }
                    }
                }
                this.numBlocksToUpdate = 0;
                this.flagsYAreasToUpdate = 0;
            }
        }
        
        private void sendTileToAllPlayersWatchingChunk(final TileEntity theTileEntity) {
            if (theTileEntity != null) {
                final Packet packet = theTileEntity.getDescriptionPacket();
                if (packet != null) {
                    this.sendToAllPlayersWatchingChunk(packet);
                }
            }
        }
    }
}
