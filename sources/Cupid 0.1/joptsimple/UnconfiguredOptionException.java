package joptsimple;

import java.util.Collection;
import java.util.Collections;

class UnconfiguredOptionException extends OptionException {
  private static final long serialVersionUID = -1L;
  
  UnconfiguredOptionException(String option) {
    this(Collections.singletonList(option));
  }
  
  UnconfiguredOptionException(Collection<String> options) {
    super(options);
  }
  
  public String getMessage() {
    return "Option " + multipleOptionMessage() + " has not been configured on this parser";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\UnconfiguredOptionException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */