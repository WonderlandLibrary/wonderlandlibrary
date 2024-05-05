package team.massacre.api.event.api.bus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.event.api.annotations.Priority;

public final class BusImpl<T> implements Bus<T> {
   private static final BusImpl.Site[] PLACEHOLDER = new BusImpl.Site[1];
   private final Map<Class<?>, List<BusImpl.Site>> map = new HashMap();

   public void register(Object subscriber) {
      Method[] var2 = subscriber.getClass().getDeclaredMethods();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method m = var2[var4];
         Handler l = (Handler)m.getAnnotation(Handler.class);
         if (l != null) {
            Class<?>[] params = m.getParameterTypes();
            if (params.length == 1) {
               Map<Class<?>, List<BusImpl.Site>> map = this.map;
               Class<?> ecs = params[0];
               BusImpl.Site cl = new BusImpl.Site(subscriber, m, l.value());
               if (map.containsKey(ecs)) {
                  List<BusImpl.Site> ss = (List)map.get(ecs);
                  ss.add(cl);
                  ss.sort(Comparator.comparingInt((site) -> {
                     return site.p;
                  }));
               } else {
                  PLACEHOLDER[0] = cl;
                  map.put(ecs, new ArrayList(Arrays.asList(PLACEHOLDER)));
               }
            }
         }
      }

   }

   public void unregister(Object subscriber) {
      Iterator var2 = this.map.values().iterator();

      while(var2.hasNext()) {
         List<BusImpl.Site> cls = (List)var2.next();
         cls.removeIf((c) -> {
            return c.s == subscriber;
         });
         cls.sort(Comparator.comparingInt((site) -> {
            return site.p;
         }));
      }

   }

   public void post(T event) {
      List<BusImpl.Site> cls = (List)this.map.get(event.getClass());
      if (cls != null) {
         int i = 0;

         for(int clsSize = cls.size(); i < clsSize; ++i) {
            try {
               BusImpl.Site cl = (BusImpl.Site)cls.get(i);
               Method m = cl.m;
               Object sub = cl.s;
               m.invoke(sub, event);
            } catch (InvocationTargetException | IndexOutOfBoundsException | IllegalAccessException var8) {
            }
         }
      }

   }

   static final class Site {
      final Object s;
      final Method m;
      final byte p;

      Site(Object s, Method m, Priority p) {
         this.s = s;
         if (!m.isAccessible()) {
            m.setAccessible(true);
         }

         this.m = m;
         this.p = (byte)p.ordinal();
      }
   }
}
