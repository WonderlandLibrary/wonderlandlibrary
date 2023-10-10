package com.ibm.icu.impl;

import java.util.Date;
import java.util.TimeZone;

public class TimeZoneAdapter extends TimeZone {
   static final long serialVersionUID = -2040072218820018557L;
   private com.ibm.icu.util.TimeZone zone;

   public static TimeZone wrap(com.ibm.icu.util.TimeZone var0) {
      return new TimeZoneAdapter(var0);
   }

   public com.ibm.icu.util.TimeZone unwrap() {
      return this.zone;
   }

   public TimeZoneAdapter(com.ibm.icu.util.TimeZone var1) {
      this.zone = var1;
      super.setID(var1.getID());
   }

   public void setID(String var1) {
      super.setID(var1);
      this.zone.setID(var1);
   }

   public boolean hasSameRules(TimeZone var1) {
      return var1 instanceof TimeZoneAdapter && this.zone.hasSameRules(((TimeZoneAdapter)var1).zone);
   }

   public int getOffset(int var1, int var2, int var3, int var4, int var5, int var6) {
      return this.zone.getOffset(var1, var2, var3, var4, var5, var6);
   }

   public int getRawOffset() {
      return this.zone.getRawOffset();
   }

   public void setRawOffset(int var1) {
      this.zone.setRawOffset(var1);
   }

   public boolean useDaylightTime() {
      return this.zone.useDaylightTime();
   }

   public boolean inDaylightTime(Date var1) {
      return this.zone.inDaylightTime(var1);
   }

   public Object clone() {
      return new TimeZoneAdapter((com.ibm.icu.util.TimeZone)this.zone.clone());
   }

   public synchronized int hashCode() {
      return this.zone.hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 instanceof TimeZoneAdapter) {
         var1 = ((TimeZoneAdapter)var1).zone;
      }

      return this.zone.equals(var1);
   }

   public String toString() {
      return "TimeZoneAdapter: " + this.zone.toString();
   }
}
