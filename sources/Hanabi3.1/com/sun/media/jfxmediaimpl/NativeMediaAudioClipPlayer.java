package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.MediaManager;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

final class NativeMediaAudioClipPlayer implements PlayerStateListener, MediaErrorListener {
   private MediaPlayer mediaPlayer;
   private int playCount;
   private int loopCount;
   private boolean playing;
   private boolean ready;
   private NativeMediaAudioClip sourceClip;
   private double volume;
   private double balance;
   private double pan;
   private double rate;
   private int priority;
   private final ReentrantLock playerStateLock = new ReentrantLock();
   private static final int MAX_PLAYER_COUNT = 16;
   private static final List activePlayers = new ArrayList(16);
   private static final ReentrantLock playerListLock = new ReentrantLock();
   private static final LinkedBlockingQueue schedule = new LinkedBlockingQueue();

   public static int getPlayerLimit() {
      return 16;
   }

   public static int getPlayerCount() {
      return activePlayers.size();
   }

   private static void clipScheduler() {
      while(true) {
         SchedulerEntry var0 = null;

         try {
            var0 = (SchedulerEntry)schedule.take();
         } catch (InterruptedException var8) {
         }

         if (null != var0) {
            if (var0.getCommand() == 0) {
               NativeMediaAudioClipPlayer var1 = var0.getPlayer();
               if (null != var1) {
                  if (addPlayer(var1)) {
                     var1.play();
                  } else {
                     var1.sourceClip.playFinished();
                  }
               }
            } else if (var0.getCommand() == 1) {
               URI var10 = var0.getClipURI();
               playerListLock.lock();

               try {
                  NativeMediaAudioClipPlayer[] var2 = new NativeMediaAudioClipPlayer[16];
                  var2 = (NativeMediaAudioClipPlayer[])activePlayers.toArray(var2);
                  if (null != var2) {
                     for(int var3 = 0; var3 < var2.length; ++var3) {
                        if (null != var2[var3] && (null == var10 || var2[var3].source().getURI().equals(var10))) {
                           var2[var3].invalidate();
                        }
                     }
                  }
               } finally {
                  playerListLock.unlock();
               }

               boolean var11 = null == var10;
               Iterator var12 = schedule.iterator();

               label151:
               while(true) {
                  SchedulerEntry var4;
                  NativeMediaAudioClipPlayer var5;
                  do {
                     if (!var12.hasNext()) {
                        break label151;
                     }

                     var4 = (SchedulerEntry)var12.next();
                     var5 = var4.getPlayer();
                  } while(!var11 && (null == var5 || !var5.sourceClip.getLocator().getURI().equals(var10)));

                  schedule.remove(var4);
                  var5.sourceClip.playFinished();
               }
            } else if (var0.getCommand() == 2) {
               var0.getMediaPlayer().dispose();
            }

            var0.signal();
         }
      }
   }

   public static void playClip(NativeMediaAudioClip var0, double var1, double var3, double var5, double var7, int var9, int var10) {
      NativeMediaAudioClipPlayer.Enthreaderator.schedulerThread;
      NativeMediaAudioClipPlayer var11 = new NativeMediaAudioClipPlayer(var0, var1, var3, var5, var7, var9, var10);
      SchedulerEntry var12 = new SchedulerEntry(var11);
      boolean var13 = schedule.contains(var12);
      if (var13 || !schedule.offer(var12)) {
         if (Logger.canLog(1) && !var13) {
            Logger.logMsg(1, "AudioClip could not be scheduled for playback!");
         }

         var0.playFinished();
      }

   }

   private static boolean addPlayer(NativeMediaAudioClipPlayer var0) {
      playerListLock.lock();

      try {
         int var1 = var0.priority();

         label104:
         while(activePlayers.size() >= 16) {
            NativeMediaAudioClipPlayer var2 = null;
            Iterator var3 = activePlayers.iterator();

            while(true) {
               NativeMediaAudioClipPlayer var4;
               do {
                  do {
                     if (!var3.hasNext()) {
                        if (null == var2) {
                           boolean var8 = false;
                           return var8;
                        }

                        var2.invalidate();
                        continue label104;
                     }

                     var4 = (NativeMediaAudioClipPlayer)var3.next();
                  } while(var4.priority() > var1);
               } while(var2 != null && (!var2.isReady() || var4.priority() >= var2.priority()));

               var2 = var4;
            }
         }

         activePlayers.add(var0);
      } finally {
         playerListLock.unlock();
      }

      return true;
   }

   public static void stopPlayers(Locator var0) {
      URI var1 = var0 != null ? var0.getURI() : null;
      if (null != NativeMediaAudioClipPlayer.Enthreaderator.schedulerThread) {
         CountDownLatch var2 = new CountDownLatch(1);
         SchedulerEntry var3 = new SchedulerEntry(var1, var2);
         if (schedule.offer(var3)) {
            try {
               var2.await(5L, TimeUnit.SECONDS);
            } catch (InterruptedException var5) {
            }
         }
      }

   }

   private NativeMediaAudioClipPlayer(NativeMediaAudioClip var1, double var2, double var4, double var6, double var8, int var10, int var11) {
      this.sourceClip = var1;
      this.volume = var2;
      this.balance = var4;
      this.pan = var8;
      this.rate = var6;
      this.loopCount = var10;
      this.priority = var11;
      this.ready = false;
   }

   private Locator source() {
      return this.sourceClip.getLocator();
   }

   public double volume() {
      return this.volume;
   }

   public void setVolume(double var1) {
      this.volume = var1;
   }

   public double balance() {
      return this.balance;
   }

   public void setBalance(double var1) {
      this.balance = var1;
   }

   public double pan() {
      return this.pan;
   }

   public void setPan(double var1) {
      this.pan = var1;
   }

