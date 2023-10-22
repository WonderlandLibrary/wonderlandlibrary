package com.sun.javafx.property.adapter;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Disposer implements Runnable {
   private static final ReferenceQueue queue = new ReferenceQueue();
   private static final Map records = new ConcurrentHashMap();
   private static Disposer disposerInstance = new Disposer();

   public static void addRecord(Object var0, Runnable var1) {
      PhantomReference var2 = new PhantomReference(var0, queue);
      records.put(var2, var1);
   }

   public void run() {
      while(true) {
         try {
            Reference var1 = queue.remove();
            ((Reference)var1).clear();
            Runnable var2 = (Runnable)records.remove(var1);
            var2.run();
         } catch (Exception var3) {
            System.out.println("Exception while removing reference: " + var3);
            var3.printStackTrace();
         }
      }
   }

   static {
      AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            ThreadGroup var1 = Thread.currentThread().getThreadGroup();

            for(ThreadGroup var2 = var1; var2 != null; var2 = var2.getParent()) {
               var1 = var2;
            }

            Thread var3 = new Thread(var1, Disposer.disposerInstance, "Property Disposer");
            var3.setContextClassLoader((ClassLoader)null);
            var3.setDaemon(true);
            var3.setPriority(10);
            var3.start();
            return null;
         }
      });
   }
}
