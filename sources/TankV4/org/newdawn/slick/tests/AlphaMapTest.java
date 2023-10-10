package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class AlphaMapTest extends BasicGame {
   private Image alphaMap;
   private Image textureMap;

   public AlphaMapTest() {
      super("AlphaMap Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.alphaMap = new Image("testdata/alphamap.png");
      this.textureMap = new Image("testdata/grass.png");
      var1.getGraphics().setBackground(Color.black);
   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.clearAlphaMap();
      var2.setDrawMode(Graphics.MODE_NORMAL);
      this.textureMap.draw(10.0F, 50.0F);
      var2.setColor(Color.red);
      var2.fillRect(290.0F, 40.0F, 200.0F, 200.0F);
      var2.setColor(Color.white);
      var2.setDrawMode(Graphics.MODE_ALPHA_MAP);
      this.alphaMap.draw(300.0F, 50.0F);
      var2.setDrawMode(Graphics.MODE_ALPHA_BLEND);
      this.textureMap.draw(300.0F, 50.0F);
      var2.setDrawMode(Graphics.MODE_NORMAL);
   }

   public void keyPressed(int var1, char var2) {
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new AlphaMapTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
