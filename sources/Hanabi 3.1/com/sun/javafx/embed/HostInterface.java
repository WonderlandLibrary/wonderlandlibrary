package com.sun.javafx.embed;

import com.sun.javafx.cursor.CursorFrame;

public interface HostInterface {
   void setEmbeddedStage(EmbeddedStageInterface var1);

   void setEmbeddedScene(EmbeddedSceneInterface var1);

   boolean requestFocus();

   boolean traverseFocusOut(boolean var1);

   void repaint();

   void setPreferredSize(int var1, int var2);

   void setEnabled(boolean var1);

   void setCursor(CursorFrame var1);

   boolean grabFocus();

   void ungrabFocus();
}
