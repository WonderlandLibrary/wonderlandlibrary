package net.ccbluex.liquidbounce.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.EventHook;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\n\u0000\n\b\n\n\n\n!\n\n\u0000\n\n\b\n\n\b\u000020B¢J\t0\n20J\f0\n2\r0J0\n20R(\f\n\b00\n\b0\b00X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/event/EventManager;", "", "()V", "registry", "Ljava/util/HashMap;", "Ljava/lang/Class;", "Lnet/ccbluex/liquidbounce/event/Event;", "", "Lnet/ccbluex/liquidbounce/event/EventHook;", "callEvent", "", "event", "registerListener", "listener", "Lnet/ccbluex/liquidbounce/event/Listenable;", "unregisterListener", "listenable", "Pride"})
public final class EventManager {
    private final HashMap<Class<? extends Event>, List<EventHook>> registry = new HashMap();

    public final void registerListener(@NotNull Listenable listener) {
        Intrinsics.checkParameterIsNotNull(listener, "listener");
        for (Method method : listener.getClass().getDeclaredMethods()) {
            Class<?> eventClass;
            if (!method.isAnnotationPresent(EventTarget.class)) continue;
            Method method2 = method;
            Intrinsics.checkExpressionValueIsNotNull(method2, "method");
            if (method2.getParameterTypes().length != 1) continue;
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            if (method.getParameterTypes()[0] == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<out net.ccbluex.liquidbounce.event.Event>");
            }
            EventTarget eventTarget = method.getAnnotation(EventTarget.class);
            ArrayList arrayList = this.registry.getOrDefault(eventClass, new ArrayList());
            Intrinsics.checkExpressionValueIsNotNull(arrayList, "registry.getOrDefault(eventClass, ArrayList())");
            List invokableEventTargets = arrayList;
            EventTarget eventTarget2 = eventTarget;
            Intrinsics.checkExpressionValueIsNotNull(eventTarget2, "eventTarget");
            invokableEventTargets.add(new EventHook(listener, method, eventTarget2));
            ((Map)this.registry).put(eventClass, invokableEventTargets);
        }
    }

    /*
     * WARNING - void declaration
     */
    public final void unregisterListener(@NotNull Listenable listenable) {
        Intrinsics.checkParameterIsNotNull(listenable, "listenable");
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
                final Listenable $listenable;

                public final boolean test(@NotNull EventHook it) {
                    Intrinsics.checkParameterIsNotNull(it, "it");
                    return Intrinsics.areEqual(it.getEventClass(), this.$listenable);
                }
                {
                    this.$listenable = listenable;
                }
            });
            ((Map)this.registry).put(key, targets);
        }
    }

    public final void callEvent(@NotNull Event event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        List<EventHook> list = this.registry.get(event.getClass());
        if (list == null) {
            return;
        }
        Intrinsics.checkExpressionValueIsNotNull(list, "registry[event.javaClass] ?: return");
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
