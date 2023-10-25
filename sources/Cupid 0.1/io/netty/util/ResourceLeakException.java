package io.netty.util;

import java.util.Arrays;

@Deprecated
public class ResourceLeakException extends RuntimeException {
  private static final long serialVersionUID = 7186453858343358280L;
  
  private final StackTraceElement[] cachedStackTrace;
  
  public ResourceLeakException() {
    this.cachedStackTrace = getStackTrace();
  }
  
  public ResourceLeakException(String message) {
    super(message);
    this.cachedStackTrace = getStackTrace();
  }
  
  public ResourceLeakException(String message, Throwable cause) {
    super(message, cause);
    this.cachedStackTrace = getStackTrace();
  }
  
  public ResourceLeakException(Throwable cause) {
    super(cause);
    this.cachedStackTrace = getStackTrace();
  }
  
  public int hashCode() {
    StackTraceElement[] trace = this.cachedStackTrace;
    int hashCode = 0;
    for (StackTraceElement e : trace)
      hashCode = hashCode * 31 + e.hashCode(); 
    return hashCode;
  }
  
  public boolean equals(Object o) {
    if (!(o instanceof ResourceLeakException))
      return false; 
    if (o == this)
      return true; 
    return Arrays.equals((Object[])this.cachedStackTrace, (Object[])((ResourceLeakException)o).cachedStackTrace);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\ResourceLeakException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */