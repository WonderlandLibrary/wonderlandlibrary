package org.apache.commons.logging.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.LogFactory;

public class ServletContextCleaner implements ServletContextListener {
   private static final Class[] RELEASE_SIGNATURE;
   static Class class$java$lang$ClassLoader;

   public void contextDestroyed(ServletContextEvent var1) {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      Object[] var3 = new Object[]{var2};
      ClassLoader var4 = var2;

      while(var4 != null) {
         try {
            Class var5 = var4.loadClass("org.apache.commons.logging.LogFactory");
            Method var6 = var5.getMethod("release", RELEASE_SIGNATURE);
            var6.invoke((Object)null, var3);
            var4 = var5.getClassLoader().getParent();
         } catch (ClassNotFoundException var7) {
            var4 = null;
         } catch (NoSuchMethodException var8) {
            System.err.println("LogFactory instance found which does not support release method!");
            var4 = null;
         } catch (IllegalAccessException var9) {
            System.err.println("LogFactory instance found which is not accessable!");
            var4 = null;
         } catch (InvocationTargetException var10) {
            System.err.println("LogFactory instance release method failed!");
            var4 = null;
         }
      }

      LogFactory.release(var2);
   }

   public void contextInitialized(ServletContextEvent var1) {
   }

   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      RELEASE_SIGNATURE = new Class[]{class$java$lang$ClassLoader == null ? (class$java$lang$ClassLoader = class$("java.lang.ClassLoader")) : class$java$lang$ClassLoader};
   }
}
