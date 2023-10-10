package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.EventTranslator;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.Message;

public class RingBufferLogEventTranslator implements EventTranslator {
   private AsyncLogger asyncLogger;
   private String loggerName;
   private Marker marker;
   private String fqcn;
   private Level level;
   private Message message;
   private Throwable thrown;
   private Map contextMap;
   private ThreadContext.ContextStack contextStack;
   private String threadName;
   private StackTraceElement location;
   private long currentTimeMillis;

   public void translateTo(RingBufferLogEvent var1, long var2) {
      var1.setValues(this.asyncLogger, this.loggerName, this.marker, this.fqcn, this.level, this.message, this.thrown, this.contextMap, this.contextStack, this.threadName, this.location, this.currentTimeMillis);
   }

   public void setValues(AsyncLogger var1, String var2, Marker var3, String var4, Level var5, Message var6, Throwable var7, Map var8, ThreadContext.ContextStack var9, String var10, StackTraceElement var11, long var12) {
      this.asyncLogger = var1;
      this.loggerName = var2;
      this.marker = var3;
      this.fqcn = var4;
      this.level = var5;
      this.message = var6;
      this.thrown = var7;
      this.contextMap = var8;
      this.contextStack = var9;
      this.threadName = var10;
      this.location = var11;
      this.currentTimeMillis = var12;
   }

   public void translateTo(Object var1, long var2) {
      this.translateTo((RingBufferLogEvent)var1, var2);
   }
}
