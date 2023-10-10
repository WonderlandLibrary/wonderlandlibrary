package net.minecraft.network.handshake.client;

import java.io.IOException;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;

public class C00Handshake implements Packet {
   private EnumConnectionState requestedState;
   private int port;
   private String ip;
   private int protocolVersion;

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.protocolVersion);
      var1.writeString(this.ip);
      var1.writeShort(this.port);
      var1.writeVarIntToBuffer(this.requestedState.getId());
   }

   public C00Handshake() {
   }

   public void processPacket(INetHandlerHandshakeServer var1) {
      var1.processHandshake(this);
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerHandshakeServer)var1);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.protocolVersion = var1.readVarIntFromBuffer();
      this.ip = var1.readStringFromBuffer(255);
      this.port = var1.readUnsignedShort();
      this.requestedState = EnumConnectionState.getById(var1.readVarIntFromBuffer());
   }

   public C00Handshake(int var1, String var2, int var3, EnumConnectionState var4) {
      this.protocolVersion = var1;
      this.ip = var2;
      this.port = var3;
      this.requestedState = var4;
   }

   public EnumConnectionState getRequestedState() {
      return this.requestedState;
   }

   public int getProtocolVersion() {
      return this.protocolVersion;
   }
}
