package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public class AsyncQueueFullPolicyFactory {
  static final String PROPERTY_NAME_ASYNC_EVENT_ROUTER = "log4j2.AsyncQueueFullPolicy";
  
  static final String PROPERTY_VALUE_DEFAULT_ASYNC_EVENT_ROUTER = "Default";
  
  static final String PROPERTY_VALUE_DISCARDING_ASYNC_EVENT_ROUTER = "Discard";
  
  static final String PROPERTY_NAME_DISCARDING_THRESHOLD_LEVEL = "log4j2.DiscardThreshold";
  
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  public static AsyncQueueFullPolicy create() {
    String router = PropertiesUtil.getProperties().getStringProperty("log4j2.AsyncQueueFullPolicy");
    if (router == null || isRouterSelected(router, (Class)DefaultAsyncQueueFullPolicy.class, "Default"))
      return new DefaultAsyncQueueFullPolicy(); 
    if (isRouterSelected(router, (Class)DiscardingAsyncQueueFullPolicy.class, "Discard"))
      return createDiscardingAsyncQueueFullPolicy(); 
    return createCustomRouter(router);
  }
  
  private static boolean isRouterSelected(String propertyValue, Class<? extends AsyncQueueFullPolicy> policy, String shortPropertyValue) {
    return (propertyValue != null && (shortPropertyValue.equalsIgnoreCase(propertyValue) || policy
      .getName().equals(propertyValue) || policy
      .getSimpleName().equals(propertyValue)));
  }
  
  private static AsyncQueueFullPolicy createCustomRouter(String router) {
    try {
      Class<? extends AsyncQueueFullPolicy> cls = Loader.loadClass(router).asSubclass(AsyncQueueFullPolicy.class);
      LOGGER.debug("Creating custom AsyncQueueFullPolicy '{}'", router);
      return cls.newInstance();
    } catch (Exception ex) {
      LOGGER.debug("Using DefaultAsyncQueueFullPolicy. Could not create custom AsyncQueueFullPolicy '{}': {}", router, ex
          .toString());
      return new DefaultAsyncQueueFullPolicy();
    } 
  }
  
  private static AsyncQueueFullPolicy createDiscardingAsyncQueueFullPolicy() {
    PropertiesUtil util = PropertiesUtil.getProperties();
    String level = util.getStringProperty("log4j2.DiscardThreshold", Level.INFO.name());
    Level thresholdLevel = Level.toLevel(level, Level.INFO);
    LOGGER.debug("Creating custom DiscardingAsyncQueueFullPolicy(discardThreshold:{})", thresholdLevel);
    return new DiscardingAsyncQueueFullPolicy(thresholdLevel);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\AsyncQueueFullPolicyFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */