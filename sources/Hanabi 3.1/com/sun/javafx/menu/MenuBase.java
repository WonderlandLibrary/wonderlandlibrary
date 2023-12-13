package com.sun.javafx.menu;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;

public interface MenuBase extends MenuItemBase {
   boolean isShowing();

   ReadOnlyBooleanProperty showingProperty();

   ObjectProperty onShowingProperty();

   void setOnShowing(EventHandler var1);

   EventHandler getOnShowing();

   ObjectProperty onShownProperty();

   void setOnShown(EventHandler var1);

   EventHandler getOnShown();

   ObjectProperty onHidingProperty();

   void setOnHiding(EventHandler var1);

   EventHandler getOnHiding();

   ObjectProperty onHiddenProperty();

   void setOnHidden(EventHandler var1);

   EventHandler getOnHidden();

   ObservableList getItemsBase();

   void show();

   void hide();
}
