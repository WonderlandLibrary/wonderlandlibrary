package org.newdawn.slick.tests;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.opengl.SlickCallable;

public class SlickCallableTest extends BasicGame {
   private Image image;
   private Image back;
   private float rot;
   private AngelCodeFont font;
   private Animation homer;

   public SlickCallableTest() {
      super("Slick Callable Test");
   }

   public void init(GameContainer var1) throws SlickException {
      this.image = new Image("testdata/rocket.png");
      this.back = new Image("testdata/sky.jpg");
      this.font = new AngelCodeFont("testdata/hiero.fnt", "testdata/hiero.png");
      SpriteSheet var2 = new SpriteSheet("testdata/homeranim.png", 36, 65);
      this.homer = new Animation(var2, 0, 0, 7, 0, true, 150, true);
   }

   public void render(GameContainer var1, Graphics var2) throws SlickException {
      var2.scale(2.0F, 2.0F);
      var2.fillRect(0.0F, 0.0F, 800.0F, 600.0F, this.back, 0.0F, 0.0F);
      var2.resetTransform();
      var2.drawImage(this.image, 100.0F, 100.0F);
      this.image.draw(100.0F, 200.0F, 80.0F, 200.0F);
      this.font.drawString(100.0F, 200.0F, "Text Drawn before the callable");
      SlickCallable var3 = new SlickCallable(this) {
         private final SlickCallableTest this$0;

         {
            this.this$0 = var1;
         }

         protected void performGLOperations() throws SlickException {
            this.this$0.renderGL();
         }
      };
      var3.call();
      this.homer.draw(450.0F, 250.0F, 80.0F, 200.0F);
      this.font.drawString(150.0F, 300.0F, "Text Drawn after the callable");
   }

   public void renderGL() {
      FloatBuffer var1 = BufferUtils.createFloatBuffer(4);
      var1.put(new float[]{5.0F, 5.0F, 10.0F, 0.0F}).flip();
      FloatBuffer var2 = BufferUtils.createFloatBuffer(4);
      var2.put(new float[]{0.8F, 0.1F, 0.0F, 1.0F}).flip();
      GL11.glLight(16384, 4611, (FloatBuffer)var1);
      GL11.glEnable(16384);
      GL11.glEnable(2884);
      GL11.glEnable(2929);
      GL11.glEnable(2896);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      float var3 = 0.75F;
      GL11.glFrustum(-1.0D, 1.0D, (double)(-var3), (double)var3, 5.0D, 60.0D);
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      GL11.glTranslatef(0.0F, 0.0F, -40.0F);
      GL11.glRotatef(this.rot, 0.0F, 1.0F, 1.0F);
      GL11.glMaterial(1028, 5634, (FloatBuffer)var2);
      this.gear(0.5F, 2.0F, 2.0F, 10, 0.7F);
   }

