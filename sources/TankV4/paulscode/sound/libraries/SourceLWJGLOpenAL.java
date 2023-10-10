package paulscode.sound.libraries;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import javax.sound.sampled.AudioFormat;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import paulscode.sound.Channel;
import paulscode.sound.FilenameURL;
import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.Source;

public class SourceLWJGLOpenAL extends Source {
   private ChannelLWJGLOpenAL channelOpenAL;
   private IntBuffer myBuffer;
   private FloatBuffer listenerPosition;
   private FloatBuffer sourcePosition;
   private FloatBuffer sourceVelocity;

   public SourceLWJGLOpenAL(FloatBuffer var1, IntBuffer var2, boolean var3, boolean var4, boolean var5, String var6, FilenameURL var7, SoundBuffer var8, float var9, float var10, float var11, int var12, float var13, boolean var14) {
      super(var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14);
      this.channelOpenAL = (ChannelLWJGLOpenAL)this.channel;
      if (this.codec != null) {
         this.codec.reverseByteOrder(true);
      }

      this.listenerPosition = var1;
      this.myBuffer = var2;
      this.libraryType = LibraryLWJGLOpenAL.class;
      this.pitch = 1.0F;
      this.resetALInformation();
   }

   public SourceLWJGLOpenAL(FloatBuffer var1, IntBuffer var2, Source var3, SoundBuffer var4) {
      super(var3, var4);
      this.channelOpenAL = (ChannelLWJGLOpenAL)this.channel;
      if (this.codec != null) {
         this.codec.reverseByteOrder(true);
      }

      this.listenerPosition = var1;
      this.myBuffer = var2;
      this.libraryType = LibraryLWJGLOpenAL.class;
      this.pitch = 1.0F;
      this.resetALInformation();
   }

   public SourceLWJGLOpenAL(FloatBuffer var1, AudioFormat var2, boolean var3, String var4, float var5, float var6, float var7, int var8, float var9) {
      super(var2, var3, var4, var5, var6, var7, var8, var9);
      this.channelOpenAL = (ChannelLWJGLOpenAL)this.channel;
      this.listenerPosition = var1;
      this.libraryType = LibraryLWJGLOpenAL.class;
      this.pitch = 1.0F;
      this.resetALInformation();
   }

   public void cleanup() {
      super.cleanup();
   }

   public void changeSource(FloatBuffer var1, IntBuffer var2, boolean var3, boolean var4, boolean var5, String var6, FilenameURL var7, SoundBuffer var8, float var9, float var10, float var11, int var12, float var13, boolean var14) {
      super.changeSource(var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14);
      this.listenerPosition = var1;
      this.myBuffer = var2;
      this.pitch = 1.0F;
      this.resetALInformation();
   }

