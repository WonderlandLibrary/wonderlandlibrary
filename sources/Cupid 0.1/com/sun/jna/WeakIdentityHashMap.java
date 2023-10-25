package com.sun.jna;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WeakIdentityHashMap implements Map {
  private final ReferenceQueue queue = new ReferenceQueue();
  
  private Map backingStore = new HashMap();
  
  public void clear() {
    this.backingStore.clear();
    reap();
  }
  
  public boolean containsKey(Object key) {
    reap();
    return this.backingStore.containsKey(new IdentityWeakReference(key));
  }
  
  public boolean containsValue(Object value) {
    reap();
    return this.backingStore.containsValue(value);
  }
  
  public Set entrySet() {
    reap();
    Set ret = new HashSet();
    for (Iterator i = this.backingStore.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry ref = i.next();
      final Object key = ((IdentityWeakReference)ref.getKey()).get();
      final Object value = ref.getValue();
      Map.Entry<Object, Object> entry = new Map.Entry<Object, Object>() {
          private final Object val$key;
          
          private final Object val$value;
          
          private final WeakIdentityHashMap this$0;
          
          public Object getKey() {
            return key;
          }
          
          public Object getValue() {
            return value;
          }
          
          public Object setValue(Object value) {
            throw new UnsupportedOperationException();
          }
        };
      ret.add(entry);
    } 
    return Collections.unmodifiableSet(ret);
  }
  
  public Set keySet() {
    reap();
    Set ret = new HashSet();
    for (Iterator i = this.backingStore.keySet().iterator(); i.hasNext(); ) {
      IdentityWeakReference ref = i.next();
      ret.add(ref.get());
    } 
    return Collections.unmodifiableSet(ret);
  }
  
  public boolean equals(Object o) {
    return this.backingStore.equals(((WeakIdentityHashMap)o).backingStore);
  }
  
  public Object get(Object key) {
    reap();
    return this.backingStore.get(new IdentityWeakReference(key));
  }
  
  public Object put(Object key, Object value) {
    reap();
    return this.backingStore.put(new IdentityWeakReference(key), value);
  }
  
  public int hashCode() {
    reap();
    return this.backingStore.hashCode();
  }
  
  public boolean isEmpty() {
    reap();
    return this.backingStore.isEmpty();
  }
  
  public void putAll(Map t) {
    throw new UnsupportedOperationException();
  }
  
  public Object remove(Object key) {
    reap();
    return this.backingStore.remove(new IdentityWeakReference(key));
  }
  
  public int size() {
    reap();
    return this.backingStore.size();
  }
  
  public Collection values() {
    reap();
    return this.backingStore.values();
  }
  
  private synchronized void reap() {
    Object zombie = this.queue.poll();
    while (zombie != null) {
      IdentityWeakReference victim = (IdentityWeakReference)zombie;
      this.backingStore.remove(victim);
      zombie = this.queue.poll();
    } 
  }
  
  class IdentityWeakReference extends WeakReference {
    int hash;
    
    private final WeakIdentityHashMap this$0;
    
    IdentityWeakReference(Object obj) {
      super((T)obj, WeakIdentityHashMap.this.queue);
      this.hash = System.identityHashCode(obj);
    }
    
    public int hashCode() {
      return this.hash;
    }
    
    public boolean equals(Object o) {
      if (this == o)
        return true; 
      IdentityWeakReference ref = (IdentityWeakReference)o;
      if (get() == ref.get())
        return true; 
      return false;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\WeakIdentityHashMap.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */