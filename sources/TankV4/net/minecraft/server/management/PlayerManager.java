package net.minecraft.server.management;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
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
   private final List playerInstancesToUpdate = Lists.newArrayList();
   private final List players = Lists.newArrayList();
   private final int[][] xzDirectionsConst = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
   private final List playerInstanceList = Lists.newArrayList();
   private int playerViewRadius;
   private static final Logger pmLogger = LogManager.getLogger();
   private final LongHashMap playerInstances = new LongHashMap();
   private final WorldServer theWorldServer;
   private long previousTotalWorldTime;

   static Logger access$0() {
      return pmLogger;
   }

   public WorldServer getWorldServer() {
      return this.theWorldServer;
   }

   public void markBlockForUpdate(BlockPos var1) {
      int var2 = var1.getX() >> 4;
      int var3 = var1.getZ() >> 4;
      PlayerManager.PlayerInstance var4 = this.getPlayerInstance(var2, var3, false);
      if (var4 != null) {
         var4.flagChunkForUpdate(var1.getX() & 15, var1.getY(), var1.getZ() & 15);
      }

   }

   public PlayerManager(WorldServer var1) {
      this.theWorldServer = var1;
      this.setPlayerViewRadius(var1.getMinecraftServer().getConfigurationManager().getViewDistance());
   }

   public void addPlayer(EntityPlayerMP var1) {
      int var2 = (int)var1.posX >> 4;
      int var3 = (int)var1.posZ >> 4;
      var1.managedPosX = var1.posX;
      var1.managedPosZ = var1.posZ;

      for(int var4 = var2 - this.playerViewRadius; var4 <= var2 + this.playerViewRadius; ++var4) {
         for(int var5 = var3 - this.playerViewRadius; var5 <= var3 + this.playerViewRadius; ++var5) {
            this.getPlayerInstance(var4, var5, true).addPlayer(var1);
         }
      }

      this.players.add(var1);
      this.filterChunkLoadQueue(var1);
   }

   private PlayerManager.PlayerInstance getPlayerInstance(int var1, int var2, boolean var3) {
      long var4 = (long)var1 + 2147483647L | (long)var2 + 2147483647L << 32;
      PlayerManager.PlayerInstance var6 = (PlayerManager.PlayerInstance)this.playerInstances.getValueByKey(var4);
      if (var6 == null && var3) {
         var6 = new PlayerManager.PlayerInstance(this, var1, var2);
         this.playerInstances.add(var4, var6);
         this.playerInstanceList.add(var6);
      }

      return var6;
   }

   public boolean isPlayerWatchingChunk(EntityPlayerMP var1, int var2, int var3) {
      PlayerManager.PlayerInstance var4 = this.getPlayerInstance(var2, var3, false);
      return var4 != null && PlayerManager.PlayerInstance.access$1(var4).contains(var1) && !var1.loadedChunks.contains(PlayerManager.PlayerInstance.access$0(var4));
   }

   public void filterChunkLoadQueue(EntityPlayerMP var1) {
      ArrayList var2 = Lists.newArrayList((Iterable)var1.loadedChunks);
      int var3 = 0;
      int var4 = this.playerViewRadius;
      int var5 = (int)var1.posX >> 4;
      int var6 = (int)var1.posZ >> 4;
      int var7 = 0;
      int var8 = 0;
      ChunkCoordIntPair var9 = PlayerManager.PlayerInstance.access$0(this.getPlayerInstance(var5, var6, true));
      var1.loadedChunks.clear();
      if (var2.contains(var9)) {
         var1.loadedChunks.add(var9);
      }

      int var10;
      for(var10 = 1; var10 <= var4 * 2; ++var10) {
         for(int var11 = 0; var11 < 2; ++var11) {
            int[] var12 = this.xzDirectionsConst[var3++ % 4];

            for(int var13 = 0; var13 < var10; ++var13) {
               var7 += var12[0];
               var8 += var12[1];
               var9 = PlayerManager.PlayerInstance.access$0(this.getPlayerInstance(var5 + var7, var6 + var8, true));
               if (var2.contains(var9)) {
                  var1.loadedChunks.add(var9);
               }
            }
         }
      }

      var3 %= 4;

      for(var10 = 0; var10 < var4 * 2; ++var10) {
         var7 += this.xzDirectionsConst[var3][0];
         var8 += this.xzDirectionsConst[var3][1];
         var9 = PlayerManager.PlayerInstance.access$0(this.getPlayerInstance(var5 + var7, var6 + var8, true));
         if (var2.contains(var9)) {
            var1.loadedChunks.add(var9);
         }
      }

   }

   static List access$3(PlayerManager var0) {
      return var0.playerInstanceList;
   }

   public void setPlayerViewRadius(int var1) {
      var1 = MathHelper.clamp_int(var1, 3, 32);
      if (var1 != this.playerViewRadius) {
         int var2 = var1 - this.playerViewRadius;
         Iterator var4 = Lists.newArrayList((Iterable)this.players).iterator();

         while(true) {
            while(var4.hasNext()) {
               EntityPlayerMP var3 = (EntityPlayerMP)var4.next();
               int var5 = (int)var3.posX >> 4;
               int var6 = (int)var3.posZ >> 4;
               int var7;
               int var8;
               if (var2 > 0) {
                  for(var7 = var5 - var1; var7 <= var5 + var1; ++var7) {
                     for(var8 = var6 - var1; var8 <= var6 + var1; ++var8) {
                        PlayerManager.PlayerInstance var9 = this.getPlayerInstance(var7, var8, true);
                        if (!PlayerManager.PlayerInstance.access$1(var9).contains(var3)) {
                           var9.addPlayer(var3);
                        }
                     }
                  }
               } else {
                  for(var7 = var5 - this.playerViewRadius; var7 <= var5 + this.playerViewRadius; ++var7) {
                     for(var8 = var6 - this.playerViewRadius; var8 <= var6 + this.playerViewRadius; ++var8) {
                        if (var6 >= var1) {
                           this.getPlayerInstance(var7, var8, true).removePlayer(var3);
                        }
                     }
                  }
               }
            }

            this.playerViewRadius = var1;
            break;
         }
      }

   }

   public void removePlayer(EntityPlayerMP var1) {
      int var2 = (int)var1.managedPosX >> 4;
      int var3 = (int)var1.managedPosZ >> 4;

      for(int var4 = var2 - this.playerViewRadius; var4 <= var2 + this.playerViewRadius; ++var4) {
         for(int var5 = var3 - this.playerViewRadius; var5 <= var3 + this.playerViewRadius; ++var5) {
            PlayerManager.PlayerInstance var6 = this.getPlayerInstance(var4, var5, false);
            if (var6 != null) {
               var6.removePlayer(var1);
            }
         }
      }

      this.players.remove(var1);
   }

   static WorldServer access$1(PlayerManager var0) {
      return var0.theWorldServer;
   }

   public boolean hasPlayerInstance(int var1, int var2) {
      long var3 = (long)var1 + 2147483647L | (long)var2 + 2147483647L << 32;
      return this.playerInstances.getValueByKey(var3) != null;
   }

   public static int getFurthestViewableBlock(int var0) {
      return var0 * 16 - 16;
   }

   public void updatePlayerInstances() {
      long var1 = this.theWorldServer.getTotalWorldTime();
      int var3;
      PlayerManager.PlayerInstance var4;
      if (var1 - this.previousTotalWorldTime > 8000L) {
         this.previousTotalWorldTime = var1;

         for(var3 = 0; var3 < this.playerInstanceList.size(); ++var3) {
            var4 = (PlayerManager.PlayerInstance)this.playerInstanceList.get(var3);
            var4.onUpdate();
            var4.processChunk();
         }
      } else {
         for(var3 = 0; var3 < this.playerInstancesToUpdate.size(); ++var3) {
            var4 = (PlayerManager.PlayerInstance)this.playerInstancesToUpdate.get(var3);
            var4.onUpdate();
         }
      }

      this.playerInstancesToUpdate.clear();
      if (this.players.isEmpty()) {
         WorldProvider var6 = this.theWorldServer.provider;
         if (!var6.canRespawnHere()) {
            this.theWorldServer.theChunkProviderServer.unloadAllChunks();
         }
      }

   }

   static LongHashMap access$2(PlayerManager var0) {
      return var0.playerInstances;
   }

   public void updateMountedMovingPlayer(EntityPlayerMP var1) {
      int var2 = (int)var1.posX >> 4;
      int var3 = (int)var1.posZ >> 4;
      double var4 = var1.managedPosX - var1.posX;
      double var6 = var1.managedPosZ - var1.posZ;
      double var8 = var4 * var4 + var6 * var6;
      if (var8 >= 64.0D) {
         int var10 = (int)var1.managedPosX >> 4;
         int var11 = (int)var1.managedPosZ >> 4;
         int var12 = this.playerViewRadius;
         int var13 = var2 - var10;
         int var14 = var3 - var11;
         if (var13 != 0 || var14 != 0) {
            for(int var15 = var2 - var12; var15 <= var2 + var12; ++var15) {
               for(int var16 = var3 - var12; var16 <= var3 + var12; ++var16) {
                  if (var11 >= var12) {
                     this.getPlayerInstance(var15, var16, true).addPlayer(var1);
                  }

                  int var10005 = var15 - var13;
                  int var10006 = var16 - var14;
                  if (var3 >= var12) {
                     PlayerManager.PlayerInstance var17 = this.getPlayerInstance(var15 - var13, var16 - var14, false);
                     if (var17 != null) {
                        var17.removePlayer(var1);
                     }
                  }
               }
            }

            this.filterChunkLoadQueue(var1);
            var1.managedPosX = var1.posX;
            var1.managedPosZ = var1.posZ;
         }
      }

   }

   static List access$4(PlayerManager var0) {
      return var0.playerInstancesToUpdate;
   }

   class PlayerInstance {
      private long previousWorldTime;
      private final List playersWatchingChunk;
      private final ChunkCoordIntPair chunkCoords;
      private int numBlocksToUpdate;
      private int flagsYAreasToUpdate;
      private short[] locationOfBlockChange;
      final PlayerManager this$0;

      public void removePlayer(EntityPlayerMP var1) {
         if (this.playersWatchingChunk.contains(var1)) {
            Chunk var2 = PlayerManager.access$1(this.this$0).getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
            if (var2.isPopulated()) {
               var1.playerNetServerHandler.sendPacket(new S21PacketChunkData(var2, true, 0));
            }

            this.playersWatchingChunk.remove(var1);
            var1.loadedChunks.remove(this.chunkCoords);
            if (this.playersWatchingChunk.isEmpty()) {
               long var3 = (long)this.chunkCoords.chunkXPos + 2147483647L | (long)this.chunkCoords.chunkZPos + 2147483647L << 32;
               this.increaseInhabitedTime(var2);
               PlayerManager.access$2(this.this$0).remove(var3);
               PlayerManager.access$3(this.this$0).remove(this);
               if (this.numBlocksToUpdate > 0) {
                  PlayerManager.access$4(this.this$0).remove(this);
               }

               this.this$0.getWorldServer().theChunkProviderServer.dropChunk(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
            }
         }

      }

      private void sendTileToAllPlayersWatchingChunk(TileEntity var1) {
         if (var1 != null) {
            Packet var2 = var1.getDescriptionPacket();
            if (var2 != null) {
               this.sendToAllPlayersWatchingChunk(var2);
            }
         }

      }

      static List access$1(PlayerManager.PlayerInstance var0) {
         return var0.playersWatchingChunk;
      }

      public void addPlayer(EntityPlayerMP var1) {
         if (this.playersWatchingChunk.contains(var1)) {
            PlayerManager.access$0().debug("Failed to add player. {} already is in chunk {}, {}", var1, this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
         } else {
            if (this.playersWatchingChunk.isEmpty()) {
               this.previousWorldTime = PlayerManager.access$1(this.this$0).getTotalWorldTime();
            }

            this.playersWatchingChunk.add(var1);
            var1.loadedChunks.add(this.chunkCoords);
         }

      }

      public void sendToAllPlayersWatchingChunk(Packet var1) {
         for(int var2 = 0; var2 < this.playersWatchingChunk.size(); ++var2) {
            EntityPlayerMP var3 = (EntityPlayerMP)this.playersWatchingChunk.get(var2);
            if (!var3.loadedChunks.contains(this.chunkCoords)) {
               var3.playerNetServerHandler.sendPacket(var1);
            }
         }

      }

      public void onUpdate() {
         if (this.numBlocksToUpdate != 0) {
            int var1;
            int var2;
            int var3;
            if (this.numBlocksToUpdate == 1) {
               var1 = (this.locationOfBlockChange[0] >> 12 & 15) + this.chunkCoords.chunkXPos * 16;
               var2 = this.locationOfBlockChange[0] & 255;
               var3 = (this.locationOfBlockChange[0] >> 8 & 15) + this.chunkCoords.chunkZPos * 16;
               BlockPos var7 = new BlockPos(var1, var2, var3);
               this.sendToAllPlayersWatchingChunk(new S23PacketBlockChange(PlayerManager.access$1(this.this$0), var7));
               if (PlayerManager.access$1(this.this$0).getBlockState(var7).getBlock().hasTileEntity()) {
                  this.sendTileToAllPlayersWatchingChunk(PlayerManager.access$1(this.this$0).getTileEntity(var7));
               }
            } else {
               int var4;
               if (this.numBlocksToUpdate != 64) {
                  this.sendToAllPlayersWatchingChunk(new S22PacketMultiBlockChange(this.numBlocksToUpdate, this.locationOfBlockChange, PlayerManager.access$1(this.this$0).getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos)));

                  for(var1 = 0; var1 < this.numBlocksToUpdate; ++var1) {
                     var2 = (this.locationOfBlockChange[var1] >> 12 & 15) + this.chunkCoords.chunkXPos * 16;
                     var3 = this.locationOfBlockChange[var1] & 255;
                     var4 = (this.locationOfBlockChange[var1] >> 8 & 15) + this.chunkCoords.chunkZPos * 16;
                     BlockPos var8 = new BlockPos(var2, var3, var4);
                     if (PlayerManager.access$1(this.this$0).getBlockState(var8).getBlock().hasTileEntity()) {
                        this.sendTileToAllPlayersWatchingChunk(PlayerManager.access$1(this.this$0).getTileEntity(var8));
                     }
                  }
               } else {
                  var1 = this.chunkCoords.chunkXPos * 16;
                  var2 = this.chunkCoords.chunkZPos * 16;
                  this.sendToAllPlayersWatchingChunk(new S21PacketChunkData(PlayerManager.access$1(this.this$0).getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos), false, this.flagsYAreasToUpdate));

                  for(var3 = 0; var3 < 16; ++var3) {
                     if ((this.flagsYAreasToUpdate & 1 << var3) != 0) {
                        var4 = var3 << 4;
                        List var5 = PlayerManager.access$1(this.this$0).getTileEntitiesIn(var1, var4, var2, var1 + 16, var4 + 16, var2 + 16);

                        for(int var6 = 0; var6 < var5.size(); ++var6) {
                           this.sendTileToAllPlayersWatchingChunk((TileEntity)var5.get(var6));
                        }
                     }
                  }
               }
            }

            this.numBlocksToUpdate = 0;
            this.flagsYAreasToUpdate = 0;
         }

      }

      public void processChunk() {
         this.increaseInhabitedTime(PlayerManager.access$1(this.this$0).getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos));
      }

      public PlayerInstance(PlayerManager var1, int var2, int var3) {
         this.this$0 = var1;
         this.playersWatchingChunk = Lists.newArrayList();
         this.locationOfBlockChange = new short[64];
         this.chunkCoords = new ChunkCoordIntPair(var2, var3);
         var1.getWorldServer().theChunkProviderServer.loadChunk(var2, var3);
      }

      public void flagChunkForUpdate(int var1, int var2, int var3) {
         if (this.numBlocksToUpdate == 0) {
            PlayerManager.access$4(this.this$0).add(this);
         }

         this.flagsYAreasToUpdate |= 1 << (var2 >> 4);
         if (this.numBlocksToUpdate < 64) {
            short var4 = (short)(var1 << 12 | var3 << 8 | var2);

            for(int var5 = 0; var5 < this.numBlocksToUpdate; ++var5) {
               if (this.locationOfBlockChange[var5] == var4) {
                  return;
               }
            }

            this.locationOfBlockChange[this.numBlocksToUpdate++] = var4;
         }

      }

      static ChunkCoordIntPair access$0(PlayerManager.PlayerInstance var0) {
         return var0.chunkCoords;
      }

      private void increaseInhabitedTime(Chunk var1) {
         var1.setInhabitedTime(var1.getInhabitedTime() + PlayerManager.access$1(this.this$0).getTotalWorldTime() - this.previousWorldTime);
         this.previousWorldTime = PlayerManager.access$1(this.this$0).getTotalWorldTime();
      }
   }
}
