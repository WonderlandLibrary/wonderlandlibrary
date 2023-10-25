package com.ibm.icu.impl.data;

import com.ibm.icu.util.Holiday;
import com.ibm.icu.util.SimpleHoliday;
import java.util.ListResourceBundle;

public class HolidayBundle_es_MX extends ListResourceBundle {
  private static final Holiday[] fHolidays = new Holiday[] { 
      (Holiday)SimpleHoliday.NEW_YEARS_DAY, (Holiday)new SimpleHoliday(1, 5, 0, "Constitution Day"), (Holiday)new SimpleHoliday(2, 21, 0, "Benito Juárez Day"), (Holiday)SimpleHoliday.MAY_DAY, (Holiday)new SimpleHoliday(4, 5, 0, "Cinco de Mayo"), (Holiday)new SimpleHoliday(5, 1, 0, "Navy Day"), (Holiday)new SimpleHoliday(8, 16, 0, "Independence Day"), (Holiday)new SimpleHoliday(9, 12, 0, "Día de la Raza"), (Holiday)SimpleHoliday.ALL_SAINTS_DAY, (Holiday)new SimpleHoliday(10, 2, 0, "Day of the Dead"), 
      (Holiday)new SimpleHoliday(10, 20, 0, "Revolution Day"), (Holiday)new SimpleHoliday(11, 12, 0, "Flag Day"), (Holiday)SimpleHoliday.CHRISTMAS };
  
  private static final Object[][] fContents = new Object[][] { { "holidays", fHolidays } };
  
  public synchronized Object[][] getContents() {
    return fContents;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\data\HolidayBundle_es_MX.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */