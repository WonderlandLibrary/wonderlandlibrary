package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class AntiAliasTest extends BasicGame {
   public AntiAliasTest() {
      super("AntiAlias Test");
   }

   public void init(GameContainer var1) throws SlickException {
      var1.getGraphics().setBackground(Color.green);
   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.setAntiAlias(true);
      var2.setColor(Color.red);
      var2.drawOval(100.0F, 100.0F, 100.0F, 100.0F);
      var2.fillOval(300.0F, 100.0F, 100.0F, 100.0F);
      var2.setAntiAlias(false);
      var2.setColor(Color.red);
      var2.drawOval(100.0F, 300.0F, 100.0F, 100.0F);
      var2.fillOval(300.0F, 300.0F, 100.0F, 100.0F);
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new AntiAliasTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
