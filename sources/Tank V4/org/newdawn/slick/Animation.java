package org.newdawn.slick;

import java.util.ArrayList;
import org.lwjgl.Sys;
import org.newdawn.slick.util.Log;

public class Animation implements Renderable {
   private ArrayList frames;
   private int currentFrame;
   private long nextChange;
   private boolean stopped;
   private long timeLeft;
   private float speed;
   private int stopAt;
   private long lastUpdate;
   private boolean firstUpdate;
   private boolean autoUpdate;
   private int direction;
   private boolean pingPong;
   private boolean loop;
   private SpriteSheet spriteSheet;

   public Animation() {
      this(true);
   }

   public Animation(Image[] var1, int var2) {
      this(var1, var2, true);
   }

   public Animation(Image[] var1, int[] var2) {
      this(var1, var2, true);
   }

   public Animation(boolean var1) {
      this.frames = new ArrayList();
      this.currentFrame = -1;
      this.nextChange = 0L;
      this.stopped = false;
      this.speed = 1.0F;
      this.stopAt = -2;
      this.firstUpdate = true;
      this.autoUpdate = true;
      this.direction = 1;
      this.loop = true;
      this.spriteSheet = null;
      this.currentFrame = 0;
      this.autoUpdate = var1;
   }

   public Animation(Image[] var1, int var2, boolean var3) {
      this.frames = new ArrayList();
      this.currentFrame = -1;
      this.nextChange = 0L;
      this.stopped = false;
      this.speed = 1.0F;
      this.stopAt = -2;
      this.firstUpdate = true;
      this.autoUpdate = true;
      this.direction = 1;
      this.loop = true;
      this.spriteSheet = null;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         this.addFrame(var1[var4], var2);
      }

