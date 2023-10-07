package viaversion.viabackwards.api.rewriters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import viaversion.viabackwards.ViaBackwards;
import viaversion.viabackwards.api.BackwardsProtocol;
import viaversion.viabackwards.api.data.VBMappingDataLoader;
import viaversion.viaversion.api.protocol.ClientboundPacketType;
import viaversion.viaversion.api.remapper.PacketRemapper;
import viaversion.viaversion.api.rewriters.ComponentRewriter;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.packets.State;

public class TranslatableRewriter extends ComponentRewriter {

	private static final Map<String, Map<String, String>> TRANSLATABLES = new HashMap<>();
	protected final Map<String, String> newTranslatables;

	public static void loadTranslatables() {
		JsonObject jsonObject = VBMappingDataLoader.loadData("translation-mappings.json");

		for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			Map<String, String> versionMappings = new HashMap<>();
			TRANSLATABLES.put(entry.getKey(), versionMappings);

			for(Map.Entry<String, JsonElement> translationEntry : entry.getValue().getAsJsonObject().entrySet()) {
				versionMappings.put(translationEntry.getKey(), translationEntry.getValue().getAsString());
			}
		}
	}

	public TranslatableRewriter(BackwardsProtocol protocol, String sectionIdentifier) {
		super(protocol);

		Map<String, String> newTranslatables = TRANSLATABLES.get(sectionIdentifier);

		if(newTranslatables == null) {
			ViaBackwards.getPlatform().getLogger().warning("Error loading " + sectionIdentifier + " translatables!");
			this.newTranslatables = new HashMap<>();
		} else {
			this.newTranslatables = newTranslatables;
		}
	}

	public void registerPing() {
		protocol.registerOutgoing(State.LOGIN, 0x00, 0x00, new PacketRemapper() {
			@Override
			public void registerMap() {
				handler(wrapper -> processText(wrapper.passthrough(Type.COMPONENT)));
			}
		});
	}

	public void registerDisconnect(ClientboundPacketType packetType) {
		protocol.registerOutgoing(packetType, new PacketRemapper() {
			@Override
			public void registerMap() {
				handler(wrapper -> processText(wrapper.passthrough(Type.COMPONENT)));
			}
		});
	}

	@Override
	public void registerChatMessage(ClientboundPacketType packetType) {
		protocol.registerOutgoing(packetType, new PacketRemapper() {
			@Override
			public void registerMap() {
				handler(wrapper -> processText(wrapper.passthrough(Type.COMPONENT)));
			}
		});
	}

	public void registerLegacyOpenWindow(ClientboundPacketType packetType) {
		protocol.registerOutgoing(packetType, new PacketRemapper() {
			@Override
			public void registerMap() {
				map(Type.UNSIGNED_BYTE); // Id
				map(Type.STRING); // Window Type
				handler(wrapper -> processText(wrapper.passthrough(Type.COMPONENT)));
			}
		});
	}

	public void registerOpenWindow(ClientboundPacketType packetType) {
		protocol.registerOutgoing(packetType, new PacketRemapper() {
			@Override
			public void registerMap() {
				map(Type.VAR_INT); // Id
				map(Type.VAR_INT); // Window Type
				handler(wrapper -> processText(wrapper.passthrough(Type.COMPONENT)));
			}
		});
	}

	public void registerTabList(ClientboundPacketType packetType) {
		protocol.registerOutgoing(packetType, new PacketRemapper() {
			@Override
			public void registerMap() {
				handler(wrapper -> {
					processText(wrapper.passthrough(Type.COMPONENT));
					processText(wrapper.passthrough(Type.COMPONENT));
				});
			}
		});
	}

	@Override
	protected void handleTranslate(JsonObject root, String translate) {
		String newTranslate = newTranslatables.get(translate);
		if(newTranslate != null) {
			root.addProperty("translate", newTranslate);
		}
	}
}
