package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.BigImage;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class BigSpriteSheetTest extends BasicGame {
   private Image original;
   private SpriteSheet bigSheet;
   private boolean oldMethod = true;

   public BigSpriteSheetTest() {
      super("Big SpriteSheet Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.original = new BigImage("testdata/bigimage.tga", 2, 256);
      this.bigSheet = new SpriteSheet(this.original, 16, 16);
   }

   public void render(GameContainer var1, Graphics var2) {
      int var3;
      int var4;
      if (this.oldMethod) {
         for(var3 = 0; var3 < 43; ++var3) {
            for(var4 = 0; var4 < 27; ++var4) {
               this.bigSheet.getSprite(var3, var4).draw((float)(10 + var3 * 18), (float)(50 + var4 * 18));
            }
         }
      } else {
         this.bigSheet.startUse();

         for(var3 = 0; var3 < 43; ++var3) {
            for(var4 = 0; var4 < 27; ++var4) {
               this.bigSheet.renderInUse(10 + var3 * 18, 50 + var4 * 18, var3, var4);
            }
         }

         this.bigSheet.endUse();
      }

      var2.drawString("Press space to toggle rendering method", 10.0F, 30.0F);
      var1.getDefaultFont().drawString(10.0F, 100.0F, "TEST");
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new BigSpriteSheetTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }

   public void update(GameContainer var1, int var2) throws SlickException {
      if (var1.getInput().isKeyPressed(57)) {
         this.oldMethod = !this.oldMethod;
      }

   }
}
