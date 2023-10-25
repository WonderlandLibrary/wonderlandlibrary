package com.ibm.icu.impl.data;

import com.ibm.icu.util.EasterHoliday;
import com.ibm.icu.util.Holiday;
import com.ibm.icu.util.SimpleHoliday;
import java.util.ListResourceBundle;

public class HolidayBundle_fr_FR extends ListResourceBundle {
  private static final Holiday[] fHolidays = new Holiday[] { 
      (Holiday)SimpleHoliday.NEW_YEARS_DAY, (Holiday)new SimpleHoliday(4, 1, 0, "Labor Day"), (Holiday)new SimpleHoliday(4, 8, 0, "Victory Day"), (Holiday)new SimpleHoliday(6, 14, 0, "Bastille Day"), (Holiday)SimpleHoliday.ASSUMPTION, (Holiday)SimpleHoliday.ALL_SAINTS_DAY, (Holiday)new SimpleHoliday(10, 11, 0, "Armistice Day"), (Holiday)SimpleHoliday.CHRISTMAS, (Holiday)EasterHoliday.EASTER_SUNDAY, (Holiday)EasterHoliday.EASTER_MONDAY, 
      (Holiday)EasterHoliday.ASCENSION, (Holiday)EasterHoliday.WHIT_SUNDAY, (Holiday)EasterHoliday.WHIT_MONDAY };
  
  private static final Object[][] fContents = new Object[][] { { "holidays", fHolidays } };
  
  public synchronized Object[][] getContents() {
    return fContents;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\data\HolidayBundle_fr_FR.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */