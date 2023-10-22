package cn.hanabi.gui.superskidder.material.clickgui.button.values;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.superskidder.material.clickgui.AnimationUtils;
import cn.hanabi.gui.superskidder.material.clickgui.ColorUtils;
import cn.hanabi.gui.superskidder.material.clickgui.Main;
import cn.hanabi.gui.superskidder.material.clickgui.Tab;
import cn.hanabi.gui.superskidder.material.clickgui.button.Button;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.value.Value;
import java.awt.Color;

public class BOption extends Button {
   AnimationUtils au = new AnimationUtils();

   public BOption(float x, float y, Value v, Tab moduleTab) {
      super(x, y, v, moduleTab);
   }

   public void draw(float mouseX, float mouseY) {
      super.draw(mouseX, mouseY);
      Hanabi.INSTANCE.fontManager.wqy18.drawString(this.v.getName(), this.x + 30.0F, this.y + 2.0F, (new Color(50, 50, 50)).getRGB());
      if ((Boolean)this.v.getValue()) {
         RenderUtil.drawRect(this.x, this.y + 1.0F, this.x + 20.0F, this.y + 9.0F, ColorUtils.lighter(Main.clientColor, 0.30000001192092896).getRGB());
         this.animation = this.au.animate(20.0F, this.animation, 0.05F);
         if (Main.isHovered(this.x, this.y + 1.0F, this.x + 20.0F, this.y + 9.0F, mouseX, mouseY)) {
            RenderUtil.circle(this.x + this.animation - 3.5F, this.y + 5.0F, 9.0F, ColorUtils.reAlpha(Main.clientColor.getRGB(), 0.1F));
         }

         RenderUtil.circle(this.x + this.animation - 3.0F, this.y + 5.0F, 6.0F, (new Color(240, 240, 240)).getRGB());
      } else {
         RenderUtil.drawRect(this.x, this.y + 1.0F, this.x + 20.0F, this.y + 9.0F, (new Color(180, 180, 180)).getRGB());
         this.animation = this.au.animate(0.0F, this.animation, 0.05F);
         if (Main.isHovered(this.x, this.y, this.x + 20.0F, this.y + 10.0F, mouseX, mouseY)) {
            RenderUtil.circle(this.x + this.animation - 1.5F, this.y + 5.0F, 9.0F, (new Color(0, 0, 0, 30)).getRGB());
         }

         RenderUtil.circle(this.x + this.animation - 1.0F, this.y + 5.0F, 6.0F, (new Color(240, 240, 240)).getRGB());
      }

   }

   public void mouseClicked(float mouseX, float mouseY) {
      super.mouseClicked(mouseX, mouseY);
      if (Main.isHovered(this.x, this.y, this.x + 20.0F, this.y + 10.0F, mouseX, mouseY)) {
         this.v.setValueState(!(Boolean)this.v.getValue());
      }

   }
}
