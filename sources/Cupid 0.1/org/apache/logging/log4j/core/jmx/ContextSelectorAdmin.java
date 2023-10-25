package org.apache.logging.log4j.core.jmx;

import java.util.Objects;
import javax.management.ObjectName;
import org.apache.logging.log4j.core.selector.ContextSelector;

public class ContextSelectorAdmin implements ContextSelectorAdminMBean {
  private final ObjectName objectName;
  
  private final ContextSelector selector;
  
  public ContextSelectorAdmin(String contextName, ContextSelector selector) {
    this.selector = Objects.<ContextSelector>requireNonNull(selector, "ContextSelector");
    try {
      String mbeanName = String.format("org.apache.logging.log4j2:type=%s,component=ContextSelector", new Object[] { Server.escape(contextName) });
      this.objectName = new ObjectName(mbeanName);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    } 
  }
  
  public ObjectName getObjectName() {
    return this.objectName;
  }
  
  public String getImplementationClassName() {
    return this.selector.getClass().getName();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jmx\ContextSelectorAdmin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */