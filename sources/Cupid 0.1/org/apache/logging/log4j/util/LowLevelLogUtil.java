package org.apache.logging.log4j.util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Objects;

final class LowLevelLogUtil {
  private static PrintWriter writer = new PrintWriter(System.err, true);
  
  public static void log(String message) {
    if (message != null)
      writer.println(message); 
  }
  
  public static void logException(Throwable exception) {
    if (exception != null)
      exception.printStackTrace(writer); 
  }
  
  public static void logException(String message, Throwable exception) {
    log(message);
    logException(exception);
  }
  
  public static void setOutputStream(OutputStream out) {
    writer = new PrintWriter(Objects.<OutputStream>requireNonNull(out), true);
  }
  
  public static void setWriter(Writer writer) {
    LowLevelLogUtil.writer = new PrintWriter(Objects.<Writer>requireNonNull(writer), true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\LowLevelLogUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */