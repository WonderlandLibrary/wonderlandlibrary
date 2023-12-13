package com.sun.javafx.applet;

import com.sun.applet2.Applet2Context;
import com.sun.applet2.Applet2Host;
import netscape.javascript.JSObject;
import sun.plugin2.applet2.Plugin2Host;

public class ExperimentalExtensions {
   Applet2Context a2c;
   private static ExperimentalExtensions instance = null;

   public static synchronized ExperimentalExtensions get() {
      return instance;
   }

   public static synchronized void init(Applet2Context var0) {
      instance = new ExperimentalExtensions(var0);
   }

   private ExperimentalExtensions(Applet2Context var1) {
      this.a2c = var1;
   }

   public JSObject getOneWayJSObject() {
      Applet2Host var1 = this.a2c.getHost();
      if (var1 instanceof Plugin2Host) {
         try {
            return ((Plugin2Host)var1).getOneWayJSObject();
         } catch (Exception var3) {
         }
      }

      return null;
   }

   public Splash getSplash() {
      return new Splash(this.a2c);
   }
}
