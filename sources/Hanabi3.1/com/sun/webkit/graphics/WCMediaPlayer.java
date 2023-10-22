package com.sun.webkit.graphics;

import com.sun.webkit.Invoker;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class WCMediaPlayer extends Ref {
   protected static final Logger log = Logger.getLogger("webkit.mediaplayer");
   protected static final boolean verbose;
   private long nPtr;
   protected static final int NETWORK_STATE_EMPTY = 0;
   protected static final int NETWORK_STATE_IDLE = 1;
   protected static final int NETWORK_STATE_LOADING = 2;
   protected static final int NETWORK_STATE_LOADED = 3;
   protected static final int NETWORK_STATE_FORMAT_ERROR = 4;
   protected static final int NETWORK_STATE_NETWORK_ERROR = 5;
   protected static final int NETWORK_STATE_DECODE_ERROR = 6;
   protected static final int READY_STATE_HAVE_NOTHING = 0;
   protected static final int READY_STATE_HAVE_METADATA = 1;
   protected static final int READY_STATE_HAVE_CURRENT_DATA = 2;
   protected static final int READY_STATE_HAVE_FUTURE_DATA = 3;
   protected static final int READY_STATE_HAVE_ENOUGH_DATA = 4;
   protected static final int PRELOAD_NONE = 0;
   protected static final int PRELOAD_METADATA = 1;
   protected static final int PRELOAD_AUTO = 2;
   private int networkState = 0;
   private int readyState = 0;
   private int preload = 2;
   private boolean paused = true;
   private boolean seeking = false;
   private Runnable newFrameNotifier = () -> {
      if (this.nPtr != 0L) {
         this.notifyNewFrame(this.nPtr);
      }

   };
   private boolean preserve = true;

   protected WCMediaPlayer() {
   }

   void setNativePointer(long var1) {
      if (var1 == 0L) {
         throw new IllegalArgumentException("nativePointer is 0");
      } else if (this.nPtr != 0L) {
         throw new IllegalStateException("nPtr is not 0");
      } else {
         this.nPtr = var1;
      }
   }

   protected abstract void load(String var1, String var2);

   protected abstract void cancelLoad();

   protected abstract void disposePlayer();

   protected abstract void prepareToPlay();

   protected abstract void play();

   protected abstract void pause();

   protected abstract float getCurrentTime();

   protected abstract void seek(float var1);

   protected abstract void setRate(float var1);

   protected abstract void setVolume(float var1);

   protected abstract void setMute(boolean var1);

   protected abstract void setSize(int var1, int var2);

   protected abstract void setPreservesPitch(boolean var1);

   protected abstract void renderCurrentFrame(WCGraphicsContext var1, int var2, int var3, int var4, int var5);

   protected boolean getPreservesPitch() {
      return this.preserve;
   }

   protected int getNetworkState() {
      return this.networkState;
   }

   protected int getReadyState() {
      return this.readyState;
   }

   protected int getPreload() {
      return this.preload;
   }

   protected boolean isPaused() {
      return this.paused;
   }

   protected boolean isSeeking() {
      return this.seeking;
   }

   protected void notifyNetworkStateChanged(int var1) {
      if (this.networkState != var1) {
         this.networkState = var1;
         Invoker.getInvoker().invokeOnEventThread(() -> {
            if (this.nPtr != 0L) {
               this.notifyNetworkStateChanged(this.nPtr, var1);
            }

         });
      }

   }

   protected void notifyReadyStateChanged(int var1) {
      if (this.readyState != var1) {
         this.readyState = var1;
         Invoker.getInvoker().invokeOnEventThread(() -> {
            if (this.nPtr != 0L) {
               this.notifyReadyStateChanged(this.nPtr, var1);
            }

         });
      }

   }

   protected void notifyPaused(boolean var1) {
      if (verbose) {
         log.log(Level.FINE, "notifyPaused, {0} => {1}", new Object[]{this.paused, var1});
      }

      if (this.paused != var1) {
         this.paused = var1;
         Invoker.getInvoker().invokeOnEventThread(() -> {
            if (this.nPtr != 0L) {
               this.notifyPaused(this.nPtr, var1);
            }

         });
      }

   }

   protected void notifySeeking(boolean var1, int var2) {
      if (verbose) {
         log.log(Level.FINE, "notifySeeking, {0} => {1}", new Object[]{this.seeking, var1});
      }

      if (this.seeking != var1 || this.readyState != var2) {
         this.seeking = var1;
         this.readyState = var2;
         Invoker.getInvoker().invokeOnEventThread(() -> {
            if (this.nPtr != 0L) {
               this.notifySeeking(this.nPtr, var1, var2);
            }

         });
      }

   }

   protected void notifyFinished() {
      Invoker.getInvoker().invokeOnEventThread(() -> {
         if (this.nPtr != 0L) {
            this.notifyFinished(this.nPtr);
         }

      });
   }

   protected void notifyReady(boolean var1, boolean var2, float var3) {
      Invoker.getInvoker().invokeOnEventThread(() -> {
         if (this.nPtr != 0L) {
            this.notifyReady(this.nPtr, var1, var2, var3);
         }

      });
   }

   protected void notifyDurationChanged(float var1) {
      Invoker.getInvoker().invokeOnEventThread(() -> {
         if (this.nPtr != 0L) {
            this.notifyDurationChanged(this.nPtr, var1);
         }

      });
   }

   protected void notifySizeChanged(int var1, int var2) {
      Invoker.getInvoker().invokeOnEventThread(() -> {
         if (this.nPtr != 0L) {
            this.notifySizeChanged(this.nPtr, var1, var2);
         }

      });
   }

   protected void notifyNewFrame() {
      Invoker.getInvoker().invokeOnEventThread(this.newFrameNotifier);
   }

   protected void notifyBufferChanged(float[] var1, int var2) {
      Invoker.getInvoker().invokeOnEventThread(() -> {
         if (this.nPtr != 0L) {
            this.notifyBufferChanged(this.nPtr, var1, var2);
         }

      });
   }

   private void fwkLoad(String var1, String var2) {
      if (verbose) {
         log.log(Level.FINE, "fwkLoad, url={0}, userAgent={1}", new Object[]{var1, var2});
      }

      this.load(var1, var2);
   }

   private void fwkCancelLoad() {
      if (verbose) {
         log.log(Level.FINE, "fwkCancelLoad");
      }

      this.cancelLoad();
   }

   private void fwkPrepareToPlay() {
      if (verbose) {
         log.log(Level.FINE, "fwkPrepareToPlay");
      }

      this.prepareToPlay();
   }

   private void fwkDispose() {
      if (verbose) {
         log.log(Level.FINE, "fwkDispose");
      }

      this.nPtr = 0L;
      this.cancelLoad();
      this.disposePlayer();
   }

   private void fwkPlay() {
      if (verbose) {
         log.log(Level.FINE, "fwkPlay");
      }

      this.play();
   }

   private void fwkPause() {
      if (verbose) {
         log.log(Level.FINE, "fwkPause");
      }

      this.pause();
   }

   private float fwkGetCurrentTime() {
      float var1 = this.getCurrentTime();
      if (verbose) {
         log.log(Level.FINER, "fwkGetCurrentTime(), return {0}", var1);
      }

      return var1;
   }

   private void fwkSeek(float var1) {
      if (verbose) {
         log.log(Level.FINE, "fwkSeek({0})", var1);
      }

      this.seek(var1);
   }

   private void fwkSetRate(float var1) {
      if (verbose) {
         log.log(Level.FINE, "fwkSetRate({0})", var1);
      }

      this.setRate(var1);
   }

   private void fwkSetVolume(float var1) {
      if (verbose) {
         log.log(Level.FINE, "fwkSetVolume({0})", var1);
      }

      this.setVolume(var1);
   }

   private void fwkSetMute(boolean var1) {
      if (verbose) {
         log.log(Level.FINE, "fwkSetMute({0})", var1);
      }

      this.setMute(var1);
   }

   private void fwkSetSize(int var1, int var2) {
      this.setSize(var1, var2);
   }

   private void fwkSetPreservesPitch(boolean var1) {
      if (verbose) {
         log.log(Level.FINE, "setPreservesPitch({0})", var1);
      }

      this.preserve = var1;
      this.setPreservesPitch(var1);
   }

   private void fwkSetPreload(int var1) {
      if (verbose) {
         log.log(Level.FINE, "fwkSetPreload({0})", var1 == 0 ? "PRELOAD_NONE" : (var1 == 1 ? "PRELOAD_METADATA" : (var1 == 2 ? "PRELOAD_AUTO" : "INVALID VALUE: " + var1)));
      }

      this.preload = var1;
   }

   void render(WCGraphicsContext var1, int var2, int var3, int var4, int var5) {
      if (verbose) {
         log.log(Level.FINER, "render(x={0}, y={1}, w={2}, h={3}", new Object[]{var2, var3, var4, var5});
      }

      this.renderCurrentFrame(var1, var2, var3, var4, var5);
   }

   private native void notifyNetworkStateChanged(long var1, int var3);

   private native void notifyReadyStateChanged(long var1, int var3);

   private native void notifyPaused(long var1, boolean var3);

   private native void notifySeeking(long var1, boolean var3, int var4);

   private native void notifyFinished(long var1);

   private native void notifyReady(long var1, boolean var3, boolean var4, float var5);

   private native void notifyDurationChanged(long var1, float var3);

   private native void notifySizeChanged(long var1, int var3, int var4);

   private native void notifyNewFrame(long var1);

   private native void notifyBufferChanged(long var1, float[] var3, int var4);

   static {
      if (log.getLevel() == null) {
         verbose = false;
         log.setLevel(Level.OFF);
      } else {
         verbose = true;
         log.log(Level.CONFIG, "webkit.mediaplayer logging is ON, level: {0}", log.getLevel());
      }

   }
}
