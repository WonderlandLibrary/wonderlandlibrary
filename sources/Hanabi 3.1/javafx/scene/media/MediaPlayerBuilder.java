package javafx.scene.media;

import javafx.event.EventHandler;
import javafx.util.Builder;
import javafx.util.Duration;

/** @deprecated */
@Deprecated
public final class MediaPlayerBuilder implements Builder {
   private int __set;
   private double audioSpectrumInterval;
   private AudioSpectrumListener audioSpectrumListener;
   private int audioSpectrumNumBands;
   private int audioSpectrumThreshold;
   private boolean autoPlay;
   private double balance;
   private int cycleCount;
   private Media media;
   private boolean mute;
   private Runnable onEndOfMedia;
   private Runnable onError;
   private Runnable onHalted;
   private EventHandler onMarker;
   private Runnable onPaused;
   private Runnable onPlaying;
   private Runnable onReady;
   private Runnable onRepeat;
   private Runnable onStalled;
   private Runnable onStopped;
   private double rate;
   private Duration startTime;
   private Duration stopTime;
   private double volume;

   protected MediaPlayerBuilder() {
   }

   public static MediaPlayerBuilder create() {
      return new MediaPlayerBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(MediaPlayer var1) {
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setAudioSpectrumInterval(this.audioSpectrumInterval);
               break;
            case 1:
               var1.setAudioSpectrumListener(this.audioSpectrumListener);
               break;
            case 2:
               var1.setAudioSpectrumNumBands(this.audioSpectrumNumBands);
               break;
            case 3:
               var1.setAudioSpectrumThreshold(this.audioSpectrumThreshold);
               break;
            case 4:
               var1.setAutoPlay(this.autoPlay);
               break;
            case 5:
               var1.setBalance(this.balance);
               break;
            case 6:
               var1.setCycleCount(this.cycleCount);
               break;
            case 7:
               var1.setMute(this.mute);
               break;
            case 8:
               var1.setOnEndOfMedia(this.onEndOfMedia);
               break;
            case 9:
               var1.setOnError(this.onError);
               break;
            case 10:
               var1.setOnHalted(this.onHalted);
               break;
            case 11:
               var1.setOnMarker(this.onMarker);
               break;
            case 12:
               var1.setOnPaused(this.onPaused);
               break;
            case 13:
               var1.setOnPlaying(this.onPlaying);
               break;
            case 14:
               var1.setOnReady(this.onReady);
               break;
            case 15:
               var1.setOnRepeat(this.onRepeat);
               break;
            case 16:
               var1.setOnStalled(this.onStalled);
               break;
            case 17:
               var1.setOnStopped(this.onStopped);
               break;
            case 18:
               var1.setRate(this.rate);
               break;
            case 19:
               var1.setStartTime(this.startTime);
               break;
            case 20:
               var1.setStopTime(this.stopTime);
               break;
            case 21:
               var1.setVolume(this.volume);
         }
      }

   }

   public MediaPlayerBuilder audioSpectrumInterval(double var1) {
      this.audioSpectrumInterval = var1;
      this.__set(0);
      return this;
   }

   public MediaPlayerBuilder audioSpectrumListener(AudioSpectrumListener var1) {
      this.audioSpectrumListener = var1;
      this.__set(1);
      return this;
   }

   public MediaPlayerBuilder audioSpectrumNumBands(int var1) {
      this.audioSpectrumNumBands = var1;
      this.__set(2);
      return this;
   }

   public MediaPlayerBuilder audioSpectrumThreshold(int var1) {
      this.audioSpectrumThreshold = var1;
      this.__set(3);
      return this;
   }

   public MediaPlayerBuilder autoPlay(boolean var1) {
      this.autoPlay = var1;
      this.__set(4);
      return this;
   }

   public MediaPlayerBuilder balance(double var1) {
      this.balance = var1;
      this.__set(5);
      return this;
   }

   public MediaPlayerBuilder cycleCount(int var1) {
      this.cycleCount = var1;
      this.__set(6);
      return this;
   }

   public MediaPlayerBuilder media(Media var1) {
      this.media = var1;
      return this;
   }

   public MediaPlayerBuilder mute(boolean var1) {
      this.mute = var1;
      this.__set(7);
      return this;
   }

   public MediaPlayerBuilder onEndOfMedia(Runnable var1) {
      this.onEndOfMedia = var1;
      this.__set(8);
      return this;
   }

   public MediaPlayerBuilder onError(Runnable var1) {
      this.onError = var1;
      this.__set(9);
      return this;
   }

   public MediaPlayerBuilder onHalted(Runnable var1) {
      this.onHalted = var1;
      this.__set(10);
      return this;
   }

   public MediaPlayerBuilder onMarker(EventHandler var1) {
      this.onMarker = var1;
      this.__set(11);
      return this;
   }

   public MediaPlayerBuilder onPaused(Runnable var1) {
      this.onPaused = var1;
      this.__set(12);
      return this;
   }

   public MediaPlayerBuilder onPlaying(Runnable var1) {
      this.onPlaying = var1;
      this.__set(13);
      return this;
   }

   public MediaPlayerBuilder onReady(Runnable var1) {
      this.onReady = var1;
      this.__set(14);
      return this;
   }

   public MediaPlayerBuilder onRepeat(Runnable var1) {
      this.onRepeat = var1;
      this.__set(15);
      return this;
   }

   public MediaPlayerBuilder onStalled(Runnable var1) {
      this.onStalled = var1;
      this.__set(16);
      return this;
   }

   public MediaPlayerBuilder onStopped(Runnable var1) {
      this.onStopped = var1;
      this.__set(17);
      return this;
   }

   public MediaPlayerBuilder rate(double var1) {
      this.rate = var1;
      this.__set(18);
      return this;
   }

   public MediaPlayerBuilder startTime(Duration var1) {
      this.startTime = var1;
      this.__set(19);
      return this;
   }

   public MediaPlayerBuilder stopTime(Duration var1) {
      this.stopTime = var1;
      this.__set(20);
      return this;
   }

   public MediaPlayerBuilder volume(double var1) {
      this.volume = var1;
      this.__set(21);
      return this;
   }

   public MediaPlayer build() {
      MediaPlayer var1 = new MediaPlayer(this.media);
      this.applyTo(var1);
      return var1;
   }
}
