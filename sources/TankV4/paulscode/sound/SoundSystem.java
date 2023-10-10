package paulscode.sound;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;
import javax.sound.sampled.AudioFormat;

public class SoundSystem {
   private static final boolean GET = false;
   private static final boolean SET = true;
   private static final boolean XXX = false;
   protected SoundSystemLogger logger = SoundSystemConfig.getLogger();
   protected Library soundLibrary;
   protected List commandQueue;
   private List sourcePlayList;
   protected CommandThread commandThread;
   public Random randomNumberGenerator;
   protected String className = "SoundSystem";
   private static Class currentLibrary = null;
   private static boolean initialized = false;
   private static SoundSystemException lastException = null;

   public SoundSystem() {
      if (this.logger == null) {
         this.logger = new SoundSystemLogger();
         SoundSystemConfig.setLogger(this.logger);
      }

      this.linkDefaultLibrariesAndCodecs();
      LinkedList var1 = SoundSystemConfig.getLibraries();
      if (var1 != null) {
         ListIterator var2 = var1.listIterator();

         while(var2.hasNext()) {
            Class var3 = (Class)var2.next();

            try {
               this.init(var3);
               return;
            } catch (SoundSystemException var6) {
               this.logger.printExceptionMessage(var6, 1);
            }
         }
      }

      try {
         this.init(Library.class);
      } catch (SoundSystemException var5) {
         this.logger.printExceptionMessage(var5, 1);
      }
   }

   public SoundSystem(Class var1) throws SoundSystemException {
      if (this.logger == null) {
         this.logger = new SoundSystemLogger();
         SoundSystemConfig.setLogger(this.logger);
      }

      this.linkDefaultLibrariesAndCodecs();
      this.init(var1);
   }

   protected void linkDefaultLibrariesAndCodecs() {
   }

   protected void init(Class var1) throws SoundSystemException {
      this.message("", 0);
      this.message("Starting up " + this.className + "...", 0);
      this.randomNumberGenerator = new Random();
      this.commandQueue = new LinkedList();
      this.sourcePlayList = new LinkedList();
      this.commandThread = new CommandThread(this);
      this.commandThread.start();
      snooze(200L);
      this.newLibrary(var1);
      this.message("", 0);
   }

   public void cleanup() {
      boolean var1 = false;
      this.message("", 0);
      this.message(this.className + " shutting down...", 0);

      try {
         this.commandThread.kill();
         this.commandThread.interrupt();
      } catch (Exception var6) {
         var1 = true;
      }

      if (!var1) {
         for(int var2 = 0; var2 < 50 && this.commandThread.alive(); ++var2) {
            snooze(100L);
         }
      }

      if (var1 || this.commandThread.alive()) {
         this.errorMessage("Command thread did not die!", 0);
         this.message("Ignoring errors... continuing clean-up.", 0);
      }

      initialized(true, false);
      currentLibrary(true, (Class)null);

      try {
         if (this.soundLibrary != null) {
            this.soundLibrary.cleanup();
         }
      } catch (Exception var5) {
         this.errorMessage("Problem during Library.cleanup()!", 0);
         this.message("Ignoring errors... continuing clean-up.", 0);
      }

      try {
         if (this.commandQueue != null) {
            this.commandQueue.clear();
         }
      } catch (Exception var4) {
         this.errorMessage("Unable to clear the command queue!", 0);
         this.message("Ignoring errors... continuing clean-up.", 0);
      }

      try {
         if (this.sourcePlayList != null) {
            this.sourcePlayList.clear();
         }
      } catch (Exception var3) {
         this.errorMessage("Unable to clear the source management list!", 0);
         this.message("Ignoring errors... continuing clean-up.", 0);
      }

      this.randomNumberGenerator = null;
      this.soundLibrary = null;
      this.commandQueue = null;
      this.sourcePlayList = null;
      this.commandThread = null;
      this.importantMessage("Author: Paul Lamb, www.paulscode.com", 1);
      this.message("", 0);
   }

   public void interruptCommandThread() {
      if (this.commandThread == null) {
         this.errorMessage("Command Thread null in method 'interruptCommandThread'", 0);
      } else {
         this.commandThread.interrupt();
      }
   }

   public void loadSound(String var1) {
      this.CommandQueue(new CommandObject(2, new FilenameURL(var1)));
      this.commandThread.interrupt();
   }

   public void loadSound(URL var1, String var2) {
      this.CommandQueue(new CommandObject(2, new FilenameURL(var1, var2)));
      this.commandThread.interrupt();
   }

   public void loadSound(byte[] var1, AudioFormat var2, String var3) {
      this.CommandQueue(new CommandObject(3, var3, new SoundBuffer(var1, var2)));
      this.commandThread.interrupt();
   }

   public void unloadSound(String var1) {
      this.CommandQueue(new CommandObject(4, var1));
      this.commandThread.interrupt();
   }

   public void queueSound(String var1, String var2) {
      this.CommandQueue(new CommandObject(5, var1, new FilenameURL(var2)));
      this.commandThread.interrupt();
   }

   public void queueSound(String var1, URL var2, String var3) {
      this.CommandQueue(new CommandObject(5, var1, new FilenameURL(var2, var3)));
      this.commandThread.interrupt();
   }

   public void dequeueSound(String var1, String var2) {
      this.CommandQueue(new CommandObject(6, var1, var2));
      this.commandThread.interrupt();
   }

   public void fadeOut(String var1, String var2, long var3) {
      FilenameURL var5 = null;
      if (var2 != null) {
         var5 = new FilenameURL(var2);
      }

      this.CommandQueue(new CommandObject(7, var1, var5, var3));
      this.commandThread.interrupt();
   }

   public void fadeOut(String var1, URL var2, String var3, long var4) {
      FilenameURL var6 = null;
      if (var2 != null && var3 != null) {
         var6 = new FilenameURL(var2, var3);
      }

      this.CommandQueue(new CommandObject(7, var1, var6, var4));
      this.commandThread.interrupt();
   }

   public void fadeOutIn(String var1, String var2, long var3, long var5) {
      this.CommandQueue(new CommandObject(8, var1, new FilenameURL(var2), var3, var5));
      this.commandThread.interrupt();
   }

   public void fadeOutIn(String var1, URL var2, String var3, long var4, long var6) {
      this.CommandQueue(new CommandObject(8, var1, new FilenameURL(var2, var3), var4, var6));
      this.commandThread.interrupt();
   }

   public void checkFadeVolumes() {
      this.CommandQueue(new CommandObject(9));
      this.commandThread.interrupt();
   }

   public void backgroundMusic(String var1, String var2, boolean var3) {
      this.CommandQueue(new CommandObject(12, true, true, var3, var1, new FilenameURL(var2), 0.0F, 0.0F, 0.0F, 0, 0.0F, false));
      this.CommandQueue(new CommandObject(24, var1));
      this.commandThread.interrupt();
   }

   public void backgroundMusic(String var1, URL var2, String var3, boolean var4) {
      this.CommandQueue(new CommandObject(12, true, true, var4, var1, new FilenameURL(var2, var3), 0.0F, 0.0F, 0.0F, 0, 0.0F, false));
      this.CommandQueue(new CommandObject(24, var1));
      this.commandThread.interrupt();
   }

   public void newSource(boolean var1, String var2, String var3, boolean var4, float var5, float var6, float var7, int var8, float var9) {
      this.CommandQueue(new CommandObject(10, var1, false, var4, var2, new FilenameURL(var3), var5, var6, var7, var8, var9));
      this.commandThread.interrupt();
   }

   public void newSource(boolean var1, String var2, URL var3, String var4, boolean var5, float var6, float var7, float var8, int var9, float var10) {
      this.CommandQueue(new CommandObject(10, var1, false, var5, var2, new FilenameURL(var3, var4), var6, var7, var8, var9, var10));
      this.commandThread.interrupt();
   }

   public void newStreamingSource(boolean var1, String var2, String var3, boolean var4, float var5, float var6, float var7, int var8, float var9) {
      this.CommandQueue(new CommandObject(10, var1, true, var4, var2, new FilenameURL(var3), var5, var6, var7, var8, var9));
      this.commandThread.interrupt();
   }

   public void newStreamingSource(boolean var1, String var2, URL var3, String var4, boolean var5, float var6, float var7, float var8, int var9, float var10) {
      this.CommandQueue(new CommandObject(10, var1, true, var5, var2, new FilenameURL(var3, var4), var6, var7, var8, var9, var10));
      this.commandThread.interrupt();
   }

   public void rawDataStream(AudioFormat var1, boolean var2, String var3, float var4, float var5, float var6, int var7, float var8) {
      this.CommandQueue(new CommandObject(11, var1, var2, var3, var4, var5, var6, var7, var8));
      this.commandThread.interrupt();
   }

   public String quickPlay(boolean var1, String var2, boolean var3, float var4, float var5, float var6, int var7, float var8) {
      String var9 = "Source_" + this.randomNumberGenerator.nextInt() + "_" + this.randomNumberGenerator.nextInt();
      this.CommandQueue(new CommandObject(12, var1, false, var3, var9, new FilenameURL(var2), var4, var5, var6, var7, var8, true));
      this.CommandQueue(new CommandObject(24, var9));
      this.commandThread.interrupt();
      return var9;
   }

   public String quickPlay(boolean var1, URL var2, String var3, boolean var4, float var5, float var6, float var7, int var8, float var9) {
      String var10 = "Source_" + this.randomNumberGenerator.nextInt() + "_" + this.randomNumberGenerator.nextInt();
      this.CommandQueue(new CommandObject(12, var1, false, var4, var10, new FilenameURL(var2, var3), var5, var6, var7, var8, var9, true));
      this.CommandQueue(new CommandObject(24, var10));
      this.commandThread.interrupt();
      return var10;
   }

   public String quickStream(boolean var1, String var2, boolean var3, float var4, float var5, float var6, int var7, float var8) {
      String var9 = "Source_" + this.randomNumberGenerator.nextInt() + "_" + this.randomNumberGenerator.nextInt();
      this.CommandQueue(new CommandObject(12, var1, true, var3, var9, new FilenameURL(var2), var4, var5, var6, var7, var8, true));
      this.CommandQueue(new CommandObject(24, var9));
      this.commandThread.interrupt();
      return var9;
   }

   public String quickStream(boolean var1, URL var2, String var3, boolean var4, float var5, float var6, float var7, int var8, float var9) {
      String var10 = "Source_" + this.randomNumberGenerator.nextInt() + "_" + this.randomNumberGenerator.nextInt();
      this.CommandQueue(new CommandObject(12, var1, true, var4, var10, new FilenameURL(var2, var3), var5, var6, var7, var8, var9, true));
      this.CommandQueue(new CommandObject(24, var10));
      this.commandThread.interrupt();
      return var10;
   }

   public void setPosition(String var1, float var2, float var3, float var4) {
      this.CommandQueue(new CommandObject(13, var1, var2, var3, var4));
      this.commandThread.interrupt();
   }

   public void setVolume(String var1, float var2) {
      this.CommandQueue(new CommandObject(14, var1, var2));
      this.commandThread.interrupt();
   }

   public float getVolume(String var1) {
      Object var2;
      synchronized(var2 = SoundSystemConfig.THREAD_SYNC){}
      return this.soundLibrary != null ? this.soundLibrary.getVolume(var1) : 0.0F;
   }

   public void setPitch(String var1, float var2) {
      this.CommandQueue(new CommandObject(15, var1, var2));
      this.commandThread.interrupt();
   }

   public float getPitch(String var1) {
      return this.soundLibrary != null ? this.soundLibrary.getPitch(var1) : 1.0F;
   }

   public void setPriority(String var1, boolean var2) {
      this.CommandQueue(new CommandObject(16, var1, var2));
      this.commandThread.interrupt();
   }

   public void setLooping(String var1, boolean var2) {
      this.CommandQueue(new CommandObject(17, var1, var2));
      this.commandThread.interrupt();
   }

   public void setAttenuation(String var1, int var2) {
      this.CommandQueue(new CommandObject(18, var1, var2));
      this.commandThread.interrupt();
   }

   public void setDistOrRoll(String var1, float var2) {
      this.CommandQueue(new CommandObject(19, var1, var2));
      this.commandThread.interrupt();
   }

   public void changeDopplerFactor(float var1) {
      this.CommandQueue(new CommandObject(20, var1));
      this.commandThread.interrupt();
   }

   public void changeDopplerVelocity(float var1) {
      this.CommandQueue(new CommandObject(21, var1));
      this.commandThread.interrupt();
   }

   public void setVelocity(String var1, float var2, float var3, float var4) {
      this.CommandQueue(new CommandObject(22, var1, var2, var3, var4));
      this.commandThread.interrupt();
   }

   public void setListenerVelocity(float var1, float var2, float var3) {
      this.CommandQueue(new CommandObject(23, var1, var2, var3));
      this.commandThread.interrupt();
   }

   public float millisecondsPlayed(String var1) {
      Object var2;
      synchronized(var2 = SoundSystemConfig.THREAD_SYNC){}
      return this.soundLibrary.millisecondsPlayed(var1);
   }

   public void feedRawAudioData(String var1, byte[] var2) {
      this.CommandQueue(new CommandObject(25, var1, var2));
      this.commandThread.interrupt();
   }

   public void play(String var1) {
      this.CommandQueue(new CommandObject(24, var1));
      this.commandThread.interrupt();
   }

   public void pause(String var1) {
      this.CommandQueue(new CommandObject(26, var1));
      this.commandThread.interrupt();
   }

   public void stop(String var1) {
      this.CommandQueue(new CommandObject(27, var1));
      this.commandThread.interrupt();
   }

   public void rewind(String var1) {
      this.CommandQueue(new CommandObject(28, var1));
      this.commandThread.interrupt();
   }

   public void flush(String var1) {
      this.CommandQueue(new CommandObject(29, var1));
      this.commandThread.interrupt();
   }

   public void cull(String var1) {
      this.CommandQueue(new CommandObject(30, var1));
      this.commandThread.interrupt();
   }

   public void activate(String var1) {
      this.CommandQueue(new CommandObject(31, var1));
      this.commandThread.interrupt();
   }

   public void setTemporary(String var1, boolean var2) {
      this.CommandQueue(new CommandObject(32, var1, var2));
      this.commandThread.interrupt();
   }

   public void removeSource(String var1) {
      this.CommandQueue(new CommandObject(33, var1));
      this.commandThread.interrupt();
   }

   public void moveListener(float var1, float var2, float var3) {
      this.CommandQueue(new CommandObject(34, var1, var2, var3));
      this.commandThread.interrupt();
   }

   public void setListenerPosition(float var1, float var2, float var3) {
      this.CommandQueue(new CommandObject(35, var1, var2, var3));
      this.commandThread.interrupt();
   }

   public void turnListener(float var1) {
      this.CommandQueue(new CommandObject(36, var1));
      this.commandThread.interrupt();
   }

   public void setListenerAngle(float var1) {
      this.CommandQueue(new CommandObject(37, var1));
      this.commandThread.interrupt();
   }

   public void setListenerOrientation(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.CommandQueue(new CommandObject(38, var1, var2, var3, var4, var5, var6));
      this.commandThread.interrupt();
   }

   public void setMasterVolume(float var1) {
      this.CommandQueue(new CommandObject(39, var1));
      this.commandThread.interrupt();
   }

   public float getMasterVolume() {
      return SoundSystemConfig.getMasterGain();
   }

   public ListenerData getListenerData() {
      Object var1;
      synchronized(var1 = SoundSystemConfig.THREAD_SYNC){}
      return this.soundLibrary.getListenerData();
   }

   public boolean switchLibrary(Class var1) throws SoundSystemException {
      Object var2;
      synchronized(var2 = SoundSystemConfig.THREAD_SYNC){}
      initialized(true, false);
      HashMap var3 = null;
      ListenerData var4 = null;
      boolean var5 = false;
      MidiChannel var6 = null;
      FilenameURL var7 = null;
      String var8 = "";
      boolean var9 = true;
      if (this.soundLibrary != null) {
         currentLibrary(true, (Class)null);
         var3 = this.copySources(this.soundLibrary.getSources());
         var4 = this.soundLibrary.getListenerData();
         var6 = this.soundLibrary.getMidiChannel();
         if (var6 != null) {
            var5 = true;
            var9 = var6.getLooping();
            var8 = var6.getSourcename();
            var7 = var6.getFilenameURL();
         }

         this.soundLibrary.cleanup();
         this.soundLibrary = null;
      }

      this.message("", 0);
      this.message("Switching to " + SoundSystemConfig.getLibraryTitle(var1), 0);
      this.message("(" + SoundSystemConfig.getLibraryDescription(var1) + ")", 1);

      try {
         this.soundLibrary = (Library)var1.newInstance();
      } catch (InstantiationException var13) {
         this.errorMessage("The specified library did not load properly", 1);
      } catch (IllegalAccessException var14) {
         this.errorMessage("The specified library did not load properly", 1);
      } catch (ExceptionInInitializerError var15) {
         this.errorMessage("The specified library did not load properly", 1);
      } catch (SecurityException var16) {
         this.errorMessage("The specified library did not load properly", 1);
      }

      if (this.errorCheck(this.soundLibrary == null, "Library null after initialization in method 'switchLibrary'", 1)) {
         SoundSystemException var10 = new SoundSystemException(this.className + " did not load properly.  " + "Library was null after initialization.", 4);
         lastException(true, var10);
         initialized(true, true);
         throw var10;
      } else {
         try {
            this.soundLibrary.init();
         } catch (SoundSystemException var12) {
            lastException(true, var12);
            initialized(true, true);
            throw var12;
         }

         this.soundLibrary.setListenerData(var4);
         if (var5) {
            if (var6 != null) {
               var6.cleanup();
            }

            var6 = new MidiChannel(var9, var8, var7);
            this.soundLibrary.setMidiChannel(var6);
         }

         this.soundLibrary.copySources(var3);
         this.message("", 0);
         lastException(true, (SoundSystemException)null);
         initialized(true, true);
         return true;
      }
   }

   public boolean newLibrary(Class var1) throws SoundSystemException {
      initialized(true, false);
      this.CommandQueue(new CommandObject(40, var1));
      this.commandThread.interrupt();

      for(int var2 = 0; var2 < 100; ++var2) {
         snooze(400L);
         this.commandThread.interrupt();
      }

      SoundSystemException var3 = new SoundSystemException(this.className + " did not load after 30 seconds.", 4);
      lastException(true, var3);
      throw var3;
   }

   private void CommandNewLibrary(Class var1) {
      initialized(true, false);
      String var2 = "Initializing ";
      if (this.soundLibrary != null) {
         currentLibrary(true, (Class)null);
         var2 = "Switching to ";
         this.soundLibrary.cleanup();
         this.soundLibrary = null;
      }

      this.message(var2 + SoundSystemConfig.getLibraryTitle(var1), 0);
      this.message("(" + SoundSystemConfig.getLibraryDescription(var1) + ")", 1);

      try {
         this.soundLibrary = (Library)var1.newInstance();
      } catch (InstantiationException var6) {
         this.errorMessage("The specified library did not load properly", 1);
      } catch (IllegalAccessException var7) {
         this.errorMessage("The specified library did not load properly", 1);
      } catch (ExceptionInInitializerError var8) {
         this.errorMessage("The specified library did not load properly", 1);
      } catch (SecurityException var9) {
         this.errorMessage("The specified library did not load properly", 1);
      }

      if (this.errorCheck(this.soundLibrary == null, "Library null after initialization in method 'newLibrary'", 1)) {
         lastException(true, new SoundSystemException(this.className + " did not load properly.  " + "Library was null after initialization.", 4));
         this.importantMessage("Switching to silent mode", 1);

         try {
            this.soundLibrary = new Library();
         } catch (SoundSystemException var5) {
            lastException(true, new SoundSystemException("Silent mode did not load properly.  Library was null after initialization.", 4));
            initialized(true, true);
            return;
         }
      }

      try {
         this.soundLibrary.init();
      } catch (SoundSystemException var4) {
         lastException(true, var4);
         initialized(true, true);
         return;
      }

      lastException(true, (SoundSystemException)null);
      initialized(true, true);
   }

   private void CommandInitialize() {
      try {
         if (this.errorCheck(this.soundLibrary == null, "Library null after initialization in method 'CommandInitialize'", 1)) {
            SoundSystemException var1 = new SoundSystemException(this.className + " did not load properly.  " + "Library was null after initialization.", 4);
            lastException(true, var1);
            throw var1;
         }

         this.soundLibrary.init();
      } catch (SoundSystemException var2) {
         lastException(true, var2);
         initialized(true, true);
      }

   }

   private void CommandLoadSound(FilenameURL var1) {
      if (this.soundLibrary != null) {
         this.soundLibrary.loadSound(var1);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandLoadSound'", 0);
      }

   }

   private void CommandLoadSound(SoundBuffer var1, String var2) {
      if (this.soundLibrary != null) {
         this.soundLibrary.loadSound(var1, var2);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandLoadSound'", 0);
      }

   }

   private void CommandUnloadSound(String var1) {
      if (this.soundLibrary != null) {
         this.soundLibrary.unloadSound(var1);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandLoadSound'", 0);
      }

   }

   private void CommandQueueSound(String var1, FilenameURL var2) {
      if (this.soundLibrary != null) {
         this.soundLibrary.queueSound(var1, var2);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandQueueSound'", 0);
      }

   }

   private void CommandDequeueSound(String var1, String var2) {
      if (this.soundLibrary != null) {
         this.soundLibrary.dequeueSound(var1, var2);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandDequeueSound'", 0);
      }

   }

   private void CommandFadeOut(String var1, FilenameURL var2, long var3) {
      if (this.soundLibrary != null) {
         this.soundLibrary.fadeOut(var1, var2, var3);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandFadeOut'", 0);
      }

   }

   private void CommandFadeOutIn(String var1, FilenameURL var2, long var3, long var5) {
      if (this.soundLibrary != null) {
         this.soundLibrary.fadeOutIn(var1, var2, var3, var5);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandFadeOutIn'", 0);
      }

   }

   private void CommandCheckFadeVolumes() {
      if (this.soundLibrary != null) {
         this.soundLibrary.checkFadeVolumes();
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandCheckFadeVolumes'", 0);
      }

   }

   private void CommandNewSource(boolean var1, boolean var2, boolean var3, String var4, FilenameURL var5, float var6, float var7, float var8, int var9, float var10) {
      if (this.soundLibrary != null) {
         if (var5.getFilename().matches(SoundSystemConfig.EXTENSION_MIDI) && !SoundSystemConfig.midiCodec()) {
            this.soundLibrary.loadMidi(var3, var4, var5);
         } else {
            this.soundLibrary.newSource(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
         }
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandNewSource'", 0);
      }

   }

   private void CommandRawDataStream(AudioFormat var1, boolean var2, String var3, float var4, float var5, float var6, int var7, float var8) {
      if (this.soundLibrary != null) {
         this.soundLibrary.rawDataStream(var1, var2, var3, var4, var5, var6, var7, var8);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandRawDataStream'", 0);
      }

   }

   private void CommandQuickPlay(boolean var1, boolean var2, boolean var3, String var4, FilenameURL var5, float var6, float var7, float var8, int var9, float var10, boolean var11) {
      if (this.soundLibrary != null) {
         if (var5.getFilename().matches(SoundSystemConfig.EXTENSION_MIDI) && !SoundSystemConfig.midiCodec()) {
            this.soundLibrary.loadMidi(var3, var4, var5);
         } else {
            this.soundLibrary.quickPlay(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
         }
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandQuickPlay'", 0);
      }

   }

   private void CommandSetPosition(String var1, float var2, float var3, float var4) {
      if (this.soundLibrary != null) {
         this.soundLibrary.setPosition(var1, var2, var3, var4);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandMoveSource'", 0);
      }

   }

   private void CommandSetVolume(String var1, float var2) {
      if (this.soundLibrary != null) {
         this.soundLibrary.setVolume(var1, var2);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetVolume'", 0);
      }

   }

   private void CommandSetPitch(String var1, float var2) {
      if (this.soundLibrary != null) {
         this.soundLibrary.setPitch(var1, var2);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetPitch'", 0);
      }

   }

   private void CommandSetPriority(String var1, boolean var2) {
      if (this.soundLibrary != null) {
         this.soundLibrary.setPriority(var1, var2);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetPriority'", 0);
      }

   }

   private void CommandSetLooping(String var1, boolean var2) {
      if (this.soundLibrary != null) {
         this.soundLibrary.setLooping(var1, var2);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetLooping'", 0);
      }

   }

   private void CommandSetAttenuation(String var1, int var2) {
      if (this.soundLibrary != null) {
         this.soundLibrary.setAttenuation(var1, var2);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetAttenuation'", 0);
      }

   }

   private void CommandSetDistOrRoll(String var1, float var2) {
      if (this.soundLibrary != null) {
         this.soundLibrary.setDistOrRoll(var1, var2);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetDistOrRoll'", 0);
      }

   }

   private void CommandChangeDopplerFactor(float var1) {
      if (this.soundLibrary != null) {
         SoundSystemConfig.setDopplerFactor(var1);
         this.soundLibrary.dopplerChanged();
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetDopplerFactor'", 0);
      }

   }

   private void CommandChangeDopplerVelocity(float var1) {
      if (this.soundLibrary != null) {
         SoundSystemConfig.setDopplerVelocity(var1);
         this.soundLibrary.dopplerChanged();
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetDopplerFactor'", 0);
      }

   }

   private void CommandSetVelocity(String var1, float var2, float var3, float var4) {
      if (this.soundLibrary != null) {
         this.soundLibrary.setVelocity(var1, var2, var3, var4);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandVelocity'", 0);
      }

   }

   private void CommandSetListenerVelocity(float var1, float var2, float var3) {
      if (this.soundLibrary != null) {
         this.soundLibrary.setListenerVelocity(var1, var2, var3);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetListenerVelocity'", 0);
      }

   }

   private void CommandPlay(String var1) {
      if (this.soundLibrary != null) {
         this.soundLibrary.play(var1);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandPlay'", 0);
      }

   }

   private void CommandFeedRawAudioData(String var1, byte[] var2) {
      if (this.soundLibrary != null) {
         this.soundLibrary.feedRawAudioData(var1, var2);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandFeedRawAudioData'", 0);
      }

   }

   private void CommandPause(String var1) {
      if (this.soundLibrary != null) {
         this.soundLibrary.pause(var1);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandPause'", 0);
      }

   }

   private void CommandStop(String var1) {
      if (this.soundLibrary != null) {
         this.soundLibrary.stop(var1);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandStop'", 0);
      }

   }

   private void CommandRewind(String var1) {
      if (this.soundLibrary != null) {
         this.soundLibrary.rewind(var1);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandRewind'", 0);
      }

   }

   private void CommandFlush(String var1) {
      if (this.soundLibrary != null) {
         this.soundLibrary.flush(var1);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandFlush'", 0);
      }

   }

   private void CommandSetTemporary(String var1, boolean var2) {
      if (this.soundLibrary != null) {
         this.soundLibrary.setTemporary(var1, var2);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetActive'", 0);
      }

   }

   private void CommandRemoveSource(String var1) {
      if (this.soundLibrary != null) {
         this.soundLibrary.removeSource(var1);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandRemoveSource'", 0);
      }

   }

   private void CommandMoveListener(float var1, float var2, float var3) {
      if (this.soundLibrary != null) {
         this.soundLibrary.moveListener(var1, var2, var3);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandMoveListener'", 0);
      }

   }

   private void CommandSetListenerPosition(float var1, float var2, float var3) {
      if (this.soundLibrary != null) {
         this.soundLibrary.setListenerPosition(var1, var2, var3);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetListenerPosition'", 0);
      }

   }

   private void CommandTurnListener(float var1) {
      if (this.soundLibrary != null) {
         this.soundLibrary.turnListener(var1);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandTurnListener'", 0);
      }

   }

   private void CommandSetListenerAngle(float var1) {
      if (this.soundLibrary != null) {
         this.soundLibrary.setListenerAngle(var1);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetListenerAngle'", 0);
      }

   }

   private void CommandSetListenerOrientation(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (this.soundLibrary != null) {
         this.soundLibrary.setListenerOrientation(var1, var2, var3, var4, var5, var6);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetListenerOrientation'", 0);
      }

   }

   private void CommandCull(String var1) {
      if (this.soundLibrary != null) {
         this.soundLibrary.cull(var1);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandCull'", 0);
      }

   }

   private void CommandActivate(String var1) {
      if (this.soundLibrary != null) {
         this.soundLibrary.activate(var1);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandActivate'", 0);
      }

   }

   private void CommandSetMasterVolume(float var1) {
      if (this.soundLibrary != null) {
         this.soundLibrary.setMasterVolume(var1);
      } else {
         this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetMasterVolume'", 0);
      }

   }

   protected void ManageSources() {
   }

   public boolean CommandQueue(CommandObject var1) {
      Object var2;
      synchronized(var2 = SoundSystemConfig.THREAD_SYNC){}
      if (var1 != null) {
         if (this.commandQueue == null) {
            return false;
         } else {
            this.commandQueue.add(var1);
            return true;
         }
      } else {
         boolean var3 = false;

         CommandObject var4;
         while(this.commandQueue != null && this.commandQueue.size() > 0) {
            var4 = (CommandObject)this.commandQueue.remove(0);
            if (var4 != null) {
               switch(var4.Command) {
               case 1:
                  this.CommandInitialize();
                  break;
               case 2:
                  this.CommandLoadSound((FilenameURL)var4.objectArgs[0]);
                  break;
               case 3:
                  this.CommandLoadSound((SoundBuffer)var4.objectArgs[0], var4.stringArgs[0]);
                  break;
               case 4:
                  this.CommandUnloadSound(var4.stringArgs[0]);
                  break;
               case 5:
                  this.CommandQueueSound(var4.stringArgs[0], (FilenameURL)var4.objectArgs[0]);
                  break;
               case 6:
                  this.CommandDequeueSound(var4.stringArgs[0], var4.stringArgs[1]);
                  break;
               case 7:
                  this.CommandFadeOut(var4.stringArgs[0], (FilenameURL)var4.objectArgs[0], var4.longArgs[0]);
                  break;
               case 8:
                  this.CommandFadeOutIn(var4.stringArgs[0], (FilenameURL)var4.objectArgs[0], var4.longArgs[0], var4.longArgs[1]);
                  break;
               case 9:
                  this.CommandCheckFadeVolumes();
                  break;
               case 10:
                  this.CommandNewSource(var4.boolArgs[0], var4.boolArgs[1], var4.boolArgs[2], var4.stringArgs[0], (FilenameURL)var4.objectArgs[0], var4.floatArgs[0], var4.floatArgs[1], var4.floatArgs[2], var4.intArgs[0], var4.floatArgs[3]);
                  break;
               case 11:
                  this.CommandRawDataStream((AudioFormat)var4.objectArgs[0], var4.boolArgs[0], var4.stringArgs[0], var4.floatArgs[0], var4.floatArgs[1], var4.floatArgs[2], var4.intArgs[0], var4.floatArgs[3]);
                  break;
               case 12:
                  this.CommandQuickPlay(var4.boolArgs[0], var4.boolArgs[1], var4.boolArgs[2], var4.stringArgs[0], (FilenameURL)var4.objectArgs[0], var4.floatArgs[0], var4.floatArgs[1], var4.floatArgs[2], var4.intArgs[0], var4.floatArgs[3], var4.boolArgs[3]);
                  break;
               case 13:
                  this.CommandSetPosition(var4.stringArgs[0], var4.floatArgs[0], var4.floatArgs[1], var4.floatArgs[2]);
                  break;
               case 14:
                  this.CommandSetVolume(var4.stringArgs[0], var4.floatArgs[0]);
                  break;
               case 15:
                  this.CommandSetPitch(var4.stringArgs[0], var4.floatArgs[0]);
                  break;
               case 16:
                  this.CommandSetPriority(var4.stringArgs[0], var4.boolArgs[0]);
                  break;
               case 17:
                  this.CommandSetLooping(var4.stringArgs[0], var4.boolArgs[0]);
                  break;
               case 18:
                  this.CommandSetAttenuation(var4.stringArgs[0], var4.intArgs[0]);
                  break;
               case 19:
                  this.CommandSetDistOrRoll(var4.stringArgs[0], var4.floatArgs[0]);
                  break;
               case 20:
                  this.CommandChangeDopplerFactor(var4.floatArgs[0]);
                  break;
               case 21:
                  this.CommandChangeDopplerVelocity(var4.floatArgs[0]);
                  break;
               case 22:
                  this.CommandSetVelocity(var4.stringArgs[0], var4.floatArgs[0], var4.floatArgs[1], var4.floatArgs[2]);
                  break;
               case 23:
                  this.CommandSetListenerVelocity(var4.floatArgs[0], var4.floatArgs[1], var4.floatArgs[2]);
                  break;
               case 24:
                  this.sourcePlayList.add(var4);
                  break;
               case 25:
                  this.sourcePlayList.add(var4);
                  break;
               case 26:
                  this.CommandPause(var4.stringArgs[0]);
                  break;
               case 27:
                  this.CommandStop(var4.stringArgs[0]);
                  break;
               case 28:
                  this.CommandRewind(var4.stringArgs[0]);
                  break;
               case 29:
                  this.CommandFlush(var4.stringArgs[0]);
                  break;
               case 30:
                  this.CommandCull(var4.stringArgs[0]);
                  break;
               case 31:
                  var3 = true;
                  this.CommandActivate(var4.stringArgs[0]);
                  break;
               case 32:
                  this.CommandSetTemporary(var4.stringArgs[0], var4.boolArgs[0]);
                  break;
               case 33:
                  this.CommandRemoveSource(var4.stringArgs[0]);
                  break;
               case 34:
                  this.CommandMoveListener(var4.floatArgs[0], var4.floatArgs[1], var4.floatArgs[2]);
                  break;
               case 35:
                  this.CommandSetListenerPosition(var4.floatArgs[0], var4.floatArgs[1], var4.floatArgs[2]);
                  break;
               case 36:
                  this.CommandTurnListener(var4.floatArgs[0]);
                  break;
               case 37:
                  this.CommandSetListenerAngle(var4.floatArgs[0]);
                  break;
               case 38:
                  this.CommandSetListenerOrientation(var4.floatArgs[0], var4.floatArgs[1], var4.floatArgs[2], var4.floatArgs[3], var4.floatArgs[4], var4.floatArgs[5]);
                  break;
               case 39:
                  this.CommandSetMasterVolume(var4.floatArgs[0]);
                  break;
               case 40:
                  this.CommandNewLibrary(var4.classArgs[0]);
               }
            }
         }

         if (var3) {
            this.soundLibrary.replaySources();
         }

         while(this.sourcePlayList != null && this.sourcePlayList.size() > 0) {
            var4 = (CommandObject)this.sourcePlayList.remove(0);
            if (var4 != null) {
               switch(var4.Command) {
               case 24:
                  this.CommandPlay(var4.stringArgs[0]);
                  break;
               case 25:
                  this.CommandFeedRawAudioData(var4.stringArgs[0], var4.buffer);
               }
            }
         }

         return this.commandQueue != null && this.commandQueue.size() > 0;
      }
   }

   public void removeTemporarySources() {
      Object var1;
      synchronized(var1 = SoundSystemConfig.THREAD_SYNC){}
      if (this.soundLibrary != null) {
         this.soundLibrary.removeTemporarySources();
      }

   }

   public boolean playing(String var1) {
      Object var2;
      synchronized(var2 = SoundSystemConfig.THREAD_SYNC){}
      if (this.soundLibrary == null) {
         return false;
      } else {
         Source var3 = (Source)this.soundLibrary.getSources().get(var1);
         return var3 == null ? false : var3.playing();
      }
   }

   public boolean playing() {
      Object var1;
      synchronized(var1 = SoundSystemConfig.THREAD_SYNC){}
      if (this.soundLibrary == null) {
         return false;
      } else {
         HashMap var2 = this.soundLibrary.getSources();
         if (var2 == null) {
            return false;
         } else {
            Set var3 = var2.keySet();
            Iterator var4 = var3.iterator();

            Source var6;
            do {
               if (!var4.hasNext()) {
                  return false;
               }

               String var5 = (String)var4.next();
               var6 = (Source)var2.get(var5);
            } while(var6 == null || !var6.playing());

            return true;
         }
      }
   }

   private HashMap copySources(HashMap var1) {
      Set var2 = var1.keySet();
      Iterator var3 = var2.iterator();
      HashMap var6 = new HashMap();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         Source var5 = (Source)var1.get(var4);
         if (var5 != null) {
            var6.put(var4, new Source(var5, (SoundBuffer)null));
         }
      }

      return var6;
   }

   public static boolean libraryCompatible(Class var0) {
      SoundSystemLogger var1 = SoundSystemConfig.getLogger();
      if (var1 == null) {
         var1 = new SoundSystemLogger();
         SoundSystemConfig.setLogger(var1);
      }

      var1.message("", 0);
      var1.message("Checking if " + SoundSystemConfig.getLibraryTitle(var0) + " is compatible...", 0);
      boolean var2 = SoundSystemConfig.libraryCompatible(var0);
      if (var2) {
         var1.message("...yes", 1);
      } else {
         var1.message("...no", 1);
      }

      return var2;
   }

   public static Class currentLibrary() {
      return currentLibrary(false, (Class)null);
   }

   public static boolean initialized() {
      return initialized(false, false);
   }

   public static SoundSystemException getLastException() {
      return lastException(false, (SoundSystemException)null);
   }

   public static void setException(SoundSystemException var0) {
      lastException(true, var0);
   }

   private static Class currentLibrary(boolean var0, Class var1) {
      Object var2;
      synchronized(var2 = SoundSystemConfig.THREAD_SYNC){}
      if (var0) {
         currentLibrary = var1;
      }

      return currentLibrary;
   }

   private static SoundSystemException lastException(boolean var0, SoundSystemException var1) {
      Object var2;
      synchronized(var2 = SoundSystemConfig.THREAD_SYNC){}
      if (var0) {
         lastException = var1;
      }

      return lastException;
   }

   protected static void snooze(long var0) {
      try {
         Thread.sleep(var0);
      } catch (InterruptedException var3) {
      }

   }

   protected void message(String var1, int var2) {
      this.logger.message(var1, var2);
   }

   protected void importantMessage(String var1, int var2) {
      this.logger.importantMessage(var1, var2);
   }

   protected boolean errorCheck(boolean var1, String var2, int var3) {
      return this.logger.errorCheck(var1, this.className, var2, var3);
   }

   protected void errorMessage(String var1, int var2) {
      this.logger.errorMessage(this.className, var1, var2);
   }
}
