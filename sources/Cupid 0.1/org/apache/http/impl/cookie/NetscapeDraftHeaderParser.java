package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.annotation.Immutable;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.ParserCursor;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class NetscapeDraftHeaderParser {
  public static final NetscapeDraftHeaderParser DEFAULT = new NetscapeDraftHeaderParser();
  
  public HeaderElement parseHeader(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
    Args.notNull(buffer, "Char array buffer");
    Args.notNull(cursor, "Parser cursor");
    NameValuePair nvp = parseNameValuePair(buffer, cursor);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    while (!cursor.atEnd()) {
      NameValuePair param = parseNameValuePair(buffer, cursor);
      params.add(param);
    } 
    return (HeaderElement)new BasicHeaderElement(nvp.getName(), nvp.getValue(), params.<NameValuePair>toArray(new NameValuePair[params.size()]));
  }
  
  private NameValuePair parseNameValuePair(CharArrayBuffer buffer, ParserCursor cursor) {
    boolean terminated = false;
    int pos = cursor.getPos();
    int indexFrom = cursor.getPos();
    int indexTo = cursor.getUpperBound();
    String name = null;
    while (pos < indexTo) {
      char ch = buffer.charAt(pos);
      if (ch == '=')
        break; 
      if (ch == ';') {
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
      return (NameValuePair)new BasicNameValuePair(name, null);
    } 
    String value = null;
    int i1 = pos;
    while (pos < indexTo) {
      char ch = buffer.charAt(pos);
      if (ch == ';') {
        terminated = true;
        break;
      } 
      pos++;
    } 
    int i2 = pos;
    while (i1 < i2 && HTTP.isWhitespace(buffer.charAt(i1)))
      i1++; 
    while (i2 > i1 && HTTP.isWhitespace(buffer.charAt(i2 - 1)))
      i2--; 
    value = buffer.substring(i1, i2);
    if (terminated)
      pos++; 
    cursor.updatePos(pos);
    return (NameValuePair)new BasicNameValuePair(name, value);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\cookie\NetscapeDraftHeaderParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */