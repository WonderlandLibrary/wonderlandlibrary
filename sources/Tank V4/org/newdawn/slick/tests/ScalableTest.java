package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;

public class ScalableTest extends BasicGame {
   public ScalableTest() {
      super("Scalable Test For Widescreen");
   }

   public void init(GameContainer var1) throws SlickException {
   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.setColor(new Color(0.4F, 0.6F, 0.8F));
      var2.fillRect(0.0F, 0.0F, 1024.0F, 568.0F);
      var2.setColor(Color.white);
      var2.drawRect(5.0F, 5.0F, 1014.0F, 558.0F);
      var2.setColor(Color.white);
      var2.drawString(var1.getInput().getMouseX() + "," + var1.getInput().getMouseY(), 10.0F, 400.0F);
      var2.setColor(Color.red);
      var2.fillOval((float)(var1.getInput().getMouseX() - 10), (float)(var1.getInput().getMouseY() - 10), 20.0F, 20.0F);
   }

   public static void main(String[] var0) {
      try {
         ScalableGame var1 = new ScalableGame(new ScalableTest(), 1024, 568, true) {
            protected void renderOverlay(GameContainer var1, Graphics var2) {
               var2.setColor(Color.white);
               var2.drawString("Outside The Game", 350.0F, 10.0F);
               var2.drawString(var1.getInput().getMouseX() + "," + var1.getInput().getMouseY(), 400.0F, 20.0F);
            }
         };
         AppGameContainer var2 = new AppGameContainer(var1);
         var2.setDisplayMode(800, 600, false);
         var2.start();
      } catch (SlickException var3) {
         var3.printStackTrace();
      }

   }
}
