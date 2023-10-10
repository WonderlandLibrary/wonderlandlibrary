package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S46PacketSetCompressionLevel implements Packet {
   private int field_179761_a;

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.field_179761_a);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleSetCompressionLevel(this);
   }

   public int func_179760_a() {
      return this.field_179761_a;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.field_179761_a = var1.readVarIntFromBuffer();
   }
}
