package paulscode.sound;

public class CommandThread extends SimpleThread {
   protected SoundSystemLogger logger = SoundSystemConfig.getLogger();
   private SoundSystem soundSystem;
   protected String className = "CommandThread";

   public CommandThread(SoundSystem var1) {
      this.soundSystem = var1;
   }

   protected void cleanup() {
      this.kill();
      this.logger = null;
      this.soundSystem = null;
      super.cleanup();
   }

   public void run() {
      long var1 = System.currentTimeMillis();
      if (this.soundSystem == null) {
         this.errorMessage("SoundSystem was null in method run().", 0);
         this.cleanup();
      } else {
         this.snooze(3600000L);

         while(!this.dying()) {
            this.soundSystem.ManageSources();
            this.soundSystem.CommandQueue((CommandObject)null);
            long var3 = System.currentTimeMillis();
            if (!this.dying() && var3 - var1 > 10000L) {
               var1 = var3;
               this.soundSystem.removeTemporarySources();
            }

            if (!this.dying()) {
               this.snooze(3600000L);
            }
         }

         this.cleanup();
      }
   }

   protected void message(String var1, int var2) {
      this.logger.message(var1, var2);
   }

   protected void importantMessage(String var1, int var2) {
      this.logger.importantMessage(var1, var2);
   }

   protected boolean errorCheck(boolean var1, String var2) {
      return this.logger.errorCheck(var1, this.className, var2, 0);
   }

   protected void errorMessage(String var1, int var2) {
      this.logger.errorMessage(this.className, var1, var2);
   }
}
