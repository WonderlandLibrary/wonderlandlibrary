package javafx.scene.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableMap;

public interface Toggle {
   ToggleGroup getToggleGroup();

   void setToggleGroup(ToggleGroup var1);

   ObjectProperty toggleGroupProperty();

   boolean isSelected();

   void setSelected(boolean var1);

   BooleanProperty selectedProperty();

   Object getUserData();

   void setUserData(Object var1);

   ObservableMap getProperties();
}
