
package Method.Client.utils.Patcher.Events;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.Event;

public final class PlayerMoveEvent extends Event
{
	private final EntityPlayerSP player;
	
	public PlayerMoveEvent(EntityPlayerSP player)
	{
		this.player = player;
	}
	
	public EntityPlayerSP getPlayer()
	{
		return player;
	}
}
