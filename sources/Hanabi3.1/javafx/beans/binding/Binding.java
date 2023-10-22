package javafx.beans.binding;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

public interface Binding extends ObservableValue {
   boolean isValid();

   void invalidate();

   ObservableList getDependencies();

   void dispose();
}
