package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public final class Predicates {
   private static final Joiner COMMA_JOINER = Joiner.on(',');

   private Predicates() {
   }

   @GwtCompatible(
      serializable = true
   )
   public static Predicate alwaysTrue() {
      return Predicates.ObjectPredicate.ALWAYS_TRUE.withNarrowedType();
   }

   @GwtCompatible(
      serializable = true
   )
   public static Predicate alwaysFalse() {
      return Predicates.ObjectPredicate.ALWAYS_FALSE.withNarrowedType();
   }

   @GwtCompatible(
      serializable = true
   )
   public static Predicate isNull() {
      return Predicates.ObjectPredicate.IS_NULL.withNarrowedType();
   }

   @GwtCompatible(
      serializable = true
   )
   public static Predicate notNull() {
      return Predicates.ObjectPredicate.NOT_NULL.withNarrowedType();
   }

   public static Predicate not(Predicate var0) {
      return new Predicates.NotPredicate(var0);
   }

   public static Predicate and(Iterable var0) {
      return new Predicates.AndPredicate(defensiveCopy(var0));
   }

   public static Predicate and(Predicate... var0) {
      return new Predicates.AndPredicate(defensiveCopy((Object[])var0));
   }

   public static Predicate and(Predicate var0, Predicate var1) {
      return new Predicates.AndPredicate(asList((Predicate)Preconditions.checkNotNull(var0), (Predicate)Preconditions.checkNotNull(var1)));
   }

   public static Predicate or(Iterable var0) {
      return new Predicates.OrPredicate(defensiveCopy(var0));
   }

   public static Predicate or(Predicate... var0) {
      return new Predicates.OrPredicate(defensiveCopy((Object[])var0));
   }

   public static Predicate or(Predicate var0, Predicate var1) {
      return new Predicates.OrPredicate(asList((Predicate)Preconditions.checkNotNull(var0), (Predicate)Preconditions.checkNotNull(var1)));
   }

   public static Predicate equalTo(@Nullable Object var0) {
      return (Predicate)(var0 == null ? isNull() : new Predicates.IsEqualToPredicate(var0));
   }

   @GwtIncompatible("Class.isInstance")
   public static Predicate instanceOf(Class var0) {
      return new Predicates.InstanceOfPredicate(var0);
   }

   @GwtIncompatible("Class.isAssignableFrom")
   @Beta
   public static Predicate assignableFrom(Class var0) {
      return new Predicates.AssignableFromPredicate(var0);
   }

   public static Predicate in(Collection var0) {
      return new Predicates.InPredicate(var0);
   }

   public static Predicate compose(Predicate var0, Function var1) {
      return new Predicates.CompositionPredicate(var0, var1);
   }

   @GwtIncompatible("java.util.regex.Pattern")
   public static Predicate containsPattern(String var0) {
      return new Predicates.ContainsPatternFromStringPredicate(var0);
   }

   @GwtIncompatible("java.util.regex.Pattern")
   public static Predicate contains(Pattern var0) {
      return new Predicates.ContainsPatternPredicate(var0);
   }

   private static List asList(Predicate var0, Predicate var1) {
      return Arrays.asList(var0, var1);
   }

   private static List defensiveCopy(Object... var0) {
      return defensiveCopy((Iterable)Arrays.asList(var0));
   }

   static List defensiveCopy(Iterable var0) {
      ArrayList var1 = new ArrayList();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         var1.add(Preconditions.checkNotNull(var3));
      }

      return var1;
   }

   static Joiner access$800() {
      return COMMA_JOINER;
   }

   @GwtIncompatible("Only used by other GWT-incompatible code.")
   private static class ContainsPatternFromStringPredicate extends Predicates.ContainsPatternPredicate {
      private static final long serialVersionUID = 0L;

      ContainsPatternFromStringPredicate(String var1) {
         super(Pattern.compile(var1));
      }

      public String toString() {
         return "Predicates.containsPattern(" + this.pattern.pattern() + ")";
      }
   }

   @GwtIncompatible("Only used by other GWT-incompatible code.")
   private static class ContainsPatternPredicate implements Predicate, Serializable {
      final Pattern pattern;
      private static final long serialVersionUID = 0L;

      ContainsPatternPredicate(Pattern var1) {
         this.pattern = (Pattern)Preconditions.checkNotNull(var1);
      }

      public boolean apply(CharSequence var1) {
         return this.pattern.matcher(var1).find();
      }

      public int hashCode() {
         return Objects.hashCode(this.pattern.pattern(), this.pattern.flags());
      }

      public boolean equals(@Nullable Object var1) {
         if (!(var1 instanceof Predicates.ContainsPatternPredicate)) {
            return false;
         } else {
            Predicates.ContainsPatternPredicate var2 = (Predicates.ContainsPatternPredicate)var1;
            return Objects.equal(this.pattern.pattern(), var2.pattern.pattern()) && Objects.equal(this.pattern.flags(), var2.pattern.flags());
         }
      }

      public String toString() {
         String var1 = Objects.toStringHelper((Object)this.pattern).add("pattern", this.pattern.pattern()).add("pattern.flags", this.pattern.flags()).toString();
         return "Predicates.contains(" + var1 + ")";
      }

      public boolean apply(Object var1) {
         return this.apply((CharSequence)var1);
      }
   }

   private static class CompositionPredicate implements Predicate, Serializable {
      final Predicate p;
      final Function f;
      private static final long serialVersionUID = 0L;

      private CompositionPredicate(Predicate var1, Function var2) {
         this.p = (Predicate)Preconditions.checkNotNull(var1);
         this.f = (Function)Preconditions.checkNotNull(var2);
      }

      public boolean apply(@Nullable Object var1) {
         return this.p.apply(this.f.apply(var1));
      }

      public boolean equals(@Nullable Object var1) {
         if (!(var1 instanceof Predicates.CompositionPredicate)) {
            return false;
         } else {
            Predicates.CompositionPredicate var2 = (Predicates.CompositionPredicate)var1;
            return this.f.equals(var2.f) && this.p.equals(var2.p);
         }
      }

      public int hashCode() {
         return this.f.hashCode() ^ this.p.hashCode();
      }

      public String toString() {
         return this.p.toString() + "(" + this.f.toString() + ")";
      }

      CompositionPredicate(Predicate var1, Function var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class InPredicate implements Predicate, Serializable {
      private final Collection target;
      private static final long serialVersionUID = 0L;

      private InPredicate(Collection var1) {
         this.target = (Collection)Preconditions.checkNotNull(var1);
      }

      public boolean apply(@Nullable Object var1) {
         try {
            return this.target.contains(var1);
         } catch (NullPointerException var3) {
            return false;
         } catch (ClassCastException var4) {
            return false;
         }
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Predicates.InPredicate) {
            Predicates.InPredicate var2 = (Predicates.InPredicate)var1;
            return this.target.equals(var2.target);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.target.hashCode();
      }

      public String toString() {
         return "Predicates.in(" + this.target + ")";
      }

      InPredicate(Collection var1, Object var2) {
         this(var1);
      }
   }

   @GwtIncompatible("Class.isAssignableFrom")
   private static class AssignableFromPredicate implements Predicate, Serializable {
      private final Class clazz;
      private static final long serialVersionUID = 0L;

      private AssignableFromPredicate(Class var1) {
         this.clazz = (Class)Preconditions.checkNotNull(var1);
      }

      public boolean apply(Class var1) {
         return this.clazz.isAssignableFrom(var1);
      }

      public int hashCode() {
         return this.clazz.hashCode();
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Predicates.AssignableFromPredicate) {
            Predicates.AssignableFromPredicate var2 = (Predicates.AssignableFromPredicate)var1;
            return this.clazz == var2.clazz;
         } else {
            return false;
         }
      }

      public String toString() {
         return "Predicates.assignableFrom(" + this.clazz.getName() + ")";
      }

      public boolean apply(Object var1) {
         return this.apply((Class)var1);
      }

      AssignableFromPredicate(Class var1, Object var2) {
         this(var1);
      }
   }

   @GwtIncompatible("Class.isInstance")
   private static class InstanceOfPredicate implements Predicate, Serializable {
      private final Class clazz;
      private static final long serialVersionUID = 0L;

      private InstanceOfPredicate(Class var1) {
         this.clazz = (Class)Preconditions.checkNotNull(var1);
      }

      public boolean apply(@Nullable Object var1) {
         return this.clazz.isInstance(var1);
      }

      public int hashCode() {
         return this.clazz.hashCode();
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Predicates.InstanceOfPredicate) {
            Predicates.InstanceOfPredicate var2 = (Predicates.InstanceOfPredicate)var1;
            return this.clazz == var2.clazz;
         } else {
            return false;
         }
      }

      public String toString() {
         return "Predicates.instanceOf(" + this.clazz.getName() + ")";
      }

      InstanceOfPredicate(Class var1, Object var2) {
         this(var1);
      }
   }

   private static class IsEqualToPredicate implements Predicate, Serializable {
      private final Object target;
      private static final long serialVersionUID = 0L;

      private IsEqualToPredicate(Object var1) {
         this.target = var1;
      }

      public boolean apply(Object var1) {
         return this.target.equals(var1);
      }

      public int hashCode() {
         return this.target.hashCode();
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Predicates.IsEqualToPredicate) {
            Predicates.IsEqualToPredicate var2 = (Predicates.IsEqualToPredicate)var1;
            return this.target.equals(var2.target);
         } else {
            return false;
         }
      }

      public String toString() {
         return "Predicates.equalTo(" + this.target + ")";
      }

      IsEqualToPredicate(Object var1, Object var2) {
         this(var1);
      }
   }

   private static class OrPredicate implements Predicate, Serializable {
      private final List components;
      private static final long serialVersionUID = 0L;

      private OrPredicate(List var1) {
         this.components = var1;
      }

      public boolean apply(@Nullable Object var1) {
         for(int var2 = 0; var2 < this.components.size(); ++var2) {
            if (((Predicate)this.components.get(var2)).apply(var1)) {
               return true;
            }
         }

         return false;
      }

      public int hashCode() {
         return this.components.hashCode() + 87855567;
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Predicates.OrPredicate) {
            Predicates.OrPredicate var2 = (Predicates.OrPredicate)var1;
            return this.components.equals(var2.components);
         } else {
            return false;
         }
      }

      public String toString() {
         return "Predicates.or(" + Predicates.access$800().join((Iterable)this.components) + ")";
      }

      OrPredicate(List var1, Object var2) {
         this(var1);
      }
   }

   private static class AndPredicate implements Predicate, Serializable {
      private final List components;
      private static final long serialVersionUID = 0L;

      private AndPredicate(List var1) {
         this.components = var1;
      }

      public boolean apply(@Nullable Object var1) {
         for(int var2 = 0; var2 < this.components.size(); ++var2) {
            if (!((Predicate)this.components.get(var2)).apply(var1)) {
               return false;
            }
         }

         return true;
      }

      public int hashCode() {
         return this.components.hashCode() + 306654252;
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Predicates.AndPredicate) {
            Predicates.AndPredicate var2 = (Predicates.AndPredicate)var1;
            return this.components.equals(var2.components);
         } else {
            return false;
         }
      }

      public String toString() {
         return "Predicates.and(" + Predicates.access$800().join((Iterable)this.components) + ")";
      }

      AndPredicate(List var1, Object var2) {
         this(var1);
      }
   }

   private static class NotPredicate implements Predicate, Serializable {
      final Predicate predicate;
      private static final long serialVersionUID = 0L;

      NotPredicate(Predicate var1) {
         this.predicate = (Predicate)Preconditions.checkNotNull(var1);
      }

      public boolean apply(@Nullable Object var1) {
         return !this.predicate.apply(var1);
      }

      public int hashCode() {
         return ~this.predicate.hashCode();
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Predicates.NotPredicate) {
            Predicates.NotPredicate var2 = (Predicates.NotPredicate)var1;
            return this.predicate.equals(var2.predicate);
         } else {
            return false;
         }
      }

      public String toString() {
         return "Predicates.not(" + this.predicate.toString() + ")";
      }
   }

   static enum ObjectPredicate implements Predicate {
      ALWAYS_TRUE {
         public boolean apply(@Nullable Object var1) {
            return true;
         }

         public String toString() {
            return "Predicates.alwaysTrue()";
         }
      },
      ALWAYS_FALSE {
         public boolean apply(@Nullable Object var1) {
            return false;
         }

         public String toString() {
            return "Predicates.alwaysFalse()";
         }
      },
      IS_NULL {
         public boolean apply(@Nullable Object var1) {
            return var1 == null;
         }

         public String toString() {
            return "Predicates.isNull()";
         }
      },
      NOT_NULL {
         public boolean apply(@Nullable Object var1) {
            return var1 != null;
         }

         public String toString() {
            return "Predicates.notNull()";
         }
      };

      private static final Predicates.ObjectPredicate[] $VALUES = new Predicates.ObjectPredicate[]{ALWAYS_TRUE, ALWAYS_FALSE, IS_NULL, NOT_NULL};

      private ObjectPredicate() {
      }

      Predicate withNarrowedType() {
         return this;
      }

      ObjectPredicate(Object var3) {
         this();
      }
   }
}
