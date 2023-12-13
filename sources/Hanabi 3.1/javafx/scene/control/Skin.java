package javafx.scene.control;

import javafx.scene.Node;

public interface Skin {
   Skinnable getSkinnable();

   Node getNode();

   void dispose();
}
