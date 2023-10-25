package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum AudioDeviceType {
  TTV_PLAYBACK_DEVICE(0),
  TTV_RECORDER_DEVICE(1),
  TTV_PASSTHROUGH_DEVICE(2),
  TTV_DEVICE_NUM(3);
  
  private static Map<Integer, AudioDeviceType> s_Map;
  
  private int m_Value;
  
  static {
    s_Map = new HashMap<Integer, AudioDeviceType>();
    EnumSet<AudioDeviceType> enumSet = EnumSet.allOf(AudioDeviceType.class);
    for (AudioDeviceType audioDeviceType : enumSet)
      s_Map.put(Integer.valueOf(audioDeviceType.getValue()), audioDeviceType); 
  }
  
  public static AudioDeviceType lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  AudioDeviceType(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\broadcast\AudioDeviceType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */