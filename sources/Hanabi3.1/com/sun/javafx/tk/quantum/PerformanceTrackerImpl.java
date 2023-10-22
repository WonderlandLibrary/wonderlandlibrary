package com.sun.javafx.tk.quantum;

import com.sun.javafx.perf.PerformanceTracker;

final class PerformanceTrackerImpl extends PerformanceTracker {
   final PerformanceTrackerHelper helper = PerformanceTrackerHelper.getInstance();

   public PerformanceTrackerImpl() {
      this.setPerfLoggingEnabled(this.helper.isPerfLoggingEnabled());
   }

   public void doLogEvent(String var1) {
      this.helper.logEvent(var1);
   }

   public void doOutputLog() {
      this.helper.outputLog();
   }

   public long nanoTime() {
      return this.helper.nanoTime();
   }
}
