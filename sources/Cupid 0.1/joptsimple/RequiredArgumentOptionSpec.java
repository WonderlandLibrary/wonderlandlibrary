package joptsimple;

import java.util.Collection;

class RequiredArgumentOptionSpec<V> extends ArgumentAcceptingOptionSpec<V> {
  RequiredArgumentOptionSpec(String option) {
    super(option, true);
  }
  
  RequiredArgumentOptionSpec(Collection<String> options, String description) {
    super(options, true, description);
  }
  
  protected void detectOptionArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions) {
    if (!arguments.hasMore())
      throw new OptionMissingRequiredArgumentException(options()); 
    addArguments(detectedOptions, arguments.next());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\RequiredArgumentOptionSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */