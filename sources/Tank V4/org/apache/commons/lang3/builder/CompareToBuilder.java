package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Comparator;
import org.apache.commons.lang3.ArrayUtils;

public class CompareToBuilder implements Builder {
   private int comparison = 0;

   public static int reflectionCompare(Object var0, Object var1) {
      return reflectionCompare(var0, var1, false, (Class)null);
   }

   public static int reflectionCompare(Object var0, Object var1, boolean var2) {
      return reflectionCompare(var0, var1, var2, (Class)null);
   }

   public static int reflectionCompare(Object var0, Object var1, Collection var2) {
      return reflectionCompare(var0, var1, ReflectionToStringBuilder.toNoNullStringArray(var2));
   }

   public static int reflectionCompare(Object var0, Object var1, String... var2) {
      return reflectionCompare(var0, var1, false, (Class)null, var2);
   }

   public static int reflectionCompare(Object var0, Object var1, boolean var2, Class var3, String... var4) {
      if (var0 == var1) {
         return 0;
      } else if (var0 != null && var1 != null) {
         Class var5 = var0.getClass();
         if (!var5.isInstance(var1)) {
            throw new ClassCastException();
         } else {
            CompareToBuilder var6 = new CompareToBuilder();
            reflectionAppend(var0, var1, var5, var6, var2, var4);

            while(var5.getSuperclass() != null && var5 != var3) {
               var5 = var5.getSuperclass();
               reflectionAppend(var0, var1, var5, var6, var2, var4);
            }

            return var6.toComparison();
         }
      } else {
         throw new NullPointerException();
      }
   }

   private static void reflectionAppend(Object var0, Object var1, Class var2, CompareToBuilder var3, boolean var4, String[] var5) {
      Field[] var6 = var2.getDeclaredFields();
      AccessibleObject.setAccessible(var6, true);

      for(int var7 = 0; var7 < var6.length && var3.comparison == 0; ++var7) {
         Field var8 = var6[var7];
         if (!ArrayUtils.contains(var5, var8.getName()) && var8.getName().indexOf(36) == -1 && (var4 || !Modifier.isTransient(var8.getModifiers())) && !Modifier.isStatic(var8.getModifiers())) {
            try {
               var3.append(var8.get(var0), var8.get(var1));
            } catch (IllegalAccessException var10) {
               throw new InternalError("Unexpected IllegalAccessException");
            }
         }
      }

   }

