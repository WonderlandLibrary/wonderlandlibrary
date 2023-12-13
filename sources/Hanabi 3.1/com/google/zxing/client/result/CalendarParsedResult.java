package com.google.zxing.client.result;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public final class CalendarParsedResult extends ParsedResult {
   private static final Pattern DATE_TIME = Pattern.compile("[0-9]{8}(T[0-9]{6}Z?)?");
   private static final DateFormat DATE_FORMAT;
   private static final DateFormat DATE_TIME_FORMAT;
   private final String summary;
   private final Date start;
   private final boolean startAllDay;
   private final Date end;
   private final boolean endAllDay;
   private final String location;
   private final String organizer;
   private final String[] attendees;
   private final String description;
   private final double latitude;
   private final double longitude;

   public CalendarParsedResult(String summary, String startString, String endString, String location, String organizer, String[] attendees, String description, double latitude, double longitude) {
      super(ParsedResultType.CALENDAR);
      this.summary = summary;

      try {
         this.start = parseDate(startString);
         this.end = endString == null ? null : parseDate(endString);
      } catch (ParseException var13) {
         throw new IllegalArgumentException(var13.toString());
      }

      this.startAllDay = startString.length() == 8;
      this.endAllDay = endString != null && endString.length() == 8;
      this.location = location;
      this.organizer = organizer;
      this.attendees = attendees;
      this.description = description;
      this.latitude = latitude;
      this.longitude = longitude;
   }

   public String getSummary() {
      return this.summary;
   }

   public Date getStart() {
      return this.start;
   }

   public boolean isStartAllDay() {
      return this.startAllDay;
   }

   public Date getEnd() {
      return this.end;
   }

   public boolean isEndAllDay() {
      return this.endAllDay;
   }

   public String getLocation() {
      return this.location;
   }

   public String getOrganizer() {
      return this.organizer;
   }

   public String[] getAttendees() {
      return this.attendees;
   }

   public String getDescription() {
      return this.description;
   }

   public double getLatitude() {
      return this.latitude;
   }

   public double getLongitude() {
      return this.longitude;
   }

   public String getDisplayResult() {
      StringBuilder result = new StringBuilder(100);
      maybeAppend(this.summary, result);
      maybeAppend(format(this.startAllDay, this.start), result);
      maybeAppend(format(this.endAllDay, this.end), result);
      maybeAppend(this.location, result);
      maybeAppend(this.organizer, result);
      maybeAppend(this.attendees, result);
      maybeAppend(this.description, result);
      return result.toString();
   }

   private static Date parseDate(String when) throws ParseException {
      if (!DATE_TIME.matcher(when).matches()) {
         throw new ParseException(when, 0);
      } else if (when.length() == 8) {
         return DATE_FORMAT.parse(when);
      } else {
         Date date;
         if (when.length() == 16 && when.charAt(15) == 'Z') {
            date = DATE_TIME_FORMAT.parse(when.substring(0, 15));
            Calendar calendar = new GregorianCalendar();
            long milliseconds = date.getTime();
            milliseconds += (long)calendar.get(15);
            calendar.setTime(new Date(milliseconds));
            milliseconds += (long)calendar.get(16);
            date = new Date(milliseconds);
         } else {
            date = DATE_TIME_FORMAT.parse(when);
         }

         return date;
      }
   }

   private static String format(boolean allDay, Date date) {
      if (date == null) {
         return null;
      } else {
         DateFormat format = allDay ? DateFormat.getDateInstance(2) : DateFormat.getDateTimeInstance(2, 2);
         return format.format(date);
      }
   }

   static {
      DATE_FORMAT = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
      DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
      DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.ENGLISH);
   }
}
