package org.apache.logging.log4j.simple;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerRegistry;
import org.apache.logging.log4j.util.PropertiesUtil;

public class SimpleLoggerContext implements LoggerContext {
  private static final String SYSTEM_OUT = "system.out";
  
  private static final String SYSTEM_ERR = "system.err";
  
  protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
  
  protected static final String SYSTEM_PREFIX = "org.apache.logging.log4j.simplelog.";
  
  private final PropertiesUtil props;
  
  private final boolean showLogName;
  
  private final boolean showShortName;
  
  private final boolean showDateTime;
  
  private final boolean showContextMap;
  
  private final String dateTimeFormat;
  
  private final Level defaultLevel;
  
  private final PrintStream stream;
  
  private final LoggerRegistry<ExtendedLogger> loggerRegistry;
  
  public SimpleLoggerContext() {
    PrintStream ps;
    this.loggerRegistry = new LoggerRegistry();
    this.props = new PropertiesUtil("log4j2.simplelog.properties");
    this.showContextMap = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showContextMap", false);
    this.showLogName = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showlogname", false);
    this.showShortName = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showShortLogname", true);
    this.showDateTime = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showdatetime", false);
    String lvl = this.props.getStringProperty("org.apache.logging.log4j.simplelog.level");
    this.defaultLevel = Level.toLevel(lvl, Level.ERROR);
    this.dateTimeFormat = this.showDateTime ? this.props.getStringProperty("org.apache.logging.log4j.simplelog.dateTimeFormat", "yyyy/MM/dd HH:mm:ss:SSS zzz") : null;
    String fileName = this.props.getStringProperty("org.apache.logging.log4j.simplelog.logFile", "system.err");
    if ("system.err".equalsIgnoreCase(fileName)) {
      ps = System.err;
    } else if ("system.out".equalsIgnoreCase(fileName)) {
      ps = System.out;
    } else {
      try {
        FileOutputStream os = new FileOutputStream(fileName);
        ps = new PrintStream(os);
      } catch (FileNotFoundException fnfe) {
        ps = System.err;
      } 
    } 
    this.stream = ps;
  }
  
  public ExtendedLogger getLogger(String name) {
    return getLogger(name, null);
  }
  
  public ExtendedLogger getLogger(String name, MessageFactory messageFactory) {
    ExtendedLogger extendedLogger = this.loggerRegistry.getLogger(name, messageFactory);
    if (extendedLogger != null) {
      AbstractLogger.checkMessageFactory(extendedLogger, messageFactory);
      return extendedLogger;
    } 
    SimpleLogger simpleLogger = new SimpleLogger(name, this.defaultLevel, this.showLogName, this.showShortName, this.showDateTime, this.showContextMap, this.dateTimeFormat, messageFactory, this.props, this.stream);
    this.loggerRegistry.putIfAbsent(name, messageFactory, (ExtendedLogger)simpleLogger);
    return this.loggerRegistry.getLogger(name, messageFactory);
  }
  
  public boolean hasLogger(String name) {
    return false;
  }
  
  public boolean hasLogger(String name, MessageFactory messageFactory) {
    return false;
  }
  
  public boolean hasLogger(String name, Class<? extends MessageFactory> messageFactoryClass) {
    return false;
  }
  
  public Object getExternalContext() {
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\simple\SimpleLoggerContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */