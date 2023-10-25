package org.apache.logging.log4j.core.jmx;

import java.util.List;
import java.util.Objects;
import javax.management.ObjectName;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class LoggerConfigAdmin implements LoggerConfigAdminMBean {
  private final LoggerContext loggerContext;
  
  private final LoggerConfig loggerConfig;
  
  private final ObjectName objectName;
  
  public LoggerConfigAdmin(LoggerContext loggerContext, LoggerConfig loggerConfig) {
    this.loggerContext = Objects.<LoggerContext>requireNonNull(loggerContext, "loggerContext");
    this.loggerConfig = Objects.<LoggerConfig>requireNonNull(loggerConfig, "loggerConfig");
    try {
      String ctxName = Server.escape(loggerContext.getName());
      String configName = Server.escape(loggerConfig.getName());
      String name = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s", new Object[] { ctxName, configName });
      this.objectName = new ObjectName(name);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    } 
  }
  
  public ObjectName getObjectName() {
    return this.objectName;
  }
  
  public String getName() {
    return this.loggerConfig.getName();
  }
  
  public String getLevel() {
    return this.loggerConfig.getLevel().name();
  }
  
  public void setLevel(String level) {
    this.loggerConfig.setLevel(Level.getLevel(level));
    this.loggerContext.updateLoggers();
  }
  
  public boolean isAdditive() {
    return this.loggerConfig.isAdditive();
  }
  
  public void setAdditive(boolean additive) {
    this.loggerConfig.setAdditive(additive);
    this.loggerContext.updateLoggers();
  }
  
  public boolean isIncludeLocation() {
    return this.loggerConfig.isIncludeLocation();
  }
  
  public String getFilter() {
    return String.valueOf(this.loggerConfig.getFilter());
  }
  
  public String[] getAppenderRefs() {
    List<AppenderRef> refs = this.loggerConfig.getAppenderRefs();
    String[] result = new String[refs.size()];
    for (int i = 0; i < result.length; i++)
      result[i] = ((AppenderRef)refs.get(i)).getRef(); 
    return result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jmx\LoggerConfigAdmin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */