package com.google.gson.internal;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Primitives {
   private static final Map PRIMITIVE_TO_WRAPPER_TYPE;
   private static final Map WRAPPER_TO_PRIMITIVE_TYPE;

   private Primitives() {
   }

   private static void add(Map var0, Map var1, Class var2, Class var3) {
      var0.put(var2, var3);
      var1.put(var3, var2);
   }

   public static boolean isPrimitive(Type var0) {
      return PRIMITIVE_TO_WRAPPER_TYPE.containsKey(var0);
   }

   public static boolean isWrapperType(Type var0) {
      return WRAPPER_TO_PRIMITIVE_TYPE.containsKey($Gson$Preconditions.checkNotNull(var0));
   }

   public static Class wrap(Class var0) {
      Class var1 = (Class)PRIMITIVE_TO_WRAPPER_TYPE.get($Gson$Preconditions.checkNotNull(var0));
      return var1 == null ? var0 : var1;
   }

   public static Class unwrap(Class var0) {
      Class var1 = (Class)WRAPPER_TO_PRIMITIVE_TYPE.get($Gson$Preconditions.checkNotNull(var0));
      return var1 == null ? var0 : var1;
   }

   static {
      HashMap var0 = new HashMap(16);
      HashMap var1 = new HashMap(16);
      add(var0, var1, Boolean.TYPE, Boolean.class);
      add(var0, var1, Byte.TYPE, Byte.class);
      add(var0, var1, Character.TYPE, Character.class);
      add(var0, var1, Double.TYPE, Double.class);
      add(var0, var1, Float.TYPE, Float.class);
      add(var0, var1, Integer.TYPE, Integer.class);
      add(var0, var1, Long.TYPE, Long.class);
      add(var0, var1, Short.TYPE, Short.class);
      add(var0, var1, Void.TYPE, Void.class);
      PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(var0);
      WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(var1);
   }
}
