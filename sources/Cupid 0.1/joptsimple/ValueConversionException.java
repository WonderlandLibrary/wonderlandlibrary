package joptsimple;

public class ValueConversionException extends RuntimeException {
  private static final long serialVersionUID = -1L;
  
  public ValueConversionException(String message) {
    this(message, null);
  }
  
  public ValueConversionException(String message, Throwable cause) {
    super(message, cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\ValueConversionException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */