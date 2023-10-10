package paulscode.sound.libraries;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import javax.sound.sampled.AudioFormat;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import paulscode.sound.Channel;

public class ChannelLWJGLOpenAL extends Channel {
   public IntBuffer ALSource;
   public int ALformat;
   public int sampleRate;
   public float millisPreviouslyPlayed = 0.0F;

   public ChannelLWJGLOpenAL(int var1, IntBuffer var2) {
      super(var1);
      this.libraryType = LibraryLWJGLOpenAL.class;
      this.ALSource = var2;
   }

   public void cleanup() {
      if (this.ALSource != null) {
         try {
            AL10.alSourceStop(this.ALSource);
            AL10.alGetError();
         } catch (Exception var3) {
         }

         try {
            AL10.alDeleteSources(this.ALSource);
            AL10.alGetError();
         } catch (Exception var2) {
         }

         this.ALSource.clear();
      }

      this.ALSource = null;
      super.cleanup();
   }

   public boolean attachBuffer(IntBuffer var1) {
      if (this.errorCheck(this.channelType != 0, "Sound buffers may only be attached to normal sources.")) {
         return false;
      } else {
         AL10.alSourcei(this.ALSource.get(0), 4105, var1.get(0));
         if (this.attachedSource != null && this.attachedSource.soundBuffer != null && this.attachedSource.soundBuffer.audioFormat != null) {
            this.setAudioFormat(this.attachedSource.soundBuffer.audioFormat);
         }

         return this.checkALError();
      }
   }

   public void setAudioFormat(AudioFormat var1) {
      boolean var2 = false;
      short var3;
      if (var1.getChannels() == 1) {
         if (var1.getSampleSizeInBits() == 8) {
            var3 = 4352;
         } else {
            if (var1.getSampleSizeInBits() != 16) {
               this.errorMessage("Illegal sample size in method 'setAudioFormat'");
               return;
            }

            var3 = 4353;
         }
      } else {
         if (var1.getChannels() != 2) {
            this.errorMessage("Audio data neither mono nor stereo in method 'setAudioFormat'");
            return;
         }

         if (var1.getSampleSizeInBits() == 8) {
            var3 = 4354;
         } else {
            if (var1.getSampleSizeInBits() != 16) {
               this.errorMessage("Illegal sample size in method 'setAudioFormat'");
               return;
            }

            var3 = 4355;
         }
      }

      this.ALformat = var3;
      this.sampleRate = (int)var1.getSampleRate();
   }

   public void setFormat(int var1, int var2) {
      this.ALformat = var1;
      this.sampleRate = var2;
   }

   public boolean preLoadBuffers(LinkedList var1) {
      if (this.errorCheck(this.channelType != 1, "Buffers may only be queued for streaming sources.")) {
         return false;
      } else if (this.errorCheck(var1 == null, "Buffer List null in method 'preLoadBuffers'")) {
         return false;
      } else {
         boolean var3 = this.playing();
         if (var3) {
            AL10.alSourceStop(this.ALSource.get(0));
            this.checkALError();
         }

         int var4 = AL10.alGetSourcei(this.ALSource.get(0), 4118);
         IntBuffer var2;
         if (var4 > 0) {
            var2 = BufferUtils.createIntBuffer(var4);
            AL10.alGenBuffers(var2);
            if (this.errorCheck(this.checkALError(), "Error clearing stream buffers in method 'preLoadBuffers'")) {
               return false;
            }

            AL10.alSourceUnqueueBuffers(this.ALSource.get(0), var2);
            if (this.errorCheck(this.checkALError(), "Error unqueuing stream buffers in method 'preLoadBuffers'")) {
               return false;
            }
         }

         if (var3) {
            AL10.alSourcePlay(this.ALSource.get(0));
            this.checkALError();
         }

         var2 = BufferUtils.createIntBuffer(var1.size());
         AL10.alGenBuffers(var2);
         if (this.errorCheck(this.checkALError(), "Error generating stream buffers in method 'preLoadBuffers'")) {
            return false;
         } else {
            ByteBuffer var5 = null;

            for(int var6 = 0; var6 < var1.size(); ++var6) {
               var5 = (ByteBuffer)BufferUtils.createByteBuffer(((byte[])var1.get(var6)).length).put((byte[])var1.get(var6)).flip();

               try {
                  AL10.alBufferData(var2.get(var6), this.ALformat, var5, this.sampleRate);
               } catch (Exception var9) {
                  this.errorMessage("Error creating buffers in method 'preLoadBuffers'");
                  this.printStackTrace(var9);
                  return false;
               }

               if (this.errorCheck(this.checkALError(), "Error creating buffers in method 'preLoadBuffers'")) {
                  return false;
               }
            }

            try {
               AL10.alSourceQueueBuffers(this.ALSource.get(0), var2);
            } catch (Exception var8) {
               this.errorMessage("Error queuing buffers in method 'preLoadBuffers'");
               this.printStackTrace(var8);
               return false;
            }

            if (this.errorCheck(this.checkALError(), "Error queuing buffers in method 'preLoadBuffers'")) {
               return false;
            } else {
               AL10.alSourcePlay(this.ALSource.get(0));
               return !this.errorCheck(this.checkALError(), "Error playing source in method 'preLoadBuffers'");
            }
         }
      }
   }

   public boolean queueBuffer(byte[] var1) {
      if (this.errorCheck(this.channelType != 1, "Buffers may only be queued for streaming sources.")) {
         return false;
      } else {
         ByteBuffer var2 = (ByteBuffer)BufferUtils.createByteBuffer(var1.length).put(var1).flip();
         IntBuffer var3 = BufferUtils.createIntBuffer(1);
         AL10.alSourceUnqueueBuffers(this.ALSource.get(0), var3);
         if (this.checkALError()) {
            return false;
         } else {
            if (AL10.alIsBuffer(var3.get(0))) {
               this.millisPreviouslyPlayed += this.millisInBuffer(var3.get(0));
            }

            this.checkALError();
            AL10.alBufferData(var3.get(0), this.ALformat, var2, this.sampleRate);
            if (this.checkALError()) {
               return false;
            } else {
               AL10.alSourceQueueBuffers(this.ALSource.get(0), var3);
               return !this.checkALError();
            }
         }
      }
   }

   public int feedRawAudioData(byte[] var1) {
      if (this.errorCheck(this.channelType != 1, "Raw audio data can only be fed to streaming sources.")) {
         return -1;
      } else {
         ByteBuffer var2 = (ByteBuffer)BufferUtils.createByteBuffer(var1.length).put(var1).flip();
         int var4 = AL10.alGetSourcei(this.ALSource.get(0), 4118);
         IntBuffer var3;
         if (var4 > 0) {
            var3 = BufferUtils.createIntBuffer(var4);
            AL10.alGenBuffers(var3);
            if (this.errorCheck(this.checkALError(), "Error clearing stream buffers in method 'feedRawAudioData'")) {
               return -1;
            }

            AL10.alSourceUnqueueBuffers(this.ALSource.get(0), var3);
            if (this.errorCheck(this.checkALError(), "Error unqueuing stream buffers in method 'feedRawAudioData'")) {
               return -1;
            }

            if (AL10.alIsBuffer(var3.get(0))) {
               this.millisPreviouslyPlayed += this.millisInBuffer(var3.get(0));
            }

            this.checkALError();
         } else {
            var3 = BufferUtils.createIntBuffer(1);
            AL10.alGenBuffers(var3);
            if (this.errorCheck(this.checkALError(), "Error generating stream buffers in method 'preLoadBuffers'")) {
               return -1;
            }
         }

         AL10.alBufferData(var3.get(0), this.ALformat, var2, this.sampleRate);
         if (this.checkALError()) {
            return -1;
         } else {
            AL10.alSourceQueueBuffers(this.ALSource.get(0), var3);
            if (this.checkALError()) {
               return -1;
            } else {
               if (this.attachedSource != null && this.attachedSource.channel == this && this.attachedSource.active() && this != false) {
                  AL10.alSourcePlay(this.ALSource.get(0));
                  this.checkALError();
               }

               return var4;
            }
         }
      }
   }

   public float millisInBuffer(int var1) {
      return (float)AL10.alGetBufferi(var1, 8196) / (float)AL10.alGetBufferi(var1, 8195) / ((float)AL10.alGetBufferi(var1, 8194) / 8.0F) / (float)this.sampleRate * 1000.0F;
   }

   public float millisecondsPlayed() {
      float var1 = (float)AL10.alGetSourcei(this.ALSource.get(0), 4134);
      float var2 = 1.0F;
      switch(this.ALformat) {
      case 4352:
         var2 = 1.0F;
         break;
      case 4353:
         var2 = 2.0F;
         break;
      case 4354:
         var2 = 2.0F;
         break;
      case 4355:
         var2 = 4.0F;
      }

      var1 = var1 / var2 / (float)this.sampleRate * 1000.0F;
      if (this.channelType == 1) {
         var1 += this.millisPreviouslyPlayed;
      }

      return var1;
   }

   public int buffersProcessed() {
      if (this.channelType != 1) {
         return 0;
      } else {
         int var1 = AL10.alGetSourcei(this.ALSource.get(0), 4118);
         return this.checkALError() ? 0 : var1;
      }
   }

   public void flush() {
      if (this.channelType == 1) {
         int var1 = AL10.alGetSourcei(this.ALSource.get(0), 4117);
         if (!this.checkALError()) {
            for(IntBuffer var2 = BufferUtils.createIntBuffer(1); var1 > 0; --var1) {
               try {
                  AL10.alSourceUnqueueBuffers(this.ALSource.get(0), var2);
               } catch (Exception var4) {
                  return;
               }

               if (this.checkALError()) {
                  return;
               }
            }

            this.millisPreviouslyPlayed = 0.0F;
         }
      }
   }

   public void close() {
      try {
         AL10.alSourceStop(this.ALSource.get(0));
         AL10.alGetError();
      } catch (Exception var2) {
      }

      if (this.channelType == 1) {
         this.flush();
      }

   }

   public void play() {
      AL10.alSourcePlay(this.ALSource.get(0));
      this.checkALError();
   }

   public void pause() {
      AL10.alSourcePause(this.ALSource.get(0));
      this.checkALError();
   }

   public void stop() {
      AL10.alSourceStop(this.ALSource.get(0));
      if (!this.checkALError()) {
         this.millisPreviouslyPlayed = 0.0F;
      }

   }

   public void rewind() {
      if (this.channelType != 1) {
         AL10.alSourceRewind(this.ALSource.get(0));
         if (!this.checkALError()) {
            this.millisPreviouslyPlayed = 0.0F;
         }

      }
   }

   private boolean checkALError() {
      switch(AL10.alGetError()) {
      case 0:
         return false;
      case 40961:
         this.errorMessage("Invalid name parameter.");
         return true;
      case 40962:
         this.errorMessage("Invalid parameter.");
         return true;
      case 40963:
         this.errorMessage("Invalid enumerated parameter value.");
         return true;
      case 40964:
         this.errorMessage("Illegal call.");
         return true;
      case 40965:
         this.errorMessage("Unable to allocate memory.");
         return true;
      default:
         this.errorMessage("An unrecognized error occurred.");
         return true;
      }
   }
}
