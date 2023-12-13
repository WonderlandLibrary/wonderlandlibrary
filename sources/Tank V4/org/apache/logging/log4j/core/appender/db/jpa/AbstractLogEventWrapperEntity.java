package org.apache.logging.log4j.core.appender.db.jpa;

import java.util.Map;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;

@MappedSuperclass
@Inheritance(
   strategy = InheritanceType.SINGLE_TABLE
)
public abstract class AbstractLogEventWrapperEntity implements LogEvent {
   private static final long serialVersionUID = 1L;
   private final LogEvent wrappedEvent;

   protected AbstractLogEventWrapperEntity() {
      this(new AbstractLogEventWrapperEntity.NullLogEvent());
   }

   protected AbstractLogEventWrapperEntity(LogEvent var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("The wrapped event cannot be null.");
      } else {
         this.wrappedEvent = var1;
      }
   }

   @Transient
   protected final LogEvent getWrappedEvent() {
      return this.wrappedEvent;
   }

   public void setLevel(Level var1) {
   }

   public void setLoggerName(String var1) {
   }

   public void setSource(StackTraceElement var1) {
   }

   public void setMessage(Message var1) {
   }

   public void setMarker(Marker var1) {
   }

   public void setThreadName(String var1) {
   }

   public void setMillis(long var1) {
   }

   public void setThrown(Throwable var1) {
   }

   public void setContextMap(Map var1) {
   }

   public void setContextStack(ThreadContext.ContextStack var1) {
   }

   public void setFQCN(String var1) {
   }

   @Transient
   public final boolean isIncludeLocation() {
      return this.getWrappedEvent().isIncludeLocation();
   }

   public final void setIncludeLocation(boolean var1) {
      this.getWrappedEvent().setIncludeLocation(var1);
   }

   @Transient
   public final boolean isEndOfBatch() {
      return this.getWrappedEvent().isEndOfBatch();
   }

   public final void setEndOfBatch(boolean var1) {
      this.getWrappedEvent().setEndOfBatch(var1);
   }

   private static class NullLogEvent implements LogEvent {
      private static final long serialVersionUID = 1L;

      private NullLogEvent() {
      }

      public Level getLevel() {
         return null;
      }

      public String getLoggerName() {
         return null;
      }

      public StackTraceElement getSource() {
         return null;
      }

      public Message getMessage() {
         return null;
      }

      public Marker getMarker() {
         return null;
      }

      public String getThreadName() {
         return null;
      }

      public long getMillis() {
         return 0L;
      }

      public Throwable getThrown() {
         return null;
      }

      public Map getContextMap() {
         return null;
      }

      public ThreadContext.ContextStack getContextStack() {
         return null;
      }

      public String getFQCN() {
         return null;
      }

      public boolean isIncludeLocation() {
         return false;
      }

      public void setIncludeLocation(boolean var1) {
      }

      public boolean isEndOfBatch() {
         return false;
      }

      public void setEndOfBatch(boolean var1) {
      }

      NullLogEvent(Object var1) {
         this();
      }
   }
}
