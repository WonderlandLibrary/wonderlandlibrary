package com.sun.deploy.uitoolkit.impl.fx;

import javafx.stage.Stage;

public interface AppletStageManager {
   Stage getAppletStage();

   Stage getPreloaderStage();

   Stage getErrorStage();

   void setSize(int var1, int var2);
}
