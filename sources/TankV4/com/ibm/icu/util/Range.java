package com.ibm.icu.util;

import java.util.Date;

class Range {
   public Date start;
   public DateRule rule;

   public Range(Date var1, DateRule var2) {
      this.start = var1;
      this.rule = var2;
   }
}
