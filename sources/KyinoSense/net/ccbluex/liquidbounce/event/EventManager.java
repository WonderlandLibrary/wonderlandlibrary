/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
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

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0006J\u000e\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000eJ\u000e\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u000eR(\u0010\u0003\u001a\u001c\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2={"Lnet/ccbluex/liquidbounce/event/EventManager;", "", "()V", "registry", "Ljava/util/HashMap;", "Ljava/lang/Class;", "Lnet/ccbluex/liquidbounce/event/Event;", "", "Lnet/ccbluex/liquidbounce/event/EventHook;", "callEvent", "", "event", "registerListener", "listener", "Lnet/ccbluex/liquidbounce/event/Listenable;", "unregisterListener", "listenable", "KyinoClient"})
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
        Iterator iterator2 = object.entrySet().iterator();
        while (iterator2.hasNext()) {
            void key;
            Map.Entry entry;
            Map.Entry entry2 = entry = iterator2.next();
            boolean bl2 = false;
            object = (Class)entry2.getKey();
            entry2 = entry;
            bl2 = false;
            List targets = (List)entry2.getValue();
            targets.removeIf((Predicate)new Predicate<EventHook>(listenable){
                final /* synthetic */ Listenable $listenable;

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
                invokableEventTarget.getMethod().invoke((Object)invokableEventTarget.getEventClass(), event);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}

