package cn.hanabi.gui.superskidder.material.clickgui.button.values;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.superskidder.material.clickgui.ColorUtils;
import cn.hanabi.gui.superskidder.material.clickgui.Main;
import cn.hanabi.gui.superskidder.material.clickgui.Tab;
import cn.hanabi.gui.superskidder.material.clickgui.button.Button;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.value.Value;
import java.awt.Color;
import org.lwjgl.input.Mouse;

public class BNumbers extends Button {
   public BNumbers(float x, float y, Value v, Tab moduleTab) {
      super(x, y, v, moduleTab);
   }

   public void drawButton(float mouseX, float mouseY) {
      Hanabi.INSTANCE.fontManager.wqy16.drawString(this.v.getName() + ":" + this.v.getValue(), this.x, this.y - 2.0F, (new Color(50, 50, 50)).getRGB());
      Hanabi.INSTANCE.fontManager.wqy16.drawString("-", this.x, this.y + 3.0F, (new Color(50, 50, 50)).getRGB());
      Hanabi.INSTANCE.fontManager.wqy16.drawString("+", this.x + 65.0F, this.y + 3.0F, (new Color(50, 50, 50)).getRGB());
      RenderUtil.drawRect(this.x + 5.0F, this.y + 5.0F, this.x + 65.0F, this.y + 6.0F, ColorUtils.reAlpha(Main.clientColor.getRGB(), 0.6F));
      this.animation = this.animationUtils.animate(60.0F * (((Number)this.v.getValue()).floatValue() / (this.v.getValueMax() != null ? ((Number)this.v.getValueMax()).floatValue() : 0.0F)), this.animation, 0.05F);
      RenderUtil.drawRect(this.x + 5.0F, this.y + 5.0F, this.x + 5.0F + this.animation, this.y + 6.0F, Main.clientColor.getRGB());
      RenderUtil.circle(this.x + 5.0F + this.animation, this.y + 5.5F, 3.0F, Main.clientColor.getRGB());
      if (Main.isHovered(this.x + 5.0F, this.y + 4.0F, this.x + 65.0F, this.y + 7.0F, mouseX, mouseY) && Mouse.isButtonDown(0)) {
         this.drag = true;
      } else if (!Mouse.isButtonDown(0)) {
         this.drag = false;
      }

      if (this.drag) {
         double reach = (double)(mouseX - (this.x + 5.0F));
         double percent = reach / 60.0;
         double val = (((Number)this.v.getValueMax()).doubleValue() - ((Number)this.v.getValueMin()).doubleValue()) * percent;
         if (val > ((Number)this.v.getValueMin()).doubleValue() && val < ((Number)this.v.getValueMax()).doubleValue()) {
            this.v.setValueState(val * 10.0 / 10.0);
         }

         if (val < ((Number)this.v.getValueMin()).doubleValue()) {
            this.v.setValueState((Number)this.v.getValueMin());
         } else if (val > ((Number)this.v.getValueMax()).doubleValue()) {
            this.v.setValueState((Number)this.v.getValueMax());
         }
      }

   }

   public void mouseClicked(float mouseX, float mouseY) {
      super.mouseClicked(mouseX, mouseY);
   }
}
