package cn.hanabi.gui.newclickui.misc;

import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.TranslateUtil;
import java.awt.Color;

public class ClickEffect {
   public float x;
   public float y;
   public TranslateUtil anima = new TranslateUtil(0.0F, 0.0F);

   public ClickEffect(float x, float y) {
      this.x = x;
      this.y = y;
      this.anima.setXY(0.0F, 0.0F);
   }

   public void draw() {
      this.anima.interpolate(100.0F, 100.0F, 0.15F);
      double radius = (double)(8.0F * this.anima.getX() / 100.0F);
      int alpha = (int)(255.0F - 255.0F * this.anima.getY() / 100.0F);
      RenderUtil.drawArc(this.x, this.y, radius, (new Color(255, 255, 255, alpha)).getRGB(), 0, 360.0, 5);
   }

   public boolean canRemove() {
      return this.anima.getX() == 100.0F && this.anima.getY() == 100.0F;
   }
}
