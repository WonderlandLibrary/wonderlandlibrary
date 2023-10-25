package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class ReliabilityStrategyFactory {
  public static ReliabilityStrategy getReliabilityStrategy(LoggerConfig loggerConfig) {
    String strategy = PropertiesUtil.getProperties().getStringProperty("log4j.ReliabilityStrategy", "AwaitCompletion");
    if ("AwaitCompletion".equals(strategy))
      return new AwaitCompletionReliabilityStrategy(loggerConfig); 
    if ("AwaitUnconditionally".equals(strategy))
      return new AwaitUnconditionallyReliabilityStrategy(loggerConfig); 
    if ("Locking".equals(strategy))
      return new LockingReliabilityStrategy(loggerConfig); 
    try {
      Class<? extends ReliabilityStrategy> cls = Loader.loadClass(strategy).asSubclass(ReliabilityStrategy.class);
      return cls.getConstructor(new Class[] { LoggerConfig.class }).newInstance(new Object[] { loggerConfig });
    } catch (Exception dynamicFailed) {
      StatusLogger.getLogger().warn("Could not create ReliabilityStrategy for '{}', using default AwaitCompletionReliabilityStrategy: {}", strategy, dynamicFailed);
      return new AwaitCompletionReliabilityStrategy(loggerConfig);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\ReliabilityStrategyFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */