package javafx.scene.media;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public final class AudioClipBuilder implements Builder {
   private int __set;
   private double balance;
   private int cycleCount;
   private double pan;
   private int priority;
   private double rate;
   private String source;
   private double volume;

   protected AudioClipBuilder() {
   }

   public static AudioClipBuilder create() {
      return new AudioClipBuilder();
   }

   public void applyTo(AudioClip var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setBalance(this.balance);
      }

      if ((var2 & 2) != 0) {
         var1.setCycleCount(this.cycleCount);
      }

      if ((var2 & 4) != 0) {
         var1.setPan(this.pan);
      }

      if ((var2 & 8) != 0) {
         var1.setPriority(this.priority);
      }

      if ((var2 & 16) != 0) {
         var1.setRate(this.rate);
      }

      if ((var2 & 32) != 0) {
         var1.setVolume(this.volume);
      }

   }

   public AudioClipBuilder balance(double var1) {
      this.balance = var1;
      this.__set |= 1;
      return this;
   }

   public AudioClipBuilder cycleCount(int var1) {
      this.cycleCount = var1;
      this.__set |= 2;
      return this;
   }

   public AudioClipBuilder pan(double var1) {
      this.pan = var1;
      this.__set |= 4;
      return this;
   }

   public AudioClipBuilder priority(int var1) {
      this.priority = var1;
      this.__set |= 8;
      return this;
   }

   public AudioClipBuilder rate(double var1) {
      this.rate = var1;
      this.__set |= 16;
      return this;
   }

   public AudioClipBuilder source(String var1) {
      this.source = var1;
      return this;
   }

   public AudioClipBuilder volume(double var1) {
      this.volume = var1;
      this.__set |= 32;
      return this;
   }

   public AudioClip build() {
      AudioClip var1 = new AudioClip(this.source);
      this.applyTo(var1);
      return var1;
   }
}
