package summer.base.utilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import summer.Summer;
import summer.base.manager.config.Cheats;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventClientTick;
import summer.cheat.eventsystem.events.client.EventPacket;
import summer.cheat.eventsystem.events.player.EventAttack;

public class Manager {

	@EventTarget
	public void onClientTick(EventClientTick ect) {
		for (Cheats m : Summer.INSTANCE.cheatManager.getModuleList()) {
			m.updateSettings();
		}
		mc.updateInfo();
	}
	
	@EventTarget
	public void onPacket(EventPacket ep) {
		if (ep.isSending() && ep.getPacket() instanceof C02PacketUseEntity) {
			C02PacketUseEntity packet = (C02PacketUseEntity) ep.getPacket();
			Entity entity = packet.getEntityFromWorld(mc.theWorld);
			if (packet.getAction() == Action.ATTACK && (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow || entity == mc.thePlayer)) {
				ep.setCancelled(true);
				return;
			} else if (packet.getAction() == Action.ATTACK) {
				EventAttack ea = new EventAttack(entity, true);
				ea.call();
				if (ea.isCancelled()) {
					ep.setCancelled(true);
				}
			}
		}
	}
}
