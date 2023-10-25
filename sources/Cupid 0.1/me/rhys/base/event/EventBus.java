package me.rhys.base.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.util.container.MapContainer;

public class EventBus extends MapContainer<Class<?>, List<EventBus.EventCall>> {
  public void register(Object handle) {
    Arrays.<Method>stream(handle.getClass().getDeclaredMethods()).filter(method -> method.isAnnotationPresent((Class)EventTarget.class)).forEach(method -> registerMethod(handle, method));
  }
  
  public void unRegister(Object handle) {
    getMap().forEach((aClass, eventCalls) -> eventCalls.removeIf(()));
  }
  
  public <T> T call(Event event) {
    List<EventCall> calls = (List<EventCall>)get(event.getClass());
    if (calls != null && !calls.isEmpty())
      calls.stream().sorted(Comparator.comparingInt(value -> value.priority)).forEachOrdered(eventCall -> eventCall.call(event)); 
    return (T)event;
  }
  
  private void registerMethod(Object handle, Method method) {
    if (!method.isAccessible())
      method.setAccessible(true); 
    Class<?>[] paramTypes = method.getParameterTypes();
    if (paramTypes.length > 0) {
      Class<?> eventType = paramTypes[0];
      if (eventType != null) {
        EventTarget target = method.<EventTarget>getAnnotation(EventTarget.class);
        if (target == null)
          return; 
        EventCall eventCall = new EventCall(handle, method, target.value().ordinal());
        getMap().putIfAbsent(eventType, new ArrayList());
        List<EventCall> calls = (List<EventCall>)get(eventType);
        calls.add(eventCall);
        put(eventType, calls);
      } 
    } 
  }
  
  static class EventCall {
    Object handle;
    
    Method method;
    
    int priority;
    
    public EventCall(Object handle, Method method, int priority) {
      this.handle = handle;
      this.method = method;
      this.priority = priority;
    }
    
    void call(Event event) {
      try {
        this.method.invoke(this.handle, new Object[] { event });
      } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
        e.printStackTrace();
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\EventBus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */