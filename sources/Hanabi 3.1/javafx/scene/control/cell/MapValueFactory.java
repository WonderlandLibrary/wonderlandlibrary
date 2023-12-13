package javafx.scene.control.cell;

import java.util.Map;
import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyFloatWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class MapValueFactory implements Callback {
   private final Object key;

   public MapValueFactory(@NamedArg("key") Object var1) {
      this.key = var1;
   }

   public ObservableValue call(TableColumn.CellDataFeatures var1) {
      Map var2 = (Map)var1.getValue();
      Object var3 = var2.get(this.key);
      if (var3 instanceof ObservableValue) {
         return (ObservableValue)var3;
      } else if (var3 instanceof Boolean) {
         return new ReadOnlyBooleanWrapper((Boolean)var3);
      } else if (var3 instanceof Integer) {
         return new ReadOnlyIntegerWrapper((Integer)var3);
      } else if (var3 instanceof Float) {
         return new ReadOnlyFloatWrapper((Float)var3);
      } else if (var3 instanceof Long) {
         return new ReadOnlyLongWrapper((Long)var3);
      } else if (var3 instanceof Double) {
         return new ReadOnlyDoubleWrapper((Double)var3);
      } else {
         return (ObservableValue)(var3 instanceof String ? new ReadOnlyStringWrapper((String)var3) : new ReadOnlyObjectWrapper(var3));
      }
   }
}
