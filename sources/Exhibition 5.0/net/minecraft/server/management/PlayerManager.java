// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.management;

import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S21PacketChunkData;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldProvider;
import com.google.common.collect.Lists;
import net.minecraft.util.LongHashMap;
import java.util.List;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.Logger;

public class PlayerManager
{
    private static final Logger field_152627_a;
    private final WorldServer theWorldServer;
    private final List players;
    private final LongHashMap playerInstances;
    private final List playerInstancesToUpdate;
    private final List playerInstanceList;
    private int playerViewRadius;
    private long previousTotalWorldTime;
    private final int[][] xzDirectionsConst;
    private static final String __OBFID = "CL_00001434";
    
    public PlayerManager(final WorldServer p_i1176_1_) {
        this.players = Lists.newArrayList();
        this.playerInstances = new LongHashMap();
        this.playerInstancesToUpdate = Lists.newArrayList();
        this.playerInstanceList = Lists.newArrayList();
        this.xzDirectionsConst = new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
        this.theWorldServer = p_i1176_1_;
        this.func_152622_a(p_i1176_1_.func_73046_m().getConfigurationManager().getViewDistance());
    }
    
    public WorldServer getMinecraftServer() {
        return this.theWorldServer;
    }
    
    public void updatePlayerInstances() {
        final long var1 = this.theWorldServer.getTotalWorldTime();
        if (var1 - this.previousTotalWorldTime > 8000L) {
            this.previousTotalWorldTime = var1;
            for (int var2 = 0; var2 < this.playerInstanceList.size(); ++var2) {
                final PlayerInstance var3 = this.playerInstanceList.get(var2);
                var3.onUpdate();
                var3.processChunk();
            }
        }
        else {
            for (int var2 = 0; var2 < this.playerInstancesToUpdate.size(); ++var2) {
                final PlayerInstance var3 = this.playerInstancesToUpdate.get(var2);
                var3.onUpdate();
            }
        }
        this.playerInstancesToUpdate.clear();
        if (this.players.isEmpty()) {
            final WorldProvider var4 = this.theWorldServer.provider;
            if (!var4.canRespawnHere()) {
                this.theWorldServer.theChunkProviderServer.unloadAllChunks();
            }
        }
    }
    
    public boolean func_152621_a(final int p_152621_1_, final int p_152621_2_) {
        final long var3 = p_152621_1_ + 2147483647L | p_152621_2_ + 2147483647L << 32;
        return this.playerInstances.getValueByKey(var3) != null;
    }
    
    private PlayerInstance getPlayerInstance(final int p_72690_1_, final int p_72690_2_, final boolean p_72690_3_) {
        final long var4 = p_72690_1_ + 2147483647L | p_72690_2_ + 2147483647L << 32;
        PlayerInstance var5 = (PlayerInstance)this.playerInstances.getValueByKey(var4);
        if (var5 == null && p_72690_3_) {
            var5 = new PlayerInstance(p_72690_1_, p_72690_2_);
            this.playerInstances.add(var4, var5);
            this.playerInstanceList.add(var5);
        }
        return var5;
    }
    
    public void func_180244_a(final BlockPos p_180244_1_) {
        final int var2 = p_180244_1_.getX() >> 4;
        final int var3 = p_180244_1_.getZ() >> 4;
        final PlayerInstance var4 = this.getPlayerInstance(var2, var3, false);
        if (var4 != null) {
            var4.flagChunkForUpdate(p_180244_1_.getX() & 0xF, p_180244_1_.getY(), p_180244_1_.getZ() & 0xF);
        }
    }
    
    public void addPlayer(final EntityPlayerMP p_72683_1_) {
        final int var2 = (int)p_72683_1_.posX >> 4;
        final int var3 = (int)p_72683_1_.posZ >> 4;
        p_72683_1_.managedPosX = p_72683_1_.posX;
        p_72683_1_.managedPosZ = p_72683_1_.posZ;
        for (int var4 = var2 - this.playerViewRadius; var4 <= var2 + this.playerViewRadius; ++var4) {
            for (int var5 = var3 - this.playerViewRadius; var5 <= var3 + this.playerViewRadius; ++var5) {
                this.getPlayerInstance(var4, var5, true).addPlayer(p_72683_1_);
            }
        }
        this.players.add(p_72683_1_);
        this.filterChunkLoadQueue(p_72683_1_);
    }
    
    public void filterChunkLoadQueue(final EntityPlayerMP p_72691_1_) {
        final ArrayList var2 = Lists.newArrayList((Iterable)p_72691_1_.loadedChunks);
        int var3 = 0;
        final int var4 = this.playerViewRadius;
        final int var5 = (int)p_72691_1_.posX >> 4;
        final int var6 = (int)p_72691_1_.posZ >> 4;
        int var7 = 0;
        int var8 = 0;
        ChunkCoordIntPair var9 = this.getPlayerInstance(var5, var6, true).currentChunk;
        p_72691_1_.loadedChunks.clear();
        if (var2.contains(var9)) {
            p_72691_1_.loadedChunks.add(var9);
        }
        for (int var10 = 1; var10 <= var4 * 2; ++var10) {
            for (int var11 = 0; var11 < 2; ++var11) {
                final int[] var12 = this.xzDirectionsConst[var3++ % 4];
                for (int var13 = 0; var13 < var10; ++var13) {
                    var7 += var12[0];
                    var8 += var12[1];
                    var9 = this.getPlayerInstance(var5 + var7, var6 + var8, true).currentChunk;
                    if (var2.contains(var9)) {
                        p_72691_1_.loadedChunks.add(var9);
                    }
                }
            }
        }
        var3 %= 4;
        for (int var10 = 0; var10 < var4 * 2; ++var10) {
            var7 += this.xzDirectionsConst[var3][0];
            var8 += this.xzDirectionsConst[var3][1];
            var9 = this.getPlayerInstance(var5 + var7, var6 + var8, true).currentChunk;
            if (var2.contains(var9)) {
                p_72691_1_.loadedChunks.add(var9);
            }
        }
    }
    
    public void removePlayer(final EntityPlayerMP p_72695_1_) {
        final int var2 = (int)p_72695_1_.managedPosX >> 4;
        final int var3 = (int)p_72695_1_.managedPosZ >> 4;
        for (int var4 = var2 - this.playerViewRadius; var4 <= var2 + this.playerViewRadius; ++var4) {
            for (int var5 = var3 - this.playerViewRadius; var5 <= var3 + this.playerViewRadius; ++var5) {
                final PlayerInstance var6 = this.getPlayerInstance(var4, var5, false);
                if (var6 != null) {
                    var6.removePlayer(p_72695_1_);
                }
            }
        }
        this.players.remove(p_72695_1_);
    }
    
    private boolean overlaps(final int p_72684_1_, final int p_72684_2_, final int p_72684_3_, final int p_72684_4_, final int p_72684_5_) {
        final int var6 = p_72684_1_ - p_72684_3_;
        final int var7 = p_72684_2_ - p_72684_4_;
        return var6 >= -p_72684_5_ && var6 <= p_72684_5_ && (var7 >= -p_72684_5_ && var7 <= p_72684_5_);
    }
    
    public void updateMountedMovingPlayer(final EntityPlayerMP p_72685_1_) {
        final int var2 = (int)p_72685_1_.posX >> 4;
        final int var3 = (int)p_72685_1_.posZ >> 4;
        final double var4 = p_72685_1_.managedPosX - p_72685_1_.posX;
        final double var5 = p_72685_1_.managedPosZ - p_72685_1_.posZ;
        final double var6 = var4 * var4 + var5 * var5;
        if (var6 >= 64.0) {
            final int var7 = (int)p_72685_1_.managedPosX >> 4;
            final int var8 = (int)p_72685_1_.managedPosZ >> 4;
            final int var9 = this.playerViewRadius;
            final int var10 = var2 - var7;
            final int var11 = var3 - var8;
            if (var10 != 0 || var11 != 0) {
                for (int var12 = var2 - var9; var12 <= var2 + var9; ++var12) {
                    for (int var13 = var3 - var9; var13 <= var3 + var9; ++var13) {
                        if (!this.overlaps(var12, var13, var7, var8, var9)) {
                            this.getPlayerInstance(var12, var13, true).addPlayer(p_72685_1_);
                        }
                        if (!this.overlaps(var12 - var10, var13 - var11, var2, var3, var9)) {
                            final PlayerInstance var14 = this.getPlayerInstance(var12 - var10, var13 - var11, false);
                            if (var14 != null) {
                                var14.removePlayer(p_72685_1_);
                            }
                        }
                    }
                }
                this.filterChunkLoadQueue(p_72685_1_);
                p_72685_1_.managedPosX = p_72685_1_.posX;
                p_72685_1_.managedPosZ = p_72685_1_.posZ;
            }
        }
    }
    
    public boolean isPlayerWatchingChunk(final EntityPlayerMP p_72694_1_, final int p_72694_2_, final int p_72694_3_) {
        final PlayerInstance var4 = this.getPlayerInstance(p_72694_2_, p_72694_3_, false);
        return var4 != null && var4.playersWatchingChunk.contains(p_72694_1_) && !p_72694_1_.loadedChunks.contains(var4.currentChunk);
    }
    
