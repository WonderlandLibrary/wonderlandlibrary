package javafx.beans.value;

import java.lang.ref.WeakReference;
import javafx.beans.NamedArg;
import javafx.beans.WeakListener;

public final class WeakChangeListener implements ChangeListener, WeakListener {
   private final WeakReference ref;

   public WeakChangeListener(@NamedArg("listener") ChangeListener var1) {
      if (var1 == null) {
         throw new NullPointerException("Listener must be specified.");
      } else {
         this.ref = new WeakReference(var1);
      }
   }

   public boolean wasGarbageCollected() {
      return this.ref.get() == null;
   }

   public void changed(ObservableValue var1, Object var2, Object var3) {
      ChangeListener var4 = (ChangeListener)this.ref.get();
      if (var4 != null) {
         var4.changed(var1, var2, var3);
      } else {
         var1.removeListener(this);
      }

   }
}
