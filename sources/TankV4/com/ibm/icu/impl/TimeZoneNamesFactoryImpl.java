package com.ibm.icu.impl;

import com.ibm.icu.text.TimeZoneNames;
import com.ibm.icu.util.ULocale;

public class TimeZoneNamesFactoryImpl extends TimeZoneNames.Factory {
   public TimeZoneNames getTimeZoneNames(ULocale var1) {
      return new TimeZoneNamesImpl(var1);
   }
}
