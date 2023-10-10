package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class S01PacketJoinGame implements Packet {
   private boolean reducedDebugInfo;
   private int entityId;
   private WorldSettings.GameType gameType;
   private WorldType worldType;
   private int maxPlayers;
   private boolean hardcoreMode;
   private EnumDifficulty difficulty;
   private int dimension;

   public int getEntityId() {
      return this.entityId;
   }

   public int getDimension() {
      return this.dimension;
   }

   public int getMaxPlayers() {
      return this.maxPlayers;
   }

   public S01PacketJoinGame(int var1, WorldSettings.GameType var2, boolean var3, int var4, EnumDifficulty var5, int var6, WorldType var7, boolean var8) {
      this.entityId = var1;
      this.dimension = var4;
      this.difficulty = var5;
      this.gameType = var2;
      this.maxPlayers = var6;
      this.hardcoreMode = var3;
      this.worldType = var7;
      this.reducedDebugInfo = var8;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = var1.readInt();
      short var2 = var1.readUnsignedByte();
      this.hardcoreMode = (var2 & 8) == 8;
      int var3 = var2 & -9;
      this.gameType = WorldSettings.GameType.getByID(var3);
      this.dimension = var1.readByte();
      this.difficulty = EnumDifficulty.getDifficultyEnum(var1.readUnsignedByte());
      this.maxPlayers = var1.readUnsignedByte();
      this.worldType = WorldType.parseWorldType(var1.readStringFromBuffer(16));
      if (this.worldType == null) {
         this.worldType = WorldType.DEFAULT;
      }

      this.reducedDebugInfo = var1.readBoolean();
   }

   public boolean isReducedDebugInfo() {
      return this.reducedDebugInfo;
   }

   public EnumDifficulty getDifficulty() {
      return this.difficulty;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public WorldType getWorldType() {
      return this.worldType;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleJoinGame(this);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeInt(this.entityId);
      int var2 = this.gameType.getID();
      if (this.hardcoreMode) {
         var2 |= 8;
      }

      var1.writeByte(var2);
      var1.writeByte(this.dimension);
      var1.writeByte(this.difficulty.getDifficultyId());
      var1.writeByte(this.maxPlayers);
      var1.writeString(this.worldType.getWorldTypeName());
      var1.writeBoolean(this.reducedDebugInfo);
   }

   public S01PacketJoinGame() {
   }

   public boolean isHardcoreMode() {
      return this.hardcoreMode;
   }

   public WorldSettings.GameType getGameType() {
      return this.gameType;
   }
}
