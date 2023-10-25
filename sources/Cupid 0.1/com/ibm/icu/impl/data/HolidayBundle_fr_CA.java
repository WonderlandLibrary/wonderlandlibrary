package com.ibm.icu.impl.data;

import com.ibm.icu.util.EasterHoliday;
import com.ibm.icu.util.Holiday;
import com.ibm.icu.util.SimpleHoliday;
import java.util.ListResourceBundle;

public class HolidayBundle_fr_CA extends ListResourceBundle {
  private static final Holiday[] fHolidays = new Holiday[] { 
      (Holiday)new SimpleHoliday(0, 1, 0, "New Year's Day"), (Holiday)new SimpleHoliday(4, 19, 0, "Victoria Day"), (Holiday)new SimpleHoliday(5, 24, 0, "National Day"), (Holiday)new SimpleHoliday(6, 1, 0, "Canada Day"), (Holiday)new SimpleHoliday(7, 1, 2, "Civic Holiday"), (Holiday)new SimpleHoliday(8, 1, 2, "Labour Day"), (Holiday)new SimpleHoliday(9, 8, 2, "Thanksgiving"), (Holiday)new SimpleHoliday(10, 11, 0, "Remembrance Day"), (Holiday)SimpleHoliday.CHRISTMAS, (Holiday)SimpleHoliday.BOXING_DAY, 
      (Holiday)SimpleHoliday.NEW_YEARS_EVE, (Holiday)EasterHoliday.GOOD_FRIDAY, (Holiday)EasterHoliday.EASTER_SUNDAY, (Holiday)EasterHoliday.EASTER_MONDAY };
  
  private static final Object[][] fContents = new Object[][] { { "holidays", fHolidays } };
  
  public synchronized Object[][] getContents() {
    return fContents;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\data\HolidayBundle_fr_CA.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */