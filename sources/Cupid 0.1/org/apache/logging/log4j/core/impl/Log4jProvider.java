package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.spi.Provider;

public class Log4jProvider extends Provider {
  public Log4jProvider() {
    super(Integer.valueOf(10), "2.6.0", Log4jContextFactory.class);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\impl\Log4jProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */