package joptsimple;

import java.util.Collection;

class RequiredArgumentOptionSpec extends ArgumentAcceptingOptionSpec {
   RequiredArgumentOptionSpec(String var1) {
      super(var1, true);
   }

   RequiredArgumentOptionSpec(Collection var1, String var2) {
      super(var1, true, var2);
   }

   protected void detectOptionArgument(OptionParser var1, ArgumentList var2, OptionSet var3) {
      if (!var2.hasMore()) {
         throw new OptionMissingRequiredArgumentException(this.options());
      } else {
         this.addArguments(var3, var2.next());
      }
   }
}
