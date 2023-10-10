package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class DoubleClickTest extends BasicGame {
   private String message = "Click or Double Click";

   public DoubleClickTest() {
      super("Double Click Test");
   }

   public void init(GameContainer var1) throws SlickException {
   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.drawString(this.message, 100.0F, 100.0F);
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new DoubleClickTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }

   public void mouseClicked(int var1, int var2, int var3, int var4) {
      if (var4 == 1) {
         this.message = "Single Click: " + var1 + " " + var2 + "," + var3;
      }

      if (var4 == 2) {
         this.message = "Double Click: " + var1 + " " + var2 + "," + var3;
      }

   }
}
