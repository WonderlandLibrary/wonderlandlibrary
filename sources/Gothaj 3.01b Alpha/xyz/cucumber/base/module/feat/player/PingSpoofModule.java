package xyz.cucumber.base.module.feat.player;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventGameLoop;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventReceivePacket;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.events.ext.EventWorldChange;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.NumberSettings;

@ModuleInfo(category = Category.PLAYER, description = "Automatically spoofs your ping", name = "Ping Spoof")
public class PingSpoofModule extends Mod {
	private ConcurrentHashMap<Packet<?>, Long> outgoing = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Packet<?>, Long> incoming = new ConcurrentHashMap<>();

	public NumberSettings delay = new NumberSettings("Delay", 90, 0, 1000, 10);

	public BooleanSettings keepAlive = new BooleanSettings("Keep alive packet", true);
	public BooleanSettings c0fPacket = new BooleanSettings("C0F packet", true);
	public BooleanSettings allPackets = new BooleanSettings("All packets", true);

	public PingSpoofModule() {
		this.addSettings(delay, keepAlive, c0fPacket, allPackets);
	}

	@EventListener
	public void onSendPacket(EventSendPacket e) {
		if(e.getPacket() instanceof C00PacketKeepAlive && keepAlive.isEnabled()) {
			outgoing.put(e.getPacket(), (long) (System.currentTimeMillis() + delay.getValue()));
			e.setCancelled(true);
		}
	}
	
	@EventListener
	public void onWorldChange(EventWorldChange e) {
		incoming.clear();
		outgoing.clear();
	}
	
	@EventListener
	public void onReceivePacket(EventReceivePacket e) {
		
	}

	@EventListener
	public void onGameLoop(EventGameLoop e) {
		if (mc.isSingleplayer())
			return;
		
		for (Iterator<Map.Entry<Packet<?>, Long>> iterator = incoming.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<Packet<?>, Long> entry = iterator.next();

			if (entry.getValue() < System.currentTimeMillis()) {
				((Packet)entry.getKey()).processPacket(mc.getNetHandler().getNetworkManager().getNetHandler());
				iterator.remove();
			}
		}

		for (Iterator<Map.Entry<Packet<?>, Long>> iterator = outgoing.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<Packet<?>, Long> entry = iterator.next();

			if (entry.getValue() < System.currentTimeMillis()) {
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(entry.getKey());
				iterator.remove();
			}
		}
	}
	
	private boolean blockPacket(Packet packet) {
		if (packet instanceof net.minecraft.network.play.server.S03PacketTimeUpdate)
			return true;
		if (packet instanceof net.minecraft.network.play.server.S00PacketKeepAlive)
			return true;
		if (packet instanceof net.minecraft.network.play.server.S12PacketEntityVelocity)
			return true;
		if (packet instanceof net.minecraft.network.play.server.S27PacketExplosion)
			return true;
		if (packet instanceof net.minecraft.network.play.server.S32PacketConfirmTransaction) {
			return true;
		}
		return false;
	}
}
