package appu26j.utils;

import com.google.common.eventbus.Subscribe;

import appu26j.Apple;
import appu26j.events.entity.EventTick;
import appu26j.interfaces.MinecraftInterface;

public enum SneakUtil implements MinecraftInterface
{
	INSTANCE;
	
    private float maximumEyeHeight = 1.62F, minimumEyeHeight = 1.54F, previousEyeHeight = this.maximumEyeHeight, eyeHeight = this.maximumEyeHeight;
    
    static
    {
    	Apple.CLIENT.getEventBus().register(INSTANCE);
    }
    
    public float getEyeHeight(float partialTicks)
    {
    	return this.previousEyeHeight + (this.eyeHeight - this.previousEyeHeight) * partialTicks;
    }
    
    @Subscribe
    public void onTick(EventTick e)
    {
    	this.previousEyeHeight = this.eyeHeight;
    	
    	if (this.mc.thePlayer.isSneaking())
    	{
    		this.eyeHeight = this.minimumEyeHeight;
    	}
    	
    	else
    	{
    		this.eyeHeight = this.maximumEyeHeight;
    	}
    }
}
