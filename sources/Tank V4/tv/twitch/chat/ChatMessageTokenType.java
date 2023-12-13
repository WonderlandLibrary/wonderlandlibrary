package tv.twitch.chat;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum ChatMessageTokenType {
   TTV_CHAT_MSGTOKEN_TEXT(0),
   TTV_CHAT_MSGTOKEN_TEXTURE_IMAGE(1),
   TTV_CHAT_MSGTOKEN_URL_IMAGE(2);

   private static Map s_Map = new HashMap();
   private int m_Value;
   private static final ChatMessageTokenType[] $VALUES = new ChatMessageTokenType[]{TTV_CHAT_MSGTOKEN_TEXT, TTV_CHAT_MSGTOKEN_TEXTURE_IMAGE, TTV_CHAT_MSGTOKEN_URL_IMAGE};

   public static ChatMessageTokenType lookupValue(int var0) {
      ChatMessageTokenType var1 = (ChatMessageTokenType)s_Map.get(var0);
      return var1;
   }

   private ChatMessageTokenType(int var3) {
      this.m_Value = var3;
   }

   public int getValue() {
      return this.m_Value;
   }

   static {
      EnumSet var0 = EnumSet.allOf(ChatMessageTokenType.class);
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         ChatMessageTokenType var2 = (ChatMessageTokenType)var1.next();
         s_Map.put(var2.getValue(), var2);
      }

   }
}
