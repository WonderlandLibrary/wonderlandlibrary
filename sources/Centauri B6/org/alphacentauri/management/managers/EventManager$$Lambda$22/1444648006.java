package org.alphacentauri.management.managers;

import java.lang.invoke.LambdaForm.Hidden;
import java.util.function.Predicate;
import org.alphacentauri.management.managers.EventManager;
import org.alphacentauri.management.modules.Module;

// $FF: synthetic class
final class EventManager$$Lambda$22 implements Predicate {
   @Hidden
   public boolean test(Object var1) {
      return EventManager.lambda$registerListeners$0((Module)var1);
   }
}
