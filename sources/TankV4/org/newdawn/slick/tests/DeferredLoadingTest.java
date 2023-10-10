package org.newdawn.slick.tests;

import java.io.IOException;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;

public class DeferredLoadingTest extends BasicGame {
   private Music music;
   private Sound sound;
   private Image image;
   private Font font;
   private DeferredResource nextResource;
   private boolean started;

   public DeferredLoadingTest() {
      super("Deferred Loading Test");
   }

   public void init(GameContainer var1) throws SlickException {
      LoadingList.setDeferredLoading(true);
      new Sound("testdata/cbrown01.wav");
      new Sound("testdata/engine.wav");
      this.sound = new Sound("testdata/restart.ogg");
      new Music("testdata/testloop.ogg");
      this.music = new Music("testdata/SMB-X.XM");
      new Image("testdata/cursor.png");
      new Image("testdata/cursor.tga");
      new Image("testdata/cursor.png");
      new Image("testdata/cursor.png");
      new Image("testdata/dungeontiles.gif");
      new Image("testdata/logo.gif");
      this.image = new Image("testdata/logo.tga");
      new Image("testdata/logo.png");
      new Image("testdata/rocket.png");
      new Image("testdata/testpack.png");
      this.font = new AngelCodeFont("testdata/demo.fnt", "testdata/demo_00.tga");
   }

   public void render(GameContainer var1, Graphics var2) {
      if (this.nextResource != null) {
         var2.drawString("Loading: " + this.nextResource.getDescription(), 100.0F, 100.0F);
      }

      int var3 = LoadingList.get().getTotalResources();
      int var4 = LoadingList.get().getTotalResources() - LoadingList.get().getRemainingResources();
      float var5 = (float)var4 / (float)var3;
      var2.fillRect(100.0F, 150.0F, (float)(var4 * 40), 20.0F);
      var2.drawRect(100.0F, 150.0F, (float)(var3 * 40), 20.0F);
      if (this.started) {
         this.image.draw(100.0F, 200.0F);
         this.font.drawString(100.0F, 500.0F, "LOADING COMPLETE");
      }

   }

   public void update(GameContainer var1, int var2) throws SlickException {
      if (this.nextResource != null) {
         try {
            this.nextResource.load();

            try {
               Thread.sleep(50L);
            } catch (Exception var4) {
            }
         } catch (IOException var5) {
            throw new SlickException("Failed to load: " + this.nextResource.getDescription(), var5);
         }

         this.nextResource = null;
      }

      if (LoadingList.get().getRemainingResources() > 0) {
         this.nextResource = LoadingList.get().getNext();
      } else if (!this.started) {
         this.started = true;
         this.music.loop();
         this.sound.play();
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new DeferredLoadingTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }

   public void keyPressed(int var1, char var2) {
   }
}
