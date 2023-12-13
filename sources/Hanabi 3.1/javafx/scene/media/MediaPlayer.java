package javafx.scene.media;

import com.sun.javafx.tk.TKPulseListener;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmedia.MediaManager;
import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.media.jfxmedia.events.AudioSpectrumEvent;
import com.sun.media.jfxmedia.events.BufferListener;
import com.sun.media.jfxmedia.events.BufferProgressEvent;
import com.sun.media.jfxmedia.events.MarkerEvent;
import com.sun.media.jfxmedia.events.MarkerListener;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.events.NewFrameEvent;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import com.sun.media.jfxmedia.events.PlayerTimeListener;
import com.sun.media.jfxmedia.events.VideoRendererListener;
import com.sun.media.jfxmedia.events.VideoTrackSizeListener;
import com.sun.media.jfxmedia.locator.Locator;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.util.Pair;

public final class MediaPlayer {
   public static final int INDEFINITE = -1;
   private static final double RATE_MIN = 0.0;
   private static final double RATE_MAX = 8.0;
   private static final int AUDIOSPECTRUM_THRESHOLD_MAX = 0;
   private static final double AUDIOSPECTRUM_INTERVAL_MIN = 1.0E-9;
   private static final int AUDIOSPECTRUM_NUMBANDS_MIN = 2;
   private com.sun.media.jfxmedia.MediaPlayer jfxPlayer;
   private MapChangeListener markerMapListener = null;
   private MarkerListener markerEventListener = null;
   private PlayerStateListener stateListener = null;
   private PlayerTimeListener timeListener = null;
   private VideoTrackSizeListener sizeListener = null;
   private MediaErrorListener errorListener = null;
   private BufferListener bufferListener = null;
   private com.sun.media.jfxmedia.events.AudioSpectrumListener spectrumListener = null;
   private RendererListener rendererListener = null;
   private boolean rateChangeRequested = false;
   private boolean volumeChangeRequested = false;
   private boolean balanceChangeRequested = false;
   private boolean startTimeChangeRequested = false;
   private boolean stopTimeChangeRequested = false;
   private boolean muteChangeRequested = false;
   private boolean playRequested = false;
   private boolean audioSpectrumNumBandsChangeRequested = false;
   private boolean audioSpectrumIntervalChangeRequested = false;
   private boolean audioSpectrumThresholdChangeRequested = false;
   private boolean audioSpectrumEnabledChangeRequested = false;
   private MediaTimerTask mediaTimerTask = null;
   private double prevTimeMs = -1.0;
   private boolean isUpdateTimeEnabled = false;
   private BufferProgressEvent lastBufferEvent = null;
   private Duration startTimeAtStop = null;
   private boolean isEOS = false;
   private final Object disposeLock = new Object();
   private static final int DEFAULT_SPECTRUM_BAND_COUNT = 128;
   private static final double DEFAULT_SPECTRUM_INTERVAL = 0.1;
   private static final int DEFAULT_SPECTRUM_THRESHOLD = -60;
   private final Set viewRefs = new HashSet();
   private AudioEqualizer audioEqualizer;
   private ReadOnlyObjectWrapper error;
   private ObjectProperty onError;
   private Media media;
   private BooleanProperty autoPlay;
   private boolean playerReady;
   private DoubleProperty rate;
   private ReadOnlyDoubleWrapper currentRate;
   private DoubleProperty volume;
   private DoubleProperty balance;
   private ObjectProperty startTime;
   private ObjectProperty stopTime;
   private ReadOnlyObjectWrapper cycleDuration;
   private ReadOnlyObjectWrapper totalDuration;
   private ReadOnlyObjectWrapper currentTime;
   private ReadOnlyObjectWrapper status;
   private ReadOnlyObjectWrapper bufferProgressTime;
   private IntegerProperty cycleCount;
   private ReadOnlyIntegerWrapper currentCount;
   private BooleanProperty mute;
   private ObjectProperty onMarker;
   private ObjectProperty onEndOfMedia;
   private ObjectProperty onReady;
   private ObjectProperty onPlaying;
   private ObjectProperty onPaused;
   private ObjectProperty onStopped;
   private ObjectProperty onHalted;
   private ObjectProperty onRepeat;
   private ObjectProperty onStalled;
   private IntegerProperty audioSpectrumNumBands;
   private DoubleProperty audioSpectrumInterval;
   private IntegerProperty audioSpectrumThreshold;
   private ObjectProperty audioSpectrumListener;
   private final Object renderLock = new Object();
   private VideoDataBuffer currentRenderFrame;
   private VideoDataBuffer nextRenderFrame;

   com.sun.media.jfxmedia.MediaPlayer retrieveJfxPlayer() {
      synchronized(this.disposeLock) {
         return this.jfxPlayer;
      }
   }

   private static double clamp(double var0, double var2, double var4) {
      if (var2 != Double.MIN_VALUE && var0 < var2) {
         return var2;
      } else {
         return var4 != Double.MAX_VALUE && var0 > var4 ? var4 : var0;
      }
   }

   private static int clamp(int var0, int var1, int var2) {
      if (var1 != Integer.MIN_VALUE && var0 < var1) {
         return var1;
      } else {
         return var2 != Integer.MAX_VALUE && var0 > var2 ? var2 : var0;
      }
   }

   public final AudioEqualizer getAudioEqualizer() {
      synchronized(this.disposeLock) {
         if (this.getStatus() == MediaPlayer.Status.DISPOSED) {
            return null;
         } else {
            if (this.audioEqualizer == null) {
               this.audioEqualizer = new AudioEqualizer();
               if (this.jfxPlayer != null) {
                  this.audioEqualizer.setAudioEqualizer(this.jfxPlayer.getEqualizer());
               }

               this.audioEqualizer.setEnabled(true);
            }

            return this.audioEqualizer;
         }
      }
   }

   public MediaPlayer(@NamedArg("media") Media var1) {
      if (null == var1) {
         throw new NullPointerException("media == null!");
      } else {
         this.media = var1;
         this.errorListener = new _MediaErrorListener();
         MediaManager.addMediaErrorListener(this.errorListener);

         try {
            Locator var2 = var1.retrieveJfxLocator();
            if (var2.canBlock()) {
               InitMediaPlayer var3 = new InitMediaPlayer();
               Thread var4 = new Thread(var3);
               var4.setDaemon(true);
               var4.start();
            } else {
               this.init();
            }

         } catch (com.sun.media.jfxmedia.MediaException var5) {
            throw MediaException.exceptionToMediaException(var5);
         } catch (MediaException var6) {
            throw var6;
         }
      }
   }

   void registerListeners() {
      synchronized(this.disposeLock) {
         if (this.getStatus() != MediaPlayer.Status.DISPOSED) {
            if (this.jfxPlayer != null) {
               MediaManager.registerMediaPlayerForDispose(this, this.jfxPlayer);
               this.jfxPlayer.addMediaErrorListener(this.errorListener);
               this.jfxPlayer.addMediaTimeListener(this.timeListener);
               this.jfxPlayer.addVideoTrackSizeListener(this.sizeListener);
               this.jfxPlayer.addBufferListener(this.bufferListener);
               this.jfxPlayer.addMarkerListener(this.markerEventListener);
               this.jfxPlayer.addAudioSpectrumListener(this.spectrumListener);
               this.jfxPlayer.getVideoRenderControl().addVideoRendererListener(this.rendererListener);
               this.jfxPlayer.addMediaPlayerListener(this.stateListener);
            }

            if (null != this.rendererListener) {
               Toolkit.getToolkit().addStageTkPulseListener(this.rendererListener);
            }

         }
      }
   }

   private void init() throws MediaException {
      try {
         Locator var1 = this.media.retrieveJfxLocator();
         var1.waitForReadySignal();
         synchronized(this.disposeLock) {
            if (this.getStatus() == MediaPlayer.Status.DISPOSED) {
               return;
            }

            this.jfxPlayer = MediaManager.getPlayer(var1);
            if (this.jfxPlayer != null) {
               MediaPlayerShutdownHook.addMediaPlayer(this);
               this.jfxPlayer.setBalance((float)this.getBalance());
               this.jfxPlayer.setMute(this.isMute());
               this.jfxPlayer.setVolume((float)this.getVolume());
               this.sizeListener = new _VideoTrackSizeListener();
               this.stateListener = new _PlayerStateListener();
               this.timeListener = new _PlayerTimeListener();
               this.bufferListener = new _BufferListener();
               this.markerEventListener = new _MarkerListener();
               this.spectrumListener = new _SpectrumListener();
               this.rendererListener = new RendererListener();
            }

            this.markerMapListener = new MarkerMapChangeListener();
            ObservableMap var3 = this.media.getMarkers();
            var3.addListener(this.markerMapListener);
            com.sun.media.jfxmedia.Media var4 = this.jfxPlayer.getMedia();
            Iterator var5 = var3.entrySet().iterator();

            while(var5.hasNext()) {
               Map.Entry var6 = (Map.Entry)var5.next();
               String var7 = (String)var6.getKey();
               if (var7 != null) {
                  Duration var8 = (Duration)var6.getValue();
                  if (var8 != null) {
                     double var9 = var8.toMillis();
                     if (var9 >= 0.0) {
                        var4.addMarker(var7, var9 / 1000.0);
                     }
                  }
               }
            }
         }
      } catch (com.sun.media.jfxmedia.MediaException var13) {
         throw MediaException.exceptionToMediaException(var13);
      }

      Platform.runLater(() -> {
         this.registerListeners();
      });
   }

   private void setError(MediaException var1) {
      if (this.getError() == null) {
         this.errorPropertyImpl().set(var1);
      }

   }

   public final MediaException getError() {
      return this.error == null ? null : (MediaException)this.error.get();
   }

