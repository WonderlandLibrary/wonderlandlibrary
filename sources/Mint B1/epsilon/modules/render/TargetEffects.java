package epsilon.modules.render;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.packet.EventSendPacket;
import epsilon.modules.Module;
import epsilon.modules.combat.KillAura;
import epsilon.settings.setting.ModeSetting;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.EnumParticleTypes;

public class TargetEffects extends Module {
	private Entity t = null;
	
	public ModeSetting mode = new ModeSetting("Mode", "Cloud", "Cloud");
	
	public TargetEffects() {
		super("TargetEffects", Keyboard.KEY_NONE, Category.RENDER, "Magic effects on target");
	}
	
	public void onEvent(Event e) {
		
		if(e instanceof EventSendPacket) {

			if(e.getPacket() instanceof C02PacketUseEntity) {
				C02PacketUseEntity packet = (C02PacketUseEntity) e.getPacket();
				if(packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
					
				}
			}
		}
	}

}
