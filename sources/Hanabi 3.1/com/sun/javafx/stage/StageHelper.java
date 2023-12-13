package com.sun.javafx.stage;

import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class StageHelper {
   private static StageAccessor stageAccessor;

   public static ObservableList getStages() {
      if (stageAccessor == null) {
         try {
            Class.forName(Stage.class.getName(), true, Stage.class.getClassLoader());
         } catch (ClassNotFoundException var1) {
         }
      }

      return stageAccessor.getStages();
   }

   public static void initSecurityDialog(Stage var0, boolean var1) {
      stageAccessor.initSecurityDialog(var0, var1);
   }

   public static void setStageAccessor(StageAccessor var0) {
      if (stageAccessor != null) {
         System.out.println("Warning: Stage accessor already set: " + stageAccessor);
         Thread.dumpStack();
      }

      stageAccessor = var0;
   }

   public interface StageAccessor {
      ObservableList getStages();

      void initSecurityDialog(Stage var1, boolean var2);
   }
}
