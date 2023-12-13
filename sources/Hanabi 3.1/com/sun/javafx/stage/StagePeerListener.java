package com.sun.javafx.stage;

import javafx.stage.Stage;

public class StagePeerListener extends WindowPeerListener {
   private final Stage stage;
   private final StageAccessor stageAccessor;

   public StagePeerListener(Stage var1, StageAccessor var2) {
      super(var1);
      this.stage = var1;
      this.stageAccessor = var2;
   }

   public void changedIconified(boolean var1) {
      this.stageAccessor.setIconified(this.stage, var1);
   }

   public void changedMaximized(boolean var1) {
      this.stageAccessor.setMaximized(this.stage, var1);
   }

   public void changedResizable(boolean var1) {
      this.stageAccessor.setResizable(this.stage, var1);
   }

   public void changedFullscreen(boolean var1) {
      this.stageAccessor.setFullScreen(this.stage, var1);
   }

   public void changedAlwaysOnTop(boolean var1) {
      this.stageAccessor.setAlwaysOnTop(this.stage, var1);
   }

   public interface StageAccessor {
      void setIconified(Stage var1, boolean var2);

      void setMaximized(Stage var1, boolean var2);

      void setResizable(Stage var1, boolean var2);

      void setFullScreen(Stage var1, boolean var2);

      void setAlwaysOnTop(Stage var1, boolean var2);
   }
}
