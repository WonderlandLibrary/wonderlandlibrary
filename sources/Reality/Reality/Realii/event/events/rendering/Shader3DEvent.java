
package Reality.Realii.event.events.rendering;

import Reality.Realii.event.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class Shader3DEvent

extends Event {
	 private float partialTicks;

	    public Shader3DEvent(float partialTicks) {
	        this.partialTicks = partialTicks;
	    }
	    
	    public float getPartialTicks() {
	        return this.partialTicks;
	    }

  
}

