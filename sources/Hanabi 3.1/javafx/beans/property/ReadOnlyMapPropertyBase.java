package javafx.beans.property;

import com.sun.javafx.binding.MapExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.MapChangeListener;

public abstract class ReadOnlyMapPropertyBase extends ReadOnlyMapProperty {
   private MapExpressionHelper helper;

   public void addListener(InvalidationListener var1) {
      this.helper = MapExpressionHelper.addListener(this.helper, this, (InvalidationListener)var1);
   }

   public void removeListener(InvalidationListener var1) {
      this.helper = MapExpressionHelper.removeListener(this.helper, var1);
   }

   public void addListener(ChangeListener var1) {
      this.helper = MapExpressionHelper.addListener(this.helper, this, (ChangeListener)var1);
   }

   public void removeListener(ChangeListener var1) {
      this.helper = MapExpressionHelper.removeListener(this.helper, var1);
   }

   public void addListener(MapChangeListener var1) {
      this.helper = MapExpressionHelper.addListener(this.helper, this, (MapChangeListener)var1);
   }

   public void removeListener(MapChangeListener var1) {
      this.helper = MapExpressionHelper.removeListener(this.helper, var1);
   }

   protected void fireValueChangedEvent() {
      MapExpressionHelper.fireValueChangedEvent(this.helper);
   }

   protected void fireValueChangedEvent(MapChangeListener.Change var1) {
      MapExpressionHelper.fireValueChangedEvent(this.helper, var1);
   }
}
