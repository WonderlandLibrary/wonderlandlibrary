package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum StartFlags {
   None(0),
   TTV_Start_BandwidthTest(1);

   private static Map s_Map = new HashMap();
   private int m_Value;
   private static final StartFlags[] $VALUES = new StartFlags[]{None, TTV_Start_BandwidthTest};

   public static StartFlags lookupValue(int var0) {
      StartFlags var1 = (StartFlags)s_Map.get(var0);
      return var1;
   }

   private StartFlags(int var3) {
      this.m_Value = var3;
   }

   public int getValue() {
      return this.m_Value;
   }

   static {
      EnumSet var0 = EnumSet.allOf(StartFlags.class);
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         StartFlags var2 = (StartFlags)var1.next();
         s_Map.put(var2.getValue(), var2);
      }

   }
}
