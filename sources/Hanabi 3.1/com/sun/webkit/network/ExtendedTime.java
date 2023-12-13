package com.sun.webkit.network;

final class ExtendedTime implements Comparable {
   private final long baseTime;
   private final int subtime;

   ExtendedTime(long var1, int var3) {
      this.baseTime = var1;
      this.subtime = var3;
   }

   static ExtendedTime currentTime() {
      return new ExtendedTime(System.currentTimeMillis(), 0);
   }

   long baseTime() {
      return this.baseTime;
   }

   int subtime() {
      return this.subtime;
   }

   ExtendedTime incrementSubtime() {
      return new ExtendedTime(this.baseTime, this.subtime + 1);
   }

   public int compareTo(ExtendedTime var1) {
      int var2 = (int)(this.baseTime - var1.baseTime);
      return var2 != 0 ? var2 : this.subtime - var1.subtime;
   }

   public String toString() {
      return "[baseTime=" + this.baseTime + ", subtime=" + this.subtime + "]";
   }
}
