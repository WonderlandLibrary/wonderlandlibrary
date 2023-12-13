package com.sun.media.jfxmedia.events;

public interface PlayerStateListener {
   void onReady(PlayerStateEvent var1);

   void onPlaying(PlayerStateEvent var1);

   void onPause(PlayerStateEvent var1);

   void onStop(PlayerStateEvent var1);

   void onStall(PlayerStateEvent var1);

   void onFinish(PlayerStateEvent var1);

   void onHalt(PlayerStateEvent var1);
}
