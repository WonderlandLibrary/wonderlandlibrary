package joptsimple;

abstract class OptionParserState {
   static OptionParserState noMoreOptions() {
      return new OptionParserState() {
         protected void handleArgument(OptionParser var1, ArgumentList var2, OptionSet var3) {
            var1.handleNonOptionArgument(var2.next(), var2, var3);
         }
      };
   }

   static OptionParserState moreOptions(boolean var0) {
      return new OptionParserState(var0) {
         final boolean val$posixlyCorrect;

         {
            this.val$posixlyCorrect = var1;
         }

         protected void handleArgument(OptionParser var1, ArgumentList var2, OptionSet var3) {
            String var4 = var2.next();

            try {
               if (ParserRules.isOptionTerminator(var4)) {
                  var1.noMoreOptions();
                  return;
               }

               if (ParserRules.isLongOptionToken(var4)) {
                  var1.handleLongOptionToken(var4, var2, var3);
                  return;
               }

               if (ParserRules.isShortOptionToken(var4)) {
                  var1.handleShortOptionToken(var4, var2, var3);
                  return;
               }
            } catch (UnrecognizedOptionException var6) {
               if (!var1.doesAllowsUnrecognizedOptions()) {
                  throw var6;
               }
            }

            if (this.val$posixlyCorrect) {
               var1.noMoreOptions();
            }

            var1.handleNonOptionArgument(var4, var2, var3);
         }
      };
   }

   protected abstract void handleArgument(OptionParser var1, ArgumentList var2, OptionSet var3);
}
