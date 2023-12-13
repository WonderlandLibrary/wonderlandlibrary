package me.theresa.fontRenderer.font;

import java.util.ArrayList;
import java.util.Iterator;
import me.theresa.fontRenderer.font.effect.Renderable;
import me.theresa.fontRenderer.font.log.Log;
import org.lwjgl.Sys;

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

   public Animation(Image[] frames, int duration) {
      this(frames, duration, true);
   }

   public Animation(Image[] frames, int[] durations) {
      this(frames, durations, true);
   }

   public Animation(boolean autoUpdate) {
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
      this.autoUpdate = autoUpdate;
   }

   public Animation(Image[] frames, int duration, boolean autoUpdate) {
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
      Image[] var4 = frames;
      int var5 = frames.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Image frame = var4[var6];
         this.addFrame(frame, duration);
      }

      this.currentFrame = 0;
      this.autoUpdate = autoUpdate;
   }

   public Animation(Image[] frames, int[] durations, boolean autoUpdate) {
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
      this.autoUpdate = autoUpdate;
      if (frames.length != durations.length) {
         throw new RuntimeException("There must be one duration per frame");
      } else {
         for(int i = 0; i < frames.length; ++i) {
            this.addFrame(frames[i], durations[i]);
         }

         this.currentFrame = 0;
      }
   }

   public Animation(SpriteSheet frames, int duration) {
      this(frames, 0, 0, frames.getHorizontalCount() - 1, frames.getVerticalCount() - 1, true, duration, true);
   }

   public Animation(SpriteSheet frames, int x1, int y1, int x2, int y2, boolean horizontalScan, int duration, boolean autoUpdate) {
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
      this.autoUpdate = autoUpdate;
      int x;
      int y;
      if (!horizontalScan) {
         for(x = x1; x <= x2; ++x) {
            for(y = y1; y <= y2; ++y) {
               this.addFrame(frames.getSprite(x, y), duration);
            }
         }
      } else {
         for(x = y1; x <= y2; ++x) {
            for(y = x1; y <= x2; ++y) {
               this.addFrame(frames.getSprite(y, x), duration);
            }
         }
      }

   }

   public Animation(SpriteSheet ss, int[] frames, int[] duration) {
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
      this.spriteSheet = ss;
      int x = true;
      int y = true;

      for(int i = 0; i < frames.length / 2; ++i) {
         int x = frames[i * 2];
         int y = frames[i * 2 + 1];
         this.addFrame(duration[i], x, y);
      }

   }

   public void addFrame(int duration, int x, int y) {
      if (duration == 0) {
         Log.error("Invalid duration: " + duration);
         throw new RuntimeException("Invalid duration: " + duration);
      } else {
         if (this.frames.isEmpty()) {
            this.nextChange = (long)((int)((float)duration / this.speed));
         }

         this.frames.add(new Frame(duration, x, y));
         this.currentFrame = 0;
      }
   }

   public void setAutoUpdate(boolean auto) {
      this.autoUpdate = auto;
   }

   public void setPingPong(boolean pingPong) {
      this.pingPong = pingPong;
   }

   public boolean isStopped() {
      return this.stopped;
   }

   public void setSpeed(float spd) {
      if (spd > 0.0F) {
         this.nextChange = (long)((float)this.nextChange * this.speed / spd);
         this.speed = spd;
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
         this.nextChange = (long)((int)((float)((Frame)this.frames.get(0)).duration / this.speed));
         this.firstUpdate = true;
         this.lastUpdate = 0L;
      }
   }

   public void addFrame(Image frame, int duration) {
      if (duration == 0) {
         Log.error("Invalid duration: " + duration);
         throw new RuntimeException("Invalid duration: " + duration);
      } else {
         if (this.frames.isEmpty()) {
            this.nextChange = (long)((int)((float)duration / this.speed));
         }

         this.frames.add(new Frame(frame, duration));
         this.currentFrame = 0;
      }
   }

   public void draw() {
      this.draw(0.0F, 0.0F);
   }

   public void draw(float x, float y) {
      this.draw(x, y, (float)this.getWidth(), (float)this.getHeight());
   }

   public void draw(float x, float y, Color filter) {
      this.draw(x, y, (float)this.getWidth(), (float)this.getHeight(), filter);
   }

   public void draw(float x, float y, float width, float height) {
      this.draw(x, y, width, height, Color.white);
   }

   public void draw(float x, float y, float width, float height, Color col) {
      if (this.frames.size() != 0) {
         if (this.autoUpdate) {
            long now = this.getTime();
            long delta = now - this.lastUpdate;
            if (this.firstUpdate) {
               delta = 0L;
               this.firstUpdate = false;
            }

            this.lastUpdate = now;
            this.nextFrame(delta);
         }

         Frame frame = (Frame)this.frames.get(this.currentFrame);
         frame.image.draw(x, y, width, height, col);
      }
   }

   public void renderInUse(int x, int y) {
      if (this.frames.size() != 0) {
         if (this.autoUpdate) {
            long now = this.getTime();
            long delta = now - this.lastUpdate;
            if (this.firstUpdate) {
               delta = 0L;
               this.firstUpdate = false;
            }

            this.lastUpdate = now;
            this.nextFrame(delta);
         }

         Frame frame = (Frame)this.frames.get(this.currentFrame);
         this.spriteSheet.renderInUse(x, y, frame.x, frame.y);
      }
   }

   public int getWidth() {
      return ((Frame)this.frames.get(this.currentFrame)).image.getWidth();
   }

   public int getHeight() {
      return ((Frame)this.frames.get(this.currentFrame)).image.getHeight();
   }

   public void drawFlash(float x, float y, float width, float height) {
      this.drawFlash(x, y, width, height, Color.white);
   }

   public void drawFlash(float x, float y, float width, float height, Color col) {
      if (this.frames.size() != 0) {
         if (this.autoUpdate) {
            long now = this.getTime();
            long delta = now - this.lastUpdate;
            if (this.firstUpdate) {
               delta = 0L;
               this.firstUpdate = false;
            }

            this.lastUpdate = now;
            this.nextFrame(delta);
         }

         Frame frame = (Frame)this.frames.get(this.currentFrame);
         frame.image.drawFlash(x, y, width, height, col);
      }
   }

   public void updateNoDraw() {
      if (this.autoUpdate) {
         long now = this.getTime();
         long delta = now - this.lastUpdate;
         if (this.firstUpdate) {
            delta = 0L;
            this.firstUpdate = false;
         }

         this.lastUpdate = now;
         this.nextFrame(delta);
      }

   }

   public void update(long delta) {
      this.nextFrame(delta);
   }

   public int getFrame() {
      return this.currentFrame;
   }

   public void setCurrentFrame(int index) {
      this.currentFrame = index;
   }

   public Image getImage(int index) {
      Frame frame = (Frame)this.frames.get(index);
      return frame.image;
   }

   public int getFrameCount() {
      return this.frames.size();
   }

   public Image getCurrentFrame() {
      Frame frame = (Frame)this.frames.get(this.currentFrame);
      return frame.image;
   }

   private void nextFrame(long delta) {
      if (!this.stopped) {
         if (this.frames.size() != 0) {
            int realDuration;
            for(this.nextChange -= delta; this.nextChange < 0L && !this.stopped; this.nextChange += (long)realDuration) {
               if (this.currentFrame == this.stopAt) {
                  this.stopped = true;
                  break;
               }

               if (this.currentFrame == this.frames.size() - 1 && !this.loop && !this.pingPong) {
                  this.stopped = true;
                  break;
               }

               this.currentFrame = (this.currentFrame + this.direction) % this.frames.size();
               if (this.pingPong) {
                  if (this.currentFrame <= 0) {
                     this.currentFrame = 0;
                     this.direction = 1;
                     if (!this.loop) {
                        this.stopped = true;
                        break;
                     }
                  } else if (this.currentFrame >= this.frames.size() - 1) {
                     this.currentFrame = this.frames.size() - 1;
                     this.direction = -1;
                  }
               }

               realDuration = (int)((float)((Frame)this.frames.get(this.currentFrame)).duration / this.speed);
            }

         }
      }
   }

   public void setLooping(boolean loop) {
      this.loop = loop;
   }

   private long getTime() {
      return Sys.getTime() * 1000L / Sys.getTimerResolution();
   }

   public void stopAt(int frameIndex) {
      this.stopAt = frameIndex;
   }

   public int getDuration(int index) {
      return ((Frame)this.frames.get(index)).duration;
   }

   public void setDuration(int index, int duration) {
      ((Frame)this.frames.get(index)).duration = duration;
   }

   public int[] getDurations() {
      int[] durations = new int[this.frames.size()];

      for(int i = 0; i < this.frames.size(); ++i) {
         durations[i] = this.getDuration(i);
      }

      return durations;
   }

   public String toString() {
      StringBuilder res = new StringBuilder("[Animation (" + this.frames.size() + ") ");
      Iterator var2 = this.frames.iterator();

      while(var2.hasNext()) {
         Object o = var2.next();
         Frame frame = (Frame)o;
         res.append(frame.duration).append(",");
      }

      res.append("]");
      return res.toString();
   }

   public Animation copy() {
      Animation copy = new Animation();
      copy.spriteSheet = this.spriteSheet;
      copy.frames = this.frames;
      copy.autoUpdate = this.autoUpdate;
      copy.direction = this.direction;
      copy.loop = this.loop;
      copy.pingPong = this.pingPong;
      copy.speed = this.speed;
      return copy;
   }

   private class Frame {
      public Image image;
      public int duration;
      public int x = -1;
      public int y = -1;

      public Frame(Image image, int duration) {
         this.image = image;
         this.duration = duration;
      }

      public Frame(int duration, int x, int y) {
         this.image = Animation.this.spriteSheet.getSubImage(x, y);
         this.duration = duration;
         this.x = x;
         this.y = y;
      }
   }
}
