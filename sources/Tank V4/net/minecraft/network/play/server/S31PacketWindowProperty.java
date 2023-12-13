package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S31PacketWindowProperty implements Packet {
   private int varValue;
   private int varIndex;
   private int windowId;

   public int getWindowId() {
      return this.windowId;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleWindowProperty(this);
   }

   public S31PacketWindowProperty() {
   }

   public int getVarIndex() {
      return this.varIndex;
   }

   public int getVarValue() {
      return this.varValue;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.windowId = var1.readUnsignedByte();
      this.varIndex = var1.readShort();
      this.varValue = var1.readShort();
   }

   public S31PacketWindowProperty(int var1, int var2, int var3) {
      this.windowId = var1;
      this.varIndex = var2;
      this.varValue = var3;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeByte(this.windowId);
      var1.writeShort(this.varIndex);
      var1.writeShort(this.varValue);
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }
}
