package org.newdawn.slick.openal;

import java.io.IOException;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.newdawn.slick.util.Log;

public class StreamSound extends AudioImpl {
   private OpenALStreamPlayer player;

   public StreamSound(OpenALStreamPlayer var1) {
      this.player = var1;
   }

   public boolean isPlaying() {
      return SoundStore.get().isPlaying(this.player);
   }

   public int playAsMusic(float var1, float var2, boolean var3) {
      try {
         this.cleanUpSource();
         this.player.setup(var1);
         this.player.play(var3);
         SoundStore.get().setStream(this.player);
      } catch (IOException var5) {
         Log.error("Failed to read OGG source: " + this.player.getSource());
      }

      return SoundStore.get().getSource(0);
   }

   private void cleanUpSource() {
      SoundStore var1 = SoundStore.get();
      AL10.alSourceStop(var1.getSource(0));
      IntBuffer var2 = BufferUtils.createIntBuffer(1);

      for(int var3 = AL10.alGetSourcei(var1.getSource(0), 4117); var3 > 0; --var3) {
         AL10.alSourceUnqueueBuffers(var1.getSource(0), var2);
      }

      AL10.alSourcei(var1.getSource(0), 4105, 0);
   }

   public int playAsSoundEffect(float var1, float var2, boolean var3, float var4, float var5, float var6) {
      return this.playAsMusic(var1, var2, var3);
   }

   public int playAsSoundEffect(float var1, float var2, boolean var3) {
      return this.playAsMusic(var1, var2, var3);
   }

   public void stop() {
      SoundStore.get().setStream((OpenALStreamPlayer)null);
   }

   public boolean setPosition(float var1) {
      return this.player.setPosition(var1);
   }

   public float getPosition() {
      return this.player.getPosition();
   }
}
