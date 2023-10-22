package cn.hanabi.gui.newclickui.impl;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.font.noway.ttfr.HFontRenderer;
import cn.hanabi.gui.newclickui.ClickUI;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.TranslateUtil;
import cn.hanabi.value.Value;
import java.awt.Color;

public class BoolValue {
   public Value values;
   public float length;
   public float x;
   public float y;
   public String name;
   public TranslateUtil anima = new TranslateUtil(0.0F, 0.0F);

   public BoolValue(Value values) {
      this.values = values;
      this.name = values.getValueName().split("_")[1];
      this.anima.setXY((Boolean)values.getValueState() ? 10.0F : 0.0F, (Boolean)values.getValueState() ? 0.0F : 255.0F);
   }

   public void draw(float x, float y, float mouseX, float mouseY) {
      this.x = x;
      this.y = y;
      this.anima.interpolate((Boolean)this.values.getValueState() ? 10.0F : 0.0F, (Boolean)this.values.getValueState() ? 255.0F : 0.0F, 0.25F);
      this.length = 30.0F;
      HFontRenderer font = Hanabi.INSTANCE.fontManager.wqy18;
      font.drawString(this.name, x + 20.0F, y + 8.0F, (new Color(255, 255, 255, 255)).getRGB());
      RenderUtil.circle(x + 220.0F, y + 12.0F, 4.0F, (new Color(70, 70, 70, 255)).getRGB());
      RenderUtil.circle(x + 210.0F, y + 12.0F, 4.0F, (new Color(70, 70, 70, 255)).getRGB());
      RenderUtil.drawRect(x + 210.0F, y + 7.5F, x + 220.0F, y + 16.5F, (new Color(70, 70, 70, 255)).getRGB());
      RenderUtil.circle(x + 210.0F + this.anima.getX(), y + 12.0F, 4.0F, (new Color(180, 180, 180, 255)).getRGB());
      RenderUtil.circle(x + 210.0F, y + 12.0F, 4.0F, (new Color(180, 180, 180, 255)).getRGB());
      RenderUtil.drawRect(x + 210.0F, y + 7.5F, x + 210.0F + this.anima.getX(), y + 16.5F, (new Color(180, 180, 180, 255)).getRGB());
      RenderUtil.circle(x + 210.0F + this.anima.getX(), y + 12.0F, 5.0F, (new Color(255, 255, 255, 255)).getRGB());
   }

   public void handleMouse(float mouseX, float mouseY, int key) {
      if (ClickUI.isHover(mouseX, mouseY, this.x + 205.0F, this.y + 7.0F, this.x + 225.0F, this.y + 17.0F) && key == 0) {
         this.values.setValueState(!(Boolean)this.values.getValueState());
      }

   }

   public float getLength() {
      return this.length;
   }
}
