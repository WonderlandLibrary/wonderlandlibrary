package org.lwjgl.openal;

public class OpenALException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  public OpenALException() {}
  
  public OpenALException(int error_code) {
    super("OpenAL error: " + AL10.alGetString(error_code) + " (" + error_code + ")");
  }
  
  public OpenALException(String message) {
    super(message);
  }
  
  public OpenALException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public OpenALException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\openal\OpenALException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */