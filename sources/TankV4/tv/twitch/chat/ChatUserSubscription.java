package tv.twitch.chat;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum ChatUserSubscription {
   TTV_CHAT_USERSUB_NONE(0),
   TTV_CHAT_USERSUB_SUBSCRIBER(1),
   TTV_CHAT_USERSUB_TURBO(2);

   private static Map s_Map = new HashMap();
   private int m_Value;
   private static final ChatUserSubscription[] $VALUES = new ChatUserSubscription[]{TTV_CHAT_USERSUB_NONE, TTV_CHAT_USERSUB_SUBSCRIBER, TTV_CHAT_USERSUB_TURBO};

   public static ChatUserSubscription lookupValue(int var0) {
      ChatUserSubscription var1 = (ChatUserSubscription)s_Map.get(var0);
      return var1;
   }

   private ChatUserSubscription(int var3) {
      this.m_Value = var3;
   }

   public int getValue() {
      return this.m_Value;
   }

   static {
      EnumSet var0 = EnumSet.allOf(ChatUserSubscription.class);
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         ChatUserSubscription var2 = (ChatUserSubscription)var1.next();
         s_Map.put(var2.getValue(), var2);
      }

   }
}
