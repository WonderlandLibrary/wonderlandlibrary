package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum AudioEncoder {
   TTV_AUD_ENC_DEFAULT(-1),
   TTV_AUD_ENC_LAMEMP3(0),
   TTV_AUD_ENC_APPLEAAC(1);

   private static Map s_Map = new HashMap();
   private int m_Value;
   private static final AudioEncoder[] $VALUES = new AudioEncoder[]{TTV_AUD_ENC_DEFAULT, TTV_AUD_ENC_LAMEMP3, TTV_AUD_ENC_APPLEAAC};

   public static AudioEncoder lookupValue(int var0) {
      AudioEncoder var1 = (AudioEncoder)s_Map.get(var0);
      return var1;
   }

   private AudioEncoder(int var3) {
      this.m_Value = var3;
   }

   public int getValue() {
      return this.m_Value;
   }

   static {
      EnumSet var0 = EnumSet.allOf(AudioEncoder.class);
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         AudioEncoder var2 = (AudioEncoder)var1.next();
         s_Map.put(var2.getValue(), var2);
      }

   }
}
