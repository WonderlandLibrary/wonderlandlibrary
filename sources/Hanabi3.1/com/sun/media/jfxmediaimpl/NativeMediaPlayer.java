package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.control.VideoRenderControl;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.events.AudioSpectrumEvent;
import com.sun.media.jfxmedia.events.AudioSpectrumListener;
import com.sun.media.jfxmedia.events.BufferListener;
import com.sun.media.jfxmedia.events.BufferProgressEvent;
import com.sun.media.jfxmedia.events.MarkerEvent;
import com.sun.media.jfxmedia.events.MarkerListener;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.events.NewFrameEvent;
import com.sun.media.jfxmedia.events.PlayerEvent;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import com.sun.media.jfxmedia.events.PlayerTimeListener;
import com.sun.media.jfxmedia.events.VideoFrameRateListener;
import com.sun.media.jfxmedia.events.VideoRendererListener;
import com.sun.media.jfxmedia.events.VideoTrackSizeListener;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmedia.track.AudioTrack;
import com.sun.media.jfxmedia.track.SubtitleTrack;
import com.sun.media.jfxmedia.track.Track;
import com.sun.media.jfxmedia.track.VideoResolution;
import com.sun.media.jfxmedia.track.VideoTrack;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class NativeMediaPlayer implements MediaPlayer, MarkerStateListener {
   public static final int eventPlayerUnknown = 100;
   public static final int eventPlayerReady = 101;
   public static final int eventPlayerPlaying = 102;
   public static final int eventPlayerPaused = 103;
   public static final int eventPlayerStopped = 104;
   public static final int eventPlayerStalled = 105;
   public static final int eventPlayerFinished = 106;
   public static final int eventPlayerError = 107;
   private static final int NOMINAL_VIDEO_FPS = 30;
   public static final long ONE_SECOND = 1000000000L;
   private NativeMedia media;
   private VideoRenderControl videoRenderControl;
   private final List errorListeners = new ArrayList();
   private final List playerStateListeners = new ArrayList();
   private final List playerTimeListeners = new ArrayList();
   private final List videoTrackSizeListeners = new ArrayList();
   private final List videoUpdateListeners = new ArrayList();
   private final List videoFrameRateListeners = new ArrayList();
   private final List markerListeners = new ArrayList();
   private final List bufferListeners = new ArrayList();
   private final List audioSpectrumListeners = new ArrayList();
   private final List cachedStateEvents = new ArrayList();
   private final List cachedTimeEvents = new ArrayList();
   private final List cachedBufferEvents = new ArrayList();
   private final List cachedErrorEvents = new ArrayList();
   private boolean isFirstFrame = true;
   private NewFrameEvent firstFrameEvent = null;
   private double firstFrameTime;
   private final Object firstFrameLock = new Object();
   private EventQueueThread eventLoop = new EventQueueThread();
   private int frameWidth = -1;
   private int frameHeight = -1;
   private final AtomicBoolean isMediaPulseEnabled = new AtomicBoolean(false);
   private final Lock mediaPulseLock = new ReentrantLock();
   private Timer mediaPulseTimer;
   private final Lock markerLock = new ReentrantLock();
   private boolean checkSeek = false;
   private double timeBeforeSeek = 0.0;
   private double timeAfterSeek = 0.0;
   private double previousTime = 0.0;
   private double firedMarkerTime = -1.0;
   private double startTime = 0.0;
   private double stopTime = Double.POSITIVE_INFINITY;
   private boolean isStartTimeUpdated = false;
   private boolean isStopTimeSet = false;
   private double encodedFrameRate = 0.0;
   private boolean recomputeFrameRate = true;
   private double previousFrameTime;
   private long numFramesSincePlaying;
   private double meanFrameDuration;
   private double decodedFrameRate;
   private PlayerStateEvent.PlayerState playerState;
   private final Lock disposeLock;
   private boolean isDisposed;
   private Runnable onDispose;

   protected NativeMediaPlayer(NativeMedia var1) {
      this.playerState = PlayerStateEvent.PlayerState.UNKNOWN;
      this.disposeLock = new ReentrantLock();
      this.isDisposed = false;
      if (var1 == null) {
         throw new IllegalArgumentException("clip == null!");
      } else {
         this.media = var1;
         this.videoRenderControl = new VideoRenderer();
      }
   }

   protected void init() {
      this.media.addMarkerStateListener(this);
      this.eventLoop.start();
   }

   void setOnDispose(Runnable var1) {
      this.disposeLock.lock();

      try {
         if (!this.isDisposed) {
            this.onDispose = var1;
         }
      } finally {
         this.disposeLock.unlock();
      }

   }

   private synchronized void onNativeInit() {
      try {
         this.playerInit();
      } catch (MediaException var2) {
         this.sendPlayerMediaErrorEvent(var2.getMediaError().code());
      }

   }

   public void addMediaErrorListener(MediaErrorListener var1) {
      if (var1 != null) {
         this.errorListeners.add(new WeakReference(var1));
         synchronized(this.cachedErrorEvents) {
            if (!this.cachedErrorEvents.isEmpty() && !this.errorListeners.isEmpty()) {
               this.cachedErrorEvents.stream().forEach((var1x) -> {
                  this.sendPlayerEvent(var1x);
               });
               this.cachedErrorEvents.clear();
            }
         }
      }

   }

   public void removeMediaErrorListener(MediaErrorListener var1) {
      if (var1 != null) {
         ListIterator var2 = this.errorListeners.listIterator();

         while(true) {
            MediaErrorListener var3;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = (MediaErrorListener)((WeakReference)var2.next()).get();
            } while(var3 != null && var3 != var1);

            var2.remove();
         }
      }
   }

   public void addMediaPlayerListener(PlayerStateListener var1) {
      if (var1 != null) {
         synchronized(this.cachedStateEvents) {
            if (!this.cachedStateEvents.isEmpty() && this.playerStateListeners.isEmpty()) {
               Iterator var3 = this.cachedStateEvents.iterator();

               while(var3.hasNext()) {
                  PlayerStateEvent var4 = (PlayerStateEvent)var3.next();
                  switch (var4.getState()) {
                     case READY:
                        var1.onReady(var4);
                        break;
                     case PLAYING:
                        var1.onPlaying(var4);
                        break;
                     case STOPPED:
                        var1.onStop(var4);
                        break;
                     case FINISHED:
                        var1.onFinish(var4);
                        break;
                     case PAUSED:
                        var1.onPause(var4);
                        break;
                     case STALLED:
                        var1.onStall(var4);
                        break;
                     case HALTED:
                        var1.onHalt(var4);
                  }
               }

               this.cachedStateEvents.clear();
            }

            this.playerStateListeners.add(new WeakReference(var1));
         }
      }

   }

   public void removeMediaPlayerListener(PlayerStateListener var1) {
      if (var1 != null) {
         ListIterator var2 = this.playerStateListeners.listIterator();

         while(true) {
            PlayerStateListener var3;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = (PlayerStateListener)((WeakReference)var2.next()).get();
            } while(var3 != null && var3 != var1);

            var2.remove();
         }
      }
   }

   public void addMediaTimeListener(PlayerTimeListener var1) {
      if (var1 != null) {
         synchronized(this.cachedTimeEvents) {
            if (!this.cachedTimeEvents.isEmpty() && this.playerTimeListeners.isEmpty()) {
               Iterator var7 = this.cachedTimeEvents.iterator();

               while(var7.hasNext()) {
                  PlayerTimeEvent var4 = (PlayerTimeEvent)var7.next();
                  var1.onDurationChanged(var4.getTime());
               }

               this.cachedTimeEvents.clear();
            } else {
               double var3 = this.getDuration();
               if (var3 != Double.POSITIVE_INFINITY) {
                  var1.onDurationChanged(var3);
               }
            }

            this.playerTimeListeners.add(new WeakReference(var1));
         }
      }

   }

   public void removeMediaTimeListener(PlayerTimeListener var1) {
      if (var1 != null) {
         ListIterator var2 = this.playerTimeListeners.listIterator();

         while(true) {
            PlayerTimeListener var3;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = (PlayerTimeListener)((WeakReference)var2.next()).get();
            } while(var3 != null && var3 != var1);

            var2.remove();
         }
      }
   }

   public void addVideoTrackSizeListener(VideoTrackSizeListener var1) {
      if (var1 != null) {
         if (this.frameWidth != -1 && this.frameHeight != -1) {
            var1.onSizeChanged(this.frameWidth, this.frameHeight);
         }

         this.videoTrackSizeListeners.add(new WeakReference(var1));
      }

   }

   public void removeVideoTrackSizeListener(VideoTrackSizeListener var1) {
      if (var1 != null) {
         ListIterator var2 = this.videoTrackSizeListeners.listIterator();

         while(true) {
            VideoTrackSizeListener var3;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = (VideoTrackSizeListener)((WeakReference)var2.next()).get();
            } while(var3 != null && var3 != var1);

            var2.remove();
         }
      }
   }

   public void addMarkerListener(MarkerListener var1) {
      if (var1 != null) {
         this.markerListeners.add(new WeakReference(var1));
      }

   }

   public void removeMarkerListener(MarkerListener var1) {
      if (var1 != null) {
         ListIterator var2 = this.markerListeners.listIterator();

         while(true) {
            MarkerListener var3;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = (MarkerListener)((WeakReference)var2.next()).get();
            } while(var3 != null && var3 != var1);

            var2.remove();
         }
      }
   }

   public void addBufferListener(BufferListener var1) {
      if (var1 != null) {
         synchronized(this.cachedBufferEvents) {
            if (!this.cachedBufferEvents.isEmpty() && this.bufferListeners.isEmpty()) {
               this.cachedBufferEvents.stream().forEach((var1x) -> {
                  var1.onBufferProgress(var1x);
               });
               this.cachedBufferEvents.clear();
            }

            this.bufferListeners.add(new WeakReference(var1));
         }
      }

   }

   public void removeBufferListener(BufferListener var1) {
      if (var1 != null) {
         ListIterator var2 = this.bufferListeners.listIterator();

         while(true) {
            BufferListener var3;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = (BufferListener)((WeakReference)var2.next()).get();
            } while(var3 != null && var3 != var1);

            var2.remove();
         }
      }
   }

   public void addAudioSpectrumListener(AudioSpectrumListener var1) {
      if (var1 != null) {
         this.audioSpectrumListeners.add(new WeakReference(var1));
      }

   }

   public void removeAudioSpectrumListener(AudioSpectrumListener var1) {
      if (var1 != null) {
         ListIterator var2 = this.audioSpectrumListeners.listIterator();

         while(true) {
            AudioSpectrumListener var3;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = (AudioSpectrumListener)((WeakReference)var2.next()).get();
            } while(var3 != null && var3 != var1);

            var2.remove();
         }
      }
   }

   public VideoRenderControl getVideoRenderControl() {
      return this.videoRenderControl;
   }

   public Media getMedia() {
      return this.media;
   }

   public void setAudioSyncDelay(long var1) {
      try {
         this.playerSetAudioSyncDelay(var1);
      } catch (MediaException var4) {
         this.sendPlayerEvent(new MediaErrorEvent(this, var4.getMediaError()));
      }

   }

   public long getAudioSyncDelay() {
      try {
         return this.playerGetAudioSyncDelay();
      } catch (MediaException var2) {
         this.sendPlayerEvent(new MediaErrorEvent(this, var2.getMediaError()));
         return 0L;
      }
   }

   public void play() {
      try {
         if (this.isStartTimeUpdated) {
            this.playerSeek(this.startTime);
         }

         this.isMediaPulseEnabled.set(true);
         this.playerPlay();
      } catch (MediaException var2) {
         this.sendPlayerEvent(new MediaErrorEvent(this, var2.getMediaError()));
      }

   }

   public void stop() {
      try {
         this.playerStop();
         this.playerSeek(this.startTime);
      } catch (MediaException var2) {
         MediaUtils.warning(this, "stop() failed!");
      }

   }

   public void pause() {
      try {
         this.playerPause();
      } catch (MediaException var2) {
         this.sendPlayerEvent(new MediaErrorEvent(this, var2.getMediaError()));
      }

   }

   public float getRate() {
      try {
         return this.playerGetRate();
      } catch (MediaException var2) {
         this.sendPlayerEvent(new MediaErrorEvent(this, var2.getMediaError()));
         return 0.0F;
      }
   }

   public void setRate(float var1) {
      try {
         this.playerSetRate(var1);
      } catch (MediaException var3) {
         MediaUtils.warning(this, "setRate(" + var1 + ") failed!");
      }

   }

   public double getPresentationTime() {
      try {
         return this.playerGetPresentationTime();
      } catch (MediaException var2) {
         return -1.0;
      }
   }

   public float getVolume() {
      try {
         return this.playerGetVolume();
      } catch (MediaException var2) {
         this.sendPlayerEvent(new MediaErrorEvent(this, var2.getMediaError()));
         return 0.0F;
      }
   }

   public void setVolume(float var1) {
      if (var1 < 0.0F) {
         var1 = 0.0F;
      } else if (var1 > 1.0F) {
         var1 = 1.0F;
      }

      try {
         this.playerSetVolume(var1);
      } catch (MediaException var3) {
         this.sendPlayerEvent(new MediaErrorEvent(this, var3.getMediaError()));
      }

   }

   public boolean getMute() {
      try {
         return this.playerGetMute();
      } catch (MediaException var2) {
         this.sendPlayerEvent(new MediaErrorEvent(this, var2.getMediaError()));
         return false;
      }
   }

   public void setMute(boolean var1) {
      try {
         this.playerSetMute(var1);
      } catch (MediaException var3) {
         this.sendPlayerEvent(new MediaErrorEvent(this, var3.getMediaError()));
      }

   }

   public float getBalance() {
      try {
         return this.playerGetBalance();
      } catch (MediaException var2) {
         this.sendPlayerEvent(new MediaErrorEvent(this, var2.getMediaError()));
         return 0.0F;
      }
   }

   public void setBalance(float var1) {
      if (var1 < -1.0F) {
         var1 = -1.0F;
      } else if (var1 > 1.0F) {
         var1 = 1.0F;
      }

      try {
         this.playerSetBalance(var1);
      } catch (MediaException var3) {
         this.sendPlayerEvent(new MediaErrorEvent(this, var3.getMediaError()));
      }

   }

   public abstract AudioEqualizer getEqualizer();

   public abstract AudioSpectrum getAudioSpectrum();

   public double getDuration() {
      try {
         return this.playerGetDuration();
      } catch (MediaException var2) {
         return Double.POSITIVE_INFINITY;
      }
   }

   public double getStartTime() {
      return this.startTime;
   }

   public void setStartTime(double var1) {
      try {
         this.markerLock.lock();
         this.startTime = var1;
         if (this.playerState != PlayerStateEvent.PlayerState.PLAYING && this.playerState != PlayerStateEvent.PlayerState.FINISHED && this.playerState != PlayerStateEvent.PlayerState.STOPPED) {
            this.playerSeek(var1);
         } else if (this.playerState == PlayerStateEvent.PlayerState.STOPPED) {
            this.isStartTimeUpdated = true;
         }
      } finally {
         this.markerLock.unlock();
      }

   }

   public double getStopTime() {
      return this.stopTime;
   }

   public void setStopTime(double var1) {
      try {
         this.markerLock.lock();
         this.stopTime = var1;
         this.isStopTimeSet = true;
         this.createMediaPulse();
      } finally {
         this.markerLock.unlock();
      }

   }

   public void seek(double var1) {
      if (this.playerState != PlayerStateEvent.PlayerState.STOPPED) {
         if (var1 < 0.0) {
            var1 = 0.0;
         } else {
            double var3 = this.getDuration();
            if (var3 >= 0.0 && var1 > var3) {
               var1 = var3;
            }
         }

         if (!this.isMediaPulseEnabled.get() && (this.playerState == PlayerStateEvent.PlayerState.PLAYING || this.playerState == PlayerStateEvent.PlayerState.PAUSED || this.playerState == PlayerStateEvent.PlayerState.FINISHED) && this.getStartTime() <= var1 && var1 <= this.getStopTime()) {
            this.isMediaPulseEnabled.set(true);
         }

         this.markerLock.lock();

         try {
            this.timeBeforeSeek = this.getPresentationTime();
            this.timeAfterSeek = var1;
            this.checkSeek = this.timeBeforeSeek != this.timeAfterSeek;
            this.previousTime = var1;
            this.firedMarkerTime = -1.0;

            try {
               this.playerSeek(var1);
            } catch (MediaException var8) {
               MediaUtils.warning(this, "seek(" + var1 + ") failed!");
            }
         } finally {
            this.markerLock.unlock();
         }

      }
   }

   protected abstract long playerGetAudioSyncDelay() throws MediaException;

   protected abstract void playerSetAudioSyncDelay(long var1) throws MediaException;

   protected abstract void playerPlay() throws MediaException;

   protected abstract void playerStop() throws MediaException;

   protected abstract void playerPause() throws MediaException;

   protected abstract void playerFinish() throws MediaException;

   protected abstract float playerGetRate() throws MediaException;

   protected abstract void playerSetRate(float var1) throws MediaException;

   protected abstract double playerGetPresentationTime() throws MediaException;

   protected abstract boolean playerGetMute() throws MediaException;

   protected abstract void playerSetMute(boolean var1) throws MediaException;

   protected abstract float playerGetVolume() throws MediaException;

   protected abstract void playerSetVolume(float var1) throws MediaException;

   protected abstract float playerGetBalance() throws MediaException;

   protected abstract void playerSetBalance(float var1) throws MediaException;

   protected abstract double playerGetDuration() throws MediaException;

   protected abstract void playerSeek(double var1) throws MediaException;

   protected abstract void playerInit() throws MediaException;

   protected abstract void playerDispose();

   public PlayerStateEvent.PlayerState getState() {
      return this.playerState;
   }

   public final void dispose() {
      this.disposeLock.lock();

      try {
         if (!this.isDisposed) {
            this.destroyMediaPulse();
            if (this.eventLoop != null) {
               this.eventLoop.terminateLoop();
               this.eventLoop = null;
            }

            synchronized(this.firstFrameLock) {
               if (this.firstFrameEvent != null) {
                  this.firstFrameEvent.getFrameData().releaseFrame();
                  this.firstFrameEvent = null;
               }
            }

            this.playerDispose();
            if (this.media != null) {
               this.media.dispose();
               this.media = null;
            }

            if (this.videoUpdateListeners != null) {
               ListIterator var1 = this.videoUpdateListeners.listIterator();

               while(var1.hasNext()) {
                  VideoRendererListener var2 = (VideoRendererListener)((WeakReference)var1.next()).get();
                  if (var2 != null) {
                     var2.releaseVideoFrames();
                  } else {
                     var1.remove();
                  }
               }

               this.videoUpdateListeners.clear();
            }

            if (this.playerStateListeners != null) {
               this.playerStateListeners.clear();
            }

            if (this.videoTrackSizeListeners != null) {
               this.videoTrackSizeListeners.clear();
            }

            if (this.videoFrameRateListeners != null) {
               this.videoFrameRateListeners.clear();
            }

            if (this.cachedStateEvents != null) {
               this.cachedStateEvents.clear();
            }

            if (this.cachedTimeEvents != null) {
               this.cachedTimeEvents.clear();
            }

            if (this.cachedBufferEvents != null) {
               this.cachedBufferEvents.clear();
            }

            if (this.errorListeners != null) {
               this.errorListeners.clear();
            }

            if (this.playerTimeListeners != null) {
               this.playerTimeListeners.clear();
            }

            if (this.markerListeners != null) {
               this.markerListeners.clear();
            }

            if (this.bufferListeners != null) {
               this.bufferListeners.clear();
            }

            if (this.audioSpectrumListeners != null) {
               this.audioSpectrumListeners.clear();
            }

            if (this.videoRenderControl != null) {
               this.videoRenderControl = null;
            }

            if (this.onDispose != null) {
               this.onDispose.run();
            }

            this.isDisposed = true;
         }
      } finally {
         this.disposeLock.unlock();
      }

   }

   public boolean isErrorEventCached() {
      synchronized(this.cachedErrorEvents) {
         return !this.cachedErrorEvents.isEmpty();
      }
   }

   protected void sendWarning(int var1, String var2) {
      if (this.eventLoop != null) {
         String var3 = String.format("Internal media warning: %d", var1);
         if (var2 != null) {
            var3 = var3 + ": " + var2;
         }

         this.eventLoop.postEvent(new WarningEvent(this, var3));
      }

   }

   protected void sendPlayerEvent(PlayerEvent var1) {
      if (this.eventLoop != null) {
         this.eventLoop.postEvent(var1);
      }

   }

   protected void sendPlayerHaltEvent(String var1, double var2) {
      Logger.logMsg(4, var1);
      if (this.eventLoop != null) {
         this.eventLoop.postEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.HALTED, var2, var1));
      }

   }

   protected void sendPlayerMediaErrorEvent(int var1) {
      this.sendPlayerEvent(new MediaErrorEvent(this, MediaError.getFromCode(var1)));
   }

   protected void sendPlayerStateEvent(int var1, double var2) {
      switch (var1) {
         case 101:
            this.sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.READY, var2));
            break;
         case 102:
            this.sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.PLAYING, var2));
            break;
         case 103:
            this.sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.PAUSED, var2));
            break;
         case 104:
            this.sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.STOPPED, var2));
            break;
         case 105:
            this.sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.STALLED, var2));
            break;
         case 106:
            this.sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.FINISHED, var2));
      }

   }

   protected void sendNewFrameEvent(long var1) {
      NativeVideoBuffer var3 = NativeVideoBuffer.createVideoBuffer(var1);
      this.sendPlayerEvent(new NewFrameEvent(var3));
   }

   protected void sendFrameSizeChangedEvent(int var1, int var2) {
      this.sendPlayerEvent(new FrameSizeChangedEvent(var1, var2));
   }

   protected void sendAudioTrack(boolean var1, long var2, String var4, int var5, String var6, int var7, int var8, float var9) {
      Locale var10 = null;
      if (!var6.equals("und")) {
         var10 = new Locale(var6);
      }

      AudioTrack var11 = new AudioTrack(var1, var2, var4, var10, Track.Encoding.toEncoding(var5), var7, var8, var9);
      TrackEvent var12 = new TrackEvent(var11);
      this.sendPlayerEvent(var12);
   }

   protected void sendVideoTrack(boolean var1, long var2, String var4, int var5, int var6, int var7, float var8, boolean var9) {
      VideoTrack var10 = new VideoTrack(var1, var2, var4, (Locale)null, Track.Encoding.toEncoding(var5), new VideoResolution(var6, var7), var8, var9);
      TrackEvent var11 = new TrackEvent(var10);
      this.sendPlayerEvent(var11);
   }

   protected void sendSubtitleTrack(boolean var1, long var2, String var4, int var5, String var6) {
      Locale var7 = null;
      if (null != var6) {
         var7 = new Locale(var6);
      }

      SubtitleTrack var8 = new SubtitleTrack(var1, var2, var4, var7, Track.Encoding.toEncoding(var5));
      this.sendPlayerEvent(new TrackEvent(var8));
   }

   protected void sendMarkerEvent(String var1, double var2) {
      this.sendPlayerEvent(new MarkerEvent(var1, var2));
   }

   protected void sendDurationUpdateEvent(double var1) {
      this.sendPlayerEvent(new PlayerTimeEvent(var1));
   }

   protected void sendBufferProgressEvent(double var1, long var3, long var5, long var7) {
      this.sendPlayerEvent(new BufferProgressEvent(var1, var3, var5, var7));
   }

   protected void sendAudioSpectrumEvent(double var1, double var3, boolean var5) {
      this.sendPlayerEvent(new AudioSpectrumEvent(this.getAudioSpectrum(), var1, var3, var5));
   }

   public void markerStateChanged(boolean var1) {
      if (var1) {
         this.markerLock.lock();

         try {
            this.previousTime = this.getPresentationTime();
         } finally {
            this.markerLock.unlock();
         }

         this.createMediaPulse();
      } else if (!this.isStopTimeSet) {
         this.destroyMediaPulse();
      }

   }

   private void createMediaPulse() {
      this.mediaPulseLock.lock();

      try {
         if (this.mediaPulseTimer == null) {
            this.mediaPulseTimer = new Timer(true);
            this.mediaPulseTimer.scheduleAtFixedRate(new MediaPulseTask(this), 0L, 40L);
         }
      } finally {
         this.mediaPulseLock.unlock();
      }

   }

   private void destroyMediaPulse() {
      this.mediaPulseLock.lock();

      try {
         if (this.mediaPulseTimer != null) {
            this.mediaPulseTimer.cancel();
            this.mediaPulseTimer = null;
         }
      } finally {
         this.mediaPulseLock.unlock();
      }

   }

   boolean doMediaPulseTask() {
      if (this.isMediaPulseEnabled.get()) {
         this.disposeLock.lock();
         if (this.isDisposed) {
            this.disposeLock.unlock();
            return false;
         } else {
            double var1 = this.getPresentationTime();
            this.markerLock.lock();

            try {
               if (this.checkSeek) {
                  boolean var3;
                  if (this.timeAfterSeek > this.timeBeforeSeek) {
                     if (!(var1 >= this.timeAfterSeek)) {
                        var3 = true;
                        return var3;
                     }

                     this.checkSeek = false;
                  } else if (this.timeAfterSeek < this.timeBeforeSeek) {
                     if (var1 >= this.timeBeforeSeek) {
                        var3 = true;
                        return var3;
                     }

                     this.checkSeek = false;
                  }
               }

               double var4;
               for(Map.Entry var12 = this.media.getNextMarker(this.previousTime, true); var12 != null; var12 = this.media.getNextMarker(var4, false)) {
                  var4 = (Double)var12.getKey();
                  if (var4 > var1) {
                     break;
                  }

                  if (var4 != this.firedMarkerTime && var4 >= this.previousTime && var4 >= this.getStartTime() && var4 <= this.getStopTime()) {
                     MarkerEvent var6 = new MarkerEvent((String)var12.getValue(), var4);
                     ListIterator var7 = this.markerListeners.listIterator();

                     while(var7.hasNext()) {
                        MarkerListener var8 = (MarkerListener)((WeakReference)var7.next()).get();
                        if (var8 != null) {
                           var8.onMarker(var6);
                        } else {
                           var7.remove();
                        }
                     }

                     this.firedMarkerTime = var4;
                  }
               }

               this.previousTime = var1;
               if (this.isStopTimeSet && var1 >= this.stopTime) {
                  this.playerFinish();
               }

               return true;
            } finally {
               this.disposeLock.unlock();
               this.markerLock.unlock();
            }
         }
      } else {
         return true;
      }
   }

   protected AudioEqualizer createNativeAudioEqualizer(long var1) {
      return new NativeAudioEqualizer(var1);
   }

   protected AudioSpectrum createNativeAudioSpectrum(long var1) {
      return new NativeAudioSpectrum(var1);
   }

   private class EventQueueThread extends Thread {
      private final BlockingQueue eventQueue = new LinkedBlockingQueue();
      private volatile boolean stopped = false;

      EventQueueThread() {
         this.setName("JFXMedia Player EventQueueThread");
         this.setDaemon(true);
      }

      public void run() {
         while(!this.stopped) {
            try {
               PlayerEvent var1 = (PlayerEvent)this.eventQueue.take();
               if (!this.stopped) {
                  if (var1 instanceof NewFrameEvent) {
                     try {
                        this.HandleRendererEvents((NewFrameEvent)var1);
                     } catch (Throwable var3) {
                        if (Logger.canLog(4)) {
                           Logger.logMsg(4, "Caught exception in HandleRendererEvents: " + var3.toString());
                        }
                     }
                  } else if (var1 instanceof PlayerStateEvent) {
                     this.HandleStateEvents((PlayerStateEvent)var1);
                  } else if (var1 instanceof FrameSizeChangedEvent) {
                     this.HandleFrameSizeChangedEvents((FrameSizeChangedEvent)var1);
                  } else if (var1 instanceof TrackEvent) {
                     this.HandleTrackEvents((TrackEvent)var1);
                  } else if (var1 instanceof MarkerEvent) {
                     this.HandleMarkerEvents((MarkerEvent)var1);
                  } else if (var1 instanceof WarningEvent) {
                     this.HandleWarningEvents((WarningEvent)var1);
                  } else if (var1 instanceof PlayerTimeEvent) {
                     this.HandlePlayerTimeEvents((PlayerTimeEvent)var1);
                  } else if (var1 instanceof BufferProgressEvent) {
                     this.HandleBufferEvents((BufferProgressEvent)var1);
                  } else if (var1 instanceof AudioSpectrumEvent) {
                     this.HandleAudioSpectrumEvents((AudioSpectrumEvent)var1);
                  } else if (var1 instanceof MediaErrorEvent) {
                     this.HandleErrorEvents((MediaErrorEvent)var1);
                  }
               }
            } catch (Exception var4) {
            }
         }

         this.eventQueue.clear();
      }

      private void HandleRendererEvents(NewFrameEvent var1) {
         if (NativeMediaPlayer.this.isFirstFrame) {
            NativeMediaPlayer.this.isFirstFrame = false;
            synchronized(NativeMediaPlayer.this.firstFrameLock) {
               NativeMediaPlayer.this.firstFrameEvent = var1;
               NativeMediaPlayer.this.firstFrameTime = NativeMediaPlayer.this.firstFrameEvent.getFrameData().getTimestamp();
               NativeMediaPlayer.this.firstFrameEvent.getFrameData().holdFrame();
            }
         } else if (NativeMediaPlayer.this.firstFrameEvent != null && NativeMediaPlayer.this.firstFrameTime != var1.getFrameData().getTimestamp()) {
            synchronized(NativeMediaPlayer.this.firstFrameLock) {
               NativeMediaPlayer.this.firstFrameEvent.getFrameData().releaseFrame();
               NativeMediaPlayer.this.firstFrameEvent = null;
            }
         }

         ListIterator var2 = NativeMediaPlayer.this.videoUpdateListeners.listIterator();

         while(var2.hasNext()) {
            VideoRendererListener var3 = (VideoRendererListener)((WeakReference)var2.next()).get();
            if (var3 != null) {
               var3.videoFrameUpdated(var1);
            } else {
               var2.remove();
            }
         }

         var1.getFrameData().releaseFrame();
         if (!NativeMediaPlayer.this.videoFrameRateListeners.isEmpty()) {
            double var12 = (double)System.nanoTime() / 1.0E9;
            if (NativeMediaPlayer.this.recomputeFrameRate) {
               NativeMediaPlayer.this.recomputeFrameRate = false;
               NativeMediaPlayer.this.previousFrameTime = var12;
               NativeMediaPlayer.this.numFramesSincePlaying = 1L;
            } else {
               boolean var4 = false;
               if (NativeMediaPlayer.this.numFramesSincePlaying == 1L) {
                  NativeMediaPlayer.this.meanFrameDuration = var12 - NativeMediaPlayer.this.previousFrameTime;
                  if (NativeMediaPlayer.this.meanFrameDuration > 0.0) {
                     NativeMediaPlayer.this.decodedFrameRate = 1.0 / NativeMediaPlayer.this.meanFrameDuration;
                     var4 = true;
                  }
               } else {
                  double var5 = NativeMediaPlayer.this.meanFrameDuration;
                  int var7 = NativeMediaPlayer.this.encodedFrameRate != 0.0 ? (int)(NativeMediaPlayer.this.encodedFrameRate + 0.5) : 30;
                  long var8 = NativeMediaPlayer.this.numFramesSincePlaying < (long)var7 ? NativeMediaPlayer.this.numFramesSincePlaying : (long)var7;
                  NativeMediaPlayer.this.meanFrameDuration = ((double)(var8 - 1L) * var5 + var12 - NativeMediaPlayer.this.previousFrameTime) / (double)var8;
                  if (NativeMediaPlayer.this.meanFrameDuration > 0.0 && Math.abs(NativeMediaPlayer.this.decodedFrameRate - 1.0 / NativeMediaPlayer.this.meanFrameDuration) > 0.5) {
                     NativeMediaPlayer.this.decodedFrameRate = 1.0 / NativeMediaPlayer.this.meanFrameDuration;
                     var4 = true;
                  }
               }

               if (var4) {
                  ListIterator var13 = NativeMediaPlayer.this.videoFrameRateListeners.listIterator();

                  while(var13.hasNext()) {
                     VideoFrameRateListener var6 = (VideoFrameRateListener)((WeakReference)var13.next()).get();
                     if (var6 != null) {
                        var6.onFrameRateChanged(NativeMediaPlayer.this.decodedFrameRate);
                     } else {
                        var13.remove();
                     }
                  }
               }

               NativeMediaPlayer.this.previousFrameTime = var12;
               NativeMediaPlayer.this.numFramesSincePlaying++;
            }
         }

      }

      private void HandleStateEvents(PlayerStateEvent var1) {
         NativeMediaPlayer.this.playerState = var1.getState();
         NativeMediaPlayer.this.recomputeFrameRate = PlayerStateEvent.PlayerState.PLAYING == var1.getState();
         switch (NativeMediaPlayer.this.playerState) {
            case READY:
               NativeMediaPlayer.this.onNativeInit();
               this.sendFakeBufferProgressEvent();
               break;
            case PLAYING:
               NativeMediaPlayer.this.isMediaPulseEnabled.set(true);
               break;
            case STOPPED:
            case FINISHED:
               NativeMediaPlayer.this.doMediaPulseTask();
            case PAUSED:
            case STALLED:
            case HALTED:
               NativeMediaPlayer.this.isMediaPulseEnabled.set(false);
         }

         synchronized(NativeMediaPlayer.this.cachedStateEvents) {
            if (NativeMediaPlayer.this.playerStateListeners.isEmpty()) {
               NativeMediaPlayer.this.cachedStateEvents.add(var1);
               return;
            }
         }

         ListIterator var2 = NativeMediaPlayer.this.playerStateListeners.listIterator();

         while(var2.hasNext()) {
            PlayerStateListener var3 = (PlayerStateListener)((WeakReference)var2.next()).get();
            if (var3 != null) {
               switch (NativeMediaPlayer.this.playerState) {
                  case READY:
                     NativeMediaPlayer.this.onNativeInit();
                     this.sendFakeBufferProgressEvent();
                     var3.onReady(var1);
                     break;
                  case PLAYING:
                     var3.onPlaying(var1);
                     break;
                  case STOPPED:
                     var3.onStop(var1);
                     break;
                  case FINISHED:
                     var3.onFinish(var1);
                     break;
                  case PAUSED:
                     var3.onPause(var1);
                     break;
                  case STALLED:
                     var3.onStall(var1);
                     break;
                  case HALTED:
                     var3.onHalt(var1);
               }
            } else {
               var2.remove();
            }
         }

      }

      private void HandlePlayerTimeEvents(PlayerTimeEvent var1) {
         synchronized(NativeMediaPlayer.this.cachedTimeEvents) {
            if (NativeMediaPlayer.this.playerTimeListeners.isEmpty()) {
               NativeMediaPlayer.this.cachedTimeEvents.add(var1);
               return;
            }
         }

         ListIterator var2 = NativeMediaPlayer.this.playerTimeListeners.listIterator();

         while(var2.hasNext()) {
            PlayerTimeListener var3 = (PlayerTimeListener)((WeakReference)var2.next()).get();
            if (var3 != null) {
               var3.onDurationChanged(var1.getTime());
            } else {
               var2.remove();
            }
         }

      }

      private void HandleFrameSizeChangedEvents(FrameSizeChangedEvent var1) {
         NativeMediaPlayer.this.frameWidth = var1.getWidth();
         NativeMediaPlayer.this.frameHeight = var1.getHeight();
         Logger.logMsg(1, "** Frame size changed (" + NativeMediaPlayer.this.frameWidth + ", " + NativeMediaPlayer.this.frameHeight + ")");
         ListIterator var2 = NativeMediaPlayer.this.videoTrackSizeListeners.listIterator();

         while(var2.hasNext()) {
            VideoTrackSizeListener var3 = (VideoTrackSizeListener)((WeakReference)var2.next()).get();
            if (var3 != null) {
               var3.onSizeChanged(NativeMediaPlayer.this.frameWidth, NativeMediaPlayer.this.frameHeight);
            } else {
               var2.remove();
            }
         }

      }

      private void HandleTrackEvents(TrackEvent var1) {
         NativeMediaPlayer.this.media.addTrack(var1.getTrack());
         if (var1.getTrack() instanceof VideoTrack) {
            NativeMediaPlayer.this.encodedFrameRate = (double)((VideoTrack)var1.getTrack()).getEncodedFrameRate();
         }

      }

      private void HandleMarkerEvents(MarkerEvent var1) {
         ListIterator var2 = NativeMediaPlayer.this.markerListeners.listIterator();

         while(var2.hasNext()) {
            MarkerListener var3 = (MarkerListener)((WeakReference)var2.next()).get();
            if (var3 != null) {
               var3.onMarker(var1);
            } else {
               var2.remove();
            }
         }

      }

      private void HandleWarningEvents(WarningEvent var1) {
         Logger.logMsg(3, var1.getSource() + var1.getMessage());
      }

      private void HandleErrorEvents(MediaErrorEvent var1) {
         Logger.logMsg(4, var1.getMessage());
         synchronized(NativeMediaPlayer.this.cachedErrorEvents) {
            if (NativeMediaPlayer.this.errorListeners.isEmpty()) {
               NativeMediaPlayer.this.cachedErrorEvents.add(var1);
               return;
            }
         }

         ListIterator var2 = NativeMediaPlayer.this.errorListeners.listIterator();

         while(var2.hasNext()) {
            MediaErrorListener var3 = (MediaErrorListener)((WeakReference)var2.next()).get();
            if (var3 != null) {
               var3.onError(var1.getSource(), var1.getErrorCode(), var1.getMessage());
            } else {
               var2.remove();
            }
         }

      }

      private void HandleBufferEvents(BufferProgressEvent var1) {
         synchronized(NativeMediaPlayer.this.cachedBufferEvents) {
            if (NativeMediaPlayer.this.bufferListeners.isEmpty()) {
               NativeMediaPlayer.this.cachedBufferEvents.add(var1);
               return;
            }
         }

         ListIterator var2 = NativeMediaPlayer.this.bufferListeners.listIterator();

         while(var2.hasNext()) {
            BufferListener var3 = (BufferListener)((WeakReference)var2.next()).get();
            if (var3 != null) {
               var3.onBufferProgress(var1);
            } else {
               var2.remove();
            }
         }

      }

      private void HandleAudioSpectrumEvents(AudioSpectrumEvent var1) {
         ListIterator var2 = NativeMediaPlayer.this.audioSpectrumListeners.listIterator();

         while(var2.hasNext()) {
            AudioSpectrumListener var3 = (AudioSpectrumListener)((WeakReference)var2.next()).get();
            if (var3 != null) {
               if (var1.queryTimestamp()) {
                  double var4 = NativeMediaPlayer.this.playerGetPresentationTime();
                  var1.setTimestamp(var4);
               }

               var3.onAudioSpectrumEvent(var1);
            } else {
               var2.remove();
            }
         }

      }

      public void postEvent(PlayerEvent var1) {
         if (this.eventQueue != null) {
            this.eventQueue.offer(var1);
         }

      }

      public void terminateLoop() {
         this.stopped = true;

         try {
            this.eventQueue.put(new PlayerEvent());
         } catch (InterruptedException var2) {
         }

      }

      private void sendFakeBufferProgressEvent() {
         String var1 = NativeMediaPlayer.this.media.getLocator().getContentType();
         String var2 = NativeMediaPlayer.this.media.getLocator().getProtocol();
         if (var1 != null && (var1.equals("audio/mpegurl") || var1.equals("application/vnd.apple.mpegurl")) || var2 != null && !var2.equals("http") && !var2.equals("https")) {
            this.HandleBufferEvents(new BufferProgressEvent(NativeMediaPlayer.this.getDuration(), 0L, 1L, 1L));
         }

      }
   }

   private class VideoRenderer implements VideoRenderControl {
      private VideoRenderer() {
      }

      public void addVideoRendererListener(VideoRendererListener var1) {
         if (var1 != null) {
            synchronized(NativeMediaPlayer.this.firstFrameLock) {
               if (NativeMediaPlayer.this.firstFrameEvent != null) {
                  var1.videoFrameUpdated(NativeMediaPlayer.this.firstFrameEvent);
               }
            }

            NativeMediaPlayer.this.videoUpdateListeners.add(new WeakReference(var1));
         }

      }

      public void removeVideoRendererListener(VideoRendererListener var1) {
         if (var1 != null) {
            ListIterator var2 = NativeMediaPlayer.this.videoUpdateListeners.listIterator();

            while(true) {
               VideoRendererListener var3;
               do {
                  if (!var2.hasNext()) {
                     return;
                  }

                  var3 = (VideoRendererListener)((WeakReference)var2.next()).get();
               } while(var3 != null && var3 != var1);

               var2.remove();
            }
         }
      }

      public void addVideoFrameRateListener(VideoFrameRateListener var1) {
         if (var1 != null) {
            NativeMediaPlayer.this.videoFrameRateListeners.add(new WeakReference(var1));
         }

      }

      public void removeVideoFrameRateListener(VideoFrameRateListener var1) {
         if (var1 != null) {
            ListIterator var2 = NativeMediaPlayer.this.videoFrameRateListeners.listIterator();

            while(true) {
               VideoFrameRateListener var3;
               do {
                  if (!var2.hasNext()) {
                     return;
                  }

                  var3 = (VideoFrameRateListener)((WeakReference)var2.next()).get();
               } while(var3 != null && var3 != var1);

               var2.remove();
            }
         }
      }

      public int getFrameWidth() {
         return NativeMediaPlayer.this.frameWidth;
      }

      public int getFrameHeight() {
         return NativeMediaPlayer.this.frameHeight;
      }

      // $FF: synthetic method
      VideoRenderer(Object var2) {
         this();
      }
   }

   private static class FrameSizeChangedEvent extends PlayerEvent {
      private final int width;
      private final int height;

      public FrameSizeChangedEvent(int var1, int var2) {
         if (var1 > 0) {
            this.width = var1;
         } else {
            this.width = 0;
         }

         if (var2 > 0) {
            this.height = var2;
         } else {
            this.height = 0;
         }

      }

      public int getWidth() {
         return this.width;
      }

      public int getHeight() {
         return this.height;
      }
   }

   private static class TrackEvent extends PlayerEvent {
      private final Track track;

      TrackEvent(Track var1) {
         this.track = var1;
      }

      public Track getTrack() {
         return this.track;
      }
   }

   private static class PlayerTimeEvent extends PlayerEvent {
      private final double time;

      public PlayerTimeEvent(double var1) {
         this.time = var1;
      }

      public double getTime() {
         return this.time;
      }
   }

   public static class MediaErrorEvent extends PlayerEvent {
      private final Object source;
      private final MediaError error;

      public MediaErrorEvent(Object var1, MediaError var2) {
         this.source = var1;
         this.error = var2;
      }

      public Object getSource() {
         return this.source;
      }

      public String getMessage() {
         return this.error.description();
      }

      public int getErrorCode() {
         return this.error.code();
      }
   }

   private static class WarningEvent extends PlayerEvent {
      private final Object source;
      private final String message;

      WarningEvent(Object var1, String var2) {
         this.source = var1;
         this.message = var2;
      }

      public Object getSource() {
         return this.source;
      }

      public String getMessage() {
         return this.message;
      }
   }
}
