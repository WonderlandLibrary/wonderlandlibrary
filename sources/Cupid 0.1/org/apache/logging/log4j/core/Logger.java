package org.apache.logging.log4j.core;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LocationAwareReliabilityStrategy;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.ReliabilityStrategy;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.util.Supplier;

public class Logger extends AbstractLogger implements Supplier<LoggerConfig> {
  private static final long serialVersionUID = 1L;
  
  protected volatile PrivateConfig privateConfig;
  
  private final LoggerContext context;
  
  protected Logger(LoggerContext context, String name, MessageFactory messageFactory) {
    super(name, messageFactory);
    this.context = context;
    this.privateConfig = new PrivateConfig(context.getConfiguration(), this);
  }
  
  protected Object writeReplace() throws ObjectStreamException {
    return new LoggerProxy(getName(), getMessageFactory());
  }
  
  public Logger getParent() {
    LoggerConfig lc = this.privateConfig.loggerConfig.getName().equals(getName()) ? this.privateConfig.loggerConfig.getParent() : this.privateConfig.loggerConfig;
    if (lc == null)
      return null; 
    String lcName = lc.getName();
    MessageFactory messageFactory = getMessageFactory();
    if (this.context.hasLogger(lcName, messageFactory))
      return this.context.getLogger(lcName, messageFactory); 
    return new Logger(this.context, lcName, messageFactory);
  }
  
  public LoggerContext getContext() {
    return this.context;
  }
  
  public synchronized void setLevel(Level level) {
    Level actualLevel;
    if (level == getLevel())
      return; 
    if (level != null) {
      actualLevel = level;
    } else {
      Logger parent = getParent();
      actualLevel = (parent != null) ? parent.getLevel() : this.privateConfig.loggerConfigLevel;
    } 
    this.privateConfig = new PrivateConfig(this.privateConfig, actualLevel);
  }
  
  public LoggerConfig get() {
    return this.privateConfig.loggerConfig;
  }
  
  protected boolean requiresLocation() {
    return this.privateConfig.requiresLocation;
  }
  
