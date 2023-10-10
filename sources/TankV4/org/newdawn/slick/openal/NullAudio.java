package org.newdawn.slick.openal;

public class NullAudio implements Audio {
   public int getBufferID() {
      return 0;
   }

   public float getPosition() {
      return 0.0F;
   }

   public boolean isPlaying() {
      return false;
   }

   public int playAsMusic(float var1, float var2, boolean var3) {
      return 0;
   }

   public int playAsSoundEffect(float var1, float var2, boolean var3) {
      return 0;
   }

   public int playAsSoundEffect(float var1, float var2, boolean var3, float var4, float var5, float var6) {
      return 0;
   }

   public boolean setPosition(float var1) {
      return false;
   }

   public void stop() {
   }
}
