package cn.hanabi.gui.superskidder.material.clickgui.button;

import cn.hanabi.gui.superskidder.material.items.impl.CircleButton;
import cn.hanabi.utils.RenderUtil;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class CButton extends Gui {
   public boolean realized;
   private float circleR = 0.0F;
   public String name;
   public String image;
   private float x;
   private float y;
   private float xp;
   private float yp;
   private float width;
   private float height;
   public CircleButton cb;

   public CButton(String name, String res, float xp, float yp, float w, float h) {
      this.name = name;
      this.image = res;
      this.xp = xp;
      this.yp = yp;
      this.width = w;
      this.height = h;
      this.cb = new CircleButton((double)xp, (double)yp, (double)w, (double)h, (new Color(0, 0, 0)).getRGB());
   }

   public void onMouseClicked(float mouseX, float mouseY) {
      if (isHovered(this.x + 1.0F, this.y + 1.0F, this.x + 17.0F, this.y + 17.0F, mouseX, mouseY) && Mouse.isButtonDown(0)) {
         this.realized = !this.realized;
      }

      this.cb.onClicked((double)mouseX, (double)mouseY, Mouse.getEventButton());
   }

   public void onRender(float x, float y, float mouseX, float mouseY) {
      this.x = x;
      this.y = y;
      this.onUpdate(mouseX, mouseY);
      this.cb.draw((double)mouseX, (double)mouseY, Mouse.getEventButton());
      this.cb.update();
      RenderUtil.drawImage(new ResourceLocation(this.image), x + this.xp, y + this.yp, this.width, this.height);
   }

   public void onUpdate(float mouseX, float mouseY) {
   }

   public static boolean isHovered(float x, float y, float x2, float y2, float mouseX, float mouseY) {
      return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
   }
}
