package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum StatType {
   TTV_ST_RTMPSTATE(0),
   TTV_ST_RTMPDATASENT(1);

   private static Map s_Map = new HashMap();
   private int m_Value;
   private static final StatType[] $VALUES = new StatType[]{TTV_ST_RTMPSTATE, TTV_ST_RTMPDATASENT};

   public static StatType lookupValue(int var0) {
      StatType var1 = (StatType)s_Map.get(var0);
      return var1;
   }

   private StatType(int var3) {
      this.m_Value = var3;
   }

   public int getValue() {
      return this.m_Value;
   }

   static {
      EnumSet var0 = EnumSet.allOf(StatType.class);
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         StatType var2 = (StatType)var1.next();
         s_Map.put(var2.getValue(), var2);
      }

   }
}
