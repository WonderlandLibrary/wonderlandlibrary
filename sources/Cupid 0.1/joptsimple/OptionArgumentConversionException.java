package joptsimple;

import java.util.Collection;

class OptionArgumentConversionException extends OptionException {
  private static final long serialVersionUID = -1L;
  
  private final String argument;
  
  OptionArgumentConversionException(Collection<String> options, String argument, Throwable cause) {
    super(options, cause);
    this.argument = argument;
  }
  
  public String getMessage() {
    return "Cannot parse argument '" + this.argument + "' of option " + multipleOptionMessage();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\OptionArgumentConversionException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */