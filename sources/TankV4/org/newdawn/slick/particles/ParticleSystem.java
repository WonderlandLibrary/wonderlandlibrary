package org.newdawn.slick.particles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.util.Log;

public class ParticleSystem {
   protected static SGL GL = Renderer.get();
   public static final int BLEND_ADDITIVE = 1;
   public static final int BLEND_COMBINE = 2;
   private static final int DEFAULT_PARTICLES = 100;
   protected HashMap particlesByEmitter;
   protected int maxParticlesPerEmitter;
   protected ArrayList emitters;
   protected Particle dummy;
   private int blendingMode;
   private int pCount;
   private boolean usePoints;
   private float x;
   private float y;
   private boolean removeCompletedEmitters;
   private Image sprite;
   private boolean visible;
   private String defaultImageName;
   private Color mask;

   public static void setRelativePath(String var0) {
      ConfigurableEmitter.setRelativePath(var0);
   }

   public ParticleSystem(Image var1) {
      this((Image)var1, 100);
   }

   public ParticleSystem(String var1) {
      this((String)var1, 100);
   }

   public void reset() {
      Iterator var1 = this.particlesByEmitter.values().iterator();

      while(var1.hasNext()) {
         ParticleSystem.ParticlePool var2 = (ParticleSystem.ParticlePool)var1.next();
         var2.reset(this);
      }

      for(int var4 = 0; var4 < this.emitters.size(); ++var4) {
         ParticleEmitter var3 = (ParticleEmitter)this.emitters.get(var4);
         var3.resetState();
      }

   }

   public boolean isVisible() {
      return this.visible;
   }

   public void setVisible(boolean var1) {
      this.visible = var1;
   }

   public void setRemoveCompletedEmitters(boolean var1) {
      this.removeCompletedEmitters = var1;
   }

   public void setUsePoints(boolean var1) {
      this.usePoints = var1;
   }

   public boolean usePoints() {
      return this.usePoints;
   }

   public ParticleSystem(String var1, int var2) {
      this(var1, var2, (Color)null);
   }

   public ParticleSystem(String var1, int var2, Color var3) {
      this.particlesByEmitter = new HashMap();
      this.emitters = new ArrayList();
      this.blendingMode = 2;
      this.removeCompletedEmitters = true;
      this.visible = true;
      this.maxParticlesPerEmitter = var2;
      this.mask = var3;
      this.setDefaultImageName(var1);
      this.dummy = this.createParticle(this);
   }

   public ParticleSystem(Image var1, int var2) {
      this.particlesByEmitter = new HashMap();
      this.emitters = new ArrayList();
      this.blendingMode = 2;
      this.removeCompletedEmitters = true;
      this.visible = true;
      this.maxParticlesPerEmitter = var2;
      this.sprite = var1;
      this.dummy = this.createParticle(this);
   }

   public void setDefaultImageName(String var1) {
      this.defaultImageName = var1;
      this.sprite = null;
   }

   public int getBlendingMode() {
      return this.blendingMode;
   }

   protected Particle createParticle(ParticleSystem var1) {
      return new Particle(var1);
   }

   public void setBlendingMode(int var1) {
      this.blendingMode = var1;
   }

   public int getEmitterCount() {
      return this.emitters.size();
   }

   public ParticleEmitter getEmitter(int var1) {
      return (ParticleEmitter)this.emitters.get(var1);
   }

   public void addEmitter(ParticleEmitter var1) {
      this.emitters.add(var1);
      ParticleSystem.ParticlePool var2 = new ParticleSystem.ParticlePool(this, this, this.maxParticlesPerEmitter);
      this.particlesByEmitter.put(var1, var2);
   }

   public void removeEmitter(ParticleEmitter var1) {
      this.emitters.remove(var1);
      this.particlesByEmitter.remove(var1);
   }

   public void removeAllEmitters() {
      for(int var1 = 0; var1 < this.emitters.size(); ++var1) {
         this.removeEmitter((ParticleEmitter)this.emitters.get(var1));
         --var1;
      }

   }

   public float getPositionX() {
      return this.x;
   }

   public float getPositionY() {
      return this.y;
   }

