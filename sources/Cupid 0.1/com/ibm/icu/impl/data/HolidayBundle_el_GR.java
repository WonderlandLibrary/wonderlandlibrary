package com.ibm.icu.impl.data;

import com.ibm.icu.util.EasterHoliday;
import com.ibm.icu.util.Holiday;
import com.ibm.icu.util.SimpleHoliday;
import java.util.ListResourceBundle;

public class HolidayBundle_el_GR extends ListResourceBundle {
  private static final Holiday[] fHolidays = new Holiday[] { 
      (Holiday)SimpleHoliday.NEW_YEARS_DAY, (Holiday)SimpleHoliday.EPIPHANY, (Holiday)new SimpleHoliday(2, 25, 0, "Independence Day"), (Holiday)SimpleHoliday.MAY_DAY, (Holiday)SimpleHoliday.ASSUMPTION, (Holiday)new SimpleHoliday(9, 28, 0, "Ochi Day"), (Holiday)SimpleHoliday.CHRISTMAS, (Holiday)SimpleHoliday.BOXING_DAY, (Holiday)new EasterHoliday(-2, true, "Good Friday"), (Holiday)new EasterHoliday(0, true, "Easter Sunday"), 
      (Holiday)new EasterHoliday(1, true, "Easter Monday"), (Holiday)new EasterHoliday(50, true, "Whit Monday") };
  
  private static final Object[][] fContents = new Object[][] { { "holidays", fHolidays } };
  
  public synchronized Object[][] getContents() {
    return fContents;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\data\HolidayBundle_el_GR.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */