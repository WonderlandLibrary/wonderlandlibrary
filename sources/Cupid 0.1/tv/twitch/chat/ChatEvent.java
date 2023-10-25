package tv.twitch.chat;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ChatEvent {
  TTV_CHAT_JOINED_CHANNEL(0),
  TTV_CHAT_LEFT_CHANNEL(1);
  
  private static Map<Integer, ChatEvent> s_Map;
  
  private int m_Value;
  
  static {
    s_Map = new HashMap<Integer, ChatEvent>();
    EnumSet<ChatEvent> enumSet = EnumSet.allOf(ChatEvent.class);
    for (ChatEvent chatEvent : enumSet)
      s_Map.put(Integer.valueOf(chatEvent.getValue()), chatEvent); 
  }
  
  public static ChatEvent lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  ChatEvent(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\chat\ChatEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */