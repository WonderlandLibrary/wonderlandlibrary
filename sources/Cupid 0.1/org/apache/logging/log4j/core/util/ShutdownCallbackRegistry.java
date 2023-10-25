package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public interface ShutdownCallbackRegistry {
  public static final String SHUTDOWN_CALLBACK_REGISTRY = "log4j.shutdownCallbackRegistry";
  
  public static final String SHUTDOWN_HOOK_ENABLED = "log4j.shutdownHookEnabled";
  
  public static final Marker SHUTDOWN_HOOK_MARKER = MarkerManager.getMarker("SHUTDOWN HOOK");
  
  Cancellable addShutdownCallback(Runnable paramRunnable);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\ShutdownCallbackRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */