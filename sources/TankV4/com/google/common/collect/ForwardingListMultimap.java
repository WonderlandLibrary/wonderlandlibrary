package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingListMultimap extends ForwardingMultimap implements ListMultimap {
   protected ForwardingListMultimap() {
   }

   protected abstract ListMultimap delegate();

   public List get(@Nullable Object var1) {
      return this.delegate().get(var1);
   }

   public List removeAll(@Nullable Object var1) {
      return this.delegate().removeAll(var1);
   }

   public List replaceValues(Object var1, Iterable var2) {
      return this.delegate().replaceValues(var1, var2);
   }

   public Collection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   public Collection removeAll(Object var1) {
      return this.removeAll(var1);
   }

   public Collection get(Object var1) {
      return this.get(var1);
   }

   protected Multimap delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }
}
