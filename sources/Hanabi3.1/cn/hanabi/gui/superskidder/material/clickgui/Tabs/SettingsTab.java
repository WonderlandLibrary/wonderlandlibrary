package cn.hanabi.gui.superskidder.material.clickgui.Tabs;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.superskidder.material.clickgui.Main;
import cn.hanabi.gui.superskidder.material.clickgui.Tab;
import cn.hanabi.gui.superskidder.material.clickgui.button.Button;
import cn.hanabi.gui.superskidder.material.clickgui.button.values.BMode;
import cn.hanabi.gui.superskidder.material.clickgui.button.values.BNumbers;
import cn.hanabi.gui.superskidder.material.clickgui.button.values.BOption;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.render.HUD;
import cn.hanabi.value.Value;
import java.util.ArrayList;
import java.util.Iterator;

public class SettingsTab extends Tab {
   private ArrayList btns = new ArrayList();
   float startX;
   float startY;

   public SettingsTab() {
      this.startX = Main.windowX + 20.0F;
      this.startY = Main.windowY + 70.0F;
      this.name = "Settings";
      Iterator var1 = Value.getValue(ModManager.getModule(HUD.class)).iterator();

      while(true) {
         while(var1.hasNext()) {
            Value v = (Value)var1.next();
            if (v.isValueBoolean) {
               Button value = new BOption(this.startX, this.startY, v, this);
               this.btns.add(value);
            } else if (!v.isValueByte && !v.isValueLong && !v.isValueDouble && !v.isValueFloat) {
               if (v.isValueMode) {
                  Button value = new BMode(this.startX, this.startY, v, this);
                  this.btns.add(value);
               }
            } else {
               Button value = new BNumbers(this.startX, this.startY, v, this);
               this.btns.add(value);
            }
         }

         return;
      }
   }

   public void render(float mouseX, float mouseY) {
      this.startX = Main.windowX + 20.0F + Main.animListX;
      this.startY = Main.windowY + 70.0F;
      Iterator var3 = this.btns.iterator();

      while(var3.hasNext()) {
         Button v = (Button)var3.next();
         v.x = this.startX;
         v.y = this.startY;
         v.draw(mouseX, mouseY);
         if (this.startX + 100.0F + (float)Hanabi.INSTANCE.fontManager.wqy18.getStringWidth(v.v.getName()) < Main.windowX + Main.windowWidth) {
            if (v instanceof BOption) {
               this.startX += (float)(40 + Hanabi.INSTANCE.fontManager.wqy18.getStringWidth(v.v.getName()));
            } else {
               this.startX += 80.0F;
            }
         } else {
            this.startX = Main.windowX + 20.0F + Main.animListX;
            this.startY += 30.0F;
         }
      }

   }

   public void mouseClicked(float mouseX, float mouseY) {
      super.mouseClicked(mouseX, mouseY);
      this.startX = Main.windowX + 20.0F + Main.animListX;
      this.startY = Main.windowY + 70.0F;
      Iterator var3 = this.btns.iterator();

      while(var3.hasNext()) {
         Button v = (Button)var3.next();
         v.mouseClicked(mouseX, mouseY);
      }

   }
}
