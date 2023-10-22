package javafx.collections;

import com.sun.javafx.collections.ArrayListenerHelper;
import javafx.beans.InvalidationListener;

public abstract class ObservableArrayBase implements ObservableArray {
   private ArrayListenerHelper listenerHelper;

   public final void addListener(InvalidationListener var1) {
      this.listenerHelper = ArrayListenerHelper.addListener(this.listenerHelper, this, (InvalidationListener)var1);
   }

   public final void removeListener(InvalidationListener var1) {
      this.listenerHelper = ArrayListenerHelper.removeListener(this.listenerHelper, var1);
   }

   public final void addListener(ArrayChangeListener var1) {
      this.listenerHelper = ArrayListenerHelper.addListener(this.listenerHelper, this, (ArrayChangeListener)var1);
   }

   public final void removeListener(ArrayChangeListener var1) {
      this.listenerHelper = ArrayListenerHelper.removeListener(this.listenerHelper, var1);
   }

   protected final void fireChange(boolean var1, int var2, int var3) {
      ArrayListenerHelper.fireValueChangedEvent(this.listenerHelper, var1, var2, var3);
   }
}
