package org.alphacentauri.management.util;

public class Timer {
   long startTime;

   public Timer() {
      this.reset();
   }

   public void add(int amount) {
      this.startTime -= (long)amount;
   }

   public void reset() {
      this.startTime = System.currentTimeMillis();
   }

   public void subtract(int amount) {
      this.startTime += (long)amount;
   }

   public boolean hasMSPassed(long toPass) {
      return this.getMSPassed() >= toPass;
   }

   public long getMSPassed() {
      return System.currentTimeMillis() - this.startTime;
   }
}
