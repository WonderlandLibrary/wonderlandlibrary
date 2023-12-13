package com.sun.javafx.webkit.prism;

import com.sun.javafx.media.PrismMediaFrameHandler;
import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaManager;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.media.jfxmedia.events.BufferListener;
import com.sun.media.jfxmedia.events.BufferProgressEvent;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.events.NewFrameEvent;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import com.sun.media.jfxmedia.events.PlayerTimeListener;
import com.sun.media.jfxmedia.events.VideoRendererListener;
import com.sun.media.jfxmedia.events.VideoTrackSizeListener;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.track.AudioTrack;
import com.sun.media.jfxmedia.track.Track;
import com.sun.media.jfxmedia.track.VideoTrack;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCMediaPlayer;
import java.lang.Thread.State;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

final class WCMediaPlayerImpl extends WCMediaPlayer implements PlayerStateListener, MediaErrorListener, VideoTrackSizeListener, BufferListener, PlayerTimeListener {
   private final Object lock = new Object();
   private volatile MediaPlayer player;
   private volatile CreateThread createThread;
   private volatile PrismMediaFrameHandler frameHandler;
   private final MediaFrameListener frameListener = new MediaFrameListener();
   private boolean gotFirstFrame = false;
   private int finished = 0;
   private float bufferedStart = 0.0F;
   private float bufferedEnd = 0.0F;
   private boolean buffering = false;

   private MediaPlayer getPlayer() {
      synchronized(this.lock) {
         return this.createThread != null ? null : this.player;
      }
   }

   private void setPlayer(MediaPlayer var1) {
      synchronized(this.lock) {
         this.player = var1;
         this.installListeners();
         this.frameHandler = PrismMediaFrameHandler.getHandler(this.player);
      }

      this.finished = 0;
   }

   protected void load(String var1, String var2) {
      synchronized(this.lock) {
         if (this.createThread != null) {
            this.createThread.cancel();
         }

         this.disposePlayer();
         this.createThread = new CreateThread(var1, var2);
      }

      if (this.getPreload() != 0) {
         this.createThread.start();
      }

   }

   protected void cancelLoad() {
      synchronized(this.lock) {
         if (this.createThread != null) {
            this.createThread.cancel();
         }
      }

      MediaPlayer var1 = this.getPlayer();
      if (var1 != null) {
         var1.stop();
      }

      this.notifyNetworkStateChanged(0);
      this.notifyReadyStateChanged(0);
   }

   protected void disposePlayer() {
      MediaPlayer var1;
      synchronized(this.lock) {
         this.removeListeners();
         var1 = this.player;
         this.player = null;
         if (this.frameHandler != null) {
            this.frameHandler.releaseTextures();
            this.frameHandler = null;
         }
      }

      if (var1 != null) {
         var1.stop();
         var1.dispose();
         var1 = null;
         if (this.frameListener != null) {
            this.frameListener.releaseVideoFrames();
         }
      }

   }

   private void installListeners() {
      if (null != this.player) {
         this.player.addMediaPlayerListener(this);
         this.player.addMediaErrorListener(this);
         this.player.addVideoTrackSizeListener(this);
         this.player.addBufferListener(this);
         this.player.getVideoRenderControl().addVideoRendererListener(this.frameListener);
      }

   }

   private void removeListeners() {
      if (null != this.player) {
         this.player.removeMediaPlayerListener(this);
         this.player.removeMediaErrorListener(this);
         this.player.removeVideoTrackSizeListener(this);
         this.player.removeBufferListener(this);
         this.player.getVideoRenderControl().removeVideoRendererListener(this.frameListener);
      }

   }

   protected void prepareToPlay() {
      synchronized(this.lock) {
         if (this.player == null) {
            CreateThread var2 = this.createThread;
            if (var2 != null && var2.getState() == State.NEW) {
               var2.start();
            }
         }

      }
   }

   protected void play() {
      MediaPlayer var1 = this.getPlayer();
      if (var1 != null) {
         var1.play();
         this.notifyPaused(false);
      }

   }

   protected void pause() {
      MediaPlayer var1 = this.getPlayer();
      if (var1 != null) {
         var1.pause();
         this.notifyPaused(true);
      }

   }

   protected float getCurrentTime() {
      MediaPlayer var1 = this.getPlayer();
      if (var1 == null) {
         return 0.0F;
      } else {
         return this.finished == 0 ? (float)var1.getPresentationTime() : (this.finished > 0 ? (float)var1.getDuration() : 0.0F);
      }
   }

   protected void seek(final float var1) {
      MediaPlayer var2 = this.getPlayer();
      if (var2 != null) {
         this.finished = 0;
         if (this.getReadyState() >= 1) {
            this.notifySeeking(true, 1);
         } else {
            this.notifySeeking(true, 0);
         }

         var2.seek((double)var1);
         Thread var4 = new Thread(new Runnable() {
            public void run() {
               while(true) {
                  if (WCMediaPlayerImpl.this.isSeeking()) {
                     MediaPlayer var1x = WCMediaPlayerImpl.this.getPlayer();
                     if (var1x != null) {
                        double var2 = var1x.getPresentationTime();
                        if (!((double)var1 < 0.01) && !(Math.abs(var2) >= 0.01)) {
                           try {
                              Thread.sleep(10L);
                           } catch (InterruptedException var5) {
                           }
                           continue;
                        }

                        WCMediaPlayerImpl.this.notifySeeking(false, 4);
                     }
                  }

                  return;
               }
            }
         });
         var4.setDaemon(true);
         var4.start();
      }

   }

   protected void setRate(float var1) {
      MediaPlayer var2 = this.getPlayer();
      if (var2 != null) {
         var2.setRate(var1);
      }

   }

   protected void setVolume(float var1) {
      MediaPlayer var2 = this.getPlayer();
      if (var2 != null) {
         var2.setVolume(var1);
      }

   }

   protected void setMute(boolean var1) {
      MediaPlayer var2 = this.getPlayer();
      if (var2 != null) {
         var2.setMute(var1);
      }

   }

   protected void setSize(int var1, int var2) {
   }

   protected void setPreservesPitch(boolean var1) {
   }

   protected void renderCurrentFrame(WCGraphicsContext var1, int var2, int var3, int var4, int var5) {
      synchronized(this.lock) {
         this.renderImpl(var1, var2, var3, var4, var5);
      }
   }

   private void renderImpl(WCGraphicsContext var1, int var2, int var3, int var4, int var5) {
      if (verbose) {
         log.log(Level.FINER, ">>(Prism)renderImpl");
      }

      Graphics var6 = (Graphics)var1.getPlatformGraphics();
      Texture var7 = null;
      VideoDataBuffer var8 = this.frameListener.getLatestFrame();
      if (null != var8) {
         if (null != this.frameHandler) {
            var7 = this.frameHandler.getTexture(var6, var8);
         }

         var8.releaseFrame();
      }

      if (var7 != null) {
         var6.drawTexture(var7, (float)var2, (float)var3, (float)(var2 + var4), (float)(var3 + var5), 0.0F, 0.0F, (float)var7.getContentWidth(), (float)var7.getContentHeight());
         var7.unlock();
      } else {
         if (verbose) {
            log.log(Level.FINEST, "  (Prism)renderImpl, texture is null, draw black rect");
         }

         var1.fillRect((float)var2, (float)var3, (float)var4, (float)var5, Color.BLACK);
      }

      if (verbose) {
         log.log(Level.FINER, "<<(Prism)renderImpl");
      }

   }

   public void onReady(PlayerStateEvent var1) {
      MediaPlayer var2 = this.getPlayer();
      if (verbose) {
         log.log(Level.FINE, "onReady");
      }

      Media var3 = var2.getMedia();
      boolean var4 = false;
      boolean var5 = false;
      if (var3 != null) {
         List var6 = var3.getTracks();
         if (var6 != null) {
            if (verbose) {
               log.log(Level.INFO, "{0} track(s) detected:", var6.size());
            }

            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               Track var8 = (Track)var7.next();
               if (var8 instanceof VideoTrack) {
                  var4 = true;
               } else if (var8 instanceof AudioTrack) {
                  var5 = true;
               }

               if (verbose) {
                  log.log(Level.INFO, "track: {0}", var8);
               }
            }
         } else if (verbose) {
            log.log(Level.WARNING, "onReady, tracks IS NULL");
         }
      } else if (verbose) {
         log.log(Level.WARNING, "onReady, media IS NULL");
      }

      if (verbose) {
         log.log(Level.FINE, "onReady, hasVideo:{0}, hasAudio: {1}", new Object[]{var4, var5});
      }

      this.notifyReady(var4, var5, (float)var2.getDuration());
      if (!var4) {
         this.notifyReadyStateChanged(4);
      } else if (this.getReadyState() < 1) {
         if (this.gotFirstFrame) {
            this.notifyReadyStateChanged(4);
         } else {
            this.notifyReadyStateChanged(1);
         }
      }

   }

   public void onPlaying(PlayerStateEvent var1) {
      if (verbose) {
         log.log(Level.FINE, "onPlaying");
      }

      this.notifyPaused(false);
   }

   public void onPause(PlayerStateEvent var1) {
      if (verbose) {
         log.log(Level.FINE, "onPause, time: {0}", var1.getTime());
      }

      this.notifyPaused(true);
   }

   public void onStop(PlayerStateEvent var1) {
      if (verbose) {
         log.log(Level.FINE, "onStop");
      }

      this.notifyPaused(true);
   }

   public void onStall(PlayerStateEvent var1) {
      if (verbose) {
         log.log(Level.FINE, "onStall");
      }

   }

   public void onFinish(PlayerStateEvent var1) {
      MediaPlayer var2 = this.getPlayer();
      if (var2 != null) {
         this.finished = var2.getRate() > 0.0F ? 1 : -1;
         if (verbose) {
            log.log(Level.FINE, "onFinish, time: {0}", var1.getTime());
         }

         this.notifyFinished();
      }

   }

   public void onHalt(PlayerStateEvent var1) {
      if (verbose) {
         log.log(Level.FINE, "onHalt");
      }

   }

   public void onError(Object var1, int var2, String var3) {
      if (verbose) {
         log.log(Level.WARNING, "onError, errCode={0}, msg={1}", new Object[]{var2, var3});
      }

      this.notifyNetworkStateChanged(5);
      this.notifyReadyStateChanged(0);
   }

   public void onDurationChanged(double var1) {
      if (verbose) {
         log.log(Level.FINE, "onDurationChanged, duration={0}", var1);
      }

      this.notifyDurationChanged((float)var1);
   }

   public void onSizeChanged(int var1, int var2) {
      if (verbose) {
         log.log(Level.FINE, "onSizeChanged, new size = {0} x {1}", new Object[]{var1, var2});
      }

      this.notifySizeChanged(var1, var2);
   }

   private void notifyFrameArrived() {
      if (!this.gotFirstFrame) {
         if (this.getReadyState() >= 1) {
            this.notifyReadyStateChanged(4);
         }

         this.gotFirstFrame = true;
      }

      if (verbose && this.finished != 0) {
         log.log(Level.FINE, "notifyFrameArrived (after finished) time: {0}", this.getPlayer().getPresentationTime());
      }

      this.notifyNewFrame();
   }

   private void updateBufferingStatus() {
      int var1 = this.buffering ? 2 : (this.bufferedStart > 0.0F ? 1 : 3);
      if (verbose) {
         log.log(Level.FINE, "updateBufferingStatus, buffered: [{0} - {1}], buffering = {2}", new Object[]{this.bufferedStart, this.bufferedEnd, this.buffering});
      }

      this.notifyNetworkStateChanged(var1);
   }

   public void onBufferProgress(BufferProgressEvent var1) {
      if (!(var1.getDuration() < 0.0)) {
         double var2 = var1.getDuration() / (double)var1.getBufferStop();
         this.bufferedStart = (float)(var2 * (double)var1.getBufferStart());
         this.bufferedEnd = (float)(var2 * (double)var1.getBufferPosition());
         this.buffering = var1.getBufferPosition() < var1.getBufferStop();
         float[] var4 = new float[]{this.bufferedStart, this.bufferedEnd};
         int var5 = (int)(var1.getBufferPosition() - var1.getBufferStart());
         if (verbose) {
            log.log(Level.FINER, "onBufferProgress, bufferStart={0}, bufferStop={1}, bufferPos={2}, duration={3}; notify range [{4},[5]], bytesLoaded: {6}", new Object[]{var1.getBufferStart(), var1.getBufferStop(), var1.getBufferPosition(), var1.getDuration(), var4[0], var4[1], var5});
         }

         this.notifyBufferChanged(var4, var5);
         this.updateBufferingStatus();
      }
   }

   private final class MediaFrameListener implements VideoRendererListener {
      private final Object frameLock;
      private VideoDataBuffer currentFrame;
      private VideoDataBuffer nextFrame;

      private MediaFrameListener() {
         this.frameLock = new Object();
      }

      public void videoFrameUpdated(NewFrameEvent var1) {
         synchronized(this.frameLock) {
            if (null != this.nextFrame) {
               this.nextFrame.releaseFrame();
            }

            this.nextFrame = var1.getFrameData();
            if (null != this.nextFrame) {
               this.nextFrame.holdFrame();
            }
         }

         WCMediaPlayerImpl.this.notifyFrameArrived();
      }

      public void releaseVideoFrames() {
         synchronized(this.frameLock) {
            if (null != this.nextFrame) {
               this.nextFrame.releaseFrame();
               this.nextFrame = null;
            }

            if (null != this.currentFrame) {
               this.currentFrame.releaseFrame();
               this.currentFrame = null;
            }

         }
      }

      public VideoDataBuffer getLatestFrame() {
         synchronized(this.frameLock) {
            if (null != this.nextFrame) {
               if (null != this.currentFrame) {
                  this.currentFrame.releaseFrame();
               }

               this.currentFrame = this.nextFrame;
               this.nextFrame = null;
            }

            if (null != this.currentFrame) {
               this.currentFrame.holdFrame();
            }

            return this.currentFrame;
         }
      }

      // $FF: synthetic method
      MediaFrameListener(Object var2) {
         this();
      }
   }

   private final class CreateThread extends Thread {
      private boolean cancelled;
      private final String url;
      private final String userAgent;

      private CreateThread(String var2, String var3) {
         this.cancelled = false;
         this.url = var2;
         this.userAgent = var3;
         WCMediaPlayerImpl.this.gotFirstFrame = false;
      }

      public void run() {
         if (WCMediaPlayerImpl.verbose) {
            WCMediaPlayerImpl.log.log(Level.FINE, "CreateThread: started, url={0}", this.url);
         }

         WCMediaPlayerImpl.this.notifyNetworkStateChanged(2);
         WCMediaPlayerImpl.this.notifyReadyStateChanged(0);
         MediaPlayer var1 = null;

         try {
            Locator var2 = new Locator(new URI(this.url));
            if (this.userAgent != null) {
               var2.setConnectionProperty("User-Agent", this.userAgent);
            }

            var2.init();
            if (WCMediaPlayerImpl.verbose) {
               WCMediaPlayerImpl.log.fine("CreateThread: locator created");
            }

            var1 = MediaManager.getPlayer(var2);
         } catch (Exception var5) {
            if (WCMediaPlayerImpl.verbose) {
               WCMediaPlayerImpl.log.log(Level.WARNING, "CreateThread ERROR: {0}", var5.toString());
               var5.printStackTrace(System.out);
            }

            WCMediaPlayerImpl.this.onError(this, 0, var5.getMessage());
            return;
         }

         synchronized(WCMediaPlayerImpl.this.lock) {
            if (this.cancelled) {
               if (WCMediaPlayerImpl.verbose) {
                  WCMediaPlayerImpl.log.log(Level.FINE, "CreateThread: cancelled");
               }

               var1.dispose();
               return;
            }

            WCMediaPlayerImpl.this.createThread = null;
            WCMediaPlayerImpl.this.setPlayer(var1);
         }

         if (WCMediaPlayerImpl.verbose) {
            WCMediaPlayerImpl.log.log(Level.FINE, "CreateThread: completed");
         }

      }

      private void cancel() {
         synchronized(WCMediaPlayerImpl.this.lock) {
            this.cancelled = true;
         }
      }

      // $FF: synthetic method
      CreateThread(String var2, String var3, Object var4) {
         this(var2, var3);
      }
   }
}
