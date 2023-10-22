package com.sun.webkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sun.reflect.misc.MethodUtil;

public abstract class Utilities {
   private static Utilities instance;
   private static final Set classMethodsAllowList = asSet("getCanonicalName", "getEnumConstants", "getFields", "getMethods", "getName", "getPackageName", "getSimpleName", "getSuperclass", "getTypeName", "getTypeParameters", "isAssignableFrom", "isArray", "isEnum", "isInstance", "isInterface", "isLocalClass", "isMemberClass", "isPrimitive", "isSynthetic", "toGenericString", "toString");
   private static final Set classesRejectList = asSet("java.lang.ClassLoader", "java.lang.Module", "java.lang.Runtime", "java.lang.System");
   private static final List packagesRejectList = Arrays.asList("java.lang.invoke", "java.lang.module", "java.lang.reflect", "java.security", "sun.misc");

   public static synchronized void setUtilities(Utilities var0) {
      instance = var0;
   }

   public static synchronized Utilities getUtilities() {
      return instance;
   }

   protected abstract Pasteboard createPasteboard();

   protected abstract PopupMenu createPopupMenu();

   protected abstract ContextMenu createContextMenu();

   private static final Set asSet(String... var0) {
      return new HashSet(Arrays.asList(var0));
   }

   private static Object fwkInvokeWithContext(Method var0, Object var1, Object[] var2, AccessControlContext var3) throws Throwable {
      Class var4 = var0.getDeclaringClass();
      if (var4.equals(Class.class)) {
         if (!classMethodsAllowList.contains(var0.getName())) {
            throw new UnsupportedOperationException("invocation not supported");
         }
      } else {
         String var5 = var4.getName();
         if (classesRejectList.contains(var5)) {
            throw new UnsupportedOperationException("invocation not supported");
         }

         packagesRejectList.forEach((var1x) -> {
            if (var5.startsWith(var1x + ".")) {
               throw new UnsupportedOperationException("invocation not supported");
            }
         });
      }

      try {
         return AccessController.doPrivileged(() -> {
            return MethodUtil.invoke(var0, var1, var2);
         }, var3);
      } catch (PrivilegedActionException var7) {
         Object var6 = var7.getCause();
         if (var6 == null) {
            var6 = var7;
         } else if (var6 instanceof InvocationTargetException && ((Throwable)var6).getCause() != null) {
            var6 = ((Throwable)var6).getCause();
         }

         throw (Throwable)var6;
      }
   }
}
