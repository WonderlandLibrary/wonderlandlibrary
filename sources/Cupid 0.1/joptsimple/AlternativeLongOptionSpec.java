package joptsimple;

import java.util.Collections;

class AlternativeLongOptionSpec extends ArgumentAcceptingOptionSpec<String> {
  AlternativeLongOptionSpec() {
    super(Collections.singletonList("W"), true, "Alternative form of long options");
    describedAs("opt=value");
  }
  
  protected void detectOptionArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions) {
    if (!arguments.hasMore())
      throw new OptionMissingRequiredArgumentException(options()); 
    arguments.treatNextAsLongOption();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\AlternativeLongOptionSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */