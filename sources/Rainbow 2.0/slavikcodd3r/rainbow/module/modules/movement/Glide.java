package slavikcodd3r.rainbow.module.modules.movement;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.FlyUtils;
import slavikcodd3r.rainbow.utils.NetUtil;

@Module.Mod(displayName = "Glide")
public class Glide extends Module
{   
    @Op(name = "Motion", min = -1, max = 1, increment = 0.01)
	private double motion;
    @Op(name = "Ticks", min = 1, max = 10, increment = 1)
	private long ticks;
	@Op(name = "OnGround")
	private boolean onground;
	@Op(name = "NoGround")
	private boolean noground;
	@Op(name = "OnGroundSpoof")
	private boolean ongroundspoof;
	@Op(name = "NoGroundSpoof")
	private boolean nogroundspoof;
	@Op(name = "OnGroundPacket")
	private boolean ongroundpacket;
	@Op(name = "NoGroundPacket")
	private boolean nogroundpacket;
    @Op(name = "Bobbing")
    private boolean bobbing;
    @Op(name = "AntiKick")
    private boolean antikick;
	Minecraft mc = Minecraft.getMinecraft();
	
	public Glide() {
		this.motion = 0;
		this.ticks = 1;
	}
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
    	this.setSuffix(new StringBuilder(String.valueOf(motion + " " + ticks)).toString());
    	if (this.bobbing) {
    		mc.thePlayer.cameraYaw = 0.11f;
    	}
    	if (this.antikick) {
	        if (mc.thePlayer.ticksExisted % 7.0 == 0.0) {
                FlyUtils.fall();
                FlyUtils.ascend();
	        }
    	}
        if (mc.thePlayer.ticksExisted % ticks == 0.0) {
		mc.thePlayer.motionY = motion;
        }
		if (this.onground) {
			mc.thePlayer.onGround = true;
		}
		if (this.noground) {
			mc.thePlayer.onGround = false;
		}
		if (this.ongroundspoof) {
			event.setGround(true);
		}
		if (this.nogroundspoof) {
			event.setGround(false);
		}
		if (this.ongroundpacket) {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
		}
		if (this.nogroundpacket) {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
		}
	}
}
