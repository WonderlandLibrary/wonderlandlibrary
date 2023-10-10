package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum EncodingCpuUsage {
   TTV_ECU_LOW(0),
   TTV_ECU_MEDIUM(1),
   TTV_ECU_HIGH(2);

   private static Map s_Map = new HashMap();
   private int m_Value;
   private static final EncodingCpuUsage[] $VALUES = new EncodingCpuUsage[]{TTV_ECU_LOW, TTV_ECU_MEDIUM, TTV_ECU_HIGH};

   public static EncodingCpuUsage lookupValue(int var0) {
      EncodingCpuUsage var1 = (EncodingCpuUsage)s_Map.get(var0);
      return var1;
   }

   private EncodingCpuUsage(int var3) {
      this.m_Value = var3;
   }

   public int getValue() {
      return this.m_Value;
   }

   static {
      EnumSet var0 = EnumSet.allOf(EncodingCpuUsage.class);
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         EncodingCpuUsage var2 = (EncodingCpuUsage)var1.next();
         s_Map.put(var2.getValue(), var2);
      }

   }
}
