package tv.twitch;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum MessageLevel {
   TTV_ML_DEBUG(0),
   TTV_ML_INFO(1),
   TTV_ML_WARNING(2),
   TTV_ML_ERROR(3),
   TTV_ML_CHAT(4),
   TTV_ML_NONE(5);

   private static Map s_Map = new HashMap();
   private int m_Value;
   private static final MessageLevel[] $VALUES = new MessageLevel[]{TTV_ML_DEBUG, TTV_ML_INFO, TTV_ML_WARNING, TTV_ML_ERROR, TTV_ML_CHAT, TTV_ML_NONE};

   public static MessageLevel lookupValue(int var0) {
      MessageLevel var1 = (MessageLevel)s_Map.get(var0);
      return var1;
   }

   private MessageLevel(int var3) {
      this.m_Value = var3;
   }

   public int getValue() {
      return this.m_Value;
   }

   static {
      EnumSet var0 = EnumSet.allOf(MessageLevel.class);
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         MessageLevel var2 = (MessageLevel)var1.next();
         s_Map.put(var2.getValue(), var2);
      }

   }
}
