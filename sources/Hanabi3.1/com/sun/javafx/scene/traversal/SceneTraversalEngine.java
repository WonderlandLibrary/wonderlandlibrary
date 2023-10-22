package com.sun.javafx.scene.traversal;

import javafx.scene.Parent;
import javafx.scene.Scene;

public final class SceneTraversalEngine extends TopMostTraversalEngine {
   private final Scene scene;

   public SceneTraversalEngine(Scene var1) {
      this.scene = var1;
   }

   protected Parent getRoot() {
      return this.scene.getRoot();
   }
}
