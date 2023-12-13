package net.minecraft.network.play.server;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

public class S37PacketStatistics implements Packet {
   private Map field_148976_a;

   public Map func_148974_c() {
      return this.field_148976_a;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      int var2 = var1.readVarIntFromBuffer();
      this.field_148976_a = Maps.newHashMap();

      for(int var3 = 0; var3 < var2; ++var3) {
         StatBase var4 = StatList.getOneShotStat(var1.readStringFromBuffer(32767));
         int var5 = var1.readVarIntFromBuffer();
         if (var4 != null) {
            this.field_148976_a.put(var4, var5);
         }
      }

   }

   public S37PacketStatistics(Map var1) {
      this.field_148976_a = var1;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public S37PacketStatistics() {
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.field_148976_a.size());
      Iterator var3 = this.field_148976_a.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         var1.writeString(((StatBase)var2.getKey()).statId);
         var1.writeVarIntToBuffer((Integer)var2.getValue());
      }

   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleStatistics(this);
   }
}
