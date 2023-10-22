package cn.hanabi.gui.newclickui.impl;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.font.noway.ttfr.HFontRenderer;
import cn.hanabi.gui.newclickui.ClickUI;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.TranslateUtil;
import cn.hanabi.value.Value;
import java.awt.Color;

public class ModeValue {
   public Value values;
   public float length;
   public float x;
   public float y;
   public String name;
   public TranslateUtil anima = new TranslateUtil(0.0F, 0.0F);
   public boolean isOpen = false;

   public ModeValue(Value values) {
      this.values = values;
      this.name = values.getModeTitle();
      this.anima.setXY(18.0F, 0.0F);
      this.isOpen = false;
   }

   public void draw(float x, float y, float mouseX, float mouseY) {
      this.x = x;
      this.y = y;
      this.anima.interpolate(this.isOpen ? (float)(this.values.mode.size() * 18) : 18.0F, 0.0F, 0.2F);
      this.length = 30.0F + this.anima.getX() - 18.0F;
      HFontRenderer font = Hanabi.INSTANCE.fontManager.wqy18;
      font.drawString(this.name, x + 20.0F, y + 8.0F, (new Color(255, 255, 255, 255)).getRGB());
      RenderUtil.drawRoundRect((double)(x + 130.0F), (double)(y + 2.0F), (double)(x + 220.0F), (double)(y + this.anima.getX() + 3.0F), 2, (new Color(255, 255, 255, 255)).getRGB());
      RenderUtil.drawRoundRect((double)(x + 131.0F), (double)(y + 3.0F), (double)(x + 219.0F), (double)(y + this.anima.getX() + 2.0F), 2, (new Color(30, 30, 30, 255)).getRGB());
      if (this.isOpen || this.anima.getX() != 18.0F) {
         RenderUtil.drawRoundRect((double)(x + 135.0F), (double)(y + 20.0F), (double)(x + 215.0F), (double)(y + 21.0F), 1, (new Color(255, 255, 255, 255)).getRGB());
      }

      int i = 0;

      for(float modeY = 18.0F; i < this.values.mode.size(); ++i) {
         if (this.values.getModeAt(this.values.getCurrentMode()) != this.values.mode.get(i)) {
            font.drawCenteredString((String)this.values.mode.get(i), x + 130.0F + 45.0F, y + modeY + 6.0F, (new Color(255, 255, 255, 255)).getRGB());
            modeY += 18.0F;
         }
      }

      RenderUtil.drawRoundRect((double)(x + 131.0F), (double)(y + this.anima.getX() + 3.0F), (double)(x + 219.0F), (double)(y + (float)(this.values.mode.size() * 18) + 3.0F), 0, (new Color(30, 30, 30, 255)).getRGB());
      font.drawCenteredString(this.values.getModeAt(this.values.getCurrentMode()), x + 130.0F + 45.0F, y + 6.0F, (new Color(255, 255, 255, 255)).getRGB());
   }

   public void handleMouse(float mouseX, float mouseY, int key) {
      if (ClickUI.isHover(mouseX, mouseY, this.x + 131.0F, this.y + 3.0F, this.x + 219.0F, this.y + this.anima.getX() + 2.0F) && ClickUI.isHover(mouseX, mouseY, this.x, this.y, this.x + 240.0F, this.y + 235.0F) && key == 0) {
         int i = 0;

         for(float modeY = 18.0F; i < this.values.mode.size(); ++i) {
            if (this.values.getModeAt(this.values.getCurrentMode()) != this.values.mode.get(i)) {
               if (ClickUI.isHover(mouseX, mouseY, this.x + 131.0F, this.y + modeY + 4.0F, this.x + 219.0F, this.y + modeY + 22.0F)) {
                  this.values.setCurrentMode(i);
               }

               modeY += 18.0F;
            }
         }

         this.isOpen = !this.isOpen;
      }

   }

   public float getLength() {
      return this.length;
   }
}
