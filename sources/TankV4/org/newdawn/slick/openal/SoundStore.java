package org.newdawn.slick.openal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.OpenALException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

public class SoundStore {
   private static SoundStore store = new SoundStore();
   private boolean sounds;
   private boolean music;
   private boolean soundWorks;
   private int sourceCount;
   private HashMap loaded = new HashMap();
   private int currentMusic = -1;
   private IntBuffer sources;
   private int nextSource;
   private boolean inited = false;
   private MODSound mod;
   private OpenALStreamPlayer stream;
   private float musicVolume = 1.0F;
   private float soundVolume = 1.0F;
   private float lastCurrentMusicVolume = 1.0F;
   private boolean paused;
   private boolean deferred;
   private FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3).put(new float[]{0.0F, 0.0F, 0.0F});
   private FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3);
   private int maxSources = 64;

   private SoundStore() {
   }

   public void clear() {
      store = new SoundStore();
   }

   public void disable() {
      this.inited = true;
   }

   public void setDeferredLoading(boolean var1) {
      this.deferred = var1;
   }

   public boolean isDeferredLoading() {
      return this.deferred;
   }

   public void setMusicOn(boolean var1) {
      if (this.soundWorks) {
         this.music = var1;
         if (var1) {
            this.restartLoop();
            this.setMusicVolume(this.musicVolume);
         } else {
            this.pauseLoop();
         }
      }

   }

   public boolean isMusicOn() {
      return this.music;
   }

   public void setMusicVolume(float var1) {
      if (var1 < 0.0F) {
         var1 = 0.0F;
      }

      if (var1 > 1.0F) {
         var1 = 1.0F;
      }

      this.musicVolume = var1;
      if (this.soundWorks) {
         AL10.alSourcef(this.sources.get(0), 4106, this.lastCurrentMusicVolume * this.musicVolume);
      }

   }

   public float getCurrentMusicVolume() {
      return this.lastCurrentMusicVolume;
   }

   public void setCurrentMusicVolume(float var1) {
      if (var1 < 0.0F) {
         var1 = 0.0F;
      }

      if (var1 > 1.0F) {
         var1 = 1.0F;
      }

      if (this.soundWorks) {
         this.lastCurrentMusicVolume = var1;
         AL10.alSourcef(this.sources.get(0), 4106, this.lastCurrentMusicVolume * this.musicVolume);
      }

   }

   public void setSoundVolume(float var1) {
      if (var1 < 0.0F) {
         var1 = 0.0F;
      }

      this.soundVolume = var1;
   }

   public boolean soundWorks() {
      return this.soundWorks;
   }

   public boolean musicOn() {
      return this.music;
   }

   public float getSoundVolume() {
      return this.soundVolume;
   }

   public float getMusicVolume() {
      return this.musicVolume;
   }

   public int getSource(int var1) {
      if (!this.soundWorks) {
         return -1;
      } else {
         return var1 < 0 ? -1 : this.sources.get(var1);
      }
   }

   public void setSoundsOn(boolean var1) {
      if (this.soundWorks) {
         this.sounds = var1;
      }

   }

   public boolean soundsOn() {
      return this.sounds;
   }

   public void setMaxSources(int var1) {
      this.maxSources = var1;
   }

   public void init() {
      if (!this.inited) {
         Log.info("Initialising sounds..");
         this.inited = true;
         AccessController.doPrivileged(new PrivilegedAction(this) {
            private final SoundStore this$0;

            {
               this.this$0 = var1;
            }

            public Object run() {
               try {
                  AL.create();
                  SoundStore.access$002(this.this$0, true);
                  SoundStore.access$102(this.this$0, true);
                  SoundStore.access$202(this.this$0, true);
                  Log.info("- Sound works");
               } catch (Exception var2) {
                  Log.error("Sound initialisation failure.");
                  Log.error((Throwable)var2);
                  SoundStore.access$002(this.this$0, false);
                  SoundStore.access$102(this.this$0, false);
                  SoundStore.access$202(this.this$0, false);
               }

               return null;
            }
         });
         if (this.soundWorks) {
            this.sourceCount = 0;
            this.sources = BufferUtils.createIntBuffer(this.maxSources);

            while(AL10.alGetError() == 0) {
               IntBuffer var1 = BufferUtils.createIntBuffer(1);

               try {
                  AL10.alGenSources(var1);
                  if (AL10.alGetError() == 0) {
                     ++this.sourceCount;
                     this.sources.put(var1.get(0));
                     if (this.sourceCount > this.maxSources - 1) {
                        break;
                     }
                  }
               } catch (OpenALException var4) {
                  break;
               }
            }

            Log.info("- " + this.sourceCount + " OpenAL source available");
            if (AL10.alGetError() != 0) {
               this.sounds = false;
               this.music = false;
               this.soundWorks = false;
               Log.error("- AL init failed");
            } else {
               FloatBuffer var5 = BufferUtils.createFloatBuffer(6).put(new float[]{0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 0.0F});
               FloatBuffer var2 = BufferUtils.createFloatBuffer(3).put(new float[]{0.0F, 0.0F, 0.0F});
               FloatBuffer var3 = BufferUtils.createFloatBuffer(3).put(new float[]{0.0F, 0.0F, 0.0F});
               var3.flip();
               var2.flip();
               var5.flip();
               AL10.alListener(4100, var3);
               AL10.alListener(4102, var2);
               AL10.alListener(4111, var5);
               Log.info("- Sounds source generated");
            }
         }

      }
   }

   void stopSource(int var1) {
      AL10.alSourceStop(this.sources.get(var1));
   }

   int playAsSound(int var1, float var2, float var3, boolean var4) {
      return this.playAsSoundAt(var1, var2, var3, var4, 0.0F, 0.0F, 0.0F);
   }

   int playAsSoundAt(int var1, float var2, float var3, boolean var4, float var5, float var6, float var7) {
      var3 *= this.soundVolume;
      if (var3 == 0.0F) {
         var3 = 0.001F;
      }

      if (this.soundWorks && this.sounds) {
         int var8 = this.findFreeSource();
         if (var8 == -1) {
            return -1;
         } else {
            AL10.alSourceStop(this.sources.get(var8));
            AL10.alSourcei(this.sources.get(var8), 4105, var1);
            AL10.alSourcef(this.sources.get(var8), 4099, var2);
            AL10.alSourcef(this.sources.get(var8), 4106, var3);
            AL10.alSourcei(this.sources.get(var8), 4103, var4 ? 1 : 0);
            this.sourcePos.clear();
            this.sourceVel.clear();
            this.sourceVel.put(new float[]{0.0F, 0.0F, 0.0F});
            this.sourcePos.put(new float[]{var5, var6, var7});
            this.sourcePos.flip();
            this.sourceVel.flip();
            AL10.alSource(this.sources.get(var8), 4100, this.sourcePos);
            AL10.alSource(this.sources.get(var8), 4102, this.sourceVel);
            AL10.alSourcePlay(this.sources.get(var8));
            return var8;
         }
      } else {
         return -1;
      }
   }

   boolean isPlaying(int var1) {
      int var2 = AL10.alGetSourcei(this.sources.get(var1), 4112);
      return var2 == 4114;
   }

   private int findFreeSource() {
      for(int var1 = 1; var1 < this.sourceCount - 1; ++var1) {
         int var2 = AL10.alGetSourcei(this.sources.get(var1), 4112);
         if (var2 != 4114 && var2 != 4115) {
            return var1;
         }
      }

      return -1;
   }

   void playAsMusic(int var1, float var2, float var3, boolean var4) {
      this.paused = false;
      this.setMOD((MODSound)null);
      if (this.soundWorks) {
         if (this.currentMusic != -1) {
            AL10.alSourceStop(this.sources.get(0));
         }

         this.getMusicSource();
         AL10.alSourcei(this.sources.get(0), 4105, var1);
         AL10.alSourcef(this.sources.get(0), 4099, var2);
         AL10.alSourcei(this.sources.get(0), 4103, var4 ? 1 : 0);
         this.currentMusic = this.sources.get(0);
         if (!this.music) {
            this.pauseLoop();
         } else {
            AL10.alSourcePlay(this.sources.get(0));
         }
      }

   }

   private int getMusicSource() {
      return this.sources.get(0);
   }

   public void setMusicPitch(float var1) {
      if (this.soundWorks) {
         AL10.alSourcef(this.sources.get(0), 4099, var1);
      }

   }

   public void pauseLoop() {
      if (this.soundWorks && this.currentMusic != -1) {
         this.paused = true;
         AL10.alSourcePause(this.currentMusic);
      }

   }

   public void restartLoop() {
      if (this.music && this.soundWorks && this.currentMusic != -1) {
         this.paused = false;
         AL10.alSourcePlay(this.currentMusic);
      }

   }

   boolean isPlaying(OpenALStreamPlayer var1) {
      return this.stream == var1;
   }

   public Audio getMOD(String var1) throws IOException {
      return this.getMOD(var1, ResourceLoader.getResourceAsStream(var1));
   }

   public Audio getMOD(InputStream var1) throws IOException {
      return this.getMOD(var1.toString(), var1);
   }

   public Audio getMOD(String var1, InputStream var2) throws IOException {
      if (!this.soundWorks) {
         return new NullAudio();
      } else if (!this.inited) {
         throw new RuntimeException("Can't load sounds until SoundStore is init(). Use the container init() method.");
      } else {
         return (Audio)(this.deferred ? new DeferredSound(var1, var2, 3) : new MODSound(this, var2));
      }
   }

   public Audio getAIF(String var1) throws IOException {
      return this.getAIF(var1, ResourceLoader.getResourceAsStream(var1));
   }

   public Audio getAIF(InputStream var1) throws IOException {
      return this.getAIF(var1.toString(), var1);
   }

   public Audio getAIF(String var1, InputStream var2) throws IOException {
      BufferedInputStream var7 = new BufferedInputStream(var2);
      if (!this.soundWorks) {
         return new NullAudio();
      } else if (!this.inited) {
         throw new RuntimeException("Can't load sounds until SoundStore is init(). Use the container init() method.");
      } else if (this.deferred) {
         return new DeferredSound(var1, var7, 4);
      } else {
         boolean var3 = true;
         int var8;
         if (this.loaded.get(var1) != null) {
            var8 = (Integer)this.loaded.get(var1);
         } else {
            try {
               IntBuffer var4 = BufferUtils.createIntBuffer(1);
               AiffData var9 = AiffData.create((InputStream)var7);
               AL10.alGenBuffers(var4);
               AL10.alBufferData(var4.get(0), var9.format, var9.data, var9.samplerate);
               this.loaded.put(var1, new Integer(var4.get(0)));
               var8 = var4.get(0);
            } catch (Exception var6) {
               Log.error((Throwable)var6);
               IOException var5 = new IOException("Failed to load: " + var1);
               var5.initCause(var6);
               throw var5;
            }
         }

         if (var8 == -1) {
            throw new IOException("Unable to load: " + var1);
         } else {
            return new AudioImpl(this, var8);
         }
      }
   }

   public Audio getWAV(String var1) throws IOException {
      return this.getWAV(var1, ResourceLoader.getResourceAsStream(var1));
   }

   public Audio getWAV(InputStream var1) throws IOException {
      return this.getWAV(var1.toString(), var1);
   }

   public Audio getWAV(String var1, InputStream var2) throws IOException {
      if (!this.soundWorks) {
         return new NullAudio();
      } else if (!this.inited) {
         throw new RuntimeException("Can't load sounds until SoundStore is init(). Use the container init() method.");
      } else if (this.deferred) {
         return new DeferredSound(var1, var2, 2);
      } else {
         boolean var3 = true;
         int var7;
         if (this.loaded.get(var1) != null) {
            var7 = (Integer)this.loaded.get(var1);
         } else {
            try {
               IntBuffer var4 = BufferUtils.createIntBuffer(1);
               WaveData var8 = WaveData.create(var2);
               AL10.alGenBuffers(var4);
               AL10.alBufferData(var4.get(0), var8.format, var8.data, var8.samplerate);
               this.loaded.put(var1, new Integer(var4.get(0)));
               var7 = var4.get(0);
            } catch (Exception var6) {
               Log.error((Throwable)var6);
               IOException var5 = new IOException("Failed to load: " + var1);
               var5.initCause(var6);
               throw var5;
            }
         }

         if (var7 == -1) {
            throw new IOException("Unable to load: " + var1);
         } else {
            return new AudioImpl(this, var7);
         }
      }
   }

   public Audio getOggStream(String var1) throws IOException {
      if (!this.soundWorks) {
         return new NullAudio();
      } else {
         this.setMOD((MODSound)null);
         this.setStream((OpenALStreamPlayer)null);
         if (this.currentMusic != -1) {
            AL10.alSourceStop(this.sources.get(0));
         }

         this.getMusicSource();
         this.currentMusic = this.sources.get(0);
         return new StreamSound(new OpenALStreamPlayer(this.currentMusic, var1));
      }
   }

   public Audio getOggStream(URL var1) throws IOException {
      if (!this.soundWorks) {
         return new NullAudio();
      } else {
         this.setMOD((MODSound)null);
         this.setStream((OpenALStreamPlayer)null);
         if (this.currentMusic != -1) {
            AL10.alSourceStop(this.sources.get(0));
         }

         this.getMusicSource();
         this.currentMusic = this.sources.get(0);
         return new StreamSound(new OpenALStreamPlayer(this.currentMusic, var1));
      }
   }

   public Audio getOgg(String var1) throws IOException {
      return this.getOgg(var1, ResourceLoader.getResourceAsStream(var1));
   }

   public Audio getOgg(InputStream var1) throws IOException {
      return this.getOgg(var1.toString(), var1);
   }

   public Audio getOgg(String var1, InputStream var2) throws IOException {
      if (!this.soundWorks) {
         return new NullAudio();
      } else if (!this.inited) {
         throw new RuntimeException("Can't load sounds until SoundStore is init(). Use the container init() method.");
      } else if (this.deferred) {
         return new DeferredSound(var1, var2, 1);
      } else {
         boolean var3 = true;
         int var8;
         if (this.loaded.get(var1) != null) {
            var8 = (Integer)this.loaded.get(var1);
         } else {
            try {
               IntBuffer var4 = BufferUtils.createIntBuffer(1);
               OggDecoder var5 = new OggDecoder();
               OggData var6 = var5.getData(var2);
               AL10.alGenBuffers(var4);
               AL10.alBufferData(var4.get(0), var6.channels > 1 ? 4355 : 4353, var6.data, var6.rate);
               this.loaded.put(var1, new Integer(var4.get(0)));
               var8 = var4.get(0);
            } catch (Exception var7) {
               Log.error((Throwable)var7);
               Sys.alert("Error", "Failed to load: " + var1 + " - " + var7.getMessage());
               throw new IOException("Unable to load: " + var1);
            }
         }

         if (var8 == -1) {
            throw new IOException("Unable to load: " + var1);
         } else {
            return new AudioImpl(this, var8);
         }
      }
   }

   void setMOD(MODSound var1) {
      if (this.soundWorks) {
         this.currentMusic = this.sources.get(0);
         this.stopSource(0);
         this.mod = var1;
         if (var1 != null) {
            this.stream = null;
         }

         this.paused = false;
      }
   }

   void setStream(OpenALStreamPlayer var1) {
      if (this.soundWorks) {
         this.currentMusic = this.sources.get(0);
         this.stream = var1;
         if (var1 != null) {
            this.mod = null;
         }

         this.paused = false;
      }
   }

   public void poll(int var1) {
      if (this.soundWorks) {
         if (!this.paused) {
            if (this.music) {
               if (this.mod != null) {
                  try {
                     this.mod.poll();
                  } catch (OpenALException var4) {
                     Log.error("Error with OpenGL MOD Player on this this platform");
                     Log.error((Throwable)var4);
                     this.mod = null;
                  }
               }

               if (this.stream != null) {
                  try {
                     this.stream.update();
                  } catch (OpenALException var3) {
                     Log.error("Error with OpenGL Streaming Player on this this platform");
                     Log.error((Throwable)var3);
                     this.mod = null;
                  }
               }
            }

         }
      }
   }

   public boolean isMusicPlaying() {
      if (!this.soundWorks) {
         return false;
      } else {
         int var1 = AL10.alGetSourcei(this.sources.get(0), 4112);
         return var1 == 4114 || var1 == 4115;
      }
   }

   public static SoundStore get() {
      return store;
   }

   public void stopSoundEffect(int var1) {
      AL10.alSourceStop(var1);
   }

   public int getSourceCount() {
      return this.sourceCount;
   }

   static boolean access$002(SoundStore var0, boolean var1) {
      return var0.soundWorks = var1;
   }

   static boolean access$102(SoundStore var0, boolean var1) {
      return var0.sounds = var1;
   }

   static boolean access$202(SoundStore var0, boolean var1) {
      return var0.music = var1;
   }
}
