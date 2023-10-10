package paulscode.sound.libraries;

import java.util.LinkedList;
import javax.sound.sampled.AudioFormat;
import paulscode.sound.Channel;
import paulscode.sound.FilenameURL;
import paulscode.sound.ListenerData;
import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.Source;
import paulscode.sound.Vector3D;

public class SourceJavaSound extends Source {
   protected ChannelJavaSound channelJavaSound;
   public ListenerData listener;
   private float pan;

   public SourceJavaSound(ListenerData var1, boolean var2, boolean var3, boolean var4, String var5, FilenameURL var6, SoundBuffer var7, float var8, float var9, float var10, int var11, float var12, boolean var13) {
      super(var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
      this.channelJavaSound = (ChannelJavaSound)this.channel;
      this.pan = 0.0F;
      this.libraryType = LibraryJavaSound.class;
      this.listener = var1;
      this.positionChanged();
   }

   public SourceJavaSound(ListenerData var1, Source var2, SoundBuffer var3) {
      super(var2, var3);
      this.channelJavaSound = (ChannelJavaSound)this.channel;
      this.pan = 0.0F;
      this.libraryType = LibraryJavaSound.class;
      this.listener = var1;
      this.positionChanged();
   }

   public SourceJavaSound(ListenerData var1, AudioFormat var2, boolean var3, String var4, float var5, float var6, float var7, int var8, float var9) {
      super(var2, var3, var4, var5, var6, var7, var8, var9);
      this.channelJavaSound = (ChannelJavaSound)this.channel;
      this.pan = 0.0F;
      this.libraryType = LibraryJavaSound.class;
      this.listener = var1;
      this.positionChanged();
   }

   public void cleanup() {
      super.cleanup();
   }

   public void changeSource(boolean var1, boolean var2, boolean var3, String var4, FilenameURL var5, SoundBuffer var6, float var7, float var8, float var9, int var10, float var11, boolean var12) {
      super.changeSource(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
      if (this.channelJavaSound != null) {
         this.channelJavaSound.setLooping(var3);
      }

      this.positionChanged();
   }

   public void listenerMoved() {
      this.positionChanged();
   }

   public void setVelocity(float var1, float var2, float var3) {
      super.setVelocity(var1, var2, var3);
      this.positionChanged();
   }

   public void setPosition(float var1, float var2, float var3) {
      super.setPosition(var1, var2, var3);
      this.positionChanged();
   }

   public void positionChanged() {
      this.calculateGain();
      this.calculatePan();
      this.calculatePitch();
   }

   public void setPitch(float var1) {
      super.setPitch(var1);
      this.calculatePitch();
   }

   public void setAttenuation(int var1) {
      super.setAttenuation(var1);
      this.calculateGain();
   }

   public void setDistOrRoll(float var1) {
      super.setDistOrRoll(var1);
      this.calculateGain();
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
         boolean var4 = this.stopped();
         super.play(var1);
         this.channelJavaSound = (ChannelJavaSound)this.channel;
         if (var2) {
            if (this.channelJavaSound != null) {
               this.channelJavaSound.setLooping(this.toLoop);
            }

            if (!this.toStream) {
               if (this.soundBuffer == null) {
                  this.errorMessage("No sound buffer to play");
                  return;
               }

               this.channelJavaSound.attachBuffer(this.soundBuffer);
            }
         }

         this.positionChanged();
         if (var4 || !this.playing()) {
            if (this.toStream && !var3) {
               this.preLoad = true;
            }

            this.channel.play();
         }

      }
   }

   public boolean preLoad() {
      if (this.codec == null) {
         return false;
      } else {
         boolean var1 = false;
         Object var2;
         synchronized(var2 = this.soundSequenceLock){}
         if (this.nextBuffers == null || this.nextBuffers.isEmpty()) {
            var1 = true;
         }

         LinkedList var5 = new LinkedList();
         if (this.nextCodec != null && !var1) {
            this.codec = this.nextCodec;
            this.nextCodec = null;
            synchronized(this.soundSequenceLock){}

            while(!this.nextBuffers.isEmpty()) {
               this.soundBuffer = (SoundBuffer)this.nextBuffers.remove(0);
               if (this.soundBuffer != null && this.soundBuffer.audioData != null) {
                  var5.add(this.soundBuffer.audioData);
               }
            }
         } else {
            this.codec.initialize(this.filenameURL.getURL());

            for(int var3 = 0; var3 < SoundSystemConfig.getNumberStreamingBuffers(); ++var3) {
               this.soundBuffer = this.codec.read();
               if (this.soundBuffer == null || this.soundBuffer.audioData == null) {
                  break;
               }

               var5.add(this.soundBuffer.audioData);
            }

            this.channelJavaSound.resetStream(this.codec.getAudioFormat());
         }

         this.positionChanged();
         this.channel.preLoadBuffers(var5);
         this.preLoad = false;
         return true;
      }
   }

   public void calculateGain() {
      float var1 = this.position.x - this.listener.position.x;
      float var2 = this.position.y - this.listener.position.y;
      float var3 = this.position.z - this.listener.position.z;
      this.distanceFromListener = (float)Math.sqrt((double)(var1 * var1 + var2 * var2 + var3 * var3));
      switch(this.attModel) {
      case 1:
         if (this.distanceFromListener <= 0.0F) {
            this.gain = 1.0F;
         } else {
            float var4 = 5.0E-4F;
            float var5 = this.distOrRoll * this.distanceFromListener * this.distanceFromListener * var4;
            if (var5 < 0.0F) {
               var5 = 0.0F;
            }

            this.gain = 1.0F / (1.0F + var5);
         }
         break;
      case 2:
         if (this.distanceFromListener <= 0.0F) {
            this.gain = 1.0F;
         } else if (this.distanceFromListener >= this.distOrRoll) {
            this.gain = 0.0F;
         } else {
            this.gain = 1.0F - this.distanceFromListener / this.distOrRoll;
         }
         break;
      default:
         this.gain = 1.0F;
      }

      if (this.gain > 1.0F) {
         this.gain = 1.0F;
      }

      if (this.gain < 0.0F) {
         this.gain = 0.0F;
      }

      this.gain *= this.sourceVolume * SoundSystemConfig.getMasterGain() * Math.abs(this.fadeOutGain) * this.fadeInGain;
      if (this.channel != null && this.channel.attachedSource == this && this.channelJavaSound != null) {
         this.channelJavaSound.setGain(this.gain);
      }

   }

   public void calculatePan() {
      Vector3D var1 = this.listener.up.cross(this.listener.lookAt);
      var1.normalize();
      float var2 = this.position.dot(this.position.subtract(this.listener.position), var1);
      float var3 = this.position.dot(this.position.subtract(this.listener.position), this.listener.lookAt);
      var1 = null;
      float var4 = (float)Math.atan2((double)var2, (double)var3);
      this.pan = (float)(-Math.sin((double)var4));
      if (this.channel != null && this.channel.attachedSource == this && this.channelJavaSound != null) {
         if (this.attModel == 0) {
            this.channelJavaSound.setPan(0.0F);
         } else {
            this.channelJavaSound.setPan(this.pan);
         }
      }

   }

   public void calculatePitch() {
      if (this.channel != null && this.channel.attachedSource == this && this.channelJavaSound != null) {
         if (SoundSystemConfig.getDopplerFactor() == 0.0F) {
            this.channelJavaSound.setPitch(this.pitch);
         } else {
            float var1 = 343.3F;
            Vector3D var2 = this.velocity;
            Vector3D var3 = this.listener.velocity;
            float var4 = SoundSystemConfig.getDopplerVelocity();
            float var5 = SoundSystemConfig.getDopplerFactor();
            Vector3D var6 = this.listener.position.subtract(this.position);
            float var7 = var6.dot(var3) / var6.length();
            float var8 = var6.dot(var2) / var6.length();
            var8 = this.min(var8, var1 / var5);
            var7 = this.min(var7, var1 / var5);
            float var9 = this.pitch * (var1 * var4 - var5 * var7) / (var1 * var4 - var5 * var8);
            if (var9 < 0.5F) {
               var9 = 0.5F;
            } else if (var9 > 2.0F) {
               var9 = 2.0F;
            }

            this.channelJavaSound.setPitch(var9);
         }
      }

   }

   public float min(float var1, float var2) {
      return var1 < var2 ? var1 : var2;
   }
}
