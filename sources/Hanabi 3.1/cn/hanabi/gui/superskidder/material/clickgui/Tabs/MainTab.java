package cn.hanabi.gui.superskidder.material.clickgui.Tabs;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.superskidder.material.clickgui.Main;
import cn.hanabi.gui.superskidder.material.clickgui.Tab;
import java.awt.Color;

public class MainTab extends Tab {
   public MainTab() {
      this.name = "Main Menu";
   }

   public void render(float mouseX, float mouseY) {
      super.render(mouseX, mouseY);
      Hanabi.INSTANCE.fontManager.raleway25.drawString("Welcome!", Main.windowX + Main.animListX + 50.0F, Main.windowY + 100.0F, (new Color(50, 50, 50)).getRGB());
      float width = (float)Hanabi.INSTANCE.fontManager.raleway25.getStringWidth("Welcome!");
   }

   public void mouseClicked(float mouseX, float mouseY) {
      super.mouseClicked(mouseX, mouseY);
   }
}
