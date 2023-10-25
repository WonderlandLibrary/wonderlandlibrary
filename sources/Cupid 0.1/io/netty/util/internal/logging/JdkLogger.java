package io.netty.util.internal.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

class JdkLogger extends AbstractInternalLogger {
  private static final long serialVersionUID = -1767272577989225979L;
  
  final transient Logger logger;
  
  JdkLogger(Logger logger) {
    super(logger.getName());
    this.logger = logger;
  }
  
  public boolean isTraceEnabled() {
    return this.logger.isLoggable(Level.FINEST);
  }
  
  public void trace(String msg) {
    if (this.logger.isLoggable(Level.FINEST))
      log(SELF, Level.FINEST, msg, null); 
  }
  
  public void trace(String format, Object arg) {
    if (this.logger.isLoggable(Level.FINEST)) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      log(SELF, Level.FINEST, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void trace(String format, Object argA, Object argB) {
    if (this.logger.isLoggable(Level.FINEST)) {
      FormattingTuple ft = MessageFormatter.format(format, argA, argB);
      log(SELF, Level.FINEST, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void trace(String format, Object... argArray) {
    if (this.logger.isLoggable(Level.FINEST)) {
      FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
      log(SELF, Level.FINEST, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void trace(String msg, Throwable t) {
    if (this.logger.isLoggable(Level.FINEST))
      log(SELF, Level.FINEST, msg, t); 
  }
  
  public boolean isDebugEnabled() {
    return this.logger.isLoggable(Level.FINE);
  }
  
  public void debug(String msg) {
    if (this.logger.isLoggable(Level.FINE))
      log(SELF, Level.FINE, msg, null); 
  }
  
  public void debug(String format, Object arg) {
    if (this.logger.isLoggable(Level.FINE)) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      log(SELF, Level.FINE, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void debug(String format, Object argA, Object argB) {
    if (this.logger.isLoggable(Level.FINE)) {
      FormattingTuple ft = MessageFormatter.format(format, argA, argB);
      log(SELF, Level.FINE, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void debug(String format, Object... argArray) {
    if (this.logger.isLoggable(Level.FINE)) {
      FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
      log(SELF, Level.FINE, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void debug(String msg, Throwable t) {
    if (this.logger.isLoggable(Level.FINE))
      log(SELF, Level.FINE, msg, t); 
  }
  
  public boolean isInfoEnabled() {
    return this.logger.isLoggable(Level.INFO);
  }
  
  public void info(String msg) {
    if (this.logger.isLoggable(Level.INFO))
      log(SELF, Level.INFO, msg, null); 
  }
  
  public void info(String format, Object arg) {
    if (this.logger.isLoggable(Level.INFO)) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      log(SELF, Level.INFO, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void info(String format, Object argA, Object argB) {
    if (this.logger.isLoggable(Level.INFO)) {
      FormattingTuple ft = MessageFormatter.format(format, argA, argB);
      log(SELF, Level.INFO, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void info(String format, Object... argArray) {
    if (this.logger.isLoggable(Level.INFO)) {
      FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
      log(SELF, Level.INFO, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void info(String msg, Throwable t) {
    if (this.logger.isLoggable(Level.INFO))
      log(SELF, Level.INFO, msg, t); 
  }
  
  public boolean isWarnEnabled() {
    return this.logger.isLoggable(Level.WARNING);
  }
  
  public void warn(String msg) {
    if (this.logger.isLoggable(Level.WARNING))
      log(SELF, Level.WARNING, msg, null); 
  }
  
  public void warn(String format, Object arg) {
    if (this.logger.isLoggable(Level.WARNING)) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      log(SELF, Level.WARNING, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void warn(String format, Object argA, Object argB) {
    if (this.logger.isLoggable(Level.WARNING)) {
      FormattingTuple ft = MessageFormatter.format(format, argA, argB);
      log(SELF, Level.WARNING, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void warn(String format, Object... argArray) {
    if (this.logger.isLoggable(Level.WARNING)) {
      FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
      log(SELF, Level.WARNING, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void warn(String msg, Throwable t) {
    if (this.logger.isLoggable(Level.WARNING))
      log(SELF, Level.WARNING, msg, t); 
  }
  
  public boolean isErrorEnabled() {
    return this.logger.isLoggable(Level.SEVERE);
  }
  
  public void error(String msg) {
    if (this.logger.isLoggable(Level.SEVERE))
      log(SELF, Level.SEVERE, msg, null); 
  }
  
  public void error(String format, Object arg) {
    if (this.logger.isLoggable(Level.SEVERE)) {
      FormattingTuple ft = MessageFormatter.format(format, arg);
      log(SELF, Level.SEVERE, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void error(String format, Object argA, Object argB) {
    if (this.logger.isLoggable(Level.SEVERE)) {
      FormattingTuple ft = MessageFormatter.format(format, argA, argB);
      log(SELF, Level.SEVERE, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void error(String format, Object... arguments) {
    if (this.logger.isLoggable(Level.SEVERE)) {
      FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
      log(SELF, Level.SEVERE, ft.getMessage(), ft.getThrowable());
    } 
  }
  
  public void error(String msg, Throwable t) {
    if (this.logger.isLoggable(Level.SEVERE))
      log(SELF, Level.SEVERE, msg, t); 
  }
  
  private void log(String callerFQCN, Level level, String msg, Throwable t) {
    LogRecord record = new LogRecord(level, msg);
    record.setLoggerName(name());
    record.setThrown(t);
    fillCallerData(callerFQCN, record);
    this.logger.log(record);
  }
  
  static final String SELF = JdkLogger.class.getName();
  
  static final String SUPER = AbstractInternalLogger.class.getName();
  
  private static void fillCallerData(String callerFQCN, LogRecord record) {
    StackTraceElement[] steArray = (new Throwable()).getStackTrace();
    int selfIndex = -1;
    for (int i = 0; i < steArray.length; i++) {
      String className = steArray[i].getClassName();
      if (className.equals(callerFQCN) || className.equals(SUPER)) {
        selfIndex = i;
        break;
      } 
    } 
    int found = -1;
    for (int j = selfIndex + 1; j < steArray.length; j++) {
      String className = steArray[j].getClassName();
      if (!className.equals(callerFQCN) && !className.equals(SUPER)) {
        found = j;
        break;
      } 
    } 
    if (found != -1) {
      StackTraceElement ste = steArray[found];
      record.setSourceClassName(ste.getClassName());
      record.setSourceMethodName(ste.getMethodName());
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\logging\JdkLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */