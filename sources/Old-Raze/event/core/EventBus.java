package markgg.event.core;

import markgg.event.Event;
import markgg.event.handler.EventExecutable;
import markgg.event.handler.EventHandler;
import markgg.event.handler.EventObject;
import markgg.event.handler.Listener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Comparator;

public class EventBus {

    private final ArrayList<EventObject> objects = new ArrayList<>();

    public void register(final Object object, final EventPriority eventPriority) {
        if (isRegistered(object)) return;
        final EventObject eventObject = new EventObject(object, eventPriority);
        for (final Method method : object.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventHandler.class)) continue;
            if (method.getParameterCount() <= 0) continue;
            eventObject.getEventExecutables().add(new EventExecutable(method));
        }
        for (final Field field : object.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(EventHandler.class)) continue;
            try {
                field.setAccessible(true);
                if (!(field.get(object) instanceof Listener)) continue;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            eventObject.getEventExecutables().add(new EventExecutable(field));
        }
        objects.add(eventObject);
        //objects.sort(Comparator.comparingInt(o1 -> o1.getEventPriority().getPriority()));
    }

    public void call(final Event event) {
        try {
            for (final EventObject eventObject : objects)
                for (final EventExecutable eventExecutable : eventObject.getEventExecutables()) {
                    if (eventExecutable.getField() != null) {
                        try {
                            eventExecutable.getField().setAccessible(true);
                            if (eventExecutable.getField().getGenericType() instanceof ParameterizedType) {
                                ParameterizedType type = (ParameterizedType) eventExecutable.getField().getGenericType();
                                if (type.getActualTypeArguments().length > 0) {
                                    if (type.getActualTypeArguments()[0] == event.getClass() || type.getActualTypeArguments()[0] == Event.class)
                                        ((Listener<Event>) eventExecutable.getField().get(eventObject.getObject())).call(event);
                                }
                            }
                        } catch (Exception ignored) {}
                    }
                    if (eventExecutable.getMethod() != null) {
                        if (isValidMethod(eventExecutable.getMethod(), event.getClass()) ||
                                isValidMethod(eventExecutable.getMethod(), Event.class)) {
                            try {
                                eventExecutable.getMethod().setAccessible(true);
                                eventExecutable.getMethod().invoke(eventObject.getObject(), event);
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
        } catch (Exception ignored) {
        }
    }

    public void register(final Object object) {
        register(object, EventPriority.DEFAULT);
    }

    public void unregister(final Object object) {
        if (!isRegistered(object)) return;
        objects.removeIf(o -> o.getObject().equals(object));
    }

    public boolean isRegistered(final Object object) {
        return objects.stream().anyMatch(o -> o.getObject().equals(object));
    }

    public boolean isValidMethod(final Method method, final Class<?> clazz) {
        for (final Class<?> parameter : method.getParameterTypes())
            if (clazz == parameter && method.getParameterCount() == 1) return true;
        return false;
    }

}