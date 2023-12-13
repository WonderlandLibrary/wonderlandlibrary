package paulscode.sound.codecs;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import javax.sound.sampled.AudioFormat;
import paulscode.sound.ICodec;
import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemLogger;

public class CodecJOrbis implements ICodec {
   private static final boolean GET = false;
   private static final boolean SET = true;
   private static final boolean XXX = false;
   private URL url;
   private URLConnection urlConnection = null;
   private InputStream inputStream;
   private AudioFormat audioFormat;
   private boolean endOfStream = false;
   private boolean initialized = false;
   private byte[] buffer = null;
   private int bufferSize;
   private int count = 0;
   private int index = 0;
   private int convertedBufferSize;
   private byte[] convertedBuffer = null;
   private float[][][] pcmInfo;
   private int[] pcmIndex;
   private Packet joggPacket = new Packet();
   private Page joggPage = new Page();
   private StreamState joggStreamState = new StreamState();
   private SyncState joggSyncState = new SyncState();
   private DspState jorbisDspState = new DspState();
   private Block jorbisBlock;
   private Comment jorbisComment;
   private Info jorbisInfo;
   private SoundSystemLogger logger;

   public CodecJOrbis() {
      this.jorbisBlock = new Block(this.jorbisDspState);
      this.jorbisComment = new Comment();
      this.jorbisInfo = new Info();
      this.logger = SoundSystemConfig.getLogger();
   }

   public void reverseByteOrder(boolean var1) {
   }

   public boolean initialize(URL var1) {
      this.initialized(true, false);
      if (this.joggStreamState != null) {
         this.joggStreamState.clear();
      }

      if (this.jorbisBlock != null) {
         this.jorbisBlock.clear();
      }

      if (this.jorbisDspState != null) {
         this.jorbisDspState.clear();
      }

      if (this.jorbisInfo != null) {
         this.jorbisInfo.clear();
      }

      if (this.joggSyncState != null) {
         this.joggSyncState.clear();
      }

      if (this.inputStream != null) {
         try {
            this.inputStream.close();
         } catch (IOException var7) {
         }
      }

      this.url = var1;
      this.bufferSize = 8192;
      this.buffer = null;
      this.count = 0;
      this.index = 0;
      this.joggStreamState = new StreamState();
      this.jorbisBlock = new Block(this.jorbisDspState);
      this.jorbisDspState = new DspState();
      this.jorbisInfo = new Info();
      this.joggSyncState = new SyncState();

      try {
         this.urlConnection = var1.openConnection();
      } catch (UnknownServiceException var5) {
         this.errorMessage("Unable to create a UrlConnection in method 'initialize'.");
         this.printStackTrace(var5);
         this.cleanup();
         return false;
      } catch (IOException var6) {
         this.errorMessage("Unable to create a UrlConnection in method 'initialize'.");
         this.printStackTrace(var6);
         this.cleanup();
         return false;
      }

      if (this.urlConnection != null) {
         try {
            this.inputStream = this.urlConnection.getInputStream();
         } catch (IOException var4) {
            this.errorMessage("Unable to acquire inputstream in method 'initialize'.");
            this.printStackTrace(var4);
            this.cleanup();
            return false;
         }
      }

      this.endOfStream(true, false);
      this.joggSyncState.init();
      this.joggSyncState.buffer(this.bufferSize);
      this.buffer = this.joggSyncState.data;

      try {
         if (this < 0) {
            this.errorMessage("Error reading the header");
            return false;
         }
      } catch (IOException var8) {
         this.errorMessage("Error reading the header");
         return false;
      }

      this.convertedBufferSize = this.bufferSize * 2;
      this.jorbisDspState.synthesis_init(this.jorbisInfo);
      this.jorbisBlock.init(this.jorbisDspState);
      int var2 = this.jorbisInfo.channels;
      int var3 = this.jorbisInfo.rate;
      this.audioFormat = new AudioFormat((float)var3, 16, var2, true, false);
      this.pcmInfo = new float[1][][];
      this.pcmIndex = new int[this.jorbisInfo.channels];
      this.initialized(true, true);
      return true;
   }

