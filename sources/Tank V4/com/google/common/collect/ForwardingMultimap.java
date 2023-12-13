package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingMultimap extends ForwardingObject implements Multimap {
   protected ForwardingMultimap() {
   }

   protected abstract Multimap delegate();

   public Map asMap() {
      return this.delegate().asMap();
   }

   public void clear() {
      this.delegate().clear();
   }

   public boolean containsEntry(@Nullable Object var1, @Nullable Object var2) {
      return this.delegate().containsEntry(var1, var2);
   }

   public boolean containsKey(@Nullable Object var1) {
      return this.delegate().containsKey(var1);
   }

   public boolean containsValue(@Nullable Object var1) {
      return this.delegate().containsValue(var1);
   }

   public Collection entries() {
      return this.delegate().entries();
   }

   public Collection get(@Nullable Object var1) {
      return this.delegate().get(var1);
   }

   public boolean isEmpty() {
      return this.delegate().isEmpty();
   }

   public Multiset keys() {
      return this.delegate().keys();
   }

   public Set keySet() {
      return this.delegate().keySet();
   }

   public boolean put(Object var1, Object var2) {
      return this.delegate().put(var1, var2);
   }

   public boolean putAll(Object var1, Iterable var2) {
      return this.delegate().putAll(var1, var2);
   }

   public boolean putAll(Multimap var1) {
      return this.delegate().putAll(var1);
   }

   public boolean remove(@Nullable Object var1, @Nullable Object var2) {
      return this.delegate().remove(var1, var2);
   }

   public Collection removeAll(@Nullable Object var1) {
      return this.delegate().removeAll(var1);
   }

   public Collection replaceValues(Object var1, Iterable var2) {
      return this.delegate().replaceValues(var1, var2);
   }

   public int size() {
      return this.delegate().size();
   }

   public Collection values() {
      return this.delegate().values();
   }

   public boolean equals(@Nullable Object var1) {
      return var1 == this || this.delegate().equals(var1);
   }

   public int hashCode() {
      return this.delegate().hashCode();
   }

   protected Object delegate() {
      return this.delegate();
   }
}
