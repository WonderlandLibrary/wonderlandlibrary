package viaversion.viarewind.protocol.protocol1_8to1_9.storage;

import viaversion.viarewind.utils.Tickable;
import viaversion.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import viaversion.viarewind.utils.PacketUtil;
import viaversion.viaversion.api.PacketWrapper;
import viaversion.viaversion.api.data.StoredObject;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.type.Type;

public class Levitation extends StoredObject implements Tickable {
	private int amplifier;
	private volatile boolean active = false;

	public Levitation(UserConnection user) {
		super(user);
	}

	@Override
	public void tick() {
		if (!active) {
			return;
		}

		int vY = (amplifier+1) * 360;
		PacketWrapper packet = new PacketWrapper(0x12, null, Levitation.this.getUser());
		packet.write(Type.VAR_INT, getUser().get(EntityTracker.class).getPlayerId());
		packet.write(Type.SHORT, (short)0);
		packet.write(Type.SHORT, (short)vY);
		packet.write(Type.SHORT, (short)0);
		PacketUtil.sendPacket(packet, Protocol1_8TO1_9.class);
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setAmplifier(int amplifier) {
		this.amplifier = amplifier;
	}
}
