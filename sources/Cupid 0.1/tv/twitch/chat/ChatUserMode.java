package tv.twitch.chat;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ChatUserMode {
  TTV_CHAT_USERMODE_VIEWER(0),
  TTV_CHAT_USERMODE_MODERATOR(1),
  TTV_CHAT_USERMODE_BROADCASTER(2),
  TTV_CHAT_USERMODE_ADMINSTRATOR(4),
  TTV_CHAT_USERMODE_STAFF(8),
  TTV_CHAT_USERMODE_BANNED(1073741824);
  
  private static Map<Integer, ChatUserMode> s_Map;
  
  private int m_Value;
  
  static {
    s_Map = new HashMap<Integer, ChatUserMode>();
    EnumSet<ChatUserMode> enumSet = EnumSet.allOf(ChatUserMode.class);
    for (ChatUserMode chatUserMode : enumSet)
      s_Map.put(Integer.valueOf(chatUserMode.getValue()), chatUserMode); 
  }
  
  public static ChatUserMode lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  ChatUserMode(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\chat\ChatUserMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */