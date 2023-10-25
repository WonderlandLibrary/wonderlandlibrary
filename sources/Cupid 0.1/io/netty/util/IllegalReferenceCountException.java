package io.netty.util;

public class IllegalReferenceCountException extends IllegalStateException {
  private static final long serialVersionUID = -2507492394288153468L;
  
  public IllegalReferenceCountException() {}
  
  public IllegalReferenceCountException(int refCnt) {
    this("refCnt: " + refCnt);
  }
  
  public IllegalReferenceCountException(int refCnt, int increment) {
    this("refCnt: " + refCnt + ", " + ((increment > 0) ? ("increment: " + increment) : ("decrement: " + -increment)));
  }
  
  public IllegalReferenceCountException(String message) {
    super(message);
  }
  
  public IllegalReferenceCountException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public IllegalReferenceCountException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\IllegalReferenceCountException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */