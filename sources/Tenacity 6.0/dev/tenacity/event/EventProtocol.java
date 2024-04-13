//package dev.tenacity.event;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.util.*;
//
//public class EventProtocol<T> {
//
//    //From Pandaware
//
//    private final Map<Type, List<EventStorage<T>>> storageMap = new HashMap<>();
//    private final Map<Type, List<EventListener<T>>> callbackMap = new HashMap<>();
//
//    public void register(Object listeningObject) {
//        for (Field declaredField : listeningObject.getClass().getDeclaredFields()) {
//            try {
//                if (declaredField.getType() == EventListener.class) {
//                    boolean accessible = declaredField.isAccessible();
//                    if (!accessible) {
//                        declaredField.setAccessible(true);
//                    }
//
//                    Type type = ((ParameterizedType) declaredField.getGenericType()).getActualTypeArguments()[0];
//                    EventListener<T> callback = (EventListener<T>) declaredField.get(listeningObject);
//                    declaredField.setAccessible(accessible);
//
//                    if (storageMap.containsKey(type)) {
//                        List<EventStorage<T>> storages = storageMap.get(type);
//                        storages.add(new EventStorage<>(listeningObject, callback));
//                    } else {
//                        storageMap.put(type, new ArrayList<>(Collections.singletonList(new EventStorage<>(listeningObject, callback))));
//                    }
//                }
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        updateCallbacks();
//    }
//
//    public void unregister(Object listeningObject) {
//        for (List<EventStorage<T>> value : storageMap.values()) {
//            value.removeIf(eventCallbackStorage -> eventCallbackStorage.getOwner() == listeningObject);
//        }
//        updateCallbacks();
//    }
//
//    public void dispatch(T t) {
//        List<EventListener<T>> callbacks = callbackMap.get(t.getClass());
//        if (callbacks != null) {
//            for (EventListener<T> callback : callbacks) {
//                callback.call(t);
//            }
//        }
//    }
//
//
//
//
//    private void updateCallbacks() {
//        for (Type type : storageMap.keySet()) {
//            List<EventStorage<T>> storages = storageMap.get(type);
//            List<EventListener<T>> callbacks = new ArrayList<>(storages.size());
//            for (EventStorage<T> storage : storages) {
//                callbacks.add(storage.getCallback());
//            }
//            callbackMap.put(type, callbacks);
//        }
//    }
//
//}
