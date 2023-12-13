package io.netty.handler.codec.serialization;

import java.lang.ref.Reference;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

abstract class ReferenceMap implements Map {
   private final Map delegate;

   protected ReferenceMap(Map var1) {
      this.delegate = var1;
   }

   abstract Reference fold(Object var1);

   private Object unfold(Reference var1) {
      return var1 == null ? null : var1.get();
   }

   public int size() {
      return this.delegate.size();
   }

   public boolean isEmpty() {
      return this.delegate.isEmpty();
   }

   public boolean containsKey(Object var1) {
      return this.delegate.containsKey(var1);
   }

   public boolean containsValue(Object var1) {
      throw new UnsupportedOperationException();
   }

   public Object get(Object var1) {
      return this.unfold((Reference)this.delegate.get(var1));
   }

   public Object put(Object var1, Object var2) {
      return this.unfold((Reference)this.delegate.put(var1, this.fold(var2)));
   }

   public Object remove(Object var1) {
      return this.unfold((Reference)this.delegate.remove(var1));
   }

   public void putAll(Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         this.delegate.put(var3.getKey(), this.fold(var3.getValue()));
      }

   }

   public void clear() {
      this.delegate.clear();
   }

   public Set keySet() {
      return this.delegate.keySet();
   }

   public Collection values() {
      throw new UnsupportedOperationException();
   }

   public Set entrySet() {
      throw new UnsupportedOperationException();
   }
}
