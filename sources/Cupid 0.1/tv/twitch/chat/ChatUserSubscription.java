package tv.twitch.chat;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ChatUserSubscription {
  TTV_CHAT_USERSUB_NONE(0),
  TTV_CHAT_USERSUB_SUBSCRIBER(1),
  TTV_CHAT_USERSUB_TURBO(2);
  
  private static Map<Integer, ChatUserSubscription> s_Map;
  
  private int m_Value;
  
  static {
    s_Map = new HashMap<Integer, ChatUserSubscription>();
    EnumSet<ChatUserSubscription> enumSet = EnumSet.allOf(ChatUserSubscription.class);
    for (ChatUserSubscription chatUserSubscription : enumSet)
      s_Map.put(Integer.valueOf(chatUserSubscription.getValue()), chatUserSubscription); 
  }
  
  public static ChatUserSubscription lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  ChatUserSubscription(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\chat\ChatUserSubscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */