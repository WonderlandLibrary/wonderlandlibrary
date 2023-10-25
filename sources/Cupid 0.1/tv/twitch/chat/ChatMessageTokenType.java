package tv.twitch.chat;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ChatMessageTokenType {
  TTV_CHAT_MSGTOKEN_TEXT(0),
  TTV_CHAT_MSGTOKEN_TEXTURE_IMAGE(1),
  TTV_CHAT_MSGTOKEN_URL_IMAGE(2);
  
  private static Map<Integer, ChatMessageTokenType> s_Map;
  
  private int m_Value;
  
  static {
    s_Map = new HashMap<Integer, ChatMessageTokenType>();
    EnumSet<ChatMessageTokenType> enumSet = EnumSet.allOf(ChatMessageTokenType.class);
    for (ChatMessageTokenType chatMessageTokenType : enumSet)
      s_Map.put(Integer.valueOf(chatMessageTokenType.getValue()), chatMessageTokenType); 
  }
  
  public static ChatMessageTokenType lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  ChatMessageTokenType(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\chat\ChatMessageTokenType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */