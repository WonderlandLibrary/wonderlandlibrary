package me.aquavit.liquidsense.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    private final HashMap<Class<? extends Event>, List<EventHook>> registry = new HashMap<>();

    public void registerListener(Listenable listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventTarget.class) && method.getParameterTypes().length == 1) {
                if (!method.isAccessible())
                    method.setAccessible(true);

                Class<? extends Event> eventClass = (Class<? extends Event>) method.getParameterTypes()[0];
                final EventTarget eventTarget = method.getAnnotation(EventTarget.class);

                final List<EventHook> invokableEventTargets = this.registry.getOrDefault(eventClass, new ArrayList<>());
                invokableEventTargets.add(new EventHook(listener, method, eventTarget));
                this.registry.put(eventClass, invokableEventTargets);
            }
        }
    }

    public void unregisterListener(final Listenable listenable) {
        for (final Map.Entry<Class<? extends Event>, List<EventHook>> entry : this.registry.entrySet()) {
            final List<EventHook> targets = entry.getValue();
            targets.removeIf(it -> it.getEventClass() == listenable);
            this.registry.put(entry.getKey(), targets);
        }
    }

    public final void callEvent(Event event) {
        List<EventHook> targets = this.registry.get(event.getClass());
        if (targets == null) return;
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
