package paulscode.sound;

import javax.sound.sampled.AudioFormat;

public class SoundBuffer {
   public byte[] audioData;
   public AudioFormat audioFormat;

   public SoundBuffer(byte[] var1, AudioFormat var2) {
      this.audioData = var1;
      this.audioFormat = var2;
   }

   public void cleanup() {
      this.audioData = null;
      this.audioFormat = null;
   }

   public void trimData(int var1) {
      if (this.audioData != null && var1 != 0) {
         if (this.audioData.length > var1) {
            byte[] var2 = new byte[var1];
            System.arraycopy(this.audioData, 0, var2, 0, var1);
            this.audioData = var2;
         }
      } else {
         this.audioData = null;
      }

   }
}
