package org.newdawn.slick.particles.effects;

import org.newdawn.slick.Image;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

public class FireEmitter implements ParticleEmitter {
   private int x;
   private int y;
   private int interval = 50;
   private int timer;
   private float size = 40.0F;

   public FireEmitter() {
   }

   public FireEmitter(int var1, int var2) {
      this.x = var1;
      this.y = var2;
   }

   public FireEmitter(int var1, int var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.size = var3;
   }

   public void update(ParticleSystem var1, int var2) {
      this.timer -= var2;
      if (this.timer <= 0) {
         this.timer = this.interval;
         Particle var3 = var1.getNewParticle(this, 1000.0F);
         var3.setColor(1.0F, 1.0F, 1.0F, 0.5F);
         var3.setPosition((float)this.x, (float)this.y);
         var3.setSize(this.size);
         float var4 = (float)(-0.019999999552965164D + Math.random() * 0.03999999910593033D);
         float var5 = (float)(-(Math.random() * 0.15000000596046448D));
         var3.setVelocity(var4, var5, 1.1F);
      }

   }

   public void updateParticle(Particle var1, int var2) {
      if (var1.getLife() > 600.0F) {
         var1.adjustSize(0.07F * (float)var2);
      } else {
         var1.adjustSize(-0.04F * (float)var2 * (this.size / 40.0F));
      }

      float var3 = 0.002F * (float)var2;
      var1.adjustColor(0.0F, -var3 / 2.0F, -var3 * 2.0F, -var3 / 4.0F);
   }

   public boolean isEnabled() {
      return true;
   }

   public void setEnabled(boolean var1) {
   }

   public boolean completed() {
      return false;
   }

   public boolean useAdditive() {
      return false;
   }

   public Image getImage() {
      return null;
   }

   public boolean usePoints(ParticleSystem var1) {
      return false;
   }

   public boolean isOriented() {
      return false;
   }

   public void wrapUp() {
   }

   public void resetState() {
   }
}
