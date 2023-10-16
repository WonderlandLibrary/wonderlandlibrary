package com.enjoytheban.module.modules.world;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPacketSend;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.TimerUtil;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import optifine.MathUtils;

public class PingSpoof extends Module {

	private List<Packet> packetList = new CopyOnWriteArrayList<Packet>();
	private TimerUtil timer = new TimerUtil();

	public PingSpoof() {
		super("PingSpoof", new String[] { "spoofping", "ping" }, ModuleType.World);
		setColor(new Color(117,52,203).getRGB());
	}

	@EventHandler
	private void onPacketSend(EventPacketSend e) {
		if (e.getPacket() instanceof C00PacketKeepAlive && mc.thePlayer.isEntityAlive()) {
			this.packetList.add(e.getPacket());
			e.setCancelled(true);
		}
		if (this.timer.hasReached(750)) {
			if (!this.packetList.isEmpty()) {
				int i = 0;
				double totalPackets = MathUtils.getIncremental(Math.random() * 10.0, 1.0);
				for (Packet packet : this.packetList) {
					if (i >= totalPackets)
						continue;
					++i;
					mc.getNetHandler().getNetworkManager().sendPacket(packet);
					this.packetList.remove(packet);
				}
			}
			mc.getNetHandler().getNetworkManager().sendPacket(new C00PacketKeepAlive(10000));
			this.timer.reset();
		}
	}
}