   public double playbackRate() {
      return this.rate;
   }

   public void setPlaybackRate(double var1) {
      this.rate = var1;
   }

   public int priority() {
      return this.priority;
   }

   public void setPriority(int var1) {
      this.priority = var1;
   }

   public int loopCount() {
      return this.loopCount;
   }

   public void setLoopCount(int var1) {
      this.loopCount = var1;
   }

   public boolean isPlaying() {
      return this.playing;
   }

   private boolean isReady() {
      return this.ready;
   }

   public synchronized void play() {
      this.playerStateLock.lock();

      try {
         this.playing = true;
         this.playCount = 0;
         if (null == this.mediaPlayer) {
            this.mediaPlayer = MediaManager.getPlayer(this.source());
            this.mediaPlayer.addMediaPlayerListener(this);
            this.mediaPlayer.addMediaErrorListener(this);
         } else {
            this.mediaPlayer.play();
         }
      } finally {
         this.playerStateLock.unlock();
      }

   }

   public void stop() {
      this.invalidate();
   }

   public synchronized void invalidate() {
      this.playerStateLock.lock();
      playerListLock.lock();

      try {
         this.playing = false;
         this.playCount = 0;
         this.ready = false;
         activePlayers.remove(this);
         this.sourceClip.playFinished();
         if (null != this.mediaPlayer) {
            this.mediaPlayer.removeMediaPlayerListener(this);
            this.mediaPlayer.setMute(true);
            SchedulerEntry var1 = new SchedulerEntry(this.mediaPlayer);
            if (!schedule.offer(var1)) {
               this.mediaPlayer.dispose();
            }

            this.mediaPlayer = null;
         }
      } catch (Throwable var5) {
      } finally {
         playerListLock.unlock();
         this.playerStateLock.unlock();
      }

   }

   public void onReady(PlayerStateEvent var1) {
      this.playerStateLock.lock();

      try {
         this.ready = true;
         if (this.playing) {
            this.mediaPlayer.setVolume((float)this.volume);
            this.mediaPlayer.setBalance((float)this.balance);
            this.mediaPlayer.setRate((float)this.rate);
            this.mediaPlayer.play();
         }
      } finally {
         this.playerStateLock.unlock();
      }

   }

   public void onPlaying(PlayerStateEvent var1) {
   }

   public void onPause(PlayerStateEvent var1) {
   }

   public void onStop(PlayerStateEvent var1) {
      this.invalidate();
   }

   public void onStall(PlayerStateEvent var1) {
   }

   public void onFinish(PlayerStateEvent var1) {
      this.playerStateLock.lock();

      try {
         if (this.playing) {
            if (this.loopCount != -1) {
               ++this.playCount;
               if (this.playCount <= this.loopCount) {
                  this.mediaPlayer.seek(0.0);
               } else {
                  this.invalidate();
               }
            } else {
               this.mediaPlayer.seek(0.0);
            }
         }
      } finally {
         this.playerStateLock.unlock();
      }

   }

   public void onHalt(PlayerStateEvent var1) {
      this.invalidate();
   }

   public void onWarning(Object var1, String var2) {
   }

   public void onError(Object var1, int var2, String var3) {
      if (Logger.canLog(4)) {
         Logger.logMsg(4, "Error with AudioClip player: code " + var2 + " : " + var3);
      }

      this.invalidate();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof NativeMediaAudioClipPlayer)) {
         return false;
      } else {
         NativeMediaAudioClipPlayer var2 = (NativeMediaAudioClipPlayer)var1;
         URI var3 = this.sourceClip.getLocator().getURI();
         URI var4 = var2.sourceClip.getLocator().getURI();
         return var3.equals(var4) && this.priority == var2.priority && this.loopCount == var2.loopCount && Double.compare(this.volume, var2.volume) == 0 && Double.compare(this.balance, var2.balance) == 0 && Double.compare(this.rate, var2.rate) == 0 && Double.compare(this.pan, var2.pan) == 0;
      }
   }

   private static class SchedulerEntry {
      private final int command;
      private final NativeMediaAudioClipPlayer player;
      private final URI clipURI;
      private final CountDownLatch commandSignal;
      private final MediaPlayer mediaPlayer;

      public SchedulerEntry(NativeMediaAudioClipPlayer var1) {
         this.command = 0;
         this.player = var1;
         this.clipURI = null;
         this.commandSignal = null;
         this.mediaPlayer = null;
      }

      public SchedulerEntry(URI var1, CountDownLatch var2) {
         this.command = 1;
         this.player = null;
         this.clipURI = var1;
         this.commandSignal = var2;
         this.mediaPlayer = null;
      }

      public SchedulerEntry(MediaPlayer var1) {
         this.command = 2;
         this.player = null;
         this.clipURI = null;
         this.commandSignal = null;
         this.mediaPlayer = var1;
      }

      public int getCommand() {
         return this.command;
      }

      public NativeMediaAudioClipPlayer getPlayer() {
         return this.player;
      }

      public URI getClipURI() {
         return this.clipURI;
      }

      public MediaPlayer getMediaPlayer() {
         return this.mediaPlayer;
      }

      public void signal() {
         if (null != this.commandSignal) {
            this.commandSignal.countDown();
         }

      }

      public boolean equals(Object var1) {
         return var1 instanceof SchedulerEntry && null != this.player ? this.player.equals(((SchedulerEntry)var1).getPlayer()) : false;
      }
   }

   private static class Enthreaderator {
      private static final Thread schedulerThread = new Thread(() -> {
         NativeMediaAudioClipPlayer.clipScheduler();
      });

      public static Thread getSchedulerThread() {
         return schedulerThread;
      }

      static {
         schedulerThread.setDaemon(true);
         schedulerThread.start();
      }
   }
}
