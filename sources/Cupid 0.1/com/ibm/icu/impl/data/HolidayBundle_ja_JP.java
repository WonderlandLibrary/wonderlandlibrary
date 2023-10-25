package com.ibm.icu.impl.data;

import com.ibm.icu.util.Holiday;
import com.ibm.icu.util.SimpleHoliday;
import java.util.ListResourceBundle;

public class HolidayBundle_ja_JP extends ListResourceBundle {
  private static final Holiday[] fHolidays = new Holiday[] { (Holiday)new SimpleHoliday(1, 11, 0, "National Foundation Day") };
  
  private static final Object[][] fContents = new Object[][] { { "holidays", fHolidays } };
  
  public synchronized Object[][] getContents() {
    return fContents;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\data\HolidayBundle_ja_JP.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */