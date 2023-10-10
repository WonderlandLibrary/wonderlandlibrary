package org.newdawn.slick.openal;

import ibxm.Module;
import ibxm.OpenALMODPlayer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

public class MODSound extends AudioImpl {
   private static OpenALMODPlayer player = new OpenALMODPlayer();
   private Module module;
   private SoundStore store;

   public MODSound(SoundStore var1, InputStream var2) throws IOException {
      this.store = var1;
      this.module = OpenALMODPlayer.loadModule(var2);
   }

   public int playAsMusic(float var1, float var2, boolean var3) {
      this.cleanUpSource();
      player.play(this.module, this.store.getSource(0), var3, SoundStore.get().isMusicOn());
      player.setup(var1, 1.0F);
      this.store.setCurrentMusicVolume(var2);
      this.store.setMOD(this);
      return this.store.getSource(0);
   }

   private void cleanUpSource() {
      AL10.alSourceStop(this.store.getSource(0));
      IntBuffer var1 = BufferUtils.createIntBuffer(1);

      for(int var2 = AL10.alGetSourcei(this.store.getSource(0), 4117); var2 > 0; --var2) {
         AL10.alSourceUnqueueBuffers(this.store.getSource(0), var1);
      }

      AL10.alSourcei(this.store.getSource(0), 4105, 0);
   }

   public void poll() {
      player.update();
   }

   public int playAsSoundEffect(float var1, float var2, boolean var3) {
      return -1;
   }

   public void stop() {
      this.store.setMOD((MODSound)null);
   }

   public float getPosition() {
      throw new RuntimeException("Positioning on modules is not currently supported");
   }

   public boolean setPosition(float var1) {
      throw new RuntimeException("Positioning on modules is not currently supported");
   }
}
