package org.apache.logging.log4j.core.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.util.ContextDataProvider;
import org.apache.logging.log4j.spi.ReadOnlyThreadContextMap;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.StringMap;

public class ThreadContextDataInjector {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  public static Collection<ContextDataProvider> contextDataProviders = new ConcurrentLinkedDeque<>();
  
  private static volatile List<ContextDataProvider> serviceProviders = null;
  
  private static final Lock providerLock = new ReentrantLock();
  
  public static void initServiceProviders() {
    if (serviceProviders == null) {
      providerLock.lock();
      try {
        if (serviceProviders == null)
          serviceProviders = getServiceProviders(); 
      } finally {
        providerLock.unlock();
      } 
    } 
  }
  
  private static List<ContextDataProvider> getServiceProviders() {
    List<ContextDataProvider> providers = new ArrayList<>();
    for (ClassLoader classLoader : LoaderUtil.getClassLoaders()) {
      try {
        for (ContextDataProvider provider : ServiceLoader.<ContextDataProvider>load(ContextDataProvider.class, classLoader)) {
          if (providers.stream().noneMatch(p -> p.getClass().isAssignableFrom(provider.getClass())))
            providers.add(provider); 
        } 
      } catch (Throwable ex) {
        LOGGER.debug("Unable to access Context Data Providers {}", ex.getMessage());
      } 
    } 
    return providers;
  }
  
  public static class ForDefaultThreadContextMap implements ContextDataInjector {
    private final List<ContextDataProvider> providers = ThreadContextDataInjector.getProviders();
    
    public StringMap injectContextData(List<Property> props, StringMap contextData) {
      Map<String, String> copy;
      if (this.providers.size() == 1) {
        copy = ((ContextDataProvider)this.providers.get(0)).supplyContextData();
      } else {
        copy = new HashMap<>();
        for (ContextDataProvider provider : this.providers)
          copy.putAll(provider.supplyContextData()); 
      } 
      if (props == null || props.isEmpty())
        return copy.isEmpty() ? ContextDataFactory.emptyFrozenContextData() : frozenStringMap(copy); 
      StringMap result = new JdkMapAdapterStringMap(new HashMap<>(copy));
      for (int i = 0; i < props.size(); i++) {
        Property prop = props.get(i);
        if (!copy.containsKey(prop.getName()))
          result.putValue(prop.getName(), prop.getValue()); 
      } 
      result.freeze();
      return result;
    }
    
    private static JdkMapAdapterStringMap frozenStringMap(Map<String, String> copy) {
      JdkMapAdapterStringMap result = new JdkMapAdapterStringMap(copy);
      result.freeze();
      return result;
    }
    
    public ReadOnlyStringMap rawContextData() {
      ReadOnlyThreadContextMap map = ThreadContext.getThreadContextMap();
      if (map instanceof ReadOnlyStringMap)
        return (ReadOnlyStringMap)map; 
      Map<String, String> copy = ThreadContext.getImmutableContext();
      return copy.isEmpty() ? (ReadOnlyStringMap)ContextDataFactory.emptyFrozenContextData() : (ReadOnlyStringMap)new JdkMapAdapterStringMap(copy);
    }
  }
  
  public static class ForGarbageFreeThreadContextMap implements ContextDataInjector {
    private final List<ContextDataProvider> providers = ThreadContextDataInjector.getProviders();
    
    public StringMap injectContextData(List<Property> props, StringMap reusable) {
      ThreadContextDataInjector.copyProperties(props, reusable);
      for (int i = 0; i < this.providers.size(); i++)
        reusable.putAll((ReadOnlyStringMap)((ContextDataProvider)this.providers.get(i)).supplyStringMap()); 
      return reusable;
    }
    
    public ReadOnlyStringMap rawContextData() {
      return (ReadOnlyStringMap)ThreadContext.getThreadContextMap().getReadOnlyContextData();
    }
  }
  
  public static class ForCopyOnWriteThreadContextMap implements ContextDataInjector {
    private final List<ContextDataProvider> providers = ThreadContextDataInjector.getProviders();
    
    public StringMap injectContextData(List<Property> props, StringMap ignore) {
      if (this.providers.size() == 1 && (props == null || props.isEmpty()))
        return ((ContextDataProvider)this.providers.get(0)).supplyStringMap(); 
      int count = (props == null) ? 0 : props.size();
      StringMap[] maps = new StringMap[this.providers.size()];
      for (int i = 0; i < this.providers.size(); i++) {
        maps[i] = ((ContextDataProvider)this.providers.get(i)).supplyStringMap();
        count += maps[i].size();
      } 
      StringMap result = ContextDataFactory.createContextData(count);
      ThreadContextDataInjector.copyProperties(props, result);
      for (StringMap map : maps)
        result.putAll((ReadOnlyStringMap)map); 
      return result;
    }
    
    public ReadOnlyStringMap rawContextData() {
      return (ReadOnlyStringMap)ThreadContext.getThreadContextMap().getReadOnlyContextData();
    }
  }
  
  public static void copyProperties(List<Property> properties, StringMap result) {
    if (properties != null)
      for (int i = 0; i < properties.size(); i++) {
        Property prop = properties.get(i);
        result.putValue(prop.getName(), prop.getValue());
      }  
  }
  
  private static List<ContextDataProvider> getProviders() {
    initServiceProviders();
    List<ContextDataProvider> providers = new ArrayList<>(contextDataProviders);
    if (serviceProviders != null)
      providers.addAll(serviceProviders); 
    return providers;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\impl\ThreadContextDataInjector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */