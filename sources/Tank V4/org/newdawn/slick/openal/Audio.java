package org.newdawn.slick.openal;

public interface Audio {
   void stop();

   int getBufferID();

   boolean isPlaying();

   int playAsSoundEffect(float var1, float var2, boolean var3);

   int playAsSoundEffect(float var1, float var2, boolean var3, float var4, float var5, float var6);

   int playAsMusic(float var1, float var2, boolean var3);

   boolean setPosition(float var1);

   float getPosition();
}
