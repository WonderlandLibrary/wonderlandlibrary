package io.netty.util.internal.logging;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

class Log4JLogger extends AbstractInternalLogger {
  private static final long serialVersionUID = 2851357342488183058L;
  
  final transient Logger logger;
  
  static final String FQCN = Log4JLogger.class.getName();
  
  final boolean traceCapable;
  
  Log4JLogger(Logger logger) {
    super(logger.getName());
    this.logger = logger;
    this.traceCapable = isTraceCapable();
  }
  
  private boolean isTraceCapable() {
    try {
      this.logger.isTraceEnabled();
      return true;
    } catch (NoSuchMethodError ignored) {
      return false;
    } 
  }
  
  public boolean isTraceEnabled() {
    if (this.traceCapable)
      return this.logger.isTraceEnabled(); 
    return this.logger.isDebugEnabled();
  }
  
  public void trace(String msg) {
    this.logger.log(FQCN, this.traceCapable ? (Priority)Level.TRACE : (Priority)Level.DEBUG, msg, null);
  }
  
  public void trace(String format, Object arg) {
    if (isTraceEnabled()) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      this.logger.log(FQCN, this.traceCapable ? (Priority)Level.TRACE : (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void trace(String format, Object argA, Object argB) {
    if (isTraceEnabled()) {
      FormattingTuple ft = MessageFormatter.format(format, argA, argB);
      this.logger.log(FQCN, this.traceCapable ? (Priority)Level.TRACE : (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void trace(String format, Object... arguments) {
    if (isTraceEnabled()) {
      FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
      this.logger.log(FQCN, this.traceCapable ? (Priority)Level.TRACE : (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void trace(String msg, Throwable t) {
    this.logger.log(FQCN, this.traceCapable ? (Priority)Level.TRACE : (Priority)Level.DEBUG, msg, t);
  }
  
  public boolean isDebugEnabled() {
    return this.logger.isDebugEnabled();
  }
  
  public void debug(String msg) {
    this.logger.log(FQCN, (Priority)Level.DEBUG, msg, null);
  }
  
  public void debug(String format, Object arg) {
    if (this.logger.isDebugEnabled()) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      this.logger.log(FQCN, (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void debug(String format, Object argA, Object argB) {
    if (this.logger.isDebugEnabled()) {
      FormattingTuple ft = MessageFormatter.format(format, argA, argB);
      this.logger.log(FQCN, (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void debug(String format, Object... arguments) {
    if (this.logger.isDebugEnabled()) {
      FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
      this.logger.log(FQCN, (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void debug(String msg, Throwable t) {
    this.logger.log(FQCN, (Priority)Level.DEBUG, msg, t);
  }
  
  public boolean isInfoEnabled() {
    return this.logger.isInfoEnabled();
  }
  
  public void info(String msg) {
    this.logger.log(FQCN, (Priority)Level.INFO, msg, null);
  }
  
  public void info(String format, Object arg) {
    if (this.logger.isInfoEnabled()) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      this.logger.log(FQCN, (Priority)Level.INFO, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void info(String format, Object argA, Object argB) {
    if (this.logger.isInfoEnabled()) {
      FormattingTuple ft = MessageFormatter.format(format, argA, argB);
      this.logger.log(FQCN, (Priority)Level.INFO, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void info(String format, Object... argArray) {
    if (this.logger.isInfoEnabled()) {
      FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
      this.logger.log(FQCN, (Priority)Level.INFO, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void info(String msg, Throwable t) {
    this.logger.log(FQCN, (Priority)Level.INFO, msg, t);
  }
  
  public boolean isWarnEnabled() {
    return this.logger.isEnabledFor((Priority)Level.WARN);
  }
  
  public void warn(String msg) {
    this.logger.log(FQCN, (Priority)Level.WARN, msg, null);
  }
  
  public void warn(String format, Object arg) {
    if (this.logger.isEnabledFor((Priority)Level.WARN)) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      this.logger.log(FQCN, (Priority)Level.WARN, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void warn(String format, Object argA, Object argB) {
    if (this.logger.isEnabledFor((Priority)Level.WARN)) {
      FormattingTuple ft = MessageFormatter.format(format, argA, argB);
      this.logger.log(FQCN, (Priority)Level.WARN, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void warn(String format, Object... argArray) {
    if (this.logger.isEnabledFor((Priority)Level.WARN)) {
      FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
      this.logger.log(FQCN, (Priority)Level.WARN, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void warn(String msg, Throwable t) {
    this.logger.log(FQCN, (Priority)Level.WARN, msg, t);
  }
  
  public boolean isErrorEnabled() {
    return this.logger.isEnabledFor((Priority)Level.ERROR);
  }
  
  public void error(String msg) {
    this.logger.log(FQCN, (Priority)Level.ERROR, msg, null);
  }
  
  public void error(String format, Object arg) {
    if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      this.logger.log(FQCN, (Priority)Level.ERROR, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void error(String format, Object argA, Object argB) {
    if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
      FormattingTuple ft = MessageFormatter.format(format, argA, argB);
      this.logger.log(FQCN, (Priority)Level.ERROR, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void error(String format, Object... argArray) {
    if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
      FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
      this.logger.log(FQCN, (Priority)Level.ERROR, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void error(String msg, Throwable t) {
    this.logger.log(FQCN, (Priority)Level.ERROR, msg, t);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\logging\Log4JLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */