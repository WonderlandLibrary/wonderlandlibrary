package javafx.collections;

import java.lang.ref.WeakReference;
import javafx.beans.NamedArg;
import javafx.beans.WeakListener;

public final class WeakMapChangeListener implements MapChangeListener, WeakListener {
   private final WeakReference ref;

   public WeakMapChangeListener(@NamedArg("listener") MapChangeListener var1) {
      if (var1 == null) {
         throw new NullPointerException("Listener must be specified.");
      } else {
         this.ref = new WeakReference(var1);
      }
   }

   public boolean wasGarbageCollected() {
      return this.ref.get() == null;
   }

   public void onChanged(MapChangeListener.Change var1) {
      MapChangeListener var2 = (MapChangeListener)this.ref.get();
      if (var2 != null) {
         var2.onChanged(var1);
      } else {
         var1.getMap().removeListener(this);
      }

   }
}
