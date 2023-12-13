package javafx.collections;

import java.lang.ref.WeakReference;
import javafx.beans.NamedArg;
import javafx.beans.WeakListener;

public final class WeakSetChangeListener implements SetChangeListener, WeakListener {
   private final WeakReference ref;

   public WeakSetChangeListener(@NamedArg("listener") SetChangeListener var1) {
      if (var1 == null) {
         throw new NullPointerException("Listener must be specified.");
      } else {
         this.ref = new WeakReference(var1);
      }
   }

   public boolean wasGarbageCollected() {
      return this.ref.get() == null;
   }

   public void onChanged(SetChangeListener.Change var1) {
      SetChangeListener var2 = (SetChangeListener)this.ref.get();
      if (var2 != null) {
         var2.onChanged(var1);
      } else {
         var1.getSet().removeListener(this);
      }

   }
}
