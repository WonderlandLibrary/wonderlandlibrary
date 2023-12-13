// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event;

import exhibition.event.impl.EventKeyPress;
import exhibition.event.impl.EventMove;
import exhibition.event.impl.EventNametagRender;
import exhibition.event.impl.EventBlockBounds;
import exhibition.event.impl.EventChat;
import exhibition.event.impl.EventMotion;
import exhibition.event.impl.EventVelocity;
import exhibition.event.impl.EventPacket;
import exhibition.event.impl.EventAttack;
import exhibition.event.impl.EventScreenDisplay;
import exhibition.event.impl.EventRenderGui;
import exhibition.event.impl.EventRender3D;
import exhibition.event.impl.EventMouse;
import exhibition.event.impl.EventDeath;
import exhibition.event.impl.EventTick;
import exhibition.event.impl.EventPushBlock;
import exhibition.event.impl.EventSendPacket;
import exhibition.event.impl.EventDamageBlock;
import exhibition.event.impl.EventStep;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

public class EventSystem
{
    private static final HashMap<Event, EventSubscription> registry;
    private static final HashMap<Class, Event> instances;
    
    public static void register(final EventListener listener) {
        final List<Event> events = getEvents(listener);
        for (final Event event : events) {
            if (isEventRegistered(event)) {
                final EventSubscription subscription = EventSystem.registry.get(event);
                subscription.add(listener);
            }
            else {
                final EventSubscription subscription = new EventSubscription((T)event);
                subscription.add(listener);
                EventSystem.registry.put(event, subscription);
            }
        }
    }
    
    public static void unregister(final EventListener listener) {
        final List<Event> events = getEvents(listener);
        for (final Event event : events) {
            if (isEventRegistered(event)) {
                final EventSubscription sub = EventSystem.registry.get(event);
                sub.remove(listener);
            }
        }
    }
    
    public static Event fire(final Event event) {
        final EventSubscription subscription = EventSystem.registry.get(event);
        if (subscription != null) {
            subscription.fire(event);
        }
        return event;
    }
    
    public static Event getInstance(final Class eventClass) {
        return EventSystem.instances.get(eventClass);
    }
    
    private static List<Event> getEvents(final EventListener listener) {
        final ArrayList<Event> events = new ArrayList<Event>();
        for (final Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(RegisterEvent.class)) {
                final RegisterEvent ireg = method.getAnnotation(RegisterEvent.class);
                for (final Class eventClass : ireg.events()) {
                    events.add(getInstance(eventClass));
                }
            }
        }
        return events;
    }
    
    private static boolean isEventRegistered(final Event event) {
        return EventSystem.registry.containsKey(event);
    }
    
    static {
        registry = new HashMap<Event, EventSubscription>();
        (instances = new HashMap<Class, Event>()).put(EventStep.class, new EventStep());
        EventSystem.instances.put(EventDamageBlock.class, new EventDamageBlock());
        EventSystem.instances.put(EventSendPacket.class, new EventSendPacket());
        EventSystem.instances.put(EventPushBlock.class, new EventPushBlock());
        EventSystem.instances.put(EventTick.class, new EventTick());
        EventSystem.instances.put(EventDeath.class, new EventDeath());
        EventSystem.instances.put(EventMouse.class, new EventMouse());
        EventSystem.instances.put(EventRender3D.class, new EventRender3D());
        EventSystem.instances.put(EventRenderGui.class, new EventRenderGui());
        EventSystem.instances.put(EventScreenDisplay.class, new EventScreenDisplay());
        EventSystem.instances.put(EventAttack.class, new EventAttack());
        EventSystem.instances.put(EventPacket.class, new EventPacket());
        EventSystem.instances.put(EventVelocity.class, new EventVelocity());
        EventSystem.instances.put(EventMotion.class, new EventMotion());
        EventSystem.instances.put(EventChat.class, new EventChat());
        EventSystem.instances.put(EventBlockBounds.class, new EventBlockBounds());
        EventSystem.instances.put(EventNametagRender.class, new EventNametagRender());
        EventSystem.instances.put(EventMove.class, new EventMove());
        EventSystem.instances.put(EventKeyPress.class, new EventKeyPress());
    }
}
