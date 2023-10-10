package net.minecraft.network.play.client;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C17PacketCustomPayload implements Packet {
   private String channel;
   private PacketBuffer data;

   public PacketBuffer getBufferData() {
      return this.data;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayServer)var1);
   }

   public void processPacket(INetHandlerPlayServer var1) {
      var1.processVanilla250Packet(this);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeString(this.channel);
      var1.writeBytes((ByteBuf)this.data);
   }

   public C17PacketCustomPayload(String var1, PacketBuffer var2) {
      this.channel = var1;
      this.data = var2;
      if (var2.writerIndex() > 32767) {
         throw new IllegalArgumentException("Payload may not be larger than 32767 bytes");
      }
   }

   public C17PacketCustomPayload() {
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.channel = var1.readStringFromBuffer(20);
      int var2 = var1.readableBytes();
      if (var2 >= 0 && var2 <= 32767) {
         this.data = new PacketBuffer(var1.readBytes(var2));
      } else {
         throw new IOException("Payload may not be larger than 32767 bytes");
      }
   }

   public String getChannelName() {
      return this.channel;
   }
}
