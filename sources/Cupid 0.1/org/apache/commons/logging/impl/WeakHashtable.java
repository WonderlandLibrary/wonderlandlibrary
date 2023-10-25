package org.apache.commons.logging.impl;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class WeakHashtable extends Hashtable {
  private static final long serialVersionUID = -1546036869799732453L;
  
  private static final int MAX_CHANGES_BEFORE_PURGE = 100;
  
  private static final int PARTIAL_PURGE_COUNT = 10;
  
  private final ReferenceQueue queue = new ReferenceQueue();
  
  private int changeCount = 0;
  
  public boolean containsKey(Object key) {
    Referenced referenced = new Referenced(key);
    return super.containsKey(referenced);
  }
  
  public Enumeration elements() {
    purge();
    return super.elements();
  }
  
  public Set entrySet() {
    purge();
    Set referencedEntries = super.entrySet();
    Set unreferencedEntries = new HashSet();
    for (Iterator it = referencedEntries.iterator(); it.hasNext(); ) {
      Map.Entry entry = it.next();
      Referenced referencedKey = (Referenced)entry.getKey();
      Object key = referencedKey.getValue();
      Object value = entry.getValue();
      if (key != null) {
        Entry dereferencedEntry = new Entry(key, value);
        unreferencedEntries.add(dereferencedEntry);
      } 
    } 
    return unreferencedEntries;
  }
  
  public Object get(Object key) {
    Referenced referenceKey = new Referenced(key);
    return super.get(referenceKey);
  }
  
  public Enumeration keys() {
    purge();
    Enumeration enumer = super.keys();
    return new Enumeration(this, enumer) {
        private final Enumeration val$enumer;
        
        private final WeakHashtable this$0;
        
        public boolean hasMoreElements() {
          return this.val$enumer.hasMoreElements();
        }
        
        public Object nextElement() {
          WeakHashtable.Referenced nextReference = this.val$enumer.nextElement();
          return nextReference.getValue();
        }
      };
  }
  
  public Set keySet() {
    purge();
    Set referencedKeys = super.keySet();
    Set unreferencedKeys = new HashSet();
    for (Iterator it = referencedKeys.iterator(); it.hasNext(); ) {
      Referenced referenceKey = (Referenced)it.next();
      Object keyValue = referenceKey.getValue();
      if (keyValue != null)
        unreferencedKeys.add(keyValue); 
    } 
    return unreferencedKeys;
  }
  
  public synchronized Object put(Object key, Object value) {
    if (key == null)
      throw new NullPointerException("Null keys are not allowed"); 
    if (value == null)
      throw new NullPointerException("Null values are not allowed"); 
    if (this.changeCount++ > 100) {
      purge();
      this.changeCount = 0;
    } else if (this.changeCount % 10 == 0) {
      purgeOne();
    } 
    Referenced keyRef = new Referenced(key, this.queue);
    return super.put(keyRef, value);
  }
  
  public void putAll(Map t) {
    if (t != null) {
      Set entrySet = t.entrySet();
      for (Iterator it = entrySet.iterator(); it.hasNext(); ) {
        Map.Entry entry = it.next();
        put(entry.getKey(), entry.getValue());
      } 
    } 
  }
  
  public Collection values() {
    purge();
    return super.values();
  }
  
  public synchronized Object remove(Object key) {
    if (this.changeCount++ > 100) {
      purge();
      this.changeCount = 0;
    } else if (this.changeCount % 10 == 0) {
      purgeOne();
    } 
    return super.remove(new Referenced(key));
  }
  
  public boolean isEmpty() {
    purge();
    return super.isEmpty();
  }
  
  public int size() {
    purge();
    return super.size();
  }
  
  public String toString() {
    purge();
    return super.toString();
  }
  
  protected void rehash() {
    purge();
    super.rehash();
  }
  
  private void purge() {
    List toRemove = new ArrayList();
    synchronized (this.queue) {
      WeakKey key;
      while ((key = (WeakKey)this.queue.poll()) != null)
        toRemove.add(key.getReferenced()); 
    } 
    int size = toRemove.size();
    for (int i = 0; i < size; i++)
      super.remove(toRemove.get(i)); 
  }
  
  private void purgeOne() {
    synchronized (this.queue) {
      WeakKey key = (WeakKey)this.queue.poll();
      if (key != null)
        super.remove(key.getReferenced()); 
    } 
  }
  
  private static final class Entry implements Map.Entry {
    private final Object key;
    
    private final Object value;
    
    private Entry(Object key, Object value) {
      this.key = key;
      this.value = value;
    }
    
    public boolean equals(Object o) {
      boolean result = false;
      if (o != null && o instanceof Map.Entry) {
        Map.Entry entry = (Map.Entry)o;
        result = (((getKey() == null) ? (entry.getKey() == null) : getKey().equals(entry.getKey())) && ((getValue() == null) ? (entry.getValue() == null) : getValue().equals(entry.getValue())));
      } 
      return result;
    }
    
    public int hashCode() {
      return ((getKey() == null) ? 0 : getKey().hashCode()) ^ ((getValue() == null) ? 0 : getValue().hashCode());
    }
    
    public Object setValue(Object value) {
      throw new UnsupportedOperationException("Entry.setValue is not supported.");
    }
    
    public Object getValue() {
      return this.value;
    }
    
    public Object getKey() {
      return this.key;
    }
  }
  
  private static final class Referenced {
    private final WeakReference reference;
    
    private final int hashCode;
    
    private Referenced(Object referant) {
      this.reference = new WeakReference(referant);
      this.hashCode = referant.hashCode();
    }
    
    private Referenced(Object key, ReferenceQueue queue) {
      this.reference = new WeakHashtable.WeakKey(key, queue, this);
      this.hashCode = key.hashCode();
    }
    
    public int hashCode() {
      return this.hashCode;
    }
    
    private Object getValue() {
      return this.reference.get();
    }
    
    public boolean equals(Object o) {
      boolean result = false;
      if (o instanceof Referenced) {
        Referenced otherKey = (Referenced)o;
        Object thisKeyValue = getValue();
        Object otherKeyValue = otherKey.getValue();
        if (thisKeyValue == null) {
          result = (otherKeyValue == null);
          result = (result && hashCode() == otherKey.hashCode());
        } else {
          result = thisKeyValue.equals(otherKeyValue);
        } 
      } 
      return result;
    }
  }
  
  private static final class WeakKey extends WeakReference {
    private final WeakHashtable.Referenced referenced;
    
    private WeakKey(Object key, ReferenceQueue queue, WeakHashtable.Referenced referenced) {
      super((T)key, queue);
      this.referenced = referenced;
    }
    
    private WeakHashtable.Referenced getReferenced() {
      return this.referenced;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\logging\impl\WeakHashtable.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */