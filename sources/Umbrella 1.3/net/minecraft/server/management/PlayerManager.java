/*
 * Decompiled with CFR 0.150.
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
    private static final Logger field_152627_a = LogManager.getLogger();
    private final WorldServer theWorldServer;
    private final List players = Lists.newArrayList();
    private final LongHashMap playerInstances = new LongHashMap();
    private final List playerInstancesToUpdate = Lists.newArrayList();
    private final List playerInstanceList = Lists.newArrayList();
    private int playerViewRadius;
    private long previousTotalWorldTime;
    private final int[][] xzDirectionsConst;
    private static final String __OBFID = "CL_00001434";

    public PlayerManager(WorldServer p_i1176_1_) {
        int[][] arrarrn = new int[4][];
        int[] arrn = new int[2];
        arrn[0] = 1;
        arrarrn[0] = arrn;
        int[] arrn2 = new int[2];
        arrn2[1] = 1;
        arrarrn[1] = arrn2;
        int[] arrn3 = new int[2];
        arrn3[0] = -1;
        arrarrn[2] = arrn3;
        int[] arrn4 = new int[2];
        arrn4[1] = -1;
        arrarrn[3] = arrn4;
        this.xzDirectionsConst = arrarrn;
        this.theWorldServer = p_i1176_1_;
        this.func_152622_a(p_i1176_1_.func_73046_m().getConfigurationManager().getViewDistance());
    }

    public WorldServer getMinecraftServer() {
        return this.theWorldServer;
    }

    public void updatePlayerInstances() {
        WorldProvider var5;
        long var1 = this.theWorldServer.getTotalWorldTime();
        if (var1 - this.previousTotalWorldTime > 8000L) {
            this.previousTotalWorldTime = var1;
            for (int var3 = 0; var3 < this.playerInstanceList.size(); ++var3) {
                PlayerInstance var4 = (PlayerInstance)this.playerInstanceList.get(var3);
                var4.onUpdate();
                var4.processChunk();
            }
        } else {
            for (int var3 = 0; var3 < this.playerInstancesToUpdate.size(); ++var3) {
                PlayerInstance var4 = (PlayerInstance)this.playerInstancesToUpdate.get(var3);
                var4.onUpdate();
            }
        }
        this.playerInstancesToUpdate.clear();
        if (this.players.isEmpty() && !(var5 = this.theWorldServer.provider).canRespawnHere()) {
            this.theWorldServer.theChunkProviderServer.unloadAllChunks();
        }
    }

    public boolean func_152621_a(int p_152621_1_, int p_152621_2_) {
        long var3 = (long)p_152621_1_ + Integer.MAX_VALUE | (long)p_152621_2_ + Integer.MAX_VALUE << 32;
        return this.playerInstances.getValueByKey(var3) != null;
    }

    private PlayerInstance getPlayerInstance(int p_72690_1_, int p_72690_2_, boolean p_72690_3_) {
        long var4 = (long)p_72690_1_ + Integer.MAX_VALUE | (long)p_72690_2_ + Integer.MAX_VALUE << 32;
        PlayerInstance var6 = (PlayerInstance)this.playerInstances.getValueByKey(var4);
        if (var6 == null && p_72690_3_) {
            var6 = new PlayerInstance(p_72690_1_, p_72690_2_);
            this.playerInstances.add(var4, var6);
            this.playerInstanceList.add(var6);
        }
        return var6;
    }

    public void func_180244_a(BlockPos p_180244_1_) {
        int var3;
        int var2 = p_180244_1_.getX() >> 4;
        PlayerInstance var4 = this.getPlayerInstance(var2, var3 = p_180244_1_.getZ() >> 4, false);
        if (var4 != null) {
            var4.flagChunkForUpdate(p_180244_1_.getX() & 0xF, p_180244_1_.getY(), p_180244_1_.getZ() & 0xF);
        }
    }

    public void addPlayer(EntityPlayerMP p_72683_1_) {
        int var2 = (int)p_72683_1_.posX >> 4;
        int var3 = (int)p_72683_1_.posZ >> 4;
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

    public void filterChunkLoadQueue(EntityPlayerMP p_72691_1_) {
        int var10;
        ArrayList var2 = Lists.newArrayList((Iterable)p_72691_1_.loadedChunks);
        int var3 = 0;
        int var4 = this.playerViewRadius;
        int var5 = (int)p_72691_1_.posX >> 4;
        int var6 = (int)p_72691_1_.posZ >> 4;
        int var7 = 0;
        int var8 = 0;
        ChunkCoordIntPair var9 = this.getPlayerInstance(var5, var6, true).currentChunk;
        p_72691_1_.loadedChunks.clear();
        if (var2.contains(var9)) {
            p_72691_1_.loadedChunks.add(var9);
        }
        for (var10 = 1; var10 <= var4 * 2; ++var10) {
            for (int var11 = 0; var11 < 2; ++var11) {
                int[] var12 = this.xzDirectionsConst[var3++ % 4];
                for (int var13 = 0; var13 < var10; ++var13) {
                    var9 = this.getPlayerInstance(var5 + (var7 += var12[0]), var6 + (var8 += var12[1]), true).currentChunk;
                    if (!var2.contains(var9)) continue;
                    p_72691_1_.loadedChunks.add(var9);
                }
            }
        }
        var3 %= 4;
        for (var10 = 0; var10 < var4 * 2; ++var10) {
            var9 = this.getPlayerInstance(var5 + (var7 += this.xzDirectionsConst[var3][0]), var6 + (var8 += this.xzDirectionsConst[var3][1]), true).currentChunk;
            if (!var2.contains(var9)) continue;
            p_72691_1_.loadedChunks.add(var9);
        }
    }

    public void removePlayer(EntityPlayerMP p_72695_1_) {
        int var2 = (int)p_72695_1_.managedPosX >> 4;
        int var3 = (int)p_72695_1_.managedPosZ >> 4;
        for (int var4 = var2 - this.playerViewRadius; var4 <= var2 + this.playerViewRadius; ++var4) {
            for (int var5 = var3 - this.playerViewRadius; var5 <= var3 + this.playerViewRadius; ++var5) {
                PlayerInstance var6 = this.getPlayerInstance(var4, var5, false);
                if (var6 == null) continue;
                var6.removePlayer(p_72695_1_);
            }
        }
        this.players.remove(p_72695_1_);
    }

    private boolean overlaps(int p_72684_1_, int p_72684_2_, int p_72684_3_, int p_72684_4_, int p_72684_5_) {
        int var6 = p_72684_1_ - p_72684_3_;
        int var7 = p_72684_2_ - p_72684_4_;
        return var6 >= -p_72684_5_ && var6 <= p_72684_5_ ? var7 >= -p_72684_5_ && var7 <= p_72684_5_ : false;
    }

    public void updateMountedMovingPlayer(EntityPlayerMP p_72685_1_) {
        int var2 = (int)p_72685_1_.posX >> 4;
        int var3 = (int)p_72685_1_.posZ >> 4;
        double var4 = p_72685_1_.managedPosX - p_72685_1_.posX;
        double var6 = p_72685_1_.managedPosZ - p_72685_1_.posZ;
        double var8 = var4 * var4 + var6 * var6;
        if (var8 >= 64.0) {
            int var10 = (int)p_72685_1_.managedPosX >> 4;
            int var11 = (int)p_72685_1_.managedPosZ >> 4;
            int var12 = this.playerViewRadius;
            int var13 = var2 - var10;
            int var14 = var3 - var11;
            if (var13 != 0 || var14 != 0) {
                for (int var15 = var2 - var12; var15 <= var2 + var12; ++var15) {
                    for (int var16 = var3 - var12; var16 <= var3 + var12; ++var16) {
                        PlayerInstance var17;
                        if (!this.overlaps(var15, var16, var10, var11, var12)) {
                            this.getPlayerInstance(var15, var16, true).addPlayer(p_72685_1_);
                        }
                        if (this.overlaps(var15 - var13, var16 - var14, var2, var3, var12) || (var17 = this.getPlayerInstance(var15 - var13, var16 - var14, false)) == null) continue;
                        var17.removePlayer(p_72685_1_);
                    }
                }
                this.filterChunkLoadQueue(p_72685_1_);
                p_72685_1_.managedPosX = p_72685_1_.posX;
                p_72685_1_.managedPosZ = p_72685_1_.posZ;
            }
        }
    }

    public boolean isPlayerWatchingChunk(EntityPlayerMP p_72694_1_, int p_72694_2_, int p_72694_3_) {
        PlayerInstance var4 = this.getPlayerInstance(p_72694_2_, p_72694_3_, false);
        return var4 != null && var4.playersWatchingChunk.contains(p_72694_1_) && !p_72694_1_.loadedChunks.contains(var4.currentChunk);
    }

    public void func_152622_a(int p_152622_1_) {
        if ((p_152622_1_ = MathHelper.clamp_int(p_152622_1_, 3, 32)) != this.playerViewRadius) {
            int var2 = p_152622_1_ - this.playerViewRadius;
            ArrayList var3 = Lists.newArrayList((Iterable)this.players);
            for (EntityPlayerMP var5 : var3) {
                int var9;
                int var8;
                int var6 = (int)var5.posX >> 4;
                int var7 = (int)var5.posZ >> 4;
                if (var2 > 0) {
                    for (var8 = var6 - p_152622_1_; var8 <= var6 + p_152622_1_; ++var8) {
                        for (var9 = var7 - p_152622_1_; var9 <= var7 + p_152622_1_; ++var9) {
                            PlayerInstance var10 = this.getPlayerInstance(var8, var9, true);
                            if (var10.playersWatchingChunk.contains(var5)) continue;
                            var10.addPlayer(var5);
                        }
                    }
                    continue;
                }
                for (var8 = var6 - this.playerViewRadius; var8 <= var6 + this.playerViewRadius; ++var8) {
                    for (var9 = var7 - this.playerViewRadius; var9 <= var7 + this.playerViewRadius; ++var9) {
                        if (this.overlaps(var8, var9, var6, var7, p_152622_1_)) continue;
                        this.getPlayerInstance(var8, var9, true).removePlayer(var5);
                    }
                }
            }
            this.playerViewRadius = p_152622_1_;
        }
    }

    public static int getFurthestViewableBlock(int p_72686_0_) {
        return p_72686_0_ * 16 - 16;
    }

    class PlayerInstance {
        private final List playersWatchingChunk = Lists.newArrayList();
        private final ChunkCoordIntPair currentChunk;
        private short[] locationOfBlockChange = new short[64];
        private int numBlocksToUpdate;
        private int flagsYAreasToUpdate;
        private long previousWorldTime;
        private static final String __OBFID = "CL_00001435";

        public PlayerInstance(int p_i1518_2_, int p_i1518_3_) {
            this.currentChunk = new ChunkCoordIntPair(p_i1518_2_, p_i1518_3_);
            PlayerManager.this.getMinecraftServer().theChunkProviderServer.loadChunk(p_i1518_2_, p_i1518_3_);
        }

        public void addPlayer(EntityPlayerMP p_73255_1_) {
            if (this.playersWatchingChunk.contains(p_73255_1_)) {
                field_152627_a.debug("Failed to add player. {} already is in chunk {}, {}", new Object[]{p_73255_1_, this.currentChunk.chunkXPos, this.currentChunk.chunkZPos});
            } else {
                if (this.playersWatchingChunk.isEmpty()) {
                    this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
                }
                this.playersWatchingChunk.add(p_73255_1_);
                p_73255_1_.loadedChunks.add(this.currentChunk);
            }
        }

        public void removePlayer(EntityPlayerMP p_73252_1_) {
            if (this.playersWatchingChunk.contains(p_73252_1_)) {
                Chunk var2 = PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.currentChunk.chunkXPos, this.currentChunk.chunkZPos);
                if (var2.isPopulated()) {
                    p_73252_1_.playerNetServerHandler.sendPacket(new S21PacketChunkData(var2, true, 0));
                }
                this.playersWatchingChunk.remove(p_73252_1_);
                p_73252_1_.loadedChunks.remove(this.currentChunk);
                if (this.playersWatchingChunk.isEmpty()) {
                    long var3 = (long)this.currentChunk.chunkXPos + Integer.MAX_VALUE | (long)this.currentChunk.chunkZPos + Integer.MAX_VALUE << 32;
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

        private void increaseInhabitedTime(Chunk p_111196_1_) {
            p_111196_1_.setInhabitedTime(p_111196_1_.getInhabitedTime() + PlayerManager.this.theWorldServer.getTotalWorldTime() - this.previousWorldTime);
            this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
        }

        public void flagChunkForUpdate(int p_151253_1_, int p_151253_2_, int p_151253_3_) {
            if (this.numBlocksToUpdate == 0) {
                PlayerManager.this.playerInstancesToUpdate.add(this);
            }
            this.flagsYAreasToUpdate |= 1 << (p_151253_2_ >> 4);
            if (this.numBlocksToUpdate < 64) {
                short var4 = (short)(p_151253_1_ << 12 | p_151253_3_ << 8 | p_151253_2_);
                for (int var5 = 0; var5 < this.numBlocksToUpdate; ++var5) {
                    if (this.locationOfBlockChange[var5] != var4) continue;
                    return;
                }
                this.locationOfBlockChange[this.numBlocksToUpdate++] = var4;
            }
        }

        public void sendToAllPlayersWatchingChunk(Packet p_151251_1_) {
            for (int var2 = 0; var2 < this.playersWatchingChunk.size(); ++var2) {
                EntityPlayerMP var3 = (EntityPlayerMP)this.playersWatchingChunk.get(var2);
                if (var3.loadedChunks.contains(this.currentChunk)) continue;
                var3.playerNetServerHandler.sendPacket(p_151251_1_);
            }
        }

        public void onUpdate() {
            if (this.numBlocksToUpdate != 0) {
                if (this.numBlocksToUpdate == 1) {
                    int var1 = (this.locationOfBlockChange[0] >> 12 & 0xF) + this.currentChunk.chunkXPos * 16;
                    int var2 = this.locationOfBlockChange[0] & 0xFF;
                    int var3 = (this.locationOfBlockChange[0] >> 8 & 0xF) + this.currentChunk.chunkZPos * 16;
                    BlockPos var4 = new BlockPos(var1, var2, var3);
                    this.sendToAllPlayersWatchingChunk(new S23PacketBlockChange(PlayerManager.this.theWorldServer, var4));
                    if (PlayerManager.this.theWorldServer.getBlockState(var4).getBlock().hasTileEntity()) {
                        this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(var4));
                    }
                } else if (this.numBlocksToUpdate == 64) {
                    int var1 = this.currentChunk.chunkXPos * 16;
                    int var2 = this.currentChunk.chunkZPos * 16;
                    this.sendToAllPlayersWatchingChunk(new S21PacketChunkData(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.currentChunk.chunkXPos, this.currentChunk.chunkZPos), false, this.flagsYAreasToUpdate));
                    for (int var3 = 0; var3 < 16; ++var3) {
                        if ((this.flagsYAreasToUpdate & 1 << var3) == 0) continue;
                        int var7 = var3 << 4;
                        List var5 = PlayerManager.this.theWorldServer.func_147486_a(var1, var7, var2, var1 + 16, var7 + 16, var2 + 16);
                        for (int var6 = 0; var6 < var5.size(); ++var6) {
                            this.sendTileToAllPlayersWatchingChunk((TileEntity)var5.get(var6));
                        }
                    }
                } else {
                    this.sendToAllPlayersWatchingChunk(new S22PacketMultiBlockChange(this.numBlocksToUpdate, this.locationOfBlockChange, PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.currentChunk.chunkXPos, this.currentChunk.chunkZPos)));
                    for (int var1 = 0; var1 < this.numBlocksToUpdate; ++var1) {
                        int var2 = (this.locationOfBlockChange[var1] >> 12 & 0xF) + this.currentChunk.chunkXPos * 16;
                        int var3 = this.locationOfBlockChange[var1] & 0xFF;
                        int var7 = (this.locationOfBlockChange[var1] >> 8 & 0xF) + this.currentChunk.chunkZPos * 16;
                        BlockPos var8 = new BlockPos(var2, var3, var7);
                        if (!PlayerManager.this.theWorldServer.getBlockState(var8).getBlock().hasTileEntity()) continue;
                        this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(var8));
                    }
                }
                this.numBlocksToUpdate = 0;
                this.flagsYAreasToUpdate = 0;
            }
        }

        private void sendTileToAllPlayersWatchingChunk(TileEntity p_151252_1_) {
            Packet var2;
            if (p_151252_1_ != null && (var2 = p_151252_1_.getDescriptionPacket()) != null) {
                this.sendToAllPlayersWatchingChunk(var2);
            }
        }
    }
}

