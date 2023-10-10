package com.ibm.icu.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RangeDateRule implements DateRule {
   List ranges = new ArrayList(2);

   public void add(DateRule var1) {
      this.add(new Date(Long.MIN_VALUE), var1);
   }

   public void add(Date var1, DateRule var2) {
      this.ranges.add(new Range(var1, var2));
   }

   public Date firstAfter(Date var1) {
      int var2 = this.startIndex(var1);
      if (var2 == this.ranges.size()) {
         var2 = 0;
      }

      Date var3 = null;
      Range var4 = this.rangeAt(var2);
      Range var5 = this.rangeAt(var2 + 1);
      if (var4 != null && var4.rule != null) {
         if (var5 != null) {
            var3 = var4.rule.firstBetween(var1, var5.start);
         } else {
            var3 = var4.rule.firstAfter(var1);
         }
      }

      return var3;
   }

   public Date firstBetween(Date var1, Date var2) {
      if (var2 == null) {
         return this.firstAfter(var1);
      } else {
         int var3 = this.startIndex(var1);
         Date var4 = null;
         Range var5 = this.rangeAt(var3);

         while(var4 == null && var5 != null && !var5.start.after(var2)) {
            Range var6 = var5;
            var5 = this.rangeAt(var3 + 1);
            if (var6.rule != null) {
               Date var7 = var5 != null && !var5.start.after(var2) ? var5.start : var2;
               var4 = var6.rule.firstBetween(var1, var7);
            }
         }

         return var4;
      }
   }

   public boolean isOn(Date var1) {
      Range var2 = this.rangeAt(this.startIndex(var1));
      return var2 != null && var2.rule != null && var2.rule.isOn(var1);
   }

   public boolean isBetween(Date var1, Date var2) {
      return this.firstBetween(var1, var2) == null;
   }

   private int startIndex(Date var1) {
      int var2 = this.ranges.size();

      for(int var3 = 0; var3 < this.ranges.size(); var2 = var3++) {
         Range var4 = (Range)this.ranges.get(var3);
         if (var1.before(var4.start)) {
            break;
         }
      }

      return var2;
   }

   private Range rangeAt(int var1) {
      return var1 < this.ranges.size() ? (Range)this.ranges.get(var1) : null;
   }
}
