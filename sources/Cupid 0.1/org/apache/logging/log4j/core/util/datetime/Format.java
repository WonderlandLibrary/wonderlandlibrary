package org.apache.logging.log4j.core.util.datetime;

import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;

public abstract class Format {
  public final String format(Object obj) {
    return format(obj, new StringBuilder(), new FieldPosition(0)).toString();
  }
  
  public abstract StringBuilder format(Object paramObject, StringBuilder paramStringBuilder, FieldPosition paramFieldPosition);
  
  public abstract Object parseObject(String paramString, ParsePosition paramParsePosition);
  
  public Object parseObject(String source) throws ParseException {
    ParsePosition pos = new ParsePosition(0);
    Object result = parseObject(source, pos);
    if (pos.getIndex() == 0)
      throw new ParseException("Format.parseObject(String) failed", pos.getErrorIndex()); 
    return result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\datetime\Format.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */