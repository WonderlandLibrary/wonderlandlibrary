package com.sun.javafx.scene.traversal;

import javafx.scene.Parent;
import javafx.scene.SubScene;

public final class SubSceneTraversalEngine extends TopMostTraversalEngine {
   private final SubScene subScene;

   public SubSceneTraversalEngine(SubScene var1) {
      this.subScene = var1;
   }

   protected Parent getRoot() {
      return this.subScene.getRoot();
   }
}
