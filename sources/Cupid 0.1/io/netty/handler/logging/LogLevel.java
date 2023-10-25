package io.netty.handler.logging;

import io.netty.util.internal.logging.InternalLogLevel;

public enum LogLevel {
  TRACE(InternalLogLevel.TRACE),
  DEBUG(InternalLogLevel.DEBUG),
  INFO(InternalLogLevel.INFO),
  WARN(InternalLogLevel.WARN),
  ERROR(InternalLogLevel.ERROR);
  
  private final InternalLogLevel internalLevel;
  
  LogLevel(InternalLogLevel internalLevel) {
    this.internalLevel = internalLevel;
  }
  
  InternalLogLevel toInternalLevel() {
    return this.internalLevel;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\logging\LogLevel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */