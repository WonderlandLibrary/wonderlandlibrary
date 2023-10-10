package joptsimple;

import java.util.Collections;

class AlternativeLongOptionSpec extends ArgumentAcceptingOptionSpec {
   AlternativeLongOptionSpec() {
      super(Collections.singletonList("W"), true, "Alternative form of long options");
      this.describedAs("opt=value");
   }

   protected void detectOptionArgument(OptionParser var1, ArgumentList var2, OptionSet var3) {
      if (!var2.hasMore()) {
         throw new OptionMissingRequiredArgumentException(this.options());
      } else {
         var2.treatNextAsLongOption();
      }
   }
}
