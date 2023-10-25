package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum AudioEncoder {
  TTV_AUD_ENC_DEFAULT(-1),
  TTV_AUD_ENC_LAMEMP3(0),
  TTV_AUD_ENC_APPLEAAC(1);
  
  private static Map<Integer, AudioEncoder> s_Map;
  
  private int m_Value;
  
  static {
    s_Map = new HashMap<Integer, AudioEncoder>();
    EnumSet<AudioEncoder> enumSet = EnumSet.allOf(AudioEncoder.class);
    for (AudioEncoder audioEncoder : enumSet)
      s_Map.put(Integer.valueOf(audioEncoder.getValue()), audioEncoder); 
  }
  
  public static AudioEncoder lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  AudioEncoder(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\broadcast\AudioEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */