package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;

@Beta
public abstract class ForwardingLoadingCache extends ForwardingCache implements LoadingCache {
   protected ForwardingLoadingCache() {
   }

   protected abstract LoadingCache delegate();

   public Object get(Object var1) throws ExecutionException {
      return this.delegate().get(var1);
   }

   public Object getUnchecked(Object var1) {
      return this.delegate().getUnchecked(var1);
   }

   public ImmutableMap getAll(Iterable var1) throws ExecutionException {
      return this.delegate().getAll(var1);
   }

   public Object apply(Object var1) {
      return this.delegate().apply(var1);
   }

   public void refresh(Object var1) {
      this.delegate().refresh(var1);
   }

   protected Cache delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }

   @Beta
   public abstract static class SimpleForwardingLoadingCache extends ForwardingLoadingCache {
      private final LoadingCache delegate;

      protected SimpleForwardingLoadingCache(LoadingCache var1) {
         this.delegate = (LoadingCache)Preconditions.checkNotNull(var1);
      }

      protected final LoadingCache delegate() {
         return this.delegate;
      }

      protected Cache delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }
}
