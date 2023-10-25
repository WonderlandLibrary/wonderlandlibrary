package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum StartFlags {
  None(0),
  TTV_Start_BandwidthTest(1);
  
  private static Map<Integer, StartFlags> s_Map;
  
  private int m_Value;
  
  static {
    s_Map = new HashMap<Integer, StartFlags>();
    EnumSet<StartFlags> enumSet = EnumSet.allOf(StartFlags.class);
    for (StartFlags startFlags : enumSet)
      s_Map.put(Integer.valueOf(startFlags.getValue()), startFlags); 
  }
  
  public static StartFlags lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  StartFlags(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\broadcast\StartFlags.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */