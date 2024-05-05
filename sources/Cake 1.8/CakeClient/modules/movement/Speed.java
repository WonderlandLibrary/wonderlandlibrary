package CakeClient.modules.movement;

import net.minecraft.entity.player.EntityPlayerMP;

import CakeClient.modules.Module;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.util.MathHelper;

public class Speed extends Module
{
	private double prevY = 0;
	private int i = 0;
    public Speed() {
        super("Speed");
    }
    public String getConfigStatus()
    {
    	return "SpeedMultiplier"+speedMultiplier;
    }
    public void onLeftConfig()  {if(speedMultiplier > 0.1F) {speedMultiplier -= 0.1f; }}
    public void onRightConfig() {speedMultiplier += 0.1f; }
    public static float speedMultiplier = 1F;
    @Override
    public void onDisable() {
    	mc.timer.timerSpeed = 1f;
    }
    
    @Override
    public void onUpdate() {
    	if (this.enabled) {
    	timer();
    	}
    }
   
    
    public void timer() {
    	mc.timer.timerSpeed = speedMultiplier ;
    }

    
    
    



}

