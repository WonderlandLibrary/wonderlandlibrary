package org.lwjgl.util;

import com.sun.media.sound.WaveFileReader;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.lwjgl.LWJGLUtil;

public class WaveData {
   public final ByteBuffer data;
   public final int format;
   public final int samplerate;
   static final boolean $assertionsDisabled = !WaveData.class.desiredAssertionStatus();

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
         WaveFileReader var1 = new WaveFileReader();
         return create(var1.getAudioInputStream(new BufferedInputStream(var0.openStream())));
      } catch (Exception var2) {
         LWJGLUtil.log("Unable to create from: " + var0 + ", " + var2.getMessage());
         return null;
      }
   }

   public static WaveData create(String var0) {
      return create(Thread.currentThread().getContextClassLoader().getResource(var0));
   }

   public static WaveData create(InputStream var0) {
      try {
         return create(AudioSystem.getAudioInputStream(var0));
      } catch (Exception var2) {
         LWJGLUtil.log("Unable to create from inputstream, " + var2.getMessage());
         return null;
      }
   }

   public static WaveData create(byte[] var0) {
      try {
         return create(AudioSystem.getAudioInputStream(new BufferedInputStream(new ByteArrayInputStream(var0))));
      } catch (Exception var2) {
         LWJGLUtil.log("Unable to create from byte array, " + var2.getMessage());
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
         LWJGLUtil.log("Unable to create from ByteBuffer, " + var2.getMessage());
         return null;
      }
   }

   public static WaveData create(AudioInputStream var0) {
      AudioFormat var1 = var0.getFormat();
      short var2 = 0;
      if (var1.getChannels() == 1) {
         if (var1.getSampleSizeInBits() == 8) {
            var2 = 4352;
         } else if (var1.getSampleSizeInBits() == 16) {
            var2 = 4353;
         } else if (!$assertionsDisabled) {
            throw new AssertionError("Illegal sample size");
         }
      } else if (var1.getChannels() == 2) {
         if (var1.getSampleSizeInBits() == 8) {
            var2 = 4354;
         } else if (var1.getSampleSizeInBits() == 16) {
            var2 = 4355;
         } else if (!$assertionsDisabled) {
            throw new AssertionError("Illegal sample size");
         }
      } else if (!$assertionsDisabled) {
         throw new AssertionError("Only mono or stereo is supported");
      }

      ByteBuffer var3 = null;

      try {
         int var4 = var0.available();
         if (var4 <= 0) {
            var4 = var0.getFormat().getChannels() * (int)var0.getFrameLength() * var0.getFormat().getSampleSizeInBits() / 8;
         }

         byte[] var5 = new byte[var0.available()];
         boolean var6 = false;

         int var11;
         for(int var7 = 0; (var11 = var0.read(var5, var7, var5.length - var7)) != -1 && var7 < var5.length; var7 += var11) {
         }

         var3 = convertAudioBytes(var5, var1.getSampleSizeInBits() == 16, var1.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
      } catch (IOException var9) {
         return null;
      }

      WaveData var10 = new WaveData(var3, var2, (int)var1.getSampleRate());

      try {
         var0.close();
      } catch (IOException var8) {
      }

      return var10;
   }

   private static ByteBuffer convertAudioBytes(byte[] var0, boolean var1, ByteOrder var2) {
      ByteBuffer var3 = ByteBuffer.allocateDirect(var0.length);
      var3.order(ByteOrder.nativeOrder());
      ByteBuffer var4 = ByteBuffer.wrap(var0);
      var4.order(var2);
      if (var1) {
         ShortBuffer var5 = var3.asShortBuffer();
         ShortBuffer var6 = var4.asShortBuffer();

         while(var6.hasRemaining()) {
            var5.put(var6.get());
         }
      } else {
         while(var4.hasRemaining()) {
            var3.put(var4.get());
         }
      }

      var3.rewind();
      return var3;
   }
}
