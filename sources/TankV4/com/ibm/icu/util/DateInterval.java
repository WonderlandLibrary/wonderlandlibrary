package com.ibm.icu.util;

import java.io.Serializable;

public final class DateInterval implements Serializable {
   private static final long serialVersionUID = 1L;
   private final long fromDate;
   private final long toDate;

   public DateInterval(long var1, long var3) {
      this.fromDate = var1;
      this.toDate = var3;
   }

   public long getFromDate() {
      return this.fromDate;
   }

   public long getToDate() {
      return this.toDate;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof DateInterval)) {
         return false;
      } else {
         DateInterval var2 = (DateInterval)var1;
         return this.fromDate == var2.fromDate && this.toDate == var2.toDate;
      }
   }

   public int hashCode() {
      return (int)(this.fromDate + this.toDate);
   }

   public String toString() {
      return this.fromDate + " " + this.toDate;
   }
}
