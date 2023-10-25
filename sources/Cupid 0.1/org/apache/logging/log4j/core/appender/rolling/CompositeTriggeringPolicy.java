package org.apache.logging.log4j.core.appender.rolling;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LifeCycle2;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "Policies", category = "Core", printObject = true)
public final class CompositeTriggeringPolicy extends AbstractTriggeringPolicy {
  private final TriggeringPolicy[] triggeringPolicies;
  
  private CompositeTriggeringPolicy(TriggeringPolicy... triggeringPolicies) {
    this.triggeringPolicies = triggeringPolicies;
  }
  
  public TriggeringPolicy[] getTriggeringPolicies() {
    return this.triggeringPolicies;
  }
  
  public void initialize(RollingFileManager manager) {
    for (TriggeringPolicy triggeringPolicy : this.triggeringPolicies) {
      LOGGER.debug("Initializing triggering policy {}", triggeringPolicy.toString());
      triggeringPolicy.initialize(manager);
    } 
  }
  
  public boolean isTriggeringEvent(LogEvent event) {
    for (TriggeringPolicy triggeringPolicy : this.triggeringPolicies) {
      if (triggeringPolicy.isTriggeringEvent(event))
        return true; 
    } 
    return false;
  }
  
  @PluginFactory
  public static CompositeTriggeringPolicy createPolicy(@PluginElement("Policies") TriggeringPolicy... triggeringPolicy) {
    return new CompositeTriggeringPolicy(triggeringPolicy);
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    int i;
    setStopping();
    boolean stopped = true;
    for (TriggeringPolicy triggeringPolicy : this.triggeringPolicies) {
      if (triggeringPolicy instanceof LifeCycle2) {
        stopped &= ((LifeCycle2)triggeringPolicy).stop(timeout, timeUnit);
      } else if (triggeringPolicy instanceof LifeCycle) {
        ((LifeCycle)triggeringPolicy).stop();
        i = stopped & true;
      } 
    } 
    setStopped();
    return i;
  }
  
  public String toString() {
    return "CompositeTriggeringPolicy(policies=" + Arrays.toString((Object[])this.triggeringPolicies) + ")";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\CompositeTriggeringPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */