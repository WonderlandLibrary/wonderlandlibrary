package javafx.scene.control;

import javafx.beans.property.ObjectProperty;

public interface Skinnable {
   ObjectProperty skinProperty();

   void setSkin(Skin var1);

   Skin getSkin();
}
