package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

public class EqualsBuilder implements Builder {
   private static final ThreadLocal REGISTRY = new ThreadLocal();
   private boolean isEquals = true;

   static Set getRegistry() {
      return (Set)REGISTRY.get();
   }

   static Pair getRegisterPair(Object var0, Object var1) {
      IDKey var2 = new IDKey(var0);
      IDKey var3 = new IDKey(var1);
      return Pair.of(var2, var3);
   }

   static void register(Object var0, Object var1) {
      Class var2 = EqualsBuilder.class;
      synchronized(EqualsBuilder.class){}
      if (getRegistry() == null) {
         REGISTRY.set(new HashSet());
      }

      Set var4 = getRegistry();
      Pair var3 = getRegisterPair(var0, var1);
      var4.add(var3);
   }

   static void unregister(Object var0, Object var1) {
      Set var2 = getRegistry();
      if (var2 != null) {
         Pair var3 = getRegisterPair(var0, var1);
         var2.remove(var3);
         Class var4 = EqualsBuilder.class;
         synchronized(EqualsBuilder.class){}
         var2 = getRegistry();
         if (var2 != null && var2.isEmpty()) {
            REGISTRY.remove();
         }
      }

   }

   public static boolean reflectionEquals(Object var0, Object var1, Collection var2) {
      return reflectionEquals(var0, var1, ReflectionToStringBuilder.toNoNullStringArray(var2));
   }

   public static boolean reflectionEquals(Object var0, Object var1, String... var2) {
      return reflectionEquals(var0, var1, false, (Class)null, var2);
   }

   public static boolean reflectionEquals(Object var0, Object var1, boolean var2) {
      return reflectionEquals(var0, var1, var2, (Class)null);
   }

   public static boolean reflectionEquals(Object var0, Object var1, boolean var2, Class var3, String... var4) {
      if (var0 == var1) {
         return true;
      } else if (var0 != null && var1 != null) {
         Class var5 = var0.getClass();
         Class var6 = var1.getClass();
         Class var7;
         if (var5.isInstance(var1)) {
            var7 = var5;
            if (!var6.isInstance(var0)) {
               var7 = var6;
            }
         } else {
            if (!var6.isInstance(var0)) {
               return false;
            }

            var7 = var6;
            if (!var5.isInstance(var1)) {
               var7 = var5;
            }
         }

         EqualsBuilder var8 = new EqualsBuilder();

         try {
            if (var7.isArray()) {
               var8.append(var0, var1);
            } else {
               reflectionAppend(var0, var1, var7, var8, var2, var4);

               while(var7.getSuperclass() != null && var7 != var3) {
                  var7 = var7.getSuperclass();
                  reflectionAppend(var0, var1, var7, var8, var2, var4);
               }
            }
         } catch (IllegalArgumentException var10) {
            return false;
         }

         return var8.isEquals();
      } else {
         return false;
      }
   }

   private static void reflectionAppend(Object var0, Object var1, Class var2, EqualsBuilder var3, boolean var4, String[] var5) {
      if (var1 == null) {
         register(var0, var1);
         Field[] var6 = var2.getDeclaredFields();
         AccessibleObject.setAccessible(var6, true);

         for(int var7 = 0; var7 < var6.length && var3.isEquals; ++var7) {
            Field var8 = var6[var7];
            if (!ArrayUtils.contains(var5, var8.getName()) && var8.getName().indexOf(36) == -1 && (var4 || !Modifier.isTransient(var8.getModifiers())) && !Modifier.isStatic(var8.getModifiers())) {
               try {
                  var3.append(var8.get(var0), var8.get(var1));
               } catch (IllegalAccessException var11) {
                  throw new InternalError("Unexpected IllegalAccessException");
               }
            }
         }

         unregister(var0, var1);
      }
   }

   public EqualsBuilder appendSuper(boolean var1) {
      if (!this.isEquals) {
         return this;
      } else {
         this.isEquals = var1;
         return this;
      }
   }

   public EqualsBuilder append(Object var1, Object var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         Class var3 = var1.getClass();
         if (!var3.isArray()) {
            this.isEquals = var1.equals(var2);
         } else if (var1.getClass() != var2.getClass()) {
            this.setEquals(false);
         } else if (var1 instanceof long[]) {
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
            this.append((Object[])((Object[])var1), (Object[])((Object[])var2));
         }

         return this;
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(long var1, long var3) {
      if (!this.isEquals) {
         return this;
      } else {
         this.isEquals = var1 == var3;
         return this;
      }
   }

   public EqualsBuilder append(int var1, int var2) {
      if (!this.isEquals) {
         return this;
      } else {
         this.isEquals = var1 == var2;
         return this;
      }
   }

   public EqualsBuilder append(short var1, short var2) {
      if (!this.isEquals) {
         return this;
      } else {
         this.isEquals = var1 == var2;
         return this;
      }
   }

   public EqualsBuilder append(char var1, char var2) {
      if (!this.isEquals) {
         return this;
      } else {
         this.isEquals = var1 == var2;
         return this;
      }
   }

   public EqualsBuilder append(byte var1, byte var2) {
      if (!this.isEquals) {
         return this;
      } else {
         this.isEquals = var1 == var2;
         return this;
      }
   }

   public EqualsBuilder append(double var1, double var3) {
      return !this.isEquals ? this : this.append(Double.doubleToLongBits(var1), Double.doubleToLongBits(var3));
   }

   public EqualsBuilder append(float var1, float var2) {
      return !this.isEquals ? this : this.append(Float.floatToIntBits(var1), Float.floatToIntBits(var2));
   }

   public EqualsBuilder append(boolean var1, boolean var2) {
      if (!this.isEquals) {
         return this;
      } else {
         this.isEquals = var1 == var2;
         return this;
      }
   }

   public EqualsBuilder append(Object[] var1, Object[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(long[] var1, long[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(int[] var1, int[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(short[] var1, short[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(char[] var1, char[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(byte[] var1, byte[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(double[] var1, double[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(float[] var1, float[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(boolean[] var1, boolean[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public boolean isEquals() {
      return this.isEquals;
   }

   public Boolean build() {
      return this.isEquals();
   }

   protected void setEquals(boolean var1) {
      this.isEquals = var1;
   }

   public void reset() {
      this.isEquals = true;
   }

   public Object build() {
      return this.build();
   }
}
