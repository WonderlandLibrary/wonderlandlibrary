
package Method.Client.utils.Patcher.Events;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public final class RenderTileEntityEvent extends Event
{
	private final TileEntity tileEntity;
	
	public RenderTileEntityEvent(TileEntity tileEntity)
	{
		this.tileEntity = tileEntity;
	}
	
	public TileEntity getTileEntity()
	{
		return tileEntity;
	}
}
