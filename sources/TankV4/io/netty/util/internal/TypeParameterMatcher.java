package io.netty.util.internal;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public abstract class TypeParameterMatcher {
   private static final TypeParameterMatcher NOOP = new NoOpTypeParameterMatcher();
   private static final Object TEST_OBJECT = new Object();
   private static final ThreadLocal getCache = new ThreadLocal() {
      protected Map initialValue() {
         return new IdentityHashMap();
      }

      protected Object initialValue() {
         return this.initialValue();
      }
   };
   private static final ThreadLocal findCache = new ThreadLocal() {
      protected Map initialValue() {
         return new IdentityHashMap();
      }

      protected Object initialValue() {
         return this.initialValue();
      }
   };

   public static TypeParameterMatcher get(Class var0) {
      Map var1 = (Map)getCache.get();
      Object var2 = (TypeParameterMatcher)var1.get(var0);
      if (var2 == null) {
         if (var0 == Object.class) {
            var2 = NOOP;
         } else if (PlatformDependent.hasJavassist()) {
            try {
               var2 = JavassistTypeParameterMatcherGenerator.generate(var0);
               ((TypeParameterMatcher)var2).match(TEST_OBJECT);
            } catch (IllegalAccessError var4) {
               var2 = null;
            } catch (Exception var5) {
               var2 = null;
            }
         }

         if (var2 == null) {
            var2 = new TypeParameterMatcher.ReflectiveMatcher(var0);
         }

         var1.put(var0, var2);
      }

      return (TypeParameterMatcher)var2;
   }

   public static TypeParameterMatcher find(Object var0, Class var1, String var2) {
      Map var3 = (Map)findCache.get();
      Class var4 = var0.getClass();
      Object var5 = (Map)var3.get(var4);
      if (var5 == null) {
         var5 = new HashMap();
         var3.put(var4, var5);
      }

      TypeParameterMatcher var6 = (TypeParameterMatcher)((Map)var5).get(var2);
      if (var6 == null) {
         var6 = get(find0(var0, var1, var2));
         ((Map)var5).put(var2, var6);
      }

      return var6;
   }

   private static Class find0(Object var0, Class var1, String var2) {
      Class var3 = var0.getClass();
      Class var4 = var3;

      do {
         while(var4.getSuperclass() != var1) {
            var4 = var4.getSuperclass();
            if (var4 == null) {
               return fail(var3, var2);
            }
         }

         int var5 = -1;
         TypeVariable[] var6 = var4.getSuperclass().getTypeParameters();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            if (var2.equals(var6[var7].getName())) {
               var5 = var7;
               break;
            }
         }

         if (var5 < 0) {
            throw new IllegalStateException("unknown type parameter '" + var2 + "': " + var1);
         }

         Type var11 = var4.getGenericSuperclass();
         if (!(var11 instanceof ParameterizedType)) {
            return Object.class;
         }

         Type[] var8 = ((ParameterizedType)var11).getActualTypeArguments();
         Type var9 = var8[var5];
         if (var9 instanceof ParameterizedType) {
            var9 = ((ParameterizedType)var9).getRawType();
         }

         if (var9 instanceof Class) {
            return (Class)var9;
         }

         if (var9 instanceof GenericArrayType) {
            Type var10 = ((GenericArrayType)var9).getGenericComponentType();
            if (var10 instanceof ParameterizedType) {
               var10 = ((ParameterizedType)var10).getRawType();
            }

            if (var10 instanceof Class) {
               return Array.newInstance((Class)var10, 0).getClass();
            }
         }

         if (!(var9 instanceof TypeVariable)) {
            return fail(var3, var2);
         }

         TypeVariable var12 = (TypeVariable)var9;
         var4 = var3;
         if (!(var12.getGenericDeclaration() instanceof Class)) {
            return Object.class;
         }

         var1 = (Class)var12.getGenericDeclaration();
         var2 = var12.getName();
      } while(var1.isAssignableFrom(var3));

      return Object.class;
   }

   private static Class fail(Class var0, String var1) {
      throw new IllegalStateException("cannot determine the type of the type parameter '" + var1 + "': " + var0);
   }

   public abstract boolean match(Object var1);

   protected TypeParameterMatcher() {
   }

   private static final class ReflectiveMatcher extends TypeParameterMatcher {
      private final Class type;

      ReflectiveMatcher(Class var1) {
         this.type = var1;
      }

      public boolean match(Object var1) {
         return this.type.isInstance(var1);
      }
   }
}