   public ReadOnlyObjectProperty errorProperty() {
      return this.errorPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper errorPropertyImpl() {
      if (this.error == null) {
         this.error = new ReadOnlyObjectWrapper() {
            protected void invalidated() {
               if (MediaPlayer.this.getOnError() != null) {
                  Platform.runLater(MediaPlayer.this.getOnError());
               }

            }

            public Object getBean() {
               return MediaPlayer.this;
            }

            public String getName() {
               return "error";
            }
         };
      }

      return this.error;
   }

   public final void setOnError(Runnable var1) {
      this.onErrorProperty().set(var1);
   }

   public final Runnable getOnError() {
      return this.onError == null ? null : (Runnable)this.onError.get();
   }

   public ObjectProperty onErrorProperty() {
      if (this.onError == null) {
         this.onError = new ObjectPropertyBase() {
            protected void invalidated() {
               if (this.get() != null && MediaPlayer.this.getError() != null) {
                  Platform.runLater((Runnable)this.get());
               }

            }

            public Object getBean() {
               return MediaPlayer.this;
            }

            public String getName() {
               return "onError";
            }
         };
      }

      return this.onError;
   }

   public final Media getMedia() {
      return this.media;
   }

   public final void setAutoPlay(boolean var1) {
      this.autoPlayProperty().set(var1);
   }

   public final boolean isAutoPlay() {
      return this.autoPlay == null ? false : this.autoPlay.get();
   }

   public BooleanProperty autoPlayProperty() {
      if (this.autoPlay == null) {
         this.autoPlay = new BooleanPropertyBase() {
            protected void invalidated() {
               if (MediaPlayer.this.autoPlay.get()) {
                  MediaPlayer.this.play();
               } else {
                  MediaPlayer.this.playRequested = false;
               }

            }

            public Object getBean() {
               return MediaPlayer.this;
            }

            public String getName() {
               return "autoPlay";
            }
         };
      }

      return this.autoPlay;
   }

   public void play() {
      synchronized(this.disposeLock) {
         if (this.getStatus() != MediaPlayer.Status.DISPOSED) {
            if (this.playerReady) {
               this.jfxPlayer.play();
            } else {
               this.playRequested = true;
            }
         }

      }
   }

   public void pause() {
      synchronized(this.disposeLock) {
         if (this.getStatus() != MediaPlayer.Status.DISPOSED) {
            if (this.playerReady) {
               this.jfxPlayer.pause();
            } else {
               this.playRequested = false;
            }
         }

      }
   }

   public void stop() {
      synchronized(this.disposeLock) {
         if (this.getStatus() != MediaPlayer.Status.DISPOSED) {
            if (this.playerReady) {
               this.jfxPlayer.stop();
               this.setCurrentCount(0);
               this.destroyMediaTimer();
            } else {
               this.playRequested = false;
            }
         }

      }
   }

   public final void setRate(double var1) {
      this.rateProperty().set(var1);
   }

   public final double getRate() {
      return this.rate == null ? 1.0 : this.rate.get();
   }

   public DoubleProperty rateProperty() {
      if (this.rate == null) {
         this.rate = new DoublePropertyBase(1.0) {
            protected void invalidated() {
               synchronized(MediaPlayer.this.disposeLock) {
                  if (MediaPlayer.this.getStatus() != MediaPlayer.Status.DISPOSED) {
                     if (MediaPlayer.this.playerReady) {
                        if (MediaPlayer.this.jfxPlayer.getDuration() != Double.POSITIVE_INFINITY) {
                           MediaPlayer.this.jfxPlayer.setRate((float)MediaPlayer.clamp(MediaPlayer.this.rate.get(), 0.0, 8.0));
                        }
                     } else {
                        MediaPlayer.this.rateChangeRequested = true;
                     }
                  }

               }
            }

            public Object getBean() {
               return MediaPlayer.this;
            }

            public String getName() {
               return "rate";
            }
         };
      }

      return this.rate;
   }

   private void setCurrentRate(double var1) {
      this.currentRatePropertyImpl().set(var1);
   }

   public final double getCurrentRate() {
      return this.currentRate == null ? 0.0 : this.currentRate.get();
   }

   public ReadOnlyDoubleProperty currentRateProperty() {
      return this.currentRatePropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyDoubleWrapper currentRatePropertyImpl() {
      if (this.currentRate == null) {
         this.currentRate = new ReadOnlyDoubleWrapper(this, "currentRate");
      }

      return this.currentRate;
   }

   public final void setVolume(double var1) {
      this.volumeProperty().set(var1);
   }

   public final double getVolume() {
      return this.volume == null ? 1.0 : this.volume.get();
   }

   public DoubleProperty volumeProperty() {
      if (this.volume == null) {
         this.volume = new DoublePropertyBase(1.0) {
            protected void invalidated() {
               synchronized(MediaPlayer.this.disposeLock) {
                  if (MediaPlayer.this.getStatus() != MediaPlayer.Status.DISPOSED) {
                     if (MediaPlayer.this.playerReady) {
                        MediaPlayer.this.jfxPlayer.setVolume((float)MediaPlayer.clamp(MediaPlayer.this.volume.get(), 0.0, 1.0));
                     } else {
                        MediaPlayer.this.volumeChangeRequested = true;
                     }
                  }

               }
            }

            public Object getBean() {
               return MediaPlayer.this;
            }

            public String getName() {
               return "volume";
            }
         };
      }

      return this.volume;
   }

   public final void setBalance(double var1) {
      this.balanceProperty().set(var1);
   }

   public final double getBalance() {
      return this.balance == null ? 0.0 : this.balance.get();
   }

   public DoubleProperty balanceProperty() {
      if (this.balance == null) {
         this.balance = new DoublePropertyBase() {
            protected void invalidated() {
               synchronized(MediaPlayer.this.disposeLock) {
                  if (MediaPlayer.this.getStatus() != MediaPlayer.Status.DISPOSED) {
                     if (MediaPlayer.this.playerReady) {
                        MediaPlayer.this.jfxPlayer.setBalance((float)MediaPlayer.clamp(MediaPlayer.this.balance.get(), -1.0, 1.0));
                     } else {
                        MediaPlayer.this.balanceChangeRequested = true;
                     }
                  }

               }
            }

            public Object getBean() {
               return MediaPlayer.this;
            }

            public String getName() {
               return "balance";
            }
         };
      }

      return this.balance;
   }

   private double[] calculateStartStopTimes(Duration var1, Duration var2) {
      double var3;
      if (var1 != null && !var1.lessThan(Duration.ZERO) && !var1.equals(Duration.UNKNOWN)) {
         if (var1.equals(Duration.INDEFINITE)) {
            var3 = Double.MAX_VALUE;
         } else {
            var3 = var1.toMillis() / 1000.0;
         }
      } else {
         var3 = 0.0;
      }

      double var5;
      if (var2 != null && !var2.equals(Duration.UNKNOWN) && !var2.equals(Duration.INDEFINITE)) {
         if (var2.lessThan(Duration.ZERO)) {
            var5 = 0.0;
         } else {
            var5 = var2.toMillis() / 1000.0;
         }
      } else {
         var5 = Double.MAX_VALUE;
      }

      Duration var7 = this.media.getDuration();
      double var8 = var7 == Duration.UNKNOWN ? Double.MAX_VALUE : var7.toMillis() / 1000.0;
      double var10 = clamp(var3, 0.0, var8);
      double var12 = clamp(var5, 0.0, var8);
      if (var10 > var12) {
         var12 = var10;
      }

      return new double[]{var10, var12};
   }

   private void setStartStopTimes(Duration var1, boolean var2, Duration var3, boolean var4) {
      if (this.jfxPlayer.getDuration() != Double.POSITIVE_INFINITY) {
         double[] var5 = this.calculateStartStopTimes(var1, var3);
         if (var2) {
            this.jfxPlayer.setStartTime(var5[0]);
            if (this.getStatus() == MediaPlayer.Status.READY || this.getStatus() == MediaPlayer.Status.PAUSED) {
               Platform.runLater(() -> {
                  this.setCurrentTime(this.getStartTime());
               });
            }
         }

         if (var4) {
            this.jfxPlayer.setStopTime(var5[1]);
         }

      }
   }

   public final void setStartTime(Duration var1) {
      this.startTimeProperty().set(var1);
   }

   public final Duration getStartTime() {
      return this.startTime == null ? Duration.ZERO : (Duration)this.startTime.get();
   }

   public ObjectProperty startTimeProperty() {
      if (this.startTime == null) {
         this.startTime = new ObjectPropertyBase() {
            protected void invalidated() {
               synchronized(MediaPlayer.this.disposeLock) {
                  if (MediaPlayer.this.getStatus() != MediaPlayer.Status.DISPOSED) {
                     if (MediaPlayer.this.playerReady) {
                        MediaPlayer.this.setStartStopTimes((Duration)MediaPlayer.this.startTime.get(), true, MediaPlayer.this.getStopTime(), false);
                     } else {
                        MediaPlayer.this.startTimeChangeRequested = true;
                     }

                     MediaPlayer.this.calculateCycleDuration();
                  }

               }
            }

            public Object getBean() {
               return MediaPlayer.this;
            }

            public String getName() {
               return "startTime";
            }
         };
      }

      return this.startTime;
   }

   public final void setStopTime(Duration var1) {
      this.stopTimeProperty().set(var1);
   }

   public final Duration getStopTime() {
      return this.stopTime == null ? this.media.getDuration() : (Duration)this.stopTime.get();
   }

   public ObjectProperty stopTimeProperty() {
      if (this.stopTime == null) {
         this.stopTime = new ObjectPropertyBase() {
            protected void invalidated() {
               synchronized(MediaPlayer.this.disposeLock) {
                  if (MediaPlayer.this.getStatus() != MediaPlayer.Status.DISPOSED) {
                     if (MediaPlayer.this.playerReady) {
                        MediaPlayer.this.setStartStopTimes(MediaPlayer.this.getStartTime(), false, (Duration)MediaPlayer.this.stopTime.get(), true);
                     } else {
                        MediaPlayer.this.stopTimeChangeRequested = true;
                     }

                     MediaPlayer.this.calculateCycleDuration();
                  }

               }
            }

            public Object getBean() {
               return MediaPlayer.this;
            }

            public String getName() {
               return "stopTime";
            }
         };
      }

      return this.stopTime;
   }

   private void setCycleDuration(Duration var1) {
      this.cycleDurationPropertyImpl().set(var1);
   }

   public final Duration getCycleDuration() {
      return this.cycleDuration == null ? Duration.UNKNOWN : (Duration)this.cycleDuration.get();
   }

   public ReadOnlyObjectProperty cycleDurationProperty() {
      return this.cycleDurationPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper cycleDurationPropertyImpl() {
      if (this.cycleDuration == null) {
         this.cycleDuration = new ReadOnlyObjectWrapper(this, "cycleDuration");
      }

      return this.cycleDuration;
   }

   private void calculateCycleDuration() {
      Duration var2 = this.media.getDuration();
      Duration var1;
      if (!this.getStopTime().isUnknown()) {
         var1 = this.getStopTime();
      } else {
         var1 = var2;
      }

      if (var1.greaterThan(var2)) {
         var1 = var2;
      }

      if ((var1.isUnknown() || this.getStartTime().isUnknown() || this.getStartTime().isIndefinite()) && !this.getCycleDuration().isUnknown()) {
         this.setCycleDuration(Duration.UNKNOWN);
      }

      this.setCycleDuration(var1.subtract(this.getStartTime()));
      this.calculateTotalDuration();
   }

   private void setTotalDuration(Duration var1) {
      this.totalDurationPropertyImpl().set(var1);
   }

   public final Duration getTotalDuration() {
      return this.totalDuration == null ? Duration.UNKNOWN : (Duration)this.totalDuration.get();
   }

   public ReadOnlyObjectProperty totalDurationProperty() {
      return this.totalDurationPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper totalDurationPropertyImpl() {
      if (this.totalDuration == null) {
         this.totalDuration = new ReadOnlyObjectWrapper(this, "totalDuration");
      }

      return this.totalDuration;
   }

   private void calculateTotalDuration() {
      if (this.getCycleCount() == -1) {
         this.setTotalDuration(Duration.INDEFINITE);
      } else if (this.getCycleDuration().isUnknown()) {
         this.setTotalDuration(Duration.UNKNOWN);
      } else {
         this.setTotalDuration(this.getCycleDuration().multiply((double)this.getCycleCount()));
      }

   }

   private void setCurrentTime(Duration var1) {
      this.currentTimePropertyImpl().set(var1);
   }

   public final Duration getCurrentTime() {
      synchronized(this.disposeLock) {
         if (this.getStatus() == MediaPlayer.Status.DISPOSED) {
            return Duration.ZERO;
         } else if (this.getStatus() == MediaPlayer.Status.STOPPED) {
            return Duration.millis(this.getStartTime().toMillis());
         } else {
            Duration var2;
            if (this.isEOS) {
               var2 = this.media.getDuration();
               Duration var3 = this.getStopTime();
               if (var3 != Duration.UNKNOWN && var2 != Duration.UNKNOWN) {
                  if (var3.greaterThan(var2)) {
                     return Duration.millis(var2.toMillis());
                  }

                  return Duration.millis(var3.toMillis());
               }
            }

            var2 = (Duration)this.currentTimeProperty().get();
            if (this.playerReady) {
               double var7 = this.jfxPlayer.getPresentationTime();
               if (var7 >= 0.0) {
                  var2 = Duration.seconds(var7);
               }
            }

            return var2;
         }
      }
   }

   public ReadOnlyObjectProperty currentTimeProperty() {
      return this.currentTimePropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper currentTimePropertyImpl() {
      if (this.currentTime == null) {
         this.currentTime = new ReadOnlyObjectWrapper(this, "currentTime");
         this.currentTime.setValue(Duration.ZERO);
         this.updateTime();
      }

      return this.currentTime;
   }

   public void seek(Duration var1) {
      synchronized(this.disposeLock) {
         if (this.getStatus() != MediaPlayer.Status.DISPOSED) {
            if (this.playerReady && var1 != null && !var1.isUnknown()) {
               if (this.jfxPlayer.getDuration() == Double.POSITIVE_INFINITY) {
                  return;
               }

               double var3;
               if (!var1.isIndefinite()) {
                  var3 = var1.toMillis() / 1000.0;
                  double[] var8 = this.calculateStartStopTimes(this.getStartTime(), this.getStopTime());
                  if (var3 < var8[0]) {
                     var3 = var8[0];
                  } else if (var3 > var8[1]) {
                     var3 = var8[1];
                  }
               } else {
                  Duration var5 = this.media.getDuration();
                  if (var5 == null || var5.isUnknown() || var5.isIndefinite()) {
                     var5 = Duration.millis(Double.MAX_VALUE);
                  }

                  var3 = var5.toMillis() / 1000.0;
               }

               if (!this.isUpdateTimeEnabled) {
                  Status var9 = this.getStatus();
                  if ((var9 == MediaPlayer.Status.PLAYING || var9 == MediaPlayer.Status.PAUSED) && this.getStartTime().toSeconds() <= var3 && var3 <= this.getStopTime().toSeconds()) {
                     this.isEOS = false;
                     this.isUpdateTimeEnabled = true;
                     this.setCurrentRate(this.getRate());
                  }
               }

               this.jfxPlayer.seek(var3);
            }

         }
      }
   }

   private void setStatus(Status var1) {
      this.statusPropertyImpl().set(var1);
   }

   public final Status getStatus() {
      return this.status == null ? MediaPlayer.Status.UNKNOWN : (Status)this.status.get();
   }

   public ReadOnlyObjectProperty statusProperty() {
      return this.statusPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper statusPropertyImpl() {
      if (this.status == null) {
         this.status = new ReadOnlyObjectWrapper() {
            protected void invalidated() {
               if (this.get() == MediaPlayer.Status.PLAYING) {
                  MediaPlayer.this.setCurrentRate(MediaPlayer.this.getRate());
               } else {
                  MediaPlayer.this.setCurrentRate(0.0);
               }

               if (this.get() == MediaPlayer.Status.READY) {
                  if (MediaPlayer.this.getOnReady() != null) {
                     Platform.runLater(MediaPlayer.this.getOnReady());
                  }
               } else if (this.get() == MediaPlayer.Status.PLAYING) {
                  if (MediaPlayer.this.getOnPlaying() != null) {
                     Platform.runLater(MediaPlayer.this.getOnPlaying());
                  }
               } else if (this.get() == MediaPlayer.Status.PAUSED) {
                  if (MediaPlayer.this.getOnPaused() != null) {
                     Platform.runLater(MediaPlayer.this.getOnPaused());
                  }
               } else if (this.get() == MediaPlayer.Status.STOPPED) {
                  if (MediaPlayer.this.getOnStopped() != null) {
                     Platform.runLater(MediaPlayer.this.getOnStopped());
                  }
               } else if (this.get() == MediaPlayer.Status.STALLED && MediaPlayer.this.getOnStalled() != null) {
                  Platform.runLater(MediaPlayer.this.getOnStalled());
               }

            }

            public Object getBean() {
               return MediaPlayer.this;
            }

            public String getName() {
               return "status";
            }
         };
      }

      return this.status;
   }

   private void setBufferProgressTime(Duration var1) {
      this.bufferProgressTimePropertyImpl().set(var1);
   }

   public final Duration getBufferProgressTime() {
      return this.bufferProgressTime == null ? null : (Duration)this.bufferProgressTime.get();
   }

   public ReadOnlyObjectProperty bufferProgressTimeProperty() {
      return this.bufferProgressTimePropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper bufferProgressTimePropertyImpl() {
      if (this.bufferProgressTime == null) {
         this.bufferProgressTime = new ReadOnlyObjectWrapper(this, "bufferProgressTime");
      }

      return this.bufferProgressTime;
   }

   public final void setCycleCount(int var1) {
      this.cycleCountProperty().set(var1);
   }

   public final int getCycleCount() {
      return this.cycleCount == null ? 1 : this.cycleCount.get();
   }

   public IntegerProperty cycleCountProperty() {
      if (this.cycleCount == null) {
         this.cycleCount = new IntegerPropertyBase(1) {
            public Object getBean() {
               return MediaPlayer.this;
            }

            public String getName() {
               return "cycleCount";
            }
         };
      }

      return this.cycleCount;
   }

   private void setCurrentCount(int var1) {
      this.currentCountPropertyImpl().set(var1);
   }

   public final int getCurrentCount() {
      return this.currentCount == null ? 0 : this.currentCount.get();
   }

   public ReadOnlyIntegerProperty currentCountProperty() {
      return this.currentCountPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyIntegerWrapper currentCountPropertyImpl() {
      if (this.currentCount == null) {
         this.currentCount = new ReadOnlyIntegerWrapper(this, "currentCount");
      }

      return this.currentCount;
   }

   public final void setMute(boolean var1) {
      this.muteProperty().set(var1);
   }

   public final boolean isMute() {
      return this.mute == null ? false : this.mute.get();
   }

   public BooleanProperty muteProperty() {
      if (this.mute == null) {
         this.mute = new BooleanPropertyBase() {
            protected void invalidated() {
               synchronized(MediaPlayer.this.disposeLock) {
                  if (MediaPlayer.this.getStatus() != MediaPlayer.Status.DISPOSED) {
                     if (MediaPlayer.this.playerReady) {
                        MediaPlayer.this.jfxPlayer.setMute(this.get());
                     } else {
                        MediaPlayer.this.muteChangeRequested = true;
                     }
                  }

               }
            }

            public Object getBean() {
               return MediaPlayer.this;
            }

            public String getName() {
               return "mute";
            }
         };
      }

      return this.mute;
   }

   public final void setOnMarker(EventHandler var1) {
      this.onMarkerProperty().set(var1);
   }

   public final EventHandler getOnMarker() {
      return this.onMarker == null ? null : (EventHandler)this.onMarker.get();
   }

   public ObjectProperty onMarkerProperty() {
      if (this.onMarker == null) {
         this.onMarker = new SimpleObjectProperty(this, "onMarker");
      }

      return this.onMarker;
   }

   void addView(MediaView var1) {
      WeakReference var2 = new WeakReference(var1);
      synchronized(this.viewRefs) {
         this.viewRefs.add(var2);
      }
   }

   void removeView(MediaView var1) {
      synchronized(this.viewRefs) {
         Iterator var3 = this.viewRefs.iterator();

         while(var3.hasNext()) {
            WeakReference var4 = (WeakReference)var3.next();
            MediaView var5 = (MediaView)var4.get();
            if (var5 != null && var5.equals(var1)) {
               this.viewRefs.remove(var4);
            }
         }

      }
   }

   void handleError(MediaException var1) {
      Platform.runLater(() -> {
         this.setError(var1);
         if (var1.getType() == MediaException.Type.MEDIA_CORRUPTED || var1.getType() == MediaException.Type.MEDIA_UNSUPPORTED || var1.getType() == MediaException.Type.MEDIA_INACCESSIBLE || var1.getType() == MediaException.Type.MEDIA_UNAVAILABLE) {
            this.media._setError(var1.getType(), var1.getMessage());
         }

      });
   }

   void createMediaTimer() {
      synchronized(MediaTimerTask.timerLock) {
         if (this.mediaTimerTask == null) {
            this.mediaTimerTask = new MediaTimerTask(this);
            this.mediaTimerTask.start();
         }

         this.isUpdateTimeEnabled = true;
      }
   }

   void destroyMediaTimer() {
      synchronized(MediaTimerTask.timerLock) {
         if (this.mediaTimerTask != null) {
            this.isUpdateTimeEnabled = false;
            this.mediaTimerTask.stop();
            this.mediaTimerTask = null;
         }

      }
   }

   void updateTime() {
      if (this.playerReady && this.isUpdateTimeEnabled && this.jfxPlayer != null) {
         double var1 = this.jfxPlayer.getPresentationTime();
         if (var1 >= 0.0) {
            double var3 = var1 * 1000.0;
            if (Double.compare(var3, this.prevTimeMs) != 0) {
               this.setCurrentTime(Duration.millis(var3));
               this.prevTimeMs = var3;
            }
         }
      }

   }

   void loopPlayback() {
      this.seek(this.getStartTime());
   }

   void handleRequestedChanges() {
      if (this.rateChangeRequested) {
         if (this.jfxPlayer.getDuration() != Double.POSITIVE_INFINITY) {
            this.jfxPlayer.setRate((float)clamp(this.getRate(), 0.0, 8.0));
         }

         this.rateChangeRequested = false;
      }

      if (this.volumeChangeRequested) {
         this.jfxPlayer.setVolume((float)clamp(this.getVolume(), 0.0, 1.0));
         this.volumeChangeRequested = false;
      }

      if (this.balanceChangeRequested) {
         this.jfxPlayer.setBalance((float)clamp(this.getBalance(), -1.0, 1.0));
         this.balanceChangeRequested = false;
      }

      if (this.startTimeChangeRequested || this.stopTimeChangeRequested) {
         this.setStartStopTimes(this.getStartTime(), this.startTimeChangeRequested, this.getStopTime(), this.stopTimeChangeRequested);
         this.startTimeChangeRequested = this.stopTimeChangeRequested = false;
      }

      if (this.muteChangeRequested) {
         this.jfxPlayer.setMute(this.isMute());
         this.muteChangeRequested = false;
      }

      if (this.audioSpectrumNumBandsChangeRequested) {
         this.jfxPlayer.getAudioSpectrum().setBandCount(clamp(this.getAudioSpectrumNumBands(), 2, Integer.MAX_VALUE));
         this.audioSpectrumNumBandsChangeRequested = false;
      }

      if (this.audioSpectrumIntervalChangeRequested) {
         this.jfxPlayer.getAudioSpectrum().setInterval(clamp(this.getAudioSpectrumInterval(), 1.0E-9, Double.MAX_VALUE));
         this.audioSpectrumIntervalChangeRequested = false;
      }

      if (this.audioSpectrumThresholdChangeRequested) {
         this.jfxPlayer.getAudioSpectrum().setSensitivityThreshold(clamp(this.getAudioSpectrumThreshold(), Integer.MIN_VALUE, 0));
         this.audioSpectrumThresholdChangeRequested = false;
      }

      if (this.audioSpectrumEnabledChangeRequested) {
         boolean var1 = this.getAudioSpectrumListener() != null;
         this.jfxPlayer.getAudioSpectrum().setEnabled(var1);
         this.audioSpectrumEnabledChangeRequested = false;
      }

      if (this.playRequested) {
         this.jfxPlayer.play();
         this.playRequested = false;
      }

   }

   void preReady() {
      synchronized(this.viewRefs) {
         Iterator var2 = this.viewRefs.iterator();

         while(var2.hasNext()) {
            WeakReference var3 = (WeakReference)var2.next();
            MediaView var4 = (MediaView)var3.get();
            if (var4 != null) {
               var4._mediaPlayerOnReady();
            }
         }
      }

      if (this.audioEqualizer != null) {
         this.audioEqualizer.setAudioEqualizer(this.jfxPlayer.getEqualizer());
      }

      double var1 = this.jfxPlayer.getDuration();
      Duration var11;
      if (var1 >= 0.0 && !Double.isNaN(var1)) {
         var11 = Duration.millis(var1 * 1000.0);
      } else {
         var11 = Duration.UNKNOWN;
      }

      this.playerReady = true;
      this.media.setDuration(var11);
      this.media._updateMedia(this.jfxPlayer.getMedia());
      this.handleRequestedChanges();
      this.calculateCycleDuration();
      if (this.lastBufferEvent != null && var11.toMillis() > 0.0) {
         double var12 = (double)this.lastBufferEvent.getBufferPosition();
         double var6 = (double)this.lastBufferEvent.getBufferStop();
         double var8 = var12 / var6 * var11.toMillis();
         this.lastBufferEvent = null;
         this.setBufferProgressTime(Duration.millis(var8));
      }

      this.setStatus(MediaPlayer.Status.READY);
   }

   public final void setOnEndOfMedia(Runnable var1) {
      this.onEndOfMediaProperty().set(var1);
   }

   public final Runnable getOnEndOfMedia() {
      return this.onEndOfMedia == null ? null : (Runnable)this.onEndOfMedia.get();
   }

   public ObjectProperty onEndOfMediaProperty() {
      if (this.onEndOfMedia == null) {
         this.onEndOfMedia = new SimpleObjectProperty(this, "onEndOfMedia");
      }

      return this.onEndOfMedia;
   }

   public final void setOnReady(Runnable var1) {
      this.onReadyProperty().set(var1);
   }

   public final Runnable getOnReady() {
      return this.onReady == null ? null : (Runnable)this.onReady.get();
   }

   public ObjectProperty onReadyProperty() {
      if (this.onReady == null) {
         this.onReady = new SimpleObjectProperty(this, "onReady");
      }

      return this.onReady;
   }

   public final void setOnPlaying(Runnable var1) {
      this.onPlayingProperty().set(var1);
   }

   public final Runnable getOnPlaying() {
      return this.onPlaying == null ? null : (Runnable)this.onPlaying.get();
   }

   public ObjectProperty onPlayingProperty() {
      if (this.onPlaying == null) {
         this.onPlaying = new SimpleObjectProperty(this, "onPlaying");
      }

      return this.onPlaying;
   }

   public final void setOnPaused(Runnable var1) {
      this.onPausedProperty().set(var1);
   }

   public final Runnable getOnPaused() {
      return this.onPaused == null ? null : (Runnable)this.onPaused.get();
   }

   public ObjectProperty onPausedProperty() {
      if (this.onPaused == null) {
         this.onPaused = new SimpleObjectProperty(this, "onPaused");
      }

      return this.onPaused;
   }

   public final void setOnStopped(Runnable var1) {
      this.onStoppedProperty().set(var1);
   }

   public final Runnable getOnStopped() {
      return this.onStopped == null ? null : (Runnable)this.onStopped.get();
   }

   public ObjectProperty onStoppedProperty() {
      if (this.onStopped == null) {
         this.onStopped = new SimpleObjectProperty(this, "onStopped");
      }

      return this.onStopped;
   }

   public final void setOnHalted(Runnable var1) {
      this.onHaltedProperty().set(var1);
   }

   public final Runnable getOnHalted() {
      return this.onHalted == null ? null : (Runnable)this.onHalted.get();
   }

   public ObjectProperty onHaltedProperty() {
      if (this.onHalted == null) {
         this.onHalted = new SimpleObjectProperty(this, "onHalted");
      }

      return this.onHalted;
   }

   public final void setOnRepeat(Runnable var1) {
      this.onRepeatProperty().set(var1);
   }

   public final Runnable getOnRepeat() {
      return this.onRepeat == null ? null : (Runnable)this.onRepeat.get();
   }

   public ObjectProperty onRepeatProperty() {
      if (this.onRepeat == null) {
         this.onRepeat = new SimpleObjectProperty(this, "onRepeat");
      }

      return this.onRepeat;
   }

   public final void setOnStalled(Runnable var1) {
      this.onStalledProperty().set(var1);
   }

   public final Runnable getOnStalled() {
      return this.onStalled == null ? null : (Runnable)this.onStalled.get();
   }

   public ObjectProperty onStalledProperty() {
      if (this.onStalled == null) {
         this.onStalled = new SimpleObjectProperty(this, "onStalled");
      }

      return this.onStalled;
   }

   public final void setAudioSpectrumNumBands(int var1) {
      this.audioSpectrumNumBandsProperty().setValue((Number)var1);
   }

   public final int getAudioSpectrumNumBands() {
      return this.audioSpectrumNumBandsProperty().getValue();
   }

   public IntegerProperty audioSpectrumNumBandsProperty() {
      if (this.audioSpectrumNumBands == null) {
         this.audioSpectrumNumBands = new IntegerPropertyBase(128) {
            protected void invalidated() {
               synchronized(MediaPlayer.this.disposeLock) {
                  if (MediaPlayer.this.getStatus() != MediaPlayer.Status.DISPOSED) {
                     if (MediaPlayer.this.playerReady) {
                        MediaPlayer.this.jfxPlayer.getAudioSpectrum().setBandCount(MediaPlayer.clamp(MediaPlayer.this.audioSpectrumNumBands.get(), 2, Integer.MAX_VALUE));
                     } else {
                        MediaPlayer.this.audioSpectrumNumBandsChangeRequested = true;
                     }
                  }

               }
            }

            public Object getBean() {
               return MediaPlayer.this;
            }

            public String getName() {
               return "audioSpectrumNumBands";
            }
         };
      }

      return this.audioSpectrumNumBands;
   }

   public final void setAudioSpectrumInterval(double var1) {
      this.audioSpectrumIntervalProperty().set(var1);
   }

   public final double getAudioSpectrumInterval() {
      return this.audioSpectrumIntervalProperty().get();
   }

   public DoubleProperty audioSpectrumIntervalProperty() {
      if (this.audioSpectrumInterval == null) {
         this.audioSpectrumInterval = new DoublePropertyBase(0.1) {
            protected void invalidated() {
               synchronized(MediaPlayer.this.disposeLock) {
                  if (MediaPlayer.this.getStatus() != MediaPlayer.Status.DISPOSED) {
                     if (MediaPlayer.this.playerReady) {
                        MediaPlayer.this.jfxPlayer.getAudioSpectrum().setInterval(MediaPlayer.clamp(MediaPlayer.this.audioSpectrumInterval.get(), 1.0E-9, Double.MAX_VALUE));
                     } else {
                        MediaPlayer.this.audioSpectrumIntervalChangeRequested = true;
                     }
                  }

               }
            }

            public Object getBean() {
               return MediaPlayer.this;
            }

            public String getName() {
               return "audioSpectrumInterval";
            }
         };
      }

      return this.audioSpectrumInterval;
   }

   public final void setAudioSpectrumThreshold(int var1) {
      this.audioSpectrumThresholdProperty().set(var1);
   }

   public final int getAudioSpectrumThreshold() {
      return this.audioSpectrumThresholdProperty().get();
   }

   public IntegerProperty audioSpectrumThresholdProperty() {
      if (this.audioSpectrumThreshold == null) {
         this.audioSpectrumThreshold = new IntegerPropertyBase(-60) {
            protected void invalidated() {
               synchronized(MediaPlayer.this.disposeLock) {
                  if (MediaPlayer.this.getStatus() != MediaPlayer.Status.DISPOSED) {
                     if (MediaPlayer.this.playerReady) {
                        MediaPlayer.this.jfxPlayer.getAudioSpectrum().setSensitivityThreshold(MediaPlayer.clamp(MediaPlayer.this.audioSpectrumThreshold.get(), Integer.MIN_VALUE, 0));
                     } else {
                        MediaPlayer.this.audioSpectrumThresholdChangeRequested = true;
                     }
                  }

               }
            }

            public Object getBean() {
               return MediaPlayer.this;
            }

            public String getName() {
               return "audioSpectrumThreshold";
            }
         };
      }

      return this.audioSpectrumThreshold;
   }

   public final void setAudioSpectrumListener(AudioSpectrumListener var1) {
      this.audioSpectrumListenerProperty().set(var1);
   }

   public final AudioSpectrumListener getAudioSpectrumListener() {
      return (AudioSpectrumListener)this.audioSpectrumListenerProperty().get();
   }

   public ObjectProperty audioSpectrumListenerProperty() {
      if (this.audioSpectrumListener == null) {
         this.audioSpectrumListener = new ObjectPropertyBase() {
            protected void invalidated() {
               synchronized(MediaPlayer.this.disposeLock) {
                  if (MediaPlayer.this.getStatus() != MediaPlayer.Status.DISPOSED) {
                     if (MediaPlayer.this.playerReady) {
                        boolean var2 = MediaPlayer.this.audioSpectrumListener.get() != null;
                        MediaPlayer.this.jfxPlayer.getAudioSpectrum().setEnabled(var2);
                     } else {
                        MediaPlayer.this.audioSpectrumEnabledChangeRequested = true;
                     }
                  }

               }
            }

            public Object getBean() {
               return MediaPlayer.this;
            }

            public String getName() {
               return "audioSpectrumListener";
            }
         };
      }

      return this.audioSpectrumListener;
   }

   public synchronized void dispose() {
      synchronized(this.disposeLock) {
         this.setStatus(MediaPlayer.Status.DISPOSED);
         this.destroyMediaTimer();
         if (this.audioEqualizer != null) {
            this.audioEqualizer.setAudioEqualizer((com.sun.media.jfxmedia.effects.AudioEqualizer)null);
            this.audioEqualizer = null;
         }

         if (this.jfxPlayer != null) {
            this.jfxPlayer.dispose();
            synchronized(this.renderLock) {
               if (this.rendererListener != null) {
                  Toolkit.getToolkit().removeStageTkPulseListener(this.rendererListener);
                  this.rendererListener = null;
               }
            }

            this.jfxPlayer = null;
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public VideoDataBuffer impl_getLatestFrame() {
      synchronized(this.renderLock) {
         if (null != this.currentRenderFrame) {
            this.currentRenderFrame.holdFrame();
         }

         return this.currentRenderFrame;
      }
   }

   private class RendererListener implements VideoRendererListener, TKPulseListener {
      boolean updateMediaViews;

      private RendererListener() {
      }

      public void videoFrameUpdated(NewFrameEvent var1) {
         VideoDataBuffer var2 = var1.getFrameData();
         if (null != var2) {
            Duration var3 = new Duration(var2.getTimestamp() * 1000.0);
            Duration var4 = MediaPlayer.this.getStopTime();
            if (!var3.greaterThanOrEqualTo(MediaPlayer.this.getStartTime()) || !var4.isUnknown() && !var3.lessThanOrEqualTo(var4)) {
               var2.releaseFrame();
            } else {
               this.updateMediaViews = true;
               synchronized(MediaPlayer.this.renderLock) {
                  var2.holdFrame();
                  if (null != MediaPlayer.this.nextRenderFrame) {
                     MediaPlayer.this.nextRenderFrame.releaseFrame();
                  }

                  MediaPlayer.this.nextRenderFrame = var2;
               }

               Toolkit.getToolkit().requestNextPulse();
            }
         }

      }

      public void releaseVideoFrames() {
         synchronized(MediaPlayer.this.renderLock) {
            if (null != MediaPlayer.this.currentRenderFrame) {
               MediaPlayer.this.currentRenderFrame.releaseFrame();
               MediaPlayer.this.currentRenderFrame = null;
            }

            if (null != MediaPlayer.this.nextRenderFrame) {
               MediaPlayer.this.nextRenderFrame.releaseFrame();
               MediaPlayer.this.nextRenderFrame = null;
            }

         }
      }

      public void pulse() {
         if (this.updateMediaViews) {
            this.updateMediaViews = false;
            synchronized(MediaPlayer.this.renderLock) {
               if (null != MediaPlayer.this.nextRenderFrame) {
                  if (null != MediaPlayer.this.currentRenderFrame) {
                     MediaPlayer.this.currentRenderFrame.releaseFrame();
                  }

                  MediaPlayer.this.currentRenderFrame = MediaPlayer.this.nextRenderFrame;
                  MediaPlayer.this.nextRenderFrame = null;
               }
            }

            synchronized(MediaPlayer.this.viewRefs) {
               Iterator var2 = MediaPlayer.this.viewRefs.iterator();

               while(var2.hasNext()) {
                  MediaView var3 = (MediaView)((WeakReference)var2.next()).get();
                  if (null != var3) {
                     var3.notifyMediaFrameUpdated();
                  } else {
                     var2.remove();
                  }
               }
            }
         }

      }

      // $FF: synthetic method
      RendererListener(Object var2) {
         this();
      }
   }

   private class _SpectrumListener implements com.sun.media.jfxmedia.events.AudioSpectrumListener {
      private float[] magnitudes;
      private float[] phases;

      private _SpectrumListener() {
      }

      public void onAudioSpectrumEvent(AudioSpectrumEvent var1) {
         Platform.runLater(() -> {
            AudioSpectrumListener var2 = MediaPlayer.this.getAudioSpectrumListener();
            if (var2 != null) {
               var2.spectrumDataUpdate(var1.getTimestamp(), var1.getDuration(), this.magnitudes = var1.getSource().getMagnitudes(this.magnitudes), this.phases = var1.getSource().getPhases(this.phases));
            }

         });
      }

      // $FF: synthetic method
      _SpectrumListener(Object var2) {
         this();
      }
   }

   private class _BufferListener implements BufferListener {
      double bufferedTime;

      private _BufferListener() {
      }

      public void onBufferProgress(BufferProgressEvent var1) {
         if (MediaPlayer.this.media != null) {
            if (var1.getDuration() > 0.0) {
               double var2 = (double)var1.getBufferPosition();
               double var4 = (double)var1.getBufferStop();
               this.bufferedTime = var2 / var4 * var1.getDuration() * 1000.0;
               MediaPlayer.this.lastBufferEvent = null;
               Platform.runLater(() -> {
                  MediaPlayer.this.setBufferProgressTime(Duration.millis(this.bufferedTime));
               });
            } else {
               MediaPlayer.this.lastBufferEvent = var1;
            }
         }

      }

      // $FF: synthetic method
      _BufferListener(Object var2) {
         this();
      }
   }

   private class _MediaErrorListener implements MediaErrorListener {
      private _MediaErrorListener() {
      }

      public void onError(Object var1, int var2, String var3) {
         MediaException var4 = MediaException.getMediaException(var1, var2, var3);
         MediaPlayer.this.handleError(var4);
      }

      // $FF: synthetic method
      _MediaErrorListener(Object var2) {
         this();
      }
   }

   private class _VideoTrackSizeListener implements VideoTrackSizeListener {
      int trackWidth;
      int trackHeight;

      private _VideoTrackSizeListener() {
      }

      public void onSizeChanged(int var1, int var2) {
         Platform.runLater(() -> {
            if (MediaPlayer.this.media != null) {
               this.trackWidth = var1;
               this.trackHeight = var2;
               this.setSize();
            }

         });
      }

      void setSize() {
         MediaPlayer.this.media.setWidth(this.trackWidth);
         MediaPlayer.this.media.setHeight(this.trackHeight);
         synchronized(MediaPlayer.this.viewRefs) {
            Iterator var2 = MediaPlayer.this.viewRefs.iterator();

            while(var2.hasNext()) {
               WeakReference var3 = (WeakReference)var2.next();
               MediaView var4 = (MediaView)var3.get();
               if (var4 != null) {
                  var4.notifyMediaSizeChange();
               }
            }

         }
      }

      // $FF: synthetic method
      _VideoTrackSizeListener(Object var2) {
         this();
      }
   }

   private class _PlayerTimeListener implements PlayerTimeListener {
      double theDuration;

      private _PlayerTimeListener() {
      }

      void handleDurationChanged() {
         MediaPlayer.this.media.setDuration(Duration.millis(this.theDuration * 1000.0));
      }

      public void onDurationChanged(double var1) {
         Platform.runLater(() -> {
            this.theDuration = var1;
            this.handleDurationChanged();
         });
      }

      // $FF: synthetic method
      _PlayerTimeListener(Object var2) {
         this();
      }
   }

   private class _PlayerStateListener implements PlayerStateListener {
      private _PlayerStateListener() {
      }

      public void onReady(PlayerStateEvent var1) {
         Platform.runLater(() -> {
            synchronized(MediaPlayer.this.disposeLock) {
               if (MediaPlayer.this.getStatus() != MediaPlayer.Status.DISPOSED) {
                  MediaPlayer.this.preReady();
               }
            }
         });
      }

      public void onPlaying(PlayerStateEvent var1) {
         MediaPlayer.this.startTimeAtStop = null;
         Platform.runLater(() -> {
            MediaPlayer.this.createMediaTimer();
            MediaPlayer.this.setStatus(MediaPlayer.Status.PLAYING);
         });
      }

      public void onPause(PlayerStateEvent var1) {
         Platform.runLater(() -> {
            MediaPlayer.this.isUpdateTimeEnabled = false;
            MediaPlayer.this.setStatus(MediaPlayer.Status.PAUSED);
         });
         if (MediaPlayer.this.startTimeAtStop != null && MediaPlayer.this.startTimeAtStop != MediaPlayer.this.getStartTime()) {
            MediaPlayer.this.startTimeAtStop = null;
            Platform.runLater(() -> {
               MediaPlayer.this.setCurrentTime(MediaPlayer.this.getStartTime());
            });
         }

      }

      public void onStop(PlayerStateEvent var1) {
         Platform.runLater(() -> {
            MediaPlayer.this.destroyMediaTimer();
            MediaPlayer.this.startTimeAtStop = MediaPlayer.this.getStartTime();
            MediaPlayer.this.setCurrentTime(MediaPlayer.this.getStartTime());
            MediaPlayer.this.setStatus(MediaPlayer.Status.STOPPED);
         });
      }

      public void onStall(PlayerStateEvent var1) {
         Platform.runLater(() -> {
            MediaPlayer.this.isUpdateTimeEnabled = false;
            MediaPlayer.this.setStatus(MediaPlayer.Status.STALLED);
         });
      }

      void handleFinish() {
         MediaPlayer.this.setCurrentCount(MediaPlayer.this.getCurrentCount() + 1);
         if (MediaPlayer.this.getCurrentCount() >= MediaPlayer.this.getCycleCount() && MediaPlayer.this.getCycleCount() != -1) {
            MediaPlayer.this.isUpdateTimeEnabled = false;
            MediaPlayer.this.setCurrentRate(0.0);
            MediaPlayer.this.isEOS = true;
            if (MediaPlayer.this.getOnEndOfMedia() != null) {
               Platform.runLater(MediaPlayer.this.getOnEndOfMedia());
            }
         } else {
            if (MediaPlayer.this.getOnEndOfMedia() != null) {
               Platform.runLater(MediaPlayer.this.getOnEndOfMedia());
            }

            MediaPlayer.this.loopPlayback();
            if (MediaPlayer.this.getOnRepeat() != null) {
               Platform.runLater(MediaPlayer.this.getOnRepeat());
            }
         }

      }

      public void onFinish(PlayerStateEvent var1) {
         MediaPlayer.this.startTimeAtStop = null;
         Platform.runLater(() -> {
            this.handleFinish();
         });
      }

      public void onHalt(PlayerStateEvent var1) {
         Platform.runLater(() -> {
            MediaPlayer.this.setStatus(MediaPlayer.Status.HALTED);
            MediaPlayer.this.handleError(MediaException.haltException(var1.getMessage()));
            MediaPlayer.this.isUpdateTimeEnabled = false;
         });
      }

      // $FF: synthetic method
      _PlayerStateListener(Object var2) {
         this();
      }
   }

   private class _MarkerListener implements MarkerListener {
      private _MarkerListener() {
      }

      public void onMarker(MarkerEvent var1) {
         Platform.runLater(() -> {
            Duration var2 = Duration.millis(var1.getPresentationTime() * 1000.0);
            if (MediaPlayer.this.getOnMarker() != null) {
               MediaPlayer.this.getOnMarker().handle(new MediaMarkerEvent(new Pair(var1.getMarkerName(), var2)));
            }

         });
      }

      // $FF: synthetic method
      _MarkerListener(Object var2) {
         this();
      }
   }

   private class MarkerMapChangeListener implements MapChangeListener {
      private MarkerMapChangeListener() {
      }

      public void onChanged(MapChangeListener.Change var1) {
         synchronized(MediaPlayer.this.disposeLock) {
            if (MediaPlayer.this.getStatus() != MediaPlayer.Status.DISPOSED) {
               String var3 = (String)var1.getKey();
               if (var3 == null) {
                  return;
               }

               com.sun.media.jfxmedia.Media var4 = MediaPlayer.this.jfxPlayer.getMedia();
               if (var1.wasAdded()) {
                  if (var1.wasRemoved()) {
                     var4.removeMarker(var3);
                  }

                  Duration var5 = (Duration)var1.getValueAdded();
                  if (var5 != null && var5.greaterThanOrEqualTo(Duration.ZERO)) {
                     var4.addMarker(var3, ((Duration)var1.getValueAdded()).toMillis() / 1000.0);
                  }
               } else if (var1.wasRemoved()) {
                  var4.removeMarker(var3);
               }
            }

         }
      }

      // $FF: synthetic method
      MarkerMapChangeListener(Object var2) {
         this();
      }
   }

   private class InitMediaPlayer implements Runnable {
      private InitMediaPlayer() {
      }

      public void run() {
         try {
            MediaPlayer.this.init();
         } catch (com.sun.media.jfxmedia.MediaException var2) {
            MediaPlayer.this.handleError(MediaException.exceptionToMediaException(var2));
         } catch (MediaException var3) {
            if (MediaPlayer.this.media.getError() != null) {
               MediaPlayer.this.handleError(MediaPlayer.this.media.getError());
            } else {
               MediaPlayer.this.handleError(var3);
            }
         } catch (Exception var4) {
            MediaPlayer.this.handleError(new MediaException(MediaException.Type.UNKNOWN, var4.getMessage()));
         }

      }

      // $FF: synthetic method
      InitMediaPlayer(Object var2) {
         this();
      }
   }

   public static enum Status {
      UNKNOWN,
      READY,
      PAUSED,
      PLAYING,
      STOPPED,
      STALLED,
      HALTED,
      DISPOSED;
   }
}
