package org.newdawn.slick.tests;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class AnimationTest extends BasicGame {
   private Animation animation;
   private Animation limited;
   private Animation manual;
   private Animation pingPong;
   private GameContainer container;
   private int start = 5000;

   public AnimationTest() {
      super("Animation Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.container = var1;
      SpriteSheet var2 = new SpriteSheet("testdata/homeranim.png", 36, 65);
      this.animation = new Animation();

      int var3;
      for(var3 = 0; var3 < 8; ++var3) {
         this.animation.addFrame(var2.getSprite(var3, 0), 150);
      }

      this.limited = new Animation();

      for(var3 = 0; var3 < 8; ++var3) {
         this.limited.addFrame(var2.getSprite(var3, 0), 150);
      }

      this.limited.stopAt(7);
      this.manual = new Animation(false);

      for(var3 = 0; var3 < 8; ++var3) {
         this.manual.addFrame(var2.getSprite(var3, 0), 150);
      }

      this.pingPong = new Animation(var2, 0, 0, 7, 0, true, 150, true);
      this.pingPong.setPingPong(true);
      var1.getGraphics().setBackground(new Color(0.4F, 0.6F, 0.6F));
   }

   public void render(GameContainer var1, Graphics var2) {
      var2.drawString("Space to restart() animation", 100.0F, 50.0F);
      var2.drawString("Til Limited animation: " + this.start, 100.0F, 500.0F);
      var2.drawString("Hold 1 to move the manually animated", 100.0F, 70.0F);
      var2.drawString("PingPong Frame:" + this.pingPong.getFrame(), 600.0F, 70.0F);
      var2.scale(-1.0F, 1.0F);
      this.animation.draw(-100.0F, 100.0F);
      this.animation.draw(-200.0F, 100.0F, 144.0F, 260.0F);
      if (this.start < 0) {
         this.limited.draw(-400.0F, 100.0F, 144.0F, 260.0F);
      }

      this.manual.draw(-600.0F, 100.0F, 144.0F, 260.0F);
      this.pingPong.draw(-700.0F, 100.0F, 72.0F, 130.0F);
   }

   public void update(GameContainer var1, int var2) {
      if (var1.getInput().isKeyDown(2)) {
         this.manual.update((long)var2);
      }

      if (this.start >= 0) {
         this.start -= var2;
      }

   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new AnimationTest());
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

      if (var1 == 57) {
         this.limited.restart();
      }

   }
}
