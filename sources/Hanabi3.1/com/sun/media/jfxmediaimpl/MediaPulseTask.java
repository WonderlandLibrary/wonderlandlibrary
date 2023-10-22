package com.sun.media.jfxmediaimpl;

import java.lang.ref.WeakReference;
import java.util.TimerTask;

class MediaPulseTask extends TimerTask {
   WeakReference playerRef;

   MediaPulseTask(NativeMediaPlayer var1) {
      this.playerRef = new WeakReference(var1);
   }

   public void run() {
      NativeMediaPlayer var1 = (NativeMediaPlayer)this.playerRef.get();
      if (var1 != null) {
         if (!var1.doMediaPulseTask()) {
            this.cancel();
         }
      } else {
         this.cancel();
      }

   }
}
