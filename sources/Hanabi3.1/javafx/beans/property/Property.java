package javafx.beans.property;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;

public interface Property extends ReadOnlyProperty, WritableValue {
   void bind(ObservableValue var1);

   void unbind();

   boolean isBound();

   void bindBidirectional(Property var1);

   void unbindBidirectional(Property var1);
}
