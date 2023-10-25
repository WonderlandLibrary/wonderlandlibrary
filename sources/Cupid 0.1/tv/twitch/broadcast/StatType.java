package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum StatType {
  TTV_ST_RTMPSTATE(0),
  TTV_ST_RTMPDATASENT(1);
  
  private static Map<Integer, StatType> s_Map;
  
  private int m_Value;
  
  static {
    s_Map = new HashMap<Integer, StatType>();
    EnumSet<StatType> enumSet = EnumSet.allOf(StatType.class);
    for (StatType statType : enumSet)
      s_Map.put(Integer.valueOf(statType.getValue()), statType); 
  }
  
  public static StatType lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  StatType(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\broadcast\StatType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */