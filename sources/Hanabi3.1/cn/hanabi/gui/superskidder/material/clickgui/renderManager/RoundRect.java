package cn.hanabi.gui.superskidder.material.clickgui.renderManager;

import cn.hanabi.utils.RenderUtil;
import java.awt.Color;
import org.lwjgl.input.Mouse;

public class RoundRect {
   float x;
   float y;
   float width;
   float height;
   float round;
   Color color;
   Runnable runnable;

   public RoundRect(float x, float y, float width, float height, float round, Color color, Runnable runnable) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.round = round;
      this.color = color;
      this.runnable = runnable;
   }

   public void render(float mouseX, float mouseY) {
      if (isHovered(this.x, this.y, this.x + this.width, this.y + this.height, mouseX, mouseY) && Mouse.isButtonDown(0)) {
         this.runnable.run();
      }

      RenderUtil.drawRoundedRect(this.x, this.y, this.x + this.width, this.y + this.height, this.round, this.color.getRGB());
   }

   public static boolean isHovered(float x, float y, float x2, float y2, float mouseX, float mouseY) {
      return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
   }
}
