package com.sun.media.jfxmedia.events;

public class PlayerStateEvent extends PlayerEvent {
   private PlayerState playerState;
   private double playerTime;
   private String message;

   public PlayerStateEvent(PlayerState var1, double var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("state == null!");
      } else if (var2 < 0.0) {
         throw new IllegalArgumentException("time < 0.0!");
      } else {
         this.playerState = var1;
         this.playerTime = var2;
      }
   }

   public PlayerStateEvent(PlayerState var1, double var2, String var4) {
      this(var1, var2);
      this.message = var4;
   }

   public PlayerState getState() {
      return this.playerState;
   }

   public double getTime() {
      return this.playerTime;
   }

   public String getMessage() {
      return this.message;
   }

   public static enum PlayerState {
      UNKNOWN,
      READY,
      PLAYING,
      PAUSED,
      STOPPED,
      STALLED,
      FINISHED,
      HALTED;
   }
}
