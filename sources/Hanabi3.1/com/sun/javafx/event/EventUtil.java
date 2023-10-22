package com.sun.javafx.event;

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventTarget;

public final class EventUtil {
   private static final EventDispatchChainImpl eventDispatchChain = new EventDispatchChainImpl();
   private static final AtomicBoolean eventDispatchChainInUse = new AtomicBoolean();

   public static Event fireEvent(EventTarget var0, Event var1) {
      if (var1.getTarget() != var0) {
         var1 = var1.copyFor(var1.getSource(), var0);
      }

      if (eventDispatchChainInUse.getAndSet(true)) {
         return fireEventImpl(new EventDispatchChainImpl(), var0, var1);
      } else {
         Event var2;
         try {
            var2 = fireEventImpl(eventDispatchChain, var0, var1);
         } finally {
            eventDispatchChain.reset();
            eventDispatchChainInUse.set(false);
         }

         return var2;
      }
   }

   public static Event fireEvent(Event var0, EventTarget... var1) {
      return fireEventImpl(new EventDispatchTreeImpl(), new CompositeEventTargetImpl(var1), var0);
   }

   private static Event fireEventImpl(EventDispatchChain var0, EventTarget var1, Event var2) {
      EventDispatchChain var3 = var1.buildEventDispatchChain(var0);
      return var3.dispatchEvent(var2);
   }
}
