package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.List;

public final class VEventResultParser extends ResultParser {
   public CalendarParsedResult parse(Result result) {
      String rawText = getMassagedText(result);
      int vEventStart = rawText.indexOf("BEGIN:VEVENT");
      if (vEventStart < 0) {
         return null;
      } else {
         String summary = matchSingleVCardPrefixedField("SUMMARY", rawText, true);
         String start = matchSingleVCardPrefixedField("DTSTART", rawText, true);
         if (start == null) {
            return null;
         } else {
            String end = matchSingleVCardPrefixedField("DTEND", rawText, true);
            String location = matchSingleVCardPrefixedField("LOCATION", rawText, true);
            String organizer = stripMailto(matchSingleVCardPrefixedField("ORGANIZER", rawText, true));
            String[] attendees = matchVCardPrefixedField("ATTENDEE", rawText, true);
            if (attendees != null) {
               for(int i = 0; i < attendees.length; ++i) {
                  attendees[i] = stripMailto(attendees[i]);
               }
            }

            String description = matchSingleVCardPrefixedField("DESCRIPTION", rawText, true);
            String geoString = matchSingleVCardPrefixedField("GEO", rawText, true);
            double latitude;
            double longitude;
            if (geoString == null) {
               latitude = Double.NaN;
               longitude = Double.NaN;
            } else {
               int semicolon = geoString.indexOf(59);

               try {
                  latitude = Double.parseDouble(geoString.substring(0, semicolon));
                  longitude = Double.parseDouble(geoString.substring(semicolon + 1));
               } catch (NumberFormatException var19) {
                  return null;
               }
            }

            try {
               return new CalendarParsedResult(summary, start, end, location, organizer, attendees, description, latitude, longitude);
            } catch (IllegalArgumentException var18) {
               return null;
            }
         }
      }
   }

   private static String matchSingleVCardPrefixedField(CharSequence prefix, String rawText, boolean trim) {
      List values = VCardResultParser.matchSingleVCardPrefixedField(prefix, rawText, trim, false);
      return values != null && !values.isEmpty() ? (String)values.get(0) : null;
   }

   private static String[] matchVCardPrefixedField(CharSequence prefix, String rawText, boolean trim) {
      List values = VCardResultParser.matchVCardPrefixedField(prefix, rawText, trim, false);
      if (values != null && !values.isEmpty()) {
         int size = values.size();
         String[] result = new String[size];

         for(int i = 0; i < size; ++i) {
            result[i] = (String)((List)values.get(i)).get(0);
         }

         return result;
      } else {
         return null;
      }
   }

   private static String stripMailto(String s) {
      if (s != null && (s.startsWith("mailto:") || s.startsWith("MAILTO:"))) {
         s = s.substring(7);
      }

      return s;
   }
}
