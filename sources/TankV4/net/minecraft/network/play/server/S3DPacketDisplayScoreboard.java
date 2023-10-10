package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.ScoreObjective;

public class S3DPacketDisplayScoreboard implements Packet {
   private String scoreName;
   private int position;

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public int func_149371_c() {
      return this.position;
   }

   public String func_149370_d() {
      return this.scoreName;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleDisplayScoreboard(this);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.position = var1.readByte();
      this.scoreName = var1.readStringFromBuffer(16);
   }

   public S3DPacketDisplayScoreboard(int var1, ScoreObjective var2) {
      this.position = var1;
      if (var2 == null) {
         this.scoreName = "";
      } else {
         this.scoreName = var2.getName();
      }

   }

   public S3DPacketDisplayScoreboard() {
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeByte(this.position);
      var1.writeString(this.scoreName);
   }
}
