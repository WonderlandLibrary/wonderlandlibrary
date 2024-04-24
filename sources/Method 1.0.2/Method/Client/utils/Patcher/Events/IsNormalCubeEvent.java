
package Method.Client.utils.Patcher.Events;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public final class IsNormalCubeEvent extends Event
{
	private final IBlockState state;
	
	public IsNormalCubeEvent(IBlockState state)
	{
		this.state = state;
	}
	
	public IBlockState getBlockState()
	{
		return state;
	}
}