   public boolean incrementSoundSequence() {
      if (!this.toStream) {
         this.errorMessage("Method 'incrementSoundSequence' may only be used for streaming sources.");
         return false;
      } else {
         Object var1;
         synchronized(var1 = this.soundSequenceLock){}
         if (this.soundSequenceQueue != null && this.soundSequenceQueue.size() > 0) {
            this.filenameURL = (FilenameURL)this.soundSequenceQueue.remove(0);
            if (this.codec != null) {
               this.codec.cleanup();
            }

            this.codec = SoundSystemConfig.getCodec(this.filenameURL.getFilename());
            if (this.codec != null) {
               this.codec.reverseByteOrder(true);
               if (this.codec.getAudioFormat() == null) {
                  this.codec.initialize(this.filenameURL.getURL());
               }

               AudioFormat var2 = this.codec.getAudioFormat();
               if (var2 == null) {
                  this.errorMessage("Audio Format null in method 'incrementSoundSequence'");
                  return false;
               }

               boolean var3 = false;
               short var5;
               if (var2.getChannels() == 1) {
                  if (var2.getSampleSizeInBits() == 8) {
                     var5 = 4352;
                  } else {
                     if (var2.getSampleSizeInBits() != 16) {
                        this.errorMessage("Illegal sample size in method 'incrementSoundSequence'");
                        return false;
                     }

                     var5 = 4353;
                  }
               } else {
                  if (var2.getChannels() != 2) {
                     this.errorMessage("Audio data neither mono nor stereo in method 'incrementSoundSequence'");
                     return false;
                  }

                  if (var2.getSampleSizeInBits() == 8) {
                     var5 = 4354;
                  } else {
                     if (var2.getSampleSizeInBits() != 16) {
                        this.errorMessage("Illegal sample size in method 'incrementSoundSequence'");
                        return false;
                     }

                     var5 = 4355;
                  }
               }

               this.channelOpenAL.setFormat(var5, (int)var2.getSampleRate());
               this.preLoad = true;
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public void listenerMoved() {
      this.positionChanged();
   }

   public void setPosition(float var1, float var2, float var3) {
      super.setPosition(var1, var2, var3);
      if (this.sourcePosition == null) {
         this.resetALInformation();
      } else {
         this.positionChanged();
      }

      this.sourcePosition.put(0, var1);
      this.sourcePosition.put(1, var2);
      this.sourcePosition.put(2, var3);
      if (this.channel != null && this.channel.attachedSource == this && this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
         AL10.alSource(this.channelOpenAL.ALSource.get(0), 4100, this.sourcePosition);
         this.checkALError();
      }

   }

   public void positionChanged() {
      this.calculateDistance();
      this.calculateGain();
      if (this.channel != null && this.channel.attachedSource == this && this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
         AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4106, this.gain * this.sourceVolume * Math.abs(this.fadeOutGain) * this.fadeInGain);
         this.checkALError();
      }

      this.checkPitch();
   }

   private void checkPitch() {
      if (this.channel != null && this.channel.attachedSource == this && LibraryLWJGLOpenAL.alPitchSupported() && this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
         AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4099, this.pitch);
         this.checkALError();
      }

   }

   public void setLooping(boolean var1) {
      super.setLooping(var1);
      if (this.channel != null && this.channel.attachedSource == this && this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
         if (var1) {
            AL10.alSourcei(this.channelOpenAL.ALSource.get(0), 4103, 1);
         } else {
            AL10.alSourcei(this.channelOpenAL.ALSource.get(0), 4103, 0);
         }

         this.checkALError();
      }

   }

   public void setAttenuation(int var1) {
      super.setAttenuation(var1);
      if (this.channel != null && this.channel.attachedSource == this && this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
         if (var1 == 1) {
            AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, this.distOrRoll);
         } else {
            AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, 0.0F);
         }

         this.checkALError();
      }

   }

   public void setDistOrRoll(float var1) {
      super.setDistOrRoll(var1);
      if (this.channel != null && this.channel.attachedSource == this && this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
         if (this.attModel == 1) {
            AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, var1);
         } else {
            AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, 0.0F);
         }

         this.checkALError();
      }

   }

   public void setVelocity(float var1, float var2, float var3) {
      super.setVelocity(var1, var2, var3);
      this.sourceVelocity = BufferUtils.createFloatBuffer(3).put(new float[]{var1, var2, var3});
      this.sourceVelocity.flip();
      if (this.channel != null && this.channel.attachedSource == this && this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
         AL10.alSource(this.channelOpenAL.ALSource.get(0), 4102, this.sourceVelocity);
         this.checkALError();
      }

   }

   public void setPitch(float var1) {
      super.setPitch(var1);
      this.checkPitch();
   }

   public void play(Channel var1) {
      if (!this.active()) {
         if (this.toLoop) {
            this.toPlay = true;
         }

      } else if (var1 == null) {
         this.errorMessage("Unable to play source, because channel was null");
      } else {
         boolean var2 = this.channel != var1;
         if (this.channel != null && this.channel.attachedSource != this) {
            var2 = true;
         }

         boolean var3 = this.paused();
         super.play(var1);
         this.channelOpenAL = (ChannelLWJGLOpenAL)this.channel;
         if (var2) {
            this.setPosition(this.position.x, this.position.y, this.position.z);
            this.checkPitch();
            if (this.channelOpenAL != null && this.channelOpenAL.ALSource != null) {
               if (LibraryLWJGLOpenAL.alPitchSupported()) {
                  AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4099, this.pitch);
                  this.checkALError();
               }

               AL10.alSource(this.channelOpenAL.ALSource.get(0), 4100, this.sourcePosition);
               this.checkALError();
               AL10.alSource(this.channelOpenAL.ALSource.get(0), 4102, this.sourceVelocity);
               this.checkALError();
               if (this.attModel == 1) {
                  AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, this.distOrRoll);
               } else {
                  AL10.alSourcef(this.channelOpenAL.ALSource.get(0), 4129, 0.0F);
               }

               this.checkALError();
               if (this.toLoop && !this.toStream) {
                  AL10.alSourcei(this.channelOpenAL.ALSource.get(0), 4103, 1);
               } else {
                  AL10.alSourcei(this.channelOpenAL.ALSource.get(0), 4103, 0);
               }

               this.checkALError();
            }

            if (!this.toStream) {
               if (this.myBuffer == null) {
                  this.errorMessage("No sound buffer to play");
                  return;
               }

               this.channelOpenAL.attachBuffer(this.myBuffer);
            }
         }

         if (!this.playing()) {
            if (this.toStream && !var3) {
               if (this.codec == null) {
                  this.errorMessage("Decoder null in method 'play'");
                  return;
               }

               if (this.codec.getAudioFormat() == null) {
                  this.codec.initialize(this.filenameURL.getURL());
               }

               AudioFormat var4 = this.codec.getAudioFormat();
               if (var4 == null) {
                  this.errorMessage("Audio Format null in method 'play'");
                  return;
               }

               boolean var5 = false;
               short var6;
               if (var4.getChannels() == 1) {
                  if (var4.getSampleSizeInBits() == 8) {
                     var6 = 4352;
                  } else {
                     if (var4.getSampleSizeInBits() != 16) {
                        this.errorMessage("Illegal sample size in method 'play'");
                        return;
                     }

                     var6 = 4353;
                  }
               } else {
                  if (var4.getChannels() != 2) {
                     this.errorMessage("Audio data neither mono nor stereo in method 'play'");
                     return;
                  }

                  if (var4.getSampleSizeInBits() == 8) {
                     var6 = 4354;
                  } else {
                     if (var4.getSampleSizeInBits() != 16) {
                        this.errorMessage("Illegal sample size in method 'play'");
                        return;
                     }

                     var6 = 4355;
                  }
               }

               this.channelOpenAL.setFormat(var6, (int)var4.getSampleRate());
               this.preLoad = true;
            }

            this.channel.play();
            if (this.pitch != 1.0F) {
               this.checkPitch();
            }
         }

      }
   }

   public boolean preLoad() {
      if (this.codec == null) {
         return false;
      } else {
         this.codec.initialize(this.filenameURL.getURL());
         LinkedList var1 = new LinkedList();

         for(int var2 = 0; var2 < SoundSystemConfig.getNumberStreamingBuffers(); ++var2) {
            this.soundBuffer = this.codec.read();
            if (this.soundBuffer == null || this.soundBuffer.audioData == null) {
               break;
            }

            var1.add(this.soundBuffer.audioData);
         }

         this.positionChanged();
         this.channel.preLoadBuffers(var1);
         this.preLoad = false;
         return true;
      }
   }

   private void resetALInformation() {
      this.sourcePosition = BufferUtils.createFloatBuffer(3).put(new float[]{this.position.x, this.position.y, this.position.z});
      this.sourceVelocity = BufferUtils.createFloatBuffer(3).put(new float[]{this.velocity.x, this.velocity.y, this.velocity.z});
      this.sourcePosition.flip();
      this.sourceVelocity.flip();
      this.positionChanged();
   }

   private void calculateDistance() {
      if (this.listenerPosition != null) {
         double var1 = (double)(this.position.x - this.listenerPosition.get(0));
         double var3 = (double)(this.position.y - this.listenerPosition.get(1));
         double var5 = (double)(this.position.z - this.listenerPosition.get(2));
         this.distanceFromListener = (float)Math.sqrt(var1 * var1 + var3 * var3 + var5 * var5);
      }

   }

   private void calculateGain() {
      if (this.attModel == 2) {
         if (this.distanceFromListener <= 0.0F) {
            this.gain = 1.0F;
         } else if (this.distanceFromListener >= this.distOrRoll) {
            this.gain = 0.0F;
         } else {
            this.gain = 1.0F - this.distanceFromListener / this.distOrRoll;
         }

         if (this.gain > 1.0F) {
            this.gain = 1.0F;
         }

         if (this.gain < 0.0F) {
            this.gain = 0.0F;
         }
      } else {
         this.gain = 1.0F;
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
