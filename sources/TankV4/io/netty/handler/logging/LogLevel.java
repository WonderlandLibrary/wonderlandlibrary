package io.netty.handler.logging;

import io.netty.util.internal.logging.InternalLogLevel;

public enum LogLevel {
   TRACE(InternalLogLevel.TRACE),
   DEBUG(InternalLogLevel.DEBUG),
   INFO(InternalLogLevel.INFO),
   WARN(InternalLogLevel.WARN),
   ERROR(InternalLogLevel.ERROR);

   private final InternalLogLevel internalLevel;
   private static final LogLevel[] $VALUES = new LogLevel[]{TRACE, DEBUG, INFO, WARN, ERROR};

   private LogLevel(InternalLogLevel var3) {
      this.internalLevel = var3;
   }

   InternalLogLevel toInternalLevel() {
      return this.internalLevel;
   }
}
