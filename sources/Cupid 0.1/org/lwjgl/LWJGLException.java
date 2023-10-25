package org.lwjgl;

public class LWJGLException extends Exception {
  private static final long serialVersionUID = 1L;
  
  public LWJGLException() {}
  
  public LWJGLException(String msg) {
    super(msg);
  }
  
  public LWJGLException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public LWJGLException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\LWJGLException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */