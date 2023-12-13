package javafx.event;

import java.lang.ref.WeakReference;
import javafx.beans.NamedArg;

public final class WeakEventHandler implements EventHandler {
   private final WeakReference weakRef;

   public WeakEventHandler(@NamedArg("eventHandler") EventHandler var1) {
      this.weakRef = new WeakReference(var1);
   }

   public boolean wasGarbageCollected() {
      return this.weakRef.get() == null;
   }

   public void handle(Event var1) {
      EventHandler var2 = (EventHandler)this.weakRef.get();
      if (var2 != null) {
         var2.handle(var1);
      }

   }

   void clear() {
      this.weakRef.clear();
   }
}
