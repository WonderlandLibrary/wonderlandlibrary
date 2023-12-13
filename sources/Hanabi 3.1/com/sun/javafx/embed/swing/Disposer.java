package com.sun.javafx.embed.swing;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;

public class Disposer implements Runnable {
   private static final ReferenceQueue queue = new ReferenceQueue();
   private static final Hashtable records = new Hashtable();
   private static Disposer disposerInstance = new Disposer();

   public static WeakReference addRecord(Object var0, DisposerRecord var1) {
      WeakReference var2 = new WeakReference(var0, queue);
      Disposer var10000 = disposerInstance;
      records.put(var2, var1);
      return var2;
   }

   public void run() {
      while(true) {
         try {
            Reference var1 = queue.remove();
            ((Reference)var1).clear();
            DisposerRecord var2 = (DisposerRecord)records.remove(var1);
            var2.dispose();
            var1 = null;
            var2 = null;
         } catch (Exception var3) {
            System.out.println("Exception while removing reference: " + var3);
            var3.printStackTrace();
         }
      }
   }

   static {
      ThreadGroup var0 = Thread.currentThread().getThreadGroup();
      AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            ThreadGroup var1 = Thread.currentThread().getThreadGroup();

            for(ThreadGroup var2 = var1; var2 != null; var2 = var2.getParent()) {
               var1 = var2;
            }

            Thread var3 = new Thread(var1, Disposer.disposerInstance, "SwingNode Disposer");
            var3.setContextClassLoader((ClassLoader)null);
            var3.setDaemon(true);
            var3.setPriority(10);
            var3.start();
            return null;
         }
      });
   }
}
