package cn.hanabi.gui.particles;

import cn.hanabi.utils.ParticleRenderUtils;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

class Particle {
   public final float size;
   private final float ySpeed = (float)(new Random()).nextInt(5);
   private final float xSpeed = (float)(new Random()).nextInt(5);
   public float x;
   public float y;
   private int height;
   private int width;

   Particle(int x, int y) {
      this.x = (float)x;
      this.y = (float)y;
      this.size = this.genRandom();
   }

   private float lint1(float f) {
      return 1.02F * (1.0F - f) + 1.0F * f;
   }

   private float lint2(float f) {
      return 1.02F + f * -0.01999998F;
   }

   void connect(float x, float y) {
      ParticleRenderUtils.connectPoints(this.getX(), this.getY(), x, y);
   }

   public int getHeight() {
      return this.height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public float getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = (float)x;
   }

   public float getY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = (float)y;
   }

   void interpolation() {
      for(int n = 0; n <= 64; ++n) {
         float f = (float)n / 64.0F;
         float p1 = this.lint1(f);
         float p2 = this.lint2(f);
         if (p1 != p2) {
            this.y -= f;
            this.x -= f;
         }
      }

   }

   void fall() {
      Minecraft mc = Minecraft.getMinecraft();
      ScaledResolution scaledResolution = new ScaledResolution(mc);
      this.y += this.ySpeed;
      this.x += this.xSpeed;
      if (this.y > (float)mc.displayHeight) {
         this.y = 1.0F;
      }

      if (this.x > (float)mc.displayWidth) {
         this.x = 1.0F;
      }

      if (this.x < 1.0F) {
         this.x = (float)scaledResolution.getScaledWidth();
      }

      if (this.y < 1.0F) {
         this.y = (float)scaledResolution.getScaledHeight();
      }

   }

   private float genRandom() {
      return (float)(0.30000001192092896 + Math.random() * 1.2999999523162842);
   }
}
