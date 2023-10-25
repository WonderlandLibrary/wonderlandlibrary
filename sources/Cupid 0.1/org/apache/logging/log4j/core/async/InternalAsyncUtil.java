package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.message.AsynchronouslyFormattable;
import org.apache.logging.log4j.message.Message;

public class InternalAsyncUtil {
  public static Message makeMessageImmutable(Message msg) {
    if (msg != null && !canFormatMessageInBackground(msg))
      msg.getFormattedMessage(); 
    return msg;
  }
  
  private static boolean canFormatMessageInBackground(Message message) {
    return (Constants.FORMAT_MESSAGES_IN_BACKGROUND || message
      .getClass().isAnnotationPresent((Class)AsynchronouslyFormattable.class));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\InternalAsyncUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */