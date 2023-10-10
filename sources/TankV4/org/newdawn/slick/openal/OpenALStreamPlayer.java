package org.newdawn.slick.openal;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

public class OpenALStreamPlayer {
   public static final int BUFFER_COUNT = 3;
   private static final int sectionSize = 81920;
   private byte[] buffer = new byte[81920];
   private IntBuffer bufferNames;
   private ByteBuffer bufferData = BufferUtils.createByteBuffer(81920);
   private IntBuffer unqueued = BufferUtils.createIntBuffer(1);
   private int source;
   private int remainingBufferCount;
   private boolean loop;
   private boolean done = true;
   private AudioInputStream audio;
   private String ref;
   private URL url;
   private float pitch;
   private float positionOffset;

   public OpenALStreamPlayer(int var1, String var2) {
      this.source = var1;
      this.ref = var2;
      this.bufferNames = BufferUtils.createIntBuffer(3);
      AL10.alGenBuffers(this.bufferNames);
   }

   public OpenALStreamPlayer(int var1, URL var2) {
      this.source = var1;
      this.url = var2;
      this.bufferNames = BufferUtils.createIntBuffer(3);
      AL10.alGenBuffers(this.bufferNames);
   }

   private void initStreams() throws IOException {
      if (this.audio != null) {
         this.audio.close();
      }

      OggInputStream var1;
      if (this.url != null) {
         var1 = new OggInputStream(this.url.openStream());
      } else {
         var1 = new OggInputStream(ResourceLoader.getResourceAsStream(this.ref));
      }

      this.audio = var1;
      this.positionOffset = 0.0F;
   }

   public String getSource() {
      return this.url == null ? this.ref : this.url.toString();
   }

   private void removeBuffers() {
      IntBuffer var1 = BufferUtils.createIntBuffer(1);

      for(int var2 = AL10.alGetSourcei(this.source, 4117); var2 > 0; --var2) {
         AL10.alSourceUnqueueBuffers(this.source, var1);
      }

   }

   public void play(boolean var1) throws IOException {
      this.loop = var1;
      this.initStreams();
      this.done = false;
      AL10.alSourceStop(this.source);
      this.removeBuffers();
      this.startPlayback();
   }

   public void setup(float var1) {
      this.pitch = var1;
   }

   public boolean done() {
      return this.done;
   }

   public void update() {
      if (!this.done) {
         float var1 = (float)this.audio.getRate();
         float var2;
         if (this.audio.getChannels() > 1) {
            var2 = 4.0F;
         } else {
            var2 = 2.0F;
         }

         int var4;
         for(int var3 = AL10.alGetSourcei(this.source, 4118); var3 > 0; --var3) {
            this.unqueued.clear();
            AL10.alSourceUnqueueBuffers(this.source, this.unqueued);
            var4 = this.unqueued.get(0);
            float var5 = (float)AL10.alGetBufferi(var4, 8196) / var2 / var1;
            this.positionOffset += var5;
            if (this != var4) {
               AL10.alSourceQueueBuffers(this.source, this.unqueued);
            } else {
               --this.remainingBufferCount;
               if (this.remainingBufferCount == 0) {
                  this.done = true;
               }
            }
         }

         var4 = AL10.alGetSourcei(this.source, 4112);
         if (var4 != 4114) {
            AL10.alSourcePlay(this.source);
         }

      }
   }

   public boolean setPosition(float var1) {
      try {
         if (this.getPosition() > var1) {
            this.initStreams();
         }

         float var2 = (float)this.audio.getRate();
         float var3;
         if (this.audio.getChannels() > 1) {
            var3 = 4.0F;
         } else {
            var3 = 2.0F;
         }

         while(this.positionOffset < var1) {
            int var4 = this.audio.read(this.buffer);
            if (var4 == -1) {
               if (this.loop) {
                  this.initStreams();
               } else {
                  this.done = true;
               }

               return false;
            }

            float var5 = (float)var4 / var3 / var2;
            this.positionOffset += var5;
         }

         this.startPlayback();
         return true;
      } catch (IOException var6) {
         Log.error((Throwable)var6);
         return false;
      }
   }

   private void startPlayback() {
      AL10.alSourcei(this.source, 4103, 0);
      AL10.alSourcef(this.source, 4099, this.pitch);
      this.remainingBufferCount = 3;

      for(int var1 = 0; var1 < 3; ++var1) {
         this.stream(this.bufferNames.get(var1));
      }

      AL10.alSourceQueueBuffers(this.source, this.bufferNames);
      AL10.alSourcePlay(this.source);
   }

   public float getPosition() {
      return this.positionOffset + AL10.alGetSourcef(this.source, 4132);
   }
}
