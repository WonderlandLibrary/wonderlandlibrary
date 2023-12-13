package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum AudioDeviceType {
   TTV_PLAYBACK_DEVICE(0),
   TTV_RECORDER_DEVICE(1),
   TTV_PASSTHROUGH_DEVICE(2),
   TTV_DEVICE_NUM(3);

   private static Map s_Map = new HashMap();
   private int m_Value;
   private static final AudioDeviceType[] $VALUES = new AudioDeviceType[]{TTV_PLAYBACK_DEVICE, TTV_RECORDER_DEVICE, TTV_PASSTHROUGH_DEVICE, TTV_DEVICE_NUM};

   public static AudioDeviceType lookupValue(int var0) {
      AudioDeviceType var1 = (AudioDeviceType)s_Map.get(var0);
      return var1;
   }

   private AudioDeviceType(int var3) {
      this.m_Value = var3;
   }

   public int getValue() {
      return this.m_Value;
   }

   static {
      EnumSet var0 = EnumSet.allOf(AudioDeviceType.class);
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         AudioDeviceType var2 = (AudioDeviceType)var1.next();
         s_Map.put(var2.getValue(), var2);
      }

   }
}
