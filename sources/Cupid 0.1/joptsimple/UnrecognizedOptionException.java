package joptsimple;

import java.util.Collections;

class UnrecognizedOptionException extends OptionException {
  private static final long serialVersionUID = -1L;
  
  UnrecognizedOptionException(String option) {
    super(Collections.singletonList(option));
  }
  
  public String getMessage() {
    return singleOptionMessage() + " is not a recognized option";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\UnrecognizedOptionException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */