package cn.hanabi.gui.superskidder.material.clickgui.renderManager;

import cn.hanabi.utils.RenderUtil;
import java.awt.Color;

public class Rect {
   float x;
   float y;
   float width;
   float height;
   Color color;
   Runnable runnable;

   public Rect(float x, float y, float width, float height, Color color, Runnable runnable) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.color = color;
      this.runnable = runnable;
   }

   public void render(float mouseX, float mouseY) {
      if (isHovered(this.x, this.y, this.x + this.width, this.y + this.height, mouseX, mouseY)) {
         this.runnable.run();
      }

      RenderUtil.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, this.color.getRGB());
   }

   public static boolean isHovered(float x, float y, float x2, float y2, float mouseX, float mouseY) {
      return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
   }
}
