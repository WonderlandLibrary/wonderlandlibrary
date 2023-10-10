package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;

public class S40PacketDisconnect implements Packet {
   private IChatComponent reason;

   public S40PacketDisconnect() {
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeChatComponent(this.reason);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.reason = var1.readChatComponent();
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleDisconnect(this);
   }

   public IChatComponent getReason() {
      return this.reason;
   }

   public S40PacketDisconnect(IChatComponent var1) {
      this.reason = var1;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }
}
