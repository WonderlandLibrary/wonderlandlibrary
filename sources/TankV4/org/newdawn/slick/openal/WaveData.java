package org.newdawn.slick.openal;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import org.lwjgl.LWJGLUtil;

public class WaveData {
   public final ByteBuffer data;
   public final int format;
   public final int samplerate;
   static Class class$org$newdawn$slick$openal$WaveData;

   private WaveData(ByteBuffer var1, int var2, int var3) {
      this.data = var1;
      this.format = var2;
      this.samplerate = var3;
   }

   public void dispose() {
      this.data.clear();
   }

   public static WaveData create(URL var0) {
      try {
         return create(AudioSystem.getAudioInputStream(new BufferedInputStream(var0.openStream())));
      } catch (Exception var2) {
         LWJGLUtil.log("Unable to create from: " + var0);
         var2.printStackTrace();
         return null;
      }
   }

   public static WaveData create(String var0) {
      return create((class$org$newdawn$slick$openal$WaveData == null ? (class$org$newdawn$slick$openal$WaveData = class$("org.newdawn.slick.openal.WaveData")) : class$org$newdawn$slick$openal$WaveData).getClassLoader().getResource(var0));
   }

   public static WaveData create(InputStream var0) {
      try {
         return create(AudioSystem.getAudioInputStream(var0));
      } catch (Exception var2) {
         LWJGLUtil.log("Unable to create from inputstream");
         var2.printStackTrace();
         return null;
      }
   }

   public static WaveData create(byte[] var0) {
      try {
         return create(AudioSystem.getAudioInputStream(new BufferedInputStream(new ByteArrayInputStream(var0))));
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static WaveData create(ByteBuffer var0) {
      try {
         Object var1 = null;
         byte[] var3;
         if (var0.hasArray()) {
            var3 = var0.array();
         } else {
            var3 = new byte[var0.capacity()];
            var0.get(var3);
         }

         return create(var3);
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static WaveData create(javax.sound.sampled.AudioInputStream var0) {
      AudioFormat var1 = var0.getFormat();
      boolean var2 = false;
      short var11;
      if (var1.getChannels() == 1) {
         if (var1.getSampleSizeInBits() == 8) {
            var11 = 4352;
         } else {
            if (var1.getSampleSizeInBits() != 16) {
               throw new RuntimeException("Illegal sample size");
            }

            var11 = 4353;
         }
      } else {
         if (var1.getChannels() != 2) {
            throw new RuntimeException("Only mono or stereo is supported");
         }

         if (var1.getSampleSizeInBits() == 8) {
            var11 = 4354;
         } else {
            if (var1.getSampleSizeInBits() != 16) {
               throw new RuntimeException("Illegal sample size");
            }

            var11 = 4355;
         }
      }

      byte[] var3 = new byte[var1.getChannels() * (int)var0.getFrameLength() * var1.getSampleSizeInBits() / 8];
      boolean var4 = false;
      int var5 = 0;

      int var12;
      try {
         while((var12 = var0.read(var3, var5, var3.length - var5)) != -1 && var5 < var3.length) {
            var5 += var12;
         }
      } catch (IOException var10) {
         return null;
      }

      ByteBuffer var6 = convertAudioBytes(var3, var1.getSampleSizeInBits() == 16);
      WaveData var7 = new WaveData(var6, var11, (int)var1.getSampleRate());

      try {
         var0.close();
      } catch (IOException var9) {
      }

      return var7;
   }

   private static ByteBuffer convertAudioBytes(byte[] var0, boolean var1) {
      ByteBuffer var2 = ByteBuffer.allocateDirect(var0.length);
      var2.order(ByteOrder.nativeOrder());
      ByteBuffer var3 = ByteBuffer.wrap(var0);
      var3.order(ByteOrder.LITTLE_ENDIAN);
      if (var1) {
         ShortBuffer var4 = var2.asShortBuffer();
         ShortBuffer var5 = var3.asShortBuffer();

         while(var5.hasRemaining()) {
            var4.put(var5.get());
         }
      } else {
         while(var3.hasRemaining()) {
            var2.put(var3.get());
         }
      }

      var2.rewind();
      return var2;
   }

   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw (new NoClassDefFoundError()).initCause(var2);
      }
   }
}
