package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public enum AuthFlag {
   TTV_AuthOption_None(0),
   TTV_AuthOption_Broadcast(1),
   TTV_AuthOption_Chat(2);

   private static Map s_Map = new HashMap();
   private int m_Value;
   private static final AuthFlag[] $VALUES = new AuthFlag[]{TTV_AuthOption_None, TTV_AuthOption_Broadcast, TTV_AuthOption_Chat};

   public static AuthFlag lookupValue(int var0) {
      AuthFlag var1 = (AuthFlag)s_Map.get(var0);
      return var1;
   }

   public static int getNativeValue(HashSet var0) {
      if (var0 == null) {
         return TTV_AuthOption_None.getValue();
      } else {
         int var1 = 0;
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            AuthFlag var3 = (AuthFlag)var2.next();
            if (var3 != null) {
               var1 |= var3.getValue();
            }
         }

         return var1;
      }
   }

   private AuthFlag(int var3) {
      this.m_Value = var3;
   }

   public int getValue() {
      return this.m_Value;
   }

   static {
      EnumSet var0 = EnumSet.allOf(AuthFlag.class);
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         AuthFlag var2 = (AuthFlag)var1.next();
         s_Map.put(var2.getValue(), var2);
      }

   }
}
