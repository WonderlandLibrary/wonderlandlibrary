package tv.twitch.chat;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public enum ChatTokenizationOption {
  TTV_CHAT_TOKENIZATION_OPTION_NONE(0),
  TTV_CHAT_TOKENIZATION_OPTION_EMOTICON_URLS(1),
  TTV_CHAT_TOKENIZATION_OPTION_EMOTICON_TEXTURES(2);
  
  private static Map<Integer, ChatTokenizationOption> s_Map;
  
  private int m_Value;
  
  static {
    s_Map = new HashMap<Integer, ChatTokenizationOption>();
    EnumSet<ChatTokenizationOption> enumSet = EnumSet.allOf(ChatTokenizationOption.class);
    for (ChatTokenizationOption chatTokenizationOption : enumSet)
      s_Map.put(Integer.valueOf(chatTokenizationOption.getValue()), chatTokenizationOption); 
  }
  
  public static ChatTokenizationOption lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  public static int getNativeValue(HashSet<ChatTokenizationOption> paramHashSet) {
    if (paramHashSet == null)
      return TTV_CHAT_TOKENIZATION_OPTION_NONE.getValue(); 
    int i = TTV_CHAT_TOKENIZATION_OPTION_NONE.getValue();
    for (ChatTokenizationOption chatTokenizationOption : paramHashSet) {
      if (chatTokenizationOption != null)
        i |= chatTokenizationOption.getValue(); 
    } 
    return i;
  }
  
  ChatTokenizationOption(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\chat\ChatTokenizationOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */