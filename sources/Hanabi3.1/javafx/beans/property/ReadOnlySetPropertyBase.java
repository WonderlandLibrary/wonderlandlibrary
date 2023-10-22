package javafx.beans.property;

import com.sun.javafx.binding.SetExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.SetChangeListener;

public abstract class ReadOnlySetPropertyBase extends ReadOnlySetProperty {
   private SetExpressionHelper helper;

   public void addListener(InvalidationListener var1) {
      this.helper = SetExpressionHelper.addListener(this.helper, this, (InvalidationListener)var1);
   }

   public void removeListener(InvalidationListener var1) {
      this.helper = SetExpressionHelper.removeListener(this.helper, var1);
   }

   public void addListener(ChangeListener var1) {
      this.helper = SetExpressionHelper.addListener(this.helper, this, (ChangeListener)var1);
   }

   public void removeListener(ChangeListener var1) {
      this.helper = SetExpressionHelper.removeListener(this.helper, var1);
   }

   public void addListener(SetChangeListener var1) {
      this.helper = SetExpressionHelper.addListener(this.helper, this, (SetChangeListener)var1);
   }

   public void removeListener(SetChangeListener var1) {
      this.helper = SetExpressionHelper.removeListener(this.helper, var1);
   }

   protected void fireValueChangedEvent() {
      SetExpressionHelper.fireValueChangedEvent(this.helper);
   }

   protected void fireValueChangedEvent(SetChangeListener.Change var1) {
      SetExpressionHelper.fireValueChangedEvent(this.helper, var1);
   }
}
