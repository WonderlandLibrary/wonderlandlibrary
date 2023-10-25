package com.ibm.icu.impl.data;

import com.ibm.icu.util.EasterHoliday;
import com.ibm.icu.util.Holiday;
import com.ibm.icu.util.SimpleHoliday;
import java.util.ListResourceBundle;

public class HolidayBundle_en_US extends ListResourceBundle {
  private static final Holiday[] fHolidays = new Holiday[] { 
      (Holiday)SimpleHoliday.NEW_YEARS_DAY, (Holiday)new SimpleHoliday(0, 15, 2, "Martin Luther King Day", 1986), (Holiday)new SimpleHoliday(1, 15, 2, "Presidents' Day", 1976), (Holiday)new SimpleHoliday(1, 22, "Washington's Birthday", 1776, 1975), (Holiday)EasterHoliday.GOOD_FRIDAY, (Holiday)EasterHoliday.EASTER_SUNDAY, (Holiday)new SimpleHoliday(4, 8, 1, "Mother's Day", 1914), (Holiday)new SimpleHoliday(4, 31, -2, "Memorial Day", 1971), (Holiday)new SimpleHoliday(4, 30, "Memorial Day", 1868, 1970), (Holiday)new SimpleHoliday(5, 15, 1, "Father's Day", 1956), 
      (Holiday)new SimpleHoliday(6, 4, "Independence Day", 1776), (Holiday)new SimpleHoliday(8, 1, 2, "Labor Day", 1894), (Holiday)new SimpleHoliday(10, 2, 3, "Election Day"), (Holiday)new SimpleHoliday(9, 8, 2, "Columbus Day", 1971), (Holiday)new SimpleHoliday(9, 31, "Halloween"), (Holiday)new SimpleHoliday(10, 11, "Veterans' Day", 1918), (Holiday)new SimpleHoliday(10, 22, 5, "Thanksgiving", 1863), (Holiday)SimpleHoliday.CHRISTMAS };
  
  private static final Object[][] fContents = new Object[][] { { "holidays", fHolidays } };
  
  public synchronized Object[][] getContents() {
    return fContents;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\data\HolidayBundle_en_US.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */