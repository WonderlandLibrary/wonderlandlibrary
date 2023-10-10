package io.netty.handler.codec.http;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

final class HttpHeaderDateFormat extends SimpleDateFormat {
   private static final long serialVersionUID = -925286159755905325L;
   private final SimpleDateFormat format1;
   private final SimpleDateFormat format2;
   private static final ThreadLocal dateFormatThreadLocal = new ThreadLocal() {
      protected HttpHeaderDateFormat initialValue() {
         return new HttpHeaderDateFormat();
      }

      protected Object initialValue() {
         return this.initialValue();
      }
   };

   static HttpHeaderDateFormat get() {
      return (HttpHeaderDateFormat)dateFormatThreadLocal.get();
   }

   private HttpHeaderDateFormat() {
      super("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
      this.format1 = new HttpHeaderDateFormat.HttpHeaderDateFormatObsolete1();
      this.format2 = new HttpHeaderDateFormat.HttpHeaderDateFormatObsolete2();
      this.setTimeZone(TimeZone.getTimeZone("GMT"));
   }

   public Date parse(String var1, ParsePosition var2) {
      Date var3 = super.parse(var1, var2);
      if (var3 == null) {
         var3 = this.format1.parse(var1, var2);
      }

      if (var3 == null) {
         var3 = this.format2.parse(var1, var2);
      }

      return var3;
   }

   HttpHeaderDateFormat(Object var1) {
      this();
   }

   private static final class HttpHeaderDateFormatObsolete2 extends SimpleDateFormat {
      private static final long serialVersionUID = 3010674519968303714L;

      HttpHeaderDateFormatObsolete2() {
         super("E MMM d HH:mm:ss yyyy", Locale.ENGLISH);
         this.setTimeZone(TimeZone.getTimeZone("GMT"));
      }
   }

   private static final class HttpHeaderDateFormatObsolete1 extends SimpleDateFormat {
      private static final long serialVersionUID = -3178072504225114298L;

      HttpHeaderDateFormatObsolete1() {
         super("E, dd-MMM-yy HH:mm:ss z", Locale.ENGLISH);
         this.setTimeZone(TimeZone.getTimeZone("GMT"));
      }
   }
}
