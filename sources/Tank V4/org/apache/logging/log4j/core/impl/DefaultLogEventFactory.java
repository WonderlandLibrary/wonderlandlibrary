package org.apache.logging.log4j.core.impl;

import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;

public class DefaultLogEventFactory implements LogEventFactory {
   public LogEvent createEvent(String var1, Marker var2, String var3, Level var4, Message var5, List var6, Throwable var7) {
      return new Log4jLogEvent(var1, var2, var3, var4, var5, var6, var7);
   }
}
