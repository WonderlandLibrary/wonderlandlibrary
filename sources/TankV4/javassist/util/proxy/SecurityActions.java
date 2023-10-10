package javassist.util.proxy;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

class SecurityActions {
   static Method[] getDeclaredMethods(Class var0) {
      return System.getSecurityManager() == null ? var0.getDeclaredMethods() : (Method[])((Method[])AccessController.doPrivileged(new PrivilegedAction(var0) {
         final Class val$clazz;

         {
            this.val$clazz = var1;
         }

         public Object run() {
            return this.val$clazz.getDeclaredMethods();
         }
      }));
   }

   static Constructor[] getDeclaredConstructors(Class var0) {
      return System.getSecurityManager() == null ? var0.getDeclaredConstructors() : (Constructor[])((Constructor[])AccessController.doPrivileged(new PrivilegedAction(var0) {
         final Class val$clazz;

         {
            this.val$clazz = var1;
         }

         public Object run() {
            return this.val$clazz.getDeclaredConstructors();
         }
      }));
   }

   static Method getDeclaredMethod(Class var0, String var1, Class[] var2) throws NoSuchMethodException {
      if (System.getSecurityManager() == null) {
         return var0.getDeclaredMethod(var1, var2);
      } else {
         try {
            return (Method)AccessController.doPrivileged(new PrivilegedExceptionAction(var0, var1, var2) {
               final Class val$clazz;
               final String val$name;
               final Class[] val$types;

               {
                  this.val$clazz = var1;
                  this.val$name = var2;
                  this.val$types = var3;
               }

               public Object run() throws Exception {
                  return this.val$clazz.getDeclaredMethod(this.val$name, this.val$types);
               }
            });
         } catch (PrivilegedActionException var4) {
            if (var4.getCause() instanceof NoSuchMethodException) {
               throw (NoSuchMethodException)var4.getCause();
            } else {
               throw new RuntimeException(var4.getCause());
            }
         }
      }
   }

   static Constructor getDeclaredConstructor(Class var0, Class[] var1) throws NoSuchMethodException {
      if (System.getSecurityManager() == null) {
         return var0.getDeclaredConstructor(var1);
      } else {
         try {
            return (Constructor)AccessController.doPrivileged(new PrivilegedExceptionAction(var0, var1) {
               final Class val$clazz;
               final Class[] val$types;

               {
                  this.val$clazz = var1;
                  this.val$types = var2;
               }

               public Object run() throws Exception {
                  return this.val$clazz.getDeclaredConstructor(this.val$types);
               }
            });
         } catch (PrivilegedActionException var3) {
            if (var3.getCause() instanceof NoSuchMethodException) {
               throw (NoSuchMethodException)var3.getCause();
            } else {
               throw new RuntimeException(var3.getCause());
            }
         }
      }
   }

   static void setAccessible(AccessibleObject var0, boolean var1) {
      if (System.getSecurityManager() == null) {
         var0.setAccessible(var1);
      } else {
         AccessController.doPrivileged(new PrivilegedAction(var0, var1) {
            final AccessibleObject val$ao;
            final boolean val$accessible;

            {
               this.val$ao = var1;
               this.val$accessible = var2;
            }

            public Object run() {
               this.val$ao.setAccessible(this.val$accessible);
               return null;
            }
         });
      }

   }

   static void set(Field var0, Object var1, Object var2) throws IllegalAccessException {
      if (System.getSecurityManager() == null) {
         var0.set(var1, var2);
      } else {
         try {
            AccessController.doPrivileged(new PrivilegedExceptionAction(var0, var1, var2) {
               final Field val$fld;
               final Object val$target;
               final Object val$value;

               {
                  this.val$fld = var1;
                  this.val$target = var2;
                  this.val$value = var3;
               }

               public Object run() throws Exception {
                  this.val$fld.set(this.val$target, this.val$value);
                  return null;
               }
            });
         } catch (PrivilegedActionException var4) {
            if (var4.getCause() instanceof NoSuchMethodException) {
               throw (IllegalAccessException)var4.getCause();
            }

            throw new RuntimeException(var4.getCause());
         }
      }

   }
}
