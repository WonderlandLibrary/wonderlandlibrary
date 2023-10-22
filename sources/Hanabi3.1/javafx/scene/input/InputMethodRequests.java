package javafx.scene.input;

import javafx.geometry.Point2D;

public interface InputMethodRequests {
   Point2D getTextLocation(int var1);

   int getLocationOffset(int var1, int var2);

   void cancelLatestCommittedText();

   String getSelectedText();
}
