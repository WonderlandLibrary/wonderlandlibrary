
package Method.Client.utils.Patcher.Events;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.Event;

public final class PostMotionEvent extends Event
{
	private final EntityPlayerSP player;
	
	public PostMotionEvent(EntityPlayerSP player)
	{
		this.player = player;
	}
	
	public EntityPlayerSP getPlayer()
	{
		return player;
	}
}
