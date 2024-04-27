/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 */
package net.ccbluex.liquidbounce.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.EventHook;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;

public final class EventManager {
    private final HashMap registry = new HashMap();

    public final void callEvent(Event event) {
        List list = (List)this.registry.get(event.getClass());
        if (list == null) {
            return;
        }
        List list2 = list;
        for (EventHook eventHook : list2) {
            try {
                if (!eventHook.getEventClass().handleEvents() && !eventHook.isIgnoreCondition()) continue;
                eventHook.getMethod().invoke(eventHook.getEventClass(), event);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public final void unregisterListener(Listenable listenable) {
        Object object = this.registry;
        boolean bl = false;
        Iterator iterator2 = object.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry entry;
            Map.Entry entry2 = entry = iterator2.next();
            boolean bl2 = false;
            object = (Class)entry2.getKey();
            entry2 = entry;
            bl2 = false;
            List list = (List)entry2.getValue();
            list.removeIf(new Predicate(listenable){
                final Listenable $listenable;

                public final boolean test(EventHook eventHook) {
                    return eventHook.getEventClass().equals(this.$listenable);
                }
                {
                    this.$listenable = listenable;
                }

                static {
                }

                public boolean test(Object object) {
                    return this.test((EventHook)object);
                }
            });
            ((Map)this.registry).put(object, list);
        }
    }

    public final void registerListener(Listenable listenable) {
        for (Method method : listenable.getClass().getDeclaredMethods()) {
            Class<?> clazz;
            if (!method.isAnnotationPresent(EventTarget.class) || method.getParameterTypes().length != 1) continue;
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            if (method.getParameterTypes()[0] == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<out net.ccbluex.liquidbounce.event.Event>");
            }
            EventTarget eventTarget = method.getAnnotation(EventTarget.class);
            List list = this.registry.getOrDefault(clazz, new ArrayList());
            list.add(new EventHook(listenable, method, eventTarget));
            ((Map)this.registry).put(clazz, list);
        }
    }
}

