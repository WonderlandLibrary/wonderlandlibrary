package tv.twitch;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum VideoEncoder {
  TTV_VID_ENC_DISABLE(-2),
  TTV_VID_ENC_DEFAULT(-1),
  TTV_VID_ENC_INTEL(0),
  TTV_VID_ENC_APPLE(2),
  TTV_VID_ENC_PLUGIN(100);
  
  private static Map<Integer, VideoEncoder> s_Map;
  
  private int m_Value;
  
  static {
    s_Map = new HashMap<Integer, VideoEncoder>();
    EnumSet<VideoEncoder> enumSet = EnumSet.allOf(VideoEncoder.class);
    for (VideoEncoder videoEncoder : enumSet)
      s_Map.put(Integer.valueOf(videoEncoder.getValue()), videoEncoder); 
  }
  
  public static VideoEncoder lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  VideoEncoder(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\VideoEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */