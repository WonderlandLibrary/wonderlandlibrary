package com.ibm.icu.impl.data;

import com.ibm.icu.util.HebrewHoliday;
import com.ibm.icu.util.Holiday;
import java.util.ListResourceBundle;

public class HolidayBundle_iw_IL extends ListResourceBundle {
  private static final Holiday[] fHolidays = new Holiday[] { (Holiday)HebrewHoliday.ROSH_HASHANAH, (Holiday)HebrewHoliday.YOM_KIPPUR, (Holiday)HebrewHoliday.HANUKKAH, (Holiday)HebrewHoliday.PURIM, (Holiday)HebrewHoliday.PASSOVER, (Holiday)HebrewHoliday.SHAVUOT, (Holiday)HebrewHoliday.SELIHOT };
  
  private static final Object[][] fContents = new Object[][] { { "holidays", fHolidays } };
  
  public synchronized Object[][] getContents() {
    return fContents;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\data\HolidayBundle_iw_IL.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */