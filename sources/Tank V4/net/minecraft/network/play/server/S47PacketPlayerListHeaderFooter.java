package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;

public class S47PacketPlayerListHeaderFooter implements Packet {
   private IChatComponent footer;
   private IChatComponent header;

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.header = var1.readChatComponent();
      this.footer = var1.readChatComponent();
   }

   public S47PacketPlayerListHeaderFooter() {
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeChatComponent(this.header);
      var1.writeChatComponent(this.footer);
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public S47PacketPlayerListHeaderFooter(IChatComponent var1) {
      this.header = var1;
   }

   public IChatComponent getHeader() {
      return this.header;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handlePlayerListHeaderFooter(this);
   }

   public IChatComponent getFooter() {
      return this.footer;
   }
}
