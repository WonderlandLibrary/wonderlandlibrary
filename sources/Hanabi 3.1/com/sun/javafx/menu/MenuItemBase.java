package com.sun.javafx.menu;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCombination;

public interface MenuItemBase {
   void setId(String var1);

   String getId();

   StringProperty idProperty();

   void setText(String var1);

   String getText();

   StringProperty textProperty();

   void setGraphic(Node var1);

   Node getGraphic();

   ObjectProperty graphicProperty();

   void setOnAction(EventHandler var1);

   EventHandler getOnAction();

   ObjectProperty onActionProperty();

   void setDisable(boolean var1);

   boolean isDisable();

   BooleanProperty disableProperty();

   void setVisible(boolean var1);

   boolean isVisible();

   BooleanProperty visibleProperty();

   void setAccelerator(KeyCombination var1);

   KeyCombination getAccelerator();

   ObjectProperty acceleratorProperty();

   void setMnemonicParsing(boolean var1);

   boolean isMnemonicParsing();

   BooleanProperty mnemonicParsingProperty();

   void fire();

   void fireValidation();
}
