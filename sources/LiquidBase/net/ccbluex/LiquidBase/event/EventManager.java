package net.ccbluex.LiquidBase.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
public class EventManager {

    private static final Map<Class<? extends Event>, List<EventMethod>> EVENT_MAP = new HashMap<>();

    public void registerListener(final EventListener eventListener) {
        for(final Method method : eventListener.getClass().getDeclaredMethods()) {
            if(method.isAnnotationPresent(EventTarget.class) && method.getParameterTypes().length == 1) {
                if(!method.isAccessible())
                    method.setAccessible(true);

                final Class<? extends Event> eventClass = (Class<? extends Event>) method.getParameterTypes()[0];

                final List<EventMethod> eventMethods = EVENT_MAP.getOrDefault(eventClass, new ArrayList<>());
                eventMethods.add(new EventMethod(eventListener, method));
                EVENT_MAP.put(eventClass, eventMethods);
            }
        }
    }

    public static void callEvent(final Event event) {
        if(event == null)
            return;

        final List<EventMethod> eventMethods = EVENT_MAP.getOrDefault(event.getClass(), null);

        if(eventMethods == null)
            return;

        eventMethods.forEach(eventMethod -> {
            try{
                eventMethod.getMethod().invoke(eventMethod.getEventListener(), event);
            }catch(IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}
