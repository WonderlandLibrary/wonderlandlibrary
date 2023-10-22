package com.sun.javafx.applet;

import com.sun.applet2.Applet2Context;
import com.sun.applet2.Applet2Host;
import com.sun.javafx.application.HostServicesDelegate;
import java.net.URI;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javafx.application.Application;
import netscape.javascript.JSObject;
import sun.plugin2.applet2.Plugin2Host;

public class HostServicesImpl extends HostServicesDelegate {
   private static HostServicesDelegate instance = null;
   private Applet2Context a2c = null;

   private HostServicesImpl(Applet2Context var1) {
      this.a2c = var1;
   }

   public static void init(Applet2Context var0) {
      instance = new HostServicesImpl(var0);
   }

   public static HostServicesDelegate getInstance(Application var0) {
      return instance;
   }

   public String getCodeBase() {
      return this.a2c.getCodeBase().toExternalForm();
   }

   public String getDocumentBase() {
      return this.a2c.getHost().getDocumentBase().toExternalForm();
   }

   private URL toURL(String var1) {
      try {
         return (new URI(var1)).toURL();
      } catch (RuntimeException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new IllegalArgumentException(var4);
      }
   }

   public void showDocument(String var1) {
      this.a2c.getHost().showDocument(this.toURL(var1), "_blank");
   }

   public JSObject getWebContext() {
      try {
         return (JSObject)AccessController.doPrivileged(new WCGetter());
      } catch (PrivilegedActionException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   class WCGetter implements PrivilegedExceptionAction {
      public JSObject run() {
         Applet2Host var1 = HostServicesImpl.this.a2c.getHost();
         if (var1 instanceof Plugin2Host) {
            try {
               return ((Plugin2Host)var1).getJSObject();
            } catch (Exception var3) {
            }
         }

         return null;
      }
   }
}
