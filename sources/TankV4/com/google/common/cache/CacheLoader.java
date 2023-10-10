package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

@GwtCompatible(
   emulated = true
)
public abstract class CacheLoader {
   protected CacheLoader() {
   }

   public abstract Object load(Object var1) throws Exception;

   @GwtIncompatible("Futures")
   public ListenableFuture reload(Object var1, Object var2) throws Exception {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      return Futures.immediateFuture(this.load(var1));
   }

   public Map loadAll(Iterable var1) throws Exception {
      throw new CacheLoader.UnsupportedLoadingOperationException();
   }

   @Beta
   public static CacheLoader from(Function var0) {
      return new CacheLoader.FunctionToCacheLoader(var0);
   }

   @Beta
   public static CacheLoader from(Supplier var0) {
      return new CacheLoader.SupplierToCacheLoader(var0);
   }

   @Beta
   @GwtIncompatible("Executor + Futures")
   public static CacheLoader asyncReloading(CacheLoader var0, Executor var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new CacheLoader(var0, var1) {
         final CacheLoader val$loader;
         final Executor val$executor;

         {
            this.val$loader = var1;
            this.val$executor = var2;
         }

         public Object load(Object var1) throws Exception {
            return this.val$loader.load(var1);
         }

         public ListenableFuture reload(Object var1, Object var2) throws Exception {
            ListenableFutureTask var3 = ListenableFutureTask.create(new Callable(this, var1, var2) {
               final Object val$key;
               final Object val$oldValue;
               final <undefinedtype> this$0;

               {
                  this.this$0 = var1;
                  this.val$key = var2;
                  this.val$oldValue = var3;
               }

               public Object call() throws Exception {
                  return this.this$0.val$loader.reload(this.val$key, this.val$oldValue).get();
               }
            });
            this.val$executor.execute(var3);
            return var3;
         }

         public Map loadAll(Iterable var1) throws Exception {
            return this.val$loader.loadAll(var1);
         }
      };
   }

   public static final class InvalidCacheLoadException extends RuntimeException {
      public InvalidCacheLoadException(String var1) {
         super(var1);
      }
   }

   static final class UnsupportedLoadingOperationException extends UnsupportedOperationException {
   }

   private static final class SupplierToCacheLoader extends CacheLoader implements Serializable {
      private final Supplier computingSupplier;
      private static final long serialVersionUID = 0L;

      public SupplierToCacheLoader(Supplier var1) {
         this.computingSupplier = (Supplier)Preconditions.checkNotNull(var1);
      }

      public Object load(Object var1) {
         Preconditions.checkNotNull(var1);
         return this.computingSupplier.get();
      }
   }

   private static final class FunctionToCacheLoader extends CacheLoader implements Serializable {
      private final Function computingFunction;
      private static final long serialVersionUID = 0L;

      public FunctionToCacheLoader(Function var1) {
         this.computingFunction = (Function)Preconditions.checkNotNull(var1);
      }

      public Object load(Object var1) {
         return this.computingFunction.apply(Preconditions.checkNotNull(var1));
      }
   }
}
