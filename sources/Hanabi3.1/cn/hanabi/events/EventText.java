package cn.hanabi.events;

import com.darkmagician6.eventapi.events.Event;

public class EventText implements Event {
   String string;

   public EventText(String string) {
      this.string = string;
   }

   public String getText() {
      return this.string;
   }

   public void setText(String pass) {
      this.string = pass;
   }
}
