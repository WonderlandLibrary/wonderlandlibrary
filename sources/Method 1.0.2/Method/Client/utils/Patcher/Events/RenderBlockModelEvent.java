
package Method.Client.utils.Patcher.Events;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public final class RenderBlockModelEvent extends Event
{
	private final IBlockState state;
	
	public RenderBlockModelEvent(IBlockState state)
	{
		this.state = state;
	}
	
	public IBlockState getState()
	{
		return state;
	}
}
