package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

@GwtCompatible
public final class Suppliers {
   private Suppliers() {
   }

   public static Supplier compose(Function var0, Supplier var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new Suppliers.SupplierComposition(var0, var1);
   }

   public static Supplier memoize(Supplier var0) {
      return (Supplier)(var0 instanceof Suppliers.MemoizingSupplier ? var0 : new Suppliers.MemoizingSupplier((Supplier)Preconditions.checkNotNull(var0)));
   }

   public static Supplier memoizeWithExpiration(Supplier var0, long var1, TimeUnit var3) {
      return new Suppliers.ExpiringMemoizingSupplier(var0, var1, var3);
   }

   public static Supplier ofInstance(@Nullable Object var0) {
      return new Suppliers.SupplierOfInstance(var0);
   }

   public static Supplier synchronizedSupplier(Supplier var0) {
      return new Suppliers.ThreadSafeSupplier((Supplier)Preconditions.checkNotNull(var0));
   }

   @Beta
   public static Function supplierFunction() {
      Suppliers.SupplierFunctionImpl var0 = Suppliers.SupplierFunctionImpl.INSTANCE;
      return var0;
   }

   private static enum SupplierFunctionImpl implements Suppliers.SupplierFunction {
      INSTANCE;

      private static final Suppliers.SupplierFunctionImpl[] $VALUES = new Suppliers.SupplierFunctionImpl[]{INSTANCE};

      public Object apply(Supplier var1) {
         return var1.get();
      }

      public String toString() {
         return "Suppliers.supplierFunction()";
      }

      public Object apply(Object var1) {
         return this.apply((Supplier)var1);
      }
   }

   private interface SupplierFunction extends Function {
   }

   private static class ThreadSafeSupplier implements Supplier, Serializable {
      final Supplier delegate;
      private static final long serialVersionUID = 0L;

      ThreadSafeSupplier(Supplier var1) {
         this.delegate = var1;
      }

      public Object get() {
         Supplier var1;
         synchronized(var1 = this.delegate){}
         return this.delegate.get();
      }

      public String toString() {
         return "Suppliers.synchronizedSupplier(" + this.delegate + ")";
      }
   }

   private static class SupplierOfInstance implements Supplier, Serializable {
      final Object instance;
      private static final long serialVersionUID = 0L;

      SupplierOfInstance(@Nullable Object var1) {
         this.instance = var1;
      }

      public Object get() {
         return this.instance;
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Suppliers.SupplierOfInstance) {
            Suppliers.SupplierOfInstance var2 = (Suppliers.SupplierOfInstance)var1;
            return Objects.equal(this.instance, var2.instance);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.instance);
      }

      public String toString() {
         return "Suppliers.ofInstance(" + this.instance + ")";
      }
   }

   @VisibleForTesting
   static class ExpiringMemoizingSupplier implements Supplier, Serializable {
      final Supplier delegate;
      final long durationNanos;
      transient volatile Object value;
      transient volatile long expirationNanos;
      private static final long serialVersionUID = 0L;

      ExpiringMemoizingSupplier(Supplier var1, long var2, TimeUnit var4) {
         this.delegate = (Supplier)Preconditions.checkNotNull(var1);
         this.durationNanos = var4.toNanos(var2);
         Preconditions.checkArgument(var2 > 0L);
      }

      public Object get() {
         long var1 = this.expirationNanos;
         long var3 = Platform.systemNanoTime();
         if (var1 == 0L || var3 - var1 >= 0L) {
            synchronized(this){}
            if (var1 == this.expirationNanos) {
               Object var6 = this.delegate.get();
               this.value = var6;
               var1 = var3 + this.durationNanos;
               this.expirationNanos = var1 == 0L ? 1L : var1;
               return var6;
            }
         }

         return this.value;
      }

      public String toString() {
         return "Suppliers.memoizeWithExpiration(" + this.delegate + ", " + this.durationNanos + ", NANOS)";
      }
   }

   @VisibleForTesting
   static class MemoizingSupplier implements Supplier, Serializable {
      final Supplier delegate;
      transient volatile boolean initialized;
      transient Object value;
      private static final long serialVersionUID = 0L;

      MemoizingSupplier(Supplier var1) {
         this.delegate = var1;
      }

      public Object get() {
         if (!this.initialized) {
            synchronized(this){}
            if (!this.initialized) {
               Object var2 = this.delegate.get();
               this.value = var2;
               this.initialized = true;
               return var2;
            }
         }

         return this.value;
      }

      public String toString() {
         return "Suppliers.memoize(" + this.delegate + ")";
      }
   }

   private static class SupplierComposition implements Supplier, Serializable {
      final Function function;
      final Supplier supplier;
      private static final long serialVersionUID = 0L;

      SupplierComposition(Function var1, Supplier var2) {
         this.function = var1;
         this.supplier = var2;
      }

      public Object get() {
         return this.function.apply(this.supplier.get());
      }

      public boolean equals(@Nullable Object var1) {
         if (!(var1 instanceof Suppliers.SupplierComposition)) {
            return false;
         } else {
            Suppliers.SupplierComposition var2 = (Suppliers.SupplierComposition)var1;
            return this.function.equals(var2.function) && this.supplier.equals(var2.supplier);
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.function, this.supplier);
      }

      public String toString() {
         return "Suppliers.compose(" + this.function + ", " + this.supplier + ")";
      }
   }
}
