package org.newdawn.slick.particles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.FastTrig;
import org.newdawn.slick.util.Log;

public class ConfigurableEmitter implements ParticleEmitter {
   private static String relativePath = "";
   public ConfigurableEmitter.Range spawnInterval = new ConfigurableEmitter.Range(this, 100.0F, 100.0F);
   public ConfigurableEmitter.Range spawnCount = new ConfigurableEmitter.Range(this, 5.0F, 5.0F);
   public ConfigurableEmitter.Range initialLife = new ConfigurableEmitter.Range(this, 1000.0F, 1000.0F);
   public ConfigurableEmitter.Range initialSize = new ConfigurableEmitter.Range(this, 10.0F, 10.0F);
   public ConfigurableEmitter.Range xOffset = new ConfigurableEmitter.Range(this, 0.0F, 0.0F);
   public ConfigurableEmitter.Range yOffset = new ConfigurableEmitter.Range(this, 0.0F, 0.0F);
   public ConfigurableEmitter.RandomValue spread = new ConfigurableEmitter.RandomValue(this, 360.0F);
   public ConfigurableEmitter.Value angularOffset = new ConfigurableEmitter.SimpleValue(this, 0.0F);
   public ConfigurableEmitter.Range initialDistance = new ConfigurableEmitter.Range(this, 0.0F, 0.0F);
   public ConfigurableEmitter.Range speed = new ConfigurableEmitter.Range(this, 50.0F, 50.0F);
   public ConfigurableEmitter.Value growthFactor = new ConfigurableEmitter.SimpleValue(this, 0.0F);
   public ConfigurableEmitter.Value gravityFactor = new ConfigurableEmitter.SimpleValue(this, 0.0F);
   public ConfigurableEmitter.Value windFactor = new ConfigurableEmitter.SimpleValue(this, 0.0F);
   public ConfigurableEmitter.Range length = new ConfigurableEmitter.Range(this, 1000.0F, 1000.0F);
   public ArrayList colors = new ArrayList();
   public ConfigurableEmitter.Value startAlpha = new ConfigurableEmitter.SimpleValue(this, 255.0F);
   public ConfigurableEmitter.Value endAlpha = new ConfigurableEmitter.SimpleValue(this, 0.0F);
   public ConfigurableEmitter.LinearInterpolator alpha;
   public ConfigurableEmitter.LinearInterpolator size;
   public ConfigurableEmitter.LinearInterpolator velocity;
   public ConfigurableEmitter.LinearInterpolator scaleY;
   public ConfigurableEmitter.Range emitCount = new ConfigurableEmitter.Range(this, 1000.0F, 1000.0F);
   public int usePoints = 1;
   public boolean useOriented = false;
   public boolean useAdditive = false;
   public String name;
   public String imageName = "";
   private Image image;
   private boolean updateImage;
   private boolean enabled = true;
   private float x;
   private float y;
   private int nextSpawn = 0;
   private int timeout;
   private int particleCount;
   private ParticleSystem engine;
   private int leftToEmit;
   private boolean wrapUp = false;
   private boolean completed = false;

   public static void setRelativePath(String var0) {
      if (!var0.endsWith("/")) {
         var0 = var0 + "/";
      }

      relativePath = var0;
   }

   public ConfigurableEmitter(String var1) {
      this.name = var1;
      this.leftToEmit = (int)this.emitCount.random();
      this.timeout = (int)this.length.random();
      this.colors.add(new ConfigurableEmitter.ColorRecord(this, 0.0F, Color.white));
      this.colors.add(new ConfigurableEmitter.ColorRecord(this, 1.0F, Color.red));
      ArrayList var2 = new ArrayList();
      var2.add(new Vector2f(0.0F, 0.0F));
      var2.add(new Vector2f(1.0F, 255.0F));
      this.alpha = new ConfigurableEmitter.LinearInterpolator(this, var2, 0, 255);
      var2 = new ArrayList();
      var2.add(new Vector2f(0.0F, 0.0F));
      var2.add(new Vector2f(1.0F, 255.0F));
      this.size = new ConfigurableEmitter.LinearInterpolator(this, var2, 0, 255);
      var2 = new ArrayList();
      var2.add(new Vector2f(0.0F, 0.0F));
      var2.add(new Vector2f(1.0F, 1.0F));
      this.velocity = new ConfigurableEmitter.LinearInterpolator(this, var2, 0, 1);
      var2 = new ArrayList();
      var2.add(new Vector2f(0.0F, 0.0F));
      var2.add(new Vector2f(1.0F, 1.0F));
      this.scaleY = new ConfigurableEmitter.LinearInterpolator(this, var2, 0, 1);
   }

   public void setImageName(String var1) {
      if (var1.length() == 0) {
         var1 = null;
      }

      this.imageName = var1;
      if (var1 == null) {
         this.image = null;
      } else {
         this.updateImage = true;
      }

   }

   public String getImageName() {
      return this.imageName;
   }

   public String toString() {
      return "[" + this.name + "]";
   }

   public void setPosition(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public void update(ParticleSystem var1, int var2) {
      this.engine = var1;
      if (this.updateImage) {
         this.updateImage = false;

         try {
            this.image = new Image(relativePath + this.imageName);
         } catch (SlickException var14) {
            this.image = null;
            Log.error((Throwable)var14);
         }
      }

      if ((this.wrapUp || this.length.isEnabled() && this.timeout < 0 || this.emitCount.isEnabled() && this.leftToEmit <= 0) && this.particleCount == 0) {
         this.completed = true;
      }

      this.particleCount = 0;
      if (!this.wrapUp) {
         if (this.length.isEnabled()) {
            if (this.timeout < 0) {
               return;
            }

            this.timeout -= var2;
         }

         if (!this.emitCount.isEnabled() || this.leftToEmit > 0) {
            this.nextSpawn -= var2;
            if (this.nextSpawn < 0) {
               this.nextSpawn = (int)this.spawnInterval.random();
               int var3 = (int)this.spawnCount.random();

               for(int var4 = 0; var4 < var3; ++var4) {
                  Particle var5 = var1.getNewParticle(this, this.initialLife.random());
                  var5.setSize(this.initialSize.random());
                  var5.setPosition(this.x + this.xOffset.random(), this.y + this.yOffset.random());
                  var5.setVelocity(0.0F, 0.0F, 0.0F);
                  float var6 = this.initialDistance.random();
                  float var7 = this.speed.random();
                  if (var6 != 0.0F || var7 != 0.0F) {
                     float var8 = this.spread.getValue(0.0F);
                     float var9 = var8 + this.angularOffset.getValue(0.0F) - this.spread.getValue() / 2.0F - 90.0F;
                     float var10 = (float)FastTrig.cos(Math.toRadians((double)var9)) * var6;
                     float var11 = (float)FastTrig.sin(Math.toRadians((double)var9)) * var6;
                     var5.adjustPosition(var10, var11);
                     float var12 = (float)FastTrig.cos(Math.toRadians((double)var9));
                     float var13 = (float)FastTrig.sin(Math.toRadians((double)var9));
                     var5.setVelocity(var12, var13, var7 * 0.001F);
                  }

                  if (this.image != null) {
                     var5.setImage(this.image);
                  }

                  ConfigurableEmitter.ColorRecord var15 = (ConfigurableEmitter.ColorRecord)this.colors.get(0);
                  var5.setColor(var15.col.r, var15.col.g, var15.col.b, this.startAlpha.getValue(0.0F) / 255.0F);
                  var5.setUsePoint(this.usePoints);
                  var5.setOriented(this.useOriented);
                  if (this.emitCount.isEnabled()) {
                     --this.leftToEmit;
                     if (this.leftToEmit <= 0) {
                        break;
                     }
                  }
               }
            }

         }
      }
   }

   public void updateParticle(Particle var1, int var2) {
      ++this.particleCount;
      var1.adjustVelocity(this.windFactor.getValue(0.0F) * 5.0E-5F * (float)var2, this.gravityFactor.getValue(0.0F) * 5.0E-5F * (float)var2);
      float var3 = var1.getLife() / var1.getOriginalLife();
      float var4 = 1.0F - var3;
      float var5 = 0.0F;
      float var6 = 1.0F;
      Color var7 = null;
      Color var8 = null;

      float var12;
      for(int var9 = 0; var9 < this.colors.size() - 1; ++var9) {
         ConfigurableEmitter.ColorRecord var10 = (ConfigurableEmitter.ColorRecord)this.colors.get(var9);
         ConfigurableEmitter.ColorRecord var11 = (ConfigurableEmitter.ColorRecord)this.colors.get(var9 + 1);
         if (var4 >= var10.pos && var4 <= var11.pos) {
            var7 = var10.col;
            var8 = var11.col;
            var12 = var11.pos - var10.pos;
            var5 = var4 - var10.pos;
            var5 /= var12;
            var5 = 1.0F - var5;
            var6 = 1.0F - var5;
         }
      }

      float var13;
      if (var7 != null) {
         var13 = var7.r * var5 + var8.r * var6;
         float var14 = var7.g * var5 + var8.g * var6;
         float var15 = var7.b * var5 + var8.b * var6;
         if (this.alpha.isActive()) {
            var12 = this.alpha.getValue(var4) / 255.0F;
         } else {
            var12 = this.startAlpha.getValue(0.0F) / 255.0F * var3 + this.endAlpha.getValue(0.0F) / 255.0F * var4;
         }

         var1.setColor(var13, var14, var15, var12);
      }

      if (this.size.isActive()) {
         var13 = this.size.getValue(var4);
         var1.setSize(var13);
      } else {
         var1.adjustSize((float)var2 * this.growthFactor.getValue(0.0F) * 0.001F);
      }

      if (this.velocity.isActive()) {
         var1.setSpeed(this.velocity.getValue(var4));
      }

      if (this.scaleY.isActive()) {
         var1.setScaleY(this.scaleY.getValue(var4));
      }

   }

   public void replay() {
      this.reset();
      this.nextSpawn = 0;
      this.leftToEmit = (int)this.emitCount.random();
      this.timeout = (int)this.length.random();
   }

   public void reset() {
      this.completed = false;
      if (this.engine != null) {
         this.engine.releaseAll(this);
      }

   }

   public void replayCheck() {
      if (this == null && this.engine != null && this.engine.getParticleCount() == 0) {
         this.replay();
      }

   }

   public ConfigurableEmitter duplicate() {
      ConfigurableEmitter var1 = null;

      try {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         ParticleIO.saveEmitter((OutputStream)var2, this);
         ByteArrayInputStream var3 = new ByteArrayInputStream(var2.toByteArray());
         var1 = ParticleIO.loadEmitter((InputStream)var3);
         return var1;
      } catch (IOException var4) {
         Log.error("Slick: ConfigurableEmitter.duplicate(): caught exception " + var4.toString());
         return null;
      }
   }

   public void addColorPoint(float var1, Color var2) {
      this.colors.add(new ConfigurableEmitter.ColorRecord(this, var1, var2));
   }

   public boolean useAdditive() {
      return this.useAdditive;
   }

   public boolean isOriented() {
      return this.useOriented;
   }

   public boolean usePoints(ParticleSystem var1) {
      return this.usePoints == 1 && var1.usePoints() || this.usePoints == 2;
   }

   public Image getImage() {
      return this.image;
   }

   public void wrapUp() {
      this.wrapUp = true;
   }

   public void resetState() {
      this.replay();
   }

   public class Range {
      private float max;
      private float min;
      private boolean enabled;
      private final ConfigurableEmitter this$0;

      private Range(ConfigurableEmitter var1, float var2, float var3) {
         this.this$0 = var1;
         this.enabled = false;
         this.min = var2;
         this.max = var3;
      }

      public float random() {
         return (float)((double)this.min + Math.random() * (double)(this.max - this.min));
      }

      public boolean isEnabled() {
         return this.enabled;
      }

      public void setEnabled(boolean var1) {
         this.enabled = var1;
      }

      public float getMax() {
         return this.max;
      }

      public void setMax(float var1) {
         this.max = var1;
      }

      public float getMin() {
         return this.min;
      }

      public void setMin(float var1) {
         this.min = var1;
      }

      Range(ConfigurableEmitter var1, float var2, float var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   public class ColorRecord {
      public float pos;
      public Color col;
      private final ConfigurableEmitter this$0;

      public ColorRecord(ConfigurableEmitter var1, float var2, Color var3) {
         this.this$0 = var1;
         this.pos = var2;
         this.col = var3;
      }
   }

   public class LinearInterpolator implements ConfigurableEmitter.Value {
      private ArrayList curve;
      private boolean active;
      private int min;
      private int max;
      private final ConfigurableEmitter this$0;

      public LinearInterpolator(ConfigurableEmitter var1, ArrayList var2, int var3, int var4) {
         this.this$0 = var1;
         this.curve = var2;
         this.min = var3;
         this.max = var4;
         this.active = false;
      }

      public void setCurve(ArrayList var1) {
         this.curve = var1;
      }

      public ArrayList getCurve() {
         return this.curve;
      }

      public float getValue(float var1) {
         Vector2f var2 = (Vector2f)this.curve.get(0);

         for(int var3 = 1; var3 < this.curve.size(); ++var3) {
            Vector2f var4 = (Vector2f)this.curve.get(var3);
            if (var1 >= var2.getX() && var1 <= var4.getX()) {
               float var5 = (var1 - var2.getX()) / (var4.getX() - var2.getX());
               float var6 = var2.getY() + var5 * (var4.getY() - var2.getY());
               return var6;
            }

            var2 = var4;
         }

         return 0.0F;
      }

      public boolean isActive() {
         return this.active;
      }

      public void setActive(boolean var1) {
         this.active = var1;
      }

      public int getMax() {
         return this.max;
      }

      public void setMax(int var1) {
         this.max = var1;
      }

      public int getMin() {
         return this.min;
      }

      public void setMin(int var1) {
         this.min = var1;
      }
   }

   public class RandomValue implements ConfigurableEmitter.Value {
      private float value;
      private final ConfigurableEmitter this$0;

      private RandomValue(ConfigurableEmitter var1, float var2) {
         this.this$0 = var1;
         this.value = var2;
      }

      public float getValue(float var1) {
         return (float)(Math.random() * (double)this.value);
      }

      public void setValue(float var1) {
         this.value = var1;
      }

      public float getValue() {
         return this.value;
      }

      RandomValue(ConfigurableEmitter var1, float var2, Object var3) {
         this(var1, var2);
      }
   }

   public class SimpleValue implements ConfigurableEmitter.Value {
      private float value;
      private float next;
      private final ConfigurableEmitter this$0;

      private SimpleValue(ConfigurableEmitter var1, float var2) {
         this.this$0 = var1;
         this.value = var2;
      }

      public float getValue(float var1) {
         return this.value;
      }

      public void setValue(float var1) {
         this.value = var1;
      }

      SimpleValue(ConfigurableEmitter var1, float var2, Object var3) {
         this(var1, var2);
      }
   }

   public interface Value {
      float getValue(float var1);
   }
}
