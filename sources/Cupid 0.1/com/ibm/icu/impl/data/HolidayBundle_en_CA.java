package com.ibm.icu.impl.data;

import com.ibm.icu.util.Holiday;
import com.ibm.icu.util.SimpleHoliday;
import java.util.ListResourceBundle;

public class HolidayBundle_en_CA extends ListResourceBundle {
  private static final Holiday[] fHolidays = new Holiday[] { (Holiday)SimpleHoliday.NEW_YEARS_DAY, (Holiday)new SimpleHoliday(4, 19, 0, "Victoria Day"), (Holiday)new SimpleHoliday(6, 1, 0, "Canada Day"), (Holiday)new SimpleHoliday(7, 1, 2, "Civic Holiday"), (Holiday)new SimpleHoliday(8, 1, 2, "Labor Day"), (Holiday)new SimpleHoliday(9, 8, 2, "Thanksgiving"), (Holiday)new SimpleHoliday(10, 11, 0, "Remembrance Day"), (Holiday)SimpleHoliday.CHRISTMAS, (Holiday)SimpleHoliday.BOXING_DAY, (Holiday)SimpleHoliday.NEW_YEARS_EVE };
  
  private static final Object[][] fContents = new Object[][] { { "holidays", fHolidays }, { "Labor Day", "Labour Day" } };
  
  public synchronized Object[][] getContents() {
    return fContents;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\data\HolidayBundle_en_CA.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */