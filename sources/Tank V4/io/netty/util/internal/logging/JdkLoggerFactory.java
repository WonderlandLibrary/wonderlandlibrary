package io.netty.util.internal.logging;

import java.util.logging.Logger;

public class JdkLoggerFactory extends InternalLoggerFactory {
   public InternalLogger newInstance(String var1) {
      return new JdkLogger(Logger.getLogger(var1));
   }
}
