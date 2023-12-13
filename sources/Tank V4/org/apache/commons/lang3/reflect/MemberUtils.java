package org.apache.commons.lang3.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.ClassUtils;

abstract class MemberUtils {
   private static final int ACCESS_TEST = 7;
   private static final Class[] ORDERED_PRIMITIVE_TYPES;

   static boolean setAccessibleWorkaround(AccessibleObject var0) {
      if (var0 != null && !var0.isAccessible()) {
         Member var1 = (Member)var0;
         if (!var0.isAccessible() && Modifier.isPublic(var1.getModifiers()) && var1.getDeclaringClass().getModifiers() == 0) {
            try {
               var0.setAccessible(true);
               return true;
            } catch (SecurityException var3) {
            }
         }

         return false;
      } else {
         return false;
      }
   }

   static boolean isAccessible(Member var0) {
      return var0 != null && Modifier.isPublic(var0.getModifiers()) && !var0.isSynthetic();
   }

   static int compareParameterTypes(Class[] var0, Class[] var1, Class[] var2) {
      float var3 = getTotalTransformationCost(var2, var0);
      float var4 = getTotalTransformationCost(var2, var1);
      return var3 < var4 ? -1 : (var4 < var3 ? 1 : 0);
   }

   private static float getTotalTransformationCost(Class[] var0, Class[] var1) {
      float var2 = 0.0F;

      for(int var3 = 0; var3 < var0.length; ++var3) {
         Class var4 = var0[var3];
         Class var5 = var1[var3];
         var2 += getObjectTransformationCost(var4, var5);
      }

      return var2;
   }

   private static float getObjectTransformationCost(Class var0, Class var1) {
      if (var1.isPrimitive()) {
         return getPrimitivePromotionCost(var0, var1);
      } else {
         float var2;
         for(var2 = 0.0F; var0 != null && !var1.equals(var0); var0 = var0.getSuperclass()) {
            if (var1.isInterface() && ClassUtils.isAssignable(var0, var1)) {
               var2 += 0.25F;
               break;
            }

            ++var2;
         }

         if (var0 == null) {
            ++var2;
         }

         return var2;
      }
   }

   private static float getPrimitivePromotionCost(Class var0, Class var1) {
      float var2 = 0.0F;
      Class var3 = var0;
      if (!var0.isPrimitive()) {
         var2 += 0.1F;
         var3 = ClassUtils.wrapperToPrimitive(var0);
      }

      for(int var4 = 0; var3 != var1 && var4 < ORDERED_PRIMITIVE_TYPES.length; ++var4) {
         if (var3 == ORDERED_PRIMITIVE_TYPES[var4]) {
            var2 += 0.1F;
            if (var4 < ORDERED_PRIMITIVE_TYPES.length - 1) {
               var3 = ORDERED_PRIMITIVE_TYPES[var4 + 1];
            }
         }
      }

      return var2;
   }

   static {
      ORDERED_PRIMITIVE_TYPES = new Class[]{Byte.TYPE, Short.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};
   }
}
