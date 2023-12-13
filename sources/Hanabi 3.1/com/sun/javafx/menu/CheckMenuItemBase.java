package com.sun.javafx.menu;

import javafx.beans.property.BooleanProperty;

public interface CheckMenuItemBase extends MenuItemBase {
   void setSelected(boolean var1);

   boolean isSelected();

   BooleanProperty selectedProperty();
}
