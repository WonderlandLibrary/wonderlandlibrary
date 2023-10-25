package nl.matsv.viabackwards.fabric.util;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.apache.logging.log4j.Logger;

public class LoggerWrapper extends Logger {
  private final Logger base;
  
  public LoggerWrapper(Logger logger) {
    super("logger", null);
    this.base = logger;
  }
  
  public void log(LogRecord record) {
    log(record.getLevel(), record.getMessage());
  }
  
  public void log(Level level, String msg) {
    if (level == Level.FINE) {
      this.base.debug(msg);
    } else if (level == Level.WARNING) {
      this.base.warn(msg);
    } else if (level == Level.SEVERE) {
      this.base.error(msg);
    } else if (level == Level.INFO) {
      this.base.info(msg);
    } else {
      this.base.trace(msg);
    } 
  }
  
  public void log(Level level, String msg, Object param1) {
    if (level == Level.FINE) {
      this.base.debug(msg, param1);
    } else if (level == Level.WARNING) {
      this.base.warn(msg, param1);
    } else if (level == Level.SEVERE) {
      this.base.error(msg, param1);
    } else if (level == Level.INFO) {
      this.base.info(msg, param1);
    } else {
      this.base.trace(msg, param1);
    } 
  }
  
  public void log(Level level, String msg, Object[] params) {
    log(level, MessageFormat.format(msg, params));
  }
  
  public void log(Level level, String msg, Throwable params) {
    if (level == Level.FINE) {
      this.base.debug(msg, params);
    } else if (level == Level.WARNING) {
      this.base.warn(msg, params);
    } else if (level == Level.SEVERE) {
      this.base.error(msg, params);
    } else if (level == Level.INFO) {
      this.base.info(msg, params);
    } else {
      this.base.trace(msg, params);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\fabri\\util\LoggerWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */