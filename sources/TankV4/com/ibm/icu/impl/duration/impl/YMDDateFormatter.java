package com.ibm.icu.impl.duration.impl;

import com.ibm.icu.impl.duration.DateFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class YMDDateFormatter implements DateFormatter {
   private String requestedFields;
   private String localeName;
   private TimeZone timeZone;
   private SimpleDateFormat df;

   public YMDDateFormatter(String var1) {
      this(var1, Locale.getDefault().toString(), TimeZone.getDefault());
   }

   public YMDDateFormatter(String var1, String var2, TimeZone var3) {
      this.requestedFields = var1;
      this.localeName = var2;
      this.timeZone = var3;
      Locale var4 = Utils.localeFromString(var2);
      this.df = new SimpleDateFormat("yyyy/mm/dd", var4);
      this.df.setTimeZone(var3);
   }

   public String format(long var1) {
      return this.format(new Date(var1));
   }

   public String format(Date var1) {
      return this.df.format(var1);
   }

   public DateFormatter withLocale(String var1) {
      return !var1.equals(this.localeName) ? new YMDDateFormatter(this.requestedFields, var1, this.timeZone) : this;
   }

   public DateFormatter withTimeZone(TimeZone var1) {
      return !var1.equals(this.timeZone) ? new YMDDateFormatter(this.requestedFields, this.localeName, var1) : this;
   }
}
