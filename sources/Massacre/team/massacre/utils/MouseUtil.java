package team.massacre.utils;

public class MouseUtil {
   public static boolean mouseWithinBounds(int mouseX, int mouseY, double x, double y, double width, double height) {
      return (double)mouseX >= x && (double)mouseX <= x + width && (double)mouseY >= y && (double)mouseY <= y + height;
   }
}
