package com.sun.javafx.applet;

import com.sun.applet2.Applet2Context;
import com.sun.applet2.AppletParameters;
import com.sun.deploy.trace.Trace;
import com.sun.deploy.trace.TraceLevel;
import netscape.javascript.JSObject;

public class Splash {
   Applet2Context a2c = null;

   public Splash(Applet2Context var1) {
      this.a2c = var1;
   }

   public void hide() {
      Runnable var1 = new Runnable() {
         public void run() {
            try {
               JSObject var1 = ExperimentalExtensions.get().getOneWayJSObject();
               if (var1 == null) {
                  Trace.println("Can not hide splash as Javascript handle is not avaialble", TraceLevel.UI);
                  return;
               }

               AppletParameters var2 = null;
               if (Splash.this.a2c != null) {
                  var2 = Splash.this.a2c.getParameters();
               }

               if (var2 != null && !"false".equals(var2.get("javafx_splash"))) {
                  String var3 = (String)var2.get("javafx_applet_id");
                  if (var3 != null) {
                     var1.eval("dtjava.hideSplash('" + var3 + "');");
                  } else {
                     Trace.println("Missing applet id parameter!");
                  }
               }
            } catch (Exception var4) {
               Trace.ignored(var4);
            }

         }
      };
      Thread var2 = new Thread(var1);
      var2.setDaemon(true);
      var2.start();
   }
}
