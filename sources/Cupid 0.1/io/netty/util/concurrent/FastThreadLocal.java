package io.netty.util.concurrent;

import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.PlatformDependent;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class FastThreadLocal<V> {
  private static final int variablesToRemoveIndex = InternalThreadLocalMap.nextVariableIndex();
  
  private final int index;
  
  public static void removeAll() {
    InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
    if (threadLocalMap == null)
      return; 
    try {
      Object v = threadLocalMap.indexedVariable(variablesToRemoveIndex);
      if (v != null && v != InternalThreadLocalMap.UNSET) {
        Set<FastThreadLocal<?>> variablesToRemove = (Set<FastThreadLocal<?>>)v;
        FastThreadLocal[] arrayOfFastThreadLocal = variablesToRemove.<FastThreadLocal>toArray(new FastThreadLocal[variablesToRemove.size()]);
        for (FastThreadLocal<?> tlv : arrayOfFastThreadLocal)
          tlv.remove(threadLocalMap); 
      } 
    } finally {
      InternalThreadLocalMap.remove();
    } 
  }
  
  public static int size() {
    InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
    if (threadLocalMap == null)
      return 0; 
    return threadLocalMap.size();
  }
  
  public static void destroy() {
    InternalThreadLocalMap.destroy();
  }
  
  private static void addToVariablesToRemove(InternalThreadLocalMap threadLocalMap, FastThreadLocal<?> variable) {
    Set<FastThreadLocal<?>> variablesToRemove;
    Object v = threadLocalMap.indexedVariable(variablesToRemoveIndex);
    if (v == InternalThreadLocalMap.UNSET || v == null) {
      variablesToRemove = Collections.newSetFromMap(new IdentityHashMap<FastThreadLocal<?>, Boolean>());
      threadLocalMap.setIndexedVariable(variablesToRemoveIndex, variablesToRemove);
    } else {
      variablesToRemove = (Set<FastThreadLocal<?>>)v;
    } 
    variablesToRemove.add(variable);
  }
  
  private static void removeFromVariablesToRemove(InternalThreadLocalMap threadLocalMap, FastThreadLocal<?> variable) {
    Object v = threadLocalMap.indexedVariable(variablesToRemoveIndex);
    if (v == InternalThreadLocalMap.UNSET || v == null)
      return; 
    Set<FastThreadLocal<?>> variablesToRemove = (Set<FastThreadLocal<?>>)v;
    variablesToRemove.remove(variable);
  }
  
  public FastThreadLocal() {
    this.index = InternalThreadLocalMap.nextVariableIndex();
  }
  
  public final V get() {
    return get(InternalThreadLocalMap.get());
  }
  
  public final V get(InternalThreadLocalMap threadLocalMap) {
    Object v = threadLocalMap.indexedVariable(this.index);
    if (v != InternalThreadLocalMap.UNSET)
      return (V)v; 
    return initialize(threadLocalMap);
  }
  
  private V initialize(InternalThreadLocalMap threadLocalMap) {
    V v = null;
    try {
      v = initialValue();
    } catch (Exception e) {
      PlatformDependent.throwException(e);
    } 
    threadLocalMap.setIndexedVariable(this.index, v);
    addToVariablesToRemove(threadLocalMap, this);
    return v;
  }
  
  public final void set(V value) {
    if (value != InternalThreadLocalMap.UNSET) {
      set(InternalThreadLocalMap.get(), value);
    } else {
      remove();
    } 
  }
  
  public final void set(InternalThreadLocalMap threadLocalMap, V value) {
    if (value != InternalThreadLocalMap.UNSET) {
      if (threadLocalMap.setIndexedVariable(this.index, value))
        addToVariablesToRemove(threadLocalMap, this); 
    } else {
      remove(threadLocalMap);
    } 
  }
  
  public final boolean isSet() {
    return isSet(InternalThreadLocalMap.getIfSet());
  }
  
  public final boolean isSet(InternalThreadLocalMap threadLocalMap) {
    return (threadLocalMap != null && threadLocalMap.isIndexedVariableSet(this.index));
  }
  
  public final void remove() {
    remove(InternalThreadLocalMap.getIfSet());
  }
  
  public final void remove(InternalThreadLocalMap threadLocalMap) {
    if (threadLocalMap == null)
      return; 
    Object v = threadLocalMap.removeIndexedVariable(this.index);
    removeFromVariablesToRemove(threadLocalMap, this);
    if (v != InternalThreadLocalMap.UNSET)
      try {
        onRemoval((V)v);
      } catch (Exception e) {
        PlatformDependent.throwException(e);
      }  
  }
  
  protected V initialValue() throws Exception {
    return null;
  }
  
  protected void onRemoval(V value) throws Exception {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\FastThreadLocal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */