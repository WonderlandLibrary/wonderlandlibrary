package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class MapPopulator {
   public static Map populateMap(Iterable var0, Iterable var1, Map var2) {
      Iterator var3 = var1.iterator();
      Iterator var5 = var0.iterator();

      while(var5.hasNext()) {
         Object var4 = (Object)var5.next();
         var2.put(var4, var3.next());
      }

      if (var3.hasNext()) {
         throw new NoSuchElementException();
      } else {
         return var2;
      }
   }

   public static Map createMap(Iterable var0, Iterable var1) {
      return populateMap(var0, var1, Maps.newLinkedHashMap());
   }
}
