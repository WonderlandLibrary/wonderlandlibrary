package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S00PacketKeepAlive implements Packet {
   private int id;

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.id);
   }

   public int func_149134_c() {
      return this.id;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleKeepAlive(this);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.id = var1.readVarIntFromBuffer();
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public S00PacketKeepAlive() {
   }

   public S00PacketKeepAlive(int var1) {
      this.id = var1;
   }
}
