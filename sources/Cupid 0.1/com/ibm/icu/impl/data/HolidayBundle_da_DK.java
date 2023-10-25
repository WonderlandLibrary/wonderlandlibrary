package com.ibm.icu.impl.data;

import com.ibm.icu.util.EasterHoliday;
import com.ibm.icu.util.Holiday;
import com.ibm.icu.util.SimpleHoliday;
import java.util.ListResourceBundle;

public class HolidayBundle_da_DK extends ListResourceBundle {
  private static final Holiday[] fHolidays = new Holiday[] { 
      (Holiday)SimpleHoliday.NEW_YEARS_DAY, (Holiday)new SimpleHoliday(3, 30, -6, "General Prayer Day"), (Holiday)new SimpleHoliday(5, 5, "Constitution Day"), (Holiday)SimpleHoliday.CHRISTMAS_EVE, (Holiday)SimpleHoliday.CHRISTMAS, (Holiday)SimpleHoliday.BOXING_DAY, (Holiday)SimpleHoliday.NEW_YEARS_EVE, (Holiday)EasterHoliday.MAUNDY_THURSDAY, (Holiday)EasterHoliday.GOOD_FRIDAY, (Holiday)EasterHoliday.EASTER_SUNDAY, 
      (Holiday)EasterHoliday.EASTER_MONDAY, (Holiday)EasterHoliday.ASCENSION, (Holiday)EasterHoliday.WHIT_MONDAY };
  
  private static final Object[][] fContents = new Object[][] { { "holidays", fHolidays } };
  
  public synchronized Object[][] getContents() {
    return fContents;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\data\HolidayBundle_da_DK.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */