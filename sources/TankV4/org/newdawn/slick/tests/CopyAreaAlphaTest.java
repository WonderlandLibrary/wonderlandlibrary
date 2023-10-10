package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class CopyAreaAlphaTest extends BasicGame {
   private Image textureMap;
   private Image copy;

   public CopyAreaAlphaTest() {
      super("CopyArea Alpha Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.textureMap = new Image("testdata/grass.png");
      var1.getGraphics().setBackground(Color.black);
      this.copy = new Image(100, 100);
   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.clearAlphaMap();
      var2.setDrawMode(Graphics.MODE_NORMAL);
      var2.setColor(Color.white);
      var2.fillOval(100.0F, 100.0F, 150.0F, 150.0F);
      this.textureMap.draw(10.0F, 50.0F);
      var2.copyArea(this.copy, 100, 100);
      var2.setColor(Color.red);
      var2.fillRect(300.0F, 100.0F, 200.0F, 200.0F);
      this.copy.draw(350.0F, 150.0F);
   }

   public void keyPressed(int var1, char var2) {
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new CopyAreaAlphaTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
