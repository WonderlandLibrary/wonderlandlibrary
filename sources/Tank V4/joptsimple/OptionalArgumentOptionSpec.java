package joptsimple;

import java.util.Collection;

class OptionalArgumentOptionSpec extends ArgumentAcceptingOptionSpec {
   OptionalArgumentOptionSpec(String var1) {
      super(var1, false);
   }

   OptionalArgumentOptionSpec(Collection var1, String var2) {
      super(var1, false, var2);
   }

   protected void detectOptionArgument(OptionParser var1, ArgumentList var2, OptionSet var3) {
      if (var2.hasMore()) {
         String var4 = var2.peek();
         if (!var1.looksLikeAnOption(var4)) {
            this.handleOptionArgument(var1, var3, var2);
         } else if (this.isArgumentOfNumberType() && this.canConvertArgument(var4)) {
            this.addArguments(var3, var2.next());
         } else {
            var3.add(this);
         }
      } else {
         var3.add(this);
      }

   }

   private void handleOptionArgument(OptionParser var1, OptionSet var2, ArgumentList var3) {
      if (var1.posixlyCorrect()) {
         var2.add(this);
         var1.noMoreOptions();
      } else {
         this.addArguments(var2, var3.next());
      }

   }
}
