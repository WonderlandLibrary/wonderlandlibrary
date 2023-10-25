package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingMapEntry;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public final class MutableTypeToInstanceMap<B> extends ForwardingMap<TypeToken<? extends B>, B> implements TypeToInstanceMap<B> {
  private final Map<TypeToken<? extends B>, B> backingMap = Maps.newHashMap();
  
  @Nullable
  public <T extends B> T getInstance(Class<T> type) {
    return trustedGet(TypeToken.of(type));
  }
  
  @Nullable
  public <T extends B> T putInstance(Class<T> type, @Nullable T value) {
    return trustedPut(TypeToken.of(type), value);
  }
  
  @Nullable
  public <T extends B> T getInstance(TypeToken<T> type) {
    return trustedGet(type.rejectTypeVariables());
  }
  
  @Nullable
  public <T extends B> T putInstance(TypeToken<T> type, @Nullable T value) {
    return trustedPut(type.rejectTypeVariables(), value);
  }
  
  public B put(TypeToken<? extends B> key, B value) {
    throw new UnsupportedOperationException("Please use putInstance() instead.");
  }
  
  public void putAll(Map<? extends TypeToken<? extends B>, ? extends B> map) {
    throw new UnsupportedOperationException("Please use putInstance() instead.");
  }
  
  public Set<Map.Entry<TypeToken<? extends B>, B>> entrySet() {
    return UnmodifiableEntry.transformEntries(super.entrySet());
  }
  
  protected Map<TypeToken<? extends B>, B> delegate() {
    return this.backingMap;
  }
  
  @Nullable
  private <T extends B> T trustedPut(TypeToken<T> type, @Nullable T value) {
    return (T)this.backingMap.put(type, (B)value);
  }
  
  @Nullable
  private <T extends B> T trustedGet(TypeToken<T> type) {
    return (T)this.backingMap.get(type);
  }
  
  private static final class UnmodifiableEntry<K, V> extends ForwardingMapEntry<K, V> {
    private final Map.Entry<K, V> delegate;
    
    static <K, V> Set<Map.Entry<K, V>> transformEntries(final Set<Map.Entry<K, V>> entries) {
      return (Set<Map.Entry<K, V>>)new ForwardingSet<Map.Entry<K, V>>() {
          protected Set<Map.Entry<K, V>> delegate() {
            return entries;
          }
          
          public Iterator<Map.Entry<K, V>> iterator() {
            return MutableTypeToInstanceMap.UnmodifiableEntry.transformEntries(super.iterator());
          }
          
          public Object[] toArray() {
            return standardToArray();
          }
          
          public <T> T[] toArray(T[] array) {
            return (T[])standardToArray((Object[])array);
          }
        };
    }
    
    private static <K, V> Iterator<Map.Entry<K, V>> transformEntries(Iterator<Map.Entry<K, V>> entries) {
      return Iterators.transform(entries, new Function<Map.Entry<K, V>, Map.Entry<K, V>>() {
            public Map.Entry<K, V> apply(Map.Entry<K, V> entry) {
              return (Map.Entry)new MutableTypeToInstanceMap.UnmodifiableEntry<Object, Object>(entry);
            }
          });
    }
    
    private UnmodifiableEntry(Map.Entry<K, V> delegate) {
      this.delegate = (Map.Entry<K, V>)Preconditions.checkNotNull(delegate);
    }
    
    protected Map.Entry<K, V> delegate() {
      return this.delegate;
    }
    
    public V setValue(V value) {
      throw new UnsupportedOperationException();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\reflect\MutableTypeToInstanceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */