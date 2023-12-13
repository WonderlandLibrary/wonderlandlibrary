package org.newdawn.slick.tests;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.PackedSpriteSheet;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class PackedSheetTest extends BasicGame {
   private PackedSpriteSheet sheet;
   private GameContainer container;
   private float r = -500.0F;
   private Image rocket;
   private Animation runner;
   private float ang;

   public PackedSheetTest() {
      super("Packed Sprite Sheet Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.container = var1;
      this.sheet = new PackedSpriteSheet("testdata/testpack.def", 2);
      this.rocket = this.sheet.getSprite("rocket");
      SpriteSheet var2 = this.sheet.getSpriteSheet("runner");
      this.runner = new Animation();

      for(int var3 = 0; var3 < 2; ++var3) {
         for(int var4 = 0; var4 < 6; ++var4) {
            this.runner.addFrame(var2.getSprite(var4, var3), 50);
         }
      }

   }

   public void render(GameContainer var1, Graphics var2) {
      this.rocket.draw((float)((int)this.r), 100.0F);
      this.runner.draw(250.0F, 250.0F);
      var2.scale(1.2F, 1.2F);
      this.runner.draw(250.0F, 250.0F);
      var2.scale(1.2F, 1.2F);
      this.runner.draw(250.0F, 250.0F);
      var2.resetTransform();
      var2.rotate(670.0F, 470.0F, this.ang);
      this.sheet.getSprite("floppy").draw(600.0F, 400.0F);
   }

   public void update(GameContainer var1, int var2) {
      this.r += (float)var2 * 0.4F;
      if (this.r > 900.0F) {
         this.r = -500.0F;
      }

      this.ang += (float)var2 * 0.1F;
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new PackedSheetTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }

   public void keyPressed(int var1, char var2) {
      if (var1 == 1) {
         this.container.exit();
      }

   }
}
