package paulscode.sound;

import java.net.URL;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.sound.sampled.AudioFormat;

public class Source {
   protected Class libraryType = Library.class;
   private static final boolean GET = false;
   private static final boolean SET = true;
   private static final boolean XXX = false;
   private SoundSystemLogger logger = SoundSystemConfig.getLogger();
   public boolean rawDataStream = false;
   public AudioFormat rawDataFormat = null;
   public boolean temporary = false;
   public boolean priority = false;
   public boolean toStream = false;
   public boolean toLoop = false;
   public boolean toPlay = false;
   public String sourcename = "";
   public FilenameURL filenameURL = null;
   public Vector3D position;
   public int attModel = 0;
   public float distOrRoll = 0.0F;
   public Vector3D velocity;
   public float gain = 1.0F;
   public float sourceVolume = 1.0F;
   protected float pitch = 1.0F;
   public float distanceFromListener = 0.0F;
   public Channel channel = null;
   public SoundBuffer soundBuffer = null;
   private boolean active = true;
   private boolean stopped = true;
   private boolean paused = false;
   protected ICodec codec = null;
   protected ICodec nextCodec = null;
   protected LinkedList nextBuffers = null;
   protected LinkedList soundSequenceQueue = null;
   protected final Object soundSequenceLock = new Object();
   public boolean preLoad = false;
   protected float fadeOutGain = -1.0F;
   protected float fadeInGain = 1.0F;
   protected long fadeOutMilis = 0L;
   protected long fadeInMilis = 0L;
   protected long lastFadeCheck = 0L;

   public Source(boolean var1, boolean var2, boolean var3, String var4, FilenameURL var5, SoundBuffer var6, float var7, float var8, float var9, int var10, float var11, boolean var12) {
      this.priority = var1;
      this.toStream = var2;
      this.toLoop = var3;
      this.sourcename = var4;
      this.filenameURL = var5;
      this.soundBuffer = var6;
      this.position = new Vector3D(var7, var8, var9);
      this.attModel = var10;
      this.distOrRoll = var11;
      this.velocity = new Vector3D(0.0F, 0.0F, 0.0F);
      this.temporary = var12;
      if (var2 && var5 != null) {
         this.codec = SoundSystemConfig.getCodec(var5.getFilename());
      }

   }

   public Source(Source var1, SoundBuffer var2) {
      this.priority = var1.priority;
      this.toStream = var1.toStream;
      this.toLoop = var1.toLoop;
      this.sourcename = var1.sourcename;
      this.filenameURL = var1.filenameURL;
      this.position = var1.position.clone();
      this.attModel = var1.attModel;
      this.distOrRoll = var1.distOrRoll;
      this.velocity = var1.velocity.clone();
      this.temporary = var1.temporary;
      this.sourceVolume = var1.sourceVolume;
      this.rawDataStream = var1.rawDataStream;
      this.rawDataFormat = var1.rawDataFormat;
      this.soundBuffer = var2;
      if (this.toStream && this.filenameURL != null) {
         this.codec = SoundSystemConfig.getCodec(this.filenameURL.getFilename());
      }

   }

   public Source(AudioFormat var1, boolean var2, String var3, float var4, float var5, float var6, int var7, float var8) {
      this.priority = var2;
      this.toStream = true;
      this.toLoop = false;
      this.sourcename = var3;
      this.filenameURL = null;
      this.soundBuffer = null;
      this.position = new Vector3D(var4, var5, var6);
      this.attModel = var7;
      this.distOrRoll = var8;
      this.velocity = new Vector3D(0.0F, 0.0F, 0.0F);
      this.temporary = false;
      this.rawDataStream = true;
      this.rawDataFormat = var1;
   }

   public void cleanup() {
      if (this.codec != null) {
         this.codec.cleanup();
      }

      Object var1;
      synchronized(var1 = this.soundSequenceLock){}
      if (this.soundSequenceQueue != null) {
         this.soundSequenceQueue.clear();
      }

      this.soundSequenceQueue = null;
      this.sourcename = null;
      this.filenameURL = null;
      this.position = null;
      this.soundBuffer = null;
      this.codec = null;
   }

   public void queueSound(FilenameURL var1) {
      if (!this.toStream) {
         this.errorMessage("Method 'queueSound' may only be used for streaming and MIDI sources.");
      } else if (var1 == null) {
         this.errorMessage("File not specified in method 'queueSound'");
      } else {
         Object var2;
         synchronized(var2 = this.soundSequenceLock){}
         if (this.soundSequenceQueue == null) {
            this.soundSequenceQueue = new LinkedList();
         }

         this.soundSequenceQueue.add(var1);
      }
   }

   public void dequeueSound(String var1) {
      if (!this.toStream) {
         this.errorMessage("Method 'dequeueSound' may only be used for streaming and MIDI sources.");
      } else if (var1 != null && !var1.equals("")) {
         Object var2;
         synchronized(var2 = this.soundSequenceLock){}
         if (this.soundSequenceQueue != null) {
            ListIterator var3 = this.soundSequenceQueue.listIterator();

            while(var3.hasNext()) {
               if (((FilenameURL)var3.next()).getFilename().equals(var1)) {
                  var3.remove();
                  break;
               }
            }
         }

      } else {
         this.errorMessage("Filename not specified in method 'dequeueSound'");
      }
   }

   public void fadeOut(FilenameURL var1, long var2) {
      if (!this.toStream) {
         this.errorMessage("Method 'fadeOut' may only be used for streaming and MIDI sources.");
      } else if (var2 < 0L) {
         this.errorMessage("Miliseconds may not be negative in method 'fadeOut'.");
      } else {
         this.fadeOutMilis = var2;
         this.fadeInMilis = 0L;
         this.fadeOutGain = 1.0F;
         this.lastFadeCheck = System.currentTimeMillis();
         Object var4;
         synchronized(var4 = this.soundSequenceLock){}
         if (this.soundSequenceQueue != null) {
            this.soundSequenceQueue.clear();
         }

         if (var1 != null) {
            if (this.soundSequenceQueue == null) {
               this.soundSequenceQueue = new LinkedList();
            }

            this.soundSequenceQueue.add(var1);
         }

      }
   }

   public void fadeOutIn(FilenameURL var1, long var2, long var4) {
      if (!this.toStream) {
         this.errorMessage("Method 'fadeOutIn' may only be used for streaming and MIDI sources.");
      } else if (var1 == null) {
         this.errorMessage("Filename/URL not specified in method 'fadeOutIn'.");
      } else if (var2 >= 0L && var4 >= 0L) {
         this.fadeOutMilis = var2;
         this.fadeInMilis = var4;
         this.fadeOutGain = 1.0F;
         this.lastFadeCheck = System.currentTimeMillis();
         Object var6;
         synchronized(var6 = this.soundSequenceLock){}
         if (this.soundSequenceQueue == null) {
            this.soundSequenceQueue = new LinkedList();
         }

         this.soundSequenceQueue.clear();
         this.soundSequenceQueue.add(var1);
      } else {
         this.errorMessage("Miliseconds may not be negative in method 'fadeOutIn'.");
      }
   }

   public boolean checkFadeOut() {
      if (!this.toStream) {
         return false;
      } else if (this.fadeOutGain == -1.0F && this.fadeInGain == 1.0F) {
         return false;
      } else {
         long var1 = System.currentTimeMillis();
         long var3 = var1 - this.lastFadeCheck;
         this.lastFadeCheck = var1;
         float var5;
         if (this.fadeOutGain >= 0.0F) {
            if (this.fadeOutMilis == 0L) {
               this.fadeOutGain = -1.0F;
               this.fadeInGain = 0.0F;
               if (this == false) {
                  this.stop();
               }

               this.positionChanged();
               this.preLoad = true;
               return false;
            } else {
               var5 = (float)var3 / (float)this.fadeOutMilis;
               this.fadeOutGain -= var5;
               if (this.fadeOutGain <= 0.0F) {
                  this.fadeOutGain = -1.0F;
                  this.fadeInGain = 0.0F;
                  if (this == false) {
                     this.stop();
                  }

                  this.positionChanged();
                  this.preLoad = true;
                  return false;
               } else {
                  this.positionChanged();
                  return true;
               }
            }
         } else if (this.fadeInGain < 1.0F) {
            this.fadeOutGain = -1.0F;
            if (this.fadeInMilis == 0L) {
               this.fadeOutGain = -1.0F;
               this.fadeInGain = 1.0F;
            } else {
               var5 = (float)var3 / (float)this.fadeInMilis;
               this.fadeInGain += var5;
               if (this.fadeInGain >= 1.0F) {
                  this.fadeOutGain = -1.0F;
                  this.fadeInGain = 1.0F;
               }
            }

            this.positionChanged();
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean readBuffersFromNextSoundInSequence() {
      if (!this.toStream) {
         this.errorMessage("Method 'readBuffersFromNextSoundInSequence' may only be used for streaming sources.");
         return false;
      } else {
         Object var1;
         synchronized(var1 = this.soundSequenceLock){}
         if (this.soundSequenceQueue != null && this.soundSequenceQueue.size() > 0) {
            if (this.nextCodec != null) {
               this.nextCodec.cleanup();
            }

            this.nextCodec = SoundSystemConfig.getCodec(((FilenameURL)this.soundSequenceQueue.get(0)).getFilename());
            this.nextCodec.initialize(((FilenameURL)this.soundSequenceQueue.get(0)).getURL());
            SoundBuffer var2 = null;

            for(int var3 = 0; var3 < SoundSystemConfig.getNumberStreamingBuffers() && !this.nextCodec.endOfStream(); ++var3) {
               var2 = this.nextCodec.read();
               if (var2 != null) {
                  if (this.nextBuffers == null) {
                     this.nextBuffers = new LinkedList();
                  }

                  this.nextBuffers.add(var2);
               }
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public int getSoundSequenceQueueSize() {
      return this.soundSequenceQueue == null ? 0 : this.soundSequenceQueue.size();
   }

   public void setTemporary(boolean var1) {
      this.temporary = var1;
   }

   public void listenerMoved() {
   }

   public void setPosition(float var1, float var2, float var3) {
      this.position.x = var1;
      this.position.y = var2;
      this.position.z = var3;
   }

   public void positionChanged() {
   }

   public void setPriority(boolean var1) {
      this.priority = var1;
   }

   public void setLooping(boolean var1) {
      this.toLoop = var1;
   }

   public void setAttenuation(int var1) {
      this.attModel = var1;
   }

   public void setDistOrRoll(float var1) {
      this.distOrRoll = var1;
   }

   public void setVelocity(float var1, float var2, float var3) {
      this.velocity.x = var1;
      this.velocity.y = var2;
      this.velocity.z = var3;
   }

   public float getDistanceFromListener() {
      return this.distanceFromListener;
   }

   public void setPitch(float var1) {
      float var2 = var1;
      if (var1 < 0.5F) {
         var2 = 0.5F;
      } else if (var1 > 2.0F) {
         var2 = 2.0F;
      }

      this.pitch = var2;
   }

   public float getPitch() {
      return this.pitch;
   }

   public boolean reverseByteOrder() {
      return SoundSystemConfig.reverseByteOrder(this.libraryType);
   }

   public void changeSource(boolean var1, boolean var2, boolean var3, String var4, FilenameURL var5, SoundBuffer var6, float var7, float var8, float var9, int var10, float var11, boolean var12) {
      this.priority = var1;
      this.toStream = var2;
      this.toLoop = var3;
      this.sourcename = var4;
      this.filenameURL = var5;
      this.soundBuffer = var6;
      this.position.x = var7;
      this.position.y = var8;
      this.position.z = var9;
      this.attModel = var10;
      this.distOrRoll = var11;
      this.temporary = var12;
   }

   public int feedRawAudioData(Channel var1, byte[] var2) {
      this.toPlay = true;
      return -1;
   }

   public void play(Channel var1) {
      if (this.toLoop) {
         this.toPlay = true;
      }

   }

   public boolean stream() {
      if (this.channel == null) {
         return false;
      } else {
         if (this.preLoad) {
            if (!this.rawDataStream) {
               return this.preLoad();
            }

            this.preLoad = false;
         }

         if (this.rawDataStream) {
            if (!this.stopped() && !this.paused()) {
               if (this.channel.buffersProcessed() > 0) {
                  this.channel.processBuffer();
               }

               return true;
            } else {
               return true;
            }
         } else if (this.codec == null) {
            return false;
         } else if (this.stopped()) {
            return false;
         } else if (this.paused()) {
            return true;
         } else {
            int var1 = this.channel.buffersProcessed();
            SoundBuffer var2 = null;

            for(int var3 = 0; var3 < var1; ++var3) {
               var2 = this.codec.read();
               if (var2 != null) {
                  if (var2.audioData != null) {
                     this.channel.queueBuffer(var2.audioData);
                  }

                  var2.cleanup();
                  var2 = null;
                  return true;
               }

               if (this.codec.endOfStream()) {
                  Object var4;
                  synchronized(var4 = this.soundSequenceLock){}
                  if (SoundSystemConfig.getStreamQueueFormatsMatch()) {
                     if (this.soundSequenceQueue != null && this.soundSequenceQueue.size() > 0) {
                        if (this.codec != null) {
                           this.codec.cleanup();
                        }

                        this.filenameURL = (FilenameURL)this.soundSequenceQueue.remove(0);
                        this.codec = SoundSystemConfig.getCodec(this.filenameURL.getFilename());
                        this.codec.initialize(this.filenameURL.getURL());
                        var2 = this.codec.read();
                        if (var2 != null) {
                           if (var2.audioData != null) {
                              this.channel.queueBuffer(var2.audioData);
                           }

                           var2.cleanup();
                           var2 = null;
                           return true;
                        }
                     } else if (this.toLoop) {
                        this.codec.initialize(this.filenameURL.getURL());
                        var2 = this.codec.read();
                        if (var2 != null) {
                           if (var2.audioData != null) {
                              this.channel.queueBuffer(var2.audioData);
                           }

                           var2.cleanup();
                           var2 = null;
                           return true;
                        }
                     }
                  }
               }
            }

            return false;
         }
      }
   }

   public boolean preLoad() {
      if (this.channel == null) {
         return false;
      } else if (this.codec == null) {
         return false;
      } else {
         SoundBuffer var1 = null;
         boolean var2 = false;
         Object var3;
         synchronized(var3 = this.soundSequenceLock){}
         if (this.nextBuffers == null || this.nextBuffers.isEmpty()) {
            var2 = true;
         }

         if (this.nextCodec != null && !var2) {
            this.codec = this.nextCodec;
            this.nextCodec = null;
            synchronized(this.soundSequenceLock){}

            while(!this.nextBuffers.isEmpty()) {
               var1 = (SoundBuffer)this.nextBuffers.remove(0);
               if (var1 != null) {
                  if (var1.audioData != null) {
                     this.channel.queueBuffer(var1.audioData);
                  }

                  var1.cleanup();
                  var1 = null;
               }
            }
         } else {
            this.nextCodec = null;
            URL var6 = this.filenameURL.getURL();
            this.codec.initialize(var6);

            for(int var4 = 0; var4 < SoundSystemConfig.getNumberStreamingBuffers(); ++var4) {
               var1 = this.codec.read();
               if (var1 != null) {
                  if (var1.audioData != null) {
                     this.channel.queueBuffer(var1.audioData);
                  }

                  var1.cleanup();
                  var1 = null;
               }
            }
         }

         return true;
      }
   }

   public void pause() {
      this.toPlay = false;
      this.paused(true, true);
      if (this.channel != null) {
         this.channel.pause();
      } else {
         this.errorMessage("Channel null in method 'pause'");
      }

   }

   public void stop() {
      this.toPlay = false;
      this.stopped(true, true);
      this.paused(true, false);
      if (this.channel != null) {
         this.channel.stop();
      } else {
         this.errorMessage("Channel null in method 'stop'");
      }

   }

   public void rewind() {
      this.stop();
      if (this.channel != null) {
         boolean var1 = this.playing();
         this.channel.rewind();
         if (this.toStream && var1) {
            this.stop();
            this.play(this.channel);
         }
      } else {
         this.errorMessage("Channel null in method 'rewind'");
      }

   }

   public void flush() {
      if (this.channel != null) {
         this.channel.flush();
      } else {
         this.errorMessage("Channel null in method 'flush'");
      }

   }

   public void cull() {
   }

   public void activate() {
      this.active(true, true);
   }

   public boolean active() {
      return this.active(false, false);
   }

   public boolean stopped() {
      return this.stopped(false, false);
   }

   public boolean paused() {
      return this.paused(false, false);
   }

   public float millisecondsPlayed() {
      return this.channel == null ? -1.0F : this.channel.millisecondsPlayed();
   }

   private synchronized boolean stopped(boolean var1, boolean var2) {
      if (var1) {
         this.stopped = var2;
      }

      return this.stopped;
   }

   public String getClassName() {
      String var1 = SoundSystemConfig.getLibraryTitle(this.libraryType);
      return var1.equals("No Sound") ? "Source" : "Source" + var1;
   }

   protected void message(String var1) {
      this.logger.message(var1, 0);
   }

   protected void importantMessage(String var1) {
      this.logger.importantMessage(var1, 0);
   }

   protected boolean errorCheck(boolean var1, String var2) {
      return this.logger.errorCheck(var1, this.getClassName(), var2, 0);
   }

   protected void errorMessage(String var1) {
      this.logger.errorMessage(this.getClassName(), var1, 0);
   }

   protected void printStackTrace(Exception var1) {
      this.logger.printStackTrace(var1, 1);
   }
}
