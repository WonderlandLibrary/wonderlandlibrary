package joptsimple;

import java.util.Collection;

class MissingRequiredOptionException extends OptionException {
  private static final long serialVersionUID = -1L;
  
  protected MissingRequiredOptionException(Collection<String> options) {
    super(options);
  }
  
  public String getMessage() {
    return "Missing required option(s) " + multipleOptionMessage();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\MissingRequiredOptionException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */