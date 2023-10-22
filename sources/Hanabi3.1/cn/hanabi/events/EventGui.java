package cn.hanabi.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.client.gui.GuiScreen;

public class EventGui implements Event {
   public GuiScreen gui;

   public EventGui(GuiScreen gui) {
      this.gui = gui;
   }
}
