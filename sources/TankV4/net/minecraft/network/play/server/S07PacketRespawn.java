package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class S07PacketRespawn implements Packet {
   private int dimensionID;
   private WorldSettings.GameType gameType;
   private EnumDifficulty difficulty;
   private WorldType worldType;

   public WorldSettings.GameType getGameType() {
      return this.gameType;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeInt(this.dimensionID);
      var1.writeByte(this.difficulty.getDifficultyId());
      var1.writeByte(this.gameType.getID());
      var1.writeString(this.worldType.getWorldTypeName());
   }

   public EnumDifficulty getDifficulty() {
      return this.difficulty;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleRespawn(this);
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.dimensionID = var1.readInt();
      this.difficulty = EnumDifficulty.getDifficultyEnum(var1.readUnsignedByte());
      this.gameType = WorldSettings.GameType.getByID(var1.readUnsignedByte());
      this.worldType = WorldType.parseWorldType(var1.readStringFromBuffer(16));
      if (this.worldType == null) {
         this.worldType = WorldType.DEFAULT;
      }

   }

   public S07PacketRespawn(int var1, EnumDifficulty var2, WorldType var3, WorldSettings.GameType var4) {
      this.dimensionID = var1;
      this.difficulty = var2;
      this.gameType = var4;
      this.worldType = var3;
   }

   public WorldType getWorldType() {
      return this.worldType;
   }

   public int getDimensionID() {
      return this.dimensionID;
   }

   public S07PacketRespawn() {
   }
}
