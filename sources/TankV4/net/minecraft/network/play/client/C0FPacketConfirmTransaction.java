package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0FPacketConfirmTransaction implements Packet {
   public boolean accepted;
   public int windowId;
   public short uid;

   public int getWindowId() {
      return this.windowId;
   }

   public void processPacket(INetHandlerPlayServer var1) {
      var1.processConfirmTransaction(this);
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayServer)var1);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = var1.readByte();
      this.uid = var1.readShort();
      this.accepted = var1.readByte() != 0;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeByte(this.windowId);
      var1.writeShort(this.uid);
      var1.writeByte(this.accepted ? 1 : 0);
   }

   public C0FPacketConfirmTransaction() {
   }

   public C0FPacketConfirmTransaction(int var1, short var2, boolean var3) {
      this.windowId = var1;
      this.uid = var2;
      this.accepted = var3;
   }

   public short getUid() {
      return this.uid;
   }
}
