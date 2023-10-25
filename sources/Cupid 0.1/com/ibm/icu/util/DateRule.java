package com.ibm.icu.util;

import java.util.Date;

public interface DateRule {
  Date firstAfter(Date paramDate);
  
  Date firstBetween(Date paramDate1, Date paramDate2);
  
  boolean isOn(Date paramDate);
  
  boolean isBetween(Date paramDate1, Date paramDate2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\DateRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */