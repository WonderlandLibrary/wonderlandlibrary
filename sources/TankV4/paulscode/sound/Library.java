package paulscode.sound;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.sound.sampled.AudioFormat;

public class Library {
   private SoundSystemLogger logger = SoundSystemConfig.getLogger();
   protected ListenerData listener;
   protected HashMap bufferMap = null;
   protected HashMap sourceMap;
   private MidiChannel midiChannel;
   protected List streamingChannels;
   protected List normalChannels;
   private String[] streamingChannelSourceNames;
   private String[] normalChannelSourceNames;
   private int nextStreamingChannel = 0;
   private int nextNormalChannel = 0;
   protected StreamThread streamThread;
   protected boolean reverseByteOrder = false;

   public Library() throws SoundSystemException {
      this.bufferMap = new HashMap();
      this.sourceMap = new HashMap();
      this.listener = new ListenerData(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 0.0F, 0.0F);
      this.streamingChannels = new LinkedList();
      this.normalChannels = new LinkedList();
      this.streamingChannelSourceNames = new String[SoundSystemConfig.getNumberStreamingChannels()];
      this.normalChannelSourceNames = new String[SoundSystemConfig.getNumberNormalChannels()];
      this.streamThread = new StreamThread();
      this.streamThread.start();
   }

   public void cleanup() {
      this.streamThread.kill();
      this.streamThread.interrupt();

      for(int var1 = 0; var1 < 50 && this.streamThread.alive(); ++var1) {
         try {
            Thread.sleep(100L);
         } catch (Exception var6) {
         }
      }

      if (this.streamThread.alive()) {
         this.errorMessage("Stream thread did not die!");
         this.message("Ignoring errors... continuing clean-up.");
      }

      if (this.midiChannel != null) {
         this.midiChannel.cleanup();
         this.midiChannel = null;
      }

      Channel var7 = null;
      if (this.streamingChannels != null) {
         while(!this.streamingChannels.isEmpty()) {
            var7 = (Channel)this.streamingChannels.remove(0);
            var7.close();
            var7.cleanup();
            var7 = null;
         }

         this.streamingChannels.clear();
         this.streamingChannels = null;
      }

      if (this.normalChannels != null) {
         while(!this.normalChannels.isEmpty()) {
            var7 = (Channel)this.normalChannels.remove(0);
            var7.close();
            var7.cleanup();
            var7 = null;
         }

         this.normalChannels.clear();
         this.normalChannels = null;
      }

      Set var2 = this.sourceMap.keySet();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         Source var5 = (Source)this.sourceMap.get(var4);
         if (var5 != null) {
            var5.cleanup();
         }
      }

      this.sourceMap.clear();
      this.sourceMap = null;
      this.listener = null;
      this.streamThread = null;
   }

   public void init() throws SoundSystemException {
      Channel var1 = null;

      int var2;
      for(var2 = 0; var2 < SoundSystemConfig.getNumberStreamingChannels(); ++var2) {
         var1 = this.createChannel(1);
         if (var1 == null) {
            break;
         }

         this.streamingChannels.add(var1);
      }

      for(var2 = 0; var2 < SoundSystemConfig.getNumberNormalChannels(); ++var2) {
         var1 = this.createChannel(0);
         if (var1 == null) {
            break;
         }

         this.normalChannels.add(var1);
      }

   }

   public static boolean libraryCompatible() {
      return true;
   }

   protected Channel createChannel(int var1) {
      return new Channel(var1);
   }

   public boolean loadSound(FilenameURL var1) {
      return true;
   }

   public boolean loadSound(SoundBuffer var1, String var2) {
      return true;
   }

   public LinkedList getAllLoadedFilenames() {
      LinkedList var1 = new LinkedList();
      Set var2 = this.bufferMap.keySet();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         var1.add(var3.next());
      }

      return var1;
   }

   public LinkedList getAllSourcenames() {
      LinkedList var1 = new LinkedList();
      Set var2 = this.sourceMap.keySet();
      Iterator var3 = var2.iterator();
      if (this.midiChannel != null) {
         var1.add(this.midiChannel.getSourcename());
      }

      while(var3.hasNext()) {
         var1.add(var3.next());
      }

      return var1;
   }

   public void unloadSound(String var1) {
      this.bufferMap.remove(var1);
   }

   public void rawDataStream(AudioFormat var1, boolean var2, String var3, float var4, float var5, float var6, int var7, float var8) {
      this.sourceMap.put(var3, new Source(var1, var2, var3, var4, var5, var6, var7, var8));
   }

   public void newSource(boolean var1, boolean var2, boolean var3, String var4, FilenameURL var5, float var6, float var7, float var8, int var9, float var10) {
      this.sourceMap.put(var4, new Source(var1, var2, var3, var4, var5, (SoundBuffer)null, var6, var7, var8, var9, var10, false));
   }

   public void quickPlay(boolean var1, boolean var2, boolean var3, String var4, FilenameURL var5, float var6, float var7, float var8, int var9, float var10, boolean var11) {
      this.sourceMap.put(var4, new Source(var1, var2, var3, var4, var5, (SoundBuffer)null, var6, var7, var8, var9, var10, var11));
   }

   public void setTemporary(String var1, boolean var2) {
      Source var3 = (Source)this.sourceMap.get(var1);
      if (var3 != null) {
         var3.setTemporary(var2);
      }

   }

   public void setPosition(String var1, float var2, float var3, float var4) {
      Source var5 = (Source)this.sourceMap.get(var1);
      if (var5 != null) {
         var5.setPosition(var2, var3, var4);
      }

   }

   public void setPriority(String var1, boolean var2) {
      Source var3 = (Source)this.sourceMap.get(var1);
      if (var3 != null) {
         var3.setPriority(var2);
      }

   }

   public void setLooping(String var1, boolean var2) {
      Source var3 = (Source)this.sourceMap.get(var1);
      if (var3 != null) {
         var3.setLooping(var2);
      }

   }

   public void setAttenuation(String var1, int var2) {
      Source var3 = (Source)this.sourceMap.get(var1);
      if (var3 != null) {
         var3.setAttenuation(var2);
      }

   }

   public void setDistOrRoll(String var1, float var2) {
      Source var3 = (Source)this.sourceMap.get(var1);
      if (var3 != null) {
         var3.setDistOrRoll(var2);
      }

   }

   public void setVelocity(String var1, float var2, float var3, float var4) {
      Source var5 = (Source)this.sourceMap.get(var1);
      if (var5 != null) {
         var5.setVelocity(var2, var3, var4);
      }

   }

   public void setListenerVelocity(float var1, float var2, float var3) {
      this.listener.setVelocity(var1, var2, var3);
   }

   public void dopplerChanged() {
   }

   public float millisecondsPlayed(String var1) {
      if (var1 != null && !var1.equals("")) {
         if (var1 != null) {
            this.errorMessage("Unable to calculate milliseconds for MIDI source.");
            return -1.0F;
         } else {
            Source var2 = (Source)this.sourceMap.get(var1);
            if (var2 == null) {
               this.errorMessage("Source '" + var1 + "' not found in " + "method 'millisecondsPlayed'");
            }

            return var2.millisecondsPlayed();
         }
      } else {
         this.errorMessage("Sourcename not specified in method 'millisecondsPlayed'");
         return -1.0F;
      }
   }

   public int feedRawAudioData(String var1, byte[] var2) {
      if (var1 != null && !var1.equals("")) {
         if (var1 != null) {
            this.errorMessage("Raw audio data can not be fed to the MIDI channel.");
            return -1;
         } else {
            Source var3 = (Source)this.sourceMap.get(var1);
            if (var3 == null) {
               this.errorMessage("Source '" + var1 + "' not found in " + "method 'feedRawAudioData'");
            }

            return this.feedRawAudioData(var3, var2);
         }
      } else {
         this.errorMessage("Sourcename not specified in method 'feedRawAudioData'");
         return -1;
      }
   }

   public int feedRawAudioData(Source var1, byte[] var2) {
      if (var1 == null) {
         this.errorMessage("Source parameter null in method 'feedRawAudioData'");
         return -1;
      } else if (!var1.toStream) {
         this.errorMessage("Only a streaming source may be specified in method 'feedRawAudioData'");
         return -1;
      } else if (!var1.rawDataStream) {
         this.errorMessage("Streaming source already associated with a file or URL in method'feedRawAudioData'");
         return -1;
      } else if (var1.playing() && var1.channel != null) {
         return var1.feedRawAudioData(var1.channel, var2);
      } else {
         Channel var3;
         if (var1.channel != null && var1.channel.attachedSource == var1) {
            var3 = var1.channel;
         } else {
            var3 = this.getNextChannel(var1);
         }

         int var4 = var1.feedRawAudioData(var3, var2);
         var3.attachedSource = var1;
         this.streamThread.watch(var1);
         this.streamThread.interrupt();
         return var4;
      }
   }

   public void play(String var1) {
      if (var1 != null && !var1.equals("")) {
         if (var1 != null) {
            this.midiChannel.play();
         } else {
            Source var2 = (Source)this.sourceMap.get(var1);
            if (var2 == null) {
               this.errorMessage("Source '" + var1 + "' not found in " + "method 'play'");
            }

            this.play(var2);
         }

      } else {
         this.errorMessage("Sourcename not specified in method 'play'");
      }
   }

   public void play(Source var1) {
      if (var1 != null) {
         if (!var1.rawDataStream) {
            if (var1.active()) {
               if (!var1.playing()) {
                  Channel var2 = this.getNextChannel(var1);
                  if (var1 != null && var2 != null) {
                     if (var1.channel != null && var1.channel.attachedSource != var1) {
                        var1.channel = null;
                     }

                     var2.attachedSource = var1;
                     var1.play(var2);
                     if (var1.toStream) {
                        this.streamThread.watch(var1);
                        this.streamThread.interrupt();
                     }
                  }
               }

            }
         }
      }
   }

   public void stop(String var1) {
      if (var1 != null && !var1.equals("")) {
         if (var1 != null) {
            this.midiChannel.stop();
         } else {
            Source var2 = (Source)this.sourceMap.get(var1);
            if (var2 != null) {
               var2.stop();
            }
         }

      } else {
         this.errorMessage("Sourcename not specified in method 'stop'");
      }
   }

   public void pause(String var1) {
      if (var1 != null && !var1.equals("")) {
         if (var1 != null) {
            this.midiChannel.pause();
         } else {
            Source var2 = (Source)this.sourceMap.get(var1);
            if (var2 != null) {
               var2.pause();
            }
         }

      } else {
         this.errorMessage("Sourcename not specified in method 'stop'");
      }
   }

   public void rewind(String var1) {
      if (var1 != null) {
         this.midiChannel.rewind();
      } else {
         Source var2 = (Source)this.sourceMap.get(var1);
         if (var2 != null) {
            var2.rewind();
         }
      }

   }

   public void flush(String var1) {
      if (var1 != null) {
         this.errorMessage("You can not flush the MIDI channel");
      } else {
         Source var2 = (Source)this.sourceMap.get(var1);
         if (var2 != null) {
            var2.flush();
         }
      }

   }

   public void cull(String var1) {
      Source var2 = (Source)this.sourceMap.get(var1);
      if (var2 != null) {
         var2.cull();
      }

   }

   public void activate(String var1) {
      Source var2 = (Source)this.sourceMap.get(var1);
      if (var2 != null) {
         var2.activate();
         if (var2.toPlay) {
            this.play(var2);
         }
      }

   }

   public void setMasterVolume(float var1) {
      SoundSystemConfig.setMasterGain(var1);
      if (this.midiChannel != null) {
         this.midiChannel.resetGain();
      }

   }

   public void setVolume(String var1, float var2) {
      if (var1 != null) {
         this.midiChannel.setVolume(var2);
      } else {
         Source var3 = (Source)this.sourceMap.get(var1);
         if (var3 != null) {
            float var4 = var2;
            if (var2 < 0.0F) {
               var4 = 0.0F;
            } else if (var2 > 1.0F) {
               var4 = 1.0F;
            }

            var3.sourceVolume = var4;
            var3.positionChanged();
         }
      }

   }

   public float getVolume(String var1) {
      if (var1 != null) {
         return this.midiChannel.getVolume();
      } else {
         Source var2 = (Source)this.sourceMap.get(var1);
         return var2 != null ? var2.sourceVolume : 0.0F;
      }
   }

   public void setPitch(String var1, float var2) {
      if (var1 != null) {
         Source var3 = (Source)this.sourceMap.get(var1);
         if (var3 != null) {
            float var4 = var2;
            if (var2 < 0.5F) {
               var4 = 0.5F;
            } else if (var2 > 2.0F) {
               var4 = 2.0F;
            }

            var3.setPitch(var4);
            var3.positionChanged();
         }
      }

   }

   public float getPitch(String var1) {
      if (var1 != null) {
         Source var2 = (Source)this.sourceMap.get(var1);
         if (var2 != null) {
            return var2.getPitch();
         }
      }

      return 1.0F;
   }

   public void moveListener(float var1, float var2, float var3) {
      this.setListenerPosition(this.listener.position.x + var1, this.listener.position.y + var2, this.listener.position.z + var3);
   }

   public void setListenerPosition(float var1, float var2, float var3) {
      this.listener.setPosition(var1, var2, var3);
      Set var4 = this.sourceMap.keySet();
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         Source var7 = (Source)this.sourceMap.get(var6);
         if (var7 != null) {
            var7.positionChanged();
         }
      }

   }

   public void turnListener(float var1) {
      this.setListenerAngle(this.listener.angle + var1);
      Set var2 = this.sourceMap.keySet();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         Source var5 = (Source)this.sourceMap.get(var4);
         if (var5 != null) {
            var5.positionChanged();
         }
      }

   }

   public void setListenerAngle(float var1) {
      this.listener.setAngle(var1);
      Set var2 = this.sourceMap.keySet();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         Source var5 = (Source)this.sourceMap.get(var4);
         if (var5 != null) {
            var5.positionChanged();
         }
      }

   }

   public void setListenerOrientation(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.listener.setOrientation(var1, var2, var3, var4, var5, var6);
      Set var7 = this.sourceMap.keySet();
      Iterator var8 = var7.iterator();

      while(var8.hasNext()) {
         String var9 = (String)var8.next();
         Source var10 = (Source)this.sourceMap.get(var9);
         if (var10 != null) {
            var10.positionChanged();
         }
      }

   }

   public void setListenerData(ListenerData var1) {
      this.listener.setData(var1);
   }

   public void copySources(HashMap var1) {
      if (var1 != null) {
         Set var2 = var1.keySet();
         Iterator var3 = var2.iterator();
         this.sourceMap.clear();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            Source var5 = (Source)var1.get(var4);
            if (var5 != null) {
               this.loadSound(var5.filenameURL);
               this.sourceMap.put(var4, new Source(var5, (SoundBuffer)null));
            }
         }

      }
   }

   public void removeSource(String var1) {
      Source var2 = (Source)this.sourceMap.get(var1);
      if (var2 != null) {
         var2.cleanup();
      }

      this.sourceMap.remove(var1);
   }

   public void removeTemporarySources() {
      Set var1 = this.sourceMap.keySet();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Source var4 = (Source)this.sourceMap.get(var3);
         if (var4 != null && var4.temporary && !var4.playing()) {
            var4.cleanup();
            var2.remove();
         }
      }

   }

   private Channel getNextChannel(Source var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = var1.sourcename;
         if (var2 == null) {
            return null;
         } else {
            int var5;
            List var6;
            String[] var7;
            if (var1.toStream) {
               var5 = this.nextStreamingChannel;
               var6 = this.streamingChannels;
               var7 = this.streamingChannelSourceNames;
            } else {
               var5 = this.nextNormalChannel;
               var6 = this.normalChannels;
               var7 = this.normalChannelSourceNames;
            }

            int var4 = var6.size();

            int var3;
            for(var3 = 0; var3 < var4; ++var3) {
               if (var2.equals(var7[var3])) {
                  return (Channel)var6.get(var3);
               }
            }

            int var9 = var5;
            var3 = 0;

            while(true) {
               String var8;
               Source var10;
               if (var3 < var4) {
                  var8 = var7[var9];
                  if (var8 == null) {
                     var10 = null;
                  } else {
                     var10 = (Source)this.sourceMap.get(var8);
                  }

                  if (var10 != null && var10.playing()) {
                     ++var9;
                     if (var9 >= var4) {
                        var9 = 0;
                     }

                     ++var3;
                     continue;
                  }

                  if (var1.toStream) {
                     this.nextStreamingChannel = var9 + 1;
                     if (this.nextStreamingChannel >= var4) {
                        this.nextStreamingChannel = 0;
                     }
                  } else {
                     this.nextNormalChannel = var9 + 1;
                     if (this.nextNormalChannel >= var4) {
                        this.nextNormalChannel = 0;
                     }
                  }

                  var7[var9] = var2;
                  return (Channel)var6.get(var9);
               }

               var9 = var5;

               for(var3 = 0; var3 < var4; ++var3) {
                  var8 = var7[var9];
                  if (var8 == null) {
                     var10 = null;
                  } else {
                     var10 = (Source)this.sourceMap.get(var8);
                  }

                  if (var10 == null || !var10.playing() || !var10.priority) {
                     if (var1.toStream) {
                        this.nextStreamingChannel = var9 + 1;
                        if (this.nextStreamingChannel >= var4) {
                           this.nextStreamingChannel = 0;
                        }
                     } else {
                        this.nextNormalChannel = var9 + 1;
                        if (this.nextNormalChannel >= var4) {
                           this.nextNormalChannel = 0;
                        }
                     }

                     var7[var9] = var2;
                     return (Channel)var6.get(var9);
                  }

                  ++var9;
                  if (var9 >= var4) {
                     var9 = 0;
                  }
               }

               return null;
            }
         }
      }
   }

   public void replaySources() {
      Set var1 = this.sourceMap.keySet();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Source var4 = (Source)this.sourceMap.get(var3);
         if (var4 != null && var4.toPlay && !var4.playing()) {
            this.play(var3);
            var4.toPlay = false;
         }
      }

   }

   public void queueSound(String var1, FilenameURL var2) {
      if (var1 != null) {
         this.midiChannel.queueSound(var2);
      } else {
         Source var3 = (Source)this.sourceMap.get(var1);
         if (var3 != null) {
            var3.queueSound(var2);
         }
      }

   }

   public void dequeueSound(String var1, String var2) {
      if (var1 != null) {
         this.midiChannel.dequeueSound(var2);
      } else {
         Source var3 = (Source)this.sourceMap.get(var1);
         if (var3 != null) {
            var3.dequeueSound(var2);
         }
      }

   }

   public void fadeOut(String var1, FilenameURL var2, long var3) {
      if (var1 != null) {
         this.midiChannel.fadeOut(var2, var3);
      } else {
         Source var5 = (Source)this.sourceMap.get(var1);
         if (var5 != null) {
            var5.fadeOut(var2, var3);
         }
      }

   }

   public void fadeOutIn(String var1, FilenameURL var2, long var3, long var5) {
      if (var1 != null) {
         this.midiChannel.fadeOutIn(var2, var3, var5);
      } else {
         Source var7 = (Source)this.sourceMap.get(var1);
         if (var7 != null) {
            var7.fadeOutIn(var2, var3, var5);
         }
      }

   }

   public void checkFadeVolumes() {
      if (this.midiChannel != null) {
         this.midiChannel.resetGain();
      }

      Channel var1;
      Source var2;
      for(int var3 = 0; var3 < this.streamingChannels.size(); ++var3) {
         var1 = (Channel)this.streamingChannels.get(var3);
         if (var1 != null) {
            var2 = var1.attachedSource;
            if (var2 != null) {
               var2.checkFadeOut();
            }
         }
      }

      var1 = null;
      var2 = null;
   }

   public void loadMidi(boolean var1, String var2, FilenameURL var3) {
      if (var3 == null) {
         this.errorMessage("Filename/URL not specified in method 'loadMidi'.");
      } else if (!var3.getFilename().matches(SoundSystemConfig.EXTENSION_MIDI)) {
         this.errorMessage("Filename/identifier doesn't end in '.mid' or'.midi' in method loadMidi.");
      } else {
         if (this.midiChannel == null) {
            this.midiChannel = new MidiChannel(var1, var2, var3);
         } else {
            this.midiChannel.switchSource(var1, var2, var3);
         }

      }
   }

   public void unloadMidi() {
      if (this.midiChannel != null) {
         this.midiChannel.cleanup();
      }

      this.midiChannel = null;
   }

   public Source getSource(String var1) {
      return (Source)this.sourceMap.get(var1);
   }

   public MidiChannel getMidiChannel() {
      return this.midiChannel;
   }

   public void setMidiChannel(MidiChannel var1) {
      if (this.midiChannel != null && this.midiChannel != var1) {
         this.midiChannel.cleanup();
      }

      this.midiChannel = var1;
   }

   public void listenerMoved() {
      Set var1 = this.sourceMap.keySet();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Source var4 = (Source)this.sourceMap.get(var3);
         if (var4 != null) {
            var4.listenerMoved();
         }
      }

   }

   public HashMap getSources() {
      return this.sourceMap;
   }

   public ListenerData getListenerData() {
      return this.listener;
   }

   public boolean reverseByteOrder() {
      return this.reverseByteOrder;
   }

   public static String getTitle() {
      return "No Sound";
   }

   public static String getDescription() {
      return "Silent Mode";
   }

   public String getClassName() {
      return "Library";
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
