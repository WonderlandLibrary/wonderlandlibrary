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
import java.util.concurrent.CopyOnWriteArrayList;
import org.jetbrains.annotations.NotNull;

public final class EventManager {
   private static final Map REGISTRY_MAP = new HashMap();

   private EventManager() {
   }

   public static void register(Object object) {
      Method[] var1 = object.getClass().getDeclaredMethods();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Method method = var1[var3];
         if (!isMethodBad(method)) {
            register(method, object);
         }
      }

   }

   public static void register(Object object, Class eventClass) {
      Method[] var2 = object.getClass().getDeclaredMethods();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method method = var2[var4];
         if (!isMethodBad(method, eventClass)) {
            register(method, object);
         }
      }

   }

   public static void unregister(Object object) {
      Iterator var1 = REGISTRY_MAP.values().iterator();

      while(var1.hasNext()) {
         List dataList = (List)var1.next();
         dataList.removeIf((data) -> {
            return data.getSource().equals(object);
         });
      }

      cleanMap(true);
   }

   public static void unregister(Object object, Class eventClass) {
      if (REGISTRY_MAP.containsKey(eventClass)) {
         ((List)REGISTRY_MAP.get(eventClass)).removeIf((data) -> {
            return data.getSource().equals(object);
         });
         cleanMap(true);
      }

   }

   private static void register(Method method, Object object) {
      Class indexClass = method.getParameterTypes()[0];
      final MethodData data = new MethodData(object, method, ((EventTarget)method.getAnnotation(EventTarget.class)).value());
      if (!data.getTarget().isAccessible()) {
         data.getTarget().setAccessible(true);
      }

      if (REGISTRY_MAP.containsKey(indexClass)) {
         if (!((List)REGISTRY_MAP.get(indexClass)).contains(data)) {
            ((List)REGISTRY_MAP.get(indexClass)).add(data);
            sortListValue(indexClass);
         }
      } else {
         REGISTRY_MAP.put(indexClass, new CopyOnWriteArrayList() {
            private static final long serialVersionUID = 666L;

            {
               this.add(data);
            }
         });
      }

   }

   public static void removeEntry(Class indexClass) {
      Iterator mapIterator = REGISTRY_MAP.entrySet().iterator();

      while(mapIterator.hasNext()) {
         if (((Class)((Map.Entry)mapIterator.next()).getKey()).equals(indexClass)) {
            mapIterator.remove();
            break;
         }
      }

   }

   public static void cleanMap(boolean onlyEmptyEntries) {
      Iterator mapIterator = REGISTRY_MAP.entrySet().iterator();

      while(true) {
         do {
            if (!mapIterator.hasNext()) {
               return;
            }
         } while(onlyEmptyEntries && !((List)((Map.Entry)mapIterator.next()).getValue()).isEmpty());

         mapIterator.remove();
      }
   }

   private static void sortListValue(Class indexClass) {
      List sortedList = new CopyOnWriteArrayList();
      byte[] var2 = Priority.VALUE_ARRAY;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         byte priority = var2[var4];
         Iterator var6 = ((List)REGISTRY_MAP.get(indexClass)).iterator();

         while(var6.hasNext()) {
            MethodData data = (MethodData)var6.next();
            if (data.getPriority() == priority) {
               sortedList.add(data);
            }
         }
      }

      REGISTRY_MAP.put(indexClass, sortedList);
   }

   private static boolean isMethodBad(Method method) {
      return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
   }

   private static boolean isMethodBad(@NotNull Method method, Class eventClass) {
      return isMethodBad(method) || !method.getParameterTypes()[0].equals(eventClass);
   }

   public static @NotNull Event call(Event event) {
      List dataList = (List)REGISTRY_MAP.get(event.getClass());
      if (dataList != null) {
         if (event instanceof EventStoppable) {
            EventStoppable stoppable = (EventStoppable)event;
            Iterator var3 = dataList.iterator();

            while(var3.hasNext()) {
               MethodData data = (MethodData)var3.next();
               invoke(data, event);
               if (stoppable.isStopped()) {
                  break;
               }
            }
         } else {
            Iterator var5 = dataList.iterator();

            while(var5.hasNext()) {
               MethodData data = (MethodData)var5.next();
               invoke(data, event);
            }
         }
      }

      return event;
   }

   private static void invoke(MethodData data, Event argument) {
      try {
         data.getTarget().invoke(data.getSource(), argument);
      } catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException var3) {
      }

   }

   private static final class MethodData {
      private final Object source;
      private final Method target;
      private final byte priority;

      public MethodData(Object source, Method target, byte priority) {
         this.source = source;
         this.target = target;
         this.priority = priority;
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
