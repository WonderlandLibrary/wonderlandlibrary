	package markgg.modules.impl.player;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.NumberSetting;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "Phase", category = Module.Category.PLAYER)
public class Phase extends Module {
	
	public NumberSetting timerSpeed = new NumberSetting("Timer", this, 1, 0.05, 10, 0.01);

	@EventHandler
	private final Listener<MotionEvent> eventListener = e -> {
		if(e.getType() == MotionEvent.Type.PRE){
			final double rotation = Math.toRadians(mc.thePlayer.rotationYaw), x = Math.sin(rotation), z = Math.cos(rotation);
			
			if(mc.thePlayer.isCollidedHorizontally) {
				mc.timer.timerSpeed = (float) timerSpeed.getValue();
				mc.thePlayer.setPosition(mc.thePlayer.posX - x * 0.005, mc.thePlayer.posY, mc.thePlayer.posZ + z * 0.005);
			}
			
			if(mc.thePlayer.isEntityInsideOpaqueBlock()) {
				mc.timer.timerSpeed = (float) timerSpeed.getValue();
				mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX - x * 1.5, mc.thePlayer.posY, mc.thePlayer.posZ + z * 1.5, false));
			}
			
			if(!mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isEntityInsideOpaqueBlock())
				mc.timer.timerSpeed = 1;
			
		}
	};
	
	public void onDisable() {
		mc.gameSettings.keyBindUseItem.pressed = false;
        mc.timer.timerSpeed = 1;
	}

}
