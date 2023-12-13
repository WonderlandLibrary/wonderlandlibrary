package javafx.beans.value;

import com.sun.javafx.binding.ExpressionHelper;
import javafx.beans.InvalidationListener;

public abstract class ObservableValueBase implements ObservableValue {
   private ExpressionHelper helper;

   public void addListener(InvalidationListener var1) {
      this.helper = ExpressionHelper.addListener(this.helper, this, (InvalidationListener)var1);
   }

   public void addListener(ChangeListener var1) {
      this.helper = ExpressionHelper.addListener(this.helper, this, (ChangeListener)var1);
   }

   public void removeListener(InvalidationListener var1) {
      this.helper = ExpressionHelper.removeListener(this.helper, var1);
   }

   public void removeListener(ChangeListener var1) {
      this.helper = ExpressionHelper.removeListener(this.helper, var1);
   }

   protected void fireValueChangedEvent() {
      ExpressionHelper.fireValueChangedEvent(this.helper);
   }
}
