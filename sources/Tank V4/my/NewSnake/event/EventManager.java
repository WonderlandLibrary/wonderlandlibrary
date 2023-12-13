package my.NewSnake.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class EventManager {
   private static final Map REGISTRY_MAP = new HashMap();

   private static void register(Method var0, Object var1) {
      Class var2 = var0.getParameterTypes()[0];
      MethodData var3 = new MethodData(var1, var0, ((EventTarget)var0.getAnnotation(EventTarget.class)).value());
      if (!var3.target.isAccessible()) {
         var3.target.setAccessible(true);
      }

      if (REGISTRY_MAP.containsKey(var2)) {
         if (!((FlexibleArray)REGISTRY_MAP.get(var2)).contains(var3)) {
            ((FlexibleArray)REGISTRY_MAP.get(var2)).add(var3);
            sortListValue(var2);
         }
      } else {
         REGISTRY_MAP.put(var2, new FlexibleArray(var3) {
            {
               this.add(var1);
            }
         });
      }

   }

   public static void removeEntry(Class var0) {
      Iterator var1 = REGISTRY_MAP.entrySet().iterator();

      while(var1.hasNext()) {
         if (((Class)((Entry)var1.next()).getKey()).equals(var0)) {
            var1.remove();
            break;
         }
      }

   }

   public static void unregister(Object var0) {
      Iterator var2 = REGISTRY_MAP.values().iterator();

      while(var2.hasNext()) {
         FlexibleArray var1 = (FlexibleArray)var2.next();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            MethodData var3 = (MethodData)var4.next();
            if (var3.source.equals(var0)) {
               var1.remove(var3);
            }
         }
      }

      cleanMap(true);
   }

   public static void register(Object param0) {
      // $FF: Couldn't be decompiled
   }

   public static void unregister(Object var0, Class var1) {
      if (REGISTRY_MAP.containsKey(var1)) {
         Iterator var3 = ((FlexibleArray)REGISTRY_MAP.get(var1)).iterator();

         while(var3.hasNext()) {
            MethodData var2 = (MethodData)var3.next();
            if (var2.source.equals(var0)) {
               ((FlexibleArray)REGISTRY_MAP.get(var1)).remove(var2);
            }
         }

         cleanMap(true);
      }

   }

   public static void register(Object var0, Class var1) {
      Method[] var5;
      int var4 = (var5 = var0.getClass().getDeclaredMethods()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Method var2 = var5[var3];
         if (var1 != false) {
            register(var2, var0);
         }
      }

   }

   public static void cleanMap(boolean var0) {
      Iterator var1 = REGISTRY_MAP.entrySet().iterator();

      while(true) {
         do {
            if (!var1.hasNext()) {
               return;
            }
         } while(var0 && !((FlexibleArray)((Entry)var1.next()).getValue()).isEmpty());

         var1.remove();
      }
   }

   private static void sortListValue(Class var0) {
      FlexibleArray var1 = new FlexibleArray();
      byte[] var5;
      int var4 = (var5 = Priority.VALUE_ARRAY).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         byte var2 = var5[var3];
         Iterator var7 = ((FlexibleArray)REGISTRY_MAP.get(var0)).iterator();

         while(var7.hasNext()) {
            MethodData var6 = (MethodData)var7.next();
            if (var6.priority == var2) {
               var1.add(var6);
            }
         }
      }

      REGISTRY_MAP.put(var0, var1);
   }

   public static void shutdown() {
      REGISTRY_MAP.clear();
   }

   public static FlexibleArray get(Class var0) {
      return (FlexibleArray)REGISTRY_MAP.get(var0);
   }
}
