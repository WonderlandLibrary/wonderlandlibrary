package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.SpriteSheetFont;
import org.newdawn.slick.util.Log;

public class SpriteSheetFontTest extends BasicGame {
   private Font font;
   private static AppGameContainer container;

   public SpriteSheetFontTest() {
      super("SpriteSheetFont Test");
   }

   public void init(GameContainer var1) throws SlickException {
      SpriteSheet var2 = new SpriteSheet("testdata/spriteSheetFont.png", 32, 32);
      this.font = new SpriteSheetFont(var2, ' ');
   }

   public void render(GameContainer var1, Graphics var2) {
      var2.setBackground(Color.gray);
      this.font.drawString(80.0F, 5.0F, "A FONT EXAMPLE", Color.red);
      this.font.drawString(100.0F, 50.0F, "A MORE COMPLETE LINE");
   }

   public void update(GameContainer var1, int var2) throws SlickException {
   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 1) {
         System.exit(0);
      }

      if (var1 == 57) {
         try {
            container.setDisplayMode(640, 480, false);
         } catch (SlickException var4) {
            Log.error((Throwable)var4);
         }
      }

   }

   public static void main(String[] var0) {
      try {
         container = new AppGameContainer(new SpriteSheetFontTest());
         container.setDisplayMode(800, 600, false);
         container.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
