package io.netty.channel.epoll;

public final class Epoll {
  private static final Throwable UNAVAILABILITY_CAUSE;
  
  static {
    Throwable cause = null;
    int epollFd = -1;
    int eventFd = -1;
    try {
      epollFd = Native.epollCreate();
      eventFd = Native.eventFd();
    } catch (Throwable t) {
      cause = t;
    } finally {
      if (epollFd != -1)
        try {
          Native.close(epollFd);
        } catch (Exception ignore) {} 
      if (eventFd != -1)
        try {
          Native.close(eventFd);
        } catch (Exception ignore) {} 
    } 
    if (cause != null) {
      UNAVAILABILITY_CAUSE = cause;
    } else {
      UNAVAILABILITY_CAUSE = null;
    } 
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\epoll\Epoll.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */