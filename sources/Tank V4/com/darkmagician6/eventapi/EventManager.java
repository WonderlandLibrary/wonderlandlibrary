package com.darkmagician6.eventapi;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.EventStoppable;
import com.darkmagician6.eventapi.types.Priority;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventManager {
   private static final Map REGISTRY_MAP = new HashMap();

   private EventManager() {
   }

   public static void register(Object param0) {
      // $FF: Couldn't be decompiled
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

   public static void unregister(Object var0) {
      Iterator var2 = REGISTRY_MAP.values().iterator();

      while(var2.hasNext()) {
         List var1 = (List)var2.next();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            EventManager.MethodData var3 = (EventManager.MethodData)var4.next();
            if (var3.getSource().equals(var0)) {
               var1.remove(var3);
            }
         }
      }

      cleanMap(true);
   }

   public static void unregister(Object var0, Class var1) {
      if (REGISTRY_MAP.containsKey(var1)) {
         Iterator var3 = ((List)REGISTRY_MAP.get(var1)).iterator();

         while(var3.hasNext()) {
            EventManager.MethodData var2 = (EventManager.MethodData)var3.next();
            if (var2.getSource().equals(var0)) {
               ((List)REGISTRY_MAP.get(var1)).remove(var2);
            }
         }

         cleanMap(true);
      }

   }

   private static void register(Method var0, Object var1) {
      Class var2 = var0.getParameterTypes()[0];
      EventManager.MethodData var3 = new EventManager.MethodData(var1, var0, ((EventTarget)var0.getAnnotation(EventTarget.class)).value());
      if (!var3.getTarget().isAccessible()) {
         var3.getTarget().setAccessible(true);
      }

      if (REGISTRY_MAP.containsKey(var2)) {
         if (!((List)REGISTRY_MAP.get(var2)).contains(var3)) {
            ((List)REGISTRY_MAP.get(var2)).add(var3);
            sortListValue(var2);
         }
      } else {
         REGISTRY_MAP.put(var2, new CopyOnWriteArrayList(var3) {
            private static final long serialVersionUID = 666L;

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

   public static void cleanMap(boolean var0) {
      Iterator var1 = REGISTRY_MAP.entrySet().iterator();

      while(true) {
         do {
            if (!var1.hasNext()) {
               return;
            }
         } while(var0 && !((List)((Entry)var1.next()).getValue()).isEmpty());

         var1.remove();
      }
   }

   private static void sortListValue(Class var0) {
      CopyOnWriteArrayList var1 = new CopyOnWriteArrayList();
      byte[] var5;
      int var4 = (var5 = Priority.VALUE_ARRAY).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         byte var2 = var5[var3];
         Iterator var7 = ((List)REGISTRY_MAP.get(var0)).iterator();

         while(var7.hasNext()) {
            EventManager.MethodData var6 = (EventManager.MethodData)var7.next();
            if (var6.getPriority() == var2) {
               var1.add(var6);
            }
         }
      }

      REGISTRY_MAP.put(var0, var1);
   }

   public static final Event call(Event var0) {
      List var1 = (List)REGISTRY_MAP.get(var0.getClass());
      if (var1 != null) {
         if (var0 instanceof EventStoppable) {
            EventStoppable var2 = (EventStoppable)var0;
            Iterator var4 = var1.iterator();

            while(var4.hasNext()) {
               EventManager.MethodData var3 = (EventManager.MethodData)var4.next();
               invoke(var3, var0);
               if (var2.isStopped()) {
                  break;
               }
            }
         } else {
            Iterator var6 = var1.iterator();

            while(var6.hasNext()) {
               EventManager.MethodData var5 = (EventManager.MethodData)var6.next();
               invoke(var5, var0);
            }
         }
      }

      return var0;
   }

   private static void invoke(EventManager.MethodData var0, Event var1) {
      try {
         var0.getTarget().invoke(var0.getSource(), var1);
      } catch (IllegalAccessException var3) {
      } catch (IllegalArgumentException var4) {
      } catch (InvocationTargetException var5) {
      }

   }

   private static final class MethodData {
      private final Object source;
      private final Method target;
      private final byte priority;

      public MethodData(Object var1, Method var2, byte var3) {
         this.source = var1;
         this.target = var2;
         this.priority = var3;
      }

      public Object getSource() {
         return this.source;
      }

      public Method getTarget() {
         return this.target;
      }

      public byte getPriority() {
         return this.priority;
      }
   }
}
