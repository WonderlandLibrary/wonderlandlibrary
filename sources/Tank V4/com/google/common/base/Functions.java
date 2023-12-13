package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
public final class Functions {
   private Functions() {
   }

   public static Function toStringFunction() {
      return Functions.ToStringFunction.INSTANCE;
   }

   public static Function identity() {
      return Functions.IdentityFunction.INSTANCE;
   }

   public static Function forMap(Map var0) {
      return new Functions.FunctionForMapNoDefault(var0);
   }

   public static Function forMap(Map var0, @Nullable Object var1) {
      return new Functions.ForMapWithDefault(var0, var1);
   }

   public static Function compose(Function var0, Function var1) {
      return new Functions.FunctionComposition(var0, var1);
   }

   public static Function forPredicate(Predicate var0) {
      return new Functions.PredicateFunction(var0);
   }

   public static Function constant(@Nullable Object var0) {
      return new Functions.ConstantFunction(var0);
   }

   @Beta
   public static Function forSupplier(Supplier var0) {
      return new Functions.SupplierFunction(var0);
   }

   private static class SupplierFunction implements Function, Serializable {
      private final Supplier supplier;
      private static final long serialVersionUID = 0L;

      private SupplierFunction(Supplier var1) {
         this.supplier = (Supplier)Preconditions.checkNotNull(var1);
      }

      public Object apply(@Nullable Object var1) {
         return this.supplier.get();
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Functions.SupplierFunction) {
            Functions.SupplierFunction var2 = (Functions.SupplierFunction)var1;
            return this.supplier.equals(var2.supplier);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.supplier.hashCode();
      }

      public String toString() {
         return "forSupplier(" + this.supplier + ")";
      }

      SupplierFunction(Supplier var1, Object var2) {
         this(var1);
      }
   }

   private static class ConstantFunction implements Function, Serializable {
      private final Object value;
      private static final long serialVersionUID = 0L;

      public ConstantFunction(@Nullable Object var1) {
         this.value = var1;
      }

      public Object apply(@Nullable Object var1) {
         return this.value;
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Functions.ConstantFunction) {
            Functions.ConstantFunction var2 = (Functions.ConstantFunction)var1;
            return Objects.equal(this.value, var2.value);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.value == null ? 0 : this.value.hashCode();
      }

      public String toString() {
         return "constant(" + this.value + ")";
      }
   }

   private static class PredicateFunction implements Function, Serializable {
      private final Predicate predicate;
      private static final long serialVersionUID = 0L;

      private PredicateFunction(Predicate var1) {
         this.predicate = (Predicate)Preconditions.checkNotNull(var1);
      }

      public Boolean apply(@Nullable Object var1) {
         return this.predicate.apply(var1);
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Functions.PredicateFunction) {
            Functions.PredicateFunction var2 = (Functions.PredicateFunction)var1;
            return this.predicate.equals(var2.predicate);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.predicate.hashCode();
      }

      public String toString() {
         return "forPredicate(" + this.predicate + ")";
      }

      public Object apply(Object var1) {
         return this.apply(var1);
      }

      PredicateFunction(Predicate var1, Object var2) {
         this(var1);
      }
   }

   private static class FunctionComposition implements Function, Serializable {
      private final Function g;
      private final Function f;
      private static final long serialVersionUID = 0L;

      public FunctionComposition(Function var1, Function var2) {
         this.g = (Function)Preconditions.checkNotNull(var1);
         this.f = (Function)Preconditions.checkNotNull(var2);
      }

      public Object apply(@Nullable Object var1) {
         return this.g.apply(this.f.apply(var1));
      }

      public boolean equals(@Nullable Object var1) {
         if (!(var1 instanceof Functions.FunctionComposition)) {
            return false;
         } else {
            Functions.FunctionComposition var2 = (Functions.FunctionComposition)var1;
            return this.f.equals(var2.f) && this.g.equals(var2.g);
         }
      }

      public int hashCode() {
         return this.f.hashCode() ^ this.g.hashCode();
      }

      public String toString() {
         return this.g + "(" + this.f + ")";
      }
   }

   private static class ForMapWithDefault implements Function, Serializable {
      final Map map;
      final Object defaultValue;
      private static final long serialVersionUID = 0L;

      ForMapWithDefault(Map var1, @Nullable Object var2) {
         this.map = (Map)Preconditions.checkNotNull(var1);
         this.defaultValue = var2;
      }

      public Object apply(@Nullable Object var1) {
         Object var2 = this.map.get(var1);
         return var2 == null && !this.map.containsKey(var1) ? this.defaultValue : var2;
      }

      public boolean equals(@Nullable Object var1) {
         if (!(var1 instanceof Functions.ForMapWithDefault)) {
            return false;
         } else {
            Functions.ForMapWithDefault var2 = (Functions.ForMapWithDefault)var1;
            return this.map.equals(var2.map) && Objects.equal(this.defaultValue, var2.defaultValue);
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.map, this.defaultValue);
      }

      public String toString() {
         return "forMap(" + this.map + ", defaultValue=" + this.defaultValue + ")";
      }
   }

   private static class FunctionForMapNoDefault implements Function, Serializable {
      final Map map;
      private static final long serialVersionUID = 0L;

      FunctionForMapNoDefault(Map var1) {
         this.map = (Map)Preconditions.checkNotNull(var1);
      }

      public Object apply(@Nullable Object var1) {
         Object var2 = this.map.get(var1);
         Preconditions.checkArgument(var2 != null || this.map.containsKey(var1), "Key '%s' not present in map", var1);
         return var2;
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Functions.FunctionForMapNoDefault) {
            Functions.FunctionForMapNoDefault var2 = (Functions.FunctionForMapNoDefault)var1;
            return this.map.equals(var2.map);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.map.hashCode();
      }

      public String toString() {
         return "forMap(" + this.map + ")";
      }
   }

   private static enum IdentityFunction implements Function {
      INSTANCE;

      private static final Functions.IdentityFunction[] $VALUES = new Functions.IdentityFunction[]{INSTANCE};

      @Nullable
      public Object apply(@Nullable Object var1) {
         return var1;
      }

      public String toString() {
         return "identity";
      }
   }

   private static enum ToStringFunction implements Function {
      INSTANCE;

      private static final Functions.ToStringFunction[] $VALUES = new Functions.ToStringFunction[]{INSTANCE};

      public String apply(Object var1) {
         Preconditions.checkNotNull(var1);
         return var1.toString();
      }

      public String toString() {
         return "toString";
      }

      public Object apply(Object var1) {
         return this.apply(var1);
      }
   }
}
