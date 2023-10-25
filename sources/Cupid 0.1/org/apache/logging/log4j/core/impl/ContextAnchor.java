package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.core.LoggerContext;

public final class ContextAnchor {
  public static final ThreadLocal<LoggerContext> THREAD_CONTEXT = new ThreadLocal<>();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\impl\ContextAnchor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */