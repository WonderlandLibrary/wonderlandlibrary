package com.ibm.icu.impl.data;

import com.ibm.icu.util.EasterHoliday;
import com.ibm.icu.util.Holiday;
import com.ibm.icu.util.SimpleHoliday;
import java.util.ListResourceBundle;

public class HolidayBundle_de_DE extends ListResourceBundle {
  private static final Holiday[] fHolidays = new Holiday[] { 
      (Holiday)SimpleHoliday.NEW_YEARS_DAY, (Holiday)SimpleHoliday.MAY_DAY, (Holiday)new SimpleHoliday(5, 15, 4, "Memorial Day"), (Holiday)new SimpleHoliday(9, 3, 0, "Unity Day"), (Holiday)SimpleHoliday.ALL_SAINTS_DAY, (Holiday)new SimpleHoliday(10, 18, 0, "Day of Prayer and Repentance"), (Holiday)SimpleHoliday.CHRISTMAS, (Holiday)SimpleHoliday.BOXING_DAY, (Holiday)EasterHoliday.GOOD_FRIDAY, (Holiday)EasterHoliday.EASTER_SUNDAY, 
      (Holiday)EasterHoliday.EASTER_MONDAY, (Holiday)EasterHoliday.ASCENSION, (Holiday)EasterHoliday.WHIT_SUNDAY, (Holiday)EasterHoliday.WHIT_MONDAY };
  
  private static final Object[][] fContents = new Object[][] { { "holidays", fHolidays } };
  
  public synchronized Object[][] getContents() {
    return fContents;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\data\HolidayBundle_de_DE.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */