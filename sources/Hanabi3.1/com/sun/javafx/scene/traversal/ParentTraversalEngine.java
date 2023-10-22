package com.sun.javafx.scene.traversal;

import javafx.scene.Parent;

public final class ParentTraversalEngine extends TraversalEngine {
   private final Parent root;
   private Boolean overridenTraversability;

   public ParentTraversalEngine(Parent var1, Algorithm var2) {
      super(var2);
      this.root = var1;
   }

   public ParentTraversalEngine(Parent var1) {
      this.root = var1;
   }

   public void setOverriddenFocusTraversability(Boolean var1) {
      this.overridenTraversability = var1;
   }

   protected Parent getRoot() {
      return this.root;
   }

   public boolean isParentTraversable() {
      return this.overridenTraversability != null ? this.root.isFocusTraversable() && this.overridenTraversability : this.root.isFocusTraversable();
   }
}
