package org.apache.logging.log4j.core.async;

import java.net.URI;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.message.MessageFactory;

public class AsyncLoggerContext extends LoggerContext {
   public AsyncLoggerContext(String var1) {
      super(var1);
   }

   public AsyncLoggerContext(String var1, Object var2) {
      super(var1, var2);
   }

   public AsyncLoggerContext(String var1, Object var2, URI var3) {
      super(var1, var2, var3);
   }

   public AsyncLoggerContext(String var1, Object var2, String var3) {
      super(var1, var2, var3);
   }

   protected Logger newInstance(LoggerContext var1, String var2, MessageFactory var3) {
      return new AsyncLogger(var1, var2, var3);
   }

   public void stop() {
      AsyncLogger.stop();
      super.stop();
   }
}
