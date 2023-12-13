package org.apache.commons.lang3.text;

import java.util.Map;
import java.util.Properties;

public abstract class StrLookup {
   private static final StrLookup NONE_LOOKUP = new StrLookup.MapStrLookup((Map)null);
   private static final StrLookup SYSTEM_PROPERTIES_LOOKUP;

   public static StrLookup noneLookup() {
      return NONE_LOOKUP;
   }

   public static StrLookup systemPropertiesLookup() {
      return SYSTEM_PROPERTIES_LOOKUP;
   }

   public static StrLookup mapLookup(Map var0) {
      return new StrLookup.MapStrLookup(var0);
   }

   protected StrLookup() {
   }

   public abstract String lookup(String var1);

   static {
      Object var0 = null;

      try {
         Properties var1 = System.getProperties();
         var0 = new StrLookup.MapStrLookup(var1);
      } catch (SecurityException var3) {
         var0 = NONE_LOOKUP;
      }

      SYSTEM_PROPERTIES_LOOKUP = (StrLookup)var0;
   }

   static class MapStrLookup extends StrLookup {
      private final Map map;

      MapStrLookup(Map var1) {
         this.map = var1;
      }

      public String lookup(String var1) {
         if (this.map == null) {
            return null;
         } else {
            Object var2 = this.map.get(var1);
            return var2 == null ? null : var2.toString();
         }
      }
   }
}
