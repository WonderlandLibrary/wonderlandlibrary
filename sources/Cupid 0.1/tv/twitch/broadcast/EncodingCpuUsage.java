package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EncodingCpuUsage {
  TTV_ECU_LOW(0),
  TTV_ECU_MEDIUM(1),
  TTV_ECU_HIGH(2);
  
  private static Map<Integer, EncodingCpuUsage> s_Map;
  
  private int m_Value;
  
  static {
    s_Map = new HashMap<Integer, EncodingCpuUsage>();
    EnumSet<EncodingCpuUsage> enumSet = EnumSet.allOf(EncodingCpuUsage.class);
    for (EncodingCpuUsage encodingCpuUsage : enumSet)
      s_Map.put(Integer.valueOf(encodingCpuUsage.getValue()), encodingCpuUsage); 
  }
  
  public static EncodingCpuUsage lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  EncodingCpuUsage(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\broadcast\EncodingCpuUsage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */