package joptsimple;

import java.util.Collection;

class OptionMissingRequiredArgumentException extends OptionException {
  private static final long serialVersionUID = -1L;
  
  OptionMissingRequiredArgumentException(Collection<String> options) {
    super(options);
  }
  
  public String getMessage() {
    return "Option " + multipleOptionMessage() + " requires an argument";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\OptionMissingRequiredArgumentException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */