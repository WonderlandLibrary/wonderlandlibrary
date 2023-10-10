package org.newdawn.slick.tests;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

public class FontTest extends BasicGame {
   private AngelCodeFont font;
   private AngelCodeFont font2;
   private Image image;
   private static AppGameContainer container;

   public FontTest() {
      super("Font Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.font = new AngelCodeFont("testdata/demo2.fnt", "testdata/demo2_00.tga");
      this.font2 = new AngelCodeFont("testdata/hiero.fnt", "testdata/hiero.png");
      this.image = new Image("testdata/demo2_00.tga", false);
   }

   public void render(GameContainer var1, Graphics var2) {
      this.font.drawString(80.0F, 5.0F, "A Font Example", Color.red);
      this.font.drawString(100.0F, 32.0F, "We - AV - Here is a more complete line that hopefully");
      this.font.drawString(100.0F, (float)(36 + this.font.getHeight("We Here is a more complete line that hopefully")), "will show some kerning.");
      this.font2.drawString(80.0F, 85.0F, "A Font Example", Color.red);
      this.font2.drawString(100.0F, 132.0F, "We - AV - Here is a more complete line that hopefully");
      this.font2.drawString(100.0F, (float)(136 + this.font2.getHeight("We - Here is a more complete line that hopefully")), "will show some kerning.");
      this.image.draw(100.0F, 400.0F);
      String var3 = "Testing Font";
      this.font2.drawString(100.0F, 300.0F, var3);
      var2.setColor(Color.white);
      var2.drawRect(100.0F, (float)(300 + this.font2.getYOffset(var3)), (float)this.font2.getWidth(var3), (float)(this.font2.getHeight(var3) - this.font2.getYOffset(var3)));
      this.font.drawString(500.0F, 300.0F, var3);
      var2.setColor(Color.white);
      var2.drawRect(500.0F, (float)(300 + this.font.getYOffset(var3)), (float)this.font.getWidth(var3), (float)(this.font.getHeight(var3) - this.font.getYOffset(var3)));
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
         container = new AppGameContainer(new FontTest());
         container.setDisplayMode(800, 600, false);
         container.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
