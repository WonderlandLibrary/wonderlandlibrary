package team.massacre.api.event;

public class Cancellable {
   private boolean canceled;

   public boolean isCancelled() {
      return this.canceled;
   }

   public void setCancelled(boolean canceled) {
      this.canceled = canceled;
   }

   public void setCancelled() {
      this.setCancelled(true);
   }
}
