package org.apache.logging.log4j.core.config;

public class ConfigurationException extends RuntimeException {
  private static final long serialVersionUID = -2413951820300775294L;
  
  public ConfigurationException(String message) {
    super(message);
  }
  
  public ConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public ConfigurationException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\ConfigurationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */