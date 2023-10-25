package com.google.common.eventbus;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

class AnnotatedSubscriberFinder implements SubscriberFindingStrategy {
  private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Class<?>, ImmutableList<Method>>() {
        public ImmutableList<Method> load(Class<?> concreteClass) throws Exception {
          return AnnotatedSubscriberFinder.getAnnotatedMethodsInternal(concreteClass);
        }
      });
  
  public Multimap<Class<?>, EventSubscriber> findAllSubscribers(Object listener) {
    HashMultimap hashMultimap = HashMultimap.create();
    Class<?> clazz = listener.getClass();
    for (Method method : getAnnotatedMethods(clazz)) {
      Class<?>[] parameterTypes = method.getParameterTypes();
      Class<?> eventType = parameterTypes[0];
      EventSubscriber subscriber = makeSubscriber(listener, method);
      hashMultimap.put(eventType, subscriber);
    } 
    return (Multimap<Class<?>, EventSubscriber>)hashMultimap;
  }
  
  private static ImmutableList<Method> getAnnotatedMethods(Class<?> clazz) {
    try {
      return (ImmutableList<Method>)subscriberMethodsCache.getUnchecked(clazz);
    } catch (UncheckedExecutionException e) {
      throw Throwables.propagate(e.getCause());
    } 
  }
  
  private static final class MethodIdentifier {
    private final String name;
    
    private final List<Class<?>> parameterTypes;
    
    MethodIdentifier(Method method) {
      this.name = method.getName();
      this.parameterTypes = Arrays.asList(method.getParameterTypes());
    }
    
    public int hashCode() {
      return Objects.hashCode(new Object[] { this.name, this.parameterTypes });
    }
    
    public boolean equals(@Nullable Object o) {
      if (o instanceof MethodIdentifier) {
        MethodIdentifier ident = (MethodIdentifier)o;
        return (this.name.equals(ident.name) && this.parameterTypes.equals(ident.parameterTypes));
      } 
      return false;
    }
  }
  
  private static ImmutableList<Method> getAnnotatedMethodsInternal(Class<?> clazz) {
    Set<? extends Class<?>> supers = TypeToken.of(clazz).getTypes().rawTypes();
    Map<MethodIdentifier, Method> identifiers = Maps.newHashMap();
    for (Class<?> superClazz : supers) {
      for (Method superClazzMethod : superClazz.getMethods()) {
        if (superClazzMethod.isAnnotationPresent((Class)Subscribe.class)) {
          Class<?>[] parameterTypes = superClazzMethod.getParameterTypes();
          if (parameterTypes.length != 1)
            throw new IllegalArgumentException("Method " + superClazzMethod + " has @Subscribe annotation, but requires " + parameterTypes.length + " arguments.  Event subscriber methods must require a single argument."); 
          MethodIdentifier ident = new MethodIdentifier(superClazzMethod);
          if (!identifiers.containsKey(ident))
            identifiers.put(ident, superClazzMethod); 
        } 
      } 
    } 
    return ImmutableList.copyOf(identifiers.values());
  }
  
  private static EventSubscriber makeSubscriber(Object listener, Method method) {
    EventSubscriber wrapper;
    if (methodIsDeclaredThreadSafe(method)) {
      wrapper = new EventSubscriber(listener, method);
    } else {
      wrapper = new SynchronizedEventSubscriber(listener, method);
    } 
    return wrapper;
  }
  
  private static boolean methodIsDeclaredThreadSafe(Method method) {
    return (method.getAnnotation(AllowConcurrentEvents.class) != null);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\eventbus\AnnotatedSubscriberFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */