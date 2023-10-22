package com.sun.javafx.scene.control.behavior;

public enum OptionalBoolean {
   TRUE,
   FALSE,
   ANY;

   public boolean equals(boolean var1) {
      if (this == ANY) {
         return true;
      } else if (var1 && this == TRUE) {
         return true;
      } else {
         return !var1 && this == FALSE;
      }
   }
}
