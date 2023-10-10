package org.apache.commons.lang3;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Map.Entry;
import org.apache.commons.lang3.exception.CloneFailedException;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.text.StrBuilder;

public class ObjectUtils {
   public static final ObjectUtils.Null NULL = new ObjectUtils.Null();

   public static Object defaultIfNull(Object var0, Object var1) {
      return var0 != null ? var0 : var1;
   }

   public static Object firstNonNull(Object... var0) {
      if (var0 != null) {
         Object[] var1 = var0;
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Object var4 = var1[var3];
            if (var4 != null) {
               return var4;
            }
         }
      }

      return null;
   }

   public static boolean notEqual(Object var0, Object var1) {
      return var0 == var1;
   }

   /** @deprecated */
   @Deprecated
   public static int hashCode(Object var0) {
      return var0 == null ? 0 : var0.hashCode();
   }

   public String toString() {
      return super.toString();
   }

   /** @deprecated */
   @Deprecated
   public static int hashCodeMulti(Object... var0) {
      int var1 = 1;
      if (var0 != null) {
         Object[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Object var5 = var2[var4];
            int var6 = hashCode(var5);
            var1 = var1 * 31 + var6;
         }
      }

      return var1;
   }

   public static String identityToString(Object var0) {
      if (var0 == null) {
         return null;
      } else {
         StringBuilder var1 = new StringBuilder();
         identityToString(var1, var0);
         return var1.toString();
      }
   }

   public static void identityToString(Appendable var0, Object var1) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("Cannot get the toString of a null identity");
      } else {
         var0.append(var1.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(var1)));
      }
   }

   public static void identityToString(StrBuilder var0, Object var1) {
      if (var1 == null) {
         throw new NullPointerException("Cannot get the toString of a null identity");
      } else {
         var0.append(var1.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(var1)));
      }
   }

   public static void identityToString(StringBuffer var0, Object var1) {
      if (var1 == null) {
         throw new NullPointerException("Cannot get the toString of a null identity");
      } else {
         var0.append(var1.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(var1)));
      }
   }

   public static void identityToString(StringBuilder var0, Object var1) {
      if (var1 == null) {
         throw new NullPointerException("Cannot get the toString of a null identity");
      } else {
         var0.append(var1.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(var1)));
      }
   }

   /** @deprecated */
   @Deprecated
   public static String toString(Object var0) {
      return var0 == null ? "" : var0.toString();
   }

   /** @deprecated */
   @Deprecated
   public static String toString(Object var0, String var1) {
      return var0 == null ? var1 : var0.toString();
   }

   public static Comparable min(Comparable... var0) {
      Comparable var1 = null;
      if (var0 != null) {
         Comparable[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Comparable var5 = var2[var4];
            if (compare(var5, var1, true) < 0) {
               var1 = var5;
            }
         }
      }

      return var1;
   }

   public static Comparable max(Comparable... var0) {
      Comparable var1 = null;
      if (var0 != null) {
         Comparable[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Comparable var5 = var2[var4];
            if (compare(var5, var1, false) > 0) {
               var1 = var5;
            }
         }
      }

      return var1;
   }

   public static int compare(Comparable var0, Comparable var1) {
      return compare(var0, var1, false);
   }

   public static int compare(Comparable var0, Comparable var1, boolean var2) {
      if (var0 == var1) {
         return 0;
      } else if (var0 == null) {
         return var2 ? 1 : -1;
      } else if (var1 == null) {
         return var2 ? -1 : 1;
      } else {
         return var0.compareTo(var1);
      }
   }

   public static Comparable median(Comparable... var0) {
      Validate.notEmpty((Object[])var0);
      Validate.noNullElements((Object[])var0);
      TreeSet var1 = new TreeSet();
      Collections.addAll(var1, var0);
      Comparable var2 = (Comparable)var1.toArray()[(var1.size() - 1) / 2];
      return var2;
   }

   public static Object median(Comparator var0, Object... var1) {
      Validate.notEmpty(var1, "null/empty items");
      Validate.noNullElements(var1);
      Validate.notNull(var0, "null comparator");
      TreeSet var2 = new TreeSet(var0);
      Collections.addAll(var2, var1);
      Object var3 = var2.toArray()[(var2.size() - 1) / 2];
      return var3;
   }

   public static Object mode(Object... var0) {
      if (ArrayUtils.isNotEmpty(var0)) {
         HashMap var1 = new HashMap(var0.length);
         Object[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Object var5 = var2[var4];
            MutableInt var6 = (MutableInt)var1.get(var5);
            if (var6 == null) {
               var1.put(var5, new MutableInt(1));
            } else {
               var6.increment();
            }
         }

         Object var7 = null;
         var3 = 0;
         Iterator var8 = var1.entrySet().iterator();

         while(var8.hasNext()) {
            Entry var9 = (Entry)var8.next();
            int var10 = ((MutableInt)var9.getValue()).intValue();
            if (var10 == var3) {
               var7 = null;
            } else if (var10 > var3) {
               var3 = var10;
               var7 = var9.getKey();
            }
         }

         return var7;
      } else {
         return null;
      }
   }

   public static Object clone(Object var0) {
      if (!(var0 instanceof Cloneable)) {
         return null;
      } else {
         Object var1;
         if (var0.getClass().isArray()) {
            Class var2 = var0.getClass().getComponentType();
            if (!var2.isPrimitive()) {
               var1 = ((Object[])((Object[])var0)).clone();
            } else {
               int var3 = Array.getLength(var0);
               var1 = Array.newInstance(var2, var3);

               while(var3-- > 0) {
                  Array.set(var1, var3, Array.get(var0, var3));
               }
            }
         } else {
            try {
               Method var7 = var0.getClass().getMethod("clone");
               var1 = var7.invoke(var0);
            } catch (NoSuchMethodException var4) {
               throw new CloneFailedException("Cloneable type " + var0.getClass().getName() + " has no clone method", var4);
            } catch (IllegalAccessException var5) {
               throw new CloneFailedException("Cannot clone Cloneable type " + var0.getClass().getName(), var5);
            } catch (InvocationTargetException var6) {
               throw new CloneFailedException("Exception cloning Cloneable type " + var0.getClass().getName(), var6.getCause());
            }
         }

         return var1;
      }
   }

   public static Object cloneIfPossible(Object var0) {
      Object var1 = clone(var0);
      return var1 == null ? var0 : var1;
   }

   public static boolean CONST(boolean var0) {
      return var0;
   }

   public static byte CONST(byte var0) {
      return var0;
   }

   public static byte CONST_BYTE(int var0) throws IllegalArgumentException {
      if (var0 >= -128 && var0 <= 127) {
         return (byte)var0;
      } else {
         throw new IllegalArgumentException("Supplied value must be a valid byte literal between -128 and 127: [" + var0 + "]");
      }
   }

   public static char CONST(char var0) {
      return var0;
   }

   public static short CONST(short var0) {
      return var0;
   }

   public static short CONST_SHORT(int var0) throws IllegalArgumentException {
      if (var0 >= -32768 && var0 <= 32767) {
         return (short)var0;
      } else {
         throw new IllegalArgumentException("Supplied value must be a valid byte literal between -32768 and 32767: [" + var0 + "]");
      }
   }

   public static int CONST(int var0) {
      return var0;
   }

   public static long CONST(long var0) {
      return var0;
   }

   public static float CONST(float var0) {
      return var0;
   }

   public static double CONST(double var0) {
      return var0;
   }

   public static Object CONST(Object var0) {
      return var0;
   }

   public static class Null implements Serializable {
      private static final long serialVersionUID = 7092611880189329093L;

      Null() {
      }

      private Object readResolve() {
         return ObjectUtils.NULL;
      }
   }
}
