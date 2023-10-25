package space.clowdy.utils;

public class GuiUtils {
     public static boolean isHovered(int integer1, int integer2, int integer3, int integer4, int integer5, int integer6) {
          return integer1 > integer3 && integer2 > integer4 && integer1 < integer5 && integer2 < integer6;
     }
}
