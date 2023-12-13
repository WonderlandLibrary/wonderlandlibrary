package my.NewSnake.event;

public class MouseEvent extends Event {
   private int key;

   public void setKey(int var1) {
      this.key = var1;
   }

   public int getKey() {
      return this.key;
   }

   public MouseEvent(int var1) {
      this.key = var1;
   }
}
