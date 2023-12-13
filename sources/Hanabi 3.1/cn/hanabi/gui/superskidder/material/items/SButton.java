package cn.hanabi.gui.superskidder.material.items;

import cn.hanabi.gui.superskidder.material.items.other.Shadow;
import java.util.ArrayList;

public class SButton {
   public double x;
   public double y;
   public double width;
   public double height;
   public ArrayList ss = new ArrayList();
   public int color;

   public SButton(double x, double y, double width, double height, int color) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.color = color;
   }

   public void draw(double mouseX, double mouseY, int mouseButton) {
   }

   public void update() {
      for(int i = 0; i < this.ss.size(); ++i) {
         Shadow sha = (Shadow)this.ss.get(i);
         sha.update();
         if (sha.delete) {
            this.ss.remove(i);
         }

         if (this.ss.size() > 8) {
            this.ss.remove(0);
         }
      }

   }

   public void onClicked(double mouseX, double mouseY, int mouseButton) {
   }
}
