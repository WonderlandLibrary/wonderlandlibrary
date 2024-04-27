/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.Event;
import net.dev.important.event.EventHook;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Listenable;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0006J\u000e\u0010\r\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u000fRF\u0010\u0003\u001a:\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0004j\u001c\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u0007`\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lnet/dev/important/event/EventManager;", "", "()V", "registry", "Ljava/util/HashMap;", "Ljava/lang/Class;", "Lnet/dev/important/event/Event;", "", "Lnet/dev/important/event/EventHook;", "Lkotlin/collections/HashMap;", "callEvent", "", "event", "registerListener", "listener", "Lnet/dev/important/event/Listenable;", "unregisterListener", "listenable", "LiquidBounce"})
public final class EventManager {
    @NotNull
    private final HashMap<Class<? extends Event>, List<EventHook>> registry = new HashMap();

    public final void registerListener(@NotNull Listenable listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        Method[] methodArray = listener.getClass().getDeclaredMethods();
        Intrinsics.checkNotNullExpressionValue(methodArray, "listener.javaClass.declaredMethods");
        Method[] methodArray2 = methodArray;
        int n = 0;
        int n2 = methodArray2.length;
        while (n < n2) {
            Class<?> eventClass;
            Method method = methodArray2[n];
            ++n;
            if (!method.isAnnotationPresent(EventTarget.class) || method.getParameterTypes().length != 1) continue;
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            if (method.getParameterTypes()[0] == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.Class<out net.dev.important.event.Event>");
            }
            EventTarget eventTarget = method.getAnnotation(EventTarget.class);
            Object object = ((Map)this.registry).get(eventClass);
            if (object == null) {
                boolean bl = false;
                object = new ArrayList();
            }
            List invokableEventTargets = (List)object;
            try {
                Intrinsics.checkNotNullExpressionValue(method, "method");
                int n3 = eventTarget.priority();
                Intrinsics.checkNotNullExpressionValue(eventTarget, "eventTarget");
                invokableEventTargets.add(new EventHook(listener, method, n3, eventTarget));
            }
            catch (Exception e) {
                e.printStackTrace();
                Intrinsics.checkNotNullExpressionValue(method, "method");
                Intrinsics.checkNotNullExpressionValue(eventTarget, "eventTarget");
                invokableEventTargets.add(new EventHook(listener, method, eventTarget));
            }
            List $this$sortBy$iv = invokableEventTargets;
            boolean $i$f$sortBy = false;
            if ($this$sortBy$iv.size() > 1) {
                CollectionsKt.sortWith($this$sortBy$iv, new Comparator(){

                    public final int compare(T a, T b) {
                        EventHook it = (EventHook)a;
                        boolean bl = false;
                        Comparable comparable = Integer.valueOf(it.getPriority());
                        it = (EventHook)b;
                        Comparable comparable2 = comparable;
                        bl = false;
                        return ComparisonsKt.compareValues(comparable2, it.getPriority());
                    }
                });
            }
            this.registry.put(eventClass, invokableEventTargets);
        }
    }

    public final void unregisterListener(@NotNull Listenable listenable) {
        Intrinsics.checkNotNullParameter(listenable, "listenable");
        for (Map.Entry entry : ((Map)this.registry).entrySet()) {
            Class key = (Class)entry.getKey();
            List targets = (List)entry.getValue();
            targets.removeIf(arg_0 -> EventManager.unregisterListener$lambda-2(listenable, arg_0));
            this.registry.put(key, targets);
        }
    }

    /*
     * WARNING - void declaration
     */
    public final void callEvent(@NotNull Event event) {
        void $this$filterTo$iv$iv;
        Intrinsics.checkNotNullParameter(event, "event");
        List<EventHook> list = this.registry.get(event.getClass());
        if (list == null) {
            return;
        }
        List<EventHook> targets = list;
        Iterable $this$filter$iv = targets;
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            EventHook it = (EventHook)element$iv$iv;
            boolean bl = false;
            if (!(it.getEventClass().handleEvents() || it.isIgnoreCondition())) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        Iterable $this$forEach$iv = (List)destination$iv$iv;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            EventHook it = (EventHook)element$iv;
            boolean bl = false;
            try {
                Object element$iv$iv;
                element$iv$iv = new Object[]{event};
                it.getMethod().invoke(it.getEventClass(), element$iv$iv);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private static final boolean unregisterListener$lambda-2(Listenable $listenable, EventHook it) {
        Intrinsics.checkNotNullParameter($listenable, "$listenable");
        Intrinsics.checkNotNullParameter(it, "it");
        return Intrinsics.areEqual(it.getEventClass(), $listenable);
    }
}

