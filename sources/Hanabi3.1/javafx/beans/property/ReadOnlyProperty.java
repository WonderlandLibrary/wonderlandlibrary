package javafx.beans.property;

import javafx.beans.value.ObservableValue;

public interface ReadOnlyProperty extends ObservableValue {
   Object getBean();

   String getName();
}
