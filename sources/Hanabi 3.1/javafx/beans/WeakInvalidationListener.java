package javafx.beans;

import java.lang.ref.WeakReference;

public final class WeakInvalidationListener implements InvalidationListener, WeakListener {
   private final WeakReference ref;

   public WeakInvalidationListener(@NamedArg("listener") InvalidationListener var1) {
      if (var1 == null) {
         throw new NullPointerException("Listener must be specified.");
      } else {
         this.ref = new WeakReference(var1);
      }
   }

   public boolean wasGarbageCollected() {
      return this.ref.get() == null;
   }

   public void invalidated(Observable var1) {
      InvalidationListener var2 = (InvalidationListener)this.ref.get();
      if (var2 != null) {
         var2.invalidated(var1);
      } else {
         var1.removeListener(this);
      }

   }
}
