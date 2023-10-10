package org.apache.commons.lang3;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.NumericEntityEscaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.OctalUnescaper;
import org.apache.commons.lang3.text.translate.UnicodeUnescaper;
import org.apache.commons.lang3.text.translate.UnicodeUnpairedSurrogateRemover;

public class StringEscapeUtils {
   public static final CharSequenceTranslator ESCAPE_JAVA = (new LookupTranslator(new String[][]{{"\"", "\\\""}, {"\\", "\\\\"}})).with(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE())}).with(JavaUnicodeEscaper.outsideOf(32, 127));
   public static final CharSequenceTranslator ESCAPE_ECMASCRIPT = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(new String[][]{{"'", "\\'"}, {"\"", "\\\""}, {"\\", "\\\\"}, {"/", "\\/"}}), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), JavaUnicodeEscaper.outsideOf(32, 127)});
   public static final CharSequenceTranslator ESCAPE_JSON = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(new String[][]{{"\"", "\\\""}, {"\\", "\\\\"}, {"/", "\\/"}}), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), JavaUnicodeEscaper.outsideOf(32, 127)});
   /** @deprecated */
   @Deprecated
   public static final CharSequenceTranslator ESCAPE_XML = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.APOS_ESCAPE())});
   public static final CharSequenceTranslator ESCAPE_XML10 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.APOS_ESCAPE()), new LookupTranslator(new String[][]{{"\u0000", ""}, {"\u0001", ""}, {"\u0002", ""}, {"\u0003", ""}, {"\u0004", ""}, {"\u0005", ""}, {"\u0006", ""}, {"\u0007", ""}, {"\b", ""}, {"\u000b", ""}, {"\f", ""}, {"\u000e", ""}, {"\u000f", ""}, {"\u0010", ""}, {"\u0011", ""}, {"\u0012", ""}, {"\u0013", ""}, {"\u0014", ""}, {"\u0015", ""}, {"\u0016", ""}, {"\u0017", ""}, {"\u0018", ""}, {"\u0019", ""}, {"\u001a", ""}, {"\u001b", ""}, {"\u001c", ""}, {"\u001d", ""}, {"\u001e", ""}, {"\u001f", ""}, {"\ufffe", ""}, {"\uffff", ""}}), NumericEntityEscaper.between(127, 132), NumericEntityEscaper.between(134, 159), new UnicodeUnpairedSurrogateRemover()});
   public static final CharSequenceTranslator ESCAPE_XML11 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.APOS_ESCAPE()), new LookupTranslator(new String[][]{{"\u0000", ""}, {"\u000b", "&#11;"}, {"\f", "&#12;"}, {"\ufffe", ""}, {"\uffff", ""}}), NumericEntityEscaper.between(1, 8), NumericEntityEscaper.between(14, 31), NumericEntityEscaper.between(127, 132), NumericEntityEscaper.between(134, 159), new UnicodeUnpairedSurrogateRemover()});
   public static final CharSequenceTranslator ESCAPE_HTML3 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE())});
   public static final CharSequenceTranslator ESCAPE_HTML4 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE()), new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE())});
   public static final CharSequenceTranslator ESCAPE_CSV = new StringEscapeUtils.CsvEscaper();
   public static final CharSequenceTranslator UNESCAPE_JAVA = new AggregateTranslator(new CharSequenceTranslator[]{new OctalUnescaper(), new UnicodeUnescaper(), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE()), new LookupTranslator(new String[][]{{"\\\\", "\\"}, {"\\\"", "\""}, {"\\'", "'"}, {"\\", ""}})});
   public static final CharSequenceTranslator UNESCAPE_ECMASCRIPT;
   public static final CharSequenceTranslator UNESCAPE_JSON;
   public static final CharSequenceTranslator UNESCAPE_HTML3;
   public static final CharSequenceTranslator UNESCAPE_HTML4;
   public static final CharSequenceTranslator UNESCAPE_XML;
   public static final CharSequenceTranslator UNESCAPE_CSV;

   public static final String escapeJava(String var0) {
      return ESCAPE_JAVA.translate(var0);
   }

   public static final String escapeEcmaScript(String var0) {
      return ESCAPE_ECMASCRIPT.translate(var0);
   }

   public static final String escapeJson(String var0) {
      return ESCAPE_JSON.translate(var0);
   }

   public static final String unescapeJava(String var0) {
      return UNESCAPE_JAVA.translate(var0);
   }

   public static final String unescapeEcmaScript(String var0) {
      return UNESCAPE_ECMASCRIPT.translate(var0);
   }

   public static final String unescapeJson(String var0) {
      return UNESCAPE_JSON.translate(var0);
   }

   public static final String escapeHtml4(String var0) {
      return ESCAPE_HTML4.translate(var0);
   }

   public static final String escapeHtml3(String var0) {
      return ESCAPE_HTML3.translate(var0);
   }

   public static final String unescapeHtml4(String var0) {
      return UNESCAPE_HTML4.translate(var0);
   }

   public static final String unescapeHtml3(String var0) {
      return UNESCAPE_HTML3.translate(var0);
   }

   /** @deprecated */
   @Deprecated
   public static final String escapeXml(String var0) {
      return ESCAPE_XML.translate(var0);
   }

   public static String escapeXml10(String var0) {
      return ESCAPE_XML10.translate(var0);
   }

   public static String escapeXml11(String var0) {
      return ESCAPE_XML11.translate(var0);
   }

   public static final String unescapeXml(String var0) {
      return UNESCAPE_XML.translate(var0);
   }

   public static final String escapeCsv(String var0) {
      return ESCAPE_CSV.translate(var0);
   }

   public static final String unescapeCsv(String var0) {
      return UNESCAPE_CSV.translate(var0);
   }

   static {
      UNESCAPE_ECMASCRIPT = UNESCAPE_JAVA;
      UNESCAPE_JSON = UNESCAPE_JAVA;
      UNESCAPE_HTML3 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0])});
      UNESCAPE_HTML4 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE()), new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0])});
      UNESCAPE_XML = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(EntityArrays.APOS_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0])});
      UNESCAPE_CSV = new StringEscapeUtils.CsvUnescaper();
   }

   static class CsvUnescaper extends CharSequenceTranslator {
      private static final char CSV_DELIMITER = ',';
      private static final char CSV_QUOTE = '"';
      private static final String CSV_QUOTE_STR = String.valueOf('"');
      private static final char[] CSV_SEARCH_CHARS = new char[]{',', '"', '\r', '\n'};

      public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
         if (var2 != 0) {
            throw new IllegalStateException("CsvUnescaper should never reach the [1] index");
         } else if (var1.charAt(0) == '"' && var1.charAt(var1.length() - 1) == '"') {
            String var4 = var1.subSequence(1, var1.length() - 1).toString();
            if (StringUtils.containsAny(var4, (char[])CSV_SEARCH_CHARS)) {
               var3.write(StringUtils.replace(var4, CSV_QUOTE_STR + CSV_QUOTE_STR, CSV_QUOTE_STR));
            } else {
               var3.write(var1.toString());
            }

            return Character.codePointCount(var1, 0, var1.length());
         } else {
            var3.write(var1.toString());
            return Character.codePointCount(var1, 0, var1.length());
         }
      }
   }

   static class CsvEscaper extends CharSequenceTranslator {
      private static final char CSV_DELIMITER = ',';
      private static final char CSV_QUOTE = '"';
      private static final String CSV_QUOTE_STR = String.valueOf('"');
      private static final char[] CSV_SEARCH_CHARS = new char[]{',', '"', '\r', '\n'};

      public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
         if (var2 != 0) {
            throw new IllegalStateException("CsvEscaper should never reach the [1] index");
         } else {
            if (StringUtils.containsNone(var1.toString(), (char[])CSV_SEARCH_CHARS)) {
               var3.write(var1.toString());
            } else {
               var3.write(34);
               var3.write(StringUtils.replace(var1.toString(), CSV_QUOTE_STR, CSV_QUOTE_STR + CSV_QUOTE_STR));
               var3.write(34);
            }

            return Character.codePointCount(var1, 0, var1.length());
         }
      }
   }
}
