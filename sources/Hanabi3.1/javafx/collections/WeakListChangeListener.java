package javafx.collections;

import java.lang.ref.WeakReference;
import javafx.beans.NamedArg;
import javafx.beans.WeakListener;

public final class WeakListChangeListener implements ListChangeListener, WeakListener {
   private final WeakReference ref;

   public WeakListChangeListener(@NamedArg("listener") ListChangeListener var1) {
      if (var1 == null) {
         throw new NullPointerException("Listener must be specified.");
      } else {
         this.ref = new WeakReference(var1);
      }
   }

   public boolean wasGarbageCollected() {
      return this.ref.get() == null;
   }

   public void onChanged(ListChangeListener.Change var1) {
      ListChangeListener var2 = (ListChangeListener)this.ref.get();
      if (var2 != null) {
         var2.onChanged(var1);
      } else {
         var1.getList().removeListener(this);
      }

   }
}
