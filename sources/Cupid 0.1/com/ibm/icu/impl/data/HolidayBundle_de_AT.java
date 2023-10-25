package com.ibm.icu.impl.data;

import com.ibm.icu.util.EasterHoliday;
import com.ibm.icu.util.Holiday;
import com.ibm.icu.util.SimpleHoliday;
import java.util.ListResourceBundle;

public class HolidayBundle_de_AT extends ListResourceBundle {
  private static final Holiday[] fHolidays = new Holiday[] { 
      (Holiday)SimpleHoliday.NEW_YEARS_DAY, (Holiday)SimpleHoliday.EPIPHANY, (Holiday)EasterHoliday.GOOD_FRIDAY, (Holiday)EasterHoliday.EASTER_SUNDAY, (Holiday)EasterHoliday.EASTER_MONDAY, (Holiday)EasterHoliday.ASCENSION, (Holiday)EasterHoliday.WHIT_SUNDAY, (Holiday)EasterHoliday.WHIT_MONDAY, (Holiday)EasterHoliday.CORPUS_CHRISTI, (Holiday)SimpleHoliday.ASSUMPTION, 
      (Holiday)SimpleHoliday.ALL_SAINTS_DAY, (Holiday)SimpleHoliday.IMMACULATE_CONCEPTION, (Holiday)SimpleHoliday.CHRISTMAS, (Holiday)SimpleHoliday.ST_STEPHENS_DAY, (Holiday)new SimpleHoliday(4, 1, 0, "National Holiday"), (Holiday)new SimpleHoliday(9, 31, -2, "National Holiday") };
  
  private static final Object[][] fContents = new Object[][] { { "holidays", fHolidays }, { "Christmas", "Christtag" }, { "New Year's Day", "Neujahrstag" } };
  
  public synchronized Object[][] getContents() {
    return fContents;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\data\HolidayBundle_de_AT.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */