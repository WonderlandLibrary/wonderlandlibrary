package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;

public class S41PacketServerDifficulty implements Packet {
   private EnumDifficulty difficulty;
   private boolean difficultyLocked;

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public S41PacketServerDifficulty() {
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleServerDifficulty(this);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.difficulty = EnumDifficulty.getDifficultyEnum(var1.readUnsignedByte());
   }

   public S41PacketServerDifficulty(EnumDifficulty var1, boolean var2) {
      this.difficulty = var1;
      this.difficultyLocked = var2;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeByte(this.difficulty.getDifficultyId());
   }

   public boolean isDifficultyLocked() {
      return this.difficultyLocked;
   }

   public EnumDifficulty getDifficulty() {
      return this.difficulty;
   }
}