    public void func_152622_a(int p_152622_1_) {
        p_152622_1_ = MathHelper.clamp_int(p_152622_1_, 3, 32);
        if (p_152622_1_ != this.playerViewRadius) {
            final int var2 = p_152622_1_ - this.playerViewRadius;
            final ArrayList var3 = Lists.newArrayList((Iterable)this.players);
            for (final EntityPlayerMP var5 : var3) {
                final int var6 = (int)var5.posX >> 4;
                final int var7 = (int)var5.posZ >> 4;
                if (var2 > 0) {
                    for (int var8 = var6 - p_152622_1_; var8 <= var6 + p_152622_1_; ++var8) {
                        for (int var9 = var7 - p_152622_1_; var9 <= var7 + p_152622_1_; ++var9) {
                            final PlayerInstance var10 = this.getPlayerInstance(var8, var9, true);
                            if (!var10.playersWatchingChunk.contains(var5)) {
                                var10.addPlayer(var5);
                            }
                        }
                    }
                }
                else {
                    for (int var8 = var6 - this.playerViewRadius; var8 <= var6 + this.playerViewRadius; ++var8) {
                        for (int var9 = var7 - this.playerViewRadius; var9 <= var7 + this.playerViewRadius; ++var9) {
                            if (!this.overlaps(var8, var9, var6, var7, p_152622_1_)) {
                                this.getPlayerInstance(var8, var9, true).removePlayer(var5);
                            }
                        }
                    }
                }
            }
            this.playerViewRadius = p_152622_1_;
        }
    }
    
    public static int getFurthestViewableBlock(final int p_72686_0_) {
        return p_72686_0_ * 16 - 16;
    }
    
    static {
        field_152627_a = LogManager.getLogger();
    }
    
    class PlayerInstance
    {
        private final List playersWatchingChunk;
        private final ChunkCoordIntPair currentChunk;
        private short[] locationOfBlockChange;
        private int numBlocksToUpdate;
        private int flagsYAreasToUpdate;
        private long previousWorldTime;
        private static final String __OBFID = "CL_00001435";
        
        public PlayerInstance(final int p_i1518_2_, final int p_i1518_3_) {
            this.playersWatchingChunk = Lists.newArrayList();
            this.locationOfBlockChange = new short[64];
            this.currentChunk = new ChunkCoordIntPair(p_i1518_2_, p_i1518_3_);
            PlayerManager.this.getMinecraftServer().theChunkProviderServer.loadChunk(p_i1518_2_, p_i1518_3_);
        }
        
        public void addPlayer(final EntityPlayerMP p_73255_1_) {
            if (this.playersWatchingChunk.contains(p_73255_1_)) {
                PlayerManager.field_152627_a.debug("Failed to add player. {} already is in chunk {}, {}", new Object[] { p_73255_1_, this.currentChunk.chunkXPos, this.currentChunk.chunkZPos });
            }
            else {
                if (this.playersWatchingChunk.isEmpty()) {
                    this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
                }
                this.playersWatchingChunk.add(p_73255_1_);
                p_73255_1_.loadedChunks.add(this.currentChunk);
            }
        }
        
        public void removePlayer(final EntityPlayerMP p_73252_1_) {
            if (this.playersWatchingChunk.contains(p_73252_1_)) {
                final Chunk var2 = PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.currentChunk.chunkXPos, this.currentChunk.chunkZPos);
                if (var2.isPopulated()) {
                    p_73252_1_.playerNetServerHandler.sendPacket(new S21PacketChunkData(var2, true, 0));
                }
                this.playersWatchingChunk.remove(p_73252_1_);
                p_73252_1_.loadedChunks.remove(this.currentChunk);
                if (this.playersWatchingChunk.isEmpty()) {
                    final long var3 = this.currentChunk.chunkXPos + 2147483647L | this.currentChunk.chunkZPos + 2147483647L << 32;
                    this.increaseInhabitedTime(var2);
                    PlayerManager.this.playerInstances.remove(var3);
                    PlayerManager.this.playerInstanceList.remove(this);
                    if (this.numBlocksToUpdate > 0) {
                        PlayerManager.this.playerInstancesToUpdate.remove(this);
                    }
                    PlayerManager.this.getMinecraftServer().theChunkProviderServer.dropChunk(this.currentChunk.chunkXPos, this.currentChunk.chunkZPos);
                }
            }
        }
        
