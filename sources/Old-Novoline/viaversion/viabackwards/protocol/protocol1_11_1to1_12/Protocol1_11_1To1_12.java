package viaversion.viabackwards.protocol.protocol1_11_1to1_12;

import com.google.gson.JsonElement;
import viaversion.viabackwards.api.BackwardsProtocol;
import viaversion.viabackwards.api.entities.storage.EntityTracker;
import viaversion.viabackwards.protocol.protocol1_11_1to1_12.data.ShoulderTracker;
import viaversion.viabackwards.protocol.protocol1_11_1to1_12.packets.BlockItemPackets1_12;
import viaversion.viabackwards.protocol.protocol1_11_1to1_12.packets.ChatPackets1_12;
import viaversion.viabackwards.protocol.protocol1_11_1to1_12.packets.EntityPackets1_12;
import viaversion.viabackwards.protocol.protocol1_11_1to1_12.packets.SoundPackets1_12;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.remapper.PacketRemapper;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;
import viaversion.viaversion.protocols.protocol1_12to1_11_1.ServerboundPackets1_12;
import viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;

public class Protocol1_11_1To1_12 extends
		BackwardsProtocol<ClientboundPackets1_12, ClientboundPackets1_9_3, ServerboundPackets1_12, ServerboundPackets1_9_3> {

	private EntityPackets1_12 entityPackets;
	private BlockItemPackets1_12 blockItemPackets;

	public Protocol1_11_1To1_12() {
		super(ClientboundPackets1_12.class, ClientboundPackets1_9_3.class, ServerboundPackets1_12.class, ServerboundPackets1_9_3.class);
	}

	@Override
	protected void registerPackets() {
		(entityPackets = new EntityPackets1_12(this)).register();
		(blockItemPackets = new BlockItemPackets1_12(this)).register();
		new SoundPackets1_12(this).register();
		new ChatPackets1_12(this).register();

		registerOutgoing(ClientboundPackets1_12.TITLE, new PacketRemapper() {
			@Override
			public void registerMap() {
				handler(wrapper -> {
					int action = wrapper.passthrough(Type.VAR_INT);
					if(action >= 0 && action <= 2) {
						JsonElement component = wrapper.read(Type.COMPONENT);
						wrapper.write(Type.COMPONENT, Protocol1_9To1_8.fixJson(component.toString()));
					}
				});
			}
		});

		cancelOutgoing(ClientboundPackets1_12.ADVANCEMENTS);
		cancelOutgoing(ClientboundPackets1_12.UNLOCK_RECIPES);
		cancelOutgoing(ClientboundPackets1_12.SELECT_ADVANCEMENTS_TAB);
	}

	@Override
	public void init(UserConnection user) {
		// Register ClientWorld
		if(!user.has(ClientWorld.class)) {
			user.put(new ClientWorld(user));
		}

		// Register EntityTracker if it doesn't exist yet.
		if(!user.has(EntityTracker.class)) {
			user.put(new EntityTracker(user));
		}

		user.put(new ShoulderTracker(user));

		// Init protocol in EntityTracker
		user.get(EntityTracker.class).initProtocol(this);
	}

	public EntityPackets1_12 getEntityPackets() {
		return entityPackets;
	}

	public BlockItemPackets1_12 getBlockItemPackets() {
		return blockItemPackets;
	}
}
