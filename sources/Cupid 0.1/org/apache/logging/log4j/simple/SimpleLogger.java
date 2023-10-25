package org.apache.logging.log4j.simple;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.Strings;

public class SimpleLogger extends AbstractLogger {
  private static final long serialVersionUID = 1L;
  
  private static final char SPACE = ' ';
  
  private final DateFormat dateFormatter;
  
  private Level level;
  
  private final boolean showDateTime;
  
  private final boolean showContextMap;
  
  private PrintStream stream;
  
  private final String logName;
  
  public SimpleLogger(String name, Level defaultLevel, boolean showLogName, boolean showShortLogName, boolean showDateTime, boolean showContextMap, String dateTimeFormat, MessageFactory messageFactory, PropertiesUtil props, PrintStream stream) {
    super(name, messageFactory);
    String lvl = props.getStringProperty("org.apache.logging.log4j.simplelog." + name + ".level");
    this.level = Level.toLevel(lvl, defaultLevel);
    if (showShortLogName) {
      int index = name.lastIndexOf(".");
      if (index > 0 && index < name.length()) {
        this.logName = name.substring(index + 1);
      } else {
        this.logName = name;
      } 
    } else if (showLogName) {
      this.logName = name;
    } else {
      this.logName = null;
    } 
    this.showDateTime = showDateTime;
    this.showContextMap = showContextMap;
    this.stream = stream;
    if (showDateTime) {
      DateFormat format;
      try {
        format = new SimpleDateFormat(dateTimeFormat);
      } catch (IllegalArgumentException e) {
        format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS zzz");
      } 
      this.dateFormatter = format;
    } else {
      this.dateFormatter = null;
    } 
  }
  
  public Level getLevel() {
    return this.level;
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, Message msg, Throwable t) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, CharSequence msg, Throwable t) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, Object msg, Throwable t) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, String msg) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, String msg, Object... p1) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public boolean isEnabled(Level testLevel, Marker marker, String msg, Throwable t) {
    return (this.level.intLevel() >= testLevel.intLevel());
  }
  
  public void logMessage(String fqcn, Level mgsLevel, Marker marker, Message msg, Throwable throwable) {
    Throwable t;
    StringBuilder sb = new StringBuilder();
    if (this.showDateTime) {
      String dateText;
      Date now = new Date();
      synchronized (this.dateFormatter) {
        dateText = this.dateFormatter.format(now);
      } 
      sb.append(dateText);
      sb.append(' ');
    } 
    sb.append(mgsLevel.toString());
    sb.append(' ');
    if (Strings.isNotEmpty(this.logName)) {
      sb.append(this.logName);
      sb.append(' ');
    } 
    sb.append(msg.getFormattedMessage());
    if (this.showContextMap) {
      Map<String, String> mdc = ThreadContext.getImmutableContext();
      if (mdc.size() > 0) {
        sb.append(' ');
        sb.append(mdc.toString());
        sb.append(' ');
      } 
    } 
    Object[] params = msg.getParameters();
    if (throwable == null && params != null && params.length > 0 && params[params.length - 1] instanceof Throwable) {
      t = (Throwable)params[params.length - 1];
    } else {
      t = throwable;
    } 
    this.stream.println(sb.toString());
    if (t != null) {
      this.stream.print(' ');
      t.printStackTrace(this.stream);
    } 
  }
  
  public void setLevel(Level level) {
    if (level != null)
      this.level = level; 
  }
  
  public void setStream(PrintStream stream) {
    this.stream = stream;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\simple\SimpleLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */