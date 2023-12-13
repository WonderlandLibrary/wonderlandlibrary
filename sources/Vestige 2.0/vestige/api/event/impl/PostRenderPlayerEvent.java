package vestige.api.event.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.entity.player.EntityPlayer;
import vestige.api.event.Event;

@Data
@AllArgsConstructor
public class PostRenderPlayerEvent extends Event {
	
	private EntityPlayer entity;
	private float partialTicks;
	
}