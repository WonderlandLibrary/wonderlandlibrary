package javafx.beans.property;

import com.sun.javafx.binding.ListExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;

public abstract class ReadOnlyListPropertyBase extends ReadOnlyListProperty {
   private ListExpressionHelper helper;

   public void addListener(InvalidationListener var1) {
      this.helper = ListExpressionHelper.addListener(this.helper, this, (InvalidationListener)var1);
   }

   public void removeListener(InvalidationListener var1) {
      this.helper = ListExpressionHelper.removeListener(this.helper, var1);
   }

   public void addListener(ChangeListener var1) {
      this.helper = ListExpressionHelper.addListener(this.helper, this, (ChangeListener)var1);
   }

   public void removeListener(ChangeListener var1) {
      this.helper = ListExpressionHelper.removeListener(this.helper, var1);
   }

   public void addListener(ListChangeListener var1) {
      this.helper = ListExpressionHelper.addListener(this.helper, this, (ListChangeListener)var1);
   }

   public void removeListener(ListChangeListener var1) {
      this.helper = ListExpressionHelper.removeListener(this.helper, var1);
   }

   protected void fireValueChangedEvent() {
      ListExpressionHelper.fireValueChangedEvent(this.helper);
   }

   protected void fireValueChangedEvent(ListChangeListener.Change var1) {
      ListExpressionHelper.fireValueChangedEvent(this.helper, var1);
   }
}
