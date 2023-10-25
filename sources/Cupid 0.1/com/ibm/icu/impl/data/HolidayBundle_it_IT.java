package com.ibm.icu.impl.data;

import com.ibm.icu.util.EasterHoliday;
import com.ibm.icu.util.Holiday;
import com.ibm.icu.util.SimpleHoliday;
import java.util.ListResourceBundle;

public class HolidayBundle_it_IT extends ListResourceBundle {
  private static final Holiday[] fHolidays = new Holiday[] { 
      (Holiday)SimpleHoliday.NEW_YEARS_DAY, (Holiday)SimpleHoliday.EPIPHANY, (Holiday)new SimpleHoliday(3, 1, 0, "Liberation Day"), (Holiday)new SimpleHoliday(4, 1, 0, "Labor Day"), (Holiday)SimpleHoliday.ASSUMPTION, (Holiday)SimpleHoliday.ALL_SAINTS_DAY, (Holiday)SimpleHoliday.IMMACULATE_CONCEPTION, (Holiday)SimpleHoliday.CHRISTMAS, (Holiday)new SimpleHoliday(11, 26, 0, "St. Stephens Day"), (Holiday)SimpleHoliday.NEW_YEARS_EVE, 
      (Holiday)EasterHoliday.EASTER_SUNDAY, (Holiday)EasterHoliday.EASTER_MONDAY };
  
  private static final Object[][] fContents = new Object[][] { { "holidays", fHolidays } };
  
  public synchronized Object[][] getContents() {
    return fContents;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\data\HolidayBundle_it_IT.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */