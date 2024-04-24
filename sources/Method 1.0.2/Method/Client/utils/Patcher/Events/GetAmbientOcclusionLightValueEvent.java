
package Method.Client.utils.Patcher.Events;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.eventhandler.Event;

public final class GetAmbientOcclusionLightValueEvent extends Event
{
	private final IBlockState state;
	private float lightValue;
	private final float defaultLightValue;
	
	public GetAmbientOcclusionLightValueEvent(IBlockState state,
		float lightValue)
	{
		this.state = state;
		this.lightValue = lightValue;
		defaultLightValue = lightValue;
	}

	public IBlockState getState()
	{
		return state;
	}
	
	public float getLightValue()
	{
		return lightValue;
	}
	
	public void setLightValue(float lightValue)
	{
		this.lightValue = lightValue;
	}
	
	public float getDefaultLightValue()
	{
		return defaultLightValue;
	}
}
