package org.apache.logging.log4j.core.parser;

import org.apache.logging.log4j.core.LogEvent;

public interface LogEventParser {
  LogEvent parseFrom(byte[] paramArrayOfbyte) throws ParseException;
  
  LogEvent parseFrom(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws ParseException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\parser\LogEventParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */