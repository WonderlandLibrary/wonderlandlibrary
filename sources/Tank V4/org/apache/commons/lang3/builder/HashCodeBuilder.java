package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;

public class HashCodeBuilder implements Builder {
   private static final ThreadLocal REGISTRY = new ThreadLocal();
   private final int iConstant;
   private int iTotal = 0;

   static Set getRegistry() {
      return (Set)REGISTRY.get();
   }

   private static void reflectionAppend(Object var0, Class var1, HashCodeBuilder var2, boolean var3, String[] var4) {
      if (var0 == null) {
         register(var0);
         Field[] var5 = var1.getDeclaredFields();
         AccessibleObject.setAccessible(var5, true);
         Field[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Field var9 = var6[var8];
            if (!ArrayUtils.contains(var4, var9.getName()) && var9.getName().indexOf(36) == -1 && (var3 || !Modifier.isTransient(var9.getModifiers())) && !Modifier.isStatic(var9.getModifiers())) {
               try {
                  Object var10 = var9.get(var0);
                  var2.append(var10);
               } catch (IllegalAccessException var12) {
                  throw new InternalError("Unexpected IllegalAccessException");
               }
            }
         }

         unregister(var0);
      }
   }

   public static int reflectionHashCode(int var0, int var1, Object var2) {
      return reflectionHashCode(var0, var1, var2, false, (Class)null);
   }

   public static int reflectionHashCode(int var0, int var1, Object var2, boolean var3) {
      return reflectionHashCode(var0, var1, var2, var3, (Class)null);
   }

   public static int reflectionHashCode(int var0, int var1, Object var2, boolean var3, Class var4, String... var5) {
      if (var2 == null) {
         throw new IllegalArgumentException("The object to build a hash code for must not be null");
      } else {
         HashCodeBuilder var6 = new HashCodeBuilder(var0, var1);
         Class var7 = var2.getClass();
         reflectionAppend(var2, var7, var6, var3, var5);

         while(var7.getSuperclass() != null && var7 != var4) {
            var7 = var7.getSuperclass();
            reflectionAppend(var2, var7, var6, var3, var5);
         }

         return var6.toHashCode();
      }
   }

   public static int reflectionHashCode(Object var0, boolean var1) {
      return reflectionHashCode(17, 37, var0, var1, (Class)null);
   }

   public static int reflectionHashCode(Object var0, Collection var1) {
      return reflectionHashCode(var0, ReflectionToStringBuilder.toNoNullStringArray(var1));
   }

   public static int reflectionHashCode(Object var0, String... var1) {
      return reflectionHashCode(17, 37, var0, false, (Class)null, var1);
   }

   static void register(Object var0) {
      Class var1 = HashCodeBuilder.class;
      synchronized(HashCodeBuilder.class){}
      if (getRegistry() == null) {
         REGISTRY.set(new HashSet());
      }

      getRegistry().add(new IDKey(var0));
   }

   static void unregister(Object var0) {
      Set var1 = getRegistry();
      if (var1 != null) {
         var1.remove(new IDKey(var0));
         Class var2 = HashCodeBuilder.class;
         synchronized(HashCodeBuilder.class){}
         var1 = getRegistry();
         if (var1 != null && var1.isEmpty()) {
            REGISTRY.remove();
         }
      }

   }

   public HashCodeBuilder() {
      this.iConstant = 37;
      this.iTotal = 17;
   }

   public HashCodeBuilder(int var1, int var2) {
      if (var1 % 2 == 0) {
         throw new IllegalArgumentException("HashCodeBuilder requires an odd initial value");
      } else if (var2 % 2 == 0) {
         throw new IllegalArgumentException("HashCodeBuilder requires an odd multiplier");
      } else {
         this.iConstant = var2;
         this.iTotal = var1;
      }
   }

   public HashCodeBuilder append(boolean var1) {
      this.iTotal = this.iTotal * this.iConstant + (var1 ? 0 : 1);
      return this;
   }

   public HashCodeBuilder append(boolean[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
      } else {
         boolean[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            boolean var5 = var2[var4];
            this.append(var5);
         }
      }

      return this;
   }

   public HashCodeBuilder append(byte var1) {
      this.iTotal = this.iTotal * this.iConstant + var1;
      return this;
   }

   public HashCodeBuilder append(byte[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
      } else {
         byte[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            byte var5 = var2[var4];
            this.append(var5);
         }
      }

      return this;
   }

   public HashCodeBuilder append(char var1) {
      this.iTotal = this.iTotal * this.iConstant + var1;
      return this;
   }

   public HashCodeBuilder append(char[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
      } else {
         char[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            char var5 = var2[var4];
            this.append(var5);
         }
      }

      return this;
   }

   public HashCodeBuilder append(double var1) {
      return this.append(Double.doubleToLongBits(var1));
   }

   public HashCodeBuilder append(double[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
      } else {
         double[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            double var5 = var2[var4];
            this.append(var5);
         }
      }

      return this;
   }

   public HashCodeBuilder append(float var1) {
      this.iTotal = this.iTotal * this.iConstant + Float.floatToIntBits(var1);
      return this;
   }

   public HashCodeBuilder append(float[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
      } else {
         float[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            float var5 = var2[var4];
            this.append(var5);
         }
      }

      return this;
   }

   public HashCodeBuilder append(int var1) {
      this.iTotal = this.iTotal * this.iConstant + var1;
      return this;
   }

   public HashCodeBuilder append(int[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
      } else {
         int[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = var2[var4];
            this.append(var5);
         }
      }

      return this;
   }

   public HashCodeBuilder append(long var1) {
      this.iTotal = this.iTotal * this.iConstant + (int)(var1 ^ var1 >> 32);
      return this;
   }

   public HashCodeBuilder append(long[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
      } else {
         long[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            long var5 = var2[var4];
            this.append(var5);
         }
      }

      return this;
   }

   public HashCodeBuilder append(Object var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
      } else if (var1.getClass().isArray()) {
         if (var1 instanceof long[]) {
            this.append((long[])((long[])var1));
         } else if (var1 instanceof int[]) {
            this.append((int[])((int[])var1));
         } else if (var1 instanceof short[]) {
            this.append((short[])((short[])var1));
         } else if (var1 instanceof char[]) {
            this.append((char[])((char[])var1));
         } else if (var1 instanceof byte[]) {
            this.append((byte[])((byte[])var1));
         } else if (var1 instanceof double[]) {
            this.append((double[])((double[])var1));
         } else if (var1 instanceof float[]) {
            this.append((float[])((float[])var1));
         } else if (var1 instanceof boolean[]) {
            this.append((boolean[])((boolean[])var1));
         } else {
            this.append((Object[])((Object[])var1));
         }
      } else {
         this.iTotal = this.iTotal * this.iConstant + var1.hashCode();
      }

      return this;
   }

   public HashCodeBuilder append(Object[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
      } else {
         Object[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Object var5 = var2[var4];
            this.append(var5);
         }
      }

      return this;
   }

   public HashCodeBuilder append(short var1) {
      this.iTotal = this.iTotal * this.iConstant + var1;
      return this;
   }

   public HashCodeBuilder append(short[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
      } else {
         short[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            short var5 = var2[var4];
            this.append(var5);
         }
      }

      return this;
   }

   public HashCodeBuilder appendSuper(int var1) {
      this.iTotal = this.iTotal * this.iConstant + var1;
      return this;
   }

   public int toHashCode() {
      return this.iTotal;
   }

   public Integer build() {
      return this.toHashCode();
   }

   public int hashCode() {
      return this.toHashCode();
   }

   public Object build() {
      return this.build();
   }
}
