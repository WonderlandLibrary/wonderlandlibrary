package net.caffeinemc.phosphor.api.event.events;

import net.caffeinemc.phosphor.api.event.Cancellable;
import net.minecraft.network.packet.Packet;

@SuppressWarnings("all")
public class PacketEvent extends Cancellable {
	public static class Receive extends PacketEvent {
		private static final Receive INSTANCE = new Receive();

		public Packet packet;

		public static Receive get(Packet packet) {
			INSTANCE.setCancelled(false);
			INSTANCE.packet = packet;
			return INSTANCE;
		}
	}

	public static class Send extends PacketEvent {
		private static final Send INSTANCE = new Send();

		public Packet packet;

		public static Send get(Packet packet) {
			INSTANCE.setCancelled(false);
			INSTANCE.packet = packet;
			return INSTANCE;
		}
	}
}
