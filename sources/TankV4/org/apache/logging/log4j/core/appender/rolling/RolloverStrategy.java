package org.apache.logging.log4j.core.appender.rolling;

public interface RolloverStrategy {
   RolloverDescription rollover(RollingFileManager var1) throws SecurityException;
}
