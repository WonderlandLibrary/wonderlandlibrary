package tv.twitch;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum VideoEncoder {
   TTV_VID_ENC_DISABLE(-2),
   TTV_VID_ENC_DEFAULT(-1),
   TTV_VID_ENC_INTEL(0),
   TTV_VID_ENC_APPLE(2),
   TTV_VID_ENC_PLUGIN(100);

   private static Map s_Map = new HashMap();
   private int m_Value;
   private static final VideoEncoder[] $VALUES = new VideoEncoder[]{TTV_VID_ENC_DISABLE, TTV_VID_ENC_DEFAULT, TTV_VID_ENC_INTEL, TTV_VID_ENC_APPLE, TTV_VID_ENC_PLUGIN};

   public static VideoEncoder lookupValue(int var0) {
      VideoEncoder var1 = (VideoEncoder)s_Map.get(var0);
      return var1;
   }

   private VideoEncoder(int var3) {
      this.m_Value = var3;
   }

   public int getValue() {
      return this.m_Value;
   }

   static {
      EnumSet var0 = EnumSet.allOf(VideoEncoder.class);
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         VideoEncoder var2 = (VideoEncoder)var1.next();
         s_Map.put(var2.getValue(), var2);
      }

   }
}
