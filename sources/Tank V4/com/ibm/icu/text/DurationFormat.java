package com.ibm.icu.text;

import com.ibm.icu.impl.duration.BasicDurationFormat;
import com.ibm.icu.util.ULocale;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

public abstract class DurationFormat extends UFormat {
   private static final long serialVersionUID = -2076961954727774282L;

   public static DurationFormat getInstance(ULocale var0) {
      return BasicDurationFormat.getInstance(var0);
   }

   /** @deprecated */
   protected DurationFormat() {
   }

   /** @deprecated */
   protected DurationFormat(ULocale var1) {
      this.setLocale(var1, var1);
   }

   public abstract StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3);

   public Object parseObject(String var1, ParsePosition var2) {
      throw new UnsupportedOperationException();
   }

   public abstract String formatDurationFromNowTo(Date var1);

   public abstract String formatDurationFromNow(long var1);

   public abstract String formatDurationFrom(long var1, long var3);
}
