package com.ibm.icu.impl.data;

import com.ibm.icu.util.EasterHoliday;
import com.ibm.icu.util.Holiday;
import com.ibm.icu.util.SimpleHoliday;
import java.util.ListResourceBundle;

public class HolidayBundle_en_GB extends ListResourceBundle {
  private static final Holiday[] fHolidays = new Holiday[] { (Holiday)SimpleHoliday.NEW_YEARS_DAY, (Holiday)SimpleHoliday.MAY_DAY, (Holiday)new SimpleHoliday(4, 31, -2, "Spring Holiday"), (Holiday)new SimpleHoliday(7, 31, -2, "Summer Bank Holiday"), (Holiday)SimpleHoliday.CHRISTMAS, (Holiday)SimpleHoliday.BOXING_DAY, (Holiday)new SimpleHoliday(11, 31, -2, "Christmas Holiday"), (Holiday)EasterHoliday.GOOD_FRIDAY, (Holiday)EasterHoliday.EASTER_SUNDAY, (Holiday)EasterHoliday.EASTER_MONDAY };
  
  private static final Object[][] fContents = new Object[][] { { "holidays", fHolidays }, { "Labor Day", "Labour Day" } };
  
  public synchronized Object[][] getContents() {
    return fContents;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\data\HolidayBundle_en_GB.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */