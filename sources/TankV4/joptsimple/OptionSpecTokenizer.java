package joptsimple;

class OptionSpecTokenizer {
   private static final char POSIXLY_CORRECT_MARKER = '+';
   private static final char HELP_MARKER = '*';
   private String specification;
   private int index;

   OptionSpecTokenizer(String var1) {
      if (var1 == null) {
         throw new NullPointerException("null option specification");
      } else {
         this.specification = var1;
      }
   }

   AbstractOptionSpec next() {
      // $FF: Couldn't be decompiled
   }

   void configure(OptionParser param1) {
      // $FF: Couldn't be decompiled
   }

   private void adjustForPosixlyCorrect(OptionParser var1) {
      if ('+' == this.specification.charAt(0)) {
         var1.posixlyCorrect(true);
         this.specification = this.specification.substring(1);
      }

   }

   private AbstractOptionSpec handleReservedForExtensionsToken() {
      // $FF: Couldn't be decompiled
   }

   private AbstractOptionSpec handleArgumentAcceptingOption(String param1) {
      // $FF: Couldn't be decompiled
   }
}