   public CompareToBuilder appendSuper(int var1) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = var1;
         return this;
      }
   }

   public CompareToBuilder append(Object var1, Object var2) {
      return this.append((Object)var1, (Object)var2, (Comparator)null);
   }

   public CompareToBuilder append(Object var1, Object var2, Comparator var3) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 == null) {
         this.comparison = -1;
         return this;
      } else if (var2 == null) {
         this.comparison = 1;
         return this;
      } else {
         if (var1.getClass().isArray()) {
            if (var1 instanceof long[]) {
               this.append((long[])((long[])var1), (long[])((long[])var2));
            } else if (var1 instanceof int[]) {
               this.append((int[])((int[])var1), (int[])((int[])var2));
            } else if (var1 instanceof short[]) {
               this.append((short[])((short[])var1), (short[])((short[])var2));
            } else if (var1 instanceof char[]) {
               this.append((char[])((char[])var1), (char[])((char[])var2));
            } else if (var1 instanceof byte[]) {
               this.append((byte[])((byte[])var1), (byte[])((byte[])var2));
            } else if (var1 instanceof double[]) {
               this.append((double[])((double[])var1), (double[])((double[])var2));
            } else if (var1 instanceof float[]) {
               this.append((float[])((float[])var1), (float[])((float[])var2));
            } else if (var1 instanceof boolean[]) {
               this.append((boolean[])((boolean[])var1), (boolean[])((boolean[])var2));
            } else {
               this.append((Object[])((Object[])var1), (Object[])((Object[])var2), var3);
            }
         } else if (var3 == null) {
            Comparable var4 = (Comparable)var1;
            this.comparison = var4.compareTo(var2);
         } else {
            this.comparison = var3.compare(var1, var2);
         }

         return this;
      }
   }

   public CompareToBuilder append(long var1, long var3) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = var1 < var3 ? -1 : (var1 > var3 ? 1 : 0);
         return this;
      }
   }

   public CompareToBuilder append(int var1, int var2) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = var1 < var2 ? -1 : (var1 > var2 ? 1 : 0);
         return this;
      }
   }

   public CompareToBuilder append(short var1, short var2) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = var1 < var2 ? -1 : (var1 > var2 ? 1 : 0);
         return this;
      }
   }

   public CompareToBuilder append(char var1, char var2) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = var1 < var2 ? -1 : (var1 > var2 ? 1 : 0);
         return this;
      }
   }

   public CompareToBuilder append(byte var1, byte var2) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = var1 < var2 ? -1 : (var1 > var2 ? 1 : 0);
         return this;
      }
   }

   public CompareToBuilder append(double var1, double var3) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = Double.compare(var1, var3);
         return this;
      }
   }

   public CompareToBuilder append(float var1, float var2) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = Float.compare(var1, var2);
         return this;
      }
   }

   public CompareToBuilder append(boolean var1, boolean var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else {
         if (!var1) {
            this.comparison = -1;
         } else {
            this.comparison = 1;
         }

         return this;
      }
   }

   public CompareToBuilder append(Object[] var1, Object[] var2) {
      return this.append((Object[])var1, (Object[])var2, (Comparator)null);
   }

   public CompareToBuilder append(Object[] var1, Object[] var2, Comparator var3) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 == null) {
         this.comparison = -1;
         return this;
      } else if (var2 == null) {
         this.comparison = 1;
         return this;
      } else if (var1.length != var2.length) {
         this.comparison = var1.length < var2.length ? -1 : 1;
         return this;
      } else {
         for(int var4 = 0; var4 < var1.length && this.comparison == 0; ++var4) {
            this.append(var1[var4], var2[var4], var3);
         }

         return this;
      }
   }

   public CompareToBuilder append(long[] var1, long[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 == null) {
         this.comparison = -1;
         return this;
      } else if (var2 == null) {
         this.comparison = 1;
         return this;
      } else if (var1.length != var2.length) {
         this.comparison = var1.length < var2.length ? -1 : 1;
         return this;
      } else {
         for(int var3 = 0; var3 < var1.length && this.comparison == 0; ++var3) {
            this.append(var1[var3], var2[var3]);
         }

         return this;
      }
   }

   public CompareToBuilder append(int[] var1, int[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 == null) {
         this.comparison = -1;
         return this;
      } else if (var2 == null) {
         this.comparison = 1;
         return this;
      } else if (var1.length != var2.length) {
         this.comparison = var1.length < var2.length ? -1 : 1;
         return this;
      } else {
         for(int var3 = 0; var3 < var1.length && this.comparison == 0; ++var3) {
            this.append(var1[var3], var2[var3]);
         }

         return this;
      }
   }

   public CompareToBuilder append(short[] var1, short[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 == null) {
         this.comparison = -1;
         return this;
      } else if (var2 == null) {
         this.comparison = 1;
         return this;
      } else if (var1.length != var2.length) {
         this.comparison = var1.length < var2.length ? -1 : 1;
         return this;
      } else {
         for(int var3 = 0; var3 < var1.length && this.comparison == 0; ++var3) {
            this.append(var1[var3], var2[var3]);
         }

         return this;
      }
   }

   public CompareToBuilder append(char[] var1, char[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 == null) {
         this.comparison = -1;
         return this;
      } else if (var2 == null) {
         this.comparison = 1;
         return this;
      } else if (var1.length != var2.length) {
         this.comparison = var1.length < var2.length ? -1 : 1;
         return this;
      } else {
         for(int var3 = 0; var3 < var1.length && this.comparison == 0; ++var3) {
            this.append(var1[var3], var2[var3]);
         }

         return this;
      }
   }

   public CompareToBuilder append(byte[] var1, byte[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 == null) {
         this.comparison = -1;
         return this;
      } else if (var2 == null) {
         this.comparison = 1;
         return this;
      } else if (var1.length != var2.length) {
         this.comparison = var1.length < var2.length ? -1 : 1;
         return this;
      } else {
         for(int var3 = 0; var3 < var1.length && this.comparison == 0; ++var3) {
            this.append(var1[var3], var2[var3]);
         }

         return this;
      }
   }

   public CompareToBuilder append(double[] var1, double[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 == null) {
         this.comparison = -1;
         return this;
      } else if (var2 == null) {
         this.comparison = 1;
         return this;
      } else if (var1.length != var2.length) {
         this.comparison = var1.length < var2.length ? -1 : 1;
         return this;
      } else {
         for(int var3 = 0; var3 < var1.length && this.comparison == 0; ++var3) {
            this.append(var1[var3], var2[var3]);
         }

         return this;
      }
   }

   public CompareToBuilder append(float[] var1, float[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 == null) {
         this.comparison = -1;
         return this;
      } else if (var2 == null) {
         this.comparison = 1;
         return this;
      } else if (var1.length != var2.length) {
         this.comparison = var1.length < var2.length ? -1 : 1;
         return this;
      } else {
         for(int var3 = 0; var3 < var1.length && this.comparison == 0; ++var3) {
            this.append(var1[var3], var2[var3]);
         }

         return this;
      }
   }

   public CompareToBuilder append(boolean[] var1, boolean[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 == null) {
         this.comparison = -1;
         return this;
      } else if (var2 == null) {
         this.comparison = 1;
         return this;
      } else if (var1.length != var2.length) {
         this.comparison = var1.length < var2.length ? -1 : 1;
         return this;
      } else {
         for(int var3 = 0; var3 < var1.length && this.comparison == 0; ++var3) {
            this.append(var1[var3], var2[var3]);
         }

         return this;
      }
   }

   public int toComparison() {
      return this.comparison;
   }

   public Integer build() {
      return this.toComparison();
   }

   public Object build() {
      return this.build();
   }
}
