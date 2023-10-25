package org.apache.logging.log4j.core.selector;

import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
import org.apache.logging.log4j.core.async.BasicAsyncLoggerContextSelector;

public class CoreContextSelectors {
  public static final Class<?>[] CLASSES = new Class[] { ClassLoaderContextSelector.class, BasicContextSelector.class, AsyncLoggerContextSelector.class, BasicAsyncLoggerContextSelector.class };
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\selector\CoreContextSelectors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */