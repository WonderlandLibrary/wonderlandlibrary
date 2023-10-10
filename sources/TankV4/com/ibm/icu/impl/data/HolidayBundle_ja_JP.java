package com.ibm.icu.impl.data;

import com.ibm.icu.util.Holiday;
import com.ibm.icu.util.SimpleHoliday;
import java.util.ListResourceBundle;

public class HolidayBundle_ja_JP extends ListResourceBundle {
   private static final Holiday[] fHolidays = new Holiday[]{new SimpleHoliday(1, 11, 0, "National Foundation Day")};
   private static final Object[][] fContents;

   public synchronized Object[][] getContents() {
      return fContents;
   }

   static {
      fContents = new Object[][]{{"holidays", fHolidays}};
   }
}
