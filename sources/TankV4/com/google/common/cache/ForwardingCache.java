package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingObject;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;

@Beta
public abstract class ForwardingCache extends ForwardingObject implements Cache {
   protected ForwardingCache() {
   }

   protected abstract Cache delegate();

   @Nullable
   public Object getIfPresent(Object var1) {
      return this.delegate().getIfPresent(var1);
   }

   public Object get(Object var1, Callable var2) throws ExecutionException {
      return this.delegate().get(var1, var2);
   }

   public ImmutableMap getAllPresent(Iterable var1) {
      return this.delegate().getAllPresent(var1);
   }

   public void put(Object var1, Object var2) {
      this.delegate().put(var1, var2);
   }

   public void putAll(Map var1) {
      this.delegate().putAll(var1);
   }

   public void invalidate(Object var1) {
      this.delegate().invalidate(var1);
   }

   public void invalidateAll(Iterable var1) {
      this.delegate().invalidateAll(var1);
   }

   public void invalidateAll() {
      this.delegate().invalidateAll();
   }

   public long size() {
      return this.delegate().size();
   }

   public CacheStats stats() {
      return this.delegate().stats();
   }

   public ConcurrentMap asMap() {
      return this.delegate().asMap();
   }

   public void cleanUp() {
      this.delegate().cleanUp();
   }

   protected Object delegate() {
      return this.delegate();
   }

   @Beta
   public abstract static class SimpleForwardingCache extends ForwardingCache {
      private final Cache delegate;

      protected SimpleForwardingCache(Cache var1) {
         this.delegate = (Cache)Preconditions.checkNotNull(var1);
      }

      protected final Cache delegate() {
         return this.delegate;
      }

      protected Object delegate() {
         return this.delegate();
      }
   }
}
