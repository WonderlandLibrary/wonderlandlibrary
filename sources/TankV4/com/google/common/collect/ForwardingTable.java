package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@GwtCompatible
public abstract class ForwardingTable extends ForwardingObject implements Table {
   protected ForwardingTable() {
   }

   protected abstract Table delegate();

   public Set cellSet() {
      return this.delegate().cellSet();
   }

   public void clear() {
      this.delegate().clear();
   }

   public Map column(Object var1) {
      return this.delegate().column(var1);
   }

   public Set columnKeySet() {
      return this.delegate().columnKeySet();
   }

   public Map columnMap() {
      return this.delegate().columnMap();
   }

   public boolean contains(Object var1, Object var2) {
      return this.delegate().contains(var1, var2);
   }

   public boolean containsColumn(Object var1) {
      return this.delegate().containsColumn(var1);
   }

   public boolean containsRow(Object var1) {
      return this.delegate().containsRow(var1);
   }

   public boolean containsValue(Object var1) {
      return this.delegate().containsValue(var1);
   }

   public Object get(Object var1, Object var2) {
      return this.delegate().get(var1, var2);
   }

   public boolean isEmpty() {
      return this.delegate().isEmpty();
   }

   public Object put(Object var1, Object var2, Object var3) {
      return this.delegate().put(var1, var2, var3);
   }

   public void putAll(Table var1) {
      this.delegate().putAll(var1);
   }

   public Object remove(Object var1, Object var2) {
      return this.delegate().remove(var1, var2);
   }

   public Map row(Object var1) {
      return this.delegate().row(var1);
   }

   public Set rowKeySet() {
      return this.delegate().rowKeySet();
   }

   public Map rowMap() {
      return this.delegate().rowMap();
   }

   public int size() {
      return this.delegate().size();
   }

   public Collection values() {
      return this.delegate().values();
   }

   public boolean equals(Object var1) {
      return var1 == this || this.delegate().equals(var1);
   }

   public int hashCode() {
      return this.delegate().hashCode();
   }

   protected Object delegate() {
      return this.delegate();
   }
}