      this.currentFrame = 0;
      this.autoUpdate = var3;
   }

   public Animation(Image[] var1, int[] var2, boolean var3) {
      this.frames = new ArrayList();
      this.currentFrame = -1;
      this.nextChange = 0L;
      this.stopped = false;
      this.speed = 1.0F;
      this.stopAt = -2;
      this.firstUpdate = true;
      this.autoUpdate = true;
      this.direction = 1;
      this.loop = true;
      this.spriteSheet = null;
      this.autoUpdate = var3;
      if (var1.length != var2.length) {
         throw new RuntimeException("There must be one duration per frame");
      } else {
         for(int var4 = 0; var4 < var1.length; ++var4) {
            this.addFrame(var1[var4], var2[var4]);
         }

         this.currentFrame = 0;
      }
   }

   public Animation(SpriteSheet var1, int var2) {
      this(var1, 0, 0, var1.getHorizontalCount() - 1, var1.getVerticalCount() - 1, true, var2, true);
   }

   public Animation(SpriteSheet var1, int var2, int var3, int var4, int var5, boolean var6, int var7, boolean var8) {
      this.frames = new ArrayList();
      this.currentFrame = -1;
      this.nextChange = 0L;
      this.stopped = false;
      this.speed = 1.0F;
      this.stopAt = -2;
      this.firstUpdate = true;
      this.autoUpdate = true;
      this.direction = 1;
      this.loop = true;
      this.spriteSheet = null;
      this.autoUpdate = var8;
      int var9;
      int var10;
      if (!var6) {
         for(var9 = var2; var9 <= var4; ++var9) {
            for(var10 = var3; var10 <= var5; ++var10) {
               this.addFrame(var1.getSprite(var9, var10), var7);
            }
         }
      } else {
         for(var9 = var3; var9 <= var5; ++var9) {
            for(var10 = var2; var10 <= var4; ++var10) {
               this.addFrame(var1.getSprite(var10, var9), var7);
            }
         }
      }

   }

   public Animation(SpriteSheet var1, int[] var2, int[] var3) {
      this.frames = new ArrayList();
      this.currentFrame = -1;
      this.nextChange = 0L;
      this.stopped = false;
      this.speed = 1.0F;
      this.stopAt = -2;
      this.firstUpdate = true;
      this.autoUpdate = true;
      this.direction = 1;
      this.loop = true;
      this.spriteSheet = null;
      this.spriteSheet = var1;
      boolean var4 = true;
      boolean var5 = true;

      for(int var6 = 0; var6 < var2.length / 2; ++var6) {
         int var7 = var2[var6 * 2];
         int var8 = var2[var6 * 2 + 1];
         this.addFrame(var3[var6], var7, var8);
      }

   }

   public void addFrame(int var1, int var2, int var3) {
      if (var1 == 0) {
         Log.error("Invalid duration: " + var1);
         throw new RuntimeException("Invalid duration: " + var1);
      } else {
         if (this.frames.isEmpty()) {
            this.nextChange = (long)((int)((float)var1 / this.speed));
         }

         this.frames.add(new Animation.Frame(this, var1, var2, var3));
         this.currentFrame = 0;
      }
   }

   public void setAutoUpdate(boolean var1) {
      this.autoUpdate = var1;
   }

   public void setPingPong(boolean var1) {
      this.pingPong = var1;
   }

   public boolean isStopped() {
      return this.stopped;
   }

   public void setSpeed(float var1) {
      if (var1 > 0.0F) {
         this.nextChange = (long)((float)this.nextChange * this.speed / var1);
         this.speed = var1;
      }

   }

   public float getSpeed() {
      return this.speed;
   }

   public void stop() {
      if (this.frames.size() != 0) {
         this.timeLeft = this.nextChange;
         this.stopped = true;
      }
   }

   public void start() {
      if (this.stopped) {
         if (this.frames.size() != 0) {
            this.stopped = false;
            this.nextChange = this.timeLeft;
         }
      }
   }

   public void restart() {
      if (this.frames.size() != 0) {
         this.stopped = false;
         this.currentFrame = 0;
         this.nextChange = (long)((int)((float)((Animation.Frame)this.frames.get(0)).duration / this.speed));
         this.firstUpdate = true;
         this.lastUpdate = 0L;
      }
   }

   public void addFrame(Image var1, int var2) {
      if (var2 == 0) {
         Log.error("Invalid duration: " + var2);
         throw new RuntimeException("Invalid duration: " + var2);
      } else {
         if (this.frames.isEmpty()) {
            this.nextChange = (long)((int)((float)var2 / this.speed));
         }

         this.frames.add(new Animation.Frame(this, var1, var2));
         this.currentFrame = 0;
      }
   }

   public void draw() {
      this.draw(0.0F, 0.0F);
   }

   public void draw(float var1, float var2) {
      this.draw(var1, var2, (float)this.getWidth(), (float)this.getHeight());
   }

   public void draw(float var1, float var2, Color var3) {
      this.draw(var1, var2, (float)this.getWidth(), (float)this.getHeight(), var3);
   }

   public void draw(float var1, float var2, float var3, float var4) {
      this.draw(var1, var2, var3, var4, Color.white);
   }

   public void draw(float var1, float var2, float var3, float var4, Color var5) {
      if (this.frames.size() != 0) {
         if (this.autoUpdate) {
            long var6 = this.getTime();
            long var8 = var6 - this.lastUpdate;
            if (this.firstUpdate) {
               var8 = 0L;
               this.firstUpdate = false;
            }

            this.lastUpdate = var6;
            this.nextFrame(var8);
         }

         Animation.Frame var10 = (Animation.Frame)this.frames.get(this.currentFrame);
         var10.image.draw(var1, var2, var3, var4, var5);
      }
   }

   public void renderInUse(int var1, int var2) {
      if (this.frames.size() != 0) {
         if (this.autoUpdate) {
            long var3 = this.getTime();
            long var5 = var3 - this.lastUpdate;
            if (this.firstUpdate) {
               var5 = 0L;
               this.firstUpdate = false;
            }

            this.lastUpdate = var3;
            this.nextFrame(var5);
         }

         Animation.Frame var7 = (Animation.Frame)this.frames.get(this.currentFrame);
         this.spriteSheet.renderInUse(var1, var2, var7.x, var7.y);
      }
   }

   public int getWidth() {
      return ((Animation.Frame)this.frames.get(this.currentFrame)).image.getWidth();
   }

   public int getHeight() {
      return ((Animation.Frame)this.frames.get(this.currentFrame)).image.getHeight();
   }

   public void drawFlash(float var1, float var2, float var3, float var4) {
      this.drawFlash(var1, var2, var3, var4, Color.white);
   }

   public void drawFlash(float var1, float var2, float var3, float var4, Color var5) {
      if (this.frames.size() != 0) {
         if (this.autoUpdate) {
            long var6 = this.getTime();
            long var8 = var6 - this.lastUpdate;
            if (this.firstUpdate) {
               var8 = 0L;
               this.firstUpdate = false;
            }

            this.lastUpdate = var6;
            this.nextFrame(var8);
         }

         Animation.Frame var10 = (Animation.Frame)this.frames.get(this.currentFrame);
         var10.image.drawFlash(var1, var2, var3, var4, var5);
      }
   }

   /** @deprecated */
   public void updateNoDraw() {
      if (this.autoUpdate) {
         long var1 = this.getTime();
         long var3 = var1 - this.lastUpdate;
         if (this.firstUpdate) {
            var3 = 0L;
            this.firstUpdate = false;
         }

         this.lastUpdate = var1;
         this.nextFrame(var3);
      }

   }

   public void update(long var1) {
      this.nextFrame(var1);
   }

   public int getFrame() {
      return this.currentFrame;
   }

   public void setCurrentFrame(int var1) {
      this.currentFrame = var1;
   }

   public Image getImage(int var1) {
      Animation.Frame var2 = (Animation.Frame)this.frames.get(var1);
      return var2.image;
   }

   public int getFrameCount() {
      return this.frames.size();
   }

   public Image getCurrentFrame() {
      Animation.Frame var1 = (Animation.Frame)this.frames.get(this.currentFrame);
      return var1.image;
   }

   private void nextFrame(long var1) {
      if (!this.stopped) {
         if (this.frames.size() != 0) {
            int var3;
            for(this.nextChange -= var1; this.nextChange < 0L && !this.stopped; this.nextChange += (long)var3) {
               if (this.currentFrame == this.stopAt) {
                  this.stopped = true;
                  break;
               }

               if (this.currentFrame == this.frames.size() - 1 && !this.loop) {
                  this.stopped = true;
                  break;
               }

               this.currentFrame = (this.currentFrame + this.direction) % this.frames.size();
               if (this.pingPong) {
                  if (this.currentFrame <= 0) {
                     this.currentFrame = 0;
                     this.direction = 1;
                  }

                  if (this.currentFrame >= this.frames.size() - 1) {
                     this.currentFrame = this.frames.size() - 1;
                     this.direction = -1;
                  }
               }

               var3 = (int)((float)((Animation.Frame)this.frames.get(this.currentFrame)).duration / this.speed);
            }

         }
      }
   }

   public void setLooping(boolean var1) {
      this.loop = var1;
   }

   private long getTime() {
      return Sys.getTime() * 1000L / Sys.getTimerResolution();
   }

   public void stopAt(int var1) {
      this.stopAt = var1;
   }

   public int getDuration(int var1) {
      return ((Animation.Frame)this.frames.get(var1)).duration;
   }

   public void setDuration(int var1, int var2) {
      ((Animation.Frame)this.frames.get(var1)).duration = var2;
   }

   public int[] getDurations() {
      int[] var1 = new int[this.frames.size()];

      for(int var2 = 0; var2 < this.frames.size(); ++var2) {
         var1[var2] = this.getDuration(var2);
      }

      return var1;
   }

   public String toString() {
      String var1 = "[Animation (" + this.frames.size() + ") ";

      for(int var2 = 0; var2 < this.frames.size(); ++var2) {
         Animation.Frame var3 = (Animation.Frame)this.frames.get(var2);
         var1 = var1 + var3.duration + ",";
      }

      var1 = var1 + "]";
      return var1;
   }

   public Animation copy() {
      Animation var1 = new Animation();
      var1.spriteSheet = this.spriteSheet;
      var1.frames = this.frames;
      var1.autoUpdate = this.autoUpdate;
      var1.direction = this.direction;
      var1.loop = this.loop;
      var1.pingPong = this.pingPong;
      var1.speed = this.speed;
      return var1;
   }

   static SpriteSheet access$000(Animation var0) {
      return var0.spriteSheet;
   }

   private class Frame {
      public Image image;
      public int duration;
      public int x;
      public int y;
      private final Animation this$0;

      public Frame(Animation var1, Image var2, int var3) {
         this.this$0 = var1;
         this.x = -1;
         this.y = -1;
         this.image = var2;
         this.duration = var3;
      }

      public Frame(Animation var1, int var2, int var3, int var4) {
         this.this$0 = var1;
         this.x = -1;
         this.y = -1;
         this.image = Animation.access$000(var1).getSubImage(var3, var4);
         this.duration = var2;
         this.x = var3;
         this.y = var4;
      }
   }
}