  public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
    Message msg = (message == null) ? (Message)new SimpleMessage("") : message;
    ReliabilityStrategy strategy = this.privateConfig.loggerConfig.getReliabilityStrategy();
    strategy.log(this, getName(), fqcn, marker, level, msg, t);
  }
  
  protected void log(Level level, Marker marker, String fqcn, StackTraceElement location, Message message, Throwable throwable) {
    ReliabilityStrategy strategy = this.privateConfig.loggerConfig.getReliabilityStrategy();
    if (strategy instanceof LocationAwareReliabilityStrategy) {
      ((LocationAwareReliabilityStrategy)strategy).log(this, getName(), fqcn, location, marker, level, message, throwable);
    } else {
      strategy.log(this, getName(), fqcn, marker, level, message, throwable);
    } 
  }
  
  public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
    return this.privateConfig.filter(level, marker, message, t);
  }
  
  public boolean isEnabled(Level level, Marker marker, String message) {
    return this.privateConfig.filter(level, marker, message);
  }
  
  public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
    return this.privateConfig.filter(level, marker, message, params);
  }
  
  public boolean isEnabled(Level level, Marker marker, String message, Object p0) {
    return this.privateConfig.filter(level, marker, message, p0);
  }
  
  public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1) {
    return this.privateConfig.filter(level, marker, message, p0, p1);
  }
  
  public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
    return this.privateConfig.filter(level, marker, message, p0, p1, p2);
  }
  
  public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3);
  }
  
  public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4);
  }
  
  public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4, p5);
  }
  
  public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4, p5, p6);
  }
  
  public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }
  
  public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }
  
  public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }
  
  public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable t) {
    return this.privateConfig.filter(level, marker, message, t);
  }
  
  public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
    return this.privateConfig.filter(level, marker, message, t);
  }
  
  public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
    return this.privateConfig.filter(level, marker, message, t);
  }
  
  public void addAppender(Appender appender) {
    this.privateConfig.config.addLoggerAppender(this, appender);
  }
  
  public void removeAppender(Appender appender) {
    this.privateConfig.loggerConfig.removeAppender(appender.getName());
  }
  
  public Map<String, Appender> getAppenders() {
    return this.privateConfig.loggerConfig.getAppenders();
  }
  
  public Iterator<Filter> getFilters() {
    Filter filter = this.privateConfig.loggerConfig.getFilter();
    if (filter == null)
      return (new ArrayList<>()).iterator(); 
    if (filter instanceof CompositeFilter)
      return ((CompositeFilter)filter).iterator(); 
    List<Filter> filters = new ArrayList<>();
    filters.add(filter);
    return filters.iterator();
  }
  
  public Level getLevel() {
    return this.privateConfig.loggerConfigLevel;
  }
  
  public int filterCount() {
    Filter filter = this.privateConfig.loggerConfig.getFilter();
    if (filter == null)
      return 0; 
    if (filter instanceof CompositeFilter)
      return ((CompositeFilter)filter).size(); 
    return 1;
  }
  
  public void addFilter(Filter filter) {
    this.privateConfig.config.addLoggerFilter(this, filter);
  }
  
  public boolean isAdditive() {
    return this.privateConfig.loggerConfig.isAdditive();
  }
  
  public void setAdditive(boolean additive) {
    this.privateConfig.config.setLoggerAdditive(this, additive);
  }
  
  protected void updateConfiguration(Configuration newConfig) {
    this.privateConfig = new PrivateConfig(newConfig, this);
  }
  
  protected class PrivateConfig {
    public final LoggerConfig loggerConfig;
    
    public final Configuration config;
    
    private final Level loggerConfigLevel;
    
    private final int intLevel;
    
    private final Logger logger;
    
    private final boolean requiresLocation;
    
    public PrivateConfig(Configuration config, Logger logger) {
      this.config = config;
      this.loggerConfig = config.getLoggerConfig(Logger.this.getName());
      this.loggerConfigLevel = this.loggerConfig.getLevel();
      this.intLevel = this.loggerConfigLevel.intLevel();
      this.logger = logger;
      this.requiresLocation = this.loggerConfig.requiresLocation();
    }
    
    public PrivateConfig(PrivateConfig pc, Level level) {
      this.config = pc.config;
      this.loggerConfig = pc.loggerConfig;
      this.loggerConfigLevel = level;
      this.intLevel = this.loggerConfigLevel.intLevel();
      this.logger = pc.logger;
      this.requiresLocation = this.loggerConfig.requiresLocation();
    }
    
    public PrivateConfig(PrivateConfig pc, LoggerConfig lc) {
      this.config = pc.config;
      this.loggerConfig = lc;
      this.loggerConfigLevel = lc.getLevel();
      this.intLevel = this.loggerConfigLevel.intLevel();
      this.logger = pc.logger;
      this.requiresLocation = this.loggerConfig.requiresLocation();
    }
    
    public void logEvent(LogEvent event) {
      this.loggerConfig.log(event);
    }
    
    boolean filter(Level level, Marker marker, String msg) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, new Object[0]);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    boolean filter(Level level, Marker marker, String msg, Throwable t) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    boolean filter(Level level, Marker marker, String msg, Object... p1) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, p1);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    boolean filter(Level level, Marker marker, String msg, Object p0) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, p0);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    boolean filter(Level level, Marker marker, String msg, Object p0, Object p1) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4, p5);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    boolean filter(Level level, Marker marker, CharSequence msg, Throwable t) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    boolean filter(Level level, Marker marker, Object msg, Throwable t) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    boolean filter(Level level, Marker marker, Message msg, Throwable t) {
      Filter filter = this.config.getFilter();
      if (filter != null) {
        Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
        if (r != Filter.Result.NEUTRAL)
          return (r == Filter.Result.ACCEPT); 
      } 
      return (level != null && this.intLevel >= level.intLevel());
    }
    
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("PrivateConfig [loggerConfig=");
      builder.append(this.loggerConfig);
      builder.append(", config=");
      builder.append(this.config);
      builder.append(", loggerConfigLevel=");
      builder.append(this.loggerConfigLevel);
      builder.append(", intLevel=");
      builder.append(this.intLevel);
      builder.append(", logger=");
      builder.append(this.logger);
      builder.append("]");
      return builder.toString();
    }
  }
  
  protected static class LoggerProxy implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String name;
    
    private final MessageFactory messageFactory;
    
    public LoggerProxy(String name, MessageFactory messageFactory) {
      this.name = name;
      this.messageFactory = messageFactory;
    }
    
    protected Object readResolve() throws ObjectStreamException {
      return new Logger(LoggerContext.getContext(), this.name, this.messageFactory);
    }
  }
  
  public String toString() {
    String nameLevel = "" + getName() + ':' + getLevel();
    if (this.context == null)
      return nameLevel; 
    String contextName = this.context.getName();
    return (contextName == null) ? nameLevel : (nameLevel + " in " + contextName);
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    Logger that = (Logger)o;
    return getName().equals(that.getName());
  }
  
  public int hashCode() {
    return getName().hashCode();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\Logger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */