package io.netty.util.internal.logging;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.LogFactory;

public class CommonsLoggerFactory extends InternalLoggerFactory {
  Map<String, InternalLogger> loggerMap = new HashMap<String, InternalLogger>();
  
  public InternalLogger newInstance(String name) {
    return new CommonsLogger(LogFactory.getLog(name), name);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\logging\CommonsLoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */