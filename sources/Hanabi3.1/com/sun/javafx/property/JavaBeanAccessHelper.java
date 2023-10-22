package com.sun.javafx.property;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.beans.property.ReadOnlyObjectProperty;

public final class JavaBeanAccessHelper {
   private static Method JAVA_BEAN_QUICK_ACCESSOR_CREATE_RO;
   private static boolean initialized;

   private JavaBeanAccessHelper() {
   }

   public static ReadOnlyObjectProperty createReadOnlyJavaBeanProperty(Object var0, String var1) throws NoSuchMethodException {
      init();
      if (JAVA_BEAN_QUICK_ACCESSOR_CREATE_RO == null) {
         throw new UnsupportedOperationException("Java beans are not supported.");
      } else {
         try {
            return (ReadOnlyObjectProperty)JAVA_BEAN_QUICK_ACCESSOR_CREATE_RO.invoke((Object)null, var0, var1);
         } catch (IllegalAccessException var3) {
            throw new UnsupportedOperationException("Java beans are not supported.");
         } catch (InvocationTargetException var4) {
            if (var4.getCause() instanceof NoSuchMethodException) {
               throw (NoSuchMethodException)var4.getCause();
            } else {
               throw new UnsupportedOperationException("Java beans are not supported.");
            }
         }
      }
   }

   private static void init() {
      if (!initialized) {
         try {
            Class var0 = Class.forName("com.sun.javafx.property.adapter.JavaBeanQuickAccessor", true, JavaBeanAccessHelper.class.getClassLoader());
            JAVA_BEAN_QUICK_ACCESSOR_CREATE_RO = var0.getDeclaredMethod("createReadOnlyJavaBeanObjectProperty", Object.class, String.class);
         } catch (ClassNotFoundException var1) {
         } catch (NoSuchMethodException var2) {
         }

         initialized = true;
      }

   }
}
