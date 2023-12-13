package tv.twitch.chat;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum ChatUserMode {
   TTV_CHAT_USERMODE_VIEWER(0),
   TTV_CHAT_USERMODE_MODERATOR(1),
   TTV_CHAT_USERMODE_BROADCASTER(2),
   TTV_CHAT_USERMODE_ADMINSTRATOR(4),
   TTV_CHAT_USERMODE_STAFF(8),
   TTV_CHAT_USERMODE_BANNED(1073741824);

   private static Map s_Map = new HashMap();
   private int m_Value;
   private static final ChatUserMode[] $VALUES = new ChatUserMode[]{TTV_CHAT_USERMODE_VIEWER, TTV_CHAT_USERMODE_MODERATOR, TTV_CHAT_USERMODE_BROADCASTER, TTV_CHAT_USERMODE_ADMINSTRATOR, TTV_CHAT_USERMODE_STAFF, TTV_CHAT_USERMODE_BANNED};

   public static ChatUserMode lookupValue(int var0) {
      ChatUserMode var1 = (ChatUserMode)s_Map.get(var0);
      return var1;
   }

   private ChatUserMode(int var3) {
      this.m_Value = var3;
   }

   public int getValue() {
      return this.m_Value;
   }

   static {
      EnumSet var0 = EnumSet.allOf(ChatUserMode.class);
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         ChatUserMode var2 = (ChatUserMode)var1.next();
         s_Map.put(var2.getValue(), var2);
      }

   }
}
