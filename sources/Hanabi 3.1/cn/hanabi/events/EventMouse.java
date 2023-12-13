package cn.hanabi.events;

import com.darkmagician6.eventapi.events.Event;

public class EventMouse implements Event {
   private final Button button;

   public EventMouse(Button button) {
      this.button = button;
   }

   public Button getButton() {
      return this.button;
   }

   public static enum Button {
      Left,
      Right,
      Middle;
   }
}
