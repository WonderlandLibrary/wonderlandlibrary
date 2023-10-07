package io.mxngo.echo;


import me.jinthium.straight.impl.event.render.Render2DEvent;
import me.jinthium.straight.impl.event.render.ShaderEvent;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

// modified lennox event system
public class EventDispatcher {
    private final Map<Type, List<EventCallbackStorage>> storageMap = new HashMap<>();
    private final Map<Type, List<EventCallback<Object>>> callbackMap = new HashMap<>();

    // a little more performance by using for index loop
    @SuppressWarnings("ForLoopReplaceableByForEach")
    public void subscribe(Object eventListener) {
        Field[] fields = eventListener.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            try {
                if (field.getType() == EventCallback.class && field.isAnnotationPresent(Callback.class)) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }

                    Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    EventCallback<Object> callback = (EventCallback<Object>) field.get(eventListener);

                    if (this.storageMap.containsKey(type)) {
                        this.storageMap.get(type)
                                .add(new EventCallbackStorage(eventListener, callback));
                    } else {
                        this.storageMap.put(type, new ArrayList<>(Collections.singletonList(
                                new EventCallbackStorage(eventListener, callback))));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        this.updateCallbacks();
    }

    public boolean unsubscribe(Object listener) {
        boolean found = false;

        for (List<EventCallbackStorage> value : this.storageMap.values()) {
            if (value.removeIf(storage -> storage.getEventListener() == listener)) {
                found = true;
            }
        }

        this.updateCallbacks();

        return found;
    }

    // a little more performance by using for index loop
    @SuppressWarnings("ForLoopReplaceableByForEach")
    public Object publish(Object event) {
        List<EventCallback<Object>> callbacks = this.callbackMap.get(event.getClass());

        if ((event instanceof Render2DEvent || event instanceof ShaderEvent) ||
                (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null)) {
            if (callbacks != null) {
                for (int i = 0; i < callbacks.size(); i++) {
                    callbacks.get(i).invokeEvent(event);
                }
            }
        }

        return event;
    }

    // a little more performance by using for index loop
    @SuppressWarnings("ForLoopReplaceableByForEach")
    void updateCallbacks() {
        for (Type type : this.storageMap.keySet()) {
            List<EventCallbackStorage> storages = this.storageMap.get(type);
            List<EventCallback<Object>> callbacks = new ArrayList<>(storages.size());

            for (int i = 0; i < storages.size(); i++) {
                callbacks.add(storages.get(i).getEventCallback());
            }

            this.callbackMap.put(type, callbacks);
        }
    }


    private static class EventCallbackStorage {
        private final Object eventListener;
        private final EventCallback<Object> eventCallback;

        public EventCallbackStorage(Object eventListener, EventCallback<Object> eventCallback){
            this.eventListener = eventListener;
            this.eventCallback = eventCallback;
        }

        public EventCallback<Object> getEventCallback() {
            return eventCallback;
        }

        public Object getEventListener() {
            return eventListener;
        }
    }
}