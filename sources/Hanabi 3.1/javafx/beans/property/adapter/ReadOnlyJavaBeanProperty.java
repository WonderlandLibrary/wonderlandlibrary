package javafx.beans.property.adapter;

import javafx.beans.property.ReadOnlyProperty;

public interface ReadOnlyJavaBeanProperty extends ReadOnlyProperty {
   void fireValueChangedEvent();

   void dispose();
}
