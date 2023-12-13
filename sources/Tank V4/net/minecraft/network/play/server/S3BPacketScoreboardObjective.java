package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;

public class S3BPacketScoreboardObjective implements Packet {
   private IScoreObjectiveCriteria.EnumRenderType type;
   private int field_149342_c;
   private String objectiveValue;
   private String objectiveName;

   public S3BPacketScoreboardObjective(ScoreObjective var1, int var2) {
      this.objectiveName = var1.getName();
      this.objectiveValue = var1.getDisplayName();
      this.type = var1.getCriteria().getRenderType();
      this.field_149342_c = var2;
   }

   public IScoreObjectiveCriteria.EnumRenderType func_179817_d() {
      return this.type;
   }

   public int func_149338_e() {
      return this.field_149342_c;
   }

   public String func_149339_c() {
      return this.objectiveName;
   }

   public S3BPacketScoreboardObjective() {
   }

   public String func_149337_d() {
      return this.objectiveValue;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.objectiveName = var1.readStringFromBuffer(16);
      this.field_149342_c = var1.readByte();
      if (this.field_149342_c == 0 || this.field_149342_c == 2) {
         this.objectiveValue = var1.readStringFromBuffer(32);
         this.type = IScoreObjectiveCriteria.EnumRenderType.func_178795_a(var1.readStringFromBuffer(16));
      }

   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleScoreboardObjective(this);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeString(this.objectiveName);
      var1.writeByte(this.field_149342_c);
      if (this.field_149342_c == 0 || this.field_149342_c == 2) {
         var1.writeString(this.objectiveValue);
         var1.writeString(this.type.func_178796_a());
      }

   }
}
