package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
@Beta
public final class Enums {
   @GwtIncompatible("java.lang.ref.WeakReference")
   private static final Map enumConstantCache = new WeakHashMap();

   private Enums() {
   }

   @GwtIncompatible("reflection")
   public static Field getField(Enum var0) {
      Class var1 = var0.getDeclaringClass();

      try {
         return var1.getDeclaredField(var0.name());
      } catch (NoSuchFieldException var3) {
         throw new AssertionError(var3);
      }
   }

   /** @deprecated */
   @Deprecated
   public static Function valueOfFunction(Class var0) {
      return new Enums.ValueOfFunction(var0);
   }

   public static Optional getIfPresent(Class var0, String var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return Platform.getEnumIfPresent(var0, var1);
   }

   @GwtIncompatible("java.lang.ref.WeakReference")
   private static Map populateCache(Class var0) {
      HashMap var1 = new HashMap();
      Iterator var2 = EnumSet.allOf(var0).iterator();

      while(var2.hasNext()) {
         Enum var3 = (Enum)var2.next();
         var1.put(var3.name(), new WeakReference(var3));
      }

      enumConstantCache.put(var0, var1);
      return var1;
   }

   @GwtIncompatible("java.lang.ref.WeakReference")
   static Map getEnumConstants(Class var0) {
      Map var1;
      synchronized(var1 = enumConstantCache){}
      Map var2 = (Map)enumConstantCache.get(var0);
      if (var2 == null) {
         var2 = populateCache(var0);
      }

      return var2;
   }

   public static Converter stringConverter(Class var0) {
      return new Enums.StringConverter(var0);
   }

   private static final class StringConverter extends Converter implements Serializable {
      private final Class enumClass;
      private static final long serialVersionUID = 0L;

      StringConverter(Class var1) {
         this.enumClass = (Class)Preconditions.checkNotNull(var1);
      }

      protected Enum doForward(String var1) {
         return Enum.valueOf(this.enumClass, var1);
      }

      protected String doBackward(Enum var1) {
         return var1.name();
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Enums.StringConverter) {
            Enums.StringConverter var2 = (Enums.StringConverter)var1;
            return this.enumClass.equals(var2.enumClass);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.enumClass.hashCode();
      }

      public String toString() {
         return "Enums.stringConverter(" + this.enumClass.getName() + ".class)";
      }

      protected Object doBackward(Object var1) {
         return this.doBackward((Enum)var1);
      }

      protected Object doForward(Object var1) {
         return this.doForward((String)var1);
      }
   }

   private static final class ValueOfFunction implements Function, Serializable {
      private final Class enumClass;
      private static final long serialVersionUID = 0L;

      private ValueOfFunction(Class var1) {
         this.enumClass = (Class)Preconditions.checkNotNull(var1);
      }

      public Enum apply(String var1) {
         try {
            return Enum.valueOf(this.enumClass, var1);
         } catch (IllegalArgumentException var3) {
            return null;
         }
      }

      public boolean equals(@Nullable Object var1) {
         return var1 instanceof Enums.ValueOfFunction && this.enumClass.equals(((Enums.ValueOfFunction)var1).enumClass);
      }

      public int hashCode() {
         return this.enumClass.hashCode();
      }

      public String toString() {
         return "Enums.valueOf(" + this.enumClass + ")";
      }

      public Object apply(Object var1) {
         return this.apply((String)var1);
      }

      ValueOfFunction(Class var1, Object var2) {
         this(var1);
      }
   }
}
