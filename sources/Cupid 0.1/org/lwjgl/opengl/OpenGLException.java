package org.lwjgl.opengl;

public class OpenGLException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  public OpenGLException(int gl_error_code) {
    this(createErrorMessage(gl_error_code));
  }
  
  private static String createErrorMessage(int gl_error_code) {
    String error_string = Util.translateGLErrorString(gl_error_code);
    return error_string + " (" + gl_error_code + ")";
  }
  
  public OpenGLException() {}
  
  public OpenGLException(String message) {
    super(message);
  }
  
  public OpenGLException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public OpenGLException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\OpenGLException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */