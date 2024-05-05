package net.minecraft.network;

import me.aidanmees.trivia.client.main.trivia;

public abstract class AbstractPacket<T extends INetHandler> implements Packet<T> {

	protected boolean cancelled = false;

	public boolean crit = false;

	@Override
	public void processPacket(T handler) {
		trivia.onPacketRecieved(this);
	}

	public void cancel() {
		this.cancelled = true;
	}

	public AbstractPacket setCrit(boolean crit) {
		this.crit = crit;
		return this;
	}
}