   public void setPosition(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public void render() {
      this.render(this.x, this.y);
   }

   public void render(float var1, float var2) {
      if (this.sprite == null && this.defaultImageName != null) {
         this.loadSystemParticleImage();
      }

      if (this.visible) {
         GL.glTranslatef(var1, var2, 0.0F);
         if (this.blendingMode == 1) {
            GL.glBlendFunc(770, 1);
         }

         if (this.usePoints()) {
            GL.glEnable(2832);
            TextureImpl.bindNone();
         }

         for(int var3 = 0; var3 < this.emitters.size(); ++var3) {
            ParticleEmitter var4 = (ParticleEmitter)this.emitters.get(var3);
            if (var4.useAdditive()) {
               GL.glBlendFunc(770, 1);
            }

            ParticleSystem.ParticlePool var5 = (ParticleSystem.ParticlePool)this.particlesByEmitter.get(var4);
            Image var6 = var4.getImage();
            if (var6 == null) {
               var6 = this.sprite;
            }

            if (!var4.isOriented() && !var4.usePoints(this)) {
               var6.startUse();
            }

            for(int var7 = 0; var7 < var5.particles.length; ++var7) {
               if (var5.particles[var7].inUse()) {
                  var5.particles[var7].render();
               }
            }

            if (!var4.isOriented() && !var4.usePoints(this)) {
               var6.endUse();
            }

            if (var4.useAdditive()) {
               GL.glBlendFunc(770, 771);
            }
         }

         if (this.usePoints()) {
            GL.glDisable(2832);
         }

         if (this.blendingMode == 1) {
            GL.glBlendFunc(770, 771);
         }

         Color.white.bind();
         GL.glTranslatef(-var1, -var2, 0.0F);
      }
   }

   private void loadSystemParticleImage() {
      AccessController.doPrivileged(new PrivilegedAction(this) {
         private final ParticleSystem this$0;

         {
            this.this$0 = var1;
         }

         public Object run() {
            try {
               if (ParticleSystem.access$000(this.this$0) != null) {
                  ParticleSystem.access$102(this.this$0, new Image(ParticleSystem.access$200(this.this$0), ParticleSystem.access$000(this.this$0)));
               } else {
                  ParticleSystem.access$102(this.this$0, new Image(ParticleSystem.access$200(this.this$0)));
               }
            } catch (SlickException var2) {
               Log.error((Throwable)var2);
               ParticleSystem.access$202(this.this$0, (String)null);
            }

            return null;
         }
      });
   }

   public void update(int var1) {
      if (this.sprite == null && this.defaultImageName != null) {
         this.loadSystemParticleImage();
      }

      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < this.emitters.size(); ++var3) {
         ParticleEmitter var4 = (ParticleEmitter)this.emitters.get(var3);
         if (var4.isEnabled()) {
            var4.update(this, var1);
            if (this.removeCompletedEmitters && var4.completed()) {
               var2.add(var4);
               this.particlesByEmitter.remove(var4);
            }
         }
      }

      this.emitters.removeAll(var2);
      this.pCount = 0;
      if (!this.particlesByEmitter.isEmpty()) {
         Iterator var6 = this.particlesByEmitter.values().iterator();

         while(var6.hasNext()) {
            ParticleSystem.ParticlePool var7 = (ParticleSystem.ParticlePool)var6.next();

            for(int var5 = 0; var5 < var7.particles.length; ++var5) {
               if (var7.particles[var5].life > 0.0F) {
                  var7.particles[var5].update(var1);
                  ++this.pCount;
               }
            }
         }
      }

   }

   public int getParticleCount() {
      return this.pCount;
   }

   public Particle getNewParticle(ParticleEmitter var1, float var2) {
      ParticleSystem.ParticlePool var3 = (ParticleSystem.ParticlePool)this.particlesByEmitter.get(var1);
      ArrayList var4 = var3.available;
      if (var4.size() > 0) {
         Particle var5 = (Particle)var4.remove(var4.size() - 1);
         var5.init(var1, var2);
         var5.setImage(this.sprite);
         return var5;
      } else {
         Log.warn("Ran out of particles (increase the limit)!");
         return this.dummy;
      }
   }

   public void release(Particle var1) {
      if (var1 != this.dummy) {
         ParticleSystem.ParticlePool var2 = (ParticleSystem.ParticlePool)this.particlesByEmitter.get(var1.getEmitter());
         var2.available.add(var1);
      }

   }

   public void releaseAll(ParticleEmitter var1) {
      if (!this.particlesByEmitter.isEmpty()) {
         Iterator var2 = this.particlesByEmitter.values().iterator();

         while(var2.hasNext()) {
            ParticleSystem.ParticlePool var3 = (ParticleSystem.ParticlePool)var2.next();

            for(int var4 = 0; var4 < var3.particles.length; ++var4) {
               if (var3.particles[var4].inUse() && var3.particles[var4].getEmitter() == var1) {
                  var3.particles[var4].setLife(-1.0F);
                  this.release(var3.particles[var4]);
               }
            }
         }
      }

   }

   public void moveAll(ParticleEmitter var1, float var2, float var3) {
      ParticleSystem.ParticlePool var4 = (ParticleSystem.ParticlePool)this.particlesByEmitter.get(var1);

      for(int var5 = 0; var5 < var4.particles.length; ++var5) {
         if (var4.particles[var5].inUse()) {
            var4.particles[var5].move(var2, var3);
         }
      }

   }

   public ParticleSystem duplicate() throws SlickException {
      for(int var1 = 0; var1 < this.emitters.size(); ++var1) {
         if (!(this.emitters.get(var1) instanceof ConfigurableEmitter)) {
            throw new SlickException("Only systems contianing configurable emitters can be duplicated");
         }
      }

      ParticleSystem var5 = null;

      try {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         ParticleIO.saveConfiguredSystem((OutputStream)var2, this);
         ByteArrayInputStream var3 = new ByteArrayInputStream(var2.toByteArray());
         var5 = ParticleIO.loadConfiguredSystem((InputStream)var3);
         return var5;
      } catch (IOException var4) {
         Log.error("Failed to duplicate particle system");
         throw new SlickException("Unable to duplicated particle system", var4);
      }
   }

   static Color access$000(ParticleSystem var0) {
      return var0.mask;
   }

   static Image access$102(ParticleSystem var0, Image var1) {
      return var0.sprite = var1;
   }

   static String access$200(ParticleSystem var0) {
      return var0.defaultImageName;
   }

   static String access$202(ParticleSystem var0, String var1) {
      return var0.defaultImageName = var1;
   }

   private class ParticlePool {
      public Particle[] particles;
      public ArrayList available;
      private final ParticleSystem this$0;

      public ParticlePool(ParticleSystem var1, ParticleSystem var2, int var3) {
         this.this$0 = var1;
         this.particles = new Particle[var3];
         this.available = new ArrayList();

         for(int var4 = 0; var4 < this.particles.length; ++var4) {
            this.particles[var4] = var1.createParticle(var2);
         }

         this.reset(var2);
      }

      public void reset(ParticleSystem var1) {
         this.available.clear();

         for(int var2 = 0; var2 < this.particles.length; ++var2) {
            this.available.add(this.particles[var2]);
         }

      }
   }
}
