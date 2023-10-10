package paulscode.sound;

public class SimpleThread extends Thread {
   private static final boolean GET = false;
   private static final boolean SET = true;
   private static final boolean XXX = false;
   private boolean alive = true;
   private boolean kill = false;

   protected void cleanup() {
      this.kill(true, true);
      this.alive(true, false);
   }

   public void run() {
      this.cleanup();
   }

   public void restart() {
      (new Thread(this) {
         final SimpleThread this$0;

         {
            this.this$0 = var1;
         }

         public void run() {
            SimpleThread.access$000(this.this$0);
         }
      }).start();
   }

   private void rerun() {
      this.kill(true, true);

      while(true) {
         this.snooze(100L);
      }
   }

   public boolean alive() {
      return this.alive(false, false);
   }

   public void kill() {
      this.kill(true, true);
   }

   protected boolean dying() {
      return this.kill(false, false);
   }

   private synchronized boolean kill(boolean var1, boolean var2) {
      if (var1) {
         this.kill = var2;
      }

      return this.kill;
   }

   protected void snooze(long var1) {
      try {
         Thread.sleep(var1);
      } catch (InterruptedException var4) {
      }

   }

   static void access$000(SimpleThread var0) {
      var0.rerun();
   }
}
