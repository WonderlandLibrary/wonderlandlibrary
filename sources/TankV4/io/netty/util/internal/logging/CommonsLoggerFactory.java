package io.netty.util.internal.logging;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.LogFactory;

public class CommonsLoggerFactory extends InternalLoggerFactory {
   Map loggerMap = new HashMap();

   public InternalLogger newInstance(String var1) {
      return new CommonsLogger(LogFactory.getLog(var1), var1);
   }
}
