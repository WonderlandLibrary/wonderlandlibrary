package markgg.modules.impl.combat;

import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.ModeSetting;
import net.minecraft.block.material.Material;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "Criticals", category = Module.Category.COMBAT)
public class Criticals extends Module {

	public ModeSetting mode = new ModeSetting("Mode", this, "Jump", "Jump", "Packet");

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		doCriticals();
	};

	public void doCriticals() {
		switch(mode.getMode()) {
		case "Jump":
			if(!mc.thePlayer.isInWater() && !mc.thePlayer.isInsideOfMaterial(Material.lava) && mc.thePlayer.onGround) {
				mc.thePlayer.motionY = 0.4f;
				mc.thePlayer.fallDistance = 0.1f;
				mc.thePlayer.onGround = false;
			}
			break;
		case "Packet":
			if(!mc.thePlayer.isInWater() && !mc.thePlayer.isInsideOfMaterial(Material.lava) && mc.thePlayer.onGround) {
				double posX = mc.thePlayer.posX,  posY = mc.thePlayer.posY, posZ = mc.thePlayer.posZ;

				NetHandlerPlayClient sendQueue = mc.thePlayer.sendQueue;

				sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 0.0625D, posZ, true));
				sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, false));
				sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 1.1E-5D, posZ, false));
				sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, false));
			}
			break;
		}
	}
	
}
