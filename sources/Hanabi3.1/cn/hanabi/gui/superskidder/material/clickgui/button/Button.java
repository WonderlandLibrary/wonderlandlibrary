package cn.hanabi.gui.superskidder.material.clickgui.button;

import cn.hanabi.gui.superskidder.material.clickgui.AnimationUtils;
import cn.hanabi.gui.superskidder.material.clickgui.Main;
import cn.hanabi.gui.superskidder.material.clickgui.Tab;
import cn.hanabi.value.Value;

public class Button {
   public float x;
   public float y;
   public Value v;
   public float animation;
   public Runnable event;
   public boolean drag;
   public AnimationUtils animationUtils = new AnimationUtils();
   public Tab tab;

   public Button(float x, float y, Value v, Tab moduleTab) {
      this.x = x;
      this.y = y;
      this.v = v;
      this.tab = moduleTab;
   }

   public void drawButton(float mouseX, float mouseY) {
   }

   public void draw(float mouseX, float mouseY) {
      this.drawButton(mouseX, mouseY);
   }

   public void mouseClicked(float mouseX, float mouseY) {
   }

   public void mouseClick(float mouseX, float mouseY) {
      if (Main.currentTab == this.tab) {
         this.mouseClicked(mouseX, mouseY);
      }
   }
}
