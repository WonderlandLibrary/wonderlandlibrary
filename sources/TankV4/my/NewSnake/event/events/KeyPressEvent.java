package my.NewSnake.event.events;

import my.NewSnake.event.Event;

public class KeyPressEvent extends Event {
   private int key;

   public void setKey(int var1) {
      this.key = var1;
   }

   public KeyPressEvent(int var1) {
      this.key = var1;
   }

   public int getKey() {
      return this.key;
   }
}
