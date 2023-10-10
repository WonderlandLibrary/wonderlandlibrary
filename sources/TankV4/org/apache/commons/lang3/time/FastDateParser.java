package org.apache.commons.lang3.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FastDateParser implements DateParser, Serializable {
   private static final long serialVersionUID = 2L;
   static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
   private final String pattern;
   private final TimeZone timeZone;
   private final Locale locale;
   private final int century;
   private final int startYear;
   private transient Pattern parsePattern;
   private transient FastDateParser.Strategy[] strategies;
   private transient String currentFormatField;
   private transient FastDateParser.Strategy nextStrategy;
   private static final Pattern formatPattern = Pattern.compile("D+|E+|F+|G+|H+|K+|M+|S+|W+|Z+|a+|d+|h+|k+|m+|s+|w+|y+|z+|''|'[^']++(''[^']*+)*+'|[^'A-Za-z]++");
   private static final ConcurrentMap[] caches = new ConcurrentMap[17];
   private static final FastDateParser.Strategy ABBREVIATED_YEAR_STRATEGY = new FastDateParser.NumberStrategy(1) {
      void setCalendar(FastDateParser var1, Calendar var2, String var3) {
         int var4 = Integer.parseInt(var3);
         if (var4 < 100) {
            var4 = FastDateParser.access$300(var1, var4);
         }

         var2.set(1, var4);
      }
   };
   private static final FastDateParser.Strategy NUMBER_MONTH_STRATEGY = new FastDateParser.NumberStrategy(2) {
      int modify(int var1) {
         return var1 - 1;
      }
   };
   private static final FastDateParser.Strategy LITERAL_YEAR_STRATEGY = new FastDateParser.NumberStrategy(1);
   private static final FastDateParser.Strategy WEEK_OF_YEAR_STRATEGY = new FastDateParser.NumberStrategy(3);
   private static final FastDateParser.Strategy WEEK_OF_MONTH_STRATEGY = new FastDateParser.NumberStrategy(4);
   private static final FastDateParser.Strategy DAY_OF_YEAR_STRATEGY = new FastDateParser.NumberStrategy(6);
   private static final FastDateParser.Strategy DAY_OF_MONTH_STRATEGY = new FastDateParser.NumberStrategy(5);
   private static final FastDateParser.Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new FastDateParser.NumberStrategy(8);
   private static final FastDateParser.Strategy HOUR_OF_DAY_STRATEGY = new FastDateParser.NumberStrategy(11);
   private static final FastDateParser.Strategy MODULO_HOUR_OF_DAY_STRATEGY = new FastDateParser.NumberStrategy(11) {
      int modify(int var1) {
         return var1 % 24;
      }
   };
   private static final FastDateParser.Strategy MODULO_HOUR_STRATEGY = new FastDateParser.NumberStrategy(10) {
      int modify(int var1) {
         return var1 % 12;
      }
   };
   private static final FastDateParser.Strategy HOUR_STRATEGY = new FastDateParser.NumberStrategy(10);
   private static final FastDateParser.Strategy MINUTE_STRATEGY = new FastDateParser.NumberStrategy(12);
   private static final FastDateParser.Strategy SECOND_STRATEGY = new FastDateParser.NumberStrategy(13);
   private static final FastDateParser.Strategy MILLISECOND_STRATEGY = new FastDateParser.NumberStrategy(14);

   protected FastDateParser(String var1, TimeZone var2, Locale var3) {
      this(var1, var2, var3, (Date)null);
   }

   protected FastDateParser(String var1, TimeZone var2, Locale var3, Date var4) {
      this.pattern = var1;
      this.timeZone = var2;
      this.locale = var3;
      Calendar var5 = Calendar.getInstance(var2, var3);
      int var6;
      if (var4 != null) {
         var5.setTime(var4);
         var6 = var5.get(1);
      } else if (var3.equals(JAPANESE_IMPERIAL)) {
         var6 = 0;
      } else {
         var5.setTime(new Date());
         var6 = var5.get(1) - 80;
      }

      this.century = var6 / 100 * 100;
      this.startYear = var6 - this.century;
      this.init(var5);
   }

   private void init(Calendar var1) {
      StringBuilder var2 = new StringBuilder();
      ArrayList var3 = new ArrayList();
      Matcher var4 = formatPattern.matcher(this.pattern);
      if (!var4.lookingAt()) {
         throw new IllegalArgumentException("Illegal pattern character '" + this.pattern.charAt(var4.regionStart()) + "'");
      } else {
         this.currentFormatField = var4.group();
         FastDateParser.Strategy var5 = this.getStrategy(this.currentFormatField, var1);

         while(true) {
            var4.region(var4.end(), var4.regionEnd());
            if (!var4.lookingAt()) {
               this.nextStrategy = null;
               if (var4.regionStart() != var4.regionEnd()) {
                  throw new IllegalArgumentException("Failed to parse \"" + this.pattern + "\" ; gave up at index " + var4.regionStart());
               }

               if (var5.addRegex(this, var2)) {
                  var3.add(var5);
               }

               this.currentFormatField = null;
               this.strategies = (FastDateParser.Strategy[])var3.toArray(new FastDateParser.Strategy[var3.size()]);
               this.parsePattern = Pattern.compile(var2.toString());
               return;
            }

            String var6 = var4.group();
            this.nextStrategy = this.getStrategy(var6, var1);
            if (var5.addRegex(this, var2)) {
               var3.add(var5);
            }

            this.currentFormatField = var6;
            var5 = this.nextStrategy;
         }
      }
   }

   public String getPattern() {
      return this.pattern;
   }

   public TimeZone getTimeZone() {
      return this.timeZone;
   }

   public Locale getLocale() {
      return this.locale;
   }

   Pattern getParsePattern() {
      return this.parsePattern;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof FastDateParser)) {
         return false;
      } else {
         FastDateParser var2 = (FastDateParser)var1;
         return this.pattern.equals(var2.pattern) && this.timeZone.equals(var2.timeZone) && this.locale.equals(var2.locale);
      }
   }

   public int hashCode() {
      return this.pattern.hashCode() + 13 * (this.timeZone.hashCode() + 13 * this.locale.hashCode());
   }

   public String toString() {
      return "FastDateParser[" + this.pattern + "," + this.locale + "," + this.timeZone.getID() + "]";
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      Calendar var2 = Calendar.getInstance(this.timeZone, this.locale);
      this.init(var2);
   }

   public Object parseObject(String var1) throws ParseException {
      return this.parse(var1);
   }

   public Date parse(String var1) throws ParseException {
      Date var2 = this.parse(var1, new ParsePosition(0));
      if (var2 == null) {
         if (this.locale.equals(JAPANESE_IMPERIAL)) {
            throw new ParseException("(The " + this.locale + " locale does not support dates before 1868 AD)\n" + "Unparseable date: \"" + var1 + "\" does not match " + this.parsePattern.pattern(), 0);
         } else {
            throw new ParseException("Unparseable date: \"" + var1 + "\" does not match " + this.parsePattern.pattern(), 0);
         }
      } else {
         return var2;
      }
   }

   public Object parseObject(String var1, ParsePosition var2) {
      return this.parse(var1, var2);
   }

   public Date parse(String var1, ParsePosition var2) {
      int var3 = var2.getIndex();
      Matcher var4 = this.parsePattern.matcher(var1.substring(var3));
      if (!var4.lookingAt()) {
         return null;
      } else {
         Calendar var5 = Calendar.getInstance(this.timeZone, this.locale);
         var5.clear();
         int var6 = 0;

         while(var6 < this.strategies.length) {
            FastDateParser.Strategy var7 = this.strategies[var6++];
            var7.setCalendar(this, var5, var4.group(var6));
         }

         var2.setIndex(var3 + var4.end());
         return var5.getTime();
      }
   }

   private static StringBuilder escapeRegex(StringBuilder var0, String var1, boolean var2) {
      var0.append("\\Q");

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         char var4 = var1.charAt(var3);
         switch(var4) {
         case '\'':
            if (var2) {
               ++var3;
               if (var3 == var1.length()) {
                  return var0;
               }

               var4 = var1.charAt(var3);
            }
            break;
         case '\\':
            ++var3;
            if (var3 != var1.length()) {
               var0.append(var4);
               var4 = var1.charAt(var3);
               if (var4 == 'E') {
                  var0.append("E\\\\E\\");
                  var4 = 'Q';
               }
            }
         }

         var0.append(var4);
      }

      var0.append("\\E");
      return var0;
   }

   private static Map getDisplayNames(int var0, Calendar var1, Locale var2) {
      return var1.getDisplayNames(var0, 0, var2);
   }

   private int adjustYear(int var1) {
      int var2 = this.century + var1;
      return var1 >= this.startYear ? var2 : var2 + 100;
   }

   boolean isNextNumber() {
      return this.nextStrategy != null && this.nextStrategy.isNumber();
   }

   int getFieldWidth() {
      return this.currentFormatField.length();
   }

   private FastDateParser.Strategy getStrategy(String var1, Calendar var2) {
      switch(var1.charAt(0)) {
      case '\'':
         if (var1.length() > 2) {
            return new FastDateParser.CopyQuotedStrategy(var1.substring(1, var1.length() - 1));
         }
      case '(':
      case ')':
      case '*':
      case '+':
      case ',':
      case '-':
      case '.':
      case '/':
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
      case ':':
      case ';':
      case '<':
      case '=':
      case '>':
      case '?':
      case '@':
      case 'A':
      case 'B':
      case 'C':
      case 'I':
      case 'J':
      case 'L':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'T':
      case 'U':
      case 'V':
      case 'X':
      case 'Y':
      case '[':
      case '\\':
      case ']':
      case '^':
      case '_':
      case '`':
      case 'b':
      case 'c':
      case 'e':
      case 'f':
      case 'g':
      case 'i':
      case 'j':
      case 'l':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      case 't':
      case 'u':
      case 'v':
      case 'x':
      default:
         return new FastDateParser.CopyQuotedStrategy(var1);
      case 'D':
         return DAY_OF_YEAR_STRATEGY;
      case 'E':
         return this.getLocaleSpecificStrategy(7, var2);
      case 'F':
         return DAY_OF_WEEK_IN_MONTH_STRATEGY;
      case 'G':
         return this.getLocaleSpecificStrategy(0, var2);
      case 'H':
         return MODULO_HOUR_OF_DAY_STRATEGY;
      case 'K':
         return HOUR_STRATEGY;
      case 'M':
         return var1.length() >= 3 ? this.getLocaleSpecificStrategy(2, var2) : NUMBER_MONTH_STRATEGY;
      case 'S':
         return MILLISECOND_STRATEGY;
      case 'W':
         return WEEK_OF_MONTH_STRATEGY;
      case 'Z':
      case 'z':
         return this.getLocaleSpecificStrategy(15, var2);
      case 'a':
         return this.getLocaleSpecificStrategy(9, var2);
      case 'd':
         return DAY_OF_MONTH_STRATEGY;
      case 'h':
         return MODULO_HOUR_STRATEGY;
      case 'k':
         return HOUR_OF_DAY_STRATEGY;
      case 'm':
         return MINUTE_STRATEGY;
      case 's':
         return SECOND_STRATEGY;
      case 'w':
         return WEEK_OF_YEAR_STRATEGY;
      case 'y':
         return var1.length() > 2 ? LITERAL_YEAR_STRATEGY : ABBREVIATED_YEAR_STRATEGY;
      }
   }

   private static ConcurrentMap getCache(int var0) {
      ConcurrentMap[] var1;
      synchronized(var1 = caches){}
      if (caches[var0] == null) {
         caches[var0] = new ConcurrentHashMap(3);
      }

      return caches[var0];
   }

   private FastDateParser.Strategy getLocaleSpecificStrategy(int var1, Calendar var2) {
      ConcurrentMap var3 = getCache(var1);
      Object var4 = (FastDateParser.Strategy)var3.get(this.locale);
      if (var4 == null) {
         var4 = var1 == 15 ? new FastDateParser.TimeZoneStrategy(this.locale) : new FastDateParser.TextStrategy(var1, var2, this.locale);
         FastDateParser.Strategy var5 = (FastDateParser.Strategy)var3.putIfAbsent(this.locale, var4);
         if (var5 != null) {
            return var5;
         }
      }

      return (FastDateParser.Strategy)var4;
   }

   static StringBuilder access$100(StringBuilder var0, String var1, boolean var2) {
      return escapeRegex(var0, var1, var2);
   }

   static Map access$200(int var0, Calendar var1, Locale var2) {
      return getDisplayNames(var0, var1, var2);
   }

   static int access$300(FastDateParser var0, int var1) {
      return var0.adjustYear(var1);
   }

   private static class TimeZoneStrategy extends FastDateParser.Strategy {
      private final String validTimeZoneChars;
      private final SortedMap tzNames;
      private static final int ID = 0;
      private static final int LONG_STD = 1;
      private static final int SHORT_STD = 2;
      private static final int LONG_DST = 3;
      private static final int SHORT_DST = 4;

      TimeZoneStrategy(Locale var1) {
         super(null);
         this.tzNames = new TreeMap(String.CASE_INSENSITIVE_ORDER);
         String[][] var2 = DateFormatSymbols.getInstance(var1).getZoneStrings();
         String[][] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String[] var6 = var3[var5];
            if (!var6[0].startsWith("GMT")) {
               TimeZone var7 = TimeZone.getTimeZone(var6[0]);
               if (!this.tzNames.containsKey(var6[1])) {
                  this.tzNames.put(var6[1], var7);
               }

               if (!this.tzNames.containsKey(var6[2])) {
                  this.tzNames.put(var6[2], var7);
               }

               if (var7.useDaylightTime()) {
                  if (!this.tzNames.containsKey(var6[3])) {
                     this.tzNames.put(var6[3], var7);
                  }

                  if (!this.tzNames.containsKey(var6[4])) {
                     this.tzNames.put(var6[4], var7);
                  }
               }
            }
         }

         StringBuilder var8 = new StringBuilder();
         var8.append("(GMT[+\\-]\\d{0,1}\\d{2}|[+\\-]\\d{2}:?\\d{2}|");
         Iterator var9 = this.tzNames.keySet().iterator();

         while(var9.hasNext()) {
            String var10 = (String)var9.next();
            FastDateParser.access$100(var8, var10, false).append('|');
         }

         var8.setCharAt(var8.length() - 1, ')');
         this.validTimeZoneChars = var8.toString();
      }

      boolean addRegex(FastDateParser var1, StringBuilder var2) {
         var2.append(this.validTimeZoneChars);
         return true;
      }

      void setCalendar(FastDateParser var1, Calendar var2, String var3) {
         TimeZone var4;
         if (var3.charAt(0) != '+' && var3.charAt(0) != '-') {
            if (var3.startsWith("GMT")) {
               var4 = TimeZone.getTimeZone(var3);
            } else {
               var4 = (TimeZone)this.tzNames.get(var3);
               if (var4 == null) {
                  throw new IllegalArgumentException(var3 + " is not a supported timezone name");
               }
            }
         } else {
            var4 = TimeZone.getTimeZone("GMT" + var3);
         }

         var2.setTimeZone(var4);
      }
   }

   private static class NumberStrategy extends FastDateParser.Strategy {
      private final int field;

      NumberStrategy(int var1) {
         super(null);
         this.field = var1;
      }

      boolean isNumber() {
         return true;
      }

      boolean addRegex(FastDateParser var1, StringBuilder var2) {
         if (var1.isNextNumber()) {
            var2.append("(\\p{Nd}{").append(var1.getFieldWidth()).append("}+)");
         } else {
            var2.append("(\\p{Nd}++)");
         }

         return true;
      }

      void setCalendar(FastDateParser var1, Calendar var2, String var3) {
         var2.set(this.field, this.modify(Integer.parseInt(var3)));
      }

      int modify(int var1) {
         return var1;
      }
   }

   private static class TextStrategy extends FastDateParser.Strategy {
      private final int field;
      private final Map keyValues;

      TextStrategy(int var1, Calendar var2, Locale var3) {
         super(null);
         this.field = var1;
         this.keyValues = FastDateParser.access$200(var1, var2, var3);
      }

      boolean addRegex(FastDateParser var1, StringBuilder var2) {
         var2.append('(');
         Iterator var3 = this.keyValues.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            FastDateParser.access$100(var2, var4, false).append('|');
         }

         var2.setCharAt(var2.length() - 1, ')');
         return true;
      }

      void setCalendar(FastDateParser var1, Calendar var2, String var3) {
         Integer var4 = (Integer)this.keyValues.get(var3);
         if (var4 != null) {
            var2.set(this.field, var4);
         } else {
            StringBuilder var5 = new StringBuilder(var3);
            var5.append(" not in (");
            Iterator var6 = this.keyValues.keySet().iterator();

            while(var6.hasNext()) {
               String var7 = (String)var6.next();
               var5.append(var7).append(' ');
            }

            var5.setCharAt(var5.length() - 1, ')');
            throw new IllegalArgumentException(var5.toString());
         }
      }
   }

   private static class CopyQuotedStrategy extends FastDateParser.Strategy {
      private final String formatField;

      CopyQuotedStrategy(String var1) {
         super(null);
         this.formatField = var1;
      }

      boolean isNumber() {
         char var1 = this.formatField.charAt(0);
         if (var1 == '\'') {
            var1 = this.formatField.charAt(1);
         }

         return Character.isDigit(var1);
      }

      boolean addRegex(FastDateParser var1, StringBuilder var2) {
         FastDateParser.access$100(var2, this.formatField, true);
         return false;
      }
   }

   private abstract static class Strategy {
      private Strategy() {
      }

      boolean isNumber() {
         return false;
      }

      void setCalendar(FastDateParser var1, Calendar var2, String var3) {
      }

      abstract boolean addRegex(FastDateParser var1, StringBuilder var2);

      Strategy(Object var1) {
         this();
      }
   }
}