        public void processChunk() {
            this.increaseInhabitedTime(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.currentChunk.chunkXPos, this.currentChunk.chunkZPos));
        }
        
        private void increaseInhabitedTime(final Chunk p_111196_1_) {
            p_111196_1_.setInhabitedTime(p_111196_1_.getInhabitedTime() + PlayerManager.this.theWorldServer.getTotalWorldTime() - this.previousWorldTime);
            this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
        }
        
        public void flagChunkForUpdate(final int p_151253_1_, final int p_151253_2_, final int p_151253_3_) {
            if (this.numBlocksToUpdate == 0) {
                PlayerManager.this.playerInstancesToUpdate.add(this);
            }
            this.flagsYAreasToUpdate |= 1 << (p_151253_2_ >> 4);
            if (this.numBlocksToUpdate < 64) {
                final short var4 = (short)(p_151253_1_ << 12 | p_151253_3_ << 8 | p_151253_2_);
                for (int var5 = 0; var5 < this.numBlocksToUpdate; ++var5) {
                    if (this.locationOfBlockChange[var5] == var4) {
                        return;
                    }
                }
                this.locationOfBlockChange[this.numBlocksToUpdate++] = var4;
            }
        }
        
        public void sendToAllPlayersWatchingChunk(final Packet p_151251_1_) {
            for (int var2 = 0; var2 < this.playersWatchingChunk.size(); ++var2) {
                final EntityPlayerMP var3 = this.playersWatchingChunk.get(var2);
                if (!var3.loadedChunks.contains(this.currentChunk)) {
                    var3.playerNetServerHandler.sendPacket(p_151251_1_);
                }
            }
        }
        
        public void onUpdate() {
            if (this.numBlocksToUpdate != 0) {
                if (this.numBlocksToUpdate == 1) {
                    final int var1 = (this.locationOfBlockChange[0] >> 12 & 0xF) + this.currentChunk.chunkXPos * 16;
                    final int var2 = this.locationOfBlockChange[0] & 0xFF;
                    final int var3 = (this.locationOfBlockChange[0] >> 8 & 0xF) + this.currentChunk.chunkZPos * 16;
                    final BlockPos var4 = new BlockPos(var1, var2, var3);
                    this.sendToAllPlayersWatchingChunk(new S23PacketBlockChange(PlayerManager.this.theWorldServer, var4));
                    if (PlayerManager.this.theWorldServer.getBlockState(var4).getBlock().hasTileEntity()) {
                        this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(var4));
                    }
                }
                else if (this.numBlocksToUpdate == 64) {
                    final int var1 = this.currentChunk.chunkXPos * 16;
                    final int var2 = this.currentChunk.chunkZPos * 16;
                    this.sendToAllPlayersWatchingChunk(new S21PacketChunkData(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.currentChunk.chunkXPos, this.currentChunk.chunkZPos), false, this.flagsYAreasToUpdate));
                    for (int var3 = 0; var3 < 16; ++var3) {
                        if ((this.flagsYAreasToUpdate & 1 << var3) != 0x0) {
                            final int var5 = var3 << 4;
                            final List var6 = PlayerManager.this.theWorldServer.func_147486_a(var1, var5, var2, var1 + 16, var5 + 16, var2 + 16);
                            for (int var7 = 0; var7 < var6.size(); ++var7) {
                                this.sendTileToAllPlayersWatchingChunk(var6.get(var7));
                            }
                        }
                    }
                }
                else {
                    this.sendToAllPlayersWatchingChunk(new S22PacketMultiBlockChange(this.numBlocksToUpdate, this.locationOfBlockChange, PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.currentChunk.chunkXPos, this.currentChunk.chunkZPos)));
                    for (int var1 = 0; var1 < this.numBlocksToUpdate; ++var1) {
                        final int var2 = (this.locationOfBlockChange[var1] >> 12 & 0xF) + this.currentChunk.chunkXPos * 16;
                        final int var3 = this.locationOfBlockChange[var1] & 0xFF;
                        final int var5 = (this.locationOfBlockChange[var1] >> 8 & 0xF) + this.currentChunk.chunkZPos * 16;
                        final BlockPos var8 = new BlockPos(var2, var3, var5);
                        if (PlayerManager.this.theWorldServer.getBlockState(var8).getBlock().hasTileEntity()) {
                            this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(var8));
                        }
                    }
                }
                this.numBlocksToUpdate = 0;
                this.flagsYAreasToUpdate = 0;
            }
        }
        
        private void sendTileToAllPlayersWatchingChunk(final TileEntity p_151252_1_) {
            if (p_151252_1_ != null) {
                final Packet var2 = p_151252_1_.getDescriptionPacket();
                if (var2 != null) {
                    this.sendToAllPlayersWatchingChunk(var2);
                }
            }
        }
    }
}
