package paulscode.sound.libraries;

import java.util.LinkedList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.FloatControl.Type;
import paulscode.sound.Channel;
import paulscode.sound.SoundBuffer;

public class ChannelJavaSound extends Channel {
   public Clip clip = null;
   SoundBuffer soundBuffer;
   public SourceDataLine sourceDataLine = null;
   private List streamBuffers;
   private int processed = 0;
   private Mixer myMixer = null;
   private AudioFormat myFormat = null;
   private FloatControl gainControl = null;
   private FloatControl panControl = null;
   private FloatControl sampleRateControl = null;
   private float initialGain = 0.0F;
   private float initialSampleRate = 0.0F;
   private boolean toLoop = false;

   public ChannelJavaSound(int var1, Mixer var2) {
      super(var1);
      this.libraryType = LibraryJavaSound.class;
      this.myMixer = var2;
      this.clip = null;
      this.sourceDataLine = null;
      this.streamBuffers = new LinkedList();
   }

   public void cleanup() {
      if (this.streamBuffers != null) {
         for(SoundBuffer var1 = null; !this.streamBuffers.isEmpty(); var1 = null) {
            var1 = (SoundBuffer)this.streamBuffers.remove(0);
            var1.cleanup();
         }

         this.streamBuffers.clear();
      }

      this.clip = null;
      this.soundBuffer = null;
      this.sourceDataLine = null;
      this.streamBuffers.clear();
      this.myMixer = null;
      this.myFormat = null;
      this.streamBuffers = null;
      super.cleanup();
   }

   public void newMixer(Mixer var1) {
      if (this.myMixer != var1) {
         try {
            if (this.clip != null) {
               this.clip.close();
            } else if (this.sourceDataLine != null) {
               this.sourceDataLine.close();
            }
         } catch (SecurityException var3) {
         }

         this.myMixer = var1;
         if (this.attachedSource != null) {
            if (this.channelType == 0 && this.soundBuffer != null) {
               this.attachBuffer(this.soundBuffer);
            } else if (this.myFormat != null) {
               this.resetStream(this.myFormat);
            }
         }
      }

   }

   public boolean attachBuffer(SoundBuffer var1) {
      if (this.errorCheck(this.channelType != 0, "Buffers may only be attached to non-streaming sources")) {
         return false;
      } else if (this.errorCheck(this.myMixer == null, "Mixer null in method 'attachBuffer'")) {
         return false;
      } else if (this.errorCheck(var1 == null, "Buffer null in method 'attachBuffer'")) {
         return false;
      } else if (this.errorCheck(var1.audioData == null, "Buffer missing audio data in method 'attachBuffer'")) {
         return false;
      } else if (this.errorCheck(var1.audioFormat == null, "Buffer missing format information in method 'attachBuffer'")) {
         return false;
      } else {
         Info var2 = new Info(Clip.class, var1.audioFormat);
         if (this.errorCheck(!AudioSystem.isLineSupported(var2), "Line not supported in method 'attachBuffer'")) {
            return false;
         } else {
            Clip var3 = null;

            try {
               var3 = (Clip)this.myMixer.getLine(var2);
            } catch (Exception var6) {
               this.errorMessage("Unable to create clip in method 'attachBuffer'");
               this.printStackTrace(var6);
               return false;
            }

            if (this.errorCheck(var3 == null, "New clip null in method 'attachBuffer'")) {
               return false;
            } else {
               if (this.clip != null) {
                  this.clip.stop();
                  this.clip.flush();
                  this.clip.close();
               }

               this.clip = var3;
               this.soundBuffer = var1;
               this.myFormat = var1.audioFormat;
               var3 = null;

               try {
                  this.clip.open(this.myFormat, var1.audioData, 0, var1.audioData.length);
               } catch (Exception var5) {
                  this.errorMessage("Unable to attach buffer to clip in method 'attachBuffer'");
                  this.printStackTrace(var5);
                  return false;
               }

               this.resetControls();
               return true;
            }
         }
      }
   }

   public void setAudioFormat(AudioFormat var1) {
      this.resetStream(var1);
      if (this.attachedSource != null && this.attachedSource.rawDataStream && this.attachedSource.active() && this.sourceDataLine != null) {
         this.sourceDataLine.start();
      }

   }

   public boolean resetStream(AudioFormat var1) {
      if (this.errorCheck(this.myMixer == null, "Mixer null in method 'resetStream'")) {
         return false;
      } else if (this.errorCheck(var1 == null, "AudioFormat null in method 'resetStream'")) {
         return false;
      } else {
         Info var2 = new Info(SourceDataLine.class, var1);
         if (this.errorCheck(!AudioSystem.isLineSupported(var2), "Line not supported in method 'resetStream'")) {
            return false;
         } else {
            SourceDataLine var3 = null;

            try {
               var3 = (SourceDataLine)this.myMixer.getLine(var2);
            } catch (Exception var6) {
               this.errorMessage("Unable to create a SourceDataLine in method 'resetStream'");
               this.printStackTrace(var6);
               return false;
            }

            if (this.errorCheck(var3 == null, "New SourceDataLine null in method 'resetStream'")) {
               return false;
            } else {
               this.streamBuffers.clear();
               this.processed = 0;
               if (this.sourceDataLine != null) {
                  this.sourceDataLine.stop();
                  this.sourceDataLine.flush();
                  this.sourceDataLine.close();
               }

               this.sourceDataLine = var3;
               this.myFormat = var1;
               var3 = null;

               try {
                  this.sourceDataLine.open(this.myFormat);
               } catch (Exception var5) {
                  this.errorMessage("Unable to open the new SourceDataLine in method 'resetStream'");
                  this.printStackTrace(var5);
                  return false;
               }

               this.resetControls();
               return true;
            }
         }
      }
   }

   private void resetControls() {
      switch(this.channelType) {
      case 0:
         try {
            if (!this.clip.isControlSupported(Type.PAN)) {
               this.panControl = null;
            } else {
               this.panControl = (FloatControl)this.clip.getControl(Type.PAN);
            }
         } catch (IllegalArgumentException var7) {
            this.panControl = null;
         }

         try {
            if (!this.clip.isControlSupported(Type.MASTER_GAIN)) {
               this.gainControl = null;
               this.initialGain = 0.0F;
            } else {
               this.gainControl = (FloatControl)this.clip.getControl(Type.MASTER_GAIN);
               this.initialGain = this.gainControl.getValue();
            }
         } catch (IllegalArgumentException var6) {
            this.gainControl = null;
            this.initialGain = 0.0F;
         }

         try {
            if (!this.clip.isControlSupported(Type.SAMPLE_RATE)) {
               this.sampleRateControl = null;
               this.initialSampleRate = 0.0F;
            } else {
               this.sampleRateControl = (FloatControl)this.clip.getControl(Type.SAMPLE_RATE);
               this.initialSampleRate = this.sampleRateControl.getValue();
            }
         } catch (IllegalArgumentException var5) {
            this.sampleRateControl = null;
            this.initialSampleRate = 0.0F;
         }
         break;
      case 1:
         try {
            if (!this.sourceDataLine.isControlSupported(Type.PAN)) {
               this.panControl = null;
            } else {
               this.panControl = (FloatControl)this.sourceDataLine.getControl(Type.PAN);
            }
         } catch (IllegalArgumentException var4) {
            this.panControl = null;
         }

         try {
            if (!this.sourceDataLine.isControlSupported(Type.MASTER_GAIN)) {
               this.gainControl = null;
               this.initialGain = 0.0F;
            } else {
               this.gainControl = (FloatControl)this.sourceDataLine.getControl(Type.MASTER_GAIN);
               this.initialGain = this.gainControl.getValue();
            }
         } catch (IllegalArgumentException var3) {
            this.gainControl = null;
            this.initialGain = 0.0F;
         }

         try {
            if (!this.sourceDataLine.isControlSupported(Type.SAMPLE_RATE)) {
               this.sampleRateControl = null;
               this.initialSampleRate = 0.0F;
            } else {
               this.sampleRateControl = (FloatControl)this.sourceDataLine.getControl(Type.SAMPLE_RATE);
               this.initialSampleRate = this.sampleRateControl.getValue();
            }
         } catch (IllegalArgumentException var2) {
            this.sampleRateControl = null;
            this.initialSampleRate = 0.0F;
         }
         break;
      default:
         this.errorMessage("Unrecognized channel type in method 'resetControls'");
         this.panControl = null;
         this.gainControl = null;
         this.sampleRateControl = null;
      }

   }

   public void setLooping(boolean var1) {
      this.toLoop = var1;
   }

   public void setPan(float var1) {
      if (this.panControl != null) {
         float var2 = var1;
         if (var1 < -1.0F) {
            var2 = -1.0F;
         }

         if (var2 > 1.0F) {
            var2 = 1.0F;
         }

         this.panControl.setValue(var2);
      }
   }

   public void setGain(float var1) {
      if (this.gainControl != null) {
         float var2 = var1;
         if (var1 < 0.0F) {
            var2 = 0.0F;
         }

         if (var2 > 1.0F) {
            var2 = 1.0F;
         }

         double var3 = (double)this.gainControl.getMinimum();
         double var5 = (double)this.initialGain;
         double var7 = 0.5D * var5 - var3;
         double var9 = Math.log(10.0D) / 20.0D;
         float var11 = (float)(var3 + 1.0D / var9 * Math.log(1.0D + (Math.exp(var9 * var7) - 1.0D) * (double)var2));
         this.gainControl.setValue(var11);
      }
   }

   public void setPitch(float var1) {
      if (this.sampleRateControl != null) {
         float var2 = var1;
         if (var1 < 0.5F) {
            var2 = 0.5F;
         }

         if (var2 > 2.0F) {
            var2 = 2.0F;
         }

         var2 *= this.initialSampleRate;
         this.sampleRateControl.setValue(var2);
      }
   }

   public boolean preLoadBuffers(LinkedList var1) {
      if (this.errorCheck(this.channelType != 1, "Buffers may only be queued for streaming sources.")) {
         return false;
      } else if (this.errorCheck(this.sourceDataLine == null, "SourceDataLine null in method 'preLoadBuffers'.")) {
         return false;
      } else {
         this.sourceDataLine.start();
         if (var1.isEmpty()) {
            return true;
         } else {
            byte[] var2 = (byte[])var1.remove(0);
            if (this.errorCheck(var2 == null, "Missing sound-bytes in method 'preLoadBuffers'.")) {
               return false;
            } else {
               while(!var1.isEmpty()) {
                  this.streamBuffers.add(new SoundBuffer((byte[])var1.remove(0), this.myFormat));
               }

               this.sourceDataLine.write(var2, 0, var2.length);
               this.processed = 0;
               return true;
            }
         }
      }
   }

   public boolean queueBuffer(byte[] var1) {
      if (this.errorCheck(this.channelType != 1, "Buffers may only be queued for streaming sources.")) {
         return false;
      } else if (this.errorCheck(this.sourceDataLine == null, "SourceDataLine null in method 'queueBuffer'.")) {
         return false;
      } else if (this.errorCheck(this.myFormat == null, "AudioFormat null in method 'queueBuffer'")) {
         return false;
      } else {
         this.streamBuffers.add(new SoundBuffer(var1, this.myFormat));
         this.processBuffer();
         this.processed = 0;
         return true;
      }
   }

   public boolean processBuffer() {
      if (this.errorCheck(this.channelType != 1, "Buffers are only processed for streaming sources.")) {
         return false;
      } else if (this.errorCheck(this.sourceDataLine == null, "SourceDataLine null in method 'processBuffer'.")) {
         return false;
      } else if (this.streamBuffers != null && !this.streamBuffers.isEmpty()) {
         SoundBuffer var1 = (SoundBuffer)this.streamBuffers.remove(0);
         this.sourceDataLine.write(var1.audioData, 0, var1.audioData.length);
         if (!this.sourceDataLine.isActive()) {
            this.sourceDataLine.start();
         }

         var1.cleanup();
         var1 = null;
         return true;
      } else {
         return false;
      }
   }

   public int feedRawAudioData(byte[] var1) {
      if (this.errorCheck(this.channelType != 1, "Raw audio data can only be processed by streaming sources.")) {
         return -1;
      } else if (this.errorCheck(this.streamBuffers == null, "StreamBuffers queue null in method 'feedRawAudioData'.")) {
         return -1;
      } else {
         this.streamBuffers.add(new SoundBuffer(var1, this.myFormat));
         return this.buffersProcessed();
      }
   }

   public int buffersProcessed() {
      this.processed = 0;
      if (this.errorCheck(this.channelType != 1, "Buffers may only be queued for streaming sources.")) {
         if (this.streamBuffers != null) {
            this.streamBuffers.clear();
         }

         return 0;
      } else if (this.sourceDataLine == null) {
         if (this.streamBuffers != null) {
            this.streamBuffers.clear();
         }

         return 0;
      } else {
         if (this.sourceDataLine.available() > 0) {
            this.processed = 1;
         }

         return this.processed;
      }
   }

   public void flush() {
      if (this.channelType == 1) {
         if (!this.errorCheck(this.sourceDataLine == null, "SourceDataLine null in method 'flush'.")) {
            this.sourceDataLine.stop();
            this.sourceDataLine.flush();
            this.sourceDataLine.drain();
            this.streamBuffers.clear();
            this.processed = 0;
         }
      }
   }

   public void close() {
      switch(this.channelType) {
      case 0:
         if (this.clip != null) {
            this.clip.stop();
            this.clip.flush();
            this.clip.close();
         }
         break;
      case 1:
         if (this.sourceDataLine != null) {
            this.flush();
            this.sourceDataLine.close();
         }
      }

   }

   public void play() {
      switch(this.channelType) {
      case 0:
         if (this.clip != null) {
            if (this.toLoop) {
               this.clip.stop();
               this.clip.loop(-1);
            } else {
               this.clip.stop();
               this.clip.start();
            }
         }
         break;
      case 1:
         if (this.sourceDataLine != null) {
            this.sourceDataLine.start();
         }
      }

   }

   public void pause() {
      switch(this.channelType) {
      case 0:
         if (this.clip != null) {
            this.clip.stop();
         }
         break;
      case 1:
         if (this.sourceDataLine != null) {
            this.sourceDataLine.stop();
         }
      }

   }

   public void stop() {
      switch(this.channelType) {
      case 0:
         if (this.clip != null) {
            this.clip.stop();
            this.clip.setFramePosition(0);
         }
         break;
      case 1:
         if (this.sourceDataLine != null) {
            this.sourceDataLine.stop();
         }
      }

   }

   public void rewind() {
      switch(this.channelType) {
      case 0:
         if (this.clip != null) {
            boolean var1 = this.clip.isRunning();
            this.clip.stop();
            this.clip.setFramePosition(0);
            if (var1) {
               if (this.toLoop) {
                  this.clip.loop(-1);
               } else {
                  this.clip.start();
               }
            }
         }
      case 1:
      default:
      }
   }

   public float millisecondsPlayed() {
      switch(this.channelType) {
      case 0:
         if (this.clip == null) {
            return -1.0F;
         }

         return (float)this.clip.getMicrosecondPosition() / 1000.0F;
      case 1:
         if (this.sourceDataLine == null) {
            return -1.0F;
         }

         return (float)this.sourceDataLine.getMicrosecondPosition() / 1000.0F;
      default:
         return -1.0F;
      }
   }

   public boolean playing() {
      switch(this.channelType) {
      case 0:
         if (this.clip == null) {
            return false;
         }

         return this.clip.isActive();
      case 1:
         if (this.sourceDataLine == null) {
            return false;
         }

         return this.sourceDataLine.isActive();
      default:
         return false;
      }
   }
}
