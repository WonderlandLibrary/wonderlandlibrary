package com.ibm.icu.impl;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;

public abstract class ICUNotifier {
   private final Object notifyLock = new Object();
   private ICUNotifier.NotifyThread notifyThread;
   private List listeners;

   public void addListener(EventListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else if (!this.acceptsListener(var1)) {
         throw new IllegalStateException("Listener invalid for this notifier.");
      } else {
         Object var2;
         synchronized(var2 = this.notifyLock){}
         if (this.listeners == null) {
            this.listeners = new ArrayList();
         } else {
            Iterator var3 = this.listeners.iterator();

            while(var3.hasNext()) {
               EventListener var4 = (EventListener)var3.next();
               if (var4 == var1) {
                  return;
               }
            }
         }

         this.listeners.add(var1);
      }
   }

   public void removeListener(EventListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         Object var2;
         synchronized(var2 = this.notifyLock){}
         if (this.listeners != null) {
            Iterator var3 = this.listeners.iterator();

            while(var3.hasNext()) {
               if (var3.next() == var1) {
                  var3.remove();
                  if (this.listeners.size() == 0) {
                     this.listeners = null;
                  }

                  return;
               }
            }
         }

      }
   }

   public void notifyChanged() {
      if (this.listeners != null) {
         Object var1;
         synchronized(var1 = this.notifyLock){}
         if (this.listeners != null) {
            if (this.notifyThread == null) {
               this.notifyThread = new ICUNotifier.NotifyThread(this);
               this.notifyThread.setDaemon(true);
               this.notifyThread.start();
            }

            this.notifyThread.queue((EventListener[])this.listeners.toArray(new EventListener[this.listeners.size()]));
         }
      }

   }

   protected abstract boolean acceptsListener(EventListener var1);

   protected abstract void notifyListener(EventListener var1);

   private static class NotifyThread extends Thread {
      private final ICUNotifier notifier;
      private final List queue = new ArrayList();

      NotifyThread(ICUNotifier var1) {
         this.notifier = var1;
      }

      public void queue(EventListener[] var1) {
         synchronized(this){}
         this.queue.add(var1);
         this.notify();
      }

      public void run() {
         while(true) {
            try {
               synchronized(this){}

               while(this.queue.isEmpty()) {
                  this.wait();
               }

               EventListener[] var1 = (EventListener[])this.queue.remove(0);

               for(int var2 = 0; var2 < var1.length; ++var2) {
                  this.notifier.notifyListener(var1[var2]);
               }
            } catch (InterruptedException var4) {
            }
         }
      }
   }
}
