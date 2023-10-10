package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C01PacketChatMessage implements Packet {
   private String message;

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeString(this.message);
   }

   public C01PacketChatMessage(String var1) {
      if (var1.length() > 100) {
         var1 = var1.substring(0, 100);
      }

      this.message = var1;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.message = var1.readStringFromBuffer(100);
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayServer)var1);
   }

   public String getMessage() {
      return this.message;
   }

   public C01PacketChatMessage() {
   }

   public void processPacket(INetHandlerPlayServer var1) {
      var1.processChatMessage(this);
   }
}
