package summer.cheat.eventsystem.events.render;

import net.minecraft.client.gui.ScaledResolution;
import summer.cheat.eventsystem.Event;

public class EventRenderGui extends Event {
	   private static ScaledResolution resolution;

	   public void fire(ScaledResolution resolution) {
	      this.resolution = resolution;
	      super.call();
	   }

	   public static ScaledResolution getResolution() {
	      return resolution;
	   }
	}
