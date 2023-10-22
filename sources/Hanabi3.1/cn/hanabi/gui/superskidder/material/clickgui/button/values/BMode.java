package cn.hanabi.gui.superskidder.material.clickgui.button.values;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.superskidder.material.clickgui.Main;
import cn.hanabi.gui.superskidder.material.clickgui.Tab;
import cn.hanabi.gui.superskidder.material.clickgui.button.Button;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.value.Value;
import java.awt.Color;
import org.lwjgl.input.Mouse;

public class BMode extends Button {
   public BMode(float x, float y, Value v, Tab moduleTab) {
      super(x, y, v, moduleTab);
   }

   public void draw(float mouseX, float mouseY) {
      RenderUtil.drawBorderedRect(this.x, this.y - 5.0F, this.x + 65.0F, this.y - 5.0F + this.animation, 0.5F, (new Color(100, 100, 100)).getRGB(), (new Color(255, 255, 255)).getRGB());
      RenderUtil.drawRect(this.x - this.animation / (float)(this.v.getModes().length * 20) * 5.0F, this.y - 2.0F - this.animation / (float)(this.v.getModes().length * 20) * 5.0F, this.x - this.animation / (float)(this.v.getModes().length * 20) * 5.0F + (float)Hanabi.INSTANCE.fontManager.wqy18.getStringWidth(this.v.getModeTitle()), this.y - 2.0F - this.animation / (float)(this.v.getModes().length * 20) * 5.0F + 9.0F, -1);
      Hanabi.INSTANCE.fontManager.wqy16.drawString(this.v.getModeTitle(), this.x - this.animation / (float)(this.v.getModes().length * 20) * 5.0F, this.y - 2.0F - this.animation / (float)(this.v.getModes().length * 20) * 5.0F, (new Color(150, 150, 150)).getRGB());
      Hanabi.INSTANCE.fontManager.wqy18.drawString(this.v.getModeAt(this.v.getCurrentMode()), this.x + 5.0F, this.y + 5.0F, (new Color(120, 120, 120)).getRGB());
      Hanabi.INSTANCE.fontManager.wqy16.drawString("V", this.x + 55.0F, this.y + 4.0F, (new Color(200, 200, 200)).getRGB());
      float modY = this.y + 25.0F;
      String[] var4 = this.v.getModes();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String e = var4[var6];
         if (!e.equals(this.v.getModeAt(this.v.getCurrentMode()))) {
            if (modY <= this.y - 5.0F + this.animation) {
               Hanabi.INSTANCE.fontManager.wqy18.drawString(e, this.x + 5.0F, modY, (new Color(120, 120, 120)).getRGB());
            }

            modY += 20.0F;
         }
      }

      if (this.drag) {
         this.animation = this.animationUtils.animate((float)(this.v.getModes().length * 20), this.animation, 0.1F);
      } else {
         this.animation = this.animationUtils.animate(20.0F, this.animation, 0.1F);
      }

      super.draw(mouseX, mouseY);
   }

   public void mouseClicked(float mouseX, float mouseY) {
      super.mouseClicked(mouseX, mouseY);
      if (Main.isHovered(this.x, this.y - 5.0F, this.x + 65.0F, this.y + 15.0F, mouseX, mouseY)) {
         this.drag = !this.drag;
      }

      float modY = this.y + 25.0F;
      String[] var4 = this.v.getModes();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String e = var4[var6];
         if (!e.equals(this.v.getModeAt(this.v.getCurrentMode()))) {
            if (modY <= this.y - 5.0F + this.animation && Main.isHovered(this.x, modY, this.x + 65.0F, modY + 20.0F, mouseX, mouseY) && Mouse.isButtonDown(0)) {
               this.drag = false;
               this.v.setCurrentMode(this.v.getModeInt(e));
            }

            modY += 20.0F;
         }
      }

   }
}
