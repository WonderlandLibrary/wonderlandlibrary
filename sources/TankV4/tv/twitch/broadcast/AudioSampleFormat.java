package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum AudioSampleFormat {
   TTV_ASF_PCM_S16(0);

   private static Map s_Map = new HashMap();
   private int m_Value;
   private static final AudioSampleFormat[] $VALUES = new AudioSampleFormat[]{TTV_ASF_PCM_S16};

   public static AudioSampleFormat lookupValue(int var0) {
      AudioSampleFormat var1 = (AudioSampleFormat)s_Map.get(var0);
      return var1;
   }

   private AudioSampleFormat(int var3) {
      this.m_Value = var3;
   }

   public int getValue() {
      return this.m_Value;
   }

   static {
      EnumSet var0 = EnumSet.allOf(AudioSampleFormat.class);
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         AudioSampleFormat var2 = (AudioSampleFormat)var1.next();
         s_Map.put(var2.getValue(), var2);
      }

   }
}
