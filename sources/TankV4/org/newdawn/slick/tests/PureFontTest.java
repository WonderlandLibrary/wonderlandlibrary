package org.newdawn.slick.tests;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class PureFontTest extends BasicGame {
   private Font font;
   private Image image;
   private static AppGameContainer container;

   public PureFontTest() {
      super("Hiero Font Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.image = new Image("testdata/sky.jpg");
      this.font = new AngelCodeFont("testdata/hiero.fnt", "testdata/hiero.png");
   }

   public void render(GameContainer var1, Graphics var2) {
      this.image.draw(0.0F, 0.0F, 800.0F, 600.0F);
      this.font.drawString(100.0F, 32.0F, "On top of old smokey, all");
      this.font.drawString(100.0F, 80.0F, "covered with sand..");
   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 1) {
         System.exit(0);
      }

   }

   public static void main(String[] var0) {
      try {
         container = new AppGameContainer(new PureFontTest());
         container.setDisplayMode(800, 600, false);
         container.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
