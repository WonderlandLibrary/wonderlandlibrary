package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.AttackEvent;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.MoveUtils;

@Module.Mod(displayName = "HitBoost")
public class HitBoost extends Module
{   
	public boolean hitted;
	Minecraft mc = Minecraft.getMinecraft();
	
	public void enable() {
		this.hitted = false;
		super.enable();
	}
	
	public void disable() {
		this.hitted = false;
		super.disable();
	}
	
	@EventTarget
    public void onMove(final MoveEvent event) {
        if (mc.thePlayer.ticksExisted % 2 == 0) {
        	this.hitted = false;
        }
		if (this.hitted) {
			MoveUtils.setMotion(1.5);
		}
	}
	
	@EventTarget
    public void onPacket(final PacketSendEvent event) {
        if (event.getPacket() instanceof C02PacketUseEntity) {
            final C02PacketUseEntity packet = (C02PacketUseEntity)event.getPacket();
            if (packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
            	this.hitted = true;
	}
}
	}
}