   private void gear(float var1, float var2, float var3, int var4, float var5) {
      float var7 = var1;
      float var8 = var2 - var5 / 2.0F;
      float var9 = var2 + var5 / 2.0F;
      float var11 = 6.2831855F / (float)var4 / 4.0F;
      GL11.glShadeModel(7424);
      GL11.glNormal3f(0.0F, 0.0F, 1.0F);
      GL11.glBegin(8);

      int var6;
      float var10;
      for(var6 = 0; var6 <= var4; ++var6) {
         var10 = (float)var6 * 2.0F * 3.1415927F / (float)var4;
         GL11.glVertex3f(var7 * (float)Math.cos((double)var10), var7 * (float)Math.sin((double)var10), var3 * 0.5F);
         GL11.glVertex3f(var8 * (float)Math.cos((double)var10), var8 * (float)Math.sin((double)var10), var3 * 0.5F);
         if (var6 < var4) {
            GL11.glVertex3f(var7 * (float)Math.cos((double)var10), var7 * (float)Math.sin((double)var10), var3 * 0.5F);
            GL11.glVertex3f(var8 * (float)Math.cos((double)(var10 + 3.0F * var11)), var8 * (float)Math.sin((double)(var10 + 3.0F * var11)), var3 * 0.5F);
         }
      }

      GL11.glEnd();
      GL11.glBegin(7);

      for(var6 = 0; var6 < var4; ++var6) {
         var10 = (float)var6 * 2.0F * 3.1415927F / (float)var4;
         GL11.glVertex3f(var8 * (float)Math.cos((double)var10), var8 * (float)Math.sin((double)var10), var3 * 0.5F);
         GL11.glVertex3f(var9 * (float)Math.cos((double)(var10 + var11)), var9 * (float)Math.sin((double)(var10 + var11)), var3 * 0.5F);
         GL11.glVertex3f(var9 * (float)Math.cos((double)(var10 + 2.0F * var11)), var9 * (float)Math.sin((double)(var10 + 2.0F * var11)), var3 * 0.5F);
         GL11.glVertex3f(var8 * (float)Math.cos((double)(var10 + 3.0F * var11)), var8 * (float)Math.sin((double)(var10 + 3.0F * var11)), var3 * 0.5F);
      }

      GL11.glEnd();
      GL11.glNormal3f(0.0F, 0.0F, -1.0F);
      GL11.glBegin(8);

      for(var6 = 0; var6 <= var4; ++var6) {
         var10 = (float)var6 * 2.0F * 3.1415927F / (float)var4;
         GL11.glVertex3f(var8 * (float)Math.cos((double)var10), var8 * (float)Math.sin((double)var10), -var3 * 0.5F);
         GL11.glVertex3f(var7 * (float)Math.cos((double)var10), var7 * (float)Math.sin((double)var10), -var3 * 0.5F);
         GL11.glVertex3f(var8 * (float)Math.cos((double)(var10 + 3.0F * var11)), var8 * (float)Math.sin((double)(var10 + 3.0F * var11)), -var3 * 0.5F);
         GL11.glVertex3f(var7 * (float)Math.cos((double)var10), var7 * (float)Math.sin((double)var10), -var3 * 0.5F);
      }

      GL11.glEnd();
      GL11.glBegin(7);

      for(var6 = 0; var6 < var4; ++var6) {
         var10 = (float)var6 * 2.0F * 3.1415927F / (float)var4;
         GL11.glVertex3f(var8 * (float)Math.cos((double)(var10 + 3.0F * var11)), var8 * (float)Math.sin((double)(var10 + 3.0F * var11)), -var3 * 0.5F);
         GL11.glVertex3f(var9 * (float)Math.cos((double)(var10 + 2.0F * var11)), var9 * (float)Math.sin((double)(var10 + 2.0F * var11)), -var3 * 0.5F);
         GL11.glVertex3f(var9 * (float)Math.cos((double)(var10 + var11)), var9 * (float)Math.sin((double)(var10 + var11)), -var3 * 0.5F);
         GL11.glVertex3f(var8 * (float)Math.cos((double)var10), var8 * (float)Math.sin((double)var10), -var3 * 0.5F);
      }

      GL11.glEnd();
      GL11.glNormal3f(0.0F, 0.0F, 1.0F);
      GL11.glBegin(8);

      for(var6 = 0; var6 < var4; ++var6) {
         var10 = (float)var6 * 2.0F * 3.1415927F / (float)var4;
         GL11.glVertex3f(var8 * (float)Math.cos((double)var10), var8 * (float)Math.sin((double)var10), var3 * 0.5F);
         GL11.glVertex3f(var8 * (float)Math.cos((double)var10), var8 * (float)Math.sin((double)var10), -var3 * 0.5F);
         float var12 = var9 * (float)Math.cos((double)(var10 + var11)) - var8 * (float)Math.cos((double)var10);
         float var13 = var9 * (float)Math.sin((double)(var10 + var11)) - var8 * (float)Math.sin((double)var10);
         float var14 = (float)Math.sqrt((double)(var12 * var12 + var13 * var13));
         var12 /= var14;
         var13 /= var14;
         GL11.glNormal3f(var13, -var12, 0.0F);
         GL11.glVertex3f(var9 * (float)Math.cos((double)(var10 + var11)), var9 * (float)Math.sin((double)(var10 + var11)), var3 * 0.5F);
         GL11.glVertex3f(var9 * (float)Math.cos((double)(var10 + var11)), var9 * (float)Math.sin((double)(var10 + var11)), -var3 * 0.5F);
         GL11.glNormal3f((float)Math.cos((double)var10), (float)Math.sin((double)var10), 0.0F);
         GL11.glVertex3f(var9 * (float)Math.cos((double)(var10 + 2.0F * var11)), var9 * (float)Math.sin((double)(var10 + 2.0F * var11)), var3 * 0.5F);
         GL11.glVertex3f(var9 * (float)Math.cos((double)(var10 + 2.0F * var11)), var9 * (float)Math.sin((double)(var10 + 2.0F * var11)), -var3 * 0.5F);
         var12 = var8 * (float)Math.cos((double)(var10 + 3.0F * var11)) - var9 * (float)Math.cos((double)(var10 + 2.0F * var11));
         var13 = var8 * (float)Math.sin((double)(var10 + 3.0F * var11)) - var9 * (float)Math.sin((double)(var10 + 2.0F * var11));
         GL11.glNormal3f(var13, -var12, 0.0F);
         GL11.glVertex3f(var8 * (float)Math.cos((double)(var10 + 3.0F * var11)), var8 * (float)Math.sin((double)(var10 + 3.0F * var11)), var3 * 0.5F);
         GL11.glVertex3f(var8 * (float)Math.cos((double)(var10 + 3.0F * var11)), var8 * (float)Math.sin((double)(var10 + 3.0F * var11)), -var3 * 0.5F);
         GL11.glNormal3f((float)Math.cos((double)var10), (float)Math.sin((double)var10), 0.0F);
      }

      GL11.glVertex3f(var8 * (float)Math.cos(0.0D), var8 * (float)Math.sin(0.0D), var3 * 0.5F);
      GL11.glVertex3f(var8 * (float)Math.cos(0.0D), var8 * (float)Math.sin(0.0D), -var3 * 0.5F);
      GL11.glEnd();
      GL11.glShadeModel(7425);
      GL11.glBegin(8);

      for(var6 = 0; var6 <= var4; ++var6) {
         var10 = (float)var6 * 2.0F * 3.1415927F / (float)var4;
         GL11.glNormal3f(-((float)Math.cos((double)var10)), -((float)Math.sin((double)var10)), 0.0F);
         GL11.glVertex3f(var7 * (float)Math.cos((double)var10), var7 * (float)Math.sin((double)var10), -var3 * 0.5F);
         GL11.glVertex3f(var7 * (float)Math.cos((double)var10), var7 * (float)Math.sin((double)var10), var3 * 0.5F);
      }

      GL11.glEnd();
   }

   public void update(GameContainer var1, int var2) {
      this.rot += (float)var2 * 0.1F;
   }

   public static void main(String[] var0) {
      try {
         AppGameContainer var1 = new AppGameContainer(new SlickCallableTest());
         var1.setDisplayMode(800, 600, false);
         var1.start();
      } catch (SlickException var2) {
         var2.printStackTrace();
      }

   }
}
