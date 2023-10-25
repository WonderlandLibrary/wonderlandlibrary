package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum AudioSampleFormat {
  TTV_ASF_PCM_S16(0);
  
  private int m_Value;
  
  private static Map<Integer, AudioSampleFormat> s_Map;
  
  static {
    s_Map = new HashMap<Integer, AudioSampleFormat>();
    EnumSet<AudioSampleFormat> enumSet = EnumSet.allOf(AudioSampleFormat.class);
    for (AudioSampleFormat audioSampleFormat : enumSet)
      s_Map.put(Integer.valueOf(audioSampleFormat.getValue()), audioSampleFormat); 
  }
  
  public static AudioSampleFormat lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  AudioSampleFormat(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\broadcast\AudioSampleFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */