package com.ibm.icu.util;

public class TimeZoneTransition {
   private final TimeZoneRule from;
   private final TimeZoneRule to;
   private final long time;

   public TimeZoneTransition(long var1, TimeZoneRule var3, TimeZoneRule var4) {
      this.time = var1;
      this.from = var3;
      this.to = var4;
   }

   public long getTime() {
      return this.time;
   }

   public TimeZoneRule getTo() {
      return this.to;
   }

   public TimeZoneRule getFrom() {
      return this.from;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("time=" + this.time);
      var1.append(", from={" + this.from + "}");
      var1.append(", to={" + this.to + "}");
      return var1.toString();
   }
}
