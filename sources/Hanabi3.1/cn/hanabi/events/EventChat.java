package cn.hanabi.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.util.IChatComponent;

public class EventChat implements Event {
   public String message;
   public boolean cancelled;
   private IChatComponent ChatComponent;

   public EventChat(String chat, IChatComponent ChatComponent) {
      this.message = chat;
      this.ChatComponent = ChatComponent;
   }

   public IChatComponent getChatComponent() {
      return this.ChatComponent;
   }

   public String getMessage() {
      return this.message;
   }

   public void setCancelled(boolean b) {
      this.cancelled = b;
   }

   public void setChatComponent(IChatComponent ChatComponent) {
      this.ChatComponent = ChatComponent;
   }
}
