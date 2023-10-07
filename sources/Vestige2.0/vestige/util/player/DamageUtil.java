package vestige.util.player;

import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import vestige.util.base.IMinecraft;
import vestige.util.movement.MovementUtils;
import vestige.util.network.PacketUtil;

public class DamageUtil implements IMinecraft {
	
	public static void vanillaDamage() {
		PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
		PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
		PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
	}
	
	public static void ncpDamage() {
		for(int i = 0; i < 48; i++) {
			PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, false));
			PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
		}
		PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
	}
	
	public static void verusDamage() {
		double y = mc.thePlayer.posY;
		double motionY = 0;
		
		while(y <= mc.thePlayer.posY + 3.01) {
			if(motionY == MovementUtils.JUMP_MOTION) {
				motionY = 0.33319999363422365;
			} else {
				motionY = MovementUtils.JUMP_MOTION;
			}
			
			y += motionY;
			
			PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + y, mc.thePlayer.posZ, false));
			PacketUtil.verusRightClick();
		}
		
		PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
		PacketUtil.verusRightClick();
		PacketUtil.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
		PacketUtil.verusRightClick();
	}
	
}
