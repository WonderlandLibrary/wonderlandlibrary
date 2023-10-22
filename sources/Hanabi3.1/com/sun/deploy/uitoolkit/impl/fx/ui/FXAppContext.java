package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.appcontext.AppContext;
import java.util.HashMap;

public class FXAppContext implements AppContext {
   private HashMap storage = new HashMap();
   private static FXAppContext theInstance = new FXAppContext();

   private FXAppContext() {
   }

   public static synchronized FXAppContext getInstance() {
      return theInstance;
   }

   public Object get(Object var1) {
      return this.storage.get(var1);
   }

   public Object put(Object var1, Object var2) {
      return this.storage.put(var1, var2);
   }

   public Object remove(Object var1) {
      return this.storage.remove(var1);
   }

   public void invokeLater(Runnable var1) {
      var1.run();
   }

   public void invokeAndWait(Runnable var1) {
      var1.run();
   }

   public ThreadGroup getThreadGroup() {
      return Thread.currentThread().getThreadGroup();
   }

   public void dispose() {
      this.storage.clear();
   }

   public boolean destroy(long var1) {
      return true;
   }
}
