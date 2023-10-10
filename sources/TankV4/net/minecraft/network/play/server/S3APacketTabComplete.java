package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S3APacketTabComplete implements Packet {
   private String[] matches;

   public S3APacketTabComplete() {
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public String[] func_149630_c() {
      return this.matches;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleTabComplete(this);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.matches.length);
      String[] var5;
      int var4 = (var5 = this.matches).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         String var2 = var5[var3];
         var1.writeString(var2);
      }

   }

   public S3APacketTabComplete(String[] var1) {
      this.matches = var1;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.matches = new String[var1.readVarIntFromBuffer()];

      for(int var2 = 0; var2 < this.matches.length; ++var2) {
         this.matches[var2] = var1.readStringFromBuffer(32767);
      }

   }
}
