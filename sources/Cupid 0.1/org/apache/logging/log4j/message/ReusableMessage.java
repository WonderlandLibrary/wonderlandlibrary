package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.StringBuilderFormattable;

@PerformanceSensitive({"allocation"})
public interface ReusableMessage extends Message, StringBuilderFormattable {
  Object[] swapParameters(Object[] paramArrayOfObject);
  
  short getParameterCount();
  
  Message memento();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\ReusableMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */