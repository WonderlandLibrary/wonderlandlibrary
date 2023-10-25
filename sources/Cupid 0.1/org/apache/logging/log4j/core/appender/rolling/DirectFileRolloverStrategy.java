package org.apache.logging.log4j.core.appender.rolling;

public interface DirectFileRolloverStrategy {
  String getCurrentFileName(RollingFileManager paramRollingFileManager);
  
  void clearCurrentFileName();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\DirectFileRolloverStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */