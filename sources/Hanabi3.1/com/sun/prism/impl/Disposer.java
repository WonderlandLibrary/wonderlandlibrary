package com.sun.prism.impl;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.LinkedList;

public class Disposer {
   private static Disposer disposerInstance;
   private static final int WEAK = 0;
   private static final int PHANTOM = 1;
   private static final int SOFT = 2;
   private static int refType = 1;
   private final ReferenceQueue queue = new ReferenceQueue();
   private final Hashtable records = new Hashtable();
   private final LinkedList disposalQueue = new LinkedList();

   private Disposer() {
   }

   public static void addRecord(Object var0, Record var1) {
      disposerInstance.add(var0, var1);
   }

   public static void disposeRecord(Record var0) {
      disposerInstance.addToDisposalQueue(var0);
   }

   public static void cleanUp() {
      disposerInstance.disposeUnreachables();
      disposerInstance.processDisposalQueue();
   }

   private synchronized void add(Object var1, Record var2) {
      if (var1 instanceof Target) {
         var1 = ((Target)var1).getDisposerReferent();
      }

      Object var3;
      if (refType == 1) {
         var3 = new PhantomReference(var1, this.queue);
      } else if (refType == 2) {
         var3 = new SoftReference(var1, this.queue);
      } else {
         var3 = new WeakReference(var1, this.queue);
      }

      this.records.put(var3, var2);
   }

   private synchronized void addToDisposalQueue(Record var1) {
      this.disposalQueue.add(var1);
   }

   private synchronized void disposeUnreachables() {
      Reference var1;
      while((var1 = this.queue.poll()) != null) {
         try {
            ((Reference)var1).clear();
            Record var2 = (Record)this.records.remove(var1);
            var2.dispose();
            var1 = null;
            var2 = null;
         } catch (Exception var3) {
            System.out.println("Exception while removing reference: " + var3);
            var3.printStackTrace();
         }
      }

   }

   private synchronized void processDisposalQueue() {
      while(!this.disposalQueue.isEmpty()) {
         ((Record)this.disposalQueue.remove()).dispose();
      }

   }

   static {
      String var0 = PrismSettings.refType;
      if (var0 != null) {
         if (var0.equals("weak")) {
            refType = 0;
            if (PrismSettings.verbose) {
               System.err.println("Using WEAK refs");
            }
         } else if (var0.equals("soft")) {
            refType = 2;
            if (PrismSettings.verbose) {
               System.err.println("Using SOFT refs");
            }
         } else {
            refType = 1;
            if (PrismSettings.verbose) {
               System.err.println("Using PHANTOM refs");
            }
         }
      }

      disposerInstance = new Disposer();
   }

   public interface Target {
      Object getDisposerReferent();
   }

   public interface Record {
      void dispose();
   }
}