   public boolean initialized() {
      return this.initialized(false, false);
   }

   public SoundBuffer read() {
      byte[] var1 = null;

      while(var1 == null || var1.length < SoundSystemConfig.getStreamingBufferSize()) {
         if (var1 == null) {
            var1 = this.readBytes();
         } else {
            var1 = appendByteArrays(var1, this.readBytes());
         }
      }

      if (var1 == null) {
         return null;
      } else {
         return new SoundBuffer(var1, this.audioFormat);
      }
   }

   public SoundBuffer readAll() {
      byte[] var1 = null;

      while(true) {
         while(var1 != null) {
            var1 = appendByteArrays(var1, this.readBytes());
         }

         var1 = this.readBytes();
      }
   }

   public boolean endOfStream() {
      return this.endOfStream(false, false);
   }

   public void cleanup() {
      this.joggStreamState.clear();
      this.jorbisBlock.clear();
      this.jorbisDspState.clear();
      this.jorbisInfo.clear();
      this.joggSyncState.clear();
      if (this.inputStream != null) {
         try {
            this.inputStream.close();
         } catch (IOException var2) {
         }
      }

      this.joggStreamState = null;
      this.jorbisBlock = null;
      this.jorbisDspState = null;
      this.jorbisInfo = null;
      this.joggSyncState = null;
      this.inputStream = null;
   }

   public AudioFormat getAudioFormat() {
      return this.audioFormat;
   }

   private byte[] readBytes() {
      return null;
   }

   private static byte[] trimArray(byte[] var0, int var1) {
      byte[] var2 = null;
      if (var0 != null && var0.length > var1) {
         var2 = new byte[var1];
         System.arraycopy(var0, 0, var2, 0, var1);
      }

      return var2;
   }

   private static byte[] appendByteArrays(byte[] var0, byte[] var1, int var2) {
      int var4 = var2;
      if (var1 != null && var1.length != 0) {
         if (var1.length < var2) {
            var4 = var1.length;
         }
      } else {
         var4 = 0;
      }

      if (var0 != null || var1 != null && var4 > 0) {
         byte[] var3;
         Object var6;
         if (var0 == null) {
            var3 = new byte[var4];
            System.arraycopy(var1, 0, var3, 0, var4);
            var6 = null;
         } else {
            Object var5;
            if (var1 != null && var4 > 0) {
               var3 = new byte[var0.length + var4];
               System.arraycopy(var0, 0, var3, 0, var0.length);
               System.arraycopy(var1, 0, var3, var0.length, var4);
               var5 = null;
               var6 = null;
            } else {
               var3 = new byte[var0.length];
               System.arraycopy(var0, 0, var3, 0, var0.length);
               var5 = null;
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   private static byte[] appendByteArrays(byte[] var0, byte[] var1) {
      if (var0 == null && var1 == null) {
         return null;
      } else {
         byte[] var2;
         Object var4;
         if (var0 == null) {
            var2 = new byte[var1.length];
            System.arraycopy(var1, 0, var2, 0, var1.length);
            var4 = null;
         } else {
            Object var3;
            if (var1 == null) {
               var2 = new byte[var0.length];
               System.arraycopy(var0, 0, var2, 0, var0.length);
               var3 = null;
            } else {
               var2 = new byte[var0.length + var1.length];
               System.arraycopy(var0, 0, var2, 0, var0.length);
               System.arraycopy(var1, 0, var2, var0.length, var1.length);
               var3 = null;
               var4 = null;
            }
         }

         return var2;
      }
   }

   private void errorMessage(String var1) {
      this.logger.errorMessage("CodecJOrbis", var1, 0);
   }

   private void printStackTrace(Exception var1) {
      this.logger.printStackTrace(var1, 1);
   }
}
