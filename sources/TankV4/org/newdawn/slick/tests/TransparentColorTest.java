package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class TransparentColorTest extends BasicGame {
   private Image image;
   private Image timage;

   public TransparentColorTest() {
      super("Transparent Color Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.image = new Image("testdata/transtest.png");
      this.timage = new Image("testdata/transtest.png", new Color(94, 66, 41, 255));
   }

   public void render(GameContainer var1, Graphics var2) {
      var2.setBackground(Color.red);
      this.image.draw(10.0F, 10.0F);
      this.timage.draw(10.0F, 310.0F);
   }

   public void update(GameContainer var1, int var2) {
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new TransparentColorTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }

   public void keyPressed(int var1, char var2) {
   }
}
