package org.alphacentauri;

import java.lang.invoke.LambdaForm.Hidden;
import org.alphacentauri.AC;

// $FF: synthetic class
final class AC$$Lambda$28 implements Runnable {
   private final AC arg$1;

   private AC$$Lambda$28(AC var1) {
      this.arg$1 = var1;
   }

   @Hidden
   public void run() {
      this.arg$1.onShutdown();
   }

   private static Runnable get$Lambda(AC var0) {
      return new AC$$Lambda$28(var0);
   }
}
