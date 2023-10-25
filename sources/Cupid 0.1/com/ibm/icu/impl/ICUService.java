package com.ibm.icu.impl;

import com.ibm.icu.util.ULocale;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class ICUService extends ICUNotifier {
  protected final String name;
  
  public ICUService() {
    this.name = "";
  }
  
  private static final boolean DEBUG = ICUDebug.enabled("service");
  
  public ICUService(String name) {
    this.name = name;
  }
  
  private final ICURWLock factoryLock = new ICURWLock();
  
  private final List<Factory> factories = new ArrayList<Factory>();
  
  private int defaultSize = 0;
  
  private SoftReference<Map<String, CacheEntry>> cacheref;
  
  private SoftReference<Map<String, Factory>> idref;
  
  private LocaleRef dnref;
  
  public static class Key {
    private final String id;
    
    public Key(String id) {
      this.id = id;
    }
    
    public final String id() {
      return this.id;
    }
    
    public String canonicalID() {
      return this.id;
    }
    
    public String currentID() {
      return canonicalID();
    }
    
    public String currentDescriptor() {
      return "/" + currentID();
    }
    
    public boolean fallback() {
      return false;
    }
    
    public boolean isFallbackOf(String idToCheck) {
      return canonicalID().equals(idToCheck);
    }
  }
  
  public static interface Factory {
    Object create(ICUService.Key param1Key, ICUService param1ICUService);
    
    void updateVisibleIDs(Map<String, Factory> param1Map);
    
    String getDisplayName(String param1String, ULocale param1ULocale);
  }
  
  public static class SimpleFactory implements Factory {
    protected Object instance;
    
    protected String id;
    
    protected boolean visible;
    
    public SimpleFactory(Object instance, String id) {
      this(instance, id, true);
    }
    
    public SimpleFactory(Object instance, String id, boolean visible) {
      if (instance == null || id == null)
        throw new IllegalArgumentException("Instance or id is null"); 
      this.instance = instance;
      this.id = id;
      this.visible = visible;
    }
    
    public Object create(ICUService.Key key, ICUService service) {
      if (this.id.equals(key.currentID()))
        return this.instance; 
      return null;
    }
    
    public void updateVisibleIDs(Map<String, ICUService.Factory> result) {
      if (this.visible) {
        result.put(this.id, this);
      } else {
        result.remove(this.id);
      } 
    }
    
    public String getDisplayName(String identifier, ULocale locale) {
      return (this.visible && this.id.equals(identifier)) ? identifier : null;
    }
    
    public String toString() {
      StringBuilder buf = new StringBuilder(super.toString());
      buf.append(", id: ");
      buf.append(this.id);
      buf.append(", visible: ");
      buf.append(this.visible);
      return buf.toString();
    }
  }
  
  public Object get(String descriptor) {
    return getKey(createKey(descriptor), null);
  }
  
  public Object get(String descriptor, String[] actualReturn) {
    if (descriptor == null)
      throw new NullPointerException("descriptor must not be null"); 
    return getKey(createKey(descriptor), actualReturn);
  }
  
  public Object getKey(Key key) {
    return getKey(key, null);
  }
  
  public Object getKey(Key key, String[] actualReturn) {
    return getKey(key, actualReturn, null);
  }
  
  public Object getKey(Key key, String[] actualReturn, Factory factory) {
    if (this.factories.size() == 0)
      return handleDefault(key, actualReturn); 
    if (DEBUG)
      System.out.println("Service: " + this.name + " key: " + key.canonicalID()); 
    CacheEntry result = null;
    if (key != null)
      try {
        this.factoryLock.acquireRead();
        Map<String, CacheEntry> cache = null;
        SoftReference<Map<String, CacheEntry>> cref = this.cacheref;
        if (cref != null) {
          if (DEBUG)
            System.out.println("Service " + this.name + " ref exists"); 
          cache = cref.get();
        } 
        if (cache == null) {
          if (DEBUG)
            System.out.println("Service " + this.name + " cache was empty"); 
          cache = Collections.synchronizedMap(new HashMap<String, CacheEntry>());
          cref = new SoftReference<Map<String, CacheEntry>>(cache);
        } 
        String currentDescriptor = null;
        ArrayList<String> cacheDescriptorList = null;
        boolean putInCache = false;
        int NDebug = 0;
        int startIndex = 0;
        int limit = this.factories.size();
        boolean cacheResult = true;
        if (factory != null) {
          for (int i = 0; i < limit; i++) {
            if (factory == this.factories.get(i)) {
              startIndex = i + 1;
              break;
            } 
          } 
          if (startIndex == 0)
            throw new IllegalStateException("Factory " + factory + "not registered with service: " + this); 
          cacheResult = false;
        } 
        do {
          currentDescriptor = key.currentDescriptor();
          if (DEBUG)
            System.out.println(this.name + "[" + NDebug++ + "] looking for: " + currentDescriptor); 
          result = cache.get(currentDescriptor);
          if (result != null) {
            if (DEBUG)
              System.out.println(this.name + " found with descriptor: " + currentDescriptor); 
            break;
          } 
          if (DEBUG)
            System.out.println("did not find: " + currentDescriptor + " in cache"); 
          putInCache = cacheResult;
          int index = startIndex;
          while (index < limit) {
            Factory f = this.factories.get(index++);
            if (DEBUG)
              System.out.println("trying factory[" + (index - 1) + "] " + f.toString()); 
            Object service = f.create(key, this);
            if (service != null) {
              result = new CacheEntry(currentDescriptor, service);
              if (DEBUG)
                System.out.println(this.name + " factory supported: " + currentDescriptor + ", caching"); 
              break;
            } 
            if (DEBUG)
              System.out.println("factory did not support: " + currentDescriptor); 
          } 
          if (cacheDescriptorList == null)
            cacheDescriptorList = new ArrayList<String>(5); 
          cacheDescriptorList.add(currentDescriptor);
        } while (key.fallback());
        if (result != null) {
          if (putInCache) {
            if (DEBUG)
              System.out.println("caching '" + result.actualDescriptor + "'"); 
            cache.put(result.actualDescriptor, result);
            if (cacheDescriptorList != null)
              for (String desc : cacheDescriptorList) {
                if (DEBUG)
                  System.out.println(this.name + " adding descriptor: '" + desc + "' for actual: '" + result.actualDescriptor + "'"); 
                cache.put(desc, result);
              }  
            this.cacheref = cref;
          } 
          if (actualReturn != null)
            if (result.actualDescriptor.indexOf("/") == 0) {
              actualReturn[0] = result.actualDescriptor.substring(1);
            } else {
              actualReturn[0] = result.actualDescriptor;
            }  
          if (DEBUG)
            System.out.println("found in service: " + this.name); 
          return result.service;
        } 
        this.factoryLock.releaseRead();
      } finally {
        this.factoryLock.releaseRead();
      }  
    if (DEBUG)
      System.out.println("not found in service: " + this.name); 
    return handleDefault(key, actualReturn);
  }
  
  private static final class CacheEntry {
    final String actualDescriptor;
    
    final Object service;
    
    CacheEntry(String actualDescriptor, Object service) {
      this.actualDescriptor = actualDescriptor;
      this.service = service;
    }
  }
  
  protected Object handleDefault(Key key, String[] actualIDReturn) {
    return null;
  }
  
  public Set<String> getVisibleIDs() {
    return getVisibleIDs(null);
  }
  
  public Set<String> getVisibleIDs(String matchID) {
    Set<String> result = getVisibleIDMap().keySet();
    Key fallbackKey = createKey(matchID);
    if (fallbackKey != null) {
      Set<String> temp = new HashSet<String>(result.size());
      for (String id : result) {
        if (fallbackKey.isFallbackOf(id))
          temp.add(id); 
      } 
      result = temp;
    } 
    return result;
  }
  
  private Map<String, Factory> getVisibleIDMap() {
    Map<String, Factory> idcache = null;
    SoftReference<Map<String, Factory>> ref = this.idref;
    if (ref != null)
      idcache = ref.get(); 
    while (idcache == null) {
      synchronized (this) {
        if (ref == this.idref || this.idref == null) {
          try {
            this.factoryLock.acquireRead();
            idcache = new HashMap<String, Factory>();
            ListIterator<Factory> lIter = this.factories.listIterator(this.factories.size());
            while (lIter.hasPrevious()) {
              Factory f = lIter.previous();
              f.updateVisibleIDs(idcache);
            } 
            idcache = Collections.unmodifiableMap(idcache);
            this.idref = new SoftReference<Map<String, Factory>>(idcache);
          } finally {
            this.factoryLock.releaseRead();
          } 
        } else {
          ref = this.idref;
          idcache = ref.get();
        } 
      } 
    } 
    return idcache;
  }
  
  public String getDisplayName(String id) {
    return getDisplayName(id, ULocale.getDefault(ULocale.Category.DISPLAY));
  }
  
  public String getDisplayName(String id, ULocale locale) {
    Map<String, Factory> m = getVisibleIDMap();
    Factory f = m.get(id);
    if (f != null)
      return f.getDisplayName(id, locale); 
    Key key = createKey(id);
    while (key.fallback()) {
      f = m.get(key.currentID());
      if (f != null)
        return f.getDisplayName(id, locale); 
    } 
    return null;
  }
  
  public SortedMap<String, String> getDisplayNames() {
    ULocale locale = ULocale.getDefault(ULocale.Category.DISPLAY);
    return getDisplayNames(locale, null, null);
  }
  
  public SortedMap<String, String> getDisplayNames(ULocale locale) {
    return getDisplayNames(locale, null, null);
  }
  
  public SortedMap<String, String> getDisplayNames(ULocale locale, Comparator<Object> com) {
    return getDisplayNames(locale, com, null);
  }
  
  public SortedMap<String, String> getDisplayNames(ULocale locale, String matchID) {
    return getDisplayNames(locale, null, matchID);
  }
  
  public SortedMap<String, String> getDisplayNames(ULocale locale, Comparator<Object> com, String matchID) {
    SortedMap<String, String> dncache = null;
    LocaleRef ref = this.dnref;
    if (ref != null)
      dncache = ref.get(locale, com); 
    while (dncache == null) {
      synchronized (this) {
        if (ref == this.dnref || this.dnref == null) {
          dncache = (SortedMap)new TreeMap<Object, String>(com);
          Map<String, Factory> m = getVisibleIDMap();
          Iterator<Map.Entry<String, Factory>> ei = m.entrySet().iterator();
          while (ei.hasNext()) {
            Map.Entry<String, Factory> e = ei.next();
            String id = e.getKey();
            Factory f = e.getValue();
            dncache.put(f.getDisplayName(id, locale), id);
          } 
          dncache = Collections.unmodifiableSortedMap(dncache);
          this.dnref = new LocaleRef(dncache, locale, com);
        } else {
          ref = this.dnref;
          dncache = ref.get(locale, com);
        } 
      } 
    } 
    Key matchKey = createKey(matchID);
    if (matchKey == null)
      return dncache; 
    SortedMap<String, String> result = new TreeMap<String, String>(dncache);
    Iterator<Map.Entry<String, String>> iter = result.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry<String, String> e = iter.next();
      if (!matchKey.isFallbackOf(e.getValue()))
        iter.remove(); 
    } 
    return result;
  }
  
  private static class LocaleRef {
    private final ULocale locale;
    
    private SoftReference<SortedMap<String, String>> ref;
    
    private Comparator<Object> com;
    
    LocaleRef(SortedMap<String, String> dnCache, ULocale locale, Comparator<Object> com) {
      this.locale = locale;
      this.com = com;
      this.ref = new SoftReference<SortedMap<String, String>>(dnCache);
    }
    
    SortedMap<String, String> get(ULocale loc, Comparator<Object> comp) {
      SortedMap<String, String> m = this.ref.get();
      if (m != null && this.locale.equals(loc) && (this.com == comp || (this.com != null && this.com.equals(comp))))
        return m; 
      return null;
    }
  }
  
  public final List<Factory> factories() {
    try {
      this.factoryLock.acquireRead();
      return new ArrayList<Factory>(this.factories);
    } finally {
      this.factoryLock.releaseRead();
    } 
  }
  
  public Factory registerObject(Object obj, String id) {
    return registerObject(obj, id, true);
  }
  
  public Factory registerObject(Object obj, String id, boolean visible) {
    String canonicalID = createKey(id).canonicalID();
    return registerFactory(new SimpleFactory(obj, canonicalID, visible));
  }
  
  public final Factory registerFactory(Factory factory) {
    if (factory == null)
      throw new NullPointerException(); 
    try {
      this.factoryLock.acquireWrite();
      this.factories.add(0, factory);
      clearCaches();
    } finally {
      this.factoryLock.releaseWrite();
    } 
    notifyChanged();
    return factory;
  }
  
  public final boolean unregisterFactory(Factory factory) {
    if (factory == null)
      throw new NullPointerException(); 
    boolean result = false;
    try {
      this.factoryLock.acquireWrite();
      if (this.factories.remove(factory)) {
        result = true;
        clearCaches();
      } 
    } finally {
      this.factoryLock.releaseWrite();
    } 
    if (result)
      notifyChanged(); 
    return result;
  }
  
  public final void reset() {
    try {
      this.factoryLock.acquireWrite();
      reInitializeFactories();
      clearCaches();
    } finally {
      this.factoryLock.releaseWrite();
    } 
    notifyChanged();
  }
  
  protected void reInitializeFactories() {
    this.factories.clear();
  }
  
  public boolean isDefault() {
    return (this.factories.size() == this.defaultSize);
  }
  
  protected void markDefault() {
    this.defaultSize = this.factories.size();
  }
  
  public Key createKey(String id) {
    return (id == null) ? null : new Key(id);
  }
  
  protected void clearCaches() {
    this.cacheref = null;
    this.idref = null;
    this.dnref = null;
  }
  
  protected void clearServiceCache() {
    this.cacheref = null;
  }
  
  protected boolean acceptsListener(EventListener l) {
    return l instanceof ServiceListener;
  }
  
  protected void notifyListener(EventListener l) {
    ((ServiceListener)l).serviceChanged(this);
  }
  
  public String stats() {
    ICURWLock.Stats stats = this.factoryLock.resetStats();
    if (stats != null)
      return stats.toString(); 
    return "no stats";
  }
  
  public String getName() {
    return this.name;
  }
  
  public String toString() {
    return super.toString() + "{" + this.name + "}";
  }
  
  public static interface ServiceListener extends EventListener {
    void serviceChanged(ICUService param1ICUService);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\ICUService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */