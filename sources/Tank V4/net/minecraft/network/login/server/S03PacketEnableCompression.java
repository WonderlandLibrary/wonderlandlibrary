package net.minecraft.network.login.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class S03PacketEnableCompression implements Packet {
   private int compressionTreshold;

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerLoginClient)var1);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.compressionTreshold = var1.readVarIntFromBuffer();
   }

   public S03PacketEnableCompression() {
   }

   public int getCompressionTreshold() {
      return this.compressionTreshold;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.compressionTreshold);
   }

   public void processPacket(INetHandlerLoginClient var1) {
      var1.handleEnableCompression(this);
   }

   public S03PacketEnableCompression(int var1) {
      this.compressionTreshold = var1;
   }
}
