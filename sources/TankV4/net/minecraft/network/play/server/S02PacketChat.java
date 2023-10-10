package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;

public class S02PacketChat implements Packet {
   private byte type;
   private IChatComponent chatComponent;

   public S02PacketChat() {
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleChat(this);
   }

   public IChatComponent getChatComponent() {
      return this.chatComponent;
   }

   public byte getType() {
      return this.type;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.chatComponent = var1.readChatComponent();
      this.type = var1.readByte();
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public S02PacketChat(IChatComponent var1) {
      this(var1, (byte)1);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeChatComponent(this.chatComponent);
      var1.writeByte(this.type);
   }

   public boolean isChat() {
      return this.type == 1 || this.type == 2;
   }

   public S02PacketChat(IChatComponent var1, byte var2) {
      this.chatComponent = var1;
      this.type = var2;
   }
}
