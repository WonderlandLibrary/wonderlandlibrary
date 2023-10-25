package org.apache.commons.lang3.concurrent;

public class ConcurrentException extends Exception {
  private static final long serialVersionUID = 6622707671812226130L;
  
  protected ConcurrentException() {}
  
  public ConcurrentException(Throwable cause) {
    super(ConcurrentUtils.checkedException(cause));
  }
  
  public ConcurrentException(String msg, Throwable cause) {
    super(msg, ConcurrentUtils.checkedException(cause));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\concurrent\ConcurrentException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */