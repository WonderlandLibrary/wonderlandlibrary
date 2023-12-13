package com.sun.media.jfxmedia.events;

public class BufferProgressEvent extends PlayerEvent {
   private double duration;
   private long start;
   private long stop;
   private long position;

   public BufferProgressEvent(double var1, long var3, long var5, long var7) {
      this.duration = var1;
      this.start = var3;
      this.stop = var5;
      this.position = var7;
   }

   public double getDuration() {
      return this.duration;
   }

   public long getBufferStart() {
      return this.start;
   }

   public long getBufferStop() {
      return this.stop;
   }

   public long getBufferPosition() {
      return this.position;
   }
}
