package com.sun.javafx.tk;

import java.util.Map;
import javafx.stage.Stage;

public interface AppletWindow {
   void setStageOnTop(Stage var1);

   void setBackgroundColor(int var1);

   void setForegroundColor(int var1);

   void setVisible(boolean var1);

   void setSize(int var1, int var2);

   int getWidth();

   int getHeight();

   void setPosition(int var1, int var2);

   int getPositionX();

   int getPositionY();

   float getPlatformScaleX();

   float getPlatformScaleY();

   int getRemoteLayerId();

   void dispatchEvent(Map var1);
}
