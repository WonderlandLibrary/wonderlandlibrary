package org.apache.logging.log4j.core.util;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public final class Throwables {
  public static Throwable getRootCause(Throwable throwable) {
    Throwable slowPointer = throwable;
    boolean advanceSlowPointer = false;
    Throwable parent = throwable;
    Throwable cause;
    while ((cause = parent.getCause()) != null) {
      parent = cause;
      if (parent == slowPointer)
        throw new IllegalArgumentException("loop in causal chain"); 
      if (advanceSlowPointer)
        slowPointer = slowPointer.getCause(); 
      advanceSlowPointer = !advanceSlowPointer;
    } 
    return parent;
  }
  
  public static List<String> toStringList(Throwable throwable) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    try {
      throwable.printStackTrace(pw);
    } catch (RuntimeException runtimeException) {}
    pw.flush();
    List<String> lines = new ArrayList<>();
    LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
    try {
      String line = reader.readLine();
      while (line != null) {
        lines.add(line);
        line = reader.readLine();
      } 
    } catch (IOException ex) {
      if (ex instanceof java.io.InterruptedIOException)
        Thread.currentThread().interrupt(); 
      lines.add(ex.toString());
    } finally {
      Closer.closeSilently(reader);
    } 
    return lines;
  }
  
  public static void rethrow(Throwable t) {
    rethrow0(t);
  }
  
  private static <T extends Throwable> void rethrow0(Throwable t) throws T {
    throw (T)t;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\Throwables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */