package org.apache.http.message;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.annotation.Immutable;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class BasicHeaderValueParser implements HeaderValueParser {
  @Deprecated
  public static final BasicHeaderValueParser DEFAULT = new BasicHeaderValueParser();
  
  public static final BasicHeaderValueParser INSTANCE = new BasicHeaderValueParser();
  
  private static final char PARAM_DELIMITER = ';';
  
  private static final char ELEM_DELIMITER = ',';
  
  private static final char[] ALL_DELIMITERS = new char[] { ';', ',' };
  
  public static HeaderElement[] parseElements(String value, HeaderValueParser parser) throws ParseException {
    Args.notNull(value, "Value");
    CharArrayBuffer buffer = new CharArrayBuffer(value.length());
    buffer.append(value);
    ParserCursor cursor = new ParserCursor(0, value.length());
    return ((parser != null) ? parser : INSTANCE).parseElements(buffer, cursor);
  }
  
  public HeaderElement[] parseElements(CharArrayBuffer buffer, ParserCursor cursor) {
    Args.notNull(buffer, "Char array buffer");
    Args.notNull(cursor, "Parser cursor");
    List<HeaderElement> elements = new ArrayList<HeaderElement>();
    while (!cursor.atEnd()) {
      HeaderElement element = parseHeaderElement(buffer, cursor);
      if (element.getName().length() != 0 || element.getValue() != null)
        elements.add(element); 
    } 
    return elements.<HeaderElement>toArray(new HeaderElement[elements.size()]);
  }
  
  public static HeaderElement parseHeaderElement(String value, HeaderValueParser parser) throws ParseException {
    Args.notNull(value, "Value");
    CharArrayBuffer buffer = new CharArrayBuffer(value.length());
    buffer.append(value);
    ParserCursor cursor = new ParserCursor(0, value.length());
    return ((parser != null) ? parser : INSTANCE).parseHeaderElement(buffer, cursor);
  }
  
  public HeaderElement parseHeaderElement(CharArrayBuffer buffer, ParserCursor cursor) {
    Args.notNull(buffer, "Char array buffer");
    Args.notNull(cursor, "Parser cursor");
    NameValuePair nvp = parseNameValuePair(buffer, cursor);
    NameValuePair[] params = null;
    if (!cursor.atEnd()) {
      char ch = buffer.charAt(cursor.getPos() - 1);
      if (ch != ',')
        params = parseParameters(buffer, cursor); 
    } 
    return createHeaderElement(nvp.getName(), nvp.getValue(), params);
  }
  
  protected HeaderElement createHeaderElement(String name, String value, NameValuePair[] params) {
    return new BasicHeaderElement(name, value, params);
  }
  
  public static NameValuePair[] parseParameters(String value, HeaderValueParser parser) throws ParseException {
    Args.notNull(value, "Value");
    CharArrayBuffer buffer = new CharArrayBuffer(value.length());
    buffer.append(value);
    ParserCursor cursor = new ParserCursor(0, value.length());
    return ((parser != null) ? parser : INSTANCE).parseParameters(buffer, cursor);
  }
  
  public NameValuePair[] parseParameters(CharArrayBuffer buffer, ParserCursor cursor) {
    Args.notNull(buffer, "Char array buffer");
    Args.notNull(cursor, "Parser cursor");
    int pos = cursor.getPos();
    int indexTo = cursor.getUpperBound();
    while (pos < indexTo) {
      char ch = buffer.charAt(pos);
      if (HTTP.isWhitespace(ch))
        pos++; 
    } 
    cursor.updatePos(pos);
    if (cursor.atEnd())
      return new NameValuePair[0]; 
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    while (!cursor.atEnd()) {
      NameValuePair param = parseNameValuePair(buffer, cursor);
      params.add(param);
      char ch = buffer.charAt(cursor.getPos() - 1);
      if (ch == ',')
        break; 
    } 
    return params.<NameValuePair>toArray(new NameValuePair[params.size()]);
  }
  
  public static NameValuePair parseNameValuePair(String value, HeaderValueParser parser) throws ParseException {
    Args.notNull(value, "Value");
    CharArrayBuffer buffer = new CharArrayBuffer(value.length());
    buffer.append(value);
    ParserCursor cursor = new ParserCursor(0, value.length());
    return ((parser != null) ? parser : INSTANCE).parseNameValuePair(buffer, cursor);
  }
  
  public NameValuePair parseNameValuePair(CharArrayBuffer buffer, ParserCursor cursor) {
    return parseNameValuePair(buffer, cursor, ALL_DELIMITERS);
  }
  
  private static boolean isOneOf(char ch, char[] chs) {
    if (chs != null)
      for (char ch2 : chs) {
        if (ch == ch2)
          return true; 
      }  
    return false;
  }
  
  public NameValuePair parseNameValuePair(CharArrayBuffer buffer, ParserCursor cursor, char[] delimiters) {
    String name;
    Args.notNull(buffer, "Char array buffer");
    Args.notNull(cursor, "Parser cursor");
    boolean terminated = false;
    int pos = cursor.getPos();
    int indexFrom = cursor.getPos();
    int indexTo = cursor.getUpperBound();
    while (pos < indexTo) {
      char ch = buffer.charAt(pos);
      if (ch == '=')
        break; 
      if (isOneOf(ch, delimiters)) {
        terminated = true;
        break;
      } 
      pos++;
    } 
    if (pos == indexTo) {
      terminated = true;
      name = buffer.substringTrimmed(indexFrom, indexTo);
    } else {
      name = buffer.substringTrimmed(indexFrom, pos);
      pos++;
    } 
    if (terminated) {
      cursor.updatePos(pos);
      return createNameValuePair(name, null);
    } 
    int i1 = pos;
    boolean qouted = false;
    boolean escaped = false;
    while (pos < indexTo) {
      char ch = buffer.charAt(pos);
      if (ch == '"' && !escaped)
        qouted = !qouted; 
      if (!qouted && !escaped && isOneOf(ch, delimiters)) {
        terminated = true;
        break;
      } 
      if (escaped) {
        escaped = false;
      } else {
        escaped = (qouted && ch == '\\');
      } 
      pos++;
    } 
    int i2 = pos;
    while (i1 < i2 && HTTP.isWhitespace(buffer.charAt(i1)))
      i1++; 
    while (i2 > i1 && HTTP.isWhitespace(buffer.charAt(i2 - 1)))
      i2--; 
    if (i2 - i1 >= 2 && buffer.charAt(i1) == '"' && buffer.charAt(i2 - 1) == '"') {
      i1++;
      i2--;
    } 
    String value = buffer.substring(i1, i2);
    if (terminated)
      pos++; 
    cursor.updatePos(pos);
    return createNameValuePair(name, value);
  }
  
  protected NameValuePair createNameValuePair(String name, String value) {
    return new BasicNameValuePair(name, value);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\message\BasicHeaderValueParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */