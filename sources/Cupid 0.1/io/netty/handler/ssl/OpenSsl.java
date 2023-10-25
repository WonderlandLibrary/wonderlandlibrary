package io.netty.handler.ssl;

import io.netty.util.internal.NativeLibraryLoader;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.apache.tomcat.jni.Library;
import org.apache.tomcat.jni.SSL;

public final class OpenSsl {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(OpenSsl.class);
  
  private static final Throwable UNAVAILABILITY_CAUSE;
  
  static final String IGNORABLE_ERROR_PREFIX = "error:00000000:";
  
  static {
    Throwable cause = null;
    try {
      NativeLibraryLoader.load("netty-tcnative", SSL.class.getClassLoader());
      Library.initialize("provided");
      SSL.initialize(null);
    } catch (Throwable t) {
      cause = t;
      logger.debug("Failed to load netty-tcnative; " + OpenSslEngine.class.getSimpleName() + " will be unavailable.", t);
    } 
    UNAVAILABILITY_CAUSE = cause;
  }
  
  public static boolean isAvailable() {
    return (UNAVAILABILITY_CAUSE == null);
  }
  
  public static void ensureAvailability() {
    if (UNAVAILABILITY_CAUSE != null)
      throw (Error)(new UnsatisfiedLinkError("failed to load the required native library")).initCause(UNAVAILABILITY_CAUSE); 
  }
  
  public static Throwable unavailabilityCause() {
    return UNAVAILABILITY_CAUSE;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\ssl\OpenSsl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */