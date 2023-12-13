package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.EventFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessage;

public class RingBufferLogEvent implements LogEvent {
   private static final long serialVersionUID = 8462119088943934758L;
   public static final RingBufferLogEvent.Factory FACTORY = new RingBufferLogEvent.Factory();
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
   private boolean endOfBatch;
   private boolean includeLocation;

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

   public void execute(boolean var1) {
      this.endOfBatch = var1;
      this.asyncLogger.actualAsyncLog(this);
   }

   public boolean isEndOfBatch() {
      return this.endOfBatch;
   }

   public void setEndOfBatch(boolean var1) {
      this.endOfBatch = var1;
   }

   public boolean isIncludeLocation() {
      return this.includeLocation;
   }

   public void setIncludeLocation(boolean var1) {
      this.includeLocation = var1;
   }

   public String getLoggerName() {
      return this.loggerName;
   }

   public Marker getMarker() {
      return this.marker;
   }

   public String getFQCN() {
      return this.fqcn;
   }

   public Level getLevel() {
      return this.level;
   }

   public Message getMessage() {
      if (this.message == null) {
         this.message = new SimpleMessage("");
      }

      return this.message;
   }

   public Throwable getThrown() {
      return this.thrown;
   }

   public Map getContextMap() {
      return this.contextMap;
   }

   public ThreadContext.ContextStack getContextStack() {
      return this.contextStack;
   }

   public String getThreadName() {
      return this.threadName;
   }

   public StackTraceElement getSource() {
      return this.location;
   }

   public long getMillis() {
      return this.currentTimeMillis;
   }

   public void mergePropertiesIntoContextMap(Map var1, StrSubstitutor var2) {
      if (var1 != null) {
         HashMap var3 = this.contextMap == null ? new HashMap() : new HashMap(this.contextMap);
         Iterator var4 = var1.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            Property var6 = (Property)var5.getKey();
            if (!var3.containsKey(var6.getName())) {
               String var7 = (Boolean)var5.getValue() ? var2.replace(var6.getValue()) : var6.getValue();
               var3.put(var6.getName(), var7);
            }
         }

         this.contextMap = var3;
      }
   }

   public void clear() {
      this.setValues((AsyncLogger)null, (String)null, (Marker)null, (String)null, (Level)null, (Message)null, (Throwable)null, (Map)null, (ThreadContext.ContextStack)null, (String)null, (StackTraceElement)null, 0L);
   }

   private static class Factory implements EventFactory {
      private Factory() {
      }

      public RingBufferLogEvent newInstance() {
         return new RingBufferLogEvent();
      }

      public Object newInstance() {
         return this.newInstance();
      }

      Factory(Object var1) {
         this();
      }
   }
}
