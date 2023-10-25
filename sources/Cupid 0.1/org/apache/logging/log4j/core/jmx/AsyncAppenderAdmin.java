package org.apache.logging.log4j.core.jmx;

import java.util.Objects;
import javax.management.ObjectName;
import org.apache.logging.log4j.core.appender.AsyncAppender;

public class AsyncAppenderAdmin implements AsyncAppenderAdminMBean {
  private final String contextName;
  
  private final AsyncAppender asyncAppender;
  
  private final ObjectName objectName;
  
  public AsyncAppenderAdmin(String contextName, AsyncAppender appender) {
    this.contextName = Objects.<String>requireNonNull(contextName, "contextName");
    this.asyncAppender = Objects.<AsyncAppender>requireNonNull(appender, "async appender");
    try {
      String ctxName = Server.escape(this.contextName);
      String configName = Server.escape(appender.getName());
      String name = String.format("org.apache.logging.log4j2:type=%s,component=AsyncAppenders,name=%s", new Object[] { ctxName, configName });
      this.objectName = new ObjectName(name);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    } 
  }
  
  public ObjectName getObjectName() {
    return this.objectName;
  }
  
  public String getName() {
    return this.asyncAppender.getName();
  }
  
  public String getLayout() {
    return String.valueOf(this.asyncAppender.getLayout());
  }
  
  public boolean isIgnoreExceptions() {
    return this.asyncAppender.ignoreExceptions();
  }
  
  public String getErrorHandler() {
    return String.valueOf(this.asyncAppender.getHandler());
  }
  
  public String getFilter() {
    return String.valueOf(this.asyncAppender.getFilter());
  }
  
  public String[] getAppenderRefs() {
    return this.asyncAppender.getAppenderRefStrings();
  }
  
  public boolean isIncludeLocation() {
    return this.asyncAppender.isIncludeLocation();
  }
  
  public boolean isBlocking() {
    return this.asyncAppender.isBlocking();
  }
  
  public String getErrorRef() {
    return this.asyncAppender.getErrorRef();
  }
  
  public int getQueueCapacity() {
    return this.asyncAppender.getQueueCapacity();
  }
  
  public int getQueueRemainingCapacity() {
    return this.asyncAppender.getQueueRemainingCapacity();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jmx\AsyncAppenderAdmin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */