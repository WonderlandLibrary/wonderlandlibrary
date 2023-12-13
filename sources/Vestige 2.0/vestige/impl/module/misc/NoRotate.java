package vestige.impl.module.misc;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import vestige.api.event.Listener;
import vestige.api.event.impl.PacketReceiveEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;

@ModuleInfo(name = "NoRotate", category = Category.MISC)
public class NoRotate extends Module {
	
	@Listener
	public void onReceive(PacketReceiveEvent event) {
		if (event.getPacket() instanceof S08PacketPlayerPosLook) {
			S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
			
			if(mc.thePlayer.ticksExisted > 100) {
				packet.setYaw(mc.thePlayer.rotationYaw);
				packet.setPitch(mc.thePlayer.rotationPitch);
			}
		}
	}

}
