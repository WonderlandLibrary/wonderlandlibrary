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
  public static final CharSequenceTranslator ESCAPE_JAVA = (new LookupTranslator((CharSequence[][])new String[][] { { "\"", "\\\"" }, { "\\", "\\\\" } })).with(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.JAVA_CTRL_CHARS_ESCAPE()) }).with(new CharSequenceTranslator[] { (CharSequenceTranslator)JavaUnicodeEscaper.outsideOf(32, 127) });
  
  public static final CharSequenceTranslator ESCAPE_ECMASCRIPT = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator((CharSequence[][])new String[][] { { "'", "\\'" }, { "\"", "\\\"" }, { "\\", "\\\\" }, { "/", "\\/" } }), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), (CharSequenceTranslator)JavaUnicodeEscaper.outsideOf(32, 127) });
  
  public static final CharSequenceTranslator ESCAPE_JSON = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator((CharSequence[][])new String[][] { { "\"", "\\\"" }, { "\\", "\\\\" }, { "/", "\\/" } }), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), (CharSequenceTranslator)JavaUnicodeEscaper.outsideOf(32, 127) });
  
  @Deprecated
  public static final CharSequenceTranslator ESCAPE_XML = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.BASIC_ESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.APOS_ESCAPE()) });
  
  public static final CharSequenceTranslator ESCAPE_XML10 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.BASIC_ESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.APOS_ESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])new String[][] { 
            { "\000", "" }, { "\001", "" }, { "\002", "" }, { "\003", "" }, { "\004", "" }, { "\005", "" }, { "\006", "" }, { "\007", "" }, { "\b", "" }, { "\013", "" }, 
            { "\f", "" }, { "\016", "" }, { "\017", "" }, { "\020", "" }, { "\021", "" }, { "\022", "" }, { "\023", "" }, { "\024", "" }, { "\025", "" }, { "\026", "" }, 
            { "\027", "" }, { "\030", "" }, { "\031", "" }, { "\032", "" }, { "\033", "" }, { "\034", "" }, { "\035", "" }, { "\036", "" }, { "\037", "" }, { "￾", "" }, 
            { "￿", "" } }), (CharSequenceTranslator)NumericEntityEscaper.between(127, 132), (CharSequenceTranslator)NumericEntityEscaper.between(134, 159), (CharSequenceTranslator)new UnicodeUnpairedSurrogateRemover() });
  
  public static final CharSequenceTranslator ESCAPE_XML11 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.BASIC_ESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.APOS_ESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])new String[][] { { "\000", "" }, { "\013", "&#11;" }, { "\f", "&#12;" }, { "￾", "" }, { "￿", "" } }), (CharSequenceTranslator)NumericEntityEscaper.between(1, 8), (CharSequenceTranslator)NumericEntityEscaper.between(14, 31), (CharSequenceTranslator)NumericEntityEscaper.between(127, 132), (CharSequenceTranslator)NumericEntityEscaper.between(134, 159), (CharSequenceTranslator)new UnicodeUnpairedSurrogateRemover() });
  
  public static final CharSequenceTranslator ESCAPE_HTML3 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.BASIC_ESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.ISO8859_1_ESCAPE()) });
  
  public static final CharSequenceTranslator ESCAPE_HTML4 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.BASIC_ESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.ISO8859_1_ESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.HTML40_EXTENDED_ESCAPE()) });
  
  public static final CharSequenceTranslator ESCAPE_CSV = new CsvEscaper();
  
  static class CsvEscaper extends CharSequenceTranslator {
    private static final char CSV_DELIMITER = ',';
    
    private static final char CSV_QUOTE = '"';
    
    private static final String CSV_QUOTE_STR = String.valueOf('"');
    
    private static final char[] CSV_SEARCH_CHARS = new char[] { ',', '"', '\r', '\n' };
    
    public int translate(CharSequence input, int index, Writer out) throws IOException {
      if (index != 0)
        throw new IllegalStateException("CsvEscaper should never reach the [1] index"); 
      if (StringUtils.containsNone(input.toString(), CSV_SEARCH_CHARS)) {
        out.write(input.toString());
      } else {
        out.write(34);
        out.write(StringUtils.replace(input.toString(), CSV_QUOTE_STR, CSV_QUOTE_STR + CSV_QUOTE_STR));
        out.write(34);
      } 
      return Character.codePointCount(input, 0, input.length());
    }
  }
  
  public static final CharSequenceTranslator UNESCAPE_JAVA = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new OctalUnescaper(), (CharSequenceTranslator)new UnicodeUnescaper(), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.JAVA_CTRL_CHARS_UNESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])new String[][] { { "\\\\", "\\" }, { "\\\"", "\"" }, { "\\'", "'" }, { "\\", "" } }) });
  
  public static final CharSequenceTranslator UNESCAPE_ECMASCRIPT = UNESCAPE_JAVA;
  
  public static final CharSequenceTranslator UNESCAPE_JSON = UNESCAPE_JAVA;
  
  public static final CharSequenceTranslator UNESCAPE_HTML3 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.BASIC_UNESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.ISO8859_1_UNESCAPE()), (CharSequenceTranslator)new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0]) });
  
  public static final CharSequenceTranslator UNESCAPE_HTML4 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.BASIC_UNESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.ISO8859_1_UNESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.HTML40_EXTENDED_UNESCAPE()), (CharSequenceTranslator)new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0]) });
  
  public static final CharSequenceTranslator UNESCAPE_XML = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.BASIC_UNESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.APOS_UNESCAPE()), (CharSequenceTranslator)new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0]) });
  
  public static final CharSequenceTranslator UNESCAPE_CSV = new CsvUnescaper();
  
  static class CsvUnescaper extends CharSequenceTranslator {
    private static final char CSV_DELIMITER = ',';
    
    private static final char CSV_QUOTE = '"';
    
    private static final String CSV_QUOTE_STR = String.valueOf('"');
    
    private static final char[] CSV_SEARCH_CHARS = new char[] { ',', '"', '\r', '\n' };
    
    public int translate(CharSequence input, int index, Writer out) throws IOException {
      if (index != 0)
        throw new IllegalStateException("CsvUnescaper should never reach the [1] index"); 
      if (input.charAt(0) != '"' || input.charAt(input.length() - 1) != '"') {
        out.write(input.toString());
        return Character.codePointCount(input, 0, input.length());
      } 
      String quoteless = input.subSequence(1, input.length() - 1).toString();
      if (StringUtils.containsAny(quoteless, CSV_SEARCH_CHARS)) {
        out.write(StringUtils.replace(quoteless, CSV_QUOTE_STR + CSV_QUOTE_STR, CSV_QUOTE_STR));
      } else {
        out.write(input.toString());
      } 
      return Character.codePointCount(input, 0, input.length());
    }
  }
  
  public static final String escapeJava(String input) {
    return ESCAPE_JAVA.translate(input);
  }
  
  public static final String escapeEcmaScript(String input) {
    return ESCAPE_ECMASCRIPT.translate(input);
  }
  
  public static final String escapeJson(String input) {
    return ESCAPE_JSON.translate(input);
  }
  
  public static final String unescapeJava(String input) {
    return UNESCAPE_JAVA.translate(input);
  }
  
  public static final String unescapeEcmaScript(String input) {
    return UNESCAPE_ECMASCRIPT.translate(input);
  }
  
  public static final String unescapeJson(String input) {
    return UNESCAPE_JSON.translate(input);
  }
  
  public static final String escapeHtml4(String input) {
    return ESCAPE_HTML4.translate(input);
  }
  
  public static final String escapeHtml3(String input) {
    return ESCAPE_HTML3.translate(input);
  }
  
  public static final String unescapeHtml4(String input) {
    return UNESCAPE_HTML4.translate(input);
  }
  
  public static final String unescapeHtml3(String input) {
    return UNESCAPE_HTML3.translate(input);
  }
  
  @Deprecated
  public static final String escapeXml(String input) {
    return ESCAPE_XML.translate(input);
  }
  
  public static String escapeXml10(String input) {
    return ESCAPE_XML10.translate(input);
  }
  
  public static String escapeXml11(String input) {
    return ESCAPE_XML11.translate(input);
  }
  
  public static final String unescapeXml(String input) {
    return UNESCAPE_XML.translate(input);
  }
  
  public static final String escapeCsv(String input) {
    return ESCAPE_CSV.translate(input);
  }
  
  public static final String unescapeCsv(String input) {
    return UNESCAPE_CSV.translate(input);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\StringEscapeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */