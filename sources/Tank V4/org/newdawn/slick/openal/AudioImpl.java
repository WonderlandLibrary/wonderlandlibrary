package org.newdawn.slick.openal;

import org.lwjgl.openal.AL10;

public class AudioImpl implements Audio {
   private SoundStore store;
   private int buffer;
   private int index = -1;
   private float length;

   AudioImpl(SoundStore var1, int var2) {
      this.store = var1;
      this.buffer = var2;
      int var3 = AL10.alGetBufferi(var2, 8196);
      int var4 = AL10.alGetBufferi(var2, 8194);
      int var5 = AL10.alGetBufferi(var2, 8195);
      int var6 = AL10.alGetBufferi(var2, 8193);
      int var7 = var3 / (var4 / 8);
      this.length = (float)var7 / (float)var6 / (float)var5;
   }

   public int getBufferID() {
      return this.buffer;
   }

   protected AudioImpl() {
   }

   public void stop() {
      if (this.index != -1) {
         this.store.stopSource(this.index);
      }

   }

   public boolean isPlaying() {
      return this.index != -1 ? this.store.isPlaying(this.index) : false;
   }

   public int playAsSoundEffect(float var1, float var2, boolean var3) {
      this.index = this.store.playAsSound(this.buffer, var1, var2, var3);
      return this.store.getSource(this.index);
   }

   public int playAsSoundEffect(float var1, float var2, boolean var3, float var4, float var5, float var6) {
      this.index = this.store.playAsSoundAt(this.buffer, var1, var2, var3, var4, var5, var6);
      return this.store.getSource(this.index);
   }

   public int playAsMusic(float var1, float var2, boolean var3) {
      this.store.playAsMusic(this.buffer, var1, var2, var3);
      this.index = 0;
      return this.store.getSource(0);
   }

   public static void pauseMusic() {
      SoundStore.get().pauseLoop();
   }

   public static void restartMusic() {
      SoundStore.get().restartLoop();
   }

   public boolean setPosition(float var1) {
      var1 %= this.length;
      AL10.alSourcef(this.store.getSource(this.index), 4132, var1);
      return AL10.alGetError() == 0;
   }

   public float getPosition() {
      return AL10.alGetSourcef(this.store.getSource(this.index), 4132);
   }
}
