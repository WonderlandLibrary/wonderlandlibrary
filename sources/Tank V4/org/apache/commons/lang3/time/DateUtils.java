package org.apache.commons.lang3.time;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class DateUtils {
   public static final long MILLIS_PER_SECOND = 1000L;
   public static final long MILLIS_PER_MINUTE = 60000L;
   public static final long MILLIS_PER_HOUR = 3600000L;
   public static final long MILLIS_PER_DAY = 86400000L;
   public static final int SEMI_MONTH = 1001;
   private static final int[][] fields = new int[][]{{14}, {13}, {12}, {11, 10}, {5, 5, 9}, {2, 1001}, {1}, {0}};
   public static final int RANGE_WEEK_SUNDAY = 1;
   public static final int RANGE_WEEK_MONDAY = 2;
   public static final int RANGE_WEEK_RELATIVE = 3;
   public static final int RANGE_WEEK_CENTER = 4;
   public static final int RANGE_MONTH_SUNDAY = 5;
   public static final int RANGE_MONTH_MONDAY = 6;
   private static final int MODIFY_TRUNCATE = 0;
   private static final int MODIFY_ROUND = 1;
   private static final int MODIFY_CEILING = 2;

   public static boolean isSameDay(Date var0, Date var1) {
      if (var0 != null && var1 != null) {
         Calendar var2 = Calendar.getInstance();
         var2.setTime(var0);
         Calendar var3 = Calendar.getInstance();
         var3.setTime(var1);
         return isSameDay(var2, var3);
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static boolean isSameDay(Calendar var0, Calendar var1) {
      if (var0 != null && var1 != null) {
         return var0.get(0) == var1.get(0) && var0.get(1) == var1.get(1) && var0.get(6) == var1.get(6);
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static boolean isSameInstant(Date var0, Date var1) {
      if (var0 != null && var1 != null) {
         return var0.getTime() == var1.getTime();
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static boolean isSameInstant(Calendar var0, Calendar var1) {
      if (var0 != null && var1 != null) {
         return var0.getTime().getTime() == var1.getTime().getTime();
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static boolean isSameLocalTime(Calendar var0, Calendar var1) {
      if (var0 != null && var1 != null) {
         return var0.get(14) == var1.get(14) && var0.get(13) == var1.get(13) && var0.get(12) == var1.get(12) && var0.get(11) == var1.get(11) && var0.get(6) == var1.get(6) && var0.get(1) == var1.get(1) && var0.get(0) == var1.get(0) && var0.getClass() == var1.getClass();
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static Date parseDate(String var0, String... var1) throws ParseException {
      return parseDate(var0, (Locale)null, var1);
   }

   public static Date parseDate(String var0, Locale var1, String... var2) throws ParseException {
      return parseDateWithLeniency(var0, var1, var2, true);
   }

   public static Date parseDateStrictly(String var0, String... var1) throws ParseException {
      return parseDateStrictly(var0, (Locale)null, var1);
   }

   public static Date parseDateStrictly(String var0, Locale var1, String... var2) throws ParseException {
      return parseDateWithLeniency(var0, (Locale)null, var2, false);
   }

   private static Date parseDateWithLeniency(String var0, Locale var1, String[] var2, boolean var3) throws ParseException {
      if (var0 != null && var2 != null) {
         SimpleDateFormat var4;
         if (var1 == null) {
            var4 = new SimpleDateFormat();
         } else {
            var4 = new SimpleDateFormat("", var1);
         }

         var4.setLenient(var3);
         ParsePosition var5 = new ParsePosition(0);
         String[] var6 = var2;
         int var7 = var2.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String var9 = var6[var8];
            String var10 = var9;
            if (var9.endsWith("ZZ")) {
               var10 = var9.substring(0, var9.length() - 1);
            }

            var4.applyPattern(var10);
            var5.setIndex(0);
            String var11 = var0;
            if (var9.endsWith("ZZ")) {
               var11 = var0.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2");
            }

            Date var12 = var4.parse(var11, var5);
            if (var12 != null && var5.getIndex() == var11.length()) {
               return var12;
            }
         }

         throw new ParseException("Unable to parse the date: " + var0, -1);
      } else {
         throw new IllegalArgumentException("Date and Patterns must not be null");
      }
   }

   public static Date addYears(Date var0, int var1) {
      return add(var0, 1, var1);
   }

   public static Date addMonths(Date var0, int var1) {
      return add(var0, 2, var1);
   }

   public static Date addWeeks(Date var0, int var1) {
      return add(var0, 3, var1);
   }

   public static Date addDays(Date var0, int var1) {
      return add(var0, 5, var1);
   }

   public static Date addHours(Date var0, int var1) {
      return add(var0, 11, var1);
   }

   public static Date addMinutes(Date var0, int var1) {
      return add(var0, 12, var1);
   }

   public static Date addSeconds(Date var0, int var1) {
      return add(var0, 13, var1);
   }

   public static Date addMilliseconds(Date var0, int var1) {
      return add(var0, 14, var1);
   }

   private static Date add(Date var0, int var1, int var2) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else {
         Calendar var3 = Calendar.getInstance();
         var3.setTime(var0);
         var3.add(var1, var2);
         return var3.getTime();
      }
   }

   public static Date setYears(Date var0, int var1) {
      return set(var0, 1, var1);
   }

   public static Date setMonths(Date var0, int var1) {
      return set(var0, 2, var1);
   }

   public static Date setDays(Date var0, int var1) {
      return set(var0, 5, var1);
   }

   public static Date setHours(Date var0, int var1) {
      return set(var0, 11, var1);
   }

   public static Date setMinutes(Date var0, int var1) {
      return set(var0, 12, var1);
   }

   public static Date setSeconds(Date var0, int var1) {
      return set(var0, 13, var1);
   }

   public static Date setMilliseconds(Date var0, int var1) {
      return set(var0, 14, var1);
   }

   private static Date set(Date var0, int var1, int var2) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else {
         Calendar var3 = Calendar.getInstance();
         var3.setLenient(false);
         var3.setTime(var0);
         var3.set(var1, var2);
         return var3.getTime();
      }
   }

   public static Calendar toCalendar(Date var0) {
      Calendar var1 = Calendar.getInstance();
      var1.setTime(var0);
      return var1;
   }

   public static Date round(Date var0, int var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else {
         Calendar var2 = Calendar.getInstance();
         var2.setTime(var0);
         modify(var2, var1, 1);
         return var2.getTime();
      }
   }

   public static Calendar round(Calendar var0, int var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else {
         Calendar var2 = (Calendar)var0.clone();
         modify(var2, var1, 1);
         return var2;
      }
   }

   public static Date round(Object var0, int var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else if (var0 instanceof Date) {
         return round((Date)var0, var1);
      } else if (var0 instanceof Calendar) {
         return round((Calendar)var0, var1).getTime();
      } else {
         throw new ClassCastException("Could not round " + var0);
      }
   }

   public static Date truncate(Date var0, int var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else {
         Calendar var2 = Calendar.getInstance();
         var2.setTime(var0);
         modify(var2, var1, 0);
         return var2.getTime();
      }
   }

   public static Calendar truncate(Calendar var0, int var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else {
         Calendar var2 = (Calendar)var0.clone();
         modify(var2, var1, 0);
         return var2;
      }
   }

   public static Date truncate(Object var0, int var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else if (var0 instanceof Date) {
         return truncate((Date)var0, var1);
      } else if (var0 instanceof Calendar) {
         return truncate((Calendar)var0, var1).getTime();
      } else {
         throw new ClassCastException("Could not truncate " + var0);
      }
   }

   public static Date ceiling(Date var0, int var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else {
         Calendar var2 = Calendar.getInstance();
         var2.setTime(var0);
         modify(var2, var1, 2);
         return var2.getTime();
      }
   }

   public static Calendar ceiling(Calendar var0, int var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else {
         Calendar var2 = (Calendar)var0.clone();
         modify(var2, var1, 2);
         return var2;
      }
   }

   public static Date ceiling(Object var0, int var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else if (var0 instanceof Date) {
         return ceiling((Date)var0, var1);
      } else if (var0 instanceof Calendar) {
         return ceiling((Calendar)var0, var1).getTime();
      } else {
         throw new ClassCastException("Could not find ceiling of for type: " + var0.getClass());
      }
   }

   private static void modify(Calendar var0, int var1, int var2) {
      if (var0.get(1) > 280000000) {
         throw new ArithmeticException("Calendar value too large for accurate calculations");
      } else if (var1 != 14) {
         Date var3 = var0.getTime();
         long var4 = var3.getTime();
         boolean var6 = false;
         int var7 = var0.get(14);
         if (0 == var2 || var7 < 500) {
            var4 -= (long)var7;
         }

         if (var1 == 13) {
            var6 = true;
         }

         int var8 = var0.get(13);
         if (!var6 && (0 == var2 || var8 < 30)) {
            var4 -= (long)var8 * 1000L;
         }

         if (var1 == 12) {
            var6 = true;
         }

         int var9 = var0.get(12);
         if (!var6 && (0 == var2 || var9 < 30)) {
            var4 -= (long)var9 * 60000L;
         }

         if (var3.getTime() != var4) {
            var3.setTime(var4);
            var0.setTime(var3);
         }

         boolean var10 = false;
         int[][] var11 = fields;
         int var12 = var11.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            int[] var14 = var11[var13];
            int[] var15 = var14;
            int var16 = var14.length;

            int var17;
            int var18;
            for(var17 = 0; var17 < var16; ++var17) {
               var18 = var15[var17];
               if (var18 == var1) {
                  if (var2 == 2 || var2 == 1 && var10) {
                     if (var1 == 1001) {
                        if (var0.get(5) == 1) {
                           var0.add(5, 15);
                        } else {
                           var0.add(5, -15);
                           var0.add(2, 1);
                        }
                     } else if (var1 == 9) {
                        if (var0.get(11) == 0) {
                           var0.add(11, 12);
                        } else {
                           var0.add(11, -12);
                           var0.add(5, 1);
                        }
                     } else {
                        var0.add(var14[0], 1);
                     }
                  }

                  return;
               }
            }

            int var19 = 0;
            boolean var20 = false;
            switch(var1) {
            case 9:
               if (var14[0] == 11) {
                  var19 = var0.get(11);
                  if (var19 >= 12) {
                     var19 -= 12;
                  }

                  var10 = var19 >= 6;
                  var20 = true;
               }
               break;
            case 1001:
               if (var14[0] == 5) {
                  var19 = var0.get(5) - 1;
                  if (var19 >= 15) {
                     var19 -= 15;
                  }

                  var10 = var19 > 7;
                  var20 = true;
               }
            }

            if (!var20) {
               var17 = var0.getActualMinimum(var14[0]);
               var18 = var0.getActualMaximum(var14[0]);
               var19 = var0.get(var14[0]) - var17;
               var10 = var19 > (var18 - var17) / 2;
            }

            if (var19 != 0) {
               var0.set(var14[0], var0.get(var14[0]) - var19);
            }
         }

         throw new IllegalArgumentException("The field " + var1 + " is not supported");
      }
   }

   public static Iterator iterator(Date var0, int var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else {
         Calendar var2 = Calendar.getInstance();
         var2.setTime(var0);
         return iterator(var2, var1);
      }
   }

   public static Iterator iterator(Calendar var0, int var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else {
         Calendar var2;
         Calendar var3;
         int var4;
         int var5;
         var2 = null;
         var3 = null;
         var4 = 1;
         var5 = 7;
         label40:
         switch(var1) {
         case 1:
         case 2:
         case 3:
         case 4:
            var2 = truncate((Calendar)var0, 5);
            var3 = truncate((Calendar)var0, 5);
            switch(var1) {
            case 1:
            default:
               break label40;
            case 2:
               var4 = 2;
               var5 = 1;
               break label40;
            case 3:
               var4 = var0.get(7);
               var5 = var4 - 1;
               break label40;
            case 4:
               var4 = var0.get(7) - 3;
               var5 = var0.get(7) + 3;
               break label40;
            }
         case 5:
         case 6:
            var2 = truncate((Calendar)var0, 2);
            var3 = (Calendar)var2.clone();
            var3.add(2, 1);
            var3.add(5, -1);
            if (var1 == 6) {
               var4 = 2;
               var5 = 1;
            }
            break;
         default:
            throw new IllegalArgumentException("The range style " + var1 + " is not valid.");
         }

         if (var4 < 1) {
            var4 += 7;
         }

         if (var4 > 7) {
            var4 -= 7;
         }

         if (var5 < 1) {
            var5 += 7;
         }

         if (var5 > 7) {
            var5 -= 7;
         }

         while(var2.get(7) != var4) {
            var2.add(5, -1);
         }

         while(var3.get(7) != var5) {
            var3.add(5, 1);
         }

         return new DateUtils.DateIterator(var2, var3);
      }
   }

   public static Iterator iterator(Object var0, int var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else if (var0 instanceof Date) {
         return iterator((Date)var0, var1);
      } else if (var0 instanceof Calendar) {
         return iterator((Calendar)var0, var1);
      } else {
         throw new ClassCastException("Could not iterate based on " + var0);
      }
   }

   public static long getFragmentInMilliseconds(Date var0, int var1) {
      return getFragment(var0, var1, TimeUnit.MILLISECONDS);
   }

   public static long getFragmentInSeconds(Date var0, int var1) {
      return getFragment(var0, var1, TimeUnit.SECONDS);
   }

   public static long getFragmentInMinutes(Date var0, int var1) {
      return getFragment(var0, var1, TimeUnit.MINUTES);
   }

   public static long getFragmentInHours(Date var0, int var1) {
      return getFragment(var0, var1, TimeUnit.HOURS);
   }

   public static long getFragmentInDays(Date var0, int var1) {
      return getFragment(var0, var1, TimeUnit.DAYS);
   }

   public static long getFragmentInMilliseconds(Calendar var0, int var1) {
      return getFragment(var0, var1, TimeUnit.MILLISECONDS);
   }

   public static long getFragmentInSeconds(Calendar var0, int var1) {
      return getFragment(var0, var1, TimeUnit.SECONDS);
   }

   public static long getFragmentInMinutes(Calendar var0, int var1) {
      return getFragment(var0, var1, TimeUnit.MINUTES);
   }

   public static long getFragmentInHours(Calendar var0, int var1) {
      return getFragment(var0, var1, TimeUnit.HOURS);
   }

   public static long getFragmentInDays(Calendar var0, int var1) {
      return getFragment(var0, var1, TimeUnit.DAYS);
   }

   private static long getFragment(Date var0, int var1, TimeUnit var2) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else {
         Calendar var3 = Calendar.getInstance();
         var3.setTime(var0);
         return getFragment(var3, var1, var2);
      }
   }

   private static long getFragment(Calendar var0, int var1, TimeUnit var2) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else {
         long var3 = 0L;
         int var5 = var2 == TimeUnit.DAYS ? 0 : 1;
         switch(var1) {
         case 1:
            var3 += var2.convert((long)(var0.get(6) - var5), TimeUnit.DAYS);
            break;
         case 2:
            var3 += var2.convert((long)(var0.get(5) - var5), TimeUnit.DAYS);
         }

         switch(var1) {
         case 1:
         case 2:
         case 5:
         case 6:
            var3 += var2.convert((long)var0.get(11), TimeUnit.HOURS);
         case 11:
            var3 += var2.convert((long)var0.get(12), TimeUnit.MINUTES);
         case 12:
            var3 += var2.convert((long)var0.get(13), TimeUnit.SECONDS);
         case 13:
            var3 += var2.convert((long)var0.get(14), TimeUnit.MILLISECONDS);
         case 14:
            return var3;
         case 3:
         case 4:
         case 7:
         case 8:
         case 9:
         case 10:
         default:
            throw new IllegalArgumentException("The fragment " + var1 + " is not supported");
         }
      }
   }

   public static boolean truncatedEquals(Calendar var0, Calendar var1, int var2) {
      return truncatedCompareTo(var0, var1, var2) == 0;
   }

   public static boolean truncatedEquals(Date var0, Date var1, int var2) {
      return truncatedCompareTo(var0, var1, var2) == 0;
   }

   public static int truncatedCompareTo(Calendar var0, Calendar var1, int var2) {
      Calendar var3 = truncate(var0, var2);
      Calendar var4 = truncate(var1, var2);
      return var3.compareTo(var4);
   }

   public static int truncatedCompareTo(Date var0, Date var1, int var2) {
      Date var3 = truncate(var0, var2);
      Date var4 = truncate(var1, var2);
      return var3.compareTo(var4);
   }

   static class DateIterator implements Iterator {
      private final Calendar endFinal;
      private final Calendar spot;

      DateIterator(Calendar var1, Calendar var2) {
         this.endFinal = var2;
         this.spot = var1;
         this.spot.add(5, -1);
      }

      public boolean hasNext() {
         return this.spot.before(this.endFinal);
      }

      public Calendar next() {
         if (this.spot.equals(this.endFinal)) {
            throw new NoSuchElementException();
         } else {
            this.spot.add(5, 1);
            return (Calendar)this.spot.clone();
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      public Object next() {
         return this.next();
      }
   }
}
