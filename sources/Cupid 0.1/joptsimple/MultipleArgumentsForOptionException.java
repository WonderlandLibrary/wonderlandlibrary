package joptsimple;

import java.util.Collection;

class MultipleArgumentsForOptionException extends OptionException {
  private static final long serialVersionUID = -1L;
  
  MultipleArgumentsForOptionException(Collection<String> options) {
    super(options);
  }
  
  public String getMessage() {
    return "Found multiple arguments for option " + multipleOptionMessage() + ", but you asked for only one";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\MultipleArgumentsForOptionException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */