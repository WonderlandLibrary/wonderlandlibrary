package org.apache.logging.log4j.core.jmx;

import java.util.Objects;
import javax.management.ObjectName;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.filter.AbstractFilterable;

public class AppenderAdmin implements AppenderAdminMBean {
  private final String contextName;
  
  private final Appender appender;
  
  private final ObjectName objectName;
  
  public AppenderAdmin(String contextName, Appender appender) {
    this.contextName = Objects.<String>requireNonNull(contextName, "contextName");
    this.appender = Objects.<Appender>requireNonNull(appender, "appender");
    try {
      String ctxName = Server.escape(this.contextName);
      String configName = Server.escape(appender.getName());
      String name = String.format("org.apache.logging.log4j2:type=%s,component=Appenders,name=%s", new Object[] { ctxName, configName });
      this.objectName = new ObjectName(name);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    } 
  }
  
  public ObjectName getObjectName() {
    return this.objectName;
  }
  
  public String getName() {
    return this.appender.getName();
  }
  
  public String getLayout() {
    return String.valueOf(this.appender.getLayout());
  }
  
  public boolean isIgnoreExceptions() {
    return this.appender.ignoreExceptions();
  }
  
  public String getErrorHandler() {
    return String.valueOf(this.appender.getHandler());
  }
  
  public String getFilter() {
    if (this.appender instanceof AbstractFilterable)
      return String.valueOf(((AbstractFilterable)this.appender).getFilter()); 
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jmx\AppenderAdmin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */