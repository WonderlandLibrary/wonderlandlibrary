package com.sun.webkit.perf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PerfLogger {
   private static Thread shutdownHook;
   private static Map loggers;
   private final HashMap probes = new HashMap();
   private final Logger log;
   private final boolean isEnabled;
   private final Comparator timeComparator = (var1x, var2) -> {
      long var3 = ((ProbeStat)this.probes.get((String)var1x)).totalTime;
      long var5 = ((ProbeStat)this.probes.get((String)var2)).totalTime;
      if (var3 > var5) {
         return 1;
      } else {
         return var3 < var5 ? -1 : 0;
      }
   };
   private final Comparator countComparator = (var1x, var2) -> {
      long var3 = (long)((ProbeStat)this.probes.get((String)var1x)).count;
      long var5 = (long)((ProbeStat)this.probes.get((String)var2)).count;
      if (var3 > var5) {
         return 1;
      } else {
         return var3 < var5 ? -1 : 0;
      }
   };

   public static synchronized PerfLogger getLogger(Logger var0) {
      if (loggers == null) {
         loggers = new HashMap();
      }

      PerfLogger var1 = (PerfLogger)loggers.get(var0);
      if (var1 == null) {
         var1 = new PerfLogger(var0);
         loggers.put(var0, var1);
      }

      if (var1.isEnabled() && shutdownHook == null) {
         shutdownHook = new Thread() {
            public void run() {
               Iterator var1 = PerfLogger.loggers.values().iterator();

               while(var1.hasNext()) {
                  PerfLogger var2 = (PerfLogger)var1.next();
                  if (var2.isEnabled()) {
                     var2.log(false);
                  }
               }

            }
         };
         Runtime.getRuntime().addShutdownHook(shutdownHook);
      }

      return var1;
   }

   public static synchronized PerfLogger getLogger(String var0) {
      return getLogger(Logger.getLogger("com.sun.webkit.perf." + var0));
   }

   private PerfLogger(Logger var1) {
      this.log = var1;
      this.isEnabled = var1.isLoggable(Level.FINE);
      this.startCount("TOTALTIME");
   }

   public boolean isEnabled() {
      return this.isEnabled;
   }

   private synchronized String fullName(String var1) {
      return this.log.getName() + "." + var1;
   }

   public synchronized void reset() {
      Iterator var1 = this.probes.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry var2 = (Map.Entry)var1.next();
         ((ProbeStat)var2.getValue()).reset();
      }

      this.startCount("TOTALTIME");
   }

   public static synchronized void resetAll() {
      Iterator var0 = loggers.values().iterator();

      while(var0.hasNext()) {
         PerfLogger var1 = (PerfLogger)var0.next();
         var1.reset();
      }

   }

   private synchronized ProbeStat registerProbe(String var1) {
      String var2 = var1.intern();
      if (this.probes.containsKey(var2)) {
         this.log.fine("Warning: \"" + this.fullName(var2) + "\" probe already exists");
      } else {
         this.log.fine("Registering \"" + this.fullName(var2) + "\" probe");
      }

      ProbeStat var3 = new ProbeStat(var2);
      this.probes.put(var2, var3);
      return var3;
   }

   public synchronized ProbeStat getProbeStat(String var1) {
      String var2 = var1.intern();
      ProbeStat var3 = (ProbeStat)this.probes.get(var2);
      if (var3 != null) {
         var3.snapshot();
      }

      return var3;
   }

   public synchronized void startCount(String var1) {
      if (this.isEnabled()) {
         String var2 = var1.intern();
         ProbeStat var3 = (ProbeStat)this.probes.get(var2);
         if (var3 == null) {
            var3 = this.registerProbe(var2);
         }

         var3.reset();
         var3.resume();
      }
   }

   public synchronized void suspendCount(String var1) {
      if (this.isEnabled()) {
         String var2 = var1.intern();
         ProbeStat var3 = (ProbeStat)this.probes.get(var2);
         if (var3 != null) {
            var3.suspend();
         } else {
            this.log.fine("Warning: \"" + this.fullName(var2) + "\" probe is not registered");
         }

      }
   }

   public synchronized void resumeCount(String var1) {
      if (this.isEnabled()) {
         String var2 = var1.intern();
         ProbeStat var3 = (ProbeStat)this.probes.get(var2);
         if (var3 == null) {
            var3 = this.registerProbe(var2);
         }

         var3.resume();
      }
   }

   public synchronized void log(StringBuffer var1) {
      if (this.isEnabled()) {
         var1.append("=========== Performance Statistics =============\n");
         ProbeStat var2 = this.getProbeStat("TOTALTIME");
         ArrayList var3 = new ArrayList();
         var3.addAll(this.probes.keySet());
         var1.append("\nTime:\n");
         Collections.sort(var3, this.timeComparator);
         Iterator var4 = var3.iterator();

         String var5;
         while(var4.hasNext()) {
            var5 = (String)var4.next();
            ProbeStat var6 = this.getProbeStat(var5);
            var1.append(String.format("%s: %dms", this.fullName(var5), var6.totalTime));
            if (var2.totalTime > 0L) {
               var1.append(String.format(", %.2f%%%n", 100.0F * (float)var6.totalTime / (float)var2.totalTime));
            } else {
               var1.append("\n");
            }
         }

         var1.append("\nInvocations count:\n");
         Collections.sort(var3, this.countComparator);
         var4 = var3.iterator();

         while(var4.hasNext()) {
            var5 = (String)var4.next();
            var1.append(String.format("%s: %d%n", this.fullName(var5), this.getProbeStat(var5).count));
         }

         var1.append("================================================\n");
      }
   }

   public synchronized void log() {
      this.log(true);
   }

   private synchronized void log(boolean var1) {
      StringBuffer var2 = new StringBuffer();
      this.log(var2);
      if (var1) {
         this.log.fine(var2.toString());
      } else {
         System.out.println(var2.toString());
         System.out.flush();
      }

   }

   public static synchronized void logAll() {
      Iterator var0 = loggers.values().iterator();

      while(var0.hasNext()) {
         PerfLogger var1 = (PerfLogger)var0.next();
         var1.log();
      }

   }

   public static final class ProbeStat {
      private final String probe;
      private int count;
      private long totalTime;
      private long startTime;
      private boolean isRunning;

      private ProbeStat(String var1) {
         this.isRunning = false;
         this.probe = var1;
      }

      public String getProbe() {
         return this.probe;
      }

      public int getCount() {
         return this.count;
      }

      public long getTotalTime() {
         return this.totalTime;
      }

      private void reset() {
         this.count = 0;
         this.totalTime = this.startTime = 0L;
      }

      private void suspend() {
         if (this.isRunning) {
            this.totalTime += System.currentTimeMillis() - this.startTime;
            this.isRunning = false;
         }

      }

      private void resume() {
         this.isRunning = true;
         ++this.count;
         this.startTime = System.currentTimeMillis();
      }

      private void snapshot() {
         if (this.isRunning) {
            this.totalTime += System.currentTimeMillis() - this.startTime;
            this.startTime = System.currentTimeMillis();
         }

      }

      public String toString() {
         return super.toString() + "[count=" + this.count + ", time=" + this.totalTime + "]";
      }

      // $FF: synthetic method
      ProbeStat(String var1, Object var2) {
         this(var1);
      }
   }
}
