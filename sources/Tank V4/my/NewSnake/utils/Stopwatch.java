package my.NewSnake.utils;

public final class Stopwatch {
   private long ms = this.getCurrentMS();

   private long getCurrentMS() {
      return System.currentTimeMillis();
   }

   public final long getElapsedTime() {
      return this.getCurrentMS() - this.ms;
   }

   public final void reset() {
      this.ms = this.getCurrentMS();
   }

   public final boolean elapsed(long var1) {
      return this.getCurrentMS() - this.ms > var1;
   }
}
