package javafx.geometry;

public enum Side {
   TOP,
   BOTTOM,
   LEFT,
   RIGHT;

   public boolean isVertical() {
      return this == LEFT || this == RIGHT;
   }

   public boolean isHorizontal() {
      return this == TOP || this == BOTTOM;
   }
}
