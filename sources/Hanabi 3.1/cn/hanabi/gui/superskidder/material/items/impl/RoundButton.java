package cn.hanabi.gui.superskidder.material.items.impl;

import cn.hanabi.gui.superskidder.material.items.SButton;
import cn.hanabi.gui.superskidder.material.items.other.Shadow;
import cn.hanabi.utils.RenderUtil;
import java.awt.Color;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;

public class RoundButton extends SButton {
   public RoundButton(double x, double y, double width, double height, int color) {
      super(x, y, width, height, color);
   }

   public void draw(double mouseX, double mouseY, int mouseButton) {
      RenderUtil.drawRoundedRect((float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + this.height), 1.0F, this.color);
      Iterator var6 = this.ss.iterator();

      while(var6.hasNext()) {
         Shadow s = (Shadow)var6.next();
         GL11.glEnable(3089);
         RenderUtil.doGlScissor((int)this.x, (int)this.y, (int)this.width, (int)this.height);
         RenderUtil.drawFilledCircle((int)s.xPos, (int)s.yPos, (float)s.size, (new Color(255, 255, 255, (int)s.alpha)).getRGB());
         GL11.glDisable(3089);
      }

   }

   public void update() {
      super.update();
   }

   public void onClicked(double mouseX, double mouseY, int mouseButton) {
      if (RenderUtil.isHovering((int)mouseX, (int)mouseY, (float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + this.height))) {
         this.ss.add(new Shadow(mouseX, mouseY, Math.max(this.width, this.height)));
      }

   }
}
