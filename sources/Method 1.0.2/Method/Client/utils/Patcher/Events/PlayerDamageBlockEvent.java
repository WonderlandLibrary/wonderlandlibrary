
package Method.Client.utils.Patcher.Events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public final class PlayerDamageBlockEvent extends Event
{
	private final BlockPos pos;
	private final EnumFacing facing;
	
	public PlayerDamageBlockEvent(BlockPos pos, EnumFacing facing)
	{
		this.pos = pos;
		this.facing = facing;
	}
	
	public BlockPos getPos()
	{
		return pos;
	}
	
	public EnumFacing getFacing()
	{
		return facing;
	}
}
