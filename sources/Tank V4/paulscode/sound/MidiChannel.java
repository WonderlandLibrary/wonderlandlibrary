package paulscode.sound;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiDevice.Info;

public class MidiChannel implements MetaEventListener {
   private SoundSystemLogger logger;
   private FilenameURL filenameURL;
   private String sourcename;
   private static final int CHANGE_VOLUME = 7;
   private static final int END_OF_TRACK = 47;
   private static final boolean GET = false;
   private static final boolean SET = true;
   private static final boolean XXX = false;
   private Sequencer sequencer = null;
   private Synthesizer synthesizer = null;
   private MidiDevice synthDevice = null;
   private Sequence sequence = null;
   private boolean toLoop = true;
   private float gain = 1.0F;
   private boolean loading = true;
   private LinkedList sequenceQueue = null;
   private final Object sequenceQueueLock = new Object();
   protected float fadeOutGain = -1.0F;
   protected float fadeInGain = 1.0F;
   protected long fadeOutMilis = 0L;
   protected long fadeInMilis = 0L;
   protected long lastFadeCheck = 0L;
   private MidiChannel.FadeThread fadeThread = null;

   public MidiChannel(boolean var1, String var2, String var3) {
      this.loading(true, true);
      this.logger = SoundSystemConfig.getLogger();
      this.filenameURL(true, new FilenameURL(var3));
      this.sourcename(true, var2);
      this.setLooping(var1);
      this.init();
      this.loading(true, false);
   }

   public MidiChannel(boolean var1, String var2, URL var3, String var4) {
      this.loading(true, true);
      this.logger = SoundSystemConfig.getLogger();
      this.filenameURL(true, new FilenameURL(var3, var4));
      this.sourcename(true, var2);
      this.setLooping(var1);
      this.init();
      this.loading(true, false);
   }

   public MidiChannel(boolean var1, String var2, FilenameURL var3) {
      this.loading(true, true);
      this.logger = SoundSystemConfig.getLogger();
      this.filenameURL(true, var3);
      this.sourcename(true, var2);
      this.setLooping(var1);
      this.init();
      this.loading(true, false);
   }

   private void init() {
      this.getSequencer();
      this.setSequence(this.filenameURL(false, (FilenameURL)null).getURL());
      this.getSynthesizer();
      this.resetGain();
   }

   public void cleanup() {
      this.loading(true, true);
      this.setLooping(true);
      if (this.sequencer != null) {
         try {
            this.sequencer.stop();
            this.sequencer.close();
            this.sequencer.removeMetaEventListener(this);
         } catch (Exception var6) {
         }
      }

      this.logger = null;
      this.sequencer = null;
      this.synthesizer = null;
      this.sequence = null;
      Object var1;
      synchronized(var1 = this.sequenceQueueLock){}
      if (this.sequenceQueue != null) {
         this.sequenceQueue.clear();
      }

      this.sequenceQueue = null;
      if (this.fadeThread != null) {
         boolean var7 = false;

         try {
            this.fadeThread.kill();
            this.fadeThread.interrupt();
         } catch (Exception var5) {
            var7 = true;
         }

         if (!var7) {
            for(int var2 = 0; var2 < 50 && this.fadeThread.alive(); ++var2) {
               try {
                  Thread.sleep(100L);
               } catch (InterruptedException var4) {
               }
            }
         }

         if (var7 || this.fadeThread.alive()) {
            this.errorMessage("MIDI fade effects thread did not die!");
            this.message("Ignoring errors... continuing clean-up.");
         }
      }

      this.fadeThread = null;
      this.loading(true, false);
   }

   public void queueSound(FilenameURL var1) {
      if (var1 == null) {
         this.errorMessage("Filename/URL not specified in method 'queueSound'");
      } else {
         Object var2;
         synchronized(var2 = this.sequenceQueueLock){}
         if (this.sequenceQueue == null) {
            this.sequenceQueue = new LinkedList();
         }

         this.sequenceQueue.add(var1);
      }
   }

   public void dequeueSound(String var1) {
      if (var1 != null && !var1.equals("")) {
         Object var2;
         synchronized(var2 = this.sequenceQueueLock){}
         if (this.sequenceQueue != null) {
            ListIterator var3 = this.sequenceQueue.listIterator();

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
      if (var2 < 0L) {
         this.errorMessage("Miliseconds may not be negative in method 'fadeOut'.");
      } else {
         this.fadeOutMilis = var2;
         this.fadeInMilis = 0L;
         this.fadeOutGain = 1.0F;
         this.lastFadeCheck = System.currentTimeMillis();
         Object var4;
         synchronized(var4 = this.sequenceQueueLock){}
         if (this.sequenceQueue != null) {
            this.sequenceQueue.clear();
         }

         if (var1 != null) {
            if (this.sequenceQueue == null) {
               this.sequenceQueue = new LinkedList();
            }

            this.sequenceQueue.add(var1);
         }

         if (this.fadeThread == null) {
            this.fadeThread = new MidiChannel.FadeThread(this);
            this.fadeThread.start();
         }

         this.fadeThread.interrupt();
      }
   }

   public void fadeOutIn(FilenameURL var1, long var2, long var4) {
      if (var1 == null) {
         this.errorMessage("Filename/URL not specified in method 'fadeOutIn'.");
      } else if (var2 >= 0L && var4 >= 0L) {
         this.fadeOutMilis = var2;
         this.fadeInMilis = var4;
         this.fadeOutGain = 1.0F;
         this.lastFadeCheck = System.currentTimeMillis();
         Object var6;
         synchronized(var6 = this.sequenceQueueLock){}
         if (this.sequenceQueue == null) {
            this.sequenceQueue = new LinkedList();
         }

         this.sequenceQueue.clear();
         this.sequenceQueue.add(var1);
         if (this.fadeThread == null) {
            this.fadeThread = new MidiChannel.FadeThread(this);
            this.fadeThread.start();
         }

         this.fadeThread.interrupt();
      } else {
         this.errorMessage("Miliseconds may not be negative in method 'fadeOutIn'.");
      }
   }

   public void play() {
      if (!this.loading()) {
         if (this.sequencer == null) {
            return;
         }

         try {
            this.sequencer.start();
            this.sequencer.addMetaEventListener(this);
         } catch (Exception var3) {
            this.errorMessage("Exception in method 'play'");
            this.printStackTrace(var3);
            SoundSystemException var2 = new SoundSystemException(var3.getMessage());
            SoundSystem.setException(var2);
         }
      }

   }

   public void stop() {
      if (!this.loading()) {
         if (this.sequencer == null) {
            return;
         }

         try {
            this.sequencer.stop();
            this.sequencer.setMicrosecondPosition(0L);
            this.sequencer.removeMetaEventListener(this);
         } catch (Exception var3) {
            this.errorMessage("Exception in method 'stop'");
            this.printStackTrace(var3);
            SoundSystemException var2 = new SoundSystemException(var3.getMessage());
            SoundSystem.setException(var2);
         }
      }

   }

   public void pause() {
      if (!this.loading()) {
         if (this.sequencer == null) {
            return;
         }

         try {
            this.sequencer.stop();
         } catch (Exception var3) {
            this.errorMessage("Exception in method 'pause'");
            this.printStackTrace(var3);
            SoundSystemException var2 = new SoundSystemException(var3.getMessage());
            SoundSystem.setException(var2);
         }
      }

   }

   public void rewind() {
      if (!this.loading()) {
         if (this.sequencer == null) {
            return;
         }

         try {
            this.sequencer.setMicrosecondPosition(0L);
         } catch (Exception var3) {
            this.errorMessage("Exception in method 'rewind'");
            this.printStackTrace(var3);
            SoundSystemException var2 = new SoundSystemException(var3.getMessage());
            SoundSystem.setException(var2);
         }
      }

   }

   public void setVolume(float var1) {
      this.gain = var1;
      this.resetGain();
   }

   public float getVolume() {
      return this.gain;
   }

   public void switchSource(boolean var1, String var2, String var3) {
      this.loading(true, true);
      this.filenameURL(true, new FilenameURL(var3));
      this.sourcename(true, var2);
      this.setLooping(var1);
      this.reset();
      this.loading(true, false);
   }

   public void switchSource(boolean var1, String var2, URL var3, String var4) {
      this.loading(true, true);
      this.filenameURL(true, new FilenameURL(var3, var4));
      this.sourcename(true, var2);
      this.setLooping(var1);
      this.reset();
      this.loading(true, false);
   }

   public void switchSource(boolean var1, String var2, FilenameURL var3) {
      this.loading(true, true);
      this.filenameURL(true, var3);
      this.sourcename(true, var2);
      this.setLooping(var1);
      this.reset();
      this.loading(true, false);
   }

   private void reset() {
      Object var1;
      synchronized(var1 = this.sequenceQueueLock){}
      if (this.sequenceQueue != null) {
         this.sequenceQueue.clear();
      }

      if (this.sequencer == null) {
         this.getSequencer();
      } else {
         this.sequencer.stop();
         this.sequencer.setMicrosecondPosition(0L);
         this.sequencer.removeMetaEventListener(this);

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var3) {
         }
      }

      if (this.sequencer == null) {
         this.errorMessage("Unable to set the sequence in method 'reset', because there wasn't a sequencer to use.");
      } else {
         this.setSequence(this.filenameURL(false, (FilenameURL)null).getURL());
         this.sequencer.start();
         this.resetGain();
         this.sequencer.addMetaEventListener(this);
      }
   }

   public void setLooping(boolean var1) {
      this.toLoop(true, var1);
   }

   public boolean getLooping() {
      return this.toLoop(false, false);
   }

   private synchronized boolean toLoop(boolean var1, boolean var2) {
      if (var1) {
         this.toLoop = var2;
      }

      return this.toLoop;
   }

   public boolean loading() {
      return this.loading(false, false);
   }

   private synchronized boolean loading(boolean var1, boolean var2) {
      if (var1) {
         this.loading = var2;
      }

      return this.loading;
   }

   public void setSourcename(String var1) {
      this.sourcename(true, var1);
   }

   public String getSourcename() {
      return this.sourcename(false, (String)null);
   }

   private synchronized String sourcename(boolean var1, String var2) {
      if (var1) {
         this.sourcename = var2;
      }

      return this.sourcename;
   }

   public void setFilenameURL(FilenameURL var1) {
      this.filenameURL(true, var1);
   }

   public String getFilename() {
      return this.filenameURL(false, (FilenameURL)null).getFilename();
   }

   public FilenameURL getFilenameURL() {
      return this.filenameURL(false, (FilenameURL)null);
   }

   private synchronized FilenameURL filenameURL(boolean var1, FilenameURL var2) {
      if (var1) {
         this.filenameURL = var2;
      }

      return this.filenameURL;
   }

   public void meta(MetaMessage var1) {
      if (var1.getType() == 47) {
         SoundSystemConfig.notifyEOS(this.sourcename, this.sequenceQueue.size());
         if (this.toLoop) {
            if (this == false) {
               if (this != null) {
                  try {
                     this.sequencer.setMicrosecondPosition(0L);
                     this.sequencer.start();
                     this.resetGain();
                  } catch (Exception var6) {
                  }
               }
            } else if (this.sequencer != null) {
               try {
                  this.sequencer.setMicrosecondPosition(0L);
                  this.sequencer.start();
                  this.resetGain();
               } catch (Exception var5) {
               }
            }
         } else if (this == false) {
            if (this != null) {
               try {
                  this.sequencer.stop();
                  this.sequencer.setMicrosecondPosition(0L);
                  this.sequencer.removeMetaEventListener(this);
               } catch (Exception var4) {
               }
            }
         } else {
            try {
               this.sequencer.stop();
               this.sequencer.setMicrosecondPosition(0L);
               this.sequencer.removeMetaEventListener(this);
            } catch (Exception var3) {
            }
         }
      }

   }

   public void resetGain() {
      if (this.gain < 0.0F) {
         this.gain = 0.0F;
      }

      if (this.gain > 1.0F) {
         this.gain = 1.0F;
      }

      int var1 = (int)(this.gain * SoundSystemConfig.getMasterGain() * Math.abs(this.fadeOutGain) * this.fadeInGain * 127.0F);
      javax.sound.midi.MidiChannel[] var2;
      int var3;
      if (this.synthesizer != null) {
         var2 = this.synthesizer.getChannels();

         for(var3 = 0; var2 != null && var3 < var2.length; ++var3) {
            var2[var3].controlChange(7, var1);
         }
      } else if (this.synthDevice != null) {
         try {
            ShortMessage var7 = new ShortMessage();

            for(var3 = 0; var3 < 16; ++var3) {
               var7.setMessage(176, var3, 7, var1);
               this.synthDevice.getReceiver().send(var7, -1L);
            }
         } catch (Exception var6) {
            this.errorMessage("Error resetting gain on MIDI device");
            this.printStackTrace(var6);
         }
      } else if (this.sequencer != null && this.sequencer instanceof Synthesizer) {
         this.synthesizer = (Synthesizer)this.sequencer;
         var2 = this.synthesizer.getChannels();

         for(var3 = 0; var2 != null && var3 < var2.length; ++var3) {
            var2[var3].controlChange(7, var1);
         }
      } else {
         try {
            Receiver var8 = MidiSystem.getReceiver();
            ShortMessage var9 = new ShortMessage();

            for(int var4 = 0; var4 < 16; ++var4) {
               var9.setMessage(176, var4, 7, var1);
               var8.send(var9, -1L);
            }
         } catch (Exception var5) {
            this.errorMessage("Error resetting gain on default receiver");
            this.printStackTrace(var5);
         }
      }

   }

   private void getSequencer() {
      try {
         this.sequencer = MidiSystem.getSequencer();
         if (this.sequencer != null) {
            try {
               this.sequencer.getTransmitter();
            } catch (MidiUnavailableException var6) {
               this.message("Unable to get a transmitter from the default MIDI sequencer");
            }

            this.sequencer.open();
         }
      } catch (MidiUnavailableException var7) {
         this.message("Unable to open the default MIDI sequencer");
         this.sequencer = null;
      } catch (Exception var8) {
         if (var8 instanceof InterruptedException) {
            this.message("Caught InterruptedException while attempting to open the default MIDI sequencer.  Trying again.");
            this.sequencer = null;
         }

         try {
            this.sequencer = MidiSystem.getSequencer();
            if (this.sequencer != null) {
               try {
                  this.sequencer.getTransmitter();
               } catch (MidiUnavailableException var3) {
                  this.message("Unable to get a transmitter from the default MIDI sequencer");
               }

               this.sequencer.open();
            }
         } catch (MidiUnavailableException var4) {
            this.message("Unable to open the default MIDI sequencer");
            this.sequencer = null;
         } catch (Exception var5) {
            this.message("Unknown error opening the default MIDI sequencer");
            this.sequencer = null;
         }
      }

      if (this.sequencer == null) {
         this.sequencer = this.openSequencer("Real Time Sequencer");
      }

      if (this.sequencer == null) {
         this.sequencer = this.openSequencer("Java Sound Sequencer");
      }

      if (this.sequencer == null) {
         this.errorMessage("Failed to find an available MIDI sequencer");
      }
   }

   private void setSequence(URL var1) {
      if (this.sequencer == null) {
         this.errorMessage("Unable to update the sequence in method 'setSequence', because variable 'sequencer' is null");
      } else if (var1 == null) {
         this.errorMessage("Unable to load Midi file in method 'setSequence'.");
      } else {
         try {
            this.sequence = MidiSystem.getSequence(var1);
         } catch (IOException var5) {
            this.errorMessage("Input failed while reading from MIDI file in method 'setSequence'.");
            this.printStackTrace(var5);
            return;
         } catch (InvalidMidiDataException var6) {
            this.errorMessage("Invalid MIDI data encountered, or not a MIDI file in method 'setSequence' (1).");
            this.printStackTrace(var6);
            return;
         }

         if (this.sequence == null) {
            this.errorMessage("MidiSystem 'getSequence' method returned null in method 'setSequence'.");
         } else {
            try {
               this.sequencer.setSequence(this.sequence);
            } catch (InvalidMidiDataException var3) {
               this.errorMessage("Invalid MIDI data encountered, or not a MIDI file in method 'setSequence' (2).");
               this.printStackTrace(var3);
               return;
            } catch (Exception var4) {
               this.errorMessage("Problem setting sequence from MIDI file in method 'setSequence'.");
               this.printStackTrace(var4);
               return;
            }
         }

      }
   }

   private void getSynthesizer() {
      if (this.sequencer == null) {
         this.errorMessage("Unable to load a Synthesizer in method 'getSynthesizer', because variable 'sequencer' is null");
      } else {
         String var1 = SoundSystemConfig.getOverrideMIDISynthesizer();
         if (var1 != null && !var1.equals("")) {
            this.synthDevice = this.openMidiDevice(var1);
            if (this.synthDevice != null) {
               try {
                  this.sequencer.getTransmitter().setReceiver(this.synthDevice.getReceiver());
                  return;
               } catch (MidiUnavailableException var7) {
                  this.errorMessage("Unable to link sequencer transmitter with receiver for MIDI device '" + var1 + "'");
               }
            }
         }

         if (this.sequencer instanceof Synthesizer) {
            this.synthesizer = (Synthesizer)this.sequencer;
         } else {
            try {
               this.synthesizer = MidiSystem.getSynthesizer();
               this.synthesizer.open();
            } catch (MidiUnavailableException var6) {
               this.message("Unable to open the default synthesizer");
               this.synthesizer = null;
            }

            if (this.synthesizer == null) {
               this.synthDevice = this.openMidiDevice("Java Sound Synthesizer");
               if (this.synthDevice == null) {
                  this.synthDevice = this.openMidiDevice("Microsoft GS Wavetable");
               }

               if (this.synthDevice == null) {
                  this.synthDevice = this.openMidiDevice("Gervill");
               }

               if (this.synthDevice == null) {
                  this.errorMessage("Failed to find an available MIDI synthesizer");
                  return;
               }
            }

            if (this.synthesizer == null) {
               try {
                  this.sequencer.getTransmitter().setReceiver(this.synthDevice.getReceiver());
               } catch (MidiUnavailableException var5) {
                  this.errorMessage("Unable to link sequencer transmitter with MIDI device receiver");
               }
            } else if (this.synthesizer.getDefaultSoundbank() == null) {
               try {
                  this.sequencer.getTransmitter().setReceiver(MidiSystem.getReceiver());
               } catch (MidiUnavailableException var4) {
                  this.errorMessage("Unable to link sequencer transmitter with default receiver");
               }
            } else {
               try {
                  this.sequencer.getTransmitter().setReceiver(this.synthesizer.getReceiver());
               } catch (MidiUnavailableException var3) {
                  this.errorMessage("Unable to link sequencer transmitter with synthesizer receiver");
               }
            }
         }

      }
   }

   private Sequencer openSequencer(String var1) {
      Sequencer var2 = null;
      var2 = (Sequencer)this.openMidiDevice(var1);
      if (var2 == null) {
         return null;
      } else {
         try {
            var2.getTransmitter();
            return var2;
         } catch (MidiUnavailableException var4) {
            this.message("    Unable to get a transmitter from this sequencer");
            var2 = null;
            return null;
         }
      }
   }

   private MidiDevice openMidiDevice(String var1) {
      this.message("Searching for MIDI device with name containing '" + var1 + "'");
      MidiDevice var2 = null;
      Info[] var3 = MidiSystem.getMidiDeviceInfo();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var2 = null;

         try {
            var2 = MidiSystem.getMidiDevice(var3[var4]);
         } catch (MidiUnavailableException var7) {
            this.message("    Problem in method 'getMidiDevice':  MIDIUnavailableException was thrown");
            var2 = null;
         }

         if (var2 != null && var3[var4].getName().contains(var1)) {
            this.message("    Found MIDI device named '" + var3[var4].getName() + "'");
            if (var2 instanceof Synthesizer) {
               this.message("        *this is a Synthesizer instance");
            }

            if (var2 instanceof Sequencer) {
               this.message("        *this is a Sequencer instance");
            }

            try {
               var2.open();
            } catch (MidiUnavailableException var6) {
               this.message("    Unable to open this MIDI device");
               var2 = null;
            }

            return var2;
         }
      }

      this.message("    MIDI device not found");
      return null;
   }

   protected void message(String var1) {
      this.logger.message(var1, 0);
   }

   protected void importantMessage(String var1) {
      this.logger.importantMessage(var1, 0);
   }

   protected boolean errorCheck(boolean var1, String var2) {
      return this.logger.errorCheck(var1, "MidiChannel", var2, 0);
   }

   protected void errorMessage(String var1) {
      this.logger.errorMessage("MidiChannel", var1, 0);
   }

   protected void printStackTrace(Exception var1) {
      this.logger.printStackTrace(var1, 1);
   }

   static boolean access$100(MidiChannel var0) {
      return var0.checkFadeOut();
   }

   private class FadeThread extends SimpleThread {
      final MidiChannel this$0;

      private FadeThread(MidiChannel var1) {
         this.this$0 = var1;
      }

      public void run() {
         while(!this.dying()) {
            if (this.this$0.fadeOutGain == -1.0F && this.this$0.fadeInGain == 1.0F) {
               this.snooze(3600000L);
            }

            MidiChannel.access$100(this.this$0);
            this.snooze(50L);
         }

         this.cleanup();
      }

      FadeThread(MidiChannel var1, Object var2) {
         this(var1);
      }
   }
}
