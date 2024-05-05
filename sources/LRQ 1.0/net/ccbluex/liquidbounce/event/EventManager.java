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
    private final HashMap<Class<? extends Event>, List<EventHook>> registry = new HashMap();

    public final void registerListener(Listenable listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            Class<?> eventClass;
            if (!method.isAnnotationPresent(EventTarget.class) || method.getParameterTypes().length != 1) continue;
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            if (method.getParameterTypes()[0] == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<out net.ccbluex.liquidbounce.event.Event>");
            }
            EventTarget eventTarget = method.getAnnotation(EventTarget.class);
            List invokableEventTargets = this.registry.getOrDefault(eventClass, new ArrayList());
            invokableEventTargets.add(new EventHook(listener, method, eventTarget));
            ((Map)this.registry).put(eventClass, invokableEventTargets);
        }
    }

    /*
     * WARNING - void declaration
     */
    public final void unregisterListener(Listenable listenable) {
        Object object = this.registry;
        boolean bl = false;
        Iterator iterator = object.entrySet().iterator();
        while (iterator.hasNext()) {
            void key;
            Map.Entry entry;
            Map.Entry entry2 = entry = iterator.next();
            boolean bl2 = false;
            object = (Class)entry2.getKey();
            entry2 = entry;
            bl2 = false;
            List targets = (List)entry2.getValue();
            targets.removeIf((Predicate)new Predicate<EventHook>(listenable){
                final /* synthetic */ Listenable $listenable;

                public final boolean test(EventHook it) {
                    return it.getEventClass().equals(this.$listenable);
                }
                {
                    this.$listenable = listenable;
                }
            });
            ((Map)this.registry).put(key, targets);
        }
    }

    public final void callEvent(Event event) {
        List<EventHook> list = this.registry.get(event.getClass());
        if (list == null) {
            return;
        }
        List<EventHook> targets = list;
        for (EventHook invokableEventTarget : targets) {
            try {
                if (!invokableEventTarget.getEventClass().handleEvents() && !invokableEventTarget.isIgnoreCondition()) continue;
                invokableEventTarget.getMethod().invoke(invokableEventTarget.getEventClass(), event);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}

