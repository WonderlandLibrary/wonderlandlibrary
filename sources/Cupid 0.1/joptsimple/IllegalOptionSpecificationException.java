package joptsimple;

import java.util.Collections;

class IllegalOptionSpecificationException extends OptionException {
  private static final long serialVersionUID = -1L;
  
  IllegalOptionSpecificationException(String option) {
    super(Collections.singletonList(option));
  }
  
  public String getMessage() {
    return singleOptionMessage() + " is not a legal option character";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\IllegalOptionSpecificationException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */