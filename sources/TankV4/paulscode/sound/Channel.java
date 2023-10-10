package paulscode.sound;

import java.util.LinkedList;
import javax.sound.sampled.AudioFormat;

public class Channel {
   protected Class libraryType = Library.class;
   public int channelType;
   private SoundSystemLogger logger = SoundSystemConfig.getLogger();
   public Source attachedSource = null;
   public int buffersUnqueued = 0;

   public Channel(int var1) {
      this.channelType = var1;
   }

   public void cleanup() {
      this.logger = null;
   }

   public boolean preLoadBuffers(LinkedList var1) {
      return true;
   }

   public boolean queueBuffer(byte[] var1) {
      return false;
   }

   public int feedRawAudioData(byte[] var1) {
      return 1;
   }

   public int buffersProcessed() {
      return 0;
   }

   public float millisecondsPlayed() {
      return -1.0F;
   }

   public boolean processBuffer() {
      return false;
   }

   public void setAudioFormat(AudioFormat var1) {
   }

   public void flush() {
   }

   public void close() {
   }

   public void play() {
   }

   public void pause() {
   }

   public void stop() {
   }

   public void rewind() {
   }

   public boolean playing() {
      return false;
   }

   public String getClassName() {
      String var1 = SoundSystemConfig.getLibraryTitle(this.libraryType);
      return var1.equals("No Sound") ? "Channel" : "Channel" + var1;
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
