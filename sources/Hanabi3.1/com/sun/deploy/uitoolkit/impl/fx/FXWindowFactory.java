package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.deploy.uitoolkit.PluginWindowFactory;
import com.sun.deploy.uitoolkit.Window;
import java.util.concurrent.Callable;
import sun.plugin2.main.client.ModalityInterface;
import sun.plugin2.message.Pipe;

public class FXWindowFactory extends PluginWindowFactory {
   public Window createWindow() {
      return new FXWindow(0L, (String)null);
   }

   public Window createWindow(final long var1, final String var3, boolean var4, ModalityInterface var5) {
      try {
         return (Window)FXPluginToolkit.callAndWait(new Callable() {
            public FXWindow call() throws Exception {
               return new FXWindow(var1, var3);
            }
         });
      } catch (Exception var7) {
         throw new RuntimeException(var7);
      }
   }

   public Window createWindow(long var1, String var3, boolean var4, ModalityInterface var5, Pipe var6, int var7) {
      return this.createWindow(var1, var3, var4, var5);
   }
}
