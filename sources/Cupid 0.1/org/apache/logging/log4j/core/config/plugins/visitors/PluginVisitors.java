package org.apache.logging.log4j.core.config.plugins.visitors;

import java.lang.annotation.Annotation;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginVisitorStrategy;
import org.apache.logging.log4j.status.StatusLogger;

public final class PluginVisitors {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  public static PluginVisitor<? extends Annotation> findVisitor(Class<? extends Annotation> annotation) {
    PluginVisitorStrategy strategy = annotation.<PluginVisitorStrategy>getAnnotation(PluginVisitorStrategy.class);
    if (strategy == null)
      return null; 
    try {
      return strategy.value().newInstance();
    } catch (Exception e) {
      LOGGER.error("Error loading PluginVisitor [{}] for annotation [{}].", strategy.value(), annotation, e);
      return null;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginVisitors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */