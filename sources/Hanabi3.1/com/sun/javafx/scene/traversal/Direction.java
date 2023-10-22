package com.sun.javafx.scene.traversal;

import javafx.geometry.NodeOrientation;

public enum Direction {
   UP(false),
   DOWN(true),
   LEFT(false),
   RIGHT(true),
   NEXT(true),
   NEXT_IN_LINE(true),
   PREVIOUS(false);

   private final boolean forward;

   private Direction(boolean var3) {
      this.forward = var3;
   }

   public boolean isForward() {
      return this.forward;
   }

   public Direction getDirectionForNodeOrientation(NodeOrientation var1) {
      if (var1 == NodeOrientation.RIGHT_TO_LEFT) {
         switch (this) {
            case LEFT:
               return RIGHT;
            case RIGHT:
               return LEFT;
         }
      }

      return this;
   }
}
