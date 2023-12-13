package org.newdawn.slick.tests;

import java.util.ArrayList;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class FontPerformanceTest extends BasicGame {
   private AngelCodeFont font;
   private String text = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin bibendum. Aliquam ac sapien a elit congue iaculis. Quisque et justo quis mi mattis euismod. Donec elementum, mi quis aliquet varius, nisi leo volutpat magna, quis ultricies eros augue at risus. Integer non magna at lorem sodales molestie. Integer diam nulla, ornare sit amet, mattis quis, euismod et, mauris. Proin eget tellus non nisl mattis laoreet. Nunc at nunc id elit pretium tempor. Duis vulputate, nibh eget rhoncus eleifend, tellus lectus sollicitudin mi, rhoncus tincidunt nisi massa vitae ipsum. Praesent tellus diam, luctus ut, eleifend nec, auctor et, orci. Praesent eu elit. Pellentesque ante orci, volutpat placerat, ornare eget, cursus sit amet, eros. Duis pede sapien, euismod a, volutpat pellentesque, convallis eu, mauris. Nunc eros. Ut eu risus et felis laoreet viverra. Curabitur a metus.";
   private ArrayList lines = new ArrayList();
   private boolean visible = true;

   public FontPerformanceTest() {
      super("Font Performance Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.font = new AngelCodeFont("testdata/perffont.fnt", "testdata/perffont.png");

      for(int var2 = 0; var2 < 2; ++var2) {
         int var3 = 90;

         for(int var4 = 0; var4 < this.text.length(); var4 += var3) {
            if (var4 + var3 > this.text.length()) {
               var3 = this.text.length() - var4;
            }

            this.lines.add(this.text.substring(var4, var4 + var3));
         }

         this.lines.add("");
      }

   }

   public void render(GameContainer var1, Graphics var2) {
      var2.setFont(this.font);
      if (this.visible) {
         for(int var3 = 0; var3 < this.lines.size(); ++var3) {
            this.font.drawString(10.0F, (float)(50 + var3 * 20), (String)this.lines.get(var3), var3 > 10 ? Color.red : Color.green);
         }
      }

   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 1) {
         System.exit(0);
      }

      if (var1 == 57) {
         this.visible = !this.visible;
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new FontPerformanceTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
