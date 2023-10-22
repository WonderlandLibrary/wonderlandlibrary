package com.sun.javafx.application;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import javafx.application.Application;
import netscape.javascript.JSObject;

public abstract class HostServicesDelegate {
   private static Method getInstanceMeth = null;

   public static HostServicesDelegate getInstance(Application var0) {
      HostServicesDelegate var1 = null;

      try {
         var1 = (HostServicesDelegate)AccessController.doPrivileged(() -> {
            if (getInstanceMeth == null) {
               try {
                  String var1 = "com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory";
                  Class var2 = Class.forName("com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory", true, HostServicesDelegate.class.getClassLoader());
                  getInstanceMeth = var2.getMethod("getInstance", Application.class);
               } catch (Exception var3) {
                  var3.printStackTrace();
                  return null;
               }
            }

            return (HostServicesDelegate)getInstanceMeth.invoke((Object)null, var0);
         });
         return var1;
      } catch (PrivilegedActionException var3) {
         System.err.println(var3.getException().toString());
         return null;
      }
   }

   protected HostServicesDelegate() {
   }

   public abstract String getCodeBase();

   public abstract String getDocumentBase();

   public abstract void showDocument(String var1);

   public abstract JSObject getWebContext();
}
