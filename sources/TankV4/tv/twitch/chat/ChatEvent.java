package tv.twitch.chat;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum ChatEvent {
   TTV_CHAT_JOINED_CHANNEL(0),
   TTV_CHAT_LEFT_CHANNEL(1);

   private static Map s_Map = new HashMap();
   private int m_Value;
   private static final ChatEvent[] $VALUES = new ChatEvent[]{TTV_CHAT_JOINED_CHANNEL, TTV_CHAT_LEFT_CHANNEL};

   public static ChatEvent lookupValue(int var0) {
      ChatEvent var1 = (ChatEvent)s_Map.get(var0);
      return var1;
   }

   private ChatEvent(int var3) {
      this.m_Value = var3;
   }

   public int getValue() {
      return this.m_Value;
   }

   static {
      EnumSet var0 = EnumSet.allOf(ChatEvent.class);
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         ChatEvent var2 = (ChatEvent)var1.next();
         s_Map.put(var2.getValue(), var2);
      }

   }
}
