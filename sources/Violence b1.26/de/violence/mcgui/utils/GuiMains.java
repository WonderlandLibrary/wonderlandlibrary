package de.violence.mcgui.utils;

public class GuiMains {
   public static boolean isHovered(int mouseX, int mouseY, int x, int y, int width, int height) {
      return mouseX >= x && mouseX - width <= x && mouseY + height / 2 >= y && mouseY - height <= y;
   }
}
