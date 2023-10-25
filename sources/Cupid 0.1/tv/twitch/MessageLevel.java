package tv.twitch;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MessageLevel {
  TTV_ML_DEBUG(0),
  TTV_ML_INFO(1),
  TTV_ML_WARNING(2),
  TTV_ML_ERROR(3),
  TTV_ML_CHAT(4),
  TTV_ML_NONE(5);
  
  private static Map<Integer, MessageLevel> s_Map;
  
  private int m_Value;
  
  static {
    s_Map = new HashMap<Integer, MessageLevel>();
    EnumSet<MessageLevel> enumSet = EnumSet.allOf(MessageLevel.class);
    for (MessageLevel messageLevel : enumSet)
      s_Map.put(Integer.valueOf(messageLevel.getValue()), messageLevel); 
  }
  
  public static MessageLevel lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  MessageLevel(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\MessageLevel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */