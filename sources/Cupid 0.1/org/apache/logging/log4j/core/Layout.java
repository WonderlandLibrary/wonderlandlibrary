package org.apache.logging.log4j.core;

import java.util.Map;
import org.apache.logging.log4j.core.layout.Encoder;

public interface Layout<T extends java.io.Serializable> extends Encoder<LogEvent> {
  public static final String ELEMENT_TYPE = "layout";
  
  byte[] getFooter();
  
  byte[] getHeader();
  
  byte[] toByteArray(LogEvent paramLogEvent);
  
  T toSerializable(LogEvent paramLogEvent);
  
  String getContentType();
  
  Map<String, String> getContentFormat();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\Layout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */