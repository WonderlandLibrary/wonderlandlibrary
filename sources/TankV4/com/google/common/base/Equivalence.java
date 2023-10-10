package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class Equivalence {
   protected Equivalence() {
   }

   public final boolean equivalent(@Nullable Object var1, @Nullable Object var2) {
      if (var1 == var2) {
         return true;
      } else {
         return var1 != null && var2 != null ? this.doEquivalent(var1, var2) : false;
      }
   }

   protected abstract boolean doEquivalent(Object var1, Object var2);

   public final int hash(@Nullable Object var1) {
      return var1 == null ? 0 : this.doHash(var1);
   }

   protected abstract int doHash(Object var1);

   public final Equivalence onResultOf(Function var1) {
      return new FunctionalEquivalence(var1, this);
   }

   public final Equivalence.Wrapper wrap(@Nullable Object var1) {
      return new Equivalence.Wrapper(this, var1);
   }

   @GwtCompatible(
      serializable = true
   )
   public final Equivalence pairwise() {
      return new PairwiseEquivalence(this);
   }

   @Beta
   public final Predicate equivalentTo(@Nullable Object var1) {
      return new Equivalence.EquivalentToPredicate(this, var1);
   }

   public static Equivalence equals() {
      return Equivalence.Equals.INSTANCE;
   }

   public static Equivalence identity() {
      return Equivalence.Identity.INSTANCE;
   }

   static final class Identity extends Equivalence implements Serializable {
      static final Equivalence.Identity INSTANCE = new Equivalence.Identity();
      private static final long serialVersionUID = 1L;

      protected boolean doEquivalent(Object var1, Object var2) {
         return false;
      }

      protected int doHash(Object var1) {
         return System.identityHashCode(var1);
      }

      private Object readResolve() {
         return INSTANCE;
      }
   }

   static final class Equals extends Equivalence implements Serializable {
      static final Equivalence.Equals INSTANCE = new Equivalence.Equals();
      private static final long serialVersionUID = 1L;

      protected boolean doEquivalent(Object var1, Object var2) {
         return var1.equals(var2);
      }

      public int doHash(Object var1) {
         return var1.hashCode();
      }

      private Object readResolve() {
         return INSTANCE;
      }
   }

   private static final class EquivalentToPredicate implements Predicate, Serializable {
      private final Equivalence equivalence;
      @Nullable
      private final Object target;
      private static final long serialVersionUID = 0L;

      EquivalentToPredicate(Equivalence var1, @Nullable Object var2) {
         this.equivalence = (Equivalence)Preconditions.checkNotNull(var1);
         this.target = var2;
      }

      public boolean apply(@Nullable Object var1) {
         return this.equivalence.equivalent(var1, this.target);
      }

      public boolean equals(@Nullable Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof Equivalence.EquivalentToPredicate)) {
            return false;
         } else {
            Equivalence.EquivalentToPredicate var2 = (Equivalence.EquivalentToPredicate)var1;
            return this.equivalence.equals(var2.equivalence) && Objects.equal(this.target, var2.target);
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.equivalence, this.target);
      }

      public String toString() {
         return this.equivalence + ".equivalentTo(" + this.target + ")";
      }
   }

   public static final class Wrapper implements Serializable {
      private final Equivalence equivalence;
      @Nullable
      private final Object reference;
      private static final long serialVersionUID = 0L;

      private Wrapper(Equivalence var1, @Nullable Object var2) {
         this.equivalence = (Equivalence)Preconditions.checkNotNull(var1);
         this.reference = var2;
      }

      @Nullable
      public Object get() {
         return this.reference;
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 == this) {
            return true;
         } else {
            if (var1 instanceof Equivalence.Wrapper) {
               Equivalence.Wrapper var2 = (Equivalence.Wrapper)var1;
               if (this.equivalence.equals(var2.equivalence)) {
                  Equivalence var3 = this.equivalence;
                  return var3.equivalent(this.reference, var2.reference);
               }
            }

            return false;
         }
      }

      public int hashCode() {
         return this.equivalence.hash(this.reference);
      }

      public String toString() {
         return this.equivalence + ".wrap(" + this.reference + ")";
      }

      Wrapper(Equivalence var1, Object var2, Object var3) {
         this(var1, var2);
      }
   }
}
