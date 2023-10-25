package io.netty.util.internal.logging;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import org.slf4j.LoggerFactory;

public class Slf4JLoggerFactory extends InternalLoggerFactory {
  public Slf4JLoggerFactory() {}
  
  Slf4JLoggerFactory(boolean failIfNOP) {
    assert failIfNOP;
    final StringBuffer buf = new StringBuffer();
    PrintStream err = System.err;
    try {
      System.setErr(new PrintStream(new OutputStream() {
              public void write(int b) {
                buf.append((char)b);
              }
            },  true, "US-ASCII"));
    } catch (UnsupportedEncodingException e) {
      throw new Error(e);
    } 
    try {
      if (LoggerFactory.getILoggerFactory() instanceof org.slf4j.helpers.NOPLoggerFactory)
        throw new NoClassDefFoundError(buf.toString()); 
      err.print(buf);
      err.flush();
    } finally {
      System.setErr(err);
    } 
  }
  
  public InternalLogger newInstance(String name) {
    return new Slf4JLogger(LoggerFactory.getLogger(name));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\logging\Slf4JLoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */