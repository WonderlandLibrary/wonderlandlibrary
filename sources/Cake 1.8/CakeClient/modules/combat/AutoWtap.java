package CakeClient.modules.combat;

import CakeClient.modules.Module;
import net.minecraft.network.Packet;


public class AutoWtap extends Module
{
	
    public AutoWtap() {
		super("AutoWtap");
	}
	private Float coolDown = 0f;
    
    @Override
    public void onUpdate() {
    	coolDown -= 1f;
    	mc.thePlayer.setSprinting(true);
        if (mc.thePlayer.isSwingInProgress)
        	if (coolDown < 0) {
        		mc.thePlayer.setSprinting(false);
        		coolDown = 3f;
        		
        	}
    }
    
}


